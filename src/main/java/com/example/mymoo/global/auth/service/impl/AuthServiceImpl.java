package com.example.mymoo.global.auth.service.impl;

import com.example.mymoo.global.auth.dto.request.AccountLoginRequestDto;
import com.example.mymoo.global.auth.dto.response.AccountLoginResponseDto;
import com.example.mymoo.global.auth.dto.response.TokenRefreshResponseDto;
import com.example.mymoo.global.auth.repository.AuthRepository;
import com.example.mymoo.global.auth.service.AuthService;
import com.example.mymoo.global.security.CustomUserDetails;
import com.example.mymoo.global.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AccountLoginResponseDto login(AccountLoginRequestDto loginRequestDto) {
        // 인증
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequestDto.email(),
                loginRequestDto.password()
            )
        );
        log.info("인증 완료", authentication.getName());

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        // Refresh 토큰 저장
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long accountId = customUserDetails.getAccountId();
        authRepository.saveRefreshToken(
            accountId,
            refreshToken,
            JwtTokenProvider.REFRESH_TOKEN_VALIDITY
        );

        return AccountLoginResponseDto.builder()
            .accountId(accountId)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    @Override
    public TokenRefreshResponseDto getNewAccessToken(String refreshToken) {
        // refresh 토큰 유효성 검사
        Claims claims = jwtTokenProvider.getClaimsFromToken(refreshToken); // throws jwtException
        Long accountId = claims.get(JwtTokenProvider.USER_ID_KEY, Long.class);
        String authority = claims.get(JwtTokenProvider.AUTHORIZATION_KEY, String.class);

        // 저장된 Refresh 토큰과 비교
        String savedRefreshToken = authRepository.findRefreshTokenByAccountId(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));
        if (!savedRefreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token mismatch");
        }

        // 새로운 액세스 토큰 생성
        String newAccessToken = jwtTokenProvider.createAccessTokenWithRefreshTokenInfo(
            accountId,
            authority
        );

        return TokenRefreshResponseDto.builder()
            .accessToken(newAccessToken)
            .build();
    }

    @Override
    public void logout(Long accountId) {
        // Refresh Token 삭제
        authRepository.deleteRefreshToken(accountId);
    }
}