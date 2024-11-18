package com.example.mymoo.domain.donation.dto.response;

import com.example.mymoo.domain.donation.entity.Donation;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record ReadAccountDonationListResponseDto(
    List<DonationDto> donations,
    boolean hasNext,
    int numberOfElements,
    int pageNumber,
    int pageSize
) {

    public static ReadAccountDonationListResponseDto from(Slice<Donation> donationSlice) {
        List<DonationDto> donationDtos = donationSlice.getContent()
            .stream()
            .map(DonationDto::from)
            .toList();

        return ReadAccountDonationListResponseDto.builder()
            .donations(donationDtos)
            .hasNext(donationSlice.hasNext())
            .numberOfElements(donationSlice.getNumberOfElements())
            .pageNumber(donationSlice.getNumber())
            .pageSize(donationSlice.getSize())
            .build();
    }
    @Builder
    public record DonationDto(
        Long point,
        String store,
        Boolean isUsed,
        LocalDateTime createdAt
    ) {
        public static DonationDto from(Donation donation) {
            return DonationDto.builder()
                .point(donation.getPoint())
                .store(donation.getStore().getName())
                .isUsed(donation.getIsUsed())
                .createdAt(donation.getCreatedAt())
                .build();
        }
    }
}