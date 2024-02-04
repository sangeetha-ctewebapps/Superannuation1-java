package com.lic.epgs.claim.temp.service;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.lic.epgs.claim.dto.ClaimMemberSearchResponseDto;
import com.lic.epgs.claim.dto.PolicyMemberSearchRequestDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface TempClaimMemberSearchService {

	public ApiResponseDto<List<ClaimMemberSearchResponseDto>> memberSearch(
			@RequestBody PolicyMemberSearchRequestDto request);

	public ApiResponseDto<List<ClaimMemberSearchResponseDto>> memberShipNoSearch(@PathVariable String memberShipNo);

}