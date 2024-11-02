package com.example.mymoo.domain.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AccountCreateRequestDto(
    @NotBlank(message = "이메일은 필수 항목입니다")
    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
        message = "이메일 형식을 지켜주세요"
    )
    String email,

    @NotBlank(message = "비밀번호는 필수 항목입니다")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$",
        message = "비밀번호는 8자 이상이어야 하며, 숫자, 영문자, 특수문자를 포함해야 합니다"
    )
    String password,

    @NotBlank(message = "전화번호는 필수 항목입니다")
    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10~11자리의 숫자여야 합니다")
    String phoneNumber,

    @NotBlank(message = "사용자 역할은 필수 항목입니다")
    @Pattern(
        regexp = "^(DONATOR|CHILD|STORE|ADMIN)$",
        message = "DONATOR, CHILD, STORE, ADMIN 중 하나를 입력해주세요"
    )
    String userRole
) {

}
