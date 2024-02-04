package com.lic.epgs.claim.temp.service.impl;

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

import com.lic.epgs.claim.constants.ClaimEntityConstants;
import com.lic.epgs.claim.constants.ClaimErrorConstants;
import com.lic.epgs.claim.constants.ClaimStatus;
import com.lic.epgs.claim.dto.ClaimMbrAppointeeDto;
import com.lic.epgs.claim.temp.entity.TempClaimEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrAppointeeEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrEntity;
import com.lic.epgs.claim.temp.repository.TempClaimMbrAppointeeRepository;
import com.lic.epgs.claim.temp.repository.TempClaimRepository;
import com.lic.epgs.claim.temp.service.TempClaimMbrAppointeeService;
import com.lic.epgs.claim.temp.service.TempSaveClaimService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.utils.CommonDateUtils;

@Service
@Transactional
public class TempClaimMbrAppointeeServiceImpl implements TempClaimMbrAppointeeService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TempClaimMbrAppointeeRepository tempClaimMbrAppointeeRepository;

	@Autowired
	TempClaimRepository tempClaimRepository;

	@Autowired
	TempSaveClaimService tempSaveTempClaimService;

	@Autowired
	ModelMapper modelMapper;

	private Specification<TempClaimMbrAppointeeEntity> findAppointeeDetailsByClaimNo(String claimNo) {
		return (root, query, criteriaBuilder) -> {
			Join<TempClaimMbrAppointeeEntity, TempClaimMbrEntity> claimMbr = root
					.join(ClaimEntityConstants.CLAIM_MBR_ENTITY);
			Join<TempClaimMbrEntity, TempClaimEntity> claim = claimMbr.join(ClaimEntityConstants.CLAIM);
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.CLAIM_NO), claimNo));
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(claim.get(ClaimEntityConstants.CLAIM_NO), claimNo);
		};
	}

	private Specification<TempClaimMbrAppointeeEntity> findAppointeeDetailsByClaimNoAndAppointeeId(String claimNo,
			Long appointeeId) {
		return (root, query, criteriaBuilder) -> {
			Join<TempClaimMbrAppointeeEntity, TempClaimMbrEntity> claimMbr = root
					.join(ClaimEntityConstants.CLAIM_MBR_ENTITY);
			Join<TempClaimMbrEntity, TempClaimEntity> claim = claimMbr.join(ClaimEntityConstants.CLAIM);
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.CLAIM_NO), claimNo));
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(root.get(ClaimEntityConstants.APPOINTEE_ID), appointeeId);
		};
	}

	@Override
	public ApiResponseDto<ClaimMbrAppointeeDto> save(ClaimMbrAppointeeDto request) {
		ApiResponseDto<ClaimMbrAppointeeDto> responseDto=new ApiResponseDto<>();
		logger.info("TempClaimMbrAppointeeServiceImpl:{}::save:{}:start");
		try {
			if (request.getAppointeeId() != null && request.getAppointeeId() > 0) {
				Specification<TempClaimMbrAppointeeEntity> specification = findAppointeeDetailsByClaimNoAndAppointeeId(
						request.getClaimNo(), request.getAppointeeId());

				List<TempClaimMbrAppointeeEntity> result = tempClaimMbrAppointeeRepository.findAll(specification);
				if (!result.isEmpty() && result.get(0) != null) {
					TempClaimMbrAppointeeEntity entity = updateClaimMbrAppointee(request, result.get(0));
					entity = tempSaveTempClaimService.insertClaimMbrAppointee(request.getClaimNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), ClaimErrorConstants.APPOINTEE_SAVE_SUCCESS);
				} else {
					responseDto= ApiResponseDto
							.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_APPOINTEE_ID).build());
				}
			} else {
				Optional<TempClaimEntity> claimEntity = tempClaimRepository.findByClaimNoAndIsActive(request.getClaimNo(),
						Boolean.TRUE);
				if (claimEntity.isPresent()) {
					
					TempClaimEntity tempClaimEntity=claimEntity.get();
					tempClaimEntity.setStatus(ClaimStatus.PRELIMINARY_REQUIREMENT_REVIEW.val());
					tempClaimRepository.save(tempClaimEntity);
					TempClaimMbrAppointeeEntity entity = insertClaimMbrAppointee(request, tempClaimEntity);
					entity = tempSaveTempClaimService.insertClaimMbrAppointee(request.getClaimNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), ClaimErrorConstants.APPOINTEE_SAVE_SUCCESS);
				} else {
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempClaimMbrAppointeeServiceImpl:{}::save:{}:error is"+e.getMessage());
		}
		logger.info("TempClaimMbrAppointeeServiceImpl:{}::save:{}:ended");
		return responseDto;
	}

	private TempClaimMbrAppointeeEntity insertClaimMbrAppointee(ClaimMbrAppointeeDto request,
			TempClaimEntity tempClaimEntity) {
		TempClaimMbrEntity claimMbr = tempClaimEntity.getClaimMbr();
		TempClaimMbrAppointeeEntity entity = new TempClaimMbrAppointeeEntity();
		convertDtoToEntity(entity, request);
		entity.setClaimMbrEntity(claimMbr);
		return entity;
	}

	private TempClaimMbrAppointeeEntity updateClaimMbrAppointee(ClaimMbrAppointeeDto request,
			TempClaimMbrAppointeeEntity tempClaimMbrAppointeeEntity) {
		TempClaimMbrAppointeeEntity entity = new TempClaimMbrAppointeeEntity();
		BeanUtils.copyProperties(tempClaimMbrAppointeeEntity, entity);
		convertDtoToEntity(entity, request);
		return entity;
	}

	private void convertDtoToEntity(TempClaimMbrAppointeeEntity entity, ClaimMbrAppointeeDto claimMbrApponteeDto) {

		entity.setDob(CommonDateUtils.convertStringToDate(claimMbrApponteeDto.getDob()));
		entity.setModifiedOn(CommonDateUtils.sysDate());
		entity.setModifiedBy(claimMbrApponteeDto.getCreatedBy());

		entity.setAadharNumber(claimMbrApponteeDto.getAadharNumber());
		entity.setAccountNumber(claimMbrApponteeDto.getAccountNumber());
		entity.setAccountType(claimMbrApponteeDto.getAccountType());
		entity.setAddressOne(claimMbrApponteeDto.getAddressOne());
		entity.setAddressTwo(claimMbrApponteeDto.getAddressTwo());
		entity.setAddressThree(claimMbrApponteeDto.getAddressThree());
		entity.setAppointeeCode(claimMbrApponteeDto.getAppointeeCode());
		entity.setBankName(claimMbrApponteeDto.getBankName());
		entity.setCountry(claimMbrApponteeDto.getCountry());
		entity.setDistrict(claimMbrApponteeDto.getDistrict());
		entity.setEmailId(claimMbrApponteeDto.getEmailId());
		entity.setIfscCode(claimMbrApponteeDto.getIfscCode());
		entity.setFirstName(claimMbrApponteeDto.getFirstName());
		entity.setLastName(claimMbrApponteeDto.getLastName());
		entity.setMaritalStatus(claimMbrApponteeDto.getMaritalStatus());
		entity.setMiddleName(claimMbrApponteeDto.getMiddleName());
		entity.setMobileNo(claimMbrApponteeDto.getMobileNo());
		entity.setPincode(claimMbrApponteeDto.getPincode());
		entity.setRelationShip(claimMbrApponteeDto.getRelationShip());
		entity.setCreatedOn(CommonDateUtils.sysDate());
		entity.setCreatedBy(claimMbrApponteeDto.getCreatedBy());
		entity.setClaimNo(claimMbrApponteeDto.getClaimNo());
		entity.setNomineeCode(claimMbrApponteeDto.getNomineeCode());
	}

	@Override
	public ApiResponseDto<List<ClaimMbrAppointeeDto>> findAppointeeByClaimNo(String claimNo) {
		ApiResponseDto<List<ClaimMbrAppointeeDto>> responseDto=new ApiResponseDto<>();
		try {
			logger.info("TempClaimMbrAppointeeServiceImpl:{}::findAppointeeByClaimNo:{}:start");
			Specification<TempClaimMbrAppointeeEntity> specification = findAppointeeDetailsByClaimNo(claimNo);

			List<TempClaimMbrAppointeeEntity> result = tempClaimMbrAppointeeRepository.findAll(specification);

			if (!result.isEmpty()) {
				List<ClaimMbrAppointeeDto> response = result.stream().map(this::convertEntityToDto)
						.collect(Collectors.toList());
				responseDto= ApiResponseDto.success(response);
			}

			responseDto= ApiResponseDto.success(Collections.emptyList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("TempClaimMbrAppointeeServiceImpl:{}::findAppointeeByClaimNo:{}:ended");
		return responseDto;
	}

	private ClaimMbrAppointeeDto convertEntityToDto(TempClaimMbrAppointeeEntity entity) {
		ModelMapper dtoModelMapper = new ModelMapper();
		dtoModelMapper.addMappings(new PropertyMap<TempClaimMbrAppointeeEntity, ClaimMbrAppointeeDto>() {
			@Override
			protected void configure() {
				skip(destination.getDob());
			}
		});
		ClaimMbrAppointeeDto dto = dtoModelMapper.map(entity, ClaimMbrAppointeeDto.class);
		dto.setDob(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(entity.getDob()));
		return dto;
	}
}
