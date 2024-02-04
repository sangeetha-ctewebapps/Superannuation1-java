package com.lic.epgs.payout.temp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.payout.constants.PayoutStatus;
import com.lic.epgs.payout.dto.PayoutSearchRequestDto;
import com.lic.epgs.payout.dto.PayoutSearchResponseDto;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;
import com.lic.epgs.payout.temp.repository.TempPayoutRepository;
import com.lic.epgs.payout.temp.service.TempPayoutSearchService;

@Service
@Transactional
public class TempPayoutSearchServiceImpl implements TempPayoutSearchService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TempPayoutRepository tempPayoutRepository;

	@Autowired
	private EntityManager entityManager;

	@Override
	public ApiResponseDto<PayoutSearchResponseDto> search(String payoutNo) {
		try {

			logger.info("PayoutSearchServiceImpl:search:Start");
			Optional<TempPayoutEntity> result = tempPayoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
			if (result.isPresent() && result.get() != null) {
				return ApiResponseDto.success(convertEntityToDto(result.get()));
			}
			return ApiResponseDto
					.error(ErrorDto.builder().message(String.format("Payout not found for %s ", payoutNo)).build());
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PayoutSearchServiceImpl:search", e);
			return ApiResponseDto.error(ErrorDto.builder().message("Error fetching payout notes").build());
		} finally {
			logger.info("PayoutSearchServiceImpl:search:Ends");
		}
	}

	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> search(PayoutSearchRequestDto request) {
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto=new ApiResponseDto<>();
		logger.info("PayoutSearchServiceImpl {} :: {}    search {} ::{}Start");
		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

			CriteriaQuery<TempPayoutEntity> searchQuery = criteriaBuilder.createQuery(TempPayoutEntity.class);
			Root<TempPayoutEntity> root = searchQuery.from(TempPayoutEntity.class);
			Join<TempPayoutEntity, TempPayoutMbrEntity> join = root.join("payoutMbr");
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

			if (StringUtils.isNotBlank(request.getPayoutNo())) {
				predicates.add(criteriaBuilder.equal(root.get("payoutNo"), request.getPayoutNo()));
			}
			if (StringUtils.isNotBlank(request.getClaimNo())) {
				
				predicates.add(criteriaBuilder.equal(root.get("claimNo"), request.getClaimNo()));
			}
			
			if (StringUtils.isNotBlank(request.getInitiMationNo())) {
				predicates.add(criteriaBuilder.like(root.get("initiMationNo"),"%"+request.getInitiMationNo()));
			}
			
			if (StringUtils.isNotBlank(request.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get("unitCode"), request.getUnitCode()));
			}



			if (request.getPayoutStatus() != null && !request.getPayoutStatus().isEmpty()) {
				Expression<String> statusExpression = root.get("status");
				predicates.add(statusExpression.in(request.getPayoutStatus()));
			}

			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));

			List<TempPayoutEntity> result = entityManager.createQuery(searchQuery).getResultList();
			if(result.isEmpty()) {
				logger.info("No Data Found For given Request");
				responseDto= ApiResponseDto.error(ErrorDto.builder().message("No Data Found").build());
			}
		
			List<PayoutSearchResponseDto> response = result.stream().map(this::convertEntityToDto)
					.collect(Collectors.toList());
			responseDto= ApiResponseDto.success(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutSearchServiceImpl{}::{}search {} ::{} error is "+e.getMessage());
		}
		logger.info("PayoutSearchServiceImpl{}::{}search {} ::{} Ends");
		return responseDto;
	}

	private PayoutSearchResponseDto convertEntityToDto(TempPayoutEntity tempPayoutsEntity) {

		PayoutSearchResponseDto response = new PayoutSearchResponseDto();
		if (tempPayoutsEntity != null) {
			response.setPayoutNo(tempPayoutsEntity.getPayoutNo());
			response.setInitiMationNo(tempPayoutsEntity.getInitiMationNo());
			response.setPayoutStatus(tempPayoutsEntity.getStatus());
			response.setMasterPolicyNo(tempPayoutsEntity.getMasterPolicyNo());
			response.setMphCode(tempPayoutsEntity.getMphCode());
			response.setMphName(tempPayoutsEntity.getMphName());
			response.setClaimNo(tempPayoutsEntity.getClaimNo());
			TempPayoutMbrEntity payoutMbr = tempPayoutsEntity.getPayoutMbr();
			if (payoutMbr != null) {
				response.setAadhar(payoutMbr.getAadharNumber());
				response.setFirstName(payoutMbr.getFirstName());
				response.setLastName(payoutMbr.getLastName());
				response.setMemberShipNo(payoutMbr.getTypeOfMembershipNo());
				response.setPan(payoutMbr.getPan());
				response.setLicId(payoutMbr.getLicId());
			}
		}
		return response;
	}

	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> findByStatusAndPayoutNo(Integer status, String payoutNo) {
		PayoutSearchRequestDto request = new PayoutSearchRequestDto();
		request.setPayoutStatus(Arrays.asList(status));
		request.setPayoutNo(payoutNo);
		return search(request);
	}

	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> inprogress(PayoutSearchRequestDto request) {
		request.setPayoutStatus(
				Arrays.asList(PayoutStatus.ONBORADED.val(), PayoutStatus.PRELIMINARY_REQUIREMENT_REVIEW.val(),
						PayoutStatus.CLOSE_WITHOUT_SETTLEMENT_PAYOUT_WRONGLY_BOOKED.val(),
						PayoutStatus.SEND_CHECKER.val(), PayoutStatus.REPUDIATE.val(),
						PayoutStatus.SEND_TO_MAKER.val(), PayoutStatus.CLOSE_WITHOUT_SETTLEMENT_OTHERS.val()));
		return search(request);
	}

	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> inprogressByPayoutNo(String payoutNo) {
		PayoutSearchRequestDto request = new PayoutSearchRequestDto();
		request.setPayoutStatus(
				Arrays.asList(PayoutStatus.ONBORADED.val(), PayoutStatus.PRELIMINARY_REQUIREMENT_REVIEW.val(),
						PayoutStatus.CLOSE_WITHOUT_SETTLEMENT_PAYOUT_WRONGLY_BOOKED.val(),
						PayoutStatus.SEND_CHECKER.val(), PayoutStatus.REPUDIATE.val(),
						PayoutStatus.SEND_TO_MAKER.val(), PayoutStatus.CLOSE_WITHOUT_SETTLEMENT_OTHERS.val()));
		request.setPayoutNo(payoutNo);
		return search(request);
	}

}
