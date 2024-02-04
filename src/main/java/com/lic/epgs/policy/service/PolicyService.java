package com.lic.epgs.policy.service;

import java.io.ByteArrayInputStream;

/**
 * @author pradeepramesh
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionResponseDto;
import com.lic.epgs.common.dto.DataTable;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.policy.dto.CommonResponseStampDto;
import com.lic.epgs.policy.dto.ContributionRequestDto;
import com.lic.epgs.policy.dto.DepositRefundPolicySearchRequestDto;
import com.lic.epgs.policy.dto.GetPolicyMemberDetailsRequestDto;
import com.lic.epgs.policy.dto.IssuePolicyResponseDto;
import com.lic.epgs.policy.dto.PolicyDetailSearchRequestDto;
import com.lic.epgs.policy.dto.PolicyDetailSearchResponseDto;
import com.lic.epgs.policy.dto.PolicyDetailsResponseDto;
import com.lic.epgs.policy.dto.PolicyFrequencyDetailsDto;
import com.lic.epgs.policy.dto.PolicyNotesDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.dto.ProposalAnnuityDto;
import com.lic.epgs.policy.dto.StampDutyConsumptionRequestDto;
import com.lic.epgs.policy.dto.TrnRegistrationResponseDto;
import com.lic.epgs.policy.old.dto.PolicyAddressOldDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentRequestDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentResponse;
import com.lic.epgs.policy.old.dto.PolicyBankOldDto;
import com.lic.epgs.policy.old.dto.PolicyDto;
import com.lic.epgs.policy.old.dto.PolicyFundStmtRequestDto;
import com.lic.epgs.policy.old.dto.PolicyFundStmtResponseDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionResponseDto;

public interface PolicyService {

	PolicyResponseDto savePolicyDetails(PolicyDto policyDto) throws ParseException;

	PolicyResponseDto savePolicyOldDto(Long mphId);

	PolicyResponseDto saveNotesDetails(PolicyNotesDto policyNotesDto);

	PolicyResponseDto getNoteList(Long policyId);

	PolicyResponseDto saveAddressDetails(PolicyAddressOldDto addressDto);

	PolicyResponseDto getAddressList(Long mphId);

	PolicyResponseDto saveBankDetails(PolicyBankOldDto bankDto);

	PolicyResponseDto getBankList(Long mphId);

	PolicyResponseDto getPolicyById(String status, Long mphId);

	PolicyResponseDto existingCitrieaSearch(PolicySearchDto policySearchDto);

	PolicyResponseDto inprogressCitrieaSearch(PolicySearchDto policySearchDto);

	PolicyResponseDto changeStatus(Long policyId, String status);

	PolicyResponseDto policyReject(Long policyId, String reject);

	PolicyResponseDto policyApproved(Long policyId, String approved, String variantType) throws ApplicationException;

	PolicyAdjustmentResponse saveAdjustment(PolicyAdjustmentRequestDto adjustmentDto);

	PolicyAdjustmentResponse saveAdjustmentOldDto(Long policyId);

	PolicyResponseDto getExistingPolicyByPolicyNumber(String policyNumber, String unitId);

	PolicyResponseDto getExistingPolicyBymphId(String existing, Long mphId, Long policyId);

	PolicyDetailsResponseDto getPolicyDetailsByProposalNo(String proposalNo);

	BigDecimal calculateStamps(BigDecimal amount);

	CommonResponseStampDto stampDutyConsuption(StampDutyConsumptionRequestDto requestDto) throws ApplicationException;

	RegularAdjustmentContributionResponseDto getFrequencyDates(PolicyFrequencyDetailsDto request) throws ParseException;

	AdjustmentContributionResponseDto getFrequencyByPolicyId(Long policyId);

	PolicyResponseDto getPolicyMember(Long memberId, Long policyId);

	PolicyResponseDto getMemberDetails(String licId, Long policyId);

	PolicyResponseDto getPolicyContributionDetails(ContributionRequestDto contributionRequestDto);

	PolicyResponseDto getMemberContributionDetails(ContributionRequestDto contributionRequestDto);

	PolicyResponseDto getInvidualContriButionDetails(ContributionRequestDto contributionRequestDto);

	InputStream policyContributionSummary(Long policyId, Long adjConId) throws IOException;

	InputStream getFullContribution(Long policyId,String finicialYear);

	IssuePolicyResponseDto issuancePolicy(String policyNumber);

	TrnRegistrationResponseDto trnRegistration(String policyNumber);

	PolicyResponseDto checkissuancePolicySuccessOrNot(String policyNumber);

	List<PolicyDetailSearchResponseDto> policyDetailSearch(PolicyDetailSearchRequestDto policyDetailSearchRequestDto);

	ByteArrayInputStream membersDownload(Long policyId, Long mphId, String unitCode) throws IOException;

	DataTable getPolicyMemberDetailsDataTable(GetPolicyMemberDetailsRequestDto getPolicyMemberDetailsRequestDto);
	
	PolicyResponseDto getMemberDetailsByLicIdandMemberId(String licId, Long policyId, String membershipNumber);

	PolicyFundStmtResponseDto fetchDetailsFrPolicyFundStatement(PolicyFundStmtRequestDto requestDto);

	ProposalAnnuityDto getProposalDetails(String proposalNo);
	
	CommonResponseStampDto DepsoitRefundPolicySearch(DepositRefundPolicySearchRequestDto dto);
	
}
