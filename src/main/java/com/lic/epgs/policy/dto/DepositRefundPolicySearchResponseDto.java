package com.lic.epgs.policy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class DepositRefundPolicySearchResponseDto {
	
    
	 private String policyNumber; 
	 private Long policyId; 
	 private String policyCode; 
	 private Long mphId; 
	 private String mphName; 
	 private String mphCode; 
	 private String unitCode; 
	 private String customerId; 
	 private String policyStatus; 
	 private String ifscCode; 
	 private String accountType; 
	 private String bankName; 
	 private String bankBranch; 
	 private String accountNumber;
	 private String confirmAccountNumber;
	 private String product; 
	 private String productCode; 
	 private String productCodeIcode; 
	 private String variantIcode; 
	 private String description; 
	 private String variant; 
	 private Long icodeForBusinessLine; 
	 private Long icodeForProductLine; 
	 private Long icodeForBusinessType; 
	 private Long icodeForParticipatingType; 
	 private Long icodeForBusinessSegment; 
	 private Long icodeForVariant;
	 private String schemeName; 
	 private String variantCode; 

}
