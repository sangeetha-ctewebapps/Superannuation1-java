package com.lic.epgs.payout.PdfDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.lic.epgs.policy.dto.PolicyDepositDto;
import com.lic.epgs.policy.dto.PolicyContributionDto;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SpPdfDto {

	private String pngsDept;
	private String divisionalName;

	private String voucherNumber;
	private String voucherDate;

	private String favouringName;

	private String amountInWords;

	private String policyNumber;
	private String schemeName;

	private String total;

	private BigDecimal balanceDeposit;

	private String ard;
	private String mode;
	private String duedate;

	private String drawn;
	private String chequeNumber;
	private String date;
	private String payDate;
	private String paidDate;

	private String preparedBy;
	private String checkedBy;

	private List<AdjustmentVoucherDetailDto> voucherDetail = new ArrayList<AdjustmentVoucherDetailDto>();
	private List<SupplementaryAdjustmentDto> supplymentary = new ArrayList<SupplementaryAdjustmentDto>();
	
	
	//pdf
	private PolicyDepositDto policyDepositDto;
	private String adjustmentDueDate;
	private List<PolicyContributionDto> policyContributionDtoList = new ArrayList<PolicyContributionDto>();
	
	
}
