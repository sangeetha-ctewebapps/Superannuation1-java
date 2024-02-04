package com.lic.epgs.claim.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaAccountConfigMasterRequestDto {

	private String psAccountContext;
	private Double pnTotalAmmount;
	private Double psPaymentAmount;
	private Double pnTdsAmmount;
	private Double pnInterFundAmount;
	private Double pnGstExpenseAmount;
	private Double pnRocAnnuityAmount;
	private Double pnGstLiabilityAmount;
	private Double pnMvaCharge;
	private Double pnExitCharge;
	private Double pnDrShortRemittance;
	private Double pnCrShortRemittance;
	private String psIsPriceApplicable;
	
	
	
}
