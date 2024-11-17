package com.example.mymoo.domain.donation.controller;

import com.example.mymoo.domain.donation.dto.request.DonationRequestDto;
import com.example.mymoo.domain.donation.service.DonationService;
import com.example.mymoo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;
    @Operation(
        summary = "[후원자] 음식점에 포인트 후원",
        description = "후원자 계정의 Access token을 header에 넣었는지 확인해주세요",
        responses = {
            @ApiResponse(responseCode = "201", description = "후원 성공"),
        }
    )
    @PostMapping("/stores/{storeId}")
    @PreAuthorize("hasAuthority('DONATOR')")
    public ResponseEntity<Void> donate(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long storeId,
        @Valid @RequestBody DonationRequestDto donationRequestDto
    ) {
        Long accountId = userDetails.getAccountId();

        donationService.createDonation(
            accountId,
            storeId,
            donationRequestDto
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @Operation(
        summary = "[음식점] 특정 후원을 사용 처리",
        description = "음식점 계정에서 QR코드 인식 시 해당 API를 호출합니다. 음식점 계정의 Access token을 header에 넣었는지 확인해주세요",
        responses = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
        }
    )
    @PatchMapping("/{donationId}")
    @PreAuthorize("hasAuthority('STORE')")
    public ResponseEntity<Void> setIsUsedToTrue(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long donationId
    ) {
        Long storeId = userDetails.getAccountId();

        // TODO - 알림 기능 추가

        donationService.setIsUsedToTrue(
            storeId,
            donationId
        );

        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }
}
