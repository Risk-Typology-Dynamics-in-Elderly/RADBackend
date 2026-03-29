package space.ememememem.radbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "app.wechat")
public class WechatAPIProperties {
    private String appId;
    private String appSecret;
}
