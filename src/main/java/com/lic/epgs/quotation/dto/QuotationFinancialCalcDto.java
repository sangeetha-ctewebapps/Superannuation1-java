package com.lic.epgs.quotation.dto;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class QuotationFinancialCalcDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal employerContribution;
	private BigDecimal employeeContribution;
	private BigDecimal totalContribution;
	private Integer totalMember;

}
