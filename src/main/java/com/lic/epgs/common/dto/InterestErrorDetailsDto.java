/**
 * 
 */
package com.lic.epgs.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author vijayt
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InterestErrorDetailsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String userName;

	private String refNumber;

	private String policyNo;

	private String memberId;

	private String createdOn;

	private String modifiedOn;

	private Boolean isFail;

	private Integer noOfAttempt;
	private String txnType;

	private String ipAddress;

	private String remarks;

	private String errorDescription;

	private String depositId;
	
	private String collectionNo;

}