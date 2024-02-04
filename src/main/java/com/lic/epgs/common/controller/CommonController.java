/**
 * 
 */
package com.lic.epgs.common.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionResponseDto;
import com.lic.epgs.common.dto.CommonDocumentMasterDto;
import com.lic.epgs.common.dto.CommonResponseDto;
import com.lic.epgs.common.dto.DepositTransferResponse;
import com.lic.epgs.common.dto.PolicyResponseDataAnnuityDto;
import com.lic.epgs.common.dto.SearchDepositTransferDto;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.common.dto.PolicyResponseDataAnnuityDto;

/**
 * @author Karthick M
 *
 */

@CrossOrigin("*")
@RestController
@RequestMapping({ "/api/common/" })
public class CommonController {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private CommonService commonService;

	@GetMapping(value = { "commonStatus" })
	public ResponseEntity<Object> commonStatus() {
		return ResponseEntity.ok().body(commonService.commonStatus());
	}

	@GetMapping(value = { "claimRequiredDocument" })
	public ResponseEntity<Object> claimrequireddocument() {
		return ResponseEntity.ok().body(commonService.claimrequireddocument());
	}

	@GetMapping(value = { "claimDocumentStatus" })
	public ResponseEntity<Object> claimDocumentStatus() {
		return ResponseEntity.ok().body(commonService.claimdocumentstatus());
	}

	@GetMapping(value = { "claimIntimaitonType" })
	public ResponseEntity<Object> claimIntimaitonType() {
		return ResponseEntity.ok().body(commonService.claimIntimaitonType());
	}

	@GetMapping(value = { "claimReasonforexit" })
	public ResponseEntity<Object> claimReasonforexit() {
		return ResponseEntity.ok().body(commonService.claimReasonforexit());
	}

	@GetMapping(value = { "claimMemberUploadFileType" })
	public ResponseEntity<Object> claimMemberUploadFileType() {
		return ResponseEntity.ok().body(commonService.claimMemberUploadFileType());
	}

	@GetMapping(value = { "claimMakerActions" })
	public ResponseEntity<Object> claimMakerActions() {
		return ResponseEntity.ok().body(commonService.claimMakerActions());
	}

	@GetMapping(value = { "claimApproverActions" })
	public ResponseEntity<Object> claimApproverActions() {
		return ResponseEntity.ok().body(commonService.claimApproverActions());
	}

	@GetMapping(value = { "claimCommutationCalc" })
	public ResponseEntity<Object> claimCommutationCalc() {
		return ResponseEntity.ok().body(commonService.claimCommutationCalc());
	}

	@GetMapping(value = { "claimAmountPayable" })
	public ResponseEntity<Object> claimAmountPayable() {
		return ResponseEntity.ok().body(commonService.claimAmountPayable());
	}

	@GetMapping(value = { "claimAnuityCreationOptions" })
	public ResponseEntity<Object> claimAnuityCreationOptions() {
		return ResponseEntity.ok().body(commonService.claimAnuityCreationOptions());
	}

	@GetMapping(value = { "nomineeRelationShip" })
	public ResponseEntity<Object> nomineeRelationShip() {
		return ResponseEntity.ok().body(commonService.nomineeRelationShip());
	}

	@GetMapping(value = { "policyStatusApi" })
	public ResponseEntity<Object> policyStatusApi() {
		return ResponseEntity.ok().body(commonService.policyStatusApi());
	}

	@GetMapping(value = { "maritalStatus" })
	public ResponseEntity<Object> maritalStatus() {
		return ResponseEntity.ok().body(commonService.maritalStatus());
	}

	@GetMapping(value = { "accountType" })
	public ResponseEntity<Object> accountType() {
		return ResponseEntity.ok().body(commonService.accountType());
	}

	@GetMapping(value = { "sharedAmountType" })
	public ResponseEntity<Object> sharedAmountType() {
		return ResponseEntity.ok().body(commonService.sharedAmountType());
	}

	@GetMapping("getSampleFile")
	public void getSampleFile(@RequestParam String fileType, HttpServletResponse response) {
		logger.info("CommonController -- getSampleFile --started");
		commonService.getSampleFile(fileType, response);
		logger.info("CommonController -- getSampleFile --ended");
	}

	@PostMapping("saveSampleFile")
	public CommonResponseDto saveSampleFile(@RequestParam String fileType, @RequestParam String createdBy,
			@RequestParam("file") MultipartFile file) throws Exception {
		logger.info("CommonController -- saveSampleFile --started");
		CommonResponseDto commonDto = commonService.saveSampleFile(fileType, createdBy, file);
		logger.info("CommonController -- saveSampleFile --ended");
		return commonDto;
	}

	@GetMapping("getFinancialYeartDetails")
	public CommonResponseDto getFinancialYeartDetails(@RequestParam String date) throws Exception {
		logger.info("CommonController -- saveSampleFile --started");
		CommonResponseDto commonDto = commonService.getFinancialYeartDetails(date);
		logger.info("CommonController -- saveSampleFile --ended");
		return commonDto;
	}

	@GetMapping(value = { "searchMember/{policyId}/{licId}/{memberId}/{financialYear}" })
	public ResponseEntity<Object> viewMemberByPolicyAndLicId(@PathVariable Long policyId, @PathVariable String licId,
			@PathVariable Long memberId, @PathVariable String financialYear) throws Exception {
		return ResponseEntity.ok().body(commonService.searchMember(policyId, licId, policyId, financialYear));

	}

	@PostMapping("getCommonDocument")
	public List<CommonDocumentMasterDto> getDocumentDetails(
			@RequestBody CommonDocumentMasterDto commonDocumentMasterDto) throws Exception {
		logger.info("CommonController -- saveSampleFile --started");
		List<CommonDocumentMasterDto> commonDto = commonService.getDocumentDetails(commonDocumentMasterDto);
		logger.info("CommonController -- saveSampleFile --ended");
		return commonDto;
	}

	@GetMapping("policyresponseData")
	public PolicyResponseDataAnnuityDto policyresponseData(@RequestParam String policyNumber) {
		logger.info("CommonController -- policyresponseData --started");
		PolicyResponseDataAnnuityDto commonDto = commonService.policyresponseData(policyNumber);
		logger.info("CommonController -- policyresponseData --ended");
		return commonDto;
	}
	
//	@PostMapping("/existing/newcitrieaSearch")
//	public PolicyResponseDto newcitrieaSearch(@RequestBody PolicySearchDto policySearchDto) {
//		logger.info("PolicyRestController:getExistingPolicyByCitriea:started");
//		PolicyResponseDto commonDto = policyDetailsChangeService.newcitrieaSearch(policySearchDto);
//		logger.info("PolicyRestController:getExistingPolicyByCitriea:ended");
//		return commonDto;
//	}
	
	
	@PostMapping("searchDepositTransfer")
	public List<DepositTransferResponse> searchDepositTransfer(@RequestBody SearchDepositTransferDto searchDepositTransferDto) {
		List<DepositTransferResponse> response = commonService.searchDepositTransfer(searchDepositTransferDto);
		return response;
	}
	
	@PostMapping("DepositTransfersearch")
	public List<DepositTransferResponse> DepositTransferSearch(@RequestBody SearchDepositTransferDto searchDepositTransferDto) {
		List<DepositTransferResponse> response = commonService.DepositTransferSearch(searchDepositTransferDto);
		return response;
	}
	
	@GetMapping("depositHistory")
	public PolicyResponseDataAnnuityDto depositAppropriation(@RequestParam String  collectionNo){
		PolicyResponseDataAnnuityDto response = commonService.depositAppropriation(collectionNo);
		return response;	
	}
}
