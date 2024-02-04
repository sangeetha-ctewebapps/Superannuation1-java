package com.lic.epgs.regularadjustmentcontribution.controller;

import javax.servlet.http.HttpServletResponse;

/**
 * @author pradeepramesh
 *
 */

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
import org.springframework.web.multipart.MultipartFile;

import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionResponseDto;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.policy.dto.PolicyFrequencyDetailsDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policy.service.impl.PolicyCalcServiceImpl;
import com.lic.epgs.policy.service.impl.PolicyServiceImpl;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularACSaveAdjustmentRequestDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionBulkResponseDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionNotesDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionResponseDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentSearchDto;
import com.lic.epgs.regularadjustmentcontribution.service.RegularadjustmentService;
import com.lic.epgs.regularadjustmentcontribution.service.impl.RegularadjustmentServiceImpl;
import com.lic.epgs.utils.CommonConstants;

import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionBatchHistoryResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("api/regularAdjustmentContribution")

public class RegularadjustmentController {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private IntegrationService integrationService;

	@Autowired
	RegularadjustmentService regularadjustmentService;
	@Autowired
	RegularadjustmentServiceImpl regularadjustmentServiceImpl;
	
	@Autowired
	PolicyCalcServiceImpl policyCalcServiceImpl;
	@Autowired
	PolicyServiceImpl policyServiceImpl;

	@PostMapping("/newcitrieaSearch")
	public PolicyResponseDto newcitrieaSearch(@RequestBody PolicySearchDto policySearchDto) {
		logger.info("RegularadjustmentController :: newcitrieaSearch :: newcitrieaSearchPradeep :: Start");
		PolicyResponseDto responseDto = regularadjustmentService.newcitrieaSearchPradeep(policySearchDto);
		logger.info("RegularadjustmentController :: newcitrieaSearch :: newcitrieaSearchPradeep :: Ends");
		return responseDto;
//		return regularadjustmentService.newcitrieaSearch(policySearchDto);
	}

	@GetMapping("/newcitrieaSearch/{mphId}/{policyId}")
	public AdjustmentContributionResponseDto newcitrieaSearchById(@PathVariable("mphId") Long mphId,
			@PathVariable("policyId") Long policyId) {
		logger.info("RegularadjustmentController :: newcitrieaSearchById :: Start");
		AdjustmentContributionResponseDto responseDto = regularadjustmentService.newcitrieaSearchById(mphId, policyId);
		logger.info("RegularadjustmentController :: newcitrieaSearchById :: Ends");
		return responseDto;
	}

	@PostMapping("/saveadjustment")
	public RegularAdjustmentContributionResponseDto saveAdjustment(
			@RequestBody RegularACSaveAdjustmentRequestDto adjustmentDto) {
		logger.info("RegularadjustmentController :: saveAdjustment :: Start");
		RegularAdjustmentContributionResponseDto responseDto = regularadjustmentService.saveAdjustment(adjustmentDto);
		logger.info("RegularadjustmentController :: saveAdjustment :: Ends");
		return responseDto;
	}

	@PostMapping("/save")
	public RegularAdjustmentContributionResponseDto save(@RequestBody RegularAdjustmentContributionDto request) {
		logger.info("RegularadjustmentController :: save :: Start");
		RegularAdjustmentContributionResponseDto responseDto = regularadjustmentService.save(request);
		logger.info("RegularadjustmentController :: save :: Ends");
		return responseDto;
	}

	@PostMapping("/notesave")
	public RegularAdjustmentContributionResponseDto notesave(
			@RequestBody RegularAdjustmentContributionNotesDto request) {
		logger.info("RegularadjustmentController :: notesave :: Start");
		RegularAdjustmentContributionResponseDto responseDto = regularadjustmentService.notesave(request);
		logger.info("RegularadjustmentController :: notesave :: Ends");
		return responseDto;
	}

	@GetMapping("/getNotesList")
	public RegularAdjustmentContributionResponseDto getNotesList(@RequestParam Long regularContributionId) {
		logger.info("RegularadjustmentController :: getNotesList :: Start");
		RegularAdjustmentContributionResponseDto responseDto = regularadjustmentService
				.getNotesList(regularContributionId);
		logger.info("RegularadjustmentController :: getNotesList :: Ends");
		return responseDto;
	}

	@PostMapping("/sendToChecker")
	public RegularAdjustmentContributionResponseDto sendToCheker(
			@RequestBody RegularAdjustmentContributionDto request) {
		request.setRegularContributionStatus(PolicyConstants.CHECKER_NO);
		logger.info("RegularadjustmentController :: sendToCheker :: Start");
		RegularAdjustmentContributionResponseDto responseDto = regularadjustmentService.changeStatus(
				request.getRegularContributionId(), request.getRegularContributionStatus(), request.getModifiedBy());
		logger.info("RegularadjustmentController :: sendToCheker :: Ends");
		return responseDto;
	}

	@PostMapping("/sendToMaker")
	public RegularAdjustmentContributionResponseDto sendToMaker(@RequestBody RegularAdjustmentContributionDto request) {
		request.setRegularContributionStatus(PolicyConstants.MAKER_NO);
		logger.info("RegularadjustmentController :: sendToMaker :: Start");
		RegularAdjustmentContributionResponseDto responseDto = regularadjustmentService.changeStatus(
				request.getRegularContributionId(), request.getRegularContributionStatus(), request.getModifiedBy());
		logger.info("RegularadjustmentController :: sendToMaker :: Ends");
		return responseDto;
	}

	@PostMapping("/approve")
	public RegularAdjustmentContributionResponseDto approve(@RequestBody RegularAdjustmentContributionDto request) throws ApplicationException {
		request.setRegularContributionStatus(PolicyConstants.APPROVED_NO);
		logger.info("RegularadjustmentController :: approve :: Start");
		RegularAdjustmentContributionResponseDto approve = regularadjustmentService.approve(request.getRegularContributionId(),request.getRegularContributionStatus(), request.getModifiedBy(), request.getVariantType());
		
		/***
		 * @notes Contribution to Transaction Entries
		 *
		 */
		
		if(approve.getTransactionStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
		logger.info("saveContribution::Start");
		policyServiceImpl.saveContributionToTxnEntries(approve.getPolicyId());
		logger.info("saveContribution::End");
		
		logger.info("saveZeroAccEntriesToTxnEntires::Start");
		policyCalcServiceImpl.setZeroAccEntriesToTxnEntires(approve.getPolicyId(),request.getVariantType());
		logger.info("saveZeroAccEntriesToTxnEntires::Ends");
		logger.info("RegularadjustmentController :: approve :: Ends");
		}
		return approve;
	}

	@PostMapping("/reject")
	public RegularAdjustmentContributionResponseDto reject(@RequestBody RegularAdjustmentContributionDto request) {
		request.setRegularContributionStatus(PolicyConstants.REJECTED_NO);
		logger.info("RegularadjustmentController :: reject :: Start");
		RegularAdjustmentContributionResponseDto responseDto = regularadjustmentService.reject(
				request.getRegularContributionId(), request.getRegularContributionStatus(), request.getModifiedBy());
		logger.info("RegularadjustmentController :: reject :: Ends");
		return responseDto;
	}

	@PostMapping(value = "/getInprogressLoad")
	public RegularAdjustmentContributionResponseDto getInprogressLoad(@RequestBody RegularAdjustmentSearchDto request) {
		logger.info("RegularadjustmentController :: getInprogressLoad :: Start");
		RegularAdjustmentContributionResponseDto responseDto = regularadjustmentService.getInprogressLoad(request);
		logger.info("RegularadjustmentController :: getInprogressLoad :: Ends");
		return responseDto;
	}

	@GetMapping(value = "/getInprogressDetails")
	public RegularAdjustmentContributionResponseDto getInprogressDetails(@RequestParam Long regularContributionId) {
		logger.info("RegularadjustmentController :: getInprogressDetails :: Start");
		RegularAdjustmentContributionResponseDto responseDto = regularadjustmentService
				.getInprogressDetails(regularContributionId);
		logger.info("RegularadjustmentController :: getInprogressDetails :: Ends");
		return responseDto;
	}

	@PostMapping(value = "/getExisitngLoad")
	public RegularAdjustmentContributionResponseDto getExisitngLoad(@RequestBody RegularAdjustmentSearchDto request) {
		logger.info("RegularadjustmentController :: getExisitngLoad :: Start");
		RegularAdjustmentContributionResponseDto responseDto = regularadjustmentService.getExisitngLoad(request);
		logger.info("RegularadjustmentController :: getExisitngLoad :: Ends");
		return responseDto;
	}

	@GetMapping(value = "/getExistingDetails")
	public RegularAdjustmentContributionResponseDto getExistingDetails(@RequestParam Long regularContributionId) {
		logger.info("RegularadjustmentController :: getExistingDetails :: Start");
		RegularAdjustmentContributionResponseDto responseDto = regularadjustmentService
				.getExistingDetails(regularContributionId);
		logger.info("RegularadjustmentController :: getExistingDetails :: Ends");
		return responseDto;
	}

	@PostMapping("/generateFrequency")
	public RegularAdjustmentContributionResponseDto getFrequencyDates(@RequestBody PolicyFrequencyDetailsDto request)
			throws Exception {
		logger.info("RegularadjustmentController :: getFrequencyDates :: Start");
		RegularAdjustmentContributionResponseDto responseDto = regularadjustmentService.getFrequencyDates(request);
		logger.info("RegularadjustmentController :: getFrequencyDates :: Ends");
		return responseDto;
	}

	@GetMapping("/getFrequencyByPolicyId/{id}")
	public AdjustmentContributionResponseDto getFrequencyByPolicyId(@PathVariable("id") Long policyId) {
		logger.info("RegularadjustmentController :: getFrequencyByPolicyId :: Start");
		AdjustmentContributionResponseDto responseDto = regularadjustmentService.getFrequencyByPolicyId(policyId);
		logger.info("RegularadjustmentController :: getFrequencyByPolicyId :: Ends");
		return responseDto;
	}

	@PostMapping("/bulk/upload")
	public RegularAdjustmentContributionBulkResponseDto bulkUploadForRegularAdjustmentContribution(
			@RequestParam Long masterPolicyId, @RequestParam Long tempPolicyId, @RequestParam String createdBy,
			@RequestParam String regularContributionId, @RequestParam String unitCode,
			@RequestParam("file") MultipartFile file) {
		logger.info("RegularadjustmentController -- bulkUploadForRegularAdjustmentContribution --started");
		RegularAdjustmentContributionBulkResponseDto commonDto = integrationService
				.bulkUploadForRegularAdjustmentContribution(masterPolicyId, tempPolicyId, createdBy,
						regularContributionId, unitCode, file);
		logger.info("RegularadjustmentController -- bulkUploadForRegularAdjustmentContribution --ended");
		return commonDto;
	}

	@GetMapping("/bulk/remove")
	public RegularAdjustmentContributionBulkResponseDto removeBatchForRegularAdjustmentContribution(@RequestParam Long batchId,
			@RequestParam(required=false) Long regularContributionId,@RequestParam(required=false) Long policyId,@RequestParam String unitCode,
			@RequestParam String modifiedBy) {
		logger.info("RegularadjustmentController -- removeBatchForRegularAdjustmentContribution --started");
		RegularAdjustmentContributionBulkResponseDto commonDto = integrationService
				.removeBatchForRegularAdjustmentContribution(batchId,regularContributionId,policyId,unitCode,modifiedBy);
		logger.info("RegularadjustmentController -- removeBatchForRegularAdjustmentContribution --ended");
		return commonDto;
	}

	@GetMapping("bulk/download")
	public void download(@RequestParam Long batchId, @RequestParam String fileType, HttpServletResponse response) {
		logger.info("RegularadjustmentController -- download --started");
		regularadjustmentService.download(batchId, fileType, response);
		logger.info("RegularadjustmentController -- download --ended");
	}

	
	
	
	@GetMapping("/getBatchHistory")
	public RegularAdjustmentContributionBatchHistoryResponse getAllBathesByPolicyId(@RequestParam(required=false) Long regularContributionId) {
		logger.info("RegularadjustmentController -- getBatchHistory --started");
		RegularAdjustmentContributionBatchHistoryResponse response = regularadjustmentService.getBatchHistory(regularContributionId);
		logger.info("RegularadjustmentController -- getBatchHistory --ended");
		return response;
	}
	
	@PostMapping("/makerReject")
	public RegularAdjustmentContributionResponseDto makerReject(@RequestBody RegularAdjustmentContributionDto request) {
		request.setRegularContributionStatus(PolicyConstants.REJECTED_NO);
		logger.info("RegularadjustmentController :: makerReject :: Start");
		RegularAdjustmentContributionResponseDto responseDto = regularadjustmentService.makerReject(
				request.getRegularContributionId(),request.getModifiedBy(),request.getRejectionRemarks());
		logger.info("RegularadjustmentController :: makerReject :: Ends");
		return responseDto;
	}
	
}
