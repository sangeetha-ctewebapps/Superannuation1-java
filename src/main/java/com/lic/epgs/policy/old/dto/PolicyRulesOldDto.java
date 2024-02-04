package com.lic.epgs.policy.old.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author pradeepramesh
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyRulesOldDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long ruleId;
	private Long policyId;
	private String category;
	private Integer normalAgeRetirement;
	private Integer minAgeRetirement;
	private Integer maxAgeRetirement;
	private Integer minServicePension;
	private Integer maxServicePension;
	private Integer minServiceWithdrawal;
	private Integer maxServiceWithdrawal;
	private String contributionType;
	private Integer minPension;
	private Integer maxPension;
	private String benifitPayableTo;
	private String annuityOption;
	private String commutationBy;
	private Integer commutationAmt;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
	
	private Long serviceId;
	
	private String effectiveFrom;
	private Boolean isActive;
	private Double percentageCorpus;
	
}
