package com.lic.epgs.policy.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssuePolicyResponseDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String van;
	private String bankUniqueId;
	private String responseMessage;

}
