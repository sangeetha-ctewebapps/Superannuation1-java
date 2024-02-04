package com.lic.epgs.adjustmentcontribution.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;

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
public class AdjustmentSearchDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String policyNumber;
	private String adjustmentContributionNumber;
	
	private String quotatonNumber;
	private String collectionNumber;
	
	private String proposalNumber;
	private String adjustmentType;
	
	private String policyStatus;
	private String adjustmentContributionStatus;
	
	
	private String mphCode;
	private String mphName;

	private String product;
	private String lineOfBusiness;
	private String variant;

	private String unitOffice;

	private String role;
	private String unitCode;
	private String isActive;

	private String pageName;
	private String userName;
	
	private String from;
	private String to;

}