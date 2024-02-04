package com.lic.epgs.claim.controller;

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

import com.lic.epgs.claim.dto.ClaimDocumentDetailDto;
import com.lic.epgs.claim.dto.ClaimDocumentStatusRequestDto;
import com.lic.epgs.claim.temp.service.TempClaimDocsService;
import com.lic.epgs.common.dto.ApiResponseDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/claim/doc")
public class ClaimDocsController {

	@Autowired
	TempClaimDocsService tempClaimDocsService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/req/upload")
	public ApiResponseDto<ClaimDocumentDetailDto> uploadDocument(@RequestBody ClaimDocumentDetailDto docsDto) {
		logger.info("ClaimDocsController -- uploadDocument --started");
		ApiResponseDto<ClaimDocumentDetailDto> responseDto = tempClaimDocsService.uploadClaimReqDoc(docsDto);
		logger.info("ClaimDocsController -- uploadDocument --ended");
		return responseDto;
	}

	@GetMapping("/req/view/{claimNo}")
	public ApiResponseDto<List<ClaimDocumentDetailDto>> getClaimDoc(@PathVariable String claimNo) {
		logger.info("ClaimDocsController -- getClaimDoc --started");
		ApiResponseDto<List<ClaimDocumentDetailDto>> responseDto = tempClaimDocsService.viewClaimReqDoc(claimNo);
		logger.info("ClaimDocsController -- getClaimDoc --ended");
		return responseDto;
	}

	@PostMapping("/update/status")
	public ApiResponseDto<String> updateStatus(@RequestBody ClaimDocumentStatusRequestDto requestDto) {
		logger.info("ClaimDocsController -- updateStatus --started");
		ApiResponseDto<String> responseDto = tempClaimDocsService.updateStatus(requestDto);
		logger.info("ClaimDocsController -- updateStatus --ended");
		return responseDto;
	}

	@GetMapping("/delete/{documentId}")
	public ApiResponseDto<String> getClaimDoc(@PathVariable Long documentId) {
		logger.info("ClaimDocsController -- getClaimDoc --started");
		ApiResponseDto<String> responseDto = tempClaimDocsService.delete(documentId);
		logger.info("ClaimDocsController -- getClaimDoc --ended");
		return responseDto;
	}

}
