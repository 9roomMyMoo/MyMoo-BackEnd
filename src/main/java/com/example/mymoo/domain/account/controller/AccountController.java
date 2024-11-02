package com.example.mymoo.domain.account.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.example.mymoo.domain.account.dto.request.AccountCreateRequestDto;
import com.example.mymoo.domain.account.dto.response.AccountCreateResponseDto;
import com.example.mymoo.domain.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(
        summary = "사용자 계정 생성",
        description = "새로운 사용자 계정을 생성합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = "계정 생성 성공"),
        }
    )
    @PostMapping("/signup")
    public ResponseEntity<AccountCreateResponseDto> signup(
        @Valid @RequestBody AccountCreateRequestDto accountCreateRequestDto
    ) {
        return ResponseEntity
            .status(CREATED)
            .body(accountService.signup(accountCreateRequestDto));
    }
}
