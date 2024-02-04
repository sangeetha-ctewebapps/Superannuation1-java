package com.lic.epgs.policy.dto;

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

public class TrnRegistrationRequestDto {
	
	private String amount;
	private String amountType;
	private String bnkUniqueId;
	private String challanNo;
	private String createdBy;
	private String policyNo;
	private String poolAccountCode;
	private String productCode;
	private String proposalNo;
	private String referenceDate;
	private String referenceIdType;
	private String status;
	private String validityUptoDate;
	private String reason;
	private String variantCode;
	

}
