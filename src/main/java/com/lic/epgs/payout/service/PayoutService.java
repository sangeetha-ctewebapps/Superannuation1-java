package com.lic.epgs.payout.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.payout.dto.AnnuityLedgerDto;
import com.lic.epgs.payout.dto.PayoutCheckerActionRequestDto;
import com.lic.epgs.payout.dto.PayoutDto;
import com.lic.epgs.payout.dto.PayoutMakerActionRequestDto;
import com.lic.epgs.payout.dto.PayoutStoredProcedureResponseDto;




public interface PayoutService {

	ApiResponseDto<String> updateMakerAction(PayoutMakerActionRequestDto request);

	ApiResponseDto<String> updateCheckerAction(PayoutCheckerActionRequestDto request);

	boolean checkPayoutStatus(String payoutNo);

	ApiResponseDto<PayoutDto> findPayoutDetails(String initiMationNo, String payoutNo);

	ApiResponseDto<String> approvedAndRejectAction(PayoutCheckerActionRequestDto request) throws ApplicationException, JsonMappingException, JsonProcessingException;
	
	ApiResponseDto<List<PayoutStoredProcedureResponseDto>> storeProcedureforAccount(Long payoutId);
	
	ApiResponseDto<Object> getPaymentStatus(Long payoutId, String unitCode);
	
	AnnuityLedgerDto getAnnuityLedgerDetails(String payoutNo);

	ApiResponseDto<String> fundDebitRecall(PayoutCheckerActionRequestDto request);

	ApiResponseDto<String> sentToIntimationMaker(PayoutCheckerActionRequestDto request);

	
}