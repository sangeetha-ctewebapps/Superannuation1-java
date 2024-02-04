package com.lic.epgs.payout.restapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PayoutNomineeDtlRestApiRequest {

	private String  aadharNo;
	private String accountNumber;
	private String accountType;
	private Long accountTypeId;
	private Long addressId	;
	private String amtPercentage;
	private String annuitantName;
	private String annuityNumber;
	private String appointeeAadharNumber;
	private String appointeeAccountNumber;
	private String appointeeAccountType;
	private Long appointeeAccountTypeId;
	private Long appointeeAddressId	;
	private Long appointeeAge;
	private String appointeeBankAddress;
	private String appointeeBankBranch;
	private Long appointeeBankId;
	private String appointeeBankName;
	private String appointeeCode;
	private String appointeeContactNumber;
	private String appointeeCountryCode;
	private String appointeeDOB;
	private String appointeeFirstName;
	private String appointeeIfscCode;
	private Long appointeeLandlineNo;
	private String appointeeLastName;
	private String appointeeMiddleName;
	private String appointeeName;
	private String appointeeOthers;
	private String appointeeStdCode;
	private String appointeepan;
	private String bankAddress;
	private String bankBranch;
	private String bankName;
	private String contactNumber;
	private String country;
	private String dateOfBirth;
	private String emailId;
	private String ifscCode;
	private String nmeStatus;
	private String nmeType	;
	private String nomineeAddress1;
	private String nomineeAddress2;
	private String nomineeAddress3;
	private Integer nomineeAge;
	private String nomineeCity;
	private String nomineeCode;
	private String nomineeCountry;
	private String nomineeCountryCode;
	private String nomineeDistrict;
	private String nomineeFirstName;
	private Long nomineeId;
	private String nomineeLastName;
	private String nomineeMiddleName;
	private String nomineeName;
	private Integer nomineePinCode;
	private String nomineeState;
	private String pan;
	private String relationShipWithNominee;
	private String relationship;
	private String status;
	private String stdCode;
}
