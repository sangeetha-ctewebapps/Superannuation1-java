package com.lic.epgs.quotation.dto;

import java.io.Serializable;
import java.util.List;

import com.lic.epgs.common.dto.CommonDocsDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QuotationApiResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<QuotationDto> responseData;
	private Object responseObject;
	private QuotationDto quotationDto;
	private CommonDocsDto docsDto;
	private int quotationId;
	private String quotationNo;
	private String transactionStatus;
	private String transactionMessage;
}
