package com.example.mymoo.global.oauth.controller;

import com.example.mymoo.global.oauth.service.OAuthService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/oauth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

    @GetMapping("/kakao")
    public ResponseEntity<Map<String, String>> kakaoLogin() {
        String kakaoAuthUrl = oAuthService.getKakaoAuthorizationUrl();
        Map<String, String> response = new HashMap<>();
        response.put("authorizationUrl", kakaoAuthUrl);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) {
        return ResponseEntity.ok(oAuthService.processKakaoCallback(code));
    }
}
