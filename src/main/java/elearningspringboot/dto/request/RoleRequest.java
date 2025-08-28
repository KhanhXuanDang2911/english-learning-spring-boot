package elearningspringboot.dto.request;

import elearningspringboot.enumeration.UserRole;
import elearningspringboot.validation.ValueOfEnum;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleRequest {
    @ValueOfEnum(enumClass = UserRole.class, message = "Role must be one of: USER, TEACHER, ADMIN")
    private String role;
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
}
