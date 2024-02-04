package com.lic.epgs.policy.dto;

import java.util.List;

import com.lic.epgs.policy.old.dto.PolicyDepositOldDto;
import com.lic.epgs.quotation.dto.QuotationApiResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyDetailsResponseDto {

//	private List<PolicyShowDepositDto> policyShowDeposit;
	private List<PolicyDepositOldDto> policyShowDeposit;
	private QuotationApiResponseDto quotationApi;
	private ProposalAnnuityDto proposalDetails;
	private String transactionStatus;
	private String transactionMessage;

}
