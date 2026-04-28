package space.ememememem.radbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import space.ememememem.radbackend.enums.UserRole;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private String username;
    private UserRole userRole;
    private String avatarBase64;
}
