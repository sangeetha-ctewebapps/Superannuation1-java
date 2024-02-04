///**
// * 
// */
//package com.lic.epgs.quotation.entity;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
///**
// * @author Muruganandam
// *
// */
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "QUO_MBR_BULK_TEMP")
//public class QuotationMembersBulkTempEntity {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QMBBULKTMP_GEN")
//	@SequenceGenerator(name = "QMBBULKTMP_GEN", sequenceName = "QMBBULKTMP_SEQ", allocationSize = 1)
//	@Column(name = "MEMBER_ID", length = 10)
//	private Long memberId;
//
//	@Column(name = "QUOTATION_ID", length = 10)
//	private Integer quotationId;
//
//	@Column(name = "PROPOSAL_NUMBER")
//	private String proposalNumber;
//
//	@Column(name = "MEMBER_SHIP_ID", length = 50)
//	private String memberShipId;
//
//	@Column(name = "LIC_ID", length = 255)
//	private String licId;
//
//	@Column(name = "MEMBER_STATUS", length = 20)
//	private String memberStatus;
//
//	@Column(name = "FIRSTNAME", length = 50)
//	private String firstName;
//
//	@Column(name = "MIDDLE_NAME", length = 50)
//	private String middleName;
//
//	@Column(name = "LAST_NAME", length = 50)
//	private String lastName;
//
//	@Column(name = "FATHER_NAME", length = 50)
//	private String fatherName;
//
//	@Column(name = "SPOUSE_NAME", length = 50)
//	private String spouseName;
//
//	@Column(name = "DATEOFBIRTH")
//	private String dateOfBirth;
//
//	@Column(name = "GENDER", length = 10)
//	private String gender;
//
//	@Column(name = "PAN", length = 10)
//	private String pan;
//
//	@Column(name = "AGE", length = 3)
//	private Integer age;
//
//	@Column(name = "AADHAR_NUMBER", length = 12)
//	private Long aadharNumber;
//
//	@Column(name = "DATEOFJOINING")
//	private String dateOfJoining;
//
//	@Column(name = "DATEOFJOINING_SCHEME")
//	private String dateOfJoiningScheme;
//
//	@Column(name = "DATEOF_RETIREMENT")
//	private String dateOfRetirement;
//
//	@Column(name = "RETIREMENT_AGE")
//	private Integer retirementAge;
//
//	@Column(name = "ANNUM_SALARY")
//	private BigDecimal annumSalary= BigDecimal.ZERO;
//
//	@Column(name = "DESIGNATION")
//	private String designation;
//
//	@Column(name = "ROLE")
//	private String role;
//
//	@Column(name = "MEMBERSHIP_NUMBER")
//	private String membershipNumber;
//
//	@Column(name = "TYPEOF_MEMEBERSHIP")
//	private String typeOfMembershipNo;
//
//	@Column(name = "PHONE")
//	private Long memberPhone;
//
//	@Column(name = "EMAILID")
//	private String emailId;
//
//	@Column(name = "LANGUAGE_PREFERENCE")
//	private String languagePreference;
//
//	@Column(name = "COMMUNICATION_PREFERENCE")
//	private String communicationPreference;
//
//	@Column(name = "CATEGORY")
//	private Integer category;
//
//	@Column(name = "EMPLOYER_CONTRIBUTION", precision = 20, scale = 8)
//	private BigDecimal employerContribution= BigDecimal.ZERO;
//
//	@Column(name = "EMPLOYEE_CONTRIBUTION", precision = 20, scale = 8)
//	private BigDecimal employeeContribution= BigDecimal.ZERO;
//
//	@Column(name = "VOLUNTARY_CONTRIBUTION", precision = 20, scale = 8)
//	private BigDecimal voluntaryContribution= BigDecimal.ZERO;
//
//	@Column(name = "TOTAL_CONTRIBUTION", precision = 20, scale = 8)
//	private BigDecimal totalContribution= BigDecimal.ZERO;
//
//	@Column(name = "ANNUITY_OPTION")
//	private String annuityOption;
//
//	@Column(name = "TOTAL_INTERESTED_ACCURED", precision = 20, scale = 8)
//	private BigDecimal totalInterestedAccured= BigDecimal.ZERO;
//
//	@Column(name = "FREQUENCY")
//	private Integer frequency;
//
//	@Column(name = "CREATED_BY")
//	private Integer createdBy;
//
//	@Column(name = "CREATED_ON")
//	private Date createdOn = new Date();
//
//	@Column(name = "MODIFIED_BY")
//	private Integer modifiedBy;
//
//	@Column(name = "MODIFIED_ON")
//	private Date modifiedOn = new Date();
//
//	@Column(name = "MBR_ADDRESS_1")
//	private String addressLineOne;
//
//	@Column(name = "PINCODE")
//	private Integer pinCode;
//
//	@Column(name = "BANK_NAME")
//	private String bankName;
//
//	@Column(name = "ACCOUNT_NUMBER")
//	private String accountNumber;
//
//	@Column(name = "IFSC_CODE")
//	private String ifscCode;
//
//	@Column(name = "NOMINEE_NAME")
//	private String nomineeName;
//
//	@Column(name = "NOMINEE_ADDRESS_1")
//	private String addressOne;
//
//	@Column(name = "NOMINEE_PHONE")
//	private Long nomineePhone;
//
//	@Column(name = "NOMINEE_AGE")
//	private Integer nomineeAge;
//
//	@Column(name = "NOMINEE_DOB")
//	private String nomineeDob;
//
//	@Column(name = "NOMINEE_RELATIONSHIP")
//	private String relationShip;
//
//	@Column(name = "APPOINTEE_NAME")
//	private String appointeeName;
//
//	@Column(name = "APPOINTEE_ADDRESS_1")
//	private String appointeeAddress;
//
//	@Column(name = "APPOINTEE_CONTACT")
//	private Long appointeeContactNumber1;
//
//	@Column(name = "APPOINTEE_AGE")
//	private Integer appointeeAge;
//
//	@Column(name = "APPOINTEE_DOB")
//	private String appointeeDob;
//
//	@Column(name = "APPOINTEE_RELATIONSHIP")
//	private String apointeeRelationship;
//
//	@Column(name = "IS_ACTIVE")
//	private Boolean isActive;
//}
