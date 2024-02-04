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
@Table(name = "MPH_MASTER")
public class MphMasterEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MPH_M_SEQUENCE")
	@SequenceGenerator(name = "MPH_M_SEQUENCE", sequenceName = "MPH_M_SEQ", allocationSize = 1)
	@Column(name = "MPH_ID ")
	private Long mphId;
	
	@Column(name = "TEMP_MPH_ID ")
	private Long tempMphId;
	
	@Column(name = "MPH_CODE ")
	private String mphCode;
	@Column(name = "MPH_NAME ")
	private String mphName;
	@Column(name = "MPH_TYPE ")
	private String mphType;
	@Column(name = "PROPOSAL_NUMBER ")
	private String proposalNumber;
	@Column(name = "PROPOSAL_ID ")
	private String proposalId;
	@Column(name = "CIN ")
	private String cin;
	@Column(name = "PAN")
	private String pan;
	@Column(name = "ALTERNATE_PAN ")
	private String alternatePan;
	@Column(name = "LANDLINE_NO ")
	private Long landlineNo;
	@Column(name = "COUNTRY_CODE ")
	private String countryCode;
	@Column(name = "MOBILE_NO ")
	private Long mobileNo;
	@Column(name = "EMAIL_ID")
	private String emailId;
	@Column(name = "FAX ")
	private Integer fax;
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
	@JoinColumn(name = "MPH_ID", referencedColumnName = "MPH_ID")
	private Set<MphAddressEntity> mphAddress = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MPH_ID", referencedColumnName = "MPH_ID")
	private Set<MphBankEntity> mphBank = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MPH_ID", referencedColumnName = "MPH_ID")
	private Set<MphRepresentativesEntity> mphRepresentative = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MPH_ID", referencedColumnName = "MPH_ID")
	private Set<PolicyMasterEntity> policyMaster = new HashSet<>();
}
