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

import com.lic.epgs.payout.dto.PayoutMbrAppointeeDto;
import com.lic.epgs.payout.dto.PayoutMbrNomineeDto;
import com.lic.epgs.payout.temp.service.TempPayoutMbrAppointeeService;
import com.lic.epgs.payout.temp.service.TempPayoutMbrNomineeService;
import com.lic.epgs.common.dto.ApiResponseDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payout/")
public class PayoutMbrController {

	@Autowired
	TempPayoutMbrNomineeService tempPayoutMbrNomineeService;

	@Autowired
	TempPayoutMbrAppointeeService tempPayoutMbrAppointeeService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/nominee/save")
	public ApiResponseDto<PayoutMbrNomineeDto> save(@RequestBody PayoutMbrNomineeDto request) {
		logger.info("PayoutMbrController -- save --started");
		ApiResponseDto<PayoutMbrNomineeDto> responseDto = tempPayoutMbrNomineeService.save(request);
		logger.info("PayoutMbrController -- save --ended");
		return responseDto;
	}

	@GetMapping("/nominee/{payoutNo}")
	public ApiResponseDto<List<PayoutMbrNomineeDto>> getNominee(@PathVariable String payoutNo) {
		logger.info("PayoutMbrController -- getNominee --started");
		ApiResponseDto<List<PayoutMbrNomineeDto>> responseDto = tempPayoutMbrNomineeService
				.findNomineeByPayoutNo(payoutNo);
		logger.info("PayoutMbrController -- getNominee --ended");
		return responseDto;
	}

	@PostMapping("/appointee/save")
	public ApiResponseDto<PayoutMbrAppointeeDto> save(@RequestBody PayoutMbrAppointeeDto request) {
		logger.info("PayoutMbrController -- save --started");
		ApiResponseDto<PayoutMbrAppointeeDto> responseDto = tempPayoutMbrAppointeeService.save(request);
		logger.info("PayoutMbrController -- save --ended");
		return responseDto;
	}

	@GetMapping("/appointee/{payoutNo}")
	public ApiResponseDto<List<PayoutMbrAppointeeDto>> getAppointee(@PathVariable String payoutNo) {
		logger.info("PayoutMbrController -- getAppointee --started");
		ApiResponseDto<List<PayoutMbrAppointeeDto>> responseDto = tempPayoutMbrAppointeeService
				.findAppointeeByPayoutNo(payoutNo);
		logger.info("PayoutMbrController -- getAppointee --ended");
		return responseDto;
	}

}
