package com.lic.epgs.policy.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositRefundPolicySearchRequestDto {

    private static final long serialVersionUID = 1L;
	
	private String policyNumber;
	private String unitCode;

}
