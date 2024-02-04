package com.lic.epgs.payout.restapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PayoutBankRestApiRequest {
	
	private String accountNumber;
	private String accountType;
	private String bankAddress;
	private String bankBranch;
	private String bankEmailId;
	private String confirmAccountNumber;
	private String countryCode;
	private String ifcsCode;
	private String landlineNumber;
	private String stdCode;
}
