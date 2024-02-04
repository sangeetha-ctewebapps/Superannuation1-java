package com.lic.epgs.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Pradeep Ramesh Date:25.05.2022
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountDetailsDto {

	private String bankAccountId;
	private String customerId;
	private String groupCustomerId;
	private String trustId;
	private String subCustomerId;
	private String gctFlag;
	private String isActive;
	private String accountNumber;
	private String accountType;
	private String ifscCode;
	private String bankName;
	private String bankBranch;
	private String bankAddress1;
	private String bankAddress2;
	private String bankAddress3;
	private String countryCode;
	private String stdCode;
	private String landlineNumber;
	private String emailID;
	private String channelColorCode;
	private String channelUserId;
	private String createdBy;
	private String createdOn;
	private String modifiedBy;
	private String modifiedOn;

}
