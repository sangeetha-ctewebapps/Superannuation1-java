package com.lic.epgs.common.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class CollectionNeftDetailLegacyModel {
	
	private static final long serialVersionUID = 1L;
	
	
	private String neftRefNo;
	private String payorBankAccountNo;
	private String payorBankBranch;
	private String payorBankIfscCode;
	private String payorBankName;
	private String payorName;
	private String utrNo;

}
