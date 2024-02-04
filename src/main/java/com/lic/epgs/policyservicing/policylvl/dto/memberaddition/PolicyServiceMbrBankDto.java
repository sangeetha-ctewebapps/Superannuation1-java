/**
 * 
 */
package com.lic.epgs.policyservicing.policylvl.dto.memberaddition;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Karthick M
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyServiceMbrBankDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	 private  Integer  bankId;
	 private  String  accountNumber;
	 private  String  accountType;
	 private  String  bankAddress;
	 private  String  bankBranch;
	 private  String  bankName;
	 private  String  confirmAccountNumber;
	 private  String  countryCode;
	 private  String  createdBy;
	 private  Date  createdOn;
	 private  String  emailId;
	 private  String  ifscCode;
	 private  String  ifscCodeAvailable;
	 private  Boolean  isActive;
	 private  Long  landlineNumber;
	 private  Long  memberId;
	 private  String  modifiedBy;
	 private  Date  modifiedOn;
	 private  Long  stdCode;

}
