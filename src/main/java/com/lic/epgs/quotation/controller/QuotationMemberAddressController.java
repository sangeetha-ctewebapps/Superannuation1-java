//package com.lic.epgs.quotation.controller;
//
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.lic.epgs.quotation.dto.QuotationMemberAddressApiResponseDto;
//import com.lic.epgs.quotation.dto.QuotationMemberAddressDto;
//import com.lic.epgs.quotation.service.QuotationMemberAddressService;
//
//@RestController
//@CrossOrigin("*")
//@RequestMapping("/api/quotation/member/address/")
//public class QuotationMemberAddressController {
//
//	protected final Logger logger = LogManager.getLogger(getClass());
//
//	@Autowired
//	private QuotationMemberAddressService quotationMemberAddressService;
//
//	@PostMapping("save")
//	public QuotationMemberAddressApiResponseDto saveQuotationMemberAddress(
//			@RequestBody QuotationMemberAddressDto quotationMemberAddressDto) {
//		logger.info("QuotationMemberAddressController -- save --started");
//		QuotationMemberAddressApiResponseDto quotationMemberAddressApiResponseDto = quotationMemberAddressService
//				.saveQuotationMemberAddress(quotationMemberAddressDto);
//		logger.info("QuotationMemberAddressController -- save --ended");
//		return quotationMemberAddressApiResponseDto;
//	}
//
//	@GetMapping("get")
//	public QuotationMemberAddressApiResponseDto getQuotationMemberAddress(@RequestParam Long quotationMemberAddressId,
//			@RequestParam Long quotationMemberId) {
//		logger.info("QuotationMemberAddressController -- get --started");
//		QuotationMemberAddressApiResponseDto quotationMemberAddressApiResponseDto = quotationMemberAddressService
//				.getQuotationMemberAddress(quotationMemberAddressId, quotationMemberId);
//		logger.info("QuotationMemberAddressController -- get --ended");
//		return quotationMemberAddressApiResponseDto;
//	}
//
//	@GetMapping("getAll/{quotationMemberId}")
//	public QuotationMemberAddressApiResponseDto getQuotationMemberAddresses(@PathVariable Long quotationMemberId) {
//		logger.info("QuotationMemberAddressController -- listAddressByMember --started");
//		QuotationMemberAddressApiResponseDto quotationMemberAddressApiResponseDto = quotationMemberAddressService
//				.getQuotationMemberAddresses(quotationMemberId);
//		logger.info("QuotationMemberAddressController -- listAddressByMember --ended");
//		return quotationMemberAddressApiResponseDto;
//	}
//
//	@PutMapping("update/{quotationMemberAddressId}")
//	public QuotationMemberAddressApiResponseDto editQuotationMemberAddress(@PathVariable Long quotationMemberAddressId,
//			@RequestBody QuotationMemberAddressDto quotationMemberAddressDto) {
//		logger.info("QuotationMemberAddressController -- update --started");
//		QuotationMemberAddressApiResponseDto quotationMemberAddressApiResponseDto = quotationMemberAddressService
//				.updateQuotationMemberAddress(quotationMemberAddressId, quotationMemberAddressDto);
//		logger.info("QuotationMemberAddressController -- update --ended");
//		return quotationMemberAddressApiResponseDto;
//	}
//
//	@DeleteMapping("delete/{addressId}")
//	public QuotationMemberAddressApiResponseDto deleteQuotationMemberAddress(@PathVariable Long addressId) {
//		logger.info("QuotationMemberAddressController -- delete --started");
//		QuotationMemberAddressApiResponseDto quotationMemberAddressApiResponseDto = quotationMemberAddressService
//				.deleteQuotationMemberAddress(addressId);
//		logger.info("QuotationMemberAddressController -- delete --ended");
//		return quotationMemberAddressApiResponseDto;
//	}
//
//}
