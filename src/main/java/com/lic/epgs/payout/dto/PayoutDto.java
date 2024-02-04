package com.lic.epgs.payout.dto;

import java.util.Date;
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
public class PayoutDto {

	private Long payoutId;

	private String payoutNo;

	private Long claimId;
	
	private Long policyId;

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

	private String repudationReason;

	private Date dateOfDeath;
	
	private PayoutOnboardingDto payoutOnboarding;
		
	private List<PayoutNotesDto> payoutNotes;

	private List<PayoutDocumentDetailDto> payoutDocDetails;

	private PayoutMbrDto payoutMbr;
	
	private String policyType;
	
	private String initiMationNo;
	
	private String createdBy;
	
	private String modifiedBy;
	
	private String createdOn;
	
	private String modifiedOn;
	
	private Double totalShortReserve;
	
	
	private String claimOnBoadingNumber;
	

}
