package com.lic.epgs.claim.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D Date:02-01-2023
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClaimFundPayableRequestDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String amountPayable;
	private Long mphId;
	private Long memberId;
	private Long nomineeId;

}
