package com.lic.epgs.claim.temp.entity;

import java.util.Date;

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
@Table(name = "TEMP_CLAIM_FUND_VALUE")
public class TempClaimFundValueEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TCLAIM_FV_SEQUENCE")
	@SequenceGenerator(name = "TCLAIM_FV_SEQUENCE", sequenceName = "TCLAIM_FV_SEQUENCE", allocationSize = 1)
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

	@Column(name = "TOTAL_CONTRIBUTION")
	private Double totalContirbution; 
	
	@Column(name = "OPENING_BALANCE")
	private Double openingBalance;
	
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
	
	@Column(name = "CORPUS_PERCENTAGE")
	private Double corpusPercentage;
	
	@Column(name = "FUND_VALUE")
	private Double fundValue;
	
	@Column(name = "REFUND_TO_MPH")
	private Double refundToMPH;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_MEMBER_ID", nullable = false)
	private TempClaimMbrEntity claimMbrEntity;
	
	@Column(name = "PURCHASE_FROM_OTHERS", length = 255)
	private String purchaseFromOthers;
	
	
	@Column(name = "DATE_OF_CALCULATION")
	private Date dateOfCalculate;
	
	@Column(name = "MARKET_VALUE_APPLICABLE")
	private Boolean isMarketValueApplicable;
	
	@Column(name = "COMMUTATION_AMOUNT", length = 20)
	private Double commutationAmount;
	
	@Column(name = "EXIST_LOAD", length = 20)
	private Double exitLoad;
	
	@Column(name = "EXIST_LOAD_RATE", length = 20)
	private Double exitLoadRate;
	
	
	@Column(name = "SPOUSE_NAME", length = 50)
	private String spouseName;

	@Column(name = "SPOUSE_DOB")
	private Date spouseDob;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

}
