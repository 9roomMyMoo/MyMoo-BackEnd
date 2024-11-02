package com.example.mymoo.domain.account.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.example.mymoo.domain.account.dto.request.AccountCreateRequestDto;
import com.example.mymoo.domain.account.dto.response.AccountCreateResponseDto;
import com.example.mymoo.domain.account.service.AccountService;
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

    @PostMapping("/signup")
    public ResponseEntity<AccountCreateResponseDto> signup(
        @Valid @RequestBody AccountCreateRequestDto accountCreateRequestDto
    ) {
        return ResponseEntity
            .status(CREATED)
            .body(accountService.signup(accountCreateRequestDto));
    }
}
