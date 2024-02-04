package com.lic.epgs.quotation.dto;

import java.io.Serializable;
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
public class QuotationMemberNomineeDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long memberId;
	private Long nomineeId;
	private String nomineeType;
	private String nomineeName;
	private String relationShip;
	private Long aadharNumber;
	private Long phone;
	private String emailId;
	private String addressOne;
	private String addressTwo;
	private String addressThree;
	private String country;
	private Long pinCode;
	private String district;
	private String state;
	private String percentage;
	
	private String modifiedBy;
	private String createdBy;
	private Date modifiedOn;
	private Date createdOn;
	
	private Integer age;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfBirth;
	
	private Boolean isActive;
}
