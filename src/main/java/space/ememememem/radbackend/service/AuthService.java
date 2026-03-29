package space.ememememem.radbackend.service;

import space.ememememem.radbackend.dto.request.LoginRequest;
import space.ememememem.radbackend.dto.request.RefreshRequest;
import space.ememememem.radbackend.dto.response.AuthTokenResponse;
import space.ememememem.radbackend.dto.response.UserInfoResponse;

public interface AuthService {
    AuthTokenResponse login(LoginRequest req);

    AuthTokenResponse refresh(RefreshRequest req);

    UserInfoResponse userinfo(String authToken);
}
