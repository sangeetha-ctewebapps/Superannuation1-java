package com.lic.epgs.claim.service;

import java.io.IOException;
import java.io.InputStream;

import com.lic.epgs.claim.dto.CommonAnnuityDto;
import com.lic.epgs.claim.dto.ClaimCheckerActionRequestDto;
import com.lic.epgs.claim.dto.ClaimDto;
import com.lic.epgs.claim.dto.ClaimMakerActionRequestDto;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.CommonResponseDto;
import com.lic.epgs.claim.dto.SaAccountConfigMasterResponseDto;
import com.lic.epgs.claim.dto.CommonGlCodesResponseDto;



public interface ClaimService {

	ApiResponseDto<String> updateMakerAction(ClaimMakerActionRequestDto request);

	ApiResponseDto<String> updateCheckerAction(ClaimCheckerActionRequestDto request);

	boolean checkClaimStatus(String claimNo);

	ApiResponseDto<ClaimDto> findClaimDetails(String claimNo);
	
	InputStream fundSummaryDownload(String memberId,String financialYear,String variant,Integer frequency)throws IOException;

	CommonResponseDto fundSummaryGet(String memberId, String financialYear,String variant,Integer frequency);

	CommonAnnuityDto getAnnuityDataBypolicyNo(String policyNumber);
	
	SaAccountConfigMasterResponseDto saAccountConfigMasterStoredProcedure(Long claimId);
	

}