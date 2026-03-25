package space.ememememem.radbackend.exception;

public class ResourceConflictException extends BaseException {
    public ResourceConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
