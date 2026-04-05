package space.ememememem.radbackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import space.ememememem.radbackend.config.JwtProperties;
import space.ememememem.radbackend.enums.UserRole;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(JwtProperties jwtProperties) {
        key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String generateToken(String username, String openId, UserRole role) {
        long expiration = 1000 * 60 * 10;

        return Jwts.builder()
                .setSubject(openId)
                .claim("username", username)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username, String openId, UserRole role) {
        long expiration = 1000L * 60 * 60 * 24 * 30;
        return Jwts.builder()
                .setSubject(openId)
                .claim("username", username)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String extractOpenId(String token) {
        return getClaims(token).getSubject();
    }

    public Claims extractClaims(String token) {
        return getClaims(token);
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