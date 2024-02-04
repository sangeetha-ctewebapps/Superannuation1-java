package com.lic.epgs.common.integration.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class AnnuityModeResponse {
	
	private String message;
	private String customErrorCode;
	private List<Annuitymode> annuityMode;
	private Integer statusCode;

}
