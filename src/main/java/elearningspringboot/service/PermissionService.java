package elearningspringboot.service;

import elearningspringboot.dto.request.PermissionRequest;
import elearningspringboot.dto.response.PageResponse;
import elearningspringboot.dto.response.PermissionResponse;
import elearningspringboot.entity.Permission;
import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest request);
    PermissionResponse updatePermission(Long id, PermissionRequest request);
    PageResponse<List<PermissionResponse>> getPermissions(int pageNumber, int pageSize, List<String> sorts);
    PermissionResponse getPermissionById(Long id);
    void deletePermission(Long id);
}
