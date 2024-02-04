package com.lic.epgs.policy.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StampDutyConsumptionRequestDto {
	private String policyNumber;
	private String createdOn;
	private String modifiedOn;
	private String createdBy;
	private String modifiedBy;
	private String consumerUnitsCode;
	private String productCode;
	private String stream;
	private String stampAmount;
	private String amountToBeAdjusted;
	private BigDecimal stampAmountBigDecimal;
	private BigDecimal amountToBeAdjustedBigDecimal;
}
