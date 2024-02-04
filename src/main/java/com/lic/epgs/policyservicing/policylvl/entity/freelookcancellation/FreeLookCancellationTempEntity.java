package com.lic.epgs.policyservicing.policylvl.entity.freelookcancellation;

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

import com.lic.epgs.policyservicing.common.entity.PolicyServiceDocumentTempEntity;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceEntity;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesTempEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity 
@Table(name = "POLICY_FLC_TEMP")
public class FreeLookCancellationTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FREELOOKCANCEL_ID_SEQUENCE")
	@SequenceGenerator(name = "FREELOOKCANCEL_ID_SEQUENCE", sequenceName = "FREELOOKCANCEL_ID_SEQ", allocationSize = 1)
	@Column(name = "FREELOOK_ID")
	private Long freeLookId;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "FREELOOK_ID", referencedColumnName = "FREELOOK_ID")
	private Set<PolicyServiceDocumentTempEntity> documents = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "FREELOOK_ID", referencedColumnName = "FREELOOK_ID")
	private Set<PolicyServiceNotesTempEntity> notes = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
	private Set<PolicyServiceEntity> service = new HashSet<>();
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@Column(name = "SERVICE_ID")
	private Long serviceId;
	
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

	@Column(name = "POLICY_STATUS")
	private String policyStatus;

	@Column(name = "POLICY_NO")
	private String policyNumber;

	@Column(name = "SERVICE_STATUS")
	private String serviceStatus;

	@Column(name = "SERVICE_NUMBER")
	private String serviceNumber;

	@Column(name = "POLICY_CANCEL_REQ_NO")
	private String policyCancellationRequestNumber;

	@Column(name = "FREELOOK_STATUS")
	private String freeLookStatus;

	@Column(name = "WORKFLOW_STATUS")
	private String workflowStatus;

	@Column(name = "REQ_RECEIVED_FROM")
	private String requestReceivedFrom;

	@Column(name = "CANCEL_REQ_DT")
	private Date cancelRequestDate;

	@Column(name = "POLICY_BOND_DISPATCH_DT")
	private Date policyBondDispatchDate;

	@Column(name = "POLICY_BOND_RECV_DT")
	private Date policyBondRecievedDate;

	@Column(name = "EFFECT_CANCEL_DT")
	private Date effectiveCancellationDate;

	@Column(name = "TOT_FUND_VAL", precision = 20, scale = 8)
	private BigDecimal totalFundValue;

	@Column(name = "TOTAL_CONTRI", precision = 20, scale = 8)
	private BigDecimal totalContribution;

	@Column(name = "POLICY_BOND_REQ_LTR_RECV")
	private String policyBondRequestLetterRecevied;

	@Column(name = "TOT_INT_ACCR", precision = 20, scale = 8)
	private BigDecimal totalIntrestAccuredOrPaid;

	@Column(name = "CLAIMS_PAID_ON")
	private Date claimsPainOnDate;

	@Column(name = "RECOVERY")
	private String reCovery;

	@Column(name = "TOT_REFUND_AMT", precision = 20, scale = 8)
	private BigDecimal totalrefundamount;

	@Column(name = "AGENT_NAME")
	private String agentName;

	@Column(name = "COMM_AMT_REVERSE", precision = 20, scale = 8)
	private BigDecimal commissionAmountToBeReversed;

	@Column(name = "OLD_FUND", precision = 20, scale = 8)
	private BigDecimal oldFundValue;

	@Column(name = "OLD_TOT_INT", precision = 20, scale = 8)
	private BigDecimal oldTotalIntrestAccuredOrPaid;

	@Column(name = "OLD_ARREAR", precision = 20, scale = 8)
	private BigDecimal oldArrearValue;

	@Column(name = "OLD_RECOVERY", precision = 20, scale = 8)
	private BigDecimal oldRecoveryValue;

	@Column(name = "COMMENT_FUND")
	private String commentFundValue;

	@Column(name = "COMMENT_TOT_INT")
	private String commentTotalIntrestAccuredOrPaid;

	@Column(name = "COMMENT_ARREAR")
	private String commentArrearValue;

	@Column(name = "COMMENT_RECOVERY")
	private String commentRecoveryValue;

	@Column(name = "NEW_FUND", precision = 20, scale = 8)
	private BigDecimal newFundValue;

	@Column(name = "NEW_TOT_INT", precision = 20, scale = 8)
	private BigDecimal newTotalIntrestAccuredOrPaid;

	@Column(name = "NEW_ARREAR", precision = 20, scale = 8)
	private BigDecimal newArrearValue;

	@Column(name = "NEW_RECOVERY", precision = 20, scale = 8)
	private BigDecimal newRecoveryValue;

	@Column(name = "UNIT_OFFICE")
	private String unitOffice;

	@Column(name = "UNIT_CODE")
	private String unitCode;

	@Column(name = "MPH_CODE")
	private String mphCode;

	@Column(name = "MPH_NAME")
	private String mphName;

	@Column(name = "PRODUCT")
	private String product;

	@Column(name = "LINE_OF_BUSINESS")
	private String lineOfBusiness;

	@Column(name = "POLICY_FROM_DT")
	private String policyFromDate;

	@Column(name = "POLICY_TO_DT")
	private String policyToDate;
	
	@Column(name = "REJECTION_REASON_CODE")
	private String rejectionReasonCode;
	
	@Column(name = "REJECTION_REMARKS")
	private String rejectionRemarks;

}