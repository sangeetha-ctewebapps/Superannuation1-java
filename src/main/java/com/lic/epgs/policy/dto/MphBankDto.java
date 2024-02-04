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
public class MphBankDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long mphBankId;
	private Long mphId;
	
	private String accountNumber;
	private String confirmAccountNumber;
	private String accountType;
	
	private String bankAddress;
	private String bankBranch;
	private String bankName;
	
	private String ifscCode;
	private String ifscCodeAvailable;
	
	private String emailId;
	private Long landlineNumber;
	private Integer stdCode;
	
	private Integer countryCode;
	private String countryId;
	private String stateId;
	private String districtId;
	private String cityId;
	private String townLocality;
	
	private Boolean isActive;
	private Boolean isDefault;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
}