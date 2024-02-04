package com.lic.epgs.policy.dto;

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

public class IssuePolicyRequestDto {
		
	private String proposalNo;
	private String policyNo;
	private String productCode;
	private String variantCode;

}
