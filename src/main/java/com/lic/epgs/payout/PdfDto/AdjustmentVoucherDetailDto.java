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
public class AdjustmentVoucherDetailDto {

	private String headOfAccount;

	private String code;

	private Double debit;

	private Double credit;

	private BigDecimal debitBigdecimal;

	private BigDecimal creditBigDecimal;

	private String debitAmount;

	private String creditAmount;
}
