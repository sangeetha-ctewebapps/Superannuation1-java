package com.lic.epgs.payout.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PayoutMbrAddressDto {

	private Long addressId;

	private String payoutNo;

	private String addressType;

	private String country;

	private Integer pinCode;

	private String district;

	private String state;

	private String city;

	private String addressLineOne;

	private String addressLineTwo;

	private String addressLineThree;

	private String createdBy;

	private String modifiedBy;

}
