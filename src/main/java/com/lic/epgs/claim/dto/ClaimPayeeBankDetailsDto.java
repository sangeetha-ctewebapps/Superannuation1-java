package com.lic.epgs.claim.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

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
public class ClaimPayeeBankDetailsDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private Long bankAccountId;
	private Integer nomineeId;
	private Integer memberId;
	private Integer mphId;
	private String accountNumber;
	private String accountType;
	private String ifscCode;
	private Boolean ifscCodeAvailable;
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
	private Integer tempBankAccountId;
	private Long codeId;
	private String claimNo;
	private String nomineeCode;
	private String amountPayable;
	private Double corpusPercentage;
	private String type;
	
}
