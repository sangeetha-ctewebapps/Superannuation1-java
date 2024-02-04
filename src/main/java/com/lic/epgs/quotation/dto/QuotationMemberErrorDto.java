package com.lic.epgs.quotation.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
public class QuotationMemberErrorDto implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	private Long id;
//	private Long memberId;
	private Integer quotationId;
	private String proposalNumber;
	private Long batchId;
	private String fileId;
	private String memberShipId;
	private String licId;
	private String memberStatus;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fatherName;
	private String maritalStatus;
	private String spouseName;
	private String dateOfBirth;
	private String gender;
	private String pan;
	private String phone;
	private Integer age;
	private String aadharNumber;
	private String dateOfJoining;
	private String dateOfJoiningScheme;
	private String dateOfRetirement;
	private Integer retirementAge;
	private BigDecimal annumSalary;
	private String designation;
	private String role;
	private String membershipNumber;
	private String typeOfMembershipNo;
	private Long memberPhone;
	private String emailId;
	private String languagePreference;
	private String communicationPreference;
	private String category;
	private String employerContribution;
	private String employeeContribution;
	private String voluntaryContribution;
	private String totalContribution;
	private String annuityOption;
	private BigDecimal totalInterestedAccured;
	private Integer frequency;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn ;
	
	
	
	

	private String addressAddressType;
	private String addressCountry;
	private String addressPincode;
	private String addressState;
	private String addressDistrict;
	private String addressCity;
	private String addressAddress1;
	private String addressAddress2;
	private String addressAddress3;
	
	
	private String bankAccountnumber;
	private String confirmBankAccountnumber;
	private String bankAccountType;
	private String bankIfscCode;
	private String bankName;
	private String bankBranch;
	private String bankAddress;
	private String bankCountryCode;
	private String bankStdCode;
	private String bankLandlineNumber;
	private String bankEmailId;
	
	
	private String nomineeType;
	private String nomineeName;
	private String nomineeRelationship;
	private String nomineeAadharNumber;
	private String nomineeDateOfBirth;
	private String nomineeAge;
	private String nomineeContactNumber;
	private String nomineePercentage;
	private String nomineeEmailId;
	private String nomineeAddress1;
	private String nomineeAddress2;
	private String nomineeAddress3;
	private String nomineePinCode;
	private String nomineeCountry;
	private String nomineeState;
	private String nomineeDistrict;
	
	
	private String appointeeName;
	private String appointeeCode;
	private String relationshipWithNominee;
	private String appointeeContactNumber;
	private String appointeeDateOfBirth;
	private String appointeePan;
	private String appointeeAadharNumber;
	private String appointeeIfscCode;
	private String appointeeBankName;
	private String appointeeAccountType;
	private String appointeeAccountNumber;
	
	private Boolean isActive;
	private String error;
	
	private Long policyId;
	private Long serviceId;
	private String policyNumber;


//	private static final long serialVersionUID = 1L;
//	
//	private Boolean isActive;
//	private String error;
//	private Integer quotationId;
//	private String proposalNumber;
//	private Long policyId;
//	private Long serviceId;
//	private String policyNumber;
//
//	private Long id;
//	private Long memberId;
//
//	private Long batchId;
//	private String fileId;
//	private String memberShipId;
//	private String licId;
//	private String memberStatus;
//	private String firstName;
//	private String middleName;
//	private String lastName;
//	private String fatherName;
//	private String maritalStatus;
//	private String spouseName;
//	private String dateOfBirth;
//	private String gender;
//	private String pan;
//	private String phone;
//	private Integer age;
//	private String aadharNumber;
//	private String dateOfJoining;
//	private String dateOfJoiningScheme;
//	private String dateOfRetirement;
//	private Integer retirementAge;
//	private BigDecimal annumSalary;
//	private String designation;
//	private String role;
//	private String membershipNumber;
//	private String typeOfMembershipNo;
//	private Long memberPhone;
//	private String emailId;
//	private String languagePreference;
//	private String communicationPreference;
//	private String category;
//	private String employerContribution;
//	private String employeeContribution;
//	private String voluntaryContribution;
//	private String totalContribution;
//	private String annuityOption;
//	private BigDecimal totalInterestedAccured;
//	private Integer frequency;
//	private Integer createdBy;
//	private Date createdOn;
//	private Integer modifiedBy;
//	private Date modifiedOn ;
//	
//	
//	
//	
//
//	private String addressAddressType;
//	private String addressCountry;
//	private String addressPincode;
//	private String addressState;
//	private String addressDistrict;
//	private String addressCity;
//	private String addressAddress1;
//	private String addressAddress2;
//	private String addressAddress3;
//	
//	
//	private String bankAccountnumber;
//	private String confirmBankAccountnumber;
//	private String bankAccountType;
//	private String bankIfscCode;
//	private String bankName;
//	private String bankBranch;
//	private String bankAddress;
//	private String bankCountryCode;
//	private String bankStdCode;
//	private String bankLandlineNumber;
//	private String bankEmailId;
//	
//	
//	private String nomineeType;
//	private String nomineeName;
//	private String nomineeRelationship;
//	private String nomineeAadharNumber;
//	private String nomineeDateOfBirth;
//	private String nomineeAge;
//	private String nomineeContactNumber;
//	private String nomineePercentage;
//	private String nomineeEmailId;
//	private String nomineeAddress1;
//	private String nomineeAddress2;
//	private String nomineeAddress3;
//	private String nomineePinCode;
//	private String nomineeCountry;
//	private String nomineeState;
//	private String nomineeDistrict;
//	
//	
//	private String appointeeName;
//	private String appointeeCode;
//	private String relationshipWithNominee;
//	private String appointeeContactNumber;
//	private String appointeeDateOfBirth;
//	private String appointeePan;
//	private String appointeeAadharNumber;
//	private String appointeeIfscCode;
//	private String appointeeBankName;
//	private String appointeeAccountType;
//	private String appointeeAccountNumber;
//	

}
