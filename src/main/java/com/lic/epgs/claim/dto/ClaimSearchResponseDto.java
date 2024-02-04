package com.lic.epgs.claim.dto;

import java.io.Serializable;

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
public class ClaimSearchResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String claimNo;

	private String masterPolicyNo;

	private String mphCode;
	
	private String mphName;

	private String pan;

	private Long aadhar;

	private String onboardingNo;

	private String memberShipNo;

	private String firstName;

	private String lastName;

	private Integer claimStatus;

	private String dateOfExit;
	
	private Integer modeOfExit;
	
	private String age;
	
	private String claimIntimaitonNo;
	
	private String licId;
	
}
