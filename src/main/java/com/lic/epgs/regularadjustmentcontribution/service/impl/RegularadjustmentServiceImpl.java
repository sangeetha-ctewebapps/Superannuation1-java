package com.lic.epgs.regularadjustmentcontribution.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * @author pradeepramesh
 *
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.adjustmentcontribution.constants.AdjustmentContibutionConstants;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionResponseDto;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionTempEntity;
import com.lic.epgs.adjustmentcontribution.repository.AdjustmentContributionTempRepository;
import com.lic.epgs.adjustmentcontribution.service.AdjustmentContributionCalcService;
import com.lic.epgs.adjustmentcontribution.service.impl.AdjustmentContributionServiceImpl;
import com.lic.epgs.common.dto.CommonResponseDto;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.common.service.impl.CommonServiceImpl;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.fund.service.FundRestApiService;
import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.policy.dto.MphMasterDto;
import com.lic.epgs.policy.dto.PolicyDepositDto;
import com.lic.epgs.policy.dto.PolicyFrequencyDetailsDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.dto.PolicySearchResponseDto;
import com.lic.epgs.policy.entity.MemberContributionEntity;
import com.lic.epgs.policy.entity.MemberContributionTempEntity;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MemberMasterTempEntity;
import com.lic.epgs.policy.entity.MphBankEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.PolicyContributionEntity;
import com.lic.epgs.policy.entity.PolicyContributionTempEntity;
import com.lic.epgs.policy.entity.PolicyDepositEntity;
import com.lic.epgs.policy.entity.PolicyDepositTempEntity;
import com.lic.epgs.policy.entity.PolicyFrequencyDetailsEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntriesEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntriesTempEntity;
import com.lic.epgs.policy.entity.ZeroAccountTempEntity;
import com.lic.epgs.policy.old.dto.PolicyAddressOldDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentOldDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentResponse;
import com.lic.epgs.policy.old.dto.PolicyDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policy.repository.MemberContributionTempRepository;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.MemberMasterTempRepository;
import com.lic.epgs.policy.repository.MphBankRepository;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.PolicyContributionRepository;
import com.lic.epgs.policy.repository.PolicyContributionTempRepository;
import com.lic.epgs.policy.repository.PolicyDepositRepository;
import com.lic.epgs.policy.repository.PolicyDepositTempRepository;
import com.lic.epgs.policy.repository.PolicyFrequencyRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.policy.repository.ZeroAccountEntriesRepository;
import com.lic.epgs.policy.repository.ZeroAccountEntriesTempRepository;
import com.lic.epgs.policy.repository.ZeroAccountRepository;
import com.lic.epgs.policy.repository.ZeroAccountTempRepository;
import com.lic.epgs.policy.service.impl.PolicyCommonServiceImpl;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularACSaveAdjustmentRequestDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionAllDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionDepositAdjustmentDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionDepositDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionNotesDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionResponseDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentSearchDto;
import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionBatchEntity;
import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionEntity;
import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionNotesEntity;
import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionNotesTempEntity;
import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionTempEntity;
import com.lic.epgs.regularadjustmentcontribution.repository.RegularAdjustmentContributionBatchRepository;
import com.lic.epgs.regularadjustmentcontribution.repository.RegularAdjustmentContributionNotesRepository;
import com.lic.epgs.regularadjustmentcontribution.repository.RegularAdjustmentContributionNotesTempRepository;
import com.lic.epgs.regularadjustmentcontribution.repository.RegularAdjustmentContributionRepository;
import com.lic.epgs.regularadjustmentcontribution.repository.RegularAdjustmentContributionTempRepository;
import com.lic.epgs.regularadjustmentcontribution.service.RegularadjustmentService;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionBatchHistoryResponse;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionBulkErrorDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionBulkResponseDto;
import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionBulkErrorEntity;
import com.lic.epgs.regularadjustmentcontribution.repository.RegularAdjustmentContributionBulkErrorRepository;
import com.lic.epgs.adjustmentcontribution.constants.AdjustmentContibutionConstants;

@Service
public class RegularadjustmentServiceImpl implements RegularadjustmentService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	EntityManager entityManager;
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	FundRestApiService fundRestApiService;
	@Autowired
	CommonService commonService;
	@Autowired
	CommonServiceImpl commonServiceImpl;
	@Autowired
	AdjustmentContributionCalcService adjustmentContributionService;
	@Autowired
	AdjustmentContributionServiceImpl adjustmentContributionServiceImpl;
	@Autowired
	PolicyCommonServiceImpl policyCommonServiceImpl;
	
	@Autowired
	RegularAdjustmentContributionTempRepository regularAdjustmentContributionTempRepository;
	@Autowired
	AdjustmentContributionTempRepository adjustmentContributionTempRepository;
	@Autowired
	RegularAdjustmentContributionTempRepository adjTempRepo;
	@Autowired
	RegularAdjustmentContributionRepository regAdjustConRep;
	@Autowired
	RegularAdjustmentContributionNotesTempRepository adjNotesTempRepo;
	@Autowired
	RegularAdjustmentContributionNotesRepository adjNotesRepo;
	@Autowired
	MphMasterRepository mphMasterRepository;
	@Autowired
	MphBankRepository mphBankRepository;
	@Autowired
	PolicyMasterRepository policyMasterRepository;
	@Autowired
	MemberContributionTempRepository memberContributionTempRepository;
	@Autowired
	PolicyContributionRepository policyContributionRepository;
	@Autowired
	PolicyContributionTempRepository policyContributionTempRepository;
	@Autowired
	PolicyDepositTempRepository policyDepositTempRepository;
	@Autowired
	PolicyDepositRepository policyDepositRepository;
	@Autowired
	ZeroAccountEntriesRepository zeroAccountEntriesRepository;
	@Autowired
	ZeroAccountRepository zeroAccountRepository;
	@Autowired
	ZeroAccountEntriesTempRepository zeroAccountEntriesTempRepository;
	@Autowired
	ZeroAccountTempRepository zeroAccountTempRepository;
	@Autowired
	PolicyFrequencyRepository policyFrequencyRepo;
	@Autowired
	RegularAdjustmentContributionBatchRepository regularAdjustmentContributionBatchRepository;
	@Autowired
	MemberMasterRepository memberMasterRepository;
	@Autowired
	MemberMasterTempRepository memberMasterTempRepository;
	
	@Autowired
	private RegularAdjustmentContributionBulkErrorRepository regularAdjustmentContributionBulkErrorRepository;
	

	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
	}
	public synchronized String getAdjNumber() {
		return commonService.getSequence(CommonConstants.REG_NUMBER_SEQ);
	}
	public synchronized String getChallanSeq() {
		return commonService.getSequence(CommonConstants.CHALLAN_SEQ);
	}
	

	@Override
	public PolicyResponseDto newcitrieaSearchPradeep(PolicySearchDto policySearchDto) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		List<PolicyDto> policyDtoList = new ArrayList<>();
		try {
			logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchPradeep :: Start");
			String policyNumber = "";
			String unitCode = "";
			Boolean isActive = Boolean.TRUE;
			if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())) {
				policyNumber = policySearchDto.getPolicyNumber();
			}
			if (StringUtils.isNotBlank(policySearchDto.getUnitCode())) {
				unitCode = policySearchDto.getUnitCode();
			}
			List<Object> result = policyMasterRepository.policySearchPradeepForSusequentSearch(policyNumber, unitCode,
					isActive);
			if (result != null && !result.isEmpty()) {
				for (Object object : result) {
					Object[] ob = (Object[]) object;
					PolicyDto resonseDto = new PolicyDto();
					resonseDto.setMphId(NumericUtils.convertStringToLong(String.valueOf(ob[0])));
					resonseDto.setMphCode(String.valueOf(ob[1]));
					resonseDto.setMphName(String.valueOf(ob[2]));
					resonseDto.setPolicyId(NumericUtils.convertStringToLong(String.valueOf(ob[3])));
					resonseDto.setPolicyNumber(String.valueOf(ob[4]));
					resonseDto.setPolicyStatus(String.valueOf(ob[5]));
					resonseDto.setUnitCode(String.valueOf(ob[6]));
					resonseDto.setProposalNumber(String.valueOf(ob[7]));
					resonseDto.setProduct(String.valueOf(ob[8]));
					resonseDto.setFrequency(NumericUtils.convertStringToInteger(String.valueOf(ob[9])));
					policyDtoList.add(resonseDto);
				}
				commonDto.setPolicyDtos(policyDtoList);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
				commonDto.setTransactionStatus(CommonConstants.STATUS);
			} else {
				commonDto.setTransactionMessage(CommonConstants.DENY);
				commonDto.setTransactionStatus(CommonConstants.STATUS);
			}

		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: newcitrieaSearchPradeep ", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchPradeep :: Ends");
		}
		return commonDto;
	}

	@Override
	public AdjustmentContributionResponseDto newcitrieaSearchById(Long mphId, Long policyId) {
		AdjustmentContributionResponseDto commonDto = new AdjustmentContributionResponseDto();
		logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchById :: Start");
		try {
			Boolean isActive = Boolean.TRUE;
			List<Object> result = policyMasterRepository.getPolicyDetailsByMphID(mphId, policyId, isActive);
			String lastPremiumDueDate = policyContributionRepository.findPolicyId(policyId);
			PolicyDto resonseDto = new PolicyDto();
			if (result != null && !result.isEmpty()) {
				
				
				/*List<PolicyFrequencyDetailsEntity> entity = policyFrequencyRepo
						.findAllByPolicyIdAndStatusAndIsActiveTrue(policyId,CommonConstants.PAID);
				
                   if(entity.isEmpty()) {
					
					List<PolicyFrequencyDetailsEntity> policyFrequencyDetailsEntityList = policyFrequencyRepo
							.findAllByPolicyIdAndStatusAndIsActiveTrue(policyId,CommonConstants.UNPAID); 
					
					if(!policyFrequencyDetailsEntityList.isEmpty()) {
						
				for(PolicyFrequencyDetailsEntity policyFrequencyDetailsEntity : policyFrequencyDetailsEntityList) {
							  
					Date frequencyDate = policyFrequencyDetailsEntity.getFrequencyDates();
							  
					Date currentDate = DateUtils.sysDate();
							
				if(frequencyDate.before(currentDate)) {*/
				
				
				for (Object object : result) {
					Object[] ob = (Object[]) object;
					resonseDto.setMphCode(String.valueOf(ob[0]));
					resonseDto.setMphName(String.valueOf(ob[2]));
					resonseDto.setProposalNumber(String.valueOf(ob[3]));
					resonseDto.setPolicyNumber(String.valueOf(ob[7]));
					resonseDto.setPolicyStatus(String.valueOf(ob[8]));
					resonseDto.setQuotationType(String.valueOf(ob[9]));
					resonseDto.setPolicyCommencementDate(DateUtils.convertStringToDate(ob[10].toString()));
					resonseDto.setProduct(String.valueOf(ob[11]));
					resonseDto.setVariant(String.valueOf(ob[12]));
					resonseDto.setUnitCode(String.valueOf(ob[13]));
					resonseDto.setFrequency(NumericUtils.convertStringToInteger(String.valueOf(ob[14])));
					resonseDto.setLastPremiumDueDate(lastPremiumDueDate);
					resonseDto.setMphId(NumericUtils.convertStringToLong(String.valueOf(ob[1])));
					resonseDto.setTempMphId(NumericUtils.convertStringToLong(String.valueOf(ob[4])));
					resonseDto.setTempPolicyId(NumericUtils.convertStringToLong(String.valueOf(ob[5])));
					resonseDto.setPolicyId(NumericUtils.convertStringToLong(String.valueOf(ob[6])));
					
				}
				String policyNumber = resonseDto.getPolicyNumber();
				AdjustmentContributionTempEntity adjustmentContributionTempEntity = adjustmentContributionTempRepository
						.findByPolicyNumberAndAdjustmentContributionStatusInAndIsActiveTrue(policyNumber,
								CommonConstants.adjustmentCheckStatus());
				RegularAdjustmentContributionTempEntity regularAdjustmentContributionTempEntity = regularAdjustmentContributionTempRepository
						.findByPolicyNumberAndRegularContributionStatusInAndIsActiveTrue(policyNumber,
								CommonConstants.adjustmentCheckStatus());
				if (adjustmentContributionTempEntity == null && regularAdjustmentContributionTempEntity == null) {
					
					
					logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchPradeep :: saveMphTemp :: Start");
					adjustmentContributionServiceImpl.saveMphTemp(resonseDto.getMphId());
					logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchPradeep :: saveMphTemp :: Ends");
					logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchPradeep :: savePolicyTemp :: Start");
					adjustmentContributionServiceImpl.savePolicyTemp(resonseDto.getPolicyId(), resonseDto.getMphId());
					logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchPradeep :: savePolicyTemp :: Ends");
				
					Object ids = policyMasterRepository.getIdsofPolicy(resonseDto.getPolicyId());
					Object[] ob = (Object[]) ids;
					commonDto.setPolicyId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[0])));
					commonDto.setTempPolicyId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[1])));
					commonDto.setMphId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[2])));
					commonDto.setTempMphId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[3])));
				
					resonseDto.setPolicyId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[0])));
					resonseDto.setTempPolicyId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[1])));
					resonseDto.setMphId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[2])));
					resonseDto.setTempMphId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[3])));
					
					commonDto.setResponseData(resonseDto);

					logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchPradeep :: calculateIsOneYrPolicy :: Start");
					commonDto.setIsCommencementdateOneYr(calculateIsOneYrPolicy(resonseDto.getPolicyCommencementDate()));
					logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchPradeep :: calculateIsOneYrPolicy :: Ends");
					logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchPradeep :: checkZeroRow :: Start");
					commonDto.setZeroRow(checkZeroRow(resonseDto.getPolicyId()));
					logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchPradeep :: checkZeroRow :: Ends");
					logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchPradeep :: getPolicyBankList :: Start");
					commonDto.setBank(getPolicyBankList(resonseDto.getPolicyId()));
					logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchPradeep :: getPolicyBankList :: Ends");
					commonDto.setTransactionMessage(PolicyConstants.FETCH);
					commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
					
				} else {
					commonDto.setTransactionMessage(CommonConstants.RECORD_ALREADY_USED_IN_ADJUSTMENTS);
					commonDto.setTransactionStatus(CommonConstants.FAIL);
				}
//				} 
//				else {
//					commonDto.setTransactionMessage(CommonConstants.FREQUENCY_DATE);
//					commonDto.setTransactionStatus(CommonConstants.FAIL);
//				}
//			}
//			}
//			}
//              else {
//   					commonDto.setTransactionMessage(CommonConstants.ADJUSTMENT_USED);
//   					commonDto.setTransactionStatus(CommonConstants.FAIL);
//   				}
			} else {
				commonDto.setTransactionMessage(CommonConstants.DENY);
				commonDto.setTransactionStatus(CommonConstants.FAIL);
			}
		} catch (Exception e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: newcitrieaSearchById ", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("RegularadjustmentServiceImpl :: newcitrieaSearchById :: Ends");
		return commonDto;
	}

	private List<MphBankEntity> getPolicyBankList(Long policyId) {
		List<MphBankEntity> policyBankEntity = new ArrayList<>();
		try {
			logger.info("RegularadjustmentServiceImpl :: getPolicyBankList :: Start");
			Long mphId = policyMasterRepository.findMphIdfromPolicy(policyId, Boolean.TRUE);
			if (mphId != null) {
				policyBankEntity = mphBankRepository.findAllByMphIdAndIsActiveTrue(mphId);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: getPolicyBankList ", e);
		} finally {
			logger.info("RegularadjustmentServiceImpl :: getPolicyBankList :: Start");
		}
		return policyBankEntity;
	}

	private ZeroAccountEntity checkZeroRow(Long policyId) {
		return zeroAccountRepository.findByPolicyIdAndIsActiveTrue(policyId);
	}
		
	private Boolean calculateIsOneYrPolicy(Date policyCommencementDate) {
		logger.info("RegularadjustmentServiceImpl :: calculateIsOneYrPolicy :: Start");
		Boolean isNewDate = false;
		if (policyCommencementDate != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(policyCommencementDate);
			c.add(Calendar.YEAR, 1);
			Date newDate = c.getTime();
			Calendar currentTime = Calendar.getInstance();
			Date currentDate = currentTime.getTime();
			if (newDate.equals(currentDate) || newDate.before(currentDate)) {
				logger.info("RegularadjustmentServiceImpl :: calculateIsOneYrPolicy :: Ends {}", true);
				return true;
			}
		}
		logger.info("RegularadjustmentServiceImpl :: calculateIsOneYrPolicy :: Ends {} ", isNewDate);
		return isNewDate;
	}

	@Override
	public RegularAdjustmentContributionResponseDto save(RegularAdjustmentContributionDto request) {
		RegularAdjustmentContributionResponseDto responseDto = new RegularAdjustmentContributionResponseDto();
		RegularAdjustmentContributionAllDto adjustmentContributionAllDto = new RegularAdjustmentContributionAllDto();
		try {
			logger.info("RegularadjustmentServiceImpl :: save :: Start");
			RegularAdjustmentContributionTempEntity responseEntity = new RegularAdjustmentContributionTempEntity();
			RegularAdjustmentContributionTempEntity saveAdjEntity = new RegularAdjustmentContributionTempEntity();
			if (request.getRegularContributionId() != null && request.getRegularContributionId() != 0) {
				RegularAdjustmentContributionTempEntity entity = adjTempRepo
						.findByRegularContributionIdAndIsActiveTrue(request.getRegularContributionId());

				saveAdjEntity.setRegularContributionId(entity.getRegularContributionId());
				saveAdjEntity.setBatchId(entity.getBatchId());
				saveAdjEntity.setTotalContribution(entity.getTotalContribution());
				saveAdjEntity.setEmployerContribution(entity.getEmployerContribution());
				saveAdjEntity.setEmployeeContribution(entity.getEmployeeContribution());
				saveAdjEntity.setVoluntaryContribution(entity.getVoluntaryContribution());
				saveAdjEntity.setUnitCode(entity.getUnitCode());
				saveAdjEntity.setModifiedBy(entity.getModifiedBy());
				saveAdjEntity.setCreatedBy(entity.getCreatedBy());
				saveAdjEntity.setRegularContributionNumber(getAdjNumber());
				saveAdjEntity.setRegularContributionStatus(PolicyConstants.DRAFT_NO);
				saveAdjEntity.setWorkFlowStatus(PolicyConstants.DRAFT_NO);
				saveAdjEntity.setCreatedOn(new Date());
				saveAdjEntity.setModifiedOn(new Date());
				saveAdjEntity.setIsActive(Boolean.TRUE);
				saveAdjEntity.setPolicyId(entity.getPolicyId());
				saveAdjEntity.setTempPolicyId(entity.getTempPolicyId());
				saveAdjEntity.setPolicyNumber(entity.getPolicyNumber());
				saveAdjEntity.setPolicyStatus(entity.getPolicyStatus());
				saveAdjEntity.setPolicyType(entity.getPolicyType());
				saveAdjEntity.setProposalNumber(entity.getProposalNumber());
				saveAdjEntity.setProduct(request.getProduct());
				saveAdjEntity.setVariant(request.getVariant());
				saveAdjEntity.setRejectionReasonCode(entity.getRejectionReasonCode());
				saveAdjEntity.setRejectionRemarks(entity.getRejectionRemarks());
				saveAdjEntity.setPolicyCommencementDate(request.getPolicyCommencementDate());
				saveAdjEntity.setCustomerId(request.getCustomerId());
				saveAdjEntity.setCustomerCode(request.getCustomerCode());
				saveAdjEntity.setCustomerName(request.getCustomerName());
				saveAdjEntity.setMphName(request.getMphName());
				saveAdjEntity.setMphCode(request.getMphCode());
				saveAdjEntity.setTotalDeposit(request.getTotalDeposit());
				saveAdjEntity.setIsDeposit(request.getIsDeposit());
				saveAdjEntity.setAmountToBeAdjusted(request.getAmountToBeAdjusted());
				saveAdjEntity.setFirstPremium(request.getFirstPremium());
				saveAdjEntity.setSinglePremiumFirstYr(request.getSinglePremiumFirstYr());
				saveAdjEntity.setRenewalPremium(request.getRenewalPremium());
				saveAdjEntity.setSubsequentSinglePremium(request.getSubsequentSinglePremium());
				saveAdjEntity.setIsCommencementdateOneYr(calculateIsOneYrPolicy(request.getPolicyCommencementDate()));
				saveAdjEntity.setAdjustmentForDate(new Date());
				saveAdjEntity.setAdjustmentDueDate(request.getAdjustmentForDate());
				responseEntity = adjTempRepo.save(saveAdjEntity);
			} else {
				saveAdjEntity = modelMapper.map(request, RegularAdjustmentContributionTempEntity.class);
				saveAdjEntity.setIsActive(true);
				saveAdjEntity.setIsDeposit(request.getIsDeposit());
				saveAdjEntity.setIsCommencementdateOneYr(calculateIsOneYrPolicy(request.getPolicyCommencementDate()));
				saveAdjEntity.setRegularContributionStatus(PolicyConstants.DRAFT_NO);
				saveAdjEntity.setRegularContributionNumber(getAdjNumber());
				saveAdjEntity.setTempPolicyId(request.getTempPolicyId());
				saveAdjEntity.setAdjustmentForDate(new Date());
				saveAdjEntity.setAdjustmentDueDate(request.getAdjustmentForDate());
				responseEntity = adjTempRepo.save(saveAdjEntity);
			}
			for (PolicyDepositDto policyDeposit : request.getRegAdjustmentContibutionDeposits()) {
				PolicyDepositTempEntity policyDepositEntity = new PolicyDepositTempEntity();
				policyDepositEntity
						.setDepositId(policyDeposit.getDepositId() != null ? policyDeposit.getDepositId() : null);
				policyDepositEntity.setPolicyId(responseEntity.getTempPolicyId());
				policyDepositEntity.setMasterPolicyId(responseEntity.getPolicyId());
				policyDepositEntity.setContributionType(PolicyConstants.REGULARADJUSTMENTNEW);
				policyDepositEntity.setAdjustmentContributionId(policyDeposit.getAdjustmentContributionId());
				policyDepositEntity.setRegularContributionId(responseEntity.getRegularContributionId());
				policyDepositEntity.setCollectionNo(policyDeposit.getCollectionNo());
				policyDepositEntity.setCollectionDate(policyDeposit.getCollectionDate());
				policyDepositEntity.setCollectionStatus(policyDeposit.getCollectionStatus());
				policyDepositEntity
						.setChallanNo(policyDeposit.getChallanNo() != null && !"0".equals(policyDeposit.getChallanNo())
								? policyDeposit.getChallanNo()
								: getChallanSeq());
				policyDepositEntity.setDepositAmount(policyDeposit.getDepositAmount());
				policyDepositEntity.setAdjustmentAmount(policyDeposit.getAdjustmentAmount());
				policyDepositEntity.setAvailableAmount(policyDeposit.getAvailableAmount());
				policyDepositEntity.setTransactionMode(policyDeposit.getTransactionMode());
				policyDepositEntity.setChequeRealisationDate(policyDeposit.getChequeRealisationDate());
				policyDepositEntity.setRemark(policyDeposit.getRemark());
				policyDepositEntity.setStatus(PolicyConstants.DEPOSITSTATUSNEW);
				policyDepositEntity.setZeroId(Boolean.FALSE);
				policyDepositEntity.setIsDeposit(Boolean.FALSE);
				policyDepositEntity.setModifiedBy(policyDeposit.getModifiedBy());
				policyDepositEntity.setModifiedOn(new Date());
				policyDepositEntity.setCreatedBy(policyDeposit.getCreatedBy());
				policyDepositEntity.setCreatedOn(new Date());
				policyDepositEntity.setIsActive(Boolean.TRUE);
				policyDepositTempRepository.save(policyDepositEntity);
			}
			RegularAdjustmentContributionDto dto = modelMapper.map(responseEntity,
					RegularAdjustmentContributionDto.class);
			dto.setAmountToBeAdjusted(responseEntity.getAmountToBeAdjusted());
			adjustmentContributionAllDto.setRegularContributionId(dto.getRegularContributionId());
			adjustmentContributionAllDto.setRegAdjustmentContribution(dto);
			adjustmentContributionAllDto.setRegAdjustmentContributDepositDtos(
					getDepositFromPolicyDeposit(dto.getRegularContributionId(), dto.getTempPolicyId()));
			responseDto.setResponseData(adjustmentContributionAllDto);
			responseDto.setIsCommencementdateOneYr(calculateIsOneYrPolicy(dto.getPolicyCommencementDate()));
			responseDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
			responseDto.setTransactionStatus(CommonConstants.SUCCESS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: save ", e);
			responseDto.setTransactionMessage(CommonConstants.ERROR);
			responseDto.setTransactionStatus(CommonConstants.FAIL);
		}
		logger.info("RegularadjustmentServiceImpl :: save :: Ends");
		return responseDto;
	}

	@Override
	public RegularAdjustmentContributionResponseDto notesave(RegularAdjustmentContributionNotesDto request) {
		RegularAdjustmentContributionResponseDto responseDto = new RegularAdjustmentContributionResponseDto();
		try {
			logger.info("RegularadjustmentServiceImpl :: notesave :: Start");
			RegularAdjustmentContributionNotesTempEntity saveRegAdjnotes = modelMapper.map(responseDto,
					RegularAdjustmentContributionNotesTempEntity.class);
			saveRegAdjnotes.setNotesId(null);
			saveRegAdjnotes.setRegularContributionId(request.getRegularContributionId());
			saveRegAdjnotes.setNoteContents(request.getNoteContents());
			saveRegAdjnotes.setCreatedBy(request.getCreatedBy());
			saveRegAdjnotes.setCreatedOn(DateUtils.sysDate());
			saveRegAdjnotes.setPolicyId(request.getPolicyId());
			RegularAdjustmentContributionNotesTempEntity responseEntity = adjNotesTempRepo.save(saveRegAdjnotes);
			RegularAdjustmentContributionNotesDto dto = modelMapper.map(responseEntity,
					RegularAdjustmentContributionNotesDto.class);
			responseDto.setResponseData(dto);
			responseDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
			responseDto.setTransactionStatus(CommonConstants.SUCCESS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: notesave ", e);
			responseDto.setTransactionMessage(CommonConstants.ERROR);
			responseDto.setTransactionStatus(CommonConstants.FAIL);
		}
		logger.info("RegularadjustmentServiceImpl :: notesave :: Ends");
		return responseDto;
	}

	@Override
	public RegularAdjustmentContributionResponseDto getNotesList(Long regularContributionId) {
		RegularAdjustmentContributionResponseDto responseDto = new RegularAdjustmentContributionResponseDto();
		List<RegularAdjustmentContributionNotesDto> dtos = new ArrayList<>();
		try {
			logger.info("RegularadjustmentServiceImpl :: getNotesList :: Start");
			List<RegularAdjustmentContributionNotesTempEntity> regAdjConTempEntity = adjNotesTempRepo
					.findByregularContributionId(regularContributionId);
			if (regAdjConTempEntity != null) {
				for (RegularAdjustmentContributionNotesTempEntity entity : regAdjConTempEntity) {
					RegularAdjustmentContributionNotesDto dto = modelMapper.map(entity,
							RegularAdjustmentContributionNotesDto.class);
					dtos.add(dto);
				}
				responseDto.setResponseData(dtos);
				responseDto.setTransactionMessage(CommonConstants.FETCH_MESSAGE);
				responseDto.setTransactionStatus(CommonConstants.SUCCESS);
			} else {
				responseDto.setTransactionMessage(CommonConstants.DENY);
				responseDto.setTransactionStatus(CommonConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: getNotesList ", e);
			responseDto.setTransactionMessage(CommonConstants.ERROR);
			responseDto.setTransactionStatus(CommonConstants.FAIL);
		}
		logger.info("RegularadjustmentServiceImpl :: getNotesList :: Ends");
		return responseDto;
	}

	@Override
	public RegularAdjustmentContributionResponseDto changeStatus(Long regularContributionId,
			String adjustmentContributionStatus, String modifiedBy) {
		RegularAdjustmentContributionResponseDto responseDto = new RegularAdjustmentContributionResponseDto();
		RegularAdjustmentContributionTempEntity regAdjConTempEntity = adjTempRepo
				.findByRegularContributionIdAndIsActiveTrue(regularContributionId);
		try {
			logger.info("RegularadjustmentServiceImpl :: changeStatus :: Start");
			if (regAdjConTempEntity != null) {
				regAdjConTempEntity.setModifiedBy(modifiedBy);
				regAdjConTempEntity.setModifiedOn(DateUtils.sysDate());
				regAdjConTempEntity.setRegularContributionStatus(adjustmentContributionStatus);
				RegularAdjustmentContributionTempEntity saveEntity = adjTempRepo.save(regAdjConTempEntity);
				RegularAdjustmentContributionDto dto = modelMapper.map(saveEntity,
						RegularAdjustmentContributionDto.class);
				responseDto.setResponseData(dto);
				responseDto.setTransactionStatus(CommonConstants.SUCCESS);
				responseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			} else {
				responseDto.setTransactionStatus(CommonConstants.ERROR);
				responseDto.setTransactionMessage(CommonConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: changeStatus ", e);
			responseDto.setTransactionStatus(CommonConstants.ERROR);
			responseDto.setTransactionMessage(CommonConstants.FAIL);
		}
		logger.info("RegularadjustmentServiceImpl :: changeStatus :: Ends");
		return responseDto;
	}

	@Override
	@Transactional
	public RegularAdjustmentContributionResponseDto approve(Long regularContributionId,
			String adjustmentContributionStatus, String modifiedBy, String variantType) {
		RegularAdjustmentContributionResponseDto responseDto = new RegularAdjustmentContributionResponseDto();
		try {
			logger.info("RegularadjustmentServiceImpl :: approve :: Start");
			CommonResponseDto financialYeartDetails = new CommonResponseDto();

			financialYeartDetails.setFinancialYear(DateUtils.getFinancialYrByDt(DateUtils.sysDate()));
			String fincialyearN = financialYeartDetails.getFinancialYear();
			RegularAdjustmentContributionTempEntity regAdjConTempEntity = adjTempRepo
					.findByRegularContributionIdAndIsActiveTrue(regularContributionId);
			RegularAdjustmentContributionEntity saveMain = null;
			List<PolicyFrequencyDetailsEntity> policyEntity = policyFrequencyRepo
					.findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyIdDesc(regAdjConTempEntity.getPolicyId(),
							CommonConstants.UNPAID);
			PolicyFrequencyDetailsEntity frequencyEntity = new PolicyFrequencyDetailsEntity();
			if (!policyEntity.isEmpty() && policyEntity.get(0) != null) {
				frequencyEntity = policyEntity.get(0);
			}
			
			List<RegularAdjustmentContributionEntity> regularAdjustmentContributionEntityList = regAdjustConRep.findAllByRegularContributionNumberAndIsActiveTrue(regAdjConTempEntity.getRegularContributionNumber());
			
			if(regularAdjustmentContributionEntityList.isEmpty()) {
			
			if (policyEntity.size() <= 1) {
				regAdjConTempEntity.setModifiedOn(DateUtils.sysDate());
				regAdjConTempEntity.setRegularContributionStatus(adjustmentContributionStatus);
				RegularAdjustmentContributionTempEntity saveEntity = adjTempRepo.save(regAdjConTempEntity);
				logger.info("RegularadjustmentServiceImpl :: approve :: convertTempToMaster :: Start");
				RegularAdjustmentContributionEntity regEntity = convertTempToMaster(saveEntity,
						adjustmentContributionStatus);
				saveMain = regAdjustConRep.save(regEntity);
				logger.info("RegularadjustmentServiceImpl :: approve :: convertTempToMaster ::Ends");
				
				logger.info("RegularadjustmentServiceImpl :: approve :: saveContribution :: Start");
				saveContribution(saveEntity, saveMain, fincialyearN, variantType);
				logger.info("RegularadjustmentServiceImpl :: approve :: saveContribution ::Ends");
				
				logger.info("RegularadjustmentServiceImpl :: approve :: getArdDateByPolicyId :: Start");
				Date priveousArd = policyMasterRepository.getArdDateByPolicyId(regAdjConTempEntity.getPolicyId());
				logger.info("RegularadjustmentServiceImpl :: approve :: getArdDateByPolicyId ::Ends");
				
				logger.info("RegularadjustmentServiceImpl :: approve :: calculateARDa :: Start");
				Date nextArd = policyCommonServiceImpl.calculateARDa(priveousArd);
				logger.info("RegularadjustmentServiceImpl :: approve :: calculateARDa ::Ends");
				
				frequencyEntity.setStatus(CommonConstants.PAID);
				policyFrequencyRepo.save(frequencyEntity);
				
				logger.info("RegularadjustmentServiceImpl :: approve :: setNewARD :: Start");
				setNewARD(regAdjConTempEntity.getPolicyId(), nextArd);
				logger.info("RegularadjustmentServiceImpl :: approve :: setNewARD ::Ends");
				
				PolicyFrequencyDetailsDto requestForNxt = new PolicyFrequencyDetailsDto();
				requestForNxt.setPolicyId(frequencyEntity.getPolicyId());
				requestForNxt.setPolicyCommencementDate(priveousArd);
				requestForNxt.setPolicyEndDate(nextArd);
				requestForNxt.setFrequency(frequencyEntity.getFrequency());
				
				logger.info("RegularadjustmentServiceImpl :: approve :: existingFrequency :: Start");
				existingFrequency(requestForNxt);
				logger.info("RegularadjustmentServiceImpl :: approve :: existingFrequency ::Ends");
			
			} else {
				frequencyEntity.setStatus(CommonConstants.PAID);
				policyFrequencyRepo.save(frequencyEntity);
				regAdjConTempEntity.setModifiedOn(DateUtils.sysDate());
				regAdjConTempEntity.setRegularContributionStatus(adjustmentContributionStatus);
				RegularAdjustmentContributionTempEntity saveEntity = adjTempRepo.save(regAdjConTempEntity);
				logger.info("RegularadjustmentServiceImpl :: approve :: convertTempToMaster :: Start");
				RegularAdjustmentContributionEntity regEntity = convertTempToMaster(saveEntity,
						adjustmentContributionStatus);
				saveMain = regAdjustConRep.save(regEntity);
				logger.info("RegularadjustmentServiceImpl :: approve :: convertTempToMaster ::Ends");
				
				logger.info("RegularadjustmentServiceImpl :: approve :: saveContribution :: Start");
				saveContribution(saveEntity, saveMain, fincialyearN, variantType);
				logger.info("RegularadjustmentServiceImpl :: approve :: saveContribution ::Ends");
			}
			RegularAdjustmentContributionDto dto = modelMapper.map(saveMain, RegularAdjustmentContributionDto.class);
			responseDto.setResponseData(dto);
			responseDto.setPolicyId(dto.getPolicyId());
			responseDto.setTransactionStatus(CommonConstants.SUCCESS);
			responseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			}
			else {
				responseDto.setTransactionStatus(CommonConstants.FAIL);
				responseDto.setTransactionMessage(CommonConstants.ADJ_APPROVE);
				return responseDto;
			}
		} catch (IllegalArgumentException | ApplicationException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: approve ", e);
			responseDto.setTransactionStatus(CommonConstants.ERROR);
			responseDto.setTransactionMessage(CommonConstants.FAIL);
		} catch (Exception e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: approve ", e);
			e.printStackTrace();
		}
		logger.info("RegularadjustmentServiceImpl :: approve :: Ends");
		return responseDto;
	}
	
	public RegularAdjustmentContributionResponseDto existingFrequency(PolicyFrequencyDetailsDto request)
			throws Exception {
		RegularAdjustmentContributionResponseDto responseDto = new RegularAdjustmentContributionResponseDto();
		try {
			logger.info("RegularadjustmentServiceImpl :: existingFrequency :: Start");

			List<String> frequencyDates = new ArrayList<>();
			int frequency = NumericUtils.convertStringToInteger(request.getFrequency());
			int totalmonths = DateUtils.getTotalInBetweenMonths(request.getPolicyCommencementDate(),
					request.getPolicyEndDate());
			switch (frequency) {
			case 1:
				frequencyDates = DateUtils.datemonthDifference(totalmonths, request.getPolicyCommencementDate(),
						request.getPolicyEndDate());
				break;
			case 2:
				frequencyDates = DateUtils.dateQuarterDifference(totalmonths, request.getPolicyCommencementDate(),
						request.getPolicyEndDate());
				break;
			case 3:
				frequencyDates = DateUtils.convertDateTimeToStringWithPeriod(totalmonths,
						request.getPolicyCommencementDate(), request.getPolicyEndDate());
				break;
			case 4:
				frequencyDates = DateUtils.datehalfDifference(totalmonths, request.getPolicyCommencementDate(),
						request.getPolicyEndDate());
				break;
			default:
				logger.info("Switch exit ");
			}
			if (frequencyDates != null) {
				for (int i = 0; i <= frequencyDates.size() - 1; i++) {
					PolicyFrequencyDetailsEntity entity = new PolicyFrequencyDetailsEntity();
					entity.setPolicyId(request.getPolicyId());
					entity.setFrequencyDates(DateUtils.convertStringToDate(frequencyDates.get(i)));
					entity.setFrequency(request.getFrequency());
					entity.setStatus(CommonConstants.UNPAID);
					entity.setPolicyCommencementDate(request.getPolicyCommencementDate());
					entity.setPolicyEndDate(request.getPolicyEndDate());

					policyFrequencyRepo.save(entity);
				}
				responseDto.setTransactionMessage(PolicyConstants.SUCCESS);
				responseDto.setTransactionStatus(PolicyConstants.STATUS);
			} else {
				responseDto.setTransactionMessage(PolicyConstants.DENY);
				responseDto.setTransactionStatus(PolicyConstants.ERRORSTATUS);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: existingFrequency ", e);
			responseDto.setTransactionMessage(PolicyConstants.EXCEPTION);
			responseDto.setTransactionStatus(PolicyConstants.ERRORSTATUS);
		}
		logger.info("RegularadjustmentServiceImpl :: existingFrequency :: Ends");
		return responseDto;
	}
	
	private void setNewARD(Long policyId, Date nextArd) {
		logger.info("setNewARD::Start");
		PolicyMasterEntity findByPolicyId = policyMasterRepository.findByPolicyIdAndIsActiveTrue(policyId);
		findByPolicyId.setArd(nextArd);
		policyMasterRepository.save(findByPolicyId);
		logger.info("setNewARD::End");
	}	
	
	@Transactional
	public void saveContribution(RegularAdjustmentContributionTempEntity tempAdj,
			RegularAdjustmentContributionEntity masterAdj, String username, String variantType)
			throws ApplicationException {
		logger.info("saveContribution::Start");
		PolicyMasterEntity policyMasterEntity = commonService.findPolicyDetails(null, masterAdj.getPolicyId());
		String financialYr = DateUtils.getFinancialYrByDt(DateUtils.sysDate());
		PolicyContributionTempEntity policyContributionTempEntity = policyContributionTempRepository
				.findTopByPolicyIdAndRegularContributionIdAndIsActiveTrueOrderByContributionIdDesc(
						tempAdj.getTempPolicyId(), tempAdj.getRegularContributionId());

		PolicyContributionEntity policyContributionEntity = setPolicyContributionEntity(policyContributionTempEntity,
				masterAdj, username, variantType);
		policyContributionRepository.save(policyContributionEntity);

		Long policyContributionId = policyContributionEntity.getContributionId();

		Set<MemberContributionEntity> setMemberContributionEntity = setMemberContributionEntity(policyMasterEntity,
				tempAdj, masterAdj, policyContributionEntity, variantType);
		setPolicyContributionBySumOfMemberContribution(policyMasterEntity, masterAdj, username,
				policyContributionEntity, setMemberContributionEntity);

		policyContributionEntity.setPolicyContribution(setMemberContributionEntity);
		policyContributionRepository.save(policyContributionEntity);

		printContribution(policyContributionEntity, masterAdj);
		Set<PolicyContributionEntity> policySet = new HashSet<>();
		policySet.add(policyContributionEntity);

		logger.info("savePolicyContributionSummary::Start");
		adjustmentContributionService.savePolicyContributionSummary(policySet, username, variantType);
		logger.info("savePolicyContributionSummary::End");

		logger.info("saveMemberContributionSummary::Start");
		adjustmentContributionService.saveMemberContributionSummary(setMemberContributionEntity, username);
		logger.info("saveMemberContributionSummary::End");

		logger.info("savePolicyDeposit::Start");
		savePolicyDeposit(policyMasterEntity, tempAdj, masterAdj, financialYr);
		logger.info("savePolicyDeposit::End");

		logger.info("saveZeroAccount::Start");
		saveZeroAcccount(policyMasterEntity, tempAdj, masterAdj, financialYr, policyContributionId);
		logger.info("saveZeroAccount::End");
	}

	private void saveZeroAcccount(PolicyMasterEntity policyMasterEntity,
			RegularAdjustmentContributionTempEntity tempAdj, RegularAdjustmentContributionEntity masterAdj,
			String financialYr, Long policyContributionId) {
		logger.info("saveZeroAccount::Start");

//		getTempZeroAccount
		ZeroAccountTempEntity zeroAccountTempEntity = zeroAccountTempRepository
				.findByPolicyIdAndIsActiveTrue(tempAdj.getTempPolicyId());
		if (zeroAccountTempEntity != null) {
			ZeroAccountEntity zeroAccountEntity = zeroAccountRepository
					.findByPolicyIdAndIsActiveTrue(tempAdj.getPolicyId());
			if (zeroAccountEntity != null) {
				// need to add old amount with new amount
				zeroAccountEntity.setZeroAccId(zeroAccountEntity.getZeroAccId());
				zeroAccountEntity.setZeroIdAmount(zeroAccountTempEntity.getZeroIdAmount());
				zeroAccountEntity.setPolicyId(zeroAccountEntity.getPolicyId());
				zeroAccountEntity.setModifiedBy(zeroAccountTempEntity.getModifiedBy());
				zeroAccountEntity.setModifiedOn(new Date());
				zeroAccountEntity.setIsActive(Boolean.TRUE);

				zeroAccountRepository.save(zeroAccountEntity);
			} else {
				zeroAccountEntity = new ZeroAccountEntity();
				// need to add old amount with new amount
				zeroAccountEntity.setZeroAccId(null);
				zeroAccountEntity.setZeroIdAmount(zeroAccountTempEntity.getZeroIdAmount());
				zeroAccountEntity.setPolicyId(tempAdj.getPolicyId());
				zeroAccountEntity.setModifiedBy(masterAdj.getModifiedBy());
				zeroAccountEntity.setModifiedOn(new Date());
				zeroAccountEntity.setCreatedBy(masterAdj.getModifiedBy());
				zeroAccountEntity.setCreatedOn(new Date());
				zeroAccountEntity.setIsActive(Boolean.TRUE);

				zeroAccountRepository.save(zeroAccountEntity);

				MemberMasterEntity memberMasterEntity = memberMasterRepository
						.findByPolicyIdAndLicIdAndMemberStatus(tempAdj.getPolicyId(), "0", CommonConstants.ACTIVE);
				if (memberMasterEntity == null) {
					MemberMasterTempEntity memberMasterTempEntity = memberMasterTempRepository
							.findByPolicyIdAndLicIdAndMemberStatus(tempAdj.getTempPolicyId(), "0",
									CommonConstants.ACTIVE);
					if (memberMasterTempEntity != null) {
						MemberMasterEntity masterEntity = new MemberMasterEntity();
						BeanUtils.copyProperties(memberMasterTempEntity, masterEntity);
						masterEntity.setMemberId(null);
						masterEntity.setTempMemberId(memberMasterTempEntity.getMemberId());
						masterEntity.setPolicyId(tempAdj.getPolicyId());
						masterEntity.setCreatedBy(zeroAccountTempEntity.getModifiedBy());
						masterEntity.setCreatedOn(new Date());
						masterEntity.setModifiedBy(zeroAccountTempEntity.getModifiedBy());
						masterEntity.setModifiedOn(new Date());
						memberMasterRepository.save(masterEntity);
					}
				}
			}

		}

//		getZeroAccount
		List<ZeroAccountEntriesTempEntity> zeroAccountEntriesTempEntityList = zeroAccountEntriesTempRepository
//				.findByPolicyIdAndIsActiveTrue(tempAdj.getTempPolicyId());
				.findByPolicyIdAndIsMovedFalseAndIsActiveTrue(tempAdj.getTempPolicyId());
		if (zeroAccountEntriesTempEntityList != null) {
			for (ZeroAccountEntriesTempEntity zeroAccountEntriesTempEntity : zeroAccountEntriesTempEntityList) {

				ZeroAccountEntriesEntity zeroAccountEntriesEntity = new ZeroAccountEntriesEntity();

				zeroAccountEntriesEntity.setZeroAccEntId(null);
				zeroAccountEntriesEntity.setPolicyId(tempAdj.getPolicyId());
				zeroAccountEntriesEntity.setPolicyConId(policyContributionId);
				zeroAccountEntriesEntity.setMemberConId(null);
				zeroAccountEntriesEntity.setZeroIdAmount(zeroAccountEntriesTempEntity.getZeroIdAmount());
				zeroAccountEntriesEntity.setTransactionType(zeroAccountEntriesTempEntity.getTransactionType());
				zeroAccountEntriesEntity.setTransactionDate(zeroAccountEntriesTempEntity.getTransactionDate());
				zeroAccountEntriesEntity.setIsActive(Boolean.TRUE);
				zeroAccountEntriesEntity.setCreatedBy(masterAdj.getCreatedBy());
				zeroAccountEntriesEntity.setCreatedOn(masterAdj.getCreatedOn());
				zeroAccountEntriesEntity.setModifiedBy(masterAdj.getModifiedBy());
				zeroAccountEntriesEntity.setModifiedOn(masterAdj.getModifiedOn());
				zeroAccountEntriesEntity.setTempPolicyId(tempAdj.getTempPolicyId());
				zeroAccountEntriesRepository.save(zeroAccountEntriesEntity);

				zeroAccountEntriesTempEntity.setIsMoved(Boolean.TRUE);
				zeroAccountEntriesTempEntity.setMasterPolicyId(tempAdj.getPolicyId());
				zeroAccountEntriesTempRepository.save(zeroAccountEntriesTempEntity);
			}
		}
		logger.info("saveZeroAccount::Ends");
	}	

	public void savePolicyDeposit(PolicyMasterEntity policyMasterEntity,
			RegularAdjustmentContributionTempEntity tempAdj, RegularAdjustmentContributionEntity masterAdj,
			String role) {
		logger.info("{}::", policyMasterEntity.getPolicyId());
		List<PolicyDepositTempEntity> policyDepositTempEntityList = policyDepositTempRepository
				.findByPolicyIdAndRegularContributionIdAndIsActiveTrue(tempAdj.getTempPolicyId(),
						tempAdj.getRegularContributionId());
		for (PolicyDepositTempEntity policyDepositTempEntity : policyDepositTempEntityList) {
			PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();
			policyDepositEntity.setDepositId(null);
			policyDepositEntity.setPolicyId(policyDepositTempEntity.getMasterPolicyId());
			policyDepositEntity.setTempPolicyId(policyDepositTempEntity.getPolicyId());
			policyDepositEntity.setContributionType(policyDepositTempEntity.getContributionType());
			policyDepositEntity.setRegularContributionId(masterAdj.getRegularContributionId());
			policyDepositEntity.setCollectionNo(policyDepositTempEntity.getCollectionNo());
			policyDepositEntity.setCollectionDate(policyDepositTempEntity.getCollectionDate());
			policyDepositEntity.setCollectionStatus(policyDepositTempEntity.getCollectionStatus());
			policyDepositEntity.setChallanNo(policyDepositTempEntity.getChallanNo());
			policyDepositEntity.setDepositAmount(policyDepositTempEntity.getDepositAmount());
			policyDepositEntity.setAdjustmentAmount(policyDepositTempEntity.getAdjustmentAmount());
			policyDepositEntity.setAvailableAmount(policyDepositTempEntity.getAvailableAmount());
			policyDepositEntity.setTransactionMode(policyDepositTempEntity.getTransactionMode());
			policyDepositEntity.setChequeRealisationDate(policyDepositTempEntity.getChequeRealisationDate());
			policyDepositEntity.setRemark(policyDepositTempEntity.getRemark());
			policyDepositEntity.setStatus(policyDepositTempEntity.getStatus());
			policyDepositEntity.setZeroId(policyDepositTempEntity.getZeroId());
			policyDepositEntity.setIsDeposit(policyDepositTempEntity.getIsDeposit());
			policyDepositEntity.setModifiedBy(role);
			policyDepositEntity.setModifiedOn(DateUtils.sysDate());
			policyDepositEntity.setCreatedBy(role);
			policyDepositEntity.setCreatedOn(DateUtils.sysDate());
			policyDepositEntity.setIsActive(Boolean.TRUE);
			policyDepositRepository.save(policyDepositEntity);
		}
	}	
	
	public void printContribution(PolicyContributionEntity policyContributionEntity,
			RegularAdjustmentContributionEntity masterAdj) {
		logger.info("*********AdjustmentContributionId::{}, MasterPolicyId::{}*********",
				masterAdj.getRegularContributionId(), masterAdj.getPolicyId());
		logger.info(
				"************************************PolicyContributionEntity**********************************************");
		logger.info("PolicyContributionId::{}, PolicyId::{}, FinancialYr::{}, Quarter::{}, txnEntryStatus::{}",
				policyContributionEntity.getContributionId(), policyContributionEntity.getPolicyId(),
				policyContributionEntity.getFinancialYear(), policyContributionEntity.getQuarter(),
				policyContributionEntity.getTxnEntryStatus());
		Set<MemberContributionEntity> memberSet = policyContributionEntity.getPolicyContribution();
		if (CommonUtils.isNonEmptyArray(memberSet)) {
			memberSet.forEach(mem -> {
				logger.info(
						"************************************MemberContributionEntity**********************************************");
				logger.info(
						"PolicyConId::{}, MemberConId::{}, PolicyId::{}, FinancialYr::{}, Quarter::{}, txnEntryStatus::{}",
						mem.getPolicyConId(), mem.getMemberConId(), mem.getPolicyId(), mem.getFinancialYear(),
						mem.getQuarter(), mem.getTxnEntryStatus());
			});
		}
		logger.info(
				"***********************************PolicyContributionEntity***********************************************");
	}	
	
	public PolicyContributionEntity setPolicyContributionBySumOfMemberContribution(
			PolicyMasterEntity policyMasterEntity, RegularAdjustmentContributionEntity masterAdj, String username,
			PolicyContributionEntity policyContributionEntity, Set<MemberContributionEntity> memberContributions) {

		double employee = 0.0;
		double employer = 0.0;
		double voluntary = 0.0;
		BigDecimal totalContribution = BigDecimal.ZERO;

		if (policyMasterEntity.getPolicyType().equalsIgnoreCase(CommonConstants.DC)) {
			employee = memberContributions.stream().filter(p -> p.getEmployeeContribution() != null)
					.mapToDouble(op -> op.getEmployeeContribution().doubleValue()).sum();
			employer = memberContributions.stream().filter(p -> p.getEmployerContribution() != null)
					.mapToDouble(op -> op.getEmployerContribution().doubleValue()).sum();
			voluntary = memberContributions.stream().filter(p -> p.getVoluntaryContribution() != null)
					.mapToDouble(op -> op.getVoluntaryContribution().doubleValue()).sum();
			totalContribution = NumericUtils.doubleToBigDecimal((employee + employer + voluntary));
			policyContributionEntity.setPolicyContribution(memberContributions);
			policyContributionEntity.setEmployeeContribution(NumericUtils.doubleToBigDecimal(employee));
			policyContributionEntity.setEmployerContribution(NumericUtils.doubleToBigDecimal(employer));
			policyContributionEntity.setVoluntaryContribution(NumericUtils.doubleToBigDecimal(voluntary));
			policyContributionEntity.setTotalContribution(totalContribution);
		}
		return policyContributionEntity;
	}	
	
	public Set<MemberContributionEntity> setMemberContributionEntity(PolicyMasterEntity policyMasterEntity,
			RegularAdjustmentContributionTempEntity tempAdj, RegularAdjustmentContributionEntity masterAdj,
			PolicyContributionEntity policyContributionEntity, String variantType) throws ApplicationException {
		if (policyMasterEntity.getPolicyType().equalsIgnoreCase(CommonConstants.DC)) {
			String financialYr = DateUtils.getFinancialYrByDt(DateUtils.sysDate());
			Set<MemberContributionTempEntity> fromMc = memberContributionTempRepository
					.findByPolicyIdAndRegularContributionIdAndIsActiveTrue(tempAdj.getTempPolicyId(),
							tempAdj.getRegularContributionId());
			if (CommonUtils.isNonEmptyArray(fromMc)) {
				Set<MemberContributionEntity> memberContributionMasterList = new HashSet<>();
				fromMc.stream().forEach(from -> {
					MemberContributionEntity to = new MemberContributionEntity();
					BeanUtils.copyProperties(from, to);
					to.setRegularContributionId(masterAdj.getRegularContributionId());
					to.setClosingBalance(null);
					to.setOpeningBalance(null);
					to.setFinancialYear(financialYr);
					to.setMemberConId(null);
					to.setPolicyId(from.getMasterPolicyId());
					to.setMemberId(from.getMasterMemberId());
					to.setModifiedOn(DateUtils.sysDate());
					if (variantType.equalsIgnoreCase("V2")) {
						to.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
					} else {
						to.setQuarter("Q0");
					}
					to.setTempMemberId(from.getMemberId());
					to.setTempPolicyId(from.getPolicyId());
					to.setTxnEntryStatus(false);
					to.setPolicyConId(policyContributionEntity.getContributionId());
					memberContributionMasterList.add(to);
				});
				return memberContributionMasterList;
			}
			throw new ApplicationException(
					"No member contribution found for the given master policy id:" + policyMasterEntity.getPolicyId());
		}
		return Collections.emptySet();
	}	
	
	public PolicyContributionEntity setPolicyContributionEntity(
			PolicyContributionTempEntity policyContributionTempEntity, RegularAdjustmentContributionEntity masterAdj,
			String username, String variantType) {
		PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();
		BeanUtils.copyProperties(policyContributionTempEntity, policyContributionEntity);
		policyContributionEntity.setPolicyId(policyContributionTempEntity.getMasterPolicyId());
		policyContributionEntity.setContributionId(null);
		policyContributionEntity.setRegularContributionId(masterAdj.getRegularContributionId());
		policyContributionEntity.setFinancialYear(DateUtils.getFinancialYrByDt(DateUtils.sysDate()));
		policyContributionEntity.setTxnEntryStatus(false);
		policyContributionEntity.setZeroAccountEntries(null);
		if (variantType.equalsIgnoreCase("V2")) {
			policyContributionEntity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
		} else {
			policyContributionEntity.setQuarter("Q0");
		}
		policyContributionEntity.setModifiedBy(username);
		return policyContributionEntity;
	}	
	
	public RegularAdjustmentContributionEntity convertTempToMaster(RegularAdjustmentContributionTempEntity tempEntity,
			String status) {
		logger.info("RegularadjustmentServiceImpl :: convertTempToMaster :: Start");

		RegularAdjustmentContributionEntity entity = new RegularAdjustmentContributionEntity();
		entity.setRegularContributionId(null);
		entity.setRegularContributionNumber(tempEntity.getRegularContributionNumber());
		entity.setRegularContributionStatus(status);
		entity.setWorkFlowStatus(tempEntity.getWorkFlowStatus());
		entity.setUnitCode(tempEntity.getUnitCode());
		entity.setModifiedBy(tempEntity.getModifiedBy());
		entity.setModifiedOn(tempEntity.getModifiedOn());
		entity.setCreatedBy(tempEntity.getCreatedBy());
		entity.setCreatedOn(tempEntity.getCreatedOn());
		entity.setIsActive(tempEntity.getIsActive());
		entity.setIsDeposit(tempEntity.getIsDeposit());
		entity.setRejectionReasonCode(tempEntity.getRejectionReasonCode());
		entity.setRejectionRemarks(tempEntity.getRejectionRemarks());
		entity.setPolicyId(tempEntity.getPolicyId());
		entity.setTempPolicyId(tempEntity.getTempPolicyId());
		entity.setPolicyNumber(tempEntity.getPolicyNumber());
		entity.setPolicyStatus(tempEntity.getPolicyStatus());
		entity.setPolicyType(tempEntity.getPolicyType());
		entity.setPolicyCommencementDate(tempEntity.getPolicyCommencementDate());
		entity.setCustomerId(tempEntity.getCustomerId());
		entity.setCustomerCode(tempEntity.getCustomerCode());
		entity.setCustomerName(tempEntity.getCustomerName());
		entity.setMphName(tempEntity.getMphName());
		entity.setMphCode(tempEntity.getMphCode());
		entity.setProposalNumber(tempEntity.getProposalNumber());
		entity.setProduct(tempEntity.getProduct());
		entity.setVariant(tempEntity.getVariant());
		entity.setTotalContribution(tempEntity.getTotalContribution());
		entity.setTotalDeposit(tempEntity.getTotalDeposit());
		entity.setAmountToBeAdjusted(tempEntity.getAmountToBeAdjusted());
		entity.setFirstPremium(tempEntity.getFirstPremium());
		entity.setSinglePremiumFirstYr(tempEntity.getSinglePremiumFirstYr());
		entity.setRenewalPremium(tempEntity.getRenewalPremium());
		entity.setSubsequentSinglePremium(tempEntity.getSubsequentSinglePremium());
		entity.setIsCommencementdateOneYr(tempEntity.getIsCommencementdateOneYr());
		entity.setAdjustmentForDate(tempEntity.getAdjustmentForDate());
		entity.setAdjustmentDueDate(tempEntity.getAdjustmentDueDate());	
		entity.setEffectiveDate(tempEntity.getEffectiveDate());
		Set<RegularAdjustmentContributionNotesEntity> noteList = new HashSet<>();
		for (RegularAdjustmentContributionNotesTempEntity noteTemp : tempEntity.getRegularContributionNotes()) {
			RegularAdjustmentContributionNotesEntity note = new RegularAdjustmentContributionNotesEntity();
			note.setNotesId(null);
			note.setRegularContributionId(entity.getRegularContributionId());
			note.setNoteContents(noteTemp.getNoteContents());
			note.setCreatedOn(noteTemp.getCreatedOn());
			note.setCreatedBy(noteTemp.getCreatedBy());
			note.setPolicyId(noteTemp.getPolicyId());
			noteList.add(note);
		}
		entity.setRegularContributionNotes(noteList);
		logger.info("RegularadjustmentServiceImpl :: convertTempToMaster :: Ends");
		return entity;
	}	
	
	@Override
	public RegularAdjustmentContributionResponseDto getInprogressLoad(RegularAdjustmentSearchDto request) {
		List<RegularAdjustmentContributionTempEntity> adjustmentEntities = new ArrayList<>();
		RegularAdjustmentContributionResponseDto commonDto = new RegularAdjustmentContributionResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("RegularadjustmentServiceImpl :: getInprogressLoad :: Start");
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<RegularAdjustmentContributionTempEntity> createQuery = criteriaBuilder
					.createQuery(RegularAdjustmentContributionTempEntity.class);
			Root<RegularAdjustmentContributionTempEntity> root = createQuery
					.from(RegularAdjustmentContributionTempEntity.class);
			if (StringUtils.isNotBlank(request.getRole()) && Objects.equals(request.getRole(), PolicyConstants.MAKER)) {
				predicates.add(root.get("regularContributionStatus")
						.in(Arrays.asList(PolicyConstants.DRAFT_NO, PolicyConstants.MAKER_NO)));
			}
			if (StringUtils.isNotBlank(request.getRole())
					&& Objects.equals(request.getRole(), PolicyConstants.CHECKER)) {
				predicates.add(root.get("regularContributionStatus").in(Arrays.asList(PolicyConstants.CHECKER_NO)));
			}
			if (StringUtils.isNotBlank(request.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyNumber")),
						request.getPolicyNumber().toLowerCase()));
			}
			if (StringUtils.isNotBlank(request.getMphCode())) {
				predicates.add(criteriaBuilder.equal(root.get("mphCode"), request.getMphCode()));
			}
			if (StringUtils.isNotBlank(request.getMphName())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("mphName")),
						request.getMphName().toLowerCase()));
			}
			if (StringUtils.isNotBlank(request.getFrom()) && StringUtils.isNotBlank(request.getTo())) {
				Date fromDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(request.getFrom());
				Date toDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(request.getTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get("createdOn"), fromDate, toDate));
			}
			if (StringUtils.isNotBlank(request.getProduct())) {
				predicates.add(criteriaBuilder.equal(root.get("product"), request.getProduct()));
			}
			if (StringUtils.isNotBlank(request.getLineOfBusiness())) {
				predicates.add(criteriaBuilder.equal(root.get("lineOfBusiness"), request.getLineOfBusiness()));
			}
			if (StringUtils.isNotBlank(request.getVariant())) {
				predicates.add(criteriaBuilder.equal(root.get("variant"), request.getVariant()));
			}
			if (StringUtils.isNotBlank(request.getPolicyStatus())) {
				predicates.add(criteriaBuilder.equal(root.get("policyStatus"), request.getPolicyStatus()));
			}
			if (StringUtils.isNotBlank(request.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get("unitCode"), request.getUnitCode()));
			}
			createQuery.orderBy(criteriaBuilder.desc(root.get("modifiedOn")));
			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			adjustmentEntities = entityManager.createQuery(createQuery).getResultList();
			List<RegularAdjustmentContributionDto> regAdjConDto = mapList(adjustmentEntities,
					RegularAdjustmentContributionDto.class);
			commonDto.setResponseData(regAdjConDto);
			commonDto.setTransactionMessage(CommonConstants.FETCH);
			commonDto.setTransactionStatus(CommonConstants.STATUS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: getInprogressLoad ", e);
			commonDto.setTransactionStatus(CommonConstants.FETCH);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("RegularadjustmentServiceImpl :: getInprogressLoad :: Ends");
		return commonDto;
	}

	@Override
	public RegularAdjustmentContributionResponseDto getInprogressDetails(Long regularContributionId) {
		RegularAdjustmentContributionResponseDto responseDto = new RegularAdjustmentContributionResponseDto();
		RegularAdjustmentContributionAllDto adjustmentContributionAllDto = new RegularAdjustmentContributionAllDto();
		List<RegularAdjustmentContributionNotesDto> regularAdjustmentContributionNotesDtoList = new ArrayList<>();
		try {
			logger.info("RegularadjustmentServiceImpl :: getInprogressDetails :: Start");
			Optional<RegularAdjustmentContributionTempEntity> regAdjConTempEntites = adjTempRepo
					.findByRegularContributionId(regularContributionId);
			if (regAdjConTempEntites.isPresent()) {
				RegularAdjustmentContributionTempEntity regAdjConTempEntity = regAdjConTempEntites.get();
				
				String lastPremiumDueDate = policyContributionRepository.findPolicyId(regAdjConTempEntity.getPolicyId());
				if (regAdjConTempEntity == null) {
					responseDto.setTransactionMessage(CommonConstants.INVALID_REQUEST);
					responseDto.setTransactionStatus(CommonConstants.FAIL);
					return responseDto;
				}
				List<RegularAdjustmentContributionNotesTempEntity> noteDetailsLit = adjNotesTempRepo
						.findByregularContributionId(regularContributionId);
				if (!noteDetailsLit.isEmpty()) {
					regularAdjustmentContributionNotesDtoList = mapList(noteDetailsLit,
							RegularAdjustmentContributionNotesDto.class);
				}

				logger.info("RegularadjustmentServiceImpl :: getInprogressDetails :: convertEntitytoDto :: Start");
				RegularAdjustmentContributionDto regAdjDto = convertEntitytoDto(regAdjConTempEntity);
				regAdjDto.setLastPremiumDueDate(lastPremiumDueDate);
				logger.info("RegularadjustmentServiceImpl :: getInprogressDetails :: convertEntitytoDto :: Ends");

				adjustmentContributionAllDto.setRegAdjustmentContribution(regAdjDto);
				adjustmentContributionAllDto.setRegularContributionId(adjustmentContributionAllDto.getRegularContributionId());

				logger.info("RegularadjustmentServiceImpl :: getInprogressDetails :: getDepositFromPolicyDeposit :: Start");
				adjustmentContributionAllDto.setRegAdjustmentContributDepositDtos(getDepositFromPolicyDeposit(regAdjDto.getRegularContributionId(), regAdjConTempEntity.getTempPolicyId()));
				logger.info("RegularadjustmentServiceImpl :: getInprogressDetails :: getDepositFromPolicyDeposit :: Ends");

				logger.info("RegularadjustmentServiceImpl :: getInprogressDetails :: getAdjustementFromPolicyDeposit :: Start");
				adjustmentContributionAllDto.setRegAdjustmentContributionDepositAdjustmentDtos(getAdjustementFromPolicyDeposit(regAdjDto.getRegularContributionId(),regAdjConTempEntity.getTempPolicyId()));
				logger.info("RegularadjustmentServiceImpl :: getInprogressDetails :: getAdjustementFromPolicyDeposit :: Ends");

				adjustmentContributionAllDto.setRegAdjustmentNotesDtos(regularAdjustmentContributionNotesDtoList);

				logger.info("RegularadjustmentServiceImpl :: getInprogressDetails ::  getPolicyBankList :: Start");
				adjustmentContributionAllDto.setBank(getPolicyBankList(regAdjDto.getPolicyId()));
				logger.info("RegularadjustmentServiceImpl :: getInprogressDetails :: getPolicyBankList :: Ends");
				responseDto.setResponseData(adjustmentContributionAllDto);
				responseDto.setTransactionMessage(CommonConstants.FETCH);
				responseDto.setTransactionStatus(CommonConstants.SUCCESS);
			}
		} catch (Exception e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: getInprogressDetails ", e);
			responseDto.setTransactionMessage(e.getMessage());
			responseDto.setTransactionStatus(CommonConstants.FAIL);
		}
		logger.info("RegularadjustmentServiceImpl :: getInprogressDetails :: Ends");
		return responseDto;
	}

	public RegularAdjustmentContributionDto convertEntitytoDto(RegularAdjustmentContributionTempEntity regAdjEntity) {
		logger.info("RegularadjustmentServiceImpl :: convertEntitytoDto :: Start");

		RegularAdjustmentContributionDto dto = new RegularAdjustmentContributionDto();
		dto.setRegularContributionId(regAdjEntity.getRegularContributionId());
		dto.setRegularContributionNumber(regAdjEntity.getRegularContributionNumber());
		dto.setRegularContributionStatus(regAdjEntity.getRegularContributionStatus());
		dto.setWorkFlowStatus(regAdjEntity.getWorkFlowStatus());
		dto.setUnitCode(regAdjEntity.getUnitCode());
		dto.setModifiedBy(regAdjEntity.getModifiedBy());
		dto.setModifiedOn(regAdjEntity.getModifiedOn());
		dto.setCreatedBy(regAdjEntity.getCreatedBy());
		dto.setCreatedOn(regAdjEntity.getCreatedOn());
		dto.setIsActive(regAdjEntity.getIsActive());
		dto.setIsDeposit(regAdjEntity.getIsDeposit());
		dto.setRejectionReasonCode(regAdjEntity.getRejectionReasonCode());
		dto.setRejectionRemarks(regAdjEntity.getRejectionRemarks());
		dto.setPolicyId(regAdjEntity.getPolicyId());
		dto.setTempPolicyId(regAdjEntity.getTempPolicyId());
		dto.setPolicyNumber(regAdjEntity.getPolicyNumber());
		dto.setPolicyStatus(regAdjEntity.getPolicyStatus());
		dto.setPolicyType(regAdjEntity.getPolicyType());
		dto.setPolicyCommencementDate(regAdjEntity.getPolicyCommencementDate());
		dto.setCustomerId(regAdjEntity.getCustomerId());
		dto.setCustomerCode(regAdjEntity.getCustomerCode());
		dto.setMphName(regAdjEntity.getMphName());
		dto.setMphCode(regAdjEntity.getMphCode());
		dto.setProposalNumber(regAdjEntity.getProposalNumber());
		dto.setProduct(regAdjEntity.getProduct());
		dto.setVariant(regAdjEntity.getVariant());
		dto.setTotalContribution(regAdjEntity.getTotalContribution());
		dto.setTotalDeposit(regAdjEntity.getTotalDeposit());
		dto.setAmountToBeAdjusted(regAdjEntity.getAmountToBeAdjusted());
		dto.setFirstPremium(regAdjEntity.getFirstPremium());
		dto.setSinglePremiumFirstYr(regAdjEntity.getSinglePremiumFirstYr());
		dto.setRenewalPremium(regAdjEntity.getRenewalPremium());
		dto.setSubsequentSinglePremium(regAdjEntity.getSubsequentSinglePremium());
		dto.setIsCommencementdateOneYr(regAdjEntity.getIsCommencementdateOneYr());
		dto.setAdjustmentForDate(regAdjEntity.getAdjustmentDueDate());
		dto.setAdjustmentDueDate(regAdjEntity.getAdjustmentDueDate());
		dto.setEffectiveDate(regAdjEntity.getEffectiveDate());	
		// RegAdjCon Notes
		Set<RegularAdjustmentContributionNotesDto> noteList = new HashSet<>();
		for (RegularAdjustmentContributionNotesTempEntity note : regAdjEntity.getRegularContributionNotes()) {
			RegularAdjustmentContributionNotesDto noteDto = new RegularAdjustmentContributionNotesDto();
			noteDto.setNotesId(note.getNotesId());
			noteDto.setRegularContributionId(note.getRegularContributionId());
			noteDto.setNoteContents(note.getNoteContents());
			noteDto.setCreatedOn(note.getCreatedOn());
			noteDto.setCreatedBy(note.getCreatedBy());
			noteDto.setPolicyId(note.getPolicyId());
			noteList.add(noteDto);
		}
		dto.setRegAdjustmentContributionNotes(noteList);
		logger.info("RegularadjustmentServiceImpl :: convertEntitytoDto :: Ends");
		return dto;
	}

	@Override
	public RegularAdjustmentContributionResponseDto getExisitngLoad(RegularAdjustmentSearchDto request) {
		List<RegularAdjustmentContributionEntity> adjustmentEntities = new ArrayList<>();
		RegularAdjustmentContributionResponseDto commonDto = new RegularAdjustmentContributionResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("RegularadjustmentServiceImpl :: getExisitngLoad :: Start");
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<RegularAdjustmentContributionEntity> createQuery = criteriaBuilder
					.createQuery(RegularAdjustmentContributionEntity.class);
			Root<RegularAdjustmentContributionEntity> root = createQuery
					.from(RegularAdjustmentContributionEntity.class);
			if (StringUtils.isNotBlank(request.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyNumber")),
						request.getPolicyNumber().toLowerCase()));
			}
			if (StringUtils.isNotBlank(request.getMphCode())) {
				predicates.add(criteriaBuilder.equal(root.get("mphCode"), request.getMphCode()));
			}
			if (StringUtils.isNotBlank(request.getMphName())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("mphName")),
						request.getMphName().toLowerCase()));
			}
			if (StringUtils.isNotBlank(request.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get("unitCode"), request.getUnitCode()));
			}
			if (StringUtils.isNotBlank(request.getProduct())) {
				predicates.add(criteriaBuilder.equal(root.get("product"), request.getProduct()));
			}
			if (StringUtils.isNotBlank(request.getLineOfBusiness())) {
				predicates.add(criteriaBuilder.equal(root.get("lineOfBusiness"), request.getLineOfBusiness()));
			}
			predicates.add(root.get("regularContributionStatus")
					.in(Arrays.asList(PolicyConstants.APPROVED_NO, PolicyConstants.REJECTED_NO)));
			createQuery.orderBy(criteriaBuilder.desc(root.get("modifiedOn")));
			if (StringUtils.isNotBlank(request.getFrom()) && StringUtils.isNotBlank(request.getTo())) {
				Date fromDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(request.getFrom());
				Date toDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(request.getTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get("createdOn"), fromDate, toDate));
			}
			if (StringUtils.isNotBlank(request.getVariant())) {
				predicates.add(criteriaBuilder.equal(root.get("variant"), request.getVariant()));
			}
			if (StringUtils.isNotBlank(request.getPolicyStatus())) {
				predicates.add(criteriaBuilder.equal(root.get("regularContributionStatus"), request.getPolicyStatus()));
			}
			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			adjustmentEntities = entityManager.createQuery(createQuery).getResultList();
			List<RegularAdjustmentContributionDto> regAdjConDto = mapList(adjustmentEntities,
					RegularAdjustmentContributionDto.class);
			commonDto.setResponseData(regAdjConDto);
			commonDto.setTransactionMessage(CommonConstants.FETCH);
			commonDto.setTransactionStatus(CommonConstants.STATUS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: getExisitngLoad ", e);
			commonDto.setTransactionStatus(CommonConstants.FETCH);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("RegularadjustmentServiceImpl :: getExisitngLoad :: Ends");
		return commonDto;
	}

	@Override
	public RegularAdjustmentContributionResponseDto getExistingDetails(Long regularContributionId) {
		RegularAdjustmentContributionResponseDto regularAdjustmentContributionResponseDto = new RegularAdjustmentContributionResponseDto();
		RegularAdjustmentContributionAllDto adjustmentContributionAllDto = new RegularAdjustmentContributionAllDto();
		List<RegularAdjustmentContributionNotesDto> regularAdjustmentContributionNotesDtoList = new ArrayList<>();
		try {
			logger.info("RegularadjustmentServiceImpl :: getExistingDetails :: Start");
			RegularAdjustmentContributionEntity redAdjConEntity = regAdjustConRep
					.findByRegularContributionId(regularContributionId);
			if (redAdjConEntity == null) {
				regularAdjustmentContributionResponseDto.setTransactionMessage(CommonConstants.INVALID_REQUEST);
				regularAdjustmentContributionResponseDto.setTransactionStatus(CommonConstants.FAIL);
				return regularAdjustmentContributionResponseDto;
			}
			List<RegularAdjustmentContributionNotesEntity> noteDetailsLit = adjNotesRepo
					.findByregularContributionId(regularContributionId);
			if (!noteDetailsLit.isEmpty()) {
				regularAdjustmentContributionNotesDtoList = mapList(noteDetailsLit,
						RegularAdjustmentContributionNotesDto.class);
			}
			RegularAdjustmentContributionDto regularAdjustmentContributionDto = convertMainEntityToDtos(
					redAdjConEntity);
			adjustmentContributionAllDto.setRegAdjustmentContribution(regularAdjustmentContributionDto);
			adjustmentContributionAllDto
					.setRegularContributionId(adjustmentContributionAllDto.getRegularContributionId());
			adjustmentContributionAllDto.setRegAdjustmentContributDepositDtos(getApprovedDepositFromPolicyDeposit(
					regularAdjustmentContributionDto.getRegularContributionId(), redAdjConEntity.getPolicyId()));
			adjustmentContributionAllDto.setRegAdjustmentContributionDepositAdjustmentDtos(
					getApprovedAdjustementFromPolicyDeposit(regularAdjustmentContributionDto.getRegularContributionId(),
							redAdjConEntity.getPolicyId()));
			adjustmentContributionAllDto.setRegAdjustmentNotesDtos(regularAdjustmentContributionNotesDtoList);
			adjustmentContributionAllDto.setBank(getPolicyBankList(regularAdjustmentContributionDto.getPolicyId()));
			regularAdjustmentContributionResponseDto.setResponseData(adjustmentContributionAllDto);
			regularAdjustmentContributionResponseDto.setTransactionMessage(CommonConstants.FETCH);
			regularAdjustmentContributionResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
		} catch (Exception e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: getExistingDetails ", e);
			regularAdjustmentContributionResponseDto.setTransactionMessage(e.getMessage());
			regularAdjustmentContributionResponseDto.setTransactionStatus(CommonConstants.FAIL);
		}
		logger.info("RegularadjustmentServiceImpl :: getExistingDetails :: Ends");
		return regularAdjustmentContributionResponseDto;
	}	
	
	private List<RegularAdjustmentContributionDepositAdjustmentDto> getApprovedAdjustementFromPolicyDeposit(
			Long adjustmentContributionId, Long policyId) {
		logger.info("RegularadjustmentServiceImpl :: getApprovedAdjustementFromPolicyDeposit :: Start");

		List<RegularAdjustmentContributionDepositAdjustmentDto> adjustmentContributionDepositDtos = new ArrayList<>();
		List<PolicyDepositEntity> policyDepositEntity = policyDepositRepository
				.findByStatusAndRegularContributionIdAndPolicyIdAndIsActiveTrue(PolicyConstants.ADJESTED,
						adjustmentContributionId, policyId);
		for (PolicyDepositEntity policyDepositEntity2 : policyDepositEntity) {
			RegularAdjustmentContributionDepositAdjustmentDto adjustmentContributionDepositDto = new RegularAdjustmentContributionDepositAdjustmentDto();
			adjustmentContributionDepositDto.setAdjustmentContributiondepositId(policyDepositEntity2.getDepositId());
			adjustmentContributionDepositDto
					.setAdjustmentContributionAdjestmentStatus(policyDepositEntity2.getStatus());
			adjustmentContributionDepositDto.setRegularContributionId(policyDepositEntity2.getRegularContributionId());
			adjustmentContributionDepositDto
					.setAdjustmentContributionId(policyDepositEntity2.getAdjustmentContributionId());
			adjustmentContributionDepositDto.setPolicyId(policyDepositEntity2.getPolicyId());
			adjustmentContributionDepositDto.setChallanNo(policyDepositEntity2.getChallanNo());
			adjustmentContributionDepositDto.setCollectionNo(policyDepositEntity2.getCollectionNo());
			adjustmentContributionDepositDto.setCollectionDate(policyDepositEntity2.getCollectionDate());
			adjustmentContributionDepositDto.setAmount(policyDepositEntity2.getDepositAmount());
			adjustmentContributionDepositDto.setAdjustmentAmount(policyDepositEntity2.getAdjustmentAmount());
			adjustmentContributionDepositDto.setAvailableAmount(policyDepositEntity2.getAvailableAmount());
			adjustmentContributionDepositDto.setTransactionMode(policyDepositEntity2.getTransactionMode());
			adjustmentContributionDepositDto.setCollectionStatus(policyDepositEntity2.getCollectionStatus());
			adjustmentContributionDepositDto.setChequeRealisationDate(policyDepositEntity2.getChequeRealisationDate());
			adjustmentContributionDepositDto.setRemark(policyDepositEntity2.getRemark());
			adjustmentContributionDepositDto.setZeroId(policyDepositEntity2.getZeroId());
			adjustmentContributionDepositDto.setModifiedBy(policyDepositEntity2.getModifiedBy());
			adjustmentContributionDepositDto.setModifiedOn(policyDepositEntity2.getModifiedOn());
			adjustmentContributionDepositDto.setCreatedBy(policyDepositEntity2.getCreatedBy());
			adjustmentContributionDepositDto.setCreatedOn(policyDepositEntity2.getCreatedOn());
			adjustmentContributionDepositDto.setIsActive(policyDepositEntity2.getIsActive());
			adjustmentContributionDepositDtos.add(adjustmentContributionDepositDto);
		}
		logger.info("RegularadjustmentServiceImpl :: getApprovedAdjustementFromPolicyDeposit :: Ends");
		return adjustmentContributionDepositDtos;
	}	
	
	private List<RegularAdjustmentContributionDepositDto> getApprovedDepositFromPolicyDeposit(
			Long adjustmentContributionId, Long policyId) {
		logger.info("RegularadjustmentServiceImpl :: getApprovedDepositFromPolicyDeposit :: Start");

		List<RegularAdjustmentContributionDepositDto> adjustmentContributionDepositDtos = new ArrayList<>();
		List<PolicyDepositEntity> policyDepositEntity = policyDepositRepository
				.findByStatusAndRegularContributionIdAndPolicyIdAndIsActiveTrue(PolicyConstants.DEPOSITSTATUSNEW,
						adjustmentContributionId, policyId);
		for (PolicyDepositEntity policyDepositEntity2 : policyDepositEntity) {
			RegularAdjustmentContributionDepositDto adjustmentContributionDepositDto = new RegularAdjustmentContributionDepositDto();
			adjustmentContributionDepositDto.setRegularContributiondepositId(policyDepositEntity2.getDepositId());
			adjustmentContributionDepositDto.setAdjustmentContributiondepositStatus(policyDepositEntity2.getStatus());
			adjustmentContributionDepositDto.setRegularContributionId(policyDepositEntity2.getRegularContributionId());
			adjustmentContributionDepositDto
					.setAdjustmentContributionId(policyDepositEntity2.getAdjustmentContributionId());
			adjustmentContributionDepositDto.setPolicyId(policyDepositEntity2.getPolicyId());
			adjustmentContributionDepositDto.setChallanNo(policyDepositEntity2.getChallanNo());
			adjustmentContributionDepositDto.setCollectionNo(policyDepositEntity2.getCollectionNo());
			adjustmentContributionDepositDto.setCollectionDate(policyDepositEntity2.getCollectionDate());
			adjustmentContributionDepositDto.setAmount(policyDepositEntity2.getDepositAmount());
			adjustmentContributionDepositDto.setAdjustmentAmount(policyDepositEntity2.getAdjustmentAmount());
			adjustmentContributionDepositDto.setAvailableAmount(policyDepositEntity2.getAvailableAmount());
			adjustmentContributionDepositDto.setTransactionMode(policyDepositEntity2.getTransactionMode());
			adjustmentContributionDepositDto.setCollectionStatus(policyDepositEntity2.getCollectionStatus());
			adjustmentContributionDepositDto.setChequeRealisationDate(policyDepositEntity2.getChequeRealisationDate());
			adjustmentContributionDepositDto.setRemark(policyDepositEntity2.getRemark());
			adjustmentContributionDepositDto.setZeroId(policyDepositEntity2.getZeroId());
			adjustmentContributionDepositDto.setModifiedBy(policyDepositEntity2.getModifiedBy());
			adjustmentContributionDepositDto.setModifiedOn(policyDepositEntity2.getModifiedOn());
			adjustmentContributionDepositDto.setCreatedBy(policyDepositEntity2.getCreatedBy());
			adjustmentContributionDepositDto.setCreatedOn(policyDepositEntity2.getCreatedOn());
			adjustmentContributionDepositDto.setIsActive(policyDepositEntity2.getIsActive());
			adjustmentContributionDepositDtos.add(adjustmentContributionDepositDto);
		}
		logger.info("RegularadjustmentServiceImpl :: getApprovedDepositFromPolicyDeposit :: Ends");
		return adjustmentContributionDepositDtos;
	}	
	
	public RegularAdjustmentContributionDto convertMainEntityToDtos(RegularAdjustmentContributionEntity regAdjEntity) {
		logger.info("RegularadjustmentServiceImpl :: convertMainEntityToDtos :: Start");

		RegularAdjustmentContributionDto dto = new RegularAdjustmentContributionDto();
		dto.setRegularContributionId(regAdjEntity.getRegularContributionId());
		dto.setRegularContributionNumber(regAdjEntity.getRegularContributionNumber());
		dto.setRegularContributionStatus(regAdjEntity.getRegularContributionStatus());
		dto.setWorkFlowStatus(regAdjEntity.getWorkFlowStatus());
		dto.setUnitCode(regAdjEntity.getUnitCode());
		dto.setModifiedBy(regAdjEntity.getModifiedBy());
		dto.setModifiedOn(regAdjEntity.getModifiedOn());
		dto.setCreatedBy(regAdjEntity.getCreatedBy());
		dto.setCreatedOn(regAdjEntity.getCreatedOn());
		dto.setIsActive(regAdjEntity.getIsActive());
		dto.setIsDeposit(regAdjEntity.getIsDeposit());
		dto.setRejectionReasonCode(regAdjEntity.getRejectionReasonCode());
		dto.setRejectionRemarks(regAdjEntity.getRejectionRemarks());
		dto.setPolicyId(regAdjEntity.getPolicyId());
		dto.setTempPolicyId(regAdjEntity.getTempPolicyId());
		dto.setPolicyNumber(regAdjEntity.getPolicyNumber());
		dto.setPolicyStatus(regAdjEntity.getPolicyStatus());
		dto.setPolicyType(regAdjEntity.getPolicyType());
		dto.setPolicyCommencementDate(regAdjEntity.getPolicyCommencementDate());
		dto.setCustomerId(regAdjEntity.getCustomerId());
		dto.setCustomerCode(regAdjEntity.getCustomerCode());
		dto.setMphName(regAdjEntity.getMphName());
		dto.setMphCode(regAdjEntity.getMphCode());
		dto.setProposalNumber(regAdjEntity.getProposalNumber());
		dto.setProduct(regAdjEntity.getProduct());
		dto.setVariant(regAdjEntity.getVariant());
		dto.setTotalContribution(regAdjEntity.getTotalContribution());
		dto.setTotalDeposit(regAdjEntity.getTotalDeposit());
		dto.setAmountToBeAdjusted(regAdjEntity.getAmountToBeAdjusted());
		dto.setFirstPremium(regAdjEntity.getFirstPremium());
		dto.setSinglePremiumFirstYr(regAdjEntity.getSinglePremiumFirstYr());
		dto.setRenewalPremium(regAdjEntity.getRenewalPremium());
		dto.setSubsequentSinglePremium(regAdjEntity.getSubsequentSinglePremium());
		dto.setIsCommencementdateOneYr(regAdjEntity.getIsCommencementdateOneYr());
		dto.setAdjustmentForDate(regAdjEntity.getAdjustmentForDate());
		dto.setAdjustmentDueDate(regAdjEntity.getAdjustmentDueDate());
		dto.setEffectiveDate(regAdjEntity.getEffectiveDate());
		logger.info("RegularadjustmentServiceImpl :: convertMainEntityToDtos :: Ends");
		return dto;
	}	
	
	@Override
	public RegularAdjustmentContributionResponseDto getFrequencyDates(PolicyFrequencyDetailsDto request)
			throws Exception {
		RegularAdjustmentContributionResponseDto responseDto = new RegularAdjustmentContributionResponseDto();
		List<String> frequencyDates = new ArrayList<>();
		try {
			logger.info("RegularadjustmentServiceImpl :: getFrequencyDates :: Start");
			String fincialyear = DateUtils.dateToHypenStringDDMMYYYY(DateUtils.sysDate());
			CommonResponseDto financialYeartDetails = commonServiceImpl.getFinancialYeartDetails(fincialyear);
			if (financialYeartDetails == null) {
				responseDto.setTransactionMessage("Invalid Financial Year");
			}
			List<PolicyFrequencyDetailsEntity> frequencyEntity = policyFrequencyRepo
					.findAllByPolicyIdAndIsActiveTrue(request.getPolicyId());

			List<Date> temp = policyContributionRepository
					.findContributionDateByPolicyIdOrderContributionDate(request.getPolicyId());
			if (!temp.isEmpty() && temp.get(0) != null) {
				request.setPolicyCommencementDate(temp.get(0));
				Date ard = policyMasterRepository.getArdDateByPolicyId(request.getPolicyId());
				request.setPolicyEndDate(ard);
			} else {
				Date ard = policyMasterRepository.getArdDateByPolicyId(request.getPolicyId());
				request.setPolicyEndDate(ard);
				Date commencementdate = policyMasterRepository.getCommemcementDateByPolicyId(request.getPolicyId());
				if (commencementdate == null) {
					responseDto.setTransactionStatus(CommonConstants.FAIL);
					responseDto.setTransactionMessage("No Frequency Details avilable");
				}
				request.setPolicyCommencementDate(commencementdate);
			}
			if (frequencyEntity.isEmpty()) {
				int frequency = NumericUtils.convertStringToInteger(request.getFrequency());
				int totalmonths = DateUtils.getTotalInBetweenMonths(request.getPolicyCommencementDate(),
						request.getPolicyEndDate());
				switch (frequency) {
				case 1:
					frequencyDates = DateUtils.datemonthDifference(totalmonths, request.getPolicyCommencementDate(),
							request.getPolicyEndDate());
					break;
				case 2:
					frequencyDates = DateUtils.dateQuarterDifference(totalmonths, request.getPolicyCommencementDate(),
							request.getPolicyEndDate());
					break;
				case 3:
					frequencyDates = DateUtils.convertDateTimeToStringWithPeriod(totalmonths,
							request.getPolicyCommencementDate(), request.getPolicyEndDate());
					break;
				case 4:
					frequencyDates = DateUtils.datehalfDifference(totalmonths, request.getPolicyCommencementDate(),
							request.getPolicyEndDate());
					break;
				default:
					logger.info("Switch Exit ");
				}
				if (frequencyDates != null) {
					for (int i = 0; i <= frequencyDates.size() - 1; i++) {
						PolicyFrequencyDetailsEntity entity = new PolicyFrequencyDetailsEntity();
						entity.setPolicyId(request.getPolicyId());
						entity.setFrequencyDates(DateUtils.convertStringToDate(frequencyDates.get(i)));
						entity.setFrequency(request.getFrequency());
						entity.setStatus(CommonConstants.UNPAID);
						entity.setPolicyCommencementDate(request.getPolicyCommencementDate());
						entity.setPolicyEndDate(request.getPolicyEndDate());
						policyFrequencyRepo.save(entity);
					}
					responseDto.setTransactionMessage(PolicyConstants.SUCCESS);
					responseDto.setTransactionStatus(PolicyConstants.STATUS);
				}
			} else {
				responseDto.setTransactionMessage(PolicyConstants.DENY);
				responseDto.setTransactionStatus(PolicyConstants.ERRORSTATUS);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: getFrequencyDates ", e);
			responseDto.setTransactionStatus(CommonConstants.FAIL);
			responseDto.setTransactionMessage(CommonConstants.ERROR);
		} finally {
			logger.info("RegularadjustmentServiceImpl :: getFrequencyDates :: Ends");
		}
		return responseDto;
	}	
	
	@Override
	public AdjustmentContributionResponseDto getFrequencyByPolicyId(Long policyId) {
		AdjustmentContributionResponseDto commonDto = new AdjustmentContributionResponseDto();
		List<PolicyFrequencyDetailsEntity> entities = new ArrayList<>();
		try {
			logger.info("RegularadjustmentServiceImpl :: getFrequencyByPolicyId :: Start");
			List<PolicyFrequencyDetailsEntity> entity = policyFrequencyRepo
					.findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyIdDesc(policyId, CommonConstants.UNPAID);
			if (entity != null) {
				for (PolicyFrequencyDetailsEntity iEntity : entity) {
					PolicyFrequencyDetailsEntity getEntity = new PolicyFrequencyDetailsEntity();
					getEntity.setFrequency(iEntity.getFrequency());
					getEntity.setFrequencyDates(iEntity.getFrequencyDates());
					getEntity.setStatus(iEntity.getStatus());
					getEntity.setPolicyId(iEntity.getPolicyId());
					getEntity.setFrequencyId(iEntity.getFrequencyId());
					entities.add(getEntity);
				}
				List<PolicyFrequencyDetailsDto> policyFrequencyDetailsDto=new ArrayList<>();
//						modelMapper.addMappings(getEntity,PolicyFrequencyDetailsDto.class);
				for(PolicyFrequencyDetailsEntity policyFrequencyDetailsEntityOne:entities) {
					PolicyFrequencyDetailsDto entityOne=new PolicyFrequencyDetailsDto();
					entityOne=	modelMapper.map(policyFrequencyDetailsEntityOne, PolicyFrequencyDetailsDto.class);
					entityOne.setFrequencyDates(DateUtils.dateToStringDDMMYYYY(policyFrequencyDetailsEntityOne.getFrequencyDates()));;
					policyFrequencyDetailsDto.add
					(entityOne);
				}
				
				commonDto.setFrequencyDtos(policyFrequencyDetailsDto);
				
				commonDto.setTransactionMessage(PolicyConstants.SUCCESS);
				commonDto.setTransactionStatus(PolicyConstants.STATUS);
			} else {
				commonDto.setTransactionMessage(PolicyConstants.DENY);
				commonDto.setTransactionStatus(PolicyConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: getFrequencyByPolicyId ", e);
			commonDto.setTransactionMessage(CommonConstants.EXCEPTION);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("RegularadjustmentServiceImpl :: getFrequencyByPolicyId :: Ends");
		return commonDto;
	}	
	
	@Override
	public PolicyResponseDto newcitrieaSearch(PolicySearchDto policySearchDto) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("RegularadjustmentServiceImpl :: newcitrieaSearch :: Start");
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<MphMasterEntity> createQuery = criteriaBuilder.createQuery(MphMasterEntity.class);
			Root<MphMasterEntity> root = createQuery.from(MphMasterEntity.class);
			Join<MphMasterEntity, PolicyMasterEntity> join = root.join("policyMaster");
			if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())) {
				join.on(criteriaBuilder.equal(join.get("policyNumber"), policySearchDto.getPolicyNumber()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getProduct())) {
				join.on(criteriaBuilder.equal(join.get("productId"), policySearchDto.getProduct()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getLineOfBusiness())) {
				join.on(criteriaBuilder.equal(join.get("lineOfBusiness"), policySearchDto.getLineOfBusiness()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getPolicyStatus())) {
				join.on(criteriaBuilder.equal(join.get("policyStatus"), policySearchDto.getPolicyStatus()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getVariant())) {
				join.on(criteriaBuilder.equal(join.get("variant"), policySearchDto.getVariant()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(join.get("unitId"), policySearchDto.getUnitCode()));
			}
			predicates.add(join.get(PolicyConstants.POLICYSTATUS)
					.in(Arrays.asList(PolicyConstants.APPROVED_NO, PolicyConstants.REJECTED_NO)));
			if (StringUtils.isNotBlank(policySearchDto.getMphCode())) {
				predicates.add(criteriaBuilder.equal(root.get("mphCode"), policySearchDto.getMphCode()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getMphName())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("mphName")),
						policySearchDto.getMphName().toLowerCase()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getFrom()) && StringUtils.isNotBlank(policySearchDto.getTo())) {
				Date fromDate = CommonDateUtils.convertStringToDate(policySearchDto.getFrom());
				Date toDate = CommonDateUtils.convertStringToDate(policySearchDto.getTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				Predicate onStart = criteriaBuilder.greaterThanOrEqualTo(root.get("createdOn"), fromDate);
				Predicate onEnd = criteriaBuilder.lessThanOrEqualTo(root.get("createdOn"), toDate);
				predicates.add(onStart);
				predicates.add(onEnd);
			}
			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			createQuery.orderBy(criteriaBuilder.desc(root.get("modifiedOn")));
			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			List<MphMasterEntity> result = entityManager.createQuery(createQuery).getResultList();
			List<PolicySearchResponseDto> response = result.stream().map(this::convertMasterEntityToDto)
					.collect(Collectors.toList());
			List<PolicyDto> policyDtoList = new ArrayList<>();
			for (PolicySearchResponseDto qwerty : response) {
				PolicyDto policyDto = new PolicyDto();
				policyDto.setProposalNumber(qwerty.getProposalNumber());
				policyDto.setPolicyNumber(qwerty.getPolicyNumber());
				policyDto.setMphName(qwerty.getMphName());
				policyDto.setProduct(qwerty.getProduct());
				policyDto.setPolicyStatus(qwerty.getPolicyStatus());
				policyDto.setUnitCode(qwerty.getUnitOffice());
				policyDto.setMphCode(qwerty.getMphCode());
				policyDto.setPolicyId(qwerty.getPolicyId());
				policyDto.setMphId(qwerty.getMphId());
				policyDtoList.add(policyDto);
			}
			commonDto.setPolicyDtos(policyDtoList);
			commonDto.setTransactionMessage(CommonConstants.FETCH);
			commonDto.setTransactionStatus(CommonConstants.STATUS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: newcitrieaSearch ", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("RegularadjustmentServiceImpl :: newcitrieaSearch :: Ends");
		}
		return commonDto;
	}

	private PolicySearchResponseDto convertMasterEntityToDto(MphMasterEntity mphMasterEntity) {
		logger.info("RegularadjustmentServiceImpl :: convertMasterEntityToDto :: Start");
		PolicySearchResponseDto policySearchResponseDto = new PolicySearchResponseDto();
		policySearchResponseDto.setMphCode(mphMasterEntity.getMphCode());
		policySearchResponseDto.setMphName(mphMasterEntity.getMphName());
		policySearchResponseDto.setMphId(mphMasterEntity.getMphId());
		policySearchResponseDto.setProposalNumber(mphMasterEntity.getProposalNumber());
		Set<PolicyMasterEntity> policyMasterEntity = mphMasterEntity.getPolicyMaster();
		if (!policyMasterEntity.isEmpty()) {
			for (PolicyMasterEntity policyMaster : policyMasterEntity) {
				policySearchResponseDto.setPolicyId(policyMaster.getPolicyId());
				policySearchResponseDto.setPolicyNumber(policyMaster.getPolicyNumber());
				policySearchResponseDto.setPolicyStatus(policyMaster.getPolicyStatus());
				policySearchResponseDto.setUnitOffice(policyMaster.getUnitId());
				policySearchResponseDto.setProduct(NumericUtils.convertLongToString(policyMaster.getProductId()));
			}
		}
		logger.info("RegularadjustmentServiceImpl :: convertMasterEntityToDto :: Ends");
		return policySearchResponseDto;
	}

	@Override
	public PolicyAdjustmentResponse saveAdjustmentOldDto(Long policyId) {
		PolicyAdjustmentResponse response = new PolicyAdjustmentResponse();
		logger.info("RegularadjustmentServiceImpl :: saveAdjustmentOldDto :: Start");

		PolicyMasterEntity policyEntityOpt = policyMasterRepository.findByPolicyId(policyId);
		if (policyEntityOpt.getMphId() == null) {
			response.setTransactionMessage("Invalid MPH");
			response.setTransactionStatus("FAIL");
		}
		MphMasterEntity qwert = mphMasterRepository.findByMphIdAndIsActiveTrue(policyEntityOpt.getMphId());
		MphMasterDto mphMasterDto = policyCommonServiceImpl.convertMphMasterEntityToMphMasterDto(qwert);
		PolicyDto policyDto = policyCommonServiceImpl.convertNewResponseToOldResponse(mphMasterDto);
		policyDto.getDeposit().removeIf(deposit -> PolicyConstants.ADJESTED.equalsIgnoreCase(deposit.getStatus()));
		policyDto.getMembers().removeIf(member -> Boolean.TRUE.equals(member.getIsZeroId()));
		policyDto.getAdjustments()
				.removeIf(deposits -> PolicyConstants.DEPOSITSTATUSNEW.equalsIgnoreCase(deposits.getStatus()));
		response.setPolicyId(policyEntityOpt.getPolicyId());
		response.setPolicyDto(policyDto);
		response.setAdjustments(mapListAdjustment(mapListAdjustment(response.getPolicyDto().getAdjustments())));
		response.setTotalAmountToBeAdjustment(response.getPolicyDto().getAmountToBeAdjusted());
		response.setTotalDeposit(response.getPolicyDto().getTotalDeposit());
		response.setTransactionStatus(CommonConstants.SUCCESS);
		response.setTransactionMessage(CommonConstants.SAVEMESSAGE);
		logger.info("RegularadjustmentServiceImpl :: saveAdjustmentOldDto :: Ends");
		return response;
	}

	private Set<PolicyAdjustmentOldDto> mapListAdjustment(Set<PolicyAdjustmentOldDto> adjustments) {
		logger.info("RegularadjustmentServiceImpl :: mapListAdjustment :: Start");
		Set<PolicyAdjustmentOldDto> response = new HashSet<>();
		for (PolicyAdjustmentOldDto policyContributionSummaryDto : adjustments) {
			PolicyAdjustmentOldDto oldDto = new PolicyAdjustmentOldDto();
			oldDto.setAdjestmentId(policyContributionSummaryDto.getDepositId());
			oldDto.setChallanNo(policyContributionSummaryDto.getChallanNo());
			oldDto.setDepositId(policyContributionSummaryDto.getDepositId());
			oldDto.setPolicyId(policyContributionSummaryDto.getPolicyId());
			oldDto.setCollectionNo(policyContributionSummaryDto.getCollectionNo());
			oldDto.setCollectionDate(policyContributionSummaryDto.getCollectionDate());
			oldDto.setAmount(policyContributionSummaryDto.getAmount());
			oldDto.setAdjestmentAmount(policyContributionSummaryDto.getAdjestmentAmount());
			oldDto.setAvailableAmount(policyContributionSummaryDto.getAvailableAmount());
			oldDto.setTransactionMode(policyContributionSummaryDto.getTransactionMode());
			oldDto.setCollectionStatus(policyContributionSummaryDto.getCollectionStatus());
			oldDto.setChequeRealisationDate(policyContributionSummaryDto.getChequeRealisationDate());
			oldDto.setRemark(policyContributionSummaryDto.getRemark());
			oldDto.setStatus(policyContributionSummaryDto.getStatus());
			oldDto.setZeroId(policyContributionSummaryDto.getZeroId());
			oldDto.setModifiedBy(policyContributionSummaryDto.getModifiedBy());
			oldDto.setModifiedOn(policyContributionSummaryDto.getModifiedOn());
			oldDto.setCreatedBy(policyContributionSummaryDto.getCreatedBy());
			oldDto.setCreatedOn(policyContributionSummaryDto.getCreatedOn());
			oldDto.setIsActive(policyContributionSummaryDto.getIsActive());
			response.add(oldDto);
		}
		logger.info("RegularadjustmentServiceImpl :: mapListAdjustment :: Ends");
		return response;
	}

	@Override
	public void download(Long batchId, String fileType, HttpServletResponse response) {
		try {
			logger.info("RegularAdjustmentServiceImpl -- downloadAdjustmentContributionRecords -- start");
			if (batchId != null && fileType != null) {
				RegularAdjustmentContributionBatchEntity batch = regularAdjustmentContributionBatchRepository
						.findByBatchIdAndIsActiveTrue(batchId);

				if (batch != null) {

					byte[] bytes = null;
					String fileName = "";
					switch (fileType) {

					case "failed":
						bytes = batch.getFailedFile();
						fileName = "Failed_Regular_Adjustment_Contributions";
						break;
					case "success":
						bytes = batch.getSuccessFile();
						fileName = "Success_Regular_Adjustment_Contributions";
						break;
					default:
						bytes = batch.getRawFile();
						fileName = "Raw_Regular_Adjustment_Contributions";

					}

					response.setContentType("Content-Type: text/csv");
					response.setHeader("Content-Disposition",
							"attachment;filename=" + fileName + "_" + batchId + ".csv");
					ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
					baos.write(bytes, 0, bytes.length);
					OutputStream os = response.getOutputStream();
					response.setHeader("STATUS", "File is Downloaded Successfully!!!");
					baos.writeTo(os);
					os.flush();
					os.close();
				} else {
					response.setHeader("STATUS", "No files Available against this BathId " + batchId);
				}

			} else {
				response.setHeader("STATUS", "BatchId and FileType is null!!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("RegularAdjustmentServiceImpl -- downloadAdjustmentContributionRecords -- exception", e);

		}

		logger.info("RegularAdjustmentServiceImpl -- downloadAdjustmentContributionRecords -- end");
	}	
	
	@Override
	public RegularAdjustmentContributionResponseDto reject(Long regularContributionId,
			String adjustmentContributionStatus, String modifiedBy) {
		RegularAdjustmentContributionResponseDto responseDto = new RegularAdjustmentContributionResponseDto();
		try {
			logger.info("RegularadjustmentServiceImpl :: reject :: Start");
			RegularAdjustmentContributionTempEntity regAdjConTempEntity = adjTempRepo
					.findByRegularContributionIdAndIsActiveTrue(regularContributionId);
			if (regAdjConTempEntity != null) {
				regAdjConTempEntity.setModifiedBy(modifiedBy);
				regAdjConTempEntity.setModifiedOn(DateUtils.sysDate());
				regAdjConTempEntity.setIsActive(false);
				RegularAdjustmentContributionTempEntity saveEntity = adjTempRepo.save(regAdjConTempEntity);
				RegularAdjustmentContributionEntity regEntity = convertTempToMaster(saveEntity,
						adjustmentContributionStatus);
				RegularAdjustmentContributionEntity saveMain = regAdjustConRep.save(regEntity);
				RegularAdjustmentContributionDto dto = modelMapper.map(saveMain,
						RegularAdjustmentContributionDto.class);
				responseDto.setResponseData(dto);
				responseDto.setTransactionStatus(CommonConstants.SUCCESS);
				responseDto.setTransactionStatus(CommonConstants.UPDATEMESSAGE);
			} else {
				responseDto.setTransactionStatus(CommonConstants.ERROR);
				responseDto.setTransactionMessage(CommonConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: reject ", e);
			responseDto.setTransactionStatus(CommonConstants.ERROR);
			responseDto.setTransactionMessage(CommonConstants.FAIL);
		}
		logger.info("RegularadjustmentServiceImpl :: reject :: Ends");
		return responseDto;
	}	
	
	@Override
	public RegularAdjustmentContributionResponseDto saveAdjustment(RegularACSaveAdjustmentRequestDto adjustmentDto) {
		RegularAdjustmentContributionResponseDto response = new RegularAdjustmentContributionResponseDto();
		RegularAdjustmentContributionAllDto adjustmentContributionAllDto = new RegularAdjustmentContributionAllDto();
		try {
			logger.info("RegularadjustmentServiceImpl :: saveAdjustment :: Start");
			String contributionType = PolicyConstants.REGULARADJUSTMENTNEW;
// Adjustment Temp Details
			Optional<RegularAdjustmentContributionTempEntity> adjusOptional = adjTempRepo
					.findById(adjustmentDto.getRegularContributionId());
			if (!adjusOptional.isPresent()) {
				response.setTransactionMessage(PolicyConstants.POLICY_INVALID);
				response.setTransactionStatus(PolicyConstants.FAIL);
				return response;
			}
			RegularAdjustmentContributionTempEntity adjustementTempEntity = adjusOptional.get();
// Policy Contribution Details
			List<PolicyContributionTempEntity> policyContributionOpt = checkPolicycontributionPreviouse(
					adjustementTempEntity.getTempPolicyId(), adjustementTempEntity.getPolicyId(),
					adjustementTempEntity.getRegularContributionId(), contributionType, adjustmentDto.getRole());

// Policy Deposit Details
			Optional<PolicyDepositTempEntity> policyDepositOpt = policyDepositTempRepository
					.findByStatusAndDepositIdAndZeroIdAndPolicyIdAndRegularContributionId(
							PolicyConstants.DEPOSITSTATUSNEW, adjustmentDto.getRegularContributiondepositId(), false,
							adjustmentDto.getPolicyId(), adjustmentDto.getRegularContributionId());
			if (!policyDepositOpt.isPresent()) {
				response.setTransactionMessage(PolicyConstants.DEPOSIT_INVALID);
				response.setTransactionStatus(PolicyConstants.FAIL);
				return response;
			}
			PolicyDepositTempEntity depositEntity = policyDepositOpt.get();
// Set Ammount Details Respectively

			String mphName = adjustementTempEntity.getMphName();
			BigDecimal amountToBeAdjusted = adjustementTempEntity.getAmountToBeAdjusted();
			BigDecimal newDepositAmount = depositEntity.getAvailableAmount();
			BigDecimal totalContribution = adjustementTempEntity.getTotalContribution();
			BigDecimal availableDepositAmt = adjustementTempEntity.getTotalDeposit();
			BigDecimal newTotalDepositAmt = availableDepositAmt.add(newDepositAmount);
			Date adjustmentDueDate = adjustementTempEntity.getAdjustmentDueDate();
			
			 Date newContributionDate = null;
				
//				if (depositEntity.getTransactionMode().equalsIgnoreCase("Q")) {
//					newContributionDate = depositEntity.getChequeRealisationDate();
//				}
//				else {
//					newContributionDate = depositEntity.getCollectionDate();
//				}
			
			    if(depositEntity.getChequeRealisationDate() != null) {
				newContributionDate = depositEntity.getChequeRealisationDate();
			    }
				
// Switch Based on Policy Type	-STARTS		
			switch (adjustementTempEntity.getPolicyType()) {
			case "DB":
				logger.info("PolicyId - {}, Contribution depositAmount is less: {}", adjustmentDto.getPolicyId(),
						newTotalDepositAmt);
				calculatePolicyContribution(policyContributionOpt, adjustementTempEntity, contributionType,
						depositEntity, availableDepositAmt, newTotalDepositAmt, totalContribution,
						adjustmentDto.getRole(), amountToBeAdjusted, newDepositAmount, adjustmentDueDate,newContributionDate);

				response.setResponseData(getResponseAdjustmentContributionAllDto(adjustementTempEntity,
						adjustmentDto.getRegularContributionId(), adjustmentDto.getPolicyId()));

				response.setTotalDeposit(adjustementTempEntity.getTotalDeposit());
				response.setIsCommencementdateOneYr(adjustementTempEntity.getIsCommencementdateOneYr());
				response.setBank(getPolicyBankList(adjustementTempEntity.getPolicyId()));
				response.setTransactionMessage(null);
				response.setTransactionStatus(null);
				response.setZeroRow(adjustmentContributionAllDto.getZeroRow());
				break;
			case "DC":
//Switch Based on Total Contribution deposit Amount Commpare Condition  STARTS 				
				switch (newTotalDepositAmt.compareTo(totalContribution)) {
				case -1:
					logger.info("PolicyId - {}, Contribution depositAmount is less: {}", adjustmentDto.getPolicyId(),
							newTotalDepositAmt);
					calculatePolicyContribution(policyContributionOpt, adjustementTempEntity, contributionType,
							depositEntity, availableDepositAmt, newTotalDepositAmt, totalContribution,
							adjustmentDto.getRole(), amountToBeAdjusted, newDepositAmount, adjustmentDueDate,newContributionDate);
					response.setResponseData(getResponseAdjustmentContributionAllDto(adjustementTempEntity,
							adjustmentDto.getRegularContributionId(), adjustmentDto.getPolicyId()));
					response.setTotalDeposit(adjustementTempEntity.getTotalDeposit());
					response.setIsCommencementdateOneYr(adjustementTempEntity.getIsCommencementdateOneYr());
					response.setBank(getPolicyBankList(adjustementTempEntity.getPolicyId()));
					response.setTransactionMessage(null);
					response.setTransactionStatus(null);
					response.setZeroRow(adjustmentContributionAllDto.getZeroRow());
					break;
				case 0:
					logger.info("PolicyId - {}, Contribution depositAmount is equals: {}", adjustmentDto.getPolicyId(),
							newTotalDepositAmt);
					calculatePolicyContribution(policyContributionOpt, adjustementTempEntity, contributionType,
							depositEntity, availableDepositAmt, newTotalDepositAmt, totalContribution,
							adjustmentDto.getRole(), amountToBeAdjusted, newDepositAmount, adjustmentDueDate,newContributionDate);
					response.setResponseData(getResponseAdjustmentContributionAllDto(adjustementTempEntity,
							adjustmentDto.getRegularContributionId(), adjustmentDto.getPolicyId()));
					response.setTotalDeposit(adjustementTempEntity.getTotalDeposit());
					response.setIsCommencementdateOneYr(adjustementTempEntity.getIsCommencementdateOneYr());
					response.setBank(getPolicyBankList(adjustementTempEntity.getPolicyId()));
					response.setTransactionMessage(null);
					response.setTransactionStatus(null);
					response.setZeroRow(adjustmentContributionAllDto.getZeroRow());
					break;
				case 1:
					logger.info("PolicyId - {}, Contribution depositAmount is Excess: {}", adjustmentDto.getPolicyId(),
							newTotalDepositAmt);
					if (!"true".equals(adjustmentDto.getIsZeroId())) {
						calculatePolicyContribution(policyContributionOpt, adjustementTempEntity, contributionType,
								depositEntity, availableDepositAmt, newTotalDepositAmt, totalContribution,
								adjustmentDto.getRole(), amountToBeAdjusted, newDepositAmount, adjustmentDueDate,newContributionDate);
						response = getFinalResponse(
								getResponseAdjustmentContributionAllDto(adjustementTempEntity,
										adjustmentDto.getRegularContributionId(), adjustmentDto.getPolicyId()),
								adjustementTempEntity);
					} else {
						Long policyContributionId = null;
						for (PolicyContributionTempEntity policyContribution : policyContributionOpt) {
							policyContribution.setContributionId(null);
							policyContribution.setPolicyId(adjustementTempEntity.getTempPolicyId());
							policyContribution.setMasterPolicyId(adjustementTempEntity.getPolicyId());
							policyContribution
									.setAdjustmentContributionId(policyContribution.getAdjustmentContributionId());
							policyContribution
									.setRegularContributionId(adjustementTempEntity.getRegularContributionId());
							policyContribution.setContributionType(contributionType);
							policyContribution.setContributionDate(new Date());
							policyContribution.setAdjustmentDueDate(adjustmentDueDate);	
							policyContribution.setContReferenceNo(depositEntity.getChallanNo());
							policyContribution.setVersionNo(policyContribution.getVersionNo() + 1);
							policyContribution.setFinancialYear(DateUtils.getFinancialYrByDt(DateUtils.sysDate()));
							policyContribution.setOpeningBalance(availableDepositAmt);
							policyContribution.setClosingBalance(newTotalDepositAmt);
							policyContribution.setEmployerContribution(adjustementTempEntity.getEmployerContribution());
							policyContribution.setEmployeeContribution(adjustementTempEntity.getEmployeeContribution());
							policyContribution
									.setVoluntaryContribution(adjustementTempEntity.getVoluntaryContribution());
							policyContribution.setTotalContribution(totalContribution);
							policyContribution.setIsDeposit(Boolean.TRUE);
							policyContribution.setIsActive(Boolean.TRUE);
							policyContribution.setCreatedBy(adjustmentDto.getRole());
							policyContribution.setCreatedOn(new Date());
							policyContribution.setModifiedBy(adjustmentDto.getRole());
							policyContribution.setModifiedOn(new Date());
							policyContribution.setTxnEntryStatus(Boolean.FALSE);
							policyContribution.setPolicyContribution(null);
							policyContribution.setEffectiveDate(newContributionDate);
							policyContribution = policyContributionTempRepository.save(policyContribution);
							Set<MemberContributionTempEntity> memberContributionList = new HashSet<>();
							Set<MemberContributionTempEntity> templist = memberContributionTempRepository
									.findByPolicyIdAndRegularContributionIdAndIsActiveTrue(
											adjustementTempEntity.getTempPolicyId(),
											adjustementTempEntity.getRegularContributionId());
							for (MemberContributionTempEntity memberContribution : templist) {
								MemberContributionTempEntity memberContribution2 = new MemberContributionTempEntity();
								memberContribution2.setMemberConId(memberContribution.getMemberConId());
								memberContribution2.setPolicyConId(policyContribution.getContributionId());
								memberContribution2.setBatchId(memberContribution.getBatchId());
								memberContribution2.setPolicyId(adjustementTempEntity.getTempPolicyId());
								memberContribution2.setMemberId(memberContribution.getMemberId());
								memberContribution2.setMasterPolicyId(adjustementTempEntity.getPolicyId());
								memberContribution2.setMasterMemberId(memberContribution.getMasterMemberId());
								memberContribution2
										.setRegularContributionId(adjustementTempEntity.getRegularContributionId());
								memberContribution2.setContributionType(contributionType);
								memberContribution2.setContributionDate(new Date());
								memberContribution2.setAdjustmentDueDate(adjustmentDueDate);
								memberContribution2.setFinancialYear(DateUtils.getFinancialYrByDt(DateUtils.sysDate()));
								memberContribution2.setVersionNo(memberContribution.getVersionNo());
								memberContribution2.setLicId(memberContribution.getLicId());
								memberContribution2.setOpeningBalance(memberContribution.getOpeningBalance());
								memberContribution2.setClosingBalance(memberContribution.getClosingBalance());
								memberContribution2
										.setEmployeeContribution(memberContribution.getEmployeeContribution());
								memberContribution2
										.setEmployerContribution(memberContribution.getEmployerContribution());
								memberContribution2
										.setVoluntaryContribution(memberContribution.getVoluntaryContribution());
								memberContribution2.setTotalContribution(memberContribution.getTotalContribution());
								memberContribution2
										.setTotalInterestedAccured(memberContribution.getTotalInterestedAccured());
								memberContribution2.setIsDeposit(Boolean.TRUE);
								memberContribution2.setIsActive(Boolean.TRUE);
								memberContribution2.setCreatedBy(memberContribution.getCreatedBy());
								memberContribution2.setCreatedOn(memberContribution.getCreatedOn());
								memberContribution2.setModifiedBy(adjustmentDto.getRole());
								memberContribution2.setModifiedOn(new Date());
								memberContribution2.setTxnEntryStatus(Boolean.FALSE);
								memberContribution2.setEffectiveDate(newContributionDate);
								memberContributionList.add(memberContribution2);
							}
							policyContribution.setPolicyContribution(memberContributionList);
							policyContributionTempRepository.save(policyContribution);

							policyContributionId = policyContribution.getContributionId();
							break;
						}
						depositEntity.setStatus(PolicyConstants.ADJESTED);
						depositEntity.setAdjustmentAmount(amountToBeAdjusted);
						depositEntity.setAvailableAmount(newDepositAmount.subtract(depositEntity.getAvailableAmount()));
						depositEntity.setIsDeposit(Boolean.TRUE);
						depositEntity.setZeroId(Boolean.TRUE);
						depositEntity.setVoucherNo(adjustmentDto.getVoucherNo());
						depositEntity.setVoucherDate(adjustmentDto.getVoucherDate());	
						policyDepositTempRepository.save(depositEntity);
						adjustementTempEntity
								.setTotalDeposit(availableDepositAmt.add(depositEntity.getAdjustmentAmount()));
						adjustementTempEntity.setAdjustmentForDate(new Date());
						adjustementTempEntity.setAdjustmentDueDate(adjustmentDueDate);
						adjustementTempEntity.setEffectiveDate(newContributionDate);
						adjTempRepo.save(adjustementTempEntity);
						response.setTotalDeposit(adjustementTempEntity.getTotalDeposit());
						if ("DC".equals(adjustementTempEntity.getPolicyType())) {
							BigDecimal zeroAmount = newDepositAmount.subtract(depositEntity.getAdjustmentAmount());
							String role = adjustmentDto.getRole();
							Long memberContibutionId = null;
							String type = contributionType;

							adjustmentContributionServiceImpl.checkZeroAccountTempInMember(
									adjustementTempEntity.getTempPolicyId(), zeroAmount, role, policyContributionId,
									memberContibutionId, type, mphName);

//							checkZeroAccountTempInMember(adjustementTempEntity.getTempPolicyId(), zeroAmount, role,
//									policyContributionId, memberContibutionId, type);
						}
						response.setTotalDeposit(adjustementTempEntity.getTotalDeposit());
						response.setIsCommencementdateOneYr(adjustementTempEntity.getIsCommencementdateOneYr());
						adjustmentContributionAllDto.setRegAdjustmentContribution(
								modelMapper.map(adjustementTempEntity, RegularAdjustmentContributionDto.class));
						adjustmentContributionAllDto.setRegularContributionId(
								adjustmentContributionAllDto.getRegAdjustmentContribution().getRegularContributionId());
						adjustmentContributionAllDto.setRegAdjustmentContributDepositDtos(getDepositFromPolicyDeposit(
								adjustmentDto.getRegularContributionId(), adjustmentDto.getPolicyId()));
						adjustmentContributionAllDto.setRegAdjustmentContributionDepositAdjustmentDtos(
								getAdjustementFromPolicyDeposit(adjustmentDto.getRegularContributionId(),
										adjustmentDto.getPolicyId()));
						adjustmentContributionAllDto
								.setZeroRow(getZeroAccountFromPolicyTemp(adjustmentDto.getPolicyId()));
						adjustmentContributionAllDto.setBank(getPolicyBankList(adjustmentDto.getPolicyId()));
						response.setResponseData(adjustmentContributionAllDto);
						response.setBank(getPolicyBankList(adjustmentDto.getPolicyId()));
						response.setTransactionMessage(null);
						response.setTransactionStatus(null);
						response.setZeroRow(adjustmentContributionAllDto.getZeroRow());
					}
					break;
				default:
					break;
				}
//   Switch Based on Total Contribution deposit Amount Commpare Condition  STARTS				
				break;
			default:
				break;
			}
// Switch Based on Policy Type	-STARTS			
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: saveAdjustment ", e);
			response.setTransactionMessage(PolicyConstants.FAIL);
			response.setTransactionStatus(PolicyConstants.ERROR);
		} finally {
			logger.info("RegularadjustmentServiceImpl :: saveAdjustment :: Ends");
		}
		return response;
	}

	private List<RegularAdjustmentContributionDepositDto> getDepositFromPolicyDeposit(Long adjustmentContributionId,
			Long policyId) {
		List<RegularAdjustmentContributionDepositDto> adjustmentContributionDepositDtos = new ArrayList<>();

		logger.info("RegularadjustmentServiceImpl :: getDepositFromPolicyDeposit :: Starts");

		List<PolicyDepositTempEntity> policyDepositEntity = policyDepositTempRepository
				.findByStatusAndRegularContributionIdAndPolicyIdAndIsActiveTrue(PolicyConstants.DEPOSITSTATUSNEW,
						adjustmentContributionId, policyId);
		for (PolicyDepositTempEntity policyDepositEntity2 : policyDepositEntity) {
			RegularAdjustmentContributionDepositDto adjustmentContributionDepositDto = new RegularAdjustmentContributionDepositDto();
			adjustmentContributionDepositDto.setRegularContributiondepositId(policyDepositEntity2.getDepositId());
			adjustmentContributionDepositDto.setAdjustmentContributiondepositStatus(policyDepositEntity2.getStatus());
			adjustmentContributionDepositDto.setRegularContributionId(policyDepositEntity2.getRegularContributionId());
			adjustmentContributionDepositDto.setPolicyId(policyDepositEntity2.getPolicyId());
			adjustmentContributionDepositDto.setChallanNo(policyDepositEntity2.getChallanNo());
			adjustmentContributionDepositDto.setCollectionNo(policyDepositEntity2.getCollectionNo());
			adjustmentContributionDepositDto.setCollectionDate(policyDepositEntity2.getCollectionDate());
			adjustmentContributionDepositDto.setAmount(policyDepositEntity2.getDepositAmount());
			adjustmentContributionDepositDto.setAdjustmentAmount(policyDepositEntity2.getAdjustmentAmount());
			adjustmentContributionDepositDto.setAvailableAmount(policyDepositEntity2.getAvailableAmount());
			adjustmentContributionDepositDto.setTransactionMode(policyDepositEntity2.getTransactionMode());
			adjustmentContributionDepositDto.setCollectionStatus(policyDepositEntity2.getCollectionStatus());
			adjustmentContributionDepositDto.setChequeRealisationDate(policyDepositEntity2.getChequeRealisationDate());
			adjustmentContributionDepositDto.setRemark(policyDepositEntity2.getRemark());
			adjustmentContributionDepositDto.setZeroId(policyDepositEntity2.getZeroId());
			adjustmentContributionDepositDto.setModifiedBy(policyDepositEntity2.getModifiedBy());
			adjustmentContributionDepositDto.setModifiedOn(policyDepositEntity2.getModifiedOn());
			adjustmentContributionDepositDto.setCreatedBy(policyDepositEntity2.getCreatedBy());
			adjustmentContributionDepositDto.setCreatedOn(policyDepositEntity2.getCreatedOn());
			adjustmentContributionDepositDto.setIsActive(policyDepositEntity2.getIsActive());
			adjustmentContributionDepositDtos.add(adjustmentContributionDepositDto);
		}

		logger.info("RegularadjustmentServiceImpl :: getDepositFromPolicyDeposit :: Ends");
		return adjustmentContributionDepositDtos;
	}	
	
	private Object getZeroAccountFromPolicyTemp(Long policyId) {
		return zeroAccountTempRepository.findByPolicyIdAndIsActiveTrue(policyId);
	}	
	
	private List<RegularAdjustmentContributionDepositAdjustmentDto> getAdjustementFromPolicyDeposit(
			Long adjustmentContributionId, Long policyId) {
		logger.info("RegularadjustmentServiceImpl :: getAdjustementFromPolicyDeposit :: Starts");
		List<RegularAdjustmentContributionDepositAdjustmentDto> adjustmentContributionDepositDtos = new ArrayList<>();
		List<PolicyDepositTempEntity> policyDepositEntity = policyDepositTempRepository
				.findByStatusAndRegularContributionIdAndPolicyIdAndIsActiveTrue(PolicyConstants.ADJESTED,
						adjustmentContributionId, policyId);
		for (PolicyDepositTempEntity policyDepositEntity2 : policyDepositEntity) {
			RegularAdjustmentContributionDepositAdjustmentDto adjustmentContributionDepositDto = new RegularAdjustmentContributionDepositAdjustmentDto();
			adjustmentContributionDepositDto.setAdjustmentContributiondepositId(policyDepositEntity2.getDepositId());
			adjustmentContributionDepositDto
					.setAdjustmentContributionAdjestmentStatus(policyDepositEntity2.getStatus());
			adjustmentContributionDepositDto
					.setAdjustmentContributionId(policyDepositEntity2.getAdjustmentContributionId());
			adjustmentContributionDepositDto.setRegularContributionId(policyDepositEntity2.getRegularContributionId());
			adjustmentContributionDepositDto.setPolicyId(policyDepositEntity2.getPolicyId());
			adjustmentContributionDepositDto.setChallanNo(policyDepositEntity2.getChallanNo());
			adjustmentContributionDepositDto.setCollectionNo(policyDepositEntity2.getCollectionNo());
			adjustmentContributionDepositDto.setCollectionDate(policyDepositEntity2.getCollectionDate());
			adjustmentContributionDepositDto.setAmount(policyDepositEntity2.getDepositAmount());
			adjustmentContributionDepositDto.setAdjustmentAmount(policyDepositEntity2.getAdjustmentAmount());
			adjustmentContributionDepositDto.setAvailableAmount(policyDepositEntity2.getAvailableAmount());
			adjustmentContributionDepositDto.setTransactionMode(policyDepositEntity2.getTransactionMode());
			adjustmentContributionDepositDto.setCollectionStatus(policyDepositEntity2.getCollectionStatus());
			adjustmentContributionDepositDto.setChequeRealisationDate(policyDepositEntity2.getChequeRealisationDate());
			adjustmentContributionDepositDto.setRemark(policyDepositEntity2.getRemark());
			adjustmentContributionDepositDto.setZeroId(policyDepositEntity2.getZeroId());
			adjustmentContributionDepositDto.setModifiedBy(policyDepositEntity2.getModifiedBy());
			adjustmentContributionDepositDto.setModifiedOn(policyDepositEntity2.getModifiedOn());
			adjustmentContributionDepositDto.setCreatedBy(policyDepositEntity2.getCreatedBy());
			adjustmentContributionDepositDto.setCreatedOn(policyDepositEntity2.getCreatedOn());
			adjustmentContributionDepositDto.setIsActive(policyDepositEntity2.getIsActive());
			adjustmentContributionDepositDtos.add(adjustmentContributionDepositDto);
		}
		logger.info("RegularadjustmentServiceImpl :: getAdjustementFromPolicyDeposit :: Ends");
		return adjustmentContributionDepositDtos;
	}	
	
	private RegularAdjustmentContributionResponseDto getFinalResponse(
			RegularAdjustmentContributionAllDto responseAdjustmentContributionAllDto,
			RegularAdjustmentContributionTempEntity adjustementTempEntity) {
		logger.info("RegularadjustmentServiceImpl :: getFinalResponse :: Starts");
		RegularAdjustmentContributionResponseDto response = new RegularAdjustmentContributionResponseDto();
		response.setResponseData(responseAdjustmentContributionAllDto);
		response.setTotalDeposit(adjustementTempEntity.getTotalDeposit());
		response.setIsCommencementdateOneYr(adjustementTempEntity.getIsCommencementdateOneYr());
		response.setBank(getPolicyBankList(adjustementTempEntity.getPolicyId()));
		response.setTransactionMessage(PolicyConstants.ADJUSTEDMESSAGE);
		response.setTransactionStatus(PolicyConstants.SUCCESS);
		response.setZeroRow(responseAdjustmentContributionAllDto.getZeroRow());
		logger.info("RegularadjustmentServiceImpl :: getFinalResponse :: Ends");
		return response;
	}

	private RegularAdjustmentContributionAllDto getResponseAdjustmentContributionAllDto(
			RegularAdjustmentContributionTempEntity adjustementTempEntity, Long adjustmentContributionId,
			Long policyId) {

		logger.info("RegularadjustmentServiceImpl :: getResponseAdjustmentContributionAllDto :: Start");

		RegularAdjustmentContributionAllDto adjustmentContributionAllDto = new RegularAdjustmentContributionAllDto();
		adjustmentContributionAllDto.setRegAdjustmentContribution(
				modelMapper.map(adjustementTempEntity, RegularAdjustmentContributionDto.class));
		adjustmentContributionAllDto.setRegularContributionId(
				adjustmentContributionAllDto.getRegAdjustmentContribution().getRegularContributionId());

		logger.info("RegularadjustmentServiceImpl :: getResponseAdjustmentContributionAllDto :: getDepositFromPolicyDeposit :: Start");
		adjustmentContributionAllDto.setRegAdjustmentContributDepositDtos(getDepositFromPolicyDeposit(adjustmentContributionId, policyId));
		logger.info("RegularadjustmentServiceImpl :: getResponseAdjustmentContributionAllDto :: getDepositFromPolicyDeposit :: Ends");

		logger.info("RegularadjustmentServiceImpl :: getResponseAdjustmentContributionAllDto :: getAdjustementFromPolicyDeposit :: Start");
		adjustmentContributionAllDto.setRegAdjustmentContributionDepositAdjustmentDtos(
				getAdjustementFromPolicyDeposit(adjustmentContributionId, policyId));
		logger.info("RegularadjustmentServiceImpl :: getResponseAdjustmentContributionAllDto ::  getAdjustementFromPolicyDeposit :: Ends");

		logger.info("RegularadjustmentServiceImpl :: getResponseAdjustmentContributionAllDto :: getZeroAccountFromPolicy :: Start");
		adjustmentContributionAllDto.setZeroRow(getZeroAccountFromPolicy(adjustementTempEntity.getPolicyId()));
		logger.info("RegularadjustmentServiceImpl :: getResponseAdjustmentContributionAllDto :: getZeroAccountFromPolicy :: Ends");

		logger.info("RegularadjustmentServiceImpl :: getResponseAdjustmentContributionAllDto :: getPolicyBankList :: Start");
		adjustmentContributionAllDto.setBank(getPolicyBankList(adjustementTempEntity.getPolicyId()));
		logger.info("RegularadjustmentServiceImpl :: getResponseAdjustmentContributionAllDto :: getPolicyBankList :: Ends");

		logger.info("RegularadjustmentServiceImpl :: getResponseAdjustmentContributionAllDto :: Ends");
		return adjustmentContributionAllDto;
	}

	private Object getZeroAccountFromPolicy(Long policyId) {
		return zeroAccountRepository.findByPolicyIdAndIsActiveTrue(policyId);
	}	
	
	private void calculatePolicyContribution(List<PolicyContributionTempEntity> policyContributionOpt,
			RegularAdjustmentContributionTempEntity adjustementTempEntity, String contributionType,
			PolicyDepositTempEntity depositEntity, BigDecimal availableDepositAmt, BigDecimal newTotalDepositAmt,
			BigDecimal totalContribution, String role, BigDecimal amountToBeAdjusted, BigDecimal newDepositAmount,
			Date adjustmentDueDate,Date effectiveDate) {
		logger.info("RegularadjustmentServiceImpl :: calculatePolicyContribution :: Start");
		for (PolicyContributionTempEntity policyContribution : policyContributionOpt) {
			BigDecimal openingBalance = policyContribution.getClosingBalance();
			policyContribution.setContributionId(null);
			policyContribution.setPolicyId(adjustementTempEntity.getTempPolicyId());
			policyContribution.setMasterPolicyId(adjustementTempEntity.getPolicyId());
			policyContribution.setRegularContributionId(adjustementTempEntity.getRegularContributionId());
			policyContribution.setAdjustmentContributionId(null);
			policyContribution.setContributionType(contributionType);
			policyContribution.setContributionDate(new Date());
			policyContribution.setContReferenceNo(depositEntity.getChallanNo());
			policyContribution.setFinancialYear(DateUtils.getFinancialYrByDt(DateUtils.sysDate()));
			policyContribution.setVersionNo(policyContribution.getVersionNo() + 1);
			policyContribution.setOpeningBalance(openingBalance);
			policyContribution.setEmployerContribution(adjustementTempEntity.getEmployerContribution());
			policyContribution.setEmployeeContribution(adjustementTempEntity.getEmployeeContribution());
			policyContribution.setVoluntaryContribution(adjustementTempEntity.getVoluntaryContribution());
			policyContribution.setTotalContribution(totalContribution);
			policyContribution.setClosingBalance(openingBalance.add(totalContribution));
			policyContribution.setIsDeposit(Boolean.TRUE);
			policyContribution.setIsActive(Boolean.TRUE);
			policyContribution.setCreatedBy(role);
			policyContribution.setCreatedOn(new Date());
			policyContribution.setModifiedBy(role);
			policyContribution.setModifiedOn(new Date());
			policyContribution.setTxnEntryStatus(Boolean.FALSE);
			policyContribution.setPolicyContribution(null);
			policyContribution.setAdjustmentDueDate(adjustmentDueDate);
			policyContribution.setEffectiveDate(effectiveDate);
			policyContribution = policyContributionTempRepository.save(policyContribution);

			Set<MemberContributionTempEntity> memberContributionList = new HashSet<>();
			Set<MemberContributionTempEntity> templist = memberContributionTempRepository
					.findByPolicyIdAndRegularContributionIdAndIsActiveTrue(adjustementTempEntity.getTempPolicyId(),
							adjustementTempEntity.getRegularContributionId());
			for (MemberContributionTempEntity memberContribution : templist) {
				MemberContributionTempEntity memberContribution2 = new MemberContributionTempEntity();
				memberContribution2.setMemberConId(memberContribution.getMemberConId());
				memberContribution2.setPolicyConId(policyContribution.getContributionId());
				memberContribution2.setBatchId(memberContribution.getBatchId());
				memberContribution2.setPolicyId(adjustementTempEntity.getTempPolicyId());
				memberContribution2.setMemberId(memberContribution.getMemberId());
				memberContribution2.setMasterPolicyId(adjustementTempEntity.getPolicyId());
				memberContribution2.setMasterMemberId(memberContribution.getMasterMemberId());
				memberContribution2.setRegularContributionId(adjustementTempEntity.getRegularContributionId());
				memberContribution2.setAdjustmentContributionId(memberContribution.getAdjustmentContributionId());
				memberContribution2.setContributionType(contributionType);
				memberContribution2.setContributionDate(new Date());
				memberContribution2.setFinancialYear(DateUtils.getFinancialYrByDt(DateUtils.sysDate()));
				memberContribution2.setVersionNo(memberContribution.getVersionNo());
				memberContribution2.setLicId(memberContribution.getLicId());
				memberContribution2.setOpeningBalance(memberContribution.getOpeningBalance());
				memberContribution2.setEmployeeContribution(memberContribution.getEmployeeContribution());
				memberContribution2.setEmployerContribution(memberContribution.getEmployerContribution());
				memberContribution2.setVoluntaryContribution(memberContribution.getVoluntaryContribution());
				memberContribution2.setTotalContribution(memberContribution.getTotalContribution());
				memberContribution2.setClosingBalance(memberContribution.getClosingBalance());
				memberContribution2.setTotalInterestedAccured(memberContribution.getTotalInterestedAccured());
				memberContribution2.setIsDeposit(Boolean.TRUE);
				memberContribution2.setIsActive(Boolean.TRUE);
				memberContribution2.setCreatedBy(memberContribution.getCreatedBy());
				memberContribution2.setCreatedOn(memberContribution.getCreatedOn());
				memberContribution2.setModifiedBy(role);
				memberContribution2.setModifiedOn(new Date());
				memberContribution2.setTxnEntryStatus(Boolean.FALSE);
				memberContribution2.setAdjustmentDueDate(adjustmentDueDate);
				memberContribution2.setEffectiveDate(effectiveDate);
				memberContributionList.add(memberContribution2);
			}
			policyContribution.setPolicyContribution(memberContributionList);
			policyContributionTempRepository.save(policyContribution);
			break;
		}
//Deposit Change 				
		depositEntity.setStatus(PolicyConstants.ADJESTED);
		depositEntity.setAdjustmentAmount(amountToBeAdjusted);
		depositEntity.setAvailableAmount(newDepositAmount.subtract(depositEntity.getAdjustmentAmount()));
		depositEntity.setIsDeposit(Boolean.TRUE);
		policyDepositTempRepository.save(depositEntity);
//update total contribution
		adjustementTempEntity.setTotalDeposit(availableDepositAmt.add(depositEntity.getAdjustmentAmount()));
		adjustementTempEntity.setAdjustmentDueDate(adjustmentDueDate);
		adjustementTempEntity.setEffectiveDate(effectiveDate);
		adjustementTempEntity.setAdjustmentForDate(new Date());
		adjTempRepo.save(adjustementTempEntity);

		logger.info("RegularadjustmentServiceImpl :: calculatePolicyContribution :: Ends");
	}	
	
	private List<PolicyContributionTempEntity> checkPolicycontributionPreviouse(Long tempPolicyId, Long policyId,
			Long regularContributionId, String contributionType, String role) {
		logger.info("RegularadjustmentServiceImpl :: checkPolicycontributionPreviouse :: Start");
		List<PolicyContributionTempEntity> policyContributionOpt = policyContributionTempRepository
				.findByPolicyIdAndIsActiveTrueOrderByVersionNoDesc(tempPolicyId);
		if (policyContributionOpt.isEmpty()) {
			PolicyContributionTempEntity policycontributionNew = new PolicyContributionTempEntity();
			policycontributionNew.setContributionId(null);
			policycontributionNew.setPolicyId(tempPolicyId);
			policycontributionNew.setMasterPolicyId(policyId);
//			policycontributionNew.setAdjustmentContributionId(regularContributionId);
			policycontributionNew.setRegularContributionId(regularContributionId);
			policycontributionNew.setContributionType(contributionType);
			policycontributionNew.setContributionDate(null);
			policycontributionNew.setContReferenceNo(null);
			policycontributionNew.setFinancialYear(DateUtils.getFinancialYrByDt(DateUtils.sysDate()));
			policycontributionNew.setVersionNo(1);
			policycontributionNew.setOpeningBalance(BigDecimal.ZERO);
			policycontributionNew.setEmployerContribution(BigDecimal.ZERO);
			policycontributionNew.setEmployeeContribution(BigDecimal.ZERO);
			policycontributionNew.setVoluntaryContribution(BigDecimal.ZERO);
			policycontributionNew.setTotalContribution(BigDecimal.ZERO);
			policycontributionNew.setClosingBalance(BigDecimal.ZERO);
			policycontributionNew.setIsDeposit(Boolean.TRUE);
			policycontributionNew.setIsActive(Boolean.TRUE);
			policycontributionNew.setCreatedBy(role);
			policycontributionNew.setCreatedOn(new Date());
			policycontributionNew.setModifiedBy(role);
			policycontributionNew.setModifiedOn(new Date());
			policycontributionNew.setTxnEntryStatus(Boolean.FALSE);
			policycontributionNew.setPolicyContribution(null);
			policyContributionTempRepository.save(policycontributionNew);
		}
		policyContributionOpt = policyContributionTempRepository
				.findByPolicyIdAndIsActiveTrueOrderByVersionNoDesc(tempPolicyId);
		logger.info("RegularadjustmentServiceImpl :: checkPolicycontributionPreviouse :: Ends");
		return policyContributionOpt;
	}
	
	
	
	
	
	

	@Override
	public RegularAdjustmentContributionBatchHistoryResponse getBatchHistory(Long regularContributionId) {
		RegularAdjustmentContributionBatchHistoryResponse response = new RegularAdjustmentContributionBatchHistoryResponse();

		List<RegularAdjustmentContributionBulkResponseDto> responseList = new ArrayList<>();
		
		
		List<RegularAdjustmentContributionBatchEntity> batches = regularAdjustmentContributionBatchRepository
				.findAllByRegularContributionIdAndIsActiveTrue(regularContributionId);
		
		if(!batches.isEmpty()) {
			
			for(RegularAdjustmentContributionBatchEntity batch : batches) {
				
				RegularAdjustmentContributionBulkResponseDto dto = new RegularAdjustmentContributionBulkResponseDto();
				dto.setBatchId(batch.getBatchId());
				dto.setSuccessCount(batch.getSuccessCount());
				dto.setFailedCount(batch.getFailedCount());
				dto.setTotalCount(batch.getTotalCount());
				dto.setFileName(batch.getFileName());
				dto.setRegularContributionId(batch.getRegularContributionId());
				
				List<RegularAdjustmentContributionBulkErrorEntity> errorList = regularAdjustmentContributionBulkErrorRepository.findAllByBatchIdAndIsActiveTrue(batch.getBatchId());
				
				if(!errorList.isEmpty()) {
					dto.setError(mapList(errorList, RegularAdjustmentContributionBulkErrorDto.class));
				}
				
				responseList.add(dto);
				
			}
			
			response.setTransactionStatus(CommonConstants.STATUS);
			response.setTransactionMessage(CommonConstants.FETCH);
			response.setBatchHistory(responseList);
			
		}else {
			response.setTransactionStatus(CommonConstants.STATUS);
			response.setTransactionMessage("Batches Not Found");
		}
		
		
		return response;
	}

	
	@Override
	public RegularAdjustmentContributionResponseDto makerReject(Long regularContributionId, String modifiedBy,
			String rejectionRemarks) {
		logger.info("RegularadjustmentServiceImpl :: makerReject :: Ends");
		RegularAdjustmentContributionResponseDto responseDto = new RegularAdjustmentContributionResponseDto();
		try {
			RegularAdjustmentContributionTempEntity regAdjConTempEntity = adjTempRepo
					.findByRegularContributionIdAndIsActiveTrue(regularContributionId);
			if (regAdjConTempEntity != null) {
				regAdjConTempEntity.setModifiedBy(modifiedBy);
				regAdjConTempEntity.setModifiedOn(DateUtils.sysDate());
				regAdjConTempEntity.setRegularContributionStatus(AdjustmentContibutionConstants.MAKER_REJECT);
				regAdjConTempEntity.setRejectionRemarks(rejectionRemarks);
				regAdjConTempEntity.setIsActive(false);
				RegularAdjustmentContributionTempEntity saveEntity = adjTempRepo.save(regAdjConTempEntity);
				RegularAdjustmentContributionDto dto = modelMapper.map(saveEntity,
						RegularAdjustmentContributionDto.class);
				responseDto.setResponseData(dto);
				responseDto.setTransactionStatus(CommonConstants.SUCCESS);
				responseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			} else {
				responseDto.setTransactionStatus(CommonConstants.ERROR);
				responseDto.setTransactionMessage(CommonConstants.FAIL);
			}
		}
		catch (IllegalArgumentException e) {
			logger.error("Exception :: RegularadjustmentServiceImpl :: makerReject ", e);
			responseDto.setTransactionStatus(CommonConstants.ERROR);
			responseDto.setTransactionMessage(CommonConstants.FAIL);
		}
		logger.info("RegularadjustmentServiceImpl :: makerReject :: Ends");
		return responseDto;
	}

	
	
//	private void checkZeroAccountInMember(Long policyId, BigDecimal zeroIdAmount, String role,
//			Long policyContributionId, Long memberContibutionId, String type) {
//		logger.info("RegularadjustmentServiceImpl :: checkZeroAccountInMember :: Start");
//		Optional<ZeroAccountEntity> zeroAccountsOpt = zeroAccountRepository.findByIsActiveTrueAndPolicyId(policyId);
//		if (!zeroAccountsOpt.isPresent()) {
//			ZeroAccountEntity zeroMember = convertSetZeroAcc(policyId, zeroIdAmount, role);
//			zeroAccountRepository.save(zeroMember);
//			ZeroAccountEntriesEntity zeroMemberEntries = convertSetZeroAccEntries(policyId, zeroIdAmount, role,
//					policyContributionId, memberContibutionId, type);
//			zeroAccountEntriesRepository.save(zeroMemberEntries);
//		} else {
//			logger.info("RegularadjustmentServiceImpl :: checkZeroAccountInMember :: Else :: Ends");
//		}
//		logger.info("RegularadjustmentServiceImpl :: checkZeroAccountInMember :: Ends");
//	}

//	private ZeroAccountEntriesEntity convertSetZeroAccEntries(Long policyId, BigDecimal zeroIdAmount, String role,
//			Long policyContributionId, Long memberContibutionId, String type) {
//		logger.info("RegularadjustmentServiceImpl :: convertSetZeroAccEntries :: Start");
//		ZeroAccountEntriesEntity response = new ZeroAccountEntriesEntity();
//		response.setZeroAccEntId(null);
//		response.setPolicyId(policyId);
//		response.setPolicyConId(policyContributionId);
//		response.setMemberConId(memberContibutionId);
//		response.setZeroIdAmount(zeroIdAmount);
//		response.setTransactionType(type);
//		response.setTransactionDate(new Date());
//		response.setCreatedOn(new Date());
//		response.setCreatedBy(role);
//		response.setModifiedOn(new Date());
//		response.setModifiedBy(role);
//		response.setIsActive(Boolean.TRUE);
//		logger.info("RegularadjustmentServiceImpl :: convertSetZeroAccEntries :: Ends");
//		return response;
//	}

//	private ZeroAccountEntity convertSetZeroAcc(Long policyId, BigDecimal zeroIdAmount, String role) {
//		logger.info("RegularadjustmentServiceImpl :: convertSetZeroAcc :: Start");
//		ZeroAccountEntity response = new ZeroAccountEntity();
//		response.setZeroAccId(null);
//		response.setZeroIdAmount(zeroIdAmount);
//		response.setPolicyId(policyId);
//		response.setCreatedBy(role);
//		response.setCreatedOn(new Date());
//		response.setModifiedBy(role);
//		response.setModifiedOn(new Date());
//		response.setIsActive(Boolean.TRUE);
//		logger.info("RegularadjustmentServiceImpl :: convertSetZeroAcc :: Ends");
//		return response;
//	}

//	private void checkZeroAccountTempInMember(Long policyId, BigDecimal zeroIdAmount, String role,
//			Long policyContributionId, Long memberContibutionId, String type) {
//		logger.info("RegularadjustmentServiceImpl :: checkZeroAccountTempInMember :: Start");
//		Optional<ZeroAccountTempEntity> zeroAccountsOpt = zeroAccountTempRepository
//				.findByIsActiveTrueAndPolicyId(policyId);
//		if (!zeroAccountsOpt.isPresent()) {
//			logger.info(
//					"RegularadjustmentServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccTemp :: Start");
//			ZeroAccountTempEntity zeroMember = convertSetZeroAccTemp(policyId, zeroIdAmount, role);
//			logger.info(
//					"RegularadjustmentServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccTemp :: Ends");
//			zeroAccountTempRepository.save(zeroMember);
//
//			logger.info(
//					"RegularadjustmentServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccEntriesTemp :: Start");
//			ZeroAccountEntriesTempEntity zeroMemberEntries = convertSetZeroAccEntriesTemp(policyId, zeroIdAmount, role,
//					policyContributionId, memberContibutionId, type);
//			logger.info(
//					"RegularadjustmentServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccEntriesTemp :: Ends");
//			zeroAccountEntriesTempRepository.save(zeroMemberEntries);
//		} else {
//			ZeroAccountTempEntity zeroMembertemp = zeroAccountsOpt.get();
//			logger.info(
//					"RegularadjustmentServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccTempExist :: Start");
//			ZeroAccountTempEntity zeroMember = convertSetZeroAccTempExist(zeroMembertemp, policyId, zeroIdAmount, role);
//			logger.info(
//					"RegularadjustmentServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccTempExist :: Ends");
//			zeroAccountTempRepository.save(zeroMember);
//
//			logger.info(
//					"RegularadjustmentServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccEntriesTemp :: Start");
//			ZeroAccountEntriesTempEntity zeroMemberEntries = convertSetZeroAccEntriesTemp(policyId, zeroIdAmount, role,
//					policyContributionId, memberContibutionId, type);
//			logger.info(
//					"RegularadjustmentServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccEntriesTemp :: Ends");
//			zeroAccountEntriesTempRepository.save(zeroMemberEntries);
//		}
//		logger.info("RegularadjustmentServiceImpl :: checkZeroAccountTempInMember :: Ends");
//	}

//	private ZeroAccountTempEntity convertSetZeroAccTempExist(ZeroAccountTempEntity response, Long policyId,
//			BigDecimal zeroIdAmount, String role) {
//		logger.info("RegularadjustmentServiceImpl :: convertSetZeroAccTempExist :: Start");
//		BigDecimal zeroIdAmountNew = response.getZeroIdAmount().add(zeroIdAmount);
//
//		response.setZeroAccId(response.getZeroAccId());
//		response.setZeroIdAmount(zeroIdAmountNew);
//		response.setPolicyId(policyId);
//		response.setModifiedBy(role);
//		response.setModifiedOn(new Date());
//		response.setIsActive(Boolean.TRUE);
//		logger.info("RegularadjustmentServiceImpl :: convertSetZeroAccTempExist :: Ends");
//
//		return response;
//	}

//	private ZeroAccountEntriesTempEntity convertSetZeroAccEntriesTemp(Long policyId, BigDecimal zeroIdAmount,
//			String role, Long policyContributionId, Long memberContibutionId, String type) {
//		logger.info("RegularadjustmentServiceImpl :: convertSetZeroAccEntriesTemp :: Start");
//		ZeroAccountEntriesTempEntity response = new ZeroAccountEntriesTempEntity();
//		response.setZeroAccEntId(null);
//		response.setPolicyId(policyId);
//		response.setPolicyConId(policyContributionId);
//		response.setMemberConId(memberContibutionId);
//		response.setZeroIdAmount(zeroIdAmount);
//		response.setTransactionType(type);
//		response.setTransactionDate(new Date());
//		response.setCreatedOn(new Date());
//		response.setCreatedBy(role);
//		response.setModifiedOn(new Date());
//		response.setModifiedBy(role);
//		response.setIsActive(Boolean.TRUE);
//		logger.info("RegularadjustmentServiceImpl :: convertSetZeroAccEntriesTemp :: Ends");
//		return response;
//	}

//	private ZeroAccountTempEntity convertSetZeroAccTemp(Long policyId, BigDecimal zeroIdAmount, String role) {
//		logger.info("RegularadjustmentServiceImpl :: convertSetZeroAccTemp :: Start");
//		ZeroAccountTempEntity response = new ZeroAccountTempEntity();
//		BigDecimal zeroIdAmountNew = response.getZeroIdAmount().add(zeroIdAmount);
//		response.setZeroAccId(null);
//		response.setZeroIdAmount(zeroIdAmountNew);
//		response.setPolicyId(policyId);
//		response.setModifiedBy(role);
//		response.setModifiedOn(new Date());
//		response.setIsActive(Boolean.TRUE);
//		logger.info("RegularadjustmentServiceImpl :: convertSetZeroAccTemp :: Ends");
//		return response;
//	}

//	public void saveContributionToTxnEntries(PolicyMasterEntity dto) {
//		FundRequestDto requestDto = new FundRequestDto();
//		requestDto.setPolicyId(dto.getPolicyId());
//		try {
//			fundRestApiService.contirbutionToTransEntries(requestDto);
//		} catch (Exception e) {
//			logger.error("saveContributionToTxnEntries::", e);
//		}
//	}

}