package elearningspringboot.service.impl;

import elearningspringboot.dto.request.AdminUserRequest;
import elearningspringboot.dto.request.UserRequest;
import elearningspringboot.dto.response.PageResponse;
import elearningspringboot.dto.response.UserResponse;
import elearningspringboot.entity.Role;
import elearningspringboot.entity.User;
import elearningspringboot.enumeration.Gender;
import elearningspringboot.enumeration.Status;
import elearningspringboot.exception.ResourceConflictException;
import elearningspringboot.exception.ResourceNotFoundException;
import elearningspringboot.mapper.UserMapper;
import elearningspringboot.repository.UserRepository;
import elearningspringboot.service.AzureBlobService;
import elearningspringboot.service.RoleService;
import elearningspringboot.service.UserService;
import elearningspringboot.util.AppUtils;
import elearningspringboot.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AzureBlobService azureBlobService;

    @Override
    public UserResponse createUser(MultipartFile avatar, AdminUserRequest request) {
                log.info("Admin creating new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("Cannot create user. Email '{}' already exists", request.getEmail());
            throw new ResourceConflictException("Email already exists");
        }

        User user = userMapper.fromAdminUserRequestToEntity(request);
        Role role = roleService.findRoleByRoleName(request.getRole());
        user.setGender(Gender.getGenderFromName(request.getGender()));
        user.setRole(role);
        user.setStatus(Status.getStatusFromName(request.getStatus()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNoPassword(false);
        if (avatar != null && !avatar.isEmpty()) {
            log.info("Uploading avatar");
            String avatarUrl = azureBlobService.uploadFile(avatar);
            user.setAvatarUrl(avatarUrl);
            log.info("Upload avatar successfully");
        }
        userRepository.save(user);
        log.info("User created successfully with ID: {}", user.getId());
        UserResponse userResponse =userMapper.toDTO(user);
        userResponse.setRole(user.getRole().getRole());
        return userResponse;
    }

    @Override
    public UserResponse registerUser(UserRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("Cannot register user. Email '{}' already exists", request.getEmail());
            throw new ResourceConflictException("Email already exists: " + request.getEmail());
        }

        User user = userMapper.fromUserRequestToEntity(request);
        Role role = roleService.findRoleByRoleName("USER");
        user.setRole(role);
        user.setStatus(Status.PENDING);
        user.setGender(Gender.getGenderFromName(request.getGender()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNoPassword(false);

        userRepository.save(user);
        log.info("User registered successfully with ID: {}", user.getId());
        UserResponse userResponse =userMapper.toDTO(user);
        userResponse.setRole(user.getRole().getRole());
        return userResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);

        User user = findUserById(id);
        log.info("Found user with ID: {}", id);

        UserResponse userResponse = userMapper.toDTO(user);
        userResponse.setRole(user.getRole().getRole());
        return userResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<List<UserResponse>> getUsersWithPaginationAndKeyword(int pageNumber, int pageSize, List<String> sorts, String keyword) {
        log.info("Fetching users with pagination: pageNumber={}, pageSize={}", pageNumber, pageSize);

        List<String> whiteListFieldSorts = List.of("fullName", "email", "role", "status", "createdAt", "updatedAt");
        Page<User> userPage = userRepository.searchUsers(keyword.toLowerCase(),
                AppUtils.generatePageableWithSort(sorts, whiteListFieldSorts, pageNumber, pageSize)
        );

        List<UserResponse> userResponses = userPage.getContent()
                .stream()
                .map(user -> {
                    UserResponse userResponse = userMapper.toDTO(user);
                    userResponse.setRole(user.getRole().getRole());
                    return userResponse;
                })
                .toList();

        log.info("Fetched {} users (page {}/{})",
                userPage.getNumberOfElements(), pageNumber, userPage.getTotalPages());

        return PageResponse.<List<UserResponse>>builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalPages(userPage.getTotalPages())
                .numberOfElements(userPage.getNumberOfElements())
                .items(userResponses)
                .build();
    }

    @Override
    public UserResponse updateUser(Long id, MultipartFile avatar, AdminUserRequest request) {
        log.info("Updating user with ID: {}", id);

        User user = findUserById(id);
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            log.error("Cannot update user ID {}. Email '{}' already exists", id, request.getEmail());
            throw new ResourceConflictException("Email already exists: " + request.getEmail());
        }

        userMapper.updateEntityFromAdminUserDTO(request, user);
        Role role = roleService.findRoleByRoleName(request.getRole());
        user.setRole(role);
        user.setGender(Gender.getGenderFromName(request.getGender()));
        user.setStatus(Status.getStatusFromName(request.getStatus()));


        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            log.debug("Password updated for user ID: {}", id);
        }

        if (avatar != null && !avatar.isEmpty()) {
            log.info("Uploading new avatar for user ID: {}", id);
            String avatarUrl = azureBlobService.uploadFile(avatar);
            user.setAvatarUrl(avatarUrl);
            log.info("Upload new avatar for user ID: {} successfully", id);
        }

        userRepository.save(user);
        log.info("User updated successfully with ID: {}", user.getId());
        UserResponse userResponse = userMapper.toDTO(user);
        userResponse.setRole(user.getRole().getRole());
        return userResponse;
    }

    @Override
    public UserResponse updateProfile(Long id, UserRequest request) {
        log.info("Updating profile for user ID: {}", id);
        User user = findUserById(id);
        userMapper.updateEntityFromUserDTO(request, user);

        userRepository.save(user);

        log.info("Profile updated successfully for user ID: {}", user.getId());
        UserResponse userResponse = userMapper.toDTO(user);
        userResponse.setRole(user.getRole().getRole());
        return userResponse;
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);

        User user = findUserById(id);
        userRepository.delete(user);

        log.info("User deleted successfully with ID: {}", id);
    }


    public User findUserByEmail(String email) {
        log.debug("Looking up user by email {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User with email {} not found", email);
                    return new ResourceNotFoundException(
                            String.format("User with email = %s not found", email)
                    );
                });
    }

    private User findUserById(Long id) {
        log.debug("Looking up user by ID: {}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", id);
                    return new ResourceNotFoundException(
                            String.format("User with id = %s not found", id)
                    );
                });
    }
}
