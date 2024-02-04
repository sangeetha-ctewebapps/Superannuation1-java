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
public class PayoutSearchResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String payoutNo;
	
	private String initiMationNo;
	
	private String claimNo;

	private String masterPolicyNo;

	private String mphCode;
	
	private String mphName;

	private String pan;

	private Long aadhar;

	private String memberShipNo;

	private String firstName;

	private String lastName;

	private Integer payoutStatus;
	
	private String licId;
	
	private String dtOfExit;
	
	private String modeOfExit;
	
	private String unitCode;

	private String utrNo;
	
	private String paymentStatus;


}
