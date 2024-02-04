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

import com.lic.epgs.claim.dto.ClaimSearchRequestDto;
import com.lic.epgs.claim.dto.ClaimSearchResponseDto;
import com.lic.epgs.claim.service.ClaimSearchService;
import com.lic.epgs.claim.temp.service.TempClaimSearchService;
import com.lic.epgs.common.dto.ApiResponseDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/claim/search")
public class ClaimSearchController {

	@Autowired
	TempClaimSearchService tempClaimSearchService;

	@Autowired
	ClaimSearchService claimSearchService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/inprogress")
	public ApiResponseDto<List<ClaimSearchResponseDto>> inprogress(@RequestBody ClaimSearchRequestDto request) {
		logger.info("ClaimSearchController -- inprogress --started");
		ApiResponseDto<List<ClaimSearchResponseDto>> responseDto = tempClaimSearchService.inprogress(request);
		logger.info("ClaimSearchController -- inprogress --ended");
		return responseDto;
	}

	@GetMapping("/inprogress/{claimNo}")
	public ApiResponseDto<List<ClaimSearchResponseDto>> inprogressByClaimNo(@PathVariable String claimNo) {
		logger.info("ClaimSearchController -- inprogressByClaimNo --started");
		ApiResponseDto<List<ClaimSearchResponseDto>> responseDto = tempClaimSearchService.inprogressByClaimNo(claimNo);
		logger.info("ClaimSearchController -- inprogressByClaimNo --ended");
		return responseDto;
	}

	@PostMapping("/existing")
	public ApiResponseDto<List<ClaimSearchResponseDto>> existing(@RequestBody ClaimSearchRequestDto request) {
		logger.info("ClaimSearchController -- existing --started");
		ApiResponseDto<List<ClaimSearchResponseDto>> responseDto = claimSearchService.existing(request);
		logger.info("ClaimSearchController -- existing --ended");
		return responseDto;
	}

	@GetMapping("/existing/{claimNo}")
	public ApiResponseDto<List<ClaimSearchResponseDto>> existingByClaimNo(@PathVariable String claimNo) {
		logger.info("ClaimSearchController -- existingByClaimNo --started");
		ApiResponseDto<List<ClaimSearchResponseDto>> responseDto = claimSearchService.existingByClaimNo(claimNo);
		logger.info("ClaimSearchController -- existingByClaimNo --ended");
		return responseDto;
	}

	@PostMapping("/payout")
	public ApiResponseDto<List<ClaimSearchResponseDto>> payoutByClaimNo(@RequestBody ClaimSearchRequestDto request) {
		logger.info("ClaimSearchController -- payoutByClaimNo --started");
		ApiResponseDto<List<ClaimSearchResponseDto>> responseDto = claimSearchService.payoutByClaimNo(request);
		logger.info("ClaimSearchController -- payoutByClaimNo --ended");
		return responseDto;
	}

}
