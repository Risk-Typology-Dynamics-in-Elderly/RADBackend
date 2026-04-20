package space.ememememem.radbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import space.ememememem.radbackend.dto.request.ChangeUsernameRequest;
import space.ememememem.radbackend.dto.request.UpdateAvatarRequest;
import space.ememememem.radbackend.dto.response.AvatarResponse;
import space.ememememem.radbackend.service.UserInfoService;

@RestController
@RequestMapping("/api/v1/user")
public class UserInfoController {
    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping("/avatar/upload")
    public ResponseEntity<Void> uploadAvatar(
        @RequestHeader("Authorization") String authToken,
        @RequestBody UpdateAvatarRequest updateAvatarRequest
    ) {
        userInfoService.saveAvatar(authToken, updateAvatarRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/avatar")
    public ResponseEntity<AvatarResponse> getAvatar(@RequestHeader("Authorization") String authToken) {
        String avatarBase64 = userInfoService.getAvatar(authToken);

        if (avatarBase64 == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new AvatarResponse(avatarBase64));
    }

    @PostMapping("/username/change")
    public ResponseEntity<Void> changeUsername(
            @RequestHeader("Authorization") String authToken,
            @RequestBody ChangeUsernameRequest changeUsernameRequest
    ) {
        userInfoService.changeUsername(authToken, changeUsernameRequest);
        return ResponseEntity.ok().build();
    }
}
