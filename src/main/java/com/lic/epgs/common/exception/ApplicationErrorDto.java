/**
 * 
 */
package com.lic.epgs.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Muruganandam
 *
 */
@Getter
@Setter
@Builder
public class ApplicationErrorDto {
	private String transactionMessage;
	private String transactionStatus;
	private Long errorRefNo;
	private Integer errorCode;
}
