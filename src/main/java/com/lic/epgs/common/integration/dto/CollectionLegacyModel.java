package com.lic.epgs.common.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class CollectionLegacyModel {

	private static final long serialVersionUID = 1L;
	
	private String financialYear;
	private String unitCode;
	
}
