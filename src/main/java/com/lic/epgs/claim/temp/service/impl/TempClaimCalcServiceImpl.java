package com.lic.epgs.claim.temp.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lic.epgs.claim.constants.ClaimConstants;
import com.lic.epgs.claim.constants.ClaimEntityConstants;
import com.lic.epgs.claim.constants.ClaimErrorConstants;
import com.lic.epgs.claim.dto.ClaimAnnuityCalcDto;
import com.lic.epgs.claim.dto.ClaimCommutationCalcDto;
import com.lic.epgs.claim.dto.ClaimFundPayableResponseDto;
import com.lic.epgs.claim.dto.ClaimFundValueDto;
import com.lic.epgs.claim.dto.ClaimMbrFundValueDto;
import com.lic.epgs.claim.dto.ClaimMbrNomineeDto;
import com.lic.epgs.claim.dto.ClaimMbrRequestDto;
import com.lic.epgs.claim.dto.ClaimPayeeBankDetailsDto;
import com.lic.epgs.claim.dto.NomineeTotalFundShared;
import com.lic.epgs.claim.dto.SurrenderChargeCalRequest;
import com.lic.epgs.claim.dto.SurrenderChargeCalResponse;
import com.lic.epgs.claim.entity.ClaimPolicyWithdrawalPercentageEntity;
import com.lic.epgs.claim.repository.ClaimPolicyWithdrawalPercentageRepository;
import com.lic.epgs.claim.temp.entity.ClaimPayeeTempBankDetailsEntity;
import com.lic.epgs.claim.temp.entity.TempClaimAnnuityCalcEntity;
import com.lic.epgs.claim.temp.entity.TempClaimCommutationCalcEntity;
import com.lic.epgs.claim.temp.entity.TempClaimEntity;
import com.lic.epgs.claim.temp.entity.TempClaimFundValueEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrBankDetailEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrFundValueEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrNomineeEntity;
import com.lic.epgs.claim.temp.repository.TempClaimAnnuityCalcRepository;
import com.lic.epgs.claim.temp.repository.TempClaimCommutationCalcRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrBankDtlsRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrFundValueRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrNomineeRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrRepository;
import com.lic.epgs.claim.temp.repository.TempClaimPayeeBankDetailsRepository;
import com.lic.epgs.claim.temp.repository.TempClaimRepository;
import com.lic.epgs.claim.temp.service.TempClaimCalcService;
import com.lic.epgs.claim.temp.service.TempSaveClaimService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.CommonResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.dto.FundRequestDto;
import com.lic.epgs.common.dto.FundedCommonMasterDto;
import com.lic.epgs.common.entity.FundedCommonMasterEntity;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.common.repository.FundedCommonMasterRepository;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.fund.repo.PolicyFundStatementSummaryRepo;
import com.lic.epgs.fund.service.FundRestApiService;
import com.lic.epgs.integration.dto.InterestFundResponseDto;
import com.lic.epgs.policy.dto.MphBankDto;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MphBankEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.PolicyTransactionEntriesEntity;
import com.lic.epgs.policy.repository.MphBankRepository;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.policy.repository.PolicyTransactionEntriesRepository;
import com.lic.epgs.policy.repository.PolicyTransactionSummaryRepository;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

@Service
@Transactional
public class TempClaimCalcServiceImpl implements TempClaimCalcService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TempClaimCommutationCalcRepository tempClaimCommutationCalcRepository;

	@Autowired
	TempClaimAnnuityCalcRepository tempClaimAnnuityCalcRepository;

	@Autowired
	TempClaimRepository tempClaimRepository;

	@Autowired
	TempClaimMbrRepository tempClaimMbrRepository;

	@Autowired
	TempSaveClaimService tempSaveTempClaimService;

	@Autowired
	TempClaimMbrFundValueRepository tempClaimMbrFundValueRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	TempClaimMbrNomineeRepository tempClaimMbrNomineeRepository;

	@Autowired
	TempClaimPayeeBankDetailsRepository tempClaimPayeeBankDetailsRepository;

	@Autowired
	FundedCommonMasterRepository fundedCommonMasterRepository;
	@Autowired
	MphMasterRepository mphMasterRepository;
	@Autowired
	MphBankRepository mphBankRepository;

	@Autowired
	TempClaimMbrBankDtlsRepository tempClaimMbrBankDtlsRepository;
	@Autowired
	CommonService commonService;
	@Autowired
	PolicyTransactionSummaryRepository policyTransactionSummaryRepository;

	@Autowired
	private TempClaimServiceImpl tempClaimServiceImpl;
	@Autowired
	PolicyMasterRepository policyMasterRepository;
	@Autowired
	PolicyFundStatementSummaryRepo policyFundStatementSummaryRepo;

	@Autowired
	FundRestApiService fundRestApiService;

	@Autowired
	ClaimPolicyWithdrawalPercentageRepository claimPolicyWithdrawalPercentageRepo;
	
	@Autowired
	PolicyTransactionEntriesRepository policyTransactionEntriesRepository;

	
	private Boolean validateCustomerLEINumber(Long mphId) {
	logger.info("TempClaimCalcServiceImpl::{}::validateCustomerLEINumber:{}::start");
	try {
		
	String leiNumber=tempClaimRepository.fetchLEInumberByMphId(mphId);
	
	if(leiNumber!=null) {
		logger.info("TempClaimCalcServiceImpl::{}::validateCustomerLEINumber:{}::end");
		return false;
	}
	else {
	logger.info("TempClaimCalcServiceImpl::{}::validateCustomerLEINumber:{}::end");
	return true;
	}
		
	}catch (Exception e) {
	logger.error("TempClaimCalcServiceImpl::{}::validateCustomerLEINumber:{}::"+e.getMessage());	
	}
	return null;
	
}
	
	
/*** CheckClosingBalance for DB Policies ***/
	
	private String checkClosingBalanceAmount(TempClaimEntity tempClaimEntity,Double purchasePrice) {
		logger.info("TempClaimCalcServiceImpl::{}::checkClosingBalanceAmount::{}::start");
		PolicyTransactionEntriesEntity polTransEntries=null;
		try {
			CommonResponseDto dto= commonService.getFinancialYeartDetails(DateUtils.dateToStringDDMMYYYY(DateUtils.sysDate()));
			
			if(tempClaimEntity.getPolicyType().equalsIgnoreCase(ClaimConstants.DB)) {
				if(tempClaimEntity.getVariant()==ClaimConstants.POLICY_VARIANT_DB_V2) {
					polTransEntries =policyTransactionEntriesRepository.
							findTopByPolicyIdAndQuarterAndFinancialYearAndIsActiveTrueOrderByTransactionDateDescCreatedOnDesc
					(tempClaimEntity.getPolicyId(),NumericUtils.stringToInteger(dto.getFrequency()),dto.getFinancialYear());
				}
				else {
					polTransEntries=policyTransactionEntriesRepository.
							findTopByPolicyIdAndAndFinancialYearAndIsActiveTrueOrderByTransactionDateDescCreatedOnDesc
							(tempClaimEntity.getPolicyId(),dto.getFinancialYear());
				}
				if(polTransEntries!=null) {
				if( polTransEntries.getClosingBalance().compareTo(BigDecimal.ZERO)> 0 &&
					polTransEntries.getClosingBalance().compareTo(BigDecimal.valueOf(purchasePrice))< 0) {
					
					return ClaimErrorConstants.checkPolicyClosingBalance(NumericUtils.
							convertBigDecimalToDouble(polTransEntries.getClosingBalance()), purchasePrice);
					
				}
				}
				else {
					return ClaimErrorConstants.NOT_FOUNT_TRANSATION_DETAILS;
				}
				
			}
			
			
			
		}
		catch (Exception e) {
			logger.error("TempClaimCalcServiceImpl::{}::checkClosingBalanceAmount::{}::error");
			return e.getMessage();
		}
		logger.info("TempClaimCalcServiceImpl::{}::checkClosingBalanceAmount::{}::end");
		
		return null;
		
	}
	
	@Override
	public ApiResponseDto<ClaimFundValueDto> saveFundvalue(ClaimFundValueDto request) throws ApplicationException {
		ApiResponseDto<ClaimFundValueDto> responseDto=new ApiResponseDto<>();
		try {
			logger.info("ClaimServiceImpl:saveFundvalue:Start");
			logger.info("saveFundvalue :" + System.currentTimeMillis() + "start");
			Optional<TempClaimEntity> optClaimEntity = tempClaimRepository.findByClaimNoAndIsActive(request.getClaimNo(),
					Boolean.TRUE);
			ApiResponseDto<SurrenderChargeCalResponse> response = new ApiResponseDto<>();
			logger.info("saveFundvalue :" + System.currentTimeMillis() + "inter");
			if (optClaimEntity.isPresent()) {
				TempClaimEntity claimEntity = optClaimEntity.get();
				Double fundValue1=0d;
				String closingBalance=checkClosingBalanceAmount(claimEntity,request.getPurchasePrice());
				if(closingBalance !=null) {
					return ApiResponseDto.error(ErrorDto.builder().message(closingBalance).build());
				}
				fundValue1=request.getFundValue()!=null?request.getFundValue():request.getPurchasePrice();
				if(fundValue1>=500000000 &&	validateCustomerLEINumber(claimEntity.getMphId())) {
					return responseDto= ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.LEI_NUMBER_MANDATORY).build());
					}
				TempClaimMbrEntity claimMbr = claimEntity.getClaimMbr();
				if (claimMbr != null) {
					TempClaimFundValueEntity entity = new TempClaimFundValueEntity();
					List<TempClaimFundValueEntity> fundValue = claimMbr.getClaimFundValue();
					if (fundValue != null && !fundValue.isEmpty()) {
						entity = fundValue.get(0);
					}
					convertToDtoToEntity(request, entity);
//				if( claimEntity.getPolicyType().equals(CommonConstants.DB)) {
//					/****Purchase Or pension Validation**start***/
//					CommonResponseDto common = commonService
//							.getFinancialYeartDetails(DateUtils.dateToHypenStringDDMMYYYY(DateUtils.sysDate()));
//					if(common==null) {
//						return ApiResponseDto.error(ErrorDto.builder().message("Invalid Final Year").build());
//					}
//					BigDecimal summaryLast;
//					if(claimEntity.getVariant().equals("56")){
//						
//						 summaryLast = policyTransactionSummaryRepository
//								.fetchByPolicyIdAndfinancialYearAndQuarterAndTotalContributionAndDesOrder(
//										optClaimEntity.get().getPolicyId(), common.getFinancialYear(),NumericUtils.stringToInteger(common.getFrequency()));
//					}
//					else {
//					 summaryLast = policyTransactionSummaryRepository
//							.fetchByPolicyIdAndfinancialYearAndTotalContributionAndDesOrder(
//									optClaimEntity.get().getPolicyId(), common.getFinancialYear());
//					}
//					logger.info("TotalContributionValue ="+NumericUtils.bigDecimalNegative(summaryLast));
//					System.out.println(summaryLast);
//					if(summaryLast == null || summaryLast.compareTo(BigDecimal.ZERO) == 0) {
//						return ApiResponseDto.error(ErrorDto.builder().message("Invalid Summary Details").build());
//					}
//					if (summaryLast.doubleValue() < (entity.getPurchasePrice())) {
//						return ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.TOTAL_CONTRIBUTION).build());
//					} else if (summaryLast.doubleValue() < (entity.getPension())) {
//						return ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.TOTAL_CONTRIBUTION).build());
//					}
//					}
//					/****Purchase Or pension Validation**End***/

					if (claimEntity.getModeOfExit() == 4 && claimEntity.getPolicyType().equals("DC")
							&& claimEntity.getProductId() == ClaimConstants.GSAGN_PRODUCT && claimEntity.getVariant().equals(ClaimConstants.GSAGN_VARIENT)) {
						Double existLoad = 0.0;
						Double commonAmount = 0.0;
						SurrenderChargeCalRequest req = new SurrenderChargeCalRequest();
						req.setPolicyNumber(claimEntity.getMasterPolicyNo());
						req.setFundAmount(request.getFundValue());
						/***
						 * Note Formula * existLoad =fundValue*(1.5/100)
						 * 
						 **/
						existLoad = request.getFundValue() * (1.5 / 100);
						commonAmount = request.getFundValue() - NumericUtils.doubleRoundInMath(existLoad,0) ;
						Boolean isMarketValueAdjustmentApplicable = false;
						entity.setIsMarketValueApplicable(isMarketValueAdjustmentApplicable);
						entity.setCommutationAmount(NumericUtils.doubleRoundInMath(commonAmount,0));
						entity.setExitLoad(NumericUtils.doubleRoundInMath(existLoad, 0));
						entity.setExitLoadRate(1.5);
					} 
					else {

						if (claimEntity.getPolicyType().equals("DB")) {
							entity.setCommutationAmount((request.getPurchasePrice() != null) ? request.getPurchasePrice()
									: request.getPension());
						} else {
							entity.setCommutationAmount(request.getFundValue());
						}
						entity.setIsMarketValueApplicable(false);
						entity.setExitLoad(0d);
						entity.setExitLoadRate(0d);
					}
//				else if(claimEntity.getModeOfExit() == ClaimConstants.WITHDRAWAL && claimEntity.getProductId() == NumericUtils.convertStringToLong(ClaimConstants.GSADA_PRODUCT)
//						&&( claimEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V3)|| claimEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DB_V3))
//						){
//					if (claimEntity.getPolicyType().equals("DB")) {
//						entity.setCommutationAmount((request.getPurchasePrice() != null) ? request.getPurchasePrice()
//								: request.getPension());
//					} else {
//						entity.setCommutationAmount(request.getFundValue());
//					}
//					entity.setIsMarketValueApplicable(false);
//					entity.setExitLoad(0d);
//					entity.setExitLoadRate(0d);	
//				}
//				else if(claimEntity.getModeOfExit() == ClaimConstants.WITHDRAWAL && claimEntity.getProductId() == NumericUtils.convertStringToLong(ClaimConstants.NGSCA_PRODUCT)
//						&&( claimEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V2)|| claimEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DB_V2))
//						){
//					if (claimEntity.getPolicyType().equals("DB")) {
//						entity.setCommutationAmount((request.getPurchasePrice() != null) ? request.getPurchasePrice()
//								: request.getPension());
//					} else {
//						entity.setCommutationAmount(request.getFundValue());
//					}
//					entity.setIsMarketValueApplicable(false);
//					entity.setExitLoad(0d);
//					entity.setExitLoadRate(0d);	
//				}
					tempSaveTempClaimService.insertClaimFundValue(request.getClaimNo(), entity);
					
					logger.info("saveFundvalue :" + System.currentTimeMillis() + "end");
					ClaimFundValueDto dto = convertEntityToDto(entity);
					dto.setCommutationAmount(entity.getCommutationAmount());
					dto.setExitLoad(entity.getExitLoad());
					responseDto= ApiResponseDto.success(dto, ClaimErrorConstants.FUND_VALUE_SAVE_SUCCESS);
				} else {
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
				}
			} else {
				responseDto= ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("ClaimServiceImpl {}::{} saveFundvalue{} ::error is "+e.getMessage());
		}
		logger.info("ClaimServiceImpl:saveFundvalue:end");
		return responseDto;
	}

	private ClaimFundValueDto convertEntityToDto(TempClaimFundValueEntity entity) {
		ClaimFundValueDto claimFundValueDto = new ClaimFundValueDto();
		claimFundValueDto = modelMapper.map(entity, ClaimFundValueDto.class);
		claimFundValueDto.setDateOfCalculate(DateUtils.dateToStringDDMMYYYY(entity.getDateOfCalculate()));
		return claimFundValueDto;
	}

	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
	}

	private void convertToDtoToEntity(ClaimFundValueDto request, TempClaimFundValueEntity entity) {
		entity.setAnnuityOption(request.getAnnuityOption());
		entity.setAnuityMode(request.getAnuityMode());
		entity.setCalculationType(request.getCalculationType());
		entity.setClaimNo(request.getClaimNo());
		entity.setEmployeeContribution((request.getEmployeeContribution() != null)
				? NumericUtils.doubleRoundInMath(request.getEmployeeContribution(), 2)
				: 0d);
		entity.setEmployerContribution((request.getEmployerContribution() != null)
				? NumericUtils.doubleRoundInMath(request.getEmployerContribution(), 2)
				: 0d);
		entity.setIsJointLiveRequired(request.getIsJointLiveRequired());
		entity.setPension(
				(request.getPension() != null) ? NumericUtils.doubleRoundInMath(request.getPension(), 0) : 0d);
		entity.setPurchasePrice(
				(request.getPurchasePrice() != null) ? NumericUtils.doubleRoundInMath(request.getPurchasePrice(), 0)
						: 0d);
		entity.setTotalFundValue(
				(request.getTotalFundValue() != null) ? NumericUtils.doubleRoundInMath(request.getTotalFundValue(), 0)
						: 0d);
		entity.setFundValue(
				(request.getFundValue() != null) ? NumericUtils.doubleRoundInMath(request.getFundValue(), 0) : 0d);
		entity.setTotalInterestAccured((request.getTotalInterestAccured() != null)
				? NumericUtils.doubleRoundInMath(request.getTotalInterestAccured(), 2)
				: 0d);
		entity.setVoluntaryContribution((request.getVoluntaryContribution() != null)
				? NumericUtils.doubleRoundInMath(request.getVoluntaryContribution(), 2)
				: 0d);
		entity.setPurchaseFromOthers(request.getPurchaseFromOthers());
		entity.setCorpusPercentage(request.getCorpusPercentage());
		entity.setRefundToMPH(
				(request.getRefundToMPH() != null) ? NumericUtils.doubleRoundInMath(request.getRefundToMPH(), 2) : 0d);
		entity.setDateOfCalculate(DateUtils.convertStringToDate(request.getDateOfCalculate()));
		entity.setSpouseName(request.getSpouseName());
		entity.setSpouseDob(DateUtils.convertStringToDate(request.getSpouseDOB()));
		

	}

	@Override
	public ApiResponseDto<ClaimMbrFundValueDto> saveMbrFundvalue(ClaimMbrFundValueDto request) {
		ApiResponseDto<ClaimMbrFundValueDto> responseDto=new ApiResponseDto<>();
		logger.info("ClaimServiceImpl{} ::{} saveMbrFundvalue{} ::{} start");
		try {
			if (request.getFundValueId() != null && request.getFundValueId() > 0) {
				Specification<TempClaimMbrFundValueEntity> specification = findMBRFundValue(request.getClaimNo(),
						request.getFundValueId());

				List<TempClaimMbrFundValueEntity> result = tempClaimMbrFundValueRepository.findAll(specification);
				if (!result.isEmpty() && result.get(0) != null) {

					TempClaimMbrFundValueEntity entity = updateClaimMbrFundValue(request, result.get(0));
					if (result.get(0).getCalculationType() != request.getCalculationType()
							|| !result.get(0).getCalculationType().equals(request.getCalculationType())) {

						entity = tempSaveTempClaimService.insertClaimMbrFundValueRefesh(request.getClaimNo(), entity,
								request.getNomineeCode());
					} else {

						entity = tempSaveTempClaimService.insertClaimMbrFundValue(request.getClaimNo(), entity);

					}
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), ClaimErrorConstants.FUND_VALUE_SAVE_SUCCESS);
				} else {
					responseDto= ApiResponseDto
							.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_FUND_VALUE_ID).build());
				}
			} else {
				Optional<TempClaimEntity> claimEntity = tempClaimRepository.findByClaimNoAndIsActive(request.getClaimNo(),
						Boolean.TRUE);
				if (claimEntity.isPresent()) {
					TempClaimMbrFundValueEntity entity = insertClaimMbrFundValue(request, claimEntity.get());
					entity = tempSaveTempClaimService.insertClaimMbrFundValue(request.getClaimNo(), entity);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), ClaimErrorConstants.FUND_VALUE_SAVE_SUCCESS);
				} else {
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("ClaimServiceImpl{} ::{} saveMbrFundvalue{} ::{} error is "+e.getMessage());
		}
		logger.info("ClaimServiceImpl{} ::{} saveMbrFundvalue{} ::{} ended");
		return responseDto;
	}

	private ClaimMbrFundValueDto convertEntityToDto(TempClaimMbrFundValueEntity entity) {
		return modelMapper.map(entity, ClaimMbrFundValueDto.class);
	}

	private ClaimPayeeBankDetailsDto bankPayeeConvertEntityToDto(ClaimPayeeTempBankDetailsEntity entity) {
		return modelMapper.map(entity, ClaimPayeeBankDetailsDto.class);
	}

	private TempClaimMbrFundValueEntity updateClaimMbrFundValue(ClaimMbrFundValueDto request,
			TempClaimMbrFundValueEntity tempClaimMbrFundValueEntity) {
		TempClaimMbrFundValueEntity entity = new TempClaimMbrFundValueEntity();
		BeanUtils.copyProperties(tempClaimMbrFundValueEntity, entity);
		convertDtoToEntity(request, entity);
		return entity;
	}

	private ClaimPayeeTempBankDetailsEntity updateClaimMbrBankPayee(ClaimPayeeBankDetailsDto claimPayeeBankDetailsDto,
			ClaimPayeeTempBankDetailsEntity claimPayeeTempBankDetailsEntity) {
		ClaimPayeeTempBankDetailsEntity entity = new ClaimPayeeTempBankDetailsEntity();
		BeanUtils.copyProperties(claimPayeeTempBankDetailsEntity, entity);
		bankpPayeeconvertDtoToEntity(claimPayeeBankDetailsDto, entity);
		return entity;
	}

	private Specification<TempClaimMbrFundValueEntity> findMBRFundValue(String claimNo, Long fundValueId) {
		return (root, query, criteriaBuilder) -> {
			Join<TempClaimMbrFundValueEntity, TempClaimMbrEntity> claimMbr = root
					.join(ClaimEntityConstants.CLAIM_MBR_ENTITY);
			Join<TempClaimMbrEntity, TempClaimEntity> claim = claimMbr.join(ClaimEntityConstants.CLAIM);
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.CLAIM_NO), claimNo));
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(root.get(ClaimEntityConstants.FUND_VAL_ID), fundValueId);
		};
	}

	private Specification<ClaimPayeeTempBankDetailsEntity> findBankPayee(String claimNo, Long bankAccountId) {
		return (root, query, criteriaBuilder) -> {
			Join<ClaimPayeeTempBankDetailsEntity, TempClaimMbrEntity> claimMbr = root
					.join(ClaimEntityConstants.CLAIM_MBR_ENTITY);
			Join<TempClaimMbrEntity, TempClaimEntity> claim = claimMbr.join(ClaimEntityConstants.CLAIM);
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.CLAIM_NO), claimNo));
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(root.get(ClaimEntityConstants.BANK_ACCOUNT_ID), bankAccountId);
		};
	}

	private TempClaimMbrFundValueEntity insertClaimMbrFundValue(ClaimMbrFundValueDto request,
			TempClaimEntity tempClaimEntity) {
		TempClaimMbrEntity claimMbr = tempClaimEntity.getClaimMbr();
		TempClaimMbrFundValueEntity entity = new TempClaimMbrFundValueEntity();
		convertDtoToEntity(request, entity);
		entity.setClaimMbrEntity(claimMbr);
		return entity;
	}

	private ClaimPayeeTempBankDetailsEntity insertClaimMbrBankPayee(ClaimPayeeBankDetailsDto request,
			TempClaimEntity tempClaimEntity) {
		TempClaimMbrEntity claimMbr = tempClaimEntity.getClaimMbr();
		ClaimPayeeTempBankDetailsEntity entity = new ClaimPayeeTempBankDetailsEntity();
		bankpPayeeconvertDtoToEntity(request, entity);
		entity.setClaimMbrEntity(claimMbr);
		return entity;
	}

	private void convertDtoToEntity(ClaimMbrFundValueDto request, TempClaimMbrFundValueEntity entity) {
		entity.setAnnuityOption(request.getAnnuityOption());
		entity.setAnuityMode(request.getAnuityMode());
		entity.setCalculationType(request.getCalculationType());
		entity.setClaimNo(request.getClaimNo());
		entity.setEmployeeContribution(
				(request.getPension() != null) ? NumericUtils.doubleRoundInMath(request.getPension(), 2) : 0d);
		entity.setEmployerContribution(
				(request.getPension() != null) ? NumericUtils.doubleRoundInMath(request.getPension(), 0) : 0d);
		entity.setIsJointLiveRequired(request.getIsJointLiveRequired());
		entity.setPension(
				(request.getPension() != null) ? NumericUtils.doubleRoundInMath(request.getPension(), 0) : 0d);
		entity.setPurchasePrice(
				(request.getPurchasePrice() != null) ? NumericUtils.doubleRoundInMath(request.getPurchasePrice(), 0)
						: 0d);
		entity.setTotalFundValue(
				(request.getTotalFundValue() != null) ? NumericUtils.doubleRoundInMath(request.getTotalFundValue(), 0)
						: 0d);
		entity.setTotalInterestAccured((request.getTotalInterestAccured() != null)
				? NumericUtils.doubleRoundInMath(request.getTotalInterestAccured(), 2)
				: 0d);
		entity.setVoluntaryContribution((request.getVoluntaryContribution() != null)
				? NumericUtils.doubleRoundInMath(request.getVoluntaryContribution(), 2)
				: 0d);
		entity.setPurchasedFrom(request.getPurchasedFrom());
		entity.setPurchasePriceForLIC(request.getPurchasePriceForLIC());
		entity.setNomineeCode(request.getNomineeCode());
		entity.setPurchaseFromOthers(request.getPurchaseFromOthers());

	}

	private void bankpPayeeconvertDtoToEntity(ClaimPayeeBankDetailsDto claimPayeeBankDetailsDto,
			ClaimPayeeTempBankDetailsEntity entity) {
//		entity.setBankAccountId(null);
		entity.setBankAccountId(claimPayeeBankDetailsDto.getBankAccountId());
		entity.setAccountNumber(claimPayeeBankDetailsDto.getAccountNumber());
		entity.setAccountType(claimPayeeBankDetailsDto.getAccountType());
		entity.setBankAddressOne(claimPayeeBankDetailsDto.getBankAddressOne());
		entity.setBankAddressTwo(claimPayeeBankDetailsDto.getBankAddressTwo());
		entity.setBankAddressThree(claimPayeeBankDetailsDto.getBankAddressThree());
		entity.setBankBranch(claimPayeeBankDetailsDto.getBankBranch());
		entity.setBankName(claimPayeeBankDetailsDto.getBankName());
		entity.setIfscCode(claimPayeeBankDetailsDto.getIfscCode());
		entity.setStdCode(claimPayeeBankDetailsDto.getStdCode());
		entity.setLandlineNumber(claimPayeeBankDetailsDto.getLandlineNumber());
		entity.setEmailID(claimPayeeBankDetailsDto.getEmailID());
		entity.setModifiedOn(DateUtils.sysDate());
		entity.setClaimNo(claimPayeeBankDetailsDto.getClaimNo());
		entity.setNomineeCode(claimPayeeBankDetailsDto.getNomineeCode());
		entity.setAmountPayable(claimPayeeBankDetailsDto.getAmountPayable());
		entity.setType(claimPayeeBankDetailsDto.getType());

	}

	private Specification<TempClaimCommutationCalcEntity> findClaimCommutationCalcById(String claimNo,
			Long communityId) {
		return (root, query, criteriaBuilder) -> {
			Join<TempClaimCommutationCalcEntity, TempClaimMbrEntity> claimMbr = root
					.join(ClaimEntityConstants.CLAIM_MBR_ENTITY);
			Join<TempClaimMbrEntity, TempClaimEntity> claim = claimMbr.join(ClaimEntityConstants.CLAIM);
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.CLAIM_NO), claimNo));
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(root.get(ClaimEntityConstants.COMMUNITY_ID), communityId);
		};
	}

	private Specification<TempClaimAnnuityCalcEntity> annuityIdEqual(String claimNo, Long annuityId) {
		return (root, query, criteriaBuilder) -> {
			Join<TempClaimAnnuityCalcEntity, TempClaimMbrEntity> claimMbr = root
					.join(ClaimEntityConstants.CLAIM_MBR_ENTITY);
			Join<TempClaimMbrEntity, TempClaimEntity> claim = claimMbr.join(ClaimEntityConstants.CLAIM);
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.CLAIM_NO), claimNo));
			claim.on(criteriaBuilder.equal(claim.get(ClaimEntityConstants.IS_ACTIVE), Boolean.TRUE));

			return criteriaBuilder.equal(root.get(ClaimEntityConstants.ANNUITY_ID), annuityId);
		};
	}

	private ClaimCommutationCalcDto convertEntityToDto(TempClaimCommutationCalcEntity tempclaimcommutationcalcentity) {
		return modelMapper.map(tempclaimcommutationcalcentity, ClaimCommutationCalcDto.class);
	}

	private void convertDtoToEntity(TempClaimCommutationCalcEntity entity, ClaimCommutationCalcDto request) {

		entity.setAmtPayableTo(request.getAmtPayableTo());
		entity.setCommutationAmt(
				(request.getCommutationAmt() != null) ? NumericUtils.doubleRoundInMath(request.getCommutationAmt(), 0)
						: 0d);
		entity.setCommutationBy(request.getCommutationBy());
		entity.setCommutationPerc(request.getCommutationPerc());
		entity.setCommutationReq(request.getCommutationReq());
		entity.setCommutationValue(request.getCommutationValue()!=null?NumericUtils.doubleRoundInMath(request.getCommutationValue(),0):0d);
		entity.setExitLoad(request.getExitLoad()!=null?NumericUtils.doubleRoundInMath(request.getExitLoad(), 0):0d);
		entity.setMvaExitLoadRate(request.getMvaExitLoadRate());
		entity.setMvaExitLoad(request.getMvaExitLoad());
		entity.setExitLoadRate(request.getExitLoadRate());
		entity.setCreatedBy(request.getCreatedBy());
		entity.setNomineeCode(request.getNomineeCode());
		entity.setCreatedOn(CommonDateUtils.sysDate());
//		entity.setPurchasePrice(request.getCommutationPerc()>0d?
//				NumericUtils.doubleRoundInMath(request.getTotalFundValue()-(request.getTotalFundValue()*(request.getCommutationPerc()/100)),0):
//					NumericUtils.doubleRoundInMath(request.getTotalFundValue(),0));
		if (request.getCommutationReq().equals(true)) {
			entity.setPurchasePrice(request.getTotalFundValue()
					- (request.getCommutationAmt() != null || request.getCommutationAmt() > 0
							? NumericUtils.doubleRoundInMath(request.getCommutationAmt(),0)
							: 0d));
		} else {
			entity.setPurchasePrice(NumericUtils.doubleRoundInMath(request.getTotalFundValue(),0));
		}
		entity.setTdsApplicable(request.getTdsApplicable());
		entity.setShortReserve(request.getShortReserve() != null ? request.getShortReserve() : 0d);
		entity.setTdsPerc(request.getTdsPerc());
		entity.setSlab(request.getSlab());
		entity.setTdsAmount(
				(request.getTdsAmount() != null) ? NumericUtils.doubleRoundInMath(request.getTdsAmount(), 0) : 0d);
		entity.setNetAmount(
				(request.getNetAmount() != null) ? NumericUtils.doubleRoundInMath(request.getNetAmount(), 0) : 0d);

		entity.setMarketValue(request.getMarketValue());
		entity.setMarketValueAdjustment(request.getMarketValueAdjustment());
//		Double commutationValue = 0d;
//		if (entity.getCommutationReq().equals(true) && (entity.getCommutationValue() != null||entity.getCommutationValue() > 0d)) {
//			commutationValue = request.getCommutationPerc() > 0
//					? request.getTotalFundValue() * (request.getCommutationPerc() / 100)
//					: 0d;
//			entity.setCommutationAmountShortReserve(commutationValue - entity.getCommutationValue());
//		}
		entity.setCommutationAmountShortReserve(0d);
	}
	
	
	private Double validateminimumAnnuityInstallmentPayable(String annuityMode) {
		logger.info("TempClaimCalcServiceImpl:{}::validateMinimumAnnuityInstallmentPayable:{}:start");
		try {
			switch (annuityMode) {
			case "1":
				return ClaimConstants.MINIMUM_INSTALLMENT_AMOUNT_MONTHLY;
			case "2":
				return ClaimConstants.MINIMUM_INSTALLMENT_AMOUNT_QUARTERLY;
			case "3":
				return ClaimConstants.MINIMUM_INSTALLMENT_AMOUNT_HALF_YEARLY;
			case "4":
				return ClaimConstants.MINIMUM_INSTALLMENT_AMOUNT_YEARLY;
			default:
				return 400d;
			}

		} catch (Exception e) {
			logger.error("TempClaimCalcServiceImpl:{}::validateMinimumAnnuityInstallmentPayable:{}:error");

		}
		logger.info("TempClaimCalcServiceImpl:{}::validateMinimumAnnuityInstallmentPayable:{}:end");
		return null;

	}

	@Override
	public ApiResponseDto<ClaimAnnuityCalcDto> saveAnuity(ClaimAnnuityCalcDto request) {
		ApiResponseDto<ClaimAnnuityCalcDto> responseDto=new ApiResponseDto<>();
		logger.info("TempClaimCalcServiceImpl:{}::saveAnuity:{}:start");
		try {
			/*** Note:-TODO Validation for Annuity minimum installment Amount **start*/
			Double minimumAnnuityInstallment = 0d;
			if (request.getAnuityMode() != null) {
				minimumAnnuityInstallment = validateminimumAnnuityInstallmentPayable(request.getAnuityMode());
				if (request.getPension() < minimumAnnuityInstallment) {
					responseDto= ApiResponseDto.error(ErrorDto.builder()
							.message(ClaimErrorConstants.annuityInstallmentAmount(
									ClaimConstants.annuityMode(request.getAnuityMode()), minimumAnnuityInstallment))
							.build());
				}

			} else {
				responseDto= ApiResponseDto
						.error(ErrorDto.builder().message(ClaimErrorConstants.ANNUITY_MODE_VALIDATION).build());
			}
			
			/*** Note:-TODO Validation for Annuity minimum installment Amount **End*/

			if (request.getAnnuityId() != null && request.getAnnuityId() > 0) {
				Specification<TempClaimAnnuityCalcEntity> specification = annuityIdEqual(request.getClaimNo(),
						request.getAnnuityId());

				List<TempClaimAnnuityCalcEntity> result = tempClaimAnnuityCalcRepository.findAll(specification);
				if (!result.isEmpty() && result.get(0) != null) {
					TempClaimAnnuityCalcEntity entity = updateClaimAnnuity(request, result.get(0));
					entity = tempSaveTempClaimService.insertClaimAnnuityCalc(request.getClaimNo(), entity, request);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), ClaimErrorConstants.ANNUITY_SAVED_SUCCESS);
				} else {
					responseDto = ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_ANNUITY_ID).build());
				}
			} else {
				Optional<TempClaimEntity> claimEntity = tempClaimRepository.findByClaimNoAndIsActive(request.getClaimNo(),
						Boolean.TRUE);
				if (claimEntity.isPresent()) {
					TempClaimAnnuityCalcEntity entity = insertClaimAnnuity(request, claimEntity.get());
					entity = tempSaveTempClaimService.insertClaimAnnuityCalc(request.getClaimNo(), entity, request);
					responseDto= ApiResponseDto.success(convertEntityToDto(entity), ClaimErrorConstants.ANNUITY_SAVED_SUCCESS);
				} else {
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempClaimCalcServiceImpl:{}::saveAnuity:{}:error is "+e.getMessage());
		}
		logger.info("TempClaimCalcServiceImpl:{}::saveAnuity:{}:ended");
		return responseDto;
	}

	private TempClaimAnnuityCalcEntity insertClaimAnnuity(ClaimAnnuityCalcDto request,
			TempClaimEntity tempClaimEntity) {
		TempClaimMbrEntity claimMbr = tempClaimEntity.getClaimMbr();
		TempClaimAnnuityCalcEntity entity = new TempClaimAnnuityCalcEntity();
		convertDtoToEntity(entity, request);
		entity.setClaimMbrEntity(claimMbr);
		return entity;
	}

	private TempClaimAnnuityCalcEntity updateClaimAnnuity(ClaimAnnuityCalcDto request,
			TempClaimAnnuityCalcEntity tempClaimAnnuityCalcEntity) {
		TempClaimAnnuityCalcEntity entity = new TempClaimAnnuityCalcEntity();
		BeanUtils.copyProperties(tempClaimAnnuityCalcEntity, entity);
		convertDtoToEntity(entity, request);
		return entity;
	}

	private ClaimAnnuityCalcDto convertEntityToDto(TempClaimAnnuityCalcEntity tempClaimAnnuityCalcEntity) {
		return modelMapper.map(tempClaimAnnuityCalcEntity, ClaimAnnuityCalcDto.class);
	}

	private void convertDtoToEntity(TempClaimAnnuityCalcEntity entity, ClaimAnnuityCalcDto request) {
		Double shortReserve = 0d;
		Double rocShortReverve = 0d;
		entity.setAmtPaidTo(request.getAmtPaidTo());
		entity.setCreatedBy(request.getCreatedBy());
		entity.setCreatedOn(CommonDateUtils.sysDate());
		entity.setNomineeCode(request.getNomineeCode());
		entity.setPurchasePrice(
				(request.getPurchasePrice() != null) ? NumericUtils.doubleRoundInMath(request.getPurchasePrice(), 0)
						: 0d);
		entity.setAnnuityOption(request.getAnnuityOption());
		entity.setAnuityMode(request.getAnuityMode());
		entity.setIsGSTApplicable(request.getIsGSTApplicable());
		entity.setIsJointLiveRequired(request.getIsJointLiveRequired());
		entity.setPension(
				(request.getPension() != null) ? NumericUtils.doubleRoundInMath(request.getPension(), 0) : 0d);
		entity.setSpouseName(request.getSpouseName());
		entity.setDateOfBirth(CommonDateUtils.convertStringToDate(request.getDateOfBirth()));
		entity.setCaptureCertainNumber(request.getCaptureCertainNumber());
		entity.setGstAmount(
				(request.getGstAmount() != null) ? NumericUtils.doubleRoundInMath(request.getGstAmount(), 2) : 0d);
		entity.setNetPurchasePrice((request.getNetPurchasePrice() != null)
				? NumericUtils.doubleRoundInMath(request.getNetPurchasePrice(), 0)
				: 0d);
		entity.setIsGSTApplicable(request.getIsGSTApplicable());
		entity.setCaptureCertainNumber(request.getCaptureCertainNumber());
		entity.setGstBondBy(request.getGstBondBy());
		entity.setPan(request.getPan());
		entity.setMobileNo(request.getMobileNo());
		entity.setEmailid(request.getEmailid());
		entity.setRate(request.getRate());
		entity.setGstRate(request.getGstRate());
		entity.setRateType(request.getRateType());
		entity.setIncentiveRate(request.getIncentiveRate());
		entity.setDisIncentiveRate(request.getDisIncentiveRate());
		entity.setNFactor(request.getNFactor());
		entity.setUnitCode(request.getUnitCode());
		entity.setAnnuityPayableTo(request.getAnnuityPayableTo());
		entity.setUnitName(request.getUnitName());
		entity.setUnitId(request.getUnitId());
		entity.setArrears(request.getArrears());
		if (entity.getGstBondBy() == 1) {
			rocShortReverve = NumericUtils.doubleRoundInMath(request.getPurchasePrice() - entity.getGstAmount(), 2);
			shortReserve = rocShortReverve - NumericUtils.doubleRoundInMath(rocShortReverve, 0);
		}
		entity.setShortReserve(shortReserve != null ? NumericUtils.doubleRoundInMath(shortReserve, 2) : 0d);

	}

	public String DatetoStringSlash(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
		String strDate = dateFormat.format(date);
		return strDate;
	}

	@Override
	public ApiResponseDto<ClaimCommutationCalcDto> saveCommutation(ClaimCommutationCalcDto request) {

		ApiResponseDto<ClaimCommutationCalcDto> responseDt=new ApiResponseDto<>();
		logger.info("TempClaimCalcServiceImpl:{}::saveCommutation:{}:start");
		try {
			if (request.getCommunityId() != null && request.getCommunityId() > 0) {
				Specification<TempClaimCommutationCalcEntity> specification = findClaimCommutationCalcById(
						request.getClaimNo(), request.getCommunityId());

				List<TempClaimCommutationCalcEntity> result = tempClaimCommutationCalcRepository.findAll(specification);
				if (!result.isEmpty() && result.get(0) != null) {
					TempClaimCommutationCalcEntity entity = updateClaimCommutation(request, result.get(0));
					entity = tempSaveTempClaimService.insertClaimCommutationCalcRefesh(request.getClaimNo(), entity,
							request.getNomineeCode());
					responseDt= ApiResponseDto.success(convertEntityToDto(entity), ClaimErrorConstants.COMMUTATION_CALC_SUCCESS);
				} else {
					responseDt= ApiResponseDto
							.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_COMMUTATION_ID).build());
				}
			} else {
				Optional<TempClaimEntity> claimEntity = tempClaimRepository.findByClaimNoAndIsActive(request.getClaimNo(),
						Boolean.TRUE);
				if (claimEntity.isPresent()) {
					TempClaimCommutationCalcEntity entity = insertClaimCommutation(request, claimEntity.get());
					entity = tempSaveTempClaimService.insertClaimCommutationCalc(request.getClaimNo(), entity);
					responseDt= ApiResponseDto.success(convertEntityToDto(entity), ClaimErrorConstants.COMMUTATION_CALC_SUCCESS);
				} else {
					responseDt= ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempClaimCalcServiceImpl:{}::saveCommutation:{}:error is "+e.getMessage());
		}
		logger.info("TempClaimCalcServiceImpl:{}::saveCommutation:{}:ended");
		return responseDt;
	}

	private TempClaimCommutationCalcEntity insertClaimCommutation(ClaimCommutationCalcDto request,
			TempClaimEntity tempClaimEntity) {
		TempClaimMbrEntity claimMbr = tempClaimEntity.getClaimMbr();
		TempClaimCommutationCalcEntity entity = new TempClaimCommutationCalcEntity();
		convertDtoToEntity(entity, request);
		entity.setClaimMbrEntity(claimMbr);
		return entity;
	}

	private TempClaimCommutationCalcEntity updateClaimCommutation(ClaimCommutationCalcDto request,
			TempClaimCommutationCalcEntity tempClaimCommutationCalcEntity) {
		TempClaimCommutationCalcEntity entity = new TempClaimCommutationCalcEntity();
		BeanUtils.copyProperties(tempClaimCommutationCalcEntity, entity);
		convertDtoToEntity(entity, request);
		return entity;
	}

	@Override
	public ApiResponseDto<ClaimPayeeBankDetailsDto> savePayeeBankDetails(
			ClaimPayeeBankDetailsDto claimPayeeBankDetailsDto) {
		ApiResponseDto<ClaimPayeeBankDetailsDto> responseDto=new ApiResponseDto<>();
//		logger.info("TempClaimCalcServiceImpl saveClaimPayeeBankAccountDetails serviceImpl--start");
//		ClaimPayeeBankDetailsDto bankAccountDetailsDto = new ClaimPayeeBankDetailsDto();
//		try {
//			ClaimPayeeTempBankDetailsEntity exUpdatebankAccountentity = null;
//			ClaimPayeeTempBankDetailsEntity updatebankAccountentity = new ClaimPayeeTempBankDetailsEntity();
//			if (StringUtils.isNotBlank(claimPayeeBankDetailsDto.getBankAccountId())) {
//				exUpdatebankAccountentity = tempClaimPayeeBankDetailsRepository.findByBankAccountId(
//						NumericUtils.convertStringToLong(claimPayeeBankDetailsDto.getBankAccountId()));
//				updatebankAccountentity.setCreatedBy(exUpdatebankAccountentity.getCreatedBy());
//				updatebankAccountentity.setCreatedOn(exUpdatebankAccountentity.getCreatedOn());
//				updatebankAccountentity.setBankAccountId(exUpdatebankAccountentity.getBankAccountId());
//			} else {
//				updatebankAccountentity.setCreatedBy(claimPayeeBankDetailsDto.getCreatedBy());
//				updatebankAccountentity.setCreatedOn(DateUtils.sysDate());
//			}
//			updatebankAccountentity.setBankAccountId(null);
//			updatebankAccountentity.setAccountNumber(claimPayeeBankDetailsDto.getAccountNumber());
//			updatebankAccountentity.setAccountType(claimPayeeBankDetailsDto.getAccountType());
//			updatebankAccountentity.setBankAddressOne(claimPayeeBankDetailsDto.getBankAddressOne());
//			updatebankAccountentity.setBankAddressTwo(claimPayeeBankDetailsDto.getBankAddressTwo());
//			updatebankAccountentity.setBankAddressThree(claimPayeeBankDetailsDto.getBankAddressThree());
//			updatebankAccountentity.setBankBranch(claimPayeeBankDetailsDto.getBankBranch());
//			updatebankAccountentity.setBankName(claimPayeeBankDetailsDto.getBankName());
//			updatebankAccountentity.setIfscCode(claimPayeeBankDetailsDto.getIfscCode());
//			updatebankAccountentity.setStdCode(claimPayeeBankDetailsDto.getStdCode());
//			updatebankAccountentity.setLandlineNumber(claimPayeeBankDetailsDto.getLandlineNumber());
//			updatebankAccountentity.setEmailID(claimPayeeBankDetailsDto.getEmailID());
//			updatebankAccountentity.setModifiedOn(DateUtils.sysDate());
//			updatebankAccountentity = tempClaimPayeeBankDetailsRepository.save(updatebankAccountentity);
//			bankAccountDetailsDto = modelMapper.map(updatebankAccountentity, ClaimPayeeBankDetailsDto.class);
//
//		} catch (ConstraintViolationException ce) {
//			logger.error("TempClaimCalcServiceImpl saveClaimPayeeBankAccountDetails serviceImpl--error",
//					ce.getMessage());
//			return ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
//		}
//		logger.info("TempClaimCalcServiceImpl saveClaimPayeeBankAccountDetails serviceImpl--end");
//		return ApiResponseDto.success(bankAccountDetailsDto, ClaimErrorConstants.CLAIM_PAYEE_BANK_SAVED);
		logger.info("TempClaimCalcServiceImpl:{}::savePayeeBankDetails:{}:start");
		try {
			if (claimPayeeBankDetailsDto.getBankAccountId() != null && claimPayeeBankDetailsDto.getBankAccountId() > 0) {
				Specification<ClaimPayeeTempBankDetailsEntity> specification = findBankPayee(
						claimPayeeBankDetailsDto.getClaimNo(), claimPayeeBankDetailsDto.getBankAccountId());

				List<ClaimPayeeTempBankDetailsEntity> result = tempClaimPayeeBankDetailsRepository.findAll(specification);
				if (!result.isEmpty() && result.get(0) != null) {
					ClaimPayeeTempBankDetailsEntity entity = updateClaimMbrBankPayee(claimPayeeBankDetailsDto,
							result.get(0));
					entity = tempSaveTempClaimService.insertClaimMbrBankPayee(claimPayeeBankDetailsDto.getClaimNo(),
							entity);
					responseDto= ApiResponseDto.success(bankPayeeConvertEntityToDto(entity),
							ClaimErrorConstants.CLAIM_PAYEE_BANK_SAVED);
				} else {
					responseDto= ApiResponseDto
							.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_FUND_VALUE_ID).build());
				}
			} else {
				Optional<TempClaimEntity> claimEntity = tempClaimRepository
						.findByClaimNoAndIsActive(claimPayeeBankDetailsDto.getClaimNo(), Boolean.TRUE);
				if (claimEntity.isPresent()) {
					ClaimPayeeTempBankDetailsEntity entity = insertClaimMbrBankPayee(claimPayeeBankDetailsDto,
							claimEntity.get());
					entity = tempSaveTempClaimService.insertClaimMbrBankPayee(claimPayeeBankDetailsDto.getClaimNo(),
							entity);
					responseDto= ApiResponseDto.success(bankPayeeConvertEntityToDto(entity),
							ClaimErrorConstants.CLAIM_PAYEE_BANK_SAVED);
				} else {
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempClaimCalcServiceImpl:{}::savePayeeBankDetails:{}:error is "+e.getMessage());
		}
		logger.info("TempClaimCalcServiceImpl:{}::savePayeeBankDetails:{}:ended");
		return responseDto;
	}

	@Override
	public ApiResponseDto<ClaimFundValueDto> saveFundCalculationvalue(ClaimFundValueDto request) {
		logger.info("TempClaimCalcServiceImpl:{}:saveFundCalculationvalue:{}:start");
		ClaimFundValueDto claimFundValueDto = new ClaimFundValueDto();
		try {
		
		if (request.getFundValue() == null || request.getFundValue().isNaN()) {
			request.setFundValue(0.0);
		}
		if (request.getCorpusPercentage() == null || request.getCorpusPercentage().isNaN()) {
			request.setCorpusPercentage(0.0);
		}

		claimFundValueDto.setTotalFundValue(
				NumericUtils.doubleRoundInMath((request.getFundValue() * request.getCorpusPercentage()) / 100.0, 0));
		claimFundValueDto.setRefundToMPH(request.getFundValue()-claimFundValueDto.getTotalFundValue());
		
		}catch (Exception e) {
			logger.error("TempClaimCalcServiceImpl:{}:saveFundCalculationvalue:{}:error"+e.getMessage());
		}
		logger.info("TempClaimCalcServiceImpl:{}:saveFundCalculationvalue:{}:end");
		return ApiResponseDto.success(claimFundValueDto, ClaimErrorConstants.UPDATED_SUCCESSFULLY);
		
	}

	@Override
	public ApiResponseDto<ClaimFundValueDto> saveMbrFundCalculationvalue(NomineeTotalFundShared request) {
//		NomineeTotalFundShared nomineeTotalFundShared = new NomineeTotalFundShared();
//		if (request.getTotalfundValue() == null || request.getTotalfundValue().isNaN()) {
//			request.setTotalfundValue(0.0);
//		}
//		Double totalFulue;
//		Long count;
//
//		for (Double req : request.getPercentages()) {
////			request.setTotalfundValue();
////			Map<String , Double> map=new HashMap<String,Double>(); 
//
////			map.put("Nominee"+count.toString(), totalFulue);
//
//		}
		return null;
	}

//	@Override
//	public ApiResponseDto<ClaimCommutationCalcDto> saveCommutationCalculation(ClaimCommutationCalcDto request) {
//		ClaimCommutationCalcDto claimCommutationCalcDto = new ClaimCommutationCalcDto();
//		if (request.getCommutationReq()) {
//			if (request.getModeOfExist().equals(4)) {
//				claimCommutationCalcDto.setCommutationAmt(request.getCommutationAmt());
//			} else if (request.getCommutationBy() == 1 && !request.getModeOfExist().equals(4)) {
//				claimCommutationCalcDto
//						.setCommutationAmt((request.getPurchasePrice() * request.getCommutationPerc()) / 100);
//			} else {
//				claimCommutationCalcDto.setCommutationAmt((request.getPurchasePrice() * request.getSlab()));
//			}
//
//			if (request.getTdsApplicable()) {
//				claimCommutationCalcDto
//						.setTdsAmount((claimCommutationCalcDto.getCommutationAmt() * request.getTdsPerc()) / 100);
//
//				claimCommutationCalcDto.setNetAmount(
//						claimCommutationCalcDto.getCommutationAmt() - claimCommutationCalcDto.getTdsAmount());
//			} else {
//				claimCommutationCalcDto.setNetAmount(claimCommutationCalcDto.getCommutationAmt());
//			}
//		} else {
//			claimCommutationCalcDto.setCommutationAmt(request.getPurchasePrice());
//		}
//		return ApiResponseDto.success(claimCommutationCalcDto, ClaimErrorConstants.UPDATED_SUCCESSFULLY);
//	}

	@Override
	public ApiResponseDto<ClaimCommutationCalcDto> saveCommutationCalculation(ClaimCommutationCalcDto request) {
		ApiResponseDto<ClaimCommutationCalcDto> responseDto=new ApiResponseDto<>();
		logger.info("ClaimServiceImpl{} ::{} saveCommutationCalculation{} ::{} start");
		ClaimCommutationCalcDto claimCommutationCalcDto = new ClaimCommutationCalcDto();
		Double commutationActualAmount = 0d;
		Double tdsActualAmount = 0d;
		Double netAmount = 0d;
		Double scaleReverse = 0d;
		try {
			if (request.getCommutationReq()) {
				if (request.getModeOfExist().equals(4)) {
					commutationActualAmount = request.getCommutationAmt();
					claimCommutationCalcDto
							.setCommutationAmt(NumericUtils.doubleRoundInMath(request.getCommutationAmt(), 0));

				} else if (request.getCommutationBy() == 1 && !request.getModeOfExist().equals(4)) {
					commutationActualAmount = request.getPurchasePrice() * (request.getCommutationPerc() / 100);
					claimCommutationCalcDto.setCommutationAmt(NumericUtils.doubleRoundInMath(commutationActualAmount, 0));
				} else {
					commutationActualAmount = request.getPurchasePrice() * request.getSlab();
					claimCommutationCalcDto.setCommutationAmt(NumericUtils.doubleRoundInMath(commutationActualAmount, 0));
				}
//			scaleReverse+=NumericUtils.doubleRoundInMath(commutationActualAmount-claimCommutationCalcDto.getCommutationAmt(),3);

				if (request.getTdsApplicable()) {
					tdsActualAmount = (claimCommutationCalcDto.getCommutationAmt() * request.getTdsPerc()) / 100;

					claimCommutationCalcDto.setTdsAmount(NumericUtils.doubleRoundUpInMath(tdsActualAmount));
//				scaleReverse+=NumericUtils.doubleRoundInMath(tdsActualAmount-claimCommutationCalcDto.getTdsAmount(),3);
					netAmount = claimCommutationCalcDto.getCommutationAmt() - claimCommutationCalcDto.getTdsAmount();
					claimCommutationCalcDto.setNetAmount(NumericUtils.doubleRoundInMath(netAmount, 0));

				} else {
					netAmount = claimCommutationCalcDto.getCommutationAmt();
					claimCommutationCalcDto.setNetAmount(NumericUtils.doubleRoundInMath(netAmount, 0));
				}
			} else {
				claimCommutationCalcDto.setCommutationAmt(NumericUtils.doubleRoundInMath(request.getPurchasePrice(), 0));
			}
//		scaleReverse+=NumericUtils.doubleRoundInMath(netAmount-claimCommutationCalcDto.getNetAmount(),3);
			claimCommutationCalcDto.setShortReserve(scaleReverse != null ? scaleReverse : 0d);

			responseDto= ApiResponseDto.success(claimCommutationCalcDto, ClaimErrorConstants.UPDATED_SUCCESSFULLY);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("ClaimServiceImpl{} ::{} saveCommutationCalculation{} ::{} error is "+e.getMessage());
		}
		logger.info("ClaimServiceImpl{} ::{} saveCommutationCalculation{} ::{} ended");
		return responseDto;
	}

	@Override
	public ApiResponseDto<ClaimCommutationCalcDto> calAnnuityPurchase(ClaimMbrRequestDto claimMbrRequestDto) {
		logger.info("ClaimServiceImpl{} ::{} calAnnuityPurchase{} ::{} start");
		
		ApiResponseDto<ClaimCommutationCalcDto> commonResponse = new ApiResponseDto<>();
		try {
			List<TempClaimMbrEntity> tempClaimMbrEntityList = tempClaimMbrRepository
					.findAllByClaimNoAndIsActiveTrue(claimMbrRequestDto.getClaimNo());
			if (!tempClaimMbrEntityList.isEmpty()) {
				for (TempClaimMbrEntity tempClaimMbrEntity : tempClaimMbrEntityList) {
					TempClaimFundValueEntity tempClaimFundValueEntity = tempClaimMbrEntity.getClaimFundValue().get(0);
					if (tempClaimFundValueEntity.getCalculationType() != null
							&& tempClaimFundValueEntity.getCalculationType() != claimMbrRequestDto.getAnnuityOption()) {

						if (tempClaimMbrEntity.getClaimMbrFundValue() != null) {
							for (TempClaimMbrFundValueEntity tempClaimMbrFundValueEntity : tempClaimMbrEntity
									.getClaimMbrFundValue()) {
								tempClaimMbrFundValueEntity.setNomineeCode(null);
								tempClaimMbrFundValueEntity.setClaimMbrEntity(null);
								tempClaimMbrFundValueRepository.save(tempClaimMbrFundValueEntity);
							}
						}

						if (tempClaimMbrEntity.getClaimCommutationCalc() != null) {
							for (TempClaimCommutationCalcEntity tempClaimCommutationCalcEntity : tempClaimMbrEntity
									.getClaimCommutationCalc()) {
								tempClaimCommutationCalcEntity.setNomineeCode(null);
								tempClaimCommutationCalcEntity.setClaimMbrEntity(null);
								tempClaimCommutationCalcRepository.save(tempClaimCommutationCalcEntity);
							}
						}

						if (tempClaimMbrEntity.getClaimAnuityCalc() != null) {
							for (TempClaimAnnuityCalcEntity tempClaimAnnuityCalcEntity : tempClaimMbrEntity
									.getClaimAnuityCalc()) {
								tempClaimAnnuityCalcEntity.setNomineeCode(null);
								tempClaimAnnuityCalcEntity.setClaimMbrEntity(null);
								tempClaimAnnuityCalcRepository.save(tempClaimAnnuityCalcEntity);
							}
						}

						if (tempClaimMbrEntity.getClaimPayeeTempBank() != null) {
							for (ClaimPayeeTempBankDetailsEntity tempClaimPayeeBank : tempClaimMbrEntity
									.getClaimPayeeTempBank()) {
								tempClaimPayeeBank.setNomineeCode(null);
								tempClaimPayeeBank.setClaimMbrEntity(null);
								tempClaimPayeeBankDetailsRepository.save(tempClaimPayeeBank);
							}
						}
					}

				}
				commonResponse.setData(null);
				commonResponse.setStatus(ClaimConstants.SUCCESS);
				commonResponse.setMessage(ClaimConstants.UPDATE);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:TempClaimMbrAppointeeServiceImpl:getAppointeeNameList", e);
		} finally {
			logger.info("TempClaimMbrAppointeeServiceImpl:getAppointeeNameList:Ends");
		}
		return commonResponse;
	}

	@Override
	public ApiResponseDto<TempClaimMbrNomineeEntity> calRefreshByNominee(ClaimMbrRequestDto claimMbrRequestDto) {
		logger.info("ClaimServiceImpl{} ::{} calRefreshByNominee{} ::{} start");
		Optional<TempClaimEntity> tempClaimEntity = tempClaimRepository
				.findByClaimNoAndIsActiveTrue(claimMbrRequestDto.getClaimNo());
		TempClaimMbrEntity tempClaimMbrEntity = tempClaimEntity.get().getClaimMbr();
		try {

			if (tempClaimMbrEntity != null) {
				TempClaimMbrNomineeEntity tempNomineeEntity = tempClaimMbrNomineeRepository
						.findByNomineeCode(claimMbrRequestDto.getNomineeCode());
				if (tempNomineeEntity != null) {
					TempClaimCommutationCalcEntity claimCommutationCalcEntity = tempClaimCommutationCalcRepository
							.findByNomineeCode(claimMbrRequestDto.getNomineeCode());
					if (claimCommutationCalcEntity != null) {
						claimCommutationCalcEntity.setNomineeCode(null);
						claimCommutationCalcEntity.setClaimMbrEntity(null);
						tempClaimCommutationCalcRepository.save(claimCommutationCalcEntity);

					}
					TempClaimAnnuityCalcEntity claimAnnuityCalcEntity = tempClaimAnnuityCalcRepository
							.findByNomineeCode(claimMbrRequestDto.getNomineeCode());
					if (claimAnnuityCalcEntity != null) {
						claimAnnuityCalcEntity.setNomineeCode(null);
						claimAnnuityCalcEntity.setClaimMbrEntity(null);
						tempClaimAnnuityCalcRepository.save(claimAnnuityCalcEntity);
					}
					ClaimPayeeTempBankDetailsEntity claimPayeeTempBankDetailsEntity = tempClaimPayeeBankDetailsRepository
							.findByNomineeCode(claimMbrRequestDto.getNomineeCode());
					if (claimCommutationCalcEntity != null) {
						claimPayeeTempBankDetailsEntity.setNomineeCode(null);
						claimPayeeTempBankDetailsEntity.setClaimMbrEntity(null);
						tempClaimPayeeBankDetailsRepository.save(claimPayeeTempBankDetailsEntity);
					}

					return ApiResponseDto.success(tempNomineeEntity, ClaimErrorConstants.UPDATED_SUCCESSFULLY);

				} else {
					return ApiResponseDto.error(ErrorDto.builder().message("Invalid Nominee").build());
				}
			} else {
				return ApiResponseDto.error(ErrorDto.builder().message("Invalid Member").build());
			}

		} catch (Exception e) {
			return ApiResponseDto.error(ErrorDto.builder().message(e.getMessage()).build());
		}
	}

	@Override
	public ApiResponseDto<List<FundedCommonMasterDto>> getFundPayableDetails(Long codeId, String modeOfExit) {
		ApiResponseDto<List<FundedCommonMasterDto>> commonResponse = new ApiResponseDto<>();
		List<FundedCommonMasterDto> fundedCommonMasterDtoList = new ArrayList<>();
		logger.info("TempClaimMbrAppointeeServiceImpl:getAppointeeNameList::{start}");
		FundedCommonMasterDto fundedCommonMasterDto = new FundedCommonMasterDto();
		try {
			List<FundedCommonMasterEntity> fundedCommonMasterEntityList = fundedCommonMasterRepository
					.findAllByCodeIdAndIsActiveTrue(codeId);
			if (!fundedCommonMasterEntityList.isEmpty()) {
				for (FundedCommonMasterEntity fundedCommonMasterEntity : fundedCommonMasterEntityList) {
					if (fundedCommonMasterEntity.getModeOfExit().contains(modeOfExit)) {
						fundedCommonMasterDto = modelMapper.map(fundedCommonMasterEntity, FundedCommonMasterDto.class);
						fundedCommonMasterDtoList.add(fundedCommonMasterDto);
					}
				}
				commonResponse.setData(fundedCommonMasterDtoList);
				commonResponse.setStatus(ClaimConstants.SUCCESS);
				commonResponse.setMessage(ClaimConstants.RETRIVE);

			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:TempClaimMbrAppointeeServiceImpl:getAppointeeNameList", e);
			commonResponse.setStatus(ClaimConstants.FAIL);
			commonResponse.setMessage(ClaimConstants.NO_RECORD_FOUND);
		} finally {
			logger.info("TempClaimMbrAppointeeServiceImpl:getAppointeeNameList:Ends");
		}
		return commonResponse;
	}

//	@Override
//	public ApiResponseDto<ClaimPayeeBankDetailsDto> getClaimBankDetails(
//			ClaimFundPayableRequestDto claimFundPayableRequestDto) {
//		ClaimPayeeBankDetailsDto claimPayeeBankDetailsDto=new ClaimPayeeBankDetailsDto();
//		 ApiResponseDto<ClaimPayeeBankDetailsDto> commonResponse = new  ApiResponseDto<>();
//		 MphBankDto mphBankDto = new MphBankDto();
//		 ClaimMbrBankDetailDto claimMbrBankDetailDto = new ClaimMbrBankDetailDto();
//		 ClaimMbrNomineeDto claimNomineeBankDetailsDto = new ClaimMbrNomineeDto();
//		 ClaimFundPayableResponseDto claimFundPayableResponseDto = new ClaimFundPayableResponseDto();
//		 
//		try {
//			MphBankEntity mphBankEntity = mphBankRepository.findByMphIdAndIsActiveTrue(claimFundPayableRequestDto.getMphId());
//			if(mphBankEntity != null) {
//				claimPayeeBankDetailsDto = modelMapper.map(mphBankEntity,ClaimPayeeBankDetailsDto.class);
//				claimFundPayableResponseDto.setMphBank(mphBankDto);
//			}
//					
//			if(!claimFundPayableRequestDto.getAmountPayable().equalsIgnoreCase(ClaimConstants.AMOUNT_PAYABLE) &&
//					(claimFundPayableRequestDto.getMemberId()>0||claimFundPayableRequestDto.getMemberId()!=null)) {
//				TempClaimMbrEntity tempClaimMbrEntity=tempClaimMbrRepository.findByMemberIdAndIsActiveTrue(claimFundPayableRequestDto.getMemberId());
//			List<TempClaimMbrBankDetailEntity> tempClaimMbrBankDetailEntityList = tempClaimMbrEntity.getClaimMbrBankDetails();
//			if(!tempClaimMbrBankDetailEntityList.isEmpty()) {
//			TempClaimMbrBankDetailEntity tempClaimMbrBankDetailEntity=tempClaimMbrBankDetailEntityList.get(0);
//			if(tempClaimMbrBankDetailEntity != null) {
//				claimPayeeBankDetailsDto =  modelMapper.map(tempClaimMbrBankDetailEntity,ClaimPayeeBankDetailsDto.class);
//			claimFundPayableResponseDto.setClaimMbrBankDetail(claimMbrBankDetailDto);
//			}
//			}
//		
//			}
//			TempClaimMbrNomineeEntity claimNomineeTempBankEntity = tempClaimMbrNomineeRepository.findByNomineeId(claimFundPayableRequestDto.getNomineeId());
//			if(claimNomineeTempBankEntity != null) {
//				claimPayeeBankDetailsDto = modelMapper.map(claimNomineeTempBankEntity,ClaimPayeeBankDetailsDto.class);
//				claimFundPayableResponseDto.setClaimNomineeBankDetails(claimNomineeBankDetailsDto);
//			}
//			
//			else {
//				return ApiResponseDto.error(ErrorDto.builder().message("Invalid Member").build());
//			}
//			commonResponse.setData(claimPayeeBankDetailsDto);
//			return ApiResponseDto.success(claimPayeeBankDetailsDto, ClaimErrorConstants.UPDATED_SUCCESSFULLY);
//		}
//		catch (IllegalArgumentException e) {
//			logger.error("Exception:TempClaimMbrAppointeeServiceImpl:getAppointeeNameList", e);
//		} 
//		return commonResponse;
//		}

	@Override
	public ApiResponseDto<ClaimPayeeBankDetailsDto> getClaimBankDetails(Long mphId, Long memberId, Long nomineeId,
			String claimNo) {
		ClaimPayeeBankDetailsDto claimPayeeBankDetailsDto = new ClaimPayeeBankDetailsDto();
		ApiResponseDto<ClaimPayeeBankDetailsDto> commonResponse = new ApiResponseDto<>();
		MphBankDto mphBankDto = new MphBankDto();
		ClaimMbrNomineeDto claimNomineeBankDetailsDto = new ClaimMbrNomineeDto();
		ClaimFundPayableResponseDto claimFundPayableResponseDto = new ClaimFundPayableResponseDto();
		logger.info("ClaimServiceImpl{} ::{} getClaimBankDetails{} ::{} start");
		try {
			Optional<TempClaimEntity> claimEntity = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (!claimEntity.isPresent()) {
				return ApiResponseDto.error(ErrorDto.builder().message("Invalid Claim").build());
			}
			if (mphId!=0) {
//				MphMasterEntity mphMasterEntity = mphMasterRepository.findByMphCodeAndIsActiveTrue(mphCode);
//				if (mphMasterEntity == null) {
//					return ApiResponseDto.error(ErrorDto.builder().message("Invalid Mph Details").build());
//				}
				MphBankEntity mphBankEntity = mphBankRepository.findByBankDetailsBymphId(mphId);
				if (mphBankEntity!=null) {
					
					claimPayeeBankDetailsDto = modelMapper.map(mphBankEntity, ClaimPayeeBankDetailsDto.class);
					claimFundPayableResponseDto.setMphBank(mphBankDto);
				} else {
					return ApiResponseDto.error(ErrorDto.builder().message("No Data Found MphBank Details").build());
				}
			} else if (memberId > 0l) {

				TempClaimMbrEntity tempClaimMbrEntity = tempClaimMbrRepository.findByMemberIdAndIsActiveTrue(memberId);
				if (!tempClaimMbrEntity.getClaimMbrBankDetails().isEmpty()) {
					TempClaimMbrBankDetailEntity tempClaimMbrBankDetailEntity = tempClaimMbrEntity
							.getClaimMbrBankDetails().get(0);
					claimPayeeBankDetailsDto = modelMapper.map(tempClaimMbrBankDetailEntity,
							ClaimPayeeBankDetailsDto.class);
				} else {
					return ApiResponseDto.error(ErrorDto.builder().message("No Data Found MbrBank Details").build());
				}
			} else if (nomineeId > 0l) {
				TempClaimMbrNomineeEntity claimNomineeTempBankEntity = tempClaimMbrNomineeRepository
						.findByNomineeId(nomineeId);
				if (claimNomineeTempBankEntity != null) {
					claimPayeeBankDetailsDto = modelMapper.map(claimNomineeTempBankEntity,
							ClaimPayeeBankDetailsDto.class);
					claimFundPayableResponseDto.setClaimNomineeBankDetails(claimNomineeBankDetailsDto);
				} else {
					return ApiResponseDto
							.error(ErrorDto.builder().message("No Data Found NomineeBank Details").build());
				}
			} else {
				return ApiResponseDto.error(ErrorDto.builder().message("Invalid Request").build());
			}

			commonResponse.setData(claimPayeeBankDetailsDto);
			return ApiResponseDto.success(claimPayeeBankDetailsDto, ClaimErrorConstants.UPDATED_SUCCESSFULLY);
		} catch (IllegalArgumentException e) {
			logger.error("Exception:TempClaimMbrAppointeeServiceImpl:getAppointeeNameList", e);
			return ApiResponseDto.error(ErrorDto.builder().message(e.getMessage()).build());
		}

	}

	@Override
	public ApiResponseDto<ClaimFundValueDto> reCalculateFund(ClaimFundValueDto claimFundValue)
			throws ApplicationException {
		ApiResponseDto<ClaimFundValueDto> responseDto=new ApiResponseDto<>();
		TempClaimFundValueEntity fundEntity = new TempClaimFundValueEntity();

		logger.info("TempClaimServiceImpl -----reCalculateFund------start");
		try {
			if (claimFundValue.getDateOfCalculate() != null) {
				Date lastCalculationDate = DateUtils.convertStringToDate(claimFundValue.getDateOfCalculate());
				Date fromDate = CommonDateUtils.constructeStartDateTime(DateUtils.sysDate());
				Date toDate = CommonDateUtils.constructeEndDateTime(DateUtils.sysDate());

				if (!((fromDate.equals(lastCalculationDate) || lastCalculationDate.after(fromDate))
						&& lastCalculationDate.before(toDate))) {
					Optional<TempClaimEntity> claimEntity = tempClaimRepository
							.findByClaimNoAndIsActiveTrue(claimFundValue.getClaimNo());
					if (claimEntity.isPresent()) {
						MemberMasterEntity member = new MemberMasterEntity();
						member.setLicId(claimEntity.get().getClaimMbr().getLicId());
						member.setPolicyId(claimEntity.get().getPolicyId());

//					fundEntity = modelMapper.map(claimFundValue, TempClaimFundValueEntity.class);
						fundEntity.setCalculationType(claimFundValue.getCalculationType());
						fundEntity.setClaimNo(claimFundValue.getClaimNo());
						fundEntity.setEmployeeContribution(claimFundValue.getEmployeeContribution());
						fundEntity.setEmployerContribution(claimFundValue.getEmployerContribution());
						fundEntity.setVoluntaryContribution(claimFundValue.getVoluntaryContribution());
						fundEntity.setCorpusPercentage(claimFundValue.getCorpusPercentage());
						fundEntity.setFundValue(claimFundValue.getFundValue());
						fundEntity.setOpeningBalance(claimFundValue.getOpeningBalance());
						fundEntity.setRefundToMPH(claimFundValue.getRefundToMPH());
						fundEntity.setTotalContirbution(claimFundValue.getTotalContirbution());
						fundEntity.setDateOfCalculate(DateUtils.convertStringToDate(claimFundValue.getDateOfCalculate()));
						fundEntity.setTotalFundValue(null);
						fundEntity.setTotalInterestAccured(null);
						/**** fundApi ****/
						try {
							tempClaimServiceImpl.setMemberFundDetailsReCalculate(member, claimEntity.get(), fundEntity);
							responseDto= ApiResponseDto.success(convertEntityToDto(fundEntity),
									ClaimErrorConstants.FUND_VALUE_SAVE_SUCCESS);
						} catch (Exception e) {
							logger.info(e.getMessage());
							responseDto= ApiResponseDto
									.error(ErrorDto.builder().message("Fund Api Error" + e.getMessage()).build());
						}
					}
				} else {
					responseDto= ApiResponseDto.error(ErrorDto.builder().message("Already Fund Calculation UptoDate").build());
				}

			} else {
				responseDto= ApiResponseDto.error(ErrorDto.builder().message("No lastCalculationDate Found").build());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempClaimServiceImpl -----reCalculateFund------error is "+e.getMessage());
		}
		logger.info("TempClaimServiceImpl -----reCalculateFund------ended");
		return responseDto;

	}

	/*** Notes:- fetch Policy_fund_Value ***/

	public InterestFundResponseDto getPolicyFundValueBatchApi(PolicyMasterEntity policyEntity) {
		InterestFundResponseDto fundResponseDto = new InterestFundResponseDto();
		FundRequestDto requestDto = new FundRequestDto();
		requestDto.setDepositDate(DateUtils.sysDateStringSlash());
		requestDto.setTrnxDate(DateUtils.sysDateStringSlash());
		requestDto.setIsAuto(true);
		requestDto.setIsBatch(false);
		requestDto.setIsExecuted(false);
		requestDto.setIsTxn(false);
		requestDto.setMemberId(null);
		requestDto.setPolicyId(policyEntity.getPolicyId());
		requestDto.setRecalculate(true);
		requestDto.setSetOpeningBalance(false);
		requestDto.setTxnType(null);
		requestDto.setTxnSubType(null);

		try {
			return fundRestApiService.viewPolicyFundDetails(requestDto);
		} catch (ApplicationException e) {
			logger.error("viewPolicyFundDetails::{}", requestDto.getPolicyId());
		}
		return fundResponseDto;

		/** return fundResponseDto; */
	}

	@Override
	public ApiResponseDto<SurrenderChargeCalResponse> surrenderPayoutCalculation(
			SurrenderChargeCalRequest surrenderChargeCalRequest) throws ApplicationException {
		SurrenderChargeCalResponse surrenderChargeCalResponse = new SurrenderChargeCalResponse();
		double wsvCharges = 0.0;
		int tenureCheck = 0;
		double tenure = 0.0;
		double commutationAmount = 0.0;
		double exitLoad = 0.0;
		double exitLoadRate = 0.0;
		BigDecimal policyFundValue = null;

		BigDecimal fundValue = NumericUtils.doubleToBigDecimal(surrenderChargeCalRequest.getFundAmount());
		try {
			logger.info("tempClaimCalcServiceImpl::surrenderPayoutCalculation::start::{}");

			/*** Note:-get Policy Related Details ***/
			Object policyEntityObj = policyMasterRepository
					.fetchPolicyDetaislBypolicyNumber(surrenderChargeCalRequest.getPolicyNumber());

			if (policyEntityObj == null) {
				return ApiResponseDto.error(ErrorDto.builder().message("Not Found Policy Details").build());
			}
			Object[] obj = (Object[]) policyEntityObj;
			PolicyMasterEntity policyEntity = new PolicyMasterEntity();
			policyEntity.setPolicyId(NumericUtils.stringToLong(String.valueOf(obj[9])));
			policyEntity.setArd(DateUtils.convertStringToDate(String.valueOf(obj[11])));
			policyEntity.setPolicyCommencementDt(DateUtils.convertStringToDate(String.valueOf(obj[2])));
			policyEntity.setVariant(String.valueOf(obj[23]));
			LocalDateTime fromDateTime = DateUtils.dateToLocalDateTime(policyEntity.getPolicyCommencementDt());
			LocalDateTime toDateTime = DateUtils.dateToLocalDateTime(policyEntity.getArd());

			InterestFundResponseDto response = getPolicyFundValueBatchApi(policyEntity);
			if (response != null) {
				policyFundValue = response.getPolicyAccountValue();
			}

			long diffInDays = ChronoUnit.DAYS.between(fromDateTime, toDateTime);
			tenure = diffInDays / 365;
			if (policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DB_V1)
					|| policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V1)) {

				if (fundValue.compareTo(BigDecimal.ZERO) < 40000000 && tenure < 1) {
					wsvCharges = 0.925;
				} else if (fundValue.compareTo(BigDecimal.ZERO) < 40000000 && tenure >= 1 && tenure < 2) {
					wsvCharges = 0.930;
				} else if (fundValue.compareTo(BigDecimal.ZERO) < 30000000 && tenure >= 2 && tenure < 3) {
					wsvCharges = 0.935;
				} else if (fundValue.compareTo(BigDecimal.ZERO) < 45000000 && tenure >= 3 && tenure < 4) {
					wsvCharges = 0.940;
				} else if (fundValue.compareTo(BigDecimal.ZERO) < 20000000 && tenure >= 4 && tenure < 5) {
					wsvCharges = 0.945;
				} else if (fundValue.compareTo(BigDecimal.ZERO) < 40000000 && tenure >= 5) {
					wsvCharges = 0.950;
				} else if (fundValue.compareTo(BigDecimal.ZERO) >= 50000000
						&& fundValue.compareTo(BigDecimal.ZERO) < 60000000 && tenure >= 5) {
					wsvCharges = 0.955;
				} else if (fundValue.compareTo(BigDecimal.ZERO) >= 60000000
						&& fundValue.compareTo(BigDecimal.ZERO) < 70000000 && tenure >= 5) {
					wsvCharges = 0.960;
				} else if (fundValue.compareTo(BigDecimal.ZERO) >= 70000000
						&& fundValue.compareTo(BigDecimal.ZERO) < 80000000 && tenure >= 5) {
					wsvCharges = 0.965;
				} else if (fundValue.compareTo(BigDecimal.ZERO) >= 80000000
						&& fundValue.compareTo(BigDecimal.ZERO) < 90000000 && tenure >= 5) {
					wsvCharges = 0.970;
				} else if (fundValue.compareTo(BigDecimal.ZERO) >= 90000000
						&& fundValue.compareTo(BigDecimal.ZERO) < 100000000 && tenure >= 5) {
					wsvCharges = 0.975;
				} else if (fundValue.compareTo(BigDecimal.ZERO) >= 100000000
						&& fundValue.compareTo(BigDecimal.ZERO) < 200000000 && tenure >= 5) {
					wsvCharges = 0.98;
				} else if (fundValue.compareTo(BigDecimal.ZERO) >= 200000000
						&& fundValue.compareTo(BigDecimal.ZERO) < 500000000 && tenure >= 5) {
					wsvCharges = 0.9825;
				} else if (fundValue.compareTo(BigDecimal.ZERO) >= 500000000 && tenure >= 5) {
					wsvCharges = 0.985;
				}
				exitLoadRate = (1-wsvCharges) * 100;
				commutationAmount = fundValue.doubleValue() * wsvCharges;
//				double wsv = fundValue.doubleValue() *  wsvCharges;
//				commutationAmount = fundValue.doubleValue() - wsv;
				exitLoad = fundValue.doubleValue() - commutationAmount;
				surrenderChargeCalResponse.setCommutationAmount(
						(commutationAmount > 0.0) ? NumericUtils.doubleRoundInMath(commutationAmount, 0) : 0d);
				surrenderChargeCalResponse
						.setExistLoad((exitLoad > 0.0) ? NumericUtils.doubleRoundInMath(exitLoad, 0) : 0d);
				surrenderChargeCalResponse.setExistLoadRate(NumericUtils.doubleRoundInMath(exitLoadRate,0));
				surrenderChargeCalResponse.setMarketValueAdjustmentApplicable(false);
				return ApiResponseDto.success(surrenderChargeCalResponse, ClaimConstants.SUCCESS);
			} else {
//				/** Note:-MVA Calculation-- start**/
//				ApiResponseDto<SurrenderChargeCalResponse> marketValueDetails=marketValuePayoutCalculation(surrenderChargeCalRequest);
//				/** Note:-MVA Calculation-- end**/
//				surrenderChargeCalResponse.setMvaExistLoadRate(marketValueDetails.getData().getExistLoadRate());
//				surrenderChargeCalResponse.setMvaExistLoad(marketValueDetails.getData().getCommutationAmount());

				if (tenure < 3) {
					wsvCharges = 0.0005;
					exitLoadRate = wsvCharges * 100;
					tenureCheck = 1;
					double wsv = surrenderChargeCalRequest.getFundAmount() * wsvCharges;

					if (wsv > 500000) {
						exitLoad = 500000;
					} else {
						exitLoad = wsv * tenureCheck;
					}

					commutationAmount = surrenderChargeCalRequest.getFundAmount() - exitLoad;
					surrenderChargeCalResponse.setCommutationAmount(NumericUtils.doubleRoundInMath(commutationAmount,0));
					surrenderChargeCalResponse.setExistLoad(NumericUtils.doubleRoundInMath(wsv,0));
					surrenderChargeCalResponse.setExistLoadRate(exitLoadRate);
					surrenderChargeCalResponse.setMarketValueAdjustmentApplicable(false);
					logger.info("tempClaimCalcServiceImpl::surrenderPayoutCalculation::end::{}");
					return ApiResponseDto.success(surrenderChargeCalResponse, ClaimConstants.SUCCESS);

				}

				else {
					surrenderChargeCalResponse.setExistLoad(NumericUtils.doubleRoundInMath(surrenderChargeCalRequest.getFundAmount(),0));
					surrenderChargeCalResponse.setExistLoadRate(exitLoadRate);
					surrenderChargeCalResponse.setMarkerValue(0.0);
					surrenderChargeCalResponse.setMarketValueAdjustmentApplicable(true);
					logger.info("tempClaimCalcServiceImpl::surrenderPayoutCalculation::end::{}");
					return ApiResponseDto.success(surrenderChargeCalResponse, ClaimConstants.SUCCESS);
				}
			}
		} catch (IllegalArgumentException e) {
			logger.error("tempClaimCalcServiceImpl::surrenderPayoutCalculation::error::{}" + e.getMessage());
			return ApiResponseDto.error(ErrorDto.builder().message(e.getMessage()).build());
		}

	}

	public static String removeScientificNotation(String value) {
		return new BigDecimal(value).toPlainString();
	}

	@Override
	public ApiResponseDto<SurrenderChargeCalResponse> marketValuePayoutCalculation(
			SurrenderChargeCalRequest surrenderChargeCalRequest) throws ApplicationException {
		SurrenderChargeCalResponse surrenderChargeCalResponse = new SurrenderChargeCalResponse();
		try {
			double tenure = 0.0;
			double commutationAmount = 0.0;
			double marketValueAdjustment = 0.0;
			double withdraw = 0.0;
			double mvf = 0.0;
			BigDecimal openingBalance = null;
			/*** Note:-get Policy Related Details ***/
			Object policyEntityResponse = policyMasterRepository
					.fetchPolicyDetaislBypolicyNumber(surrenderChargeCalRequest.getPolicyNumber());
			PolicyMasterEntity policyEntity = new PolicyMasterEntity();
			if (policyEntityResponse != null) {
				logger.info("TempClaimCalServiceImpl --marketValuePayoutCalculation---start");
				Object[] ob = (Object[]) policyEntityResponse;
				policyEntity.setPolicyCommencementDt(DateUtils.convertStringToDate(String.valueOf(ob[2])));
				policyEntity.setArd(DateUtils.convertStringToDate(String.valueOf(ob[11])));
				policyEntity.setVariant(String.valueOf(ob[21]));
				policyEntity.setPolicyId(NumericUtils.stringToLong(String.valueOf(ob[9])));
			}

			LocalDateTime fromDateTime = DateUtils.dateToLocalDateTime(policyEntity.getPolicyCommencementDt());
			LocalDateTime toDateTime = DateUtils.dateToLocalDateTime(policyEntity.getArd());

			long diffInDays = ChronoUnit.DAYS.between(fromDateTime, toDateTime);
			tenure = diffInDays / 365;
			if (!policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DB_V1)
					|| !policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V1)) {
				CommonResponseDto common = commonService
						.getFinancialYeartDetails(DateUtils.dateToHypenStringDDMMYYYY(DateUtils.sysDate()));
				if (policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DB_V2)
						|| policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V2)) {
					openingBalance = policyTransactionSummaryRepository
							.getByPolicyIdAndfinancialYearAndQuarterAndTotalContributionAndDesOrder(
									policyEntity.getPolicyId(), common.getFinancialYear(),
									NumericUtils.stringToInteger(common.getFrequency()));
				} else {
					openingBalance = policyTransactionSummaryRepository
							.getByPolicyIdAndfinancialYearAndTotalContributionAndDesOrder(policyEntity.getPolicyId(),
									common.getFinancialYear());
				}
				if (openingBalance == null || openingBalance.compareTo(BigDecimal.ZERO) == 0) {
					return ApiResponseDto.error(ErrorDto.builder().message("Opening Balance is Empty").build());
				} else {
					/*** withDrawalPer=1-(openingBalance-fundAmount/openingBalance) ***/

					withdraw = 1 - ((openingBalance.doubleValue() - surrenderChargeCalRequest.getFundAmount())
							/ openingBalance.doubleValue());

				}

				/***
				 * Note:-withDrawal Percentage Cumulative Check
				 ***/
				Double withdrawPer = checkerWithDrawalPercentage(policyEntity.getPolicyId(), withdraw);

				surrenderChargeCalResponse.setExistLoadRate(withdrawPer);

				if (withdrawPer >= 0.25 && (policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V2)
						|| policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V2))) {
					mvf = surrenderChargeCalRequest.getMarketValue();
					/*** Note:- mva=fundAmount*(1-mvf) ***/
					marketValueAdjustment = surrenderChargeCalRequest.getFundAmount() * (1 - mvf);
				}

				else if (withdrawPer >= 0.25 && openingBalance.doubleValue() >= 1000000
						&& (policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V3)
								|| policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V3))) {
					mvf = surrenderChargeCalRequest.getMarketValue();
					marketValueAdjustment = surrenderChargeCalRequest.getFundAmount() * (1 - mvf);
				}

				else if (withdraw < 0.25) {
					mvf = 0.0;
					marketValueAdjustment = surrenderChargeCalRequest.getFundAmount();
				}

				if (marketValueAdjustment > 0.0) {

					commutationAmount = surrenderChargeCalRequest.getFundAmount()
							- ((surrenderChargeCalRequest.getFundAmount() - marketValueAdjustment)
									+ surrenderChargeCalRequest.getExistLoad());

				} else {
//				exitAmount = exitAmount - applicableValue;
					commutationAmount = surrenderChargeCalRequest.getFundAmount()
							- surrenderChargeCalRequest.getExistLoad();
				}

				surrenderChargeCalResponse.setCommutationAmount(commutationAmount);
				surrenderChargeCalResponse.setMarkerValue(mvf);
				surrenderChargeCalResponse.setMarketValueAdjustment(marketValueAdjustment);
				surrenderChargeCalResponse.setMvaExistLoad(withdraw);
				surrenderChargeCalResponse.setMvaExistLoadRate(withdrawPer);

				return ApiResponseDto.success(surrenderChargeCalResponse, ClaimConstants.SUCCESS);
			}
		} catch (Exception e) {
			return ApiResponseDto.error(ErrorDto.builder().message(e.getMessage()).build());
		}

		return null;
	}

	private Double checkerWithDrawalPercentage(Long policyId, double withdraw) {
		logger.info("TempClaimCalcServiceImpl{}--checkerWithDrawalPercentage---Start");
		double withdrawPer = 0d;
		ClaimPolicyWithdrawalPercentageEntity claimPolicyWithdrawalPercentageEntity = claimPolicyWithdrawalPercentageRepo
				.findByPolicyIdAndStatusAndIsActiveTrue(policyId, ClaimConstants.ACTIVE);
		if (claimPolicyWithdrawalPercentageEntity != null) {
			withdrawPer = (claimPolicyWithdrawalPercentageEntity.getCumPercentage() / 100) + withdraw;
			return withdrawPer;
		} else {
			logger.info("TempClaimCalcServiceImpl{}--checkerWithDrawalPercentage---ended");
			return withdrawPer = withdraw;
		}
	}

	@Override
	public ApiResponseDto<String> getGsaGnPolicyConvertToWithdrawral(Long claimId, String type) {
		String message="";
		logger.info("claimServiceImpl:getGsaGnPolicyConvertToWithdrawral Method:Start");
		ApiResponseDto<String> response = new ApiResponseDto();
		
		try {
			Optional<TempClaimEntity> optClaimEntity = tempClaimRepository.findByClaimIdAndIsActiveTrue(claimId);
			
			if (optClaimEntity.isPresent()) {
				
				TempClaimEntity claimEntity = optClaimEntity.get();
				if ((claimEntity.getModeOfExit().equals(ClaimConstants.RETRIERMENT) || claimEntity.getModeOfExit().equals(ClaimConstants.RESIGNATION)) 
						&& claimEntity.getPolicyType().equals(ClaimConstants.DC) && claimEntity.getProductId() == ClaimConstants.GSAGN_PRODUCT && claimEntity.getVariant().equals(ClaimConstants.GSAGN_VARIENT)) {
					
					 message=tempSaveTempClaimService.insertDataForChangetoWithdrawalGSAGNPolicy(claimEntity.getClaimNo(),type);
					
					
				}
			}
			else {
				logger.info("claimServiceImpl:getGsaGnPolicyConvertToWithdrawral Method:error");
				return ApiResponseDto.success(ClaimConstants.NO_RECORD_FOUND);
			}
			
			return ApiResponseDto.success(message);

		} catch (Exception e) {
			logger.error("claimServiceImpl:getGsaGnPolicyConvertToWithdrawral Method:error" + e.getMessage());
			return ApiResponseDto.error(ErrorDto.builder().message(e.getMessage()).build());
		}
		
		
	}

//	@Override
//	public ApiResponseDto<SurrenderChargeCalResponse> marketValuePayoutCalculation(
//			SurrenderChargeCalRequest surrenderChargeCalRequest) throws ApplicationException {
//		SurrenderChargeCalResponse surrenderChargeCalResponse = new SurrenderChargeCalResponse();
//		try {
//			double tenure = 0.0;
//			double commutationAmount = 0.0;
//			double marketValueAdjustment = 0.0;
//			double withdraw = 0.0;
//			double mvf = 0.0;
//			BigDecimal openingBalance;
//			/*** Note:-get Policy Related Details ***/
//			Object policyEntityResponse = policyMasterRepository
//					.fetchPolicyDetaislBypolicyNumber(surrenderChargeCalRequest.getPolicyNumber());
//			PolicyMasterEntity policyEntity = new PolicyMasterEntity();
//			if (policyEntityResponse != null) {
//				logger.info("TempClaimCalServiceImpl --marketValuePayoutCalculation---start");
//				Object[] ob = (Object[]) policyEntityResponse;
//				policyEntity.setPolicyCommencementDt(DateUtils.convertStringToDate(String.valueOf(ob[2])));
//				policyEntity.setArd(DateUtils.convertStringToDate(String.valueOf(ob[11])));
//				policyEntity.setVariant(String.valueOf(ob[21]));
//				policyEntity.setPolicyId(NumericUtils.stringToLong(String.valueOf(ob[9])));
//			}
//
//			LocalDateTime fromDateTime = DateUtils.dateToLocalDateTime(policyEntity.getPolicyCommencementDt());
//			LocalDateTime toDateTime = DateUtils.dateToLocalDateTime(policyEntity.getArd());
//
//			long diffInDays = ChronoUnit.DAYS.between(fromDateTime, toDateTime);
//			tenure = diffInDays / 365;
//			if (!policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DB_V1)
//					|| !policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V1)) {
//				CommonResponseDto common = commonService
//						.getFinancialYeartDetails(DateUtils.dateToHypenStringDDMMYYYY(DateUtils.sysDate()));
//				if (policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DB_V2)
//						|| policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V2)) {
//					openingBalance = policyTransactionSummaryRepository
//							.getByPolicyIdAndfinancialYearAndQuarterAndTotalContributionAndDesOrder(
//									policyEntity.getPolicyId(), common.getFinancialYear(),
//									NumericUtils.stringToInteger(common.getFrequency()));
//				} else {
//					openingBalance = policyTransactionSummaryRepository
//							.getByPolicyIdAndfinancialYearAndTotalContributionAndDesOrder(policyEntity.getPolicyId(),
//									common.getFinancialYear());
//				}
//				if(openingBalance.compareTo(BigDecimal.ZERO) == 0 ||openingBalance==null) {
//					return ApiResponseDto.error(ErrorDto.builder().message("Opening Balance is Empty").build());
//				}
//				else {
//					/***withDrawalPer=1-(openingBalance-fundAmount/openingBalance)***/
//					
//					withdraw = 1 - ((openingBalance.doubleValue() - surrenderChargeCalRequest.getFundAmount())
//							/ openingBalance.doubleValue());
//				}
//				
//				/***
//				 * Note:-withDrawal Percentage Cumulative Check
//				 ***/
//				Double withdrawPer=checkerWithDrawalPercentage(policyEntity.getPolicyId(),withdraw);
//				
//				
//				surrenderChargeCalResponse.setExistLoadRate(withdrawPer);
//				
//				if(withdrawPer >= 0.25 &&(policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V2) || 
//						policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V2))){
//					mvf = surrenderChargeCalRequest.getMarketValue();
//					marketValueAdjustment = (openingBalance.doubleValue() - surrenderChargeCalRequest.getMarketValue())
//							/ openingBalance.doubleValue()
//							* ((openingBalance.doubleValue() - surrenderChargeCalRequest.getFundAmount()));
//				}
//				
//				
//				else if (withdrawPer >= 0.25 && openingBalance.doubleValue() >= 1000000 &&(policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V3) || 
//						policyEntity.getVariant().equals(ClaimConstants.POLICY_VARIANT_DC_V3))) {
//					mvf = surrenderChargeCalRequest.getMarketValue();
//					marketValueAdjustment = (openingBalance.doubleValue() - surrenderChargeCalRequest.getMarketValue())
//							/ openingBalance.doubleValue()
//							* ((openingBalance.doubleValue() - surrenderChargeCalRequest.getFundAmount()));
//				}
//				
//				else if (withdraw < 0.25) {
//					mvf = 0.0;
//					marketValueAdjustment = surrenderChargeCalRequest.getFundAmount();
//				}
//				
//				if (marketValueAdjustment > 0.0) {
//					commutationAmount = surrenderChargeCalRequest.getFundAmount() - marketValueAdjustment;
//
//					
//				} else {
////				exitAmount = exitAmount - applicableValue;
//					commutationAmount = surrenderChargeCalRequest.getFundAmount();
//				}
//
//				surrenderChargeCalResponse.setCommutationAmount(commutationAmount);
//				surrenderChargeCalResponse.setMarkerValue(mvf);
//				surrenderChargeCalResponse.setMarketValueAdjustment(marketValueAdjustment);
//
//				return ApiResponseDto.success(surrenderChargeCalResponse, ClaimConstants.SUCCESS);
//			}
//		} catch (Exception e) {
//			return ApiResponseDto.error(ErrorDto.builder().message(e.getMessage()).build());
//		}
//
//		return null;
//	}

}
