package com.lic.epgs.policy.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MPHBankAccountDetailsDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Integer bankAccountId;
	
	
	private String mphId;
	
	
	private String accountNumber;
	
	
	private String accountType;
	
	
	private String ifscCode;
	
	
	private String bankName;
	
	
	private String bankBranch;
	
	
	private String bankAddressOne;
	
	private String bankAddressTwo;
	
	private String bankAddressThree;
	
	
	private String countryCode;
	
	
	private String stdCode;
	
	
	private String landlineNumber;
	
	
	private String emailID;
	
	
	private String createdBy;
	
	
	private String createdOn;
	
	
	private String modifiedBy;
	
	
	private String modifiedOn;
	
	private	String proposalId;
	
	private String flag;
	
	private String defaultValue;
	
	
	
	
	
	
	
}
