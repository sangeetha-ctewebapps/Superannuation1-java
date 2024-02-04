package com.lic.epgs.quotation.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "QUO_MEMBER_BULK_ERROR")
public class QuotationMemberErrorEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUO_MEMBER_BULK_ERROR_SEQ")
	@SequenceGenerator(name = "QUO_MEMBER_BULK_ERROR_SEQ", sequenceName = "QUO_MEMBER_BULK_ERROR_SEQ", allocationSize = 1)
	@Column(name = "ID", length = 10)
	private Long id;
	
	@Column(name = "MEMBERSHIP_ID")
	private String memberShipId;
	
	@Column(name = "QUOTATION_ID")
	private Integer quotationId;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@Column(name = "PROPOSAL_NUMBER")
	private String proposalNumber;
	
	@Column(name = "BATCH_ID")
	private Long batchId;
	
	@Column(name = "FILE_ID")
	private String fileId;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "MIDDLE_NAME")
	private String middleName;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	@Column(name = "FATHER_NAME")
	private String fatherName;
	
	@Column(name = "MARITAL_STATUS")
	private String maritalStatus;
	
	@Column(name = "SPOUSE_NAME")
	private String spouseName;
	
	@Column(name = "DATE_OF_BIRTH")
	private String dateOfBirth;

	@Column(name = "GENDER")
	private String gender;
	

	@Column(name = "PAN")
	private String pan;
	
	@Column(name = "PHONE")
	private Long memberPhone;
	
	@Column(name = "AGE")
	private Integer age;

	@Column(name = "AADHAR_NUMBER")
	private Long aadharNumber;

	@Column(name = "DATE_OF_JOINING")
	private String dateOfJoining;

	@Column(name = "DATE_OF_JOINING_SCHEME")
	private String dateOfJoiningScheme;

	@Column(name = "DATE_OF_RETRIREMENT")
	private String dateOfRetirement;

	@Column(name = "DESIGNATION")
	private String designation;

	@Column(name = "ROLE")
	private String role;

	@Column(name = "MEMBERSHIP_NUMBER")
	private String membershipNumber;

	@Column(name = "TYPEOF_MEMEBERSHIP")
	private String typeOfMembershipNo;


	@Column(name = "EMAILID")
	private String emailId;

	@Column(name = "LANGUAGE_PREFERENCE")
	private String languagePreference;

	@Column(name = "COMMUNICATION_PREFERENCE")
	private String communicationPreference;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "EMPLOYER_CONTRIBUTION")
	private String employerContribution;

	@Column(name = "EMPLOYEE_CONTRIBUTION")
	private String employeeContribution;

	@Column(name = "TOTAL_CONTRIBUTION")
	private String totalContribution;

	@Column(name = "VOLUNTARY_CONTRIBUTION")
	private String voluntaryContribution;

	@Column(name = "ANNUITY_OPTION")
	private String annuityOption;

	@Column(name = "TOTAL_INTERESTED_ACCURED")
	private BigDecimal totalInterestedAccured;

	@Column(name = "FREQUENCY")
	private Integer frequency;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();


	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;
	
	
	
	


	@Column(name = "ADDRESS_ADDRESS_TYPE")
	private String addressAddressType;

	@Column(name = "ADDRESS_COUNTRY")
	private String addressCountry;

	@Column(name = "ADDRESS_PINCODE")
	private Integer addressPincode;

	@Column(name = "ADDRESS_DISTRICT")
	private String addressDistrict;

	@Column(name = "ADDRESS_STATE")
	private String addressState;
	
	@Column(name = "ADDRESS_CITY")
	private String addressCity;

	@Column(name = "ADDRESS_ADDRESS_1")
	private String addressAddress1;

	@Column(name = "ADDRESS_ADDRESS_2")
	private String addressAddress2;

	@Column(name = "ADDRESS_ADDRESS_3")
	private String addressAddress3;
	
	
	
	
	
	
	
	@Column(name = "BANK_ACCOUNT_NUMBER")
	private String bankAccountnumber;

	@Column(name = "BANK_CONFIRM_ACCOUNT_NUMBER")
	private String confirmBankAccountnumber;

	@Column(name = "BANK_ACCOUNT_TYPE")
	private String bankAccountType;

	@Column(name = "BANK_IFSC_CODE_AVAILABLE")
	private String ifscCodeAvailable;

	@Column(name = "BANK_IFSC_CODE")
	private String bankIfscCode;

	@Column(name = "BANK_BANK_NAME")
	private String bankName;

	@Column(name = "BANK_BANK_BRANCH")
	private String bankBranch;

	@Column(name = "BANK_BANK_ADDRESS")
	private String bankAddress;

	@Column(name = "BANK_COUNTRY_CODE")
	private String  bankCountryCode;

	@Column(name = "BANK_STD_CODE")
	private String  bankStdCode;

	@Column(name = "BANK_LANDLINE_NUMBER")
	private String  bankLandlineNumber;

	@Column(name = "BANK_EMAIL_ID")
	private String bankEmailId;
	
	
	


	@Column(name = "NOMINEE_TYPE")
	private String nomineeType;

	@Column(name = "NOMINEE_NAME")
	private String nomineeName;

	@Column(name = "NOMINEE_RELATION_SHIP")
	private String nomineeRelationship;

	@Column(name = "NOMINEE_AADHAR_NUMBER")
	private String  nomineeAadharNumber;
	
	@Column(name = "NOMINEE_DATE_OF_BIRTH")
	private String  nomineeDateOfBirth;
	
	@Column(name = "NOMINEE_AGE")
	private String  nomineeAge;

	@Column(name = "NOMINEE_PHONE")
	private String  nomineeContactNumber;
	
	@Column(name = "NOMINEE_PERCENTAGE")
	private String nomineePercentage;

	@Column(name = "NOMINEE_EMAIL_ID")
	private String nomineeEmailId;

	@Column(name = "NOMINEE_ADDRESS_1")
	private String nomineeAddress1;

	@Column(name = "NOMINEE_ADDRESS_2")
	private String nomineeAddress2;

	@Column(name = "NOMINEE_ADDRESS_3")
	private String nomineeAddress3;

	@Column(name = "NOMINEE_COUNTRY")
	private String nomineeCountry;

	@Column(name = "NOMINEE_PINCODE")
	private String  nomineePinCode;

	@Column(name = "NOMINEE_DISTRICT")
	private String nomineeDistrict;;

	@Column(name = "NOMINEE_STATE")
	private String nomineeState;
	
	
	
	
	
	@Column(name = "APPOINTEE_NAME")
	private String appointeeName;

	@Column(name = "APPOINTEE_CODE")
	private String appointeeCode;

	@Column(name = "APPOINTEE_RELATIONSHIP")
	private String relationshipWithNominee;

	@Column(name = "APPOINTEE_CONTACT_NUMBER")
	private String  appointeeContactNumber;

	@Column(name = "APPOINTEE_DATE_OF_BIRTH")
	private String  appointeeDateOfBirth;

	@Column(name = "APPOINTEE_PAN")
	private String appointeePan;

	@Column(name = "APPOINTEE_AADHAR_NUMBER")
	private String  appointeeAadharNumber;

	@Column(name = "APPOINTEE_IFSC_CODE")
	private String appointeeIfscCode;

	@Column(name = "APPOINTEE_BANK_NAME")
	private String appointeeBankName;;

	@Column(name = "APPOINTEE_ACCOUNT_TYPE")
	private String appointeeAccountType;

	@Column(name = "APPOINTEE_ACCOUNT_NUMBER")
	private String  appointeeAccountNumber;


	@Column(name = "ERROR",length = 1000)
	private String error;
	
	

	
	

	
	
	
	
	

//	@Column(name = "QUOTATION_ID", length = 20)
//	private Integer quotationId;
//
//	@Column(name = "PROPOSAL_NUMBER", length = 20)
//	private String proposalNumber;
//
//	@Column(name = "MEMBERSHIP_ID", length = 20)
//	private String memberShipId;
//
//	@Column(name = "FIRST_NAME", length = 255)
//	private String firstName;
//	
//	@Column(name = "DATE_OF_BIRTH", length = 20)
//	private String dateOfBirth;
//
//	@Column(name = "DATE_OF_JOINING", length = 20)
//	private String dateOfJoining;
//	
//	@Column(name = "GENDER", length = 20)
//	private String gender;
//	
//	@Column(name = "CATEGORY", length = 10)
//	private String category;
//	
//	@Column(name = "EMPLOYER_CONTRIBUTION", length = 500)
//	private String employerContribution;
//
//	@Column(name = "EMPLOYEE_CONTRIBUTION", length = 500)
//	private String employeeContribution;
//
//	@Column(name = "VOLUNTARY_CONTRIBUTION", length = 500)
//	private String voluntaryContribution;
//	
//	@Column(name = "ADDRESS_ADDRESS_TYPE", length = 50)
//	private String addressAddressType;
//	
//	@Column(name = "ADDRESS_PINCODE", length = 100)
//	private String addressPincode;
//	
//	@Column(name = "ADDRESS_ADDRESS1", length = 255)
//	private String addressAddress1;
//	
//	@Column(name = "BANK_ACCOUNTNUMBER", length = 255)
//	private String bankAccountnumber;
//	
//	
//	@Column(name = "CONFIRM_BANK_ACCOUNTNUMBER", length = 255)
//	private String confirmBankAccountnumber;
//	
//	
//	@Column(name = "NOMINEE_NAME", length = 255)
//	private String nomineeName;
//
//	@Column(name = "NOMINEE_RELATIONSHIP", length = 100)
//	private String nomineeRelationship;
//
//	@Column(name = "NOMINEE_DATE_OF_BIRTH", length = 20)
//	private String nomineeDateOfBirth;
//
//	@Column(name = "NOMINEE_PERCENTAGE", length = 20)
//	private String nomineePercentage;
//
//	@Column(name = "NOMINEE_ADDRESS1", length = 255)
//	private String nomineeAddress1;
//
//
//	@Column(name = "NOMINEE_PIN_CODE", length = 255)
//	private String nomineePinCode;
//
//	@Column(name = "NOMINEE_AGE", length = 100)
//	private String nomineeAge;
//
//	@Column(name = "APPOINTEE_NAME", length = 255)
//	private String appointeeName;
//
//	@Column(name = "ERROR", length = 400)
//	private String error;
//
//	@Column(name = "BATCH_ID", length = 20)
//	private Long batchId;
//
//	@Column(name = "FILE_ID", length = 20)
//	private String fileId;
//	
//	@Column(name = "IS_ACTIVE", length = 20)
//	private Boolean isActive;
}
