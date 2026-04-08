package space.ememememem.radbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import space.ememememem.radbackend.enums.UserRole;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    public String username;
    public UserRole userRole;
}
