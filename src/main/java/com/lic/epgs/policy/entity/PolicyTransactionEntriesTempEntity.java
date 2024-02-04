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
@Table(name = "POLICY_TRANS_ENTRIES_TEMP")
public class PolicyTransactionEntriesTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_T_E_T_SEQUENCE")
	@SequenceGenerator(name = "POL_T_E_T_SEQUENCE", sequenceName = "POL_T_E_T_SEQ", allocationSize = 1)
	@Column(name = "POL_TRAN_ID")
	private Long polTranId;
	@Column(name = "POLICY_ID ")
	private Long policyId;
	@Column(name = "TRANSATION_TYPE ")
	private String transationType;
	@Column(name = "ENTRY_TYPE  ")
	private String entryType;
	@Column(name = "OPENING_BALANCE")
	private BigDecimal openingBalance;
	@Column(name = "EMPLOYER_CONTRIBUTION")
	private BigDecimal employerContribution;
	@Column(name = "EMPLOYEE_CONTRIBUTION ")
	private BigDecimal employeeContribution;
	@Column(name = "VOLUNTARY_CONTRIBUTION")
	private BigDecimal voluntaryContribution;
	@Column(name = "TOTAL_CONTRIBUTION")
	private BigDecimal totalContribution;
	@Column(name = "CLOSING_BALANCE")
	private BigDecimal closingBalance;
	@Column(name = "TRANSACTION_DATE ")
	private Date transactionDate;
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
}
