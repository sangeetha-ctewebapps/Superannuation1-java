package com.lic.epgs.claim.temp.service;

import java.util.List;

import com.lic.epgs.claim.dto.ClaimSearchRequestDto;
import com.lic.epgs.claim.dto.ClaimSearchResponseDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface TempClaimSearchService {

	ApiResponseDto<ClaimSearchResponseDto> search(String claimNo);

	ApiResponseDto<List<ClaimSearchResponseDto>> search(ClaimSearchRequestDto request);

	ApiResponseDto<List<ClaimSearchResponseDto>> findByStatusAndClaimNo(Integer status, String claimNo);

	ApiResponseDto<List<ClaimSearchResponseDto>> inprogress(ClaimSearchRequestDto request);

	ApiResponseDto<List<ClaimSearchResponseDto>> inprogressByClaimNo(String claimNo);

}