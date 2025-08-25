package elearningspringboot.service;

import elearningspringboot.dto.request.AdminUserRequest;
import elearningspringboot.dto.request.UserRequest;
import elearningspringboot.dto.response.PageResponse;
import elearningspringboot.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(AdminUserRequest request);

    UserResponse registerUser(UserRequest request);

    UserResponse getUserById(Long id);

    PageResponse<List<UserResponse>> getUsersWithPaginationAndKeyword(int pageNumber, int pageSize, List<String> sorts, String keyword);

    UserResponse updateUser(Long id, AdminUserRequest request);

    UserResponse updateProfile(Long id, UserRequest request);

    void deleteUser(Long id);
}
