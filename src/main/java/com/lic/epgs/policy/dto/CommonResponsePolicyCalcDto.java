/**
 * 
 */
package com.lic.epgs.policy.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Muruganandam
 * @param <T>
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonResponsePolicyCalcDto<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient T responseData;
	/***
	 * private InterestFundResponseDto interestFundResponse;
	 */

	private String message;

	@JsonIgnore(value = true)
	private String transactionMessage;

	private String code;

	private String status;
	private @Default Integer statusId = 0;

	private String exception;
	private String batchNumber;

	private Integer pageNo;
	private Integer perPage;
	private Integer total;
	private String columnSort;
	private String columnOrder;
	private List<String> errors;
	private Map<String, String> responseSummary;

}
