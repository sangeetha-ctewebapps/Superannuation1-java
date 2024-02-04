package com.lic.epgs.claim.entity;

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
@Table(name = "CLAIM_MBR_NOMINEE")
public class ClaimMbrNomineeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLAIM_NOMINE_SEQ")
	@SequenceGenerator(name = "CLAIM_NOMINE_SEQ", sequenceName = "CLAIM_NOMINE_SEQ", allocationSize = 1)
	@Column(name = "NOMINEE_ID", length = 10)
	private Long nomineeId;

	@Column(name = "CLAIM_NO", length = 20)
	private String claimNo;

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

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@Column(name = "MODIFIED_BY", length = 50)
	private String modifiedBy;
	
	@Column(name = "BANK_BRANCH", length = 50)
	private String bankBranch;

	@Column(name = "APPOINTEE_NAME", length = 250)
	private String appointeeName;

	@Column(name = "CLAIMANT_TYPE", length = 50)
	private String claimantType;

	@Column(name = "AGE", length = 10)
	private Integer age;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_MEMBER_ID", nullable = true)
	private ClaimMbrEntity claimMbrEntity;
	
	@Column(name = "TOTAL_FUND_VALUE", length = 50)
	private Double totalFundValue;
	
	@Column(name = "GENDER", length = 50)
	private String gender;
	
	@Column(name = "PAN", length = 50)
	private String pan;
	
//	@OneToMany(mappedBy = "claimMbrEntity")
//	private List<ClaimCommutationCalcEntity> claimCommutationCalc;

//	@OneToMany(mappedBy = "claimMbrEntity")
//	private List<ClaimAnnuityCalcEntity> claimAnuityCalc;
	
//	@OneToMany(mappedBy = "claimMbrEntity")
//	private List<ClaimMbrFundValueEntity> claimMbrFundValue;
//	
//	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "claimMbrNomineeEntity")
//	private ClaimMbrAppointeeEntity claimMbrAppointeeEntity;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
}
