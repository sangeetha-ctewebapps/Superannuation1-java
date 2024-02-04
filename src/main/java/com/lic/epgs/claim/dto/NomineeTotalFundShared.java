package com.lic.epgs.claim.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
public class NomineeTotalFundShared {

	private List<Double> percentages;
	private Double totalfundValue;
	private Map<String , Double> nominess;
	
	
	
	
	
	
}
