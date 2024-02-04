package com.lic.epgs.quotation.dto;

import java.io.Serializable;
import java.util.Date;

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
public class QuotationMemberBatchDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private Long batchId;
	private Long successCount = 0L;
	private Long failedCount = 0L;
	private Long totalCount = 0L;
	private String fileName;
	private byte[] successFile;
	private byte[] rawFile;
	private byte[] failedFile;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	private Boolean isActive;
	private Integer quotationId;

//	private String policyNumber;
//	private String serviceNo;
//	private Long batchId;
//	private String quotationNo;
//	private Long successCount = 0L;
//	private Long failedCount = 0L;
//	private Long totalCount = 0L;
//	private String fileName;
//	private Long batchFile;
//	private String modifiedBy;
//	private String createdBy;
//	private Date modifiedOn;
//	private Date createdOn;
}
