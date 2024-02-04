package com.lic.epgs.payout.temp.service;

import java.util.List;

import com.lic.epgs.payout.dto.PayoutMbrAppointeeDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface TempPayoutMbrAppointeeService {

	ApiResponseDto<PayoutMbrAppointeeDto> save(PayoutMbrAppointeeDto docsDto);

	ApiResponseDto<List<PayoutMbrAppointeeDto>> findAppointeeByPayoutNo(String payoutNo);

}