package com.lic.epgs.payout.service.impl;

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
import com.lic.epgs.payout.constants.PayoutConstants;
import com.lic.epgs.payout.constants.PayoutStatus;
import com.lic.epgs.payout.dto.PayoutSearchRequestDto;
import com.lic.epgs.payout.dto.PayoutSearchResponseDto;
import com.lic.epgs.payout.entity.PayoutEntity;
import com.lic.epgs.payout.entity.PayoutMbrEntity;
import com.lic.epgs.payout.entity.StoredProcedureResponseEntity;
import com.lic.epgs.payout.repository.PayoutRepository;
import com.lic.epgs.payout.repository.StoredProcedureResponseRepository;
import com.lic.epgs.payout.service.PayoutSearchService;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

@Service
@Transactional
public class PayoutSearchServiceImpl implements PayoutSearchService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	PayoutRepository payoutRepository;

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	StoredProcedureResponseRepository storedProcedureResponseRepository;

	@Override
	public ApiResponseDto<PayoutSearchResponseDto> search(String payoutNo) {
		try {

			logger.info("PayoutSearchServiceImpl:search:Start");
			Optional<PayoutEntity> result = payoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
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
		try {
			logger.info("PayoutSearchServiceImpl:search:Start");
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			List<Predicate> predicates = new ArrayList<>();

			CriteriaQuery<PayoutEntity> searchQuery = criteriaBuilder.createQuery(PayoutEntity.class);
			Root<PayoutEntity> root = searchQuery.from(PayoutEntity.class);
			Join<PayoutEntity, PayoutMbrEntity> join = root.join("payoutMbr");
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
				predicates.add(criteriaBuilder.like(root.get("payoutNo"),"%"+request.getPayoutNo()));
			}
			if (StringUtils.isNotBlank(request.getClaimNo())) {
				predicates.add(criteriaBuilder.equal(root.get("claimNo"), request.getClaimNo()));
			}
			if (StringUtils.isNotBlank(request.getMembershipNumber())) {
				predicates.add(criteriaBuilder.equal(join.get("membershipNumber"), request.getMembershipNumber()));
			}
			if (StringUtils.isNotBlank(request.getLicId())) {
				predicates.add(criteriaBuilder.equal(join.get("licId"), request.getLicId()));
			}
			if (StringUtils.isNotBlank(request.getDateOfBirth())) {
				predicates.add(criteriaBuilder.equal(join.get("dateOfBirth"), request.getDateOfBirth()));
			}
			if (StringUtils.isNotBlank(request.getPhone())) {
				predicates.add(criteriaBuilder.equal(join.get("phone"), request.getPhone()));
			}
			
			predicates.add(criteriaBuilder.equal(root.get("unitCode"), request.getUnitCode()));


			if (request.getPayoutStatus() != null && !request.getPayoutStatus().isEmpty()) {
				Expression<String> statusExpression = root.get("status");
				predicates.add(statusExpression.in(request.getPayoutStatus()));
			}

			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));

			List<PayoutEntity> result = entityManager.createQuery(searchQuery).getResultList();
			if(result.isEmpty()) {
				logger.info("No Data Found For given Request");
				responseDto= ApiResponseDto.error(ErrorDto.builder().message("No Data Found").build());
			}
			logger.info("PayoutSearchServiceImpl:search:Ends");
			List<PayoutSearchResponseDto> response = result.stream().map(this::convertEntityToDto)
					.collect(Collectors.toList());
			responseDto= ApiResponseDto.success(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutSearchServiceImpl:search: error is "+e.getMessage());
		}
		logger.info("PayoutSearchServiceImpl:search:Ends");
		return responseDto;
	}

	private PayoutSearchResponseDto convertEntityToDto(PayoutEntity payoutsEntity) {
		PayoutSearchResponseDto response = new PayoutSearchResponseDto();
		if (payoutsEntity != null) {
			response.setPayoutNo(payoutsEntity.getPayoutNo());
			response.setPayoutStatus(payoutsEntity.getStatus());
			response.setMasterPolicyNo(payoutsEntity.getMasterPolicyNo());
			response.setMphCode(payoutsEntity.getMphCode());
			response.setInitiMationNo(payoutsEntity.getInitiMationNo());
			response.setMphName(payoutsEntity.getMphName());
			response.setClaimNo(payoutsEntity.getClaimNo());
			response.setDtOfExit(DateUtils.dateToStringDDMMYYYY(payoutsEntity.getDtOfExit()));
			response.setModeOfExit(NumericUtils.convertIntegerToString(payoutsEntity.getModeOfExit()));
			response.setUnitCode(payoutsEntity.getUnitCode());
			PayoutMbrEntity payoutMbr = payoutsEntity.getPayoutMbr();
			if (payoutMbr != null) {
				response.setAadhar(payoutMbr.getAadharNumber());
				response.setFirstName(payoutMbr.getFirstName());
				response.setLastName(payoutMbr.getLastName());
				response.setMemberShipNo(payoutMbr.getTypeOfMembershipNo());
				response.setPan(payoutMbr.getPan());
				response.setLicId(payoutMbr.getLicId());
			}
//			StoredProcedureResponseEntity result=storedProcedureResponseRepository.findByUTRNoAndPaymentStatus(payoutsEntity.getPayoutNo(),PayoutConstants.PAYOUT_SP_STATUS);
//			if (result != null ) {
//				response.setUtrNo(result.getUtrNo());
//				response.setPaymentStatus((result.getProcessedStatus()));
//			}
		}

		return response;
	}


	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> existing(PayoutSearchRequestDto request) {
		request.setPayoutStatus(Arrays.asList(PayoutStatus.APPROVE.val(), PayoutStatus.REJECT.val(),PayoutStatus.NEFT_APPROVED.val(),PayoutStatus.NEFT_APPROVED.val()));
		return search(request);
	}

	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> existingByPayoutNo(String payoutNo) {
		PayoutSearchRequestDto request = new PayoutSearchRequestDto();
		request.setPayoutStatus(Arrays.asList(PayoutStatus.APPROVE.val(), PayoutStatus.REJECT.val(),PayoutStatus.NEFT_APPROVED.val(),PayoutStatus.NEFT_APPROVED.val()));
		request.setPayoutNo(payoutNo);
		return search(request);
	}

	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> enquirySearch(Integer val, PayoutSearchRequestDto request) {
		return search(request);
	}
	
}
