package com.lic.epgs.quotation.controller;

import java.io.IOException;

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

import com.lic.epgs.common.dto.CommonChallanDto;
import com.lic.epgs.common.dto.CommonDocsDto;
import com.lic.epgs.quotation.dto.QuotationApiResponseDto;
import com.lic.epgs.quotation.dto.QuotationDto;
import com.lic.epgs.quotation.dto.QuotationSearchDto;
import com.lic.epgs.quotation.service.QuotationService;
import com.lic.epgs.utils.CommonUtils;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/Quotation/")
public class QuotationController {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private QuotationService quotationService;

	@PostMapping("/save")
	public QuotationApiResponseDto saveQuotation(@RequestBody QuotationDto depositTransferDto) {
		logger.info("QuotationController:saveQuotation:started");
		QuotationApiResponseDto commonDto = quotationService.saveQuotation(depositTransferDto);
		logger.info("QuotationController:saveQuotation:ended");
		return commonDto;
	}

	@GetMapping("/")
	public QuotationApiResponseDto getQuotation() {
		logger.info("QuotationController:getQuotation:started");
		QuotationApiResponseDto commonDto = quotationService.getQuotations();

		/***
		 * commonDto.setQuotationNo("");
		 */
		logger.info("QuotationController:getQuotation:ended");
		return commonDto;
	}

	@GetMapping("/existing")
	public QuotationApiResponseDto getExistingQuotationById(@RequestParam("payload") String payload)
			throws IOException {
		logger.info("QuotationController:getExistingQuotationById:started");
		QuotationApiResponseDto commonDto = quotationService.getQuotationById(CommonUtils.jsonToSearchLeadDto(payload));
		logger.info("QuotationController:getExistingQuotationById:ended");
		return commonDto;
	}

	@GetMapping("/inprogress")
	public QuotationApiResponseDto getInprogressQuotationById(@RequestParam("payload") String payload)
			throws IOException {
		logger.info("QuotationController:getQuotation:started");
		QuotationApiResponseDto commonDto = quotationService.getQuotationById(CommonUtils.jsonToSearchLeadDto(payload));
		logger.info("QuotationController:getQuotation:ended");
		return commonDto;
	}

	@PostMapping("/existing/citrieaSearch")
	public QuotationApiResponseDto getExistingQuotationByCitriea(@RequestBody QuotationSearchDto quotationSearchDto) {
		logger.info("QuotationController:citrieaSearch:started");
		QuotationApiResponseDto commonDto = quotationService.existingCitrieaSearch(quotationSearchDto);
		logger.info("QuotationController:citrieaSearch:ended");
		return commonDto;
	}

	@PostMapping("/inprogress/citrieaSearch")
	public QuotationApiResponseDto getInprogressQuotationByCitriea(@RequestBody QuotationSearchDto quotationSearchDto) {
		logger.info("QuotationController:saveQuotation:started");
		QuotationApiResponseDto commonDto = quotationService.inprogressCitrieaSearch(quotationSearchDto);
		logger.info("QuotationController:saveQuotation:ended");
		return commonDto;
	}

	@PutMapping("sendToCheker")
	public QuotationApiResponseDto sendToCheker(@RequestParam Integer quotationId,
			@RequestParam String quotationStatus) {
		logger.info("QuotationController:sendToCheker:started");
		QuotationApiResponseDto quotationApiResponseDto = quotationService.sendToCheker(quotationId, quotationStatus);
		logger.info("QuotationController:sendToCheker:ended");
		return quotationApiResponseDto;
	}

	@PutMapping("sendToMaker")
	public QuotationApiResponseDto sendToMaker(@RequestParam Integer quotationId,
			@RequestParam String quotationStatus) {
		logger.info("QuotationController:sendToMaker:started");
		QuotationApiResponseDto quotationApiResponseDto = quotationService.sendToMaker(quotationId, quotationStatus);
		logger.info("QuotationController:sendToMaker:ended");
		return quotationApiResponseDto;
	}

	@GetMapping("searchChecker")
	public QuotationApiResponseDto searchChecker(@RequestParam String quotationStatus) {
		logger.info("QuotationController:searchChecker:started");
		QuotationApiResponseDto quotationApiResponseDto = quotationService.searchChecker(quotationStatus);
		logger.info("QuotationController:searchChecker:ended");
		return quotationApiResponseDto;
	}

	@PutMapping("sendToApprover")
	public QuotationApiResponseDto sentToApprover(@RequestParam Integer quotationId,
			@RequestParam String quotationStatus) {
		logger.info("QuotationController:sendToApprover:started");
		QuotationApiResponseDto quotationApiResponseDto = quotationService.sendToApprover(quotationId, quotationStatus);
		logger.info("QuotationController:sendToApprover:ended");
		return quotationApiResponseDto;
	}

	@PutMapping("approveOrReject")
	public QuotationApiResponseDto approve(@RequestBody QuotationDto dto) {
		logger.info("QuotationController:sendToApprover:started");
		QuotationApiResponseDto quotationApiResponseDto = quotationService.approveOrReject(dto);
		logger.info("QuotationController:sendToApprover:ended");
		return quotationApiResponseDto;
	}

	@GetMapping("getQuotationByProposalNumber/{proposalNumber}")
	public QuotationApiResponseDto getQuotationByProposalNumber(@PathVariable("proposalNumber") String proposalNumber) {
		logger.info("QuotationController:getQuotationByProposalNumber:started");
		QuotationApiResponseDto commonDto = quotationService.getQuotationByProposalNumber(proposalNumber);
		logger.info("QuotationController:getQuotationByProposalNumber:ended");
		return commonDto;
	}

	@PostMapping("getQuotationOtherCriteria")
	public QuotationApiResponseDto getQuotationOtherCriteria(@RequestBody QuotationSearchDto quotationSearchDto) {
		logger.info("QuotationController:getQuotationOtherCriteria:started");
		QuotationApiResponseDto commonDto = quotationService.getQuotationOtherCriteria(quotationSearchDto);
		logger.info("QuotationController:getQuotationOtherCriteria:ended");
		return commonDto;
	}

	@PutMapping("sendToActuary")
	public QuotationApiResponseDto sendToActuary(@RequestParam("payload") String payload) throws IOException {
		logger.info("QuotationController:sendToActuary:started");
		QuotationApiResponseDto quotationApiResponseDto = quotationService
				.sendToActuary(CommonUtils.jsonToSearchLeadDto(payload));
		logger.info("QuotationController:sendToActuary:ended");
		return quotationApiResponseDto;
	}

	@PostMapping("/uploadDocument")
	public QuotationApiResponseDto uploadDocument(@RequestBody CommonDocsDto docsDto) {
		logger.info("QuotationController:uploadDocument:started");
		QuotationApiResponseDto commonDto = quotationService.uploadDocument(docsDto);
		logger.info("QuotationController:uploadDocument:ended");
		return commonDto;
	}

	@GetMapping("removeDocumentByDocId")
	public QuotationApiResponseDto removeDocumentByremoveDocumentId(@RequestParam String docId, String quotationId) {
		logger.info("QuotationController:removeDocumentByremoveDocumentId:started");
		QuotationApiResponseDto commonDto = quotationService.removeDocumentByremoveDocumentId(docId, quotationId);
		logger.info("QuotationController:removeDocumentByremoveDocumentId:ended");
		return commonDto;
	}

	@GetMapping("getDocumentDetails")
	public QuotationApiResponseDto getDocumentDetails(@RequestParam Integer quotationId) {
		logger.info("QuotationController:getDocumentDetails:started");
		QuotationApiResponseDto quotationApiResponseDto = quotationService.getDocumentDetails(quotationId);
		logger.info("QuotationController:getDocumentDetails:ended");
		return quotationApiResponseDto;
	}

	@PostMapping("generateChellanForQuotation")
	public QuotationApiResponseDto generateChellanForQuotation(@RequestBody CommonChallanDto dto) {
		logger.info("QuotationController:generateChellanForQuotation:started");
		QuotationApiResponseDto commonDto = quotationService.generateChellanForQuotation(dto);
		logger.info("QuotationController:generateChellanForQuotation:ended");
		return commonDto;
	}

	@PostMapping("saveChellanForQuotation")
	public QuotationApiResponseDto saveChellanForQuotation(@RequestBody CommonChallanDto dto) {
		logger.info("QuotationController:saveChellanForQuotation:started");
		QuotationApiResponseDto commonDto = quotationService.generateChellanForQuotation(dto);
		logger.info("QuotationController:saveChellanForQuotation:ended");
		return commonDto;
	}

	@PostMapping("getChellanNumber")
	public QuotationApiResponseDto getChellanNumber(@RequestBody CommonChallanDto dto) {
		logger.info("QuotationController:getChellanNumber:started");
		QuotationApiResponseDto commonDto = quotationService.getChellanNumber(dto);
		logger.info("QuotationController:getChellanNumber:ended");
		return commonDto;
	}

	@PostMapping("activateQuotation")
	public QuotationApiResponseDto activateQuotation(@RequestBody QuotationDto dto) {
		logger.info("QuotationController:activateQuotation:started");
		QuotationApiResponseDto commonDto = quotationService.activateQuotation(dto);
		logger.info("QuotationController:activateQuotation:ended");
		return commonDto;
	}
	
	@PostMapping("matrix")
	private QuotationApiResponseDto getMatrix(@RequestParam Long policyId ,@RequestParam String Servicetype) {
		logger.info("QuotationController:matrix:started");
		QuotationApiResponseDto matrix = quotationService.getMatrix(policyId,Servicetype);
		logger.info("QuotationController:matrix:ended");
		return matrix;
		
	}
	
	
	
	

}
