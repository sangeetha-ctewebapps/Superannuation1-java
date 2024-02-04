package com.lic.epgs.payout.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayoutOnboardingResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public PayoutOnboardingResponseDto(String payoutNo) {
		this.payoutNo = payoutNo;
	}

	private String payoutNo;

}
