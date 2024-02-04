package com.lic.epgs.payout.service;

import java.util.List;

import com.lic.epgs.payout.dto.PayoutSearchResponseDto;
import com.lic.epgs.payout.dto.PayoutSearchRequestDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface PayoutSearchService {

	ApiResponseDto<PayoutSearchResponseDto> search(String payoutNo);

	ApiResponseDto<List<PayoutSearchResponseDto>> search(PayoutSearchRequestDto request);

	ApiResponseDto<List<PayoutSearchResponseDto>> existing(PayoutSearchRequestDto request);

	ApiResponseDto<List<PayoutSearchResponseDto>> existingByPayoutNo(String payoutNo);

	ApiResponseDto<List<PayoutSearchResponseDto>> enquirySearch(Integer val, PayoutSearchRequestDto request);

}