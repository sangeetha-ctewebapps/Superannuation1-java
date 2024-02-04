package com.lic.epgs.regularadjustmentcontribution.service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author pradeepramesh
 *
 */

import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionResponseDto;
import com.lic.epgs.policy.dto.PolicyFrequencyDetailsDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentResponse;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularACSaveAdjustmentRequestDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionNotesDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionResponseDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentSearchDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionBatchHistoryResponse;

public interface RegularadjustmentService {

	PolicyResponseDto newcitrieaSearchPradeep(PolicySearchDto policySearchDto);

	AdjustmentContributionResponseDto newcitrieaSearchById(Long mphId, Long policyId);

	RegularAdjustmentContributionResponseDto saveAdjustment(RegularACSaveAdjustmentRequestDto adjustmentDto);

	RegularAdjustmentContributionResponseDto save(RegularAdjustmentContributionDto request);

	RegularAdjustmentContributionResponseDto notesave(RegularAdjustmentContributionNotesDto request);

	RegularAdjustmentContributionResponseDto getNotesList(Long regularContributionId);

	RegularAdjustmentContributionResponseDto changeStatus(Long regularContributionId,String adjustmentContributionStatus, String modifiedBy);

	RegularAdjustmentContributionResponseDto approve(Long regularContributionId, String adjustmentContributionStatus,String modifiedBy, String variantType);

	RegularAdjustmentContributionResponseDto reject(Long regularContributionId, String adjustmentContributionStatus,String modifiedBy);

	RegularAdjustmentContributionResponseDto getInprogressLoad(RegularAdjustmentSearchDto request);

	RegularAdjustmentContributionResponseDto getInprogressDetails(Long regularContributionId);

	RegularAdjustmentContributionResponseDto getExisitngLoad(RegularAdjustmentSearchDto request);

	RegularAdjustmentContributionResponseDto getExistingDetails(Long regularContributionId);

	RegularAdjustmentContributionResponseDto getFrequencyDates(PolicyFrequencyDetailsDto request) throws Exception;

	AdjustmentContributionResponseDto getFrequencyByPolicyId(Long policyId);

	PolicyResponseDto newcitrieaSearch(PolicySearchDto policySearchDto);

	PolicyAdjustmentResponse saveAdjustmentOldDto(Long policyId);

	void download(Long batchId, String fileType, HttpServletResponse response);
	
	RegularAdjustmentContributionBatchHistoryResponse getBatchHistory(Long regularContributionId);
	
	RegularAdjustmentContributionResponseDto makerReject(Long regularContributionId, String modifiedBy,
			String rejectionRemarks);
	
}
