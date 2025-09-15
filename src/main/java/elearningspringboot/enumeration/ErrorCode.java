package elearningspringboot.enumeration;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_ROLE_ENUM(40001, "Invalid name role enum"),
    INVALID_GENDER_ENUM(40002, "Invalid name gender enum"),
    INVALID_STATUS_ENUM(40003, "Invalid name status enum"),
    INVALID_REFRESH_TOKEN(40101, "Invalid refresh token"),
    PENDING_ACCOUNT(40004, "Pending account"),
    PASSWORD_NOT_MATCH(40005, "Password not match"),
    ACCOUNT_NOT_ACTIVE(40006, "Account not active"),
    UPLOAD_FILE_FAILED(50001, "Upload file failed");
    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
