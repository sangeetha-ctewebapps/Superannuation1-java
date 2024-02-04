/**
 * 
 */
package com.lic.epgs.common.dto;

import javax.validation.constraints.NotBlank;

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
public class MemberChangeDto {

	@JsonProperty(required = true)
	@NotBlank(message = "Policy Number is required")
	private String policyNumber;

	private String memberId;

	private String newMemberId;

	@JsonProperty(required = true)
	@NotBlank(message = "Update type i.e., CONVERSION/MERGE/PARTIAL/FULL is required")
	private String updateType;

}
