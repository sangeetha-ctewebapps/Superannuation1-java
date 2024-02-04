package com.lic.epgs.common.integration.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class Annuitymode {
	
	private Integer annuityModeId;
	private String  annuityModeCode;
	private String description;
	private Date createdOn;
	private String createdBy;

}
