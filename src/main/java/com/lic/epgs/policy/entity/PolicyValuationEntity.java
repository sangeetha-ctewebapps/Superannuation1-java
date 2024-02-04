package com.lic.epgs.policy.entity;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
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
@Table(name = "POLICY_VALUATION")
public class PolicyValuationEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_V_M_SEQUENCE")
	@SequenceGenerator(name = "POL_V_M_SEQUENCE", sequenceName = "POL_V_M_SEQ", allocationSize = 1)
	@Column(name = "VALUATION_ID")
	private Long valuationId;
	@Column(name = "POLICY_ID")
	private Long policyId;
	@Column(name = "VALUATION_TYPE")
	private String valuationType;
	@Column(name = "ATTRITION_RATE")
	private Integer attritionRate;
	@Column(name = "SALARY_ESCALATION")
	private Integer salaryEscalation;
	@Column(name = "DEATH_RATE")
	private Integer deathRate;
	@Column(name = "DIS_RATE_INTREST")
	private Integer disRateIntrest;
	@Column(name = "DIS_RATE_SALARY_ESC")
	private Integer disRateSalaryEsc;
	@Column(name = "ANNUITY_OPT_ID")
	private Long annuityOptId;
	@Column(name = "ANNUITY_OPTION_CODE")
	private Long annuityOptionCode;
	@Column(name = "ANNUITY_OPTION")
	private String annuityOption;
	@Column(name = "ACCURAL_RATE_FACTOR")
	private Integer accuralRateFactor;
	@Column(name = "MIN_PENSION")
	private Integer minPension;
	@Column(name = "MAX_PENSION")
	private Integer maxPension;
	@Column(name = "WITHDRAWAL_RATE")
	private Integer withdrawalRate;
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
