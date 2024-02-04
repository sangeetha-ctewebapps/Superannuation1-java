package com.lic.epgs.surrender.service;

import com.lic.epgs.common.dto.CommonDocsDto;
import com.lic.epgs.common.dto.CommonNotesDto;
import com.lic.epgs.surrender.dto.CommonResponseDto;
import com.lic.epgs.surrender.dto.PolicySurrenderApiResponse;
import com.lic.epgs.surrender.dto.PolicySurrenderDto;
import com.lic.epgs.surrender.dto.PolicySurrenderPayOutDto;

public interface PolicySurrenderService {

	
	PolicySurrenderApiResponse saveAndUpdatePolicySurrender(PolicySurrenderDto policySurrenderDto);

	PolicySurrenderApiResponse sendToMakerChecker(PolicySurrenderDto policySurrenderDto);
	
	PolicySurrenderApiResponse approvePolicySurrender(PolicySurrenderDto policySurrednerDto);
	
	PolicySurrenderApiResponse rejectPolicySurrender(PolicySurrenderDto policySurrednerDto);

	PolicySurrenderApiResponse uploadDocument(CommonDocsDto docsDto);

	PolicySurrenderApiResponse saveNotesDetails(CommonNotesDto commonNotesDto);

	PolicySurrenderApiResponse inprogresOrExitingSurrender(String roleType, String inprogress);

	PolicySurrenderApiResponse getPolicySurrenderbySurrenderId(Long surrenderId);

	PolicySurrenderApiResponse savePayoutDetails(PolicySurrenderPayOutDto policySurrenderPayOutDto);

	PolicySurrenderApiResponse editPayoutDetails(PolicySurrenderPayOutDto policySurrenderPayOutDto);

	PolicySurrenderApiResponse getPolicySurrenderBasicDetailsById(Long surrenderId);

	CommonResponseDto removeDocumentDetails(Long surrenderId, Integer docId, String modifiedBy);

	CommonResponseDto getSurrenderNotesList(Long surrenderId);
	
}
