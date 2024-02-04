package com.lic.epgs.adjustmentcontribution.service;


import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author pradeepramesh
 *
 */
import javax.servlet.http.HttpServletResponse;

import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionNotesDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionResponseDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentSearchDto;
import com.lic.epgs.common.integration.dto.CommonIntegrationDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentResponse;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionBatchHistoryResponse;

public interface AdjustmentContributionService {

	PolicyResponseDto newcitrieaSearch(PolicySearchDto policySearchDto);
	PolicyResponseDto newcitrieaSearchPradeep(PolicySearchDto policySearchDto);
	
	AdjustmentContributionResponseDto newcitrieaSearchById(Long mphId, Long policyId);
	AdjustmentContributionResponseDto saveMasterTemp(CommonIntegrationDto dto);

	AdjustmentContributionResponseDto save(AdjustmentContributionDto request);
	AdjustmentContributionResponseDto notesave(AdjustmentContributionNotesDto request);
	AdjustmentContributionResponseDto getNotesList(Long adjustmentContributionId);

	AdjustmentContributionResponseDto changeStatus(Long adjustmentContributionId, String adjustmentContributionStatus, String role);
	AdjustmentContributionResponseDto reject(Long adjustmentContributionId, String adjustmentContributionStatus, String role ,String rejectionRemarks,String rejectionReasonCode);

	AdjustmentContributionResponseDto getInprogressLoad(AdjustmentSearchDto request);
	AdjustmentContributionResponseDto getInprogressDetails(Long adjustmentContributionId);

	AdjustmentContributionResponseDto getExisitngLoad(AdjustmentSearchDto request);
	AdjustmentContributionResponseDto getExistingDetails(Long adjustmentContributionId);

	void download(Long batchId, String fileType, HttpServletResponse response);

	PolicyAdjustmentResponse saveAdjustmentOldDto(Long policyId);
	
	ByteArrayInputStream policyDepositAdjustment(String policyNumber, String unitId, String receivedFrom ,String receivedTo,String adjustedFrom, String adjustedTo)throws IOException;

	AdjustmentContributionResponseDto newcitrieaSearchById(String policyNumber, String unitId, String receivedFrom ,String receivedTo,String adjustedFrom, String adjustedTo);

	AdjustmentContributionBatchHistoryResponse getBatchHistory(Long adjustmentId);
	
	AdjustmentContributionResponseDto makerReject(Long adjustmentContributionId,
			String modifiedBy, String rejectionRemarks);
}