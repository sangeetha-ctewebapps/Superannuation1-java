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
//import com.lic.epgs.quotation.dto.QuotationMemberBankDetailApiResponseDto;
//import com.lic.epgs.quotation.service.QuotationMemberBankDetailService;
//
//@RestController
//@CrossOrigin("*")
//@RequestMapping("/api/quotation/member/bankDetail/")
//public class QuotationMemberBankDetailController {
//
//	protected final Logger logger = LogManager.getLogger(getClass());
//
//	@Autowired
//	private QuotationMemberBankDetailService quotationMemberBankDetailService;
//
//	@GetMapping("get")
//	public QuotationMemberBankDetailApiResponseDto getQuotationMemberBankDetail(@RequestParam Long bankId,
//			@RequestParam Long quotationMemberId) {
//		logger.info("QuotationMemberBankDetailController -- get --started");
//		QuotationMemberBankDetailApiResponseDto quotationMemberBankDetailApiResponseDto = quotationMemberBankDetailService
//				.getQuotationMemberBankDetail(bankId, quotationMemberId);
//		logger.info("QuotationMemberBankDetailController -- get --ended");
//		return quotationMemberBankDetailApiResponseDto;
//	}
//
//	@GetMapping("getAll/{quotationMemberId}")
//	public QuotationMemberBankDetailApiResponseDto getQuotationMemberBankDetails(@PathVariable Long quotationMemberId) {
//		logger.info("QuotationMemberBankDetailController -- getQuotationMemberBankDetails --started");
//		QuotationMemberBankDetailApiResponseDto quotationMemberBankDetailApiResponseDto = quotationMemberBankDetailService
//				.getQuotationMemberBankDetails(quotationMemberId);
//		logger.info("QuotationMemberBankDetailController -- getQuotationMemberBankDetails --ended");
//		return quotationMemberBankDetailApiResponseDto;
//	}
//
//	@DeleteMapping("delete/{bankId}")
//	public QuotationMemberBankDetailApiResponseDto deleteQuotationMemberBankDetail(@PathVariable Long bankId) {
//		logger.info("QuotationMemberBankDetailController -- delete --started");
//		QuotationMemberBankDetailApiResponseDto quotationMemberBankDetailApiResponseDto = quotationMemberBankDetailService
//				.deleteQuotationMemberBankDetail(bankId);
//		logger.info("QuotationMemberBankDetailController -- delete --ended");
//		return quotationMemberBankDetailApiResponseDto;
//	}
//
//}
