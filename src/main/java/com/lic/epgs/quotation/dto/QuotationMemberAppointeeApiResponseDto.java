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
public class QuotationMemberAppointeeApiResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long quotationMemberAppointeeId;
	private List<QuotationMemberAppointeeDto> responseData;
	private QuotationMemberAppointeeDto quotationMemberAppointeeDto;
	private String transactionStatus;
	private String transactionMessage;
}
