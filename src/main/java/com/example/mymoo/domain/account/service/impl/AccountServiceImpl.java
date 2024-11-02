package com.example.mymoo.domain.account.service.impl;

import com.example.mymoo.domain.account.dto.request.AccountCreateRequestDto;
import com.example.mymoo.domain.account.dto.response.AccountCreateResponseDto;
import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.account.repository.AccountRepository;
import com.example.mymoo.domain.account.service.AccountService;
import com.example.mymoo.global.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    @Override
    public AccountCreateResponseDto signup(final AccountCreateRequestDto accountCreateRequestDto) {
        // 사용 가능한 이메일인지 확인
        if (accountRepository.existsByEmail(accountCreateRequestDto.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Account savedAccount = accountRepository.save(
            Account.builder()
                .email(accountCreateRequestDto.email())
                .password(passwordEncoder.encode(accountCreateRequestDto.password()))
                .phoneNumber(accountCreateRequestDto.phoneNumber())
                .role(UserRole.valueOf(accountCreateRequestDto.userRole()))
                .build()
        );

        return AccountCreateResponseDto.builder()
            .accountId(savedAccount.getId())
            .build();
    }
}
