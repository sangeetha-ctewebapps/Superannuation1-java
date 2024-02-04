package com.lic.epgs.payout.controller;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.payout.dto.AnnuityLedgerDto;
import com.lic.epgs.payout.dto.PayoutCheckerActionRequestDto;
import com.lic.epgs.payout.dto.PayoutDto;
import com.lic.epgs.payout.dto.PayoutMakerActionRequestDto;
import com.lic.epgs.payout.dto.PayoutOnboardingRequestDto;
import com.lic.epgs.payout.dto.PayoutOnboardingResponseDto;
import com.lic.epgs.payout.dto.PayoutStoredProcedureResponseDto;
import com.lic.epgs.payout.service.PayoutService;
import com.lic.epgs.payout.temp.service.TempPayoutService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payout")
public class PayoutController {

	@Autowired
	TempPayoutService tempPayoutService;

	@Autowired
	PayoutService payoutService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/onboard")
	public ApiResponseDto<PayoutOnboardingResponseDto> onboard(@RequestBody PayoutOnboardingRequestDto request) {
		logger.info("PayoutController -- onboard --started");
		ApiResponseDto<PayoutOnboardingResponseDto> responseDto = tempPayoutService.onboard(request);
		logger.info("PayoutController -- onboard --ended");
		return responseDto;
	}

	@PostMapping("/maker/action")
	public ApiResponseDto<String> makerAction(@RequestBody PayoutMakerActionRequestDto request) {
		logger.info("PayoutController -- makerAction --started");
		ApiResponseDto<String> responseDto = payoutService.updateMakerAction(request);
		logger.info("PayoutController -- makerAction --ended");
		return responseDto;
	}

	@PostMapping("/checker/action1")
	public ApiResponseDto<String> checkerAction(@RequestBody PayoutCheckerActionRequestDto request) {
		logger.info("PayoutController -- checkerAction --started");
		ApiResponseDto<String> responseDto = payoutService.updateCheckerAction(request);
		logger.info("PayoutController -- checkerAction --ended");
		return responseDto;
	}

	@GetMapping("/checker/approve/{payoutNo}")
	public ApiResponseDto<String> checkerAction(@PathVariable("payoutNo") String payoutNo) {
		PayoutCheckerActionRequestDto request = new PayoutCheckerActionRequestDto();
		request.setPayoutNo(payoutNo);
		request.setAction(7);
		logger.info("PayoutController -- checkerAction --started");
		ApiResponseDto<String> responseDto = payoutService.updateCheckerAction(request);
		logger.info("PayoutController -- checkerAction --ended");
		return responseDto;
	}

	@GetMapping("/fetch")
	public ApiResponseDto<PayoutDto> findByPayoutNo(@RequestParam(defaultValue = "") String initiMationNo,
			@RequestParam(defaultValue = "") String payoutNo) {
		logger.info("PayoutController -- findByPayoutNo --started");
		ApiResponseDto<PayoutDto> responseDto = payoutService.findPayoutDetails(initiMationNo, payoutNo);
		logger.info("PayoutController -- findByPayoutNo --ended");
		return responseDto;
	}

	@PostMapping("/checker/action")
	public ApiResponseDto<String> approvedAndRejectAction(@RequestBody PayoutCheckerActionRequestDto request)
			throws ApplicationException, JsonMappingException, JsonProcessingException {
		logger.info("PayoutController -- approvedAndRejectAction --started");
		ApiResponseDto<String> response = payoutService.approvedAndRejectAction(request);
		logger.info("PayoutController -- approvedAndRejectAction --ended");
		return response;
	}

	@GetMapping("/storeProcedureforAccount")
	public ApiResponseDto<List<PayoutStoredProcedureResponseDto>> storeProcedureforAccount(@RequestParam Long payoutId)
			throws ApplicationException {
		logger.info("PayoutController -- getPaymentStatus --started");
		ApiResponseDto<List<PayoutStoredProcedureResponseDto>> responseDto = payoutService
				.storeProcedureforAccount(payoutId);
		logger.info("PayoutController -- getPaymentStatus --ended");
		return responseDto;
	}

	@GetMapping("/payment/status")
	public ApiResponseDto<Object> getPaymentStatus(@RequestParam("payoutId") Long payoutId,
			@RequestParam("unitCode") String unitCode) {
		logger.info("PayoutController -- getPaymentStatus --started");
		ApiResponseDto<Object> response = payoutService.getPaymentStatus(payoutId, unitCode);
		logger.info("PayoutController -- getPaymentStatus --ended");
		return response;
	}

	@GetMapping("/annuityLedger")
	public AnnuityLedgerDto getAnnuityLedgerDetails(@RequestParam String payoutNo) {
		logger.info("PayoutController -- getAnnuityLedgerDetails --started");
		AnnuityLedgerDto annuityLedgerDto = new AnnuityLedgerDto();
		try {
			annuityLedgerDto = payoutService.getAnnuityLedgerDetails(payoutNo);
		} catch (Exception e) {
			logger.error("PayoutController -- getAnnuityLedgerDetails --Error" + e.getMessage());
		}
		return annuityLedgerDto;
	}
	
	/** Note:- In this api for Manully call fundDebit after payout Approved **/
	@GetMapping("/debitWithAnnuityApi")
		public ApiResponseDto<String> fundDebitRecall(@RequestBody PayoutCheckerActionRequestDto request)
				throws ApplicationException, JsonMappingException, JsonProcessingException {
			logger.info("PayoutController -- approvedAndRejectAction --started");
			ApiResponseDto<String> response= payoutService.fundDebitRecall(request);
			logger.info("PayoutController -- approvedAndRejectAction --ended");
			return response;
		}
	
	@PostMapping("/sendToIntimationMaker")
	public ApiResponseDto<String> sentToIntimationMaker(@RequestBody PayoutCheckerActionRequestDto request)
			throws ApplicationException, JsonMappingException, JsonProcessingException {
		logger.info("PayoutController -- approvedAndRejectAction --started");
		ApiResponseDto<String> response= payoutService.sentToIntimationMaker(request);
		logger.info("PayoutController -- approvedAndRejectAction --ended");
		return response;
	}	
}
