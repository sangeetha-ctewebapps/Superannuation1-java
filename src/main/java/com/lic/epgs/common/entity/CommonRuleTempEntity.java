package com.lic.epgs.common.entity;

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
 *
 * @author KrunalGothiwala
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "RULE_DETAIL_TEMP")
public class CommonRuleTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUOTATION_RULE_TEMP_SEQ")
	@SequenceGenerator(name = "QUOTATION_RULE_TEMP_SEQ", sequenceName = "QUOTATION_RULE_TEMP_SEQ", allocationSize = 1)
	@Column(name = "RULE_ID", length = 10)
	private Integer ruleId;

	@Column(name = "CATEGORY", length = 10)
	private String category;

	@Column(name = "NORMAL_AGE_RETIREMENT", length = 10)
	private Integer normalAgeRetirement;

	@Column(name = "MIN_AGE_RETIREMENT", length = 10)
	private Integer minAgeRetirement;

	@Column(name = "MIN_SERVICE_PENSION", length = 10)
	private Integer minServicePension;

	@Column(name = "MIN_SERVICE_WITHDRAWAL", length = 10)
	private Integer minServiceWithdrawal;

	@Column(name = "CONTRIBUTION_TYPE", length = 10)
	private String contributionType;

	@Column(name = "MIN_PENSION", length = 10)
	private Integer minPension;

	@Column(name = "MAX_PENSION", length = 10)
	private Integer maxPension;

	@Column(name = "BENIFIT_PAYABLE_TO", length = 10)
	private String benifitPayableTo;

	@Column(name = "ANNUITY_OPTION", length = 10)
	private String annuityOption;

	@Column(name = "COMMUTATION_BY", length = 255)
	private String commutationBy;

	@Column(name = "COMMUTATION_AMT", length = 10)
	private Integer commutationAmt;

	@Column(name = "MODIFIED_BY", length = 50)
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "CREATED_BY", length = 50)
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();
	
	@Column(name = "PERCENTAGE_CORPUS")
	private Double percentageCorpus;
}
