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
public class TrnRegistrationResponseDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String returnCode;
	private String challanNo;
	private String van;
	private String BankBranch;
	private String Bank;
	private String IFSCCode;
	private String bankUniqueId;
	private String TRINo;
	private String responseMessage;
	private String referenceIdType;
	
}
