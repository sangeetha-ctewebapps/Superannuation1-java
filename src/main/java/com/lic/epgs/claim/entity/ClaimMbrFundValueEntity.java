package com.lic.epgs.claim.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "CLAIM_MBR_FUND_VALUE")
public class ClaimMbrFundValueEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLAIM_MBR_FV_SEQUENCE")
	@SequenceGenerator(name = "CLAIM_MBR_FV_SEQUENCE", sequenceName = "CLAIM_MBR_FV_SEQUENCE", allocationSize = 1)
	@Column(name = "FV_ID", length = 10)
	private Long fundValueId;

	@Column(name = "CLAIM_NO", length = 30)
	private String claimNo;

	@Column(name = "EMPLOYER_CONTRIBUTION", precision = 20, scale = 2)
	private Double employerContribution;

	@Column(name = "EMPLOYEE_CONTRIBUTION", precision = 20, scale = 2)
	private Double employeeContribution;

	@Column(name = "VOLUNTARY_CONTRIBUTION", precision = 20, scale = 2)
	private Double voluntaryContribution;

	@Column(name = "TOTAL_INTEREST_ACCURED", precision = 20, scale = 2)
	private Double totalInterestAccured;

	@Column(name = "TOTAL_FUND_VALUE", precision = 20, scale = 2)
	private Double totalFundValue;

	@Column(name = "PURCHASE_PRICE", precision = 20, scale = 2)
	private Double purchasePrice;

	@Column(name = "PENSION", precision = 20, scale = 2)
	private Double pension;

	@Column(name = "ANNUITY_OPTION", length = 50)
	private String annuityOption;

	@Column(name = "ANNUITY_MODE", length = 50)
	private String anuityMode;

	@Column(name = "IS_JOINT_LIVE_REQ", length = 5)
	private Boolean isJointLiveRequired;

	@Column(name = "CALC_TYPE", length = 100)
	private Long calculationType;

	@Column(name = "NOMINEE_CODE", length = 50)
	private String nomineeCode;
	
	@Column(name = "PURCHASE_PRICE_LIC", precision = 20, scale = 2)
	private Double purchasePriceForLIC;
	
	@Column(name = "PURCHASE_PRICE_OTHER", precision = 20, scale = 2)
	private String purchaseFromOthers;
	
	@Column(name = "PURCHASED_FROM", length = 100)
	private Long purchasedFrom;
	
	@Column(name = "CORPUS_PERCENTAGE")
	private Double corpusPercentage;
	
	@Column(name = "FUND_VALUE")
	private Double fundValue;
	
	@Column(name = "REFUND_TO_MPH")
	private Double refundToMPH;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_MEMBER_ID", nullable = true)
	private ClaimMbrEntity claimMbrEntity;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "CLAIM_NOMINEE_ID", nullable = true)
//	private ClaimMbrNomineeEntity claimMbrNomineeEntity;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
}
