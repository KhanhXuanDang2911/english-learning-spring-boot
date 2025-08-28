package elearningspringboot.enumeration;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_ROLE_ENUM(601, "Invalid name role enum"),
    INVALID_GENDER_ENUM(602, "Invalid name gender enum"),
    INVALID_STATUS_ENUM(603, "Invalid name status enum"),
    INVALID_REFRESH_TOKEN(604, "Invalid refresh token");
    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
