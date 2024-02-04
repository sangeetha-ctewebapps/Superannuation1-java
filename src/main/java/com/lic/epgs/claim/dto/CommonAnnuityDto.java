package com.lic.epgs.claim.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lic.epgs.policy.dto.PolicySearchDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonAnnuityDto {

	private static final long serialVersionUID = 1L;
	
	private PolicyMasterAnnuityDto policyDetailsSupAnnuRequest;
	private MphMasterAnnuityDto mphbasicDetailRequest;
	private List<MphAddressDetailsAnnuityDto> mphaddressDetailsRequestList;
	private MphContactDetailsAnnuityDto mphcontactDetailsRequest;
	private MphBankAnnuityDto mphBankDetailRequest;
	
	private String status;
	private String message;
}
