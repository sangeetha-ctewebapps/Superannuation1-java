package com.lic.epgs.policyservicing.policylvl.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.policy.dto.MphAddressDto;
import com.lic.epgs.policy.dto.MphBankDto;
import com.lic.epgs.policy.dto.PolicyRulesDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.old.dto.PolicyAddressOldDto;
import com.lic.epgs.policy.old.dto.PolicyBankOldDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policy.old.dto.PolicyRulesOldDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;
import com.lic.epgs.policyservicing.policylvl.dto.policydetailschange.PolicyDetailsChangeDto;
import com.lic.epgs.policyservicing.policylvl.dto.policydetailschange.PolicyDtlsResponseDto;
import com.lic.epgs.policyservicing.policylvl.service.PolicyDetailsChangeService;
import com.lic.epgs.utils.CommonConstants;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/policyDetailsChange")
public class PolicyLevelDetailsChangeController {
	
	@Autowired
	private PolicyDetailsChangeService policyDetailsChangeService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/savePolicyDetailsChange")
	public PolicyDtlsResponseDto savePolicyDetailsChange(@RequestBody PolicyDetailsChangeDto policyDtlsDto) {
		logger.info("PolicyDetailsChangeController--savePolicyDetailsChange-started");
		PolicyDtlsResponseDto commonDto = null;
		try {
			commonDto = policyDetailsChangeService.savePolicyDetailsChange(policyDtlsDto);
		} catch (IllegalArgumentException e) {

			logger.error("PolicyDetailsChangeController--savePolicyDetailsChange-error", e);
			commonDto = new PolicyDtlsResponseDto();
			commonDto.setTransactionMessage(PolicyConstants.FAIL);
			commonDto.setTransactionStatus(PolicyConstants.ERROR);
		}
		logger.info("PolicyDetailsChangeController--savePolicyDetailsChange-ended");
		return commonDto;
	}
	
	@PostMapping("/saveAddressDetails")
	public PolicyDtlsResponseDto saveAddressDetails(@RequestBody PolicyAddressOldDto addressDto) {
		logger.info("PolicyDetailsChangeController--savePolicyDetailsChange-started");
		PolicyDtlsResponseDto commonDto = null;
		try {
			commonDto = policyDetailsChangeService.saveAddressDetails(addressDto);
		} catch (IllegalArgumentException e) {

			logger.error("PolicyDetailsChangeController--savePolicyDetailsChange-error", e);
			commonDto = new PolicyDtlsResponseDto();
			commonDto.setTransactionMessage(PolicyConstants.FAIL);
			commonDto.setTransactionStatus(PolicyConstants.ERROR);
		}
		logger.info("PolicyDetailsChangeController--savePolicyDetailsChange-ended");
		return commonDto;
	}
	@PutMapping("sendToChecker")
	public PolicyDtlsResponseDto sendToCheker(@RequestParam Long policyDtlsId) {
		logger.info("PolicyRestController:sendToCheker:started");
		PolicyDtlsResponseDto policyResponseDto = policyDetailsChangeService.changeStatus(policyDtlsId, PolicyConstants.CHECKER_NO);
		logger.info("PolicyRestController:sendToCheker:ended");
		return policyResponseDto;
	}

	@PutMapping("sendToMaker")
	public PolicyDtlsResponseDto sendToMaker(@RequestParam Long policyDtlsId) {
		logger.info("PolicyRestController:sendToMaker:started");
		PolicyDtlsResponseDto policyResponseDto = policyDetailsChangeService.changeStatus(policyDtlsId, PolicyConstants.MAKER_NO);
		logger.info("PolicyRestController:sendToMaker:ended");
		return policyResponseDto;
	}
	@PutMapping("sendToApproved")
	public PolicyDtlsResponseDto sendToApproved(@RequestParam Long policyDtlsId) {
		logger.info("PolicyRestController:sendToApproved:started");
		PolicyDtlsResponseDto policyResponseDto = policyDetailsChangeService.policyApproved(policyDtlsId);
		logger.info("PolicyRestController:sendToApproved:ended");
		return policyResponseDto;
	}

	@PostMapping("sendToReject")
	public PolicyDtlsResponseDto sendToReject(@RequestBody PolicyDetailsChangeDto policyDto) {
		logger.info("PolicyRestController:sendToReject:started");
		PolicyDtlsResponseDto policyResponseDto = policyDetailsChangeService.sendToReject(policyDto.getPolicyDtlsId(), PolicyConstants.REJECTED_NO,policyDto.getRejectionRemarks(),policyDto.getRejectionReasonCode());
		logger.info("PolicyRestController:sendToReject:ended");
		return policyResponseDto;
	}
	@PostMapping("/saveNotesDetails")
	public PolicyDtlsResponseDto saveNotesDetails(@RequestBody PolicyServiceNotesDto policyServiceNotesDto) {
	logger.info("PolicyController--saveNotesDetails--started");
	PolicyDtlsResponseDto commonDto = null;
	try {
		commonDto = policyDetailsChangeService.saveNotesDetails(policyServiceNotesDto);
	} catch (IllegalArgumentException e) {
	
		logger.error("PolicyController--saveNotesDetails-error", e);
		commonDto = new PolicyDtlsResponseDto();
		commonDto.setTransactionMessage(PolicyConstants.FAIL);
		commonDto.setTransactionStatus(PolicyConstants.ERROR);
	}
	logger.info("PolicyController--saveNotesDetails-ended");
	return commonDto;
	}
	
	@GetMapping("/getNoteList/{id}/{id1}")
	public PolicyDtlsResponseDto getNoteList(@PathVariable("id") Long policyId,@PathVariable("id1") Long serviceId) {
	logger.info("PolicyRestController:getNoteList:started");
	PolicyDtlsResponseDto commonDto = policyDetailsChangeService.getNoteList( policyId,serviceId);
	logger.info("PolicyRestController:getNoteList:ended");
	return commonDto;
	}
	
	@PostMapping("/saveBankDetails")
	public PolicyDtlsResponseDto saveBankDetails(@RequestBody PolicyBankOldDto policyBankDto) {
		logger.info("PolicyController--saveBankDetails--started");
		PolicyDtlsResponseDto commonDto = null;
		try {
			commonDto = policyDetailsChangeService.saveBankDetails(policyBankDto);
		} catch (IllegalArgumentException e) {

			logger.error("PolicyController--saveBankDetails--error", e);
			commonDto = new PolicyDtlsResponseDto();
			commonDto.setTransactionMessage(PolicyConstants.FAIL);
			commonDto.setTransactionStatus(PolicyConstants.ERROR);
		}
		logger.info("PolicyController--saveBankDetails--ended");
		return commonDto;
	} 
	@PostMapping("/saveRulesDetails")
	public PolicyDtlsResponseDto saveRulesDetails(@RequestBody PolicyRulesOldDto saveRulesDetails) {
		logger.info("PolicyController--saveBankDetails--started");
		PolicyDtlsResponseDto commonDto = null;
		try {
			commonDto = policyDetailsChangeService.saveRulesDetails(saveRulesDetails);
		} catch (IllegalArgumentException e) {

			logger.error("PolicyController--saveBankDetails--error", e);
			commonDto = new PolicyDtlsResponseDto();
			commonDto.setTransactionMessage(PolicyConstants.FAIL);
			commonDto.setTransactionStatus(PolicyConstants.ERROR);
		}
		logger.info("PolicyController--saveBankDetails--ended");
		return commonDto;
	} 
	@GetMapping("/getBankList/{id}")
	public PolicyDtlsResponseDto getBankList(@PathVariable("id") Long mphId) {
	logger.info("PolicyRestController:getBankList:started");
	PolicyDtlsResponseDto commonDto = policyDetailsChangeService.getBankList( mphId);
	logger.info("PolicyRestController:getBankList:ended");
	return commonDto;
	}
	
	@PutMapping("/removeBankDetails/{id}/{id1}")
	public PolicyDtlsResponseDto removeBankDetails(@PathVariable("id") Long policyId,@PathVariable("id1") Long bankAccountId) {
	logger.info("PolicyRestController:getBankList:started");
	PolicyDtlsResponseDto commonDto = policyDetailsChangeService.removeBankDetails( policyId,bankAccountId);
	logger.info("PolicyRestController:getBankList:ended");
	return commonDto;
	}
	@GetMapping("/inprogress/{id}")
	public PolicyDtlsResponseDto getInprogressPolicyById(@PathVariable("id") Long policyDtlsId) {
		logger.info("PolicyRestController:getInprogressPolicyById:started");
		PolicyDtlsResponseDto commonDto = policyDetailsChangeService.getPolicyById(CommonConstants.INPROGRESS, policyDtlsId);
		logger.info("PolicyRestController:getInprogressPolicyById:ended");
		return commonDto;
	}
	@GetMapping("/existing/{id}")
	public PolicyDtlsResponseDto getExistingQuotationById(@PathVariable("id") Long policyDtlsId) {
		logger.info("PolicyRestController:getExistingQuotationById:started");
		PolicyDtlsResponseDto commonDto = policyDetailsChangeService.getPolicyById(CommonConstants.EXISTING, policyDtlsId);
		logger.info("PolicyRestController:getExistingQuotationById:ended");
		return commonDto;
	}
	@PostMapping("/existing/citrieaSearch")
	public PolicyDtlsResponseDto getExistingPolicyByCitriea(@RequestBody PolicySearchDto policySearchDto) {
		logger.info("PolicyRestController:getExistingPolicyByCitriea:started");
		PolicyDtlsResponseDto commonDto = policyDetailsChangeService.existingCitrieaSearch(policySearchDto);
		logger.info("PolicyRestController:getExistingPolicyByCitriea:ended");
		return commonDto;
	}
	@PostMapping("/inprogress/citrieaSearch")
	public PolicyDtlsResponseDto getInprogressPolicyByCitriea(@RequestBody PolicySearchDto policySearchDto) {
		logger.info("PolicyRestController:getInprogressPolicyByCitriea:started");
		PolicyDtlsResponseDto commonDto = policyDetailsChangeService.inprogressCitrieaSearch(policySearchDto);
		logger.info("PolicyRestController:getInprogressPolicyByCitriea:ended");
		return commonDto;
	}
	@PostMapping("/existing/newcitrieaSearch")
	public PolicyResponseDto newcitrieaSearch(@RequestBody PolicySearchDto policySearchDto) {
		logger.info("PolicyRestController:getExistingPolicyByCitriea:started");
		PolicyResponseDto commonDto = policyDetailsChangeService.newcitrieaSearch(policySearchDto);
		logger.info("PolicyRestController:getExistingPolicyByCitriea:ended");
		return commonDto;
	}
	@GetMapping("/newcitrieaSearch/{id}")
	public PolicyDtlsResponseDto newcitrieaSearchById(@PathVariable("id") String policyNumber) {
		logger.info("PolicyRestController:getExistingQuotationById:started");
		PolicyDtlsResponseDto commonDto = policyDetailsChangeService.newcitrieaSearchById(policyNumber);
		logger.info("PolicyRestController:getExistingQuotationById:ended");
		return commonDto;
	}
	@GetMapping("/getAddressList/{id}")
	public PolicyDtlsResponseDto getAddressList(@PathVariable("id") Long mphId) {
		logger.info("PolicyRestController:getAddressList:started");
		PolicyDtlsResponseDto commonDto = policyDetailsChangeService.getAddressList(mphId);
		logger.info("PolicyRestController:getAddressList:ended");
		return commonDto;
	}
}
