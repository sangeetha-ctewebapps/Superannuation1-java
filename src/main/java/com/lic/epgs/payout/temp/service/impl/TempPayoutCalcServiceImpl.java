package com.lic.epgs.payout.temp.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Join;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lic.epgs.payout.constants.PayoutEntityConstants;
import com.lic.epgs.payout.constants.PayoutErrorConstants;
import com.lic.epgs.payout.dto.PayoutAnnuityCalcDto;
import com.lic.epgs.payout.dto.PayoutCommutationCalcDto;
import com.lic.epgs.payout.dto.PayoutFundValueDto;
import com.lic.epgs.payout.dto.PayoutMbrFundValueDto;
import com.lic.epgs.payout.temp.entity.TempPayoutAnnuityCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutCommutationCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutFundValueEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrFundValueEntity;
import com.lic.epgs.payout.temp.repository.TempPayoutAnnuityCalcRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutCommutationCalcRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrFundValueRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutRepository;
import com.lic.epgs.payout.temp.service.TempPayoutCalcService;
import com.lic.epgs.payout.temp.service.TempSavePayoutService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.utils.CommonDateUtils;

@Service
@Transactional
public class TempPayoutCalcServiceImpl implements TempPayoutCalcService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TempPayoutCommutationCalcRepository tempPayoutCommutationCalcRepository;

	@Autowired
	TempPayoutAnnuityCalcRepository tempPayoutAnnuityCalcRepository;

	@Autowired
	TempPayoutRepository tempPayoutRepository;

	@Autowired
	TempPayoutMbrRepository tempPayoutMbrRepository;

	@Autowired
	TempSavePayoutService tempSaveTempPayoutService;

	@Autowired
	TempPayoutMbrFundValueRepository tempPayoutMbrFundValueRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public ApiResponseDto<PayoutFundValueDto> saveFundvalue(PayoutFundValueDto request) {
		ApiResponseDto<PayoutFundValueDto> responseDto=new ApiResponseDto<>();
		logger.info(" TempPayoutCalcServiceImpl{}::--{}:: saveFundvalue {}:: start");
		try {
			Optional<TempPayoutEntity> optPayoutEntity = tempPayoutRepository.findByPayoutNoAndIsActive(request.getPayoutNo(),
					Boolean.TRUE);
			if (optPayoutEntity.isPresent()) {
				TempPayoutEntity payoutEntity = optPayoutEntity.get();
				TempPayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();
				if (payoutMbr != null) {
					TempPayoutFundValueEntity entity = new TempPayoutFundValueEntity();
					List<TempPayoutFundValueEntity> fundValue = payoutMbr.getPayoutFundValue();
					if (fundValue != null && !fundValue.isEmpty()) {
						entity = fundValue.get(0);
					}
					convertToDtoToEntity(request, entity);
					tempSaveTempPayoutService.insertPayoutFundValue(request.getPayoutNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), PayoutErrorConstants.FUND_VALUE_SAVE_SUCCESS);
				} else {
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
				}
			} else {
				responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempPayoutCalcServiceImpl{}::--{}:: saveFundvalue {}:: error is "+ e.getMessage());
		}
		logger.info(" TempPayoutCalcServiceImpl{}::--{}:: saveFundvalue {}:: ended");
		return responseDto;
	}

	private PayoutFundValueDto convertEntityToDto(TempPayoutFundValueEntity entity) {
		return modelMapper.map(entity, PayoutFundValueDto.class);
	}

	private void convertToDtoToEntity(PayoutFundValueDto request, TempPayoutFundValueEntity entity) {
		entity.setAnnuityOption(request.getAnnuityOption());
		entity.setAnuityMode(request.getAnuityMode());
		entity.setCalculationType(request.getCalculationType());
		entity.setPayoutNo(request.getPayoutNo());
		entity.setEmployeeContribution(request.getEmployeeContribution());
		entity.setEmployerContribution(request.getEmployerContribution());
		entity.setIsJointLiveRequired(request.getIsJointLiveRequired());
		entity.setPension(request.getPension());
		entity.setPurchasePrice(request.getPurchasePrice());
		entity.setTotalFundValue(request.getTotalFundValue());
		entity.setTotalInterestAccured(request.getTotalInterestAccured());
		entity.setVoluntaryContribution(request.getVoluntaryContribution());
	}

	@Override
	public ApiResponseDto<PayoutMbrFundValueDto> saveMbrFundvalue(PayoutMbrFundValueDto request) {
		ApiResponseDto<PayoutMbrFundValueDto> responseDto=new ApiResponseDto<>();
		logger.info(" TempPayoutCalcServiceImpl{}::--{}:: saveMbrFundvalue {}:: start");
		try {
			if (request.getFundValueId() != null && request.getFundValueId() > 0) {
				Specification<TempPayoutMbrFundValueEntity> specification = findMBRFundValue(request.getPayoutNo(),
						request.getFundValueId());

				List<TempPayoutMbrFundValueEntity> result = tempPayoutMbrFundValueRepository.findAll(specification);
				if (!result.isEmpty() && result.get(0) != null) {
					TempPayoutMbrFundValueEntity entity = updatePayoutMbrFundValue(request, result.get(0));
					entity = tempSaveTempPayoutService.insertPayoutMbrFundValue(request.getPayoutNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), PayoutErrorConstants.FUND_VALUE_SAVE_SUCCESS);
				} else {
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_FUND_VALUE_ID).build());
				}
			} else {
				Optional<TempPayoutEntity> payoutEntity = tempPayoutRepository.findByPayoutNoAndIsActive(request.getPayoutNo(),
						Boolean.TRUE);
				if (payoutEntity.isPresent()) {
					TempPayoutMbrFundValueEntity entity = insertPayoutMbrFundValue(request, payoutEntity.get());
					entity = tempSaveTempPayoutService.insertPayoutMbrFundValue(request.getPayoutNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), PayoutErrorConstants.FUND_VALUE_SAVE_SUCCESS);
				} else {
					responseDto=ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempPayoutCalcServiceImpl{}::--{}:: saveMbrFundvalue {}:: erroe is "+e.getMessage());
		}
		logger.info(" TempPayoutCalcServiceImpl{}::--{}:: saveMbrFundvalue {}:: ended");
		return responseDto;
	}

	private PayoutMbrFundValueDto convertEntityToDto(TempPayoutMbrFundValueEntity entity) {
		return modelMapper.map(entity, PayoutMbrFundValueDto.class);
	}

	private TempPayoutMbrFundValueEntity updatePayoutMbrFundValue(PayoutMbrFundValueDto request,
			TempPayoutMbrFundValueEntity tempPayoutMbrFundValueEntity) {
		TempPayoutMbrFundValueEntity entity = new TempPayoutMbrFundValueEntity();
		BeanUtils.copyProperties(tempPayoutMbrFundValueEntity, entity);
		convertDtoToEntity(request, entity);
		return entity;
	}

	private Specification<TempPayoutMbrFundValueEntity> findMBRFundValue(String payoutNo, Long fundValueId) {
		return (root, query, criteriaBuilder) -> {
			Join<TempPayoutMbrFundValueEntity, TempPayoutMbrEntity> payoutMbr = root
					.join(PayoutEntityConstants.PAYOUT_MBR_ENTITY);
			Join<TempPayoutMbrEntity, TempPayoutEntity> payout = payoutMbr.join(PayoutEntityConstants.PAYOUT);
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.PAYOUT_NO), payoutNo));
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(root.get(PayoutEntityConstants.FUND_VAL_ID), fundValueId);
		};
	}

	private TempPayoutMbrFundValueEntity insertPayoutMbrFundValue(PayoutMbrFundValueDto request,
			TempPayoutEntity tempPayoutEntity) {
		TempPayoutMbrEntity payoutMbr = tempPayoutEntity.getPayoutMbr();
		TempPayoutMbrFundValueEntity entity = new TempPayoutMbrFundValueEntity();
		convertDtoToEntity(request, entity);
		entity.setPayoutMbrEntity(payoutMbr);
		return entity;
	}

	private void convertDtoToEntity(PayoutMbrFundValueDto request, TempPayoutMbrFundValueEntity entity) {
		entity.setAnnuityOption(request.getAnnuityOption());
		entity.setAnuityMode(request.getAnuityMode());
		entity.setCalculationType(request.getCalculationType());
		entity.setPayoutNo(request.getPayoutNo());
		entity.setEmployeeContribution(request.getEmployeeContribution());
		entity.setEmployerContribution(request.getEmployerContribution());
		entity.setIsJointLiveRequired(request.getIsJointLiveRequired());
		entity.setPension(request.getPension());
		entity.setPurchasePrice(request.getPurchasePrice());
		entity.setTotalFundValue(request.getTotalFundValue());
		entity.setTotalInterestAccured(request.getTotalInterestAccured());
		entity.setVoluntaryContribution(request.getVoluntaryContribution());
		entity.setPurchasedFrom(request.getPurchasedFrom());
		entity.setPurchasePriceForLIC(request.getPurchasePriceForLIC());
		entity.setNomineeCode(request.getNomineeCode());
		entity.setPurchasePriceOther(request.getPurchasePriceOther());		
		
	}

	private Specification<TempPayoutCommutationCalcEntity> findPayoutCommutationCalcById(String payoutNo,
			Long communityId) {
		return (root, query, criteriaBuilder) -> {
			Join<TempPayoutCommutationCalcEntity, TempPayoutMbrEntity> payoutMbr = root
					.join(PayoutEntityConstants.PAYOUT_MBR_ENTITY);
			Join<TempPayoutMbrEntity, TempPayoutEntity> payout = payoutMbr.join(PayoutEntityConstants.PAYOUT);
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.PAYOUT_NO), payoutNo));
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(root.get(PayoutEntityConstants.COMMUNITY_ID), communityId);
		};
	}

	private Specification<TempPayoutAnnuityCalcEntity> annuityIdEqual(String payoutNo, Long annuityId) {
		return (root, query, criteriaBuilder) -> {
			Join<TempPayoutAnnuityCalcEntity, TempPayoutMbrEntity> payoutMbr = root
					.join(PayoutEntityConstants.PAYOUT_MBR_ENTITY);
			Join<TempPayoutMbrEntity, TempPayoutEntity> payout = payoutMbr.join(PayoutEntityConstants.PAYOUT);
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.PAYOUT_NO), payoutNo));
			payout.on(criteriaBuilder.equal(payout.get(PayoutEntityConstants.IS_ACTIVE), Boolean.TRUE));

			return criteriaBuilder.equal(root.get(PayoutEntityConstants.ANNUITY_ID), annuityId);
		};
	}

	private PayoutCommutationCalcDto convertEntityToDto(TempPayoutCommutationCalcEntity temppayoutcommutationcalcentity) {
		return modelMapper.map(temppayoutcommutationcalcentity, PayoutCommutationCalcDto.class);
	}

	private void convertDtoToEntity(TempPayoutCommutationCalcEntity entity, PayoutCommutationCalcDto request) {

		entity.setAmtPayableTo(request.getAmtPayableTo());
		entity.setCommutationAmt((request.getCommutationAmt()));
		entity.setCommutationBy(request.getCommutationBy());
		entity.setCommutationPerc(request.getCommutationPerc());
		entity.setCommutationReq(request.getCommutationReq());
		entity.setCommutationValue(request.getCommutationValue());
		entity.setExitLoad(request.getExitLoad());
		entity.setCreatedBy(request.getCreatedBy());
		entity.setNomineeCode(request.getNomineeCode());
		entity.setCreatedOn(CommonDateUtils.sysDate());
		entity.setPurchasePrice((request.getPurchasePrice()));
		entity.setTdsApplicable(request.getTdsApplicable());
		entity.setTdsPerc(request.getTdsPerc());
	}

	@Override
	public ApiResponseDto<PayoutAnnuityCalcDto> saveAnuity(PayoutAnnuityCalcDto request) {
		 ApiResponseDto<PayoutAnnuityCalcDto> responseDto=new ApiResponseDto<>();
		 logger.info(" TempPayoutCalcServiceImpl{}::--{}:: saveAnuity {}:: start");
		try {
			if (request.getAnnuityId() != null && request.getAnnuityId() > 0) {
				Specification<TempPayoutAnnuityCalcEntity> specification = annuityIdEqual(request.getPayoutNo(),
						request.getAnnuityId());

				List<TempPayoutAnnuityCalcEntity> result = tempPayoutAnnuityCalcRepository.findAll(specification);
				if (!result.isEmpty() && result.get(0) != null) {
					TempPayoutAnnuityCalcEntity entity = updatePayoutAnnuity(request, result.get(0));
					entity = tempSaveTempPayoutService.insertPayoutAnnuityCalc(request.getPayoutNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), PayoutErrorConstants.ANNUITY_SAVED_SUCCESS);
				} else {
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_ANNUITY_ID).build());
				}
			} else {
				Optional<TempPayoutEntity> payoutEntity = tempPayoutRepository.findByPayoutNoAndIsActive(request.getPayoutNo(),
						Boolean.TRUE);
				if (payoutEntity.isPresent()) {
					TempPayoutAnnuityCalcEntity entity = insertPayoutAnnuity(request, payoutEntity.get());
					entity = tempSaveTempPayoutService.insertPayoutAnnuityCalc(request.getPayoutNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), PayoutErrorConstants.ANNUITY_SAVED_SUCCESS);
				} else {
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempPayoutCalcServiceImpl{}::--{}:: saveAnuity {}:: error is "+e.getMessage());
		}
		logger.info(" TempPayoutCalcServiceImpl{}::--{}:: saveAnuity {}:: ended");
		return responseDto;
	}

	private TempPayoutAnnuityCalcEntity insertPayoutAnnuity(PayoutAnnuityCalcDto request,
			TempPayoutEntity tempPayoutEntity) {
		TempPayoutMbrEntity payoutMbr = tempPayoutEntity.getPayoutMbr();
		TempPayoutAnnuityCalcEntity entity = new TempPayoutAnnuityCalcEntity();
		convertDtoToEntity(entity, request);
		entity.setPayoutMbrEntity(payoutMbr);
		return entity;
	}

	private TempPayoutAnnuityCalcEntity updatePayoutAnnuity(PayoutAnnuityCalcDto request,
			TempPayoutAnnuityCalcEntity tempPayoutAnnuityCalcEntity) {
		TempPayoutAnnuityCalcEntity entity = new TempPayoutAnnuityCalcEntity();
		BeanUtils.copyProperties(tempPayoutAnnuityCalcEntity, entity);
		convertDtoToEntity(entity, request);
		return entity;
	}

	private PayoutAnnuityCalcDto convertEntityToDto(TempPayoutAnnuityCalcEntity tempPayoutAnnuityCalcEntity) {
		return modelMapper.map(tempPayoutAnnuityCalcEntity, PayoutAnnuityCalcDto.class);
	}

	private void convertDtoToEntity(TempPayoutAnnuityCalcEntity entity, PayoutAnnuityCalcDto request) {
		entity.setAmtPaidTo(request.getAmtPaidTo());
		entity.setCreatedBy(request.getCreatedBy());
		entity.setCreatedOn(CommonDateUtils.sysDate());
		entity.setNomineeCode(request.getNomineeCode());
		entity.setPurchasePrice((request.getPurchasePrice()));
		entity.setAnnuityOption(request.getAnnuityOption());
		entity.setAnuityMode(request.getAnuityMode());
		entity.setIsGSTApplicable(request.getIsGSTApplicable());
		entity.setIsJointLiveRequired(request.getIsJointLiveRequired());
		entity.setPension(request.getPension());
		entity.setUnitCode(request.getUnitCode());
		entity.setUnitName(request.getUnitName());
		entity.setUnitId(request.getUnitId());
		entity.setUnitCode(request.getUnitCode());
		entity.setRate(request.getRate());
		entity.setRateType(request.getRateType());
		entity.setIncentiveRate(request.getIncentiveRate());
		entity.setDisIncentiveRate(request.getDisIncentiveRate());
		entity.setNFactor(null);
		
	}

	@Override
	public ApiResponseDto<PayoutCommutationCalcDto> saveCommutation(PayoutCommutationCalcDto request) {
		ApiResponseDto<PayoutCommutationCalcDto> responseDto=new ApiResponseDto<>();
		logger.info(" TempPayoutCalcServiceImpl{}::--{}:: saveCommutation {}:: start");
		try {
			if (request.getCommunityId() != null && request.getCommunityId() > 0) {
				Specification<TempPayoutCommutationCalcEntity> specification = findPayoutCommutationCalcById(
						request.getPayoutNo(), request.getCommunityId());

				List<TempPayoutCommutationCalcEntity> result = tempPayoutCommutationCalcRepository.findAll(specification);
				if (!result.isEmpty() && result.get(0) != null) {
					TempPayoutCommutationCalcEntity entity = updatePayoutCommutation(request, result.get(0));
					entity = tempSaveTempPayoutService.insertPayoutCommutationCalc(request.getPayoutNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), PayoutErrorConstants.COMMUTATION_CALC_SUCCESS);
				} else {
					responseDto= ApiResponseDto
							.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_COMMUTATION_ID).build());
				}
			} else {
				Optional<TempPayoutEntity> payoutEntity = tempPayoutRepository.findByPayoutNoAndIsActive(request.getPayoutNo(),
						Boolean.TRUE);
				if (payoutEntity.isPresent()) {
					TempPayoutCommutationCalcEntity entity = insertPayoutCommutation(request, payoutEntity.get());
					entity = tempSaveTempPayoutService.insertPayoutCommutationCalc(request.getPayoutNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), PayoutErrorConstants.COMMUTATION_CALC_SUCCESS);
				} else {
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempPayoutCalcServiceImpl{}::--{}:: saveCommutation {}:: error is "+e.getMessage());
		}
		logger.info(" TempPayoutCalcServiceImpl{}::--{}:: saveCommutation {}:: ended");
		return responseDto;
	}

	private TempPayoutCommutationCalcEntity insertPayoutCommutation(PayoutCommutationCalcDto request,
			TempPayoutEntity tempPayoutEntity) {
		TempPayoutMbrEntity payoutMbr = tempPayoutEntity.getPayoutMbr();
		TempPayoutCommutationCalcEntity entity = new TempPayoutCommutationCalcEntity();
		convertDtoToEntity(entity, request);
		entity.setPayoutMbrEntity(payoutMbr);
		return entity;
	}

	private TempPayoutCommutationCalcEntity updatePayoutCommutation(PayoutCommutationCalcDto request,
			TempPayoutCommutationCalcEntity tempPayoutCommutationCalcEntity) {
		TempPayoutCommutationCalcEntity entity = new TempPayoutCommutationCalcEntity();
		BeanUtils.copyProperties(tempPayoutCommutationCalcEntity, entity);
		convertDtoToEntity(entity, request);
		return entity;
	}

}
