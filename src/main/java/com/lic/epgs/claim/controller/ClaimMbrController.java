package com.lic.epgs.claim.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.claim.dto.ClaimAppointeeDto;
import com.lic.epgs.claim.dto.ClaimMbrAppointeeDto;
import com.lic.epgs.claim.dto.ClaimMbrNomineeDto;
import com.lic.epgs.claim.temp.service.TempClaimMbrAppointeeService;
import com.lic.epgs.claim.temp.service.TempClaimMbrNomineeService;
import com.lic.epgs.common.dto.ApiResponseDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/claim/")
public class ClaimMbrController {

	@Autowired
	TempClaimMbrNomineeService tempClaimMbrNomineeService;

	@Autowired
	TempClaimMbrAppointeeService tempClaimMbrAppointeeService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/nominee/save")
	public ApiResponseDto<ClaimMbrNomineeDto> save(@RequestBody ClaimMbrNomineeDto request) {
		logger.info("ClaimMbrController -- save --started");
		ApiResponseDto<ClaimMbrNomineeDto> responseDto = tempClaimMbrNomineeService.save(request);
		logger.info("ClaimMbrController -- save --ended");
		return responseDto;
	}

	@GetMapping("/nominee/{claimNo}")
	public ApiResponseDto<List<ClaimMbrNomineeDto>> getNominee(@PathVariable String claimNo) {
		logger.info("ClaimMbrController -- getNominee --started");
		ApiResponseDto<List<ClaimMbrNomineeDto>> responseDto = tempClaimMbrNomineeService.findNomineeByClaimNo(claimNo);
		logger.info("ClaimMbrController -- getNominee --ended");
		return responseDto;
	}

	@PostMapping("/appointee/save")
	public ApiResponseDto<ClaimMbrAppointeeDto> save(@RequestBody ClaimMbrAppointeeDto request) {
		logger.info("ClaimMbrController -- save --started");
		ApiResponseDto<ClaimMbrAppointeeDto> responseDto = tempClaimMbrAppointeeService.save(request);
		logger.info("ClaimMbrController -- save --ended");
		return responseDto;
	}

	@GetMapping("/appointee/{claimNo}")
	public ApiResponseDto<List<ClaimMbrAppointeeDto>> getAppointee(@PathVariable String claimNo) {
		logger.info("ClaimMbrController -- getAppointee --started");
		ApiResponseDto<List<ClaimMbrAppointeeDto>> responseDto = tempClaimMbrAppointeeService
				.findAppointeeByClaimNo(claimNo);
		logger.info("ClaimMbrController -- getAppointee --ended");
		return responseDto;
	}

	@GetMapping("/getAppointeeNameList")
	public ApiResponseDto<List<ClaimAppointeeDto>> getAppointeeNameList(@RequestParam String claimNo) {
		logger.info("ClaimMbrController -- getAppointeeNameList --started");
		ApiResponseDto<List<ClaimAppointeeDto>> responseDto = tempClaimMbrNomineeService.getAppointeeNameList(claimNo);
		logger.info("ClaimMbrController -- getAppointeeNameList --ended");
		return responseDto;
	}

}
