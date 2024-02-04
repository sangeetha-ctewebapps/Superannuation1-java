/**
 * 
 */
package com.lic.epgs.fund.entity;

import java.math.BigDecimal;
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

import com.lic.epgs.policy.entity.PolicyMasterEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Muruganandam
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "POLICY_FUND_STATEMENT_SUMMARY")
public class PolicyFundStatementSummaryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POLICY_FUND_STML_SUMM_GEN")
	@SequenceGenerator(name = "POLICY_FUND_STML_SUMM_GEN", sequenceName = "POLICY_FUND_STML_SUMM_SEQ", allocationSize = 1)
	@Column(name = "POL_STATEMENT_ID")
	private Long polStatementId;
	
	@Column(name = "STATEMENT_TYPE", length = 255)
	private String statementType;
	@Column(name = "STATEMENT_DATE")
	private Date statementDate;
	@Column(name = "STATEMENT_FREQUENCY", length = 255)
	private String statementFrequency;
	@Column(name = "TRAN_FROM_DATE")
	private Date tranFromDate;
	@Column(name = "TRAN_TO_DATE")
	private Date tranToDate;
	@Column(name = "OPENING_BALANCE")
	private BigDecimal openingBalance;
	@Column(name = "OPENING_BALANCE_INT")
	private BigDecimal openingBalanceInt;
	@Column(name = "EMPLOYER_CONTRIBUTION")
	private BigDecimal employerContribution;
	@Column(name = "EMPLOYEE_CONTRIBUTION")
	private BigDecimal employeeContribution;
	@Column(name = "VOLUNTARY_CONTRIBUTION")
	private BigDecimal voluntaryContribution;
	@Column(name = "TOTAL_CONTRIBUTION")
	private BigDecimal totalContribution;
	@Column(name = "CLOSING_BALANCE")
	private BigDecimal closingBalance;
	@Column(name = "CLOSING_BALANCE_INT")
	private BigDecimal closingBalanceInt;
	@Column(name = "AVERAGE_BALANCE")
	private BigDecimal averageBalance;
	@Column(name = "TOTAL_INTEREST_RATE")
	private BigDecimal totalInterestRate;
	@Column(name = "TOTAL_INTEREST_AMOUNT")
	private BigDecimal totalInterestAmount;
	@Column(name = "AIR")
	private Double air;
	@Column(name = "AIR_AMOUNT")
	private BigDecimal airAmount;
	@Column(name = "MFR")
	private Double mfr;
	@Column(name = "MFR_AMOUNT")
	private BigDecimal mfrAmount;
	@Column(name = "RA")
	private Double ra;
	@Column(name = "RA_AMOUNT")
	private BigDecimal raAmount;
	@Column(name = "FMC")
	private BigDecimal fmc;
	@Column(name = "GST")
	private BigDecimal gst;
	@Column(name = "CREATED_BY", length = 255)
	private String createdBy;
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POLICY_ID", nullable = true)
	private PolicyMasterEntity policy;

}
