package com.lic.epgs.common.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	private String message;
}
