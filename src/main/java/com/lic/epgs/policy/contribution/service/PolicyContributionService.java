package com.lic.epgs.policy.contribution.service;

import java.util.List;

import com.lic.epgs.policy.contribution.dto.PolicyContributionResponseDto;

public interface PolicyContributionService {

	List<PolicyContributionResponseDto> getPolcyContributionsDetails(Integer policyId, String financialYear,
			String quarter);

}
