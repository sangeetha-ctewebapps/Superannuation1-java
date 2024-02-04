/**
 * 
 */
package com.lic.epgs.adjustmentcontribution.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Karthick M
 *
 */


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdjustmentContributionBulkErrorDto {
	
	private Long errorId;
	private Long batchId;
	private String membershipId;
	private String licId;
	private String error;

}
