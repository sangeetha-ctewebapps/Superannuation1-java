package com.lic.epgs.claim.dto;

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
public class MphAddressDetailsAnnuityDto {

	private String addressLine1	;
	private	String addressLine2;
	private	String addressLine3;
	private String addressType;
	
	private Integer pinCode;
	private String district;
	private String state;
	private Integer country;
	
	private String city;
	private	String modifiedBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private	Date modifiedOn;
	
	
	
}
