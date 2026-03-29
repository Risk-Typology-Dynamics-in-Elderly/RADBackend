package space.ememememem.radbackend.dto.response;

import lombok.Data;

@Data
public class WechatAuthResponse {
    private String session_key;
    private String unionid;
    private String openid;
    private int errcode;
    private String errmsg;
}
