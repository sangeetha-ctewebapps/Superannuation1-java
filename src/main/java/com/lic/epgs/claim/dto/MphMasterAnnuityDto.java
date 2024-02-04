package com.lic.epgs.claim.dto;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lic.epgs.policy.dto.MphAddressDto;
import com.lic.epgs.policy.dto.MphBankDto;
import com.lic.epgs.policy.dto.MphRepresentativesDto;
import com.lic.epgs.policy.dto.PolicyMasterDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MphMasterAnnuityDto {

	
	private static final long serialVersionUID = 1L;


	private String mphCode;
	private String mphName;

	private String proposalId;

	private String cin;

	private String pan;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private String createdBy;
	private String modifiedBy;
	
//	data from commonModule
	  private String mphSource;
	  private String customerCode;
	  private String customerName;
	  private String customerType;
	  private String industryType;
	  private String customerGroupName;
	  private String apan;
	  private String gstinApplicable;
	  private String gstin;
	  private Long gstinRate;
	  private String urbanOrRural;
	  private String productCode;
	  private Long numberOfLives;
	  private String marketingOfficername;
	  private String marketingOfficercode;
	  private String intermediaryName;
	  private String intermediaryCode;

	
	
	
}
