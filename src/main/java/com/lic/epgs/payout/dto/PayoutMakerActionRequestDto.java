package com.lic.epgs.payout.dto;

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
public class PayoutMakerActionRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String payoutNo;
	
	private String initiMationNo;

	private Integer action;

	private String checkerCode;

	private String makerCode;

	private String repudationReason;
	
	private String modifiedBy;

}
