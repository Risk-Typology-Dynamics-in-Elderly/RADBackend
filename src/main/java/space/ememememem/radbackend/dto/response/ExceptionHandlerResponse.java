package space.ememememem.radbackend.dto.response;

import lombok.Data;

@Data
public class ExceptionHandlerResponse {
    private String message;

    private int code;
}
