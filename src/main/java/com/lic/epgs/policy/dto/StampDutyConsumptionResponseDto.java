package com.lic.epgs.policy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StampDutyConsumptionResponseDto {
	private String challanGrnNumber;	
	private String challanGrnDate;	
	private String certificateQueryIdNo;
	private String purchaserUnitCode;
	private String consumerUnitsCode;
	private String requestAmount;
	private String responseStatus;
	private String responseMessage;


}
