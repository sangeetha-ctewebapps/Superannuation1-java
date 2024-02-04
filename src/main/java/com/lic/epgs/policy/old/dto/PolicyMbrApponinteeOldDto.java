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
public class PolicyMbrApponinteeOldDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private  Long  appointeeId;
	 private  Long  aadharNumber;
	 private  String  accountNumber;
	 private  String  accountType;
	 private  String  appointeeCode;
	 private  String  appointeeName;
	 private  String  bankName;
	 private  Long  contactNumber;
	 private  String  createdBy;
	 private  Date  createdOn;
	 
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	 private  Date  dateOfBirth;
	 private  String  ifscCode;
	 private  Boolean  isActive;
	 private  Long  memberId;
	 private  String  modifiedBy;
	 private  Date  modifiedOn;
	 private  String  pan;
	 private  String  relationship;

}
