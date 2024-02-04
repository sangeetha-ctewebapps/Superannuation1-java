package com.lic.epgs.claim.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClaimMbrFundValueDto {

	private Long fundValueId;

	private String claimNo;

	private Double employerContribution;

	private Double employeeContribution;

	private Double voluntaryContribution;

	private Double totalInterestAccured;

	private Double totalFundValue;

	private Double purchasePrice;

	private Double pension;

	private String annuityOption;

	private String anuityMode;

	private Boolean isJointLiveRequired;

	private Long calculationType;

	private String nomineeCode;
	
	private Double purchasePriceForLIC;
	
	private String purchaseFromOthers;
	
	private Long purchasedFrom;
	
	private Long policyId;
	
	private String category;
	

}
