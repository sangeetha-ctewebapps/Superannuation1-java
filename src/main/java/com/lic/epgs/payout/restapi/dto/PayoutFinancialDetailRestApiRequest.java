package com.lic.epgs.payout.restapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
public class PayoutFinancialDetailRestApiRequest {

	private String anOrigin	;
	private String anPayoutMethod;
	private String anPayoutType;
	private Double annuityAmount;
	private String annuityDueDate;
	private String annuityMode;
	private String annuityOption;
	private Integer	arrears	;
	private Integer	basicPension;
	private Integer	certailLifePeriod;
	private Integer	certainPeriod;
	private String daDueDate;
	private String dateOfExit;
	private String dateOfVesting;
	private Double gstAmnt	;
	private Double incomeTaxDeducted;
	private Double incomeTaxPending	;
	private Double incomeTaxProjected;
	private String modeOfExit;
	private String pensionAmtPaidTo;
	private Double purchasePrice;
	private Integer recovery;
}
