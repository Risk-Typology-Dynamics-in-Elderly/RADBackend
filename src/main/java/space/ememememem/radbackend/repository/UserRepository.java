package space.ememememem.radbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import space.ememememem.radbackend.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByRefreshToken(String refreshToken);
    Optional<User> findByOpenId(String openid);
}