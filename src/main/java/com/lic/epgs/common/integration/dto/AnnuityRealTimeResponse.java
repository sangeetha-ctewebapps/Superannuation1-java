package com.lic.epgs.common.integration.dto;

import java.io.Serializable;
import java.util.Date;

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
public class AnnuityRealTimeResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String policyNumber;
	private String annuitantNumber;
	private String annuitantName;
	private String annuityNumber;
	private String adharNumber;
	private String pan;
	private String accountNumber;
	private String statusCode;
	private String status;
	private Date firstAnnuityInstallmentDueDate;
	private String referenceType;
	private String referenceId;
	
}
