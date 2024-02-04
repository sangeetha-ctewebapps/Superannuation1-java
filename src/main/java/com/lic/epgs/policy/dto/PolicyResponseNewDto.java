package com.lic.epgs.policy.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.List;

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
public class PolicyResponseNewDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<MphMasterDto> mphMasterDtoList;
	private MphMasterDto mphMasterDto;

	private List<PolicyMasterDto> policyMasterDtoList;
	private PolicyMasterDto policyMasterDto;
	
	private List<MemberMasterDto> memberMasterDtoList;
	private MemberMasterDto memberMasterDto;

	private List<PolicyNotesDto> policyNotesList;	
	private PolicyNotesDto policyNotesDto;

	private Long policyId;
	private Long mphId;
	private Long memberId;
		
	private String transactionStatus;
	private String transactionMessage;

}
