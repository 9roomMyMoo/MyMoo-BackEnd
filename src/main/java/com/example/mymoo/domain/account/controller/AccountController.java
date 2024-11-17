package com.example.mymoo.domain.account.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.example.mymoo.domain.account.dto.request.AccountCreateRequestDto;
import com.example.mymoo.domain.account.dto.request.ChargePointsRequestDto;
import com.example.mymoo.domain.account.dto.response.AccountCreateResponseDto;
import com.example.mymoo.domain.account.dto.response.ChargePointsResponseDto;
import com.example.mymoo.domain.account.service.AccountService;
import com.example.mymoo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(
        summary = "[공통] 사용자 계정 생성",
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

    @Operation(
        summary = "[후원자] 포인트를 충전",
        description = "충전할 포인트를 받아, 충전 후 총 포인트를 반환합니다. 후원자 계정의 Access token을 header에 넣었는지 확인해주세요",
        responses = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
        }
    )
    @PatchMapping("/")
    @PreAuthorize("hasAuthority('DONATOR')")
    public ResponseEntity<ChargePointsResponseDto> donate(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody ChargePointsRequestDto chargePointsRequestDto
    ) {
        Long accountId = userDetails.getAccountId();

        ChargePointsResponseDto chargePointsResponseDto = accountService.chargePoints(
            accountId,
            chargePointsRequestDto
        );

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(chargePointsResponseDto);
    }
}
