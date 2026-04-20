package space.ememememem.radbackend.service;

import space.ememememem.radbackend.dto.request.ChangeUsernameRequest;
import space.ememememem.radbackend.dto.request.UpdateAvatarRequest;

public interface UserInfoService {
    void saveAvatar(String authToken, UpdateAvatarRequest updateAvatarRequest);
    String getAvatar(String authToken);
    void changeUsername(String authToken, ChangeUsernameRequest changeUsernameRequest);
}
