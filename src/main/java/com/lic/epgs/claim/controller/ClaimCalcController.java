package com.lic.epgs.claim.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.claim.dto.ClaimAnnuityCalcDto;
import com.lic.epgs.claim.dto.ClaimCommutationCalcDto;
import com.lic.epgs.claim.dto.ClaimFundValueDto;
import com.lic.epgs.claim.dto.ClaimMbrFundValueDto;
import com.lic.epgs.claim.dto.ClaimMbrRequestDto;
import com.lic.epgs.claim.dto.ClaimPayeeBankDetailsDto;
import com.lic.epgs.claim.dto.SurrenderChargeCalRequest;
import com.lic.epgs.claim.dto.SurrenderChargeCalResponse;
import com.lic.epgs.claim.temp.entity.TempClaimMbrNomineeEntity;
import com.lic.epgs.claim.temp.service.TempClaimCalcService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.FundedCommonMasterDto;
import com.lic.epgs.common.exception.ApplicationException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/claim")
public class ClaimCalcController {

	@Autowired
	TempClaimCalcService tempClaimCalcService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/fundvalue/save")
	public ApiResponseDto<ClaimFundValueDto> saveFundvalue(@RequestBody ClaimFundValueDto request)
			throws ApplicationException {
		logger.info("ClaimCalcController -- saveFundvalue --started");
		ApiResponseDto<ClaimFundValueDto> responseDto = tempClaimCalcService.saveFundvalue(request);
		logger.info("ClaimCalcController -- saveFundvalue --end");
		return responseDto;
	}

	@PostMapping("/mbr/fundvalue/save")
	public ApiResponseDto<ClaimMbrFundValueDto> getFundvalue(@RequestBody ClaimMbrFundValueDto request) {
		logger.info("ClaimCalcController -- getFundvalue --started");
		ApiResponseDto<ClaimMbrFundValueDto> responseDto = tempClaimCalcService.saveMbrFundvalue(request);
		logger.info("ClaimCalcController -- getFundvalue --end");
		return responseDto;
	}

	@PostMapping("/calc/save/commutation")
	public ApiResponseDto<ClaimCommutationCalcDto> saveCommutation(@RequestBody ClaimCommutationCalcDto request) {
		logger.info("ClaimCalcController -- saveCommutation --started");
		ApiResponseDto<ClaimCommutationCalcDto> responseDto = tempClaimCalcService.saveCommutation(request);
		logger.info("ClaimCalcController -- saveCommutation --end");
		return responseDto;
	}

	@PostMapping("/calc/save/anuity")
	public ApiResponseDto<ClaimAnnuityCalcDto> saveAnuity(@RequestBody ClaimAnnuityCalcDto request) {
		logger.info("ClaimCalcController -- saveAnuity --started");
		ApiResponseDto<ClaimAnnuityCalcDto> responseDto = tempClaimCalcService.saveAnuity(request);
		logger.info("ClaimCalcController -- saveAnuity --end");
		return responseDto;
	}

	@PostMapping("/payeeBank/save")
	public ApiResponseDto<ClaimPayeeBankDetailsDto> savePayeeBankDetails(
			@RequestBody ClaimPayeeBankDetailsDto claimPayeeBankDetailsDto) {
		logger.info("ClaimCalcController -- savePayeeBankDetails --started");
		ApiResponseDto<ClaimPayeeBankDetailsDto> responseDto = tempClaimCalcService
				.savePayeeBankDetails(claimPayeeBankDetailsDto);
		logger.info("ClaimCalcController -- savePayeeBankDetails --end");
		return responseDto;
	}

	@PostMapping("/fundvalue/calculation")
	public ApiResponseDto<ClaimFundValueDto> saveFundCalculationvalue(@RequestBody ClaimFundValueDto request) {
		logger.info("ClaimCalcController -- saveFundCalculationvalue --started");
		ApiResponseDto<ClaimFundValueDto> responseDto = tempClaimCalcService.saveFundCalculationvalue(request);
		logger.info("ClaimCalcController -- saveFundCalculationvalue --end");
		return responseDto;
	}

//	@GetMapping("/mbr/fundvalue/calculation")
//	public ApiResponseDto<ClaimFundValueDto> saveMbrFundCalculationvalue(@RequestBody NomineeTotalFundShared request) {
//		
//		return tempClaimCalcService.saveMbrFundCalculationvalue(request);
//		
//	}

	@PostMapping("/commutation/calculation")
	public ApiResponseDto<ClaimCommutationCalcDto> saveCommutationCalculation(
			@RequestBody ClaimCommutationCalcDto request) {
		logger.info("ClaimCalcController -- saveCommutationCalculation --started");
		ApiResponseDto<ClaimCommutationCalcDto> responseDto = tempClaimCalcService.saveCommutationCalculation(request);
		logger.info("ClaimCalcController -- saveCommutationCalculation --end");
		return responseDto;
	}

//
//	@GetMapping("/get/commutation/{claimNo}")
//	public ApiResponseDto<List<ClaimCommutationCalcDto>> findCommutation(@PathVariable String claimNo) {
//		return tempClaimCalcService.findCommutation(claimNo);
//	}
//
//	@GetMapping("/get/annuity/{claimNo}")
//	public ApiResponseDto<List<ClaimAnnuityCalcDto>> findAnnuity(@PathVariable String claimNo) {
//		return tempClaimCalcService.findAnnuity(claimNo);
//	}

	@PostMapping("/calAnnuityPurchase")
	public ApiResponseDto<ClaimCommutationCalcDto> calAnnuityPurchase(
			@RequestBody ClaimMbrRequestDto claimMbrRequestDto) {
		logger.info("ClaimCalcController -- calAnnuityPurchase --started");
		ApiResponseDto<ClaimCommutationCalcDto> responseDto = tempClaimCalcService
				.calAnnuityPurchase(claimMbrRequestDto);
		logger.info("ClaimCalcController -- calAnnuityPurchase --end");
		return responseDto;
	}

	@PostMapping("/calRefreshByNominee")
	public ApiResponseDto<TempClaimMbrNomineeEntity> calRefreshByNominee(
			@RequestBody ClaimMbrRequestDto claimMbrRequestDto) {
		logger.info("ClaimCalcController -- calRefreshByNominee --started");
		ApiResponseDto<TempClaimMbrNomineeEntity> responseDto = tempClaimCalcService
				.calRefreshByNominee(claimMbrRequestDto);
		logger.info("ClaimCalcController -- calRefreshByNominee --end");
		return responseDto;
	}

	@GetMapping("/getFundPayableDetails")
	public ApiResponseDto<List<FundedCommonMasterDto>> getFundPayableDetails(@RequestParam Long codeId,
			String modeOfExit) {
		logger.info("ClaimCalcController -- getFundPayableDetails --started");
		ApiResponseDto<List<FundedCommonMasterDto>> responseDto = tempClaimCalcService.getFundPayableDetails(codeId,
				modeOfExit);
		logger.info("ClaimCalcController -- getFundPayableDetails --end");
		return responseDto;
	}

	@GetMapping("/getClaimBankDetails")
	public ApiResponseDto<ClaimPayeeBankDetailsDto> getClaimBankDetails(@RequestParam(defaultValue = "0") Long mphId,
			@RequestParam(defaultValue = "0") Long memberId, @RequestParam(defaultValue = "0") Long nomineeId,
			@RequestParam String claimNo) {
		logger.info("ClaimCalcController -- getClaimBankDetails --started");
		ApiResponseDto<ClaimPayeeBankDetailsDto> response = tempClaimCalcService.getClaimBankDetails(mphId, memberId,
				nomineeId, claimNo);
		logger.info("ClaimCalcController -- getClaimBankDetails --end");
		return response;
	}

	@PostMapping("/reCalculateFund")
	public ApiResponseDto<ClaimFundValueDto> reCalculateFund(@RequestBody ClaimFundValueDto claimFundValue)
			throws ApplicationException {
		logger.info("ClaimCalcController -- reCalculateFund --started");
		ApiResponseDto<ClaimFundValueDto> responseDto = tempClaimCalcService.reCalculateFund(claimFundValue);
		logger.info("ClaimCalcController -- reCalculateFund --end");
		return responseDto;
	}

	@PostMapping("/marketValueAdjCalculation")
	public ApiResponseDto<SurrenderChargeCalResponse> marketValuePayoutCalculation(
			@RequestBody SurrenderChargeCalRequest surrenderChargeCalRequest) throws ApplicationException {
		logger.info("ClaimCalcController -- marketValuePayoutCalculation --started");
		ApiResponseDto<SurrenderChargeCalResponse> responseDto = tempClaimCalcService
				.marketValuePayoutCalculation(surrenderChargeCalRequest);
		logger.info("ClaimCalcController -- marketValuePayoutCalculation --end");
		return responseDto;
	}
	
	@PostMapping("/getGsaGnPolicyConvertToWithdrawral")
	public ApiResponseDto<String> getGsaGnPolicyConvertToWithdrawral(@RequestParam Long claimId,@RequestParam String type) {
		logger.info("ClaimController -- getGsaGnPolicyConvertToWithdrawral --started");
		ApiResponseDto<String> commonResponseDto = tempClaimCalcService.getGsaGnPolicyConvertToWithdrawral(claimId,type);
		logger.info("ClaimController -- getGsaGnPolicyConvertToWithdrawral --ended");
		return commonResponseDto;
	}

}
