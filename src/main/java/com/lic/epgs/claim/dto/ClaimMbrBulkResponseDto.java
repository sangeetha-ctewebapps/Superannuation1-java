/**
 * 
 */
package com.lic.epgs.claim.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ClaimMbrBulkResponseDto implements Serializable {

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
	private Long policyId;
	private String policyNumber;
	private String transactionStatus;
	private String transactionMessage;
	private List<TempClaimMbrBulkErrorDto> error;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
}
