package com.lic.epgs.claim.dto;

import java.util.Date;
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
public class CommonGlCodesResponseDto {
	
	private Long procSaSprId;
	private Long payoutId;
	private String glCodeDesc;
	private Double amount;
	private String accountRuleCode;
	private String accountType;
	private String glCode;
	private String accountRuleCodeDesc;
	
	
	private String status;
	private String message;

}
