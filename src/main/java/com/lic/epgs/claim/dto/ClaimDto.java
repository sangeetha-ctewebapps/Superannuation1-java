package com.lic.epgs.claim.dto;

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
public class ClaimDto {

	private Long claimId;

	private String claimNo;

	private Integer status;

	private Boolean isActive;

	private String dtOfExit;

	private Integer modeOfExit;

	private Long reasonExit;

	private String otherReason;

	private String placeOfEvent;

	private String mphName;

	private String product;

	private String variant;

	private String lineOfBusiness;

	private String unitCode;

	private String mphCode;

	private String masterPolicyNo;

	private String masterpolicyStatus;

	private String workflowStatus;
	
	private String checkerCode;

	private String makerCode;
	
	private String policyType;
	
	private Long mphId;

	private String repudationReason;

	private ClaimOnboardingDto claimOnboarding;

	private ClaimMbrDto claimMbr;
	
	private List<ClaimNotesDto> claimNotes;

	private List<ClaimDocumentDetailDto> claimDocDetails;
	
	private String dateOfDeath;
	
	private String otherReasonForDeath;
	
	private String createdBy;
	
	private String modifiedBy;
	
	private Double totalShortReserve;
	
	

}
