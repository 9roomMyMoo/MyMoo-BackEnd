package com.example.mymoo.global.oauth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfo(
    Long id,
    @JsonProperty("connected_at") String connectedAt,
    @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {
    public record KakaoAccount(
        @JsonProperty("email_needs_agreement") Boolean emailNeedsAgreement,
        @JsonProperty("is_email_valid") Boolean isEmailValid,
        @JsonProperty("is_email_verified") Boolean isEmailVerified,
        String email
    ) {}
}
