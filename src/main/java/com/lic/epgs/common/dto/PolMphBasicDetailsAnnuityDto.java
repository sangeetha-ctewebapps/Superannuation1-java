package com.lic.epgs.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class PolMphBasicDetailsAnnuityDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String apan;
	private String cin;
	private String createdBy;
	private Date createdDate;
	private String customerCode;

	private String customerGroupName;
	private Integer customerId;
	private String customerName;
	private String customerType;
	private String gstBorneBy;

	private String gstin;
	private String gstinApplicable;
	private Integer gstinRate;
	private String industryType;
	private String intermediaryCode;

	private String intermediaryName;
	private String lineofBusiness;
	private String marketingOfficercode;
	private String marketingOfficername;
	private String modifiedBy;

	private String mphCode;
	private String mphName;
	private Integer numberOfLives;
	private String pan;
	private Date policyEndDate;

	private Date policyIssuanceDate;
	private Date adjustedDate;
	private String policyNumber;
	private String policyStatus;
	private String product;

	private Long productId;
	private String rateOfApplicable;
	private String registeredWith;
	private BigDecimal totalQuotationAmount;
	private String typeofRegistration;

	private String unitCode;
	private Integer unitId;
	private String unitName;
	private String urbanOrRural;
	private String variant;

//	"polMphBasicDetails":
//	{
//	        "apan": "string",
//	        "cin": "string",
//	        "createdBy": "string",
//	        "createdDate": "dd-MM-yyyy",
//	        "customerCode": "string",
//	        "customerGroupName": "string",
//	        "customerId": 0,
//	        "customerName": "string",
//	        "customerType": "string",
//	        "gstBorneBy": "string",
//	        "gstin": "string",
//	        "gstinApplicable": true,
//	        "gstinRate": 0,
//	        "industryType": "string",
//	        "intermediaryCode": "string",
//	        "intermediaryName": "string",
//	        "lineofBusiness": "string",
//	        "marketingOfficercode": "string",
//	        "marketingOfficername": "string",
//	        "modifiedBy": "string",
//	        "mphCode": "string",
//	        "mphName": "string",
//	        "numberOfLives": 0,
//	        "pan": "string",
//	        "policyEndDate": "dd-MM-yyyy",
//	        "policyIssuanceDate": "dd-MM-yyyy",
//	        "adjustedDate": "dd-MM-yyyy",
//	        "policyNumber": "string",
//	        "policyStatus": "string",
//	        "product": "string",
//	        "productId": 0,
//	        "rateOfApplicable": "string",
//	        "registeredWith": "string",
//	        "totalQuotationAmount": 0,
//	        "typeofRegistration": "string",
//	        "unitCode": "string",
//	        "unitId": 0,
//	        "unitName": "string",
//	        "urbanOrRural": "string",
//	        "variant": "string"
//	    }

}