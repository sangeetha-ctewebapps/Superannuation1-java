package com.lic.epgs.payout.controller;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.payout.dto.PayoutDocumentDetailDto;
import com.lic.epgs.payout.dto.PayoutDocumentStatusRequestDto;
import com.lic.epgs.payout.temp.service.TempPayoutDocsService;
import com.lic.epgs.common.dto.ApiResponseDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payout/doc")
public class PayoutDocsController {

	@Autowired
	TempPayoutDocsService tempPayoutDocsService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/req/upload")
	public ApiResponseDto<PayoutDocumentDetailDto> uploadDocument(@RequestBody PayoutDocumentDetailDto docsDto) {
		logger.info("PayoutDocsController -- uploadDocument --started"); 
		ApiResponseDto<PayoutDocumentDetailDto> responseDto = tempPayoutDocsService.uploadPayoutReqDoc(docsDto);
		logger.info("PayoutDocsController -- uploadDocument --ended");
		return responseDto;
	}

	@GetMapping("/req/view/{payoutNo}")
	public ApiResponseDto<List<PayoutDocumentDetailDto>> getPayoutDoc(@PathVariable String payoutNo) {
		logger.info("PayoutDocsController -- getPayoutDoc --started");  
		ApiResponseDto<List<PayoutDocumentDetailDto>> responseDto = tempPayoutDocsService.viewPayoutReqDoc(payoutNo);
		logger.info("PayoutDocsController -- getPayoutDoc --ended");
		return responseDto;
	}

	@PostMapping("/update/status")
	public ApiResponseDto<String> updateStatus(@RequestBody PayoutDocumentStatusRequestDto requestDto) {
		logger.info("PayoutDocsController -- updateStatus --started"); 
		ApiResponseDto<String> responseDto = tempPayoutDocsService.updateStatus(requestDto);
		logger.info("PayoutDocsController -- updateStatus --ended"); 
		return responseDto;
	}

	@GetMapping("/delete/{documentId}")
	public ApiResponseDto<String> getPayoutDoc(@PathVariable Long documentId) {
		logger.info("PayoutDocsController -- getPayoutDoc --started");  
		ApiResponseDto<String> responseDto = tempPayoutDocsService.delete(documentId);
		logger.info("PayoutDocsController -- getPayoutDoc --ended"); 
		return responseDto;
	}

}
