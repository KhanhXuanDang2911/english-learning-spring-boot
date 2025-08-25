package elearningspringboot.controller;

import elearningspringboot.dto.request.PermissionRequest;
import elearningspringboot.dto.response.PageResponse;
import elearningspringboot.dto.response.PermissionResponse;
import elearningspringboot.dto.response.ResponseData;
import elearningspringboot.service.PermissionService;
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
@RequestMapping(value = "/api/v1/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<ResponseData<PageResponse<List<PermissionResponse>>>> getAll(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page number must be >= 1") int pageNumber,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size must be >= 1") int pageSize,
            @RequestParam(required = false) List<String> sorts
    ) {
        log.info("Request: Get all permissions with pageNumber={}, pageSize={}, sorts={}", pageNumber, pageSize, sorts);
        PageResponse<List<PermissionResponse>> response = permissionService.getPermissions(pageNumber, pageSize, sorts);
        log.info("Response: {} permissions found on page {}", response.getNumberOfElements(), response.getPageNumber());
        return ResponseBuilder.withData(HttpStatus.OK, "Get list permissions successfully", response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<PermissionResponse>> getById(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 0") Long id) {
        log.info("Request: Get permission by id = {}", id);
        PermissionResponse response = permissionService.getPermissionById(id);
        log.info("Response: Found permission = {}", response);
        return ResponseBuilder.withData(HttpStatus.OK, "Get permission successfully", response);
    }

    @PostMapping
    public ResponseEntity<ResponseData<PermissionResponse>> create(@RequestBody @Validated PermissionRequest request) {
        log.info("Request: Create permission with data = {}", request);
        PermissionResponse response = permissionService.createPermission(request);
        log.info("Response: Permission created = {}", response);
        return ResponseBuilder.withData(HttpStatus.CREATED, "Permission created successfully", response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<PermissionResponse>> update(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 0") Long id,
            @RequestBody @Validated PermissionRequest request) {
        log.info("Request: Update permission with id = {}, data = {}", id, request);
        PermissionResponse response = permissionService.updatePermission(id, request);
        log.info("Response: Permission updated = {}", response);
        return ResponseBuilder.withData(HttpStatus.OK, "Permission updated successfully", response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> delete(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 1") Long id) {
        log.info("Request: Delete permission with id = {}", id);
        permissionService.deletePermission(id);
        log.info("Response: Permission deleted with id = {}", id);
        return ResponseBuilder.noData(HttpStatus.OK, "Permission deleted successfully");
    }
}
