package elearningspringboot.controller;

import elearningspringboot.dto.request.AdminUserRequest;
import elearningspringboot.dto.request.UserRequest;
import elearningspringboot.dto.response.PageResponse;
import elearningspringboot.dto.response.ResponseData;
import elearningspringboot.dto.response.UserResponse;
import elearningspringboot.service.UserService;
import elearningspringboot.util.ResponseBuilder;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<UserResponse>> getUserById(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 0") Long id) {
        log.info("Request: Get user by ID = {}", id);
        UserResponse response = userService.getUserById(id);
        log.info("Response: Found user = {}", response);
        return ResponseBuilder.withData(HttpStatus.OK, "Get user successfully", response);
    }

    @PostMapping
    public ResponseEntity<ResponseData<UserResponse>> createUser(@RequestBody @Validated AdminUserRequest request) {
        log.info("Request: Admin create user with data = {}", request);
        UserResponse response = userService.createUser(request);
        log.info("Response: User created = {}", response);
        return ResponseBuilder.withData(HttpStatus.CREATED, "User created successfully", response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<UserResponse>> updateUser(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 0") Long id,
            @RequestBody @Validated AdminUserRequest request) {
        log.info("Request: Update user with ID = {}, data = {}", id, request);
        UserResponse response = userService.updateUser(id, request);
        log.info("Response: User updated = {}", response);
        return ResponseBuilder.withData(HttpStatus.OK, "User updated successfully", response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteUser(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 0") Long id) {
        log.info("Request: Delete user with ID = {}", id);
        userService.deleteUser(id);
        log.info("Response: User deleted with ID = {}", id);
        return ResponseBuilder.noData(HttpStatus.OK, "User deleted successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseData<UserResponse>> registerUser(@RequestBody @Validated UserRequest request) {
        log.info("Request: User register with data = {}", request);
        UserResponse response = userService.registerUser(request);
        log.info("Response: User registered = {}", response);
        return ResponseBuilder.withData(HttpStatus.CREATED, "User registered successfully", response);
    }

    @PutMapping("/me")
    public ResponseEntity<ResponseData<UserResponse>> updateProfile(
            @RequestParam @Min(value = 1, message = "Id must be greater than 0") Long id,
            @RequestBody @Validated UserRequest request) {
        log.info("Request: Update profile for user ID = {}, data = {}", id, request);
        UserResponse response = userService.updateProfile(id, request);
        log.info("Response: Profile updated = {}", response);
        return ResponseBuilder.withData(HttpStatus.OK, "Profile updated successfully", response);
    }

}
