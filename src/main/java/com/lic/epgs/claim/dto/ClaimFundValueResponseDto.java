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
public class ClaimFundValueResponseDto {

	private String claimNo;

	private Double purchasePrice;

	private Double annuityAmount;

	private Double gstOnPurchasePrice;

	private Double purchasePriceWithGst;

	private Double purchasePriceWithoutGst;
	
    private Double totalFundValue;
	
	private Double corpusPercentage;
	
	private Double fundValue;
	
	private Double refundToMPH;

}
