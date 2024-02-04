package com.lic.epgs.policy.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberBankDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long memberBankId;
	private Long memberId;
	private Long policyId;
	
	private String accountNumber;
	private String confirmAccountNumber;
	private String accountType;
	
	private String ifscCodeAvailable;
	private String ifscCode;
	
	private String bankName;
	private String bankBranch;
	private String bankAddress;
	
	private Integer countryCode;
	private Integer stdCode;
	private Long landlineNumber;
	private String emailId;
	private Boolean isActive = Boolean.TRUE;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
}
