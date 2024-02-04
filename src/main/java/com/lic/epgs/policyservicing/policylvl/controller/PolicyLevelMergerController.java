package com.lic.epgs.policyservicing.policylvl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.common.dto.CommonDocsDto;
import com.lic.epgs.common.dto.CommonNotesDto;
import com.lic.epgs.policyservicing.policylvl.dto.merger.PolicyLevelMergerApiResponse;
import com.lic.epgs.policyservicing.policylvl.dto.merger.PolicyLevelMergerDto;
import com.lic.epgs.policyservicing.policylvl.dto.merger.PolicyLevelMergerSearchDto;
import com.lic.epgs.policyservicing.policylvl.dto.merger.PolicyLevelServiceDto;
import com.lic.epgs.policyservicing.policylvl.service.PolicyMergerService;
import com.lic.epgs.utils.CommonConstants;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policyLevelMerger")
public class PolicyLevelMergerController {

	@Autowired
	private PolicyMergerService policyLevelMergerService;

	@PostMapping("/savePolicyLevelMerge")
	public PolicyLevelMergerApiResponse saveAndUpdatePolicyLevelMerger(
			@RequestBody PolicyLevelMergerDto policyLevelMergerDto) {
		return policyLevelMergerService.saveAndUpdatePolicyLevelMerger(policyLevelMergerDto);
	}

	@PostMapping("/sendToChecker")
	public PolicyLevelMergerApiResponse sendToChecker(@RequestBody PolicyLevelMergerDto policyLevelMergerDto) {
		return policyLevelMergerService.sendToChecker(policyLevelMergerDto);
	}
	
	@PostMapping("/sendToMaker")
	public PolicyLevelMergerApiResponse sendToMaker(@RequestBody PolicyLevelMergerDto policyLevelMergerDto) {
		return policyLevelMergerService.sendToChecker(policyLevelMergerDto);
	}
	
	@PostMapping("/approved")
	public PolicyLevelMergerApiResponse approvedAndRejectPolicyLevelMerger(
			@RequestBody PolicyLevelMergerDto policyLevelMergerDto) {
		return policyLevelMergerService.approvedAndRejectPolicyLevelMerger(policyLevelMergerDto);
	}

	@PostMapping("/rejected")
	public PolicyLevelMergerApiResponse rejectedAndRejectPolicyLevelMerger(
			@RequestBody PolicyLevelMergerDto policyLevelMergerDto) {
		return policyLevelMergerService.approvedAndRejectPolicyLevelMerger(policyLevelMergerDto);
	}

	@PostMapping("/uploadDocument")
	public PolicyLevelMergerApiResponse uploadDocument(@RequestBody CommonDocsDto docsDto) {
		return policyLevelMergerService.uploadDocument(docsDto);
	}
	
	@GetMapping("/removeDocument")
	public PolicyLevelMergerApiResponse removeDocument(@RequestParam Integer docId,@RequestParam Long mergerId ) {
		return policyLevelMergerService.removeDocument(docId,mergerId);
	}

	@GetMapping("/inprogressMergeLoad")
	public PolicyLevelMergerApiResponse inprogressMergeLoad(@RequestParam String unitCode, String roleType) {
		return policyLevelMergerService.inprogressMergerLoad(unitCode, roleType, CommonConstants.INPROGRESS);
	}

	@GetMapping("/existingMergeLoad")
	public PolicyLevelMergerApiResponse exitingMergeLoad(@RequestParam String unitCode, String roleType) {
		return policyLevelMergerService.inprogressMergerLoad(unitCode, roleType, CommonConstants.EXISTING);
	}

	@PostMapping("/saveMergeNotes")
	public PolicyLevelMergerApiResponse saveNotedPolicyMeger(@RequestBody CommonNotesDto commonNotesDto) {
		return policyLevelMergerService.saveNotedPolicyMeger(commonNotesDto);
	}
	
	
	@GetMapping("/getPolicyMergebyMergeId")
	public PolicyLevelMergerApiResponse getPolicyMergebyMergeId(@RequestParam Long mergeId) {
		return policyLevelMergerService.getPolicyMergebyMergeId(mergeId);
	}
	
	@GetMapping("/getMasterPolicyMergebyMergeId")
	public PolicyLevelMergerApiResponse getMasterPolicyMergebyMergeId(@RequestParam Long mergeId) {
		return policyLevelMergerService.getMasterPolicyMergebyMergeId(mergeId);
	}
	
	@PostMapping("/getCriteriaSearchPolicy")
	public PolicyLevelMergerApiResponse getCriteriaSearchPolicy(@RequestBody PolicyLevelMergerSearchDto policyLevelMergerSearchDto) {
		return policyLevelMergerService.getCriteriaSearchPolicy(policyLevelMergerSearchDto);
	}
	
	@PostMapping("/getexistingCriteriaSearchPolicy")
	public PolicyLevelMergerApiResponse getexistingCriteriaSearchPolicy(@RequestBody PolicyLevelMergerSearchDto policyLevelMergerSearchDto) {
		return policyLevelMergerService.getexistingCriteriaSearchPolicy(policyLevelMergerSearchDto);
	}
	
	
//	@PostMapping("/existing/citrieaSearch")
//	public PolicyResponseDto getExistingPolicyByCitriea(@RequestBody PolicySearchDto policySearchDto) {
//		PolicyResponseDto commonDto = policyLevelMergerService.existingCitrieaSearch(policySearchDto);
//		return commonDto;
//	}
//	
//	@GetMapping("/existingbyId/{id}")
//	public PolicyResponseDto getExistingQuotationById(@PathVariable("id") Long policyId) {
//		PolicyResponseDto commonDto = policyLevelMergerService.getPolicyById(CommonConstants.EXISTING, policyId);
//		return commonDto;
//	}
	
	
	

}
