package com.lic.epgs.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonRuleDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer ruleId;
	private String category;
	private Integer normalAgeRetirement;
	private Integer minAgeRetirement;
	private Integer minServicePension;
	private Integer minServiceWithdrawal;
	private String contributionType;
	private Integer minPension;
	private Integer maxPension;
	private String benifitPayableTo;
	private String annuityOption;
	private Integer commutationAmt;
	private String commutationBy;
	private Double percentageCorpus;
}
