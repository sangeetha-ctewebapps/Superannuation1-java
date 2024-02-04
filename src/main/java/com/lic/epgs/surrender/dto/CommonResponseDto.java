package com.lic.epgs.surrender.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D Date:20-09-2022
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommonResponseDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private transient Object responseData;
	private String transactionMessage;
	private String transactionStatus;

}
