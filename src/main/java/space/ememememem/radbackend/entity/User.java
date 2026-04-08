package space.ememememem.radbackend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import space.ememememem.radbackend.enums.UserRole;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;

    private String refreshToken;

    private UserRole userRole;

    @Id
    private String openId;

    private String sessionKey;

    private String avatarBase64;
}
