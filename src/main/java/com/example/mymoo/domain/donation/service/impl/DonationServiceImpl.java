package com.example.mymoo.domain.donation.service.impl;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.account.exception.AccountException;
import com.example.mymoo.domain.account.exception.AccountExceptionDetails;
import com.example.mymoo.domain.account.repository.AccountRepository;
import com.example.mymoo.domain.donation.dto.request.DonationRequestDto;
import com.example.mymoo.domain.donation.dto.response.ReadAccountDonationListResponseDto;
import com.example.mymoo.domain.donation.dto.response.ReadStoreDonationListResponseDto;
import com.example.mymoo.domain.donation.entity.Donation;
import com.example.mymoo.domain.donation.exception.DonationException;
import com.example.mymoo.domain.donation.exception.DonationExceptionDetails;
import com.example.mymoo.domain.donation.repository.DonationRepository;
import com.example.mymoo.domain.donation.service.DonationService;
import com.example.mymoo.domain.store.entity.Store;
import com.example.mymoo.domain.store.exception.StoreException;
import com.example.mymoo.domain.store.exception.StoreExceptionDetails;
import com.example.mymoo.domain.store.repository.StoreRepository;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    public ReadStoreDonationListResponseDto getStoreDonationList(
        final Long storeId,
        final Pageable pageable
    ) {
        // 해당 storeId가 없는 경우
        if (!storeRepository.existsById(storeId)){
            throw new StoreException(StoreExceptionDetails.STORE_NOT_FOUND);
        }
        return ReadStoreDonationListResponseDto.from(
            donationRepository.findAllByStore_IdAndIsUsedFalse(storeId, pageable)
        );
    }

    @Override
    public ReadAccountDonationListResponseDto getAccountDonationList(
        final Long accountId,
        final Integer limit,
        final Pageable pageable
    ) {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountException(AccountExceptionDetails.ACCOUNT_NOT_FOUND);
        }

        Slice<Donation> donations = (limit == null) ?
            donationRepository.findAllByAccount_Id(
                accountId,
                pageable
            )
            : donationRepository.findRecentDonationsByAccountId(
                accountId,
                LocalDateTime.now().minusMonths(limit),
                pageable
            );

        return ReadAccountDonationListResponseDto.from(donations);
    }
}
