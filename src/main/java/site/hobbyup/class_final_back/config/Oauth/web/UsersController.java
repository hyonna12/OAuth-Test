package site.hobbyup.class_final_back.config.Oauth.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import site.hobbyup.class_final_back.config.Oauth.domain.Users;
import site.hobbyup.class_final_back.config.Oauth.dto.KakaoProfile;
import site.hobbyup.class_final_back.config.Oauth.dto.OAuthToken;
import site.hobbyup.class_final_back.config.Oauth.service.KakaoApiService;
import site.hobbyup.class_final_back.config.Oauth.service.UsersService;

@RequiredArgsConstructor
@Controller
public class UsersController {
    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final KakaoApiService kakaoApiService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/auth/kakao")
    public String kakaoCallback(@RequestParam("code") String code) {

        System.out.println("디버그=" + code);

        OAuthToken oAuthToken = kakaoApiService.tokenRequest(code);
        System.out.println("디버그:토큰받기 완료");
        System.out.println("디버그 토큰:" + oAuthToken);
        KakaoProfile kakaoProfile = kakaoApiService.userInfoRequest(oAuthToken);
        System.out.println("디버그:정보받기 완료");
        System.out.println("디버그:" + kakaoProfile.getKakao_account().getEmail());
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
