package com.lic.epgs.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import javax.validation.constraints.DecimalMin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Muruganandam
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore(value = true)
	private Long id;

	private Long policyId;

	@JsonProperty(value = "licId")
	private String memberId;

	private String policyNumber;

	private String trnxDate;

	@JsonIgnore(value = true)
	private String depositDate;

	@JsonIgnore(value = true)
	private String modifiedBy;

	@JsonIgnore(value = true)
	private String createdBy;

	@JsonIgnore(value = true)
	private String createdOn;

	@JsonIgnore(value = true)
	private String remarks;

	private String variant;

	private String policyType;

	@JsonIgnore(value = true)
	private String batchType;

	@JsonIgnore(value = true)
	private @Default Boolean isExit = false;

	private String txnType;

	private String txnSubType;

	@JsonIgnore(value = true)
	private @Default Boolean isTxn = false;

	@JsonIgnore(value = true)
	private @Default Boolean isExecuted = false;

	private @Default Boolean isBatch = true;

	private @Default Boolean isAuto = true;

	private @Default Boolean setOpeningBalance = true;

	private @Default Boolean recalculate = false;

	@JsonIgnore(value = true)
	private BigDecimal totalAverageBalanceAmount;

	@JsonIgnore(value = true)
	private String batchTime;

	@JsonIgnore(value = true)
	private @Default Double interestRate = 0.0;

	@JsonIgnore(value = true)
	@DecimalMin(value = "1.0", message = "Transaction Amount is required")
	private @Default BigDecimal txnAmount = BigDecimal.ZERO;

	@JsonIgnore(value = true)
	private transient Map<String, Object> batch;

}