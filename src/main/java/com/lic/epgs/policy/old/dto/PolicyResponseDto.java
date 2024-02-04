package com.lic.epgs.policy.old.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.policy.dto.MemberContributionDto;
import com.lic.epgs.policy.dto.PolicySearchResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private PolicyMbrOldDto zeroActRow;
	
	private List<PolicyDto> policyDtos;
	private List<PolicyNotesOldDto> policyNotesList;
	private List<PolicyAddressOldDto> addressList;
	private List<PolicyBankOldDto> bankList;
	private Long policyId;
	private Long mphId;
	private PolicyDto policyDto;
	private PolicyAddressOldDto policyAddressDto;
	private PolicyNotesOldDto policyNotesDto;
	private PolicyRulesOldDto policyRulesDto;
	private PolicyBankOldDto policyBankDto;
	private MemberContributionDto memberContribution;
	private String transactionStatus;
	private String transactionMessage;
	
	private Object response;

	private Long inprogressTotalMember;
	private Long existingTotalMember;
	private List<PolicySearchResponseDto> policySearchResponse;
	
	private String policyNumber;
	private String proposalNumber;
}
