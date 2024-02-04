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
public class MphBankAnnuityDto {

	
	private String accountNumber;
	private String accountType;
	private String ifscCode;
	private String bankBranch;
	private String bankName;
	private Integer countryCode;
	private Integer stdCode;
	private Long landlineNumber;
	private String emailId;
	
	
//	private String countryId;
//	private String stateId;
//	private String districtId;
//	private String cityId;
//	private String townLocality;
	
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
}
