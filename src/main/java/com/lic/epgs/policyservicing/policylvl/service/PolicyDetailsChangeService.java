package com.lic.epgs.policyservicing.policylvl.service;

import com.lic.epgs.policy.dto.MphAddressDto;
import com.lic.epgs.policy.dto.MphBankDto;
import com.lic.epgs.policy.dto.PolicyRulesDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.old.dto.PolicyAddressOldDto;
import com.lic.epgs.policy.old.dto.PolicyBankOldDto;
import com.lic.epgs.policy.old.dto.PolicyDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policy.old.dto.PolicyRulesOldDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;
import com.lic.epgs.policyservicing.policylvl.dto.policydetailschange.PolicyDetailsChangeDto;
import com.lic.epgs.policyservicing.policylvl.dto.policydetailschange.PolicyDtlsResponseDto;

public interface PolicyDetailsChangeService {

PolicyDtlsResponseDto editPolicyDetails(PolicyDto policyDto);

PolicyDtlsResponseDto changeStatus(Long policyDtlsId, String checkerNo) ;
	
PolicyDtlsResponseDto policyApproved(Long policyDtlsId);

PolicyDtlsResponseDto savePolicyDetailsChange(PolicyDetailsChangeDto policyDtlsDto);

PolicyDtlsResponseDto saveAddressDetails(PolicyAddressOldDto addressDto);

PolicyDtlsResponseDto saveNotesDetails(PolicyServiceNotesDto policyServiceNotesDto);

PolicyDtlsResponseDto getNoteList(Long policyId, Long serviceId);

PolicyDtlsResponseDto inprogressCitrieaSearch(PolicySearchDto policySearchDto);

PolicyDtlsResponseDto saveBankDetails(PolicyBankOldDto policyBankDto);

PolicyDtlsResponseDto getBankList(Long mphId);

PolicyDtlsResponseDto removeBankDetails(Long policyId, Long bankAccountId);

PolicyDtlsResponseDto getPolicyById(String inprogress, Long policyDtlsId);

PolicyDtlsResponseDto saveRulesDetails(PolicyRulesOldDto saveRulesDetails);

PolicyDtlsResponseDto existingCitrieaSearch(PolicySearchDto policySearchDto);

PolicyResponseDto newcitrieaSearch(PolicySearchDto policySearchDto);

PolicyDtlsResponseDto sendToReject(Long policyDtlsDto, String rejectedNo, String rejectionRemarks, String rejectionCode);

PolicyDtlsResponseDto newcitrieaSearchById(String policyNumber);

PolicyDtlsResponseDto getAddressList(Long mphId);



}
