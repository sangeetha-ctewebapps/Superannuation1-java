package com.lic.epgs.policy.service.impl;

/**
 * @author pradeepramesh
 *
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionResponseDto;
import com.lic.epgs.common.dto.CommonResponseDto;
import com.lic.epgs.common.dto.DataTable;
import com.lic.epgs.common.dto.FundRequestDto;
import com.lic.epgs.common.entity.IcodeMasterEntity;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.common.repository.IcodeMasterRepository;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.common.service.impl.CommonServiceImpl;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.fund.constants.InterestFundConstants;
import com.lic.epgs.fund.service.FundRestApiService;
import com.lic.epgs.payout.entity.AnnuityCreationRequestResponseStoreEntity;
import com.lic.epgs.payout.repository.AnnuityCreationReqAndResRepository;
import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.policy.dao.MemberMasterDao;
import com.lic.epgs.policy.dto.CommonResponseStampDto;
import com.lic.epgs.policy.dto.ContributionRequestDto;
import com.lic.epgs.policy.dto.ContributionResponseDto;
import com.lic.epgs.policy.dto.DepositRefundPolicySearchRequestDto;
import com.lic.epgs.policy.dto.DepositRefundPolicySearchResponseDto;
import com.lic.epgs.policy.dto.GetPolicyMemberDetailsRequestDto;
import com.lic.epgs.policy.dto.GetPolicyMemberDetailsResponseDto;
import com.lic.epgs.policy.dto.IssuePolicyRequestDto;
import com.lic.epgs.policy.dto.IssuePolicyResponseDto;
import com.lic.epgs.policy.dto.MemberContributionDto;
import com.lic.epgs.policy.dto.MemberMasterDto;
import com.lic.epgs.policy.dto.MphAddressDto;
import com.lic.epgs.policy.dto.MphBankDto;
import com.lic.epgs.policy.dto.MphMasterDto;
import com.lic.epgs.policy.dto.PolicyDetailSearchRequestDto;
import com.lic.epgs.policy.dto.PolicyDetailSearchResponseDto;
import com.lic.epgs.policy.dto.PolicyDetailsResponseDto;
import com.lic.epgs.policy.dto.PolicyFrequencyDetailsDto;
import com.lic.epgs.policy.dto.PolicyNotesDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.dto.PolicySearchResponseDto;
import com.lic.epgs.policy.dto.PolicyShowDepositDto;
import com.lic.epgs.policy.dto.ProposalAnnuityDto;
import com.lic.epgs.policy.dto.StampDutyConsumptionRequestDto;
import com.lic.epgs.policy.dto.TrnRegistrationRequestDto;
import com.lic.epgs.policy.dto.TrnRegistrationResponseDto;
import com.lic.epgs.policy.dto.policyContributionExportDto;
import com.lic.epgs.policy.entity.MemberContributionEntity;
import com.lic.epgs.policy.entity.MemberContributionTempEntity;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MemberMasterTempEntity;
import com.lic.epgs.policy.entity.MphAddressEntity;
import com.lic.epgs.policy.entity.MphAddressTempEntity;
import com.lic.epgs.policy.entity.MphBankEntity;
import com.lic.epgs.policy.entity.MphBankTempEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.MphMasterTempEntity;
import com.lic.epgs.policy.entity.PolicyContributionSummaryEntity;
import com.lic.epgs.policy.entity.PolicyContributionSummaryTempEntity;
import com.lic.epgs.policy.entity.PolicyContributionTempEntity;
import com.lic.epgs.policy.entity.PolicyDepositEntity;
import com.lic.epgs.policy.entity.PolicyDepositTempEntity;
import com.lic.epgs.policy.entity.PolicyFrequencyDetailsEntity;
import com.lic.epgs.policy.entity.PolicyFrequencyDetailsTempEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterTempEntity;
import com.lic.epgs.policy.entity.PolicyNotesEntity;
import com.lic.epgs.policy.entity.PolicyNotesTempEntity;
import com.lic.epgs.policy.entity.PolicyRulesEntity;
import com.lic.epgs.policy.entity.PolicyValuationEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntriesTempEntity;
import com.lic.epgs.policy.entity.ZeroAccountTempEntity;
import com.lic.epgs.policy.old.dto.PolicyAddressOldDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentOldDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentRequestDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentResponse;
import com.lic.epgs.policy.old.dto.PolicyBankOldDto;
import com.lic.epgs.policy.old.dto.PolicyDepositOldDto;
import com.lic.epgs.policy.old.dto.PolicyDto;
import com.lic.epgs.policy.old.dto.PolicyFundStmtRequestDto;
import com.lic.epgs.policy.old.dto.PolicyFundStmtResponseDto;
import com.lic.epgs.policy.old.dto.PolicyNotesOldDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policy.old.dto.PolicyStmtDto;
import com.lic.epgs.policy.repository.MemberContributionRepository;
import com.lic.epgs.policy.repository.MemberContributionTempRepository;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.MemberMasterTempRepository;
import com.lic.epgs.policy.repository.MphAddressRepository;
import com.lic.epgs.policy.repository.MphAddressTempRepository;
import com.lic.epgs.policy.repository.MphBankRepository;
import com.lic.epgs.policy.repository.MphBankTempRepository;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.MphMasterTempRepository;
import com.lic.epgs.policy.repository.PolicyContributionRepository;
import com.lic.epgs.policy.repository.PolicyContributionSummaryRepository;
import com.lic.epgs.policy.repository.PolicyContributionSummaryTempRepository;
import com.lic.epgs.policy.repository.PolicyContributionTempRepository;
import com.lic.epgs.policy.repository.PolicyDepositRepository;
import com.lic.epgs.policy.repository.PolicyDepositTempRepository;
import com.lic.epgs.policy.repository.PolicyFrequencyRepository;
import com.lic.epgs.policy.repository.PolicyFrequencyTempRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.policy.repository.PolicyMasterTempRepository;
import com.lic.epgs.policy.repository.PolicyNotesRepository;
import com.lic.epgs.policy.repository.PolicyNotesTempRepository;
import com.lic.epgs.policy.repository.PolicyRulesRepository;
import com.lic.epgs.policy.repository.PolicyTransactionEntriesRepository;
import com.lic.epgs.policy.repository.PolicyTransactionSummaryRepository;
import com.lic.epgs.policy.repository.PolicyValuationRepository;
import com.lic.epgs.policy.repository.ZeroAccountEntriesTempRepository;
import com.lic.epgs.policy.repository.ZeroAccountTempRepository;
import com.lic.epgs.policy.service.PolicyService;
import com.lic.epgs.quotation.dto.QuotationApiResponseDto;
import com.lic.epgs.quotation.entity.QuotationEntity;
import com.lic.epgs.quotation.repository.QuotationRepository;
import com.lic.epgs.quotation.service.impl.QuotationServiceImpl;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionResponseDto;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

@Service
@Transactional
public class PolicyServiceImpl implements PolicyService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	CommonService commonService;
	@Autowired
	RestTemplate restTemplateService;
	@Autowired
	Environment environment;
	@Autowired
	EntityManager entityManager;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	QuotationServiceImpl quotationServiceImpl;
	@Autowired
	PolicyCommonServiceImpl policyCommonServiceImpl;
	@Autowired
	QuotationRepository quotationRepository;
	@Autowired
	MphMasterRepository mphMasterRepository;
	@Autowired
	MphMasterTempRepository mphMasterTempRepository;
	@Autowired
	MphAddressTempRepository mphAddressTempRepository;
	@Autowired
	MphBankTempRepository mphBankTempRepository;
	@Autowired
	PolicyMasterTempRepository policyMasterTempRepository;
	@Autowired
	PolicyMasterRepository policyMasterRepository;
	@Autowired
	PolicyDepositTempRepository policyDepositTempRepository;
	@Autowired
	PolicyContributionTempRepository policyContributionTempRepository;
	@Autowired
	PolicyContributionSummaryTempRepository policyContributionSummaryTempRepository;
	@Autowired
	ZeroAccountTempRepository zeroAccountTempRepository;
	@Autowired
	ZeroAccountEntriesTempRepository zeroAccountEntriesTempRepository;
	@Autowired
	PolicyNotesTempRepository policyNotesTempRepo;
	@Autowired
	MemberMasterTempRepository memberMasterTempRepository;
	@Autowired
	MemberContributionTempRepository memberContributionTempRepository;
	@Autowired
	MemberMasterRepository memberMasterRepository;
	@Autowired
	PolicyContributionRepository policyContributionRepository;
	@Autowired
	MemberContributionRepository memberContributionRepository;
	@Autowired
	MphAddressRepository mphAddressRepository;
	@Autowired
	MphBankRepository mphBankRepository;
	@Autowired
	PolicyRulesRepository policyRulesRepository;
	@Autowired
	PolicyContributionSummaryRepository policyContributionSummaryRepository;
	@Autowired
	PolicyValuationRepository policyValuationRepository;
	@Autowired
	PolicyDepositRepository policyDepositRepository;
	@Autowired
	PolicyTransactionSummaryRepository policyTransactionSummaryRepository;
	@Autowired
	PolicyNotesRepository policyNotesRepository;
	@Autowired
	PolicyTransactionEntriesRepository policyTransactionEntriesRepository;
	@Autowired
	@Qualifier("jsonObjectMapper")
	ObjectMapper objectMapper;
	@Autowired
	FundRestApiService fundRestApiService;
	@Autowired
	CommonServiceImpl commonServiceImpl;
	@Autowired
	PolicyFrequencyTempRepository policyFrequencyTempRepository;
	@Autowired
	PolicyFrequencyRepository policyFrequencyRepository;
	@Autowired
	IntegrationService integrationService;
	@Autowired
	AnnuityCreationReqAndResRepository annuityCreationReqAndResRepository;

	@Autowired
	IcodeMasterRepository icodeMasterRepository;

	@Autowired
	MemberMasterDao memberMasterDao;

	JdbcTemplate jdbcTemplate;

	@Autowired
	public PolicyServiceImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//	
//
//	public void setDataSource(DataSource dataSource) {
//		this.jdbcTemplate = new JdbcTemplate(dataSource);
//	}

	public synchronized String getPolicySeq() {
		return commonService.getSequence(CommonConstants.POLICY_MODULE);
	}

	public synchronized String getChallanSeq() {
		return commonService.getSequence(CommonConstants.CHALLAN_SEQ);
	}

	public synchronized String getZeroIdSeq() {
		return commonService.getSequence(CommonConstants.ZERO_ACT_SEQ);
	}

	@Override
	public PolicyResponseDto savePolicyDetails(PolicyDto policyDto) throws ParseException {
		PolicyResponseDto response = new PolicyResponseDto();
		logger.info("PolicyServiceImpl:savePolicyDetails---Started::{}", policyDto.getQuotationId());
		if (policyDto.getQuotationId() == null) {
			response.setTransactionMessage(PolicyConstants.QUOTATION_NUMBER_EMPTY);
			response.setTransactionStatus(PolicyConstants.FAIL);
			return response;
		} else {
			Optional<QuotationEntity> quotationOpt = quotationRepository.findByQuotationIdAndStatusAndIsActiveTrue(
					policyDto.getQuotationId(), NumericUtils.convertStringToInteger(PolicyConstants.APPROVED_NO));
			if (!quotationOpt.isPresent()) {
				response.setTransactionMessage(PolicyConstants.QUOTATION_INVALID);
				response.setTransactionStatus(PolicyConstants.FAIL);
				return response;
			}
		}
		MphMasterDto mphMasterDto = null;
		MphMasterTempEntity mphTempEntity = null;
		if (policyDto.getPolicyId() == null) {
			mphMasterDto = policyCommonServiceImpl.convertOldRequestToNewRequest(policyDto);
			mphTempEntity = mphMasterTempRepository
					.save(policyCommonServiceImpl.convertMphMasterDtoToMphMasterTempEntity(mphMasterDto));
			if (mphTempEntity.getMphId() != null) {
				convertQutationMemberToPolicyMember(mphTempEntity.getMphId(), policyDto, policyDto.getVariantType());
			}
		} else {
			List<PolicyContributionTempEntity> policyContribution = policyContributionTempRepository
					.findByPolicyIdAndIsActiveTrue(policyDto.getPolicyId());
			List<PolicyContributionSummaryTempEntity> policyContributionSummary = policyContributionSummaryTempRepository
					.findAllByPolicyIdAndIsActiveTrue(policyDto.getPolicyId());
			List<MemberMasterTempEntity> memberMaster = memberMasterTempRepository
					.findAllByPolicyIdAndIsActiveTrue(policyDto.getPolicyId());
			mphMasterDto = policyCommonServiceImpl.convertEditRequestToNewRequest(policyDto, policyContribution,
					policyContributionSummary, memberMaster);
			mphTempEntity = mphMasterTempRepository
					.save(policyCommonServiceImpl.convertMphMasterDtoToMphMasterTempEntity(mphMasterDto));
		}
		for (PolicyMasterTempEntity policyMasterTempEntity : mphTempEntity.getPolicyMaster()) {
			PolicyFrequencyDetailsDto qwerty = new PolicyFrequencyDetailsDto();
			qwerty.setPolicyId(policyMasterTempEntity.getPolicyId());
			qwerty.setFrequency(NumericUtils.convertIntegerToString(policyMasterTempEntity.getContributionFrequency()));
			getFrequencyDates(qwerty);
		}
		response.setMphId(mphTempEntity.getMphId());
		response.setTransactionMessage(PolicyConstants.SAVEMESSAGE);
		response.setTransactionStatus(PolicyConstants.SUCCESS);
		return response;
	}

	private void convertQutationMemberToPolicyMember(Long mphId, PolicyDto policyDto, String variantType) {
		MphMasterTempEntity mphEntity = mphMasterTempRepository.findAllByMphIdAndIsActiveTrue(mphId);
		PolicyMasterTempEntity policyTempEntity = policyMasterTempRepository
				.findAllByMphIdAndIsActiveTrue(mphEntity.getMphId());
		Optional<QuotationEntity> quotationOpt = quotationRepository.findByQuotationIdAndStatusAndIsActiveTrue(
				policyTempEntity.getQuotationId(), NumericUtils.convertStringToInteger(PolicyConstants.APPROVED_NO));
		if (!quotationOpt.isPresent()) {
			logger.info(" Quotation Is not present - QuotationId - {}", policyTempEntity.getQuotationId());
			return;
		}
		QuotationEntity quotationEntity = quotationOpt.get();
		Set<MemberMasterTempEntity> qwert = policyCommonServiceImpl.convertQuoMemEntityToMemMasEntity(
				quotationEntity.getMembers(), policyTempEntity.getPolicyId(), null, policyDto.getCreatedBy(),
				variantType);
		memberMasterTempRepository.saveAll(qwert);
	}

	@Override
	public PolicyResponseDto savePolicyOldDto(Long mphId) {
		PolicyResponseDto response = new PolicyResponseDto();
		MphMasterTempEntity qwert = mphMasterTempRepository.findByMphIdAndIsActiveTrue(mphId);
		MphMasterDto mphMasterDto = policyCommonServiceImpl.convertMphMasterTempEntityToMphMasterDto(qwert);
		PolicyDto poilcDto = policyCommonServiceImpl.convertNewResponseToOldResponse(mphMasterDto);
		poilcDto.getDeposit().removeIf(deposit -> PolicyConstants.ADJESTED.equalsIgnoreCase(deposit.getStatus()));
		poilcDto.getMembers().removeIf(member -> Boolean.TRUE.equals(member.getIsZeroId()));
		poilcDto.getAdjustments()
				.removeIf(deposits -> PolicyConstants.DEPOSITSTATUSNEW.equalsIgnoreCase(deposits.getStatus()));
		String quotationNumber = policyCommonServiceImpl.getQuotationNumber(poilcDto.getQuotationId());
		poilcDto.setQuotationNo(quotationNumber);
		response.setPolicyDto(poilcDto);
		response.setPolicyId(response.getPolicyDto().getPolicyId());
		response.setMphId(mphId);
		response.setTransactionStatus(CommonConstants.SUCCESS);
		response.setTransactionMessage(CommonConstants.SAVEMESSAGE);
		return response;
	}

	@Override
	public PolicyResponseDto saveNotesDetails(PolicyNotesDto policyNotesDto) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		try {
			logger.info("PolicyServiceImpl:saveNotesDetails:Starts");
			PolicyNotesTempEntity policyNotesTempEntity = modelMapper.map(policyNotesDto, PolicyNotesTempEntity.class);
			PolicyNotesTempEntity savePolicyNotesTempEntity = policyNotesTempRepo.save(policyNotesTempEntity);
			commonDto.setPolicyNotesDto(modelMapper.map(savePolicyNotesTempEntity, PolicyNotesOldDto.class));
			commonDto.setPolicyId(savePolicyNotesTempEntity.getPolicyId());
			commonDto.setTransactionStatus(CommonConstants.SUCCESS);
			commonDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);

		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:saveNotesDetails", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:saveNotesDetails:Ends");
		}
		return commonDto;
	}

	@Override
	public PolicyResponseDto getNoteList(Long policyId) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		List<PolicyNotesOldDto> oldDtos = new ArrayList<>();
		try {
			logger.info("PolicyServiceImpl:saveNotesDetails:Starts");
			List<PolicyNotesTempEntity> policyEntity = policyNotesTempRepo.findAllByPolicyId(policyId);
			if (policyEntity != null) {
				for (PolicyNotesTempEntity entity : policyEntity) {
					PolicyNotesOldDto newDto = new PolicyNotesOldDto();
					newDto.setPolicyNoteId(entity.getPolicyNoteId());
					newDto.setPolicyId(entity.getPolicyId());
					newDto.setNoteContents(entity.getNoteContents());
					newDto.setModifiedBy(entity.getModifiedBy());
					newDto.setModifiedOn(entity.getModifiedOn());
					newDto.setCreatedBy(entity.getCreatedBy());
					newDto.setCreatedOn(entity.getCreatedOn());
					newDto.setIsActive(entity.getIsActive());
					oldDtos.add(newDto);
				}
				commonDto.setPolicyNotesList(oldDtos);
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonDto.setTransactionStatus(CommonConstants.ERROR);
				commonDto.setTransactionMessage(CommonConstants.DENY);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:getNoteList", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:saveNotesDetails:Ends");
		}
		return commonDto;
	}

	@Override
	public PolicyResponseDto saveAddressDetails(PolicyAddressOldDto addressDto) {
		PolicyResponseDto responseDto = new PolicyResponseDto();
		try {
			MphAddressDto mphAddressNewDto = new MphAddressDto();
			mphAddressNewDto = convertOldBankToMphBankDto(addressDto, mphAddressNewDto);
			MphAddressTempEntity mphAddressTempEntity = modelMapper.map(mphAddressNewDto, MphAddressTempEntity.class);
			mphAddressTempEntity.setIsActive(true);
			mphAddressTempEntity.setIsDefault(true);
			MphAddressTempEntity saveAddressEntity = mphAddressTempRepository.save(mphAddressTempEntity);
			MphAddressDto mphAdressDto = modelMapper.map(saveAddressEntity, MphAddressDto.class);
			PolicyAddressOldDto oldDto = new PolicyAddressOldDto();
			oldDto.setAddressId(mphAdressDto.getAddressId());
			oldDto.setAddressLine1(mphAdressDto.getAddressLine1());
			oldDto.setAddressLine2(mphAdressDto.getAddressLine2());
			oldDto.setAddressLine3(mphAdressDto.getAddressLine3());
			oldDto.setAddressType(mphAdressDto.getAddressType());
			oldDto.setCityId(mphAdressDto.getCityId());
			oldDto.setDistrictId(mphAdressDto.getDistrictId());
			oldDto.setCountryId(mphAdressDto.getCountryId());
			oldDto.setStateId(mphAdressDto.getStateId());
			oldDto.setPinCode(NumericUtils.convertIntegerToString(mphAdressDto.getPincode()));
			oldDto.setTownLocality(mphAdressDto.getCityLocality());
			oldDto.setCreatedBy(mphAdressDto.getCreatedBy());
			oldDto.setCreatedOn(mphAdressDto.getCreatedOn());
			oldDto.setModifiedBy(mphAdressDto.getModifiedBy());
			oldDto.setModifiedOn(mphAdressDto.getModifiedOn());
			oldDto.setMphId(mphAdressDto.getMphId());
			responseDto.setPolicyAddressDto(oldDto);
			responseDto.setTransactionStatus(PolicyConstants.SUCCESS);
			responseDto.setTransactionMessage(PolicyConstants.SAVEMESSAGE);
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:saveAddressDetails", e);
			responseDto.setTransactionMessage(PolicyConstants.FAIL);
			responseDto.setTransactionStatus(PolicyConstants.ERROR);
		}
		return responseDto;
	}

	private MphAddressDto convertOldBankToMphBankDto(PolicyAddressOldDto addressDto, MphAddressDto mphAddressNewDto) {
		mphAddressNewDto.setAddressId(addressDto.getAddressId());
		mphAddressNewDto.setAddressLine1(addressDto.getAddressLine1());
		mphAddressNewDto.setAddressLine2(addressDto.getAddressLine2());
		mphAddressNewDto.setAddressLine3(addressDto.getAddressLine3());
		mphAddressNewDto.setAddressType(addressDto.getAddressType());
		mphAddressNewDto.setCityId(addressDto.getCityId());
		mphAddressNewDto.setDistrictId(addressDto.getDistrictId());
		mphAddressNewDto.setCountryId(addressDto.getCountryId());
		mphAddressNewDto.setStateId(addressDto.getStateId());
		mphAddressNewDto.setPincode(NumericUtils.convertStringToInteger(addressDto.getPinCode()));
		mphAddressNewDto.setCityLocality(addressDto.getTownLocality());
		mphAddressNewDto.setCreatedBy(addressDto.getCreatedBy());
		mphAddressNewDto.setCreatedOn(addressDto.getCreatedOn());
		mphAddressNewDto.setModifiedBy(addressDto.getModifiedBy());
		mphAddressNewDto.setModifiedOn(addressDto.getModifiedOn());
		mphAddressNewDto.setMphId(addressDto.getMphId());
		return mphAddressNewDto;
	}

	@Override
	public PolicyResponseDto getAddressList(Long mphId) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		List<PolicyAddressOldDto> dtos = new ArrayList<>();
		List<MphAddressDto> addressDtos = new ArrayList<>();
		try {
			logger.info("PolicyServiceImpl:getAddressList:Starts");
			List<MphAddressTempEntity> policyAddressEntity = mphAddressTempRepository
					.findAllByMphIdAndIsActiveTrue(mphId);
			if (policyAddressEntity != null) {
				for (MphAddressTempEntity entity : policyAddressEntity) {
					addressDtos.add(modelMapper.map(entity, MphAddressDto.class));
				}
				for (MphAddressDto dto : addressDtos) {
					dtos.add(modelMapper.map(dto, PolicyAddressOldDto.class));
				}
				commonDto.setAddressList(dtos);
				commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				commonDto.setTransactionMessage(PolicyConstants.FETCH);
			} else {
				commonDto.setTransactionStatus(PolicyConstants.ERROR);
				commonDto.setTransactionMessage(PolicyConstants.DENY);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:getAddressList", e);
			commonDto.setTransactionMessage(PolicyConstants.FAIL);
			commonDto.setTransactionStatus(PolicyConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:getAddressList:Ends");
		}
		return commonDto;
	}

	@Override
	public PolicyResponseDto saveBankDetails(PolicyBankOldDto policyBankDto) {
		PolicyResponseDto responseDto = new PolicyResponseDto();
		try {
			MphBankDto bankDto = new MphBankDto();
			bankDto = convertPolicyBankToMphBankDto(policyBankDto, bankDto);
			MphBankTempEntity entity = mphBankTempRepository.findByMphBankId(bankDto.getMphBankId());
			if (entity != null) {
				MphBankTempEntity policyNew = modelMapper.map(bankDto, MphBankTempEntity.class);
				MphBankTempEntity banks = new MphBankTempEntity();
				banks.setMphBankId(entity.getMphBankId());
				banks.setAccountNumber(policyNew.getAccountNumber());
				banks.setAccountType(policyNew.getAccountType());
				banks.setIfscCode(policyNew.getIfscCode());
				banks.setBankName(policyNew.getBankName());
				banks.setBankBranch(policyNew.getBankBranch());
				banks.setBankAddress(policyNew.getBankAddress());
				banks.setStdCode(policyNew.getStdCode());
				banks.setLandlineNumber(policyNew.getLandlineNumber());
				banks.setEmailId(policyNew.getEmailId());
				banks.setCountryId(policyNew.getCountryId());
				banks.setStateId(policyNew.getStateId());
				banks.setDistrictId(policyNew.getDistrictId());
				banks.setCityId(policyNew.getCityId());
				banks.setTownLocality(policyNew.getTownLocality());
				banks.setIsActive(policyNew.getIsActive());
				banks.setModifiedBy(policyNew.getModifiedBy());
				banks.setModifiedOn(policyNew.getModifiedOn());
				banks.setCreatedBy(policyNew.getCreatedBy());
				banks.setMphId(entity.getMphId());
				banks.setCreatedOn(policyNew.getCreatedOn());
				MphBankTempEntity updateEntity = mphBankTempRepository.save(banks);
				MphBankDto dto = modelMapper.map(updateEntity, MphBankDto.class);
				PolicyBankOldDto oldDto = new PolicyBankOldDto();
				oldDto = convertNewDtoToOld(dto, oldDto);
				responseDto.setPolicyBankDto(oldDto);
				responseDto.setTransactionStatus(PolicyConstants.SUCCESS);
				responseDto.setTransactionMessage(PolicyConstants.UPDATEMESSAGE);
			} else {
				MphBankTempEntity policyNew = modelMapper.map(bankDto, MphBankTempEntity.class);
				MphBankTempEntity saveEntity = mphBankTempRepository.save(policyNew);
				MphBankDto dto = modelMapper.map(saveEntity, MphBankDto.class);
				PolicyBankOldDto oldDto = new PolicyBankOldDto();
				oldDto = convertNewDtoToOld(dto, oldDto);
				responseDto.setPolicyBankDto(oldDto);
				responseDto.setTransactionStatus(PolicyConstants.SUCCESS);
				responseDto.setTransactionMessage(PolicyConstants.SAVEMESSAGE);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:saveBankDetails", e);
			responseDto.setTransactionMessage(PolicyConstants.FAIL);
			responseDto.setTransactionStatus(PolicyConstants.ERROR);
		}
		return responseDto;
	}

	private MphBankDto convertPolicyBankToMphBankDto(PolicyBankOldDto policyBankDto, MphBankDto bankDto) {
		bankDto.setMphBankId(policyBankDto.getBankAccountId());
		bankDto.setAccountNumber(policyBankDto.getAccountNumber());
		bankDto.setAccountType(policyBankDto.getAccountType());
		bankDto.setIfscCode(policyBankDto.getIfscCode());
		bankDto.setBankName(policyBankDto.getBankName());
		bankDto.setBankBranch(policyBankDto.getBankBranch());
		bankDto.setBankAddress(policyBankDto.getBankAddress());
		bankDto.setStdCode(NumericUtils.convertStringToInteger(policyBankDto.getStdCode()));
		bankDto.setLandlineNumber(NumericUtils.convertStringToLong(policyBankDto.getLandLineNumber()));
		bankDto.setEmailId(policyBankDto.getEmailId());
		bankDto.setCountryId(policyBankDto.getCountryId());
		bankDto.setStateId(policyBankDto.getStateId());
		bankDto.setDistrictId(policyBankDto.getDistrictId());
		bankDto.setCityId(policyBankDto.getCityId());
		bankDto.setTownLocality(policyBankDto.getTownLocality());
		bankDto.setIsActive(policyBankDto.getIsActive());
		bankDto.setModifiedBy(policyBankDto.getModifiedBy());
		bankDto.setModifiedOn(policyBankDto.getModifiedOn());
		bankDto.setCreatedBy(policyBankDto.getCreatedBy());
		bankDto.setCreatedOn(policyBankDto.getCreatedOn());
		bankDto.setMphId(policyBankDto.getMphId());
		return bankDto;
	}

	private PolicyBankOldDto convertNewDtoToOld(MphBankDto dto, PolicyBankOldDto oldDto) {
		oldDto.setBankAccountId(dto.getMphBankId());
		oldDto.setAccountNumber(dto.getAccountNumber());
		oldDto.setAccountType(dto.getAccountType());
		oldDto.setIfscCode(dto.getIfscCode());
		oldDto.setBankName(dto.getBankName());
		oldDto.setBankBranch(dto.getBankBranch());
		oldDto.setBankAddress(dto.getBankAddress());
		oldDto.setStdCode(NumericUtils.convertIntegerToString(dto.getStdCode()));
		oldDto.setLandLineNumber(NumericUtils.convertLongToString(dto.getLandlineNumber()));
		oldDto.setEmailId(dto.getEmailId());
		oldDto.setCountryId(dto.getCountryId());
		oldDto.setStateId(dto.getStateId());
		oldDto.setDistrictId(dto.getDistrictId());
		oldDto.setCityId(dto.getCityId());
		oldDto.setTownLocality(dto.getTownLocality());
		oldDto.setIsActive(dto.getIsActive());
		oldDto.setModifiedBy(dto.getModifiedBy());
		oldDto.setModifiedOn(dto.getModifiedOn());
		oldDto.setCreatedBy(dto.getCreatedBy());
		oldDto.setCreatedOn(dto.getCreatedOn());
		oldDto.setMphId(dto.getMphId());
		return oldDto;
	}

	@Override
	public PolicyResponseDto getBankList(Long mphId) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		List<PolicyBankOldDto> dtos = new ArrayList<>();
		List<MphBankDto> bankDtos = new ArrayList<>();
		try {
			logger.info("PolicyServiceImpl:getBankList:Starts");
			List<MphBankTempEntity> policyBankEntity = mphBankTempRepository.findAllByMphIdAndIsActiveTrue(mphId);
			if (policyBankEntity != null) {
				for (MphBankTempEntity entity : policyBankEntity) {
					bankDtos.add(modelMapper.map(entity, MphBankDto.class));
				}
				for (MphBankDto dto : bankDtos) {
					PolicyBankOldDto oldDto = new PolicyBankOldDto();
					oldDto = convertNewDtoToOld(dto, oldDto);
					dtos.add(oldDto);
				}
				commonDto.setBankList(dtos);
				commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				commonDto.setTransactionMessage(PolicyConstants.FETCH);
			} else {
				commonDto.setTransactionStatus(PolicyConstants.ERROR);
				commonDto.setTransactionMessage(PolicyConstants.DENY);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:getBankList", e);
			commonDto.setTransactionMessage(PolicyConstants.FAIL);
			commonDto.setTransactionStatus(PolicyConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:getBankList:Ends");
		}
		return commonDto;
	}

	@Override
	public PolicyResponseDto getPolicyById(String status, Long mphId) {
		logger.info("PolicyServiceImpl:inprogressCitrieaSearch:Starts");
		PolicyResponseDto commonDto = new PolicyResponseDto();
		MphMasterDto mphMasterDto = new MphMasterDto();
		try {
			if (status.equalsIgnoreCase(CommonConstants.INPROGRESS)) {
				MphMasterTempEntity mphMasterTempEntity = mphMasterTempRepository.findByMphIdAndIsActiveTrue(mphId);
				if (mphMasterTempEntity != null) {
					mphMasterDto = policyCommonServiceImpl
							.convertMphMasterTempEntityToMphMasterDto(mphMasterTempEntity);
					PolicyDto poilcDto = policyCommonServiceImpl.convertNewResponseToOldResponse(mphMasterDto);
					poilcDto.getDeposit()
							.removeIf(deposit -> PolicyConstants.ADJESTED.equalsIgnoreCase(deposit.getStatus()));
					poilcDto.getMembers().removeIf(member -> Boolean.TRUE.equals(member.getIsZeroId()));
					poilcDto.getAdjustments().removeIf(
							deposits -> PolicyConstants.DEPOSITSTATUSNEW.equalsIgnoreCase(deposits.getStatus()));
					commonDto.setPolicyDto(poilcDto);
					commonDto.setPolicyId(commonDto.getPolicyDto().getPolicyId());
					commonDto.setMphId(commonDto.getPolicyDto().getMphId());
					commonDto.setTransactionMessage(PolicyConstants.FETCH);
					commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				}
			}
			if (status.equalsIgnoreCase(CommonConstants.EXISTING)) {
				MphMasterEntity mphMasterEntity = mphMasterRepository.findByMphIdAndIsActiveTrue(mphId);
				if (mphMasterEntity != null) {
					mphMasterDto = policyCommonServiceImpl.convertMphMasterEntityToMphMasterDto(mphMasterEntity);
					PolicyDto poilcDto = policyCommonServiceImpl.convertNewResponseToOldResponse(mphMasterDto);
					poilcDto.getDeposit()
							.removeIf(deposit -> PolicyConstants.ADJESTED.equalsIgnoreCase(deposit.getStatus()));
					poilcDto.getMembers().removeIf(member -> Boolean.TRUE.equals(member.getIsZeroId()));
					poilcDto.getAdjustments().removeIf(
							deposits -> PolicyConstants.DEPOSITSTATUSNEW.equalsIgnoreCase(deposits.getStatus()));
					commonDto.setPolicyDto(poilcDto);
					commonDto.setPolicyId(commonDto.getPolicyDto().getPolicyId());
					commonDto.setMphId(commonDto.getPolicyDto().getMphId());
					commonDto.setTransactionMessage(PolicyConstants.FETCH);
					commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				}
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:getPolicyById", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:getPolicyById:Ends");
		}
		return commonDto;
	}

	@Override
	public PolicyResponseDto existingCitrieaSearch(PolicySearchDto policySearchDto) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("PolicyServiceImpl:existingCitrieaSearch:Start");
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
				Predicate onStart = criteriaBuilder.greaterThanOrEqualTo(root.get(PolicyConstants.CREATEDON), fromDate);
				Predicate onEnd = criteriaBuilder.lessThanOrEqualTo(root.get(PolicyConstants.CREATEDON), toDate);
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
			logger.error("Exception:PolicyServiceImpl:existingCitrieaSearch", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:existingCitrieaSearch:Ends");
		}
		return commonDto;
	}

	private PolicySearchResponseDto convertMasterEntityToDto(MphMasterEntity mphMasterEntity) {
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
		return policySearchResponseDto;
	}

	private PolicyDetailSearchResponseDto convertMasterEntityToDtoForPolicySearch(MphMasterEntity mphMasterEntity) {
		PolicyDetailSearchResponseDto policySearchResponseDto = new PolicyDetailSearchResponseDto();
		try {
			policySearchResponseDto.setMphCode(mphMasterEntity.getMphCode());
			policySearchResponseDto.setMphName(mphMasterEntity.getMphName());
			policySearchResponseDto.setPan(mphMasterEntity.getPan());
			policySearchResponseDto.setEmailAddress(mphMasterEntity.getEmailId());
			policySearchResponseDto.setMobileNo(NumericUtils.convertLongToString(mphMasterEntity.getMobileNo()));
			if (!mphMasterEntity.getMphAddress().isEmpty()) {
				MphAddressEntity entity = mphMasterEntity.getMphAddress().stream().findFirst().get();
				if (entity != null) {
					policySearchResponseDto.setAddress(entity.getAddressLine1());
					policySearchResponseDto.setState(NumericUtils.convertIntegerToString(entity.getStateId()));
				}
			}
			Set<PolicyMasterEntity> policyMasterEntity = mphMasterEntity.getPolicyMaster();
			if (!policyMasterEntity.isEmpty()) {
				for (PolicyMasterEntity policyMaster : policyMasterEntity) {
					policySearchResponseDto.setPolicyNumber(policyMaster.getPolicyNumber());
					policySearchResponseDto.setOperatingUnit(policyMaster.getUnitId());
					JsonNode proposalResponse = integrationService
							.getProductDetailsByProductId(policyMaster.getProductId());
					if (proposalResponse != null) {
						JsonNode proposeDetails = proposalResponse.path("responseData");
						String product = proposeDetails.path("productCode").textValue();
						policySearchResponseDto.setProductCode(product);
					}
					JsonNode newResponse = integrationService
							.getVariantDetailsByProductVariantId(NumericUtils.stringToLong(policyMaster.getVariant()));
					if (newResponse != null) {
						JsonNode proposeDetails = newResponse.path("responseData");
						String variantCode = proposeDetails.path("subCategory").textValue();
						policySearchResponseDto.setVariantCode(variantCode);
					}
					JsonNode gstStateResponse = integrationService.getGstStateDetails(policyMaster.getUnitId());
					if (gstStateResponse != null) {
						JsonNode stateDetails = gstStateResponse.path("Data");
						String stateCode = stateDetails.path("stateCode").textValue();
						policySearchResponseDto.setMasterPolicyHolderGstStateCode(stateCode);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception:PolicyServiceImpl:inprogressCitrieaSearch", e);
		}
		return policySearchResponseDto;
	}

	@Override
	public PolicyResponseDto inprogressCitrieaSearch(PolicySearchDto policySearchDto) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("PolicyServiceImpl:inprogressCitrieaSearch:Start");
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<MphMasterTempEntity> createQuery = criteriaBuilder.createQuery(MphMasterTempEntity.class);
			Root<MphMasterTempEntity> root = createQuery.from(MphMasterTempEntity.class);
			Join<MphMasterTempEntity, PolicyMasterTempEntity> join = root.join("policyMaster");

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
			if (StringUtils.isNotBlank(policySearchDto.getProposalNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("proposalNumber")),
						policySearchDto.getProposalNumber().toLowerCase()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getMphCode())) {
				predicates.add(criteriaBuilder.equal(root.get("mphCode"), policySearchDto.getMphCode()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getMphName())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("mphName")),
						policySearchDto.getMphName().toLowerCase()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getRole())
					&& Objects.equals(policySearchDto.getRole().toLowerCase(), (PolicyConstants.MAKER).toLowerCase())) {
				predicates.add(join.get(PolicyConstants.POLICYSTATUS)
						.in(Arrays.asList(PolicyConstants.DRAFT_NO, PolicyConstants.MAKER_NO)));
			}
			if (StringUtils.isNotBlank(policySearchDto.getRole()) && Objects
					.equals(policySearchDto.getRole().toLowerCase(), (PolicyConstants.CHECKER).toLowerCase())) {
				predicates.add(join.get(PolicyConstants.POLICYSTATUS).in(Arrays.asList(PolicyConstants.CHECKER_NO)));
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
			List<MphMasterTempEntity> result = entityManager.createQuery(createQuery).getResultList();

			List<PolicySearchResponseDto> response = result.stream().map(this::convertEntityToDto)
					.collect(Collectors.toList());

			List<PolicyDto> policyDtoList = setInResponse(response);

			commonDto.setPolicyDtos(policyDtoList);
			commonDto.setTransactionMessage(CommonConstants.FETCH);
			commonDto.setTransactionStatus(CommonConstants.STATUS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:inprogressCitrieaSearch", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:inprogressCitrieaSearch:Ends");
		}
		return commonDto;
	}

	private List<PolicyDto> setInResponse(List<PolicySearchResponseDto> response) {
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
		return policyDtoList;
	}

	private PolicySearchResponseDto convertEntityToDto(MphMasterTempEntity mphMasterTempEntity) {
		PolicySearchResponseDto policySearchResponseDto = new PolicySearchResponseDto();
		policySearchResponseDto.setMphCode(mphMasterTempEntity.getMphCode());
		policySearchResponseDto.setMphName(mphMasterTempEntity.getMphName());
		policySearchResponseDto.setMphId(mphMasterTempEntity.getMphId());
		policySearchResponseDto.setProposalNumber(mphMasterTempEntity.getProposalNumber());
		Set<PolicyMasterTempEntity> policyMasterTempEntity = mphMasterTempEntity.getPolicyMaster();
		for (PolicyMasterTempEntity policyMasterTemp : policyMasterTempEntity) {
			policySearchResponseDto.setPolicyId(policyMasterTemp.getPolicyId());
			policySearchResponseDto.setPolicyNumber(policyMasterTemp.getPolicyNumber());
			policySearchResponseDto.setPolicyStatus(policyMasterTemp.getPolicyStatus());
			policySearchResponseDto.setUnitOffice(policyMasterTemp.getUnitId());
			policySearchResponseDto.setProduct(NumericUtils.convertLongToString(policyMasterTemp.getProductId()));
		}
		return policySearchResponseDto;
	}

	@Override
	public PolicyResponseDto changeStatus(Long policyId, String status) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		try {
			Optional<PolicyMasterTempEntity> policyMasterTempOpt = policyMasterTempRepository
					.findByPolicyIdAndIsActiveTrue(policyId);
			if (!policyMasterTempOpt.isPresent()) {
				commonDto.setTransactionMessage(PolicyConstants.DENY);
				commonDto.setTransactionStatus(PolicyConstants.FAIL);
				return commonDto;
			}
			PolicyMasterTempEntity policyMasterTempEntity = policyMasterTempOpt.get();
			if (policyMasterTempEntity != null) {
				policyMasterTempEntity.setPolicyStatus(status);
				policyMasterTempEntity.setModifiedOn(DateUtils.sysDate());
				policyMasterTempRepository.save(policyMasterTempEntity);
				MphMasterTempEntity mphMasterTempEntity = mphMasterTempRepository
						.findByMphIdAndIsActiveTrue(policyMasterTempEntity.getMphId());
				commonDto.setProposalNumber(mphMasterTempEntity.getProposalNumber());
				commonDto.setPolicyId(policyMasterTempEntity.getPolicyId());
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:changeStatus", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:changeStatus:Ends");
		}
		return commonDto;
	}

	@Override
	public PolicyResponseDto policyReject(Long policyId, String reject) {
		PolicyResponseDto responseDto = new PolicyResponseDto();
		try {
			Optional<PolicyMasterTempEntity> tempEntityOpt = policyMasterTempRepository
					.findByPolicyIdAndIsActiveTrue(policyId);
			PolicyMasterTempEntity tempEntity = null;
			if (tempEntityOpt.isPresent()) {
				tempEntity = tempEntityOpt.get();
			} else {
				responseDto.setTransactionMessage(PolicyConstants.DENY);
				responseDto.setTransactionStatus(PolicyConstants.FAIL);
			}

			if (tempEntity != null) {
				MphMasterTempEntity tempMasterEntity = mphMasterTempRepository
						.findByMphIdAndIsActiveTrue(tempEntity.getMphId());
				MphMasterEntity masterEntity = policyCommonServiceImpl
						.convertMphMasterTempEntityToMphMasterMasterEntityNew(tempMasterEntity, reject, null, "");
				MphMasterEntity saveMasterEntity = mphMasterRepository.save(masterEntity);
				for (PolicyMasterEntity policyMasterEntity : saveMasterEntity.getPolicyMaster()) {
					tempEntity.setPolicyId(tempEntity.getPolicyId());
					tempEntity.setPolicyStatus(policyMasterEntity.getPolicyStatus());
					tempEntity.setIsActive(Boolean.FALSE);
					policyMasterTempRepository.save(tempEntity);
				}
				tempMasterEntity.setIsActive(Boolean.FALSE);
				mphMasterTempRepository.save(tempMasterEntity);
				MphMasterDto dto = policyCommonServiceImpl.convertMphMasterEntityToMphMasterDto(saveMasterEntity);
				PolicyDto policyDto = policyCommonServiceImpl.convertNewResponseToOldResponse(dto);
				responseDto.setProposalNumber(saveMasterEntity.getProposalNumber());
				responseDto.setPolicyDto(policyDto);
				responseDto.setTransactionMessage(PolicyConstants.REJECT);
				responseDto.setTransactionStatus(PolicyConstants.SUCCESS);
			}

		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:policyReject", e);
			responseDto.setTransactionStatus(CommonConstants.FAIL);
			responseDto.setTransactionStatus(CommonConstants.ERROR);
		}
		return responseDto;
	}

	@Override
	public PolicyResponseDto policyApproved(Long policyId, String status, String variantType)
			throws ApplicationException {

		PolicyResponseDto responseDto = new PolicyResponseDto();
		try {
			Optional<PolicyMasterTempEntity> tempEntityOpt = policyMasterTempRepository
					.findByPolicyIdAndIsActiveTrue(policyId);
			if (!tempEntityOpt.isPresent()) {
				responseDto.setTransactionMessage(PolicyConstants.DENY);
				responseDto.setTransactionStatus(PolicyConstants.FAIL);
				return responseDto;
			}
			PolicyMasterTempEntity tempEntity = tempEntityOpt.get();
			MphMasterTempEntity tempMasterEntity = mphMasterTempRepository
					.findByMphIdAndIsActiveTrue(tempEntity.getMphId());
			String policyNumber = mphMasterRepository.getPolicyNumber(getPolicySeq());

			StampDutyConsumptionRequestDto requestDto = new StampDutyConsumptionRequestDto();

			requestDto.setConsumerUnitsCode(tempEntity.getUnitId());
			requestDto.setStampAmount(NumericUtils.convertBigDecimalToString((tempEntity.getStampDuty())));
			requestDto.setStream("SA");
			requestDto.setProductCode(tempEntity.getPolicyType());
			requestDto.setPolicyNumber(tempEntity.getPolicyNumber());
			requestDto.setCreatedBy(tempEntity.getModifiedBy());
			requestDto.setCreatedOn(DateUtils.dateToStringDDMMYYYY(new Date()));
			requestDto.setModifiedBy(tempEntity.getModifiedBy());
			requestDto.setModifiedOn(DateUtils.dateToStringDDMMYYYY(new Date()));

			CommonResponseStampDto response = getStampCunsumptionDetails(requestDto);
			if (response != null) {
				if (response.getTransactionStatus() != null && response.getTransactionStatus()
						.equalsIgnoreCase(PolicyConstants.SUCCESSRESPONSEFORSTAMPCONSUMPTION)) {
					MphMasterEntity masterEntity = policyCommonServiceImpl
							.convertMphMasterTempEntityToMphMasterMasterEntityNew(tempMasterEntity, status,
									policyNumber, variantType);
					MphMasterEntity saveMasterEntity = mphMasterRepository.save(masterEntity);
					tempMasterEntity.setMasterMphId(saveMasterEntity.getMphId());
					mphMasterTempRepository.save(tempMasterEntity);

					updateMasterPolicyIdsInTemp(saveMasterEntity.getPolicyMaster(), tempEntity, policyId);

					responseDto.setTransactionMessage(PolicyConstants.SAVEMESSAGE);
					responseDto.setTransactionStatus(PolicyConstants.UPDATEMESSAGE);

					MphMasterDto dto = policyCommonServiceImpl.convertMphMasterEntityToMphMasterDto(saveMasterEntity);
					PolicyDto policyDto = policyCommonServiceImpl.convertNewResponseToOldResponse(dto);

					responseDto.setPolicyNumber(policyDto.getPolicyNumber());
					responseDto.setPolicyDto(policyDto);
					responseDto.setPolicyId(responseDto.getPolicyDto().getPolicyId());
					responseDto.setTransactionMessage(PolicyConstants.APPROVED);
					responseDto.setTransactionStatus(PolicyConstants.SUCCESS);

				} else if (response.getTransactionStatus() != null && response.getTransactionStatus()
						.equalsIgnoreCase(PolicyConstants.FAILURERESPONSEFORSTAMPCONSUMPTION)) {
					responseDto.setTransactionMessage(PolicyConstants.FAILURE_MESSAGE_FORSTAMPCONSUMPTION);
					responseDto.setTransactionStatus(PolicyConstants.FAIL);
				}
			} else {
				throw new ApplicationException(PolicyConstants.SA_STAMPCONSUMPTION_FUND_SERVICE + "No response.");
			}

		} catch (IllegalArgumentException e) {
			responseDto.setTransactionMessage(e.getMessage());
			responseDto.setTransactionStatus(PolicyConstants.ERROR);
			logger.error("policyApproved::", e);
		}
		return responseDto;
	}

	private PolicyMasterTempEntity updateMasterPolicyIdsInTemp(Set<PolicyMasterEntity> policyMaster,
			PolicyMasterTempEntity tempEntity, Long policyId) {
		for (PolicyMasterEntity policyMasterEntity : policyMaster) {
			tempEntity.setPolicyId(tempEntity.getPolicyId());
			tempEntity.setPolicyNumber(policyMasterEntity.getPolicyNumber());
			tempEntity.setPolicyStatus(policyMasterEntity.getPolicyStatus());
			tempEntity.setMasterPolicyId(policyMasterEntity.getPolicyId());

			Set<PolicyDepositTempEntity> deposits = updateDepositPolicyIDs(tempEntity.getDeposits(),
					policyMasterEntity.getPolicyId());
			tempEntity.setDeposits(deposits);

			Set<PolicyContributionTempEntity> policyContributionTempEntities = updateContributionPolicyIDs(
					tempEntity.getPolicyContributions(), policyMasterEntity.getPolicyId());
			tempEntity.setPolicyContributions(policyContributionTempEntities);

			Set<ZeroAccountEntriesTempEntity> entriesTempEntity = updateZeroEntiresPolicyIds(
					tempEntity.getZeroAccountEntries(), policyMasterEntity.getPolicyId());
			tempEntity.setZeroAccountEntries(entriesTempEntity);
			policyMasterTempRepository.save(tempEntity);

			Set<MemberMasterTempEntity> memberMasterTemp = tempEntity.getMemberMaster();
			Set<MemberMasterEntity> memberMaster = policyMasterEntity.getMemberMaster();
			for (MemberMasterEntity memberMasterEntity : memberMaster) {
				for (MemberMasterTempEntity memberMasterTempEntity : memberMasterTemp) {
					if (Objects.equals(memberMasterTempEntity.getLicId(), memberMasterEntity.getLicId())) {
						memberMasterTempEntity.setMasterMemberId(memberMasterEntity.getMemberId());
						memberMasterTempEntity.setPolicyId(policyId);
						memberMasterTempRepository.save(memberMasterTempEntity);
					}
				}
			}
			logger.error("policyApproved::frequencyStatusUpdate::Starts");
			frequencyStatusUpdate(policyId, policyMasterEntity.getPolicyId());
			logger.error("policyApproved::frequencyStatusUpdate::Ends");
		}
		return null;
	}

	private Set<ZeroAccountEntriesTempEntity> updateZeroEntiresPolicyIds(
			Set<ZeroAccountEntriesTempEntity> zeroAccountEntries, Long policyId) {
		Set<ZeroAccountEntriesTempEntity> entriesTempEntity = zeroAccountEntries;
		for (ZeroAccountEntriesTempEntity zeroAccountEntriesTempEntity : entriesTempEntity) {
			zeroAccountEntriesTempEntity.setIsMoved(Boolean.TRUE);
			zeroAccountEntriesTempEntity.setMasterPolicyId(policyId);
			entriesTempEntity.add(zeroAccountEntriesTempEntity);
		}
		return entriesTempEntity;
	}

	private Set<PolicyContributionTempEntity> updateContributionPolicyIDs(
			Set<PolicyContributionTempEntity> policyContributions, Long policyId) {
		Set<PolicyContributionTempEntity> policyContributionTempEntities = policyContributions;
		for (PolicyContributionTempEntity policyContributionTempEntity : policyContributionTempEntities) {
			policyContributionTempEntity.setMasterPolicyId(policyId);
			policyContributionTempEntities.add(policyContributionTempEntity);
		}
		return policyContributionTempEntities;
	}

	private Set<PolicyDepositTempEntity> updateDepositPolicyIDs(Set<PolicyDepositTempEntity> deposits, Long policyId) {
		Set<PolicyDepositTempEntity> deposit = deposits;
		for (PolicyDepositTempEntity policyDepositTempEntity : deposit) {
			policyDepositTempEntity.setMasterPolicyId(policyId);
			deposit.add(policyDepositTempEntity);
		}
		return deposit;
	}

	private void frequencyStatusUpdate(Long policyId, Long masterPolicyId) {
		logger.error("frequencyStatusUpdate::Starts");
		List<PolicyFrequencyDetailsTempEntity> frequencyDetailsTempEntities = policyFrequencyTempRepository
				.findAllByPolicyIdAndIsActiveTrue(policyId);
		for (PolicyFrequencyDetailsTempEntity policyFrequencyDetailsTempEntity : frequencyDetailsTempEntities) {
			PolicyFrequencyDetailsEntity frequencyDetailsEntity = new PolicyFrequencyDetailsEntity();
			frequencyDetailsEntity.setFrequencyId(null);
			frequencyDetailsEntity.setPolicyId(masterPolicyId);
			frequencyDetailsEntity.setFrequency(policyFrequencyDetailsTempEntity.getFrequency());
			frequencyDetailsEntity.setFrequencyDates(policyFrequencyDetailsTempEntity.getFrequencyDates());
			frequencyDetailsEntity.setStatus(policyFrequencyDetailsTempEntity.getStatus());
			frequencyDetailsEntity.setProgress(policyFrequencyDetailsTempEntity.getProgress());
			frequencyDetailsEntity
					.setPolicyCommencementDate(policyFrequencyDetailsTempEntity.getPolicyCommencementDate());
			frequencyDetailsEntity.setPolicyEndDate(policyFrequencyDetailsTempEntity.getPolicyEndDate());
			frequencyDetailsEntity.setModifiedBy(policyFrequencyDetailsTempEntity.getModifiedBy());
			frequencyDetailsEntity.setModifiedOn(new Date());
			frequencyDetailsEntity.setCreatedBy(policyFrequencyDetailsTempEntity.getCreatedBy());
			frequencyDetailsEntity.setCreatedOn(new Date());
			frequencyDetailsEntity.setIsActive(policyFrequencyDetailsTempEntity.getIsActive());
			policyFrequencyRepository.save(frequencyDetailsEntity);
		}
		logger.error("frequencyStatusUpdate::Ends");
	}

	public CommonResponseStampDto getStampCunsumptionDetails(
			StampDutyConsumptionRequestDto stampDutyConsumptionRequestDto) throws ApplicationException {
		CommonResponseStampDto responseDto = new CommonResponseStampDto();
		responseDto.setTransactionStatus(CommonConstants.ERROR);
		logger.info("getStampCunsumptionDetails::Start::{},", stampDutyConsumptionRequestDto.getPolicyNumber());
		try {
			stampDutyConsumptionRequestDto.setStampAmount(
					NumericUtils.convertBigDecimalToString(stampDutyConsumptionRequestDto.getStampAmountBigDecimal()));
			/**
			 * // try { //
			 * logger.info("getStampCunsumptionDetails::writeValueAsString::{},",
			 * stampDutyConsumptionRequestDto.getPolicyNumber()); //
			 * System.out.println(objectMapper.writeValueAsString(
			 * stampDutyConsumptionRequestDto)); // } catch (JsonProcessingException e) { //
			 * logger.error("Error:", e); // }
			 */
			String url = environment.getProperty("STAMP_CONSUMSTION_DETAILS");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				HttpEntity<StampDutyConsumptionRequestDto> entity = new HttpEntity<>(stampDutyConsumptionRequestDto,
						headers);
				CommonResponseStampDto rateResponseDto = restTemplateService.postForObject(url, entity,
						CommonResponseStampDto.class);
				if (rateResponseDto != null) {
					if (StringUtils.isNotBlank(rateResponseDto.getTransactionStatus())
							&& rateResponseDto.getTransactionStatus()
									.equalsIgnoreCase(PolicyConstants.SUCCESSRESPONSEFORSTAMPCONSUMPTION)) {
						return rateResponseDto;
					} else if (StringUtils.isNotBlank(rateResponseDto.getTransactionStatus())
							&& rateResponseDto.getTransactionStatus()
									.equalsIgnoreCase(PolicyConstants.FAILURERESPONSEFORSTAMPCONSUMPTION)) {
						return rateResponseDto;
					} else {
						throw new ApplicationException(PolicyConstants.SA_STAMPCONSUMPTION_FUND_SERVICE + "-"
								+ stampDutyConsumptionRequestDto.getPolicyNumber() + "_"
								+ rateResponseDto.getTransactionMessage());
					}
				} else {
					throw new ApplicationException(PolicyConstants.SA_STAMPCONSUMPTION_FUND_SERVICE + "No response.");
				}
			} else {
				throw new ApplicationException(PolicyConstants.SA_STAMPCONSUMPTION_FUND_SERVICE + URL_EMPTY);
			}
		} catch (HttpClientErrorException | IllegalArgumentException e) {
			responseDto.setTransactionStatus(CommonConstants.ERROR);
			responseDto.setTransactionMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception getStampCunsumptionDetails::End::{},",
					stampDutyConsumptionRequestDto.getPolicyNumber(), e);
		}
		logger.info("getStampCunsumptionDetails::End::{},", stampDutyConsumptionRequestDto.getPolicyNumber());
		return responseDto;
	}

	public HttpHeaders getAuthHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(CommonConstants.AUTHORIZATION, "Bearer ");
		headers.add(CommonConstants.CORELATIONID, UUID.randomUUID().toString());
		headers.add(CommonConstants.BUSINESSCORELATIONID, UUID.randomUUID().toString());
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	public static final String URL_EMPTY = "URL is empty";

	public void saveContributionToTxnEntries(Long policyId) {
		logger.info("PolicyServiceImpl::saveContributionToTxnEntries::Starts");
		FundRequestDto requestDto = new FundRequestDto();
		requestDto.setPolicyId(policyId);
		try {
			fundRestApiService.contirbutionToTransEntries(requestDto);
		} catch (Exception e) {
			logger.error("PolicyServiceImpl::saveContributionToTxnEntries::Ends::", e);
		}
		logger.info("PolicyServiceImpl::saveContributionToTxnEntries::Ends");
	}

	@Override
	public PolicyAdjustmentResponse saveAdjustment(PolicyAdjustmentRequestDto adjustmentDto) {
		PolicyAdjustmentResponse response = new PolicyAdjustmentResponse();
		try {
			logger.info("PolicyServiceImpl:saveAdjustment---Started");
			Optional<PolicyMasterTempEntity> policyOpt = policyMasterTempRepository
					.findById(adjustmentDto.getPolicyId());
			if (!policyOpt.isPresent()) {
				response.setTransactionMessage(PolicyConstants.POLICY_INVALID);
				response.setTransactionStatus(PolicyConstants.FAIL);
				return response;
			}
			PolicyMasterTempEntity policyTempEntity = policyOpt.get();

			Optional<PolicyDepositTempEntity> policyDepositOpt = policyDepositTempRepository
					.findByStatusAndDepositIdAndZeroIdAndPolicyId(PolicyConstants.DEPOSITSTATUSNEW,
							adjustmentDto.getDepositId(), false, adjustmentDto.getPolicyId());
			if (!policyDepositOpt.isPresent()) {
				response.setTransactionMessage(PolicyConstants.DEPOSIT_INVALID);
				response.setTransactionStatus(PolicyConstants.FAIL);
				return response;
			}
			PolicyDepositTempEntity depositTempEntity = policyDepositOpt.get();

			Optional<PolicyContributionSummaryTempEntity> policyContributionSummaryOpt = policyContributionSummaryTempRepository
					.findByPolicyId(adjustmentDto.getPolicyId());
			if (!policyContributionSummaryOpt.isPresent()) {
				response.setTransactionMessage(PolicyConstants.POLICY_INVALID);
				response.setTransactionStatus(PolicyConstants.FAIL);
				return response;
			}
			PolicyContributionSummaryTempEntity policyContributionSummaryTempEntity = policyContributionSummaryOpt
					.get();

			String mphName = mphMasterTempRepository.getMphName(policyTempEntity.getMphId());
			if (mphName == null) {
				mphName = "";
			}

			Date adjustedForDate = policyTempEntity.getAdjustmentDt();
			Long policyId = policyTempEntity.getPolicyId();
			BigDecimal amountToBeAdjusted = policyTempEntity.getAmountToBeAdjusted();
			BigDecimal newDepositAmount = depositTempEntity.getDepositAmount();
			BigDecimal totalContribution = policyContributionSummaryTempEntity.getTotalContribution();
			BigDecimal availableDepositAmt = policyContributionSummaryTempEntity.getClosingBalance();
			BigDecimal newTotalDepositAmt = availableDepositAmt.add(newDepositAmount);
			String contributionType = PolicyConstants.NEWBUISSNESS;

			PolicyContributionTempEntity policyContributionTempEntity = new PolicyContributionTempEntity();
			switch (policyTempEntity.getPolicyType()) {
			case "DB":
				logger.info("PolicyId - {}, PolicyType - {}, Contribution depositAmount {}",
						adjustmentDto.getPolicyId(), policyTempEntity.getPolicyType(), newTotalDepositAmt);

				policyContributionTempEntity.setContributionId(null);
				policyContributionTempEntity.setPolicyId(adjustmentDto.getPolicyId());
				policyContributionTempEntity.setContReferenceNo(depositTempEntity.getChallanNo());
				policyContributionTempEntity.setContributionType(contributionType);
				policyContributionTempEntity.setContributionDate(adjustedForDate);
				policyContributionTempEntity.setOpeningBalance(BigDecimal.ZERO);
				policyContributionTempEntity
						.setEmployerContribution(policyContributionSummaryTempEntity.getTotalEmployerContribution());
				policyContributionTempEntity
						.setEmployeeContribution(policyContributionSummaryTempEntity.getTotalEmployeeContribution());
				policyContributionTempEntity
						.setVoluntaryContribution(policyContributionSummaryTempEntity.getTotalVoluntaryContribution());
				policyContributionTempEntity.setTotalContribution(totalContribution);
				policyContributionTempEntity
						.setClosingBalance(policyContributionTempEntity.getOpeningBalance().add(totalContribution));

				policyContributionTempEntity.setIsDeposit(Boolean.TRUE);
				policyContributionTempEntity.setFinancialYear(DateUtils.getFinancialYrByDt(new Date()));
				policyContributionTempEntity.setVersionNo(1);
				policyContributionTempEntity.setCreatedOn(new Date());
				policyContributionTempEntity.setCreatedBy(adjustmentDto.getRole());
				policyContributionTempEntity.setModifiedOn(new Date());
				policyContributionTempEntity.setModifiedBy(adjustmentDto.getRole());
				policyContributionTempEntity.setIsActive(Boolean.TRUE);
				policyContributionTempEntity.setTxnEntryStatus(Boolean.FALSE);
				if (adjustmentDto.getVariantType().equalsIgnoreCase("V2")) {
					policyContributionTempEntity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
				} else {
					policyContributionTempEntity.setQuarter("Q0");
				}
				policyContributionTempRepository.save(policyContributionTempEntity);

				depositTempEntity.setStatus(PolicyConstants.ADJESTED);
				depositTempEntity.setContributionType(contributionType);
				depositTempEntity.setAdjustmentAmount(amountToBeAdjusted);
				depositTempEntity
						.setAvailableAmount(newDepositAmount.subtract(depositTempEntity.getAdjustmentAmount()));
				depositTempEntity.setIsDeposit(Boolean.TRUE);
				policyDepositTempRepository.save(depositTempEntity);

				policyContributionSummaryTempEntity.setClosingBalance(newTotalDepositAmt);
				policyContributionSummaryTempEntity.setIsActive(Boolean.TRUE);
				policyContributionSummaryTempEntity.setModifiedOn(new Date());
				policyContributionSummaryTempEntity.setModifiedBy(adjustmentDto.getRole());
				policyContributionSummaryTempRepository.save(policyContributionSummaryTempEntity);

				saveAdjustment(depositTempEntity, policyContributionTempEntity, policyContributionSummaryTempEntity);
				response.setTotalDeposit(policyContributionSummaryTempEntity.getClosingBalance());
				break;
			case "DC":
				switch (newTotalDepositAmt.compareTo(totalContribution)) {
				case -1:
					logger.info("PolicyId - {}, PolicyType - {}, Contribution depositAmount is less: {}",
							adjustmentDto.getPolicyId(), policyTempEntity.getPolicyType(), newTotalDepositAmt);

					policyContributionTempEntity.setOpeningBalance(BigDecimal.ZERO);
					policyContributionTempEntity.setEmployerContribution(
							policyContributionSummaryTempEntity.getTotalEmployerContribution());
					policyContributionTempEntity.setEmployeeContribution(
							policyContributionSummaryTempEntity.getTotalEmployeeContribution());
					policyContributionTempEntity.setVoluntaryContribution(
							policyContributionSummaryTempEntity.getTotalVoluntaryContribution());
					policyContributionTempEntity.setTotalContribution(totalContribution);
					policyContributionTempEntity
							.setClosingBalance(policyContributionTempEntity.getOpeningBalance().add(totalContribution));

					policyContributionTempEntity.setContributionType(PolicyConstants.NEWBUISSNESS);
					policyContributionTempEntity.setContReferenceNo(depositTempEntity.getChallanNo());
					policyContributionTempEntity.setContributionDate(adjustedForDate);
					policyContributionTempEntity.setIsDeposit(Boolean.TRUE);
					policyContributionTempEntity.setVersionNo(policyContributionTempEntity.getVersionNo() + 1);
					if (adjustmentDto.getVariantType().equalsIgnoreCase("V2")) {
						policyContributionTempEntity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
					} else {
						policyContributionTempEntity.setQuarter("Q0");
					}
					policyContributionTempRepository.save(policyContributionTempEntity);

					depositTempEntity.setStatus(PolicyConstants.ADJESTED);
					depositTempEntity.setAdjustmentAmount(amountToBeAdjusted);
					depositTempEntity
							.setAvailableAmount(newDepositAmount.subtract(depositTempEntity.getAdjustmentAmount()));
					depositTempEntity.setContributionType(contributionType);
					depositTempEntity.setIsDeposit(Boolean.TRUE);
					policyDepositTempRepository.save(depositTempEntity);

					saveAdjustment(depositTempEntity, policyContributionTempEntity,
							policyContributionSummaryTempEntity);
					response.setTotalDeposit(policyContributionTempEntity.getClosingBalance());
					break;
				case 0:
					logger.info("PolicyId - {}, PolicyType - {}, Contribution depositAmount is equals: {}",
							adjustmentDto.getPolicyId(), policyTempEntity.getPolicyType(), newTotalDepositAmt);

					policyContributionTempEntity.setContributionId(null);
					policyContributionTempEntity.setPolicyId(adjustmentDto.getPolicyId());
					policyContributionTempEntity.setContReferenceNo(depositTempEntity.getChallanNo());
					policyContributionTempEntity.setContributionType(contributionType);
					policyContributionTempEntity.setContributionDate(adjustedForDate);
					policyContributionTempEntity.setOpeningBalance(BigDecimal.ZERO);
					policyContributionTempEntity.setEmployerContribution(
							policyContributionSummaryTempEntity.getTotalEmployerContribution());
					policyContributionTempEntity.setEmployeeContribution(
							policyContributionSummaryTempEntity.getTotalEmployeeContribution());
					policyContributionTempEntity.setVoluntaryContribution(
							policyContributionSummaryTempEntity.getTotalVoluntaryContribution());
					policyContributionTempEntity.setTotalContribution(totalContribution);
					policyContributionTempEntity
							.setClosingBalance(policyContributionTempEntity.getOpeningBalance().add(totalContribution));
					policyContributionTempEntity.setIsDeposit(Boolean.TRUE);
					policyContributionTempEntity.setFinancialYear(DateUtils.getFinancialYrByDt(new Date()));

					if (adjustmentDto.getVariantType().equalsIgnoreCase("V2")) {
						policyContributionTempEntity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
					} else {
						policyContributionTempEntity.setQuarter("Q0");
					}

					policyContributionTempEntity.setVersionNo(1);
					policyContributionTempEntity.setCreatedOn(new Date());
					policyContributionTempEntity.setCreatedBy(adjustmentDto.getRole());
					policyContributionTempEntity.setModifiedOn(new Date());
					policyContributionTempEntity.setModifiedBy(adjustmentDto.getRole());
					policyContributionTempEntity.setIsActive(Boolean.TRUE);
					policyContributionTempEntity.setTxnEntryStatus(Boolean.FALSE);
					policyContributionTempRepository.save(policyContributionTempEntity);

					depositTempEntity.setStatus(PolicyConstants.ADJESTED);
					depositTempEntity.setAdjustmentAmount(amountToBeAdjusted);
					depositTempEntity
							.setAvailableAmount(newDepositAmount.subtract(depositTempEntity.getAdjustmentAmount()));
					depositTempEntity.setContributionType(contributionType);
					depositTempEntity.setIsDeposit(Boolean.TRUE);
					depositTempEntity.setModifiedOn(new Date());
					depositTempEntity.setModifiedBy(adjustmentDto.getRole());
					policyDepositTempRepository.save(depositTempEntity);

					policyContributionSummaryTempEntity.setClosingBalance(newTotalDepositAmt);
					policyContributionSummaryTempEntity.setIsActive(Boolean.TRUE);
					policyContributionSummaryTempEntity.setModifiedOn(new Date());
					policyContributionSummaryTempEntity.setModifiedBy(adjustmentDto.getRole());
					policyContributionSummaryTempRepository.save(policyContributionSummaryTempEntity);

					List<MemberContributionTempEntity> memberContributionOpt = memberContributionTempRepository
							.findByPolicyId(adjustmentDto.getPolicyId());
					if (memberContributionOpt.isEmpty()) {
						response.setTransactionMessage(PolicyConstants.POLICY_INVALID);
						response.setTransactionStatus(PolicyConstants.FAIL);
						return response;
					}
					for (MemberContributionTempEntity member : memberContributionOpt) {
						member.setPolicyConId(policyContributionTempEntity.getContributionId());
						member.setContributionDate(adjustedForDate);
						member.setContributionType(policyContributionTempEntity.getContributionType());
						member.setFinancialYear(policyContributionTempEntity.getFinancialYear());
						member.setQuarter(policyContributionTempEntity.getQuarter());
						member.setIsDeposit(Boolean.TRUE);
						memberContributionTempRepository.save(member);
					}
					saveAdjustment(depositTempEntity, policyContributionTempEntity,
							policyContributionSummaryTempEntity);
					response.setTotalDeposit(policyContributionTempEntity.getClosingBalance());
					break;
				case 1:
					logger.info("PolicyId - {}, PolicyType - {}, Contribution depositAmount is Excess: {}",
							adjustmentDto.getPolicyId(), policyTempEntity.getPolicyType(), newTotalDepositAmt);

					if (!"1".equals(adjustmentDto.getZeroActId())) {
						logger.info("PolicyId - {}, PolicyType - {}, depositAmount is moved back to deposit: {}",
								adjustmentDto.getPolicyId(), policyTempEntity.getPolicyType(), newTotalDepositAmt);

						policyContributionTempEntity.setContributionId(null);
						policyContributionTempEntity.setPolicyId(adjustmentDto.getPolicyId());
						policyContributionTempEntity.setContReferenceNo(depositTempEntity.getChallanNo());
						policyContributionTempEntity.setContributionType(contributionType);
						policyContributionTempEntity.setContributionDate(adjustedForDate);
						policyContributionTempEntity.setOpeningBalance(BigDecimal.ZERO);
						policyContributionTempEntity.setEmployerContribution(
								policyContributionSummaryTempEntity.getTotalEmployerContribution());
						policyContributionTempEntity.setEmployeeContribution(
								policyContributionSummaryTempEntity.getTotalEmployeeContribution());
						policyContributionTempEntity.setVoluntaryContribution(
								policyContributionSummaryTempEntity.getTotalVoluntaryContribution());
						policyContributionTempEntity.setTotalContribution(totalContribution);
						policyContributionTempEntity.setClosingBalance(
								policyContributionTempEntity.getOpeningBalance().add(totalContribution));
						policyContributionTempEntity.setIsDeposit(Boolean.TRUE);
						policyContributionTempEntity.setFinancialYear(DateUtils.getFinancialYrByDt(new Date()));

						if (adjustmentDto.getVariantType().equalsIgnoreCase("V2")) {
							policyContributionTempEntity
									.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
						} else {
							policyContributionTempEntity.setQuarter("Q0");
						}

						policyContributionTempEntity.setVersionNo(1);

						policyContributionTempEntity.setCreatedOn(new Date());
						policyContributionTempEntity.setCreatedBy(adjustmentDto.getRole());
						policyContributionTempEntity.setModifiedOn(new Date());
						policyContributionTempEntity.setModifiedBy(adjustmentDto.getRole());
						policyContributionTempEntity.setIsActive(Boolean.TRUE);
						policyContributionTempEntity.setTxnEntryStatus(Boolean.FALSE);
						policyContributionTempRepository.save(policyContributionTempEntity);

						depositTempEntity.setStatus(PolicyConstants.ADJESTED);
						depositTempEntity.setAdjustmentAmount(amountToBeAdjusted);
						depositTempEntity
								.setAvailableAmount(newDepositAmount.subtract(depositTempEntity.getAdjustmentAmount()));
						depositTempEntity.setContributionType(contributionType);
						depositTempEntity.setIsDeposit(Boolean.TRUE);
						depositTempEntity.setModifiedOn(new Date());
						depositTempEntity.setModifiedBy(adjustmentDto.getRole());
						policyDepositTempRepository.save(depositTempEntity);

						policyContributionSummaryTempEntity.setClosingBalance(newTotalDepositAmt);
						policyContributionSummaryTempEntity.setIsActive(Boolean.TRUE);
						policyContributionSummaryTempEntity.setModifiedOn(new Date());
						policyContributionSummaryTempEntity.setModifiedBy(adjustmentDto.getRole());
						policyContributionSummaryTempRepository.save(policyContributionSummaryTempEntity);

						memberContributionOpt = memberContributionTempRepository
								.findByPolicyId(adjustmentDto.getPolicyId());
						if (memberContributionOpt.isEmpty()) {
							response.setTransactionMessage(PolicyConstants.POLICY_INVALID);
							response.setTransactionStatus(PolicyConstants.FAIL);
							return response;
						}
						for (MemberContributionTempEntity member : memberContributionOpt) {
							member.setPolicyConId(policyContributionTempEntity.getContributionId());
							member.setContributionDate(adjustedForDate);
							member.setContributionType(policyContributionTempEntity.getContributionType());
							member.setFinancialYear(policyContributionTempEntity.getFinancialYear());
							if (adjustmentDto.getVariantType().equalsIgnoreCase("V2")) {
								member.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
							} else {
								member.setQuarter("Q0");
							}
							member.setIsDeposit(Boolean.TRUE);
							memberContributionTempRepository.save(member);
						}
					} else {
						logger.info("PolicyId - {}, PolicyType - {}, depositAmount is moved to Zero Acoount: {}",
								adjustmentDto.getPolicyId(), policyTempEntity.getPolicyType(),
								newDepositAmount.subtract(depositTempEntity.getAdjustmentAmount()));

						policyContributionTempEntity.setContributionId(null);
						policyContributionTempEntity.setPolicyId(adjustmentDto.getPolicyId());

						policyContributionTempEntity.setContReferenceNo(depositTempEntity.getChallanNo());
						policyContributionTempEntity.setContributionType(contributionType);
						policyContributionTempEntity.setContributionDate(adjustedForDate);

						policyContributionTempEntity.setOpeningBalance(BigDecimal.ZERO);
						policyContributionTempEntity.setEmployerContribution(
								policyContributionSummaryTempEntity.getTotalEmployerContribution());
						policyContributionTempEntity.setEmployeeContribution(
								policyContributionSummaryTempEntity.getTotalEmployeeContribution());
						policyContributionTempEntity.setVoluntaryContribution(
								policyContributionSummaryTempEntity.getTotalVoluntaryContribution());
						policyContributionTempEntity.setTotalContribution(totalContribution);
						policyContributionTempEntity.setClosingBalance(
								policyContributionTempEntity.getOpeningBalance().add(totalContribution));
						policyContributionTempEntity.setIsDeposit(Boolean.TRUE);
						policyContributionTempEntity.setFinancialYear(DateUtils.getFinancialYrByDt(new Date()));

						if (adjustmentDto.getVariantType().equalsIgnoreCase("V2")) {
							policyContributionTempEntity
									.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
						} else {
							policyContributionTempEntity.setQuarter("Q0");
						}

						policyContributionTempEntity.setVersionNo(1);

						policyContributionTempEntity.setCreatedOn(new Date());
						policyContributionTempEntity.setCreatedBy(adjustmentDto.getRole());
						policyContributionTempEntity.setModifiedOn(new Date());
						policyContributionTempEntity.setModifiedBy(adjustmentDto.getRole());
						policyContributionTempEntity.setIsActive(Boolean.TRUE);
						policyContributionTempEntity.setTxnEntryStatus(Boolean.FALSE);
						policyContributionTempRepository.save(policyContributionTempEntity);

						depositTempEntity.setStatus(PolicyConstants.ADJESTED);
						depositTempEntity.setAdjustmentAmount(amountToBeAdjusted);
						depositTempEntity
								.setAvailableAmount(newDepositAmount.subtract(depositTempEntity.getAvailableAmount()));
						depositTempEntity.setContributionType(contributionType);
						depositTempEntity.setIsDeposit(Boolean.TRUE);
						depositTempEntity.setZeroId(Boolean.TRUE);
						depositTempEntity.setModifiedOn(new Date());
						depositTempEntity.setModifiedBy(adjustmentDto.getRole());
						policyDepositTempRepository.save(depositTempEntity);

						policyContributionSummaryTempEntity.setClosingBalance(newTotalDepositAmt);
						policyContributionSummaryTempEntity.setIsActive(Boolean.TRUE);
						policyContributionSummaryTempEntity.setModifiedOn(new Date());
						policyContributionSummaryTempEntity.setModifiedBy(adjustmentDto.getRole());
						policyContributionSummaryTempRepository.save(policyContributionSummaryTempEntity);

						memberContributionOpt = memberContributionTempRepository
								.findByPolicyId(adjustmentDto.getPolicyId());
						if (memberContributionOpt.isEmpty()) {
							response.setTransactionMessage(PolicyConstants.POLICY_INVALID);
							response.setTransactionStatus(PolicyConstants.FAIL);
							return response;
						}
						for (MemberContributionTempEntity member : memberContributionOpt) {
							member.setPolicyConId(policyContributionTempEntity.getContributionId());
							member.setContributionDate(adjustedForDate);
							member.setContributionType(policyContributionTempEntity.getContributionType());
							member.setFinancialYear(policyContributionTempEntity.getFinancialYear());
							member.setQuarter(policyContributionTempEntity.getQuarter());
							member.setIsDeposit(Boolean.TRUE);
							memberContributionTempRepository.save(member);
						}

						if ("DC".equals(policyTempEntity.getPolicyType())) {
							Set<ZeroAccountTempEntity> zeroAccount = policyTempEntity.getZeroAccount();
							if (zeroAccount.isEmpty()) {
								BigDecimal zeroAmount = newDepositAmount
										.subtract(depositTempEntity.getAdjustmentAmount());
								String role = adjustmentDto.getRole();
								Long policyContributionId = policyContributionTempEntity.getContributionId();
								Long memberContibutionId = null;
								String type = policyContributionTempEntity.getContributionType();
								checkZeroAccountInMember(policyTempEntity.getPolicyId(), zeroAmount, role,
										policyContributionId, memberContibutionId, type, mphName);
							}
						}
					}
					saveAdjustment(depositTempEntity, policyContributionTempEntity,
							policyContributionSummaryTempEntity);
					response.setTotalDeposit(policyContributionTempEntity.getClosingBalance());
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}

			List<PolicyFrequencyDetailsTempEntity> policyEntity = policyFrequencyTempRepository
					.findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyIdDesc(policyId, CommonConstants.UNPAID);
			PolicyFrequencyDetailsTempEntity frequencyEntity = new PolicyFrequencyDetailsTempEntity();
			if (!policyEntity.isEmpty() && policyEntity.get(0) != null) {
				frequencyEntity = policyEntity.get(0);
			}
			frequencyEntity.setStatus(CommonConstants.PAID);
			policyFrequencyTempRepository.save(frequencyEntity);
		} catch (IllegalArgumentException e) {
			logger.error("PolicyServiceImpl:saveAdjustment---ERROR", e);
			response.setTransactionMessage(PolicyConstants.FAIL);
			response.setTransactionStatus(PolicyConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:saveAdjustment--Ends");
		}
		return response;
	}

	private PolicyAdjustmentOldDto saveAdjustment(PolicyDepositTempEntity depositTempEntity,
			PolicyContributionTempEntity policyContributionTempEntity,
			PolicyContributionSummaryTempEntity policyContributionSummaryTemp) {
		PolicyAdjustmentOldDto response = new PolicyAdjustmentOldDto();
		policyContributionSummaryTemp.setPolContSummaryId(policyContributionSummaryTemp.getPolContSummaryId() != null
				? policyContributionSummaryTemp.getPolContSummaryId()
				: null);
		policyContributionSummaryTemp.setPolicyId(depositTempEntity.getPolicyId());
		policyContributionSummaryTemp.setOpeningBalance(policyContributionTempEntity.getOpeningBalance());
		policyContributionSummaryTemp
				.setTotalEmployerContribution(policyContributionTempEntity.getEmployerContribution());
		policyContributionSummaryTemp
				.setTotalEmployeeContribution(policyContributionTempEntity.getEmployeeContribution());
		policyContributionSummaryTemp
				.setTotalVoluntaryContribution(policyContributionTempEntity.getVoluntaryContribution());
		policyContributionSummaryTemp.setTotalContribution(policyContributionTempEntity.getTotalContribution());
		policyContributionSummaryTemp.setClosingBalance(policyContributionTempEntity.getClosingBalance());
		policyContributionSummaryTemp.setFinancialYear(policyContributionTempEntity.getFinancialYear());
		policyContributionSummaryTemp.setQuarter(policyContributionTempEntity.getQuarter());
		policyContributionSummaryTemp.setIsActive(Boolean.TRUE);
		policyContributionSummaryTemp.setCreatedOn(new Date());
		policyContributionSummaryTemp.setCreatedBy(depositTempEntity.getCreatedBy());
		policyContributionSummaryTemp.setModifiedOn(new Date());
		policyContributionSummaryTemp.setModifiedBy(depositTempEntity.getModifiedBy());
		policyContributionSummaryTemp.setStampDuty(policyContributionSummaryTemp.getStampDuty() != null
				&& policyContributionSummaryTemp.getStampDuty() != BigDecimal.ZERO
						? policyContributionSummaryTemp.getStampDuty()
						: calculateStamps(policyContributionSummaryTemp.getTotalContribution()));
		policyContributionSummaryTempRepository.save(policyContributionSummaryTemp);
		return response;
	}

	private void checkZeroAccountInMember(Long policyId, BigDecimal zeroIdAmount, String role,
			Long policyContributionId, Long memberContibutionId, String type, String mPhName) {
		logger.info("PolicyServiceImpl:checkZeroAccountInMember--Starts");
		Optional<ZeroAccountTempEntity> zeroAccountsOpt = zeroAccountTempRepository
				.findByIsActiveTrueAndPolicyId(policyId);
		if (!zeroAccountsOpt.isPresent()) {
			ZeroAccountTempEntity zeroMember = convertSetZeroAcc(policyId, zeroIdAmount, role);
			zeroAccountTempRepository.save(zeroMember);
			MemberMasterTempEntity memberMasterTempEntity = setMemberForZeroRow(policyId, mPhName, role);
			memberMasterTempRepository.save(memberMasterTempEntity);
			ZeroAccountEntriesTempEntity zeroMemberEntries = convertSetZeroAccEntries(policyId, zeroIdAmount, role,
					policyContributionId, memberContibutionId, type);
			zeroAccountEntriesTempRepository.save(zeroMemberEntries);
		} else {
			logger.info("PolicyServiceImpl:checkZeroAccountInMember--Ends");
		}
	}

	private ZeroAccountTempEntity convertSetZeroAcc(Long policyId, BigDecimal zeroIdAmount, String role) {
		ZeroAccountTempEntity response = new ZeroAccountTempEntity();
		response.setZeroAccId(null);
		response.setZeroIdAmount(zeroIdAmount);
		response.setPolicyId(policyId);
		response.setCreatedBy(role);
		response.setCreatedOn(new Date());
		response.setModifiedBy(role);
		response.setModifiedOn(new Date());
		response.setIsActive(Boolean.TRUE);
		return response;
	}

	private MemberMasterTempEntity setMemberForZeroRow(Long policyId, String mPhName, String role) {
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
		return tempEntity;
	}

	private ZeroAccountEntriesTempEntity convertSetZeroAccEntries(Long policyId, BigDecimal zeroIdAmount, String role,
			Long policyContributionId, Long memberContibutionId, String type) {
		logger.info("PolicyServiceImpl:convertSetZeroAccEntries:Start");
		ZeroAccountEntriesTempEntity response = new ZeroAccountEntriesTempEntity();
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
		logger.info("PolicyServiceImpl:convertSetZeroAccEntries:Start");
		return response;
	}

	@Override
	public PolicyAdjustmentResponse saveAdjustmentOldDto(Long policyId) {
		logger.info("PolicyServiceImpl:saveAdjustmentOldDto:Start");
		PolicyAdjustmentResponse response = new PolicyAdjustmentResponse();
		PolicyMasterTempEntity policyEntityOpt = policyMasterTempRepository.findById(policyId).orElse(null);
		if (policyEntityOpt != null) {
			MphMasterTempEntity qwert = mphMasterTempRepository.findByMphIdAndIsActiveTrue(policyEntityOpt.getMphId());
			MphMasterDto mphMasterDto = policyCommonServiceImpl.convertMphMasterTempEntityToMphMasterDto(qwert);
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
		} else {
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionMessage(CommonConstants.DENY);
		}
		logger.info("PolicyServiceImpl:saveAdjustmentOldDto:Start");
		return response;
	}

	private Set<PolicyAdjustmentOldDto> mapListAdjustment(Set<PolicyAdjustmentOldDto> adjustments) {
		logger.info("PolicyServiceImpl:mapListAdjustment:Start");
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
		logger.info("PolicyServiceImpl:mapListAdjustment:Ends");
		return response;
	}

	@Override
	public PolicyResponseDto getExistingPolicyByPolicyNumber(String policyNumber, String unitId) {
		PolicyResponseDto res = new PolicyResponseDto();
		List<PolicySearchResponseDto> response = new ArrayList<>();
		logger.info("PolicyServiceImpl:getExistingPolicyByPolicyNumber:Start");
		try {
			Boolean isActive = Boolean.TRUE;
			List<Object> result = policyMasterRepository.policySearchPradeepInPolicyModule(policyNumber, unitId,
					isActive);
			if (result != null && !result.isEmpty()) {
				for (Object object : result) {
					Object[] ob = (Object[]) object;
					PolicySearchResponseDto resonseDto = new PolicySearchResponseDto();
					resonseDto.setMphId((ob[0] != null) ? NumericUtils.convertStringToLong(ob[0].toString()) : null);
					resonseDto.setMphCode(String.valueOf(ob[1]));
					resonseDto.setMphName(String.valueOf(ob[2]));
					resonseDto.setPolicyId((ob[3] != null) ? NumericUtils.convertStringToLong(ob[3].toString()) : null);
					resonseDto.setPolicyNumber(String.valueOf(ob[4]));
					resonseDto.setPolicyStatus(String.valueOf(ob[5]));
					resonseDto.setUnitCode(String.valueOf(ob[6]));
					resonseDto.setProposalNumber(String.valueOf(ob[7]));
					resonseDto.setProduct(String.valueOf(ob[8]));
					response.add(resonseDto);
				}
				res.setTransactionStatus(PolicyConstants.SUCCESS);
				res.setTransactionMessage(PolicyConstants.FETCH_MESSAGE);
				res.setPolicySearchResponse(response);
			} else {
				res.setTransactionStatus(PolicyConstants.FAIL);
				res.setTransactionMessage(PolicyConstants.DENY);
				res.setPolicySearchResponse(response);
			}
			return res;
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:getExistingPolicyByPolicyNumber:error", e);
			res.setTransactionStatus(PolicyConstants.FAIL);
			res.setTransactionMessage(e.getMessage());
		} finally {
			logger.info("PolicyServiceImpl:getExistingPolicyByPolicyNumber:Ends");
		}
		return res;
	}

	@Override
	public PolicyResponseDto getExistingPolicyBymphId(String existing, Long mphId, Long policyId) {
		logger.info("PolicyServiceImpl---getExistingPolicyByMphId---start");
		PolicyResponseDto commonDto = new PolicyResponseDto();
		MphMasterEntity mphMasterEntity = new MphMasterEntity();
		Object mphDetails = mphMasterRepository.fetchMphBasicDetailByMphId(mphId, policyId, Boolean.TRUE);
		if (mphDetails != null) {
			Object[] ob = (Object[]) mphDetails;
			mphMasterEntity.setMphId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[0])));
			mphMasterEntity.setMphCode(NumericUtils.validObjectToString(ob[1]));
			mphMasterEntity.setMphName(NumericUtils.validObjectToString(ob[2]));
			mphMasterEntity.setProposalNumber(NumericUtils.validObjectToString(ob[3]));
		}
		Set<MphAddressEntity> mphAddressEntityList = new HashSet<>();
		MphAddressEntity findByMphIdAndIsActiveTrueAndIsDefaultTrue = mphAddressRepository
				.findByMphIdAndIsActiveTrueAndIsDefaultTrue(mphId);
		mphAddressEntityList.add(findByMphIdAndIsActiveTrueAndIsDefaultTrue);
		mphMasterEntity.setMphAddress(mphAddressEntityList);
		Set<MphBankEntity> mphBankEntityList = new HashSet<>();
		MphBankEntity findByMphIdAndIsActiveTrueAndIsDefaultTrue2 = mphBankRepository
				.findByMphIdAndIsActiveTrueAndIsDefaultTrue(mphId);
		mphBankEntityList.add(findByMphIdAndIsActiveTrueAndIsDefaultTrue2);
		mphMasterEntity.setMphBank(mphBankEntityList);
		Object policyDtoResponse = policyMasterRepository.fetchPolicyBasicDetailByMphId(mphId, policyId, Boolean.TRUE);
		if (policyDtoResponse != null) {
			Object[] ob = (Object[]) policyDtoResponse;
			PolicyMasterEntity resonseDto = new PolicyMasterEntity();
			resonseDto.setPolicyNumber(NumericUtils.validObjectToString(ob[1]));
			resonseDto.setPolicyStatus(NumericUtils.validObjectToString(ob[2]));
			resonseDto.setPolicyCommencementDt(DateUtils.convertStringToDate(NumericUtils.validObjectToString(ob[3])));
			resonseDto.setPolicyRecievedDate(DateUtils.convertStringToDate(NumericUtils.validObjectToString(ob[4])));
			resonseDto.setAdjustmentDt(DateUtils.convertStringToDate(NumericUtils.validObjectToString(ob[5])));
			resonseDto.setProductId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[8])));
			resonseDto.setMarketingOfficerName(NumericUtils.validObjectToString(ob[9]));
			resonseDto.setMarketingOfficerCode(NumericUtils.validObjectToString(ob[10]));
			resonseDto.setAdvanceotarrears(NumericUtils.validObjectToString(ob[11]));
			resonseDto.setPolicyId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[12])));
			resonseDto.setAmountToBeAdjusted(NumericUtils.stringToBigDecimal(NumericUtils.validObjectToString(ob[13])));
			resonseDto.setArd(DateUtils.convertStringToDate(NumericUtils.validObjectToString(ob[14])));
			resonseDto.setNoOfCategory(NumericUtils.stringToInteger(NumericUtils.validObjectToString(ob[15])));
			resonseDto.setConType(NumericUtils.validObjectToString(ob[16]));
			resonseDto.setContributionFrequency(NumericUtils.stringToInteger(NumericUtils.validObjectToString(ob[17])));
			resonseDto.setLineOfBusiness(NumericUtils.validObjectToString(ob[18]));
			resonseDto.setPolicyDispatchDate(DateUtils.convertStringToDate(NumericUtils.validObjectToString(ob[19])));
			resonseDto.setQuotationId(NumericUtils.stringToInteger(NumericUtils.validObjectToString(ob[20])));
			resonseDto.setPolicyType(NumericUtils.validObjectToString(ob[21]));
			resonseDto.setTempPolicyId(NumericUtils.convertStringToLong(NumericUtils.validObjectToString(ob[22])));
			resonseDto.setTotalMember(NumericUtils.stringToInteger(NumericUtils.validObjectToString(ob[23])));
			resonseDto.setUnitId(NumericUtils.validObjectToString(ob[24]));
			resonseDto.setUnitOffice(NumericUtils.validObjectToString(ob[25]));
			resonseDto.setVariant(NumericUtils.validObjectToString(ob[26]));
			resonseDto.setFirstPremium(NumericUtils.stringToBigDecimal(NumericUtils.validObjectToString(ob[27])));
			resonseDto
					.setSinglePremiumFirstYr(NumericUtils.stringToBigDecimal(NumericUtils.validObjectToString(ob[28])));
			resonseDto.setRenewalPremium(NumericUtils.stringToBigDecimal(NumericUtils.validObjectToString(ob[29])));
			resonseDto.setSubsequentSinglePremium(
					NumericUtils.stringToBigDecimal(NumericUtils.validObjectToString(ob[30])));
			resonseDto.setStampDuty(NumericUtils.stringToBigDecimal(NumericUtils.validObjectToString(ob[31])));
			Set<PolicyValuationEntity> valuations = policyValuationRepository
					.findByPolicyIdAndIsActiveTrue(resonseDto.getPolicyId());
			Set<PolicyRulesEntity> rules = policyRulesRepository
					.findByPolicyIdAndIsActiveTrue(resonseDto.getPolicyId());
			Set<PolicyDepositEntity> depositEntity = policyDepositRepository
					.findByPolicyIdAndIsActiveTrue(resonseDto.getPolicyId());
			Set<PolicyContributionSummaryEntity> policyContributionSummary = policyContributionSummaryRepository
					.findByPolicyIdAndIsActive(resonseDto.getPolicyId(), Boolean.TRUE);
			Set<PolicyNotesEntity> notes = policyNotesRepository
					.findByPolicyIdAndIsActiveTrue(resonseDto.getPolicyId());
			String lastPremiumDueDate = policyContributionRepository.findPolicyId(policyId);
			resonseDto.setRules(rules);
			resonseDto.setPolicyContributionSummary(policyContributionSummary);
			resonseDto.setDeposits(depositEntity);
			resonseDto.setValuations(valuations);
			resonseDto.setNotes(notes);
			Set<PolicyMasterEntity> policyList = new HashSet<>();
			policyList.add(resonseDto);
			mphMasterEntity.setPolicyMaster(policyList);
			MphMasterDto mphMasterDto = policyCommonServiceImpl.convertMphMasterEntityToMphMasterDto(mphMasterEntity);
			PolicyDto poilcDto = policyCommonServiceImpl.convertNewResponseToOldResponse(mphMasterDto);
			poilcDto.setLastPremiumDueDate(lastPremiumDueDate);
			poilcDto.getDeposit().removeIf(deposit -> PolicyConstants.ADJESTED.equalsIgnoreCase(deposit.getStatus()));
			poilcDto.getMembers().removeIf(member -> Boolean.TRUE.equals(member.getIsZeroId()));
			poilcDto.getAdjustments()
					.removeIf(deposits -> PolicyConstants.DEPOSITSTATUSNEW.equalsIgnoreCase(deposits.getStatus()));
			String quotationNumber = policyCommonServiceImpl
					.getQuotationNumber(NumericUtils.stringToInteger(NumericUtils.validObjectToString(ob[20])));
			poilcDto.setQuotationNo(quotationNumber);
			commonDto.setPolicyDto(poilcDto);
			commonDto.setPolicyId(commonDto.getPolicyDto().getPolicyId());
			commonDto.setMphId(commonDto.getPolicyDto().getMphId());
			commonDto.setTransactionMessage(PolicyConstants.FETCH);
			commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
		}
		logger.info("PolicyServiceImpl---getExistingPolicyByMphId---Ends");
		return commonDto;
	}

	@Override
	public PolicyDetailsResponseDto getPolicyDetailsByProposalNo(String proposalNo) {
		logger.info("PolicyServiceImpl:getPolicyDetailsByProposalNo---Started");
		PolicyDetailsResponseDto commonDto = new PolicyDetailsResponseDto();
		try {
			List<PolicyDepositOldDto> policyShowDepositDtoList = getDumyDeposit();
			/**
			 * List<PolicyShowDepositDto> policyShowDepositDtoLists =
			 * getDepositDetails(proposalNo);
			 **/
			if (!policyShowDepositDtoList.isEmpty()) {
				commonDto.setPolicyShowDeposit(policyShowDepositDtoList);
			}
			QuotationApiResponseDto quotationApiResponseDto = quotationServiceImpl
					.getQuotationByProposalNumber(proposalNo);
			ProposalAnnuityDto proposalAnnuityDto = getProposalDetails(proposalNo);
			if (proposalAnnuityDto != null && quotationApiResponseDto != null) {
				if (quotationApiResponseDto.getTransactionMessage()
						.equalsIgnoreCase(CommonConstants.RECORDALREADYUSED)) {
					commonDto.setTransactionMessage(CommonConstants.RECORDALREADYUSED + "Quotation");
					commonDto.setTransactionStatus(CommonConstants.FAIL);
				} else {
					commonDto.setQuotationApi(quotationApiResponseDto);
					commonDto.setProposalDetails(proposalAnnuityDto);
					commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
					commonDto.setTransactionMessage(PolicyConstants.FETCH);
				}
			}
		} catch (IllegalArgumentException e) {
			logger.error("PolicyServiceImpl:getPolicyDetailsByProposalNo--Exception--", e);
			commonDto.setTransactionMessage(PolicyConstants.ERROR);
			commonDto.setTransactionStatus(PolicyConstants.FAIL);
		} finally {
			logger.info("PolicyServiceImpl:getPolicyDetailsByProposalNo---Ended");
		}
		return commonDto;
	}

	private List<PolicyDepositOldDto> getDumyDeposit() {
		List<PolicyDepositOldDto> deposits = new ArrayList<>();
		PolicyDepositOldDto deposit1 = new PolicyDepositOldDto();
		deposit1.setCollectionNo("1050");
		deposit1.setCollectionDate(new Date());
		deposit1.setAmount(BigDecimal.valueOf(1000000.0));
		deposit1.setAdjestmentAmount(BigDecimal.valueOf(0.0));
		deposit1.setAvailableAmount(BigDecimal.valueOf(1000000.0));
		deposit1.setTransactionMode("N");
		deposit1.setCollectionStatus("A");
		deposit1.setChequeRealisationDate(new Date());
		deposit1.setRemark(null);
		deposits.add(deposit1);
		return deposits;
	}

	public ProposalAnnuityDto getProposalDetails(String proposalNo) {
		logger.info("PolicyServiceImpl-getProposalDetails-Starts");
		ProposalAnnuityDto responseDto = null;
		try {
			String url = environment.getProperty("PROPOSAL_DETAILS");
			if (StringUtils.isNotBlank(url)) {
				responseDto = restTemplateService
						.exchange(url + proposalNo, HttpMethod.GET, null, ProposalAnnuityDto.class).getBody();
				if (responseDto == null) {
					responseDto = new ProposalAnnuityDto();
				}
			}
		} catch (HttpStatusCodeException e) {
			logger.info("PolicyServiceImpl-getProposalDetails-Error:");
		}
		logger.info("PolicyServiceImpl-getProposalDetails-End");
		return responseDto;
	}

	@Override
	public BigDecimal calculateStamps(BigDecimal amount) {
		logger.info("PolicyServiceImpl-calculateStamps-Starts");
		String check = String.valueOf(amount);
		int num1 = NumericUtils.convertStringToInteger(check);
		int num2 = 1000;
		int quotient = 0;
		int remainder = 0;
		quotient = num1 / num2;
		remainder = num1 % num2;
		if (remainder > 0) {
			quotient = quotient + 1;
		}
		int adjestmentAmount = quotient * 1000;
		logger.info("PolicyServiceImpl-calculateStamps-End");
		return BigDecimal.valueOf(adjestmentAmount * 0.2 / 1000);
	}

	@Override
	public CommonResponseStampDto stampDutyConsuption(StampDutyConsumptionRequestDto requestDto)
			throws ApplicationException {
		CommonResponseStampDto response = new CommonResponseStampDto();
		logger.info("PolicyServiceImpl::stampDutyConsuption::Start::{},", requestDto.getPolicyNumber());
		try {
			BigDecimal stampAmount = calculateStamps(requestDto.getAmountToBeAdjustedBigDecimal());
			requestDto.setStampAmountBigDecimal(stampAmount);
			response = getStampCunsumptionDetails(requestDto);
		} catch (Exception e) {
			logger.error("Exception:PolicyServiceImpl:stampDutyConsuption", e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionMessage(CommonConstants.ERROR);
		}
		logger.info("PolicyServiceImpl::stampDutyConsuption::End::{},", requestDto.getPolicyNumber());
		return response;
	}

	@Override
	public RegularAdjustmentContributionResponseDto getFrequencyDates(PolicyFrequencyDetailsDto request)
			throws ParseException {
		logger.info("PolicyServiceImpl:getFrequencyDates:Starts");
		RegularAdjustmentContributionResponseDto responseDto = new RegularAdjustmentContributionResponseDto();
		List<String> frequencyDates = new ArrayList<>();
		try {
			String fincialyear = DateUtils.dateToHypenStringDDMMYYYY(DateUtils.sysDate());
			CommonResponseDto financialYeartDetails = commonServiceImpl.getFinancialYeartDetails(fincialyear);
			if (financialYeartDetails == null) {
				responseDto.setTransactionMessage("Invalid Financial Year");
			}
			List<PolicyFrequencyDetailsTempEntity> frequencyEntity = policyFrequencyTempRepository
					.findAllByPolicyIdAndIsActiveTrue(request.getPolicyId());
			Date ard = policyMasterTempRepository.getArdDateByPolicyId(request.getPolicyId());
			request.setPolicyEndDate(ard);
			Date commencementdate = policyMasterTempRepository.getCommemcementDateByPolicyId(request.getPolicyId());
			if (commencementdate == null) {
				responseDto.setTransactionStatus(CommonConstants.FAIL);
				responseDto.setTransactionMessage("No Frequency Details avilable");
			}
			request.setPolicyCommencementDate(commencementdate);
			if (frequencyEntity.isEmpty()) {
				int frequency = NumericUtils.convertStringToInteger(request.getFrequency());
				int totalmonths = DateUtils.getTotalInBetweenMonths(request.getPolicyCommencementDate(),
						request.getPolicyEndDate());
				switch (frequency) {
				case 1:
					frequencyDates = DateUtils.datemonthDifferenceForNB(totalmonths,
							request.getPolicyCommencementDate(), request.getPolicyEndDate());
					break;
				case 2:
					frequencyDates = DateUtils.dateQuarterDifferenceForNB(totalmonths,
							request.getPolicyCommencementDate(), request.getPolicyEndDate());
					break;
				case 3:
					frequencyDates = DateUtils.convertDateTimeToStringWithPeriodForNB(totalmonths,
							request.getPolicyCommencementDate(), request.getPolicyEndDate());
					break;
				case 4:
					frequencyDates = DateUtils.datehalfDifferenceForNB(totalmonths, request.getPolicyCommencementDate(),
							request.getPolicyEndDate());
					break;
				default:
					frequencyDates = null;
				}
				if (frequencyDates != null) {
					for (int i = 0; i <= frequencyDates.size() - 1; i++) {
						PolicyFrequencyDetailsTempEntity entity = new PolicyFrequencyDetailsTempEntity();
						entity.setPolicyId(request.getPolicyId());
						entity.setFrequencyDates(DateUtils.convertStringToDate(frequencyDates.get(i)));
						entity.setFrequency(request.getFrequency());
						entity.setStatus(CommonConstants.UNPAID);
						entity.setPolicyCommencementDate(commencementdate);
						entity.setPolicyEndDate(ard);
						policyFrequencyTempRepository.save(entity);
					}
					responseDto.setTransactionMessage(PolicyConstants.SUCCESS);
					responseDto.setTransactionStatus(PolicyConstants.STATUS);
				}
			} else {
				responseDto.setTransactionMessage(PolicyConstants.DENY);
				responseDto.setTransactionStatus(PolicyConstants.ERRORSTATUS);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:getFrequencyDates", e);
			responseDto.setTransactionStatus(CommonConstants.FAIL);
			responseDto.setTransactionMessage(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:getFrequencyDates:Ends");
		}
		return responseDto;
	}

	@Override
	public AdjustmentContributionResponseDto getFrequencyByPolicyId(Long policyId) {
		logger.info("PolicyServiceImpl:getFrequencyByPolicyId:Starts");
		AdjustmentContributionResponseDto commonDto = new AdjustmentContributionResponseDto();
		List<PolicyFrequencyDetailsTempEntity> entities = new ArrayList<>();
		try {
			List<PolicyFrequencyDetailsTempEntity> entity = policyFrequencyTempRepository
					.findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyIdDesc(policyId, CommonConstants.UNPAID);
			if (entity != null) {
				for (PolicyFrequencyDetailsTempEntity iEntity : entity) {
					PolicyFrequencyDetailsTempEntity getEntity = new PolicyFrequencyDetailsTempEntity();
					getEntity.setFrequency(iEntity.getFrequency());
					getEntity.setFrequencyDates(iEntity.getFrequencyDates());
					getEntity.setStatus(iEntity.getStatus());
					getEntity.setPolicyId(iEntity.getPolicyId());
					getEntity.setFrequencyId(iEntity.getFrequencyId());
					entities.add(getEntity);
				}
				commonDto.setFrequencyDtosTemp(entities);
				commonDto.setTransactionMessage(PolicyConstants.SUCCESS);
				commonDto.setTransactionStatus(PolicyConstants.STATUS);
			} else {
				commonDto.setTransactionMessage(PolicyConstants.DENY);
				commonDto.setTransactionStatus(PolicyConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception: PolicyServiceImpl : getFrequencyByPolicyId ", e);
			commonDto.setTransactionMessage(CommonConstants.EXCEPTION);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("PolicyServiceImpl:getFrequencyByPolicyId:Ends");
		return commonDto;
	}

	@Override
	public PolicyResponseDto getPolicyMember(Long memberId, Long policyId) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		try {
			MemberMasterEntity memberMasterEntity = memberMasterRepository
					.findByMemberIdAndPolicyIdAndIsActiveTrueAndIsZeroidFalse(memberId, policyId);
			if (memberMasterEntity != null) {
				MemberMasterDto memberDto = modelMapper.map(memberMasterEntity, MemberMasterDto.class);
				if (!memberDto.getMemberContributionSummary().isEmpty()) {
					String financialYear = DateUtils.getFinancialYrByDt(DateUtils.sysDate());
					memberDto.getMemberContributionSummary()
							.removeIf(mcs -> !mcs.getFinancialYear().equalsIgnoreCase(financialYear));
				}
				commonDto.setResponse(memberDto);
				commonDto.setTransactionStatus(CommonConstants.STATUS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				logger.info("Policy member not found for the given PolicyMemberId: {} and PolicyId: {}", memberId,
						policyId);
				commonDto.setTransactionMessage(CommonConstants.FAIL);
				commonDto.setTransactionStatus("Policy member not found for the given PolicyMemberId: " + memberId
						+ " and PolicyId: " + policyId);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- PolicyMemberServiceImpl-- getPolicyMember--", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		return commonDto;
	}

	@Override
	public PolicyResponseDto getMemberDetails(String licId, Long policyId) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		try {
			MemberMasterEntity memberMasterEntity = memberMasterRepository
					.findByLicIdAndPolicyIdAndIsActiveTrueAndIsZeroidFalse(licId, policyId);
			if (memberMasterEntity != null) {
				commonDto.setResponse(modelMapper.map(memberMasterEntity, MemberMasterDto.class));
				commonDto.setTransactionStatus(CommonConstants.STATUS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				logger.info("Policy member not found for the given licId: {} and PolicyId: {}", licId, policyId);
				commonDto.setTransactionMessage(CommonConstants.FAIL);
				commonDto.setTransactionStatus(
						"Policy member not found for the given licId: " + licId + " and PolicyId: " + policyId);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- getMemberContributionDetails-- getPolicyMember--", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionMessage(CommonConstants.ERROR);
		}
		return commonDto;
	}

	@Override
	public PolicyResponseDto getPolicyContributionDetails(ContributionRequestDto contributionRequestDto) {
		logger.info("PolicyServiceImpl:getPolicyContributionDetails---Started");
		PolicyResponseDto policyResponseDto = new PolicyResponseDto();
		List<ContributionResponseDto> contributionResponseDtoList = new ArrayList<>();
		try {
			List<Object[]> contributionDetails = policyContributionRepository.getPolicyContributionDetails(
					contributionRequestDto.getPolicyId(), contributionRequestDto.getFinancialYear(),
					contributionRequestDto.getQuarter(), contributionRequestDto.getFrequency(), PolicyConstants.DC_V2,
					PolicyConstants.DB_V2);
			if (!contributionDetails.isEmpty()) {
				for (Object[] object : contributionDetails) {
					ContributionResponseDto newContributionResponseDto = new ContributionResponseDto();
					newContributionResponseDto.setSNo(String.valueOf(object[0]));
					newContributionResponseDto.setContributionId(object[1].toString());
					newContributionResponseDto
							.setDate(DateUtils.stringToDateYYYYMMDDHHMMSSHyphen(String.valueOf(object[2])));
					newContributionResponseDto.setAdjustmentForDate(
							DateUtils.stringToDateYYYYMMDDHHMMSSHyphen(String.valueOf(object[3])));
					newContributionResponseDto.setType(String.valueOf(object[4]));
					newContributionResponseDto.setEmployeeContribution(String.valueOf(object[5]));
					newContributionResponseDto.setEmployerContribution(String.valueOf(object[6]));
					newContributionResponseDto.setVoluntaryContribution(String.valueOf(object[7]));
					newContributionResponseDto.setTotalAmount(String.valueOf(object[8]));
					newContributionResponseDto.setClosingBalance(String.valueOf(object[9]));
					contributionResponseDtoList.add(newContributionResponseDto);
				}
				policyResponseDto.setResponse(contributionResponseDtoList);
				policyResponseDto.setTransactionStatus(PolicyConstants.SUCCESS);
				policyResponseDto.setTransactionMessage(PolicyConstants.FETCH);
			} else {
				policyResponseDto.setTransactionStatus(PolicyConstants.FAIL);
				policyResponseDto.setTransactionMessage(PolicyConstants.LISTTRUSTCONTACTDETAILSERROR);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- getPolicyContributionDetails-- getPolicyMember--", e);
			policyResponseDto.setTransactionStatus(CommonConstants.FAIL);
			policyResponseDto.setTransactionMessage(CommonConstants.ERROR);
		}
		logger.info("PolicyServiceImpl:getPolicyContributionDetails--Ends");
		return policyResponseDto;
	}

	@Override
	public PolicyResponseDto getMemberContributionDetails(ContributionRequestDto contributionRequestDto) {
		logger.info("PolicyServiceImpl:getMemberContributionDetails---Started");
		PolicyResponseDto policyResponseDto = new PolicyResponseDto();
		List<ContributionResponseDto> contributionResponseDtoList = new ArrayList<>();
		try {
			List<Object[]> contributionDetails = memberContributionRepository.getMemberContributionDetails(
					contributionRequestDto.getPolicyId(), contributionRequestDto.getFinancialYear(),
					contributionRequestDto.getQuarter(), contributionRequestDto.getFrequency(), PolicyConstants.DC_V2,
					PolicyConstants.DB_V2, contributionRequestDto.getLicId());
			if (!contributionDetails.isEmpty()) {
				for (Object[] object : contributionDetails) {
					ContributionResponseDto newContributionResponseDto = new ContributionResponseDto();
					newContributionResponseDto.setSNo(String.valueOf(object[0]));
					newContributionResponseDto.setContributionId(object[1].toString());
					newContributionResponseDto
							.setDate(DateUtils.stringToDateYYYYMMDDHHMMSSHyphen(String.valueOf(object[2])));

					newContributionResponseDto.setType(String.valueOf(object[3]));
					newContributionResponseDto.setEmployeeContribution(String.valueOf(object[4]));
					newContributionResponseDto.setEmployerContribution(String.valueOf(object[5]));
					newContributionResponseDto.setVoluntaryContribution(String.valueOf(object[6]));
					newContributionResponseDto.setTotalAmount(String.valueOf(object[7]));
					newContributionResponseDto.setClosingBalance(String.valueOf(object[8]));
					contributionResponseDtoList.add(newContributionResponseDto);
				}
				policyResponseDto.setResponse(contributionResponseDtoList);
				policyResponseDto.setTransactionStatus(PolicyConstants.SUCCESS);
				policyResponseDto.setTransactionMessage(PolicyConstants.FETCH);
			} else {
				policyResponseDto.setTransactionStatus(PolicyConstants.FAIL);
				policyResponseDto.setTransactionMessage(PolicyConstants.LISTTRUSTCONTACTDETAILSERROR);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- getMemberContributionDetails-- getPolicyMember--", e);
			policyResponseDto.setTransactionStatus(CommonConstants.FAIL);
			policyResponseDto.setTransactionMessage(CommonConstants.ERROR);

		}
		logger.info("PolicyServiceImpl:getMemberContributionDetails--Ends");
		return policyResponseDto;
	}

	@Override
	public PolicyResponseDto getInvidualContriButionDetails(ContributionRequestDto contributionRequestDto) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		try {
			MemberContributionEntity memberEntity = memberContributionRepository
					.getInvidualContributionDetails(contributionRequestDto.getPolicyConId());
			if (memberEntity != null) {
				MemberContributionDto dto = modelMapper.map(memberEntity, MemberContributionDto.class);
				commonDto.setMemberContribution(dto);
				commonDto.setTransactionMessage(PolicyConstants.FETCH_MESSAGE);
				commonDto.setTransactionStatus(PolicyConstants.SUCESSSTATUS);
			} else {
				commonDto.setTransactionMessage(PolicyConstants.DENY);
				commonDto.setTransactionStatus(PolicyConstants.ERRORSTATUS);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- getInvidualContriButionDetails--", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionMessage(CommonConstants.ERROR);
		}
		return commonDto;
	}

	@Override
	public InputStream policyContributionSummary(Long policyId, Long adjConId) throws IOException {
		logger.info("PolicyServiceImpl -- policyContributionSummary --started");
		List<Object[]> contributionList = null;
		List<String> headers = null;
		List<String> data = null;
		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
			if (adjConId != null && policyId != null) {
				contributionList = memberContributionRepository.getPolicyContributionSummary(policyId, adjConId);
				headers = Arrays.asList("Date", "Contribution Number", "LIC ID", "Employer Contribution",
						"Employee Contribution", "Voluntary Contribution", "Total Contribution");
				csvPrinter.printRecord(headers);
				for (Object[] obj : contributionList) {
					policyContributionExportDto dto = new policyContributionExportDto();
					if (obj[0] != null)
						dto.setDate((Date) obj[1]);
					if (obj[2] != null)
						dto.setContributionNumber(obj[0].toString());
					if (obj[2] != null)
						dto.setLicId(obj[2].toString());
					if (obj[3] != null)
						dto.setEmployerContribution(obj[3].toString());
					if (obj[4] != null)
						dto.setEmployeeContribution(obj[4].toString());
					if (obj[5] != null)
						dto.setVoluntaryContribution(obj[5].toString());
					if (obj[6] != null)
						dto.setTotalContribution(obj[6].toString());
					data = Arrays.asList(DateUtils.dateToStringFormatYyyyMmDd(dto.getDate()),
							dto.getContributionNumber(), dto.getLicId(), dto.getEmployerContribution(),
							dto.getEmployeeContribution(), dto.getVoluntaryContribution(), dto.getTotalContribution());
					csvPrinter.printRecord(data);
				}
			}
			csvPrinter.flush();
			logger.info("PolicyServiceImpl -- exportFundCalculation --started");
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			logger.info("PolicyServiceImpl -- exportFundCalculation --Exception ", e);
			throw new IOException(e);
		}
	}

	@Override
	public InputStream getFullContribution(Long policyId, String finicialYear) {

		List<String> headers = null;
		List<Object[]> entity = policyMasterRepository.findByPolicyEntity(policyId);
		List<Object[]> contributionList = null;
		List<Object[]> fullList = null;
		List<String> date = null;
		List<String> groupSchemeDepartment = null;
		List<String> policyNo = null;
		List<String> contributionFrom = null;
		List<String> contributionTo = null;
		List<String> customerCode = null;
		List<String> customerName = null;
		List<String> annualRenewalDate = null;
		List<String> totalContribution = null;

//		BigDecimal totalPolicyContribution = policyContributionSummaryRepository.findByPolicyTotalContributionByFinicialYear(policyId);

//		BigDecimal totalPolicyContribution = policyContributionSummaryRepository.findByPolicy(policyId);

		BigDecimal totalPolicyContribution = policyContributionSummaryRepository.findByPolicyByFinicialYear(policyId,
				finicialYear);

		policyContributionExportDto dataDto = new policyContributionExportDto();
		dataDto.setTotalContribution(NumericUtils.convertBigDecimalToString(totalPolicyContribution));
		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
		contributionList = memberContributionRepository.findByDistinctDate(policyId, finicialYear);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {

			fullList = memberContributionRepository.findByContributionDataByFinicialYear(policyId, finicialYear);

			List<String> d = new ArrayList<>();
			List<String> p = new ArrayList<>();

			for (Object[] obj : contributionList) {
				if (obj[0] != null)
					d.add(DateUtils.dateToStringFormatYyyyMmDd((Date) obj[0]));
				dataDto.setDueOn(d);
				if (obj[1] != null)
					p.add(DateUtils.dateToStringFormatYyyyMmDd((Date) obj[1]));
				dataDto.setPaidOn(p);
			}
			Long mphId = null;
			for (Object[] obj : entity) {
				if (obj[0] != null)
					dataDto.setAnnualRenewalDate(DateUtils.dateToStringFormatYyyyMmDd((Date) obj[0]));
				if (obj[1] != null)
					mphId = NumericUtils.convertStringToLong(obj[1].toString());
				if (obj[2] != null)
					dataDto.setPolicyNo(obj[2].toString());
				if (obj[3] != null)
					dataDto.setGroupSchemeDepartment(obj[3].toString());
			}
			Object mphEntity = mphMasterRepository.findByMphId(mphId);
			Object[] obj1 = (Object[]) mphEntity;
			if (obj1[0] != null)
				dataDto.setCustomerCode(obj1[0].toString());
			if (obj1[1] != null)
				dataDto.setCustomerName(obj1[1].toString());
			String[] arr = finicialYear.split("-");

			dataDto.setContributionFrom("01-04-" + arr[0]);
			dataDto.setContributionTo("31-03-" + arr[1]);
			String pattern = "dd-MM-yyyy";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date1 = simpleDateFormat.format(new Date());
			headers = Arrays.asList("", "", "", "Life  Insurance  Corporation  of  India  Page");
			date = Arrays.asList("Date :", date1);
			groupSchemeDepartment = Arrays.asList("Group Scheme Department :", dataDto.getGroupSchemeDepartment());
			policyNo = Arrays.asList("Policy No:", dataDto.getPolicyNo());
			contributionFrom = Arrays.asList("Contribution From :", dataDto.getContributionFrom());
			contributionTo = Arrays.asList("Contribution To :", dataDto.getContributionTo());
			customerCode = Arrays.asList("Customer Code :", dataDto.getCustomerCode());
			customerName = Arrays.asList("Customer Name :", dataDto.getCustomerName());
			annualRenewalDate = Arrays.asList("Annual Renewal  Date : ", dataDto.getAnnualRenewalDate());
			totalContribution = Arrays.asList("Total Contribution :", dataDto.getTotalContribution());
			csvPrinter.printRecord(headers);
			csvPrinter.printRecord(date);
			csvPrinter.printRecord(groupSchemeDepartment);
			csvPrinter.printRecord(policyNo);
			csvPrinter.printRecord(contributionFrom);
			csvPrinter.printRecord(contributionTo);
			csvPrinter.printRecord(customerCode);
			csvPrinter.printRecord(customerName);
			csvPrinter.printRecord(annualRenewalDate);
			csvPrinter.printRecord(totalContribution);

			List<String> header1 = new ArrayList<>();
			header1.add("");
			header1.add("Due On");
			for (int i = 0; i <= d.size() - 1; i++) {
				header1.add(d.get(i));
			}

			header1.add("Total");

			List<String> header2 = new ArrayList<>();
			header2.add("LIC ID");
			header2.add("Paid On");
			for (int i = 0; i <= p.size() - 1; i++) {
				header2.add(p.get(i));
			}
//			header2.add("Total");

			csvPrinter.printRecord(header1);
			csvPrinter.printRecord(header2);

			String licid = "";
			double sumOfContribution = 0;
			int headerTracker = 0;
			List<String> data = new ArrayList<>();
			for (Object[] obj : fullList) {
				if ((!licid.equals(obj[0].toString())) && (!licid.equals(""))) {
					for (int i = data.size(); i < header1.size() - 1; i++) {
						data.add("");
					}
					data.add(NumericUtils.convertDoubleToString(sumOfContribution));
					csvPrinter.printRecord(data);
					licid = "";
					data.clear();
					sumOfContribution = 0;
				}

				if (licid.equals(obj[0].toString()) || licid.equals("")) {
					if (licid.equals("")) {
						licid = obj[0].toString();
						data.add(obj[0].toString());
						data.add("");
						headerTracker = 0;
					}

					while (headerTracker < d.size()) {
						if (Objects.equals(DateUtils.dateToStringFormatYyyyMmDd((Date) obj[1]), d.get(headerTracker))) {
							data.add(obj[2].toString());
							sumOfContribution += NumericUtils.stringToDouble(obj[2].toString());
							headerTracker++;
							break;
						} else {
							data.add("");
							headerTracker++;
						}
					}
				}
			}

			if (!fullList.isEmpty()) {
				for (int i = data.size(); i < header1.size() - 1; i++) {
					data.add("");
				}
				data.add(NumericUtils.convertDoubleToString(sumOfContribution));
				csvPrinter.printRecord(data);
			}

			csvPrinter.flush();
			logger.info("PolicyServiceImpl -- exportFundCalculation --started");
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			logger.info("PolicyServiceImpl -- exportFundCalculation --Exception ", e);
		}
		return null;
	}

	public List<PolicyShowDepositDto> getDepositDetails(String proposalNo) {
		logger.info("PolicyServiceImpl-getDepositDetails-Starts");
		List<PolicyShowDepositDto> responseDto = null;
		try {
			String url = environment.getProperty("DEPOSIT_DETAILS");
			if (StringUtils.isNotBlank(url)) {
				ResponseEntity<List<PolicyShowDepositDto>> response = restTemplateService.exchange(url + proposalNo,
						HttpMethod.GET, null, new ParameterizedTypeReference<List<PolicyShowDepositDto>>() {
						});
				responseDto = response.getBody();
			}
		} catch (HttpStatusCodeException e) {
			logger.info("PolicyServiceImpl-getDepositDetails-Error:");
		}
		logger.info("PolicyServiceImpl-getDepositDetails-End");
		return responseDto;
	}

	@Override
	public IssuePolicyResponseDto issuancePolicy(String policyNumber) {
		logger.info("PolicyServiceImpl-issuancePolicy-Starts");
		IssuePolicyResponseDto response = new IssuePolicyResponseDto();
		AnnuityCreationRequestResponseStoreEntity creationRequestResponseStoreEntity = new AnnuityCreationRequestResponseStoreEntity();
		try {
			logger.info("PolicyServiceImpl-issuancePolicy Request-Set-starts::{},", policyNumber);
			IssuePolicyRequestDto request = new IssuePolicyRequestDto();
			Object policyDetails = policyMasterRepository.getIssuePolicyRequestDetailsByPolicyNumber(policyNumber);
			Object[] ob = (Object[]) policyDetails;
			request.setPolicyNo(NumericUtils.validObjectToString(ob[0]));
			request.setProposalNo(NumericUtils.validObjectToString(ob[1]));
			JsonNode productDetailsResponse = integrationService
					.getProductDetailsByProductId(NumericUtils.stringToLong(NumericUtils.validObjectToString(ob[2])));
			if (productDetailsResponse != null) {
				JsonNode proposeDetails = productDetailsResponse.path(CommonConstants.RESPONSE_DATA);
				String productCode = proposeDetails.path("productCode").textValue();
				request.setProductCode(productCode);
			}
			JsonNode variantDetailsResponse = integrationService.getVariantDetailsByProductVariantId(
					NumericUtils.stringToLong(NumericUtils.validObjectToString(ob[3])));
			if (variantDetailsResponse != null) {
				JsonNode proposeDetails = variantDetailsResponse.path(CommonConstants.RESPONSE_DATA);
				String variantCode = proposeDetails.path("subCategory").textValue();
				request.setVariantCode(variantCode);
			}
			String requestString = objectMapper.writeValueAsString(request);
			creationRequestResponseStoreEntity.setApiRequestString(CommonUtils.stringMax(requestString));
			creationRequestResponseStoreEntity.setRequestDate(DateUtils.sysDate());
			creationRequestResponseStoreEntity.setVariant(request.getVariantCode());
			creationRequestResponseStoreEntity.setPolicyNumber(policyNumber);
			logger.info("PolicyServiceImpl-issuancePolicy-Request-Set-End::POLICYNUMBER,{},REQUESTSTRING,{}",
					policyNumber, requestString);
			logger.info("PolicyServiceImpl-issuancePolicy RESPONSE-starts::{},", policyNumber);
			response.setResponseMessage(CommonConstants.ERROR);
			String url = environment.getProperty("ISSUANCE_POLICY");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				HttpEntity<IssuePolicyRequestDto> entity = new HttpEntity<>(request, headers);
				creationRequestResponseStoreEntity.setApiUrl(url);
				creationRequestResponseStoreEntity.setRequestMethod("POST");
				creationRequestResponseStoreEntity.setContentType("application/json");
				creationRequestResponseStoreEntity.setResponseDate(DateUtils.sysDate());
				creationRequestResponseStoreEntity.setType(CommonConstants.ISSUANCE_POLICY_TYPE);
				response = restTemplateService.exchange(url, HttpMethod.POST, entity,
						new ParameterizedTypeReference<IssuePolicyResponseDto>() {
						}).getBody();
				String responseString = objectMapper.writeValueAsString(response);

				if (response != null) {
					if (StringUtils.isNotBlank(response.getResponseMessage())
							&& response.getResponseMessage().equalsIgnoreCase(CommonConstants.ISSUANCE_POLICY_SUCCESS)
							&& response.getResponseMessage() != null) {
						creationRequestResponseStoreEntity.setApiResponseString(CommonUtils.stringMax(responseString));
						creationRequestResponseStoreEntity.setStatus(CommonConstants.SUCCESS);
						logger.info("PolicyServiceImpl-issuancePolicy-RESPONSE-End::POLICYNUMBER,{},REQUESTSTRING,{}",
								policyNumber, responseString);
						return response;
					} else {
						creationRequestResponseStoreEntity.setApiResponseString(CommonUtils.stringMax(responseString));
						creationRequestResponseStoreEntity.setStatus(CommonConstants.FAIL);
						creationRequestResponseStoreEntity
								.setErrorResponse(CommonUtils.stringMax(response.getResponseMessage()));
						logger.info("PolicyServiceImpl-issuancePolicy-RESPONSE-End::{},{},", policyNumber,
								response.getResponseMessage());
					}
				} else {
					creationRequestResponseStoreEntity.setApiResponseString(CommonUtils.stringMax(responseString));
					creationRequestResponseStoreEntity.setStatus(CommonConstants.FAIL);
					creationRequestResponseStoreEntity.setErrorResponse(CommonConstants.NO_RESPONSE);
					logger.info("PolicyServiceImpl-issuancePolicy-RESPONSE-End::{},{}", policyNumber,
							CommonConstants.NO_RESPONSE);
				}
			} else {
				creationRequestResponseStoreEntity.setApiResponseString(CommonConstants.ERROR);
				creationRequestResponseStoreEntity.setStatus(CommonConstants.FAIL);
				creationRequestResponseStoreEntity.setErrorResponse(CommonConstants.ISSUANCE_POLICY_ERROR + URL_EMPTY);
				logger.info("PolicyServiceImpl-issuancePolicy-RESPONSE-End::{},{}", policyNumber,
						CommonConstants.ISSUANCE_POLICY_ERROR + URL_EMPTY);
			}
		} catch (Exception e) {
			response = new IssuePolicyResponseDto();
			response.setResponseMessage(CommonConstants.ERROR);
			creationRequestResponseStoreEntity.setApiResponseString(CommonConstants.ERROR);
			creationRequestResponseStoreEntity.setStatus(CommonConstants.FAIL);
			creationRequestResponseStoreEntity.setErrorResponse(CommonUtils.stringMax(e.getMessage()));
			logger.error("PolicyServiceImpl::Exception::issuancePolicy::End::{},", policyNumber, e);
		} finally {
			creationRequestResponseStoreEntity
					.setRemark(checkStatusAndSetRemark(creationRequestResponseStoreEntity.getStatus()));
			annuityCreationReqAndResRepository.save(creationRequestResponseStoreEntity);
			if (Objects.equals(creationRequestResponseStoreEntity.getStatus(), CommonConstants.SUCCESS)) {
				activePrevious(policyNumber, CommonConstants.ISSUANCE_POLICY_TYPE, CommonConstants.SUCCESS);
			}
			logger.info("PolicyServiceImpl::Finally::issuancePolicy::End::{},", policyNumber);
		}
		logger.info("PolicyServiceImpl::issuancePolicy::End::{},", policyNumber);
		return response;
	}

	private String checkStatusAndSetRemark(String status) {
		String response = "";
		if (Objects.equals(status, CommonConstants.SUCCESS)) {
			response = "COMPLETED";
		} else if (Objects.equals(status, CommonConstants.FAIL)) {
			response = "UNCOMPLETED";
		}
		return response;
	}

	@Override
	public TrnRegistrationResponseDto trnRegistration(String policyNumber) {
		logger.info("PolicyServiceImpl-trnRegistration-Starts");
		TrnRegistrationResponseDto response = new TrnRegistrationResponseDto();
		AnnuityCreationRequestResponseStoreEntity creationRequestResponseStoreEntity = new AnnuityCreationRequestResponseStoreEntity();
		try {
			logger.info("PolicyServiceImpl-trnRegistration Request-Set-starts::{},", policyNumber);
			TrnRegistrationRequestDto request = new TrnRegistrationRequestDto();
			Object policyDetails = policyMasterRepository.getTrnRegistrationRequestDetailsByPolicyNumber(policyNumber);
			Object[] ob = (Object[]) policyDetails;
			request.setPolicyNo(NumericUtils.validObjectToString(ob[0]));
			request.setProposalNo(NumericUtils.validObjectToString(ob[1]));
			JsonNode productDetailsResponse = integrationService
					.getProductDetailsByProductId(NumericUtils.stringToLong(NumericUtils.validObjectToString(ob[2])));
			if (productDetailsResponse != null) {
				JsonNode proposeDetails = productDetailsResponse.path(CommonConstants.RESPONSE_DATA);
				String productCode = proposeDetails.path("productCode").textValue();
				request.setProductCode(productCode);
			}
			JsonNode variantDetailsResponse = integrationService.getVariantDetailsByProductVariantId(
					NumericUtils.stringToLong(NumericUtils.validObjectToString(ob[3])));
			if (variantDetailsResponse != null) {
				JsonNode proposeDetails = variantDetailsResponse.path(CommonConstants.RESPONSE_DATA);
				String variantCode = proposeDetails.path("subCategory").textValue();
				request.setVariantCode(variantCode);
			}
			request.setValidityUptoDate(DateUtils.DateTostringYYYYMMDDHyphen(
					DateUtils.convertStringToDate(NumericUtils.validObjectToString(ob[4]))));
			request.setReferenceDate(DateUtils.DateTostringYYYYMMDDHyphen(DateUtils.sysDate()));
			request.setAmount("1");
			request.setAmountType("9");
			request.setBnkUniqueId("");
			request.setPoolAccountCode("");
			request.setChallanNo("0");
			request.setCreatedBy("1");
			request.setReferenceIdType("GRI");
			request.setStatus("1");
			request.setReason("Active");
			String requestString = objectMapper.writeValueAsString(request);
			creationRequestResponseStoreEntity.setApiRequestString(CommonUtils.stringMax(requestString));
			creationRequestResponseStoreEntity.setRequestDate(DateUtils.sysDate());
			creationRequestResponseStoreEntity.setVariant(request.getVariantCode());
			creationRequestResponseStoreEntity.setPolicyNumber(policyNumber);
			logger.info("PolicyServiceImpl-trnRegistration-Request-Set-End::POLICYNUMBER,{},REQUESTSTRING,{},",
					policyNumber, requestString);

			logger.info("PolicyServiceImpl-trnRegistration RESPONSE-starts::{},", policyNumber);
			response.setResponseMessage(CommonConstants.ERROR);
			String url = environment.getProperty("TRN_REGISTRATION");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				HttpEntity<TrnRegistrationRequestDto> entity = new HttpEntity<>(request, headers);

				creationRequestResponseStoreEntity.setApiUrl(url);
				creationRequestResponseStoreEntity.setRequestMethod("POST");
				creationRequestResponseStoreEntity.setContentType("application/json");
				creationRequestResponseStoreEntity.setResponseDate(DateUtils.sysDate());
				creationRequestResponseStoreEntity.setType(CommonConstants.TRN_REGISTRATION_TYPE);

				response = restTemplateService.exchange(url, HttpMethod.POST, entity,
						new ParameterizedTypeReference<TrnRegistrationResponseDto>() {
						}).getBody();
				String responseString = objectMapper.writeValueAsString(response);

				if (response != null) {
					if (StringUtils.isNotBlank(response.getResponseMessage())
							&& response.getResponseMessage().equalsIgnoreCase(CommonConstants.SUCCESS)
							&& response.getResponseMessage() != null) {
						creationRequestResponseStoreEntity.setApiResponseString(CommonUtils.stringMax(responseString));
						creationRequestResponseStoreEntity.setStatus(CommonConstants.SUCCESS);
						logger.info("PolicyServiceImpl-trnRegistration-RESPONSE-End::POLICYNUMBER,{},REQUESTSTRING,{},",
								policyNumber, responseString);
						return response;
					} else {
						creationRequestResponseStoreEntity.setApiResponseString(CommonUtils.stringMax(responseString));
						creationRequestResponseStoreEntity.setStatus(CommonConstants.FAIL);
						creationRequestResponseStoreEntity
								.setErrorResponse(CommonUtils.stringMax(response.getResponseMessage()));
						logger.info("PolicyServiceImpl-trnRegistration-RESPONSE-End::{},{},", policyNumber,
								response.getResponseMessage());
					}
				} else {
					creationRequestResponseStoreEntity.setApiResponseString(CommonUtils.stringMax(responseString));
					creationRequestResponseStoreEntity.setStatus(CommonConstants.FAIL);
					creationRequestResponseStoreEntity.setErrorResponse(CommonConstants.NO_RESPONSE);
					logger.info("PolicyServiceImpl-trnRegistration-RESPONSE-End::{},{}", policyNumber,
							CommonConstants.NO_RESPONSE);
				}
			} else {
				creationRequestResponseStoreEntity.setApiResponseString(CommonConstants.ERROR);
				creationRequestResponseStoreEntity.setStatus(CommonConstants.FAIL);
				creationRequestResponseStoreEntity.setErrorResponse(CommonConstants.TRN_REGISTRATION_ERROR + URL_EMPTY);
				logger.info("PolicyServiceImpl-trnRegistration-RESPONSE-End::{},{}", policyNumber,
						CommonConstants.TRN_REGISTRATION_ERROR + URL_EMPTY);
			}
		} catch (Exception e) {
			response = new TrnRegistrationResponseDto();
			response.setResponseMessage(CommonConstants.ERROR);
			creationRequestResponseStoreEntity.setApiResponseString(CommonConstants.ERROR);
			creationRequestResponseStoreEntity.setStatus(CommonConstants.FAIL);
			creationRequestResponseStoreEntity.setErrorResponse(CommonUtils.stringMax(e.getMessage()));
			logger.info("PolicyServiceImpl::Exception::trnRegistration::End::{},", policyNumber, e);
		} finally {
			creationRequestResponseStoreEntity
					.setRemark(checkStatusAndSetRemark(creationRequestResponseStoreEntity.getStatus()));
			annuityCreationReqAndResRepository.save(creationRequestResponseStoreEntity);
			if (Objects.equals(creationRequestResponseStoreEntity.getStatus(), CommonConstants.SUCCESS)) {
				activePrevious(policyNumber, CommonConstants.TRN_REGISTRATION_TYPE, CommonConstants.SUCCESS);
			}
			logger.info("PolicyServiceImpl::Finally::trnRegistration::End::{},", policyNumber);
		}
		logger.info("PolicyServiceImpl-trnRegistration-End");
		return response;
	}

	@Override
	public PolicyResponseDto checkissuancePolicySuccessOrNot(String policyNumber) {
		PolicyResponseDto response = new PolicyResponseDto();

		List<AnnuityCreationRequestResponseStoreEntity> issuanceentity = annuityCreationReqAndResRepository
				.findByPolicyNumberAndStatusAndType(policyNumber, CommonConstants.SUCCESS,
						CommonConstants.ISSUANCE_POLICY_TYPE);
		int issuanceSuccess = 0;
		if (issuanceentity.isEmpty()) {
			inactivePrevious(policyNumber, CommonConstants.ISSUANCE_POLICY_TYPE, CommonConstants.FAIL);
			issuancePolicy(policyNumber);
		} else if (!issuanceentity.isEmpty()) {
			inactivePrevious(policyNumber, CommonConstants.ISSUANCE_POLICY_TYPE, CommonConstants.FAIL);
			issuanceSuccess = 1;
		}

		List<AnnuityCreationRequestResponseStoreEntity> trnRegistrationentity = annuityCreationReqAndResRepository
				.findByPolicyNumberAndStatusAndType(policyNumber, CommonConstants.SUCCESS,
						CommonConstants.TRN_REGISTRATION_TYPE);
		int trnRegistrationSuccess = 0;
		if (trnRegistrationentity.isEmpty()) {
			inactivePrevious(policyNumber, CommonConstants.TRN_REGISTRATION_TYPE, CommonConstants.FAIL);
			trnRegistration(policyNumber);
		} else if (!trnRegistrationentity.isEmpty()) {
			inactivePrevious(policyNumber, CommonConstants.ISSUANCE_POLICY_TYPE, CommonConstants.FAIL);
			trnRegistrationSuccess = 1;
		}

		if (trnRegistrationSuccess == 1 && issuanceSuccess == 1) {
			response.setTransactionMessage(null);
			response.setTransactionStatus(CommonConstants.SUCCESS);
		} else {
			response.setTransactionMessage(null);
			response.setTransactionStatus(CommonConstants.FAIL);
		}
		return response;
	}

	private void inactivePrevious(String policyNumber, String type, String status) {
		policyMasterRepository.inactivePreviousRecord(policyNumber, type, status);
	}

	private void activePrevious(String policyNumber, String type, String status) {
		policyMasterRepository.activePreviousRecord(policyNumber, type, status);
	}

	boolean stopScheduler = false;

	@Scheduled(cron = "0 0 0 * * ?") // Execute at midnight 12 am every day
	public boolean runTaskcheckissuancePolicySuccessOrNot() {
		if (!stopScheduler) {
			List<String> policyNumberLists = policyMasterRepository.checktrnandissuancePolicy();
			if (!policyNumberLists.isEmpty()) {
				for (String policyNumber : policyNumberLists) {
					checkissuancePolicySuccessOrNot(policyNumber);
				}
			} else {
				stopScheduler = true;
			}
		}
		return stopScheduler;
	}

	public void saveContributionToTxnEntries(PolicyMasterEntity dto) {
		FundRequestDto requestDto = new FundRequestDto();
		requestDto.setPolicyId(dto.getPolicyId());
		try {
			fundRestApiService.contirbutionToTransEntries(requestDto);
		} catch (Exception e) {
			logger.error("saveContributionToTxnEntries::", e);
		}
	}

	@Override
	public List<PolicyDetailSearchResponseDto> policyDetailSearch(
			PolicyDetailSearchRequestDto policyDetailSearchRequestDto) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		List<PolicyDetailSearchResponseDto> policyDetailSearchResponseList = new ArrayList<>();
		try {
			logger.info("PolicyServiceImpl:policyDetailSearch:Start");
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<MphMasterEntity> createQuery = criteriaBuilder.createQuery(MphMasterEntity.class);
			Root<MphMasterEntity> root = createQuery.from(MphMasterEntity.class);
			Join<MphMasterEntity, PolicyMasterEntity> join = root.join("policyMaster");
			if (StringUtils.isNotBlank(policyDetailSearchRequestDto.getPolicyNumber())) {
				join.on(criteriaBuilder.equal(join.get("policyNumber"),
						policyDetailSearchRequestDto.getPolicyNumber()));
			}

			predicates.add(join.get(PolicyConstants.POLICYSTATUS).in(Arrays.asList(PolicyConstants.APPROVED_NO)));
			if (StringUtils.isNotBlank(policyDetailSearchRequestDto.getMphCode())) {
				predicates.add(criteriaBuilder.equal(root.get("mphCode"), policyDetailSearchRequestDto.getMphCode()));
			}
			if (StringUtils.isNotBlank(policyDetailSearchRequestDto.getMphName())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("mphName")),
						policyDetailSearchRequestDto.getMphName().toLowerCase()));
			}
			if (StringUtils.isNotBlank(policyDetailSearchRequestDto.getPan())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("pan")),
						policyDetailSearchRequestDto.getPan().toLowerCase()));
			}
			if (StringUtils.isNotBlank(policyDetailSearchRequestDto.getMobileNo())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("mobileNo")),
						policyDetailSearchRequestDto.getMobileNo()));
			}
			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			createQuery.orderBy(criteriaBuilder.desc(root.get("modifiedOn")));
			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			List<MphMasterEntity> result = entityManager.createQuery(createQuery).getResultList();
			List<PolicyDetailSearchResponseDto> response = result.stream()
					.map(this::convertMasterEntityToDtoForPolicySearch).collect(Collectors.toList());
			for (PolicyDetailSearchResponseDto policyDetailSearchResponse : response) {
				PolicyDetailSearchResponseDto policyDetailSearchResponseDto = new PolicyDetailSearchResponseDto();
				policyDetailSearchResponseDto.setPan(policyDetailSearchResponse.getPan());
				policyDetailSearchResponseDto.setPolicyNumber(policyDetailSearchResponse.getPolicyNumber());
				policyDetailSearchResponseDto.setMphName(policyDetailSearchResponse.getMphName());
				policyDetailSearchResponseDto.setMphCode(policyDetailSearchResponse.getMphCode());
				policyDetailSearchResponseDto.setEmailAddress(policyDetailSearchResponse.getEmailAddress());
				policyDetailSearchResponseDto.setProductCode(policyDetailSearchResponse.getProductCode());
				policyDetailSearchResponseDto.setVariantCode(policyDetailSearchResponse.getVariantCode());
				policyDetailSearchResponseDto.setMobileNo(policyDetailSearchResponse.getMobileNo());
				policyDetailSearchResponseDto.setOperatingUnit(policyDetailSearchResponse.getOperatingUnit());
				policyDetailSearchResponseDto.setOperatingUnitType(PolicyConstants.OPERATING_UNIT_TYPE);
				policyDetailSearchResponseDto.setSchemeName(PolicyConstants.SCHEME_NAME);
				policyDetailSearchResponseDto.setTypeOfClient(PolicyConstants.TYPE_OF_CLIENT);
				policyDetailSearchResponseDto.setMasterPolicyHolderGstStateCode(
						policyDetailSearchResponse.getMasterPolicyHolderGstStateCode());
				policyDetailSearchResponseList.add(policyDetailSearchResponseDto);
			}

		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:policyDetailSearch", e);
		} finally {
			logger.info("PolicyServiceImpl:policyDetailSearch:Ends");
		}
		return policyDetailSearchResponseList;
	}

	@Override
	public DataTable getPolicyMemberDetailsDataTable(
			GetPolicyMemberDetailsRequestDto getPolicyMemberDetailsRequestDto) {
		DataTable dataTable = new DataTable();
		try {
			List<GetPolicyMemberDetailsResponseDto> getPolicyMemberDetailsResponseDtoList = memberMasterDao
					.getPolicyMemberDetailsDataTable(getPolicyMemberDetailsRequestDto);

			dataTable.setData(getPolicyMemberDetailsResponseDtoList);

			if (getPolicyMemberDetailsResponseDtoList.size() > 0) {
				dataTable.setNoOfPages(getPolicyMemberDetailsResponseDtoList.get(0).getNoOfPages());
				dataTable.setRecordsTotal(getPolicyMemberDetailsResponseDtoList.get(0).getTotalRecords());
			}

			return dataTable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataTable;
	}

	@Override
	public ByteArrayInputStream membersDownload(Long policyId, Long mphId, String unitCode) throws IOException {
		logger.info("PolicyServiceImpl:membersDownload:Started");
		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
			if (policyId != null && mphId != null && StringUtils.isNotBlank(unitCode)) {

				List<Object[]> fundDetailsList = memberMasterRepository
						.findMembersByPolicyIdAndMphIdAndUnitCodeAndIsActiveAndIsZeroId(policyId, mphId, unitCode);
				csvPrinter.printRecord(Arrays.asList("Employee Id", "LIC Id", "Name", "Date Of Birth"));

				for (Object[] obj : fundDetailsList) {
					csvPrinter.printRecord(Arrays.asList("\t" + ((obj[0] != null) ? obj[0].toString() : null),
							((obj[1] != null) ? obj[1].toString() : null),
							((obj[3] != null) ? obj[3].toString() : null),
							((obj[2] != null) ? (obj[2].toString()).substring(0, 10) : null)));

				}

			}

			csvPrinter.flush();
			logger.info("PolicyServiceImpl:membersDownload:Ended");
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			logger.info("PolicyServiceImpl:membersDownload:Exception ", e);
			throw new IOException(e);
		}

	}

//	private void checkZeroAccountInMember(Long policyId, BigDecimal zeroIdAmount, String role,
//			Long policyContributionId, Long memberContibutionId, String type) {
//		Optional<ZeroAccountTempEntity> zeroAccountsOpt = zeroAccountTempRepository
//				.findByIsActiveTrueAndPolicyId(policyId);
//		if (!zeroAccountsOpt.isPresent()) {
//			ZeroAccountTempEntity zeroMember = convertSetZeroAcc(policyId, zeroIdAmount, role);
//			zeroAccountTempRepository.save(zeroMember);
//
//			ZeroAccountEntriesTempEntity zeroMemberEntries = convertSetZeroAccEntries(policyId, zeroIdAmount, role,
//					policyContributionId, memberContibutionId, type);
//			zeroAccountEntriesTempRepository.save(zeroMemberEntries);
//		} else {
//			logger.info("PolicyServiceImpl:checkZeroAccountInMember--Ends");
//		}
//	}
//
//	private ZeroAccountEntriesTempEntity convertSetZeroAccEntries(Long policyId, BigDecimal zeroIdAmount, String role,
//			Long policyContributionId, Long memberContibutionId, String type) {
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
//		return response;
//	}
//
//	private ZeroAccountTempEntity convertSetZeroAcc(Long policyId, BigDecimal zeroIdAmount, String role) {
//		ZeroAccountTempEntity response = new ZeroAccountTempEntity();
//		response.setZeroAccId(null);
//		response.setZeroIdAmount(zeroIdAmount);
//		response.setPolicyId(policyId);
//		response.setCreatedBy(role);
//		response.setCreatedOn(new Date());
//		response.setModifiedBy(role);
//		response.setModifiedOn(new Date());
//		response.setIsActive(Boolean.TRUE);
//		return response;
//	}
//	
//	public HttpHeaders getAuthHeaders() {
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(CommonConstants.AUTHORIZATION, "Bearer ");
//		headers.add(CommonConstants.CORELATIONID, UUID.randomUUID().toString());
//		headers.add(CommonConstants.BUSINESSCORELATIONID, UUID.randomUUID().toString());
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		return headers;
//	}
//
//	public static final String URL_EMPTY = "URL is empty";
//
//	public CommonResponseStampDto getStampCunsumptionDetails(
//			StampDutyConsumptionRequestDto stampDutyConsumptionRequestDto) throws ApplicationException {
//		CommonResponseStampDto responseDto = new CommonResponseStampDto();
//		responseDto.setTransactionStatus(CommonConstants.ERROR);
//		logger.info("getStampCunsumptionDetails::Start::{},", stampDutyConsumptionRequestDto.getPolicyNumber());
//		try {
//			stampDutyConsumptionRequestDto.setStampAmount(
//					NumericUtils.convertBigDecimalToString(stampDutyConsumptionRequestDto.getStampAmountBigDecimal()));
//			try {
//				System.out.println(objectMapper.writeValueAsString(stampDutyConsumptionRequestDto));
//			} catch (JsonProcessingException e) {
//				logger.error("Error:", e);
//			}
//			String url = environment.getProperty("STAMP_CONSUMSTION_DETAILS");
//			if (StringUtils.isNotBlank(url)) {
//				HttpHeaders headers = getAuthHeaders();
//				HttpEntity<StampDutyConsumptionRequestDto> entity = new HttpEntity<>(stampDutyConsumptionRequestDto,
//						headers);
//				CommonResponseStampDto rateResponseDto = restTemplateService.postForObject(url, entity,
//						CommonResponseStampDto.class);
//				if (rateResponseDto != null) {
//					if (StringUtils.isNotBlank(rateResponseDto.getTransactionStatus())
//							&& rateResponseDto.getTransactionStatus()
//									.equalsIgnoreCase(PolicyConstants.SUCCESSRESPONSEFORSTAMPCONSUMPTION)) {
//						return rateResponseDto;
//					} else if (StringUtils.isNotBlank(rateResponseDto.getTransactionStatus())
//							&& rateResponseDto.getTransactionStatus()
//									.equalsIgnoreCase(PolicyConstants.FAILURERESPONSEFORSTAMPCONSUMPTION)) {
//						return rateResponseDto;
//					} else {
//						throw new ApplicationException(PolicyConstants.SA_STAMPCONSUMPTION_FUND_SERVICE + "-"
//								+ stampDutyConsumptionRequestDto.getPolicyNumber() + "_"
//								+ rateResponseDto.getTransactionMessage());
//					}
//				} else {
//					throw new ApplicationException(PolicyConstants.SA_STAMPCONSUMPTION_FUND_SERVICE + "No response.");
//				}
//			} else {
//				throw new ApplicationException(PolicyConstants.SA_STAMPCONSUMPTION_FUND_SERVICE + URL_EMPTY);
//			}
//		} catch (HttpClientErrorException | IllegalArgumentException e) {
//			responseDto.setTransactionStatus(CommonConstants.ERROR);
//			responseDto.setTransactionMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
//			logger.error("Exception getStampCunsumptionDetails::End::{},",
//					stampDutyConsumptionRequestDto.getPolicyNumber(), e);
//		}
//		logger.info("getStampCunsumptionDetails::End::{},", stampDutyConsumptionRequestDto.getPolicyNumber());
//		return responseDto;
//	}
//
//	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
//		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
//	}

	@Override
	public PolicyResponseDto getMemberDetailsByLicIdandMemberId(String licId, Long policyId, String membershipNumber) {
		PolicyResponseDto commonDto = new PolicyResponseDto();

		try {
			List<Object> memberMasterEntity = memberMasterRepository.fetchByLicIdAndPolicyIdAndMembershipNumber(licId,
					policyId, membershipNumber);
			List<MemberMasterDto> response = new ArrayList<>();
			if (memberMasterEntity != null && !memberMasterEntity.isEmpty()) {
				for (Object object : memberMasterEntity) {
					Object[] ob = (Object[]) object;
					MemberMasterDto masterDto = new MemberMasterDto();
					masterDto.setMembershipNumber(String.valueOf(ob[0]));
					masterDto.setLicId(String.valueOf(ob[1]));
					masterDto.setMemberStatus(String.valueOf(ob[2]));
					masterDto.setFirstName(String.valueOf(ob[3]));
					masterDto.setDateOfBirth(DateUtils.convertStringToDate(String.valueOf(ob[4])));
					masterDto.setCategoryNo(NumericUtils.convertStringToInteger(String.valueOf(ob[5])));
					masterDto.setPolicyId(policyId);
					response.add(masterDto);
				}

				commonDto.setResponse(response);
//				commonDto.setResponse(modelMapper.map(memberMasterEntity, MemberMasterDto.class));

				commonDto.setTransactionStatus(CommonConstants.STATUS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				logger.info("Policy member not found for the given licId: {} and PolicyId: {}", licId, policyId);
				commonDto.setTransactionMessage(CommonConstants.FAIL);
				commonDto.setTransactionStatus(
						"Policy member not found for the given licId: " + licId + " and PolicyId: " + policyId);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- getMemberContributionDetails-- getPolicyMember--", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionMessage(CommonConstants.ERROR);
		}
		return commonDto;
	}

	@Override
	public CommonResponseStampDto DepsoitRefundPolicySearch(DepositRefundPolicySearchRequestDto dto) {
		CommonResponseStampDto responseStampDto = new CommonResponseStampDto();
		DepositRefundPolicySearchResponseDto responseDto = new DepositRefundPolicySearchResponseDto();
		Object result = null;
		String getvarient = null;
		IcodeMasterEntity entity = null;
		try {
			logger.info("PolicyServiceImpl---DepsoitRefundPolicySearch---start");
			result = policyMasterRepository.getDepsoitRefundPolicySearch(dto.getPolicyNumber(), dto.getUnitCode());
			Object[] newObject = (Object[]) result;

			if (result != null) {
				Object[] newObjects = (Object[]) result;
				responseDto.setPolicyNumber((newObjects[0] != null)?newObjects[0].toString():null);
			responseDto.setPolicyId(Long.valueOf(String.valueOf(newObjects[1])));
			responseDto.setMphId(Long.valueOf(String.valueOf(newObjects[2])));
			responseDto.setMphName((newObjects[3] != null)?newObjects[3].toString():null);
			responseDto.setMphCode((newObjects[4] != null)?newObjects[4].toString():null);
			responseDto.setUnitCode((newObjects[5] != null)?newObjects[5].toString():null);
			responseDto.setPolicyStatus((newObjects[6] != null)?newObjects[6].toString():null);
			responseDto.setIfscCode((newObjects[7] != null)?newObjects[7].toString():null);
			responseDto.setAccountType((newObjects[8] != null)?newObjects[8].toString():null);
			responseDto.setBankName((newObjects[9] != null)?newObjects[9].toString():null);
			responseDto.setBankBranch((newObjects[10] != null)?newObjects[10].toString():null);
			responseDto.setProduct((newObjects[11] != null)?newObjects[11].toString():null);			
			responseDto.setVariant((newObjects[13] != null)?newObjects[13].toString():null);
			responseDto.setCustomerId((newObjects[2] != null)?newObjects[2].toString():null);	
			responseDto.setPolicyCode((newObjects[1] != null)?newObjects[1].toString():null);
			responseDto.setAccountNumber((newObjects[14] != null)?newObjects[14].toString():null);		
			responseDto.setConfirmAccountNumber((newObjects[15] != null)?newObjects[15].toString():null);

				JsonNode response = integrationService
						.getProductDetailsByProductId(NumericUtils.stringToLong(String.valueOf(newObjects[11])));
				if (response != null) {
					JsonNode proposeDetails = response.path("responseData");
					String product = proposeDetails.path("productCode").textValue();
					String productId = proposeDetails.path("productName").textValue();
					responseDto.setProduct(productId);
					responseDto.setProductCode(product);
					responseDto.setProductCodeIcode(product);

				}

				JsonNode newResponse = integrationService
						.getVariantDetailsByProductVariantId(NumericUtils.stringToLong(responseDto.getVariant()));
				if (newResponse != null) {
					JsonNode proposeDetails = newResponse.path("responseData");
					String variantCode = proposeDetails.path("subCategory").textValue();
					String variantVersion = proposeDetails.path("variantVersion").textValue();

					if (StringUtils.isNoneBlank(variantVersion)) {
						entity = icodeMasterRepository.findByVariant(variantVersion);
					}

					responseDto.setIcodeForBusinessType(Long.valueOf(String.valueOf(entity.getIcodeForBusinessType())));
					responseDto.setIcodeForBusinessLine(Long.valueOf(String.valueOf(entity.getIcodeForLob())));
					responseDto.setIcodeForParticipatingType(
							Long.valueOf(String.valueOf(entity.getIcodeForParticipating())));
					responseDto.setIcodeForBusinessSegment(
							Long.valueOf(String.valueOf(entity.getIcodeForBusinessSegment())));

					responseDto.setIcodeForProductLine(Long.valueOf(String.valueOf(entity.getIcodeForProductLine())));
					responseDto.setIcodeForVariant(Long.valueOf(String.valueOf(entity.getIcodeForVariant())));
					responseDto.setVariantIcode(String.valueOf(entity.getIcodeForVariant()));

					responseDto.setVariantCode(variantCode);
					responseDto.setSchemeName(variantCode);
//					responseDto.setVariantIcode(variantCode);		
				}

				responseStampDto.setResponseData(responseDto);
				responseStampDto.setTransactionMessage(CommonConstants.FETCH);
				responseStampDto.setTransactionStatus(CommonConstants.STATUS);
			} else {
				responseStampDto.setTransactionMessage(CommonConstants.DENY);
				responseStampDto.setTransactionStatus(CommonConstants.ERROR);
			}

		} catch (Exception e) {
			logger.info("PolicyServiceImpl---DepsoitRefundPolicySearch---error" + e);
			responseStampDto.setTransactionMessage(CommonConstants.DENY);
			responseStampDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("PolicyServiceImpl---DepsoitRefundPolicySearch---ended");
		return responseStampDto;
	}

	@Override
	public PolicyFundStmtResponseDto fetchDetailsFrPolicyFundStatement(PolicyFundStmtRequestDto requestDto) {
		PolicyFundStmtResponseDto responseDto = new PolicyFundStmtResponseDto();
		List<PolicyStmtDto> listOfPolicies = new ArrayList<>();
		List<PolicyMasterEntity> listPolicyMasterEntity = new ArrayList<>();
		try {
//			listPolicyMasterEntity = (List<PolicyMasterEntity>) policyMasterRepository
//					.findByYearQtrVariant(requestDto.getUnitId(),requestDto.getFinancialYear(),requestDto.getFrequency());
			List<Object[]> policyDetails = policyMasterRepository.findByYearQtrVariant(requestDto.getUnitId(),
					requestDto.getFinancialYear(), requestDto.getFrequency(), requestDto.getVariant());
			BeanUtils.copyProperties(requestDto, responseDto);
			if (!policyDetails.isEmpty()) {
				for (Object[] object : policyDetails) {
					PolicyStmtDto policyStmtDto = new PolicyStmtDto();
					policyStmtDto.setPolicyId(String.valueOf(object[0]));
					policyStmtDto.setPolicyNumber(String.valueOf(object[1]));
//					Default all flags are false
					policyStmtDto.setIsOsClaim(false);
					policyStmtDto.setIsOSDeposit(false);
					if (String.valueOf(object[2]).equals("1")) {
						policyStmtDto.setIsFundGenerated(true);
						policyStmtDto.setRemarks("Statement already generated");
					} else if (String.valueOf(object[2]).equals("0")) {
						policyStmtDto.setIsFundGenerated(false);
					}
					listOfPolicies.add(policyStmtDto);
				}
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		responseDto.setListOfPolicies(listOfPolicies);
		// TODO Auto-generated method stub
		return responseDto;
	}

}
