package com.lic.epgs.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FundedCommonMasterDto {
	
	private Long id;
	private String type;
	private String value;
	private Long codeId;
	private Boolean isActive;
	private String stream;
	private String displayValue;
	private String description;

}
