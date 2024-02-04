package com.lic.epgs.payout.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PayoutPayeeBankDetailsDto {

	
	
	private Long bankAccountId;
	private Integer tempBankAccountId;
	private Long masterBankAccountId;
	
	private Long codeId;
	private Integer nomineeId;
	private String nomineeCode;
	private String amountPayable;
	private Double corpusPercentage;
	private String type;
	private String claimNo;
	private Integer memberId;
	private Integer mphId;
	private Boolean isActive;
	private String versionNo;
	private String accountNumber;
	private String accountType;
	private String ifscCode;
	private String bankName;
	private String bankBranch;
	private String bankAddressOne;
	private String bankAddressTwo;
	private String bankAddressThree;
	private String countryCode;
	private String stdCode;
	private String landlineNumber;
	private String emailID;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	private PayoutMbrDto payoutMbrEntity;
	
	private String benefiaryPaymentId;
}
