package com.lic.epgs.payout.PdfDto;

import com.lic.epgs.notifyDomain.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DcFundDocumentDto {

	private String unitAddress;
	private String trusteName;
	private String trusteAddressLine1;
	private String trusteStreet;
	private String trusteState;
	private String trustePincode;
	private String masterPolicyNo;
	private String year;
	private String interest;
	private String balanceRunningAccountDate;
	private String balanceRunningAccountAmount;
	private String openingBalanceDate;
	private String openingBalanceAmount;
	private String  exitCases;
	private String exitCasesAmount;
	private String netOpeningBalanceDate;
	private String netOpeningBalanceAmount;
	private String purificationOn;
	private String purificationOff;
	private String revisedOpeningBalance;
	private String totalContributionReceived;
	private String equitableInterestReceived;
	private String contributionExitCases;	
	private String NETContributionDuringTheYear;
	private String addInterest;
	private String closingBalanceDate;
	private String closingBalanceAmount;
	
}
