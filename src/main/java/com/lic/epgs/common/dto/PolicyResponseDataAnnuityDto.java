package com.lic.epgs.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class PolicyResponseDataAnnuityDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private transient Object policyresponseData;
	private String transactionMessage;
	private String transactionStatus;

	
//	"policyresponseData": {
//    "polMphBasicDetails": {},
//    "mphAddressDetails": [{}],
//    "mphBankAccountDetails": {},
//    "mphContactDetails": [{}]
//}
}