package com.lic.epgs.claim.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lic.epgs.claim.dto.ClaimAnnuityCalcDto;
import com.lic.epgs.claim.dto.ClaimCheckerActionRequestDto;
import com.lic.epgs.claim.dto.ClaimDto;
import com.lic.epgs.claim.dto.ClaimMakerActionRequestDto;
import com.lic.epgs.claim.dto.ClaimMbrBulkResponseDto;
import com.lic.epgs.claim.dto.ClaimOnboardingDto;
import com.lic.epgs.claim.dto.ClaimOnboardingRequestDto;
import com.lic.epgs.claim.dto.ClaimOnboardingResponseDto;
import com.lic.epgs.claim.dto.ClaimUpdateRequestDto;
import com.lic.epgs.claim.service.ClaimService;
import com.lic.epgs.claim.temp.service.TempClaimService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.CommonResponseDto;
import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.claim.dto.CommonAnnuityDto;
import com.lic.epgs.claim.dto.SaAccountConfigMasterResponseDto;
import com.lic.epgs.claim.dto.CommonGlCodesResponseDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/claim")
public class ClaimController {

	@Autowired
	TempClaimService tempClaimService;

	@Autowired
	private IntegrationService integrationService;

	@Autowired
	ClaimService claimService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/onboard")
	public ApiResponseDto<ClaimOnboardingResponseDto> onboard(@RequestBody ClaimOnboardingRequestDto request) {
		logger.info("ClaimMbrController -- onboard --started");
		ApiResponseDto<ClaimOnboardingResponseDto> responseDto = tempClaimService.onboard(request);
		logger.info("ClaimMbrController -- onboard --ended");
		return responseDto;
	}

	@PostMapping("/update")
	public ApiResponseDto<ClaimDto> onboard(@RequestBody ClaimUpdateRequestDto request) {
		logger.info("ClaimMbrController -- onboard --started");
		ApiResponseDto<ClaimDto> responseDto = tempClaimService.update(request);
		logger.info("ClaimMbrController -- onboard --ended");
		return responseDto;
	}

	@PostMapping("/maker/action")
	public ApiResponseDto<String> makerAction(@RequestBody ClaimMakerActionRequestDto request) {
		logger.info("ClaimMbrController -- makerAction --started");
		ApiResponseDto<String> responseDto = claimService.updateMakerAction(request);
		logger.info("ClaimMbrController -- makerAction --ended");
		return responseDto;
	}

	@PostMapping("/checker/action")
	public ApiResponseDto<String> checkerAction(@RequestBody ClaimCheckerActionRequestDto request) {
		logger.info("ClaimMbrController -- checkerAction --started");
		ApiResponseDto<String> responseDto = claimService.updateCheckerAction(request);
		logger.info("ClaimMbrController -- checkerAction --ended");
		return responseDto;
	}

	@GetMapping("/{claimNo}")
	public ApiResponseDto<ClaimDto> findByClaimNo(@PathVariable String claimNo) {
		logger.info("ClaimMbrController -- findByClaimNo --started");
		ApiResponseDto<ClaimDto> responseDto = claimService.findClaimDetails(claimNo);
		logger.info("ClaimMbrController -- findByClaimNo --ended");
		return responseDto;
	}

	@PostMapping("/bulk")
	public ClaimMbrBulkResponseDto saveBulkMember(@RequestParam Long policyId, @RequestParam String createdBy,
			@RequestParam String unitCode, @RequestParam("file") MultipartFile file) throws Exception {
		logger.info("ClaimMbrController -- saveBulkMember --started");
		ClaimMbrBulkResponseDto commonDto = integrationService.saveBulkMemberForClaim(policyId, createdBy, unitCode,
				file);
		logger.info("ClaimMbrController -- saveBulkMember --ended");
		return commonDto;
	}

	@GetMapping("/getAllBatches")
	public CommonResponseDto getAllBatches() {
		logger.info("ClaimMbrController -- getAllBathes --started");
		CommonResponseDto commonDto = integrationService.getAllBatches();
		logger.info("ClaimMbrController -- getAllBatches --ended");
		return commonDto;
	}

	@GetMapping("/getAllBathMembers")
	public CommonResponseDto getAllBathMembers() {
		logger.info("ClaimMbrController -- getAllBathMembers --started");
		CommonResponseDto commonDto = integrationService.getAllBathMembers();
		logger.info("ClaimMbrController -- getAllBathMembers --ended");
		return commonDto;
	}

	@GetMapping("/removeClaimMembers")
	public CommonResponseDto removeClaimMembers(@RequestParam Long batchId) {
		logger.info("ClaimMbrController -- removeClaimMembers --started");
		CommonResponseDto commonDto = integrationService.removeClaimMembers(batchId);
		logger.info("ClaimMbrController -- removeClaimMembers --ended");
		return commonDto;
	}

	@GetMapping("/getBatchAssociateData")
	public CommonResponseDto getBatchAssociatedData(@RequestParam Long batchId) {
		logger.info("ClaimMbrController -- getBatchAssociateData --started");
		CommonResponseDto commonDto = integrationService.getBatchAssociateData(batchId);
		logger.info("ClaimMbrController -- getBatchAssociateData --ended");
		return commonDto;
	}

//	@GetMapping("/exportFundCalculation")
	@GetMapping("/fundSummary/download")
	public ResponseEntity<Resource> fundSummaryDownload(@RequestParam String memberId,
			@RequestParam String financialYear, @RequestParam String variant, @RequestParam Integer frequency,
			HttpServletResponse response) throws Exception {
		logger.info("ClaimMbrController -- fundSummaryDownload --started");
		InputStreamResource file = new InputStreamResource(
				claimService.fundSummaryDownload(memberId, financialYear, variant, frequency));
		logger.info("ClaimMbrController -- fundSummaryDownload --ended");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Fund_Statement_" + memberId + ".csv")
				.contentType(MediaType.parseMediaType("application/csv")).body(file);
	}

	@GetMapping("/fundSummary/get")
	public CommonResponseDto fundSummaryGet(@RequestParam String memberId, @RequestParam String financialYear,
			@RequestParam String variant, @RequestParam Integer frequency) {
		logger.info("ClaimMbrController -- fundSummaryGet --started");
		CommonResponseDto commonDto = claimService.fundSummaryGet(memberId, financialYear, variant, frequency);
		logger.info("ClaimMbrController -- fundSummaryGet --ended");
		return commonDto;
	}

	@GetMapping("/getAllDataByPolicyNo")
	public CommonResponseDto getAllDataByPolicyNo(@RequestParam String masterPolicyNo) {
		logger.info("ClaimMbrController -- getAllDataByPolicyNo --started");
		CommonResponseDto commonDto = integrationService.getAllDataByPolicyNo(masterPolicyNo);
		logger.info("ClaimMbrController -- getAllDataByPolicyNo --ended");
		return commonDto;
	}

//	@GetMapping("removeBatch")
//	public CommonResponseDto removeBatchForAom(@RequestParam Long batchId,@RequestParam Integer serviceId) {
//		logger.info("AdditionOfMemberController -- removeBatchForAom --started");
//		CommonResponseDto commonDto = integrationService.removeBatchForAom(batchId, serviceId);
//		logger.info("AdditionOfMemberController -- removeBatchForAom --ended");
//		return commonDto;
//	
//	}

	@GetMapping("/getClaimOnboardDetails")
	public ApiResponseDto<ClaimOnboardingDto> getClaimOnboardDetails(@RequestParam String claimOnBoardNo) {
		return tempClaimService.getClaimOnboardDetails(claimOnBoardNo);
	}

	@PostMapping("/gstAnnutityCalculate")
	public ApiResponseDto<ClaimAnnuityCalcDto> gstAnnutityCalculate(
			@RequestBody ClaimAnnuityCalcDto claimAnnuityCalcDto) {
		return tempClaimService.gstAnnutityCalculate(claimAnnuityCalcDto);
	}

	/* praveen */
	@GetMapping("/getAnnuityDataBypolicyNo")
	public CommonAnnuityDto getAnnuityDataBypolicyNo(@RequestParam String policyNumber) {
		logger.info("ClaimController -- getAnnuityDataBypolicyNo --started");
		CommonAnnuityDto commonAnnuityDto = claimService.getAnnuityDataBypolicyNo(policyNumber);
		logger.info("ClaimController -- getAnnuityDataBypolicyNo --ended");
		return commonAnnuityDto;
	}

	@PostMapping("/rejectClaimOnBoarding")
	public CommonResponseDto rejectClaimOnBoarding(@RequestParam String claimNo) {
		logger.info("ClaimController -- rejectClaimOnBoarding --started");
		CommonResponseDto commonResponseDto = tempClaimService.rejectClaimOnBoarding(claimNo);
		logger.info("ClaimController -- rejectClaimOnBoarding --ended");
		return commonResponseDto;
	}

	@PostMapping("/saAccountConfigStoredProcdure")
	public SaAccountConfigMasterResponseDto saAccountConfigMasterStoredProcedure(@RequestParam Long claimId) {
		logger.info("ClaimController -- saAccountConfigMasterStoredProcedure --started");
		SaAccountConfigMasterResponseDto commonResponseDto = claimService.saAccountConfigMasterStoredProcedure(claimId);
		logger.info("ClaimController -- saAccountConfigMasterStoredProcedure --ended");
		return commonResponseDto;
	}

}
