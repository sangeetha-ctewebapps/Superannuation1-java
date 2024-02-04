package com.lic.epgs.policy.entity;

/**
 * @author pradeepramesh
 *
 */

import java.io.Serializable;
import java.math.BigDecimal;
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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "MEMBER_CONTRIBUTION")
public class MemberContributionEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "P_MBR_CO_M_SEQUENCE")
	@SequenceGenerator(name = "P_MBR_CO_M_SEQUENCE", sequenceName = "P_MBR_CO_M_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_CON_ID")
	private Long memberConId;
	
	@Column(name = "POLICY_CON_ID")
	private Long policyConId;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	@Column(name = "MEMBER_ID")
	private Long memberId;
	
	@Column(name = "TEMP_POLICY_ID")
	private Long tempPolicyId;
	@Column(name = "TEMP_MEMBER_ID")
	private Long tempMemberId;
	
	@Column(name = "ADJ_CON_ID")
	private Long adjustmentContributionId;
	@Column(name = "REG_CON_ID")
	private Long regularContributionId;
	
	@Column(name = "CONTRIBUTION_TYPE ")
	private String contributionType;
	
	@Column(name = "CONTRIBUTION_DATE")
	private Date contributionDate;
	
	@Column(name = "FINANCIAL_YEAR")
	private String financialYear;
	@Column(name = "QUARTER")
	private String quarter;
	@Column(name = "VERSION_NO")
	private Integer versionNo;
	
	@Column(name = "LIC_ID")
	private String licId;

	@Column(name = "OPENING_BALANCE")
	private BigDecimal openingBalance = BigDecimal.ZERO;
	@Column(name = "CLOSING_BALANCE")
	private BigDecimal closingBalance = BigDecimal.ZERO;

	@Column(name = "EMPLOYEE_CONTRIBUTION")
	private BigDecimal employeeContribution = BigDecimal.ZERO;
	@Column(name = "EMPLOYER_CONTRIBUTION")
	private BigDecimal employerContribution = BigDecimal.ZERO;
	@Column(name = "VOLUNTARY_CONTRIBUTION")
	private BigDecimal voluntaryContribution = BigDecimal.ZERO;
	@Column(name = "TOTAL_CONTRIBUTION")
	private BigDecimal totalContribution = BigDecimal.ZERO;
	
	@Column(name = "TOTAL_INTREST_ACCURED")
	private BigDecimal totalInterestedAccured = BigDecimal.ZERO;

	@Column(name = "IS_ZERO_ACCOUNT")
	private Boolean isZeroAccount= Boolean.FALSE;
	
	@Column(name = "IS_DEPOSIT ")
	private Boolean isDeposit = Boolean.FALSE;
	
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

	@Column(name = "TXN_ENTRY_STATUS")
	private Boolean txnEntryStatus = Boolean.FALSE;
	

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_CON_ID", referencedColumnName = "MEMBER_CON_ID")
	private Set<ZeroAccountEntriesEntity> zeroAccountEntries = new HashSet<>();

//	@Column(name = "ZERO_ACCOUNT_ENTRIES")
//	private String zeroAccountEntries;
	
	@Column(name = "ADJUSTMENT_DUE_DATE")
	private Date adjustmentDueDate;
	
	@Column(name = "EFFECTIVE_DATE")
	private Date effectiveDate;
	

	

}
