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
//import com.lic.epgs.quotation.dto.QuotationMemberAppointeeApiResponseDto;
//import com.lic.epgs.quotation.service.QuotationMemberAppointeeService;
//
//@RestController
//@CrossOrigin("*")
//@RequestMapping("/api/quotation/member/appointee/")
//public class QuotationMemberAppointeeController {
//
//	protected final Logger logger = LogManager.getLogger(getClass());
//
//	@Autowired
//	private QuotationMemberAppointeeService quotationMemberAppointeeService;
//
//	@GetMapping("get")
//	public QuotationMemberAppointeeApiResponseDto getQuotationMemberAppointee(@RequestParam Long appointeeId,
//			@RequestParam Long quotationMemberId) {
//		logger.info("QuotationMemberAppointeeController -- get --started");
//		QuotationMemberAppointeeApiResponseDto quotationMemberAppointeeApiResponseDto = quotationMemberAppointeeService
//				.getQuotationMemberAppointee(appointeeId, quotationMemberId);
//		logger.info("QuotationMemberAppointeeController -- get --ended");
//		return quotationMemberAppointeeApiResponseDto;
//	}
//
//	@GetMapping("getAll/{quotationMemberId}")
//	public QuotationMemberAppointeeApiResponseDto getQuotationMemberAppointees(@PathVariable Long quotationMemberId) {
//		logger.info("QuotationMemberAppointeeController -- getQuotationMemberAppointees --started");
//		QuotationMemberAppointeeApiResponseDto quotationMemberAppointeeApiResponseDto = quotationMemberAppointeeService
//				.getQuotationMemberAppointees(quotationMemberId);
//		logger.info("QuotationMemberAppointeeController -- getQuotationMemberAppointees --ended");
//		return quotationMemberAppointeeApiResponseDto;
//	}
//
//	@DeleteMapping("delete/{appointeeId}")
//	public QuotationMemberAppointeeApiResponseDto deleteQuotationMemberAppointee(@PathVariable Long appointeeId) {
//		logger.info("QuotationMemberAppointeeController -- delete --started");
//		QuotationMemberAppointeeApiResponseDto quotationMemberAppointeeApiResponseDto = quotationMemberAppointeeService
//				.deleteQuotationMemberAppointee(appointeeId);
//		logger.info("QuotationMemberAppointeeController -- delete --ended");
//		return quotationMemberAppointeeApiResponseDto;
//	}
//
//}
