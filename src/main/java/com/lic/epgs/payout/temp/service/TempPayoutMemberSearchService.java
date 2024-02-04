package com.lic.epgs.payout.temp.service;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.lic.epgs.payout.dto.PolicyMemberSearchRequestDto;
import com.lic.epgs.payout.dto.PayoutMemberSearchResponseDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface TempPayoutMemberSearchService {

	public ApiResponseDto<List<PayoutMemberSearchResponseDto>> memberSearch(
			@RequestBody PolicyMemberSearchRequestDto request);

	public ApiResponseDto<List<PayoutMemberSearchResponseDto>> memberShipNoSearch(@PathVariable String memberShipNo);

}