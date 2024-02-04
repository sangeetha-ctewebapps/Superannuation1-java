/**
 * 
 */
package com.lic.epgs.common.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;

/**
 * @author Muruganandam
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FundChangeDto {

	@JsonProperty(required = true)
	@NotBlank(message = "Stream Name is required")
	private String stream;

	private Long policyId;

	private Long approvedPolicyId;

	private Long memberId;

	private Long approvedMemberId;

	@JsonProperty(required = true)
	@NotBlank(message = "Module is required i.e., Policy CONVERSION/MERGE/ADJUSTMENT/FREELOOK CANCElLATION/SURRENDER ")
	private String module;

	@JsonProperty(required = true)
	@NotBlank(message = "Update type i.e., CONVERSION/MERGE/PARTIAL/FULL is required")
	private String updateType;

	/**
	 * @JsonProperty(required = true)
	 * @NotBlank(message = "Update Sub type i.e., CONVERSION/MERGE/PARTIAL/FULL is
	 *                   required")
	 */
	private String updateSubType;

	@JsonProperty(required = true)
	private Long destinationPolicyNumber;

	@JsonProperty(required = true)
	@NotBlank(message = "Variant is required")
	private String variant;

	private Long sourcePolicyNumber;

	@JsonProperty(required = true)
	@NotBlank(message = "Policy Type is required")
	private String policyType;

	@Pattern(regexp = "([0-2][0-9]|3[0-1])\\/(0[1-9]|1[0-2])\\/[0-9]{4}", message = "Transaction Date format should be DD/MM/YYYY")
	private String effectiveTxnDate;

	@JsonProperty(required = true)
	@NotBlank(message = "Username is required")
	private String createdBy;

	private String licId;

	private String newLicId;

	private String creditDebit;

	private BigDecimal txnAmount;

	private BigDecimal purchasePrice;

	private BigDecimal commutationAmt;

	private String dateOfExit;

	private List<String> members;

	private List<MemberChangeDto> policyMembers;
	private String unitCode;
	private @Default Boolean annuityApplicable = false;
}
