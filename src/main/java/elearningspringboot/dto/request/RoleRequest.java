package elearningspringboot.dto.request;

import elearningspringboot.enumeration.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequest {
    private UserRole role;
    private String description;
}
