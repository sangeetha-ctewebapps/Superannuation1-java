package com.lic.epgs.policy.contribution.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomResponseEntity<T> {
	private int statusCode;
	private String statusMessage;
	private T data;
}
