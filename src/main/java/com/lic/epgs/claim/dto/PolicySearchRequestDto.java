package com.lic.epgs.claim.dto;

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
public class PolicySearchRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String masterPolicyNo;

	private String mphCode;

	private String mphName;

	private String aadharNumber;

	private String pan;

	private String dateOfBirth;

	private String policyStatus;

	private String memberShipNo;

	private String contactNo;
	
	private String unitCode;

}
