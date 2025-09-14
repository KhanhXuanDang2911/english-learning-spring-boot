package elearningspringboot.service;

import elearningspringboot.dto.request.AdminUserRequest;
import elearningspringboot.dto.request.UserCreationPassword;
import elearningspringboot.dto.request.UserRequest;
import elearningspringboot.dto.response.PageResponse;
import elearningspringboot.dto.response.UserResponse;
import elearningspringboot.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {

    UserResponse createUser(MultipartFile avatar, AdminUserRequest request);

    UserResponse registerUser(UserRequest request) throws MessagingException, UnsupportedEncodingException;

    UserResponse getUserById(Long id);

    User findUserByEmail(String email);

    PageResponse<List<UserResponse>> getUsersWithPaginationAndKeyword(int pageNumber, int pageSize, List<String> sorts, String keyword);

    UserResponse updateUser(Long id, MultipartFile avatar, AdminUserRequest request);

    UserResponse updateProfile(Long id, UserRequest request);

    UserResponse updateAvatar(Long id, MultipartFile avatar);

    Boolean isNoPassword(String email);

    void verifyEmail(String token);

    void createPassword(UserCreationPassword request);

    void deleteUser(Long id);
}
