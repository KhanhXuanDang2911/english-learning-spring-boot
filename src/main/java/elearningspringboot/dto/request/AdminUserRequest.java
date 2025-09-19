package elearningspringboot.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import elearningspringboot.enumeration.Gender;
import elearningspringboot.enumeration.Status;
import elearningspringboot.enumeration.UserRole;
import elearningspringboot.validation.OnCreate;
import elearningspringboot.validation.ValueOfEnum;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserRequest {
    @NotBlank(message = "FullName must be not blank")
    @Size(min = 3, max = 160, message = "FullName must be between 3 and 160 characters")
    private String fullName;
    @NotBlank(message = "Email must be not blank")
    @NotBlank(message = "Email must be not blank")
    @Pattern(
            regexp = "^[a-zA-Z0-9](?:[a-zA-Z0-9._%+-]{0,63}[a-zA-Z0-9])?@[a-zA-Z0-9](?:[a-zA-Z0-9.-]{0,253}[a-zA-Z0-9])?\\.[a-zA-Z]{2,}$",
            message = "Invalid email format"
    )
    private String email;
    @Pattern(
            regexp = "^(?:\\+84|0)[35789][0-9]{8}$",
            message = "Invalid phoneNumber"
    )
    private String phoneNumber;
    @NotBlank(message = "Password must be not blank", groups = OnCreate.class)
    @Size(min = 8, max = 160, message = "password must be between 8 and 160 characters")
    private String password;
    private String address;
    @NotNull(message = "Birthdate is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    @NotNull(message = "Role must be not null")
    @ValueOfEnum(enumClass = UserRole.class, message = "Role must be one of: USER, TEACHER, ADMIN")
    private String role;
    @NotNull(message = "Status must be not null")
    @ValueOfEnum(enumClass = Status.class, message = "Status must be one of: ACTIVE, BANNED")
    private String status;
    @NotNull(message = "Gender must be not null")
    @ValueOfEnum(enumClass = Gender.class, message = "Gender must be one of: MALE, FEMALE")
    private String gender;
}
