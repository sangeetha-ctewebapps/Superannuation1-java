package com.lic.epgs.claim.temp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.claim.constants.ClaimStatus;
import com.lic.epgs.claim.dto.ClaimSearchRequestDto;
import com.lic.epgs.claim.dto.ClaimSearchResponseDto;
import com.lic.epgs.claim.temp.entity.TempClaimEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrEntity;
import com.lic.epgs.claim.temp.entity.TempClaimOnboardingEntity;
import com.lic.epgs.claim.temp.repository.TempClaimRepository;
import com.lic.epgs.claim.temp.service.TempClaimSearchService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.utils.CommonDateUtils;

@Service
@Transactional
public class TempClaimSearchServiceImpl implements TempClaimSearchService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TempClaimRepository tempClaimRepository;

	@Autowired
	private EntityManager entityManager;

	@Override
	public ApiResponseDto<ClaimSearchResponseDto> search(String claimNo) {
		try {
			logger.info("TempClaimSearchServiceImpl:search:Start");
			Optional<TempClaimEntity> result = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (result.isPresent() && result.get() != null) {
				return ApiResponseDto.success(convertEntityToDto(result.get()));
			}
			return ApiResponseDto
					.error(ErrorDto.builder().message(String.format("Claim not found for %s ", claimNo)).build());
		} catch (IllegalArgumentException e) {
			logger.error("Exception:ClaimSearchServiceImpl:search", e);
			return ApiResponseDto.error(ErrorDto.builder().message("Error fetching claim notes").build());
		} finally {
			logger.info("TempClaimSearchServiceImpl:search:Ends");
		}
	}

	@Override
	public ApiResponseDto<List<ClaimSearchResponseDto>> search(ClaimSearchRequestDto request) {
		ApiResponseDto<List<ClaimSearchResponseDto>> responseDto=new ApiResponseDto<>();
		try {
			logger.info("TempClaimSearchServiceImpl:search:Start");
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

			CriteriaQuery<TempClaimEntity> searchQuery = criteriaBuilder.createQuery(TempClaimEntity.class);
			Root<TempClaimEntity> root = searchQuery.from(TempClaimEntity.class);
			Join<TempClaimEntity, TempClaimMbrEntity> join = root.join("claimMbr");
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
			if (StringUtils.isNotBlank(request.getContactNumber())) {
				predicates.add(criteriaBuilder.equal(join.get("phone"), request.getContactNumber()));
			}
			if (StringUtils.isNotBlank(request.getAadhar())) {
				predicates.add(criteriaBuilder.equal(join.get("aadharNumber"), request.getAadhar()));
			}
			if (StringUtils.isNotBlank(request.getMembershipNumber())) {
				predicates.add(criteriaBuilder.like(join.get("memberShipId"), "%" + request.getMembershipNumber() + "%"));
			}
			if (StringUtils.isNotBlank(request.getContactNumber())) {
				predicates.add(criteriaBuilder.equal(join.get("phone"), request.getContactNumber()));
			}

			if (StringUtils.isNotBlank(request.getOnboardingNo())) {
				Join<TempClaimEntity, TempClaimOnboardingEntity> onboardJoin = root.join("claimOnboarding");
				predicates.add(criteriaBuilder.like(onboardJoin.get("claimOnBoardNo"), "%" + request.getOnboardingNo()));
			}

			if (StringUtils.isNotBlank(request.getClaimNo())) {
				predicates.add(criteriaBuilder.equal(root.get("claimNo"), request.getClaimNo()));
			}
			predicates.add(criteriaBuilder.equal(root.get("unitCode"), request.getUnitCode()));

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
			searchQuery.orderBy(criteriaBuilder.desc(root.get("modifiedOn")));
			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));

			List<TempClaimEntity> result = entityManager.createQuery(searchQuery).getResultList();
			if (result.isEmpty()) {
				logger.info("No Data Found For given Request");
				return ApiResponseDto.error(ErrorDto.builder().message("No Data Found").build());
			}
			
			List<ClaimSearchResponseDto> response = result.stream().map(this::convertEntityToDto)
					.collect(Collectors.toList());

			responseDto= ApiResponseDto.success(response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("TempClaimSearchServiceImpl:search:error is "+e.getMessage());
		}
		logger.info("TempClaimSearchServiceImpl:search:Ends");
		return responseDto;
	}

	private ClaimSearchResponseDto convertEntityToDto(TempClaimEntity tempClaimsEntity) {

		ClaimSearchResponseDto response = new ClaimSearchResponseDto();
		if (tempClaimsEntity != null) {
			response.setClaimNo(tempClaimsEntity.getClaimNo());
			response.setClaimStatus(tempClaimsEntity.getStatus());
			response.setMasterPolicyNo(tempClaimsEntity.getMasterPolicyNo());
			response.setMphCode(tempClaimsEntity.getMphCode());
			response.setMphName(tempClaimsEntity.getMphName());
			response.setOnboardingNo(tempClaimsEntity.getClaimOnboarding().getClaimOnBoardNo());
			response.setDateOfExit(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(tempClaimsEntity.getDtOfExit()));
			response.setModeOfExit(tempClaimsEntity.getModeOfExit());	
//			response.setAge(tempClaimsEntity.getClaimOnboarding().get);
			
			TempClaimMbrEntity claimMbr = tempClaimsEntity.getClaimMbr();
			if (claimMbr != null) {
				response.setAadhar(claimMbr.getAadharNumber());
				response.setFirstName(claimMbr.getFirstName());
				response.setLastName(claimMbr.getLastName());
				response.setMemberShipNo(claimMbr.getTypeOfMembershipNo());
				response.setPan(claimMbr.getPan());
				response.setLicId(claimMbr.getLicId());
				
			}
		}

		return response;
	}

	@Override
	public ApiResponseDto<List<ClaimSearchResponseDto>> findByStatusAndClaimNo(Integer status, String claimNo) {
		ClaimSearchRequestDto request = new ClaimSearchRequestDto();
		request.setClaimStatus(Arrays.asList(status));
		request.setClaimNo(claimNo);
		return search(request);
	}

	@Override
	public ApiResponseDto<List<ClaimSearchResponseDto>> inprogress(ClaimSearchRequestDto request) {
		logger.info("TempClaimSearchServiceImpl{}::{} inprogress{}::start");
		try {
			if(request.getClaimStatus().isEmpty()) {
				
			
			request.setClaimStatus(
					Arrays.asList(ClaimStatus.ONBORADED.val(), ClaimStatus.PRELIMINARY_REQUIREMENT_REVIEW.val(),
							ClaimStatus.CLOSE_WITHOUT_SETTLEMENT_CLAIM_WRONGLY_BOOKED.val(),
							ClaimStatus.SEND_CHECKER.val(), ClaimStatus.REPUDIATE.val(),
							ClaimStatus.SEND_TO_MAKER.val(), ClaimStatus.CLOSE_WITHOUT_SETTLEMENT_OTHERS.val()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempClaimSearchServiceImpl{}::{} inprogress{}::error is "+e.getMessage());
		}
		logger.info("TempClaimSearchServiceImpl{}::{} inprogress{}::ended");
		return search(request);
	}

	@Override
	public ApiResponseDto<List<ClaimSearchResponseDto>> inprogressByClaimNo(String claimNo) {
		ClaimSearchRequestDto request = new ClaimSearchRequestDto();
		logger.info("TempClaimSearchServiceImpl{}::{} inprogressByClaimNo{}::start");
		try {
			request.setClaimStatus(
					Arrays.asList(ClaimStatus.ONBORADED.val(), ClaimStatus.PRELIMINARY_REQUIREMENT_REVIEW.val(),
							ClaimStatus.CLOSE_WITHOUT_SETTLEMENT_CLAIM_WRONGLY_BOOKED.val(),
							ClaimStatus.SEND_CHECKER.val(), ClaimStatus.REPUDIATE.val(),
							ClaimStatus.SEND_TO_MAKER.val(), ClaimStatus.CLOSE_WITHOUT_SETTLEMENT_OTHERS.val()));
			request.setClaimNo(claimNo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempClaimSearchServiceImpl{}::{} inprogressByClaimNo{}::error is "+e.getMessage());
		}
		logger.info("TempClaimSearchServiceImpl{}::{} inprogressByClaimNo{}::ended");
		return search(request);
	}

}
