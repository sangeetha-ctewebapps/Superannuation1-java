package com.lic.epgs.policy.contribution.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.policy.contribution.dao.PolicyContributionDao;
import com.lic.epgs.policy.contribution.dto.PolicyContributionResponseDto;
import com.lic.epgs.policy.contribution.service.PolicyContributionService;

@Service
public class PolicyContributionServiceImpl implements PolicyContributionService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	PolicyContributionDao policyContributionDao;

	@Override
	public List<PolicyContributionResponseDto> getPolcyContributionsDetails(Integer policyId, String financialYear,
			String quarter) {
		return policyContributionDao.getPolcyContributionsDetails(policyId, financialYear, quarter);
	}

}
