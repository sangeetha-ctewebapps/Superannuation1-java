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
public class ClaimOnboardingDto {

	private Long claimOnBoardId;

	private String claimOnBoardNo;

	private Integer initimationType;

	private String onboardingDate;

	private String createdOn;

	private String createdBy;

	private String modifiedOn;

	private String modifiedBy;

	private String initiMationNo;

	private Boolean isActive;

	private String onboardingStatus;

}
