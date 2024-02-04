package com.lic.epgs.payout.entity;

import java.math.BigDecimal;
import java.sql.Blob;
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
@Table(name = "REINITIATE_STORED_PROCEDURE_RESPONSE")
public class ReinitiateStoredProcedureResponseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REINITIATE_SP_SEQ")
	@SequenceGenerator(name = "REINITIATE_SP_SEQ", sequenceName = "REINITIATE_SP_SEQ", allocationSize = 1)
	@Column(name = "REINITIATE_SP_ID")
	private Long reinitiateSpId;

	@Column(name = "PAYOUT_ID")
	private Long payoutId;

	@Column(name = "JOURNAL_NO")
	private Long journalNo;
	
	@Column(name = "DEBIT_ACCOUNT")
	private Long debitAccount;
	
	@Column(name = "TOTAL_AMOUNT")
	private BigDecimal totalAmount;
	
	@Column(name = "CREDIT_ACCOUNT")
	private Long creditAccount;
	
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
	private Integer sqlCode;

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
	
	@Column(name = "BENEFICIARY_PAYMENT_ID")
	private String benefiaryPaymentId;
	
	@Column(name = "PAYMENT_ID_OLD")
	private String paymentIdOld;
	
	@Column(name = "api_request")
	private Blob apiRequest;

	@Column(name = "api_request_string", length = 4000)
	private String apiRequestString;

	@Column(name = "api_response")
	private Blob apiResponse;

	@Column(name = "api_response_string", length = 4000)
	private String apiResponseString;

	@Column(name = "request_date")
	private Date requestDate;

	@Column(name = "response_date")
	private Date responseDate;

	@Column(name = "type")
	private String type;

	@Column(name = "api_url")
	private String apiUrl;

	@Column(name = "remark")
	private String remark;

	@Column(name = "error_response", length = 4000)
	private String errorResponse;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
}
