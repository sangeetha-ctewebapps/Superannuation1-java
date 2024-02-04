package com.lic.epgs.quotation.dto;

import java.io.Serializable;
import java.util.List;

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
public class QuotationMemberBulkResponseDto extends QuotationMemberBatchDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer quotationId;
	private String quotationNo;
	
	private Long policyId;
	private String policyNumber;
	private Long memberAdditionId;
	private Long serviceId;
	private String serviceNumber;
	
	private String transactionStatus;
	private String transactionMessage;
	private List<QuotationMemberErrorDto> error;

}
