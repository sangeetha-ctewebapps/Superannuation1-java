package com.lic.epgs.payout.temp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lic.epgs.claim.constants.ClaimConstants;
import com.lic.epgs.claim.constants.ClaimEntityConstants;
import com.lic.epgs.claim.entity.ClaimAnnuityCalcEntity;
import com.lic.epgs.claim.entity.ClaimCommutationCalcEntity;
import com.lic.epgs.claim.entity.ClaimEntity;
import com.lic.epgs.claim.entity.ClaimFundValueEntity;
import com.lic.epgs.claim.entity.ClaimMbrAddressEntity;
import com.lic.epgs.claim.entity.ClaimMbrAppointeeEntity;
import com.lic.epgs.claim.entity.ClaimMbrBankDetailEntity;
import com.lic.epgs.claim.entity.ClaimMbrEntity;
import com.lic.epgs.claim.entity.ClaimMbrFundValueEntity;
import com.lic.epgs.claim.entity.ClaimMbrNomineeEntity;
import com.lic.epgs.claim.entity.ClaimPayeeBankDetailsEntity;
import com.lic.epgs.claim.repository.ClaimRepository;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.payout.constants.PayoutConstants;
import com.lic.epgs.payout.constants.PayoutErrorConstants;
import com.lic.epgs.payout.constants.PayoutStatus;
import com.lic.epgs.payout.constants.PayoutStoredProcedureConstants;
import com.lic.epgs.payout.dto.PayoutAnnuityCalcDto;
import com.lic.epgs.payout.dto.PayoutCheckerActionRequestDto;
import com.lic.epgs.payout.dto.PayoutCommutationCalcDto;
import com.lic.epgs.payout.dto.PayoutDocumentDetailDto;
import com.lic.epgs.payout.dto.PayoutDto;
import com.lic.epgs.payout.dto.PayoutFundValueDto;
import com.lic.epgs.payout.dto.PayoutMakerActionRequestDto;
import com.lic.epgs.payout.dto.PayoutMbrAddressDto;
import com.lic.epgs.payout.dto.PayoutMbrAppointeeDto;
import com.lic.epgs.payout.dto.PayoutMbrBankDetailDto;
import com.lic.epgs.payout.dto.PayoutMbrDto;
import com.lic.epgs.payout.dto.PayoutMbrFundValueDto;
import com.lic.epgs.payout.dto.PayoutMbrNomineeDto;
import com.lic.epgs.payout.dto.PayoutNotesDto;
import com.lic.epgs.payout.dto.PayoutOnboardingRequestDto;
import com.lic.epgs.payout.dto.PayoutOnboardingResponseDto;
import com.lic.epgs.payout.dto.PayoutPayeeBankDetailsDto;
import com.lic.epgs.payout.dto.PayoutUpdateRequestDto;
import com.lic.epgs.payout.service.SavePayoutService;
import com.lic.epgs.payout.temp.entity.PayoutPayeeBankDetailsTempEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutAnnuityCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutCommutationCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutFundValueEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrAddressEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrAppointeeEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrBankDetailEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrFundValueEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrNomineeEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutNotesEntity;
import com.lic.epgs.payout.temp.repository.PayoutPayeeBankDetailsTempRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutAnnuityCalcRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutCommutationCalcRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutFundValueRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrAddressRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrAppointeeRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrBankDtlsRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrFundValueRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrNomineeRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutRepository;
import com.lic.epgs.payout.temp.service.TempPayoutService;
import com.lic.epgs.payout.temp.service.TempSavePayoutService;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.DateUtils;

@Service
@Transactional
public class TempPayoutServiceImpl implements TempPayoutService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	ClaimRepository claimRepository;

	@Autowired
	TempPayoutRepository tempPayoutRepository;

	@Autowired
	TempPayoutMbrRepository tempPayoutMbrRepository;

	@Autowired
	TempPayoutMbrAddressRepository tempPayoutMbrAddressRepository;

	@Autowired
	private CommonService commonSequenceService;

	@Autowired
	TempPayoutMbrAppointeeRepository tempPayoutMbrAppointeeRepository;

	@Autowired
	TempPayoutMbrNomineeRepository tempPayoutMbrNomineeRepository;

	@Autowired
	TempPayoutMbrBankDtlsRepository tempPayoutBankDtlsRepository;

	@Autowired
	TempPayoutAnnuityCalcRepository tempPayoutAnnuityCalcRepository;

	@Autowired
	TempPayoutCommutationCalcRepository tempPayoutCommutationCalcRepository;

	@Autowired
	TempPayoutMbrFundValueRepository tempPayoutMbrFundValueRepository;

	@Autowired
	TempPayoutFundValueRepository tempPayoutFundValueRepository;

	@Autowired
	TempSavePayoutService tempSavePayoutService;

	@Autowired
	SavePayoutService savePayoutService;

	@Autowired
	PayoutPayeeBankDetailsTempRepository payoutPayeeBankDetailsTempRepository;
//	@Autowired
//	PolicyMasterRepository policyMasterRepository;
//	
	@Autowired
	MemberMasterRepository memberMasterRepository;

//	@Autowired
//	PolicyRepository policyRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	CommonService commonService;
	
	public synchronized String getStoreProcedureSeq() {
		return commonService.getSequence(PayoutStoredProcedureConstants.BENEFIARY_PAYMENT_ID);
	}
	
	private synchronized String getPayoutSeq() {
		return commonSequenceService.getSequence(CommonConstants.PAYOUT_SEQ);
	}

	private Specification<TempPayoutEntity> findByClaimNo(String claimNo) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(ClaimEntityConstants.CLAIM_NO),
				claimNo);
	}

	@Override
	public ApiResponseDto<PayoutOnboardingResponseDto> onboard(PayoutOnboardingRequestDto request) {
		try {
			logger.info("PayoutServiceImpl:add:Start");
			if (request == null) {
				return ApiResponseDto.error(ErrorDto.builder().message("Request is null").build());
			} else {

				if (StringUtils.isBlank(request.getClaimNo())) {
					return ApiResponseDto.error(ErrorDto.builder().message("Invalid claim number").build());
				} else {

					Specification<TempPayoutEntity> specification = findByClaimNo(request.getClaimNo());

					Long count = tempPayoutRepository.count(specification);
					if (count > 0) {
						return ApiResponseDto.error(ErrorDto.builder().message("Payout already exits.").build());
					}
				}
				String payoutNo = null;

				Optional<ClaimEntity> optional = claimRepository.findByClaimNoAndIsActive(request.getClaimNo(),
						Boolean.TRUE);
				if (optional.isPresent()) {
					ClaimEntity claimEntity = optional.get();

					TempPayoutEntity tempPayoutEntity = insertPayoutEntity(claimEntity, payoutNo);

					if (claimEntity.getClaimMbr() != null) {
						ClaimMbrEntity claimMbr = claimEntity.getClaimMbr();

						TempPayoutMbrEntity payoutMbrEntity = insertPayoutMbr(claimMbr, tempPayoutEntity, payoutNo);

						insertPayoutMbrAddress(claimMbr.getClaimMbrAddresses(), payoutMbrEntity, payoutNo);

						insertPayoutMbrAppointee(claimMbr.getClaimMbrAppointeeDtls(), payoutMbrEntity, payoutNo);

						insertPayoutMbrBank(claimMbr.getClaimMbrBankDetails(), payoutMbrEntity, payoutNo);

						insertPayoutMbrNominee(claimMbr.getClaimMbrNomineeDtls(), payoutMbrEntity, payoutNo);

						insertPayoutMbrAnnuity(claimMbr.getClaimAnuityCalc(), payoutMbrEntity, payoutNo);

						insertPayoutMbrCommutation(claimMbr.getClaimCommutationCalc(), payoutMbrEntity, payoutNo);

						insertPayoutFundValue(claimMbr.getClaimFundValue(), payoutMbrEntity, payoutNo);

						insertPayoutMbrFundValue(claimMbr.getClaimMbrFundValue(), payoutMbrEntity, payoutNo);

						insertPayoutMbrBankPayeeValue(claimMbr.getClaimPayeeTempBank(), payoutMbrEntity, payoutNo);
						
						
						memberStatusUpdated(tempPayoutEntity,payoutMbrEntity);
					}

				} else {
					return ApiResponseDto.error(ErrorDto.builder().message("Invalid claim number").build());
				}

				return ApiResponseDto.success(new PayoutOnboardingResponseDto(payoutNo));
			}
		} catch (

		IllegalArgumentException e) {
			logger.error("Exception:PayoutServiceImpl:add", e);
			return ApiResponseDto.error(ErrorDto.builder().message("Error onboarding payouts").build());
		} finally {
			logger.info("PayoutServiceImpl:add:Ends");
		}
	}
	
	/****** @PolicyMember Updated *****/
	private void memberStatusUpdated(TempPayoutEntity claimEntity,TempPayoutMbrEntity payoutMbrEntity) {
		memberMasterRepository.updateMemberStatusByMemberId(ClaimConstants.MEMBER_PAYOUT, payoutMbrEntity.getPolicyMemberId());

		}
//	}

	private void insertPayoutMbrBankPayeeValue(List<ClaimPayeeBankDetailsEntity> result,
			TempPayoutMbrEntity payoutMbrEntity, String payoutNo) {
		if (result != null && !result.isEmpty()) {
			List<PayoutPayeeBankDetailsTempEntity> docs = result.stream()
					.map(x -> convertToPayeeBank(x, payoutMbrEntity, payoutNo)).collect(Collectors.toList());
			payoutPayeeBankDetailsTempRepository.saveAll(docs);
		}

	}

	private PayoutPayeeBankDetailsTempEntity convertToPayeeBank(ClaimPayeeBankDetailsEntity tempEntity,
			TempPayoutMbrEntity payoutMbrEntity, String payoutNo) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<ClaimPayeeBankDetailsEntity, PayoutPayeeBankDetailsTempEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		PayoutPayeeBankDetailsTempEntity entity = modelMapper.map(tempEntity, PayoutPayeeBankDetailsTempEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setClaimNo(tempEntity.getClaimNo());
		entity.setBankAccountId(null);
		entity.setMasterBankAccountId(null);
		String benefiaryPaymentId = getStoreProcedureSeq();
		if(benefiaryPaymentId != null ) {
			entity.setBenefiaryPaymentId(benefiaryPaymentId);
		}else {
			entity.setBenefiaryPaymentId(null);
		}
		entity.setVersionNo("1");
		return entity;
	}

	public void insertPayoutMbrFundValue(List<ClaimMbrFundValueEntity> result, TempPayoutMbrEntity payoutMbrEntity,
			String payoutNo) {
		if (result != null && !result.isEmpty()) {
			List<TempPayoutMbrFundValueEntity> docs = result.stream()
					.map(x -> convertToPayoutMbrFundValue(x, payoutMbrEntity, payoutNo)).collect(Collectors.toList());
			tempPayoutMbrFundValueRepository.saveAll(docs);
		}

	}

	private TempPayoutMbrFundValueEntity convertToPayoutMbrFundValue(ClaimMbrFundValueEntity tempEntity,
			TempPayoutMbrEntity payoutMbrEntity, String payoutNo) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<ClaimMbrFundValueEntity, TempPayoutMbrFundValueEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		TempPayoutMbrFundValueEntity entity = modelMapper.map(tempEntity, TempPayoutMbrFundValueEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setFundValueId(null);
		entity.setPayoutNo(payoutNo);
		return entity;
	}

	public void insertPayoutFundValue(List<ClaimFundValueEntity> result, TempPayoutMbrEntity payoutMbrEntity,
			String payoutNo) {
		if (result != null && !result.isEmpty()) {
			List<TempPayoutFundValueEntity> docs = result.stream()
					.map(x -> convertToPayoutFundValue(x, payoutMbrEntity, payoutNo)).collect(Collectors.toList());
			tempPayoutFundValueRepository.saveAll(docs);
		}

	}

	private TempPayoutFundValueEntity convertToPayoutFundValue(ClaimFundValueEntity tempEntity,
			TempPayoutMbrEntity payoutMbrEntity, String payoutNo) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<ClaimFundValueEntity, TempPayoutFundValueEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		TempPayoutFundValueEntity entity = modelMapper.map(tempEntity, TempPayoutFundValueEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setFundValueId(null);
		entity.setPayoutNo(payoutNo);
		return entity;
	}

	private void insertPayoutMbrCommutation(List<ClaimCommutationCalcEntity> result,
			TempPayoutMbrEntity payoutMbrEntity, String payoutNo) {
		if (result != null && !result.isEmpty()) {
			List<TempPayoutCommutationCalcEntity> docs = result.stream()
					.map(x -> convertToCommutation(x, payoutMbrEntity, payoutNo)).collect(Collectors.toList());
			tempPayoutCommutationCalcRepository.saveAll(docs);
		}

	}

	private TempPayoutCommutationCalcEntity convertToCommutation(ClaimCommutationCalcEntity tempEntity,
			TempPayoutMbrEntity payoutMbrEntity, String payoutNo) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<ClaimCommutationCalcEntity, TempPayoutCommutationCalcEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		TempPayoutCommutationCalcEntity entity = modelMapper.map(tempEntity, TempPayoutCommutationCalcEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setCommunityId(null);
		// entity.setPayoutNo(payoutNo);
		return entity;
	}

	private void insertPayoutMbrAnnuity(List<ClaimAnnuityCalcEntity> result, TempPayoutMbrEntity payoutMbrEntity,
			String payoutNo) {

		if (result != null && !result.isEmpty()) {
			List<TempPayoutAnnuityCalcEntity> docs = result.stream()
					.map(x -> convertToAnnuity(x, payoutMbrEntity, payoutNo)).collect(Collectors.toList());
			tempPayoutAnnuityCalcRepository.saveAll(docs);
		}

	}

	private TempPayoutAnnuityCalcEntity convertToAnnuity(ClaimAnnuityCalcEntity tempEntity,
			TempPayoutMbrEntity payoutMbrEntity, String payoutNo) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<ClaimAnnuityCalcEntity, TempPayoutAnnuityCalcEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		TempPayoutAnnuityCalcEntity entity = modelMapper.map(tempEntity, TempPayoutAnnuityCalcEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setAnnuityId(null);
		// entity.setPayoutNo(payoutNo);
		return entity;
	}

	private void insertPayoutMbrNominee(List<ClaimMbrNomineeEntity> result, TempPayoutMbrEntity payoutMbrEntity,
			String payoutNo) {
		if (result != null && !result.isEmpty()) {
			List<TempPayoutMbrNomineeEntity> docs = result.stream()
					.map(x -> convertToNominee(x, payoutMbrEntity, payoutNo)).collect(Collectors.toList());
			tempPayoutMbrNomineeRepository.saveAll(docs);
		}

	}

	private TempPayoutMbrNomineeEntity convertToNominee(ClaimMbrNomineeEntity tempEntity,
			TempPayoutMbrEntity payoutMbrEntity, String payoutNo) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<ClaimMbrNomineeEntity, TempPayoutMbrNomineeEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		TempPayoutMbrNomineeEntity entity = modelMapper.map(tempEntity, TempPayoutMbrNomineeEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setNomineeId(null);
		entity.setDob(tempEntity.getDob());
		entity.setPayoutNo(payoutNo);
		return entity;
	}

	private void insertPayoutMbrBank(List<ClaimMbrBankDetailEntity> result, TempPayoutMbrEntity payoutMbrEntity,
			String payoutNo) {
		if (result != null && !result.isEmpty()) {
			List<TempPayoutMbrBankDetailEntity> docs = result.stream()
					.map(x -> convertToBank(x, payoutMbrEntity, payoutNo)).collect(Collectors.toList());
			tempPayoutBankDtlsRepository.saveAll(docs);
		}

	}

	private TempPayoutMbrBankDetailEntity convertToBank(ClaimMbrBankDetailEntity tempEntity,
			TempPayoutMbrEntity payoutMbrEntity, String payoutNo) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<ClaimMbrBankDetailEntity, TempPayoutMbrBankDetailEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		TempPayoutMbrBankDetailEntity entity = modelMapper.map(tempEntity, TempPayoutMbrBankDetailEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setPayoutNo(payoutNo);
		entity.setBankId(null);
		return entity;
	}

	private void insertPayoutMbrAppointee(List<ClaimMbrAppointeeEntity> result, TempPayoutMbrEntity payoutMbrEntity,
			String payoutNo) {
		if (result != null && !result.isEmpty()) {
			List<TempPayoutMbrAppointeeEntity> docs = result.stream()
					.map(x -> convertToAppointee(x, payoutMbrEntity, payoutNo)).collect(Collectors.toList());
			tempPayoutMbrAppointeeRepository.saveAll(docs);
		}
	}

	private TempPayoutMbrAppointeeEntity convertToAppointee(ClaimMbrAppointeeEntity tempEntity,
			TempPayoutMbrEntity payoutMbrEntity, String payoutNo) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<ClaimMbrAppointeeEntity, TempPayoutMbrAppointeeEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		TempPayoutMbrAppointeeEntity entity = modelMapper.map(tempEntity, TempPayoutMbrAppointeeEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setPayoutNo(payoutNo);
		entity.setAppointeeId(null);
		return entity;
	}

	private void insertPayoutMbrAddress(List<ClaimMbrAddressEntity> result, TempPayoutMbrEntity payoutMbrEntity,
			String payoutNo) {
		if (result != null && !result.isEmpty()) {
			List<TempPayoutMbrAddressEntity> docs = result.stream()
					.map(x -> convertToAddress(x, payoutMbrEntity, payoutNo)).collect(Collectors.toList());
			tempPayoutMbrAddressRepository.saveAll(docs);
		}
	}

	private TempPayoutMbrAddressEntity convertToAddress(ClaimMbrAddressEntity tempEntity,
			TempPayoutMbrEntity payoutMbrEntity, String payoutNo) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<ClaimMbrAddressEntity, TempPayoutMbrAddressEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		TempPayoutMbrAddressEntity entity = modelMapper.map(tempEntity, TempPayoutMbrAddressEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setPayoutNo(payoutNo);
		entity.setAddressId(null);
		return entity;
	}

	private TempPayoutMbrEntity insertPayoutMbr(ClaimMbrEntity claimMbr, TempPayoutEntity tempPayoutEntity,
			String payoutNo) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<ClaimMbrEntity, TempPayoutMbrEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayout());
				skip(destination.getPayoutAnuityCalc());
				skip(destination.getPayoutCommutationCalc());
				skip(destination.getPayoutMbrAppointeeDtls());
				skip(destination.getPayoutMbrNomineeDtls());
				skip(destination.getPayoutMbrAddresses());
				skip(destination.getPayoutMbrBankDetails());
				skip(destination.getPayoutFundValue());
				skip(destination.getPayoutMbrFundValue());
			}
		});
		TempPayoutMbrEntity entity = modelMapper.map(claimMbr, TempPayoutMbrEntity.class);
		entity.setPayout(tempPayoutEntity);
		entity.setPayoutNo(payoutNo);
		entity.setMemberId(null);
		entity.setMemberStatus(ClaimConstants.MEMBER_PAYOUT);
		entity = tempPayoutMbrRepository.save(entity);
		return entity;
	}

	private TempPayoutEntity insertPayoutEntity(ClaimEntity claimEntity, String payoutNo) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<ClaimEntity, TempPayoutEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbr());
			}
		});
		TempPayoutEntity entity = modelMapper.map(claimEntity, TempPayoutEntity.class);
		entity.setPayoutId(null);
		entity.setPayoutNo(payoutNo);
		entity.setInitiMationNo(claimEntity.getClaimOnboarding().getInitiMationNo());
		entity.setStatus(PayoutStatus.ONBORADED.val());
		entity = tempPayoutRepository.save(entity);
		return entity;
	}

//	@Override
//	public ApiResponseDto<String> updateMakerAction(PayoutMakerActionRequestDto request) {
//		Optional<TempPayoutEntity> result = tempPayoutRepository
//				.findByInitiMationNoAndIsActive(request.getInitiMationNo(), Boolean.TRUE);
//		if (result.isPresent()) {
//			TempPayoutEntity payoutEntity = result.get();
//			TempPayoutEntity newPayoutMbrEntity = new TempPayoutEntity();
//			BeanUtils.copyProperties(payoutEntity, newPayoutMbrEntity);
//
//			if (request.getAction().equals(PayoutStatus.PRELIMINARY_REQUIREMENT_REVIEW.val())) {
//				newPayoutMbrEntity.setStatus(PayoutStatus.PRELIMINARY_REQUIREMENT_REVIEW.val());
//			} else if (request.getAction().equals(PayoutStatus.CLOSE_WITHOUT_SETTLEMENT_OTHERS.val())) {
//				newPayoutMbrEntity.setStatus(PayoutStatus.CLOSE_WITHOUT_SETTLEMENT_OTHERS.val());
//			} else if (request.getAction().equals(PayoutStatus.CLOSE_WITHOUT_SETTLEMENT_PAYOUT_WRONGLY_BOOKED.val())) {
//				newPayoutMbrEntity.setStatus(PayoutStatus.CLOSE_WITHOUT_SETTLEMENT_PAYOUT_WRONGLY_BOOKED.val());
//				newPayoutMbrEntity.setCheckerCode(request.getCheckerCode());
//				newPayoutMbrEntity.setIsActive(Boolean.FALSE);
//			} else if (request.getAction().equals(PayoutStatus.SEND_CHECKER.val())) {
//				newPayoutMbrEntity.setStatus(PayoutStatus.SEND_CHECKER.val());
//				newPayoutMbrEntity.setCheckerCode(request.getCheckerCode());
//				newPayoutMbrEntity.setIsActive(Boolean.TRUE);
//			}
//			newPayoutMbrEntity = tempSavePayoutService.insertPayout(request.getInitiMationNo(), newPayoutMbrEntity);
//			return ApiResponseDto.success(null,
//					"ClaimIntimationNo: " + request.getInitiMationNo() + " " + ClaimErrorConstants.SEND_TO_CHECKER);
//		} else {
//			return ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_INTIMATION_NO).build());
//		}
//	}
	
	/**
 * Check InterestCalculation Date Validation
 **/
private Boolean checkInterestCalculationDate(TempPayoutEntity payoutEntity) {
	logger.info("TempPayoutServiceImpl -- checkInterestCalculationDate --started");
	TempPayoutFundValueEntity tempPayoutFundValueEntity = payoutEntity.getPayoutMbr().getPayoutFundValue().get(0);
	Date fromDate = CommonDateUtils.convertStringToDate(DateUtils.sysDateStringSlash());
	Date toDate = CommonDateUtils.convertStringToDate(DateUtils.sysDateStringSlash());
	toDate = CommonDateUtils.constructeEndDateTime(toDate);
	if (tempPayoutFundValueEntity.getDateOfCalculate()!=null && tempPayoutFundValueEntity.getDateOfCalculate().compareTo(fromDate) >= 0
			&& tempPayoutFundValueEntity.getDateOfCalculate().compareTo(toDate) <= 0) {
		logger.info("TempPayoutServiceImpl -- checkInterestCalculationDate --error");

		return false;

	}
	logger.info("TempPayoutServiceImpl -- checkInterestCalculationDate --end");
	return true;
}

@Override
	public ApiResponseDto<String> updateMakerAction(PayoutMakerActionRequestDto request) {
		Optional<TempPayoutEntity> result = tempPayoutRepository
				.findByInitiMationNoAndIsActive(request.getInitiMationNo(), Boolean.TRUE);
		if (result.isPresent()) {
			TempPayoutEntity payoutEntity = result.get();
			
			if (payoutEntity.getPolicyType().equalsIgnoreCase(ClaimConstants.DC) && checkInterestCalculationDate(payoutEntity)) {
				return ApiResponseDto
						.error(ErrorDto.builder().message(PayoutErrorConstants.INTEREST_DATE_VALIDATE).build());
			}
			
			TempPayoutEntity newPayoutMbrEntity = new TempPayoutEntity();
			BeanUtils.copyProperties(payoutEntity, newPayoutMbrEntity);
			if (request.getAction().equals(PayoutStatus.PRELIMINARY_REQUIREMENT_REVIEW.val())) {
				newPayoutMbrEntity.setStatus(PayoutStatus.PRELIMINARY_REQUIREMENT_REVIEW.val());
			} else if (request.getAction().equals(PayoutStatus.CLOSE_WITHOUT_SETTLEMENT_OTHERS.val())) {
				newPayoutMbrEntity.setStatus(PayoutStatus.CLOSE_WITHOUT_SETTLEMENT_OTHERS.val());
			} else if (request.getAction().equals(PayoutStatus.CLOSE_WITHOUT_SETTLEMENT_PAYOUT_WRONGLY_BOOKED.val())) {
				newPayoutMbrEntity.setStatus(PayoutStatus.CLOSE_WITHOUT_SETTLEMENT_PAYOUT_WRONGLY_BOOKED.val());
				newPayoutMbrEntity.setCheckerCode(request.getCheckerCode());
				newPayoutMbrEntity.setIsActive(Boolean.FALSE);
			} else if (request.getAction().equals(PayoutStatus.SEND_CHECKER.val())) {
				newPayoutMbrEntity.setStatus(PayoutStatus.SEND_CHECKER.val());
				newPayoutMbrEntity.setCheckerCode(request.getCheckerCode());
				newPayoutMbrEntity.setIsActive(Boolean.TRUE);
			}
			TempPayoutEntity tempPayoutEntityRes=tempSavePayoutService.insertPayout(request.getInitiMationNo(), newPayoutMbrEntity);
			
			
			return ApiResponseDto.success(null, "ClaimIntimationNo :"+tempPayoutEntityRes.getInitiMationNo()+" "+PayoutErrorConstants.SEND_TO_CHECKER);
		} else {
			return ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
		}
	}

	@Override
	public ApiResponseDto<String> updateCheckerAction(PayoutCheckerActionRequestDto request) {
		Optional<TempPayoutEntity> result = tempPayoutRepository
				.findByInitiMationNoAndIsActive(request.getInitiMationNo(), Boolean.TRUE);

		String status = null;
		if (result.isPresent()) {
			TempPayoutEntity payoutEntity = result.get();
			TempPayoutEntity newPayoutMbrEntity = new TempPayoutEntity();
			BeanUtils.copyProperties(payoutEntity, newPayoutMbrEntity);
			newPayoutMbrEntity.setPayoutId(null);
			newPayoutMbrEntity.setModifiedBy(request.getModifiedBy());
			newPayoutMbrEntity.setModifiedOn(CommonDateUtils.sysDate());
			if (request.getAction().equals(PayoutStatus.APPROVE.val())) {
				newPayoutMbrEntity.setStatus(PayoutStatus.APPROVE.val());
				String payoutNo = getPayoutSeq();
				newPayoutMbrEntity.setPayoutNo(payoutNo);
				status = PayoutErrorConstants.APPROVED;
			} else if (request.getAction().equals(PayoutStatus.REJECT.val())) {
				newPayoutMbrEntity.setStatus(PayoutStatus.REJECT.val());
				String payoutNo = getPayoutSeq();
				newPayoutMbrEntity.setPayoutNo(payoutNo);
				status = PayoutErrorConstants.REJECT;
			} else if (request.getAction().equals(PayoutStatus.SEND_TO_MAKER.val())) {
				newPayoutMbrEntity.setStatus(PayoutStatus.SEND_TO_MAKER.val());
				newPayoutMbrEntity.setPayoutNo(null);
				status = PayoutErrorConstants.SEND_TO_MAKER;
			}

			newPayoutMbrEntity = tempSavePayoutService.insertPayout(request.getInitiMationNo(), newPayoutMbrEntity);

			return ApiResponseDto.success(null, "ClaimIntimationNo : " + request.getInitiMationNo() + " " + status);

		} else {
			return ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
		}
	}

	@Override
	public ApiResponseDto<PayoutDto> update(PayoutUpdateRequestDto request) {
		ApiResponseDto<PayoutDto> responseDto=new ApiResponseDto<>();
		try {
			Optional<TempPayoutEntity> result = tempPayoutRepository.findByPayoutNoAndIsActive(request.getPayoutNo(),
					Boolean.TRUE);
			if (result.isPresent()) {
				TempPayoutEntity payoutEntity = result.get();
				TempPayoutEntity newPayoutMbrEntity = new TempPayoutEntity();
				BeanUtils.copyProperties(payoutEntity, newPayoutMbrEntity);

				newPayoutMbrEntity.setDtOfExit(CommonDateUtils.stringDateToDateFormatter(request.getDateOfExit()));
				newPayoutMbrEntity.setModeOfExit(request.getModeOfExit());
				newPayoutMbrEntity.setOtherReason(request.getReasonForOther());
				newPayoutMbrEntity.setPlaceOfEvent(request.getPlaceOfEvent());
				newPayoutMbrEntity.setReasonExit(request.getReasonForExit());

				newPayoutMbrEntity = tempSavePayoutService.insertPayout(request.getPayoutNo(), newPayoutMbrEntity);

				responseDto= ApiResponseDto.success(convertEntityToDto(newPayoutMbrEntity),
						PayoutErrorConstants.UPDATED_SUCCESSFULLY);
			} else {
				responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
			}
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseDto;
	}

	private PayoutDto convertEntityToDto(TempPayoutEntity tempPayoutsEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempPayoutEntity, PayoutDto>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbr().getPayoutAnuityCalc());
				skip(destination.getPayoutMbr().getPayoutCommutationCalc());
				skip(destination.getPayoutMbr().getPayoutMbrAppointeeDtls());
				skip(destination.getPayoutMbr().getPayoutMbrNomineeDtls());
				skip(destination.getPayoutMbr().getPayoutMbrAddresses());
				skip(destination.getPayoutMbr().getPayoutMbrBankDetails());
				skip(destination.getPayoutMbr().getPayoutMbrFundValue());
				skip(destination.getPayoutMbr().getPayoutFundValue());
			}
		});
		return modelMapper.map(tempPayoutsEntity, PayoutDto.class);
	}

	@Override
	public ApiResponseDto<PayoutDto> findPayoutDetails(String initiMationNo) {
		Optional<TempPayoutEntity> result = tempPayoutRepository.findByInitiMationNoAndIsActive(initiMationNo,
				Boolean.TRUE);
		if (result.isPresent()) {
			TempPayoutEntity payoutEntity = result.get();

			/**
			 * PayoutDto payoutDto = new PayoutDto();
			 * 
			 * payoutDto.setPayoutDocDetails(null); payoutDto.setPayoutMbr(null);
			 * payoutDto.setPayoutOnboarding(null);
			 */

			/** PayoutDto payoutDto = modelMapper.map(payoutEntity, PayoutDto.class); */
			return ApiResponseDto.success(payoutDto(payoutEntity));
		} else {
			return ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
		}
	}

	
	public PayoutDto payoutDto(TempPayoutEntity from) {
		String claimOBNo=claimRepository.fetchByClaimNoAndIsActive(from.getClaimNo(), Boolean.TRUE);

		PayoutDto to = new PayoutDto();
		to.setDtOfExit(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDtOfExit()));
		BeanUtils.copyProperties(from, to);
		to.setPayoutDocDetails(payoutDocDetails(from));
		to.setPayoutMbr(payoutMbr(from));
		to.setPayoutNotes(payoutNotes(from));
		
	/*   #sumShortReserve   */ 
		Double commutatiomSum =0d;
		Double annuitySum =0d;
		if(!to.getPayoutMbr().getPayoutCommutationCalc().isEmpty()) {
			List<Double> commutaionSR=to.getPayoutMbr().getPayoutAnuityCalc().stream().map(i->i.getShortReserve()).collect(Collectors.toList());
			commutatiomSum = commutaionSR.stream().collect(Collectors.summingDouble(Double::doubleValue));
			
			}
			if(!to.getPayoutMbr().getPayoutAnuityCalc().isEmpty()) {
			List<Double> annuitySR=to.getPayoutMbr().getPayoutAnuityCalc().stream().map(i->i.getShortReserve()).collect(Collectors.toList());
			annuitySum = annuitySR.stream().collect(Collectors.summingDouble(Double::doubleValue));
			}
		to.setTotalShortReserve(commutatiomSum+annuitySum);
		/*   #sumShortReserve  {} End */ 
		to.setClaimOnBoadingNumber(claimOBNo);
		return to;
	}
	
	public List<PayoutDocumentDetailDto> payoutDocDetails(TempPayoutEntity payoutEntity) {
		List<PayoutDocumentDetailDto> tos = new ArrayList<>();
		payoutEntity.getPayoutDocDetails().forEach(from -> {
			PayoutDocumentDetailDto to = new PayoutDocumentDetailDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}
	
	public PayoutMbrDto payoutMbr(TempPayoutEntity payoutEntity) {
		TempPayoutMbrEntity from = payoutEntity.getPayoutMbr();
		PayoutMbrDto to = new PayoutMbrDto();
		to.setDateOfBirth(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDateOfBirth()));
		to.setDateOfJoining(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDateOfJoining()));
		to.setDateOfRetirement(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDateOfRetirement()));
		BeanUtils.copyProperties(from, to);
		to.setPayoutAnuityCalc(payoutAnuityCalc(from.getPayoutAnuityCalc()));
		to.setPayoutCommutationCalc(payoutCommutationCalc(from.getPayoutCommutationCalc()));
		to.setPayoutFundValue(payoutFundValue(from.getPayoutFundValue()));
		to.setPayoutMbrAddresses(payoutMbrAddresses(from.getPayoutMbrAddresses()));
		to.setPayoutMbrAppointeeDtls(payoutMbrAppointeeDtls(from.getPayoutMbrAppointeeDtls()));
		to.setPayoutMbrBankDetails(payoutMbrBankDetails(from.getPayoutMbrBankDetails()));
		to.setPayoutMbrFundValue(payoutMbrFundValue(from.getPayoutMbrFundValue()));
		to.setPayoutMbrNomineeDtls(payoutMbrNomineeDtls(from.getPayoutMbrNomineeDtls()));
		to.setPayoutPayeeBank(payoutPayeeBank(from.getPayoutPayeeBank()));
		return to;
	}
	
	public List<PayoutAnnuityCalcDto> payoutAnuityCalc(List<TempPayoutAnnuityCalcEntity> froms) {
		List<PayoutAnnuityCalcDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutAnnuityCalcDto to = new PayoutAnnuityCalcDto();
			BeanUtils.copyProperties(from, to);
			to.setSpouseDob(DateUtils.dateToStringDDMMYYYY( from.getDateOfBirth()));
			tos.add(to);
		});
		return tos;
	}
	
	public List<PayoutCommutationCalcDto> payoutCommutationCalc(List<TempPayoutCommutationCalcEntity> froms) {
		List<PayoutCommutationCalcDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutCommutationCalcDto to = new PayoutCommutationCalcDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}
	
	public List<PayoutFundValueDto> payoutFundValue(List<TempPayoutFundValueEntity> froms) {
		List<PayoutFundValueDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutFundValueDto to = new PayoutFundValueDto();
			BeanUtils.copyProperties(from, to);
			to.setSpouseDOB(from.getSpouseDob()!=null?DateUtils.dateToStringDDMMYYYY(from.getSpouseDob()):"");
			tos.add(to);
		});
		return tos;
	}
	
	public List<PayoutMbrAddressDto> payoutMbrAddresses(List<TempPayoutMbrAddressEntity> froms) {
		List<PayoutMbrAddressDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrAddressDto to = new PayoutMbrAddressDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}
	
	public List<PayoutMbrAppointeeDto> payoutMbrAppointeeDtls(List<TempPayoutMbrAppointeeEntity> froms) {
		List<PayoutMbrAppointeeDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrAppointeeDto to = new PayoutMbrAppointeeDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}
	
	public List<PayoutMbrBankDetailDto> payoutMbrBankDetails(List<TempPayoutMbrBankDetailEntity> froms) {
		List<PayoutMbrBankDetailDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrBankDetailDto to = new PayoutMbrBankDetailDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}
	
	public List<PayoutMbrFundValueDto> payoutMbrFundValue(List<TempPayoutMbrFundValueEntity> froms) {
		List<PayoutMbrFundValueDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrFundValueDto to = new PayoutMbrFundValueDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}
	
	public List<PayoutMbrNomineeDto> payoutMbrNomineeDtls(List<TempPayoutMbrNomineeEntity> froms) {
		List<PayoutMbrNomineeDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrNomineeDto to = new PayoutMbrNomineeDto();
			BeanUtils.copyProperties(from, to);
			to.setDob(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDob()));
			tos.add(to);
		});
		return tos;
	}
	
	public List<PayoutPayeeBankDetailsDto> payoutPayeeBank(List<PayoutPayeeBankDetailsTempEntity> froms) {
		List<PayoutPayeeBankDetailsDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutPayeeBankDetailsDto to = new PayoutPayeeBankDetailsDto();
			BeanUtils.copyProperties(from, to);
			to.setBankAddressOne(from.getBankAddressOne());
			tos.add(to);
		});
		return tos;
	}
	
	public List<PayoutNotesDto> payoutNotes(TempPayoutEntity payoutEntity) {
		List<TempPayoutNotesEntity> froms = payoutEntity.getPayoutNotes();
		List<PayoutNotesDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutNotesDto to = new PayoutNotesDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
		
	}

}
