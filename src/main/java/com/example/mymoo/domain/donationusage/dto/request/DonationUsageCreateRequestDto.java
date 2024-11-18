package com.example.mymoo.domain.donationusage.dto.request;

import jakarta.validation.constraints.NotNull;

public record DonationUsageCreateRequestDto(
    @NotNull(message = "donationId는 필수 입력 항목입니다.")
    Long donationId,
    @NotNull(message = "childId는 필수 입력 항목입니다.")
    Long childId
) {

}
