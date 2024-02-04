package com.lic.epgs.policy.old.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyNotesOldDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long policyNoteId;
	private Long policyId;
	private String noteContents;
	private Long serviceId;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	private Boolean isActive= Boolean.TRUE;
}
