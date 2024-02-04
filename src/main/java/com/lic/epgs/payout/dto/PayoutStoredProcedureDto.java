package com.lic.epgs.payout.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PayoutStoredProcedureDto {
	
	private String hsnCode;
	private Integer cgstRate;
	private Integer sgstRate;
	private Integer igstRate;
	private String utgstRate;
	private String description;

}
