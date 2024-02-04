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
@Table(name = "MEMBER_CONTRI_SUMMARY_TEMP")
public class MemberContributionSummaryTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "P_MBR_CO_S_T_SEQUENCE")
	@SequenceGenerator(name = "P_MBR_CO_S_T_SEQUENCE", sequenceName = "P_MBR_CO_S_T_SEQ", allocationSize = 1)
	@Column(name = "MEM_CONT_SUMMARY_ID")
	private Long memContSummaryId;
	@Column(name = "POLICY_ID")
	private Long policyId;
	@Column(name = "MEMBER_ID")
	private Long memberId;
	@Column(name = "LIC_ID")
	private String licId;
	@Column(name = "OPENING_BALANCE")
	private BigDecimal openingBalance = BigDecimal.ZERO;
	@Column(name = "TOTAL_EMPLOYEE_CONTRIBUTION")
	private BigDecimal totalEmployeeContribution = BigDecimal.ZERO;
	@Column(name = "TOTAL_EMPLOYER_CONTRIBUTION")
	private BigDecimal totalEmployerContribution = BigDecimal.ZERO;
	@Column(name = "TOTAL_VOLUNTARY_CONTRIBUTION")
	private BigDecimal totalVoluntaryContribution = BigDecimal.ZERO;
	
	@Column(name = "TOTAL_CONTRIBUTION")
	private BigDecimal totalContribution = BigDecimal.ZERO;
	
	@Column(name = "TOTAL_INTREST_ACCURED")
	private BigDecimal totalInterestedAccured = BigDecimal.ZERO;
	
	
	@Column(name = "CLOSING_BALANCE")
	private BigDecimal closingBalance = BigDecimal.ZERO;
	@Column(name = "FINANCIAL_YEAR")
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
	
	@Column(name = "QUARTER")
	private String quarter;
}
