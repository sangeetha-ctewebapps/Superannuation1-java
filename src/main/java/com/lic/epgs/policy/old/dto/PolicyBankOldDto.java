package com.lic.epgs.policy.old.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author pradeepramesh
 *
 */
@Data
public class PolicyBankOldDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long bankAccountId;
	
	private Long policyId;

	private String accountNumber;

	private String accountType;

	private String ifscCode;
	
	private String ifscCodeAvailable;
	
	private String bankName;

	private String bankBranch;
	
	private String bankAddress;
	
	private String countryId;
	
	private Long mphId;
	
	private String stateId;
	
	private String districtId;
	
	private String cityId;
	
	private String townLocality;
	
	private String stdCode;
	
	private String landLineNumber;
	
	private String emailId;

	private String createdBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	
	private String modifiedBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
	
	private Boolean isActive = Boolean.TRUE;
	
	private Boolean isMapped = Boolean.FALSE;
	
	private Long surrenderId;
	
}
