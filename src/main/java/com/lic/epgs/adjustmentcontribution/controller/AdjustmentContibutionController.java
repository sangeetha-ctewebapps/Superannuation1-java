package com.lic.epgs.adjustmentcontribution.controller;

/**
 * @author pradeepramesh
 *
 */

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lic.epgs.adjustmentcontribution.dto.ACSaveAdjustmentRequestDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionBatchHistoryResponse;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionBulkResponseDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionNotesDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionResponseDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentSearchDto;
import com.lic.epgs.adjustmentcontribution.service.AdjustmentContributionCalcService;
import com.lic.epgs.adjustmentcontribution.service.AdjustmentContributionService;
import com.lic.epgs.adjustmentcontribution.service.impl.AdjustmentContributionCalcServiceImpl;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.common.integration.dto.CommonIntegrationDto;
import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policy.service.impl.PolicyCalcServiceImpl;
import com.lic.epgs.policy.service.impl.PolicyServiceImpl;
import com.lic.epgs.utils.CommonConstants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/adjustmentContribution")
public class AdjustmentContibutionController {

	
	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	AdjustmentContributionService adjustmentContributionService;
	@Autowired
	AdjustmentContributionCalcService adjustmentContributionCalcService;
	@Autowired
	AdjustmentContributionCalcServiceImpl adjustmentContributionCalcServiceImpl;
	@Autowired
	IntegrationService integrationService;
	@Autowired
	PolicyCalcServiceImpl policyCalcServiceImpl;
	@Autowired
	PolicyServiceImpl policyServiceImpl;

	@PostMapping("/newcitrieaSearch")
	public PolicyResponseDto newcitrieaSearch(@RequestBody PolicySearchDto policySearchDto) {
		logger.info("AdjustmentContibutionController :: newcitrieaSearch :: newcitrieaSearchPradeep :: Start");
		PolicyResponseDto responseDto = adjustmentContributionService.newcitrieaSearchPradeep(policySearchDto);
		logger.info("AdjustmentContibutionController :: newcitrieaSearch :: newcitrieaSearchPradeep :: Ends");
		return responseDto;
		/** return adjustmentContributionService.newcitrieaSearch(policySearchDto); */
	}

	@GetMapping("/newcitrieaSearch/{mphId}/{policyId}")
	public AdjustmentContributionResponseDto newcitrieaSearchById(@PathVariable("mphId") Long mphId,@PathVariable("policyId") Long policyId) {
		logger.info("AdjustmentContibutionController :: newcitrieaSearchById :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionService.newcitrieaSearchById(mphId,policyId);
		logger.info("AdjustmentContibutionController :: newcitrieaSearchById :: Ends");
		return responseDto;
	}

	@PostMapping("/save")
	public AdjustmentContributionResponseDto save(@RequestBody AdjustmentContributionDto request) {
		logger.info("AdjustmentContibutionController :: save :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionService.save(request);
		logger.info("AdjustmentContibutionController :: save :: Ends");
		return responseDto;
	}

	@PostMapping("/notesave")
	public AdjustmentContributionResponseDto notesave(@RequestBody AdjustmentContributionNotesDto request) {
		logger.info("AdjustmentContibutionController :: notesave :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionService.notesave(request);
		logger.info("AdjustmentContibutionController :: notesave :: Ends");
		return responseDto;
	}

	@GetMapping("/getNotesList/{id}")
	public AdjustmentContributionResponseDto getNotesList(@PathVariable("id") Long adjustmentContributionId) {
		logger.info("AdjustmentContibutionController :: getNotesList :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionService.getNotesList(adjustmentContributionId);
		logger.info("AdjustmentContibutionController :: getNotesList :: Ends");
		return responseDto;
	}

	@PostMapping("/sendToChecker")
	public AdjustmentContributionResponseDto sendToCheker(@RequestBody AdjustmentContributionDto request) {
		request.setAdjustmentContributionStatus(PolicyConstants.CHECKER_NO);
		logger.info("AdjustmentContibutionController :: sendToCheker :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionService.changeStatus(request.getAdjustmentContributionId(), request.getAdjustmentContributionStatus(),request.getModifiedBy());
		logger.info("AdjustmentContibutionController :: sendToCheker :: Ends");
		return responseDto;
	}

	@PostMapping("/sendToMaker")
	public AdjustmentContributionResponseDto sendToMaker(@RequestBody AdjustmentContributionDto request) {
		request.setAdjustmentContributionStatus(PolicyConstants.MAKER_NO);
		logger.info("AdjustmentContibutionController :: sendToMaker :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionService.changeStatus(request.getAdjustmentContributionId(), request.getAdjustmentContributionStatus(),request.getModifiedBy());
		logger.info("AdjustmentContibutionController :: sendToMaker :: Ends");
		return responseDto;
	}

	@PostMapping("/reject")
	public AdjustmentContributionResponseDto reject(@RequestBody AdjustmentContributionDto request) {
		request.setAdjustmentContributionStatus(PolicyConstants.REJECTED_NO);
		logger.info("AdjustmentContibutionController :: reject :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionService.reject(request.getAdjustmentContributionId(), request.getAdjustmentContributionStatus(),request.getModifiedBy(), request.getRejectionRemarks(), request.getRejectionReasonCode());
		logger.info("AdjustmentContibutionController :: reject :: Ends");
		return responseDto;
	}

	@PostMapping(value = "/getInprogressLoad")
	public AdjustmentContributionResponseDto getInprogressLoad(@RequestBody AdjustmentSearchDto request) {
		logger.info("AdjustmentContibutionController :: getInprogressLoad :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionService.getInprogressLoad(request);
		logger.info("AdjustmentContibutionController :: getInprogressLoad :: Ends");
		return responseDto;
	}

	@GetMapping(value = "/getInprogressDetails")
	public AdjustmentContributionResponseDto getInprogressDetails(@RequestParam Long adjustmentContributionId) {
		logger.info("AdjustmentContibutionController :: getInprogressDetails :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionService.getInprogressDetails(adjustmentContributionId);
		logger.info("AdjustmentContibutionController :: getInprogressDetails :: Ends");
		return responseDto;
	}

	@PostMapping(value = "/getExisitngLoad")
	public AdjustmentContributionResponseDto getExisitngLoad(@RequestBody AdjustmentSearchDto request) {
		logger.info("AdjustmentContibutionController :: getExisitngLoad :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionService.getExisitngLoad(request);
		logger.info("AdjustmentContibutionController :: getExisitngLoad :: Ends");
		return responseDto;
	}

	@PostMapping(value = "/saveMasterTemp")
	public AdjustmentContributionResponseDto saveMasterTemp(@RequestBody CommonIntegrationDto dto) {
		logger.info("AdjustmentContibutionController :: saveMasterTemp :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionService.saveMasterTemp(dto);
		logger.info("AdjustmentContibutionController :: saveMasterTemp :: Ends");
		return responseDto;
	}

	@GetMapping(value = "/getExistingDetails")
	public AdjustmentContributionResponseDto getExistingDetails(@RequestParam Long adjustmentContributionId) {
		logger.info("AdjustmentContibutionController :: getExistingDetails :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionService.getExistingDetails(adjustmentContributionId);
		logger.info("AdjustmentContibutionController :: getExistingDetails :: Ends");
		return responseDto;
	}

	@PostMapping("/bulk/upload")
	public AdjustmentContributionBulkResponseDto bulkUploadForAdjustmentContribution(@RequestParam Long masterPolicyId,@RequestParam Long tempPolicyId, @RequestParam String createdBy,@RequestParam String adjustmentContributionId, @RequestParam String unitCode,@RequestParam("file") MultipartFile file) {
		logger.info("AdjustmentContibutionController -- bulkUploadForAdjustmentContribution --started");
		AdjustmentContributionBulkResponseDto commonDto = integrationService.bulkUploadForAdjustmentContribution(masterPolicyId, tempPolicyId, createdBy, adjustmentContributionId, unitCode, file);
		logger.info("AdjustmentContibutionController -- bulkUploadForAdjustmentContribution --ended");
		return commonDto;
	}

	@GetMapping("/bulk/remove")
	public AdjustmentContributionBulkResponseDto removeBatchForAdjustmentContribution(@RequestParam Long batchId ,
			@RequestParam(required=false) Long adjustmentContributionId,@RequestParam(required=false) Long policyId,@RequestParam String unitCode,
			@RequestParam String modifiedBy) {
		logger.info("AdjustmentContibutionController -- removeBatchForAdjustmentContribution --started");
		AdjustmentContributionBulkResponseDto commonDto = integrationService
				.removeBatchForAdjustmentContribution(batchId,adjustmentContributionId,policyId,unitCode,modifiedBy);
		logger.info("AdjustmentContibutionController -- removeBatchForAdjustmentContribution --ended");
		return commonDto;
	}

	@GetMapping("bulk/download")
	public void download(@RequestParam Long batchId, @RequestParam String fileType, HttpServletResponse response) {
		logger.info("AdjustmentContibutionController -- download --started");
		adjustmentContributionService.download(batchId, fileType, response);
		logger.info("AdjustmentContibutionController -- download --ended");
	}

	@PostMapping("/saveadjustment")
	public AdjustmentContributionResponseDto saveAdjustment(@RequestBody ACSaveAdjustmentRequestDto adjustmentDto) {
		logger.info("AdjustmentContibutionController :: saveAdjustment :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionCalcService.saveAdjustment(adjustmentDto);
		logger.info("AdjustmentContibutionController :: saveAdjustment :: Ends");
		return responseDto;
	}

	@PostMapping("/approve")
	public AdjustmentContributionResponseDto approve(@RequestBody AdjustmentContributionDto request) throws ApplicationException {
		request.setAdjustmentContributionStatus(PolicyConstants.APPROVED_NO);
		logger.info("AdjustmentContibutionController :: approve :: Start");
		AdjustmentContributionResponseDto approve = adjustmentContributionCalcService.approve(request.getAdjustmentContributionId(), request.getAdjustmentContributionStatus(),request.getModifiedBy(), request.getVariantType());
		
		if(approve.getTransactionStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
		
		logger.info("saveContribution::Start");
		policyServiceImpl.saveContributionToTxnEntries(approve.getPolicyId());
		logger.info("saveContribution::End");

		logger.info("saveZeroAccEntriesToTxnEntires::Start");
		policyCalcServiceImpl.setZeroAccEntriesToTxnEntires(approve.getPolicyId(), request.getVariantType());
		logger.info("saveZeroAccEntriesToTxnEntires::Ends");
		
		logger.info("AdjustmentContibutionController :: approve :: Ends");
		}
		return approve;
	}
	
	
	
	@GetMapping("/newcitrieaSearchById")
	public AdjustmentContributionResponseDto newcitrieaSearchById(
			@RequestParam String policyNumber,
			@RequestParam String unitId,
			@RequestParam  String receivedFrom , 
			@RequestParam  String receivedTo,
			@RequestParam  String adjustedFrom,
			@RequestParam  String adjustedTo) {
		logger.info("AdjustmentContibutionController :: newcitrieaSearchById :: Started");
		AdjustmentContributionResponseDto commonDto = adjustmentContributionService.newcitrieaSearchById(policyNumber, unitId, receivedFrom, receivedTo, adjustedFrom, adjustedTo);
		logger.info("AdjustmentContibutionController :: newcitrieaSearchById :: Ended");
		return commonDto;
	}
	
	@GetMapping("/policyDepositAdjustment/download")
	public ResponseEntity<InputStreamResource> policyDepositAdjustment(@RequestParam String policyNumber,
			@RequestParam String unitId,
			@RequestParam  String receivedFrom , 
			@RequestParam  String receivedTo,
			@RequestParam  String adjustedFrom,
			@RequestParam  String adjustedTo) throws IOException {
		logger.info("AdjustmentContibutionController -- policyDepositAdjustment --started");
		
		ByteArrayInputStream file = adjustmentContributionService.policyDepositAdjustment(policyNumber, unitId, receivedFrom, receivedTo, adjustedFrom, adjustedTo);
				
		 HttpHeaders headers = new HttpHeaders();
		 headers.add("Content-Disposition", "attachment; filename=Policy_Deposit_Adjustment.xlsx");	
		
		logger.info("AdjustmentContibutionController -- policyDepositAdjustment --ended");
		
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(file));
	}

	
	@GetMapping("getBatchHistory")
	public AdjustmentContributionBatchHistoryResponse getAllBathesByPolicyId(@RequestParam(required=false) Long adjustmentId) {
		logger.info("AdjustmentContibutionController -- getBatchHistory --started");
		AdjustmentContributionBatchHistoryResponse response = adjustmentContributionService.getBatchHistory(adjustmentId);
		logger.info("AdjustmentContibutionController -- getBatchHistory --ended");
		return response;
	}
	
	@PostMapping("/makerReject")
	public AdjustmentContributionResponseDto makerReject(@RequestBody AdjustmentContributionDto request) {
		request.setAdjustmentContributionStatus(PolicyConstants.REJECTED_NO);
		logger.info("AdjustmentContibutionController :: makerReject :: Start");
		AdjustmentContributionResponseDto responseDto = adjustmentContributionService.makerReject(request.getAdjustmentContributionId(),request.getModifiedBy(), request.getRejectionRemarks());
		logger.info("AdjustmentContibutionController :: makerReject :: Ends");
		return responseDto;
	}

	
	
}