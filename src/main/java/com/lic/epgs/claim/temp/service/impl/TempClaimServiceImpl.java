package com.lic.epgs.claim.temp.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Join;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.lic.epgs.claim.constants.ClaimConstants;
import com.lic.epgs.claim.constants.ClaimEntityConstants;
import com.lic.epgs.claim.constants.ClaimErrorConstants;
import com.lic.epgs.claim.constants.ClaimOnboardStatus;
import com.lic.epgs.claim.constants.ClaimStatus;
import com.lic.epgs.claim.dto.ClaimAnnuityCalcDto;
import com.lic.epgs.claim.dto.ClaimCheckerActionRequestDto;
import com.lic.epgs.claim.dto.ClaimDto;
import com.lic.epgs.claim.dto.ClaimFundExportDto;
import com.lic.epgs.claim.dto.ClaimMakerActionRequestDto;
import com.lic.epgs.claim.dto.ClaimOnboardingDto;
import com.lic.epgs.claim.dto.ClaimOnboardingRequestDto;
import com.lic.epgs.claim.dto.ClaimOnboardingResponseDto;
import com.lic.epgs.claim.dto.ClaimUpdateRequestDto;
import com.lic.epgs.claim.repository.ClaimOnboardingRepository;
import com.lic.epgs.claim.service.ClaimService;
import com.lic.epgs.claim.service.SaveClaimService;
import com.lic.epgs.claim.service.impl.ClaimServiceImpl;
import com.lic.epgs.claim.temp.entity.TempClaimAnnuityCalcEntity;
import com.lic.epgs.claim.temp.entity.TempClaimEntity;
import com.lic.epgs.claim.temp.entity.TempClaimFundValueEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrAddressEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrAppointeeEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrBankDetailEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrNomineeEntity;
import com.lic.epgs.claim.temp.entity.TempClaimOnboardingEntity;
import com.lic.epgs.claim.temp.repository.TempClaimFundValueRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrAddressRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrAppointeeRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrBankDtlsRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrNomineeRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrRepository;
import com.lic.epgs.claim.temp.repository.TempClaimOnboardingRepository;
import com.lic.epgs.claim.temp.repository.TempClaimRepository;
import com.lic.epgs.claim.temp.service.TempClaimService;
import com.lic.epgs.claim.temp.service.TempSaveClaimService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.CommonResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.dto.FundRequestDto;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.fund.constants.InterestFundConstants;
import com.lic.epgs.fund.service.FundRestApiService;
import com.lic.epgs.integration.dto.InterestFundResponseDto;
import com.lic.epgs.payout.constants.PayoutStatus;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrRepository;
import com.lic.epgs.policy.entity.MemberAddressEntity;
import com.lic.epgs.policy.entity.MemberAppointeeEntity;
import com.lic.epgs.policy.entity.MemberBankEntity;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MemberNomineeEntity;
import com.lic.epgs.policy.entity.MemberTransactionSummaryEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.PolicyRulesEntity;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.MemberTransactionSummaryRepository;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.policy.repository.PolicyRulesRepository;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;


@Service
@Transactional
public class TempClaimServiceImpl implements TempClaimService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TempClaimRepository tempClaimRepository;

	@Autowired
	TempClaimMbrRepository tempClaimDtlsRepository;

	@Autowired
	TempClaimOnboardingRepository tempClaimOnboardingRepository;

	@Autowired
	private CommonService commonSequenceService;

	@Autowired
	TempClaimMbrAddressRepository tempClaimMbrAddressRepository;

	@Autowired
	TempClaimFundValueRepository tempClaimFundValueRepository;

	@Autowired
	TempClaimMbrAppointeeRepository tempClaimMbrAppointeeRepository;

	@Autowired
	TempClaimMbrNomineeRepository tempClaimMbrNomineeRepository;

	@Autowired
	TempClaimMbrBankDtlsRepository tempClaimBankDtlsRepository;

	@Autowired
	TempSaveClaimService tempSaveClaimService;

	@Autowired
	SaveClaimService saveClaimService;

	@Autowired
	PolicyRulesRepository policyRulesRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ClaimOnboardingRepository claimOnboardingRepository;

	@Autowired

	MemberMasterRepository memberMasterRepository;

	@Autowired
	MphMasterRepository mphMasterRepository;

	@Autowired
	private FundRestApiService fundRestApiService;

	@Autowired
	PolicyMasterRepository policyMasterRepository;

	@Autowired
	CommonService commonService;
	@Autowired
	ClaimService claimService;

	@Autowired
	MemberTransactionSummaryRepository memberTransactionSummaryRepository;
	
	@Autowired
	TempPayoutMbrRepository tempPayoutMbrRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate; 
	
	@Autowired
	ClaimServiceImpl claimServiceImpl;
	

	private synchronized String getClaimSeq() {
		return commonSequenceService.getSequence(CommonConstants.CLAIM_SEQ);
	}

	/*
	 * private synchronized String getClaimIntiMationSequence() { return
	 * commonSequenceService.getSequence(CommonConstants.CLAIM_INTIMATION_SEQ); }
	 */

	private synchronized String getClaimOnBoardingSequence() {
		return commonSequenceService.getSequence(CommonConstants.CLAIM_OB_SEQ);
	}

	private synchronized String getClaimNomineeSequence() {
		return commonSequenceService.getSequence(CommonConstants.CLAIM_NOMINEE_SEQ);
	}

	private Specification<TempClaimMbrEntity> findByPolicyNoAndMemberIdAndMemberStatus(String policyNo, Long memberId,
			String status) {
		return (root, query, criteriaBuilder) -> {
			Join<TempClaimMbrEntity, TempClaimEntity> claim = root.join(ClaimEntityConstants.CLAIM);
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.POLICY_NO), policyNo));
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.IS_ACTIVE), Boolean.TRUE));
			criteriaBuilder.equal(root.get(ClaimEntityConstants.MEMBER_STATUS), status);
			return criteriaBuilder.equal(root.get(ClaimEntityConstants.POLICY_MEMBER_ID), memberId);
		};
	}
	
	private Boolean checkMemberExistOrnot(ClaimOnboardingRequestDto request) {
		logger.info("TempClaimServiceImpl--checkMemberExistOrnot--start");
		TempClaimMbrEntity tempClaimMbrEntity=tempClaimDtlsRepository.checkInitimationMemberExit(request.getLicId(),request.getPolicyNo(),ClaimStatus.REJECT.val());
		if(tempClaimMbrEntity==null) {
		TempPayoutMbrEntity tempPayoutMbrEntity=tempPayoutMbrRepository.checkPayoutMemberExit(request.getLicId(),request.getPolicyNo(),PayoutStatus.REJECT.val());
		if(tempPayoutMbrEntity==null) {
			return true;
		}
		else {
			return true;
			
		}
		}
		else {
			return true;
		}}
	
	

	@Override
	@Transactional
	public ApiResponseDto<ClaimOnboardingResponseDto> onboard(ClaimOnboardingRequestDto request) {
		try {
			logger.info("ClaimServiceImpl:add:Start");
			if (request != null) {
				if (StringUtils.isBlank(request.getPolicyNo())) {
					return ApiResponseDto.error(ErrorDto.builder().message("Invalid policy number").build());
				}
				if (StringUtils.isBlank(request.getUnitCode())) {
					return ApiResponseDto.error(ErrorDto.builder().message("Invalid unit code").build());
				}
				if (request.getMemberId() == null) {
					return ApiResponseDto.error(ErrorDto.builder().message("Invalid member id").build());
				} else {
					Boolean exist=checkMemberExistOrnot(request);
					if(exist.equals(false)) {
						return ApiResponseDto.error(ErrorDto.builder().message("Claim already exits.").build());
					}
//					Specification<TempClaimMbrEntity> specification = findByPolicyNoAndMemberIdAndMemberStatus(
//							request.getPolicyNo(), request.getMemberId(), ClaimConstants.ACTIVE);
//
//					Long count = tempClaimDtlsRepository.count(specification);
//					if (count > 0) {
//						return ApiResponseDto.error(ErrorDto.builder().message("Claim already exits.").build());
//					}
				}
				logger.info("policyDetails :" + System.currentTimeMillis() + "start");
				PolicyMasterEntity policyDetails = commonService.findPolicyDetails(null, request.getPolicyId());

				logger.info("policyDetails :" + System.currentTimeMillis() + "end");

				logger.info("getFinancialYeartDetails :" + System.currentTimeMillis() + "start");
				CommonResponseDto response = commonService
						.getFinancialYeartDetails(DateUtils.dateToHypenStringDDMMYYYY(DateUtils.sysDate()));
				logger.info("getFinancialYeartDetails :" + System.currentTimeMillis() + "End");
				if (response == null) {
					return ApiResponseDto.error(ErrorDto.builder().message("Invalid Financial Year").build());
				}
				if (policyDetails != null) {

					logger.info("member :" + System.currentTimeMillis() + "start");
					MemberMasterEntity member = new MemberMasterEntity();

					member = memberMasterRepository.fetchByPolicyIdAndLicIdAndMemberStatusAndIsActiveTrue(
							request.getPolicyId(), request.getLicId(), Boolean.TRUE);
					logger.info("member :" + System.currentTimeMillis() + "end");
					if (member != null) {

						String claimNo = getClaimSeq();
						logger.info("mphMasterEntity :" + System.currentTimeMillis() + "start");
						MphMasterEntity mphMasterEntity = commonService.findMphDetails(policyDetails.getMphId());
						logger.info("mphMasterEntity :" + System.currentTimeMillis() + "end");
						if (mphMasterEntity == null) {
							return ApiResponseDto.error(ErrorDto.builder().message("Invalid Mph Details").build());

						}
						String onBoardingNo = getClaimOnBoardingSequence();

						TempClaimOnboardingEntity tempClaimOnboardingEntity = insertOnboardingDetails(request,
								onBoardingNo);

						TempClaimEntity claimsEntity = insertTempClaimEntity(request, claimNo,
								tempClaimOnboardingEntity, policyDetails, mphMasterEntity);
						logger.info("policyRulesEntity :" + System.currentTimeMillis() + "start");
						PolicyRulesEntity policyRulesEntity = policyRulesRepository
								.findTopByPolicyIdAndCategory(policyDetails.getPolicyId(), request.getCategory());
						logger.info("policyRulesEntity :" + System.currentTimeMillis() + "End");
						if (policyRulesEntity == null) {
							return ApiResponseDto
									.error(ErrorDto.builder().message("Member BenefitType Not Mapped").build());
						}

						MemberTransactionSummaryEntity memberTransactionSummaryEntity = new MemberTransactionSummaryEntity();
						if (policyDetails.getPolicyType().equals(CommonConstants.DC)) {
							if (claimsEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V2)) {
								memberTransactionSummaryEntity = memberTransactionSummaryRepository
										.findTopByPolicyIdAndMemberIdAndFinancialYearAndQuarterAndIsActiveTrueOrderByFinancialYearDesc(
												policyDetails.getPolicyId(), request.getLicId(),
												response.getFinancialYear(),
												NumericUtils.stringToInteger(response.getFrequency()));
							} else {

								memberTransactionSummaryEntity = memberTransactionSummaryRepository
										.findTopByPolicyIdAndMemberIdAndFinancialYearAndIsActiveTrueOrderByFinancialYearDesc(
												policyDetails.getPolicyId(), request.getLicId(),
												response.getFinancialYear());
							}

							if (memberTransactionSummaryEntity == null) {
								return ApiResponseDto
										.error(ErrorDto.builder().message("Invalid FundTransaction").build());
							}
						} else {
							memberTransactionSummaryEntity = new MemberTransactionSummaryEntity();
						}
			

						TempClaimMbrEntity claimMember = insertTempClaimDetails(request, claimNo, claimsEntity,
								policyDetails, member, policyRulesEntity, mphMasterEntity,
								memberTransactionSummaryEntity);
						logger.info("policyRulesEntity :" + System.currentTimeMillis() + "End");
						ClaimDto claims = convertEntityToDto(claimsEntity);
						logger.info("policyRulesEntity :" + System.currentTimeMillis() + "End");
						if (claims.getStatus().equals(ClaimStatus.ONBORADED.val())) {
							memberStatusUpdated(member.getMemberId());
						}

						return ApiResponseDto.success(new ClaimOnboardingResponseDto(claimNo, onBoardingNo, claims));

					} else {
						return ApiResponseDto.error(ErrorDto.builder().message("Already member existed").build());
					}
				} else {
					return ApiResponseDto.error(ErrorDto.builder().message("Invalid policy number").build());
				}
	
			} else {
				return ApiResponseDto.error(ErrorDto.builder().message("Request is null").build());
			}
		} catch (IllegalArgumentException | ApplicationException e) {
			logger.error("Exception:ClaimServiceImpl:add", e);
			return ApiResponseDto
					.error(ErrorDto.builder().message("Error onboarding claims:" + e.getMessage()).build());
		} finally {
			logger.info("ClaimServiceImpl:add:Ends");
		}
	}

	/****** @PolicyMember Updated *****/
	private void memberStatusUpdated(Long memberId) {
		logger.info("memberStatusUpdated :" + System.currentTimeMillis() + "start");
		memberMasterRepository.updateMemberStatusByMemberId(ClaimConstants.MEMBER_ONBOARD, memberId);
		logger.info("memberStatusUpdated :" + System.currentTimeMillis() + "end");
	}

	private TempClaimEntity insertTempClaimEntity(ClaimOnboardingRequestDto request, String claimNo,
			TempClaimOnboardingEntity tempClaimOnboardingEntity, PolicyMasterEntity policyDetails,
			MphMasterEntity mphMasterEntity) {
		TempClaimEntity claimsEntity = new TempClaimEntity();
		claimsEntity.setClaimNo(claimNo);
		claimsEntity.setCreatedBy(request.getCreatedBy());
		claimsEntity.setCreatedOn(CommonDateUtils.sysDate());
		claimsEntity.setModifiedOn(CommonDateUtils.sysDate());
		claimsEntity.setIsActive(Boolean.TRUE);

		claimsEntity.setDtOfExit(CommonDateUtils.stringDateToDateFormatter(request.getDateOfExit()));
		claimsEntity.setModeOfExit(request.getModeOfExit());
		claimsEntity.setOtherReason(request.getReasonForOther());
		claimsEntity.setOtherReasonForDeath(request.getOtherReasonForDeath());
		claimsEntity.setPlaceOfEvent(request.getPlaceOfEvent());
		claimsEntity.setReasonExit(request.getReasonForExit());
		claimsEntity.setStatus(ClaimStatus.ONBORADED.val());
		claimsEntity.setClaimOnboarding(tempClaimOnboardingEntity);

		claimsEntity.setMasterPolicyNo(policyDetails.getPolicyNumber());
		claimsEntity.setMphCode(mphMasterEntity.getMphCode());
		claimsEntity.setMphName(mphMasterEntity.getMphName());
		claimsEntity.setMphId(mphMasterEntity.getMphId());
		claimsEntity.setLineOfBusiness(policyDetails.getLineOfBusiness());
		claimsEntity.setVariant(policyDetails.getVariant());
		claimsEntity.setPolicyId(policyDetails.getPolicyId());
		claimsEntity.setMasterpolicyStatus(policyDetails.getPolicyStatus());
		claimsEntity.setProductId(policyDetails.getProductId());
		claimsEntity.setUnitCode(request.getUnitCode());
		claimsEntity.setWorkflowStatus(policyDetails.getWorkflowStatus());
		claimsEntity.setDateOfDeath(CommonDateUtils.stringDateToDateFormatter(request.getDateOfDeath()));
		claimsEntity.setPolicyType(policyDetails.getPolicyType());
		claimsEntity = tempClaimRepository.save(claimsEntity);

		return claimsEntity;
	}

	private TempClaimOnboardingEntity insertOnboardingDetails(ClaimOnboardingRequestDto request, String onboardingNo) {
		TempClaimOnboardingEntity entity = new TempClaimOnboardingEntity();
		entity.setClaimOnBoardNo(onboardingNo);
		entity.setCreatedBy(request.getCreatedBy());
		entity.setCreatedOn(CommonDateUtils.sysDate());
		entity.setInitiMationNo(null);
		entity.setInitimationType(request.getInitimationType());
		entity.setOnboardingDate(CommonDateUtils.stringDateToDateFormatter(request.getOnboardedDate()));
		entity.setOnboardingStatus(ClaimOnboardStatus.SUCCESS.val());
		entity.setIsActive(Boolean.TRUE);
		return tempClaimOnboardingRepository.save(entity);
	}

	private TempClaimMbrEntity insertTempClaimDetails(ClaimOnboardingRequestDto request, String claimNo,
			TempClaimEntity claimsEntity, PolicyMasterEntity policyDetails, MemberMasterEntity member,
			PolicyRulesEntity policyRulesEntity, MphMasterEntity mphMasterEntity,
			MemberTransactionSummaryEntity contribut) throws ServiceException {
		TempClaimMbrEntity claimMbrEntity = new TempClaimMbrEntity();
		if (member != null) {
			claimMbrEntity.setAadharNumber(member.getAadharNumber());
			claimMbrEntity.setCalculationAmount(null);
			claimMbrEntity.setCalculationType(null);
			claimMbrEntity.setFirstName(member.getFirstName());
			claimMbrEntity.setLastName(member.getLastName());
			claimMbrEntity.setCategory(member.getCategoryNo());
			claimMbrEntity.setClaim(claimsEntity);
			claimMbrEntity.setClaimNo(claimNo);
			claimMbrEntity.setCommunicationPreference(member.getCommunicationPreference());
			claimMbrEntity.setCreatedBy(request.getCreatedBy());
			claimMbrEntity.setCreatedOn(CommonDateUtils.sysDate());
			claimMbrEntity.setDateOfBirth(member.getDateOfBirth());
			claimMbrEntity.setDateOfJoining(member.getDateOfJoining());
			claimMbrEntity.setDateOfJoiningScheme(member.getDateOfJoiningScheme());
			claimMbrEntity.setDateOfRetirement(member.getDateOfRetrirement());
			claimMbrEntity.setDesignation(member.getDesignation());
			claimMbrEntity.setEmailId(member.getEmailid());
			claimMbrEntity.setPolicyMemberId(member.getMemberId());
			if (contribut.getEmployeeContribution() != null) {
				claimMbrEntity.setEmployeeContribution(contribut.getEmployeeContribution().doubleValue());
			}
			if (contribut.getEmployerContribution() != null) {
				claimMbrEntity.setEmployerContribution(contribut.getEmployerContribution().doubleValue());
			}
			/**
			 * Note:-withdrawal claim default value corpusPercentage is 100%
			 **/			
			
			claimMbrEntity.setCorpusPercentage((request.getModeOfExit() == ClaimConstants.WITHDRAWAL
					|| claimsEntity.getPolicyType().equals(ClaimConstants.DB)
					|| policyRulesEntity.getPercentageCorpus() == null || policyRulesEntity.getPercentageCorpus() == 0d)
							? 100d
							: policyRulesEntity.getPercentageCorpus());

			claimMbrEntity.setFatherName(member.getFatherName());
			claimMbrEntity.setGender(member.getGender());
			claimMbrEntity.setLanguagePreference(member.getLanguagePreference());
			claimMbrEntity.setLastName(member.getLastName());
			claimMbrEntity.setLicId(member.getLicId());
			claimMbrEntity.setMemberShipId(member.getMembershipNumber());
			claimMbrEntity.setMembershipNumber(member.getMembershipNumber());
			claimMbrEntity.setMemberStatus(ClaimConstants.MEMBER_ONBOARD);
			claimMbrEntity.setMiddleName(member.getMiddleName());
			claimMbrEntity.setModifiedBy(null);
			claimMbrEntity.setModifiedOn(null);
			claimMbrEntity.setNoOfAnnuity(null);
			claimMbrEntity.setPan(member.getMemberPan());
			claimMbrEntity.setPhone(member.getMobileNo());
			claimMbrEntity.setSpouseName(member.getSpouseName());
			claimMbrEntity.setIsActive(Boolean.TRUE);
			if (contribut.getTxnAmount() != null) {
				claimMbrEntity.setTotalContribution(contribut.getTxnAmount().doubleValue());
			}
//			if (contribut.getTotalInterestedAccured() != null) {
//				claimMbrEntity.setTotalInterestAccured(contribut.getTotalInterestedAccured().doubleValue());
//			}
			claimMbrEntity.setTypeOfMembershipNo(member.getTypeOfMemebership());
			if (contribut.getVoluntaryContribution() != null) {
				claimMbrEntity.setVoluntaryContribution(contribut.getVoluntaryContribution().doubleValue());
			}
			logger.info("insertTempClaimDetails :" + System.currentTimeMillis() + "start");
			claimMbrEntity = tempClaimDtlsRepository.save(claimMbrEntity);
			logger.info("insertTempClaimDetails :" + System.currentTimeMillis() + "end");

//			claimMbrEntity.setClaimAnuityCalc(null);

//			claimMbrEntity.setClaimCommutationCalc(null);

			if (policyDetails.getPolicyType().equals(CommonConstants.DC)) {
				saveClaimFundValueEntity(member, claimNo, claimMbrEntity, contribut, claimsEntity);
			}
			saveClaimMbrAddresses(member, claimNo, claimMbrEntity);

			saveClaimMbrAppointeeDtls(member, claimNo, claimMbrEntity);

			saveClaimMbrBankDetails(member, claimNo, claimMbrEntity);

			saveClaimMbrNomineeDtls(member, claimNo, claimMbrEntity);

		}
		return claimMbrEntity;
	}

	private void saveClaimFundValueEntity(MemberMasterEntity member, String claimNo, TempClaimMbrEntity claimMbrEntity,
			MemberTransactionSummaryEntity contribut, TempClaimEntity claimsEntity) throws ServiceException {
		if (member != null) {
			TempClaimFundValueEntity entity = new TempClaimFundValueEntity();
			entity.setClaimMbrEntity(claimMbrEntity);
			entity.setClaimNo(claimNo);
			if (contribut.getEmployeeContribution() != null) {
				entity.setEmployeeContribution(
						NumericUtils.doubleRoundInMath(contribut.getEmployeeContribution().doubleValue(), 2));
			}
			if (contribut.getEmployerContribution() != null) {
				entity.setEmployerContribution(
						NumericUtils.doubleRoundInMath(contribut.getEmployerContribution().doubleValue(), 2));
			}
			if (contribut.getVoluntaryContribution() != null) {
				entity.setVoluntaryContribution(
						NumericUtils.doubleRoundInMath(contribut.getVoluntaryContribution().doubleValue(), 2));
			}
			if (contribut.getTxnAmount() != null) {
				entity.setTotalContirbution(NumericUtils.doubleRoundInMath(contribut.getTxnAmount().doubleValue(), 2));
			}
//			if (contribut.getOpeningBalance() != null) {
//				entity.setOpeningBalance(
//						NumericUtils.doubleRoundInMath(contribut.getOpeningBalance().doubleValue(), 2));
//			}
//			if (contribut.getTotalInterestedAccured() != null) {
//				entity.setTotalInterestAccured(contribut.getTotalInterestedAccured().doubleValue());
//			}
			
			entity.setCorpusPercentage(claimMbrEntity.getCorpusPercentage());
			setMemberFundDetails(member, claimsEntity, entity,contribut);
			logger.info("tempClaimFundValuesave :" + System.currentTimeMillis() + "start");
			tempClaimFundValueRepository.save(entity);
			logger.info("tempClaimFundValuesave :" + System.currentTimeMillis() + "end");
		}
	}

	public void setMemberFundDetailsReCalculate(MemberMasterEntity member, TempClaimEntity claimsEntity,
			TempClaimFundValueEntity entity) throws ApplicationException {
		Date currentDate=DateUtils.sysDate();
		FundRequestDto requestDto = new FundRequestDto();
		requestDto.setDepositDate(DateUtils.dateToStringDDMMYYYY(currentDate));
		requestDto.setTrnxDate(DateUtils.dateToStringDDMMYYYY(currentDate));
		requestDto.setIsAuto(true);
		requestDto.setIsBatch(false);
		requestDto.setIsExecuted(false);
		requestDto.setIsTxn(false);
		requestDto.setMemberId(member.getLicId());
		requestDto.setPolicyId(member.getPolicyId());
		requestDto.setRecalculate(true);
		requestDto.setSetOpeningBalance(false);
		requestDto.setTxnType(null);
		requestDto.setTxnSubType(InterestFundConstants.POLICY_CLAIM);

		logger.info("InterestFundResponseDto :" + System.currentTimeMillis() + "start");
		InterestFundResponseDto fundResponseDto = new InterestFundResponseDto();
		try {
			fundResponseDto = fundRestApiService.viewMemberFundDetails(requestDto);
		} catch (ApplicationException e) {
			logger.info(e.getMessage());
			logger.error("Error in pushing the claim amount into FUND for the payout number-{}",
					requestDto.getPolicyNumber());
		}
		logger.info("InterestFundResponseDto :" + System.currentTimeMillis() + "end");
		/**
		 * @Notes TotalIntrestAccured = OpneningBalIntr + TotalContrIntr -
		 *        TotalDebitIntr
		 */
		entity.setTotalInterestAccured(NumericUtils.doubleRoundInMath(
				NumericUtils.bigDecimalValid(fundResponseDto.getTotalContributionInterestAmount()).doubleValue()
						+ NumericUtils.bigDecimalValid(fundResponseDto.getOpeningBalanceInterestAmount()).doubleValue()
						+ NumericUtils.bigDecimalValid(fundResponseDto.getTotalDebitInterestAmount()).doubleValue(),
				2));

		/**
		 * @Notes Fund Value = TotalContr + OpeningBal + TotalIntrAccured
		 */

		entity.setFundValue(NumericUtils.doubleRoundInMath(
				entity.getTotalContirbution() + entity.getOpeningBalance() + entity.getTotalInterestAccured(), 2));
		entity.setOpeningBalance(NumericUtils.bigDecimalValid(fundResponseDto.getOpeningBalanceAmount()).doubleValue());
		entity.setDateOfCalculate(currentDate);
	}

	public void setMemberFundDetails(MemberMasterEntity member, TempClaimEntity claimsEntity,
			TempClaimFundValueEntity entity,MemberTransactionSummaryEntity contribut) throws ServiceException {
		Date currentDate = DateUtils.sysDate();
		FundRequestDto requestDto = new FundRequestDto();
		requestDto.setDepositDate(DateUtils.dateToStringDDMMYYYY(currentDate));
		requestDto.setTrnxDate(DateUtils.dateToStringDDMMYYYY(currentDate));
		requestDto.setIsAuto(true);
		requestDto.setIsBatch(false);
		requestDto.setIsExecuted(false);
		requestDto.setIsTxn(false);
		requestDto.setMemberId(member.getLicId());
		requestDto.setPolicyId(member.getPolicyId());
		requestDto.setRecalculate(true);
		requestDto.setSetOpeningBalance(false);
		requestDto.setTxnType(null);
		requestDto.setTxnSubType(InterestFundConstants.POLICY_CLAIM);

		requestDto.setTrnxDate(DateUtils.dateToStringDDMMYYYY(currentDate));
		logger.info("InterestFundResponseDto :" + System.currentTimeMillis() + "start");
		InterestFundResponseDto fundResponseDto = new InterestFundResponseDto();
		try {
			fundResponseDto = fundRestApiService.viewMemberFundDetails(requestDto);
		} catch (ApplicationException e) {
			logger.info(e.getMessage());
			logger.error("Error in pushing the claim amount into FUND for the payout number-{}",
					requestDto.getPolicyNumber());
			throw new ServiceException("Not Found viewPolicyFundDetails");
		}
		logger.info("InterestFundResponseDto :" + System.currentTimeMillis() + "end");
		/**
		 * @Notes TotalIntrestAccured = OpneningBalIntr + TotalContrIntr -
		 *        TotalDebitIntr
		 */
		entity.setTotalInterestAccured(NumericUtils.doubleRoundInMath(
				NumericUtils.bigDecimalValid(fundResponseDto.getTotalContributionInterestAmount()).doubleValue()
						+ NumericUtils.bigDecimalValid(fundResponseDto.getOpeningBalanceInterestAmount()).doubleValue()
						+ NumericUtils.bigDecimalValid(fundResponseDto.getTotalDebitInterestAmount()).doubleValue(),
				2));
		entity.setOpeningBalance(NumericUtils.bigDecimalValid(fundResponseDto.getOpeningBalanceAmount()).doubleValue());
		/**
		 * @Notes Fund Value = TotalContr + OpeningBal + TotalIntrAccured
		 */

//		getFundFmcAndGstSubtraction(requestDto.getMemberId(),)
		if(claimsEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DB_V2) || claimsEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V2)) {

			entity.setFundValue(getFundFmcAndGstSubtraction(NumericUtils.convertLongToString(member.getMemberId()),contribut.getFinancialYear(),"V2",contribut.getQuarter()));
		}
		else {
		entity.setFundValue(NumericUtils.doubleRoundInMath(
				entity.getTotalContirbution() + entity.getOpeningBalance() + entity.getTotalInterestAccured(), 2));
		}
		entity.setDateOfCalculate(currentDate);
		
	
	}
	
	
	public Double getFundFmcAndGstSubtraction(String memberId,String financialYear,String variant,Integer frequency) {
		logger.info("getFundFmcAndGstSubtraction ---start");
		CommonResponseDto dto= claimService.fundSummaryGet(memberId, financialYear, variant, frequency);
		Double fundValue=0.0;
		if(dto.getResponseData()!=null) {
			List<ClaimFundExportDto> dtoList =(List<ClaimFundExportDto>) dto.getResponseData();
		for(ClaimFundExportDto  obj: dtoList) {
//			Object[] obj1=(Object[]) obj;
			fundValue=NumericUtils.convertStringToDouble(obj.getTotal());
		}
		}	
		logger.info("getFundFmcAndGstSubtraction ---end");
		return fundValue;
		
	}
	
	
	private void saveClaimMbrNomineeDtls(MemberMasterEntity member, String claimNo, TempClaimMbrEntity claimMbrEntity) {
		if (member.getMemberNominee() != null && !member.getMemberNominee().isEmpty()) {
			List<TempClaimMbrNomineeEntity> addressList = new ArrayList<>();
			for (MemberNomineeEntity nominee : member.getMemberNominee()) {
				TempClaimMbrNomineeEntity entity = new TempClaimMbrNomineeEntity();

				entity.setAadharNumber(nominee.getAadharNumber());
//				entity.setAccountNumber(nominee.getacc);
//				entity.setAccountType(nominee.getacc);
				entity.setAddressOne(nominee.getAddressOne());
				entity.setAddressThree(nominee.getAddressThree());
				entity.setAddressTwo(nominee.getAddressTwo());
				entity.setAge(nominee.getAge());
//				entity.setApponiteName(nominee.geta);
//				entity.setBankName(nominee.getba);
//				entity.setClaimantType(nominee.getc);
				entity.setCountry(nominee.getCountry());
				entity.setDistrict(nominee.getDistrict());
				entity.setDob(nominee.getDateOfBirth());
				entity.setEmailId(nominee.getEmailId());
				entity.setFirstName(nominee.getNomineeName());
				entity.setClaimantType(nominee.getNomineeType());
//				entity.setIfscCode(nominee.getc);
//				entity.setIfscFlag(claimNo);
//				entity.setLastName(nominee.getno);
//				entity.setLastName(claimNo);
//				entity.setMaritalStatus(nominee.getma);
//				entity.setMiddleName(claimNo);
//				entity.setMobileNo(nominee.getn);
//				entity.setNomineeCode(nominee.getnom);
				entity.setPincode(nominee.getPinCode());
//				entity.setRelationShip(nominee.getRelationShip());
//				entity.setSharedAmount(nominee.getsh);
				entity.setSharedPercentage(NumericUtils.stringToDouble(nominee.getPercentage()));
				entity.setState(nominee.getState());

				entity.setClaimMbrEntity(claimMbrEntity);
				entity.setClaimNo(claimNo);
				entity.setNomineeCode(getClaimNomineeSequence());
				addressList.add(entity);
			}
			tempClaimMbrNomineeRepository.saveAll(addressList);
		}
	}

	private void saveClaimMbrBankDetails(MemberMasterEntity member, String claimNo, TempClaimMbrEntity claimMbrEntity) {
		if (member.getMemberBank() != null && !member.getMemberBank().isEmpty()) {
			List<TempClaimMbrBankDetailEntity> addressList = new ArrayList<>();
			for (MemberBankEntity bank : member.getMemberBank()) {
				TempClaimMbrBankDetailEntity entity = new TempClaimMbrBankDetailEntity();

				entity.setAccountNumber(bank.getAccountNumber());
				entity.setAccountType(bank.getAccountType());
				entity.setBankAddress(bank.getBankAddress());
				entity.setBankBranch(bank.getBankBranch());
				entity.setBankName(bank.getBankName());
				entity.setConfirmAccountNumber(bank.getConfirmAccountNumber());
				entity.setCountryCode(bank.getCountryCode());
				entity.setEmailId(bank.getEmailId());
				entity.setIfscCode(bank.getIfscCode());
				entity.setIfscCodeAvailable(bank.getIfscCodeAvailable());
//				entity.setLandlineNumber(bank.getLandlineNumber());
				entity.setStdCode(bank.getStdCode());

				entity.setClaimMbrEntity(claimMbrEntity);
				entity.setClaimNo(claimNo);
				addressList.add(entity);
			}
			tempClaimBankDtlsRepository.saveAll(addressList);
		}
	}

//
	private void saveClaimMbrAppointeeDtls(MemberMasterEntity member, String claimNo,
			TempClaimMbrEntity claimMbrEntity) {
		if (member.getMemberAppointee() != null && !member.getMemberAppointee().isEmpty()) {
			List<TempClaimMbrAppointeeEntity> appointList = new ArrayList<>();
			for (MemberAppointeeEntity appointee : member.getMemberAppointee()) {
				TempClaimMbrAppointeeEntity entity = new TempClaimMbrAppointeeEntity();

				entity.setAadharNumber(appointee.getAadharNumber());
				if (appointee.getAccountNumber() != null) {
					entity.setAccountNumber(String.valueOf(appointee.getAccountNumber()));
				}

				entity.setAccountType(appointee.getAccountType());
//				entity.setAddressOne(appointee.geta);
//				entity.setAddressThree(claimNo);
//				entity.setAddressTwo(claimNo);
				if (appointee.getAccountNumber() != null) {
					entity.setAppointeeCode(String.valueOf(appointee.getAppointeeCode()));
				}

				entity.setBankName(appointee.getBankName());
//				entity.setCountry(appointee.getc);
//				entity.setDistrict(claimNo);
				entity.setDob(appointee.getDateOfBirth());
//				entity.setEmailId(appointee.gete);
				entity.setFirstName(appointee.getAppointeeName());
				entity.setIfscCode(appointee.getIfscCode());
//				entity.setLastName(appointee.getap);
//				entity.setMaritalStatus(appointee.getm);
//				entity.setMiddleName(claimNo);
//				entity.setMobileNo(appointee.getmo);
//				entity.setNomineeCode(appointee.getno);
//				entity.setPincode(null);
//				entity.setRelationShip(appointee.getRelationship());
//				entity.setState(claimNo);

				entity.setClaimMbrEntity(claimMbrEntity);
				entity.setClaimNo(claimNo);
				appointList.add(entity);
			}
			tempClaimMbrAppointeeRepository.saveAll(appointList);
		}

	}

	private void saveClaimMbrAddresses(MemberMasterEntity member, String claimNo, TempClaimMbrEntity claimMbrEntity) {
		if (member.getMemberAddress() != null && !member.getMemberAddress().isEmpty()) {
			List<TempClaimMbrAddressEntity> addressList = new ArrayList<>();
			for (MemberAddressEntity address : member.getMemberAddress()) {
				TempClaimMbrAddressEntity entity = new TempClaimMbrAddressEntity();
				entity.setAddressId(null);
				entity.setAddressLineOne(address.getAddress1());
				entity.setAddressLineThree(address.getAddress3());
				entity.setAddressLineTwo(address.getAddress2());
				entity.setAddressType(address.getAddressType());
				entity.setCity(address.getCity());
				entity.setClaimNo(claimNo);
				entity.setCountry(address.getCountry());
				entity.setDistrict(address.getDistrict());
				entity.setPinCode(address.getPincode());
				entity.setState(address.getStateName());
				entity.setClaimMbrEntity(claimMbrEntity);
				entity.setClaimNo(claimNo);
				addressList.add(entity);
			}
			tempClaimMbrAddressRepository.saveAll(addressList);
		}

	}
	
	/**
	 * Check InterestCalculation Date Validation
	 **/
	private Boolean checkInterestCalculationDate(TempClaimEntity claimEntity) {
		logger.info("TempClaimServiceImpl -- checkInterestCalculationDate --started");
		TempClaimFundValueEntity tempClaimFundValueEntity = claimEntity.getClaimMbr().getClaimFundValue().get(0);
		Date fromDate = CommonDateUtils.convertStringToDate(DateUtils.sysDateStringSlash());
		Date toDate = CommonDateUtils.convertStringToDate(DateUtils.sysDateStringSlash());
		toDate = CommonDateUtils.constructeEndDateTime(toDate);
		if (tempClaimFundValueEntity.getDateOfCalculate()!=null && tempClaimFundValueEntity.getDateOfCalculate().compareTo(fromDate) >= 0
				&& tempClaimFundValueEntity.getDateOfCalculate().compareTo(toDate) <= 0) {
			logger.info("TempClaimServiceImpl -- checkInterestCalculationDate --error");

			return false;

		}
		logger.info("TempClaimServiceImpl -- checkInterestCalculationDate --end");
		return true;
	}

//	@Override
	public ApiResponseDto<String> updateMakerAction(ClaimMakerActionRequestDto request) {
		ApiResponseDto<String> responseDto=new ApiResponseDto<>();
		logger.info("TempClaimServiceImpl{}::{} updateMakerAction{}::start");
		try {
			Optional<TempClaimEntity> result = tempClaimRepository.findByClaimNoAndIsActive(request.getClaimNo(),
					Boolean.TRUE);
			if (result.isPresent()) {
				TempClaimEntity claimEntity = result.get();
				if (claimEntity.getPolicyType().equalsIgnoreCase(ClaimConstants.DC)&& checkInterestCalculationDate(claimEntity)) {
					return ApiResponseDto
							.error(ErrorDto.builder().message(ClaimErrorConstants.INTEREST_DATE_VALIDATE).build());
				}
				TempClaimEntity newClaimMbrEntity = new TempClaimEntity();
				BeanUtils.copyProperties(claimEntity, newClaimMbrEntity);
				newClaimMbrEntity.setModifiedBy(request.getModifiedBy());
				newClaimMbrEntity.setModifiedOn(DateUtils.sysDate());
				if (request.getAction().equals(ClaimStatus.PRELIMINARY_REQUIREMENT_REVIEW.val())) {
					newClaimMbrEntity.setStatus(ClaimStatus.PRELIMINARY_REQUIREMENT_REVIEW.val());
				} else if (request.getAction().equals(ClaimStatus.CLOSE_WITHOUT_SETTLEMENT_OTHERS.val())) {
					newClaimMbrEntity.setStatus(ClaimStatus.CLOSE_WITHOUT_SETTLEMENT_OTHERS.val());
				} else if (request.getAction().equals(ClaimStatus.CLOSE_WITHOUT_SETTLEMENT_CLAIM_WRONGLY_BOOKED.val())) {
					newClaimMbrEntity.setStatus(ClaimStatus.CLOSE_WITHOUT_SETTLEMENT_CLAIM_WRONGLY_BOOKED.val());
					newClaimMbrEntity.setCheckerCode(request.getCheckerCode());
					newClaimMbrEntity.setIsActive(Boolean.FALSE);
				} else if (request.getAction().equals(ClaimStatus.SEND_CHECKER.val())) {
					newClaimMbrEntity.setStatus(ClaimStatus.SEND_CHECKER.val());
					newClaimMbrEntity.setCheckerCode(request.getCheckerCode());
					newClaimMbrEntity.setIsActive(Boolean.TRUE);
				}
				/*** calling the Glcode data */
				claimServiceImpl.saAccountConfigMasterStoredProcedure(claimEntity.getClaimId());

				TempClaimEntity tempClaimEntity = tempSaveClaimService.insertClaim(request.getClaimNo(), newClaimMbrEntity);
				responseDto= ApiResponseDto.success(null,
						"ClaimOnBoardNo: " + tempClaimEntity.getClaimOnboarding().getClaimOnBoardNo() + " "
								+ ClaimErrorConstants.SEND_TO_CHECKER);
			} else {
				responseDto= ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
			}
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempClaimServiceImpl{}::{} updateMakerAction{}::error is "+e.getMessage());
		}
		logger.info("TempClaimServiceImpl{}::{} updateMakerAction{}::ended");
		return responseDto;
	}

	@Override
	public ApiResponseDto<String> updateCheckerAction(ClaimCheckerActionRequestDto request) {
		logger.info("TempClaimServiceImpl -- updateCheckerAction --started");
		ApiResponseDto<String> response = new ApiResponseDto<String>();

		try {
			Optional<TempClaimEntity> result = tempClaimRepository.findByClaimNoAndIsActive(request.getClaimNo(),
					Boolean.TRUE);
			if (result.isPresent()) {
				TempClaimEntity claimEntity = result.get();
				
				if (claimEntity.getPolicyType().equalsIgnoreCase(ClaimConstants.DC) &&  checkInterestCalculationDate(claimEntity)) {
					return ApiResponseDto
							.error(ErrorDto.builder().message(ClaimErrorConstants.INTEREST_DATE_VALIDATE).build());
				}
				
				TempClaimEntity newClaimMbrEntity = new TempClaimEntity();
				BeanUtils.copyProperties(claimEntity, newClaimMbrEntity);
				newClaimMbrEntity.setModifiedBy(request.getModifiedBy());
				newClaimMbrEntity.setModifiedOn(DateUtils.sysDate());
				if (request.getAction().equals(ClaimStatus.APPROVE.val())) {
					newClaimMbrEntity.setStatus(ClaimStatus.APPROVE.val());
					/**** Update mobileNo,Pan,emailid in member master table ****/
					updateMemberMaster(claimEntity);
				} else if (request.getAction().equals(ClaimStatus.REJECT.val())) {
					newClaimMbrEntity.setStatus(ClaimStatus.REJECT.val());
					newClaimMbrEntity.setRepudationReason(request.getRepudationReason());
				} else if (request.getAction().equals(ClaimStatus.SEND_TO_MAKER.val())) {
					newClaimMbrEntity.setStatus(ClaimStatus.SEND_TO_MAKER.val());
				}

				TempClaimEntity tempEntity = tempSaveClaimService.insertClaim(request.getClaimNo(), newClaimMbrEntity);
				if (request.getAction().equals(PayoutStatus.REJECT.val())) {
					memberMasterRepository.updateMemberStatusByMemberId(ClaimConstants.ACTIVE,
							claimEntity.getClaimMbr().getPolicyMemberId());
				}
				response = ApiResponseDto.success(null,
						"ClaimOnBoardNo: " + tempEntity.getClaimOnboarding().getClaimOnBoardNo() + " "
								+ ClaimErrorConstants.SEND_TO_MAKER);
			} else {
				logger.error("TempClaimServiceImpl -- updateCheckerAction --error");
				return ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
			}
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}

		logger.info("TempClaimServiceImpl -- updateCheckerAction --end");
		return response;

	}
	
	
	/*** update member details in master ***/
	private void updateMemberMaster(TempClaimEntity claimEntity) {

		try {
			logger.info("TempClaimCalcServiceImpl:{}::updateMemberMaster:{}:start");
			if (claimEntity != null) {
				if (claimEntity.getClaimMbr() != null) {
					MemberMasterEntity mEntity = memberMasterRepository.findByLicIdAndPolicyIdAndIsActive(
							claimEntity.getClaimMbr().getLicId(), claimEntity.getPolicyId());
					if (claimEntity.getModeOfExit() == ClaimConstants.RETRIERMENT
							|| claimEntity.getModeOfExit() == ClaimConstants.RESIGNATION) {
						if (mEntity != null) {
							if (claimEntity.getClaimMbr().getClaimAnuityCalc() != null) {
								List<TempClaimAnnuityCalcEntity> annuityCalcEntities = claimEntity.getClaimMbr()
										.getClaimAnuityCalc();
								mEntity.setMemberPan(annuityCalcEntities.get(0).getPan());
								mEntity.setMobileNo(annuityCalcEntities.get(0).getMobileNo());
								mEntity.setEmailid(annuityCalcEntities.get(0).getEmailid());
								memberMasterRepository.save(mEntity);
							}
							else {
								logger.info("TempClaimCalcServiceImpl:{}::updateMemberMaster:{}::MemberMaster is null");
							}

						}

					}
				}
			}
		} catch (Exception e) {
			logger.error("TempClaimCalcServiceImpl:{}::updateMemberMaster:{}:error");

			e.printStackTrace();
		}
		logger.info("TempClaimCalcServiceImpl:{}::updateMemberMaster:{}:end");

	}

	@Override
	public ApiResponseDto<ClaimDto> update(ClaimUpdateRequestDto request) {
		ApiResponseDto<ClaimDto> responseDto=new ApiResponseDto<>();
		logger.info("TempClaimCalcServiceImpl:{}::update:{}:start");
		try {
			Optional<TempClaimEntity> result = tempClaimRepository.findByClaimNoAndIsActive(request.getClaimNo(),
					Boolean.TRUE);
			if (result.isPresent()) {
				TempClaimEntity claimEntity = result.get();
				TempClaimEntity newClaimMbrEntity = new TempClaimEntity();
				BeanUtils.copyProperties(claimEntity, newClaimMbrEntity);
				newClaimMbrEntity.setDtOfExit(CommonDateUtils.stringDateToDateFormatter(request.getDateOfExit()));
				newClaimMbrEntity.setModeOfExit(request.getModeOfExit());
				newClaimMbrEntity.setOtherReason(request.getReasonForOther());
				newClaimMbrEntity.setPlaceOfEvent(request.getPlaceOfEvent());
				newClaimMbrEntity.setReasonExit(request.getReasonForExit());
				newClaimMbrEntity.setClaimId(null);

				claimEntity = tempSaveClaimService.insertClaim(request.getClaimNo(), newClaimMbrEntity);
				responseDto= ApiResponseDto.success(convertEntityToDto(claimEntity), ClaimErrorConstants.UPDATED_SUCCESSFULLY);
			} else {
				responseDto= ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
			}
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempClaimCalcServiceImpl:{}::update:{}:error is "+e.getMessage());	
		}
		logger.info("TempClaimCalcServiceImpl:{}::update:{}:ended");
		return responseDto;
	}

	private ClaimDto convertEntityToDto(TempClaimEntity tempClaimsEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempClaimEntity, ClaimDto>() {
			@Override
			protected void configure() {
				skip(destination.getClaimMbr().getClaimAnuityCalc());
				skip(destination.getClaimMbr().getClaimCommutationCalc());
				skip(destination.getClaimMbr().getClaimMbrAppointeeDtls());
				skip(destination.getClaimMbr().getClaimMbrNomineeDtls());
				skip(destination.getClaimMbr().getClaimMbrAddresses());
				skip(destination.getClaimMbr().getClaimMbrBankDetails());
				skip(destination.getClaimMbr().getClaimMbrFundValue());
				skip(destination.getClaimMbr().getClaimFundValue());
				skip(destination.getClaimMbr().getClaimPayeeTempBank());
			}
		});
		return modelMapper.map(tempClaimsEntity, ClaimDto.class);
	}

	@Override
	public ApiResponseDto<ClaimDto> findClaimDetails(String claimNo) {
		ApiResponseDto<ClaimDto> responseDto=new ApiResponseDto<>();
		logger.info("TempClaimMbrAppointeeServiceImpl::{}findClaimDetails{}::start");
		try {
			Optional<TempClaimEntity> result = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (result.isPresent()) {
				TempClaimEntity claimEntity = result.get();
				ClaimDto claimDto = modelMapper.map(claimEntity, ClaimDto.class);
				Double commutatiomSum =0d;
				Double annuitySum =0d;
				claimDto.setTotalShortReserve(0d);
//			if(!claimDto.getClaimMbr().getClaimCommutationCalc().isEmpty()) {
//				List<Double> commutaionSR=claimDto.getClaimMbr().getClaimCommutationCalc().stream().map(i->i.getCommutationAmountShortReserve()).collect(Collectors.toList());
//				commutatiomSum = commutaionSR.stream().collect(Collectors.summingDouble(Double::doubleValue));
//				
//				}
//				if(!claimDto.getClaimMbr().getClaimAnuityCalc().isEmpty()) {
//				List<Double> annuitySR=claimDto.getClaimMbr().getClaimAnuityCalc().stream().map(i->i.getShortReserve()).collect(Collectors.toList());
//				annuitySum = annuitySR.stream().collect(Collectors.summingDouble(Double::doubleValue));
//				}
//				claimDto.setTotalShortReserve(NumericUtils.doubleRoundInMath(commutatiomSum+annuitySum, 2));
					
				responseDto= ApiResponseDto.success(claimDto);
			} else {
				responseDto= ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempClaimMbrAppointeeServiceImpl::{}findClaimDetails{}::error is "+e.getMessage());
		}
		logger.info("TempClaimMbrAppointeeServiceImpl::{}findClaimDetails{}::ended");
		return responseDto;
	}

	@Override
	public ApiResponseDto<ClaimOnboardingDto> getClaimOnboardDetails(String claimOnBoardNo) {
		ApiResponseDto<ClaimOnboardingDto> commonResponse = new ApiResponseDto<>();
		ClaimOnboardingDto claimOnboardingDto = new ClaimOnboardingDto();
		logger.info("TempClaimMbrAppointeeServiceImpl::{}getClaimOnboardDetails{}::start");
		try {
			TempClaimOnboardingEntity claimOnboardingEntity = tempClaimOnboardingRepository
					.findByClaimOnBoardNo(claimOnBoardNo);
			if (claimOnboardingEntity != null) {
				claimOnboardingDto = modelMapper.map(claimOnboardingEntity, ClaimOnboardingDto.class);
				commonResponse.setData(claimOnboardingDto);
				commonResponse.setStatus(ClaimConstants.SUCCESS);
				commonResponse.setMessage(ClaimConstants.RETRIVE);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:TempClaimMbrAppointeeServiceImpl::{}getClaimOnboardDetails{} :: " , e);
		} finally {
			logger.info("TempClaimMbrAppointeeServiceImpl::{}getClaimOnboardDetails{}:Ends");
		}
		logger.info("TempClaimMbrAppointeeServiceImpl::{}getClaimOnboardDetails{}::Ends");
		return commonResponse;
	}

	@Override
	public ApiResponseDto<ClaimAnnuityCalcDto> gstAnnutityCalculate(ClaimAnnuityCalcDto claimAnnuityCalcDto) {
		ApiResponseDto<ClaimAnnuityCalcDto> responseDto=new ApiResponseDto<>();
		logger.info("TempClaimCalcServiceImpl:{}::gstAnnutityCalculate:{}:start");
		try {
			if (claimAnnuityCalcDto.getIsGSTApplicable() && claimAnnuityCalcDto.getGstBondBy() == 1) {

				claimAnnuityCalcDto.setNetPurchasePrice(claimAnnuityCalcDto.getPurchasePrice() * (100.0 / 118.0));
			} else {
				claimAnnuityCalcDto.setNetPurchasePrice(claimAnnuityCalcDto.getPurchasePrice());
			}
			claimAnnuityCalcDto.setNetPurchasePrice(
					Double.parseDouble(new DecimalFormat("##.##").format(claimAnnuityCalcDto.getNetPurchasePrice())));
			responseDto= ApiResponseDto.success(claimAnnuityCalcDto, ClaimErrorConstants.CLAIM_ANNUITY_CALC_SUCCESS);

		} catch (Exception e) {
			responseDto= ApiResponseDto.error(ErrorDto.builder().message(e.getMessage()).build());
			logger.info("TempClaimCalcServiceImpl:{}::gstAnnutityCalculate:{}:error is "+e.getMessage());
		}
		logger.info("TempClaimCalcServiceImpl:{}::gstAnnutityCalculate:{}:ended");
		return responseDto;
	}
	
	
	
	
	
	
	
	
	

//	@Override
//	public ApiResponseDto<ClaimOnboardingResponseDto> onboard(ClaimOnboardingRequestDto request) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public ApiResponseDto<ClaimOnboardingResponseDto> onboard(ClaimOnboardingRequestDto request) {
//		try {
//			logger.info("ClaimServiceImpl:add:Start");
//			if (request != null) {
//				if (StringUtils.isBlank(request.getPolicyNo())) {
//					return ApiResponseDto.error(ErrorDto.builder().message("Invalid policy number").build());
//				}
//				if (StringUtils.isBlank(request.getUnitCode())) {
//					return ApiResponseDto.error(ErrorDto.builder().message("Invalid unit code").build());
//				}
//				if (request.getMemberId() == null) {
//					return ApiResponseDto.error(ErrorDto.builder().message("Invalid member id").build());
//				} else {
//					Specification<TempClaimMbrEntity> specification = findByPolicyNoAndMemberIdAndMemberStatus(
//							request.getPolicyNo(), request.getMemberId(), ClaimConstants.ACTIVE);
//
//					Long count = tempClaimDtlsRepository.count(specification);
//					if (count > 0) {
//						return ApiResponseDto.error(ErrorDto.builder().message("Claim already exits.").build());
//					}
//				}
////				if (request.getMemberId() > 0) {
//				logger.info("policyDetails :" + System.currentTimeMillis() + "start");
//				PolicyMasterEntity policyDetails = commonService.findPolicyDetails(null, request.getPolicyId());
//
//				logger.info("policyDetails :" + System.currentTimeMillis() + "end");
//				if (policyDetails != null) {
////				if (request.getMemberId() > 0) {
//					logger.info("member :" + System.currentTimeMillis() + "start");
//					MemberMasterEntity member = new MemberMasterEntity();
//					if (policyDetails.getPolicyType().equals(CommonConstants.DC)) {
//						member = commonService.searchMember(request.getPolicyId(), request.getLicId(),
//								request.getMemberId(),
//								DateUtils.getFinancialYrByDt(DateUtils.convertStringToDate(request.getDateOfExit())));
//					} else {
//						member = memberMasterRepository.fetchByPolicyIdAndLicIdAndMemberStatusAndIsActiveTrue(
//								request.getPolicyId(), request.getLicId(), Boolean.TRUE);
//					}
//					/*
//					 * memberMasterRepository
//					 * .findByLicIdAndPolicyIdAndAndMemberStatusAndIsActiveTrueAndIsZeroidFalse
//					 * (request.getLicId(), request.getPolicyId(), ClaimConstants.ACTIVE);
//					 */
//					logger.info("member :" + System.currentTimeMillis() + "end");
//					if (member != null) {
//
//						String claimNo = getClaimSeq();
//						logger.info("mphMasterEntity :" + System.currentTimeMillis() + "start");
//						MphMasterEntity mphMasterEntity = commonService.findMphDetails(policyDetails.getMphId());
//						logger.info("mphMasterEntity :" + System.currentTimeMillis() + "end");
//						if (mphMasterEntity == null) {
//							return ApiResponseDto.error(ErrorDto.builder().message("Invalid Mph Details").build());
//
//						}
//						String onBoardingNo = getClaimOnBoardingSequence();
//
//						TempClaimOnboardingEntity tempClaimOnboardingEntity = insertOnboardingDetails(request,
//								onBoardingNo);
//
//						TempClaimEntity claimsEntity = insertTempClaimEntity(request, claimNo,
//								tempClaimOnboardingEntity, policyDetails, mphMasterEntity);
//						logger.info("policyRulesEntity :" + System.currentTimeMillis() + "start");
//						PolicyRulesEntity policyRulesEntity = policyRulesRepository
//								.findByPolicyIdAndCategory(policyDetails.getPolicyId(), request.getCategory());
//						logger.info("policyRulesEntity :" + System.currentTimeMillis() + "End");
//						if (policyRulesEntity == null) {
//							return ApiResponseDto
//									.error(ErrorDto.builder().message("Member BenefitType Not Mapped").build());
//						}
//						MemberTransactionSummaryEntity memberTransactionSummaryEntity;
//						if (policyDetails.getPolicyType().equals(CommonConstants.DC)) {
//							memberTransactionSummaryEntity = memberTransactionSummaryRepository
//									.findTopByPolicyIdAndMemberIdAndFinancialYearAndIsActiveTrueOrderByFinancialYearDesc(
//											policyDetails.getPolicyId(), request.getLicId(),
//											DateUtils.getFinancialYrByDt(
//													DateUtils.convertStringToDate(request.getDateOfExit())));
//
//							if (memberTransactionSummaryEntity == null) {
//								return ApiResponseDto
//										.error(ErrorDto.builder().message("Invalid FundTransaction").build());
//							}
//						} else {
//							memberTransactionSummaryEntity = new MemberTransactionSummaryEntity();
//						}
//						/*
//						 * Set<MemberTransactionSummaryEntity> summaryList =
//						 * member.getMemberTransactionSummary();
//						 */
//						logger.info("getFinancialYeartDetails :" + System.currentTimeMillis() + "start");
//						CommonResponseDto response = commonService
//								.getFinancialYeartDetails(DateUtils.dateToHypenStringDDMMYYYY(DateUtils.sysDate()));
//						logger.info("getFinancialYeartDetails :" + System.currentTimeMillis() + "End");
//						if (response == null) {
//							return ApiResponseDto.error(ErrorDto.builder().message("Invalid Financial Year").build());
//						}
//
//						TempClaimMbrEntity claimMember = insertTempClaimDetails(request, claimNo, claimsEntity,
//								policyDetails, member, policyRulesEntity, mphMasterEntity,
//								memberTransactionSummaryEntity);
//						logger.info("policyRulesEntity :" + System.currentTimeMillis() + "End");
//						ClaimDto claims = convertEntityToDto(claimsEntity);
//						logger.info("policyRulesEntity :" + System.currentTimeMillis() + "End");
//						if (claims.getStatus().equals(ClaimStatus.ONBORADED.val())) {
//							memberStatusUpdated(member.getMemberId());
//						}
//
//						return ApiResponseDto.success(new ClaimOnboardingResponseDto(claimNo, onBoardingNo, claims));
//
//					} else {
//						return ApiResponseDto.error(ErrorDto.builder().message("Already member existed").build());
//					}
//				} else {
//					return ApiResponseDto.error(ErrorDto.builder().message("Invalid policy number").build());
//				}
////				} else {
////					return ApiResponseDto.error(ErrorDto.builder().message("Invalid member ship number").build());
////				}
//			} else {
//				return ApiResponseDto.error(ErrorDto.builder().message("Request is null").build());
//			}
//		} catch (IllegalArgumentException | ApplicationException e) {
//			logger.error("Exception:ClaimServiceImpl:add", e);
//			return ApiResponseDto
//					.error(ErrorDto.builder().message("Error onboarding claims:" + e.getMessage()).build());
//		} finally {
//			logger.info("ClaimServiceImpl:add:Ends");
//		}
//	}
	
	
	@Override
	public CommonResponseDto rejectClaimOnBoarding(String claimNo) {
		logger.info("TempClaimServiceImpl:rejectClaimOnBoarding:Start");
		CommonResponseDto commonResponseDto = new CommonResponseDto();
		try {
			TempClaimEntity tempClaimEntity = tempClaimRepository.findAllByClaimNoAndIsActiveTrue(claimNo);
			
			if(tempClaimEntity != null ) {
			List<Integer> statusList =new ArrayList<>();
			statusList.add(ClaimStatus.ONBORADED.val());statusList.add(ClaimStatus.PRELIMINARY_REQUIREMENT_REVIEW.val());
			statusList.add(ClaimStatus.CLOSE_WITHOUT_SETTLEMENT_OTHERS.val());statusList.add(ClaimStatus.CLOSE_WITHOUT_SETTLEMENT_CLAIM_WRONGLY_BOOKED.val());
			statusList.add(ClaimStatus.REPUDIATE.val());statusList.add(ClaimStatus.SEND_TO_MAKER.val());
			
			if(statusList.contains(tempClaimEntity.getStatus())) {
				
				String licId = tempClaimDtlsRepository.getLicId(tempClaimEntity.getClaimId());
				if(licId != null) {
					
					tempClaimEntity.setStatus(ClaimStatus.CLAIM_DECLINE.val());
					tempClaimEntity=tempClaimRepository.save(tempClaimEntity) ;
					
					jdbcTemplate.update(
							"UPDATE MEMBER_MASTER  SET MEMBER_STATUS = ?  WHERE POLICY_ID = ? AND LIC_ID = ? AND IS_ACTIVE = '1'",
							ClaimConstants.ACTIVE, tempClaimEntity.getPolicyId(),licId);				
										
					commonResponseDto.setClaimOnBoardNumber(tempClaimEntity.getClaimOnboarding().getClaimOnBoardNo());
					commonResponseDto.setTransactionStatus(ClaimConstants.SUCCESS);
					commonResponseDto.setTransactionMessage(ClaimConstants.REJECTCLAIMONBOARD);
				}else {
					commonResponseDto.setTransactionStatus(ClaimConstants.FAIL);
					commonResponseDto.setTransactionMessage(ClaimConstants.NO_RECORD_FOUND);
				}
			}else {
				commonResponseDto.setTransactionStatus(ClaimConstants.FAIL);
				commonResponseDto.setTransactionMessage(ClaimConstants.NO_RECORD_FOUND);
			}
			}else {
				commonResponseDto.setTransactionStatus(ClaimConstants.FAIL);
				commonResponseDto.setTransactionMessage(ClaimConstants.NO_RECORD_FOUND);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- TempClaimServiceImpl-- rejectClaimOnBoarding --", e);
			commonResponseDto.setTransactionStatus(ClaimConstants.FAIL);
			commonResponseDto.setTransactionMessage(e.getMessage());
		}
		logger.info("TempClaimServiceImpl:rejectClaimOnBoarding:ended");
		return commonResponseDto;
	}
}
