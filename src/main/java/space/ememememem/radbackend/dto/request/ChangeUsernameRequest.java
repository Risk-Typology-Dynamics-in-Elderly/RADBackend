package space.ememememem.radbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeUsernameRequest {
    @NotBlank
    private String newUsername;
}
