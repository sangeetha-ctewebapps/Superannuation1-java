package com.lic.epgs.payout.temp.entity;

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
@Table(name = "TEMP_PAYOUT_MBR")
public class TempPayoutMbrEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TPAYOUT_MBR_SEQUENCE")
	@SequenceGenerator(name = "TPAYOUT_MBR_SEQUENCE", sequenceName = "TPAYOUT_MBR_SEQUENCE", allocationSize = 1)
	@Column(name = "MEMBER_ID", length = 10)
	private Long memberId;
	
	@Column(name = "POLICY_MEMBER_SHIP_ID", length = 50)
	private Long policyMemberId;

	@Column(name = "MEMBER_SHIP_ID", length = 50)
	private String memberShipId;

	@Column(name = "PAYOUT_NO", length = 30)
	private String payoutNo;

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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAYOUT_ID", referencedColumnName = "PAYOUT_ID")
	private TempPayoutEntity payout;

	@OneToMany(mappedBy = "payoutMbrEntity")
	private List<TempPayoutMbrNomineeEntity> payoutMbrNomineeDtls;

	@OneToMany(mappedBy = "payoutMbrEntity")
	private List<TempPayoutMbrAppointeeEntity> payoutMbrAppointeeDtls;

	@OneToMany(mappedBy = "payoutMbrEntity")
	private List<TempPayoutCommutationCalcEntity> payoutCommutationCalc;

	@OneToMany(mappedBy = "payoutMbrEntity")
	private List<TempPayoutAnnuityCalcEntity> payoutAnuityCalc;

	@OneToMany(mappedBy = "payoutMbrEntity")
	private List<TempPayoutMbrAddressEntity> payoutMbrAddresses;

	@OneToMany(mappedBy = "payoutMbrEntity")
	private List<TempPayoutMbrBankDetailEntity> payoutMbrBankDetails;

	@OneToMany(mappedBy = "payoutMbrEntity")
	private List<TempPayoutMbrFundValueEntity> payoutMbrFundValue;

	@OneToMany(mappedBy = "payoutMbrEntity")
	private List<TempPayoutFundValueEntity> payoutFundValue;
	

	@OneToMany(mappedBy = "payoutMbrEntity")
	private List<PayoutPayeeBankDetailsTempEntity> payoutPayeeBank;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
}
