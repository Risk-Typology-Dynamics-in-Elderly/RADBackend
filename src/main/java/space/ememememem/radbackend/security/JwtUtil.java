package space.ememememem.radbackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import space.ememememem.radbackend.config.JwtProperties;
import space.ememememem.radbackend.entity.User;
import space.ememememem.radbackend.repository.UserRepository;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private final UserRepository userRepository;
    private final Key key;

    public JwtUtil(UserRepository userRepository, JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String generateToken(String username, String openId) {
        long expiration = 1000 * 60 * 10;

        return Jwts.builder()
                .setSubject(username)
                .setClaims(Map.of("openId", openId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username, String openId) {
        long expiration = 1000L * 60 * 60 * 24 * 30;
        String newRefreshToken = Jwts.builder()
                                    .setSubject(username)
                                    .setClaims(Map.of("openId", openId))
                                    .setIssuedAt(new Date())
                                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                                    .signWith(key)
                                    .compact();
        User user = userRepository.findByUsername(username).orElseThrow();
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);
        return newRefreshToken;
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {

        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}