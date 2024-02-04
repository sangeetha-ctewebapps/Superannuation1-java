package com.lic.epgs.common.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter

public class CommonDocumentMasterDto {

	private Long documentId;
	
	private Long productId;
	
	private Long variantId;
	
	private String documentCategory;
	
	private String documentSubCategory;
	
	private String documentType;
	
	private Boolean isMandatory;
	
	private Boolean isActive;
	
	private String createdBy;
	
	private Date createdOn;
	
	private String modifiedBy;
	
	private Date modifiedOn;
}
