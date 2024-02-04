package com.lic.epgs.claim.temp.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TEMP_CLAIM_MBR")
public class TempClaimMbrEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TCLAIM_MBR_SEQUENCE")
	@SequenceGenerator(name = "TCLAIM_MBR_SEQUENCE", sequenceName = "TCLAIM_MBR_SEQUENCE", allocationSize = 1)
	@Column(name = "MEMBER_ID", length = 10)
	private Long memberId;

	@Column(name = "POLICY_MEMBER_SHIP_ID", length = 50)
	private Long policyMemberId;

	@Column(name = "MEMBER_SHIP_ID", length = 50)
	private String memberShipId;

	@Column(name = "CLAIM_NO", length = 30)
	private String claimNo;

	@Column(name = "LIC_ID", length = 255)
	private String licId;

	@Column(name = "MEMBER_STATUS", length = 20)
	private String memberStatus;

	@Column(name = "FIRSTNAME", length = 50)
	private String firstName;

	@Column(name = "MIDDLE_NAME", length = 50)
	private String middleName;

	@Column(name = "LAST_NAME", length = 50)
	private String lastName;

	@Column(name = "FATHER_NAME", length = 50)
	private String fatherName;

	@Column(name = "SPOUSE_NAME", length = 50)
	private String spouseName;

	@Column(name = "DATEOFBIRTH")
	private Date dateOfBirth;

	@Column(name = "GENDER", length = 10)
	private String gender;

	@Column(name = "PAN", length = 10)
	private String pan;

	@Column(name = "AADHAR_NUMBER", length = 12)
	private Long aadharNumber;

	@Column(name = "DATEOFJOINING")
	private Date dateOfJoining;

	@Column(name = "DATEOFJOINING_SCHEME")
	private Date dateOfJoiningScheme;

	@Column(name = "DATEOF_RETRIREMENT")
	private Date dateOfRetirement;

	@Column(name = "DESIGNATION", length = 50)
	private String designation;

	@Column(name = "ROLE", length = 50)
	private String role;

	@Column(name = "MEMBERSHIP_NUMBER", length = 20)
	private String membershipNumber;

	@Column(name = "TYPEOF_MEMEBERSHIP", length = 20)
	private String typeOfMembershipNo;

	@Column(name = "PHONE", length = 10)
	private Long phone;

	@Column(name = "EMAILID", length = 50)
	private String emailId;

	@Column(name = "LANGUAGE_PREFERENCE", length = 50)
	private String languagePreference;

	@Column(name = "COMMUNICATION_PREFERENCE", length = 50)
	private String communicationPreference;

	@Column(name = "CATEGORY", length = 10)
	private Integer category;

	@Column(name = "EMPLOYER_CONTRIBUTION", precision = 20, scale = 2)
	private Double employerContribution;

	@Column(name = "EMPLOYEE_CONTRIBUTION", precision = 20, scale = 2)
	private Double employeeContribution;

	@Column(name = "VOLUNTARY_CONTRIBUTION", precision = 20, scale = 2)
	private Double voluntaryContribution;

	@Column(name = "TOTAL_INTEREST_ACCURED", precision = 20, scale = 2)
	private Double totalInterestAccured;

	@Column(name = "NO_OF_ANNUITY", length = 10)
	private Integer noOfAnnuity;

	@Column(name = "TOTAL_CONTRIBUTION", precision = 20, scale = 2)
	private Double totalContribution;

	@Column(name = "ANNUITY_OPTION", length = 50)
	private String annuityOption;

	@Column(name = "TOTAL_INTERESTED_ACCURED", precision = 20, scale = 2)
	private Double totalInterestedAccured;

	@Column(name = "FREQUENCY", length = 10)
	private Integer frequency;

	@Column(name = "COMM_CALC_TYP", length = 100)
	private String calculationType;

	@Column(name = "COMM_CALC_AMOUNT", precision = 20, scale = 2)
	private Double calculationAmount;

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "MODIFIED_BY", length = 100)
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@Column(name = "IS_ACTIVE", length = 10)
	private Boolean isActive;

	@Column(name = "CORPUS_PERCENTAGE")
	private Double corpusPercentage;
	
	@Column(name = "DB_CALC_VALUE", length = 10)
	private Integer dbCalcValue;

	@Column(name = "DB_PENSION", precision = 20, scale = 2)
	private Double dbPension;

	@Column(name = "DB_PURCHASE_PRICE", precision = 20, scale = 2)
	private Double dbPurchasePrice;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_ID", referencedColumnName = "CLAIM_ID")
	private TempClaimEntity claim;

	@OneToMany(mappedBy = "claimMbrEntity")
	private List<TempClaimMbrNomineeEntity> claimMbrNomineeDtls;

	@OneToMany(mappedBy = "claimMbrEntity")
	private List<TempClaimMbrAppointeeEntity> claimMbrAppointeeDtls;

	@OneToMany(mappedBy = "claimMbrEntity")
	private List<TempClaimCommutationCalcEntity> claimCommutationCalc;

	@OneToMany(mappedBy = "claimMbrEntity")
	private List<TempClaimAnnuityCalcEntity> claimAnuityCalc;

	@OneToMany(mappedBy = "claimMbrEntity")
	private List<TempClaimMbrAddressEntity> claimMbrAddresses;

	@OneToMany(mappedBy = "claimMbrEntity")
	private List<TempClaimMbrBankDetailEntity> claimMbrBankDetails;

	@OneToMany(mappedBy = "claimMbrEntity")
	private List<TempClaimMbrFundValueEntity> claimMbrFundValue;

	@OneToMany(mappedBy = "claimMbrEntity")
	private List<TempClaimFundValueEntity> claimFundValue;

	@OneToMany(mappedBy = "claimMbrEntity")
	private List<ClaimPayeeTempBankDetailsEntity> claimPayeeTempBank;
	
	@Column(name = "BATCH_ID", length = 20)
	private Long batchId;

}
