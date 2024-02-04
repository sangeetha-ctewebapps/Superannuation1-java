package com.lic.epgs.claim.dto;

import java.io.Serializable;

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
public class ClaimCheckerActionRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String claimNo;

	private Integer action;

	private String approverCode;

	private String makerCode;
	
	private String repudationReason;

	public String modifiedBy;
	

}
