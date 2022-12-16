package site.hobbyup.class_final_back.config.Oauth.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import site.hobbyup.class_final_back.config.Oauth.dto.KakaoProfile;
import site.hobbyup.class_final_back.config.Oauth.dto.OAuthToken;
import site.hobbyup.class_final_back.config.Oauth.dto.kakao.KakaoReqDto;
import site.hobbyup.class_final_back.config.Oauth.service.KakaoApiService;
import site.hobbyup.class_final_back.service.UserService;

@RequiredArgsConstructor
@Controller
public class UsersController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final KakaoApiService kakaoApiService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/auth/kakao")
    public String kakaoCallback(@RequestParam("code") String code) {

        OAuthToken oAuthToken = kakaoApiService.tokenRequest(code);
        KakaoProfile kakaoProfile = kakaoApiService.userInfoRequest(oAuthToken);

        String email = kakaoProfile.getKakao_account().getEmail();
        int idx = email.indexOf("@");
        String username = (kakaoProfile.getKakao_account().getEmail().substring(0,
                idx));

        KakaoReqDto kakaoUser = KakaoReqDto.builder()
                .username(username)
                .password(kakaoProfile.getKakao_account().getEmail() + kakaoProfile.getId())
                .email(email)
                .oauth("kakao")
                .build();
        // 회원가입, 로그인
        userService.kakaoJoin(kakaoUser);

        return "redirect:/";
    }

}
