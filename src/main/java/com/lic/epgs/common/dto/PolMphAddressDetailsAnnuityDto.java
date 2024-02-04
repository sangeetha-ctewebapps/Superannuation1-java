package com.lic.epgs.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class PolMphAddressDetailsAnnuityDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String addressStatus;
	private String addressType;
	private String city;
	private String country;
	private String district;
	private String createdBy;
	private Integer pinCode;
	private String state;

//"mphAddressDetails": [
//	        {
//	            "addressLine1": "string",
//	            "addressLine2": "string",
//	            "addressLine3": "string",
//	            "addressStatus": "string",
//	            "addressType": "string",
//	            "city": "string",
//	            "country": "string",
//	            "district": "string",
//	            "createdBy": "string",
//	            "pinCode": 0,
//	            "state": "string"
//	        }
//	    ]	

}