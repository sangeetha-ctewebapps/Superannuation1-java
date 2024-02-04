package com.lic.epgs.claim.dto;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaAccountConfigMasterResponseDto {
	
	List<AccountingGLcodeDto> accountingGLCodeModel;
	
	private String status;
	private String message;
}
