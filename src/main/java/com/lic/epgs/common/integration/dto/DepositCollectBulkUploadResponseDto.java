package com.lic.epgs.common.integration.dto;

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

public class DepositCollectBulkUploadResponseDto {
	
	private static final long serialVersionUID = 1L;
	
	private String collectionNo;
	private String utrNo;
	private Object data;
	
	private String transactionStatus;
	private String transactionMessage;

}
