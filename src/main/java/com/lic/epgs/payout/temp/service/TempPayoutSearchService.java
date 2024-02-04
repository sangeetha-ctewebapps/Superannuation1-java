package com.lic.epgs.payout.temp.service;

import java.util.List;

import com.lic.epgs.payout.dto.PayoutSearchRequestDto;
import com.lic.epgs.payout.dto.PayoutSearchResponseDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface TempPayoutSearchService {

	ApiResponseDto<PayoutSearchResponseDto> search(String payoutNo);

	ApiResponseDto<List<PayoutSearchResponseDto>> search(PayoutSearchRequestDto request);

	ApiResponseDto<List<PayoutSearchResponseDto>> findByStatusAndPayoutNo(Integer status, String payoutNo);

	ApiResponseDto<List<PayoutSearchResponseDto>> inprogress(PayoutSearchRequestDto request);

	ApiResponseDto<List<PayoutSearchResponseDto>> inprogressByPayoutNo(String payoutNo);

}