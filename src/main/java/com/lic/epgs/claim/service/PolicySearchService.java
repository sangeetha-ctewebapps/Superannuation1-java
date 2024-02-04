package com.lic.epgs.claim.service;

import java.util.List;

import com.lic.epgs.claim.dto.PolicyMemberSearchRequestDto;
import com.lic.epgs.claim.dto.PolicySearchMemberDto;
import com.lic.epgs.claim.dto.PolicySearchRequestDto;
import com.lic.epgs.claim.dto.PolicySearchResponseDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface PolicySearchService {

	public ApiResponseDto<List<PolicySearchResponseDto>> policySearch(
			PolicySearchRequestDto claimMemberSearchRequestDto);

	public ApiResponseDto<List<PolicySearchResponseDto>> memberSearch(
			PolicyMemberSearchRequestDto claimMemberSearchRequestDto);

	public ApiResponseDto<List<PolicySearchResponseDto>> memberShipNoSearch(String memberShipNo);

	public ApiResponseDto<PolicySearchResponseDto> memberIdSearch(Long memberId);

	public ApiResponseDto<List<PolicySearchMemberDto>> memberSearchNew(
			PolicyMemberSearchRequestDto claimMemberSearchRequestDto);

	public ApiResponseDto<PolicySearchResponseDto> getMemberListBypolicyId(Long policyId);

	public ApiResponseDto<List<PolicySearchResponseDto>> policySearchPradeep(
			PolicySearchRequestDto claimMemberSearchRequestDto);

	public ApiResponseDto<List<PolicySearchMemberDto>> memberNewSearch(
			PolicyMemberSearchRequestDto claimMemberSearchRequestDto);

	public ApiResponseDto<List<PolicySearchMemberDto>> getmemberListByPolicyNumber(Long policyId);

	public ApiResponseDto<List<PolicySearchResponseDto>> policycriteriaSearch(
			PolicySearchRequestDto claimMemberSearchRequestDto);
}