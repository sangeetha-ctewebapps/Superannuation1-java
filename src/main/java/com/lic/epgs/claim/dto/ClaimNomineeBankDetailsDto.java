package com.lic.epgs.claim.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClaimNomineeBankDetailsDto {
	
	private Long bankAccountId;
	private String accountNumber;
	private String accountype;
	private String bankAddress1;
	private String bankAddress2;
	private String bankAddress3;
	private String bankBranch;
	private String bankName;
	private String countryCount;
	private String createdBy;
	private Date createdOn;
	private String emailId;
	private String ifscCode;
	private String landLineNumber;
	private Long memberId;
	private String modifiedBy;
	private Date modifiedOn;
	private Long nomineeId;
	private String nomineeCode;
	private String stdCode;
	private String tempBankAccountId;

}
