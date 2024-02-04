package com.lic.epgs.fund.dto;

import java.util.Date;

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
public class VouchereffectiveRequest {

	private String collectionNo;
	private String newEffectiveDateOfPayment;
	
//	private Date newEffectiveDateOfPayment;
	
}
