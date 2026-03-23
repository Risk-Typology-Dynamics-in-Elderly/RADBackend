package space.ememememem.radbackend.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import space.ememememem.radbackend.dto.request.LoginRequest;
import space.ememememem.radbackend.dto.request.RefreshRequest;
import space.ememememem.radbackend.dto.request.RegisterRequest;
import space.ememememem.radbackend.entity.User;
import space.ememememem.radbackend.exception.BadRequestException;
import space.ememememem.radbackend.exception.ResourceConflictException;
import space.ememememem.radbackend.repository.UserRepository;
import space.ememememem.radbackend.security.JwtUtil;

import javax.security.auth.login.LoginException;
import java.util.Map;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest req) throws LoginException {

        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid request value"));

        if(!passwordEncoder.matches(req.getPassword(), user.getPasswordHash()))
            throw new LoginException("Invalid Password");

        return Map.of(
                "accessToken", jwtUtil.generateToken(req.getUsername()),
                "refreshToken", jwtUtil.generateRefreshToken(req.getUsername())
        );
    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody RefreshRequest req) throws LoginException {
        String refreshToken = req.getRefreshToken();
        User user = userRepo.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new LoginException("Invalid refresh token"));

        if (!jwtUtil.validateToken(refreshToken)) throw new LoginException("Refresh token expired");

        String newAccessToken = jwtUtil.generateToken(user.getUsername());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername());
        return Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken
        );
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent())
            throw new ResourceConflictException("Username already exists");
        User newUser = User.builder()
                .username(req.getUsername())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .userRole(req.getUserRole())
                .build();
        userRepo.save(newUser);
    }

    @GetMapping("/userinfo")
    public Map<String, String> userInfo(@RequestHeader("Authorization") String auth) {
        String token = auth.substring(7);
        String username = jwtUtil.extractUsername(token);
        User user = userRepo.findByUsername(username).orElseThrow();
        return Map.of(
                "username", username,
                "uid", user.getId().toString()
        );
    }
}
