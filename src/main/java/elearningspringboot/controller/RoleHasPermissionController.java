package elearningspringboot.controller;

import elearningspringboot.dto.request.RoleHasPermissionRequest;
import elearningspringboot.dto.response.ResponseData;
import elearningspringboot.dto.response.RoleHasPermissionResponse;
import elearningspringboot.service.RoleHasPermissionService;
import elearningspringboot.util.ResponseBuilder;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<ResponseData<List<RoleHasPermissionResponse>>> getAll() {
        log.info("Request: Get all role-permission records");
        List<RoleHasPermissionResponse> response = roleHasPermissionService.getAll();
        log.info("Response: {} role-permission records found", response.size());
        String message = messageSource.getMessage("role.permission.get.list.success", null, LocaleContextHolder.getLocale());
        return ResponseBuilder.withData(HttpStatus.OK, message, response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<RoleHasPermissionResponse>> getById(
            @PathVariable("id") @Min(value = 1, message = "{validation.id.min}") Long id) {
        log.info("Request: Get role-permission by id = {}", id);
        RoleHasPermissionResponse response = roleHasPermissionService.getById(id);
        log.info("Response: Found role-permission = {}", response);
        String message = messageSource.getMessage("role.permission.get.success", null, LocaleContextHolder.getLocale());
        return ResponseBuilder.withData(HttpStatus.OK, message, response);
    }

    @PostMapping
    public ResponseEntity<ResponseData<RoleHasPermissionResponse>> create(
            @RequestBody @Validated RoleHasPermissionRequest request) {
        log.info("Request: Create role-permission with data = {}", request);
        RoleHasPermissionResponse response = roleHasPermissionService.create(request);
        log.info("Response: Role-permission created = {}", response);
        String message = messageSource.getMessage("role.permission.create.success", null, LocaleContextHolder.getLocale());
        return ResponseBuilder.withData(HttpStatus.CREATED, message, response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<RoleHasPermissionResponse>> update(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 0") Long id,
            @RequestBody @Validated RoleHasPermissionRequest request) {
        log.info("Request: Update role-permission with id = {}, data = {}", id, request);
        RoleHasPermissionResponse response = roleHasPermissionService.update(id, request);
        log.info("Response: Role-permission updated = {}", response);
        String message = messageSource.getMessage("role.permission.update.success", null, LocaleContextHolder.getLocale());
        return ResponseBuilder.withData(HttpStatus.OK, message, response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> delete(
            @PathVariable("id") @Min(value = 1, message = "{validation.id.min}") Long id) {
        log.info("Request: Delete role-permission with id = {}", id);
        roleHasPermissionService.delete(id);
        log.info("Response: Role-permission deleted with id = {}", id);
        String message = messageSource.getMessage("role.permission.delete.success", null, LocaleContextHolder.getLocale());
        return ResponseBuilder.noData(HttpStatus.OK, message);
    }
}
