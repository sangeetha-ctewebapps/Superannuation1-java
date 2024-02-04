/**
 * 
 */
package com.lic.epgs.common.integration.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lic.epgs.common.integration.dto.AnnuityRealTimeData;
import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.quotation.dto.QuotationMemberBulkResponseDto;
import com.lic.epgs.common.integration.dto.DepositReceiptGenerationDto;

import com.lic.epgs.common.integration.dto.DepositCollectBulkUploadResponseDto;

/**
 * @author Muruganandam
 *
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/common/integration")
public class IntegrationController {
	
	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private IntegrationService integrationService;

//	@PostMapping(value = "accounting/getMphAndICodeDetail", produces = { MediaType.APPLICATION_JSON_VALUE })
//	public ResponseEntity<Object> getMphAndIcodeDetail(@RequestBody MphAndIcodeRequestDto requestDto) {
//		return ResponseEntity.ok().body(integrationService.getMphAndIcodeDetail(requestDto));
//	}
	
	
	
	@PostMapping("aom/bulk/upload")
	public QuotationMemberBulkResponseDto saveBulkForAom(@RequestParam(required = false) final Long serviceId,@RequestParam final String serviceNumber,
													@RequestParam final String unitCode,@RequestParam final String policyNumber,
													@RequestParam final String createdBy,@RequestParam(required = false) final Long memberAdditionId,
													@RequestParam final MultipartFile file) throws Exception {
		logger.info("IntegrationController -- saveBulkForAom --started");
		QuotationMemberBulkResponseDto commonDto = integrationService.saveBulkForAom(serviceId,serviceNumber,unitCode,policyNumber,createdBy,memberAdditionId,file);
		logger.info("IntegrationController -- saveBulkForAom --ended");
		return commonDto;
	}
	
	
	
	
	@GetMapping("/aom/bulk/remove")
	public QuotationMemberBulkResponseDto removeBatchForAom(@RequestParam Long batchId,@RequestParam Long policyId) {
		logger.info("MemberAdditionController -- bulkRemove --started");
		QuotationMemberBulkResponseDto commonDto = integrationService.removeBatchForAom(batchId,policyId);
		logger.info("MemberAdditionController -- bulkRemove --ended");
		return commonDto;
	}
	
	
	
	@GetMapping("/annuity/real/time")
	public AnnuityRealTimeData callAnnuityRealtimeApi(@RequestParam String  policyNumber) throws JsonMappingException, JsonProcessingException {
		logger.info("MemberAdditionController -- callAnnuityRealtimeApi --started");
		AnnuityRealTimeData commonDto = integrationService.callAnnuityRealtimeApi(policyNumber);
		logger.info("MemberAdditionController -- callAnnuityRealtimeApi --ended");
		return commonDto;
	}
	
	@GetMapping("/getDepositReceiptGenaration")
	public DepositReceiptGenerationDto getDepositReceiptGenaration(@RequestParam String receiptNumber,
			@RequestParam Boolean isProposalDeposit,@RequestParam String policyNumber)throws Exception {
		logger.info("IntegrationController -- getDepositReceiptGenaration --started");
		DepositReceiptGenerationDto depositReceiptGenerationDto = integrationService.getDepositReceiptGenaration(receiptNumber,isProposalDeposit,policyNumber);
		logger.info("IntegrationController -- getDepositReceiptGenaration --ended");
		return depositReceiptGenerationDto;
	}
	
	/*
	 *  depositCollect/bulk/upload 
	 */
	@PostMapping("/depositCollect/bulk/upload")
	public DepositCollectBulkUploadResponseDto depositCollectBulk(@RequestParam("file") MultipartFile file) {
		logger.info("IntegrationController -- depositCollectBulk --started");
		DepositCollectBulkUploadResponseDto commonDto = integrationService.depositCollectBulk(file);
		logger.info("IntegrationController -- depositCollectBulk --ended");
		return commonDto;
	}
	
	
}
