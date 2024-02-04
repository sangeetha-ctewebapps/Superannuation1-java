/**
 * 
 */
package com.lic.epgs.common.exception;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Dhatchanamoorthy M
 *
 */

public class ErrorDescriptions implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("errorCode")
	private String errorCode;
	@JsonProperty("errorDescription")
	private String errorDescription;
	
	
	public String getErrorCode() {
		return errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
}
