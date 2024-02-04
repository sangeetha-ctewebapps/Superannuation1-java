package com.lic.epgs.claim.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.claim.constants.ClaimConstants;
import com.lic.epgs.claim.dto.PolicyMemberSearchRequestDto;
import com.lic.epgs.claim.dto.PolicySearchMemberDto;
import com.lic.epgs.claim.dto.PolicySearchRequestDto;
import com.lic.epgs.claim.dto.PolicySearchResponseDto;
import com.lic.epgs.claim.service.PolicySearchService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.NumericUtils;

@Service
public class PolicySearchServiceImpl implements PolicySearchService {

	protected final Logger logger = LogManager.getLogger(getClass());

//	@Autowired
//	private MemberMasterEntity memberMasterEntity;

	@Autowired
	private MemberMasterRepository memberMasterRepository;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	PolicyMasterRepository policyMasterRepository;

	@Autowired
	MphMasterRepository mphMasterRepository;

	@Override
	public ApiResponseDto<List<PolicySearchResponseDto>> policySearchPradeep(
			PolicySearchRequestDto claimMemberSearchRequestDto) {
		List<PolicySearchResponseDto> response = new ArrayList<>();
		try {
			logger.info("PolicyServiceImpl:policySearch-Pradeep:Start");

			String policyNumber = "";
			Boolean isActive = Boolean.TRUE;

			if (StringUtils.isNotBlank(claimMemberSearchRequestDto.getMasterPolicyNo())) {
				policyNumber = claimMemberSearchRequestDto.getMasterPolicyNo();
			}

			List<Object> result = policyMasterRepository.policySearchPradeep(policyNumber, isActive,claimMemberSearchRequestDto.getUnitCode());
			if (result != null && !result.isEmpty()) {
				for (Object object : result) {
					Object[] ob = (Object[]) object;
					PolicySearchResponseDto resonseDto = new PolicySearchResponseDto();
					resonseDto.setMphCode(String.valueOf(ob[1]));
					resonseDto.setMphName(String.valueOf(ob[2]));
					resonseDto.setPolicyId(String.valueOf(ob[3]));
					resonseDto.setPolicyNo(String.valueOf(ob[4]));
					resonseDto.setPolicyStatus(String.valueOf(ob[5]));
					resonseDto.setUnitCode(String.valueOf(ob[6]));
					response.add(resonseDto);
				}
				return ApiResponseDto.success(response);
			}
			else {
				return ApiResponseDto.error(ErrorDto.builder().message(ClaimConstants.POLICY_NOT_AVAILABLE).build());
			}
			
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:policySearch-Pradeep", e);
			return ApiResponseDto.error(ErrorDto.builder().message(e.getMessage()).build());
		} finally {
			logger.info("PolicyServiceImpl:policySearch-Pradeep:Ends");
		}
	}

	@Override
	public ApiResponseDto<List<PolicySearchResponseDto>> policySearch(
			PolicySearchRequestDto claimMemberSearchRequestDto) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("PolicyServiceImpl {}  :: {} inprogressCitrieaSearch {} :: {} Start");
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<MphMasterEntity> createQuery = criteriaBuilder.createQuery(MphMasterEntity.class);
			Root<MphMasterEntity> root = createQuery.from(MphMasterEntity.class);
			Join<MphMasterEntity, PolicyMasterEntity> join = root.join("policyMaster");

			if (StringUtils.isNotBlank(claimMemberSearchRequestDto.getMasterPolicyNo())) {
				join.on(criteriaBuilder.equal(join.get("policyNumber"),
						claimMemberSearchRequestDto.getMasterPolicyNo()));
			}

			join.on(criteriaBuilder.equal(join.get("policyStatus"), CommonConstants.COMMON_APPROVED));

			if (StringUtils.isNotBlank(claimMemberSearchRequestDto.getUnitCode())) {
				join.on(criteriaBuilder.equal(join.get("unitId"), claimMemberSearchRequestDto.getUnitCode()));
			}

			if (StringUtils.isNotBlank(claimMemberSearchRequestDto.getMphName())) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("mphName")),
						"%" + claimMemberSearchRequestDto.getMphName().toLowerCase() + "%"));
			}

			if (StringUtils.isNotBlank(claimMemberSearchRequestDto.getPan())) {
				predicates.add(criteriaBuilder.equal(root.get("pan"), claimMemberSearchRequestDto.getPan()));
			}

			if (StringUtils.isNotBlank(claimMemberSearchRequestDto.getMasterPolicyNo())) {
				predicates.add(criteriaBuilder.equal(join.get("policyNumber"),
						claimMemberSearchRequestDto.getMasterPolicyNo()));
			}

			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			createQuery.orderBy(criteriaBuilder.desc(root.get("modifiedOn")));

			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			List<MphMasterEntity> result = entityManager.createQuery(createQuery).getResultList();
			List<PolicySearchResponseDto> response = result.stream().map(this::convertMasterEntityToDto)
					.collect(Collectors.toList());
//			response.forEach(policyDto -> policyDto.getDeposit()
//					.removeIf(deposit -> PolicyConstants.ADJESTED.equalsIgnoreCase(deposit.getStatus())));

			return ApiResponseDto.success(response);

		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:inprogressCitrieaSearch", e);
			return ApiResponseDto.error(ErrorDto.builder().message(e.getMessage()).build());
		} finally {
			logger.info("PolicyServiceImpl:inprogressCitrieaSearch:Ends");
		}

	}

	private PolicySearchResponseDto convertMasterEntityToDto(MphMasterEntity mphMasterEntity) {

		PolicySearchResponseDto policySearchResponseDto = new PolicySearchResponseDto();

		policySearchResponseDto.setMphCode(mphMasterEntity.getMphCode());
		policySearchResponseDto.setMphName(mphMasterEntity.getMphName());
//		policySearchResponseDto.setMphId(mphMasterEntity.getMphId());
//		policySearchResponseDto.setProposalNumber(mphMasterEntity.getProposalNumber());
		Set<PolicyMasterEntity> policyMasterEntity = mphMasterEntity.getPolicyMaster();
		if (!policyMasterEntity.isEmpty()) {
			for (PolicyMasterEntity policyMaster : policyMasterEntity) {
				policySearchResponseDto.setPolicyId(NumericUtils.convertLongToString(policyMaster.getPolicyId()));
				policySearchResponseDto.setPolicyNo(policyMaster.getPolicyNumber());
				policySearchResponseDto.setPolicyStatus(policyMaster.getPolicyStatus());
				policySearchResponseDto.setUnitCode(policyMaster.getUnitId());
				policySearchResponseDto.setProduct(NumericUtils.convertLongToString(policyMaster.getProductId()));
				Set<PolicySearchMemberDto> memberMasterEntityList = new HashSet<>();
				if (!policyMaster.getMemberMaster().isEmpty()) {
					for (MemberMasterEntity policyMasterMbr : policyMaster.getMemberMaster()) {
						PolicySearchMemberDto memberMasterEntity = new PolicySearchMemberDto();
						memberMasterEntity.setLicId(policyMasterMbr.getLicId());
						memberMasterEntity.setMemberShipId(policyMasterMbr.getMembershipNumber());
						memberMasterEntity.setFirstName(policyMasterMbr.getFirstName());
						memberMasterEntity.setMemberStatus(policyMasterMbr.getMemberStatus());
						memberMasterEntity.setPolicyId(policyMasterMbr.getPolicyId());
						memberMasterEntityList.add(memberMasterEntity);

					}
				}
				policySearchResponseDto.setMember(memberMasterEntityList);
			}
		}

		return policySearchResponseDto;
	}

	@Override
	public ApiResponseDto<List<PolicySearchResponseDto>> memberSearch(
			PolicyMemberSearchRequestDto claimMemberSearchRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponseDto<List<PolicySearchResponseDto>> memberShipNoSearch(String memberShipNo) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public ApiResponseDto<PolicySearchResponseDto> memberIdSearch(String licId, String policyId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Autowired
//	PolicyRepository policyRepository;

//	private List<PolicyEntity> search(PolicySearchRequestDto request) {
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		List<Predicate> predicates = new ArrayList<>();
//
//		CriteriaQuery<PolicyEntity> searchQuery = criteriaBuilder.createQuery(PolicyEntity.class);
//		Root<PolicyEntity> root = searchQuery.from(PolicyEntity.class);
//		if (StringUtils.isNotBlank(request.getMasterPolicyNo())) {
//			predicates.add(criteriaBuilder.equal(root.get("policyNumber"), request.getMasterPolicyNo()));
//		}
//		if (StringUtils.isNotBlank(request.getMphName())) {
//			predicates.add(criteriaBuilder.equal(root.get("mphName"), request.getMphName()));
//		}
//
//		if (StringUtils.isNotBlank(request.getMphCode())) {
//			predicates.add(criteriaBuilder.equal(root.get("mphCode"), request.getMphCode()));
//		}
//
//		if (StringUtils.isNotBlank(request.getPolicyStatus())) {
//			predicates.add(criteriaBuilder.equal(root.get("policyStatus"), request.getPolicyStatus()));
//		} else {
//			predicates.add(criteriaBuilder.equal(root.get("policyStatus"), CommonConstants.COMMON_APPROVED));
//		}
//
//		predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
//		searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));
//
//		return entityManager.createQuery(searchQuery).getResultList();
//	}
//
//	@Override
//	public ApiResponseDto<List<PolicySearchResponseDto>> policySearch(PolicySearchRequestDto request) {
//		List<PolicyEntity> searchResponse = search(request);
//		List<PolicySearchResponseDto> result = new ArrayList<>();
//		if (searchResponse != null && !searchResponse.isEmpty()) {
//			for (PolicyEntity policyTempEntity : searchResponse) {
//				result.add(convertToSearchResponse(policyTempEntity));
//			}
//		}
//		return ApiResponseDto.success(result);
//	}
//
//	private PolicySearchResponseDto convertToSearchResponse(PolicyEntity policyEntity) {
//		PolicySearchResponseDto response = new PolicySearchResponseDto();
//		if (policyEntity != null) {
//			response.setPolicyNo(policyEntity.getPolicyNumber());
//			response.setPolicyId(policyEntity.getPolicyId());
//			response.setPolicyStatus(policyEntity.getPolicyStatus());
//			List<PolicySearchMemberDto> member = new ArrayList<>();
//			if (policyEntity.getMembers() != null && !policyEntity.getMembers().isEmpty()) {
//				for (PolicyMbrEntity memberEntity : policyEntity.getMembers()) {
//					if(memberEntity.getIsZeroId()==Boolean.FALSE) {
//					member.add(convertToPolicyMemberDto(memberEntity));
//					}
//				}
//			}
//			response.setMember(member);
//			response.setMphCode(policyEntity.getMphCode());
//			response.setMphName(policyEntity.getMphName());
//			response.setProduct(policyEntity.getProduct());
//			response.setCategory(String.valueOf(policyEntity.getCategory()));
//			response.setVarient(policyEntity.getVariant());
//			response.setCommencementDate(policyEntity.getPolicyCommencementDate());
//			
//		}
//		return response;
//	}
//
	private PolicySearchMemberDto convertToPolicyMemberDto(MemberMasterEntity memberEntity) {
		PolicySearchMemberDto member = new PolicySearchMemberDto();
		member.setAadhar(memberEntity.getAadharNumber());
		if (memberEntity.getDateOfBirth() != null) {
			member.setDob(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(memberEntity.getDateOfBirth()));
		}
		member.setMemberId(memberEntity.getMemberId());
		member.setFirstName(memberEntity.getFirstName());
		member.setLastName(memberEntity.getLastName());
		member.setFirstName(memberEntity.getFirstName());
		member.setLicId(memberEntity.getLicId());
		member.setPolicyId(memberEntity.getPolicyId());
		member.setMemberShipId(memberEntity.getMembershipNumber());
		member.setMemberStatus(memberEntity.getMemberStatus());
		member.setCategory(String.valueOf(memberEntity.getCategoryNo()));
		member.setDateOfJoining(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(memberEntity.getDateOfJoining()));
		return member;
	}

//
//	@Override
//	public ApiResponseDto<List<PolicySearchResponseDto>> memberSearch(PolicyMemberSearchRequestDto request) {
//		List<PolicyEntity> searchResponse = search(request);
//		List<PolicySearchResponseDto> result = new ArrayList<>();
//		if (searchResponse != null && !searchResponse.isEmpty()) {
//			for (PolicyEntity policyTempEntity : searchResponse) {
//				result.add(convertToSearchResponse(policyTempEntity));
//			}
//		}
//		return ApiResponseDto.success(result);
//	}
//
//	private List<PolicyEntity> search(PolicyMemberSearchRequestDto request) {
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		List<Predicate> predicates = new ArrayList<>();
//
//		CriteriaQuery<PolicyEntity> searchQuery = criteriaBuilder.createQuery(PolicyEntity.class);
//		Root<PolicyEntity> root = searchQuery.from(PolicyEntity.class);
//
//		Join<PolicyEntity, PolicyMbrEntity> join = root.join("members");
//		if (StringUtils.isNotBlank(request.getPan())) {
//			join.on(criteriaBuilder.equal(join.get("pan"), request.getPan()));
//		}
//		if (StringUtils.isNotBlank(request.getAadhar())) {
//			join.on(criteriaBuilder.equal(join.get("aadharNumber"), request.getAadhar()));
//		}
//		
//
//		if (StringUtils.isNotBlank(request.getMemberShipNo())) {
//			join.on(criteriaBuilder.equal(join.get("memberShipId"), request.getMemberShipNo()));
//		}
//		if (StringUtils.isNotBlank(request.getDob())) {
//			join.on(criteriaBuilder.equal(join.get("dateofbirth"),
//					CommonDateUtils.convertStringToDate(request.getDob())));
//		}
//
//		if (StringUtils.isNotBlank(request.getMasterPolicyNo())) {
//			predicates.add(criteriaBuilder.equal(root.get("policyNumber"), request.getMasterPolicyNo()));
//
//		}
////		if (StringUtils.isNotBlank(request.getMphName())) {
////			predicates.add(criteriaBuilder.equal(root.get("mphName"), request.getMphName()));
////		}
//
//		if (StringUtils.isNotBlank(request.getMphCode())) {
//			predicates.add(criteriaBuilder.equal(root.get("mphCode"), request.getMphCode()));
//		}
//
//		if (StringUtils.isNotBlank(request.getPolicyStatus())) {
//			predicates.add(criteriaBuilder.equal(root.get("policyStatus"), request.getPolicyStatus()));
//		}
////		else
////		{
////			predicates.add(criteriaBuilder.equal(root.get("policyStatus"), CommonConstants.COMMON_APPROVED));
////		}
//		predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
//		searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));
//		return entityManager.createQuery(searchQuery).getResultList();
//		
//		
//		
//		
//		
//	}
//
//	@Override
//	public ApiResponseDto<List<PolicySearchResponseDto>> memberShipNoSearch(String memberShipNo) {
//		PolicyMemberSearchRequestDto request = new PolicyMemberSearchRequestDto();
//		request.setMemberShipNo(memberShipNo);
//		List<PolicyEntity> searchResponse = search(request);
//		List<PolicySearchResponseDto> result = new ArrayList<>();
//		if (searchResponse != null && !searchResponse.isEmpty()) {
//			for (PolicyEntity policyTempEntity : searchResponse) {
//				result.add(convertToSearchResponse(policyTempEntity));
//			}
//		}
//		return ApiResponseDto.success(result);
//	}
//
	@Override
	public ApiResponseDto<PolicySearchResponseDto> memberIdSearch(Long memberId) {
		PolicySearchResponseDto response = new PolicySearchResponseDto();
//		ApiResponseDto<PolicySearchResponseDto> responseDto=new ApiResponseDto<>();
//		PolicyMasterEntity policyMasterEntity = policyMasterRepository.findByPolicyIdAndIsActive(policyId,
//				Boolean.TRUE);
//		if (policyMasterEntity == null) {
//			return ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_POLICY).build());
//		}
//		
//		MemberMasterEntity policyMbrEntity = memberMasterRepository.findByLicIdAndPolicyIdAndIsZeroidFalse(licId,
//				policyId);
		try {
			logger.info("PolicyServiceImpl{} :: {} memberIdSearch {} :: {} Start");
			Object memberDetails= memberMasterRepository.findByMemberbyMemberId(memberId,Boolean.TRUE);
			if (memberDetails != null) {
					Object[] ob = (Object[]) memberDetails;
					PolicySearchMemberDto resonseDto = new PolicySearchMemberDto();
					resonseDto.setMemberShipId(String.valueOf(ob[0]));
					resonseDto.setFirstName(String.valueOf(ob[1]));
					resonseDto.setCategory(String.valueOf(ob[6]));
					resonseDto.setDob(String.valueOf(ob[7]));
					resonseDto.setDateOfJoining(String.valueOf(ob[8]));
					resonseDto.setProduct(String.valueOf(ob[9]));
					resonseDto.setMemberId(NumericUtils.stringToLong(String.valueOf(ob[10])));
					resonseDto.setPolicyId(NumericUtils.stringToLong(String.valueOf(ob[12])));
					resonseDto.setLicId(String.valueOf(ob[11]));
					response.setPolicyNo(String.valueOf(ob[2]));
					response.setPolicyStatus(String.valueOf(ob[3]));
					response.setMphCode(String.valueOf(ob[4]));
					response.setMphName(String.valueOf(ob[5]));
					
					response.setMemberDto(resonseDto);
				
			}
			
//		if (policyMbrEntity == null) {
//			return ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_MEMBER).build());
//		}
//		if (policyMbrEntity != null) {
//			PolicySearchMemberDto dto = convertToPolicyMemberDto(policyMbrEntity);
//			dto.setProduct(NumericUtils.convertLongToString(policyMasterEntity.getProductId()));
//			response.setMemberDto(dto);
//			response.setPolicyId(NumericUtils.convertLongToString(policyMbrEntity.getPolicyId()));
//		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PolicyServiceImpl{} :: {} memberIdSearch {} :: {} Error is {} "+e.getMessage());
		}
		logger.info("PolicyServiceImpl{} :: {} memberIdSearch {} :: {} Ended");
		return  ApiResponseDto.success(response);
	}

//
	@Override
	public ApiResponseDto<List<PolicySearchMemberDto>> memberSearchNew(
			PolicyMemberSearchRequestDto claimMemberSearchRequestDto) {
		logger.info("PolicyServiceImpl{} :: {} memberSearchNew {} :: {} Start");
		ApiResponseDto<List<PolicySearchMemberDto>> responseDto=new ApiResponseDto<>();
		List<PolicySearchMemberDto> result;
		try {
			PolicyMasterEntity policyEntity = policyMasterRepository
					.findByPolicyNumberAndIsActiveTrue(claimMemberSearchRequestDto.getMasterPolicyNo());
			if (policyEntity != null) {
				ApiResponseDto.error(ErrorDto.builder().message("Invalid PolicyNumber").build());
			}
			List<MemberMasterEntity> searchResponse = searchMember(claimMemberSearchRequestDto, policyEntity);
			result = new ArrayList<>();
			if (searchResponse != null && !searchResponse.isEmpty()) {
				for (MemberMasterEntity policyMbrrEntity : searchResponse) {
					result.add(convertToPolicyMemberDto(policyMbrrEntity));
					responseDto.setData(result);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PolicyServiceImpl{} :: {} memberSearchNew {} :: {} error is "+e.getMessage());
		}
		logger.info("PolicyServiceImpl{} :: {} memberSearchNew {} :: {} ended");
		return responseDto;

	}

//	
	private List<MemberMasterEntity> searchMember(PolicyMemberSearchRequestDto request,
			PolicyMasterEntity policyEntity) {
		List<MemberMasterEntity> masterEntities=new ArrayList<>();
		logger.info("PolicyServiceImpl{} :: {} searchMember {} :: {} Start");
		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			List<Predicate> predicates = new ArrayList<>();

			CriteriaQuery<MemberMasterEntity> searchQuery = criteriaBuilder.createQuery(MemberMasterEntity.class);
			Root<MemberMasterEntity> root = searchQuery.from(MemberMasterEntity.class);
			if (StringUtils.isNotBlank(request.getPan())) {
				predicates.add(criteriaBuilder.equal(root.get("pan"), request.getPan()));
			}
			if (StringUtils.isNotBlank(request.getLicId())) {
				predicates.add(criteriaBuilder.equal(root.get("licId"), request.getLicId()));
			}
			if (StringUtils.isNotBlank(request.getAadhar())) {

				predicates.add(criteriaBuilder.equal(root.get("aadharNumber"), request.getAadhar()));
			}

			if (StringUtils.isNotBlank(request.getFirstName())) {

				predicates.add(criteriaBuilder.equal(root.get("firstName"), request.getFirstName()));
			}

			if (StringUtils.isNotBlank(request.getMemberShipId())) {

				predicates.add(criteriaBuilder.equal(root.get("memberShipId"), request.getMemberShipId()));
			}
			if (StringUtils.isNotBlank(request.getMemberShipNo())) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("memberShipNo")),
						"%" + request.getMemberShipNo().toLowerCase() + "%"));
			}
			if (StringUtils.isNotBlank(request.getMasterPolicyNo())) {
				predicates.add(criteriaBuilder.equal(root.get("policyId"), policyEntity.getPolicyId()));

			} else {
				predicates.add(criteriaBuilder.equal(root.get("policyStatus"), CommonConstants.COMMON_APPROVED));
			}
			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			masterEntities= entityManager.createQuery(searchQuery).getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PolicyServiceImpl{} :: {} searchMember {} :: {} error is "+e.getMessage());
		}

		logger.info("PolicyServiceImpl{} :: {} searchMember {} :: {} ended");
		return masterEntities;
		
	}

	@Override
	public ApiResponseDto<PolicySearchResponseDto> getMemberListBypolicyId(Long policyId) {
		ApiResponseDto<PolicySearchResponseDto> responseDto=new ApiResponseDto<>();
		logger.info("PolicyServiceImpl{} :: {} getMemberListBypolicyId {} :: {} start");
		try {
			PolicyMasterEntity policyMasterEntity = policyMasterRepository.findByPolicyIdAndIsActive(policyId,
					Boolean.TRUE);
			if (policyMasterEntity == null) {
				return ApiResponseDto.error(ErrorDto.builder().message("Not Found Member List").build());
			}
			PolicySearchResponseDto response = convertMasterPolicyEntityToDtoResponse(policyMasterEntity);
			responseDto.setData(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PolicyServiceImpl{} :: {} getMemberListBypolicyId {} :: {} error is "+e.getMessage());
		}
		logger.info("PolicyServiceImpl{} :: {} getMemberListBypolicyId {} :: {} ended");
		return responseDto;
	}

	private PolicySearchResponseDto convertMasterPolicyEntityToDtoResponse(PolicyMasterEntity mphMasterEntity) {
		PolicySearchResponseDto policySearchResponseDto = new PolicySearchResponseDto();
		Set<PolicySearchMemberDto> memberDto = new HashSet<>();
		for (MemberMasterEntity policyMasterMbr : mphMasterEntity.getMemberMaster()) {
			PolicySearchMemberDto memberMasterEntity = new PolicySearchMemberDto();
			memberMasterEntity.setLicId(policyMasterMbr.getLicId());
			memberMasterEntity.setMemberShipId(policyMasterMbr.getMembershipNumber());
			memberMasterEntity.setFirstName(policyMasterMbr.getFirstName());
			memberMasterEntity.setMemberStatus(policyMasterMbr.getMemberStatus());
			memberMasterEntity.setPolicyId(policyMasterMbr.getPolicyId());
			memberDto.add(memberMasterEntity);

		}
		policySearchResponseDto.setMember(memberDto);

		return policySearchResponseDto;

	}

	@Override
	public ApiResponseDto<List<PolicySearchMemberDto>> memberNewSearch(
			PolicyMemberSearchRequestDto claimMemberSearchRequestDto) {
		ApiResponseDto<List<PolicySearchMemberDto>> responseDto=new ApiResponseDto<>();
		logger.info("PolicyServiceImpl{} :: {} memberNewSearch {} :: {} start");
		try {
			List<Object> result=memberMasterRepository.findByMemberDetails(claimMemberSearchRequestDto.getMasterPolicyNo(),claimMemberSearchRequestDto.getLicId(),
					Boolean.TRUE,claimMemberSearchRequestDto.getUnitCode());
			List<PolicySearchMemberDto> reponseList=new ArrayList<>();
			if (result != null && result.size() > 0) {
				for (Object object : result) {
					PolicySearchMemberDto response=new PolicySearchMemberDto();
					Object[] ob = (Object[]) object;
					response.setLicId((ob[0]!=null)?ob[0].toString():null);
					response.setMemberShipId((ob[2]!=null)?ob[2].toString():null);
					response.setFirstName((ob[3]!=null)?ob[3].toString():null);
					response.setMemberStatus((ob[4]!=null)?ob[4].toString():null);
					response.setMemberId((ob[1]!=null)?NumericUtils.convertStringToLong(ob[1].toString()):null);
					reponseList.add(response);
				}
}
			else {
				responseDto= ApiResponseDto.error(ErrorDto.builder().message("No Data Found").build());
			}
			responseDto= ApiResponseDto.success(reponseList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PolicyServiceImpl{} :: {} memberNewSearch {} :: {} error is "+e.getMessage());
		}
		
		logger.info("PolicyServiceImpl{} :: {} memberNewSearch {} :: {} ended");
		return responseDto;
}

	@Override
	public ApiResponseDto<List<PolicySearchMemberDto>> getmemberListByPolicyNumber(Long policyId) {
		ApiResponseDto<List<PolicySearchMemberDto>> responseDto=new ApiResponseDto<>();
		logger.info("PolicyServiceImpl{} :: {} getmemberListByPolicyNumber {} :: {} start");
		try {
			List<Object> result=memberMasterRepository.findMemberListByPoicyId(policyId,Boolean.TRUE,ClaimConstants.POLICY_STATUS);
			List<PolicySearchMemberDto> reponseList=new ArrayList<>();
			if (result != null && result.size() > 0) {
				for (Object object : result) {
					PolicySearchMemberDto response=new PolicySearchMemberDto();
					Object[] ob = (Object[]) object;
					response.setLicId((ob[0]!=null)?ob[0].toString():null);
					response.setMemberId((ob[1]!=null)?NumericUtils.convertStringToLong(ob[1].toString()):null);
					response.setMemberShipId((ob[2]!=null)?ob[2].toString():null);
					response.setFirstName((ob[3]!=null)?ob[3].toString():null);
					response.setMemberStatus((ob[4]!=null)?ob[4].toString():null);
					reponseList.add(response);
				}
			
				responseDto= ApiResponseDto.success(reponseList);
}
			else {
				responseDto= ApiResponseDto.error(ErrorDto.builder().message("No Data Found").build());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PolicyServiceImpl{} :: {} getmemberListByPolicyNumber {} :: {} error is "+e.getMessage());
		}
		logger.info("PolicyServiceImpl{} :: {} getmemberListByPolicyNumber {} :: {} ended");
		return responseDto;
	}

	@Override
	public ApiResponseDto<List<PolicySearchResponseDto>> policycriteriaSearch(
			PolicySearchRequestDto claimMemberSearchRequestDto) {
				return null;

//		List<PolicySearchResponseDto> response = new ArrayList<>();
//		try {
//			logger.info("PolicyServiceImpl:policycriteriaSearch:Start");
//
//			String policyNumber = "";
//			Boolean isActive = Boolean.TRUE;
//
//			if (StringUtils.isNotBlank(claimMemberSearchRequestDto.getMasterPolicyNo())) {
//				policyNumber = claimMemberSearchRequestDto.getMasterPolicyNo();
//			}
//
//			List<Object> result = policyMasterRepository.policycriteriaSearch(policyNumber, isActive,claimMemberSearchRequestDto.getUnitCode());
//			if (result != null && !result.isEmpty()) {
//				for (Object object : result) {
//					Object[] ob = (Object[]) object;
//					PolicySearchResponseDto resonseDto = new PolicySearchResponseDto();
//					resonseDto.setMphCode(String.valueOf(ob[1]));
//					resonseDto.setMphName(String.valueOf(ob[2]));
//					resonseDto.setPolicyId(String.valueOf(ob[3]));
//					resonseDto.setPolicyNo(String.valueOf(ob[4]));
//					resonseDto.setPolicyStatus(String.valueOf(ob[5]));
//					resonseDto.setUnitCode(String.valueOf(ob[6]));
//					response.add(resonseDto);
//				}
//			}
//			return ApiResponseDto.success(response);
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception:PolicyServiceImpl:policycriteriaSearch", e);
//			return ApiResponseDto.error(ErrorDto.builder().message(e.getMessage()).build());
//		} finally {
//			logger.info("PolicyServiceImpl:policycriteriaSearch:Ends");
//		}		
	}
}
