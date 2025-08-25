package elearningspringboot.service;

import elearningspringboot.dto.request.RoleHasPermissionRequest;
import elearningspringboot.dto.response.RoleHasPermissionResponse;

import java.util.List;

public interface RoleHasPermissionService {
    RoleHasPermissionResponse create(RoleHasPermissionRequest request);
    RoleHasPermissionResponse update(Long id, RoleHasPermissionRequest request);
    RoleHasPermissionResponse getById(Long id);
    List<RoleHasPermissionResponse> getAll();
    void delete(Long id);
}
