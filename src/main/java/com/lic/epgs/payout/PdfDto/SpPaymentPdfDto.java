package com.lic.epgs.payout.PdfDto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SpPaymentPdfDto {

	private String vouchernum;
	private String divisionalName;
	private String date;
	private String schemeName;
	private String amountInWords;
	private String policyNumber;
	private String total;
	private String balanceDeposit;
	private String ard;
	private String mode;
	private String duedate;
	private String details;
	private String endDate;
	private String drawn;
	private String chequeNumber;
	private String payDate;
	private String paidDate;
	private String voucherNumber;
	private String voucherDate;
	private String preparedBy;
	private String favouring;
	private String unitName;
	private String unitCode;
	private String mphName;

	private List<AdjustmentVoucherDetailDto> voucherDetail = new ArrayList<AdjustmentVoucherDetailDto>();
}
	

