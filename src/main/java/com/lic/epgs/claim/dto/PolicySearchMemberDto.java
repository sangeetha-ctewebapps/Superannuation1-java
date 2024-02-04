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
public class PolicySearchMemberDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long memberId;

	private String memberShipNo;

	private String firstName;

	private String lastName;

	private String pan;

	private String dob;

	private Long aadhar;

	private String product;

	private String category;
	
	private String memberShipId;
	
	private String dateOfJoining;
	
	private String licId;
	
	private String memberStatus;
	private Long policyId;

}
