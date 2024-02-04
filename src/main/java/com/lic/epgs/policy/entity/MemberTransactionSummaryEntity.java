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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "MEMBER_TRANSACTION_SUMMARY")
public class MemberTransactionSummaryEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FUND_MBR_MASTER_GEN")
	@SequenceGenerator(name = "FUND_MBR_MASTER_GEN", sequenceName = "P_MBR_T_S_M_SEQ", allocationSize = 1)
	@Column(name = "MEM_TRAN_SUM_ID")
	private Long id;

	/***
	 * @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy =
	 *                 "memberFundMasterContribution") private
	 *                 MembersContributionMasterV2Entity memberContribution;
	 */

	/**
	 * @OneToMany(mappedBy = "policyMemberFund", cascade = CascadeType.ALL, fetch =
	 *                     FetchType.LAZY) private List<MemberFundHistoryDto>
	 *                     policyMemberFundHistory = Collections.emptyList();
	 */
//	@OneToMany(mappedBy = "memberTxnSumm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	private List<MemberFundStmtV2Entity> memberFundStmtEntities = Collections.emptyList();

	/**
	 * @ManyToOne(fetch = FetchType.EAGER)
	 * 
	 * @JoinColumn(name = "MEMBER_ID", nullable = false) private MemberMasterEntity
	 *                  memberMasterEntity;
	 */

	@Column(name = "MEMBER_ID", nullable = false)
	private Long policyMbrId;

	@Column(name = "STATEMENT_ID")
	private Long stmtId;

	@Column(name = "POLICY_ID", nullable = false)
	private Long policyId;

	@Column(name = "LIC_ID", length = 20, nullable = false)
	private String memberId;

	@Column(name = "REF_LIC_ID", length = 20)
	private String refLicId;

	/**
	 * @Column(name = "POLICY_NUMBER", length = 15) private String policyNumber;
	 */

	@Column(name = "CLOSING_BALANCE")
	private BigDecimal closingBalance;

	@Column(name = "TOTAL_CONTRIBUTION")
	private BigDecimal txnAmount;

	@Column(name = "OPENING_BALANCE")
	private BigDecimal openingBalance;

	/***
	 * @Column(name = "FMC_AMOUNT_PER_RATE") private BigDecimal fmcAmountPerRate;
	 */

	@Column(name = "MODIFIED_BY", length = 100)
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON", nullable = false)
	private Date createdOn = new Date();

	@Column(name = "IS_ACTIVE", nullable = false)
	private Boolean isActive = Boolean.TRUE;

	@Column(name = "REMARKS", length = 400)
	private String remarks;

	/**
	 * @Column(name = "POLICY_TYPE", length = 10, nullable = false) private String
	 *              policyType;
	 */
	/**
	 * @Column(name = "VARIANT_TYPE", length = 5, nullable = false) private String
	 *              variant;
	 */

	@Column(name = "STATEMENT_FREQUENCY")
	private Integer quarter;

	@Column(name = "financial_year")
	private String financialYear;

	@Column(name = "STREAM_NAME", length = 50)
	private String stream;

	@Column(name = "IS_EXIT")
	private Boolean isExit;

	@Column(name = "EMPLOYER_CONTRIBUTION ")
	private BigDecimal employerContribution;

	@Column(name = "EMPLOYEE_CONTRIBUTION ")
	private BigDecimal employeeContribution;

	@Column(name = "VOLUNTARY_CONTRIBUTION ")
	private BigDecimal voluntaryContribution;

}
