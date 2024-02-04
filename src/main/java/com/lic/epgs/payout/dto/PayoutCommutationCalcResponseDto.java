package com.lic.epgs.payout.dto;

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
public class PayoutCommutationCalcResponseDto {

	private String payoutNo;

	private Double commutationAmt;

	private Double tdsAmount;

}
