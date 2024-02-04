package com.lic.epgs.claim.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClaimMbrBankDetailDto {

	private Long bankId;

	private Long memberId;

	private String claimNo;

	private String accountNumber;

	private String confirmAccountNumber;

	private String accountType;

	private String ifscCodeAvailable;

	private String ifscCode;

	private String bankName;

	private String bankBranch;

	private String bankAddress;

	private Integer countryCode;

	private Integer stdCode;

	private Integer landlineNumber;

	private String emailId;

	private String createdBy;

	private String modifiedBy;

}
