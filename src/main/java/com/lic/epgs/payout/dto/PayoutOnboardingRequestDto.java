package com.lic.epgs.payout.dto;

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
public class PayoutOnboardingRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String policyNo;

	private String claimNo;

	private String createdBy;
	
	private String modifiedBy;

}
