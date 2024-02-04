package com.lic.epgs.common.integration.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnnuityOptionalResponse {
	
	private String message;
	private String customErrorCode;
	private List<AnnuityOption> annuityOption;
	private Integer statusCode;
	

}
