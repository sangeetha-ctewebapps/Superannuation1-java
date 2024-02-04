package com.lic.epgs.common.gi.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GiRequestDto {
	private String policyNumber;
	private long customerCode;
	private String vanNumber;
	private String proposalNumber;
	private String quotationNumber;
	private String panNumber;
	private String gstIn;
	private LocalDate policyCreationFromDate;
	private LocalDate policyCreationToDate;
	private String mphName;
	private String mphCode;
	private String rollType;

}