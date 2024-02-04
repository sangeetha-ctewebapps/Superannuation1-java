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
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "POLICY_TRANSACTION_ENTRIES")
public class PolicyTransactionEntriesEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FUND_POLICY_TXN_ENTRIES_GEN")
	@SequenceGenerator(name = "FUND_POLICY_TXN_ENTRIES_GEN", sequenceName = "POL_T_E_M_SEQ", allocationSize = 1)
	@Column(name = "POL_TRAN_ID")
	private Long polTranId;

	@Column(name = "TRANSATION_TYPE", length = 255)
	private String transationType;

	@Column(name = "ENTRY_TYPE", length = 255)
	private String entryType;

	@Column(name = "OPENING_BALANCE")
	private BigDecimal openingBalance;

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

	@Column(name = "TRANSACTION_DATE")
	private Date transactionDate;

	@Column(name = "FINANCIAL_YEAR", length = 20)
	private String financialYear;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "CREATED_BY", length = 255)
	private String createdBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@Column(name = "MODIFIED_BY", length = 255)
	private String modifiedBy;

	@Column(name = "IS_OPENINGBAL")
	private @Default Boolean isOpeningBal = Boolean.FALSE;

	@Column(name = "IS_ACTIVE", nullable = false)
	private @Default Boolean isActive = Boolean.TRUE;

	@Column(name = "STATEMENT_FREQUENCY")
	private Integer quarter;

	@Column(name = "TXN_REF_NO", length = 15)
	private String txnRefNo;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "POLICY_ID", nullable = true)
//	private PolicyMasterEntity policy;
	
	@Column(name = "POLICY_ID", nullable = false)
	private Long policyId;
	
	@Column(name = "POLICY_CONTRIBUTION_ID")
	private Long policyContributionId;
}
