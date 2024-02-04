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
@Table(name = "TEMP_CLAIM_PAYEE_BANK")
public class ClaimPayeeTempBankDetailsEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLAIM_PAYEE_BANK_TMP_SEQ")
	@SequenceGenerator(name = "CLAIM_PAYEE_BANK_TMP_SEQ", sequenceName = "CLAIM_PAYEE_BANK_TMP_SEQ", allocationSize = 1)
	@Column(name = "BANK_ACCOUNT_ID")
	private Long bankAccountId;
	
	@Column(name = "CLAIM_NO", length = 30)
	private String claimNo;
	
	@Column(name = "NOMINEE_ID")
	private Integer nomineeId;
	
	@Column(name = "MEMBER_ID")
	private Integer memberId;
	
	@Column(name = "MPH_ID")
	private Integer mphId;
	
	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;
	
	@Column(name = "ACCOUNT_TYPE")
	private String accountType;
	
	@Column(name = "IFSC_CODE")
	private String ifscCode;
	
	@Column(name = "BANK_NAME")
	private String bankName;
	
	@Column(name = "BANK_BRANCH")
	private String bankBranch;
	
	@Column(name = "BANK_ADDRESS_1")
	private String bankAddressOne;
	
	@Column(name = "BANK_ADDRESS_2")
	private String bankAddressTwo;
	
	@Column(name = "BANK_ADDRESS_3")
	private String bankAddressThree;
	
	@Column(name = "COUNTRY_CODE")
	private String countryCode;
	
	@Column(name = "STD_CODE")
	private String stdCode;
	
	@Column(name = "LANDLINE_NUMBER")
	private String landlineNumber;
	
	@Column(name = "EMAIL_ID")
	private String emailID;
	
	@Column(name = "CREATED_BY",length = 50)
	private String createdBy;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;
	
	@Column(name = "MODIFIED_BY",length = 50)
	private String modifiedBy;
	
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_MEMBER_ID", nullable = true)
	private TempClaimMbrEntity claimMbrEntity;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "CLAIM_MEMBER_ID", nullable = true)
//	private TempClaimMbrNomineeEntity claimMbrNomineeEntity;
	
	@Column(name = "CODE_ID")
	private Long codeId;
	
	@Column(name = "NOMINEE_CODE", length = 50)
	private String nomineeCode;
	@Column(name = "AMOUNT_PAYABLE", length = 50)
	private String amountPayable;

	@Column(name = "CORPUS_PERCENTAGE")
	private Double corpusPercentage;
	
	@Column(name="Type")
	private String type;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

}

