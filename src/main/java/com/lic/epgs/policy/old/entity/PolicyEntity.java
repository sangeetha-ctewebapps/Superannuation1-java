package com.lic.epgs.policy.old.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

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
//@Entity
//@Table(name = "POLICY")
public class PolicyEntity implements Serializable {

	private static final long serialVersionUID = 1L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POLICYID_M_SEQUENCE")
//	@SequenceGenerator(name = "POLICYID_M_SEQUENCE", sequenceName = "POLICYID_M_SEQ", allocationSize = 1)
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyRulesEntity> rules = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyAddressEntity> addresses = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyBankEntity> bankDetails = new HashSet<>();
 
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
//	private Set<PolicyDocsEntity> docs = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyNotesEntity> notes = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyDepositEntity> deposit = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
	private Set<PolicyDepositAdjustmentEntity> adjustments = new HashSet<>();
	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "SOURCE_POLICY_ID", referencedColumnName = "POLICY_ID")
//	private Set<MemberLevelTransferOutEntity> sourcePolicyId = new HashSet<>();
//	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "DEST_POLICY_ID", referencedColumnName = "POLICY_ID")
//	private Set<MemberLevelTransferOutEntity> destPolicyID = new HashSet<>();
//	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "POLICY_ID", referencedColumnName = "POLICY_ID")
//	private Set<PolicyMbrEntity> members = new HashSet<>();
	
	@Column(name = "PROPOSAL_ID")
	private Integer proposalId;
	
	@Column(name = "PROPOSAL_NAME")
	private String proposalName;	
	
	@Column(name = "PROPOSAL_NUMBER")
	private String proposalNumber;
	
	@Column(name = "LEAD_ID")
	private Integer leadId;
	
	@Column(name = "LEAD_NUMBER")
	private Integer leadNumber;
	
	@Column(name = "CUSTOMER_ID")
	private Integer customerId;

	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	@Column(name = "CUSTOMER_CODE")
	private String customerCode;
	
	@Column(name = "MPH_CODE")
	private String mphCode;

	@Column(name = "MPH_NAME")
	private String mphName;

	@Column(name = "PRODUCT")
	private String product;

	@Column(name = "VARIANT")
	private String variant;

	@Column(name = "LINE_OF_BUSINESS")
	private String lineOfBusiness;
	
	@Column(name = "MARKETING_OFFICER_NAME")
	private String marketingOfficerName;

	@Column(name = "MARKETING_OFFICER_CODE")
	private String marketingOfficerCode;

	@Column(name = "INTERMEDIARY_OFFICER_NAME")
	private String intermediaryOfficerName;

	@Column(name = "INTERMEDIARY_OFFICER_CODE")
	private String intermediaryOfficerCode;
	
	@Column(name = "UNIT_CODE")
	private String unitCode;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "POLICY_STATUS")
	private String policyStatus;

	@Column(name = "WORKFLOW_STATUS")
	private String workFlowStatus;

	@Column(name = "POLICY_COMMENCEMENT_DT")
	private Date policyCommencementDate;
	
	@Column(name = "ST_DT")
	private Date policyStartDate;
	
	@Column(name = "END_DT")
	private Date policyEndDt;

	@Column(name = "ADJUSTMENT_DT")
	private Date policyAdjustmentDate;

	@Column(name = "DEPOSIT_DATE")
	private Date policyDepositDate;
	
	//FOR ADJUSTMENT PANEL
	
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
	
	@Column(name = "IS_COMMENCEMENT_DATE_ONEYR")
	private Boolean isCommencementdateOneYr;
	

//From QUOTATION
	
	@Column(name = "QUOTATION_NO")
	private String quotationNo;

	@Column(name = "QUOTATION_ID")
	private Integer quotationId;

	@Column(name = "QUOTATION_TYPE")
	private String quotationType;

	@Column(name = "QUOTATION_DATE")
	private Date quotationDate;

	@Column(name = "VALUATION_TYPE")
	private String valuationType;

	@Column(name = "ATTRITION_RATE")
	private Integer attritionRate;

	@Column(name = "SALARY_ESCALATION")
	private Integer salaryEscalation;

	@Column(name = "DEATH_RATE")
	private Integer deathRate;

	@Column(name = "DIS_RATE_INTREST")
	private Integer disRateIntrest;

	@Column(name = "ANNUITY_OPTION")
	private String annuityOption;

	@Column(name = "ACCURAL_RATE_FACTOR")
	private Integer accuralRateFactor;

	@Column(name = "MIN_PENSION")
	private Integer minPension;

	@Column(name = "MAX_PENSION")
	private Integer maxPension;

	@Column(name = "WITHDRAWAL_RATE")
	private Integer withdrawalRate;

	@Column(name = " DIS_RATE_SALARY_ESC")
	private Integer disRateSalaryEsc;

	@Column(name = "NUMBER_OF_LIVES")
	private Integer numberOfLives;
	
	@Column(name = "QUO_PREMIUM_AMT", precision = 20, scale = 8)
	private BigDecimal quotationPremiumAmount= BigDecimal.ZERO;
	
	@Column(name = "TOTAL_MEMBER")
	private Integer totalMember;
	

	@Column(name = "TOTAL_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal totalContribution= BigDecimal.ZERO;
	@Column(name = "EMPLOYER_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal employerContribution= BigDecimal.ZERO;
	@Column(name = "EMPLOYEE_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal employeeContribution= BigDecimal.ZERO;
	@Column(name = "VOLUNTARY_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal voluntaryContribution= BigDecimal.ZERO;


	//UNKNOWN
	@Column(name = "PAN")
	private String pan;
	@Column(name = "APAN")
	private String apan;
	@Column(name = "CIN")
	private Integer cin;
	@Column(name = "FAX")
	private Integer fax;
	@Column(name = "CONTACT_NO")
	private String contactNo;
	@Column(name = "EMAIL_ID")
	private String emailId;

	//FOR MERGER
	@Column(name = "Frequency")
	private Integer frequency;
	@Column(name = "NO_OF_CATEGORY")
	private Integer category;
	@Column(name = "ARD")
	private Date ard;
	
	//For POLICY DETAILS CHANGE
	@Column(name = "POLICY_DISPATCH_DATE")
	private Date policyDispatchDate = new Date();
	@Column(name = "POLICY_RECIEVED_DATE")
	private Date policyRecievedDate = new Date();
	
	//FOR FREELOOKCANCELATION
	@Column(name = "CANCEL_REQUEST_DATE")
	private Date cancelRequestDate;
		
	//DEFAULT FOR TABLE FIELDS
	@Column(name = "TYPE")
	private String type;
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();
	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;
	
	//FOR REJECT 
	@Column(name = "REJECTION_REASON_CODE")
	private String rejectionReasonCode;
	@Column(name = "REJECTION_REMARKS")
	private String rejectionRemarks;
	
	//FOR SERVICE
	@Column(name = "SERVICE_ID")
	private Long serviceId;
	@Column(name = "SERVICE_NO")
	private String serviceNo;
	@Column(name = "SERVICE_STATUS")
	private Integer serviceStatus;
	
	
	@Column(name = "TOTAL_DEPOSIT", precision = 20, scale = 8)
	private BigDecimal totalDeposit = BigDecimal.ZERO;
	@Column(name = "STAMP_DUTY")
	private BigDecimal stampDuty= BigDecimal.ZERO;
	
	@Column(name = "CON_TYPE")
	private String contributionType;
	
	@Column(name = "ADVANCEOTARREARS")
	private String advanceOrArrears;
	

	@Column(name = "UNIT_OFFICE")
	private String unitOffice;

}