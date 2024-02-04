package com.lic.epgs.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter

public class PolMphBankAccountDetailsAnnuityDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String accountNumber;
	private String accountType;
	private String bankBranch;
	private String bankName;
	private String bankStatus;
	private String countryCode;
	private String createdBy;
	private String emailId;
	private String ifscCode;
	private String landLineNumber;
	private String stdCode;
		
//"mphBankAccountDetails": {
//	        "accountNumber": "string",
//	        "accountType": "string",
//	        "bankBranch": "string",
//	        "bankName": "string",
//	        "bankStatus": "string",
//	        "countryCode": "string",
//	        "createdBy": "string",
//	        "emailId": "string",
//	        "ifscCode": "string",
//	        "landLineNumber": "string",
//	        "stdCode": "string"
//	    }
	
}