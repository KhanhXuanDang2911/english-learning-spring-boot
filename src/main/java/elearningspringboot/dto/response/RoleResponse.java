package elearningspringboot.dto.response;

import elearningspringboot.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RoleResponse extends BaseResponse {
    private UserRole role;
    private String description;
    private List<PermissionResponse> permissions;
}
