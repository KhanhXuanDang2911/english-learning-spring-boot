package elearningspringboot.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleHasPermissionRequest {
    private Long roleId;
    private Long permissionId;
    private String description;
}
