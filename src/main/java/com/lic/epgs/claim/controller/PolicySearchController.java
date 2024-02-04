package com.lic.epgs.claim.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.claim.dto.PolicyMemberSearchRequestDto;
import com.lic.epgs.claim.dto.PolicySearchMemberDto;
import com.lic.epgs.claim.dto.PolicySearchRequestDto;
import com.lic.epgs.claim.dto.PolicySearchResponseDto;
import com.lic.epgs.claim.service.PolicySearchService;
import com.lic.epgs.common.dto.ApiResponseDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy")
public class PolicySearchController {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	PolicySearchService policySearchService;

	@PostMapping("/search")
	public ApiResponseDto<List<PolicySearchResponseDto>> memberSearch(
			@RequestBody PolicySearchRequestDto claimMemberSearchRequestDto) {
//		return policySearchService.policySearch(claimMemberSearchRequestDto);
		logger.info("PolicySearchController -- memberSearch --started");
		ApiResponseDto<List<PolicySearchResponseDto>> responseDto = policySearchService
				.policySearchPradeep(claimMemberSearchRequestDto);
		logger.info("PolicySearchController -- memberSearch --ended");
		return responseDto;
	}

	@PostMapping("/criteriasearch")
	public ApiResponseDto<List<PolicySearchResponseDto>> policySearchOtherCriteriamemberSearch(
			@RequestBody PolicySearchRequestDto claimMemberSearchRequestDto) {
//		return policySearchService.policySearch(claimMemberSearchRequestDto);
		logger.info("PolicySearchController -- policySearchOtherCriteriamemberSearch --started");
		ApiResponseDto<List<PolicySearchResponseDto>> responseDto = policySearchService
				.policycriteriaSearch(claimMemberSearchRequestDto);
		logger.info("PolicySearchController -- policySearchOtherCriteriamemberSearch --ended");
		return responseDto;
	}

	@PostMapping("/memberSearch")
	public ApiResponseDto<List<PolicySearchResponseDto>> memberSearch(
			@RequestBody PolicyMemberSearchRequestDto claimMemberSearchRequestDto) {
		logger.info("PolicySearchController -- memberSearch --started");
		ApiResponseDto<List<PolicySearchResponseDto>> responseDto = policySearchService
				.memberSearch(claimMemberSearchRequestDto);
		logger.info("PolicySearchController -- memberSearch --ended");
		return responseDto;
	}

	@PostMapping("/memberSearchNew")
	public ApiResponseDto<List<PolicySearchMemberDto>> memberSearchNew(
			@RequestBody PolicyMemberSearchRequestDto claimMemberSearchRequestDto) {
//		return policySearchService.memberSearchNew(claimMemberSearchRequestDto);
		logger.info("PolicySearchController -- memberSearchNew --started");
		ApiResponseDto<List<PolicySearchMemberDto>> responseDto = policySearchService
				.memberNewSearch(claimMemberSearchRequestDto);
		logger.info("PolicySearchController -- memberSearchNew --ended");
		return responseDto;
	}

	@GetMapping("/getmemberList")
	public ApiResponseDto<List<PolicySearchMemberDto>> getmemberListByPolicyNumber(@RequestParam Long policyId) {
		logger.info("PolicySearchController -- getmemberListByPolicyNumber --started");
		ApiResponseDto<List<PolicySearchMemberDto>> responseDto = policySearchService
				.getmemberListByPolicyNumber(policyId);
		logger.info("PolicySearchController -- getmemberListByPolicyNumber --ended");
		return responseDto;
	}

	@GetMapping("/search/{memberShipNo}")
	public ApiResponseDto<List<PolicySearchResponseDto>> memberShipNoSearch(@PathVariable String memberShipNo) {
		logger.info("PolicySearchController -- memberShipNoSearch --started");
		ApiResponseDto<List<PolicySearchResponseDto>> responseDto = policySearchService
				.memberShipNoSearch(memberShipNo);
		logger.info("PolicySearchController -- memberShipNoSearch --ended");
		return responseDto;
	}
//	@GetMapping("/memberById/{memberId}")
//	public ApiResponseDto<PolicySearchResponseDto> memberIdSearch(@PathVariable Long memberId) {
//		return policySearchService.memberIdSearch(memberId);
//	}

	@GetMapping("/memberById")
	public ApiResponseDto<PolicySearchResponseDto> memberIdSearch(@RequestParam Long memberId) {
		logger.info("PolicySearchController -- memberIdSearch --started");
		ApiResponseDto<PolicySearchResponseDto> responseDto = policySearchService.memberIdSearch(memberId);
		logger.info("PolicySearchController -- memberIdSearch --ended");
		return responseDto;
	}

	@GetMapping("/memberListBypolicyId")
	public ApiResponseDto<PolicySearchResponseDto> getMemberListBypolicyId(@RequestParam Long policyId) {
		logger.info("PolicySearchController -- getMemberListBypolicyId --started");
		ApiResponseDto<PolicySearchResponseDto> responseDto = policySearchService.getMemberListBypolicyId(policyId);
		logger.info("PolicySearchController -- getMemberListBypolicyId --ended");
		return responseDto;
	}
}
