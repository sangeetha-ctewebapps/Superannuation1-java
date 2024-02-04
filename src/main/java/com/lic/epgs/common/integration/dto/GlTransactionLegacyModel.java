package com.lic.epgs.common.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class GlTransactionLegacyModel {

	private static final long serialVersionUID = 1L;
	
	private Integer iCodeForBusinessSegment;
	private Integer iCodeForBusinessType;
	private Integer iCodeForInvestmentPortfolio;
	private Integer iCodeForLob;
	private Integer iCodeForParticipatingType;
	private Integer iCodeForProductLine;
	private String iCodeForVariant;
	private String refNo;
	private String subRefNo;
	

}
