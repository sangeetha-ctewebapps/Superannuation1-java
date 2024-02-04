package com.lic.epgs.policy.contribution.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyContributionResponseDto {

	private String contributionId;
	private String depositNumber;
	private String depositDate;
	private String contributionType;
	private String contributionDate;
	private String amount;

}
