package com.lic.epgs.policy.old.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyAddressOldDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long addressId;
	private Long policyId;
	
	private String addressType;
	
	private String pinCode;
	private Long mphId;
	private Integer countryId;
	private Integer stateId;
	private Integer districtId;
	private String cityId;
	private String townLocality;
	
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
	private Boolean isActive = Boolean.TRUE;
	
	private Boolean isMapped = Boolean.FALSE;
}
