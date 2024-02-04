package com.lic.epgs.payout.dto;

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
public class PolicyMemberSearchRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String masterPolicyNo;

	private String mphCode;
	
	private String memberShipNo;

	private String pan;

	private String aadhar;

	private String dob;

	private String mphName;

	private String policyStatus;

}
