package com.example.mymoo.domain.child.service.Impl;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.account.exception.AccountException;
import com.example.mymoo.domain.account.exception.AccountExceptionDetails;
import com.example.mymoo.domain.account.repository.AccountRepository;
import com.example.mymoo.domain.child.dto.request.ChildReqeustDTO;
import com.example.mymoo.domain.child.entity.Child;
import com.example.mymoo.domain.child.exception.ChildException;
import com.example.mymoo.domain.child.exception.ChildExceptionDetails;
import com.example.mymoo.domain.child.repository.ChildRepository;
import com.example.mymoo.domain.child.service.ChildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private ChildRepository childRepository;
    private AccountRepository accountRepository;

    public Child createChild(ChildReqeustDTO request){
        Account foundAcccount = accountRepository.findById(request.getAccountId())
                .orElseThrow(()->new AccountException(AccountExceptionDetails.ACCOUNT_NOT_FOUND));

        childRepository.findByAccountId(request.getAccountId())
                .ifPresent(child -> {new ChildException(ChildExceptionDetails.CHILD_ALREADY_EXISTS);});

        return childRepository.save(
                Child.builder()
                        .account(foundAcccount)
                        .cardNumber(request.getCardNumber())
                        .build()
        );
    }
}
