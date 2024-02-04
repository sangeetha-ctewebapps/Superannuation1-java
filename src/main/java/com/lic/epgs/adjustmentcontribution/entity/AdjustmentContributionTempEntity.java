package com.lic.epgs.adjustmentcontribution.entity;

/**
 * @author pradeepramesh
 *
 */
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
@Table(name = "ADJUSTMENT_CONTRIBUTION_TEMP")
public class AdjustmentContributionTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADJ_CON_ID_T_SEQUENCE")
	@SequenceGenerator(name = "ADJ_CON_ID_T_SEQUENCE", sequenceName = "ADJ_CON_ID_T_SEQ", allocationSize = 1)
	@Column(name = "ADJ_CON_ID")
	private Long adjustmentContributionId;

	@Column(name = "ADJ_CON_NUMBER")
	private String adjustmentContributionNumber;

	@Column(name = "ADJ_CON_STATUS")
	private String adjustmentContributionStatus;
	@Column(name = "WORKFLOW_STATUS")
	private String workFlowStatus;

	@Column(name = "UNIT_CODE")
	private String unitCode;

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

	// FOR REJECT
	@Column(name = "REJECTION_REASON_CODE")
	private String rejectionReasonCode;
	@Column(name = "REJECTION_REMARKS")
	private String rejectionRemarks;

	@Column(name = "POLICY_ID")
	private Long policyId;
	@Column(name = "TEMP_POLICY_ID")
	private Long tempPolicyId;
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	@Column(name = "POLICY_STATUS")
	private String policyStatus;
	@Column(name = "POLICY_TYPE")
	private String policyType;

	@Column(name = "POLICY_COMMENCEMENT_DT")
	private Date policyCommencementDate;
	@Column(name = "IS_COMMENCEMENT_DATE_ONEYR")
	private Boolean isCommencementdateOneYr;

	@Column(name = "CUSTOMER_ID")
	private Integer customerId;
	@Column(name = "CUSTOMER_CODE")
	private String customerCode;
	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	@Column(name = "MPH_NAME")
	private String mphName;
	@Column(name = "MPH_CODE")
	private String mphCode;
	@Column(name = "PROPOSAL_NUMBER")
	private String proposalNumber;

	@Column(name = "PRODUCT")
	private String product;
	@Column(name = "VARIANT")
	private String variant;

	@Column(name = "TOTAL_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal totalContribution = BigDecimal.ZERO;
	@Column(name = "EMPLOYER_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal employerContribution = BigDecimal.ZERO;
	@Column(name = "EMPLOYEE_CONTRIBUTION ", precision = 20, scale = 8)
	private BigDecimal employeeContribution = BigDecimal.ZERO;
	@Column(name = "VOLUNTARY_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal voluntaryContribution = BigDecimal.ZERO;

	@Column(name = "TOTAL_DEPOSIT", precision = 20, scale = 8)
	private BigDecimal totalDeposit = BigDecimal.ZERO;

	@Column(name = "IS_DEPOSIT")
	private Boolean isDeposit;

	@Column(name = "ADJUSTMENT_FOR_DATE")
	private Date adjustmentForDate;
	@Column(name = "AMT_TO_bE_ADJUSTED", precision = 20, scale = 8)
	private BigDecimal amountToBeAdjusted = BigDecimal.ZERO;

	@Column(name = "FIRST_PREMIUM", precision = 20, scale = 8)
	private BigDecimal firstPremium = BigDecimal.ZERO;
	@Column(name = "SINGLE_PREMIUM_FIRSTYR", precision = 20, scale = 8)
	private BigDecimal singlePremiumFirstYr = BigDecimal.ZERO;

	@Column(name = "RENEWAL_PREMIUM", precision = 20, scale = 8)
	private BigDecimal renewalPremium = BigDecimal.ZERO;
	@Column(name = "SUBSEQUENT_SINGLE_PREMIUM", precision = 20, scale = 8)
	private BigDecimal subsequentSinglePremium = BigDecimal.ZERO;


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "ADJ_CON_ID", referencedColumnName = "ADJ_CON_ID")
	private Set<AdjustmentContributionNotesTempEntity> adjustmentContributionNotes = new HashSet<>();

	@Column(name = "BATCH_ID", length = 10)
	private Long batchId;
	
	@Column(name = "ADJUSTMENT_DUE_DATE")
	private Date adjustmentDueDate;
	
	@Column(name = "EFFECTIVE_DATE")
	private Date effectiveDate;
	
	
}