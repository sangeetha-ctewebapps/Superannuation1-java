package com.lic.epgs.policy.contribution.dao;

import java.util.List;

import com.lic.epgs.policy.contribution.dto.PolicyContributionResponseDto;

public interface PolicyContributionDao {
	List<PolicyContributionResponseDto> getPolcyContributionsDetails(Integer policyId, String financialYear,
			String quarter);
}
