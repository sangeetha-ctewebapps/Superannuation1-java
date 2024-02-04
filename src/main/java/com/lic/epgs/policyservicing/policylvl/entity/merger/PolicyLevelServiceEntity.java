package com.lic.epgs.policyservicing.policylvl.entity.merger;

import java.util.Date;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@Entity
//@Table(name = "POLICY_SRV")
public class PolicyLevelServiceEntity {
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POLICY_SRV_SEQUENCE")
//	@SequenceGenerator(name = "POLICY_SRV_SEQUENCE", sequenceName = "POLICY_SRV_SEQ", allocationSize = 1)
	@Column(name = "SERVICE_ID")
	private Long serviceId;
	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
//	private Set<PolicySurrenderTempEntity> surrender = new HashSet<>();
//	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
//	private Set<TransferInAndOutMasterEntity> memberLevelTrnOut = new HashSet<>();
//	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
//	private Set<FreeLookCancellationTempEntity> flcTemp = new HashSet<>();
//	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
//	private Set<PolicyLevelMergerTempEntity> mergerTemp = new HashSet<>();
	
	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
//	private Set<PolicyMbrInprogressEntity> additionOfMemberTemp = new HashSet<>();
		
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
//	private Set<policyDetailsChangeTempEntity> policyDetailChangeTemp = new HashSet<>();
	
	@Column(name = "SERVICE_NO")
	private String serviceNo;
	
	@Column(name = "SERVICE_STATUS")
	private Integer serviceStatus;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;
	
	@Column(name = "SRV_EFFECTIVE_DT")
	private Date srvEffectiveDt;
	
	@Column(name = "WORKFLOW_STATUS")
	private Integer workflowStatus;
	
	@Column(name = "SERVICE_TYPE")
	private Long serviceType;
	
	@Column(name = "UNIT_ID")
	private Long unitId;
	
	@Column(name = "REQ_RECVD_BY")
	private String reqRecvdBy;
	
	@Column(name = "REQ_RECVD_DT")
	private Date reqRecvdDt;
	
	@Column(name = "SRV_DONE_BY")
	private String srvDoneBy;
}
