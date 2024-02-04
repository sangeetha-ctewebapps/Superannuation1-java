package com.lic.epgs.quotation.dto;

import java.io.Serializable;
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
public class QuotationMemberBankDetailDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long bankId;
	private Long memberId;
	private String accountNumber;
	private String confirmAccountNumber;
	private String accountType;
	private String ifscCodeAvailable;
	private String ifscCode;
	private String bankName;
	private String bankBranch;
	private String bankAddress;
	private Integer countryCode;
	private String stdCode;
	private Long landlineNumber;
	private String emailId;
	
	private String modifiedBy;
	private String createdBy;
	private Date modifiedOn;
	private Date createdOn;

}
