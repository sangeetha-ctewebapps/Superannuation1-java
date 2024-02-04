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

import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceCommonResponseDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceDto;
import com.lic.epgs.policyservicing.policylvl.constants.FreeLookCancellationConstants;
import com.lic.epgs.policyservicing.policylvl.dto.freelookcancellation.FreeLookCancellationDto;
import com.lic.epgs.policyservicing.policylvl.dto.freelookcancellation.FreeLookCancellationResponseDto;
import com.lic.epgs.policyservicing.policylvl.dto.freelookcancellation.FreeLookCancellationSearchDto;
import com.lic.epgs.policyservicing.policylvl.service.PolicyFreeLookCancelService;
import com.lic.epgs.utils.CommonConstants;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/flc")
public class PolicyLevelFreeLookCancelController {


	@Autowired
	private PolicyFreeLookCancelService freeLookCancellationService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/saveFreeLookCancellationDetails")
	public PolicyServiceCommonResponseDto saveFreeLookCancellationDetailss(@RequestBody PolicyServiceDto flcDto) {
		logger.info("Policy--FLC--Controller--saveFreeLookCancellationDetailss-started");
		PolicyServiceCommonResponseDto commonDto = null;
		try {
			commonDto = freeLookCancellationService.saveFreeLookCancellationDetails(flcDto);
		} catch (IllegalArgumentException e) {
			logger.error("Policy--FLC--Controller--saveFreeLookCancellationDetailss-error", e);
			commonDto = new PolicyServiceCommonResponseDto();
			commonDto.setTransactionMessage(FreeLookCancellationConstants.FAIL);
			commonDto.setTransactionStatus(FreeLookCancellationConstants.ERROR);
		}
		logger.info("Policy--FLC--Controller--saveFreeLookCancellationDetailss-ended");
		return commonDto;
	}

	@PutMapping("sendToChecker")
	public FreeLookCancellationResponseDto sendToCheker(@RequestParam Long freeLookcancellationId) {
		logger.info("Policy--FLC--Controller:sendToCheker:started");
		FreeLookCancellationResponseDto freeLookCancellationResponseDto = freeLookCancellationService.changeStatus(
				freeLookcancellationId, FreeLookCancellationConstants.CHECKER_NO);
		logger.info("Policy--FLC--Controller:sendToCheker:ended");
		return freeLookCancellationResponseDto;
	}

	
	@PostMapping("removeFlcDocument")
	public FreeLookCancellationResponseDto removeflcDocument(@RequestParam  Long documentId , Long freeLookId,String modifiedby) {
		logger.info("Policy--FLC--Controller:removeflcDocument:started");
		FreeLookCancellationResponseDto freeLookCancellationResponseDto = new FreeLookCancellationResponseDto();
		freeLookCancellationResponseDto = freeLookCancellationService.removeFLCDocument(documentId,freeLookId,modifiedby);
		logger.info("Policy--FLC--Controller:removeflcDocument:ended");
		return freeLookCancellationResponseDto;
	}

	@PutMapping("sendToMaker")
	public FreeLookCancellationResponseDto sendToMaker(@RequestParam Long freeLookcancellationId) {
		logger.info("Policy--FLC--Controller:sendToMaker:started");
		FreeLookCancellationResponseDto freeLookCancellationResponseDto = freeLookCancellationService.changeStatus(
				freeLookcancellationId, FreeLookCancellationConstants.MAKER_NO);
		logger.info("Policy--FLC--Controller:sendToMaker:ended");
		return freeLookCancellationResponseDto;
	}

	@PutMapping("sendToApproved")
	public FreeLookCancellationResponseDto sendToApproved(@RequestParam Long freeLookcancellationId) {
		logger.info("Policy--FLC--Controller:sendToApproved:started");
		FreeLookCancellationResponseDto freeLookCancellationResponseDto = freeLookCancellationService
				
//				.changeStatus(
//				freeLookcancellationId, NumericUtils.convertStringToInteger(FreeLookCancellationConstants.APPROVED_NO));

		.freeLookCancellationApproved(freeLookcancellationId, FreeLookCancellationConstants.APPROVED_NO);
		 
		
		logger.info("Policy--FLC--Controller:sendToApproved:ended");
		return freeLookCancellationResponseDto;
	}

	@PutMapping("sendToReject")
	public FreeLookCancellationResponseDto sendToReject(@RequestParam Long freeLookcancellationId ,@RequestBody FreeLookCancellationDto dto) {
		logger.info("Policy--FLC--Controller:sendToReject:started");
		FreeLookCancellationResponseDto freeLookCancellationResponseDto = freeLookCancellationService.changeStatusReject(
				freeLookcancellationId, FreeLookCancellationConstants.REJECTED_NO,dto);
		logger.info("Policy--FLC--Controller:sendToReject:ended");
		return freeLookCancellationResponseDto;
	}

	@GetMapping("/getFreeLookCancellationDetails/{freeLookcancellationId}")
	public FreeLookCancellationResponseDto getFreeLookCancellationDetails(@PathVariable Long freeLookcancellationId) {
		logger.info("Policy--FLC--Controller--getFreeLookCancellationDetails-started");
		FreeLookCancellationResponseDto commonDto = null;
		try {
			commonDto = freeLookCancellationService.getFreeLookCancellationDetails(freeLookcancellationId);
		} catch (IllegalArgumentException e) {
			logger.error("Policy--FLC--Controller--getFreeLookCancellationDetails-error", e);
			commonDto = new FreeLookCancellationResponseDto();
			commonDto.setTransactionMessage(FreeLookCancellationConstants.FAIL);
			commonDto.setTransactionStatus(FreeLookCancellationConstants.ERROR);
		}
		logger.info("Policy--FLC--Controller--getFreeLookCancellationDetails-ended");
		return commonDto;
	}
	
	@GetMapping("/getnotesDetails/{freeLookcancellationId}")
	public FreeLookCancellationResponseDto getnotesDetails(@PathVariable Long freeLookcancellationId) {
		logger.info("Policy--FLC--Controller--getnotesDetails-started");
		FreeLookCancellationResponseDto commonDto = null;
		try {
			commonDto = freeLookCancellationService.getnotesDetails(freeLookcancellationId);
		} catch (IllegalArgumentException e) {
			logger.error("Policy--FLC--Controller--getnotesDetails-error", e);
			commonDto = new FreeLookCancellationResponseDto();
			commonDto.setTransactionMessage(FreeLookCancellationConstants.FAIL);
			commonDto.setTransactionStatus(FreeLookCancellationConstants.ERROR);
		}
		logger.info("Policy--FLC--Controller--getnotesDetails-ended");
		return commonDto;
	}
	
	@GetMapping("/getFlcDocumentDetails/{freeLookcancellationId}")
	public FreeLookCancellationResponseDto getFlcDocumentDetails(@PathVariable Long freeLookcancellationId) {
		logger.info("Policy--FLC--Controller--getFlcDocumentDetails-started");
		FreeLookCancellationResponseDto commonDto = null;

		try {
			commonDto = freeLookCancellationService.getFlcDocumentDetails(freeLookcancellationId);
		} catch (IllegalArgumentException e) {

			logger.error("Policy--FLC--Controller--getFlcDocumentDetails-error", e);
			commonDto = new FreeLookCancellationResponseDto();
			commonDto.setTransactionMessage(FreeLookCancellationConstants.FAIL);
			commonDto.setTransactionStatus(FreeLookCancellationConstants.ERROR);
		}
		logger.info("Policy--FLC--Controller--getFlcDocumentDetails-ended");
		return commonDto;
	}

	@GetMapping("/inprogress/{id}")
	public FreeLookCancellationResponseDto getInprogressPolicyById(@PathVariable("id") Long freeLookcancellationId) {
		logger.info("Policy--FLC--Controller:getInprogressPolicyById:started");
		FreeLookCancellationResponseDto commonDto = freeLookCancellationService.getFreeLookCancellationById(CommonConstants.INPROGRESS, freeLookcancellationId);
		logger.info("Policy--FLC--Controller:getInprogressPolicyById:ended");
		return commonDto;
	}

	@GetMapping("/existing/{id}")
	public FreeLookCancellationResponseDto getExistingQuotationById(@PathVariable("id") Long freeLookcancellationId) {
		logger.info("Policy--FLC--Controller:getExistingQuotationById:started");
		FreeLookCancellationResponseDto commonDto = freeLookCancellationService.getFreeLookCancellationById(CommonConstants.EXISTING, freeLookcancellationId);
		logger.info("Policy--FLC--Controller:getExistingQuotationById:ended");
		return commonDto;
	}

	@PostMapping("/existing/citrieaSearch")
	public FreeLookCancellationResponseDto getExistingPolicyByCitriea(
			@RequestBody FreeLookCancellationSearchDto freeLookCancellationSearchDto) {
		logger.info("Policy--FLC--Controller:getExistingPolicyByCitriea:started");
		FreeLookCancellationResponseDto commonDto = freeLookCancellationService.existingCitrieaSearch(freeLookCancellationSearchDto);
		logger.info("Policy--FLC--Controller:getExistingPolicyByCitriea:ended");
		return commonDto;
	}

	@PostMapping("/inprogress/citrieaSearch")
	public FreeLookCancellationResponseDto getInprogressPolicyByCitriea(
			@RequestBody FreeLookCancellationSearchDto freeLookCancellationSearchDto) {
		logger.info("Policy--FLC--Controller:getInprogressPolicyByCitriea:started");
		FreeLookCancellationResponseDto commonDto = freeLookCancellationService.inprogressCitrieaSearch(freeLookCancellationSearchDto);
		logger.info("Policy--FLC--Controller:getInprogressPolicyByCitriea:ended");
		return commonDto;
	}
	
	//  9090/api/flc/existingPolicy/citrieaSearch
	
	@PostMapping("/existingPolicy/citrieaSearch")
	public PolicyResponseDto getExistingPolicyByCitriea(@RequestBody PolicySearchDto policySearchDto) {
		logger.info("PolicyRestController:getExistingPolicyByCitriea:started");
		PolicyResponseDto commonDto = freeLookCancellationService.existingPolicyCitrieaSearch(policySearchDto);
		logger.info("PolicyRestController:getExistingPolicyByCitriea:ended");
		return commonDto;
	}

	
}
