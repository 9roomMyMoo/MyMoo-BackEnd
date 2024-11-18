package com.example.mymoo.domain.donation.service;

import com.example.mymoo.domain.donation.dto.request.DonationRequestDto;
import com.example.mymoo.domain.donation.dto.response.ReadAccountDonationListResponseDto;
import com.example.mymoo.domain.donation.dto.response.ReadStoreDonationListResponseDto;
import org.springframework.data.domain.Pageable;

public interface DonationService {
    void createDonation(
        final Long accountId,
        final Long storeId,
        final DonationRequestDto donationRequestDto
    );

    ReadStoreDonationListResponseDto getStoreDonationList(
        final Long storeId,
        final Pageable pageable
    );

    ReadAccountDonationListResponseDto getAccountDonationList(
        final Long accountId,
        final Integer limit,
        final Pageable pageable
    );
}
