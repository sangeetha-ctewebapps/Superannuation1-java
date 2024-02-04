package com.lic.epgs.payout.controller;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.payout.dto.PayoutAnnuityCalcDto;
import com.lic.epgs.payout.dto.PayoutCommutationCalcDto;
import com.lic.epgs.payout.dto.PayoutFundValueDto;
import com.lic.epgs.payout.dto.PayoutMbrFundValueDto;
import com.lic.epgs.payout.temp.service.TempPayoutCalcService;
import com.lic.epgs.common.dto.ApiResponseDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payout")
public class PayoutCalcController {

	@Autowired
	TempPayoutCalcService tempPayoutCalcService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/fundvalue/save")
	public ApiResponseDto<PayoutFundValueDto> saveFundvalue(@RequestBody PayoutFundValueDto request) {
		logger.info("PayoutCalcController -- saveFundvalue --started");
		ApiResponseDto<PayoutFundValueDto> responseDto = tempPayoutCalcService.saveFundvalue(request);
		logger.info("PayoutCalcController -- saveFundvalue --ended");
		return responseDto;
	}

	@PostMapping("/mbr/fundvalue/save")
	public ApiResponseDto<PayoutMbrFundValueDto> getFundvalue(@RequestBody PayoutMbrFundValueDto request) {
		logger.info("PayoutCalcController -- getFundvalue --started");
		ApiResponseDto<PayoutMbrFundValueDto> responseDto = tempPayoutCalcService.saveMbrFundvalue(request);
		logger.info("PayoutCalcController -- getFundvalue --ended");
		return responseDto;
	}

	@PostMapping("/calc/save/commutation")
	public ApiResponseDto<PayoutCommutationCalcDto> saveCommutation(@RequestBody PayoutCommutationCalcDto request) {
		logger.info("PayoutCalcController -- saveCommutation --started");
		ApiResponseDto<PayoutCommutationCalcDto> responseDto = tempPayoutCalcService.saveCommutation(request);
		logger.info("PayoutCalcController -- saveCommutation --ended");
		return responseDto;
	}

	@PostMapping("/calc/save/anuity")
	public ApiResponseDto<PayoutAnnuityCalcDto> saveAnuity(@RequestBody PayoutAnnuityCalcDto request) {
		logger.info("PayoutCalcController -- saveAnuity --started");
		ApiResponseDto<PayoutAnnuityCalcDto> responseDto = tempPayoutCalcService.saveAnuity(request);
		logger.info("PayoutCalcController -- saveAnuity --ended");
		return responseDto;
	}
//
//	@GetMapping("/get/commutation/{payoutNo}")
//	public ApiResponseDto<List<PayoutCommutationCalcDto>> findCommutation(@PathVariable String payoutNo) {
//		return tempPayoutCalcService.findCommutation(payoutNo);
//	}
//
//	@GetMapping("/get/annuity/{payoutNo}")
//	public ApiResponseDto<List<PayoutAnnuityCalcDto>> findAnnuity(@PathVariable String payoutNo) {
//		return tempPayoutCalcService.findAnnuity(payoutNo);
//	}
}
