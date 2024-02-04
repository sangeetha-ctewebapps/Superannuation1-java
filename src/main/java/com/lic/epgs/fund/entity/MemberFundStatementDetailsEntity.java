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
@Entity
@Builder
@Table(name = "MEMBER_FUND_STATEMENT_DETAILS")
public class MemberFundStatementDetailsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_FUND_STML_DTLS_GEN")
	@SequenceGenerator(name = "MEMBER_FUND_STML_DTLS_GEN", sequenceName = "MEMBER_FUND_STML_DTLS_SEQ", allocationSize = 1)
	@Column(name = "MEM_STMT_DETAIL_ID")
	private BigDecimal memStmtDetailId;
	@Column(name = "POLICY_ID")
	private Long policyId;
	@Column(name = "MEMBER_ID")
	private Long memberId;
	@Column(name = "LIC_ID")
	private String licId;
	@Column(name = "TRANSACTION_TYPE", length = 255)
	private String transactionType;
	@Column(name = "TRANSACTION_DATE")
	private Date transactionDate;
	@Column(name = "TRANSACTION_DESCRIPTION", length = 255)
	private String transactionDescription;
	@Column(name = "EMPLOYER_CONTRIBUTION")
	private BigDecimal employerContribution;
	@Column(name = "EMPLOYEE_CONTRIBUTION")
	private BigDecimal employeeContribution;
	@Column(name = "VOLUNTARY_CONTRIBUTION")
	private BigDecimal voluntaryContribution;
	@Column(name = "TOTAL_CONTRIBUTION")
	private BigDecimal totalContribution;
	@Column(name = "INTEREST_RATE")
	private BigDecimal interestRate;
	@Column(name = "INTEREST_AMOUNT")
	private BigDecimal interestAmount;
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
	@Column(name = "DEPOSIT_DATE")
	private Date depositDate;
	@Column(name = "RECON_DAYS")
	private Integer reconDays;
	@Column(name = "FMC_RECON_DAYS")
	private Integer fmcReconDays;
	@Column(name = "FMC")
	private BigDecimal fmc;
	@Column(name = "GST")
	private BigDecimal gst;
	@Column(name = "CREATED_BY", length = 255)
	private String createdBy;
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEM_STATEMENT_ID", nullable = true)
	private MemberFundStatementSummaryEntity memberFundStatementSummary;

}
