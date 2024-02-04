package com.lic.epgs.quotation.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "QUO_MBR")
public class QuotationMemberEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUOTATION_MEMBER_SEQ")
	@SequenceGenerator(name = "QUOTATION_MEMBER_SEQ", sequenceName = "QUOTATION_MEMBER_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_ID")
	private Long memberId;

	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Set<QuotationMemberAddressEntity> quotationMemberAddresses = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Set<QuotationMemberBankDetailEntity> quotationMemberBankDetails = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Set<QuotationMemberNomineeEntity> quotationMemberNominees = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Set<QuotationMemberAppointeeEntity> quotationMemberAppointees = new HashSet<>();
	
	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@Fetch(value = FetchMode.SUBSELECT)
//	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
//	private List<QuotationMemberAddressEntity> quotationMemberAddresses = new ArrayList<>();
//
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@Fetch(value = FetchMode.SUBSELECT)
//	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
//	private List<QuotationMemberBankDetailEntity> quotationMemberBankDetails = new ArrayList<>();
//
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@Fetch(value = FetchMode.SUBSELECT)
//	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
//	private List<QuotationMemberNomineeEntity> quotationMemberNominees = new ArrayList<>();
//
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@Fetch(value = FetchMode.SUBSELECT)
//	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
//	private List<QuotationMemberAppointeeEntity> quotationMemberAppointees = new ArrayList<>();

	@Column(name = "MEMBER_SHIP_ID")
	private String memberShipId;
	@Column(name = "MEMBERSHIP_NUMBER")
	private String membershipNumber;
	@Column(name = "MEMBER_STATUS")
	private String memberStatus;
	@Column(name = "TYPE_OF_MEMEBERSHIP")
	private String typeOfMembershipNo;

	@Column(name = "QUOTATION_ID")
	private Integer quotationId;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	@Column(name = "POLICY_NO")
	private String policyNo;
	@Column(name = "POLICY_STATUS")
	private String policyStatus;
	
	@Column(name = "MPH_CODE")
	private String mphCode;
	@Column(name = "MPH_NAME")
	private String mphName;
	@Column(name = "FREQUENCY")
	private Integer frequency;
	@Column(name = "CATEGORY")
	private Integer category;
	@Column(name = "ANNUITY_OPTION")
	private String annuityOption;
	@Column(name = "PRODUCT")
	private String product;
	
	@Column(name = "LIC_ID")
	private String licId;
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "MIDDLE_NAME")
	private String middleName;
	@Column(name = "LAST_NAME")
	private String lastName;
	@Column(name = "FATHER_NAME")
	private String fatherName;
	@Column(name = "SPOUSE_NAME")
	private String spouseName;
	@Column(name = "MARITAL_STATUS")
	private String maritalStatus;
	
	@Column(name = "DATE_OF_BIRTH")
	private Date dateOfBirth;
	@Column(name = "GENDER")
	private String gender;

	@Column(name = "DATE_OF_JOINING")
	private Date dateOfJoining;
	@Column(name = "DATE_OF_JOINING_SCHEME")
	private Date dateOfJoiningScheme;
	@Column(name = "DATE_OF_RETRIREMENT")
	private Date dateOfRetirement;
	
	@Column(name = "DESIGNATION")
	private String designation;
	@Column(name = "ROLE")
	private String role;
	
	@Column(name = "PHONE")
	private Long phone;
	@Column(name = "EMAILID")
	private String emailId;
	@Column(name = "LANGUAGE_PREFERENCE")
	private String languagePreference;
	@Column(name = "COMMUNICATION_PREFERENCE")
	private String communicationPreference;
	
	@Column(name = "PAN")
	private String pan;
	@Column(name = "AADHAR_NUMBER")
	private Long aadharNumber;
	
	@Column(name = "EMPLOYER_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal employerContribution= BigDecimal.ZERO;
	@Column(name = "EMPLOYEE_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal employeeContribution= BigDecimal.ZERO;
	@Column(name = "VOLUNTARY_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal voluntaryContribution= BigDecimal.ZERO;
	@Column(name = "TOTAL_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal totalContribution= BigDecimal.ZERO;
	
	@Column(name = "TOTAL_INTERESTED_ACCURED", precision = 20, scale = 8)
	private BigDecimal totalInterestedAccured= BigDecimal.ZERO;

	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
}
