package com.lic.epgs.common.integration.dto;

/**
 * @author Logesh.D
 *
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositReceiptGenerationDto {

	private String returnCode;
	private String responseMessage;

	@JsonProperty("Base64Code")
	private String base64Code;
}
