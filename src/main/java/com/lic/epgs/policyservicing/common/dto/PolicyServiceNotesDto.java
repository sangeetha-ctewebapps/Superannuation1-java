package com.lic.epgs.policyservicing.common.dto;

import java.io.Serializable;
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
public class PolicyServiceNotesDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long policyNoteId;

	private Long policyId;

	private String noteContents;

	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn ;
	
	private Long serviceId;
	
	private Boolean isActive;
	
	private Long conversionId;
	
	private Long mergeId;
	
	private Long memberAdditionId;
	
	private Long policyDtlsId;
	
	private Long freeLookId;
	
	private Long trnsfrId;
}
