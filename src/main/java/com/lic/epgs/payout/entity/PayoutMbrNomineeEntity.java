package com.lic.epgs.payout.entity;

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
@Table(name = "PAYOUT_MBR_NOMINEE")
public class PayoutMbrNomineeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYOUT_NOMINE_SEQ")
	@SequenceGenerator(name = "PAYOUT_NOMINE_SEQ", sequenceName = "PAYOUT_NOMINE_SEQ", allocationSize = 1)
	@Column(name = "NOMINEE_ID", length = 10)
	private Long nomineeId;

	@Column(name = "PAYOUT_NO", length = 20)
	private String payoutNo;

	@Column(name = "NOMINEE_CODE", length = 19)
	private String nomineeCode;

	@Column(name = "FIRST_NAME", length = 250)
	private String firstName;

	@Column(name = "LAST_NAME", length = 250)
	private String lastName;

	@Column(name = "MIDDLE_NAME", length = 250)
	private String middleName;

	@Column(name = "DOB")
	private Date dob;

	@Column(name = "MARITAL_STATUS", length = 10)
	private Long maritalStatus;

	@Column(name = "AADHAR_NUMBER", length = 19)
	private Long aadharNumber;

	@Column(name = "RELATION_SHIP", length = 10)
	private Long relationShip;

	@Column(name = "EMAIL_ID", length = 50)
	private String emailId;

	@Column(name = "MOBILE_NO", length = 19)
	private Long mobileNo;

	@Column(name = "ADDRESS_ONE", length = 255)
	private String addressOne;

	@Column(name = "ADDRESS_THREE", length = 255)
	private String addressThree;

	@Column(name = "ADDRESS_TWO", length = 255)
	private String addressTwo;

	@Column(name = "COUNTRY", length = 10)
	private String country;

	@Column(name = "DISTRICT", length = 255)
	private String district;

	@Column(name = "PINCODE", length = 10)
	private Long pincode;

	@Column(name = "STATE", length = 250)
	private String state;

	@Column(name = "ACCOUNT_NUMBER", length = 50)
	private String accountNumber;

	@Column(name = "ACCOUNT_TYPE", length = 25)
	private String accountType;

	@Column(name = "IFSC_CODE", length = 25)
	private String ifscCode;

	@Column(name = "BANK_NAME", length = 250)
	private String bankName;

	@Column(name = "SHARED_AMOUNT", length = 20, scale = 2)
	private Double sharedAmount;

	@Column(name = "SHARED_PERCENT", length = 20, scale = 2)
	private Double sharedPercentage;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "APPONITE_NAME", length = 250)
	private String apponiteName;

	@Column(name = "CLAIMANT_TYPE", length = 50)
	private String claimantType;

	@Column(name = "IFSC_FLAG", length = 10)
	private String ifscFlag;

	@Column(name = "AGE", length = 10)
	private Integer age;
	
	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@Column(name = "MODIFIED_BY", length = 50)
	private String modifiedBy;
	
	@Column(name = "BANK_BRANCH", length = 50)
	private String bankBranch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAYOUT_MEMBER_ID", nullable = false)
	private PayoutMbrEntity payoutMbrEntity;
	
	@Column(name = "GENDER", length = 50)
	private String gender;
	
	@Column(name = "PAN", length = 50)
	private String pan;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
}
