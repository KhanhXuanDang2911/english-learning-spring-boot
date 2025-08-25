package elearningspringboot.controller;

import elearningspringboot.dto.request.RoleHasPermissionRequest;
import elearningspringboot.dto.response.ResponseData;
import elearningspringboot.dto.response.RoleHasPermissionResponse;
import elearningspringboot.service.RoleHasPermissionService;
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
@RequestMapping(value = "/api/v1/role-permissions")
public class RoleHasPermissionController {

    private final RoleHasPermissionService roleHasPermissionService;

    @GetMapping
    public ResponseEntity<ResponseData<List<RoleHasPermissionResponse>>> getAll() {
        log.info("Request: Get all role-permission records");
        List<RoleHasPermissionResponse> response = roleHasPermissionService.getAll();
        log.info("Response: {} role-permission records found", response.size());
        return ResponseBuilder.withData(HttpStatus.OK, "Get list role-permissions successfully", response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<RoleHasPermissionResponse>> getById(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 0") Long id) {
        log.info("Request: Get role-permission by id = {}", id);
        RoleHasPermissionResponse response = roleHasPermissionService.getById(id);
        log.info("Response: Found role-permission = {}", response);
        return ResponseBuilder.withData(HttpStatus.OK, "Get role-permission successfully", response);
    }

    @PostMapping
    public ResponseEntity<ResponseData<RoleHasPermissionResponse>> create(
            @RequestBody @Validated RoleHasPermissionRequest request) {
        log.info("Request: Create role-permission with data = {}", request);
        RoleHasPermissionResponse response = roleHasPermissionService.create(request);
        log.info("Response: Role-permission created = {}", response);
        return ResponseBuilder.withData(HttpStatus.CREATED, "Role-permission created successfully", response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<RoleHasPermissionResponse>> update(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 0") Long id,
            @RequestBody @Validated RoleHasPermissionRequest request) {
        log.info("Request: Update role-permission with id = {}, data = {}", id, request);
        RoleHasPermissionResponse response = roleHasPermissionService.update(id, request);
        log.info("Response: Role-permission updated = {}", response);
        return ResponseBuilder.withData(HttpStatus.OK, "Role-permission updated successfully", response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> delete(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 0") Long id) {
        log.info("Request: Delete role-permission with id = {}", id);
        roleHasPermissionService.delete(id);
        log.info("Response: Role-permission deleted with id = {}", id);
        return ResponseBuilder.noData(HttpStatus.OK, "Role-permission deleted successfully");
    }
}
