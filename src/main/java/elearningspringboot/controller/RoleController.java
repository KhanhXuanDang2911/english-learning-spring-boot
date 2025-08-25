package elearningspringboot.controller;

import elearningspringboot.dto.request.RoleRequest;
import elearningspringboot.dto.response.ResponseData;
import elearningspringboot.dto.response.RoleResponse;
import elearningspringboot.service.RoleService;
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
@RequestMapping(value = "/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<ResponseData<List<RoleResponse>>> getAll() {
        log.info("Request: Get all roles");
        List<RoleResponse> response = roleService.getAllRoles();
        log.info("Response: {} roles found", response.size());
        return ResponseBuilder.withData(HttpStatus.OK, "Get list roles successfully", response);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ResponseData<RoleResponse>> getByRoleName(@PathVariable("name") String name) {
        log.info("Request: Get role by name = {}", name);
        RoleResponse response = roleService.getRoleByRoleName(name);
        log.info("Response: Found role = {}", response);
        return ResponseBuilder.withData(HttpStatus.OK, "Get role successfully", response);
    }

    @PostMapping
    public ResponseEntity<ResponseData<RoleResponse>> create(@RequestBody @Validated RoleRequest request) {
        log.info("Request: Create role with data = {}", request);
        RoleResponse response = roleService.createRole(request);
        log.info("Response: Role created = {}", response);
        return ResponseBuilder.withData(HttpStatus.CREATED, "Role created successfully", response);
    }

    @PutMapping
    public ResponseEntity<ResponseData<RoleResponse>> update(
            @RequestParam @Min(value = 1, message = "Id must be greater than 0") Long id,
            @RequestBody @Validated RoleRequest request) {
        log.info("Request: Update role with id = {}, data = {}", id, request);
        RoleResponse response = roleService.updateRole(request, id);
        log.info("Response: Role updated = {}", response);
        return ResponseBuilder.withData(HttpStatus.OK, "Role updated successfully", response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> delete(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than 1") Long id) {
        log.info("Request: Delete role with id = {}", id);
        roleService.deleteRole(id);
        log.info("Response: Role deleted with id = {}", id);
        return ResponseBuilder.noData(HttpStatus.OK, "Role deleted successfully");
    }
}
