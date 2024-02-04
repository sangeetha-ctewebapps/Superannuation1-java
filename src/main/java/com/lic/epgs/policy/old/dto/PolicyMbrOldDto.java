/**
 * 
 */
package com.lic.epgs.policy.old.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
public class PolicyMbrOldDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long memberId;
	private Set<PolicyMbrAddressOldDto> address = new HashSet<>();
	private Set<PolicyMbrBankOldDto> bank = new HashSet<>();
	private Set<PolicyMbrNomineeOldDto> nominee = new HashSet<>();
	private Set<PolicyMbrApponinteeOldDto> appointee = new HashSet<>();
	
	
	private Long aadharNumber;
	private String annuityOption;
	private Integer category;
	private String communicationPreference;
	private String createdBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfBirth;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfJoining;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfJoiningScheme;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfRetirement;
	
	private String designation;
	private String emailId;
	private BigDecimal employeeContribution;
	private BigDecimal employerContribution;
	private BigDecimal voluntaryContribution;
	private String fatherName;
	private String firstName;
	private Integer frequency;
	private String gender;
	private Boolean isActive;
	private String languagePreference;
	private String lastName;
	private String licId;
	private String memberShipId;
	private String memberStatus;
	private String membershipNumber;
	private String middleName;
	private String modifiedBy;
	private String pan;
	private Long phone;
	private Long quotationId;
	private String role;
	private String spouseName;
	private BigDecimal totalContribution;
	private BigDecimal totalInterestedAccured;
	private String typeofMemebership;
	private String policyNo;
	private Long policyId;
	private String policyStatus;
	private String mphCode;
	private String mphName;
	private String product;
	private String lineOfBusiness;
	private String unitCode;
	private String serviceNo;
	private Long serviceId;
	private String serviceStatus;
	private String proposalNumber;
	private String maritalStatus;
	private Long conversionId;


	private Boolean isZeroId = Boolean.FALSE;
	private String zeroId;
	private String zeroIdStatus;
	private BigDecimal zeroAvailableAmount= BigDecimal.ZERO;
	private BigDecimal zeroUsedAmount= BigDecimal.ZERO;
	private PolicyDto policyDto;
	
	
	
}
