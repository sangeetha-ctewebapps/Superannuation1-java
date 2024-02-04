package com.lic.epgs.policyservicing.policylvl.service;

import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceCommonResponseDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceDto;
import com.lic.epgs.policyservicing.policylvl.dto.freelookcancellation.FreeLookCancellationDto;
import com.lic.epgs.policyservicing.policylvl.dto.freelookcancellation.FreeLookCancellationResponseDto;
import com.lic.epgs.policyservicing.policylvl.dto.freelookcancellation.FreeLookCancellationSearchDto;

public interface PolicyFreeLookCancelService {


	PolicyServiceCommonResponseDto saveFreeLookCancellationDetails(PolicyServiceDto policyServiceDto);

	FreeLookCancellationResponseDto getFreeLookCancellationDetails(Long policyId);

	FreeLookCancellationResponseDto getFreeLookCancellationById(String status, Long policyId);

	
	PolicyResponseDto existingPolicyCitrieaSearch(PolicySearchDto freeLookCancellationSearchDto);
	
	FreeLookCancellationResponseDto existingCitrieaSearch(FreeLookCancellationSearchDto freeLookCancellationSearchDto);

	FreeLookCancellationResponseDto inprogressCitrieaSearch(FreeLookCancellationSearchDto freeLookCancellationSearchDto);

	FreeLookCancellationResponseDto changeStatus(Long policyId, String status);
	
	FreeLookCancellationResponseDto changeStatusReject(Long policyId, String status,FreeLookCancellationDto dto);

	FreeLookCancellationResponseDto freeLookCancellationApproved(Long policyId, String status);

	FreeLookCancellationResponseDto getnotesDetails(Long freeLookcancellationId);

	FreeLookCancellationResponseDto removeFLCDocument(Long documentId, Long freeLookId,String modifiedby);

	FreeLookCancellationResponseDto getFlcDocumentDetails(Long freeLookcancellationId);
	
}
