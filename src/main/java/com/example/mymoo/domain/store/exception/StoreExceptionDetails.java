package com.example.mymoo.domain.store.exception;

import com.example.mymoo.global.exception.ExceptionDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StoreExceptionDetails implements ExceptionDetails {
    // 가게 id가 store 테이블에 존재하지 않을 때
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 가게 id입니다."),
    NOT_ENOUGH_STORE_POINT(HttpStatus.NOT_FOUND, "후원된 포인트가 부족합니다."),
    QUERY_PARAMETER_INVALID(HttpStatus.BAD_REQUEST, "현재 위치 위도와 경도를 넘겨주세요"),
    ;

    private final HttpStatus status;
    private final String message;
}

