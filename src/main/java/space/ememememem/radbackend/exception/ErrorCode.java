package space.ememememem.radbackend.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    DEFAULT(-1, ""),

    BAD_REQUEST_VALUE(4003, "Invalid request value"),

    PASSWORD_INVALID(4011, "Invalid password"),
    REFRESH_TOKEN_INVALID(4012, "Invalid refresh token"),

    USERNAME_CONFLICT(4091, "Username already exists");

    private final int code;
    private final String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
