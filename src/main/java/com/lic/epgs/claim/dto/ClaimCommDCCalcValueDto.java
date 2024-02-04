package com.lic.epgs.claim.dto;

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
public class ClaimCommDCCalcValueDto {

	private String claimNo;

	private Double employeeContribution;

	private Double employerContribution;

	private Double voluntaryContribution;

	private Double totalInterestAccured;

	private Double totalFundValue;

	private Integer noOfAnnuity;

}
