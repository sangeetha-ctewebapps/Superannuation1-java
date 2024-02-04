package com.lic.epgs.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class PolMphContactDetailsAnnuityDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String communicationPreferences;
	private String contactStatus;
	private String contactType;
	private String countryCode;
	private String createdBy;
	private String designation;
	private String emailId;
	private String faxNumber;
	private String firstName;
	private String lastName;
	private String landlineNumber;
	private String middleName;
	private String mobileNumber;
	private String stdCode;

//	"mphContactDetails":[
//	{
//	            "communicationPreferences": "string",
//	            "contactStatus": "string",
//	            "contactType": "string",
//	            "countryCode": "string",
//	            "createdBy": "string",
//	            "designation": "string",
//	            "emailId": "string",
//	            "faxNumber": "string",
//	            "firstName": "string",
//	            "lastName": "string",
//	            "landlineNumber": "string",
//	            "middleName": "string",
//	            "mobileNumber": "string",
//	            "stdCode": "string"
//	        }
//	]

}