package com.lic.epgs.payout.restapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PayoutAnnuityRestApiRes {

	PayoutAnnuityRestApiResponse data;
	private String message;
	private Integer status;
}
