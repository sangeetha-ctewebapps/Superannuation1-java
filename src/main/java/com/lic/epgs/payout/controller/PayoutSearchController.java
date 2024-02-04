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

import com.lic.epgs.payout.constants.PayoutStatus;
import com.lic.epgs.payout.dto.PayoutSearchRequestDto;
import com.lic.epgs.payout.dto.PayoutSearchResponseDto;
import com.lic.epgs.payout.service.PayoutSearchService;
import com.lic.epgs.payout.temp.service.TempPayoutSearchService;
import com.lic.epgs.common.dto.ApiResponseDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payout/search")
public class PayoutSearchController {

	@Autowired
	TempPayoutSearchService tempPayoutSearchService;

	@Autowired
	PayoutSearchService payoutSearchService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/inprogress")
	public ApiResponseDto<List<PayoutSearchResponseDto>> inprogress(@RequestBody PayoutSearchRequestDto request) {
		logger.info("PayoutSearchController -- inprogress --started");
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = tempPayoutSearchService.inprogress(request);
		logger.info("PayoutSearchController -- inprogress --ended");
		return responseDto;
	}

	@GetMapping("/inprogress/{payoutNo}")
	public ApiResponseDto<List<PayoutSearchResponseDto>> inprogressByPayoutNo(@PathVariable String payoutNo) {
		logger.info("PayoutSearchController -- inprogressByPayoutNo --started");
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = tempPayoutSearchService
				.inprogressByPayoutNo(payoutNo);
		logger.info("PayoutSearchController -- inprogressByPayoutNo --ended");
		return responseDto;
	}

	@PostMapping("/existing")
	public ApiResponseDto<List<PayoutSearchResponseDto>> existing(@RequestBody PayoutSearchRequestDto request) {
		logger.info("PayoutSearchController -- existing --started");
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = payoutSearchService.existing(request);
		logger.info("PayoutSearchController -- existing --ended");
		return responseDto;
	}

	@GetMapping("/existing/{payoutNo}")
	public ApiResponseDto<List<PayoutSearchResponseDto>> existingByPayoutNo(@PathVariable String payoutNo) {
		logger.info("PayoutSearchController -- existingByPayoutNo --started");
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = payoutSearchService.existingByPayoutNo(payoutNo);
		logger.info("PayoutSearchController -- existingByPayoutNo --ended");
		return responseDto;
	}

	@PostMapping("/payoutExisting/enquirySearch")
	public ApiResponseDto<List<PayoutSearchResponseDto>> enquirySearch(@RequestBody PayoutSearchRequestDto request) {
		logger.info("PayoutSearchController -- enquirySearch --started");
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = payoutSearchService
				.enquirySearch(PayoutStatus.APPROVE.val(), request);
		logger.info("PayoutSearchController -- enquirySearch --ended");
		return responseDto;
	}

}
