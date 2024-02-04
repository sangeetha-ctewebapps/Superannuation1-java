package com.lic.epgs.policy.entity;

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

/**
 * @author pradeepramesh
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "POLICY_RULES_TEMP")
public class PolicyRulesTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_RUL_T_SEQUENCE")
	@SequenceGenerator(name = "POL_RUL_T_SEQUENCE", sequenceName = "POL_RUL_T_SEQ", allocationSize = 1)
	@Column(name = "RULE_ID")
	private Long ruleId;

	@Column(name = "POLICY_ID")
	private Long policyId;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "NORMAL_AGE_RETIREMENT")
	private Integer normalAgeRetirement;

	@Column(name = "MIN_AGE_RETIREMENT")
	private Integer minAgeRetirement;

	@Column(name = "MAX_AGE_RETIREMENT")
	private Integer maxAgeRetirement;

	@Column(name = "MIN_SERVICE_PENSION")
	private Integer minServicePension;

	@Column(name = "MAX_SERVICE_PENSION")
	private Integer maxServicePension;

	@Column(name = "MIN_SERVICE_WITHDRAWAL")
	private Integer minServiceWithdrawal;

	@Column(name = "MAX_SERVICE_WITHDRAWAL")
	private Integer maxServiceWithdrawal;

	@Column(name = "CONTRIBUTION_TYPE")
	private String contributionType;

	@Column(name = "MIN_PENSION")
	private Integer minPension;

	@Column(name = "MAX_PENSION")
	private Integer maxPension;

	@Column(name = "BENIFIT_PAYABLE_TO")
	private String benifitPayableTo;

	@Column(name = "ANNUITY_OPTION")
	private String annuityOption;

	@Column(name = "COMMUTATION_BY")
	private String commutationBy;

	@Column(name = "COMMUTATION_AMT")
	private Integer commutationAmt;

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
	
	@Column(name = "EFFECTIVE_FROM")
	private String effectiveFrom;
	
	@Column(name = "PERCENTAGE_CORPUS")
	private Double percentageCorpus;

}
