package com.lic.epgs.payout.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReinitiateRequestDto {
	
	private String accountRuleContext;
	private String RefNo;
	private String BeneficiaryBankIfsc;
	private String BeneficiaryAccountNumber;
	private String EffectiveDateOfPayment;
	private String PayoutSourceModule;
	private String PaymentIdOld;
	private String BeneficiaryPaymentId;
	private String ProductCode;
	private String VariantCode;
	private BigDecimal TotalAmount;
	private String OperatingUnit;
	private String OperatingUnitType;
	private String PaymentMode;
	private String PolicyNo;
	private String LOB;
	private String Product;
	private String MPHCODE;
	private String ProductVariant;
	private String IcodeForLOB;
	private String IcodeForProductLine;
	private String IcodeForVarient;
	private String IcodeForBusinessType;
	private String IcodeForParticipatingType;
	private String IcodeForBusinessSegment;
	private String IcodeForInvestmentPortfolio;
	private String BeneficiaryName;
	private String BeneficiaryBankName;
	private String BeneficiaryBranchIFSC;
	private String BeneficiaryBranchName;
	private String BeneficiaryAccountType;
	private String BeneficiaryLei;
	private String SenderLei;
	private String UnitCode;
	private String PolicyNumber;
	private String MemberNumber;
	private String PaymentCategory;
	private String PaymentSubCategory;
	private String NroAccount;
	private String Iban;
	private String Remarks;
	private String OUT_JournalNumber;
	private String OUT_DebitAccount;
	private String OUT_CreditAccount;
	private String OUT_TotalAmount;
	private String OUT_CreditICode;
	private String OUT_DebitICode;
	private String OUT_Message;
	private String OUT_Status;
	private String OUT_StatusCode;
	private String P_SQLCODE;
	private String P_SQLERROR_MESSAGE;
	private String createdBy;
    
}
