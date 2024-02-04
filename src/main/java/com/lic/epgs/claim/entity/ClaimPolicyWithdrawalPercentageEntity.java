package com.lic.epgs.claim.entity;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Entity
@Table(name="CLAIM_POLICY_WITHDRAWAL_PERCENTAGE")
public class ClaimPolicyWithdrawalPercentageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLM_POL_WITHDRAW_SEQ")
	@SequenceGenerator(name = "CLM_POL_WITHDRAW_SEQ", sequenceName = "CLM_POL_WITHDRAW_SEQ", allocationSize = 1)
	@Column(name="CUMULATIVE_ID")
	private Long cumulativeId;
	@Column(name="POLICY_ID")
	private Long policyId;
	@Column(name="POLICY_NO")
	private String policyNo;
	@Column(name="CLAIM_NO")
	private String claimNo;
	@Column(name="STATUS")
	private String status;
	@Column(name="MEMBER_ID")
	private Long memberId;
	@Column(name="LIC_ID")
	private String licId;
	@Column(name="MODE_OF_EXIST")
	private String modeOfExist;
	@Column(name="CREATED_ON")
	private Date createdOn;
	@Column(name="FINANCIAL_YEAR")
	private String financialYear;
	@Column(name="CLAIM_PERCENTAGE")
	private Double claimPercentage;
	@Column(name="CUM_PERCENTAGE")
	private Double  cumPercentage;
	@Column(name="IS_ACTIVE")
	private Boolean  isActive;
	

}
