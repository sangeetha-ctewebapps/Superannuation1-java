package com.lic.epgs.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.utils.CommonConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponseDto<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String status;
	private String message;
	private List<ErrorDto> error;
	private transient T data;

	public static <T> ApiResponseDto<T> success(T t) {
		ApiResponseDto<T> response = new ApiResponseDto<>();
		response.setStatus(CommonConstants.SUCCESS);
		response.setError(null);
		response.setMessage(null);
		response.setData(t);
		return response;
	}

	public static <T> ApiResponseDto<T> success(T t, String message) {
		ApiResponseDto<T> response = new ApiResponseDto<>();
		response.setStatus(CommonConstants.SUCCESS);
		response.setMessage(message);
		response.setData(t);
		return response;
	}

	public static <T> ApiResponseDto<T> error(List<ErrorDto> error) {
		ApiResponseDto<T> response = new ApiResponseDto<>();
		response.setStatus(CommonConstants.ERROR);
		response.setError(error);
		return response;
	}

	public static <T> ApiResponseDto<T> error(ErrorDto error) {
		ApiResponseDto<T> response = new ApiResponseDto<>();
		response.setStatus(CommonConstants.ERROR);
		List<ErrorDto> errorList = new ArrayList<>(1);
		errorList.add(error);
		response.setError(errorList);
		return response;
	}

}
