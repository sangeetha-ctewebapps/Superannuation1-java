package com.lic.epgs.policyservicing.policylvl.entity.merger;

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

import com.lic.epgs.policyservicing.common.entity.PolicyServiceDocumentEntity;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
*
* @author Narayanan
*
*/

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "POLICY_MERGE")
public class PolicyLevelMergerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POLICY_MERGE_ID_SEQUENCE")
	@SequenceGenerator(name = "POLICY_MERGE_ID_SEQUENCE", sequenceName = "POLICY_MERGE_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "MERGE_ID")
	private Long mergeId;
	@Column(name = "SERVICE_ID")
	private Long serviceId;
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@OneToMany(cascade = CascadeType.ALL,  fetch = FetchType.EAGER)
	@JoinColumn(name = "MERGE_ID", referencedColumnName = "MERGE_ID")
	private Set<PolicyServiceDocumentEntity> docs = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MERGE_ID", referencedColumnName = "MERGE_ID")
	private Set<PolicyServiceNotesEntity> notes = new HashSet<>();
	
	@Column(name = "EFFECTIVE_DT")
	private Date effectiveDt;
	@Column(name = "REQ_RECVD_DT")
	private Date reqRecvdDt;
	@Column(name = "MERGER_TYPE")
	private Long mergerType;
	@Column(name = "MERGING_POLICY")
	private String mergingPolicy;
	@Column(name = "DESTINATION_POLICY")
	private String destinationPolicy;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATED_ON")
	private Date createdOn;
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;
	@Column(name = "MERGE_STATUS")
	private Integer mergeStatus;
	@Column(name = "WORKFLOW_STATUS")
	private Integer workflowStatus;
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	@Column(name = "UNIT_CODE")
	private String unitCode;
	
	@Column(name = "REJECTION_REASON_CODE")
	private String rejectionReasonCode;
	@Column(name = "REJECTION_REMARKS")
	private String rejectionRemarks;
	
	@Column(name="POLICY_NUMBER")
	private String policyNumber;

	@Column(name = "MPH_CODE")
	private String mphCode;

	@Column(name = "MPH_NAME")
	private String mphName;
	
	@Column(name = "PRODUCT")
	private String product;
	
	@Column(name = "DESTINATION_POLICY_TYPE")
	private Long destinationPolicyType;
}
