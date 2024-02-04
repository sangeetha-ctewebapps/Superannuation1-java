package com.lic.epgs.quotation.dto;

import java.io.Serializable;

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
public class QuotationFinancialResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private QuotationFinancialCalcDto financialCalcDto;
	private Integer quotationId;
	private String transactionStatus;
	private String transactionMessage;
}
