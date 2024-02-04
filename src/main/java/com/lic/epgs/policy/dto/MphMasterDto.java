package com.lic.epgs.policy.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class MphMasterDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long mphId;
	private Long masterMphId;
	private Long tempMphId;
	private String mphCode;
	private String mphName;
	private String mphType;
	private String proposalNumber;
	private String proposalId;
	private String cin;
	private String pan;
	private String alternatePan;
	private Long landlineNo;
	private String countryCode;
	private Long mobileNo;
	private String emailId;
	private Integer fax;
	private Boolean isActive;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
	private String modifiedBy;
	
	private Set<MphAddressDto> mphAddress = new HashSet<>();
	private Set<MphBankDto> mphBank = new HashSet<>();
	private Set<MphRepresentativesDto> mphRepresentative = new HashSet<>();
	private Set<PolicyMasterDto> policyMaster = new HashSet<>();

}
