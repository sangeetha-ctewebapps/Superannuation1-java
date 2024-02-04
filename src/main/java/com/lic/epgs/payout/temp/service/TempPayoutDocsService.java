package com.lic.epgs.payout.temp.service;

import java.util.List;

import com.lic.epgs.payout.dto.PayoutDocumentDetailDto;
import com.lic.epgs.payout.dto.PayoutDocumentStatusRequestDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface TempPayoutDocsService {

	public ApiResponseDto<PayoutDocumentDetailDto> uploadPayoutReqDoc(PayoutDocumentDetailDto docsDto);

	public ApiResponseDto<List<PayoutDocumentDetailDto>> viewPayoutReqDoc(String payoutNo);

	public ApiResponseDto<String> delete(Long documentId);

	public ApiResponseDto<String> updateStatus(PayoutDocumentStatusRequestDto requestDto);

}