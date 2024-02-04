package com.lic.epgs.claim.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimOnboardingResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public ClaimOnboardingResponseDto(String claimNo, String onBoardingNo,ClaimDto claims) {
		this.claimNo = claimNo;
		this.onboardingNo = onBoardingNo;
		this.claims=claims;
	}

	private String claimNo;

	private String onboardingNo;
	
	private ClaimDto claims;
}
