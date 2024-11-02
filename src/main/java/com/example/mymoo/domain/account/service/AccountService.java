package com.example.mymoo.domain.account.service;

import com.example.mymoo.domain.account.dto.request.AccountCreateRequestDto;
import com.example.mymoo.domain.account.dto.response.AccountCreateResponseDto;

public interface AccountService {
    AccountCreateResponseDto signup(AccountCreateRequestDto accountCreateRequestDto);
}
