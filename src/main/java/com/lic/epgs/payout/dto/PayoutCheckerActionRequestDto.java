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
public class PayoutCheckerActionRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String payoutNo;

	private Integer action;

	private String approverCode;

	private String unitCode;
	
	private String makerCode;
	
	private String initiMationNo;
	
	private String modifiedBy;

}
