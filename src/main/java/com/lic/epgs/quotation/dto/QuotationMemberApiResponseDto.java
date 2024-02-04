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
public class QuotationMemberApiResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<QuotationMemberDto> quotationMemberDtos;
	private Long memberId;
	private QuotationMemberDto quotationMemberDto;
	private transient Object responseData;
	private String transactionStatus;
	private String transactionMessage;
}
