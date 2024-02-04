package com.lic.epgs.payout.dto;

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
public class PayoutMbrNomineeDto {

	private Long nomineeId;

	private String payoutNo;

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

	private Double sharedPercentage;
	
	private String createdBy;
	
	private String bankBranch;
	
	private String claimantType;
	
	private String gender;
	
	private String pan;

}
