package com.lic.epgs.policyservicing.policylvl.dto.conversion;

import java.io.Serializable;
import java.util.List;

import com.lic.epgs.policy.dto.PolicyMasterDto;
import com.lic.epgs.policy.dto.PolicyRulesDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceDocumentDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;
import com.lic.epgs.policyservicing.policylvl.dto.memberaddition.PolicyServiceMbrDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D Date:23-09-2022
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyConversionDetailsDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private PolicyLevelConversionDto policyLevelConversionDto;
	private List<PolicyServiceNotesDto> policyNotesDto;
	private List<PolicyRulesDto> policyRulesDto;
    private PolicyMasterDto policyDetails;
    private List<PolicyServiceMbrDto> policyMbrDtoList;
    private List<PolicyServiceDocumentDto> policyServiceDocument;
    
}
