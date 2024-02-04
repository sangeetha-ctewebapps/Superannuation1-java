/**
 * 
 */
package com.lic.epgs.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Muruganandam
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class InterestRequestDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Object requestData;
	private String policyNo;
	private String txnType;
	private String serviceType;
	private String username;
	private String depositId;
	private String collectionNo;
}
