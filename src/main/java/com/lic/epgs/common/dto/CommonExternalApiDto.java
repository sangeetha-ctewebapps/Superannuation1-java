package com.lic.epgs.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class CommonExternalApiDto {
	private String id;
	private String code;
	private String description;
	private String ismandatory;
	private String status;
}
