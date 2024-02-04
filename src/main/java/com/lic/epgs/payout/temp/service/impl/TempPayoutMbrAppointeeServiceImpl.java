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

import com.lic.epgs.payout.constants.PayoutEntityConstants;
import com.lic.epgs.payout.constants.PayoutErrorConstants;
import com.lic.epgs.payout.dto.PayoutMbrAppointeeDto;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrAppointeeEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrAppointeeRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutRepository;
import com.lic.epgs.payout.temp.service.TempPayoutMbrAppointeeService;
import com.lic.epgs.payout.temp.service.TempSavePayoutService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.utils.CommonDateUtils;

@Service
@Transactional
public class TempPayoutMbrAppointeeServiceImpl implements TempPayoutMbrAppointeeService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TempPayoutMbrAppointeeRepository tempPayoutMbrAppointeeRepository;

	@Autowired
	TempPayoutRepository tempPayoutRepository;

	@Autowired
	TempSavePayoutService tempSaveTempPayoutService;

	@Autowired
	ModelMapper modelMapper;

	private Specification<TempPayoutMbrAppointeeEntity> findAppointeeDetailsByPayoutNo(String payoutNo) {
		return (root, query, criteriaBuilder) -> {
			Join<TempPayoutMbrAppointeeEntity, TempPayoutMbrEntity> payoutMbr = root
					.join(PayoutEntityConstants.PAYOUT_MBR_ENTITY);
			Join<TempPayoutMbrEntity, TempPayoutEntity> payout = payoutMbr.join(PayoutEntityConstants.PAYOUT);
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.PAYOUT_NO), payoutNo));
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(payout.get(PayoutEntityConstants.PAYOUT_NO), payoutNo);
		};
	}

	private Specification<TempPayoutMbrAppointeeEntity> findAppointeeDetailsByPayoutNoAndAppointeeId(String payoutNo,
			Long appointeeId) {
		return (root, query, criteriaBuilder) -> {
			Join<TempPayoutMbrAppointeeEntity, TempPayoutMbrEntity> payoutMbr = root
					.join(PayoutEntityConstants.PAYOUT_MBR_ENTITY);
			Join<TempPayoutMbrEntity, TempPayoutEntity> payout = payoutMbr.join(PayoutEntityConstants.PAYOUT);
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.PAYOUT_NO), payoutNo));
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(root.get(PayoutEntityConstants.APPOINTEE_ID), appointeeId);
		};
	}

	@Override
	public ApiResponseDto<PayoutMbrAppointeeDto> save(PayoutMbrAppointeeDto request) {

		ApiResponseDto<PayoutMbrAppointeeDto> responseDto=new ApiResponseDto<>();
		logger.info(" TempPayoutMbrAppointeeServiceImpl{}::--{}:: save {}:: start");
		try {
			if (request.getAppointeeId() != null && request.getAppointeeId() > 0) {
				Specification<TempPayoutMbrAppointeeEntity> specification = findAppointeeDetailsByPayoutNoAndAppointeeId(
						request.getPayoutNo(), request.getAppointeeId());

				List<TempPayoutMbrAppointeeEntity> result = tempPayoutMbrAppointeeRepository.findAll(specification);
				if (!result.isEmpty() && result.get(0) != null) {
					TempPayoutMbrAppointeeEntity entity = updatePayoutMbrAppointee(request, result.get(0));
					entity = tempSaveTempPayoutService.insertPayoutMbrAppointee(request.getPayoutNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), PayoutErrorConstants.APPOINTEE_SAVE_SUCCESS);
				} else {
					responseDto= ApiResponseDto
							.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_APPOINTEE_ID).build());
				}
			} else {
				Optional<TempPayoutEntity> payoutEntity = tempPayoutRepository.findByPayoutNoAndIsActive(request.getPayoutNo(),
						Boolean.TRUE);
				if (payoutEntity.isPresent()) {
					TempPayoutMbrAppointeeEntity entity = insertPayoutMbrAppointee(request, payoutEntity.get());
					entity = tempSaveTempPayoutService.insertPayoutMbrAppointee(request.getPayoutNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), PayoutErrorConstants.APPOINTEE_SAVE_SUCCESS);
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

	private TempPayoutMbrAppointeeEntity insertPayoutMbrAppointee(PayoutMbrAppointeeDto request,
			TempPayoutEntity tempPayoutEntity) {
		TempPayoutMbrEntity payoutMbr = tempPayoutEntity.getPayoutMbr();
		TempPayoutMbrAppointeeEntity entity = new TempPayoutMbrAppointeeEntity();
		convertDtoToEntity(entity, request);
		entity.setPayoutMbrEntity(payoutMbr);
		return entity;
	}

	private TempPayoutMbrAppointeeEntity updatePayoutMbrAppointee(PayoutMbrAppointeeDto request,
			TempPayoutMbrAppointeeEntity tempPayoutMbrAppointeeEntity) {
		TempPayoutMbrAppointeeEntity entity = new TempPayoutMbrAppointeeEntity();
		BeanUtils.copyProperties(tempPayoutMbrAppointeeEntity, entity);
		convertDtoToEntity(entity, request);
		return entity;
	}

	private void convertDtoToEntity(TempPayoutMbrAppointeeEntity entity, PayoutMbrAppointeeDto payoutMbrApponteeDto) {

		entity.setDob(CommonDateUtils.convertStringToDate(payoutMbrApponteeDto.getDob()));
		entity.setModifiedOn(CommonDateUtils.sysDate());
		entity.setModifiedBy(payoutMbrApponteeDto.getCreatedBy());

		entity.setAadharNumber(payoutMbrApponteeDto.getAadharNumber());
		entity.setAccountNumber(payoutMbrApponteeDto.getAccountNumber());
		entity.setAccountType(payoutMbrApponteeDto.getAccountType());
		entity.setAddressOne(payoutMbrApponteeDto.getAddressOne());
		entity.setAddressTwo(payoutMbrApponteeDto.getAddressTwo());
		entity.setAddressThree(payoutMbrApponteeDto.getAddressThree());
		entity.setAppointeeCode(payoutMbrApponteeDto.getAppointeeCode());
		entity.setBankName(payoutMbrApponteeDto.getBankName());
		entity.setCountry(payoutMbrApponteeDto.getCountry());
		entity.setDistrict(payoutMbrApponteeDto.getDistrict());
		entity.setEmailId(payoutMbrApponteeDto.getEmailId());
		entity.setIfscCode(payoutMbrApponteeDto.getIfscCode());
		entity.setFirstName(payoutMbrApponteeDto.getFirstName());
		entity.setLastName(payoutMbrApponteeDto.getLastName());
		entity.setMaritalStatus(payoutMbrApponteeDto.getMaritalStatus());
		entity.setMiddleName(payoutMbrApponteeDto.getMiddleName());
		entity.setMobileNo(payoutMbrApponteeDto.getMobileNo());
		entity.setPincode(payoutMbrApponteeDto.getPincode());
		entity.setRelationShip(payoutMbrApponteeDto.getRelationShip());
		entity.setCreatedOn(CommonDateUtils.sysDate());
		entity.setCreatedBy(payoutMbrApponteeDto.getCreatedBy());
		
		entity.setNomineeCode(payoutMbrApponteeDto.getNomineeCode());
	}

	@Override
	public ApiResponseDto<List<PayoutMbrAppointeeDto>> findAppointeeByPayoutNo(String payoutNo) {
		ApiResponseDto<List<PayoutMbrAppointeeDto>> responseDto=new ApiResponseDto<>();
		logger.info(" TempPayoutMbrAppointeeServiceImpl{}::--{}:: findAppointeeByPayoutNo {}:: start");
		try {
			Specification<TempPayoutMbrAppointeeEntity> specification = findAppointeeDetailsByPayoutNo(payoutNo);

			List<TempPayoutMbrAppointeeEntity> result = tempPayoutMbrAppointeeRepository.findAll(specification);

			if (!result.isEmpty()) {
				List<PayoutMbrAppointeeDto> response = result.stream().map(this::convertEntityToDto)
						.collect(Collectors.toList());
				responseDto= ApiResponseDto.success(response);
			}

			responseDto= ApiResponseDto.success(Collections.emptyList());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempPayoutMbrAppointeeServiceImpl{}::--{}:: findAppointeeByPayoutNo {}:: error is "+e.getMessage());
		}
		logger.info(" TempPayoutMbrAppointeeServiceImpl{}::--{}:: findAppointeeByPayoutNo {}:: ended");
		return responseDto;
	}

	private PayoutMbrAppointeeDto convertEntityToDto(TempPayoutMbrAppointeeEntity entity) {
		ModelMapper dtoModelMapper = new ModelMapper();
		dtoModelMapper.addMappings(new PropertyMap<TempPayoutMbrAppointeeEntity, PayoutMbrAppointeeDto>() {
			@Override
			protected void configure() {
				skip(destination.getDob());
			}
		});
		PayoutMbrAppointeeDto dto = dtoModelMapper.map(entity, PayoutMbrAppointeeDto.class);
		dto.setDob(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(entity.getDob()));
		return dto;
	}
}
