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
public class QuotationMemberNomineeApiResponseDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<QuotationMemberNomineeDto> quotationMemberNomineeDtos;
	private Long memberId;
	private QuotationMemberNomineeDto quotationMemberNomineeDto;
	private String transactionStatus;
	private String transactionMessage;
}
