package com.lic.epgs.claim.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class MphContactDetailsAnnuityDto {

//	data from commonModule
	
	private String firstName;
	private String middleName;
	private String lastName;
	private String designation;
	private String country;
	private String mobileNumber;
	private String stdCode;
	private String landlineNumber;
	private String emailId;
	private String contactName;
	private String contactStatus;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedDate;

	
	
	
}
