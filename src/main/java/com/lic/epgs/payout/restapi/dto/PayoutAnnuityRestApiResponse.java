package com.lic.epgs.payout.restapi.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PayoutAnnuityRestApiResponse {
	
	private Long comCreationStgId;
	private String uploadedBy;
	private String annuityMode;
	private Date uploadedOn;
	private Date ecDueDate;
	private String unitName;
	private Date dateOfBirth;
	private String firstName;
	private String middleName;
	private String lastName;
	private Date dateOfJoiningScheme;
	private String gender;
	private String fatherName;
	private String spouseName;
	private String product;
	private String variant;
	private String addharNumber;
	private String pan;
	private String mobileNo;
	private String emailId;
	private String communicationPreference;
	private String languagePreference;
	private Date lastEcSubmissionDate;
	private String jointLifeFirstName;
	private String jointLifeMiddleName;
	private String jointLifeLastName;
	private String jointLifeGender;
	private Date jointLifeDOB;
	private String jointLifePercent;
	private Long certainPeriod;
	private String certailLifePeriod;
	private String annuityOption;
	private String totalTopupAmnt;
	private String frequencyOfDaIncrease;
	private String modeOfExit;
	private Date dateOfExit;
	private Date dateOfVesting;
	private Long annuityAmount;
	private Long purchasePrice;
	private String anOrigin;
	private String anPayoutMethod;
	private Long basicPension;
	private Date annuityDueDate;
	private Long gstAmnt;
	private Long incomeTaxProjected;
	private Long incomeTaxDeducted;
	private Long incomeTaxPending;
	private String pensionAmntPaidTo;
	private Date daDueDate;
	private Long arrears;
	private Long recovery;
	private Long accountNumber;
	private String confirmAccountNumber;
	private String ifcsCode;
	private String bankEmailId;
	private String accountType;
	private String bankBranch;
	private String bankAddress;
	private String bankCountryCode;
	private String bankStdCode;
	private String bankLandLineNumber;
	private String relationship;
	private String status;
	private Long sourceId;
	private String policyNumber;
	private Date certainPeriodEndDate;
	private Date annuityOptionEndDate;
	
	private String  referenceId;
	private String  referenceType;
	
	private String unitCode;

}
