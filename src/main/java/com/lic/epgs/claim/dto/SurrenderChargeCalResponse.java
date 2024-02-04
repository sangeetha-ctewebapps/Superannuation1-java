package com.lic.epgs.claim.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SurrenderChargeCalResponse {
	
	private Long policyId;
	private Double existLoad;
	private Double commutationAmount;
	private Double  markerValue;
	private Double marketValueAdjustment;
	private Boolean marketValueAdjustmentApplicable;
	private Double existLoadRate;
	private Double mvaExistLoadRate;
	private Double mvaExistLoad;
	
	
	
	
	
	
	

}
