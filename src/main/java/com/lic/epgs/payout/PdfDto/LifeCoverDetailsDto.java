package com.lic.epgs.payout.PdfDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LifeCoverDetailsDto {
	
	private String masterPolicyNo ;
	private Integer ModeofExit;
	private String Address1 ;
	private String Address2;
	private String Address3 ;
	private String Address4 ;
	private Integer Address5 ;
	private String city ;
	private String state ;
	private String cusName ;
	private String managerName ;
	private String encl;
	private String chequeNo;
	private String dated;
	private String forRs;
	private String schemeName;
	private String pincode;
	private String mphName;
	private String accountNo;
	private String bankName;
	private String ifscCode;

	private List<LifeInsuranceDetailDto> getLifeInsuranceDetailDto;

}		
	
