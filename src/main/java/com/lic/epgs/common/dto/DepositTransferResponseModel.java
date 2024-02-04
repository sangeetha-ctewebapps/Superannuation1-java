package com.lic.epgs.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositTransferResponseModel {
	private String policyNumber;
	private String mphName;
	private String mphCode;
	private String product;
	private String unitCode;
	private long customerId;
	private String policyStatus;
	private String ifscCode;
	private String accountType;
	private String bankName;
	private String bankBranch;
	private String productCode;
	private String schemeName;
	private String variantCode;
}
