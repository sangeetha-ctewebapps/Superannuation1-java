package com.lic.epgs.policy.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class PolicyRulesDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long ruleId;
	private Long policyId;
	
	private String contributionType;
	private String benifitPayableTo;
	private String annuityOption;
	private String commutationBy;
	private Integer commutationAmt;
	private String effectiveFrom;
	private Double percentageCorpus;
	private String category;
	
	private Integer normalAgeRetirement;
	private Integer minAgeRetirement;
	private Integer maxAgeRetirement;
	
	private Integer minServicePension;
	private Integer maxServicePension;
	
	private Integer minServiceWithdrawal;
	private Integer maxServiceWithdrawal;
	
	private Integer minPension;
	private Integer maxPension;
	
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private Boolean isActive;
	
}
