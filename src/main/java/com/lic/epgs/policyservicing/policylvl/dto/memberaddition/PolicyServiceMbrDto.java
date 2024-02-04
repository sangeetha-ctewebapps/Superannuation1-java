/**
 * 
 */
package com.lic.epgs.policyservicing.policylvl.dto.memberaddition;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.policyservicing.memberlvl.transferout.dto.TransferInAndOutMemberDto;

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
public class PolicyServiceMbrDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long memberId;
	private Set<PolicyServiceMbrAddressDto> address = new HashSet<>();
	private Set<PolicyServiceMbrBankDto> bank = new HashSet<>();
	private Set<PolicyServiceMbrNomineeDto> nominee = new HashSet<>();
	private Set<PolicyServiceMbrApponinteeDto> appointee = new HashSet<>();
	private Set<TransferInAndOutMemberDto> membersTrnOutEntities = new HashSet<>();
	
	
	private Long aadharNumber;
	private String annuityOption;
	private Integer category;
	private String communicationPreference;
	private String createdBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	
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
	private Long employeeContribution;
	private Long employerContribution;
	private Long voluntaryContribution;
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
	private Date modifiedOn;
	private String pan;
	private Long phone;
	private Long quotationId;
	private String role;
	private String spouseName;
	private Long totalContribution;
	private Long totalInterestedAccured;
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

}
