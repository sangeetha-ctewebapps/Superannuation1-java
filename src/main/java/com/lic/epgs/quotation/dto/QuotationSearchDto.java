package com.lic.epgs.quotation.dto;

import java.io.Serializable;
import java.util.Date;

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
public class QuotationSearchDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String proposalNumber;
	private String quotationStatus;
	private String mphCode;
	private String mphName;
	private String from;
	private String to;
	private String product;
	private String lineOfBusiness;
	private String unitOffice;
	
	private String quotationNo;
	private Integer quotationId;
	private String pageName;
	private String role;
	private String userName;
	private Boolean isLoad;
	
	private String modifiedBy;
	private String createdBy;
	private Date modifiedOn;
	private Date createdOn;
	
}
