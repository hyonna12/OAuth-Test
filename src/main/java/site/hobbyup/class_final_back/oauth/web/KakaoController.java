package site.hobbyup.class_final_back.oauth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import site.hobbyup.class_final_back.oauth.service.KakaoService;

@Controller
public class KakaoController {
    @Autowired
    private KakaoService kakaoService;

    @GetMapping("/auth/kakao")
    public KakaoAccount getKakaoAccount(@RequestParam("code") String code) {
        log.debug("code = {}", code);
        return kakaoService.getInfo(code).getKakaoAccount();
    }
}