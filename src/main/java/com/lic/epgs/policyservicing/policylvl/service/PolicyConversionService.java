/**
 * 
 */
package com.lic.epgs.policyservicing.policylvl.service;

import java.util.List;

import com.lic.epgs.policy.dto.PolicyRulesDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceDocumentDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.CommonResponseDto;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.PolicyConversionDetailsDto;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.PolicyConversionSearchDto;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.PolicyDetailsDto;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.PolicyLevelConversionDto;

/**
 * @author Logesh.D
 *
 */
public interface PolicyConversionService {
	
	CommonResponseDto<PolicyLevelConversionDto> savePolicyConversionDetails(PolicyLevelConversionDto policyLevelConversionDto);
     
	CommonResponseDto<PolicyLevelConversionDto> sendToChecker(String conversionId,String modifiedBy);
	
	CommonResponseDto<List<PolicyLevelConversionDto>> getInprogressPolicyConversionDetailsList(String role,String unitCode);
	
	CommonResponseDto<List<PolicyLevelConversionDto>> getExistingPolicyConversionDetailsList(String role,String unitCode);

	CommonResponseDto<PolicyLevelConversionDto> approvePolicyConversion(Long conversionId, String modifiedBy);
	
	CommonResponseDto<PolicyServiceNotesDto> savePolicyConversionNotes(PolicyServiceNotesDto policyNotesDto);
	
	CommonResponseDto<List<PolicyRulesDto>> saveBenefitTypes(List<PolicyRulesDto> policyRulesDto);
	 
	CommonResponseDto<PolicyConversionDetailsDto> getInprogressOverallDetails(Long serviceId);
	
	CommonResponseDto<PolicyConversionDetailsDto> getExistingOverallDetails(Long serviceId);
	
	CommonResponseDto<List<PolicyServiceNotesDto>> getNotesDetailsList(Long serviceId);
	
	CommonResponseDto<PolicyLevelConversionDto> sendToMaker(String conversionId, String modifiedBy);
	
	CommonResponseDto<PolicyLevelConversionDto> policyConversionRejection(PolicyLevelConversionDto policyLevelConversionDto);
	
	CommonResponseDto<PolicyLevelConversionDto> getPolicyConversionDetailsBypolicyNo(String prevPolicyNo, String role);
	
	CommonResponseDto<PolicyLevelConversionDto> getExistingDetailsByNewPolicyNo(String newPolicyNo);
	
	CommonResponseDto<List<PolicyConversionSearchDto>> InProgressCommonSearch(PolicyConversionSearchDto policyConversionSearchDto);
    
	CommonResponseDto<List<PolicyConversionSearchDto>> existingCommonSearch(PolicyConversionSearchDto policyConversionSearchDto);

//	CommonResponseDto<PolicyServiceMemberTempEntity>  getMemberDetailsInConversion(String conversionId, String memberId);
//
//	CommonResponseDto<PolicyServiceMemberEntity> getMemberDetailsInConversionMaster(String conversionId, String memberId);
//
//	CommonResponseDto<List<PolicyServiceMemberTempEntity>> getMemberDetailsbyConversionIdInprocess(String conversionId);
//
//	CommonResponseDto<List<PolicyServiceMemberEntity>> getMemberDetailsbyConversionIdExisting(String conversionId);
//
//	CommonResponseDto<PolicyServiceMbrBankDto> removeMemberBankDetails(Integer bankId,Long memberId,String modifiedBy);
//
//	CommonResponseDto<PolicyServiceMbrAddressDto> removeMemberAddressDetails(Long addressId, Long memberId, String modifiedBy);

	CommonResponseDto<PolicyServiceDocumentDto> removeDocumentDetails(Long docId, Long conversionId, String modifiedBy);

	CommonResponseDto<PolicyDetailsDto> policyCriteriaSearch(String policyNo);

	CommonResponseDto<List<PolicyServiceDocumentDto>> getDocumentList(Long conversionId);

	CommonResponseDto<PolicyServiceDocumentDto> saveDocumentDetails(PolicyServiceDocumentDto policyServiceDocumentDto);



}
