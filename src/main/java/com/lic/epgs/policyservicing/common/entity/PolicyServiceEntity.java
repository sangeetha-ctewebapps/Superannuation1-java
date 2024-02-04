package com.lic.epgs.policyservicing.common.entity;

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

import com.lic.epgs.policyservicing.memberlvl.transferout.entity.TransferInAndOutMasterEntity;
import com.lic.epgs.policyservicing.policylvl.entity.conversion.PolicyLevelConversionTempEntity;
import com.lic.epgs.policyservicing.policylvl.entity.freelookcancellation.FreeLookCancellationTempEntity;
import com.lic.epgs.policyservicing.policylvl.entity.merger.PolicyLevelMergerTempEntity;
import com.lic.epgs.surrender.entity.PolicySurrenderTempEntity;

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
@Table(name = "POLICY_SERVICE")
public class PolicyServiceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_SRV_SEQUENCE")
	@SequenceGenerator(name = "POL_SRV_SEQUENCE", sequenceName = "POL_SRV_SEQ", allocationSize = 1)
	@Column(name = "SERVICE_ID")
	private Long serviceId;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
	private Set<PolicySurrenderTempEntity> surrender = new HashSet<>();
	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
//	private Set<TransferInAndOutMasterEntity> memberLevelTrnOut = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
	private Set<FreeLookCancellationTempEntity> flcTemp = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
	private Set<PolicyLevelMergerTempEntity> mergerTemp = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
	private Set<PolicyLevelConversionTempEntity> conversionTemp = new HashSet<>();

	@Column(name = "SERVICE_NUMBER")
	private String serviceNumber;

	@Column(name = "SERVICE_TYPE")
	private String serviceType;

	@Column(name = "SERVICE_DONE_BY")
	private String serviceDoneBy;

	@Column(name = "SERVICE_EFFECTIVE_DATE")
	private Date serviceEffectiveDate = new Date();

	@Column(name = "REQUEST_RECEIVED_BY")
	private String requestReceivedBy;

	@Column(name = "REQUEST_RECEIVED_DATE")
	private Date requestReceivedDate = new Date();

	@Column(name = "SERVICE_STATUS")
	private String serviceStatus;

	@Column(name = "WORKFLOW_STATUS")
	private String workflowStatus;

	@Column(name = "POLICY_ID")
	private Long policyId;

	@Column(name = "IS_Using")
	private Boolean isUsing;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "UNIT_CODE")
	private String unitCode;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();
	


}
