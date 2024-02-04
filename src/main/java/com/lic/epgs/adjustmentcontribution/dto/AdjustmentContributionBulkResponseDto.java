/**
 * 
 */
package com.lic.epgs.adjustmentcontribution.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
public class AdjustmentContributionBulkResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long batchId;
	private String quotationNo;
	private Long successCount = 0L;
	private Long failedCount = 0L;
	private Long totalCount = 0L;
	private String fileName;
	private byte[] file;
	private String modifiedBy;
	private String createdBy;
	private Date modifiedOn;
	private Date createdOn;
	private Long policyId;
	private String policyNumber;
	private String transactionStatus;
	private String transactionMessage;
	private Long adjustmentContributionId;
	private BigDecimal totalContribution;
	private List<AdjustmentContributionBulkErrorDto> error;
}
