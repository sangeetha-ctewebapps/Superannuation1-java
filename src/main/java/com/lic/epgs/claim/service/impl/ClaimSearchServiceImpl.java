package com.lic.epgs.claim.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.claim.constants.ClaimStatus;
import com.lic.epgs.claim.dto.ClaimSearchRequestDto;
import com.lic.epgs.claim.dto.ClaimSearchResponseDto;
import com.lic.epgs.claim.entity.ClaimEntity;
import com.lic.epgs.claim.entity.ClaimMbrEntity;
import com.lic.epgs.claim.entity.ClaimOnboardingEntity;
import com.lic.epgs.claim.repository.ClaimRepository;
import com.lic.epgs.claim.service.ClaimSearchService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.utils.CommonDateUtils;

@Service
@Transactional
public class ClaimSearchServiceImpl implements ClaimSearchService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	ClaimRepository claimRepository;

	@Autowired
	private EntityManager entityManager;

//	@Override
//	public ApiResponseDto<ClaimSearchResponseDto> search(String claimNo) {
//		try {
//
//			logger.info("ClaimSearchServiceImpl:search:Start");
//			Optional<ClaimEntity> result = claimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
//			if (result.isPresent() && result.get() != null) {
//				return ApiResponseDto.success(convertEntityToDto(result.get()));
//			}
//			return ApiResponseDto
//					.error(ErrorDto.builder().message(String.format("Claim not found for %s ", claimNo)).build());
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception:ClaimSearchServiceImpl:search", e);
//			return ApiResponseDto.error(ErrorDto.builder().message("Error fetching claim notes").build());
//		} finally {
//			logger.info("ClaimSearchServiceImpl:search:Ends");
//		}
//	}

	@Override
	public ApiResponseDto<List<ClaimSearchResponseDto>> search(ClaimSearchRequestDto request) {
		
		ApiResponseDto<List<ClaimSearchResponseDto>> responseDto=new ApiResponseDto<>();
		logger.info("ClaimSearchServiceImpl:search:Start");
		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

			CriteriaQuery<ClaimEntity> searchQuery = criteriaBuilder.createQuery(ClaimEntity.class);
			Root<ClaimEntity> root = searchQuery.from(ClaimEntity.class);
			Join<ClaimEntity, ClaimMbrEntity> join = root.join("claimMbr");
			
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(request.getMasterPolicyNo())) {
				predicates.add(criteriaBuilder.equal(root.get("masterPolicyNo"), request.getMasterPolicyNo()));
			}
			if (StringUtils.isNotBlank(request.getMph())) {
				predicates.add(criteriaBuilder.equal(root.get("mphName"), request.getMph()));
			}
			if (StringUtils.isNotBlank(request.getPan())) {
				predicates.add(criteriaBuilder.equal(join.get("pan"), request.getPan()));
			}
			if (StringUtils.isNotBlank(request.getAadhar())) {
				predicates.add(criteriaBuilder.equal(join.get("aadharNumber"), request.getAadhar()));
			}
			if (StringUtils.isNotBlank(request.getContactNumber())) {
				predicates.add(criteriaBuilder.equal(join.get("phone"), request.getContactNumber()));
			}
			if (StringUtils.isNotBlank(request.getClaimNo())) {
				predicates.add(criteriaBuilder.equal(root.get("claimNo"), request.getClaimNo()));
			}
				predicates.add(criteriaBuilder.equal(root.get("unitCode"), request.getUnitCode()));
			if (StringUtils.isNotBlank(request.getOnboardingNo())) {
				Join<ClaimEntity, ClaimOnboardingEntity> onboardJoin = root.join("claimOnboarding");
				predicates.add(criteriaBuilder.equal(onboardJoin.get("claimOnBoardNo"), request.getOnboardingNo()));
			}
			
			if (StringUtils.isNotBlank(request.getClaimIntimationNo())) {
				Join<ClaimEntity, ClaimOnboardingEntity> onboardJoin = root.join("claimOnboarding");
				predicates.add(criteriaBuilder.like(onboardJoin.get("initiMationNo"),"%"+request.getClaimIntimationNo()));
			}
			
			if (StringUtils.isNotBlank(request.getOnboardingFrom()) && StringUtils.isNotBlank(request.getOnboardingTo())) {
				Date fromDate = CommonDateUtils.convertStringToDate(request.getOnboardingFrom());
				Date toDate = CommonDateUtils.convertStringToDate(request.getOnboardingTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get("createdOn"), fromDate, toDate));
			}

			if (request.getClaimStatus() != null && !request.getClaimStatus().isEmpty()) {
				Expression<String> statusExpression = root.get("status");
				predicates.add(statusExpression.in(request.getClaimStatus()));
			}
			searchQuery.orderBy(criteriaBuilder.desc(root.get("createdOn")));
			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));

			List<ClaimEntity> result = entityManager.createQuery(searchQuery).getResultList();
			
			if(result.isEmpty()) {
				logger.info("No Data Found For given Request");
				responseDto= ApiResponseDto.error(ErrorDto.builder().message("No Data Found").build());
			}
			List<ClaimSearchResponseDto> response = result.stream().map(this::convertEntityToDto)
					.collect(Collectors.toList());
			responseDto= ApiResponseDto.success(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Exception ClaimSearchServiceImpl  :  error is  "+e.getMessage());
		}
		logger.info("ClaimSearchServiceImpl {} :: {} search{} :: Ends");
		return responseDto;
	}

	private ClaimSearchResponseDto convertEntityToDto(ClaimEntity claimsEntity) {
		ClaimSearchResponseDto response = new ClaimSearchResponseDto();
		logger.info("ClaimSearchServiceImpl::{}  {}::     convertEntityToDto    {} :: start");
		try {
			if (claimsEntity != null) {
				response.setClaimNo(claimsEntity.getClaimNo());
				response.setClaimStatus(claimsEntity.getStatus());
				response.setMasterPolicyNo(claimsEntity.getMasterPolicyNo());
				response.setMphCode(claimsEntity.getMphCode());
				response.setMphName(claimsEntity.getMphName());
				
				response.setOnboardingNo(claimsEntity.getClaimOnboarding().getClaimOnBoardNo());
				response.setClaimIntimaitonNo(claimsEntity.getClaimOnboarding().getInitiMationNo());
				ClaimMbrEntity claimMbr = claimsEntity.getClaimMbr();
				if (claimMbr != null) {
					response.setAadhar(claimMbr.getAadharNumber());
					response.setFirstName(claimMbr.getFirstName());
					response.setLastName(claimMbr.getLastName());
					response.setMemberShipNo(claimMbr.getMembershipNumber());
					response.setPan(claimMbr.getPan());
					response.setLicId(claimMbr.getLicId());
				}
			}
			logger.info("ClaimSearchServiceImpl::{}  {}::     convertEntityToDto    {} :: Ended");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Exception ClaimSearchServiceImpl  :  error is  "+e.getMessage());
		}

		return response;
	}

	@Override
	public ApiResponseDto<List<ClaimSearchResponseDto>> existing(ClaimSearchRequestDto request) {
		request.setClaimStatus(Arrays.asList(ClaimStatus.APPROVE.val(), ClaimStatus.REJECT.val()));
		return search(request);
	}

	@Override
	public ApiResponseDto<List<ClaimSearchResponseDto>> existingByClaimNo(String claimNo) {
		ClaimSearchRequestDto request = new ClaimSearchRequestDto();
		request.setClaimStatus(Arrays.asList(ClaimStatus.APPROVE.val(), ClaimStatus.REJECT.val()));
		request.setClaimNo(claimNo);
		return search(request);
	}

	public List<ClaimEntity> claimSearch(ClaimSearchRequestDto request) {
		List<ClaimEntity> responce =new ArrayList<>();
		logger.info("ClaimSearchServiceImpl::{}  {}::     claimSearch    {} :: start");
		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			List<Predicate> predicates = new ArrayList<>();

			CriteriaQuery<ClaimEntity> searchQuery = criteriaBuilder.createQuery(ClaimEntity.class);
			Root<ClaimEntity> root = searchQuery.from(ClaimEntity.class);

			Join<ClaimEntity, ClaimMbrEntity> join = root.join("claimMbr");

			if (StringUtils.isNotBlank(request.getMembershipNumber())) {
				join.on(criteriaBuilder.equal(join.get("membershipNumber"), request.getMembershipNumber()));
			}

			if (StringUtils.isNotBlank(request.getAadharNumber())) {
				join.on(criteriaBuilder.equal(join.get("aadharNumber"), request.getAadharNumber()));

			}
			if (StringUtils.isNotBlank(request.getPan())) {
				join.on(criteriaBuilder.equal(join.get("pan"), request.getPan()));
			}

			if (StringUtils.isNotBlank(request.getClaimNo())) {
				predicates.add(criteriaBuilder.equal(root.get("claimNo"), request.getClaimNo()));
			}

			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			predicates.add(criteriaBuilder.equal(root.get("status"), request.getClaimStatus()));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));

			responce= entityManager.createQuery(searchQuery).getResultList();
			logger.info("ClaimSearchServiceImpl::{}  {}::     claimSearch    {} :: ended");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Exception ClaimSearchServiceImpl{}::  error is {} ::"+e.getMessage());
		}
		
		
		return responce;
	}

	@Override
	public ApiResponseDto<List<ClaimSearchResponseDto>> payoutByClaimNo(ClaimSearchRequestDto request) {
		ApiResponseDto<List<ClaimSearchResponseDto>> responseDto=new ApiResponseDto<>();
		logger.info("ClaimSearchServiceImpl{}:: payoutByClaimNo   {}::Start");
		try {
			request.setClaimStatus(Arrays.asList(ClaimStatus.APPROVE.val()));

			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

			CriteriaQuery<ClaimEntity> searchQuery = criteriaBuilder.createQuery(ClaimEntity.class);
			Root<ClaimEntity> root = searchQuery.from(ClaimEntity.class);
			Join<ClaimEntity, ClaimMbrEntity> join = root.join("claimMbr");
			if (StringUtils.isNotBlank(request.getMasterPolicyNo())) {
				join.on(criteriaBuilder.equal(root.get("masterPolicyNo"), request.getMasterPolicyNo()));
			}
			if (StringUtils.isNotBlank(request.getMph())) {
				join.on(criteriaBuilder.equal(root.get("mphName"), request.getMph()));
			}
			if (StringUtils.isNotBlank(request.getPan())) {
				join.on(criteriaBuilder.equal(join.get("pan"), request.getPan()));
			}
			if (StringUtils.isNotBlank(request.getContactNumber())) {
				join.on(criteriaBuilder.equal(join.get("phone"), request.getContactNumber()));
			}
			if (StringUtils.isNotBlank(request.getAadhar())) {
				join.on(criteriaBuilder.equal(join.get("aadharNumber"), request.getAadhar()));
			}
			if (StringUtils.isNotBlank(request.getClaimNo())) {
				join.on(criteriaBuilder.equal(root.get("claimNo"), request.getClaimNo()));
			}
			if (StringUtils.isNotBlank(request.getUnitCode())) {
				join.on(criteriaBuilder.equal(root.get("unitCode"), request.getUnitCode()));
			}
			if (StringUtils.isNotBlank(request.getOnboardingNo())) {
				Join<ClaimEntity, ClaimOnboardingEntity> onboardJoin = root.join("claimOnboarding");
				onboardJoin.on(criteriaBuilder.equal(onboardJoin.get("claimOnBoardNo"), request.getOnboardingNo()));
			}
			if (StringUtils.isNotBlank(request.getClaimIntimationNo())) {
				Join<ClaimEntity, ClaimOnboardingEntity> onboardJoin = root.join("claimOnboarding");
				onboardJoin.on(criteriaBuilder.like(onboardJoin.get("initiMationNo"),"%"+request.getClaimIntimationNo()));
			}
			List<Predicate> predicates = new ArrayList<>();

			if (StringUtils.isNotBlank(request.getOnboardingFrom()) && StringUtils.isNotBlank(request.getOnboardingTo())) {
				Date fromDate = CommonDateUtils.convertStringToDate(request.getOnboardingFrom());
				Date toDate = CommonDateUtils.convertStringToDate(request.getOnboardingTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get("createdOn"), fromDate, toDate));
			}

			if (request.getClaimStatus() != null && !request.getClaimStatus().isEmpty()) {
				Path<String> statusExpression = root.get("status");
				predicates.add(statusExpression.in(request.getClaimStatus()));
			
			}

//		Root<TempPayoutEntity> rootP = searchQuery.from(TempPayoutEntity.class);
//		predicates.add(criteriaBuilder.in(root.get("claimNo")).value((rootP.get("claimNo"))).not());

			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));

			List<ClaimEntity> searchResponse = entityManager.createQuery(searchQuery).getResultList();
			logger.info("ClaimSearchServiceImpl:search:Ends");
			if(searchResponse.isEmpty()) {
				responseDto= ApiResponseDto.error(ErrorDto.builder().message("No Record Found").build());
			}
			List<ClaimSearchResponseDto> response = null;
			if (!searchResponse.isEmpty()) {
				response = searchResponse.stream().map(this::convertEntityToDto).collect(Collectors.toList());
			}
			
			responseDto= ApiResponseDto.success(response);
			logger.info("ClaimSearchServiceImpl{}:: payoutByClaimNo   {}::Start");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Exception ClaimSearchServiceImpl{}::  error is {} ::"+e.getMessage());
		}
		
		return responseDto;

	}
}
