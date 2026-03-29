package space.ememememem.radbackend.controller;

import org.springframework.web.bind.annotation.*;
import space.ememememem.radbackend.dto.request.LoginRequest;
import space.ememememem.radbackend.dto.request.RefreshRequest;
import space.ememememem.radbackend.dto.response.AuthTokenResponse;
import space.ememememem.radbackend.dto.response.UserInfoResponse;
import space.ememememem.radbackend.exception.LoginException;
import space.ememememem.radbackend.service.AuthService;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthTokenResponse login(@RequestBody LoginRequest req) throws LoginException {
        return authService.login(req);
    }

    @PostMapping("/refresh")
    public AuthTokenResponse refresh(@RequestBody RefreshRequest req) throws LoginException {
        return authService.refresh(req);
    }

    @GetMapping("/userinfo")
    public UserInfoResponse userInfo(@RequestHeader("Authorization") String auth) {
        return authService.userinfo(auth);
    }
}
