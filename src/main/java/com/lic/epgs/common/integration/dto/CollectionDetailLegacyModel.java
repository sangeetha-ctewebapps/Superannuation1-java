package com.lic.epgs.common.integration.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class CollectionDetailLegacyModel {

	private static final long serialVersionUID = 1L;
	
	private String bankAccountNo;
	private Integer collectionAmount;
	private Date collectionDate;
	private String collectionMode;
	private String collectionRefNo;
	private String mphCode;
	private String mphName;
	private String remarks;
	private String schemeName;
	private Date voucherEffectiveDate;
	
	
	
}
