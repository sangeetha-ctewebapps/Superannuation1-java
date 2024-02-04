/**
 * 
 */
package com.lic.epgs.policyservicing.memberlvl.transferout.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesTempEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Karthick M
 *
 */


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TRANSFER_IN_OUT_MASTER_TEMP")
public class TransferInAndOutMasterTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRNSFR_IN_OUT_MASTER_TEMP_SEQ")
	@SequenceGenerator(name = "TRNSFR_IN_OUT_MASTER_TEMP_SEQ", sequenceName = "TRNSFR_IN_OUT_MASTER_TEMP_SEQ", allocationSize = 1)
	@Column(name = "TRNSFR_ID", length = 10)
	private Long trnsfrId;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "transferoutTemp")
	private TransferInAndOutMemberTempEntity transOutMembers;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "TRNSFR_ID", referencedColumnName = "TRNSFR_ID")
	private List<PolicyServiceNotesTempEntity> notes = new ArrayList<>();
	
	@Column(name = "SERVICE_ID")
	private Long serviceId;
	
	@Column(name = "SERVICE_NUMBER", length = 10)
	private String serviceNo;
	
	@Column(name = "SOURCE_POLICY_ID", length = 10)
	private Long sourcePolicyId;
	@Column(name = "SOURCE_POLICY_NO", length = 20)
	private String sourcePolicyNo;
	
	@Column(name = "DEST_POLICY_ID", length = 10)
	private Long destPolicyId;
	@Column(name = "DEST_POLICY_NO", length = 20)
	private String destPolicyNo;
	
	@Column(name = "INTEREST_ACCRUED", length = 10)
	private Long interestAccrued;
	@Column(name = "CREATED_BY", length = 10)
	private String createdBy;
	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();
	@Column(name = "MODIFIED_BY", length = 10)
	private String modifiedBy;
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();
	
	
	
	@Column(name = "TRNSFR_STATUS", length = 20)
	private String trnsfrStatus;
	@Column(name = "WORKFLOW_STATUS", length = 10)
	private Integer workflowStatus;
	
	
	@Column(name = "TRNSFR_AMOUNT", length = 10)
	private Long trnsfrAmount;
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "UNIT_CODE")
	private String unitCode;
	
	@Column(name = "DES_UNIT_CODE")
	private String desUnitCode;
	@Column(name = "SRC_UNIT_CODE")
	private String srcUnitCode;
	
	@Column(name = "REQ_RECEIVED_DATE")
	private Date reqReceivedDate;
	
	@Column(name = "SERVICE_EFFECTIVE_DATE")
	private Date serviceEffectiveDate;
	
	@Column(name = "REJECTION_REASON_CODE")
	private String rejectionReasonCode;
	
	@Column(name = "REJECTION_REMARKS")
	private String rejectionRemarks;
	
	@Column(name = "SRC_TRANSFER_ID")
	private Long srcTransferId;

}
