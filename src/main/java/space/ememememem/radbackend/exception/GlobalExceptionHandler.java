package space.ememememem.radbackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import space.ememememem.radbackend.dto.response.ExceptionHandlerResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionHandlerResponse> handleRuntime(RuntimeException ex) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse();
        response.setCode(500);
        response.setMessage("Internal Server Error");
        log.error("Internal server error: ", ex);

        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ExceptionHandlerResponse> handleLogin(LoginException ex) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse();
        response.setCode(ex.getErrorCode().getCode());
        response.setMessage(ex.getErrorCode().getMessage());
        return ResponseEntity.status(401).body(response);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ExceptionHandlerResponse> handleResourceConflict(ResourceConflictException ex) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse();
        response.setCode(ex.getErrorCode().getCode());
        response.setMessage(ex.getErrorCode().getMessage());
        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionHandlerResponse> handleBadRequest(ResourceConflictException ex) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse();
        response.setCode(ex.getErrorCode().getCode());
        response.setMessage(ex.getErrorCode().getMessage());
        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionHandlerResponse> handleReadableException() {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse();
        response.setCode(4001);
        response.setMessage("Invalid argument structure");
        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionHandlerResponse> handleValidationException() {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse();
        response.setCode(4002);
        response.setMessage("Argument value validation failed");
        return ResponseEntity.status(400).body(response);
    }
}