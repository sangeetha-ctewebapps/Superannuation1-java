package com.lic.epgs.policy.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
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
public class MemberNomineeDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long nomineeId;
	private Long memberId;
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
	private Boolean isActive = Boolean.TRUE;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
	private String percentage;
	private Integer age;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfBirth;
}
