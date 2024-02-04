package com.lic.epgs.payout.temp.service;

import com.lic.epgs.payout.dto.PayoutCheckerActionRequestDto;
import com.lic.epgs.payout.dto.PayoutDto;
import com.lic.epgs.payout.dto.PayoutMakerActionRequestDto;
import com.lic.epgs.payout.dto.PayoutOnboardingRequestDto;
import com.lic.epgs.payout.dto.PayoutOnboardingResponseDto;
import com.lic.epgs.payout.dto.PayoutUpdateRequestDto;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;

import java.util.Optional;

import com.lic.epgs.common.dto.ApiResponseDto;

public interface TempPayoutService {

	public ApiResponseDto<PayoutOnboardingResponseDto> onboard(PayoutOnboardingRequestDto request);

	public ApiResponseDto<String> updateMakerAction(PayoutMakerActionRequestDto request);

	public ApiResponseDto<String> updateCheckerAction(PayoutCheckerActionRequestDto request);

	public ApiResponseDto<PayoutDto> update(PayoutUpdateRequestDto request);

	public ApiResponseDto<PayoutDto> findPayoutDetails(String initiMationNo);

}