package com.example.mymoo.global.oauth.dto.response;

import lombok.Builder;

@Builder
public record KakaoLoginResponseDto(
    Long accountId,
    String accessToken,
    String refreshToken,
    boolean isNewUser
) {

}