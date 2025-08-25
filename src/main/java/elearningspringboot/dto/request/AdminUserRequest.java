package elearningspringboot.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import elearningspringboot.enumeration.Gender;
import elearningspringboot.enumeration.Status;
import elearningspringboot.enumeration.UserRole;
import elearningspringboot.validation.OnCreate;
import elearningspringboot.validation.OnUpdate;
import elearningspringboot.validation.ValueOfEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdminUserRequest {
    @NotBlank(message = "FullName must be not blank")
    @Size(min = 3, max = 160, message = "FullName must be between 3 and 160 characters")
    private String fullName;
    @NotBlank(message = "Email must be not blank")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email invalid format"
    )
    private String email;
    @NotBlank(message = "Phone number must be not blank")
    @Pattern(
            regexp = "^(?:\\+84|0)[35789][0-9]{8}$",
            message = "Invalid phoneNumber"
    )
    private String phoneNumber;
    @NotBlank(message = "Password must be not blank", groups = OnCreate.class)
    @Size(min = 8, max = 160, message = "password must be between 8 and 160 characters")
    private String password;
    private String avatarUrl;
    private String address;
    @NotNull(message = "Birthdate is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @PastOrPresent(message = "Birth date must be in the past or present")
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
