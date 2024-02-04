package com.lic.epgs.policy.controller;

import java.io.IOException;

/**
 * @author pradeepramesh
 *
 */

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionResponseDto;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.policy.dto.CommonResponseStampDto;
import com.lic.epgs.policy.dto.ContributionRequestDto;
import com.lic.epgs.policy.dto.DepositRefundPolicySearchRequestDto;
import com.lic.epgs.policy.dto.GetPolicyMemberDetailsRequestDto;
import com.lic.epgs.policy.dto.IssuePolicyResponseDto;
import com.lic.epgs.policy.dto.PolicyDetailSearchRequestDto;
import com.lic.epgs.policy.dto.PolicyDetailSearchResponseDto;
import com.lic.epgs.policy.dto.PolicyDetailsResponseDto;
import com.lic.epgs.policy.dto.PolicyFrequencyDetailsDto;
import com.lic.epgs.policy.dto.PolicyNotesDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.dto.StampDutyConsumptionRequestDto;
import com.lic.epgs.policy.dto.TrnRegistrationResponseDto;
import com.lic.epgs.policy.old.dto.PolicyAddressOldDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentRequestDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentResponse;
import com.lic.epgs.policy.old.dto.PolicyBankOldDto;
import com.lic.epgs.policy.old.dto.PolicyDto;
import com.lic.epgs.policy.old.dto.PolicyFundStmtRequestDto;
import com.lic.epgs.policy.old.dto.PolicyFundStmtResponseDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policy.service.PolicyService;
import com.lic.epgs.policy.service.impl.PolicyCalcServiceImpl;
import com.lic.epgs.policy.service.impl.PolicyServiceImpl;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionResponseDto;
import com.lic.epgs.utils.CommonConstants;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy")
public class PolicyRestController {

	@Autowired
	PolicyService policyService;

	@Autowired
	PolicyServiceImpl policyServiceImpl;

	@Autowired
	PolicyCalcServiceImpl policyCalcServiceImpl;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/savePolicyDetails")
	public PolicyResponseDto savePolicyDetails(@RequestBody PolicyDto policyDto) throws ParseException {
		logger.info("PolicyRestController--savePolicyDetails-started");
		PolicyResponseDto commonDto = null;
		try {
			commonDto = policyService.savePolicyDetails(policyDto);
		} catch (IllegalArgumentException e) {
			logger.error("PolicyRestController--savePolicyDetails-error", e);
			commonDto = new PolicyResponseDto();
			commonDto.setTransactionMessage(PolicyConstants.FAIL);
			commonDto.setTransactionStatus(PolicyConstants.ERROR);
		}
		logger.info("PolicyRestController--savePolicyDetails-ended");
		commonDto = policyService.savePolicyOldDto(commonDto.getMphId());
		return commonDto;
	}

	@PostMapping("/saveNotesDetails")
	public PolicyResponseDto saveNotesDetails(@RequestBody PolicyNotesDto policyNotesDto) {
		logger.info("PolicyRestController--saveNotesDetails--started");
		PolicyResponseDto commonDto = null;
		try {
			commonDto = policyService.saveNotesDetails(policyNotesDto);
		} catch (IllegalArgumentException e) {
			logger.error("PolicyRestController--saveNotesDetails-error", e);
			commonDto = new PolicyResponseDto();
			commonDto.setTransactionMessage(PolicyConstants.FAIL);
			commonDto.setTransactionStatus(PolicyConstants.ERROR);
		}
		logger.info("PolicyRestController--saveNotesDetails-ended");
		return commonDto;
	}

	@GetMapping("/getNoteList/{id}")
	public PolicyResponseDto getNoteList(@PathVariable("id") Long policyId) {
		logger.info("PolicyRestController:getNoteList:started");
		PolicyResponseDto commonDto = policyService.getNoteList(policyId);
		logger.info("PolicyRestController:getNoteList:ended");
		return commonDto;
	}

	@PostMapping("/saveAddressDetails")
	public PolicyResponseDto saveAddressDetails(@RequestBody PolicyAddressOldDto addressDto) {
		logger.info("PolicyRestController--saveAddressDetails-started");
		PolicyResponseDto commonDto = null;
		try {
			commonDto = policyService.saveAddressDetails(addressDto);
		} catch (IllegalArgumentException e) {
			logger.error("PolicyRestController--saveAddressDetails--error", e);
			commonDto = new PolicyResponseDto();
			commonDto.setTransactionMessage(PolicyConstants.FAIL);
			commonDto.setTransactionStatus(PolicyConstants.ERROR);
		}
		logger.info("PolicyRestController--saveAddressDetails-ended");
		return commonDto;
	}

	@GetMapping("/getAddressList/{id}")
	public PolicyResponseDto getAddressList(@PathVariable("id") Long mphId) {
		logger.info("PolicyRestController:getAddressList:started");
		PolicyResponseDto commonDto = policyService.getAddressList(mphId);
		logger.info("PolicyRestController:getAddressList:ended");
		return commonDto;
	}

	@PostMapping("/saveBankDetails")
	public PolicyResponseDto saveBankDetails(@RequestBody PolicyBankOldDto bankDto) {
		logger.info("PolicyRestController--saveBankDetails--started");
		PolicyResponseDto commonDto = null;
		try {
			commonDto = policyService.saveBankDetails(bankDto);
		} catch (IllegalArgumentException e) {
			logger.error("PolicyRestController--saveBankDetails--error", e);
			commonDto = new PolicyResponseDto();
			commonDto.setTransactionMessage(PolicyConstants.FAIL);
			commonDto.setTransactionStatus(PolicyConstants.ERROR);
		}
		logger.info("PolicyRestController--saveBankDetails--ended");
		return commonDto;
	}

	@GetMapping("/getBankList/{id}")
	public PolicyResponseDto getBankList(@PathVariable("id") Long mphId) {
		logger.info("PolicyRestController:getBankList:started");
		PolicyResponseDto commonDto = policyService.getBankList(mphId);
		logger.info("PolicyRestController:getBankList:ended");
		return commonDto;
	}

	@GetMapping("/inprogress/{id}")
	public PolicyResponseDto getInprogressPolicyById(@PathVariable("id") Long mphId) {
		logger.info("PolicyRestController:getInprogressPolicyById:started");
		PolicyResponseDto commonDto = policyService.getPolicyById(CommonConstants.INPROGRESS, mphId);
		logger.info("PolicyRestController:getInprogressPolicyById:ended");
		commonDto = policyService.savePolicyOldDto(commonDto.getMphId());
		return commonDto;
	}

	@GetMapping("/existing/{id}")
	public PolicyResponseDto getExistingById(@PathVariable("id") Long policyId) {
		logger.info("PolicyRestController:getExistingById:started");
		PolicyResponseDto commonDto = policyService.getPolicyById(CommonConstants.EXISTING, policyId);
		logger.info("PolicyRestController:getExistingById:ended");
		return commonDto;
	}

	@PostMapping("/existing/citrieaSearch")
	public PolicyResponseDto getExistingPolicyByCitriea(@RequestBody PolicySearchDto policySearchDto) {
		logger.info("PolicyRestController:getExistingPolicyByCitriea:started");
		PolicyResponseDto commonDto = policyService.existingCitrieaSearch(policySearchDto);
		logger.info("PolicyRestController:getExistingPolicyByCitriea:ended");
		return commonDto;
	}

	@PostMapping("/inprogress/citrieaSearch")
	public PolicyResponseDto getInprogressPolicyByCitriea(@RequestBody PolicySearchDto policySearchDto) {
		logger.info("PolicyRestController:getInprogressPolicyByCitriea:started");
		PolicyResponseDto commonDto = policyService.inprogressCitrieaSearch(policySearchDto);
		logger.info("PolicyRestController:getInprogressPolicyByCitriea:ended");
		return commonDto;
	}

	@PutMapping("sendToChecker")
	public PolicyResponseDto sendToCheker(@RequestParam Long policyId) {
		logger.info("PolicyRestController:sendToCheker:started");
		PolicyResponseDto policyResponseDto = policyService.changeStatus(policyId, PolicyConstants.CHECKER_NO);
		logger.info("PolicyRestController:sendToCheker:ended");
		return policyResponseDto;
	}

	@PutMapping("sendToMaker")
	public PolicyResponseDto sendToMaker(@RequestParam Long policyId) {
		logger.info("PolicyRestController:sendToMaker:started");
		PolicyResponseDto policyResponseDto = policyService.changeStatus(policyId, PolicyConstants.MAKER_NO);
		logger.info("PolicyRestController:sendToMaker:ended");
		return policyResponseDto;
	}

	@PutMapping("sendToReject")
	public PolicyResponseDto sendToReject(@RequestParam Long policyId) {
		logger.info("PolicyRestController:sendToReject:started");
		PolicyResponseDto policyResponseDto = policyService.policyReject(policyId, PolicyConstants.REJECT);
		logger.info("PolicyRestController:sendToReject:ended");
		return policyResponseDto;
	}

	@PostMapping("/adjustment")
	public PolicyAdjustmentResponse contributionAdjustment(@RequestBody PolicyAdjustmentRequestDto adjustmentDto) {
		logger.info("PolicyRestController--contributionAdjustment-started");
		PolicyAdjustmentResponse commonDto = null;
		try {
			policyService.saveAdjustment(adjustmentDto);
		} catch (IllegalArgumentException e) {
			logger.error("PolicyRestController--contributionAdjustment-error", e);
			commonDto = new PolicyAdjustmentResponse();
			commonDto.setTransactionMessage(PolicyConstants.FAIL);
			commonDto.setTransactionStatus(PolicyConstants.ERROR);
		}
		logger.info("PolicyRestController--contributionAdjustment-ended");
		commonDto = policyService.saveAdjustmentOldDto(adjustmentDto.getPolicyId());
		return commonDto;
	}

	@GetMapping("/existing/policy")
	public PolicyResponseDto getExistingPolicyByPolicyNumber(@RequestParam String policyNumber, String unitId) {
		logger.info("PolicyRestController :: getExistingPolicyByPolicyNumber :: started");
		PolicyResponseDto commonDto = policyService.getExistingPolicyByPolicyNumber(policyNumber, unitId);
		logger.info("PolicyRestController :: getExistingPolicyByPolicyNumber :: ended");
		return commonDto;
	}

	@GetMapping("/existing/new")
	public PolicyResponseDto getExistingPolicyBymphId(@RequestParam Long mphId, @RequestParam Long policyId) {
		logger.info("PolicyRestController :: getExistingPolicyBymphId :: started");
		PolicyResponseDto commonDto = policyService.getExistingPolicyBymphId(CommonConstants.EXISTING, mphId, policyId);
		logger.info("PolicyRestController :: getExistingPolicyBymphId :: ended");
		return commonDto;
	}

	@GetMapping("/getPolicyDetailsByProposalNo")
	public PolicyDetailsResponseDto getPolicyDetailsByProposalNo(@RequestParam String proposalNo) {
		logger.info("PolicyRestController -- getPolicyDetailsByProposalNo --started");
		PolicyDetailsResponseDto commonDto = policyService.getPolicyDetailsByProposalNo(proposalNo);
		logger.info("PolicyRestController -- getPolicyDetailsByProposalNo --ended");
		return commonDto;
	}

	@GetMapping("/StampDuty/{amount}")
	public BigDecimal stampDuty(@PathVariable("amount") BigDecimal amount) {
		logger.info("PolicyRestController:stampDuty:started");
		BigDecimal commonDto = policyService.calculateStamps(amount);
		logger.info("PolicyRestController:stampDuty:ended");
		return commonDto;
	}

	@PostMapping("/StampDutyConsuption")
	public CommonResponseStampDto stampDutyConsuption(@RequestBody StampDutyConsumptionRequestDto requestDto)
			throws ApplicationException {
		logger.info("PolicyRestController:stampDutyConsuption:started");
		CommonResponseStampDto commonDto = policyService.stampDutyConsuption(requestDto);
		logger.info("PolicyRestController:stampDutyConsuption:ended");
		return commonDto;
	}

	@PostMapping("/generateFrequency")
	public RegularAdjustmentContributionResponseDto getFrequencyDates(@RequestBody PolicyFrequencyDetailsDto request)
			throws ParseException {
		logger.info("PolicyRestController:getFrequencyDatesy:started");
		return policyService.getFrequencyDates(request);
	}

	@GetMapping("/getFrequencyByPolicyId/{id}")
	public AdjustmentContributionResponseDto getFrequencyByPolicyId(@PathVariable("id") Long policyId) {
		logger.info("PolicyRestController:getFrequencyByPolicyId:started");
		return policyService.getFrequencyByPolicyId(policyId);
	}

	@GetMapping("getMember")
	public PolicyResponseDto getQuotationMember(@RequestParam Long memberId, @RequestParam Long policyId) {
		logger.info("PolicyMemberController -- get --started");
		PolicyResponseDto commonDto = policyService.getPolicyMember(memberId, policyId);
		logger.info("PolicyMemberController -- get --ended");
		return commonDto;
	}

	@GetMapping("getMemberDetails")
	public PolicyResponseDto getMemberDetails(@RequestParam String licId, @RequestParam Long policyId) {
		logger.info("PolicyMemberController -- getMemberDetails --started");
		PolicyResponseDto commonDto = policyService.getMemberDetails(licId, policyId);
		logger.info("PolicyMemberController -- getMemberDetails --ended");
		return commonDto;
	}

	@RequestMapping(value = "/getPolicyMemberDetailsDataTable", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPolicyMemberDetailsDataTable(
			@RequestBody GetPolicyMemberDetailsRequestDto getPolicyMemberDetailsRequestDto) {
		return new ResponseEntity<>(policyService.getPolicyMemberDetailsDataTable(getPolicyMemberDetailsRequestDto),
				HttpStatus.OK);
	}

	@PostMapping("/getPolicyContributionDetails")
	public PolicyResponseDto getPolicyContributionDetails(@RequestBody ContributionRequestDto contributionRequestDto) {
		logger.info("PolicyMemberController -- getPolicyContributionDetails --started");
		PolicyResponseDto commonDto = policyService.getPolicyContributionDetails(contributionRequestDto);
		logger.info("PolicyMemberController -- getPolicyContributionDetails --ended");
		return commonDto;
	}

	@PostMapping("/getMemberContributionDetails")
	public PolicyResponseDto getMemberContributionDetails(@RequestBody ContributionRequestDto contributionRequestDto) {
		logger.info("PolicyMemberController -- getMemberContributionDetails --started");
		PolicyResponseDto commonDto = policyService.getMemberContributionDetails(contributionRequestDto);
		logger.info("PolicyMemberController -- getMemberContributionDetails --ended");
		return commonDto;
	}

	@PostMapping("/getInvidualContriButionDetails")
	public PolicyResponseDto getInvidualContriButionDetails(
			@RequestBody ContributionRequestDto contributionRequestDto) {
		logger.info("PolicyMemberController -- getInvidualContriButionDetails --started");
		PolicyResponseDto commonDto = policyService.getInvidualContriButionDetails(contributionRequestDto);
		logger.info("PolicyMemberController -- getInvidualContriButionDetails --ended");
		return commonDto;
	}

	@GetMapping("/getInvidualContriButionDetails/download")
	public ResponseEntity<Resource> policyContributionSummary(@RequestParam Long policyId, @RequestParam Long adjConId,
			HttpServletResponse response) throws IOException {
		logger.info("ClaimMbrController -- fundSummaryDownload --started");
		InputStreamResource file = new InputStreamResource(policyService.policyContributionSummary(policyId, adjConId));
		logger.info("ClaimMbrController -- fundSummaryDownload --ended");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=Contribution_Entires" + adjConId + ".csv")
				.contentType(MediaType.parseMediaType("application/csv")).body(file);
	}

	@GetMapping("/getFullContribution/download")
	public ResponseEntity<Resource> getFullContribution(@RequestParam Long policyId, @RequestParam String finicialYear,
			HttpServletResponse response) {
		logger.info("ClaimMbrController -- getFullContribution --started");
		InputStreamResource file = new InputStreamResource(policyService.getFullContribution(policyId, finicialYear));
		logger.info("ClaimMbrController -- getFullContribution --ended");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=FullContribution_Entries" + policyId + ".csv")
				.contentType(MediaType.parseMediaType("application/csv")).body(file);
	}

	@PostMapping("callIssuancePolicyFromAccounting")
	public IssuePolicyResponseDto issuancePolicy(@RequestParam String policyNumber) {
		logger.info("PolicyRestController:IssuancePolicy:started");
		IssuePolicyResponseDto response = policyService.issuancePolicy(policyNumber);
		logger.info("PolicyRestController:IssuancePolicy:ended");
		return response;
	}

	@PostMapping("callTrnRegistrationFromAccounting")
	public TrnRegistrationResponseDto trnRegistration(@RequestParam String policyNumber) {
		logger.info("PolicyRestController:trnRegistration:started");
		TrnRegistrationResponseDto response = policyService.trnRegistration(policyNumber);
		logger.info("PolicyRestController:trnRegistration:ended");
		return response;
	}

	@PutMapping("sendToApproved")
	public PolicyResponseDto sendToApproved(@RequestParam Long policyId, String variantType)
			throws ApplicationException {
		logger.info("PolicyRestController:sendToApproved:started");
		PolicyResponseDto policyResponseDto = policyService.policyApproved(policyId, PolicyConstants.APPROVED,
				variantType);

		logger.info("PolicyRestController:saveContributionToTxnEntries::Start");
		policyServiceImpl.saveContributionToTxnEntries(policyResponseDto.getPolicyId());
		logger.info("PolicyRestController:saveContributionToTxnEntries::End");

		logger.info("PolicyRestController:saveZeroAccEntriesToTxnEntires::Start");
		policyCalcServiceImpl.setZeroAccEntriesToTxnEntires(policyResponseDto.getPolicyId(), variantType);
		logger.info("PolicyRestController:saveZeroAccEntriesToTxnEntires::Ends");

		logger.info("PolicyRestController:issuancePolicy::Start");
		issuancePolicy(policyResponseDto.getPolicyNumber());
		logger.info("PolicyRestController:issuancePolicy::Ends");

		logger.info("PolicyServiceImpl-trnRegistration-CallstrnRegistration-Start::{},",
				policyResponseDto.getPolicyNumber());
		trnRegistration(policyResponseDto.getPolicyNumber());
		logger.info("PolicyServiceImpl-trnRegistration-CallstrnRegistration-End::{},",
				policyResponseDto.getPolicyNumber());

		logger.info("PolicyRestController:sendToApproved:ended");
		return policyResponseDto;
	}

	@PostMapping("checkByPolicyNumber/issuancePolicy")
	public PolicyResponseDto checkissuancePolicySuccessOrNot(@RequestParam String policyNumber) {
		logger.info("PolicyRestController:checkissuancePolicySuccessOrNot:started");
		PolicyResponseDto policyResponseDto = policyService.checkissuancePolicySuccessOrNot(policyNumber);
		logger.info("PolicyRestController:sendToApproved:ended");
		return policyResponseDto;
	}

	@PostMapping("policyDetailSearch")
	public List<PolicyDetailSearchResponseDto> policyDetailSearch(
			@RequestBody PolicyDetailSearchRequestDto policyDetailSearchRequestDto) {
		logger.info("PolicyRestController:policyDetailSearch:started");
		List<PolicyDetailSearchResponseDto> policyResponseDto = policyService
				.policyDetailSearch(policyDetailSearchRequestDto);
		logger.info("PolicyRestController:policyDetailSearch:ended");
		return policyResponseDto;
	}

	@GetMapping("members/download")
	public ResponseEntity<Resource> membersDownload(@RequestParam Long policyId, @RequestParam Long mphId,
			@RequestParam String unitCode) throws Exception {
		logger.info("PolicyRestController:membersDownload:started");
		InputStreamResource file = new InputStreamResource(policyService.membersDownload(policyId, mphId, unitCode));
		logger.info("PolicyRestController:membersDownload:ended");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Members_List_" + policyId + ".csv")
				.contentType(MediaType.parseMediaType("application/csv")).body(file);
	}

	@GetMapping("getMemberDetails/LicIdandMemberId")
	public PolicyResponseDto getMemberDetailsByLicIdandMemberId(@RequestParam(defaultValue = "") String licId,
			@RequestParam Long policyId, @RequestParam(defaultValue = "") String membershipNumber) {
		logger.info("PolicyMemberController -- getMemberDetailsByLicIdandMemberId --started");
		PolicyResponseDto commonDto = policyService.getMemberDetailsByLicIdandMemberId(licId, policyId,
				membershipNumber);
		logger.info("PolicyMemberController -- getMemberDetailsByLicIdandMemberId --ended");
		return commonDto;
	}

//	Depsoit Refund Policy Search
	@PostMapping("/depsoitRefundPolicySearch")
	public CommonResponseStampDto DepsoitRefundPolicySearch(@RequestBody DepositRefundPolicySearchRequestDto dto) {
		CommonResponseStampDto response = policyService.DepsoitRefundPolicySearch(dto);
		return response;
	}

	@PostMapping("/fetchDetailsFrPolicyFundStatement")
	public PolicyFundStmtResponseDto fetchDetailsFrPolicyFundStatement(
			@RequestBody PolicyFundStmtRequestDto requestDto) {
		PolicyFundStmtResponseDto responseDto = null;
		try {
			logger.info("PolicyMemberController -- fetchDetailsFrPolicyFundStatement --started");
			responseDto = policyService.fetchDetailsFrPolicyFundStatement(requestDto);
			logger.info("PolicyMemberController -- fetchDetailsFrPolicyFundStatement --ended");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseDto;
	}

}
