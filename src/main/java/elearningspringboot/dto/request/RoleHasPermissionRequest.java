package elearningspringboot.dto.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class RoleHasPermissionRequest {

    @NotNull(message = "Role ID is required")
    @Positive(message = "Role ID must be greater than 0")
    private Long roleId;

    @NotNull(message = "Permission ID is required")
    @Positive(message = "Permission ID must be greater than 0")
    private Long permissionId;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
}

