package elearningspringboot.service.impl;

import elearningspringboot.dto.request.RoleHasPermissionRequest;
import elearningspringboot.dto.response.RoleHasPermissionResponse;
import elearningspringboot.entity.Permission;
import elearningspringboot.entity.Role;
import elearningspringboot.entity.RoleHasPermission;
import elearningspringboot.exception.ResourceConflictException;
import elearningspringboot.exception.ResourceNotFoundException;
import elearningspringboot.repository.PermissionRepository;
import elearningspringboot.repository.RoleHasPermissionRepository;
import elearningspringboot.repository.RoleRepository;
import elearningspringboot.service.RoleHasPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleHasPermissionServiceImpl implements RoleHasPermissionService {

    private final RoleHasPermissionRepository repository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public RoleHasPermissionResponse create(RoleHasPermissionRequest request) {
        log.info("Creating RoleHasPermission with roleId={} and permissionId={}",
                request.getRoleId(), request.getPermissionId());

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> {
                    log.error("Role with id={} not found", request.getRoleId());
                    return new ResourceNotFoundException(
                            String.format("Role with id = %d not found", request.getRoleId()));
                });

        Permission permission = permissionRepository.findById(request.getPermissionId())
                .orElseThrow(() -> {
                    log.error("Permission with id={} not found", request.getPermissionId());
                    return new ResourceNotFoundException(
                            String.format("Permission with id = %d not found", request.getPermissionId()));
                });

        if (repository.existsByRoleAndPermission(role, permission)) {
            log.warn("Conflict: Role id={} already has permission id={}", role.getId(), permission.getId());
            throw new ResourceConflictException(
                    String.format("Role with id = %d already has permission with id = %d",
                            role.getId(), permission.getId()));
        }

        RoleHasPermission rhp = RoleHasPermission.builder()
                .role(role)
                .permission(permission)
                .description(request.getDescription())
                .build();

        repository.save(rhp);
        log.info("RoleHasPermission created successfully with id={}", rhp.getId());

        return mapToResponse(rhp);
    }

    @Override
    public RoleHasPermissionResponse update(Long id, RoleHasPermissionRequest request) {
        log.info("Updating RoleHasPermission with id={}", id);

        RoleHasPermission rhp = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("RoleHasPermission with id={} not found", id);
                    return new ResourceNotFoundException(
                            String.format("RoleHasPermission with id = %d not found", id));
                });

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> {
                    log.error("Role with id={} not found", request.getRoleId());
                    return new ResourceNotFoundException(
                            String.format("Role with id = %d not found", request.getRoleId()));
                });

        Permission permission = permissionRepository.findById(request.getPermissionId())
                .orElseThrow(() -> {
                    log.error("Permission with id={} not found", request.getPermissionId());
                    return new ResourceNotFoundException(
                            String.format("Permission with id = %d not found", request.getPermissionId()));
                });

        if (!rhp.getRole().equals(role) || !rhp.getPermission().equals(permission)) {
            if (repository.existsByRoleAndPermission(role, permission)) {
                log.warn("Conflict: Role id={} already has permission id={}", role.getId(), permission.getId());
                throw new ResourceConflictException(
                        String.format("Role with id = %d already has permission with id = %d",
                                role.getId(), permission.getId()));
            }
        }

        rhp.setRole(role);
        rhp.setPermission(permission);
        rhp.setDescription(request.getDescription());

        repository.save(rhp);
        log.info("RoleHasPermission updated successfully with id={}", rhp.getId());

        return mapToResponse(rhp);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleHasPermissionResponse getById(Long id) {
        log.info("Fetching RoleHasPermission with id={}", id);

        RoleHasPermission rhp = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("RoleHasPermission with id={} not found", id);
                    return new ResourceNotFoundException(
                            String.format("RoleHasPermission with id = %d not found", id));
                });

        log.info("RoleHasPermission found with id={}", rhp.getId());
        return mapToResponse(rhp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleHasPermissionResponse> getAll() {
        log.info("Fetching all RoleHasPermission records");
        List<RoleHasPermissionResponse> responses = repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        log.info("Fetched {} RoleHasPermission records", responses.size());
        return responses;
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting RoleHasPermission with id={}", id);

        RoleHasPermission rhp = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("RoleHasPermission with id={} not found", id);
                    return new ResourceNotFoundException(
                            String.format("RoleHasPermission with id = %d not found", id));
                });

        repository.delete(rhp);
        log.info("RoleHasPermission deleted successfully with id={}", id);
    }

    private RoleHasPermissionResponse mapToResponse(RoleHasPermission rhp) {
        return RoleHasPermissionResponse.builder()
                .id(rhp.getId())
                .role(rhp.getRole().getRole())
                .permission(rhp.getPermission().getName())
                .description(rhp.getDescription())
                .createdAt(rhp.getCreatedAt())
                .updatedAt(rhp.getUpdatedAt())
                .build();
    }
}
