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
public class ClaimOnboardingRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String policyNo;
	
	private Long memberId;

	private Integer modeOfExit;

	private String dateOfExit;

	private Long reasonForExit;

	private String reasonForOther;

	private String placeOfEvent;

	private String onboardedDate;

	private Integer initimationType;
	
	private String unitCode;
	
	private String firstName;
	
	private String dateOfDeath;
    
	private String otherReasonForDeath;
	
	private String category;
	
	private Long policyId;
	
	private String licId;
	
	private String createdBy;

}
