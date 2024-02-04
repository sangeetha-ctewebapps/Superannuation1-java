package com.lic.epgs.common.integration.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AnnuityRealTimeData {
	 @JsonProperty
	private List<AnnuityRealTimeResponse> DATA;
	private String message;
	private Integer status;
}