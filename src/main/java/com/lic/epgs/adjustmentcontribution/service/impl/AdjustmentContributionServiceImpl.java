package com.lic.epgs.adjustmentcontribution.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author pradeepramesh
 *
 */

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.lic.epgs.adjustmentcontribution.constants.AdjustmentContibutionConstants;
import com.lic.epgs.adjustmentcontribution.dto.ACPolicyDepositAdjustmentDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionAllDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionDepositAdjustmentDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionDepositDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionNotesDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionResponseDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentSearchDto;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionBatchEntity;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionEntity;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionNotesTempEntity;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionTempEntity;
import com.lic.epgs.adjustmentcontribution.repository.AdjustmentContributionBatchRepository;
import com.lic.epgs.adjustmentcontribution.repository.AdjustmentContributionNotesTempRepository;
import com.lic.epgs.adjustmentcontribution.repository.AdjustmentContributionRepository;
import com.lic.epgs.adjustmentcontribution.repository.AdjustmentContributionTempRepository;
import com.lic.epgs.adjustmentcontribution.service.AdjustmentContributionService;
import com.lic.epgs.common.dto.CommonResponseDto;
import com.lic.epgs.common.integration.dto.CommonIntegrationDto;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.common.service.impl.CommonServiceImpl;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.policy.dto.MphMasterDto;
import com.lic.epgs.policy.dto.PolicyDepositDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.dto.PolicySearchResponseDto;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MemberMasterTempEntity;
import com.lic.epgs.policy.entity.MphBankEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.PolicyDepositEntity;
import com.lic.epgs.policy.entity.PolicyDepositTempEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntriesEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntriesTempEntity;
import com.lic.epgs.policy.entity.ZeroAccountTempEntity;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentOldDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentResponse;
import com.lic.epgs.policy.old.dto.PolicyDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.MemberMasterTempRepository;
import com.lic.epgs.policy.repository.MphBankRepository;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.MphMasterTempRepository;
import com.lic.epgs.policy.repository.PolicyContributionRepository;
import com.lic.epgs.policy.repository.PolicyContributionTempRepository;
import com.lic.epgs.policy.repository.PolicyDepositRepository;
import com.lic.epgs.policy.repository.PolicyDepositTempRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.policy.repository.PolicyMasterTempRepository;
import com.lic.epgs.policy.repository.ZeroAccountEntriesRepository;
import com.lic.epgs.policy.repository.ZeroAccountEntriesTempRepository;
import com.lic.epgs.policy.repository.ZeroAccountRepository;
import com.lic.epgs.policy.repository.ZeroAccountTempRepository;
import com.lic.epgs.policy.service.impl.PolicyCommonServiceImpl;
import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionTempEntity;
import com.lic.epgs.regularadjustmentcontribution.repository.RegularAdjustmentContributionTempRepository;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionBatchHistoryResponse;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionBulkErrorDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionBulkResponseDto;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionBulkErrorEntity;
import com.lic.epgs.adjustmentcontribution.repository.AdjustmentContributionBulkErrorRepository;

@Service
public class AdjustmentContributionServiceImpl implements AdjustmentContributionService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CommonService commonSequenceService;

	@Autowired
	private AdjustmentContributionCommonServiceImpl adjustmentContributionCommonServiceImpl;

	@Autowired
	private PolicyCommonServiceImpl policyCommonServiceImpl;

	@Autowired
	private AdjustmentContributionRepository adjRepo;

	@Autowired
	private AdjustmentContributionNotesTempRepository adjNotesTempRepo;

	@Autowired
	private PolicyDepositTempRepository policyDepositTempRepository;

	@Autowired
	private PolicyDepositRepository policyDepositRepository;

	@Autowired
	private MphMasterRepository mphMasterRepository;

	@Autowired
	private MphBankRepository mphBankRepository;

	@Autowired
	private PolicyMasterRepository policyMasterRepository;

	@Autowired
	private PolicyMasterTempRepository policyMasterTempRepository;

	@Autowired
	private PolicyContributionTempRepository policyContributionTempRepository;

	@Autowired
	private ZeroAccountEntriesRepository zeroAccountEntriesRepository;

	@Autowired
	private ZeroAccountRepository zeroAccountRepository;

	@Autowired
	private MemberMasterRepository memberMasterRepository;

	@Autowired
	private MemberMasterTempRepository memberMasterTempRepository;

	@Autowired
	private AdjustmentContributionTempRepository adjustmentContributionTempRepository;

	@Autowired
	private RegularAdjustmentContributionTempRepository regularAdjustmentContributionTempRepository;

	@Autowired
	private PolicyContributionRepository policyContributionRepository;

	@Autowired
	private MphMasterTempRepository mphMasterTempRepository;

	@Autowired
	private AdjustmentContributionBatchRepository adjustmentContributionBatchRepository;

	@Autowired
	private CommonServiceImpl commonServiceImpl;
	

	@Autowired
	ZeroAccountTempRepository zeroAccountTempRepository;

	@Autowired
	ZeroAccountEntriesTempRepository zeroAccountEntriesTempRepository;
	
	@Autowired
	private AdjustmentContributionBulkErrorRepository ajustmentContributionBulkErrorRepository;

	public synchronized String getAdjNumber() {
		return commonSequenceService.getSequence(CommonConstants.ADJ_NUMBER_SEQ);
	}

	public synchronized String getChallanSeq() {
		return commonSequenceService.getSequence(CommonConstants.CHALLAN_SEQ);
	}

	@Override
	public PolicyResponseDto newcitrieaSearch(PolicySearchDto policySearchDto) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearch :: Start");
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
			predicates.add(join.get(PolicyConstants.POLICYSTATUS).in(Arrays.asList(PolicyConstants.APPROVED_NO, PolicyConstants.REJECTED_NO)));
			createQuery.orderBy(criteriaBuilder.desc(root.get("modifiedOn")));

			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			List<MphMasterEntity> result = entityManager.createQuery(createQuery).getResultList();
			List<PolicySearchResponseDto> response = result.stream().map(this::convertMasterEntityToDto).collect(Collectors.toList());
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
			logger.error("Exception :: AdjustmentContributionServiceImpl :: newcitrieaSearch", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearch :: Ends");
		}
		return commonDto;
	}

	private PolicySearchResponseDto convertMasterEntityToDto(MphMasterEntity mphMasterEntity) {
		logger.info("AdjustmentContributionServiceImpl :: convertMasterEntityToDto :: Start");
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
		logger.info("AdjustmentContributionServiceImpl :: convertMasterEntityToDto :: Ends");

		return policySearchResponseDto;
	}

	@Override
	public PolicyResponseDto newcitrieaSearchPradeep(PolicySearchDto policySearchDto) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		List<PolicyDto> policyDtoList = new ArrayList<>();
		try {
			logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearchPradeep :: Start");

			String policyNumber = "";
			String unitCode = "";
			Boolean isActive = Boolean.TRUE;
			if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())) {
				policyNumber = policySearchDto.getPolicyNumber();
			}
			if (StringUtils.isNotBlank(policySearchDto.getUnitCode())) {
				unitCode = policySearchDto.getUnitCode();
			}
			List<Object> result = policyMasterRepository.policySearchPradeepForSusequentSearch(policyNumber, unitCode,isActive);
			Object object2 = result.get(result.size()-1);
			Object[] obj=(Object[]) object2;
			String lastPremiumDueDate = policyContributionRepository.findPolicyId(Long.valueOf(String.valueOf(obj[3])));
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
					resonseDto.setLastPremiumDueDate(lastPremiumDueDate);
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
			logger.error("Exception :: AdjustmentContributionServiceImpl :: newcitrieaSearchPradeep", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearchPradeep :: Ends");
		}
		return commonDto;
	}

	@Override
	public AdjustmentContributionResponseDto newcitrieaSearchById(Long mphId, Long policyId) {
		AdjustmentContributionResponseDto commonDto = new AdjustmentContributionResponseDto();
		try {
			logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearchById :: Start");
			Boolean isActive = Boolean.TRUE;
			List<Object> result = policyMasterRepository.getPolicyDetailsByMphID(mphId, policyId, isActive);
			String lastPremiumDueDate = policyContributionRepository.findPolicyId(policyId);
			PolicyDto resonseDto = new PolicyDto();
			if (result != null && !result.isEmpty()) {
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
					resonseDto.setPolicyId(NumericUtils.convertStringToLong(String.valueOf(ob[6])));
					resonseDto.setTempMphId(NumericUtils.convertStringToLong(String.valueOf(ob[4])));
					resonseDto.setTempPolicyId(NumericUtils.convertStringToLong(String.valueOf(ob[5])));
				
				}
				String policyNumber = resonseDto.getPolicyNumber();
				AdjustmentContributionTempEntity adjustmentContributionTempEntity = adjustmentContributionTempRepository
						.findByPolicyNumberAndAdjustmentContributionStatusInAndIsActiveTrue(policyNumber,
								CommonConstants.adjustmentCheckStatus());

				RegularAdjustmentContributionTempEntity regularAdjustmentContributionTempEntity = regularAdjustmentContributionTempRepository
						.findByPolicyNumberAndRegularContributionStatusInAndIsActiveTrue(policyNumber,
								CommonConstants.adjustmentCheckStatus());

				if (adjustmentContributionTempEntity == null && regularAdjustmentContributionTempEntity == null) {
					
					logger.info("AdjustmentContributionServiceImpl :: saveMphTemp :: Start");
					saveMphTemp(resonseDto.getMphId());
					logger.info("AdjustmentContributionServiceImpl :: saveMphTemp :: Ends");

					logger.info("AdjustmentContributionServiceImpl :: savePolicyTemp :: Start");
					savePolicyTemp(resonseDto.getPolicyId(), resonseDto.getMphId());
					logger.info("AdjustmentContributionServiceImpl :: savePolicyTemp :: Ends");
					
					
					Object idss = policyMasterRepository.getIdsofPolicy(resonseDto.getPolicyId());
					Object[] ob = (Object[]) idss;
					commonDto.setPolicyId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[0])));
					commonDto.setTempPolicyId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[1])));
					commonDto.setMphId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[2])));
					commonDto.setTempMphId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[3])));

					resonseDto.setPolicyId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[0])));
					resonseDto.setTempPolicyId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[1])));
					resonseDto.setMphId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[2])));
					resonseDto.setTempMphId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[3])));

					commonDto.setResponseData(resonseDto);
	
					logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearchPradeep :: calculateIsOneYrPolicy :: Start");
					commonDto.setIsCommencementdateOneYr(calculateIsOneYrPolicy(resonseDto.getPolicyCommencementDate()));
					logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearchPradeep :: calculateIsOneYrPolicy :: Ends");
					logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearchPradeep :: checkZeroRow :: Start");
					commonDto.setZeroRow(checkZeroRow(resonseDto.getPolicyId()));
					logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearchPradeep :: checkZeroRow :: Ends");
					logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearchPradeep :: getPolicyBankList :: Start");
					commonDto.setBank(getPolicyBankList(resonseDto.getPolicyId()));
					logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearchPradeep :: getPolicyBankList :: Ends");										
					commonDto.setTransactionMessage(PolicyConstants.FETCH);
					commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				} else {
					commonDto.setTransactionMessage(CommonConstants.RECORD_ALREADY_USED_IN_ADJUSTMENTS);
					commonDto.setTransactionStatus(CommonConstants.FAIL);
				}
			} else {
				commonDto.setTransactionMessage(CommonConstants.DENY);
				commonDto.setTransactionStatus(CommonConstants.FAIL);

			}
		} catch (Exception e) {
			logger.error("Exception :: AdjustmentContributionServiceImpl :: newcitrieaSearchById", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearchById :: Ends");
		return commonDto;
	}

	/***
	 * @author Muruganandam
	 * @implNote Insert the MPH details into temp tbl
	 */

	public int saveMphTemp(Long mphId) {
		try {
			logger.info("AdjustmentContributionServiceImpl:saveMphTemp:Start::{}", mphId);
			Object mphTempEntity = mphMasterTempRepository.findMasterMphIdbyMphId(mphId);
			if (mphTempEntity == null) {
				Object mphEntity = mphMasterRepository.findMphDetails(mphId);
				Object[] ob = (Object[]) mphEntity;
				return jdbcTemplate.update(
						"Insert into MPH_MASTER_TEMP (MPH_ID,ALTERNATE_PAN,CIN,COUNTRY_CODE,CREATED_BY,CREATED_ON,EMAIL_ID,FAX,IS_ACTIVE,LANDLINE_NO,MASTER_MPH_ID,MOBILE_NO,MODIFIED_BY,MODIFIED_ON,MPH_CODE,MPH_NAME,MPH_TYPE,PAN,PROPOSAL_ID,PROPOSAL_NUMBER) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						mphMasterTempRepository.getMphTempSeq(), ob[1], ob[2], ob[3], ob[4], ob[5], ob[6], ob[7], ob[8],
						ob[9], ob[0], ob[10], ob[11], new Date(), ob[13], ob[14], ob[15], ob[16], ob[17], ob[18]);
			}
		} finally {
			logger.info("AdjustmentContributionServiceImpl:saveMphTemp:End::{}", mphId);
		}
		return 1;

	}

	public int savePolicyTemp(Long policyId, Long mphId) {
		try {
			logger.info("AdjustmentContributionServiceImpl:savePolicyTemp:Start::{}", policyId);
			Object policyMasterEntity = policyMasterTempRepository.findPolicyDetailsAndIsActive(policyId);
			if (policyMasterEntity == null) {
				Object mphEntity = mphMasterTempRepository.findMasterMphIdbyMphId(mphId);
				Object[] ob = (Object[]) mphEntity;
				Object policyEntity = policyMasterRepository.findPolicyDetailsAndIsActive(policyId);
				Object[] obj = (Object[]) policyEntity;
				jdbcTemplate.update(
						"INSERT INTO POLICY_MASTER_TEMP (POLICY_ID,	ADJUSTMENT_DT, ADVANCEOTARREARS, AMT_TO_BE_ADJUSTED, ARD, CON_TYPE,	CONTRIBUTION_FREQUENCY,	CREATED_BY,	CREATED_ON,	FIRST_PREMIUM, INTERMEDIARY_OFFICER_CODE, INTERMEDIARY_OFFICER_NAME, IS_ACTIVE, IS_COMMENCEMENT_DATE_ONEYR,	LEAD_ID, LINE_OF_BUSINESS,	MARKETING_OFFICER_CODE,	MARKETING_OFFICER_NAME, MASTER_POLICY_ID, MODIFIED_BY, MODIFIED_ON,	MPH_ID,	NO_OF_CATEGORY,	POLICY_COMMENCEMENT_DT,	POLICY_DISPATCH_DATE, POLICY_NUMBER, POLICY_RECIEVED_DATE, POLICY_STATUS, POLICY_TYPE, PRODUCT_ID, PROPOSAL_ID,	QUOTATION_ID, REJECTION_REASON_CODE, REJECTION_REMARKS, RENEWAL_PREMIUM, SINGLE_PREMIUM_FIRSTYR, STAMP_DUTY, SUBSEQUENT_SINGLE_PREMIUM, TOTAL_MEMBER, UNIT_ID, UNIT_OFFICE, VARIANT, WORKFLOW_STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						policyMasterTempRepository.getPolicyTempSeq(), obj[1], obj[2], obj[3], obj[4], obj[5], obj[6],
						obj[7], obj[8], obj[9], obj[10], obj[11], obj[12], obj[13], obj[14], obj[15], obj[16], obj[17],
						obj[0], obj[18], new Date(), ob[0], obj[21], obj[22], obj[23], obj[24], obj[25], obj[26],
						obj[27], obj[28], obj[29], obj[30], obj[31], obj[32], obj[33], obj[34], obj[35], obj[36],
						obj[38], obj[39], obj[40], obj[41], obj[42]);
//				jdbcTemplate.update(AdjustmentContibutionConstants.INSERT_POLICY_TEMP,
//						policyMasterTempRepository.getPolicyTempSeq(), obj[1], obj[2], obj[3], obj[4], obj[5], obj[6],
//						obj[7], obj[8], obj[9], obj[10], obj[11], obj[12], obj[13], obj[14], obj[15], obj[16], obj[17],
//						obj[0], obj[18], new Date(), ob[0], obj[21], obj[22], obj[23], obj[24], obj[25], obj[26],
//						obj[27], obj[28], obj[29], obj[30], obj[31], obj[32], obj[33], obj[34], obj[35], obj[36],
//						obj[38], obj[39], obj[40], obj[41], obj[42]);
			}
			Long tempPolicyId = policyMasterTempRepository.findPolicyDetailsAndIsActive(policyId);
			if (tempPolicyId != null) {
				jdbcTemplate.update("UPDATE POLICY_MASTER SET TEMP_POLICY_ID =? WHERE POLICY_ID =?", tempPolicyId,
						policyId);
			}
			Long mphIdd = policyMasterTempRepository.findMphIdDetailsAndIsActive(policyId);
			Long tempMphId = policyMasterTempRepository.findMPHDetailsAndIsActive(policyId);

			if (tempMphId != null) {
				jdbcTemplate.update("UPDATE MPH_MASTER SET TEMP_MPH_ID =? WHERE MPH_ID =?", tempMphId, mphIdd);
			}
		} finally {
			logger.info("AdjustmentContributionServiceImpl:savePolicyTemp:End::{}", policyId);
		}
		return 0;
	}

	public int savePolicyContribution(Long policyId) {
		try {
			logger.info("AdjustmentContributionServiceImpl:savePolicyTemp:Start::{}", policyId);
			CommonResponseDto responseDto = commonServiceImpl.getFinancialYeartDetails(DateUtils.dateToStringDDMMYYYY(new Date()));
			Long tempPolicyId = policyMasterTempRepository.findPolicyDetailsAndIsActive(policyId);
			Object policyEntity = policyContributionRepository.findPolicyContributionByPolicyIdAndFinancialYr(policyId,responseDto.getFinancialYear());
			if (policyEntity == null) {
				policyEntity = policyContributionRepository.findPolicyContributionByPolicyIdAndFinancialYr(policyId,responseDto.getFinancialYear());
				Object[] ob = (Object[]) policyEntity;
				return jdbcTemplate.update(
						"INSERT INTO POLICY_CONTRIBUTION_TEMP(CONTRIBUTION_ID, ADJ_CON_ID, CLOSING_BALANCE, CONT_REFERENCE_NO, CONTRIBUTION_DATE, CONTRIBUTION_TYPE, CREATED_BY, CREATED_ON, EMPLOYEE_CONTRIBUTION, EMPLOYER_CONTRIBUTION, FINANCIAL_YEAR, IS_ACTIVE, IS_DEPOSIT, MASTER_POLICY_ID, MODIFIED_BY, MODIFIED_ON, OPENING_BALANCE, POLICY_ID, REG_CON_ID, TOTAL_CONTRIBUTION, TXN_ENTRY_STATUS, VERSION_NO, VOLUNTARY_CONTRIBUTION, ZERO_ACCOUNT_ENTRIES) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						policyContributionTempRepository.getContributionTempSeq(), ob[1], BigDecimal.ZERO, ob[3],
						new Date(), ob[5], ob[6], ob[7], BigDecimal.ZERO, BigDecimal.ZERO,
						responseDto.getFinancialYear(), ob[11], ob[12], policyId, ob[13], new Date(), BigDecimal.ZERO,
						tempPolicyId, ob[17], BigDecimal.ZERO, 1, 1, BigDecimal.ZERO, ob[23]);
			}
		} finally {
			logger.info("AdjustmentContributionServiceImpl:savePolicyTemp:End::{}", policyId);
		}
		return 0;
	}

	private Boolean calculateIsOneYrPolicy(Date policyCommencementDate) {
		logger.info("AdjustmentContributionServiceImpl:calculateIsOneYrPolicy:Start");
		Boolean isNewDate = false;
		if (policyCommencementDate != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(policyCommencementDate);
			c.add(Calendar.YEAR, 1);
			Date newDate = c.getTime();
			Calendar currentTime = Calendar.getInstance();
			Date currentDate = currentTime.getTime();
			if (newDate.equals(currentDate) || newDate.before(currentDate)) {
				logger.info("AdjustmentContributionServiceImpl:calculateIsOneYrPolicy:Ends :- Above Oneyear");
				return true;
			}
		}
		logger.info("AdjustmentContributionServiceImpl:calculateIsOneYrPolicy:Ends:- InBetween Oneyear");
		return isNewDate;
	}

	private ZeroAccountEntity checkZeroRow(Long policyId) {
		return zeroAccountRepository.findByPolicyIdAndIsActiveTrue(policyId);
	}

	public List<MphBankEntity> getPolicyBankList(Long policyId) {
		List<MphBankEntity> policyBankEntity = new ArrayList<>();
		try {
			logger.info("AdjustmentContributionServiceImpl :: getBankList:Starts");
			Long mphId = policyMasterRepository.findMphIdfromPolicy(policyId, Boolean.TRUE);
			if (mphId != null) {
				policyBankEntity = mphBankRepository.findAllByMphIdAndIsActiveTrue(mphId);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:: AdjustmentContributionServiceImpl :: getBankList", e);
		} finally {
			logger.info("AdjustmentContributionServiceImpl :: getBankList:Ends");
		}
		return policyBankEntity;
	}

	@Override
	public AdjustmentContributionResponseDto save(AdjustmentContributionDto request) {
		AdjustmentContributionResponseDto responseDto = new AdjustmentContributionResponseDto();
		AdjustmentContributionAllDto adjustmentContributionAllDto = new AdjustmentContributionAllDto();
		try {
			logger.info("AdjustmentContributionServiceImpl:save:Start");
			AdjustmentContributionTempEntity responseEntity = null;
			AdjustmentContributionTempEntity saveAdjEntity = new AdjustmentContributionTempEntity();
			if (request.getAdjustmentContributionId() != null && request.getAdjustmentContributionId() != 0) {
				AdjustmentContributionTempEntity entity = adjustmentContributionTempRepository.findByAdjustmentContributionIdAndIsActiveTrue(request.getAdjustmentContributionId());
				saveAdjEntity.setAdjustmentContributionId(entity.getAdjustmentContributionId());
				saveAdjEntity.setTotalContribution(entity.getTotalContribution());
				saveAdjEntity.setEmployerContribution(entity.getEmployerContribution());
				saveAdjEntity.setEmployeeContribution(entity.getEmployeeContribution());
				saveAdjEntity.setVoluntaryContribution(entity.getVoluntaryContribution());
				saveAdjEntity.setPolicyId(entity.getPolicyId());
				saveAdjEntity.setTempPolicyId(entity.getTempPolicyId());
				saveAdjEntity.setPolicyNumber(entity.getPolicyNumber());
				saveAdjEntity.setPolicyStatus(entity.getPolicyStatus());
				saveAdjEntity.setPolicyType(entity.getPolicyType());
				saveAdjEntity.setUnitCode(entity.getUnitCode());
				saveAdjEntity.setModifiedBy(request.getModifiedBy());
				saveAdjEntity.setModifiedOn(new Date());
				saveAdjEntity.setCreatedBy(entity.getCreatedBy());
				saveAdjEntity.setCreatedOn(entity.getCreatedOn());
				saveAdjEntity.setAdjustmentContributionNumber(getAdjNumber());
				saveAdjEntity.setAdjustmentContributionStatus(PolicyConstants.DRAFT_NO);
				saveAdjEntity.setWorkFlowStatus(PolicyConstants.DRAFT_NO);
				saveAdjEntity.setIsActive(Boolean.TRUE);
				saveAdjEntity.setBatchId(entity.getBatchId());
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
				saveAdjEntity.setProposalNumber(request.getProposalNumber());
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
				responseEntity = adjustmentContributionTempRepository.save(saveAdjEntity);
			} else {
				saveAdjEntity = modelMapper.map(request, AdjustmentContributionTempEntity.class);
				saveAdjEntity.setIsActive(true);
				saveAdjEntity.setIsCommencementdateOneYr(calculateIsOneYrPolicy(request.getPolicyCommencementDate()));
				saveAdjEntity.setAdjustmentContributionStatus(PolicyConstants.DRAFT_NO);
				saveAdjEntity.setAdjustmentContributionNumber(getAdjNumber());
				saveAdjEntity.setModifiedBy(request.getModifiedBy());
				saveAdjEntity.setModifiedOn(new Date());
				saveAdjEntity.setCreatedBy(request.getCreatedBy());
				saveAdjEntity.setCreatedOn(new Date());
				saveAdjEntity.setAdjustmentDueDate(request.getAdjustmentForDate());
				responseEntity = adjustmentContributionTempRepository.save(saveAdjEntity);
			}
			if (request.getAdjustmentContibutionDeposits() != null) {
				for (PolicyDepositDto policyDeposit : request.getAdjustmentContibutionDeposits()) {
					PolicyDepositTempEntity policyDepositEntity = new PolicyDepositTempEntity();
					policyDepositEntity.setDepositId(policyDeposit.getDepositId() != null ? policyDeposit.getDepositId() : null);
					policyDepositEntity.setPolicyId(responseEntity.getTempPolicyId());
					policyDepositEntity.setMasterPolicyId(responseEntity.getPolicyId());
					policyDepositEntity.setContributionType(PolicyConstants.SUBSQEUENTADJUSTMENTNEW);
					policyDepositEntity.setAdjustmentContributionId(request.getAdjustmentContributionId());
					policyDepositEntity.setRegularContributionId(policyDeposit.getRegularContributionId());
					policyDepositEntity.setCollectionNo(policyDeposit.getCollectionNo());
					policyDepositEntity.setCollectionDate(policyDeposit.getCollectionDate());
					policyDepositEntity.setCollectionStatus(policyDeposit.getCollectionStatus());
					policyDepositEntity.setChallanNo(policyDeposit.getChallanNo() != null && !"0".equals(policyDeposit.getChallanNo())? policyDeposit.getChallanNo(): getChallanSeq());
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
			}
			AdjustmentContributionDto dto = modelMapper.map(responseEntity, AdjustmentContributionDto.class);
			dto.setAmountToBeAdjusted(responseEntity.getAmountToBeAdjusted());
			adjustmentContributionAllDto.setAdjustmentContributionId(dto.getAdjustmentContributionId());
			adjustmentContributionAllDto.setAdjustmentContribution(dto);
			adjustmentContributionAllDto.setAdjustmentDepositDtos(getDepositFromPolicyDeposit(dto.getAdjustmentContributionId(), responseEntity.getTempPolicyId()));
			adjustmentContributionAllDto.setAdjustmentDepositAdjustmentDtos(getAdjustementFromPolicyDeposit(dto.getAdjustmentContributionId(), responseEntity.getTempPolicyId()));
			adjustmentContributionAllDto.setBank(getPolicyBankList(dto.getPolicyId()));
			responseDto.setResponseData(adjustmentContributionAllDto);
			responseDto.setBank(getPolicyBankList(dto.getPolicyId()));
			responseDto.setIsCommencementdateOneYr(calculateIsOneYrPolicy(dto.getPolicyCommencementDate()));
			responseDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
			responseDto.setTransactionStatus(CommonConstants.SUCCESS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception:AdjustmentContributionServiceImpl:save", e);
			responseDto.setTransactionMessage(CommonConstants.ERROR);
			responseDto.setTransactionStatus(CommonConstants.FAIL);
		}
		logger.info("AdjustmentContributionServiceImpl:save:Ends");
		return responseDto;
	}

	public List<AdjustmentContributionDepositDto> getDepositFromPolicyDeposit(Long adjustmentContributionId,Long policyId) {
		logger.info("AdjustmentContributionServiceImpl :: getDepositFromPolicyDeposit :: Start");
		List<AdjustmentContributionDepositDto> adjustmentContributionDepositDtos = new ArrayList<>();
		List<PolicyDepositTempEntity> policyDepositEntity = policyDepositTempRepository.findByStatusAndAdjustmentContributionIdAndPolicyIdAndIsActiveTrue(PolicyConstants.DEPOSITSTATUSNEW,adjustmentContributionId, policyId);
		for (PolicyDepositTempEntity policyDepositEntity2 : policyDepositEntity) {
			AdjustmentContributionDepositDto adjustmentContributionDepositDto = new AdjustmentContributionDepositDto();
			adjustmentContributionDepositDto.setAdjustmentContributiondepositId(policyDepositEntity2.getDepositId());
			adjustmentContributionDepositDto.setAdjustmentContributiondepositStatus(policyDepositEntity2.getStatus());
			adjustmentContributionDepositDto.setAdjustmentContributionId(policyDepositEntity2.getAdjustmentContributionId());
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
		logger.info("AdjustmentContributionServiceImpl :: getDepositFromPolicyDeposit :: Ends");
		return adjustmentContributionDepositDtos;
	}

	public List<AdjustmentContributionDepositAdjustmentDto> getAdjustementFromPolicyDeposit(Long adjustmentContributionId, Long policyId) {
		logger.info("AdjustmentContributionServiceImpl :: getAdjustementFromPolicyDeposit :: Start");
		List<AdjustmentContributionDepositAdjustmentDto> adjustmentContributionDepositDtos = new ArrayList<>();
		List<PolicyDepositTempEntity> policyDepositEntity = policyDepositTempRepository.findByStatusAndAdjustmentContributionIdAndPolicyIdAndIsActiveTrue(PolicyConstants.ADJESTED,adjustmentContributionId, policyId);
		for (PolicyDepositTempEntity policyDepositEntity2 : policyDepositEntity) {
			AdjustmentContributionDepositAdjustmentDto adjustmentContributionDepositDto = new AdjustmentContributionDepositAdjustmentDto();
			adjustmentContributionDepositDto.setAdjustmentContributiondepositId(policyDepositEntity2.getDepositId());
			adjustmentContributionDepositDto.setAdjustmentContributionAdjestmentStatus(policyDepositEntity2.getStatus());
			adjustmentContributionDepositDto.setAdjustmentContributionId(policyDepositEntity2.getAdjustmentContributionId());
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
		logger.info("AdjustmentContributionServiceImpl :: getAdjustementFromPolicyDeposit :: Ends");
		return adjustmentContributionDepositDtos;
	}

	public void checkZeroAccountInMember(Long policyId, BigDecimal zeroIdAmount, String role, Long policyContributionId,Long memberContibutionId, String type) {
		logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountInMember :: Start");
		Optional<ZeroAccountEntity> zeroAccountsOpt = zeroAccountRepository.findByIsActiveTrueAndPolicyId(policyId);
		if (!zeroAccountsOpt.isPresent()) {
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountInMember ::  convertSetZeroAcc :: Start");
			ZeroAccountEntity zeroMember = convertSetZeroAcc(policyId, zeroIdAmount, role);
			zeroAccountRepository.save(zeroMember);
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountInMember :: convertSetZeroAcc :: Ends");
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountInMember :: convertSetZeroAccEntries :: Start");
			ZeroAccountEntriesEntity zeroMemberEntries = convertSetZeroAccEntries(policyId, zeroIdAmount, role,policyContributionId, memberContibutionId, type);
			zeroAccountEntriesRepository.save(zeroMemberEntries);
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountInMember :: convertSetZeroAccEntries :: Ends");
		} else {
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountInMember :: else ::  Ends");
		}
		logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountInMember :: Ends");
	}

	private ZeroAccountEntriesEntity convertSetZeroAccEntries(Long policyId, BigDecimal zeroIdAmount, String role,Long policyContributionId, Long memberContibutionId, String type) {
		logger.info("AdjustmentContributionServiceImpl :: convertSetZeroAccEntries :: Start");
		ZeroAccountEntriesEntity response = new ZeroAccountEntriesEntity();
		response.setZeroAccEntId(null);
		response.setPolicyId(policyId);
		response.setPolicyConId(policyContributionId);
		response.setMemberConId(memberContibutionId);
		response.setZeroIdAmount(zeroIdAmount);
		response.setTransactionType(type);
		response.setTransactionDate(new Date());
		response.setCreatedOn(new Date());
		response.setCreatedBy(role);
		response.setModifiedOn(new Date());
		response.setModifiedBy(role);
		response.setIsActive(Boolean.TRUE);
		logger.info("AdjustmentContributionServiceImpl :: convertSetZeroAccEntries :: Ends");
		return response;
	}

	private ZeroAccountEntity convertSetZeroAcc(Long policyId, BigDecimal zeroIdAmount, String role) {
		logger.info("AdjustmentContributionServiceImpl :: convertSetZeroAcc :: Start");
		ZeroAccountEntity response = zeroAccountRepository.findByPolicyIdAndIsActiveTrue(policyId);
		BigDecimal zeroIdAmountNew = response.getZeroIdAmount().add(zeroIdAmount);
		response.setZeroIdAmount(zeroIdAmountNew);
		response.setPolicyId(policyId);
		response.setModifiedBy(role);
		response.setModifiedOn(new Date());
		response.setIsActive(Boolean.TRUE);
		logger.info("AdjustmentContributionServiceImpl :: convertSetZeroAcc :: Ends");
		return response;
	}

	public Object getZeroAccountFromPolicy(Long policyId) {
		return zeroAccountRepository.findByPolicyIdAndIsActiveTrue(policyId);
	}

	public Object getZeroAccountFromPolicyTemp(Long policyId) {
		return zeroAccountTempRepository.findByPolicyIdAndIsActiveTrue(policyId);
	}
	
	public void checkZeroAccountTempInMember(Long policyId, BigDecimal zeroIdAmount, String role,Long policyContributionId, Long memberContibutionId, String type,String mPhName) {
		logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountTempInMember :: Start");
		Optional<ZeroAccountTempEntity> zeroAccountsOpt = zeroAccountTempRepository.findByIsActiveTrueAndPolicyId(policyId);
		if (!zeroAccountsOpt.isPresent()) {
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccTemp :: Start");
			ZeroAccountTempEntity zeroMember = convertSetZeroAccTemp(policyId, zeroIdAmount, role);
			zeroAccountTempRepository.save(zeroMember);
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccTemp :: Ends");
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountTempInMember :: setMemberForZeroRow :: Start");
			MemberMasterTempEntity memberMasterTempEntity = setMemberForZeroRow(policyId,mPhName,role);
			memberMasterTempRepository.save(memberMasterTempEntity);
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountTempInMember :: setMemberForZeroRow :: Ends");
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccEntriesTemp :: Start");
			ZeroAccountEntriesTempEntity zeroMemberEntries = convertSetZeroAccEntriesTemp(policyId, zeroIdAmount, role,policyContributionId, memberContibutionId, type);
			zeroAccountEntriesTempRepository.save(zeroMemberEntries);
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccEntriesTemp :: Ends");
		} else {
			ZeroAccountTempEntity zeroMembertemp = zeroAccountsOpt.get();
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccTempExist :: Start");
			ZeroAccountTempEntity zeroMember = convertSetZeroAccTempExist(zeroMembertemp, policyId, zeroIdAmount, role);
			zeroAccountTempRepository.save(zeroMember);
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccTempExist :: Ends");
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccEntriesTemp :: Start");
			ZeroAccountEntriesTempEntity zeroMemberEntries = convertSetZeroAccEntriesTemp(policyId, zeroIdAmount, role,policyContributionId, memberContibutionId, type);
			zeroAccountEntriesTempRepository.save(zeroMemberEntries);
			logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountTempInMember :: convertSetZeroAccEntriesTemp :: Ends");
		}
		logger.info("AdjustmentContributionServiceImpl :: checkZeroAccountTempInMember :: Ends");	
	}

	private MemberMasterTempEntity setMemberForZeroRow(Long policyId, String mPhName, String role) {
		logger.info("AdjustmentContributionServiceImpl :: setMemberForZeroRow :: Starts");
		MemberMasterTempEntity tempEntity = new MemberMasterTempEntity();
		tempEntity.setMemberId(null);
		tempEntity.setPolicyId(policyId);
		tempEntity.setMasterMemberId(null);
		tempEntity.setMembershipNumber("");
		tempEntity.setMemberStatus(CommonConstants.ACTIVE);
		tempEntity.setLicId("0");
		tempEntity.setTypeOfMemebership(null);
		tempEntity.setAadharNumber(null);
		tempEntity.setCategoryNo(null);
		tempEntity.setCommunicationPreference(null);
		tempEntity.setLanguagePreference(null);
		tempEntity.setDateOfBirth(null);
		tempEntity.setDateOfJoining(null);
		tempEntity.setDateOfJoiningScheme(null);
		tempEntity.setDateOfRetrirement(null);
		tempEntity.setDesignation(null);
		tempEntity.setEmailid(null);
		tempEntity.setGender(null);
		tempEntity.setFatherName(null);
		tempEntity.setFirstName(mPhName);
		tempEntity.setMiddleName(null);
		tempEntity.setLastName(null);
		tempEntity.setSpouseName(null);
		tempEntity.setMaritalStatus(null);
		tempEntity.setMemberPan(null);
		tempEntity.setLandlineNo(null);
		tempEntity.setMobileNo(null);
		tempEntity.setIsZeroid(Boolean.TRUE);
		tempEntity.setIsActive(Boolean.TRUE);
		tempEntity.setCreatedBy(role);
		tempEntity.setCreatedOn(new Date());
		tempEntity.setModifiedBy(role);
		tempEntity.setModifiedOn(new Date());
		logger.info("AdjustmentContributionServiceImpl :: setMemberForZeroRow :: Ends");
		return tempEntity;
	}

	private ZeroAccountTempEntity convertSetZeroAccTempExist(ZeroAccountTempEntity response, Long policyId,BigDecimal zeroIdAmount, String role) {
		logger.info("AdjustmentContributionServiceImpl :: convertSetZeroAccTempExist :: Start");
		BigDecimal zeroIdAmountNew = response.getZeroIdAmount().add(zeroIdAmount);
		response.setZeroIdAmount(zeroIdAmountNew);
		response.setPolicyId(policyId);
		response.setModifiedBy(role);
		response.setModifiedOn(new Date());
		response.setIsActive(Boolean.TRUE);
		logger.info("AdjustmentContributionServiceImpl :: convertSetZeroAccTempExist :: Ends");
		return response;
	}

	private ZeroAccountEntriesTempEntity convertSetZeroAccEntriesTemp(Long policyId, BigDecimal zeroIdAmount,String role, Long policyContributionId, Long memberContibutionId, String type) {
		ZeroAccountEntriesTempEntity response = new ZeroAccountEntriesTempEntity();
		logger.info("AdjustmentContributionServiceImpl :: convertSetZeroAccEntriesTemp :: Start");
		response.setZeroAccEntId(null);
		response.setPolicyId(policyId);
		response.setPolicyConId(policyContributionId);
		response.setMemberConId(memberContibutionId);
		response.setZeroIdAmount(zeroIdAmount);
		response.setTransactionType(type);
		response.setTransactionDate(new Date());
		response.setCreatedOn(new Date());
		response.setCreatedBy(role);
		response.setModifiedOn(new Date());
		response.setModifiedBy(role);
		response.setIsActive(Boolean.TRUE);
		logger.info("AdjustmentContributionServiceImpl :: convertSetZeroAccEntriesTemp :: Ends");
		return response;
	}

	private ZeroAccountTempEntity convertSetZeroAccTemp(Long policyId, BigDecimal zeroIdAmount, String role) {
		logger.info("AdjustmentContributionServiceImpl :: convertSetZeroAccTemp :: Start");
		ZeroAccountTempEntity response = zeroAccountTempRepository.findByPolicyIdAndIsActiveTrue(policyId);
			if(response==null) {
				response = new ZeroAccountTempEntity();
				response.setZeroIdAmount(BigDecimal.ZERO);
			}
			BigDecimal zeroIdAmountNew = response.getZeroIdAmount().add(zeroIdAmount);
			response.setZeroAccId(null);
			response.setZeroIdAmount(zeroIdAmountNew);
			response.setPolicyId(policyId);
			response.setModifiedBy(role);
			response.setModifiedOn(new Date());
			response.setIsActive(Boolean.TRUE);
		logger.info("AdjustmentContributionServiceImpl :: convertSetZeroAccTemp :: Ends");
		return response;
	}

	public Object getZeroAccountTempFromPolicy(Long policyId) {
		return zeroAccountTempRepository.findByPolicyIdAndIsActiveTrue(policyId);
	}

	@Override
	public AdjustmentContributionResponseDto notesave(AdjustmentContributionNotesDto request) {
		AdjustmentContributionResponseDto responseDto = new AdjustmentContributionResponseDto();
		AdjustmentContributionAllDto adjustmentContributionAllDto = new AdjustmentContributionAllDto();
		try {
			logger.info("AdjustmentContributionServiceImpl:: notesave::Start");
			AdjustmentContributionNotesTempEntity saveAdjnotes = modelMapper.map(request,AdjustmentContributionNotesTempEntity.class);
			saveAdjnotes.setNotesId(null);
			saveAdjnotes.setAdjustmentContributionId(request.getAdjustmentContributionId());
			saveAdjnotes.setPolicyId(request.getPolicyId());
			AdjustmentContributionNotesTempEntity responseEntity = adjNotesTempRepo.save(saveAdjnotes);
			AdjustmentContributionNotesDto dto = modelMapper.map(responseEntity, AdjustmentContributionNotesDto.class);
			adjustmentContributionAllDto.setAdjustmentContributionId(dto.getAdjustmentContributionId());
			adjustmentContributionAllDto.setAdjustmentNotesDto(dto);
			responseDto.setResponseData(adjustmentContributionAllDto);
			responseDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
			responseDto.setTransactionStatus(CommonConstants.SUCCESS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception::AdjustmentContributionServiceImpl::notesave", e);
			responseDto.setTransactionMessage(CommonConstants.ERROR);
			responseDto.setTransactionStatus(CommonConstants.FAIL);
		}
		logger.info("AdjustmentContributionServiceImpl::notesave::Ends");
		return responseDto;
	}

	@Override
	public AdjustmentContributionResponseDto getNotesList(Long adjustmentContributionId) {
		AdjustmentContributionResponseDto responseDto = new AdjustmentContributionResponseDto();
		AdjustmentContributionAllDto adjustmentContributionAllDto = new AdjustmentContributionAllDto();
		try {
			logger.info("AdjustmentContributionServiceImpl::getNotesList::Start");
			List<AdjustmentContributionNotesTempEntity> responseEntity = adjNotesTempRepo.findAllByAdjustmentContributionId(adjustmentContributionId);
			adjustmentContributionAllDto.setAdjustmentNotesDtos(adjustmentContributionCommonServiceImpl.convertNoteEntityToDto(responseEntity));
			responseDto.setResponseData(adjustmentContributionAllDto);
			responseDto.setTransactionMessage(CommonConstants.FETCH);
			responseDto.setTransactionStatus(CommonConstants.SUCCESS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception::AdjustmentContributionServiceImpl::getNotesList", e);
			responseDto.setTransactionMessage(e.getMessage());
			responseDto.setTransactionStatus(CommonConstants.FAIL);
		}
		logger.info("AdjustmentContributionServiceImpl::getNotesList:Ends");
		return responseDto;
	}

	@Override
	public AdjustmentContributionResponseDto changeStatus(Long adjustmentContributionId,String adjustmentContributionStatus, String role) {
		AdjustmentContributionAllDto adjustmentContributionAllDto = new AdjustmentContributionAllDto();
		AdjustmentContributionResponseDto responseDto = new AdjustmentContributionResponseDto();
		try {
			logger.info("AdjustmentContributionServiceImpl:changeStatus:Start");
			AdjustmentContributionTempEntity adjContriTempEntity = adjustmentContributionTempRepository.findByAdjustmentContributionIdAndIsActiveTrue(adjustmentContributionId);
			if (adjContriTempEntity != null) {
				adjContriTempEntity.setModifiedOn(DateUtils.sysDate());
				adjContriTempEntity.setAdjustmentContributionStatus(adjustmentContributionStatus);
				AdjustmentContributionTempEntity saveEntity = adjustmentContributionTempRepository.save(adjContriTempEntity);
				AdjustmentContributionDto dto = modelMapper.map(saveEntity, AdjustmentContributionDto.class);
				adjustmentContributionAllDto.setAdjustmentContribution(dto);
				adjustmentContributionAllDto.setAdjustmentContributionId(dto.getAdjustmentContributionId());
				responseDto.setResponseData(adjustmentContributionAllDto);
				responseDto.setTransactionStatus(CommonConstants.SUCCESS);
				responseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			} else {
				responseDto.setTransactionStatus(CommonConstants.ERROR);
				responseDto.setTransactionMessage(CommonConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:AdjustmentContributionServiceImpl:changeStatus", e);
			responseDto.setTransactionStatus(CommonConstants.ERROR);
			responseDto.setTransactionMessage(CommonConstants.FAIL);
		}
		logger.info("AdjustmentContributionServiceImpl:changeStatus:Ends");
		return responseDto;
	}

	@Override
	public AdjustmentContributionResponseDto reject(Long adjustmentContributionId, String adjustmentContributionStatus,String role, String rejectionRemarks, String rejectionReasonCode) {
		AdjustmentContributionResponseDto responseDto = new AdjustmentContributionResponseDto();
		try {
			logger.info("AdjustmentContributionServiceImpl:reject:Start");
			AdjustmentContributionTempEntity adjContriTempEntity = adjustmentContributionTempRepository.findByAdjustmentContributionIdAndIsActiveTrue(adjustmentContributionId);
			if (adjContriTempEntity != null) {
				logger.info("AdjustmentContributionServiceImpl:reject:: saveTemp ::Start");
				adjContriTempEntity.setModifiedBy(role);
				adjContriTempEntity.setModifiedOn(DateUtils.sysDate());
				adjContriTempEntity.setIsActive(false);
				adjContriTempEntity.setAdjustmentContributionStatus(adjustmentContributionStatus);
				adjContriTempEntity.setRejectionReasonCode(rejectionReasonCode);
				adjContriTempEntity.setRejectionRemarks(rejectionRemarks);
				AdjustmentContributionTempEntity saveEntity = adjustmentContributionTempRepository.save(adjContriTempEntity);
				logger.info("AdjustmentContributionServiceImpl:reject:: saveTemp ::Ends");
				logger.info("AdjustmentContributionServiceImpl:reject:: convertRejectTempToMaster ::Start");
				AdjustmentContributionEntity adjConEntity = adjustmentContributionCommonServiceImpl.convertRejectTempToMaster(saveEntity);
				AdjustmentContributionEntity saveMain = adjRepo.save(adjConEntity);
				logger.info("AdjustmentContributionServiceImpl:reject:: convertRejectTempToMaster ::Ends");
				logger.info("AdjustmentContributionServiceImpl:reject:: convertRejectContibutionTempToMain ::Start");
				adjustmentContributionCommonServiceImpl.convertRejectContibutionTempToMain(saveMain.getPolicyId(),saveMain.getTempPolicyId(), adjustmentContributionId, saveMain.getAdjustmentContributionId(),role);
				logger.info("AdjustmentContributionServiceImpl:reject:: convertRejectContibutionTempToMain ::Ends");
				AdjustmentContributionDto dto = modelMapper.map(saveMain, AdjustmentContributionDto.class);
				AdjustmentContributionAllDto adjustmentContributionAllDto = new AdjustmentContributionAllDto();
				adjustmentContributionAllDto.setAdjustmentContribution(dto);
				adjustmentContributionAllDto.setAdjustmentContributionId(dto.getAdjustmentContributionId());
				responseDto.setResponseData(adjustmentContributionAllDto);
				responseDto.setTransactionStatus(CommonConstants.SUCCESS);
				responseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			} else {
				responseDto.setTransactionStatus(CommonConstants.ERROR);
				responseDto.setTransactionMessage(CommonConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:AdjustmentContributionServiceImpl:reject", e);
			responseDto.setTransactionStatus(CommonConstants.ERROR);
			responseDto.setTransactionMessage(CommonConstants.FAIL);
		}
		logger.info("AdjustmentContributionServiceImpl:reject:Ends");
		return responseDto;
	}

	@Override
	public AdjustmentContributionResponseDto getInprogressLoad(AdjustmentSearchDto request) {
		AdjustmentContributionResponseDto commonDto = new AdjustmentContributionResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("adjustmentContributionServiceImpl:getInprogressLoad:Start");
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<AdjustmentContributionTempEntity> createQuery = criteriaBuilder.createQuery(AdjustmentContributionTempEntity.class);
			Root<AdjustmentContributionTempEntity> root = createQuery.from(AdjustmentContributionTempEntity.class);

			if (StringUtils.isNotBlank(request.getRole()) && Objects.equals(request.getRole(), PolicyConstants.MAKER)) {
				predicates.add(root.get(AdjustmentContibutionConstants.ADJUSTMENTCONTRIBUTION_STATUS).in(Arrays.asList(PolicyConstants.DRAFT_NO, PolicyConstants.MAKER_NO)));
			}
			if (StringUtils.isNotBlank(request.getRole()) && Objects.equals(request.getRole(), PolicyConstants.CHECKER)) {
				predicates.add(root.get(AdjustmentContibutionConstants.ADJUSTMENTCONTRIBUTION_STATUS).in(Arrays.asList(PolicyConstants.CHECKER_NO)));
			}
			if (StringUtils.isNotBlank(request.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get(AdjustmentContibutionConstants.POLICY_NUMBER)),request.getPolicyNumber().toLowerCase()));
			}
			if (StringUtils.isNotBlank(request.getMphCode())) {
				predicates.add(criteriaBuilder.equal(root.get(AdjustmentContibutionConstants.MPH_CODE), request.getMphCode()));
			}
			if (StringUtils.isNotBlank(request.getMphName())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get(AdjustmentContibutionConstants.MPH_NAME)),request.getMphName().toLowerCase()));
			}
			if (StringUtils.isNotBlank(request.getFrom()) && StringUtils.isNotBlank(request.getTo())) {
				Date fromDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(request.getFrom());
				Date toDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(request.getTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get(AdjustmentContibutionConstants.CREATED_ON), fromDate, toDate));
			}
			if (StringUtils.isNotBlank(request.getProduct())) {
				predicates.add(criteriaBuilder.equal(root.get(AdjustmentContibutionConstants.PRODUCT), request.getProduct()));
			}
			if (StringUtils.isNotBlank(request.getLineOfBusiness())) {
				predicates.add(criteriaBuilder.equal(root.get(AdjustmentContibutionConstants.LINE_OF_BUSINESS),request.getLineOfBusiness()));
			}
			if (StringUtils.isNotBlank(request.getVariant())) {
				predicates.add(criteriaBuilder.equal(root.get(AdjustmentContibutionConstants.VARIANT), request.getVariant()));
			}
			if (StringUtils.isNotBlank(request.getPolicyStatus())) {
				predicates.add(criteriaBuilder.equal(root.get(AdjustmentContibutionConstants.POLICY_STATUS),request.getPolicyStatus()));
			}
			if (StringUtils.isNotBlank(request.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get(AdjustmentContibutionConstants.UNIT_CODE),request.getUnitCode()));
			}
			predicates.add(criteriaBuilder.equal(root.get(AdjustmentContibutionConstants.IS_ACTIVE), Boolean.TRUE));
			createQuery.orderBy(criteriaBuilder.desc(root.get(AdjustmentContibutionConstants.MODIFIED_ON)));
			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			List<AdjustmentContributionTempEntity> adjustmentEntities = entityManager.createQuery(createQuery).getResultList();
			List<AdjustmentContributionDto> adjDto = mapList(adjustmentEntities, AdjustmentContributionDto.class);
			commonDto.setResponseData(adjDto);
			commonDto.setTransactionMessage(CommonConstants.FETCH);
			commonDto.setTransactionStatus(CommonConstants.STATUS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception:adjustmentContributionServiceImpl:getInprogressLoad", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("adjustmentContributionServiceImpl:getInprogressLoad:Ends");
		}
		return commonDto;
	}

	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		logger.info("AdjustmentContributionServiceImpl:mapList:Start");
		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
	}

	@Override
	public AdjustmentContributionResponseDto getInprogressDetails(Long adjustmentContributionId) {
		AdjustmentContributionResponseDto adjustmentContributionResponseDto = new AdjustmentContributionResponseDto();
		AdjustmentContributionAllDto adjustmentContributionAllDto = new AdjustmentContributionAllDto();
		try {
			logger.info("AdjustmentContributionServiceImpl:getInprogressDetails:Start");
			AdjustmentContributionTempEntity adjustmentContributionTempEntity = adjustmentContributionTempRepository.findByAdjustmentContributionIdAndIsActiveTrue(adjustmentContributionId);
			if (adjustmentContributionTempEntity == null) {
				adjustmentContributionResponseDto.setTransactionMessage(CommonConstants.INVALID_REQUEST);
				adjustmentContributionResponseDto.setTransactionStatus(CommonConstants.FAIL);
				return adjustmentContributionResponseDto;
			}
			
			
			AdjustmentContributionDto adjustmentContributionDto = adjustmentContributionCommonServiceImpl.convertTempEntityToDto(adjustmentContributionTempEntity);
			String LastPremiumDueDate = policyContributionRepository.findPolicyId(adjustmentContributionTempEntity.getPolicyId());
			adjustmentContributionDto.setLastPremiumDueDate(LastPremiumDueDate);		
			adjustmentContributionAllDto.setAdjustmentContribution(adjustmentContributionDto);
			adjustmentContributionAllDto.setAdjustmentDepositDtos(getDepositFromPolicyDeposit(adjustmentContributionDto.getAdjustmentContributionId(),adjustmentContributionTempEntity.getTempPolicyId()));
			adjustmentContributionAllDto.setAdjustmentDepositAdjustmentDtos(getAdjustementFromPolicyDeposit(adjustmentContributionDto.getAdjustmentContributionId(),adjustmentContributionTempEntity.getTempPolicyId()));
			adjustmentContributionAllDto.setBank(getPolicyBankList(adjustmentContributionTempEntity.getPolicyId()));
			adjustmentContributionResponseDto.setResponseData(adjustmentContributionAllDto);
			adjustmentContributionResponseDto.setBank(getPolicyBankList(adjustmentContributionTempEntity.getPolicyId()));
			adjustmentContributionResponseDto.setTransactionMessage(CommonConstants.FETCH);
			adjustmentContributionResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
		} catch (Exception e) {
			logger.error("Exception:AdjustmentContributionServiceImpl:getInprogressDetails", e);
			adjustmentContributionResponseDto.setTransactionMessage(e.getMessage());
			adjustmentContributionResponseDto.setTransactionStatus(CommonConstants.FAIL);
		}
		logger.info("AdjustmentContributionServiceImpl:getInprogressDetails:Ends");
		return adjustmentContributionResponseDto;
	}

	@Override
	public AdjustmentContributionResponseDto getExisitngLoad(AdjustmentSearchDto request) {
		List<AdjustmentContributionEntity> adjustmentEntities = new ArrayList<>();
		AdjustmentContributionResponseDto commonDto = new AdjustmentContributionResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("adjustmentContributionServiceImpl:getExisitngLoad:Start");
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<AdjustmentContributionEntity> createQuery = criteriaBuilder.createQuery(AdjustmentContributionEntity.class);
			Root<AdjustmentContributionEntity> root = createQuery.from(AdjustmentContributionEntity.class);
			if (StringUtils.isNotBlank(request.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get(AdjustmentContibutionConstants.POLICY_NUMBER)),request.getPolicyNumber().toLowerCase()));
			}
			if (StringUtils.isNotBlank(request.getMphCode())) {
				predicates.add(criteriaBuilder.equal(root.get(AdjustmentContibutionConstants.MPH_CODE), request.getMphCode()));
			}
			if (StringUtils.isNotBlank(request.getMphName())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get(AdjustmentContibutionConstants.MPH_NAME)),request.getMphName().toLowerCase()));
			}
			if (StringUtils.isNotBlank(request.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get(AdjustmentContibutionConstants.UNIT_CODE),request.getUnitCode()));
			}
			if (StringUtils.isNotBlank(request.getProduct())) {
				predicates.add(criteriaBuilder.equal(root.get(AdjustmentContibutionConstants.PRODUCT), request.getProduct()));
			}
			if (StringUtils.isNotBlank(request.getLineOfBusiness())) {
				predicates.add(criteriaBuilder.equal(root.get(AdjustmentContibutionConstants.LINE_OF_BUSINESS),request.getLineOfBusiness()));
			}
			predicates.add(root.get(AdjustmentContibutionConstants.ADJUSTMENTCONTRIBUTION_STATUS).in(Arrays.asList(PolicyConstants.APPROVED_NO, PolicyConstants.REJECTED_NO)));
			createQuery.orderBy(criteriaBuilder.desc(root.get(AdjustmentContibutionConstants.MODIFIED_ON)));
			if (StringUtils.isNotBlank(request.getFrom()) && StringUtils.isNotBlank(request.getTo())) {
				Date fromDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(request.getFrom());
				Date toDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(request.getTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get(AdjustmentContibutionConstants.CREATED_ON), fromDate, toDate));
			}
			if (StringUtils.isNotBlank(request.getVariant())) {
				predicates.add(criteriaBuilder.equal(root.get(AdjustmentContibutionConstants.VARIANT), request.getVariant()));
			}
			if (StringUtils.isNotBlank(request.getPolicyStatus())) {
				predicates.add(criteriaBuilder.equal(root.get(AdjustmentContibutionConstants.ADJUSTMENTCONTRIBUTION_STATUS),request.getPolicyStatus()));
			}
			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			adjustmentEntities = entityManager.createQuery(createQuery).getResultList();
			List<AdjustmentContributionDto> adjDto = mapList(adjustmentEntities, AdjustmentContributionDto.class);
			commonDto.setResponseData(adjDto);
			commonDto.setTransactionMessage(CommonConstants.FETCH);
			commonDto.setTransactionStatus(CommonConstants.STATUS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception:adjustmentContributionServiceImpl:getExisitngLoad", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("adjustmentContributionServiceImpl:getExisitngLoad:Ends");
		}
		return commonDto;
	}

	@Override
	public AdjustmentContributionResponseDto getExistingDetails(Long adjustmentContributionId) {
		AdjustmentContributionResponseDto adjustmentContributionResponseDto = new AdjustmentContributionResponseDto();
		AdjustmentContributionAllDto adjustmentContributionAllDto = new AdjustmentContributionAllDto();
		try {
			logger.info("AdjustmentContributionServiceImpl:getExistingDetails:Start");
			AdjustmentContributionEntity adjustmentContributionEntity = adjRepo.findByAdjustmentContributionIdAndIsActiveTrue(adjustmentContributionId);
			if (adjustmentContributionEntity == null) {
				adjustmentContributionResponseDto.setTransactionMessage(CommonConstants.INVALID_REQUEST);
				adjustmentContributionResponseDto.setTransactionStatus(CommonConstants.FAIL);
				return adjustmentContributionResponseDto;
			}
			AdjustmentContributionDto adjustmentContributionDto = adjustmentContributionCommonServiceImpl.convertMainEntityToDtos(adjustmentContributionEntity);
			adjustmentContributionAllDto.setAdjustmentContribution(adjustmentContributionDto);
			adjustmentContributionAllDto.setAdjustmentDepositDtos(getApprovedDepositFromPolicyDeposit(adjustmentContributionDto.getAdjustmentContributionId(), adjustmentContributionDto.getPolicyId()));
			adjustmentContributionAllDto.setAdjustmentDepositAdjustmentDtos(getApprovedAdjustementFromPolicyDeposit(adjustmentContributionDto.getAdjustmentContributionId(), adjustmentContributionDto.getPolicyId()));
			adjustmentContributionAllDto.setBank(getPolicyBankList(adjustmentContributionDto.getPolicyId()));
			adjustmentContributionResponseDto.setResponseData(adjustmentContributionAllDto);
			adjustmentContributionResponseDto.setBank(getPolicyBankList(adjustmentContributionDto.getPolicyId()));
			adjustmentContributionResponseDto.setTransactionMessage(CommonConstants.FETCH);
			adjustmentContributionResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
		} catch (Exception e) {
			logger.error("Exception:AdjustmentContributionServiceImpl:getExistingDetails", e);
			adjustmentContributionResponseDto.setTransactionMessage(e.getMessage());
			adjustmentContributionResponseDto.setTransactionStatus(CommonConstants.FAIL);
		}
		logger.info("AdjustmentContributionServiceImpl:getExistingDetails:Ends");
		return adjustmentContributionResponseDto;
	}

	private List<AdjustmentContributionDepositDto> getApprovedDepositFromPolicyDeposit(Long adjustmentContributionId,Long policyId) {
		logger.info("AdjustmentContributionServiceImpl :: getApprovedDepositFromPolicyDeposit :: Start");
		List<AdjustmentContributionDepositDto> adjustmentContributionDepositDtos = new ArrayList<>();
		List<PolicyDepositEntity> policyDepositEntity = policyDepositRepository.findByStatusAndAdjustmentContributionIdAndPolicyIdAndIsActiveTrue(PolicyConstants.DEPOSITSTATUSNEW,adjustmentContributionId, policyId);
		for (PolicyDepositEntity policyDepositEntity2 : policyDepositEntity) {
			AdjustmentContributionDepositDto adjustmentContributionDepositDto = new AdjustmentContributionDepositDto();
			adjustmentContributionDepositDto.setAdjustmentContributiondepositId(policyDepositEntity2.getDepositId());
			adjustmentContributionDepositDto.setAdjustmentContributiondepositStatus(policyDepositEntity2.getStatus());
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
		logger.info("AdjustmentContributionServiceImpl :: getApprovedDepositFromPolicyDeposit :: Ends");
		return adjustmentContributionDepositDtos;
	}

	private List<AdjustmentContributionDepositAdjustmentDto> getApprovedAdjustementFromPolicyDeposit(Long adjustmentContributionId, Long policyId) {
		logger.info("AdjustmentContributionServiceImpl :: getApprovedAdjustementFromPolicyDeposit :: Start");
		List<AdjustmentContributionDepositAdjustmentDto> adjustmentContributionDepositDtos = new ArrayList<>();
		List<PolicyDepositEntity> policyDepositEntity = policyDepositRepository.findByStatusAndAdjustmentContributionIdAndPolicyIdAndIsActiveTrue(PolicyConstants.ADJESTED,adjustmentContributionId, policyId);
		for (PolicyDepositEntity policyDepositEntity2 : policyDepositEntity) {
			AdjustmentContributionDepositAdjustmentDto adjustmentContributionDepositDto = new AdjustmentContributionDepositAdjustmentDto();
			adjustmentContributionDepositDto.setAdjustmentContributiondepositId(policyDepositEntity2.getDepositId());
			adjustmentContributionDepositDto.setAdjustmentContributionAdjestmentStatus(policyDepositEntity2.getStatus());
			adjustmentContributionDepositDto.setAdjustmentContributionId(policyDepositEntity2.getAdjustmentContributionId());
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
		logger.info("AdjustmentContributionServiceImpl :: getApprovedAdjustementFromPolicyDeposit :: Ends");
		return adjustmentContributionDepositDtos;
	}

	@Override
	public PolicyAdjustmentResponse saveAdjustmentOldDto(Long policyId) {
		logger.info("AdjustmentContributionServiceImpl :: saveAdjustmentOldDto :: Start");
		PolicyAdjustmentResponse response = new PolicyAdjustmentResponse();
		PolicyMasterEntity policyEntityOpt = policyMasterRepository.findById(policyId).orElse(null);
		if (policyEntityOpt != null) {
			MphMasterEntity qwert = mphMasterRepository.findByMphIdAndIsActiveTrue(policyEntityOpt.getMphId());
			MphMasterDto mphMasterDto = policyCommonServiceImpl.convertMphMasterEntityToMphMasterDto(qwert);
			PolicyDto policyDto = policyCommonServiceImpl.convertNewResponseToOldResponse(mphMasterDto);
			policyDto.getDeposit().removeIf(deposit -> PolicyConstants.ADJESTED.equalsIgnoreCase(deposit.getStatus()));
			policyDto.getMembers().removeIf(member -> Boolean.TRUE.equals(member.getIsZeroId()));
			policyDto.getAdjustments().removeIf(deposits -> PolicyConstants.DEPOSITSTATUSNEW.equalsIgnoreCase(deposits.getStatus()));
			response.setPolicyId(policyEntityOpt.getPolicyId());
			response.setPolicyDto(policyDto);
			response.setAdjustments(mapListAdjustment(mapListAdjustment(response.getPolicyDto().getAdjustments())));
			response.setTotalAmountToBeAdjustment(response.getPolicyDto().getAmountToBeAdjusted());
			response.setTotalDeposit(response.getPolicyDto().getTotalDeposit());
			response.setTransactionStatus(CommonConstants.SUCCESS);
			response.setTransactionMessage(CommonConstants.SAVEMESSAGE);
		}
		logger.info("AdjustmentContributionServiceImpl :: saveAdjustmentOldDto :: Ends");
		return response;
	}

	private Set<PolicyAdjustmentOldDto> mapListAdjustment(Set<PolicyAdjustmentOldDto> adjustments) {
		logger.info("AdjustmentContributionServiceImpl :: mapListAdjustment :: Start");
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
		logger.info("AdjustmentContributionServiceImpl :: mapListAdjustment :: Ends");
		return response;
	}

	@Override
	public AdjustmentContributionResponseDto saveMasterTemp(CommonIntegrationDto dto) {
		AdjustmentContributionResponseDto responseDto = new AdjustmentContributionResponseDto();
		try {
			logger.info("AdjustmentContributionServiceImpl:saveMasterTemp:Start::{}", dto.getPolicyId());
			List<MemberMasterEntity> memberEntity = memberMasterRepository.findPolicyDetailsAndIsActive(dto.getPolicyId(), dto.getLicId());
			Long policyEntity = policyMasterTempRepository.findPolicyDetailsAndIsActive(dto.getPolicyId());
			if (memberEntity != null && !memberEntity.isEmpty()) {
				for (MemberMasterEntity memberMasterEntity : memberEntity) {
					MemberMasterTempEntity masterResponse = new MemberMasterTempEntity();
					masterResponse.setMemberId(null);
					masterResponse.setPolicyId(policyEntity);
					masterResponse.setMasterMemberId(memberMasterEntity.getMemberId());
					masterResponse.setMembershipNumber(memberMasterEntity.getMembershipNumber());
					masterResponse.setMemberStatus(memberMasterEntity.getMemberStatus());
					masterResponse.setLicId(memberMasterEntity.getLicId());
					masterResponse.setTypeOfMemebership(memberMasterEntity.getTypeOfMemebership());
					masterResponse.setAadharNumber(memberMasterEntity.getAadharNumber());
					masterResponse.setCategoryNo(memberMasterEntity.getCategoryNo());
					masterResponse.setCommunicationPreference(memberMasterEntity.getCommunicationPreference());
					masterResponse.setLanguagePreference(memberMasterEntity.getLanguagePreference());
					masterResponse.setDateOfBirth(memberMasterEntity.getDateOfBirth());
					masterResponse.setDateOfJoining(memberMasterEntity.getDateOfJoining());
					masterResponse.setDateOfJoiningScheme(memberMasterEntity.getDateOfJoiningScheme());
					masterResponse.setDateOfRetrirement(memberMasterEntity.getDateOfRetrirement());
					masterResponse.setDesignation(memberMasterEntity.getDesignation());
					masterResponse.setEmailid(memberMasterEntity.getEmailid());
					masterResponse.setGender(memberMasterEntity.getGender());
					masterResponse.setFatherName(memberMasterEntity.getFatherName());
					masterResponse.setFirstName(memberMasterEntity.getFirstName());
					masterResponse.setMiddleName(memberMasterEntity.getMiddleName());
					masterResponse.setLastName(memberMasterEntity.getLastName());
					masterResponse.setSpouseName(memberMasterEntity.getSpouseName());
					masterResponse.setMaritalStatus(memberMasterEntity.getMaritalStatus());
					masterResponse.setMemberPan(memberMasterEntity.getMemberPan());
					masterResponse.setLandlineNo(memberMasterEntity.getLandlineNo());
					masterResponse.setMobileNo(memberMasterEntity.getMobileNo());
					masterResponse.setIsZeroid(memberMasterEntity.getIsZeroid());
					masterResponse.setIsActive(memberMasterEntity.getIsActive());
					masterResponse.setCreatedBy(memberMasterEntity.getCreatedBy());
					masterResponse.setCreatedOn(memberMasterEntity.getCreatedOn());
					masterResponse.setModifiedBy(memberMasterEntity.getModifiedBy());
					masterResponse.setModifiedOn(memberMasterEntity.getModifiedOn());
					memberMasterTempRepository.save(masterResponse);
					responseDto.setTransactionStatus(PolicyConstants.SUCCESS);
					responseDto.setTransactionMessage(PolicyConstants.SAVEMESSAGE);
				}
			} else {
				logger.info("AdjustmentContributionServiceImpl:saveMemberMasterTemp:: Policy Id: {},",dto.getPolicyId());
				responseDto.setTransactionMessage("no member data");
				responseDto.setTransactionStatus(PolicyConstants.SUCCESS);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:AdjustmentContributionServiceImpl:saveMasterTemp", e);

		} finally {
			logger.info("AdjustmentContributionServiceImpl:saveMemberMasterTemp:End:: Policy Id: {}" ,dto.getPolicyId());
		}
		return responseDto;
	}

	@Override
	public void download(Long batchId, String fileType, HttpServletResponse response) {
		try {
			logger.info("AdjustmentContributionServiceImpl -- downloadAdjustmentContributionRecords -- start");
			if (batchId != null && fileType != null) {
				AdjustmentContributionBatchEntity batch = adjustmentContributionBatchRepository.findByBatchIdAndIsActiveTrue(batchId);
				if (batch != null) {
					byte[] bytes = null;
					String fileName = "";
					switch (fileType) {
					case "failed":
						bytes = batch.getFailedFile();
						fileName = "Failed_Adjustment_Contributions";
						break;
					case "success":
						bytes = batch.getSuccessFile();
						fileName = "Success_Adjustment_Contributions";
						break;
					default:
						bytes = batch.getRawFile();
						fileName = "Raw_Adjustment_Contributions";
					}
					response.setContentType("Content-Type: text/csv");
					response.setHeader("Content-Disposition","attachment;filename=" + fileName + "_" + batchId + ".csv");
					ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
					baos.write(bytes, 0, bytes.length);
					OutputStream os = response.getOutputStream();
					response.setHeader(PolicyConstants.STATUS_STRING, "File is Downloaded Successfully!!!");
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
			logger.info("AdjustmentContributionServiceImpl -- downloadAdjustmentContributionRecords -- exception", e);
		}
		logger.info("AdjustmentContributionServiceImpl -- downloadAdjustmentContributionRecords -- end");
	}
	
	
	
	
	@Override
	public AdjustmentContributionResponseDto newcitrieaSearchById(String policyNumber, String unitId, String receivedFrom ,String receivedTo,String adjustedFrom, String adjustedTo) {

		AdjustmentContributionResponseDto contributionDto = new AdjustmentContributionResponseDto();
		List<ACPolicyDepositAdjustmentDto> depositAdjustmentDtoList = new ArrayList<>();
		try {
			logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearchById :: Starts");
			
			List<Object[]> result = adjRepo.getPolicyDepositAdjustment(policyNumber, unitId, receivedFrom, receivedTo,adjustedFrom,adjustedTo);
						
			if(result != null && !result.isEmpty()){
				for (Object object : result) {
					Object[] ob = (Object[]) object;
					ACPolicyDepositAdjustmentDto adjustmentContributionDto = new ACPolicyDepositAdjustmentDto();
					adjustmentContributionDto.setPolicyId(NumericUtils.convertStringToLong(String.valueOf(ob[0])));
					adjustmentContributionDto.setUnitCode(String.valueOf(ob[1]));
					adjustmentContributionDto.setPolicyNumber(String.valueOf(ob[2]));
					adjustmentContributionDto.setMphName(String.valueOf(ob[3]));
					adjustmentContributionDto.setCollectionNo(String.valueOf(ob[4]));
					adjustmentContributionDto.setCollectionDate(String.valueOf(ob[5]));
						
//					String valueOf = String.valueOf(ob[6].toString());
//					Long valueOf2 = Long.valueOf(valueOf);
//					double bigDecimal=valueOf2.doubleValue();
//					BigDecimal decimal=new BigDecimal(bigDecimal);
//					adjustmentContributionDto.setDepositAmount(decimal);
					adjustmentContributionDto.setDepositAmount(new BigDecimal((Long.valueOf(String.valueOf(ob[6].toString())).doubleValue())));

//					adjustmentContributionDto.setDepositAmount(NumericUtils.convertBigDecimalToString(ob[6].toString()));
					adjustmentContributionDto.setContributionId(NumericUtils.convertStringToLong(String.valueOf(ob[7])));
					
//					adjustmentContributionDto.setDepositAmount(new BigDecimal((Long.valueOf(String.valueOf(ob[6].toString())).doubleValue())));
					BigDecimal TotalContribution  = new BigDecimal((Long.valueOf(String.valueOf(ob[8]!=null?ob[8]:"0")).doubleValue()));
					adjustmentContributionDto.setTotalContribution(TotalContribution);
//					adjustmentContributionDto.setTotalContribution(new BigDecimal((Long.valueOf(String.valueOf(ob[8].toString())).doubleValue())));
					adjustmentContributionDto.setCreatedOn(String.valueOf(ob[9]));
					adjustmentContributionDto.setNewMembers(NumericUtils.convertStringToLong(String.valueOf(ob[10])));
					adjustmentContributionDto.setRenewedMembers(NumericUtils.convertStringToLong(String.valueOf(ob[11])));
					adjustmentContributionDto.setNewPremium(NumericUtils.convertStringToLong(String.valueOf(ob[12])));
					adjustmentContributionDto.setRenewedPremium(NumericUtils.convertStringToLong(String.valueOf(ob[13])));					
                    adjustmentContributionDto.setVoucherNumber(String.valueOf(ob[14]));	
                    adjustmentContributionDto.setVoucherDate((String.valueOf(ob[15])!=null?String.valueOf(ob[15]):"")); 
                    depositAdjustmentDtoList.add(adjustmentContributionDto);
                    }				
			}
			contributionDto.setPolicyDepositAdjustmentDtos(depositAdjustmentDtoList);
			contributionDto.setTransactionMessage(CommonConstants.FETCH);
			contributionDto.setTransactionStatus(CommonConstants.STATUS);			
		} catch (Exception e) {
			logger.error("Exception :: AdjustmentContributionServiceImpl :: newcitrieaSearchById", e);
			contributionDto.setTransactionStatus(CommonConstants.FAIL);
			contributionDto.setTransactionStatus(CommonConstants.ERROR);
		}finally {
			logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearchById :: End");
		}
		return contributionDto;
	
	}
	
	
	@Override
	public ByteArrayInputStream policyDepositAdjustment(String policyNumber, String unitId, String receivedFrom ,String receivedTo,String adjustedFrom, String adjustedTo) throws IOException {

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();

		sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 1));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 15));
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 16, 17));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 15));
		sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 5));
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 5));
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 2));
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 3, 5));
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 6, 8));
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 2));
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 3, 5));
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 6, 8));
		sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 2));
		sheet.addMergedRegion(new CellRangeAddress(9, 9, 0, 2));
		
		InputStream islicLogo = new ClassPathResource("liclogo.png").getInputStream();
		byte[] bytes = IOUtils.toByteArray(islicLogo);
		int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		islicLogo.close();
		CreationHelper helper = wb.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(0);
		anchor.setRow1(0);
		anchor.setCol2(1);
		anchor.setRow2(1);
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize(0.30);

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

		Row headerRow0 = sheet.createRow(0);
		Row headerRow1 = sheet.createRow(3);
		Row headerRow2 = sheet.createRow(4);
		Row headerRow3 = sheet.createRow(5);
		Row headerRow4 = sheet.createRow(6);
		Row headerRow5 = sheet.createRow(7);
		Row headerRow6 = sheet.createRow(8);
		Row headerRow7 = sheet.createRow(9);
		Row headerRow = sheet.createRow(10);

		List<Object[]> acObjectList = adjRepo.getPolicyDepositAdjustment(policyNumber, unitId, receivedFrom, receivedTo,adjustedFrom,adjustedTo);

		headerRow0.createCell(2).setCellValue(" LIFE INSURANCE CORPORATION OF INDIA");
		headerRow0.createCell(16).setCellValue(dateFormat.format(new Date()));
		headerRow1.createCell(1).setCellValue(" Policy_Deposit_Adjustment");
		headerRow2.createCell(0).setCellValue(" Name of the Insurer : L.I.C. of India");
		headerRow3.createCell(0).setCellValue(" Date of Submission : " + DateUtils.dateToStringDDMMYYYY(new Date()));
		headerRow4.createCell(0).setCellValue(" Deposit Created : " + DateUtils.dateToStringDDMMYYYY(new Date()));
		headerRow4.createCell(3).setCellValue(" Received From : " + DateUtils.dateToStringDDMMYYYY(new Date()));
		headerRow4.createCell(6).setCellValue(" Received To : " + DateUtils.dateToStringDDMMYYYY(new Date()));
		headerRow5.createCell(0).setCellValue(" Deposit Adjusted : " + DateUtils.dateToStringDDMMYYYY(new Date()));
		headerRow5.createCell(3).setCellValue(" Adjusted From : " + DateUtils.dateToStringDDMMYYYY(new Date()));
		headerRow5.createCell(6).setCellValue(" Adjusted To : " + DateUtils.dateToStringDDMMYYYY(new Date()));
		headerRow6.createCell(0).setCellValue(" UNIT: " + unitId);
		headerRow7.createCell(0).setCellValue(" POLICY NUMBER: " + policyNumber);
		String[] headerCells = { " Unit Code", " Policy Number", " Name Of Master Policy Holder", " Deposit Number"," Deposit Date", " Deposit Amount", " Adjustment/Batch No.", " Adjusted Amount", " Adjustment Date"," New Members", " Renewed Members", " New Premium", " Renewal Premium", " Voucher No. (Accounts)"," Voucher Date (Accounts)" };
		for (int i = 0; i < headerCells.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headerCells[i]);
		}
		int rowNum = 11;
		for (Object[] obj : acObjectList) {
			Row row2 = sheet.createRow(rowNum++);
			for (int i = 1; i < 14; i++) {
				Cell cell = row2.createCell(i - 1);
				if (obj[i] != null) {
					cell.setCellValue(obj[i].toString());
				}
			}
		}
		
		
		 ByteArrayOutputStream stream = new ByteArrayOutputStream();
		 wb.write(stream);
	     return new ByteArrayInputStream(stream.toByteArray());
				
//		String file = tempPdfLocation + "PolicyDepositDetails.xls"; // Excel File Download Path
//		if (wb instanceof XSSFWorkbook) {
//			file += "x";
//			try (OutputStream fileOut = new FileOutputStream(file)) {
//				wb.write(fileOut);
//			}
//		}
//		return null;
	}
	
	@Value("${temp.pdf.location}")
	private String tempPdfLocation;
	
	
	
	
	
	@Override
	public AdjustmentContributionBatchHistoryResponse getBatchHistory(Long adjustmentId) {
		
		logger.info("AdjustmentContributionServiceImpl :: getBatchHistory :: Start");

		
		AdjustmentContributionBatchHistoryResponse response = new AdjustmentContributionBatchHistoryResponse();

		List<AdjustmentContributionBulkResponseDto> responseList = new ArrayList<>();
		
		
		try{
			
			List<AdjustmentContributionBatchEntity> batches = adjustmentContributionBatchRepository
					.findAllByAdjustmentContributionIdAndIsActiveTrue(adjustmentId);
			
			if(!batches.isEmpty()) {
				
				for(AdjustmentContributionBatchEntity batch : batches) {
					
					AdjustmentContributionBulkResponseDto dto = new AdjustmentContributionBulkResponseDto();
					dto.setBatchId(batch.getBatchId());
					dto.setSuccessCount(batch.getSuccessCount());
					dto.setFailedCount(batch.getFailedCount());
					dto.setTotalCount(batch.getTotalCount());
					dto.setFileName(batch.getFileName());
					dto.setAdjustmentContributionId(batch.getAdjustmentContributionId());
					
					List<AdjustmentContributionBulkErrorEntity> errorList = ajustmentContributionBulkErrorRepository.findAllByBatchIdAndIsActiveTrue(batch.getBatchId());
					
					if(!errorList.isEmpty()) {
						dto.setError(mapList(errorList, AdjustmentContributionBulkErrorDto.class));
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
			
		}catch (Exception e) {
			
			logger.error("Exception :: AdjustmentContributionServiceImpl :: getBatchHistory", e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionMessage("Exception --> +");
			
		}
		
		logger.info("AdjustmentContributionServiceImpl :: getBatchHistory :: End");

		return response;
	}
	
	@Override
	public AdjustmentContributionResponseDto makerReject(Long adjustmentContributionId,
			 String modifiedBy, String rejectionRemarks) {
		logger.info("AdjustmentContributionServiceImpl :: makerReject :: Start");
		AdjustmentContributionResponseDto adjustmentContributionResponseDto = new AdjustmentContributionResponseDto();
		try {
			AdjustmentContributionTempEntity adjContriTempEntity = adjustmentContributionTempRepository.findByAdjustmentContributionIdAndIsActiveTrue(adjustmentContributionId);
		    if(adjContriTempEntity != null) {
		    	adjContriTempEntity.setModifiedBy(modifiedBy);
		    	adjContriTempEntity.setModifiedOn(DateUtils.sysDate());
		    	adjContriTempEntity.setAdjustmentContributionStatus(AdjustmentContibutionConstants.MAKER_REJECT);
		    	adjContriTempEntity.setRejectionRemarks(rejectionRemarks);
		    	adjContriTempEntity.setIsActive(false);
				AdjustmentContributionTempEntity saveEntity = adjustmentContributionTempRepository.save(adjContriTempEntity);
				AdjustmentContributionDto dto = modelMapper.map(saveEntity, AdjustmentContributionDto.class);
				AdjustmentContributionAllDto adjustmentContributionAllDto = new AdjustmentContributionAllDto();
				adjustmentContributionAllDto.setAdjustmentContribution(dto);
				adjustmentContributionAllDto.setAdjustmentContributionId(dto.getAdjustmentContributionId());
				adjustmentContributionResponseDto.setResponseData(adjustmentContributionAllDto);
				adjustmentContributionResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				adjustmentContributionResponseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
		    }
		    else {
		    	adjustmentContributionResponseDto.setTransactionStatus(CommonConstants.ERROR);
		    	adjustmentContributionResponseDto.setTransactionMessage(CommonConstants.FAIL);
			}
		}
		catch (Exception e) {
			logger.error("Exception :: AdjustmentContributionServiceImpl :: makerReject", e);
			adjustmentContributionResponseDto.setTransactionStatus(CommonConstants.FAIL);
			adjustmentContributionResponseDto.setTransactionStatus(CommonConstants.ERROR);
		}finally {
			logger.info("AdjustmentContributionServiceImpl :: makerReject :: End");
		}
		return adjustmentContributionResponseDto;
	}

	

}