package com.lic.epgs.payout.temp.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.payout.constants.PayoutEntityConstants;
import com.lic.epgs.payout.constants.PayoutErrorConstants;
import com.lic.epgs.payout.dto.PayoutMbrNomineeDto;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrNomineeEntity;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrNomineeRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutRepository;
import com.lic.epgs.payout.temp.service.TempPayoutMbrNomineeService;
import com.lic.epgs.payout.temp.service.TempSavePayoutService;

@Service
@Transactional
public class TempPayoutMbrNomineeServiceImpl implements TempPayoutMbrNomineeService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TempPayoutMbrNomineeRepository tempPayoutMbrNomineeRepository;

	@Autowired
	TempPayoutRepository tempPayoutRepository;

	@Autowired
	TempSavePayoutService tempSaveTempPayoutService;

	@Autowired
	ModelMapper modelMapper;

	private Specification<TempPayoutMbrNomineeEntity> findNomineeDetailsByPayoutNo(String payoutNo) {
		return (root, query, criteriaBuilder) -> {
			Join<TempPayoutMbrNomineeEntity, TempPayoutMbrEntity> payoutMbr = root
					.join(PayoutEntityConstants.PAYOUT_MBR_ENTITY);
			Join<TempPayoutMbrEntity, TempPayoutEntity> payout = payoutMbr.join(PayoutEntityConstants.PAYOUT);
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.PAYOUT_NO), payoutNo));
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(payout.get(PayoutEntityConstants.PAYOUT_NO), payoutNo);
		};
	}

	private Specification<TempPayoutMbrNomineeEntity> findNomineeDetailsByPayoutNoAndNomineeId(String payoutNo,
			Long nomineeId) {
		return (root, query, criteriaBuilder) -> {
			Join<TempPayoutMbrNomineeEntity, TempPayoutMbrEntity> payoutMbr = root
					.join(PayoutEntityConstants.PAYOUT_MBR_ENTITY);
			Join<TempPayoutMbrEntity, TempPayoutEntity> payout = payoutMbr.join(PayoutEntityConstants.PAYOUT);
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.PAYOUT_NO), payoutNo));
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(root.get(PayoutEntityConstants.NOMINEE_ID), nomineeId);
		};
	}

	@Override
	public ApiResponseDto<PayoutMbrNomineeDto> save(PayoutMbrNomineeDto request) {
		ApiResponseDto<PayoutMbrNomineeDto> responseDto=new ApiResponseDto<>();
		logger.info(" TempPayoutMbrAppointeeServiceImpl{}::--{}:: save {}:: start");
		try {
			if (request.getNomineeId() != null && request.getNomineeId() > 0) {
				Specification<TempPayoutMbrNomineeEntity> specification = findNomineeDetailsByPayoutNoAndNomineeId(
						request.getPayoutNo(), request.getNomineeId());

				List<TempPayoutMbrNomineeEntity> result = tempPayoutMbrNomineeRepository.findAll(specification);
				if (!result.isEmpty() && result.get(0) != null) {
					TempPayoutMbrNomineeEntity entity = updatePayoutMbrNominee(request, result.get(0));
					entity = tempSaveTempPayoutService.insertPayoutMbrNominee(request.getPayoutNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), PayoutErrorConstants.NOMINEE_SAVE_SUCCESS);
				} else {
					responseDto= ApiResponseDto
							.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_NOMINEE_ID).build());
				}
			} else {
				Optional<TempPayoutEntity> payoutEntity = tempPayoutRepository
						.findByPayoutNoAndIsActive(request.getPayoutNo(), Boolean.TRUE);
				if (payoutEntity.isPresent()) {
					TempPayoutMbrNomineeEntity entity = insertPayoutMbrNominee(request, payoutEntity.get());
					entity = tempSaveTempPayoutService.insertPayoutMbrNominee(request.getPayoutNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), PayoutErrorConstants.NOMINEE_SAVE_SUCCESS);
				} else {
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempPayoutMbrAppointeeServiceImpl{}::--{}:: save {}:: error is "+e.getMessage());
		}
		logger.info(" TempPayoutMbrAppointeeServiceImpl{}::--{}:: save {}:: ended");
		return responseDto;
	}

	private TempPayoutMbrNomineeEntity insertPayoutMbrNominee(PayoutMbrNomineeDto request,
			TempPayoutEntity tempPayoutEntity) {
		TempPayoutMbrEntity payoutMbr = tempPayoutEntity.getPayoutMbr();
		TempPayoutMbrNomineeEntity entity = new TempPayoutMbrNomineeEntity();
		convertDtoToEntity(entity, request);
		entity.setPayoutMbrEntity(payoutMbr);
		return entity;
	}

	private TempPayoutMbrNomineeEntity updatePayoutMbrNominee(PayoutMbrNomineeDto request,
			TempPayoutMbrNomineeEntity tempPayoutMbrNomineeEntity) {
		TempPayoutMbrNomineeEntity entity = new TempPayoutMbrNomineeEntity();
		BeanUtils.copyProperties(tempPayoutMbrNomineeEntity, entity);
		convertDtoToEntity(entity, request);
		return entity;
	}

	private void convertDtoToEntity(TempPayoutMbrNomineeEntity tempPayoutMbrNomineeEntity,
			PayoutMbrNomineeDto payoutMbrNomineeDto) {

		tempPayoutMbrNomineeEntity.setNomineeCode(tempPayoutMbrNomineeEntity.getNomineeCode());

		tempPayoutMbrNomineeEntity.setDob(CommonDateUtils.convertStringToDate(payoutMbrNomineeDto.getDob()));
		tempPayoutMbrNomineeEntity.setModifiedOn(CommonDateUtils.sysDate());
		tempPayoutMbrNomineeEntity.setModifiedBy(payoutMbrNomineeDto.getCreatedBy());

		tempPayoutMbrNomineeEntity.setAadharNumber(payoutMbrNomineeDto.getAadharNumber());
		tempPayoutMbrNomineeEntity.setAccountNumber(payoutMbrNomineeDto.getAccountNumber());
		tempPayoutMbrNomineeEntity.setAccountType(payoutMbrNomineeDto.getAccountType());
		tempPayoutMbrNomineeEntity.setAddressOne(payoutMbrNomineeDto.getAddressOne());
		tempPayoutMbrNomineeEntity.setAddressThree(payoutMbrNomineeDto.getAddressThree());
		tempPayoutMbrNomineeEntity.setAddressTwo(payoutMbrNomineeDto.getAddressTwo());
		tempPayoutMbrNomineeEntity.setBankName(payoutMbrNomineeDto.getBankName());
		tempPayoutMbrNomineeEntity.setCountry(payoutMbrNomineeDto.getCountry());
		tempPayoutMbrNomineeEntity.setDistrict(payoutMbrNomineeDto.getDistrict());
		tempPayoutMbrNomineeEntity.setEmailId(payoutMbrNomineeDto.getEmailId());
		tempPayoutMbrNomineeEntity.setFirstName(payoutMbrNomineeDto.getFirstName());
		tempPayoutMbrNomineeEntity.setIfscCode(payoutMbrNomineeDto.getIfscCode());
		tempPayoutMbrNomineeEntity.setLastName(payoutMbrNomineeDto.getLastName());
		tempPayoutMbrNomineeEntity.setMaritalStatus(payoutMbrNomineeDto.getMaritalStatus());
		tempPayoutMbrNomineeEntity.setMiddleName(payoutMbrNomineeDto.getMiddleName());
		tempPayoutMbrNomineeEntity.setMobileNo(payoutMbrNomineeDto.getMobileNo());
		tempPayoutMbrNomineeEntity.setPincode(payoutMbrNomineeDto.getPincode());
		tempPayoutMbrNomineeEntity.setRelationShip(payoutMbrNomineeDto.getRelationShip());
		tempPayoutMbrNomineeEntity.setSharedAmount(payoutMbrNomineeDto.getSharedAmount());
		tempPayoutMbrNomineeEntity.setSharedPercentage(payoutMbrNomineeDto.getSharedPercentage());
		tempPayoutMbrNomineeEntity.setState(payoutMbrNomineeDto.getState());
	}

	@Override
	public ApiResponseDto<List<PayoutMbrNomineeDto>> findNomineeByPayoutNo(String payoutNo) {

		Specification<TempPayoutMbrNomineeEntity> specification = findNomineeDetailsByPayoutNo(payoutNo);

		List<TempPayoutMbrNomineeEntity> result = tempPayoutMbrNomineeRepository.findAll(specification);

		if (!result.isEmpty()) {
			List<PayoutMbrNomineeDto> response = result.stream().map(this::convertEntityToDto)
					.collect(Collectors.toList());
			return ApiResponseDto.success(response);
		}

		return ApiResponseDto.success(Collections.emptyList());
	}

	private PayoutMbrNomineeDto convertEntityToDto(TempPayoutMbrNomineeEntity entity) {
		ModelMapper dtoModelMapper = new ModelMapper();
		dtoModelMapper.addMappings(new PropertyMap<TempPayoutMbrNomineeEntity, PayoutMbrNomineeDto>() {
			@Override
			protected void configure() {
				skip(destination.getDob());
			}
		});
		PayoutMbrNomineeDto dto = dtoModelMapper.map(entity, PayoutMbrNomineeDto.class);
		dto.setDob(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(entity.getDob()));
		return dto;
	}
}
