/**
 * 
 */
package com.lic.epgs.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author SanthoshKumar.E
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonExternalApiResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient Object responseData;
	private String transactionMessage;
	private String transactionStatus;

}
