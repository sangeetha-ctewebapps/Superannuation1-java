package com.lic.epgs.common.gi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GiResponseDto {
	
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
