package com.lic.epgs.claim.dto;

import javax.persistence.Column;

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
public class ClaimCommutationCalcDto {

	private Long communityId;
	
	private String claimNo;

	private Boolean commutationReq;

	private Integer commutationBy;

	private Double commutationValue;

	private Double commutationAmt;

	private Double commutationPerc;

	private Boolean tdsApplicable;

	private String nomineeCode;

	private Double tdsPerc;

	private Double tdsAmount;
	
	private Double netAmount;

	private String amtPayableTo;

	private Double exitLoad;
	
	private Double exitLoadRate;
	
	private Double mvaExitLoad;
	
	private Double mvaExitLoadRate;

	private Double purchasePrice;

	private String createdBy;
	
	private Double slab;
	
	private Double rate;
	
	private String rateType;
	
	private Double incentiveRate;
	
	private Double disIncentiveRate;
	
	private Integer modeOfExist; 
	
	private Double marketValue;
	
	private Double marketValueAdjustment;
	
	private Double shortReserve;
	
	private Double totalFundValue;
	
	private Double commutationAmountShortReserve;
}
