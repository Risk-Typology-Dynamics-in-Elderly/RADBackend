package space.ememememem.radbackend.exception;

public class LoginException extends BaseException {
    public LoginException(ErrorCode errorCode) {
        super(errorCode);
    }
}
