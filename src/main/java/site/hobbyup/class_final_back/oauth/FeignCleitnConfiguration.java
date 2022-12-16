package site.hobbyup.class_final_back.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import site.hobbyup.class_final_back.domain.user.User;

@Configuration
public class KakaoFeignConfiguration {
    @Bean
    public User feignClient() {
        return new User.Default(null, null);
    }
}