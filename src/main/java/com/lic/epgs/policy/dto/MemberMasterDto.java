package com.lic.epgs.policy.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberMasterDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long memberId;
	private Long masterMemberId;
	private Long tempMemberId;
	private Long aadharNumber;
	private Integer categoryNo;
	private String communicationPreference;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfBirth;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfJoining;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfJoiningScheme;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfRetrirement;
	private String designation;
	private String emailid;
	private String fatherName;
	private String firstName;
	private String gender;
	private String languagePreference;
	private String lastName;
	private String licId;
	private String maritalStatus;
	private String membershipNumber;
	private String memberStatus;
	private String middleName;
	private String memberPan;
	private Long landlineNo;
	private Long mobileNo;
	private Long policyId;
	private String spouseName;
	private String typeOfMemebership;
	private Boolean isZeroid;
	private Boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;

	private Set<MemberAddressDto> memberAddress = new HashSet<>();
	private Set<MemberBankDto> memberBank = new HashSet<>();
	private Set<MemberNomineeDto> memberNominee = new HashSet<>();
	private Set<MemberAppointeeDto> memberAppointee = new HashSet<>();
	private Set<MemberContributionDto> memberContribution = new HashSet<>();
	private Set<MemberContributionSummaryDto> memberContributionSummary = new HashSet<>();
	private Set<MemberTransactionEntriesDto> memberTransactionEntries = new HashSet<>();
	private Set<MemberTransactionSummaryDto> memberTransactionSummary = new HashSet<>();
	
}