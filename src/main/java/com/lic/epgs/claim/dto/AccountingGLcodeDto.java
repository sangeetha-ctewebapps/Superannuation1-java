package com.lic.epgs.claim.dto;

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
public class AccountingGLcodeDto {

	private String glCodeDesc;
	private Long payoutId;
	private BigDecimal amount;
	private String accountRuleCode;
	private String accountType;
	private String glCode;
	private String accountRuleCodeDesc;
	
}
