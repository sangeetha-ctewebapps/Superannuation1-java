package com.lic.epgs.policy.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class GetPolicyMemberDetailsResponseDto {

	private Integer memberId;

	private String licId;

	private String memberStatus;

	private String fatherName;

	private String firstName;

	private String lastName;

	private String dateOfBirth;

	private Integer categoryNo;
	
	private Integer policyId;
	
	private String membershipNumber;
	
	private Long totalRecords;
	
	private Long noOfPages;

}
