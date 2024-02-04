package com.lic.epgs.quotation.controller;

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

import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.quotation.dto.QuotationFinancialResponseDto;
import com.lic.epgs.quotation.dto.QuotationMemberApiResponseDto;
import com.lic.epgs.quotation.dto.QuotationMemberBulkResponseDto;
import com.lic.epgs.quotation.dto.QuotationMemberDto;
import com.lic.epgs.quotation.service.QuotationMemberService;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/quotation/member/", "/api/Quotation/member/" })
public class QuotationMemberController {
	@Autowired
	private QuotationMemberService service;
	@Autowired
	private IntegrationService integrationService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("save")
	public QuotationMemberApiResponseDto saveQuotationMember(@RequestBody QuotationMemberDto quotationPersonalDataDto) {
		logger.info("QuotationMemberController -- save --started");
		QuotationMemberApiResponseDto commonDto = service.saveQuotationMember(quotationPersonalDataDto);
		logger.info("QuotationMemberController -- save --ended");
		return commonDto;
	}

	@GetMapping("get")
	public QuotationMemberApiResponseDto getQuotationMember(@RequestParam Long memberId,
			@RequestParam Integer quotationId) {
		logger.info("QuotationMemberController -- get --started");
		QuotationMemberApiResponseDto commonDto = service.getQuotationMember(memberId, quotationId);
		logger.info("QuotationMemberController -- get --ended");
		return commonDto;
	}

	@GetMapping("getAll")
	public QuotationMemberApiResponseDto getQuotationMembers(@RequestParam Integer quotationId,@RequestParam String pageName) {
		logger.info("QuotationMemberController -- getAll --started");
		QuotationMemberApiResponseDto commonDto = service.getQuotationMembers(quotationId,pageName);
		logger.info("QuotationMemberController -- getAll --ended");
		return commonDto;
	}

	@PostMapping("bulk")
	public QuotationMemberBulkResponseDto saveBulkMember(@RequestParam Integer quotationId,
			@RequestParam("file") MultipartFile file) {
		logger.info("QuotationMemberController -- saveBulkMember --started");
		QuotationMemberBulkResponseDto commonDto = integrationService.bulkMemberUpload(quotationId, file);
		logger.info("QuotationMemberController -- saveBulkMember --ended");
		return commonDto;
	}
	
	@GetMapping("remove")
	public QuotationMemberBulkResponseDto removeBatch(@RequestParam Long batchId,@RequestParam Integer quotationId) {
		logger.info("AdditionOfMemberController -- removeBatch --started");
		QuotationMemberBulkResponseDto commonDto = integrationService.removeBatchForQuotation(batchId, quotationId);
		logger.info("AdditionOfMemberController -- removeBatch --ended");
		return commonDto;
	
	}
	
	
	
	
	@GetMapping("bulk/download")
	public void download(@RequestParam Long batchId, @RequestParam String fileType, HttpServletResponse response){
		logger.info("QuotationMemberController -- download --started");
		service.download(batchId,fileType,response);
		logger.info("QuotationMemberController -- download --ended");
	}
	
	
	
	@GetMapping("getBatches")
	public QuotationMemberApiResponseDto getBatches(@RequestParam Long batchId){
		logger.info("QuotationMemberController -- getBatches --started");
		QuotationMemberApiResponseDto commonDto = service.getBatches(batchId);
		logger.info("QuotationMemberController -- getBatches --ended");
		return commonDto;
	}
	
	
	
	@GetMapping("getBatchIdList")
	public QuotationMemberApiResponseDto getBatchIdList(){
		logger.info("QuotationMemberController -- getBatchIdList --started");
		QuotationMemberApiResponseDto commonDto = service.getBatchIdList();
		logger.info("QuotationMemberController -- getBatchIdList --ended");
		return commonDto;
	}
	
	

	@GetMapping("fincalc")
	public QuotationFinancialResponseDto getFinancialCalculation(@RequestParam Integer quotationId) {
		logger.info("QuotationMemberController -- getFinancialCalculation --started");
		QuotationFinancialResponseDto commonDto = service.getFinancialCalculation(quotationId);
		logger.info("QuotationMemberController -- getFinancialCalculation --ended");
		return commonDto;
	}
	
	
	
//	ram prasad codes
	
	 @PostMapping(value = "remove/{addressId}/{memberId}")
		public QuotationMemberApiResponseDto deleteAddressesDetails(@PathVariable("addressId") Long addressId,@PathVariable("memberId") Long memberId) {
			logger.info("QuotationMemberController -- removeAddressesDetails --started");
			QuotationMemberApiResponseDto deleteAddressesDetails = service.deleteAddressesDetails(addressId, memberId);
			logger.info("QuotationMemberController -- removeAddressesDetails --ended");
			return deleteAddressesDetails;
		}

		@PostMapping(value = "removeNominee/{nomineeId}/{memberId}")
		public QuotationMemberApiResponseDto deleteNomineeDetails(@PathVariable("nomineeId") Long nomineeId,@PathVariable("memberId") Long memberId) {
			logger.info("QuotationMemberController -- removeNomineeDetails --started");
			QuotationMemberApiResponseDto deleteNomineeDetails = service.deleteNomineeDetails(nomineeId, memberId);
			logger.info("QuotationMemberController -- removeNomineeDetails --started");
			return deleteNomineeDetails;
		}
}
