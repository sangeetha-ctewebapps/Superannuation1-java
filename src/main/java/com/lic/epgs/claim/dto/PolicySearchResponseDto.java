package com.lic.epgs.claim.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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
public class PolicySearchResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mphCode;

	private String mphName;

	private String policyNo;

	private String policyId;

	private String policyStatus;

	private String product;

	private String category;

	private PolicySearchMemberDto memberDto;

	private Set<PolicySearchMemberDto> member;

	private String varient;

	private Date commencementDate;

	private String unitCode;

}