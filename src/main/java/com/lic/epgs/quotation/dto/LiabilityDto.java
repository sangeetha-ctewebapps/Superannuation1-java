package com.lic.epgs.quotation.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LiabilityDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer liabilityId;
	private String liability;
	private Integer internalValuation;
	private Integer externalaluation;
	private Integer validity;
}
