//package com.lic.epgs.quotation.controller;
//
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.lic.epgs.quotation.dto.QuotationMemberNomineeApiResponseDto;
//import com.lic.epgs.quotation.service.QuotationMemberNomineeService;
//
//@RestController
//@CrossOrigin("*")
//@RequestMapping("/api/quotation/member/nominee")
//public class QuotationMemberNomineeController {
//
//	@Autowired
//	private QuotationMemberNomineeService service;
//
//	protected final Logger logger = LogManager.getLogger(getClass());
//
//	@GetMapping("get")
//	public QuotationMemberNomineeApiResponseDto getQuotationMemberNominee(@RequestParam Long memberId, @RequestParam Long nomineeId) {
//		logger.info("QuotationMemberNomineeController -- get --started");
//		QuotationMemberNomineeApiResponseDto commonDto = service.getQuotationMemberNominee(memberId, nomineeId);
//		logger.info("QuotationMemberNomineeController -- get --ended");
//		return commonDto;
//	}
//	
//	@GetMapping("getAll")
//	public QuotationMemberNomineeApiResponseDto getQuotationMemberNominees(@RequestParam Long memberId) {
//		logger.info("QuotationMemberNomineeController -- getAll --started");
//		QuotationMemberNomineeApiResponseDto commonDto = service.getQuotationMemberNominees(memberId);
//		logger.info("QuotationMemberNomineeController -- getAll --ended");
//		return commonDto;
//	}
//	
//	@DeleteMapping("delete/{nomineeId}")
//	public QuotationMemberNomineeApiResponseDto deleteQuotationMemberNominee(@PathVariable Long nomineeId) {
//		logger.info("QuotationMemberNomineeController -- delete --started");
//		QuotationMemberNomineeApiResponseDto commonDto = service.deleteQuotationMemberNominee(nomineeId);
//		logger.info("QuotationMemberNomineeController -- delete --ended");
//		return commonDto;
//	}
//	
//}
