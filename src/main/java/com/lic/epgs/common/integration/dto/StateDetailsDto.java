package com.lic.epgs.common.integration.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
public class StateDetailsDto {
		
	private Integer stateId;
	private String stateCode;
	private String description;
	private String isActive;
	private String isDeleted;
	private String createdBy;
	private String modifiedBy;
	private Date createdOn;
	private Date modifiedOn;
	private String code;
	private String type;
	private String pan;
	private String gstin;

	
}
