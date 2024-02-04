package com.lic.epgs.claim.service;

import java.util.List;

import com.lic.epgs.claim.dto.ClaimSearchRequestDto;
import com.lic.epgs.claim.dto.ClaimSearchResponseDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface ClaimSearchService {

//	ApiResponseDto<ClaimSearchResponseDto> search(String claimNo);

	ApiResponseDto<List<ClaimSearchResponseDto>> search(ClaimSearchRequestDto request);

	ApiResponseDto<List<ClaimSearchResponseDto>> existing(ClaimSearchRequestDto request);

	ApiResponseDto<List<ClaimSearchResponseDto>> existingByClaimNo(String claimNo);

	ApiResponseDto<List<ClaimSearchResponseDto>> payoutByClaimNo(ClaimSearchRequestDto request);

}