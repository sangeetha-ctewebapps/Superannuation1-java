package com.lic.epgs.common.gi.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DepositTransferDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Object responseObj;
	private String transactionStatus;
	private String transactionMessage;
}