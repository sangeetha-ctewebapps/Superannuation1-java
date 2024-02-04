package com.lic.epgs.common.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter

public class PolicyResponseAnnuityDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private PolMphBasicDetailsAnnuityDto polMphBasicDetails;
	private List<PolMphAddressDetailsAnnuityDto> mphAddressDetails;
	private PolMphBankAccountDetailsAnnuityDto  mphBankAccountDetails;
	private List<PolMphContactDetailsAnnuityDto> mphContactDetails;
	
//	    "polMphBasicDetails": {},
//	    "mphAddressDetails": [{}],
//	    "mphBankAccountDetails": {},
//	    "mphContactDetails": [{}]
}