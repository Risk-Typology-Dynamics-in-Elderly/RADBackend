package space.ememememem.radbackend.config;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient restClient(RestClient.Builder builder, BuildProperties buildProperties) {
        return builder
                .baseUrl("https://api.weixin.qq.com")
                .defaultHeader("User-Agent", "RADBackend/" +
                        buildProperties.getVersion() + " (Springboot)")
                .build();
    }
}
