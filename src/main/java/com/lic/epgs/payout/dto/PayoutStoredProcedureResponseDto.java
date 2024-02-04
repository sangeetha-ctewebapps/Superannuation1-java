package com.lic.epgs.payout.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PayoutStoredProcedureResponseDto {
	
	private Object journalNo;
	private Object debitaccount;
	private Object creditaccount;
	private Object tdsCreditaccount;
	private Object paymentCreditaccount;
	private Object totalamount;
	private Object paymentamount;
	private Object tdsamount;
	private Object crediticode;
	private Object debiticode;
	private Object message;
	private Object status;
	private Object statuscode;
	private Object sqlcode;
	private Object sqlerrorMessage;
	private Object anJournalNo;
	private Object anCrCode;
	private Object anGstExpenseCrCode;
	private Object anDrCode;
	private Object anGstDrCode;
	
	
	
	

}
