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
public class PayoutUpdateRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer modeOfExit;

	private String dateOfExit;

	private Long reasonForExit;

	private String reasonForOther;

	private String placeOfEvent;

	private String payoutNo;

}
