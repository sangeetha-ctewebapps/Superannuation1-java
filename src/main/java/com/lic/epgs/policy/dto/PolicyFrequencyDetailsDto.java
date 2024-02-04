package com.lic.epgs.policy.dto;

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
public class PolicyFrequencyDetailsDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long frequencyId;
	
	private Long policyId;
	
	private String frequency;
	
	private String frequencyDates;
	
	private String status;
	
	private String progress;
	
	private String modifiedBy;

	private Date modifiedOn = new Date();

	private String createdBy;

	private Date createdOn = new Date();

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyCommencementDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyEndDate;

	private Boolean isActive = Boolean.TRUE;

}
