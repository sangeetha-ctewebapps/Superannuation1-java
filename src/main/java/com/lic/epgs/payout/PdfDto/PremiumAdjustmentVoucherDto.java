package com.lic.epgs.payout.PdfDto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PremiumAdjustmentVoucherDto {

	private String dateOfAdjustment;
	private String servicingUnit;
	private String adjustmentNumber;
	private String addressOfUnit;
	private String unitGstNumber;
	private String mphName;
	private String modeOfDeposit;
	private String policyNumber;
	private String uin;
	private String nameOfPlan;
	
	private String annualRenewalDate;
	private String premiumAdjustmentFor;
	private String nextPremiumDue;
	private String frequency;
	private String addressOfPolicyHolder;
	private String customerGstNumber;
	private BigDecimal totalPremiumAmount;
	private String totalPremiumAmountInWords;
	private String sysDate;

	private String depositNo;
	private String depositDate;
	private BigDecimal depositAmount;

	private String TERMINSURANCE;
	private String TERMINSURANCECGST;
	private String TERMINSURANCESGST;
	private String TERMINSURANCEUGST;
	private String TERMINSURANCEIGST;
	private String TERMINSURANCETOTAL;

	private String ANNUITYPURCHASE;
	private String ANNUITYPURCHASECGST;
	private String ANNUITYPURCHASESGST;
	private String ANNUITYPURCHASEUGST ;
	private String ANNUITYPURCHASEIGST;
	private String ANNUITYPURCHASETOTAL;

	private String LATEFEEONPREMIUM;
	private String LATEFEEONPREMIUMCGST;
	private String LATEFEEONPREMIUMSGST;
	private String LATEFEEONPREMIUMUGST;
	private String LATEFEEONPREMIUMIGST;
	private String LATEFEEONPREMIUMTOTAL;

	private String OTHERS;
	private String OTHERSCGST;
	private String OTHERSSGST;
	private String OTHERSUGST;
	private String OTHERSIGST;
	private String OTHERSTOTAL;

	private String TOTAL;
	private String TOTALCGST;
	private String TOTALSGST;
	private String TOTALUGST;
	private String TOTALIGST;
	private String TOTALTOTAL;
			
}
