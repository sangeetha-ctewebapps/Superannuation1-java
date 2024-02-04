package com.lic.epgs.claim.dto;

import java.util.Set;

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
public class ClaimDetailsDto {

	private Long id;

	private Long policyId;

	private String masterPolicyNo;

	private String mphCode;

	private String mphName;

	private Integer masterPolicyStatus;

	private Integer category;

	private Integer product;

	private Long membershipId;

	private String membershipNo;

	private String memberName;

	private String claimNo;

	private String pan;

	private String aadhar;

	private String dob;

	private Integer workflowStatus;

	private String unitId;

	private String calculationType;

	private Double calculationAmount;

	private String calculatedBy;

	private Double employeeContribution;

	private Double employerContribution;

	private Double voluntaryContribution;

	private Double totalInterestAccured;

	private Double totalFundValue;

	private Integer noOfAnnuity;
	
	private String policyType;

	private Set<ClaimMbrNomineeDto> claimMbrNomineeDtls;

	private Set<ClaimMbrAppointeeDto> claimMbrAppointeeDtls;

	private Set<ClaimCommutationCalcDto> claimCommutationCalc;

	private Set<ClaimAnnuityCalcDto> claimAnuityCalc;

}
