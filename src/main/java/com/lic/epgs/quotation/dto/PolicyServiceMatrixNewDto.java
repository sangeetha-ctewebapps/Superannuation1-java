package com.lic.epgs.quotation.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PolicyServiceMatrixNewDto {

    private String currentService;
	
	private String ongoingService;
	
	private String isAllowed;
	
	private Boolean isActive;
	
	private Date createdOn = new Date();
	
	private Date modifiedOn = new Date();
	
	private String modifiedBy;
	
	private String createdBy;
}
