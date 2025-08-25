package elearningspringboot.dto.response;

import elearningspringboot.enumeration.UserRole;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RoleHasPermissionResponse extends BaseResponse {
    private UserRole role;
    private String permission;
    private String description;
}
