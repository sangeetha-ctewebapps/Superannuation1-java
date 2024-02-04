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
public class PolicyServiceMbrAddressDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long addressId;
	
	private String addressLineOne;
	private String addressLineTwo;
	private String addressLineThree;
	private String addressType;
	private String country;
	private String createdBy;
	private Date createdOn;
	private String district;
	private Boolean isActive;
	private Long memberId;
	private String modifiedBy;
	private Date modifiedOn;
	private Long pinCode;
	private String state;
}
