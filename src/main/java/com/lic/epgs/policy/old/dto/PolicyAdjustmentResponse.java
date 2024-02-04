package com.lic.epgs.policy.old.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

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
public class PolicyAdjustmentResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long policyId;
	private BigDecimal totalDeposit = BigDecimal.ZERO;
	private BigDecimal totalAmountToBeAdjustment = BigDecimal.ZERO;
	private PolicyDto policyDto;
	private Set<PolicyAdjustmentOldDto> adjustments;
	private transient Object response;
	private String transactionStatus;
	private String transactionMessage;
	private transient Object responseData;
}
