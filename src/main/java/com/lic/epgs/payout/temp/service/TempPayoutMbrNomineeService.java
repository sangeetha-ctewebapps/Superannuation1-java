package com.lic.epgs.payout.temp.service;

import java.util.List;

import com.lic.epgs.payout.dto.PayoutMbrNomineeDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface TempPayoutMbrNomineeService {

	ApiResponseDto<PayoutMbrNomineeDto> save(PayoutMbrNomineeDto docsDto);

	ApiResponseDto<List<PayoutMbrNomineeDto>> findNomineeByPayoutNo(String payoutNo);

}