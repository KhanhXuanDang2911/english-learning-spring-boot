package elearningspringboot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequest {

    @NotBlank(message = "Permission name must be not blank")
    @Size(max = 50, message = "Permission name must not exceed 50 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotBlank(message = "Module must be not blank")
    @Size(max = 50, message = "Module name must not exceed 50 characters")
    private String module;
}
