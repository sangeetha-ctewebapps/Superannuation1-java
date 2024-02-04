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
public class MphAddressDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long addressId;
//	private Long policyId;
	private Long mphId;
	private Boolean isDefault;
	private String addressType;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	
	
	private String cityLocality;
	private String cityId;
	
	private String district;
	private Integer districtId;
	
	private Integer stateId;
	private String stateName;
	
	private Integer countryId;
	
	private Integer pincode;
	
	private Boolean isActive;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
	private String modifiedBy;

}
