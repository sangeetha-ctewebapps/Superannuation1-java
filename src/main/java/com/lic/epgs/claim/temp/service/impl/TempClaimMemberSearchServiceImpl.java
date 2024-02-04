package com.lic.epgs.claim.temp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.claim.dto.ClaimMemberSearchResponseDto;
import com.lic.epgs.claim.dto.PolicyMemberSearchRequestDto;
import com.lic.epgs.claim.temp.service.TempClaimMemberSearchService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.quotation.repository.QuotationMemberRepo;

@Service
public class TempClaimMemberSearchServiceImpl implements TempClaimMemberSearchService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	QuotationMemberRepo quotationMemberRepo;

//	@Autowired
//	PolicyRepository policyRepository;

	private ClaimMemberSearchResponseDto buildDummyData(String memberShipNo) {

		ClaimMemberSearchResponseDto response = new ClaimMemberSearchResponseDto();
		response.setMasterPolicyNo("POL00000248");
		response.setMasterPolicyStatus(1);
		response.setMemberId("309");
		response.setMemberName(buildName("first name", "last name"));
		if (StringUtils.isNotBlank(memberShipNo)) {
			response.setMemberShipNo(memberShipNo);
		} else {
			response.setMemberShipNo(UUID.randomUUID().toString());
		}
		response.setMphCode("C00000356");
		response.setMphName("BISLARI");
		return response;
	}

	@Override
	public ApiResponseDto<List<ClaimMemberSearchResponseDto>> memberSearch(PolicyMemberSearchRequestDto request) {
		List<ClaimMemberSearchResponseDto> result = new ArrayList<>(1);
		result.add(buildDummyData(null));
		return ApiResponseDto.success(result);
	}

	@Override
	public ApiResponseDto<List<ClaimMemberSearchResponseDto>> memberShipNoSearch(String memberShipNo) {
//		List<ClaimMemberSearchResponseDto> result = null;
//		List<QuotationMemberEntity> memberResult = quotationMemberRepo.findAllByMemberShipIdAndIsActive(memberShipNo,
//				Boolean.TRUE);
//		if (memberResult != null && !memberResult.isEmpty()) {
//			result = new ArrayList<>(memberResult.size());
//			for (int i = 0; i < memberResult.size(); i++) {
//				ClaimMemberSearchResponseDto response = convertToSearchResponseDto(memberResult.get(i));
//				if (response != null) {
//					result.add(response);
//				}
//			}
//		}
//		return ApiResponseDto.success(result);
		List<ClaimMemberSearchResponseDto> result = new ArrayList<>(1);
		result.add(buildDummyData(memberShipNo));
		return ApiResponseDto.success(result);
	}

//	private ClaimMemberSearchResponseDto convertToSearchResponseDto(QuotationMemberEntity quotationMemberEntity) {
//		ClaimMemberSearchResponseDto response = null;
//		if (quotationMemberEntity != null) {
//			PolicyEntity policyEntity = policyRepository.findByQuotationNo(quotationMemberEntity.getQuotationId());
//			if (policyEntity != null) {
//				response = new ClaimMemberSearchResponseDto();
//				response.setAge(1);
//				response.setCategory(policyEntity.getCategory());
//				response.setMasterPolicyNo(policyEntity.getPolicyNumber());
//				response.setMasterPolicyStatus(1);
//				response.setMemberId(quotationMemberEntity.getMemberShipId());
//				response.setMemberName(
//						buildName(quotationMemberEntity.getFirstName(), quotationMemberEntity.getLastName()));
//				response.setMemberShipNo(quotationMemberEntity.getMembershipNumber());
//				response.setMphCode(policyEntity.getMphCode());
//				response.setMphName(policyEntity.getMphName());
//				response.setProduct(policyEntity.getProduct());
//			}
//		}
//		return response;
//	}

	private String buildName(String firstName, String lastName) {
		if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName))
			return String.format("%s %s", firstName, lastName);
		return firstName;
	}

}
