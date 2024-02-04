package com.lic.epgs.policy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh D  date :25-02-2023
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class ContributionRequestDto {
	
	private Long policyConId;
	private Long policyId;
	private String financialYear;
	private String quarter;
	private Integer frequency;
	private Integer licId;

}
