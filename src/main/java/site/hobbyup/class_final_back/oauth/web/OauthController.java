package site.hobbyup.class_final_back.oauth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OauthController {

    @GetMapping("/auth/kakao")
    public String getCode(@RequestParam String code) {
        System.out.println(code);
        return "code = " + code;
    }
}
