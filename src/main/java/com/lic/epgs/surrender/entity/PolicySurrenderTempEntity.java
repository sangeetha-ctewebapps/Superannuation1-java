package com.lic.epgs.surrender.entity;

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

import com.lic.epgs.common.entity.CommonDocsTempEntity;
import com.lic.epgs.common.entity.CommonNoteTempEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "POLICY_SURRENDER_TMP")
public class PolicySurrenderTempEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POLICY_SURRENDER_TEMP_SEQUENCE")
	@SequenceGenerator(name = "POLICY_SURRENDER_TEMP_SEQUENCE", sequenceName = "POLICY_SURRENDER_TEMP_SEQUENCE", allocationSize = 1)
	@Column(name = "SURRENDER_ID", length = 10)
	private Long surrenderId;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "SURRENDER_ID", referencedColumnName = "SURRENDER_ID")
	private Set<CommonDocsTempEntity> docs = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "SURRENDER_ID", referencedColumnName = "SURRENDER_ID")
	private Set<CommonNoteTempEntity> notes = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "SURRENDER_ID", referencedColumnName = "SURRENDER_ID")
	private Set<PolicySurrenderPayOutTempEntity> surrenderPayouts = new HashSet<>();
	
	@Column(name = "SURRENDER_NO", length = 20)
	private String surrenderNo;
	@Column(name = "POLICY_ID", length = 10)
	private Long policyId;
	@Column(name = "SERVICE_ID")
	private Long serviceId;
	@Column(name = "SURRENDER_STATUS", length = 10)
	private String surrenderStatus;
	@Column(name = "SURRENDER_EFFCT_DT")
	private Date surrenderEffctDt;
	@Column(name = "SURRENDER_REASON", length = 200)
	private String surrenderReason;
	@Column(name = "REQ_RECV_DT")
	private Date reqRecvDt;
	@Column(name = "SURRENDER_TYPE", length = 3)
	private Long surrenderType;
	@Column(name = "POLICY_NO", length = 20)
	private String policyNo;
	@Column(name = "REQ_RECVD_FROM", length = 10)
	private Long reqRecvdFrom;
	@Column(name = "SURRENDER_DONE_BY", length = 10)
	private Long surrenderDoneBy;
	@Column(name = "ANNUAL_RENEW_DT")
	private Date annualRenewDt;
	@Column(name = "LAST_PAID_DT")
	private Date lastPaidDt;
	@Column(name = "NEXT_DUE_DT")
	private Date nextDueDt;
	@Column(name = "LAST_AMT_PAID", length = 20)
	private Long lastAmtPaid;
	@Column(name = "NEXT_AMT_PAID", length = 20)
	private Long nextAmtPaid;
	@Column(name = "AGING", length = 10)
	private String AGING;
	@Column(name = "WAIT_PERIOD", length = 10)
	private Long waitPeriod;
	@Column(name = "NOTICE_PERIOD", length = 10)
	private Long noticePeriod;
	@Column(name = "CREATED_BY", length = 10)
	private String createdBy;
	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();
	@Column(name = "MODIFIED_BY", length = 10)
	private String modifiedBy;
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "MPH_CODE")
	private String mphCode;

	@Column(name = "MPH_NAME")
	private String mphName;

	@Column(name = "PRODUCT")
	private String product;
	
	@Column(name = "UNIT_CODE")
	private String unitCode;
}
