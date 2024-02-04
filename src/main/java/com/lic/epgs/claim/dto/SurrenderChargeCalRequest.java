package com.lic.epgs.claim.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SurrenderChargeCalRequest {

	
	private Double fundAmount;
	private Long policyId;
	private Long licId;
	private String policyNumber;
	private Double marketValue;
	private Double existLoad;
	
	
}
