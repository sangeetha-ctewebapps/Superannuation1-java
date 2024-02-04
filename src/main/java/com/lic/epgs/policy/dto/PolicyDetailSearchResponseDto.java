package com.lic.epgs.policy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyDetailSearchResponseDto {
	
	private String address; 
	private String name;
	private String pan;
	private String emailAddress; 
	private String gstin;
	private String mphCode; 
	private String mphName;
	private String policyNumber; 
	private String memberCode;
	private String mobileNo;
	private String schemeName;
	private String operatingUnit;
	private String operatingUnitType;
	private String productCode;
	private String variantCode;
	private String masterPolicyHolderGstStateCode;
	private String state;
	private String typeOfClient;
	private String gstApplicability;
	private String isGstNotApplicableExempted;
}
