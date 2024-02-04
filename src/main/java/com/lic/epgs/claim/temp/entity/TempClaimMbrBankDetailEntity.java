package com.lic.epgs.claim.temp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "TEMP_CLAIM_MBR_BANK")
public class TempClaimMbrBankDetailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TCLAIM_MBR_BANK_SEQUENCE")
	@SequenceGenerator(name = "TCLAIM_MBR_BANK_SEQUENCE", sequenceName = "TCLAIM_MBR_BANK_SEQUENCE", allocationSize = 1)
	@Column(name = "BANK_ID", length = 10)
	private Long bankId;

	@Column(name = "CLAIM_NO", length = 30)
	private String claimNo;

	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;

	@Column(name = "CONFIRM_ACCOUNT_NUMBER")
	private String confirmAccountNumber;

	@Column(name = "ACCOUNT_TYPE")
	private String accountType;

	@Column(name = "IFSC_CODE_AVAILABLE")
	private String ifscCodeAvailable;

	@Column(name = "IFSC_CODE")
	private String ifscCode;

	@Column(name = "BANK_NAME")
	private String bankName;

	@Column(name = "BANK_BRANCH")
	private String bankBranch;

	@Column(name = "BANK_ADDRESS", length = 10)
	private String bankAddress;

	@Column(name = "COUNTRY_CODE")
	private Integer countryCode;

	@Column(name = "STD_CODE")
	private Integer stdCode;

	@Column(name = "LANDLINE_NUMBER", length = 10)
	private Integer landlineNumber;

	@Column(name = "EMAIL_ID", length = 50)
	private String emailId;

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "MODIFIED_BY", length = 100)
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_MEMBER_ID", nullable = false)
	private TempClaimMbrEntity claimMbrEntity;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
}
