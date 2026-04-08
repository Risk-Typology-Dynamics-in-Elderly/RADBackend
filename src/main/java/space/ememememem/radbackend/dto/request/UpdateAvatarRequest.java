package space.ememememem.radbackend.dto.request;

import lombok.Data;

@Data
public class UpdateAvatarRequest {
    private String newAvatarBase64;
}