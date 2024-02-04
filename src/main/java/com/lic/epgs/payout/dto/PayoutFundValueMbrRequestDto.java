package com.lic.epgs.payout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayoutFundValueMbrRequestDto {

	private Long fundValueId;
	
	private String payoutNo;

	private Long calculatedBy;
	
	private String nomineeCode;

	private Double purchasePrice;

	private Double pension;

	private String anuityOption;

	private String anuityMode;

	private Boolean isJointLiveRequired;

	private Double employeeContribution;

	private Double employeerContribution;

	private Double voluntaryContribution;

	private Double totalInterestAccured;

	private Double totalFundValue;

}
