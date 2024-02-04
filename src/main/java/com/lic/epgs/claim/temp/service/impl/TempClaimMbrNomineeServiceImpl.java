package com.lic.epgs.claim.temp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lic.epgs.claim.constants.ClaimConstants;
import com.lic.epgs.claim.constants.ClaimEntityConstants;
import com.lic.epgs.claim.constants.ClaimErrorConstants;
import com.lic.epgs.claim.constants.ClaimStatus;
import com.lic.epgs.claim.dto.ClaimAppointeeDto;
import com.lic.epgs.claim.dto.ClaimMbrNomineeDto;
import com.lic.epgs.claim.temp.entity.TempClaimEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrNomineeEntity;
import com.lic.epgs.claim.temp.repository.TempClaimMbrNomineeRepository;
import com.lic.epgs.claim.temp.repository.TempClaimRepository;
import com.lic.epgs.claim.temp.service.TempClaimMbrNomineeService;
import com.lic.epgs.claim.temp.service.TempSaveClaimService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.utils.CommonConstants;

@Service
@Transactional
public class TempClaimMbrNomineeServiceImpl implements TempClaimMbrNomineeService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TempClaimMbrNomineeRepository tempClaimMbrNomineeRepository;

	@Autowired
	TempClaimRepository tempClaimRepository;

	@Autowired
	TempSaveClaimService tempSaveTempClaimService;

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private CommonService commonSequenceService;

	
	private synchronized String getClaimNomineeSequence() {
		return commonSequenceService.getSequence(CommonConstants.CLAIM_NOMINEE_SEQ);
	}

	private Specification<TempClaimMbrNomineeEntity> findNomineeDetailsByClaimNo(String claimNo) {
		return (root, query, criteriaBuilder) -> {
			Join<TempClaimMbrNomineeEntity, TempClaimMbrEntity> claimMbr = root
					.join(ClaimEntityConstants.CLAIM_MBR_ENTITY);
			Join<TempClaimMbrEntity, TempClaimEntity> claim = claimMbr.join(ClaimEntityConstants.CLAIM);
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.CLAIM_NO), claimNo));
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(claim.get(ClaimEntityConstants.CLAIM_NO), claimNo);
		};
	}

	private Specification<TempClaimMbrNomineeEntity> findNomineeDetailsByClaimNoAndNomineeId(String claimNo,
			Long nomineeId) {
		return (root, query, criteriaBuilder) -> {
			Join<TempClaimMbrNomineeEntity, TempClaimMbrEntity> claimMbr = root
					.join(ClaimEntityConstants.CLAIM_MBR_ENTITY);
			Join<TempClaimMbrEntity, TempClaimEntity> claim = claimMbr.join(ClaimEntityConstants.CLAIM);
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.CLAIM_NO), claimNo));
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(root.get(ClaimEntityConstants.NOMINEE_ID), nomineeId);
		};
	}

	@Override
	public ApiResponseDto<ClaimMbrNomineeDto> save(ClaimMbrNomineeDto request) {
		ApiResponseDto<ClaimMbrNomineeDto> responseDto=new ApiResponseDto<>();
		
		logger.info("TempClaimMbrAppointeeServiceImpl:{}::save:{}:start");
		try {
			
			if (request.getNomineeId() != null && request.getNomineeId() > 0) {
				Specification<TempClaimMbrNomineeEntity> specification = findNomineeDetailsByClaimNoAndNomineeId(
						request.getClaimNo(), request.getNomineeId());

				List<TempClaimMbrNomineeEntity> result = tempClaimMbrNomineeRepository.findAll(specification);
				if (!result.isEmpty()) {
					System.out.println("enter");
					TempClaimMbrNomineeEntity entity = updateClaimMbrNominee(request, result.get(0));
					System.out.println("enter 1");
					entity = tempSaveTempClaimService.insertClaimMbrNominee(request.getClaimNo(), entity);
					System.out.println("enter2");
					return ApiResponseDto.success(convertEntityToDto(entity), ClaimErrorConstants.SAVED_SUCCESSFULLY);
				} else {
					return ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_NOMINEE_ID).build());
				}
			} else {
				Optional<TempClaimEntity> claimEntity = tempClaimRepository.findByClaimNoAndIsActiveTrue(request.getClaimNo());
				if (claimEntity.isPresent()) {
					TempClaimEntity tempClaimEntity=claimEntity.get();
					tempClaimEntity.setStatus(ClaimStatus.PRELIMINARY_REQUIREMENT_REVIEW.val());
					tempClaimRepository.save(tempClaimEntity);
					TempClaimMbrNomineeEntity entity = insertClaimMbrNominee(request, tempClaimEntity);
					entity = tempSaveTempClaimService.insertClaimMbrNominee(request.getClaimNo(), entity);				
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), ClaimErrorConstants.NOMINEE_SAVE_SUCCESS);
				} else {
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("TempClaimMbrAppointeeServiceImpl:{}::save:{}:error is"+e.getMessage());	
		}
		logger.info("TempClaimMbrAppointeeServiceImpl:{}::save:{}:ended");
		return responseDto;
	}

	private TempClaimMbrNomineeEntity insertClaimMbrNominee(ClaimMbrNomineeDto request,
			TempClaimEntity tempClaimEntity) {
		TempClaimMbrEntity claimMbr = tempClaimEntity.getClaimMbr();
		TempClaimMbrNomineeEntity entity = new TempClaimMbrNomineeEntity();
		entity.setNomineeCode(getClaimNomineeSequence());
		convertDtoToEntity(entity, request);
		entity.setClaimMbrEntity(claimMbr);
		return entity;
	}

	private TempClaimMbrNomineeEntity updateClaimMbrNominee(ClaimMbrNomineeDto request,	TempClaimMbrNomineeEntity tempClaimMbrNomineeEntity) {
		TempClaimMbrNomineeEntity entity = new TempClaimMbrNomineeEntity();
		BeanUtils.copyProperties(tempClaimMbrNomineeEntity, entity);
		convertDtoToEntity(entity, request);
		return entity;
	}

	private void convertDtoToEntity(TempClaimMbrNomineeEntity tempClaimMbrNomineeEntity,
			ClaimMbrNomineeDto claimMbrNomineeDto) {
		
		

		tempClaimMbrNomineeEntity.setClaimNo(claimMbrNomineeDto.getClaimNo());
		tempClaimMbrNomineeEntity.setDob(CommonDateUtils.convertStringToDate(claimMbrNomineeDto.getDob()));
		tempClaimMbrNomineeEntity.setModifiedOn(CommonDateUtils.sysDate());
		tempClaimMbrNomineeEntity.setModifiedBy(claimMbrNomineeDto.getCreatedBy());
		tempClaimMbrNomineeEntity.setApponiteName(claimMbrNomineeDto.getApponiteName());
		tempClaimMbrNomineeEntity.setIfscCodeAvailable(claimMbrNomineeDto.getIfscCodeAvailable());
		tempClaimMbrNomineeEntity.setClaimantType(claimMbrNomineeDto.getClaimantType());
		tempClaimMbrNomineeEntity.setAge(claimMbrNomineeDto.getAge());
		tempClaimMbrNomineeEntity.setAadharNumber(claimMbrNomineeDto.getAadharNumber());
		tempClaimMbrNomineeEntity.setAccountNumber(claimMbrNomineeDto.getAccountNumber());
		tempClaimMbrNomineeEntity.setAccountType(claimMbrNomineeDto.getAccountType());
		tempClaimMbrNomineeEntity.setAddressOne(claimMbrNomineeDto.getAddressOne());
		tempClaimMbrNomineeEntity.setAddressThree(claimMbrNomineeDto.getAddressThree());
		tempClaimMbrNomineeEntity.setAddressTwo(claimMbrNomineeDto.getAddressTwo());
		tempClaimMbrNomineeEntity.setBankName(claimMbrNomineeDto.getBankName());
		tempClaimMbrNomineeEntity.setCountry(claimMbrNomineeDto.getCountry());
		tempClaimMbrNomineeEntity.setDistrict(claimMbrNomineeDto.getDistrict());
		tempClaimMbrNomineeEntity.setEmailId(claimMbrNomineeDto.getEmailId());
		tempClaimMbrNomineeEntity.setFirstName(claimMbrNomineeDto.getFirstName());
		tempClaimMbrNomineeEntity.setIfscCode(claimMbrNomineeDto.getIfscCode());
		tempClaimMbrNomineeEntity.setLastName(claimMbrNomineeDto.getLastName());
		tempClaimMbrNomineeEntity.setMaritalStatus(claimMbrNomineeDto.getMaritalStatus());
		tempClaimMbrNomineeEntity.setMiddleName(claimMbrNomineeDto.getMiddleName());
		tempClaimMbrNomineeEntity.setMobileNo(claimMbrNomineeDto.getMobileNo());
		tempClaimMbrNomineeEntity.setPincode(claimMbrNomineeDto.getPincode());
		tempClaimMbrNomineeEntity.setRelationShip(claimMbrNomineeDto.getRelationShip());
		tempClaimMbrNomineeEntity.setSharedAmount(claimMbrNomineeDto.getSharedAmount());
		tempClaimMbrNomineeEntity.setSharedPercentage(claimMbrNomineeDto.getSharedPercentage());
		tempClaimMbrNomineeEntity.setState(claimMbrNomineeDto.getState());
		tempClaimMbrNomineeEntity.setBankBranch(claimMbrNomineeDto.getBankBranch());
		tempClaimMbrNomineeEntity.setGender(claimMbrNomineeDto.getGender());
		tempClaimMbrNomineeEntity.setPan(claimMbrNomineeDto.getPan());
		tempClaimMbrNomineeEntity.setParentNomineeId(claimMbrNomineeDto.getParentNomineeId());
	}

	@Override
	public ApiResponseDto<List<ClaimMbrNomineeDto>> findNomineeByClaimNo(String claimNo) {
		ApiResponseDto<List<ClaimMbrNomineeDto>> responseDto=new ApiResponseDto<>();
		logger.info("TempClaimMbrAppointeeServiceImpl:{}::findNomineeByClaimNo:{}:start");
		try {
			Specification<TempClaimMbrNomineeEntity> specification = findNomineeDetailsByClaimNo(claimNo);

			List<TempClaimMbrNomineeEntity> result = tempClaimMbrNomineeRepository.findAll(specification);

			if (!result.isEmpty()) {
				List<ClaimMbrNomineeDto> response = result.stream().map(this::convertEntityToDto)
						.collect(Collectors.toList());
				return ApiResponseDto.success(response);
			}

			return ApiResponseDto.success(Collections.emptyList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("TempClaimMbrAppointeeServiceImpl:{}::findNomineeByClaimNo:{}:error is "+e.getMessage());
		}
		logger.info("TempClaimMbrAppointeeServiceImpl:{}::findNomineeByClaimNo:{}:ended");
		return responseDto;
	}

	private ClaimMbrNomineeDto convertEntityToDto(TempClaimMbrNomineeEntity entity) {
		ModelMapper dtoModelMapper = new ModelMapper();
		dtoModelMapper.addMappings(new PropertyMap<TempClaimMbrNomineeEntity, ClaimMbrNomineeDto>() {
			@Override
			protected void configure() {
				skip(destination.getDob());
			}
		});
		ClaimMbrNomineeDto dto = dtoModelMapper.map(entity, ClaimMbrNomineeDto.class);
		dto.setDob(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(entity.getDob()));
		return dto;
	}
	
	@Override
	public ApiResponseDto<List<ClaimAppointeeDto>> getAppointeeNameList(String claimNo) {
		logger.info("TempClaimMbrAppointeeServiceImpl:getAppointeeNameList:Ends");
		ApiResponseDto<List<ClaimAppointeeDto>> commonResponse = new ApiResponseDto<>();
		List<ClaimAppointeeDto> claimAppointeeDtoList = new ArrayList<>();
		try {
			Optional<TempClaimEntity> tempClaimEntity = tempClaimRepository.findByClaimNoAndIsActiveTrue(claimNo);
			if(tempClaimEntity.isPresent()){
				TempClaimEntity tempClaimEntitys=tempClaimEntity.get();
				List<TempClaimMbrNomineeEntity> claimMbrNomineeDtls=tempClaimEntitys.getClaimMbr().getClaimMbrNomineeDtls();
//				List<String> tempClaimMbrAppointeeEntityList=claimMbrNomineeDtls.stream().map(i->i.getApponiteName()).collect(Collectors.toList());
				if(!claimMbrNomineeDtls.isEmpty()) {
					for(TempClaimMbrNomineeEntity appointeeName :claimMbrNomineeDtls) {
						ClaimAppointeeDto claimAppointeeDto = new ClaimAppointeeDto();
						if(appointeeName.getClaimantType().equalsIgnoreCase("Appointee")) {
						claimAppointeeDto.setAppointeeName(appointeeName.getFirstName());
						claimAppointeeDtoList.add(claimAppointeeDto);
						}
					}
				commonResponse.setData(claimAppointeeDtoList);
				commonResponse.setStatus(ClaimConstants.SUCCESS);
				commonResponse.setMessage(ClaimConstants.RETRIVE);
				}
			}
			} catch (IllegalArgumentException e) {
				logger.error("Exception:TempClaimMbrAppointeeServiceImpl:getAppointeeNameList", e);
			} finally {
				logger.info("TempClaimMbrAppointeeServiceImpl:getAppointeeNameList:Ends");
			}
				return commonResponse;
	      }
}
