package com.lic.epgs.claim.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClaimMbrRequestDto {
	
	private String claimNo;
	private Long annuityOption;
	private String nomineeCode;

}
