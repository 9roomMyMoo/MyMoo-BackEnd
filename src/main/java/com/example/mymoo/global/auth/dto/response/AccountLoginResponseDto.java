package com.example.mymoo.global.auth.dto.response;

import lombok.Builder;

@Builder
public record AccountLoginResponseDto(
    Long accountId,
    String accessToken,
    String refreshToken
) {

}
