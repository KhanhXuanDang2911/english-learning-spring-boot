package elearningspringboot.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import elearningspringboot.enumeration.Gender;
import elearningspringboot.enumeration.Status;
import elearningspringboot.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserResponse extends BaseResponse {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private String address;
    private LocalDate birthDate;
    private UserRole role;
    private Status status;
    private Gender gender;
    private Boolean noPassword;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> permissions;
}
