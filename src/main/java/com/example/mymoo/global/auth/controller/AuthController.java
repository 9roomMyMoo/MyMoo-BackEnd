package com.example.mymoo.global.auth.controller;

import com.example.mymoo.global.auth.dto.request.AccountLoginRequestDto;
import com.example.mymoo.global.auth.dto.request.TokenRefreshRequestDto;
import com.example.mymoo.global.auth.dto.response.AccountLoginResponseDto;
import com.example.mymoo.global.auth.dto.response.TokenRefreshResponseDto;
import com.example.mymoo.global.auth.service.AuthService;
import com.example.mymoo.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<AccountLoginResponseDto> login(
        @Valid @RequestBody AccountLoginRequestDto loginRequestDto
    ) {
        return ResponseEntity.ok(
            authService.login(loginRequestDto)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // 현재 인증된 사용자 정보에서 userId 추출
        Long userId = userDetails.getAccountId();

        // 로그아웃 처리
        authService.logout(userId);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenRefreshResponseDto> refreshToken(
        @Valid @RequestBody TokenRefreshRequestDto tokenRefreshRequestDto
    ) {
        return ResponseEntity.ok(
            authService.getNewAccessToken(tokenRefreshRequestDto.refreshToken())
        );
    }
}
