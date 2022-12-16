package site.hobbyup.class_final_back.oauth.service;

import java.net.URI;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoClient client;

    @Value("${kakao.auth-url}")
    private String kakaoAuthUrl;

    @Value("${kakao.user-api-url}")
    private String kakaoUserApiUrl;

    @Value("${kakao.restapi-key}")
    private String restapiKey;

    @Value("${kakao.redirect-url}")
    private String redirectUrl;

    // getInfo : userAPI를 찔러서, 사용자가 동의한 정보들을 가져옴
    // 이 api를 사용하려면 accessToken이 필요.
    // getToken : 인증 API를 찔러서, accessToken, refreshToken,
    // 유효기간,인증 타입(ex,Bearer)을 받아옴

    public KakaoInfo getInfo(final String code) {
        final KakaoToken token = getToken(code);
        log.debug("token = {}", token);
        try {
            return client.getInfo(new URI(kakaoUserApiUrl), token.getTokenType() + " " + token.getAccessToken());
        } catch (Exception e) {
            log.error("something error..", e);
            return KakaoInfo.fail();
        }
    }

    private KakaoToken getToken(final String code) {
        try {
            return client.getToken(new URI(kakaoAuthUrl), restapiKey, redirectUrl, code, "authorization_code");
        } catch (Exception e) {
            log.error("Something error..", e);
            return KakaoToken.fail();
        }
    }
}