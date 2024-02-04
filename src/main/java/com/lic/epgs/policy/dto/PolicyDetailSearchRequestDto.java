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
public class PolicyDetailSearchRequestDto {
	
	private String gstn;
	private String memberCode;
	private String mobileNo;
	private String mphCode;
	private String mphName;
	private String pan;
	private String policyNumber;
}
