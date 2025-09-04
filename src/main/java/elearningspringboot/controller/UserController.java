package elearningspringboot.controller;

import elearningspringboot.dto.request.AdminUserRequest;
import elearningspringboot.dto.request.UserRequest;
import elearningspringboot.dto.response.PageResponse;
import elearningspringboot.dto.response.ResponseData;
import elearningspringboot.dto.response.UserResponse;
import elearningspringboot.service.UserService;
import elearningspringboot.util.ResponseBuilder;
import elearningspringboot.validation.OnCreate;
import elearningspringboot.validation.OnUpdate;
import elearningspringboot.validation.ValidImageFile;
import jakarta.validation.constraints.Min;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static elearningspringboot.util.AppUtils.getUserIdFromSecurityContext;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ResponseData<PageResponse<List<UserResponse>>>> getAllUsers(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page number must be >= 1") int pageNumber,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size must be >= 1") int pageSize,
            @RequestParam(required = false) List<String> sorts,
            @RequestParam(defaultValue = "") String keyword
    ) {
        log.info("Request: Get users with pageNumber={}, pageSize={}, sorts={}, keyword={}", pageNumber, pageSize, sorts, keyword);
        PageResponse<List<UserResponse>> response = userService.getUsersWithPaginationAndKeyword(pageNumber, pageSize, sorts, keyword);
        log.info("Response: {} users fetched (page {}/{})", response.getNumberOfElements(), response.getPageNumber(), response.getTotalPages());
        return ResponseBuilder.withData(HttpStatus.OK, "Get list users successfully", response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<UserResponse>> getUserById(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 0") Long id) {
        log.info("Request: Get user by ID = {}", id);
        UserResponse response = userService.getUserById(id);
        log.info("Response: Found user = {}", response);
        return ResponseBuilder.withData(HttpStatus.OK, "Get user successfully", response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseData<UserResponse>> createUser(@ValidImageFile @RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                                                 @RequestPart("user") @Validated({OnCreate.class, Default.class}) AdminUserRequest request) {
        log.info("Request: Admin create user with data = {}", request);
        UserResponse response = userService.createUser(avatar, request);
        log.info("Response: User created = {}", response);
        return ResponseBuilder.withData(HttpStatus.CREATED, "User created successfully", response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseData<UserResponse>> updateUser(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 0") Long id,
            @ValidImageFile @RequestPart(value = "avatar", required = false) MultipartFile avatar, @RequestPart("user") @Validated({OnUpdate.class, Default.class}) AdminUserRequest request) {
        log.info("Request: Update user with ID = {}, data = {}", id, request);
        UserResponse response = userService.updateUser(id, avatar, request);
        log.info("Response: User updated = {}", response);
        return ResponseBuilder.withData(HttpStatus.OK, "User updated successfully", response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteUser(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 0") Long id) {
        log.info("Request: Delete user with ID = {}", id);
        userService.deleteUser(id);
        log.info("Response: User deleted with ID = {}", id);
        return ResponseBuilder.noData(HttpStatus.OK, "User deleted successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseData<UserResponse>> getProfile() {
        Long userId = getUserIdFromSecurityContext();
        UserResponse response = userService.getUserById(userId);
        return ResponseBuilder.withData(HttpStatus.OK, "Get profile successfully", response);
    }

    @PutMapping("/me")
    public ResponseEntity<ResponseData<UserResponse>> updateProfile(
            @RequestBody @Validated({OnUpdate.class, Default.class}) UserRequest request) {
        Long userId = getUserIdFromSecurityContext();

        log.info("Request: Update profile for user ID = {}, data = {}", userId, request);
        UserResponse response = userService.updateProfile(userId, request);
        log.info("Response: Profile updated = {}", response);

        return ResponseBuilder.withData(HttpStatus.OK, "Profile updated successfully", response);
    }
    @PatchMapping(value = "/me/avatar",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseData<UserResponse>> updateAvatar(@ValidImageFile @RequestParam("avatar") MultipartFile avatar) {
        Long userId = getUserIdFromSecurityContext();
        log.info("Request: Update avatar for user ID = {}", userId);
        UserResponse response = userService.updateAvatar(userId, avatar);
        log.info("Response: Avatar updated");

        return ResponseBuilder.withData(HttpStatus.OK, "Avatar user updated successfully", response);
    }

}
