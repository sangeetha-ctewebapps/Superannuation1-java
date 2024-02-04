/**
 * 
 */
package com.lic.epgs.policyservicing.policylvl.service.impl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.lic.epgs.common.dto.CommonDocsDto;
import com.lic.epgs.common.dto.CommonNotesDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policyservicing.policylvl.dto.merger.PolicyLevelMergerApiResponse;
import com.lic.epgs.policyservicing.policylvl.dto.merger.PolicyLevelMergerDto;
import com.lic.epgs.policyservicing.policylvl.dto.merger.PolicyLevelMergerSearchDto;
import com.lic.epgs.policyservicing.policylvl.service.PolicyMergerService;

/**
 * @author Muruganandam
 *
 */
@Service
public class PolicyMergerServiceImpl implements PolicyMergerService{
	
	protected final Logger logger = LogManager.getLogger(getClass());

	@Override
	public PolicyLevelMergerApiResponse saveAndUpdatePolicyLevelMerger(PolicyLevelMergerDto policyLevelMergerDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyLevelMergerApiResponse sendToChecker(PolicyLevelMergerDto policyLevelMergerDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyLevelMergerApiResponse approvedAndRejectPolicyLevelMerger(PolicyLevelMergerDto policyLevelMergerDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyLevelMergerApiResponse uploadDocument(CommonDocsDto docsDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyLevelMergerApiResponse inprogressMergerLoad(String unitCode, String roleType, String inprogress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyLevelMergerApiResponse saveNotedPolicyMeger(CommonNotesDto commonNotesDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyLevelMergerApiResponse getPolicyMergebyMergeId(Long mergeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyLevelMergerApiResponse getMasterPolicyMergebyMergeId(Long mergeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyLevelMergerApiResponse getCriteriaSearchPolicy(PolicyLevelMergerSearchDto policyLevelMergerSearchDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyLevelMergerApiResponse getexistingCriteriaSearchPolicy(
			PolicyLevelMergerSearchDto policyLevelMergerSearchDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyLevelMergerApiResponse removeDocument(Integer docId, Long mergerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyResponseDto existingCitrieaSearch(PolicySearchDto policySearchDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyResponseDto getPolicyById(String existing, Long policyId) {
		// TODO Auto-generated method stub
		return null;
	} 
	
	
	

	

	
}