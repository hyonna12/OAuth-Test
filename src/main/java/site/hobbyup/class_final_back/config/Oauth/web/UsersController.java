package site.hobbyup.class_final_back.config.Oauth.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import site.hobbyup.class_final_back.config.Oauth.domain.Users;
import site.hobbyup.class_final_back.config.Oauth.dto.KakaoProfile;
import site.hobbyup.class_final_back.config.Oauth.dto.OAuthToken;
import site.hobbyup.class_final_back.config.Oauth.service.KakaoApiService;
import site.hobbyup.class_final_back.config.Oauth.service.UsersService;

@Controller
public class UsersController {
    private UsersService usersService;
    private AuthenticationManager authenticationManager;
    private KakaoApiService kakaoApiService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(@RequestParam("code") String code) {

        OAuthToken oAuthToken = kakaoApiService.tokenRequest(code);

        KakaoProfile kakaoProfile = kakaoApiService.userInfoRequest(oAuthToken);

        int idx = kakaoProfile.getKakao_account().getEmail().indexOf("@");
        String username = kakaoProfile.getKakao_account().getEmail().substring(0,
                idx);
        System.out.println("유저네임:" + username);

        Users kakaoUser = Users.builder()
                .username(username)
                .password(kakaoProfile.getKakao_account().getEmail() + kakaoProfile.getId())
                .oauth("kakao")
                .build();

        // 회원가입, 로그인
        usersService.join(kakaoUser);

        return "redirect:/";
    }

}
