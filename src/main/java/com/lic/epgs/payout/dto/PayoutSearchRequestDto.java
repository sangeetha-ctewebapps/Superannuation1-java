package com.lic.epgs.payout.dto;

import java.io.Serializable;
import java.util.List;

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
public class PayoutSearchRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Integer> payoutStatus;
	
	private String payoutNo;
	
	private String initiMationNo;

	private String masterPolicyNo;

	private String mph;

	private String pan;

	private String aadhar;
	
	private String claimNo;
	
	private String unitCode;
	
	private String dtOfExit;
	
	private String modeOfExit;
	
	private String dateOfBirth;
	
	private String membershipNumber;

	private String licId;

	private String aadharNumber;

	private String phone;

}
