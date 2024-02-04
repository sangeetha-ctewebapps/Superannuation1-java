package com.lic.epgs.claim.temp.service;

import java.util.List;

import com.lic.epgs.claim.dto.ClaimDocumentDetailDto;
import com.lic.epgs.claim.dto.ClaimDocumentStatusRequestDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface TempClaimDocsService {

	public ApiResponseDto<ClaimDocumentDetailDto> uploadClaimReqDoc(ClaimDocumentDetailDto docsDto);

	public ApiResponseDto<List<ClaimDocumentDetailDto>> viewClaimReqDoc(String claimNo);

	public ApiResponseDto<String> delete(Long documentId);

	public ApiResponseDto<String> updateStatus(ClaimDocumentStatusRequestDto requestDto);

}