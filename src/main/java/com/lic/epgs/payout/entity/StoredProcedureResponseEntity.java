package com.lic.epgs.payout.entity;

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
@Table(name = "PAYOUT_STORED_PROCEDURE_RESPONSE")
public class StoredProcedureResponseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYOUT_SP_SEQ")
	@SequenceGenerator(name = "PAYOUT_SP_SEQ", sequenceName = "PAYOUT_SP_SEQ", allocationSize = 1)
	@Column(name = "PAYOUT_SP_ID")
	private Long payoutSpId;

	@Column(name = "PAYOUT_ID")
	private Long payoutId;

	@Column(name = "JOURNAL_NO")
	private Long journalNo;

	@Column(name = "DEBIT_ACCOUNT")
	private Long debitAccount;

	@Column(name = "PAYMENT_CREDIT_ACCOUNT")
	private Long paymentCreditAccount;

	@Column(name = "TDS_CREDIT_ACCOUNT")
	private Long tdsCreditAccount;

	@Column(name = "TOTAL_AMOUNT")
	private Long totalAmount;

	@Column(name = "PAYMENT_AMOUNT")
	private Long paymentAmount;

	@Column(name = "TDS_AMOUNT")
	private Long tdsAmount;

	@Column(name = "CREDIT_CODE")
	private String creditCode;

	@Column(name = "DEBIT_CODE")
	private String debitCode;

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "STATUSCODE")
	private String statusCode;

	@Column(name = "SQLCODE")
	private Long sqlCode;

	@Column(name = "SQL_ERROR_MESSAGE")
	private String sqlErrorMessage;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@Column(name = "PROCESSED_STATUS")
	private String processedStatus;

	@Column(name = "PROCESSED_MESSAGE")
	private String processedMessage;

	@Column(name = "BENEFIARY_PAYMENT_ID")
	private String benefiaryPaymentId;
	
	@Column(name = "ANNUITY_CHALLAN_NO")
	private String challanNumber;
	
	@Column(name = "UTR_NO")
	private String utrNo;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
}
