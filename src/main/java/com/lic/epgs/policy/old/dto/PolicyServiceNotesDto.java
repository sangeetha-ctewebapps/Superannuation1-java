package com.lic.epgs.policy.old.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter	
public class PolicyServiceNotesDto {
	
	private Long policyNoteId;

	private Long policyId;

	private String noteContents;

	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn ;
	
	private Long serviceId;
	
	private Boolean isActive;
}
