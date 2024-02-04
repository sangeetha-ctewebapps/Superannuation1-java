package com.lic.epgs.common.gi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GiServiceRequestDto {
	private String policyNumber;
	private long customerCode;
	private String vanNumber;
	private String proposalNumber;
	private String quotationNumber;
	private String panNumber;
	private String gstIn;
	private String policyCreationFromDate;
	private String policyCreationToDate;
	private String mphName;
	private String rollType;
	private String mphCode;

}
