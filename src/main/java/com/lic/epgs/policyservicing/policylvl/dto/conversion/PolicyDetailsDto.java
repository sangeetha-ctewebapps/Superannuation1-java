package com.lic.epgs.policyservicing.policylvl.dto.conversion;

import java.io.Serializable;

import com.lic.epgs.policy.dto.MphMasterDto;
import com.lic.epgs.policy.dto.PolicyMasterDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D Date:25-01-2023
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyDetailsDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private PolicyMasterDto policyMaster;
	private MphMasterDto mphMaster;

}
