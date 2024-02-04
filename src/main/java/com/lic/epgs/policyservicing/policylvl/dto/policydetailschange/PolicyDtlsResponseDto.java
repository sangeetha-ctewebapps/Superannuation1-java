package com.lic.epgs.policyservicing.policylvl.dto.policydetailschange;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.policy.dto.MphAddressDto;
import com.lic.epgs.policy.dto.MphBankDto;
import com.lic.epgs.policy.dto.PolicyNotesDto;
import com.lic.epgs.policy.dto.PolicyRulesDto;
import com.lic.epgs.policy.old.dto.PolicyAddressOldDto;
import com.lic.epgs.policy.old.dto.PolicyBankOldDto;
import com.lic.epgs.policy.old.dto.PolicyDto;
import com.lic.epgs.policy.old.dto.PolicyRulesOldDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyDtlsResponseDto implements Serializable{

	private static final long serialVersionUID = 1L;


	private List<PolicyDto> policyDtos;
	private List<PolicyNotesDto> policyNotesList;
	private List<PolicyAddressOldDto> addressList;
	private List<PolicyBankOldDto> bankList;
	private List<PolicyRulesOldDto> policyRulesOldDto;
	private List<PolicyServiceNotesDto> serviceNotesList;
	private List<PolicyDetailsChangeDto> policyDtlsDtos;
	private PolicyDetailsChangeDto policyDtlsDto;
	private Long policyId;
	private PolicyDto policyDto;
	private PolicyAddressOldDto policyAddressDto;
	private PolicyNotesDto policyNotesDto;
	private PolicyRulesOldDto policyRulesDto;
	private PolicyBankOldDto policyBankDto;
	private PolicyServiceNotesDto serviceNotes;
	private String transactionStatus;
	private String transactionMessage;
	private Object response;
	private Long mphId;
	
}
