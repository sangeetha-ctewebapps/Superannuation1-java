package com.lic.epgs.payout.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PayoutFundValueDto {

	private Long fundValueId;

	private String payoutNo;

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
	
	private Double corpusPercentage;
	
	private Double fundValue;
	
	private Double refundToMPH;
	

	private Double totalContirbution; 
	
	private Double openingBalance;
	
	private Date dateOfCalculate;
	
	private Boolean ismarketValueApplicable;

	private Double commutationAmount;
	
	private Double existLoad;
	
	private Double existLoadRate;
	
	private String spouseName;

	private String spouseDOB;
	

}
