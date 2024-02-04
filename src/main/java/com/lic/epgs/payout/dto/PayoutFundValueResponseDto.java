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
public class PayoutFundValueResponseDto {

	private String payoutNo;

	private Double purchasePrice;

	private Double annuityAmount;

	private Double gstOnPurchasePrice;

	private Double purchasePriceWithGst;

	private Double purchasePriceWithoutGst;

}
