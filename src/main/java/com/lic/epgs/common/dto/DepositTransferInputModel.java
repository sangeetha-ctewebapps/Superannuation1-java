package com.lic.epgs.common.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositTransferInputModel {
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
