package com.lic.epgs.payout.temp.service;

import com.lic.epgs.payout.dto.PayoutAnnuityCalcDto;
import com.lic.epgs.payout.dto.PayoutCommutationCalcDto;
import com.lic.epgs.payout.dto.PayoutFundValueDto;
import com.lic.epgs.payout.dto.PayoutMbrFundValueDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface TempPayoutCalcService {

	ApiResponseDto<PayoutFundValueDto> saveFundvalue(PayoutFundValueDto request);

	ApiResponseDto<PayoutMbrFundValueDto> saveMbrFundvalue(PayoutMbrFundValueDto request);

	ApiResponseDto<PayoutCommutationCalcDto> saveCommutation(PayoutCommutationCalcDto request);

	ApiResponseDto<PayoutAnnuityCalcDto> saveAnuity(PayoutAnnuityCalcDto request);

}