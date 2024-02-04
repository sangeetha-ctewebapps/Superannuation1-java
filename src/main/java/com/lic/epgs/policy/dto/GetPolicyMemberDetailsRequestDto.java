package com.lic.epgs.policy.dto;

import java.sql.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class GetPolicyMemberDetailsRequestDto {

	private Integer memberId;
	
	private String licId;
	
	private String memberStatus;
	
	private String fatherName;
	
	private String firstName;
	
	private String lastName;
	
	private Date dateOfBirth;
	
	private Integer categoryNo;
	
	private Integer policyId;
	
	private String membershipNumber;
	
	private Long start;

	private Long end;

	private Integer columnSort;

}
