package com.lic.epgs.quotation.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationMemberAddressDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long addressId;
	private Long memberId;
	private String addressType;
	private String country;
	private Integer pinCode;
	private String district;
	private String state;
	private String addressLineOne;
	private String addressLineTwo;
	private String addressLineThree;
	private String city;

	private String modifiedBy;
	private String createdBy;
	private Date modifiedOn;
	private Date createdOn;
}
