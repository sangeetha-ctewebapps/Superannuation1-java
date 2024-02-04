package com.lic.epgs.regularadjustmentcontribution.dto;
import java.io.Serializable;
/**
 * @author pradeepramesh
 *
 */
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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

public class RegularAdjustmentContributionNotesDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long notesId;
	private Long regularContributionId;
	private String noteContents;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private String createdBy;
	private Long policyId;

	
}
