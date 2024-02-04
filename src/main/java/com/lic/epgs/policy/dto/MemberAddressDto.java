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
public class MemberAddressDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long addressId;
	private Long memberId;
	
	private String address1;
	private String address2;
	private String address3;
	private String addressType;
	
	private String city;
	private String district;
	private String stateName;
	private String country;
	private Integer pincode;
	
	private Boolean isActive;
	
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
	
	private Long policyId;
}