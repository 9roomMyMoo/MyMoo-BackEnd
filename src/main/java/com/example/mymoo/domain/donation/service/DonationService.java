package com.example.mymoo.domain.donation.service;

import com.example.mymoo.domain.donation.dto.request.DonationRequestDto;

public interface DonationService {
    void createDonation(
        final Long accountId,
        final Long storeId,
        final DonationRequestDto donationRequestDto
    );

    void setIsUsedToTrue(
        final Long storeId,
        final Long donationId
    );
}
