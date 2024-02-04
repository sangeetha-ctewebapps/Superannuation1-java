package com.lic.epgs.payout.service;

import java.util.List;
import java.util.Set;

import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.payout.dto.PayoutDto;
import com.lic.epgs.payout.dto.PayoutNotesDto;
import com.lic.epgs.payout.dto.PayoutPayeeBankDetailsDto;
import com.lic.epgs.payout.dto.PayoutSearchRequestDto;
import com.lic.epgs.payout.dto.PayoutSearchResponseDto;
import com.lic.epgs.payout.dto.ReinitiateResponse;
import com.lic.epgs.payout.temp.entity.PayoutPayeeBankDetailsTempEntity;

public interface PayoutNeftRejectService {
	
//	boolean checkPayoutStatus(String payoutNo);

	ApiResponseDto<Set<PayoutNotesDto>> add(PayoutNotesDto payoutNotesDto);

	ApiResponseDto<Set<PayoutNotesDto>> getNotesByPayoutNo(String payoutNo);

	ApiResponseDto<Set<PayoutNotesDto>> addTempNotes(PayoutNotesDto payoutNotesDto);

	ApiResponseDto<Set<PayoutNotesDto>> getTempNotesByPayoutNo(String payoutNo);


	ApiResponseDto<List<PayoutSearchResponseDto>> existingfindPayoutNeftDetails(PayoutSearchRequestDto request);

	ApiResponseDto<List<PayoutSearchResponseDto>> search(PayoutSearchRequestDto request);

	ApiResponseDto<PayoutDto> findTempPayoutDetails(String payoutNo);

	ApiResponseDto<PayoutDto> findPayoutDetails(String payoutNo);

	ApiResponseDto<PayoutPayeeBankDetailsDto> editTempPayoutPayeeBankValueDetails(String payoutNo,PayoutPayeeBankDetailsTempEntity entity);

	ApiResponseDto<PayoutDto> sentToChecker(String payoutNo);

	ApiResponseDto<PayoutDto> sentToMaker(String payoutNo);

	ApiResponseDto<PayoutDto> sentToReject(String payoutNo);

	ApiResponseDto<List<PayoutSearchResponseDto>> inprogress(PayoutSearchRequestDto request);

	ApiResponseDto<List<PayoutSearchResponseDto>> inprogressByPayoutNo(String payoutNo);

	ApiResponseDto<List<PayoutSearchResponseDto>> searchTemp(PayoutSearchRequestDto request);

	ApiResponseDto<List<PayoutSearchResponseDto>> existing(PayoutSearchRequestDto request);

	ApiResponseDto<List<PayoutSearchResponseDto>> existingByPayoutNo(String payoutNo);

	ApiResponseDto<List<PayoutSearchResponseDto>> searchMain(PayoutSearchRequestDto request);

	ApiResponseDto<PayoutDto> sentToApprove(String payoutNo);
	
	ReinitiateResponse reinitiateStoredProcedure(Long payoutId,String beneficiaryPaymentId,String reinitiateBeneficiaryPaymentId);

}
