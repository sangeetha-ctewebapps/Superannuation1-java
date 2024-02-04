package com.lic.epgs.payout.PdfDto;

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
public class LifeInsuranceDetailDto {

	//private String ModeofExit ;
	private String Licid ;
	private String EmpNo ;
	private String Name ;
	private Double  Amount ;
	private String Date ;
	private String RefundOfPremium ;
	
	
	

}