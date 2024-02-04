package com.lic.epgs.claim.entity;

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
@Table(name = "PROC_SA_STORED_PROCEDURE_REQUEST")
public class ProcSaStoredProcedureRequestEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROC_SA_SPR_SEQ")
	@SequenceGenerator(name = "PROC_SA_SPR_SEQ", sequenceName = "PROC_SA_SPR_SEQ", allocationSize = 1)
	@Column(name = "PROC_SA_ID")
	private Long procSaSprId;
	
	
	@Column(name = "P_S_ACCOUNT_CONTEXT")
	private String psAccountContext;
	
	@Column(name = "P_N_TOTAL_AMOUNT")
	private Double pnTotalAmount;
	
	@Column(name = "P_N_PAYMENT_AMOUNT")
	private Double pnPaymentAmount;
	
	@Column(name = "P_N_TDS_AMOUNT")
	private Double pnTdsAmount;
	
	@Column(name = "P_N_INTERFUND_AMOUNT")
	private Double pnInterfundAmount;

	@Column(name = "P_N_GST_EXPENSE_AMOUNT")
	private Double pnGstExpenseAmount;

	@Column(name = "P_N_ROC_ANNUITY_AMOUNT")
	private Double pnRocAnnuityAmount;
	
	@Column(name = "P_N_GST_LIABILITY_AMOUNT")
	private Double pnGstLiabilityAmount;

	@Column(name = "P_N_MVA_CHARGE")
	private Double pnMvaCharge;

	@Column(name = "P_N_EXIT_CHARGE")
	private Double pnExtiCharge;

	@Column(name = "P_N_DR_SHORT_REMITTANCE")
	private Double pnDrShortRemittance;

	@Column(name = "P_N_CR_SHORT_REMITTANCE")
	private Double pnCrShortRemittance;

	@Column(name = "P_S_ISPRICEAPPLICABLE")
	private String psIspriceApplicable;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;
	
	@Column(name = "CLAIM_ID")
	private Long claimId;

	@Column(name = "CLAIM_ONBOARD_NO", length = 20)
	private String claimOnBoardNo;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

}
