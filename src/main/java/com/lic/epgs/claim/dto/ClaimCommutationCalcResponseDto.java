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
public class ClaimCommutationCalcResponseDto {

	private String claimNo;

	private Double commutationAmt;

	private Double tdsAmount;

}
