package com.lic.epgs.topupda.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author dhanush
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TOP_UP_DA")
public class TopupdaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOPUP_ID_SEQUENCE")
	@SequenceGenerator(name = "TOPUP_ID_SEQUENCE", sequenceName = "TOPUP_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "TOPUP_ID", length = 10)
	private Long topupId;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "TOPUP_ID", referencedColumnName = "TOPUP_ID")
	private Set<TopupdaNotesEntity> notes = new HashSet<>();

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "CUSTOMER_ERROR_CODE")
	private String customErrorCode;

	@Column(name = "QUOTATION_NUMBER")
	private String quotationNumber;

	@Column(name = "QUOTATION_STATUS")
	private String quotationStatus;

	@Column(name = "POLICY_NUMBER")
	private String policyNumber;

	@Column(name = "POLICY_STATUS")
	private String policyStatus;

	@Column(name = "MPH_NAME")
	private String mphName;

	@Column(name = "PRODUCT")
	private String product;

	@Column(name = "VARIENT")
	private String varient;

	@Column(name = "CALCULATION_VALUE")
	private String calculationValue;

	@Column(name = "TYPE_OF_TOPUP")
	private String typeOfTopup;

	@Column(name = "GST_BORNE_BY")
	private String gstBorneBy;

	@Column(name = "GROSS_PURCHASE_PRICE")
	private String grossPurchasePrice;

	@Column(name = "GST")
	private String gst;

	@Column(name = "QUOTATION_AMOUNT")
	private String quotationAmount;

	@Column(name = "PURCHASE_PRICE")
	private String purchasePrice;

	@Column(name = "ARREARS")
	private String arrears;

	@Column(name = "RECOVERY")
	private String recovery;

	@Column(name = "TOPUP_STAGE_ID")
	private String topupStagId;

	@Column(name = "Topup_Status")
	private String topupStatus;

	@Column(name = "UNIT_CODE")
	private String unitCode;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;

	// FOR REJECT
	@Column(name = "REJECTION_REASON_CODE")
	private String rejectionReasonCode;

	@Column(name = "REJECTION_REMARKS")
	private String rejectionRemarks;
	
	@Column(name = "AMOUNT_STATUS")
	private String amountStatus;
	@Column(name = "ANREASON")
	private String anReason;
}
