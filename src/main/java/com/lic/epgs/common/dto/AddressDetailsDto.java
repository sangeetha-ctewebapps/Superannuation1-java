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
public class AddressDetailsDto {
	private String addressId;
	private String customerId;
	private String groupCustomerId;
	private String trustId;
	private String subCustomerId;
	private String gctFlag;
	private String isActive;
	private String addressType;
	private String pinCode;
	private String countryId;
	private String stateId;
	private String districtId;
	private String cityId;
	private String townLocality;
	private String printableFlag;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String channelColorCode;
	private String channelUserId;
	private String createdBy;
	private String createdOn;
	private String modifiedBy;
	private String modifiedOn;
	
  
  
}
