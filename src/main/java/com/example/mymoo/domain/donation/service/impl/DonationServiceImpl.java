package com.example.mymoo.domain.donation.service.impl;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.account.exception.AccountException;
import com.example.mymoo.domain.account.exception.AccountExceptionDetails;
import com.example.mymoo.domain.account.repository.AccountRepository;
import com.example.mymoo.domain.donation.dto.request.DonationRequestDto;
import com.example.mymoo.domain.donation.entity.Donation;
import com.example.mymoo.domain.donation.exception.DonationException;
import com.example.mymoo.domain.donation.exception.DonationExceptionDetails;
import com.example.mymoo.domain.donation.repository.DonationRepository;
import com.example.mymoo.domain.donation.service.DonationService;
import com.example.mymoo.domain.store.entity.Store;
import com.example.mymoo.domain.store.exception.StoreException;
import com.example.mymoo.domain.store.exception.StoreExceptionDetails;
import com.example.mymoo.domain.store.repository.StoreRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

    private final AccountRepository accountRepository;
    private final StoreRepository storeRepository;
    private final DonationRepository donationRepository;

    @Override
    public void createDonation(
        final Long accountId,
        final Long storeId,
        final DonationRequestDto donationRequestDto
    ) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountException(AccountExceptionDetails.ACCOUNT_NOT_FOUND));
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new StoreException(StoreExceptionDetails.STORE_NOT_FOUND));
        Long point = donationRequestDto.point();

        // 후원자의 point 감소
        account.usePoint(point);
        // 가게의 사용 가능한 후원 금액 증가
        store.addUsableDonation(point);
        // 후원 정보 저장
        donationRepository.save(
            Donation.builder()
                .store(store)
                .account(account)
                .point(point)
                .isUsed(false)
                .build()
        );
    }

    @Override
    public void setIsUsedToTrue(
        final Long storeId,
        final Long donationId
    ) {
        Donation donation = donationRepository.findById(donationId)
            .orElseThrow(() -> new DonationException(DonationExceptionDetails.DONATION_NOT_FOUND));
        Store store = donation.getStore();
        // 자신의 가게가 아닌 다른 가게의 후원을 사용하려 할 때
        if (!Objects.equals(storeId, store.getId())){
            throw new DonationException(DonationExceptionDetails.FORBIDDEN_ACCESS_TO_OTHER_STORE);
        }
        // 사용 여부 업데이트
        donation.setIsUsedToTrue();
        // 사용 가능한 후원 금액 감소
        store.useUsableDonation(donation.getPoint());
        // store 계정의 point 증가. 향후 현금으로 바꿀 수 있음
        store.getAccount().chargePoint(donation.getPoint());
    }
}
