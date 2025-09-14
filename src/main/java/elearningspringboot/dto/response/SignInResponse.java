package elearningspringboot.dto.response;

import elearningspringboot.enumeration.UserRole;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInResponse {
    private Long id;
    private String fullName;
    private String email;
    private String avatarUrl;
    private UserRole role;
    private Boolean noPassword;
    private List<String> permissions;
}
