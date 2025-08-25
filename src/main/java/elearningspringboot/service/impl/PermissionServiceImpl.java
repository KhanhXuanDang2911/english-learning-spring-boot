package elearningspringboot.service.impl;

import elearningspringboot.dto.request.PermissionRequest;
import elearningspringboot.dto.response.PageResponse;
import elearningspringboot.dto.response.PermissionResponse;
import elearningspringboot.entity.Permission;
import elearningspringboot.exception.ResourceConflictException;
import elearningspringboot.exception.ResourceNotFoundException;
import elearningspringboot.mapper.PermissionMapper;
import elearningspringboot.repository.PermissionRepository;
import elearningspringboot.service.PermissionService;
import elearningspringboot.util.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        log.info("Creating permission with name: {}", request.getName());

        if (permissionRepository.existsByName(request.getName())) {
            log.error("Permission with name '{}' already exists", request.getName());
            throw new ResourceConflictException(
                    String.format("Permission with name '%s' already exists", request.getName())
            );
        }

        Permission permission = permissionMapper.toEntity(request);
        permissionRepository.save(permission);

        log.info("Permission created successfully with id: {}", permission.getId());
        return permissionMapper.toDTO(permission);
    }

    @Override
    public PermissionResponse updatePermission(Long id, PermissionRequest request) {
        log.info("Updating permission with id: {}", id);

        if (permissionRepository.existsByNameExceptForId(request.getName(), id)) {
            log.error("Permission with name '{}' already exists for another id", request.getName());
            throw new ResourceConflictException(
                    String.format("Permission with name '%s' already exists", request.getName())
            );
        }

        Permission permission = findPermissionById(id);
        permissionMapper.updateEntityFromDTO(request, permission);
        permissionRepository.save(permission);

        log.info("Permission updated successfully with id: {}", id);
        return permissionMapper.toDTO(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<List<PermissionResponse>> getPermissions(int pageNumber, int pageSize, List<String> sorts) {
        log.info("Fetching permissions with pagination: pageNumber={}, pageSize={}, sorts={}", pageNumber, pageSize, sorts);

        List<String> whiteListFieldSorts = List.of("createdAt", "updatedAt", "name", "module");
        Pageable pageable = AppUtils.generatePageableWithSort(sorts, whiteListFieldSorts, pageNumber, pageSize);

        Page<Permission> permissionPage = permissionRepository.findAll(pageable);

        List<PermissionResponse> permissionResponses = permissionPage.getContent()
                .stream()
                .map(permissionMapper::toDTO)
                .toList();

        log.info("Fetched {} permissions out of total {} elements",
                permissionResponses.size(), permissionPage.getTotalElements());

        return PageResponse.<List<PermissionResponse>>builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalPages(permissionPage.getTotalPages())
                .numberOfElements(permissionPage.getNumberOfElements())
                .items(permissionResponses)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionResponse getPermissionById(Long id) {
        log.info("Fetching permission by id: {}", id);

        Permission permission = findPermissionById(id);

        log.info("Permission found with id: {}", id);
        return permissionMapper.toDTO(permission);
    }

    @Override
    public void deletePermission(Long id) {
        log.info("Deleting permission with id: {}", id);

        Permission permission = findPermissionById(id);
        permissionRepository.delete(permission);

        log.info("Permission deleted successfully with id: {}", id);
    }

    private Permission findPermissionById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Permission with id = {} not found", id);
                    return new ResourceNotFoundException(
                            String.format("Permission with id = %d not found", id)
                    );
                });
    }
}
