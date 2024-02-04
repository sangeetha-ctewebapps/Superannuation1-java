package com.lic.epgs.policy.entity;

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

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pradeepramesh
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "POLICY_MASTER")
public class PolicyMasterEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_M_SEQUENCE")
	@SequenceGenerator(name = "POL_M_SEQUENCE", sequenceName = "POL_M_SEQUENCE", allocationSize = 1)
	@Column(name = "POLICY_ID ")
	private Long policyId;
	
	@Column(name = "TEMP_POLICY_ID ")
	private Long tempPolicyId;
	
	@Column(name = "MPH_ID ")
	private Long mphId;

	@Column(name = "PROPOSAL_ID ")
	private Integer proposalId;
	
	@Column(name = "QUOTATION_ID ")
	private Integer quotationId;
	
	@Column(name = "LEAD_ID ")
	private Integer leadId;
	
	@Column(name = "POLICY_NUMBER ")
	private String policyNumber;
	
	@Column(name = "POLICY_STATUS ")
	private String policyStatus;
	
	@Column(name = "POLICY_TYPE")
	private String policyType;
	
	@Column(name = "UNIT_ID ")
	private String unitId;
	
	@Column(name = "UNIT_OFFICE ")
	private String unitOffice;
	
	@Column(name = "STAMP_DUTY")
	private BigDecimal stampDuty;
	
	@Column(name = "NO_OF_CATEGORY ")
	private Integer noOfCategory;
	
	@Column(name = "CONTRIBUTION_FREQUENCY ")
	private Integer contributionFrequency;
		
	@Column(name = "INTERMEDIARY_OFFICER_CODE ")
	private String intermediaryOfficerCode;
	
	@Column(name = "INTERMEDIARY_OFFICER_NAME ")
	private String intermediaryOfficerName;
			
	@Column(name = "MARKETING_OFFICER_CODE")
	private String marketingOfficerCode;
	
	@Column(name = "MARKETING_OFFICER_NAME ")
	private String marketingOfficerName;

	@Column(name = "LINE_OF_BUSINESS ")
	private String lineOfBusiness;
	
	@Column(name = "PRODUCT_ID ")
	private Long productId;

	@Column(name = "VARIANT ")
	private String variant;
	
	@Column(name = "TOTAL_MEMBER")
	private Integer totalMember;
	
	@Column(name = "CON_TYPE ")
	private String conType;
	
	@Column(name = "ADVANCEOTARREARS")
	private String advanceotarrears;
	
	@Column(name = "POLICY_DISPATCH_DATE ")
	private Date policyDispatchDate;
	
	@Column(name = "POLICY_RECIEVED_DATE")
	private Date policyRecievedDate;
	
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	@Column(name = "ARD ")
	private Date ard;
	
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	@Column(name = "POLICY_COMMENCEMENT_DT ")
	private Date policyCommencementDt;
	
	@Column(name = "IS_COMMENCEMENT_DATE_ONEYR")
	private Boolean isCommencementdateOneYr;
	
	@Column(name = "ADJUSTMENT_DT ")
	private Date adjustmentDt;
	
	@Column(name = "AMT_TO_bE_ADJUSTED", precision = 20, scale = 8)
	private BigDecimal amountToBeAdjusted= BigDecimal.ZERO;
	
	@Column(name = "FIRST_PREMIUM", precision = 20, scale = 8)
	private BigDecimal firstPremium= BigDecimal.ZERO;
	
	@Column(name = "SINGLE_PREMIUM_FIRSTYR", precision = 20, scale = 8)
	private BigDecimal singlePremiumFirstYr= BigDecimal.ZERO;
	
	@Column(name = "RENEWAL_PREMIUM", precision = 20, scale = 8)
	private BigDecimal renewalPremium= BigDecimal.ZERO;
	
	@Column(name = "SUBSEQUENT_SINGLE_PREMIUM", precision = 20, scale = 8)
	private BigDecimal subsequentSinglePremium= BigDecimal.ZERO;
	
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
	
	@Column(name = "WORKFLOW_STATUS ")
	private String workflowStatus;
	
	@Column(name = "REJECTION_REASON_CODE ")
	private String rejectionReasonCode;
	
	@Column(name = "REJECTION_REMARKS ")
	private String rejectionRemarks;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyValuationEntity> valuations = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyRulesEntity> rules = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyDepositEntity> deposits = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyContributionEntity> policyContributions = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyContributionSummaryEntity> policyContributionSummary = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyTransactionEntriesEntity> policyTransactionEntries = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyTransactionSummaryEntity> policyTransactionSummary = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<ZeroAccountEntity> zeroAccount = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<ZeroAccountEntriesEntity> zeroAccountEntries = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyNotesEntity> notes = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<MemberMasterEntity> memberMaster = new HashSet<>();

}