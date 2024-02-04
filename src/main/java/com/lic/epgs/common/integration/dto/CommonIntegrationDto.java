/**
 * 
 */
package com.lic.epgs.common.integration.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Karthick M
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonIntegrationDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long policyId;
	private List<String> licId;
	
	private String transactionStatus;
	private String transactionMessage;


}
