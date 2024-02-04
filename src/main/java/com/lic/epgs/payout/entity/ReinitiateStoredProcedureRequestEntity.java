package com.lic.epgs.payout.entity;

/**
 * @author Logesh.D   Date :18-08-2023
 *
 */

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "REINITIATE_STORED_PROCEDURE_REQUEST")
public class ReinitiateStoredProcedureRequestEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REINITIATE_SPR_SEQ")
	@SequenceGenerator(name = "REINITIATE_SPR_SEQ", sequenceName = "REINITIATE_SPR_SEQ", allocationSize = 1)
	@Column(name = "REINITIATE_SPR_ID")
	private Long reinitiateSprId;
	
	@Column(name = "ACCOUNT_RULE_CONTEXT")
	private String accountRuleContext;
	
	@Column(name = "REF_NO")
	private String RefNo;
	
	@Column(name = "BENEFICIARY_BANK_IFSC")
	private String BeneficiaryBankIfsc;
	
	@Column(name = "BENEFICIARY_ACCOUNT_NUMBER")
	private String BeneficiaryAccountNumber;
	
	@Column(name = "EFFECTIVE_DATE_OF_PAYMENT")
	private Date EffectiveDateOfPayment;
	
	@Column(name = "PAYOUT_SOURCE_MODULE")
	private String PayoutSourceModule;
	
	@Column(name = "PAYMENT_ID_OLD")
	private String PaymentIdOld;
	
	@Column(name = "BENEFICIARY_PAYMENT_ID")
	private String BeneficiaryPaymentId;
	
	@Column(name = "PRODUCT_CODE")
	private String ProductCode;
	
	@Column(name = "VARIANT_CODE")
	private String VariantCode;
	
	@Column(name = "TOTAL_AMOUNT")
	private BigDecimal TotalAmount;
	
	@Column(name = "OPERATING_UNIT")
	private String OperatingUnit;
	
	@Column(name = "OPERATING_UNIT_TYPE")
	private String OperatingUnitType;
	
	@Column(name = "PAYMENT_MODE")
	private String PaymentMode;
	
	@Column(name = "POLICY_NO")
	private String PolicyNo;
	
	@Column(name = "LOB")
	private String lob;
	
	@Column(name = "PRODUCT")
	private String Product;
	
	@Column(name = "MPH_CODE")
	private String mphCode;
	
	@Column(name = "PRODUCT_VARIANT")
	private String ProductVariant;
	
	@Column(name = "ICODE_FOR_LOB")
	private String IcodeForLOB;
	
	@Column(name = "ICODE_FOR_PRODUCTLINE")
	private String IcodeForProductLine;
	
	@Column(name = "ICODE_FOR_VARIENT")
	private String IcodeForVarient;
	
	@Column(name = "ICODE_FOR_BUSINESS_TYPE")
	private String IcodeForBusinessType;
	
	@Column(name = "ICODE_FOR_PARTICIPATING_TYPE")
	private String IcodeForParticipatingType;
	
	@Column(name = "ICODE_FOR_BUSINESS_SEGMENT")
	private String IcodeForBusinessSegment;
	
	@Column(name = "ICODE_FOR_INVESTMENT_PORTFOLIO")
	private String IcodeForInvestmentPortfolio;
	
	@Column(name = "BENEFICIARY_NAME")
	private String BeneficiaryName;
	
	@Column(name = "BENEFICIARY_BANK_NAME")
	private String BeneficiaryBankName;
	
	@Column(name = "BENEFICIARY_BRANCH_IFSC")
	private String BeneficiaryBranchIFSC;
	
	@Column(name = "BENEFICIARY_BRANCH_NAME")
	private String BeneficiaryBranchName;
	
	@Column(name = "BENEFICIARY_ACCOUNT_TYPE")
	private String BeneficiaryAccountType;
	
	@Column(name = "BENEFICIARY_LEI")
	private String BeneficiaryLei;
	
	
	@Column(name = "SENDER_LEI")
	private String SenderLei;
	
	@Column(name = "UNIT_CODE")
	private String UnitCode;
	
	@Column(name = "POLICY_NUMBER")
	private String PolicyNumber;
	
	@Column(name = "MEMBER_NUMBER")
	private String MemberNumber;
	
	@Column(name = "PAYMENT_CATEGORY")
	private String PaymentCategory;
	
	@Column(name = "PAYMENT_SUBCATEGORY")
	private String PaymentSubCategory;
	
	@Column(name = "NRO_ACCOUNT")
	private String NroAccount;
	
	@Column(name = "IBAN")
	private String Iban;
	
	@Column(name = "REMARKS")
	private String Remarks;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

}
