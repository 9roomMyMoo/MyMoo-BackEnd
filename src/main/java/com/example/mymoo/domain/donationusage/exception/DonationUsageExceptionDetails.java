package com.example.mymoo.domain.donationusage.exception;

import com.example.mymoo.global.exception.ExceptionDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DonationUsageExceptionDetails implements ExceptionDetails {
    // 자신의 가게가 아닌 다른 가게의 후원을 사용하려 할 때
    FORBIDDEN_ACCESS_TO_OTHER_STORE(HttpStatus.FORBIDDEN, "해당 후원을 사용할 권한이 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}