package space.ememememem.radbackend.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import space.ememememem.radbackend.config.WechatAPIProperties;
import space.ememememem.radbackend.dto.request.LoginRequest;
import space.ememememem.radbackend.dto.request.RefreshRequest;
import space.ememememem.radbackend.dto.response.AuthTokenResponse;
import space.ememememem.radbackend.dto.response.UserInfoResponse;
import space.ememememem.radbackend.dto.response.WechatAuthResponse;
import space.ememememem.radbackend.entity.User;
import space.ememememem.radbackend.enums.UserRole;
import space.ememememem.radbackend.exception.ErrorCode;
import space.ememememem.radbackend.exception.LoginException;
import space.ememememem.radbackend.repository.UserRepository;
import space.ememememem.radbackend.security.JwtUtil;
import space.ememememem.radbackend.service.AuthService;

@Service
@Transactional(rollbackOn = Exception.class)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final WechatAPIProperties wechatAPIProperties;
    private final RestClient restClient;

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, WechatAPIProperties wechatAPIProperties, RestClient restClient) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.wechatAPIProperties = wechatAPIProperties;
        this.restClient = restClient;
    }

    @Override
    public AuthTokenResponse login(LoginRequest req) {

        WechatAuthResponse wechatAuthResponse = restClient.get().uri(
                uriBuilder -> uriBuilder
                        .path("/sns/jscode2session")
                        .queryParam("appid", wechatAPIProperties.getAppId())
                        .queryParam("secret", wechatAPIProperties.getAppSecret())
                        .queryParam("js_code", req.getUserCode())
                        .queryParam("grant_type", "authorization_code")
                        .build()
        ).retrieve().toEntity(WechatAuthResponse.class).getBody();

        assert wechatAuthResponse != null;
        if (wechatAuthResponse.getErrcode() == 40029) throw new LoginException(ErrorCode.AUTH_CODE_INVALID);
        else if (wechatAuthResponse.getErrcode() == 40226) throw new LoginException(ErrorCode.RISK_CHECK_FAILED);
        else if (wechatAuthResponse.getErrcode() != 0) throw new RuntimeException(wechatAuthResponse.getErrmsg());


        User user = userRepository.findByOpenId(wechatAuthResponse.getOpenid())
                .orElse(null);

        if (user == null) {
            user = User.builder()
                    .username(wechatAuthResponse.getOpenid().substring(0, 11))
                    .openId(wechatAuthResponse.getOpenid())
                    .userRole(UserRole.USER)
                    .build();
        }

        user.setSessionKey(wechatAuthResponse.getSession_key());

        String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getOpenId());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getOpenId());

        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new AuthTokenResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public AuthTokenResponse refresh(RefreshRequest req) {
        String refreshToken = req.getRefreshToken();
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new LoginException(ErrorCode.REFRESH_TOKEN_INVALID));

        if (!jwtUtil.validateToken(refreshToken)) throw new LoginException(ErrorCode.REFRESH_TOKEN_INVALID);

        String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getOpenId());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getOpenId());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new AuthTokenResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public UserInfoResponse userinfo(String authToken) {
        String token = authToken.substring(7);
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow();
        return new UserInfoResponse(user.getUsername(), user.getId(), user.getUserRole());
    }
}
