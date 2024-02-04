package com.lic.epgs.payout.restapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PayoutAddressRestApiRequest {
	
	private String address1;
	private String address2;
	private String address3;
	private String city;
	private String country;
	private String district;
	private Integer	pinCode;
	private String state;
	private String typeOfAddress;

	
}
