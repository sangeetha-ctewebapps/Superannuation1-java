package com.lic.epgs.claim.dto;

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
public class ClaimMbrNomineeDto {

	private Long nomineeId;

	private String claimNo;

	private String nomineeCode;

	private String firstName;

	private String lastName;

	private String middleName;

	private String dob;

	private Long maritalStatus;

	private Long aadharNumber;

	private Long relationShip;

	private String emailId;

	private Boolean isActive;

	private Long mobileNo;

	private String addressOne;

	private String addressThree;

	private String addressTwo;

	private String country;

	private String district;

	private Long pincode;

	private String state;

	private String accountNumber;

	private String accountType;

	private String ifscCode;

	private String bankName;

	private Double sharedAmount;
	
	private String apponiteName;

	private String claimantType;

	private Boolean ifscCodeAvailable;

	private Integer age;

	private Double sharedPercentage;
	
	private String createdBy;
	
	private String bankBranch;
	
	private Double totalFundValue;
	
	private ClaimMbrAppointeeDto claimMbrAppointeeEntity;
	
	private String gender;
	
	private String pan;
	
	private Long parentNomineeId;

}
