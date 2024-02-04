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
public class PayoutCommutationCalcDto {

	private Long communityId;
	
	private String payoutNo;

	private Boolean commutationReq;

	private Integer commutationBy;

	private Double commutationValue;

	private Double commutationAmt;

	private Double commutationPerc;

	private Boolean tdsApplicable;

	private String nomineeCode;

	private Double tdsPerc;

	private Double tdsAmount;

	private String amtPayableTo;

	private Double exitLoad;
	
	private Double exitLoadRate;
	
	private Double mvaExitLoad;
	
	private Double mvaExitLoadRate;

	private Double purchasePrice;

	private String createdBy;
	
	private Double netAmount;
	
	private Double marketValue;
	
	private Double marketValueAdjustment;
	
	private Double shortReserve;
	
	private Double slab;
	

}
