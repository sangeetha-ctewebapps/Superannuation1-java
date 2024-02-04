package com.lic.epgs.claim.temp.service;

import com.lic.epgs.claim.dto.ClaimAnnuityCalcDto;
import com.lic.epgs.claim.dto.ClaimCheckerActionRequestDto;
import com.lic.epgs.claim.dto.ClaimDto;
import com.lic.epgs.claim.dto.ClaimMakerActionRequestDto;
import com.lic.epgs.claim.dto.ClaimOnboardingDto;
import com.lic.epgs.claim.dto.ClaimOnboardingRequestDto;
import com.lic.epgs.claim.dto.ClaimOnboardingResponseDto;
import com.lic.epgs.claim.dto.ClaimUpdateRequestDto;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.CommonResponseDto;

public interface TempClaimService {

	public ApiResponseDto<ClaimOnboardingResponseDto> onboard(ClaimOnboardingRequestDto request);

	public ApiResponseDto<String> updateMakerAction(ClaimMakerActionRequestDto request);

	public ApiResponseDto<String> updateCheckerAction(ClaimCheckerActionRequestDto request);

	public ApiResponseDto<ClaimDto> update(ClaimUpdateRequestDto request);

	public ApiResponseDto<ClaimDto> findClaimDetails(String claimNo);

	public ApiResponseDto<ClaimOnboardingDto> getClaimOnboardDetails(String claimNo);

	public ApiResponseDto<ClaimAnnuityCalcDto> gstAnnutityCalculate(ClaimAnnuityCalcDto claimAnnuityCalcDto);
	
	public CommonResponseDto rejectClaimOnBoarding(String claimNo);


}