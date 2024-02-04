/**
 * 
 */
package com.lic.epgs.policyservicing.policylvl.service;

import com.lic.epgs.common.dto.CommonDocsDto;
import com.lic.epgs.common.dto.CommonNotesDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policyservicing.policylvl.dto.merger.PolicyLevelMergerApiResponse;
import com.lic.epgs.policyservicing.policylvl.dto.merger.PolicyLevelMergerDto;
import com.lic.epgs.policyservicing.policylvl.dto.merger.PolicyLevelMergerSearchDto;
import com.lic.epgs.policyservicing.policylvl.dto.merger.PolicyLevelServiceDto;

/**
 * @author Muruganandam
 *
 */
public interface PolicyMergerService {

	PolicyLevelMergerApiResponse saveAndUpdatePolicyLevelMerger(PolicyLevelMergerDto policyLevelMergerDto);

	PolicyLevelMergerApiResponse sendToChecker(PolicyLevelMergerDto policyLevelMergerDto);

	PolicyLevelMergerApiResponse approvedAndRejectPolicyLevelMerger(PolicyLevelMergerDto policyLevelMergerDto);

	PolicyLevelMergerApiResponse uploadDocument(CommonDocsDto docsDto);

	PolicyLevelMergerApiResponse inprogressMergerLoad(String unitCode, String roleType, String inprogress);

	PolicyLevelMergerApiResponse saveNotedPolicyMeger(CommonNotesDto commonNotesDto);

	PolicyLevelMergerApiResponse getPolicyMergebyMergeId(Long mergeId);

	PolicyLevelMergerApiResponse getMasterPolicyMergebyMergeId(Long mergeId);

	PolicyLevelMergerApiResponse getCriteriaSearchPolicy(PolicyLevelMergerSearchDto policyLevelMergerSearchDto);

	PolicyLevelMergerApiResponse getexistingCriteriaSearchPolicy(PolicyLevelMergerSearchDto policyLevelMergerSearchDto);

	PolicyLevelMergerApiResponse removeDocument(Integer docId, Long mergerId);

	PolicyResponseDto existingCitrieaSearch(PolicySearchDto policySearchDto);

	PolicyResponseDto getPolicyById(String existing, Long policyId);
   
	


}
