package com.lic.epgs.gst.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GstCalculationRequest {
	private String fromStateCode;
	private String toStateCode;
	private String fromStateName;
	private String toStateName;
	private BigDecimal amount;
	private String isAmountWithTax;
	private String hsnCode;
}
