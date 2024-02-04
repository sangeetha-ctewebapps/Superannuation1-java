package com.lic.epgs.payout.restapi.dto;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PayoutAnnuityRestApiRequest {

	private Set<PayoutAddressRestApiRequest> anAddressDtlReqList = null;
	private PayoutBankRestApiRequest anBankDetailRequest;
	private PayoutFinancialDetailRestApiRequest anFinancialDetailRequest;
	private PayoutJointDetailRestApiRequest anJointDetailRequest = null;
	private Set<PayoutNomineeDtlRestApiRequest> anNomineeDtlRequestList = null;
	private PayoutMbrPrslDtRestApiRequest antPrslDtlRequest;
	private String policyNumber;
	private String sourceCode;
	private String uploadedBy; 
	private String uploadedOn;
	
	private String  referenceId;
	private String  referenceType;

}
