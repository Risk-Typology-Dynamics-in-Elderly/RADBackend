package space.ememememem.radbackend.service.impl;

import org.springframework.stereotype.Service;
import space.ememememem.radbackend.dto.request.ChangeUsernameRequest;
import space.ememememem.radbackend.dto.request.UpdateAvatarRequest;
import space.ememememem.radbackend.entity.User;
import space.ememememem.radbackend.exception.BadRequestException;
import space.ememememem.radbackend.exception.ErrorCode;
import space.ememememem.radbackend.repository.UserRepository;
import space.ememememem.radbackend.security.JwtUtil;
import space.ememememem.radbackend.service.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public UserInfoServiceImpl(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void saveAvatar(String authToken, UpdateAvatarRequest updateAvatarRequest) {
        String avatarBase64 = updateAvatarRequest.getNewAvatarBase64();
        if (avatarBase64 == null || avatarBase64.isEmpty()) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_VALUE);
        }

        User user = userRepository.findByOpenId(jwtUtil.extractOpenId(authToken.substring(7))).orElseThrow();
        user.setAvatarBase64(avatarBase64);
        userRepository.save(user);
    }

    @Override
    public String getAvatar(String authToken) {
        User user = userRepository.findByOpenId(jwtUtil.extractOpenId(authToken.substring(7))).orElseThrow();
        return user.getAvatarBase64();
    }

    @Override
    public void changeUsername(String authToken, ChangeUsernameRequest changeUsernameRequest) {
        User user = userRepository.findByOpenId(jwtUtil.extractOpenId(authToken.substring(7))).orElseThrow();
        String newUsername = changeUsernameRequest.getNewUsername();
        if (newUsername == null || newUsername.isEmpty()) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_VALUE);
        }
        user.setUsername(newUsername);
        userRepository.save(user);
    }
}
