/**
 * 
 */
package com.lic.epgs.adjustmentcontribution.dto;

import java.io.Serializable;
import java.util.List;

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
public class AdjustmentContributionBatchHistoryResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String transactionStatus;
	private String transactionMessage;
	private List<AdjustmentContributionBulkResponseDto> batchHistory;

}
