package com.example.mymoo.domain.donationusage.exception;

import com.example.mymoo.global.exception.ExceptionDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DonationUsageExceptionDetails implements ExceptionDetails {
    // 찾고자 하는 donationUsage 엔티티가 테이블에 존재하지 않을 때
    DONATION_USAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 donationUsage 입니다."),
    // 자신의 가게가 아닌 다른 가게의 후원을 사용하려 할 때
    FORBIDDEN_ACCESS_TO_OTHER_STORE(HttpStatus.FORBIDDEN, "해당 후원을 사용할 권한이 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}