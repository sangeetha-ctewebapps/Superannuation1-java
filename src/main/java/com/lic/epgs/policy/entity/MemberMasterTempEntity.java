package com.lic.epgs.policy.entity;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "MEMBER_MASTER_TEMP")
public class MemberMasterTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "P_MBR_T_SEQUENCE")
	@SequenceGenerator(name = "P_MBR_T_SEQUENCE", sequenceName = "P_MBR_T_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_ID ")
	private Long memberId;
	
	@Column(name = "MASTER_MEMBER_ID ")
	private Long masterMemberId;
	
	@Column(name = "AADHAR_NUMBER ")
	private Long aadharNumber;
	@Column(name = "CATEGORY_NO ")
	private Integer categoryNo;
	@Column(name = "COMMUNICATION_PREFERENCE ")
	private String communicationPreference;

	@Column(name = "DATE_OF_BIRTH ")
	private Date dateOfBirth;
	@Column(name = "DATE_OF_JOINING")
	private Date dateOfJoining;
	@Column(name = "DATE_OF_JOINING_SCHEME")
	private Date dateOfJoiningScheme;
	@Column(name = "DATE_OF_RETRIREMENT")
	private Date dateOfRetrirement;
	@Column(name = "DESIGNATION")
	private String designation;
	@Column(name = "EMAILID")
	private String emailid;
//	@Column(name = "EMPLOYEE_CONTRIBUTION")
//	private BigDecimal employeeContribution;
//	@Column(name = "EMPLOYER_CONTRIBUTION")
//	private BigDecimal employerContribution;
	@Column(name = "FATHER_NAME")
	private String fatherName;
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "GENDER")
	private String gender;

	@Column(name = "LANGUAGE_PREFERENCE")
	private String languagePreference;
	@Column(name = "LAST_NAME")
	private String lastName;
	@Column(name = "LIC_ID")
	private String licId;
	@Column(name = "MARITAL_STATUS")
	private String maritalStatus;
	@Column(name = "MEMBERSHIP_NUMBER")
	private String membershipNumber;
	@Column(name = "MEMBER_STATUS")
	private String memberStatus;
	@Column(name = "MIDDLE_NAME")
	private String middleName;

	@Column(name = "MEMBER_PAN")
	private String memberPan;
	@Column(name = "LANDLINE_NO")
	private Long landlineNo;
	@Column(name = "MOBILE_NO")
	private Long mobileNo;
	@Column(name = "POLICY_ID")
	private Long policyId;
	@Column(name = "SPOUSE_NAME")
	private String spouseName;
	@Column(name = "TYPE_OF_MEMEBERSHIP")
	private String typeOfMemebership;

	@Column(name = "IS_ZEROID")
	private Boolean isZeroid=Boolean.FALSE;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Set<MemberAddressTempEntity> memberAddress = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Set<MemberBankTempEntity> memberBank = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Set<MemberNomineeTempEntity> memberNominee = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Set<MemberAppointeeTempEntity> memberAppointee = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Set<MemberContributionTempEntity> memberContribution = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Set<MemberContributionSummaryTempEntity> memberContributionSummary = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Set<MemberTransactionEntriesTempEntity> memberTransactionEntries = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Set<MemberTransactionSummaryTempEntity> memberTransactionSummary = new HashSet<>();
	
	
}
