/**
 * 
 */
package com.lic.epgs.policy.old.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PolicyMbrNomineeOldDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	 private  Long  nomineeId;
	 private  Long  aadharNumber;
	 private  String  addressOne;
	 private  String  addressThree;
	 private  String  addressTwo;
	 private  String  country;
	 private  String  createdBy;
	 private  Date  createdOn;
	 private  String  district;
	 private  String  emailId;
	 private  Boolean  isActive;
	 private  Long  memberId;
	 private  String  modifiedBy;
	 private  Date  modifiedOn;
	 private  String  nomineeName;
	 private  String  nomineeType;
	 private  Long  phone;
	 private  Long  pinCode;
	 private  String  relationShip;
	 private  String  state;
	 private  Integer percentage;
	 private  Integer age;
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	 private Date dateOfBirth;

}
