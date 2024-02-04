package com.lic.epgs.claim.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClaimFundValueDto {

	private Long fundValueId;

	private String claimNo;

	private Double employerContribution;

	private Double employeeContribution;

	private Double voluntaryContribution;

	private Double totalInterestAccured;

	private Double totalFundValue;

	private Double totalContirbution;

	private Double openingBalance;

	private Double corpusPercentage;

	private Double fundValue;

	private Double refundToMPH;

	private Double purchasePrice;

	private Double pension;

	private String annuityOption;

	private String anuityMode;

	private Boolean isJointLiveRequired;

	private Long calculationType;

	private String purchaseFromOthers;

	private String policyId;

	private String category;

	private String dateOfCalculate;

	private Boolean isMarketValueApplicable = false;

	private Double commutationAmount;

	private Double exitLoad;

	private Double exitLoadRate;

	private String spouseName;

	private String spouseDOB;

}
