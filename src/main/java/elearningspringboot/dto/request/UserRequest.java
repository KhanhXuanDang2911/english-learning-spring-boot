package elearningspringboot.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import elearningspringboot.enumeration.UserRole;
import elearningspringboot.util.ValueOfEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequest {
    @NotBlank(message = "FullName must be not blank")
    @Size(min = 3, max = 160, message = "FullName must be between 3 and 160 characters")
    private String fullName;
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email invalid format"
    )
    private String email;
    @Pattern(
            regexp = "^(?:\\+84|0)[35789][0-9]{8}$\n",
            message = "Invalid phoneNumber"
    )
    private String phoneNumber;
    @NotBlank(message = "Password must be not blank")
    @Size(min = 8, max = 160, message = "password must be between 8 and 160 characters")
    private String password;
    private String avatarUrl;
    private String address;
    @NotNull(message = "birthday is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @PastOrPresent(message = "Birth date must be in the past or present")
    private LocalDate birthDate;
    @ValueOfEnum(enumClass = UserRole.class, message = "Role must be one of: ADMIN, USER, TEACHER")
    private UserRole role;
}
