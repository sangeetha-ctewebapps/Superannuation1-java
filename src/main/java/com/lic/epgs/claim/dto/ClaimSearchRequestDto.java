package com.lic.epgs.claim.dto;

import java.io.Serializable;
import java.util.List;

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
public class ClaimSearchRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Integer> claimStatus;
	
	private String claimNo;

	private String masterPolicyNo;

	private String mph;
	
	private String membershipNumber;

	private String pan;

	private String aadharNumber;
	
	private String aadhar;
	
	private String contactNumber;

	private String onboardingFrom;

	private String onboardingTo;
	
	private String onboardingNo;
	
	private String unitCode;
	
	private String claimIntimationNo;

}
