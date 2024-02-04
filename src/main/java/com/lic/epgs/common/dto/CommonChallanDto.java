package com.lic.epgs.common.dto;

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
public class CommonChallanDto {

	private Integer challanId;
	private Long quotationId;
	private String challanNo;
	private Date createdOn;
	private Boolean isActive;
	private String mphName;
	private String productCode;
	private String proposalNo;
	private String vanNo;
	private String createdBy;
	private String modifiedBy;
	private Date modifiedOn;
	private String quotationNo;
	private String bankBranch;
	private String bank;
	private String bankUniueld;
	private String ifscCode;
	private String triNo;
	private Long policyId;
	private String policyNumber;
	private String proposalId;
	private String proposalNumber;

}
