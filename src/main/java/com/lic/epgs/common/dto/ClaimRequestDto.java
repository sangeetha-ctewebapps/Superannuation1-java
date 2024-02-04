/**
 * 
 */
package com.lic.epgs.common.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Muruganandam
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClaimRequestDto {

	@JsonProperty(required = true)
	@NotBlank(message = "Stream Name is mandatory")
	private String stream;

	@JsonProperty(required = true)
	@NotBlank(message = "Policy Id is mandatory")
	private Long policyId;

	@JsonProperty(required = true)
	@NotBlank(message = "Module name is mandatory")
	private String module;

	@Pattern(regexp = "([0-2][0-9]|3[0-1])\\/(0[1-9]|1[0-2])\\/[0-9]{4}", message = "Transaction Date format should be DD/MM/YYYY")
	private String transactionDate;

	@JsonProperty(required = true, value = "username")
	@NotBlank(message = "Username is required")
	private String createdBy;

	private String licId;

	private Long memberId;

	private BigDecimal txnAmount;

	private BigDecimal purchasePrice;

	private BigDecimal commutationAmt;

	private String dateOfExit;
}
