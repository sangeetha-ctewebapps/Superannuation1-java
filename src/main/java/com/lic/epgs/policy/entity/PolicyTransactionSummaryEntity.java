package com.lic.epgs.policy.entity;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "POLICY_TRANSACTION_SUMMARY")
public class PolicyTransactionSummaryEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POLICY_TRANS_SUMM_GEN")
	@SequenceGenerator(name = "POLICY_TRANS_SUMM_GEN", sequenceName = "POL_T_S_M_SEQ", allocationSize = 1)
	@Column(name = "POL_TRAN_SUM_ID")
	private Long id;

	/***
	 * @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy =
	 *                 "policyFundMasterContribution") private
	 *                 PolicyContributionMasterV2Entity policyFundContribution;
	 */

	/***
	 * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	 *                    "policyFundMaster") private
	 *                    List<PolicyFundHistoryDto> policyFundHistory =
	 *                    Collections.emptyList();
	 */

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "policyFundMasterStmt")
//	private List<PolicyFundStmtV2Entity> policyFundStmts = Collections.emptyList();

	@Column(name = "STATEMENT_ID")
	private Long stmtId;

	/**
	 * @ManyToOne(fetch = FetchType.EAGER)
	 * 
	 * @JoinColumn(name = "POLICY_ID") private PolicyMasterEntity
	 *                  policyMasterEntity;
	 */

	@Column(name = "POLICY_ID", nullable = false)
	private Long policyId;

	/**
	 * @Column(name = "POLICY_NUMBER", length = 15) private String policyNumber;
	 */

	@Column(name = "OPENING_BALANCE ")
	private BigDecimal openingBalanceAmount;

	@Column(name = "EMPLOYER_CONTRIBUTION ")
	private BigDecimal employerContribution;

	@Column(name = "EMPLOYEE_CONTRIBUTION ")
	private BigDecimal employeeContribution;

	@Column(name = "VOLUNTARY_CONTRIBUTION ")
	private BigDecimal voluntaryContribution;

	@Column(name = "TOTAL_CONTRIBUTION ")
	private BigDecimal totalContribution;

	@Column(name = "CLOSING_BALANCE ")
	private BigDecimal closingBalance;

	@Column(name = "FINANCIAL_YEAR ")
	private String financialYear;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "IS_EXIT")
	private Boolean isExit = Boolean.FALSE;

	@Column(name = "STATEMENT_FREQUENCY")
	private Integer quarter;

}
