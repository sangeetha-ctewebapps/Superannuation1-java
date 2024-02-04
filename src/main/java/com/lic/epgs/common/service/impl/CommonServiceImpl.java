package com.lic.epgs.common.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.lic.epgs.common.dto.AmountAdjusted;
import com.lic.epgs.common.dto.CommonDocumentMasterDto;
import com.lic.epgs.common.dto.CommonExternalApiDto;
import com.lic.epgs.common.dto.CommonExternalApiResponseDto;
import com.lic.epgs.common.dto.CommonResponseDto;
import com.lic.epgs.common.dto.CommonStatusDto;
import com.lic.epgs.common.dto.DepositTransferInputModel;
import com.lic.epgs.common.dto.PolicyResponseDataAnnuityDto;
import com.lic.epgs.common.dto.DepositTransferResponse;
import com.lic.epgs.common.dto.DepositTransferResponseModel;
import com.lic.epgs.common.dto.PolMphAddressDetailsAnnuityDto;
import com.lic.epgs.common.dto.PolMphBankAccountDetailsAnnuityDto;
import com.lic.epgs.common.dto.PolMphBasicDetailsAnnuityDto;
import com.lic.epgs.common.dto.PolMphContactDetailsAnnuityDto;
import com.lic.epgs.common.dto.PolicyResponseAnnuityDto;
import com.lic.epgs.common.dto.PolicyResponseDataAnnuityDto;
import com.lic.epgs.common.dto.SearchDepositTransferDto;
import com.lic.epgs.common.entity.CommoclaimMemberUploadFileTypeEntity;
import com.lic.epgs.common.entity.CommonAccountTypeEntity;
import com.lic.epgs.common.entity.CommonClaimAmountPayableEntity;
import com.lic.epgs.common.entity.CommonClaimAnuityCreationOptionsEntity;
import com.lic.epgs.common.entity.CommonClaimApproverActionsEntity;
import com.lic.epgs.common.entity.CommonClaimCommutationCalcEntity;
import com.lic.epgs.common.entity.CommonClaimDocumentStatusEntity;
import com.lic.epgs.common.entity.CommonClaimIntimaitonTypeEntity;
import com.lic.epgs.common.entity.CommonClaimMakerActionsEntity;
import com.lic.epgs.common.entity.CommonClaimReasonforexitEntity;
import com.lic.epgs.common.entity.CommonClaimRequiredDocumentEntity;
import com.lic.epgs.common.entity.CommonDocumentMasterEntity;
import com.lic.epgs.common.entity.CommonMaritalStatusEntity;
import com.lic.epgs.common.entity.CommonNomineeRelationShipEntity;
import com.lic.epgs.common.entity.CommonPolicyStatusEntity;
import com.lic.epgs.common.entity.CommonSharedAmountTypeEntity;
import com.lic.epgs.common.entity.CommonStatusEntity;
import com.lic.epgs.common.entity.SampleFileMasterEntity;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.common.repository.CommonAccountTypeRepository;
import com.lic.epgs.common.repository.CommonClaimAmountPayableRepository;
import com.lic.epgs.common.repository.CommonClaimAnuityCreationOptionsRepository;
import com.lic.epgs.common.repository.CommonClaimApproverActionsRepository;
import com.lic.epgs.common.repository.CommonClaimCommutationCalcRepository;
import com.lic.epgs.common.repository.CommonClaimDocumentStatusRepository;
import com.lic.epgs.common.repository.CommonClaimIntimaitonTypeRepository;
import com.lic.epgs.common.repository.CommonClaimMakerActionsRepository;
import com.lic.epgs.common.repository.CommonClaimReasonforexitRepository;
import com.lic.epgs.common.repository.CommonClaimRequiredDocumentRepository;
import com.lic.epgs.common.repository.CommonDocumentMasterRepository;
import com.lic.epgs.common.repository.CommonMaritalStatusRepository;
import com.lic.epgs.common.repository.CommonNomineeRelationShipRepository;
import com.lic.epgs.common.repository.CommonPolicyStatusRepository;
import com.lic.epgs.common.repository.CommonSharedAmountTypeRepository;
import com.lic.epgs.common.repository.CommonStatusRepository;
import com.lic.epgs.common.repository.CommonclaimMemberUploadFileTypeRepository;
import com.lic.epgs.common.repository.SampleFileMasterRepository;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.policy.entity.MemberAddressEntity;
import com.lic.epgs.policy.entity.MemberAddressTempEntity;
import com.lic.epgs.policy.entity.MemberAppointeeEntity;
import com.lic.epgs.policy.entity.MemberAppointeeTempEntity;
import com.lic.epgs.policy.entity.MemberBankEntity;
import com.lic.epgs.policy.entity.MemberBankTempEntity;
import com.lic.epgs.policy.entity.MemberContributionEntity;
import com.lic.epgs.policy.entity.MemberContributionSummaryEntity;
import com.lic.epgs.policy.entity.MemberContributionSummaryTempEntity;
import com.lic.epgs.policy.entity.MemberContributionTempEntity;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MemberMasterTempEntity;
import com.lic.epgs.policy.entity.MemberNomineeEntity;
import com.lic.epgs.policy.entity.MemberNomineeTempEntity;
import com.lic.epgs.policy.entity.MemberTransactionEntriesEntity;
import com.lic.epgs.policy.entity.MphAddressEntity;
import com.lic.epgs.policy.entity.MphAddressTempEntity;
import com.lic.epgs.policy.entity.MphBankEntity;
import com.lic.epgs.policy.entity.MphBankTempEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.MphMasterTempEntity;
import com.lic.epgs.policy.entity.MphRepresentativesEntity;
import com.lic.epgs.policy.entity.MphRepresentativesTempEntity;
import com.lic.epgs.policy.entity.PolicyContributionEntity;
import com.lic.epgs.policy.entity.PolicyContributionSummaryEntity;
import com.lic.epgs.policy.entity.PolicyContributionSummaryTempEntity;
import com.lic.epgs.policy.entity.PolicyContributionTempEntity;
import com.lic.epgs.policy.entity.PolicyDepositEntity;
import com.lic.epgs.policy.entity.PolicyDepositTempEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterTempEntity;
import com.lic.epgs.policy.entity.PolicyNotesEntity;
import com.lic.epgs.policy.entity.PolicyNotesTempEntity;
import com.lic.epgs.policy.entity.PolicyRulesEntity;
import com.lic.epgs.policy.entity.PolicyRulesTempEntity;
import com.lic.epgs.policy.entity.PolicyValuationEntity;
import com.lic.epgs.policy.entity.PolicyValuationTempEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntriesEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntriesTempEntity;
import com.lic.epgs.policy.entity.ZeroAccountTempEntity;
import com.lic.epgs.policy.repository.MemberContributionRepository;
import com.lic.epgs.policy.repository.MemberTransactionEntriesRepository;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.PolicyDepositRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.policyservicing.policylvl.entity.conversion.PolicyLevelConversionEntity;
import com.lic.epgs.quotation.repository.QuotationRepository;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

@Service
public class CommonServiceImpl implements CommonService {
	protected final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	@Qualifier(value = "restTemplateService")
	private RestTemplate restTemplateService;

	@Autowired
	private Environment environment;

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private CommonStatusRepository commonStatusRepository;

	@Autowired
	private CommonClaimRequiredDocumentRepository commonClaimRequiredDocumentRepository;

	@Autowired
	private CommonClaimDocumentStatusRepository commonClaimDocumentStatusRepository;

	@Autowired
	private CommonClaimIntimaitonTypeRepository commonClaimIntimaitonTypeRepository;

	@Autowired
	private CommonClaimReasonforexitRepository commonClaimReasonforexitRepository;

	@Autowired
	private CommonclaimMemberUploadFileTypeRepository commonclaimMemberUploadFileTypeRepository;

	@Autowired
	private CommonClaimMakerActionsRepository commonClaimMakerActionsRepository;

	@Autowired
	private CommonClaimApproverActionsRepository commonClaimApproverActionsRepository;

	@Autowired
	private CommonClaimCommutationCalcRepository commonClaimCommutationCalcRepository;

	@Autowired
	private CommonClaimAmountPayableRepository commonClaimAmountPayableRepository;

	@Autowired
	private CommonClaimAnuityCreationOptionsRepository commonClaimAnuityCreationOptionsRepository;

	@Autowired
	private CommonNomineeRelationShipRepository commonNomineeRelationShipRepository;

	@Autowired
	private CommonPolicyStatusRepository commonPolicyStatusRepository;

	@Autowired
	private CommonMaritalStatusRepository commonMaritalStatusRepository;

	@Autowired
	private CommonAccountTypeRepository commonAccountTypeRepository;

	@Autowired
	private CommonSharedAmountTypeRepository commonSharedAmountTypeRepository;

	@Autowired
	private SampleFileMasterRepository sampleFileMasterRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	private PolicyMasterRepository policyMasterRepository;
	
	@Autowired
	private MemberContributionRepository memberContributionRepository;

	@Autowired
	protected EntityManager entityManager;

	@Autowired
	private CommonDocumentMasterRepository commonDocumentMasterRepository;
	
	@Autowired
	private MemberTransactionEntriesRepository memberTransactionEntriesRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	
	@Autowired
	private PolicyDepositRepository  depositRepository;
	
	@Autowired
	IntegrationService integrationService;

	public synchronized String getPolicySeq() {
		return commonService.getSequence(CommonConstants.POLICY_MODULE);
	}

	public synchronized String getSequence(String type) {
		logger.info("CommonSequenceService-getSequence-Start");
		try {
			return quotationRepository.getSequence(type);
		} catch (NonUniqueResultException e) {
			logger.error("CommonSequenceService-getSequence-Error:");
		}
		logger.info("CommonSequenceService-getSequence-End");
		return null;
	}

	@Override
	public CommonResponseDto commonStatus() {
		CommonResponseDto statusResponseDto = new CommonResponseDto();
		logger.info("CommonServiceImpl --commonStatus-- Start");

		try {
			List<CommonStatusEntity> entity = commonStatusRepository.findAllByIsActiveTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonStatusDto> commonStatusList = new ArrayList<>();
				for (CommonStatusEntity commonStatus : entity) {
					CommonStatusDto statusDto = new CommonStatusDto();
					statusDto.setId(commonStatus.getId());
					statusDto.setCode(commonStatus.getCode());
					statusDto.setType(commonStatus.getType());
					statusDto.setDescription(commonStatus.getDescription());
					statusDto.setDescription1(commonStatus.getDescription1());
					statusDto.setName(commonStatus.getName());
					statusDto.setIsActive(NumericUtils.convertBooleanToString(commonStatus.getIsActive()));
					commonStatusList.add(statusDto);
				}
				statusResponseDto.setResponseData(commonStatusList);
				statusResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				statusResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				statusResponseDto.setTransactionStatus(CommonConstants.ERROR);
				statusResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			statusResponseDto.setTransactionStatus(CommonConstants.ERROR);
			statusResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --commonStatus-- End");
		return statusResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto claimrequireddocument() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --claimrequireddocument-- Start");

		try {
			List<CommonClaimRequiredDocumentEntity> entity = commonClaimRequiredDocumentRepository
					.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonClaimRequiredDocumentEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --claimrequireddocument-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto claimdocumentstatus() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --claimrequireddocument-- Start");

		try {
			List<CommonClaimDocumentStatusEntity> entity = commonClaimDocumentStatusRepository.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonClaimDocumentStatusEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --claimrequireddocument-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto claimIntimaitonType() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --claimIntimaitonType-- Start");

		try {
			List<CommonClaimIntimaitonTypeEntity> entity = commonClaimIntimaitonTypeRepository.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonClaimIntimaitonTypeEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --claimIntimaitonType-- End");
		return commonExternalApiResponseDto;

	}

	@Override
	public CommonExternalApiResponseDto claimReasonforexit() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --claimReasonforexit-- Start");

		try {
			List<CommonClaimReasonforexitEntity> entity = commonClaimReasonforexitRepository.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonClaimReasonforexitEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --claimReasonforexit-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto claimMemberUploadFileType() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --claimMemberUploadFileType-- Start");

		try {
			List<CommoclaimMemberUploadFileTypeEntity> entity = commonclaimMemberUploadFileTypeRepository
					.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommoclaimMemberUploadFileTypeEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --claimMemberUploadFileType-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto claimMakerActions() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --claimMakerActions-- Start");

		try {
			List<CommonClaimMakerActionsEntity> entity = commonClaimMakerActionsRepository.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonClaimMakerActionsEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --claimMakerActions-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto claimApproverActions() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --claimMakerActions-- Start");

		try {
			List<CommonClaimApproverActionsEntity> entity = commonClaimApproverActionsRepository.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonClaimApproverActionsEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --claimMakerActions-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto claimCommutationCalc() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --claimCommutationCalc-- Start");

		try {
			List<CommonClaimCommutationCalcEntity> entity = commonClaimCommutationCalcRepository.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonClaimCommutationCalcEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --claimCommutationCalc-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto claimAmountPayable() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --claimAmountPayable-- Start");
		try {
			List<CommonClaimAmountPayableEntity> entity = commonClaimAmountPayableRepository.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonClaimAmountPayableEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --claimAmountPayable-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto claimAnuityCreationOptions() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --claimAnuityCreationOptions-- Start");
		try {
			List<CommonClaimAnuityCreationOptionsEntity> entity = commonClaimAnuityCreationOptionsRepository
					.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonClaimAnuityCreationOptionsEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --claimAnuityCreationOptions-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto nomineeRelationShip() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --nomineeRelationShip-- Start");
		try {
			List<CommonNomineeRelationShipEntity> entity = commonNomineeRelationShipRepository.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonNomineeRelationShipEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --nomineeRelationShip-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto policyStatusApi() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --policyStatusApi-- Start");
		try {
			List<CommonPolicyStatusEntity> entity = commonPolicyStatusRepository.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonPolicyStatusEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --policyStatusApi-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto maritalStatus() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --maritalStatus-- Start");
		try {
			List<CommonMaritalStatusEntity> entity = commonMaritalStatusRepository.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonMaritalStatusEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --maritalStatus-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto accountType() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --accountType-- Start");
		try {
			List<CommonAccountTypeEntity> entity = commonAccountTypeRepository.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonAccountTypeEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --accountType-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public CommonExternalApiResponseDto sharedAmountType() {
		CommonExternalApiResponseDto commonExternalApiResponseDto = new CommonExternalApiResponseDto();
		logger.info("CommonServiceImpl --sharedAmountType-- Start");
		try {
			List<CommonSharedAmountTypeEntity> entity = commonSharedAmountTypeRepository.findAllByStatusTrue();
			if (!entity.isEmpty()) {
				ArrayList<CommonExternalApiDto> commonExternalApiDtoList = new ArrayList<>();
				for (CommonSharedAmountTypeEntity requiredDocument : entity) {
					CommonExternalApiDto commonExternalApiDto = new CommonExternalApiDto();
					commonExternalApiDto.setId(requiredDocument.getId());
					commonExternalApiDto.setCode(requiredDocument.getCode());
					commonExternalApiDto.setDescription(requiredDocument.getDescription());
					commonExternalApiDto
							.setIsmandatory(NumericUtils.convertBooleanToString(requiredDocument.getIsMandatory()));
					commonExternalApiDto.setStatus(NumericUtils.convertBooleanToString(requiredDocument.getStatus()));
					commonExternalApiDtoList.add(commonExternalApiDto);
				}
				commonExternalApiResponseDto.setResponseData(commonExternalApiDtoList);
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
				commonExternalApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (NonUniqueResultException e) {
			commonExternalApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
			commonExternalApiResponseDto.setTransactionMessage(CommonConstants.INVALIDREQUEST);
		}
		logger.info("CommonServiceImpl --sharedAmountType-- End");
		return commonExternalApiResponseDto;
	}

	@Override
	public void getSampleFile(String fileType, HttpServletResponse response) {
		try {
			logger.info("CommonServiceImpl:getSampleFile:Starts");
			if (fileType != null) {
				SampleFileMasterEntity fileEntity = sampleFileMasterRepository
						.findByModuleNameAndIsActiveTrue(fileType);

				if (fileEntity != null) {

					byte[] bytes = fileEntity.getActualFile();

					response.setContentType(
							"Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					response.setHeader("Content-Disposition",
							"attachment;filename=" + fileType + "_UPLOAD_FORMAT.xlsx");
					ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
					baos.write(bytes, 0, bytes.length);
					OutputStream os = response.getOutputStream();
					response.setHeader("STATUS", "File is Downloaded Successfully!!!");
					baos.writeTo(os);
					os.flush();
					os.close();
				}

			} else {
				response.setHeader("STATUS", "FileType is null!!");
			}

		} catch (Exception e) {
			logger.error("Exception-- CommonServiceImpl-- getSampleFile --", e);

		}

		logger.info("CommonServiceImpl:getSampleFile:Ends");

	}

	private boolean isFileValid(MultipartFile file) {
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		return Arrays.asList("xlsx").contains(ext);

	}

	@Override
	public CommonResponseDto saveSampleFile(String fileType, String createdBy, MultipartFile file) {
		CommonResponseDto response = new CommonResponseDto();
		try {
			logger.info("CommonServiceImpl:saveSampleFile:Starts");

			if (fileType != null && isFileValid(file)) {

				SampleFileMasterEntity existingFileEntity = sampleFileMasterRepository
						.findByModuleNameAndIsActiveTrue(fileType);

				if (existingFileEntity != null) {
					existingFileEntity.setIsActive(Boolean.FALSE);
					existingFileEntity.setModifiedBy(createdBy);
					existingFileEntity.setModifiedOn(new Date());
					sampleFileMasterRepository.save(existingFileEntity);

				}

				SampleFileMasterEntity newFileEntity = new SampleFileMasterEntity();
				newFileEntity.setActualFile(file.getBytes());
				newFileEntity.setIsActive(Boolean.TRUE);
				newFileEntity.setCreatedBy(createdBy);
				newFileEntity.setModuleName(fileType);
				newFileEntity.setCreatedOn(new Date());
				sampleFileMasterRepository.save(newFileEntity);

				response.setTransactionStatus(CommonConstants.SUCCESS);
				response.setTransactionMessage(CommonConstants.FETCH);
			} else {
				response.setTransactionStatus(CommonConstants.FAIL);
				response.setTransactionMessage("File Type & File is invalid!");
			}

		} catch (Exception e) {
			logger.error("Exception-- CommonServiceImpl-- saveSampleFile --", e);

		}

		logger.info("CommonServiceImpl:saveSampleFile:Ends");
		return response;
	}

	public MphMasterEntity convertMphMasterTempEntityToMphMasterMasterEntityNewForService(MphMasterTempEntity masterDto,
			String status, String policyNumber, PolicyLevelConversionEntity policyLevelConversionEntity) {

		MphMasterEntity response = new MphMasterEntity();

		response.setMphId(null);
		response.setMphCode(masterDto.getMphCode());
		response.setMphName(masterDto.getMphName());
		response.setMphType(masterDto.getMphType());
		response.setProposalNumber(masterDto.getProposalNumber());
		response.setProposalId(masterDto.getProposalId());
		response.setCin(masterDto.getCin());
		response.setPan(masterDto.getPan());
		response.setAlternatePan(masterDto.getAlternatePan());
		response.setLandlineNo(masterDto.getLandlineNo());
		response.setCountryCode(masterDto.getCountryCode());
		response.setMobileNo(masterDto.getMobileNo());
		response.setEmailId(masterDto.getEmailId());
		response.setFax(masterDto.getFax());
		response.setIsActive(masterDto.getIsActive());
		response.setCreatedOn(masterDto.getCreatedOn());
		response.setCreatedBy(masterDto.getCreatedBy());
		response.setModifiedOn(masterDto.getModifiedOn());
		response.setModifiedBy(masterDto.getModifiedBy());
		Set<MphAddressEntity> mphAddress = new HashSet<>();
		for (MphAddressTempEntity mphAddressDto : masterDto.getMphAddress()) {
			MphAddressEntity mphAddressTempEntity = new MphAddressEntity();
			mphAddressTempEntity.setAddressId(null);
			mphAddressTempEntity.setMphId(response.getMphId());
			mphAddressTempEntity.setAddressType(mphAddressDto.getAddressType());
			mphAddressTempEntity.setAddressLine1(mphAddressDto.getAddressLine1());
			mphAddressTempEntity.setAddressLine2(mphAddressDto.getAddressLine2());
			mphAddressTempEntity.setAddressLine3(mphAddressDto.getAddressLine3());
			mphAddressTempEntity.setPincode(mphAddressDto.getPincode());
			mphAddressTempEntity.setCityLocality(mphAddressDto.getCityLocality());
			mphAddressTempEntity.setCityId(mphAddressDto.getCityId());
			mphAddressTempEntity.setDistrict(mphAddressDto.getDistrict());
			mphAddressTempEntity.setDistrictId(mphAddressDto.getDistrictId());
			mphAddressTempEntity.setCountryId(mphAddressDto.getCountryId());
			mphAddressTempEntity.setStateId(mphAddressDto.getStateId());
			mphAddressTempEntity.setStateName(mphAddressDto.getStateName());
			mphAddressTempEntity.setIsActive(mphAddressDto.getIsActive());
			mphAddressTempEntity.setCreatedOn(mphAddressDto.getCreatedOn());
			mphAddressTempEntity.setCreatedBy(mphAddressDto.getCreatedBy());
			mphAddressTempEntity.setModifiedOn(mphAddressDto.getModifiedOn());
			mphAddressTempEntity.setModifiedBy(mphAddressDto.getModifiedBy());
			mphAddress.add(mphAddressTempEntity);
		}
		response.setMphAddress(mphAddress);
		Set<MphBankEntity> mphBank = new HashSet<>();
		for (MphBankTempEntity mphBankDto : masterDto.getMphBank()) {
			MphBankEntity mphBankTempEntity = new MphBankEntity();
			mphBankTempEntity.setMphBankId(null);
			mphBankTempEntity.setMphId(response.getMphId());
			mphBankTempEntity.setAccountNumber(mphBankDto.getAccountNumber());
			mphBankTempEntity.setConfirmAccountNumber(mphBankDto.getConfirmAccountNumber());
			mphBankTempEntity.setAccountType(mphBankDto.getAccountType());
			mphBankTempEntity.setIfscCodeAvailable(mphBankDto.getIfscCodeAvailable());
			mphBankTempEntity.setIfscCode(mphBankDto.getIfscCode());
			mphBankTempEntity.setBankName(mphBankDto.getBankName());
			mphBankTempEntity.setBankBranch(mphBankDto.getBankBranch());
			mphBankTempEntity.setBankAddress(mphBankDto.getBankAddress());
			mphBankTempEntity.setStdCode(mphBankDto.getStdCode());
			mphBankTempEntity.setLandlineNumber(mphBankDto.getLandlineNumber());
			mphBankTempEntity.setEmailId(mphBankDto.getEmailId());
			mphBankTempEntity.setCountryCode(mphBankDto.getCountryCode());
			mphBankTempEntity.setCountryId(mphBankDto.getCountryId());
			mphBankTempEntity.setStateId(mphBankDto.getStateId());
			mphBankTempEntity.setDistrictId(mphBankDto.getDistrictId());
			mphBankTempEntity.setCityId(mphBankDto.getCityId());
			mphBankTempEntity.setTownLocality(mphBankDto.getTownLocality());
			mphBankTempEntity.setIsActive(mphBankDto.getIsActive());
			mphBankTempEntity.setCreatedBy(mphBankDto.getCreatedBy());
			mphBankTempEntity.setCreatedOn(mphBankDto.getCreatedOn());
			mphBankTempEntity.setModifiedOn(mphBankDto.getModifiedOn());
			mphBankTempEntity.setModifiedBy(mphBankDto.getModifiedBy());
			mphBank.add(mphBankTempEntity);
		}
		response.setMphBank(mphBank);
		Set<MphRepresentativesEntity> mphRepresentative = new HashSet<>();
		for (MphRepresentativesTempEntity mphRepresentativeDto : masterDto.getMphRepresentative()) {
			MphRepresentativesEntity mphRepresentativesTempEntity = new MphRepresentativesEntity();
			mphRepresentativesTempEntity.setRepId(null);
			mphRepresentativesTempEntity.setMphId(response.getMphId());
			mphRepresentativesTempEntity.setRepresentativeCode(mphRepresentativeDto.getRepresentativeCode());
			mphRepresentativesTempEntity.setRepresentativeName(mphRepresentativeDto.getRepresentativeName());
			mphRepresentativesTempEntity.setLandlineNo(mphRepresentativeDto.getLandlineNo());
			mphRepresentativesTempEntity.setCountryCode(mphRepresentativeDto.getCountryCode());
			mphRepresentativesTempEntity.setMobileNo(mphRepresentativeDto.getMobileNo());
			mphRepresentativesTempEntity.setEmailId(mphRepresentativeDto.getEmailId());
			mphRepresentativesTempEntity.setAddressType(mphRepresentativeDto.getAddressType());
			mphRepresentativesTempEntity.setAddressLine1(mphRepresentativeDto.getAddressLine1());
			mphRepresentativesTempEntity.setAddressLine2(mphRepresentativeDto.getAddressLine2());
			mphRepresentativesTempEntity.setAddressLine3(mphRepresentativeDto.getAddressLine3());
			mphRepresentativesTempEntity.setCityLocality(mphRepresentativeDto.getCityLocality());
			mphRepresentativesTempEntity.setDistrict(mphRepresentativeDto.getDistrict());
			mphRepresentativesTempEntity.setStateName(mphRepresentativeDto.getStateName());
			mphRepresentativesTempEntity.setPincode(mphRepresentativeDto.getPincode());
			mphRepresentativesTempEntity.setIsActive(mphRepresentativeDto.getIsActive());
			mphRepresentativesTempEntity.setCreatedOn(mphRepresentativeDto.getCreatedOn());
			mphRepresentativesTempEntity.setCreatedBy(mphRepresentativeDto.getCreatedBy());
			mphRepresentativesTempEntity.setModifiedOn(mphRepresentativeDto.getModifiedOn());
			mphRepresentativesTempEntity.setModifiedBy(mphRepresentativeDto.getModifiedBy());
			mphRepresentative.add(mphRepresentativesTempEntity);
		}
		response.setMphRepresentative(mphRepresentative);

		Set<PolicyMasterEntity> policyMaster = new HashSet<>();
		for (PolicyMasterTempEntity policyDto : masterDto.getPolicyMaster()) {
			PolicyMasterEntity policyMasterTempEntity = convertPolicyMasterTempEntityToPolicyMasterEntityForService(
					policyDto, status, response.getMphId(), policyNumber, policyLevelConversionEntity);
			policyMaster.add(policyMasterTempEntity);
		}
		response.setPolicyMaster(policyMaster);
		return response;
	}

	public PolicyMasterEntity convertPolicyMasterTempEntityToPolicyMasterEntityForService(
			PolicyMasterTempEntity policyMasterDto, String status, Long mphId, String policyNumber,
			PolicyLevelConversionEntity policyLevelConversionEntity) {
		PolicyMasterEntity response = new PolicyMasterEntity();
		response.setPolicyId(null);
		response.setMphId(mphId);

//		response.setPolicyNumber(tempEntity.getPolicyNumber());
		if (Objects.equals(status, PolicyConstants.APPROVED)) {
			response.setPolicyStatus(PolicyConstants.APPROVED_NO);
			response.setPolicyNumber(getPolicySeq());
		} else if (Objects.equals(status, PolicyConstants.REJECT)) {
			response.setPolicyStatus(PolicyConstants.REJECTED_NO);
			response.setPolicyNumber(null);
		}
//		response.setPolicyNumber(policyMasterDto.getPolicyNumber());
//		response.setPolicyStatus(policyMasterDto.getPolicyStatus());
		response.setPolicyType(policyMasterDto.getPolicyType());
		response.setNoOfCategory(policyMasterDto.getNoOfCategory());
		response.setContributionFrequency(policyMasterDto.getContributionFrequency());
		response.setIntermediaryOfficerCode(policyMasterDto.getIntermediaryOfficerCode());
		response.setIntermediaryOfficerName(policyMasterDto.getIntermediaryOfficerName());
		response.setMarketingOfficerCode(policyMasterDto.getMarketingOfficerCode());
		response.setMarketingOfficerName(policyMasterDto.getMarketingOfficerName());
		response.setProposalId(policyMasterDto.getProposalId());
		response.setQuotationId(policyMasterDto.getQuotationId());
		response.setLeadId(policyMasterDto.getLeadId());
		response.setLineOfBusiness(policyMasterDto.getLineOfBusiness());
		response.setVariant(NumericUtils.convertLongToString(policyLevelConversionEntity.getNewPolicyVariant()));
		response.setProductId(policyLevelConversionEntity.getNewPolicyProduct());
		response.setTotalMember(policyMasterDto.getTotalMember());
		response.setUnitId(policyMasterDto.getUnitId());
		response.setUnitOffice(policyMasterDto.getUnitOffice());
		response.setAdjustmentDt(policyMasterDto.getAdjustmentDt());
		response.setRejectionReasonCode(policyMasterDto.getRejectionReasonCode());
		response.setRejectionRemarks(policyMasterDto.getRejectionRemarks());
		response.setWorkflowStatus(policyMasterDto.getWorkflowStatus());
		response.setArd(policyMasterDto.getArd());
		response.setIsCommencementdateOneYr(policyMasterDto.getIsCommencementdateOneYr());
		response.setPolicyCommencementDt(policyMasterDto.getPolicyCommencementDt());
		response.setPolicyDispatchDate(policyMasterDto.getPolicyDispatchDate());
		response.setPolicyRecievedDate(policyMasterDto.getPolicyRecievedDate());
		response.setConType(policyMasterDto.getConType());
		response.setAdvanceotarrears(policyMasterDto.getAdvanceotarrears());
		response.setIsActive(policyMasterDto.getIsActive());
		response.setCreatedBy(policyMasterDto.getCreatedBy());
		response.setCreatedOn(policyMasterDto.getCreatedOn());
		response.setModifiedBy(policyLevelConversionEntity.getModifiedBy());
		response.setModifiedOn(policyLevelConversionEntity.getModifiedOn());
		response.setAmountToBeAdjusted(policyMasterDto.getAmountToBeAdjusted());
		response.setFirstPremium(policyMasterDto.getFirstPremium());
		response.setSinglePremiumFirstYr(policyMasterDto.getSinglePremiumFirstYr());
		response.setRenewalPremium(policyMasterDto.getRenewalPremium());
		response.setSubsequentSinglePremium(policyMasterDto.getSubsequentSinglePremium());
		Set<PolicyValuationEntity> valuations = new HashSet<>();
		for (PolicyValuationTempEntity policyValuationDto : policyMasterDto.getValuations()) {
			PolicyValuationEntity policyValuationTempEntity = new PolicyValuationEntity();
			policyValuationTempEntity.setValuationId(null);
			policyValuationTempEntity.setPolicyId(response.getPolicyId());
			policyValuationTempEntity.setValuationType(policyValuationDto.getValuationType());
			policyValuationTempEntity.setAttritionRate(policyValuationDto.getAttritionRate());
			policyValuationTempEntity.setSalaryEscalation(policyValuationDto.getSalaryEscalation());
			policyValuationTempEntity.setDeathRate(policyValuationDto.getDeathRate());
			policyValuationTempEntity.setDisRateIntrest(policyValuationDto.getDisRateIntrest());
			policyValuationTempEntity.setDisRateSalaryEsc(policyValuationDto.getDisRateSalaryEsc());
			policyValuationTempEntity.setAnnuityOption(policyValuationDto.getAnnuityOption());
			policyValuationTempEntity.setAccuralRateFactor(policyValuationDto.getAccuralRateFactor());
			policyValuationTempEntity.setMinPension(policyValuationDto.getMinPension());
			policyValuationTempEntity.setMaxPension(policyValuationDto.getMaxPension());
			policyValuationTempEntity.setWithdrawalRate(policyValuationDto.getWithdrawalRate());
			policyValuationTempEntity.setIsActive(policyValuationDto.getIsActive());
			policyValuationTempEntity.setCreatedOn(policyValuationDto.getCreatedOn());
			policyValuationTempEntity.setCreatedBy(policyValuationDto.getCreatedBy());
			policyValuationTempEntity.setModifiedOn(policyValuationDto.getModifiedOn());
			policyValuationTempEntity.setModifiedBy(policyValuationDto.getModifiedBy());
			valuations.add(policyValuationTempEntity);
		}
		response.setValuations(valuations);
		Set<PolicyRulesEntity> rules = new HashSet<>();
		for (PolicyRulesTempEntity policyRulesDto : policyMasterDto.getRules()) {
			PolicyRulesEntity policyAnnuityTempEntity = new PolicyRulesEntity();
			policyAnnuityTempEntity.setRuleId(null);
			policyAnnuityTempEntity.setPolicyId(response.getPolicyId());
			policyAnnuityTempEntity.setEffectiveFrom(policyRulesDto.getEffectiveFrom());
			policyAnnuityTempEntity.setPercentageCorpus(policyRulesDto.getPercentageCorpus());
			policyAnnuityTempEntity.setCategory(policyRulesDto.getCategory());
			policyAnnuityTempEntity.setContributionType(policyRulesDto.getContributionType());
			policyAnnuityTempEntity.setBenifitPayableTo(policyRulesDto.getBenifitPayableTo());
			policyAnnuityTempEntity.setAnnuityOption(policyRulesDto.getAnnuityOption());
			policyAnnuityTempEntity.setCommutationBy(policyRulesDto.getCommutationBy());
			policyAnnuityTempEntity.setCommutationAmt(policyRulesDto.getCommutationAmt());
			policyAnnuityTempEntity.setNormalAgeRetirement(policyRulesDto.getNormalAgeRetirement());
			policyAnnuityTempEntity.setMinAgeRetirement(policyRulesDto.getMinAgeRetirement());
			policyAnnuityTempEntity.setMaxAgeRetirement(policyRulesDto.getMaxAgeRetirement());
			policyAnnuityTempEntity.setMinServicePension(policyRulesDto.getMinServicePension());
			policyAnnuityTempEntity.setMaxServicePension(policyRulesDto.getMaxServicePension());
			policyAnnuityTempEntity.setMinServiceWithdrawal(policyRulesDto.getMinServiceWithdrawal());
			policyAnnuityTempEntity.setMaxServiceWithdrawal(policyRulesDto.getMaxServiceWithdrawal());
			policyAnnuityTempEntity.setMinPension(policyRulesDto.getMinPension());
			policyAnnuityTempEntity.setMaxPension(policyRulesDto.getMaxPension());
			policyAnnuityTempEntity.setModifiedBy(policyRulesDto.getModifiedBy());
			policyAnnuityTempEntity.setModifiedOn(policyRulesDto.getModifiedOn());
			policyAnnuityTempEntity.setCreatedBy(policyRulesDto.getCreatedBy());
			policyAnnuityTempEntity.setCreatedOn(policyRulesDto.getCreatedOn());
			policyAnnuityTempEntity.setIsActive(policyRulesDto.getIsActive());
			rules.add(policyAnnuityTempEntity);
		}
		response.setRules(rules);
		Set<PolicyDepositEntity> deposits = new HashSet<>();
		for (PolicyDepositTempEntity policyDepositDto : policyMasterDto.getDeposits()) {
			PolicyDepositEntity PolicyDepositTempEntity = new PolicyDepositEntity();
			PolicyDepositTempEntity.setDepositId(null);
			PolicyDepositTempEntity.setPolicyId(response.getPolicyId());
			PolicyDepositTempEntity.setCollectionNo(policyDepositDto.getCollectionNo());
			PolicyDepositTempEntity.setCollectionDate(policyDepositDto.getCollectionDate());
			PolicyDepositTempEntity.setCollectionStatus(policyDepositDto.getCollectionStatus());
			PolicyDepositTempEntity.setChallanNo(policyDepositDto.getChallanNo());
			PolicyDepositTempEntity.setDepositAmount(policyDepositDto.getDepositAmount());
			PolicyDepositTempEntity.setAdjustmentAmount(policyDepositDto.getAdjustmentAmount());
			PolicyDepositTempEntity.setAvailableAmount(policyDepositDto.getAvailableAmount());
			PolicyDepositTempEntity.setTransactionMode(policyDepositDto.getTransactionMode());
			PolicyDepositTempEntity.setChequeRealisationDate(policyDepositDto.getChequeRealisationDate());
			PolicyDepositTempEntity.setRemark(policyDepositDto.getRemark());
			PolicyDepositTempEntity.setStatus(policyDepositDto.getStatus());
			PolicyDepositTempEntity.setZeroId(policyDepositDto.getZeroId());
			PolicyDepositTempEntity.setModifiedBy(policyDepositDto.getModifiedBy());
			PolicyDepositTempEntity.setModifiedOn(policyDepositDto.getModifiedOn());
			PolicyDepositTempEntity.setCreatedBy(policyDepositDto.getCreatedBy());
			PolicyDepositTempEntity.setCreatedOn(policyDepositDto.getCreatedOn());
			PolicyDepositTempEntity.setIsActive(policyDepositDto.getIsActive());
			deposits.add(PolicyDepositTempEntity);
		}
		response.setDeposits(deposits);

		Set<PolicyContributionEntity> policyContributions = new HashSet<>();
		for (PolicyContributionTempEntity policyContributionDto : policyMasterDto.getPolicyContributions()) {
			PolicyContributionEntity policyContributionTempEntity = new PolicyContributionEntity();
			policyContributionTempEntity.setContributionId(null);
			policyContributionTempEntity.setPolicyId(response.getPolicyId());
			policyContributionTempEntity.setContReferenceNo(policyContributionDto.getContReferenceNo());
			policyContributionTempEntity.setContributionType(policyContributionDto.getContributionType());
			policyContributionTempEntity.setContributionDate(policyContributionDto.getContributionDate());
			policyContributionTempEntity.setOpeningBalance(policyContributionDto.getOpeningBalance());
			policyContributionTempEntity.setClosingBalance(policyContributionDto.getClosingBalance());
			policyContributionTempEntity.setEmployerContribution(policyContributionDto.getEmployerContribution());
			policyContributionTempEntity.setEmployeeContribution(policyContributionDto.getEmployeeContribution());
			policyContributionTempEntity.setVoluntaryContribution(policyContributionDto.getVoluntaryContribution());
			policyContributionTempEntity.setTotalContribution(policyContributionDto.getTotalContribution());
			policyContributionTempEntity.setIsDeposit(policyContributionDto.getIsDeposit());
			policyContributionTempEntity.setFinancialYear(policyContributionDto.getFinancialYear());
			policyContributionTempEntity.setVersionNo(policyContributionDto.getVersionNo());
			policyContributionTempEntity.setCreatedOn(policyContributionDto.getCreatedOn());
			policyContributionTempEntity.setCreatedBy(policyContributionDto.getCreatedBy());
			policyContributionTempEntity.setModifiedOn(policyContributionDto.getModifiedOn());
			policyContributionTempEntity.setModifiedBy(policyContributionDto.getModifiedBy());
			policyContributionTempEntity.setIsActive(policyContributionDto.getIsActive());
			policyContributions.add(policyContributionTempEntity);
		}
		response.setPolicyContributions(policyContributions);

		Set<PolicyContributionSummaryEntity> policyContributionSummary = new HashSet<>();
		for (PolicyContributionSummaryTempEntity policyContributionSummaryDto : policyMasterDto
				.getPolicyContributionSummary()) {
			PolicyContributionSummaryEntity policyContributionSummaryTempEntity = new PolicyContributionSummaryEntity();
			policyContributionSummaryTempEntity.setPolContSummaryId(null);
			policyContributionSummaryTempEntity.setPolicyId(response.getPolicyId());
			policyContributionSummaryTempEntity.setStampDuty(policyContributionSummaryDto.getStampDuty());
			policyContributionSummaryTempEntity.setOpeningBalance(policyContributionSummaryDto.getOpeningBalance());
			policyContributionSummaryTempEntity
					.setTotalEmployerContribution(policyContributionSummaryDto.getTotalEmployerContribution());
			policyContributionSummaryTempEntity
					.setTotalEmployeeContribution(policyContributionSummaryDto.getTotalEmployeeContribution());
			policyContributionSummaryTempEntity
					.setTotalVoluntaryContribution(policyContributionSummaryDto.getTotalVoluntaryContribution());
			policyContributionSummaryTempEntity
					.setTotalContribution(policyContributionSummaryDto.getTotalContribution());
			policyContributionSummaryTempEntity.setClosingBalance(policyContributionSummaryDto.getClosingBalance());
			policyContributionSummaryTempEntity.setFinancialYear(policyContributionSummaryDto.getFinancialYear());
			policyContributionSummaryTempEntity.setIsActive(policyContributionSummaryDto.getIsActive());
			policyContributionSummaryTempEntity.setCreatedOn(policyContributionSummaryDto.getCreatedOn());
			policyContributionSummaryTempEntity.setCreatedBy(policyContributionSummaryDto.getCreatedBy());
			policyContributionSummaryTempEntity.setModifiedOn(policyContributionSummaryDto.getModifiedOn());
			policyContributionSummaryTempEntity.setModifiedBy(policyContributionSummaryDto.getModifiedBy());
			policyContributionSummary.add(policyContributionSummaryTempEntity);
		}
		response.setPolicyContributionSummary(policyContributionSummary);
		Set<ZeroAccountEntity> zeroAccount = new HashSet<>();
		for (ZeroAccountTempEntity zeroAccountdto : policyMasterDto.getZeroAccount()) {
			ZeroAccountEntity zeroAccountTempEntity = new ZeroAccountEntity();
			zeroAccountTempEntity.setZeroAccId(null);
			zeroAccountTempEntity.setPolicyId(response.getPolicyId());
			zeroAccountTempEntity.setZeroIdAmount(zeroAccountdto.getZeroIdAmount());
			zeroAccountTempEntity.setIsActive(zeroAccountdto.getIsActive());
			zeroAccountTempEntity.setCreatedOn(zeroAccountdto.getCreatedOn());
			zeroAccountTempEntity.setCreatedBy(zeroAccountdto.getCreatedBy());
			zeroAccountTempEntity.setModifiedOn(zeroAccountdto.getModifiedOn());
			zeroAccountTempEntity.setModifiedBy(zeroAccountdto.getModifiedBy());
			zeroAccount.add(zeroAccountTempEntity);
		}
		response.setZeroAccount(zeroAccount);
		Set<ZeroAccountEntriesEntity> zeroAccountEntries = new HashSet<>();
		for (ZeroAccountEntriesTempEntity zeroAccountEntriesDto : policyMasterDto.getZeroAccountEntries()) {
			ZeroAccountEntriesEntity zeroAccountEntriesTempEntity = new ZeroAccountEntriesEntity();
			zeroAccountEntriesTempEntity.setZeroAccEntId(null);
			zeroAccountEntriesTempEntity.setPolicyId(response.getPolicyId());
			zeroAccountEntriesTempEntity.setPolicyConId(zeroAccountEntriesDto.getPolicyConId());
			zeroAccountEntriesTempEntity.setMemberConId(zeroAccountEntriesDto.getMemberConId());
			zeroAccountEntriesTempEntity.setTransactionType(zeroAccountEntriesDto.getTransactionType());
			zeroAccountEntriesTempEntity.setTransactionDate(zeroAccountEntriesDto.getTransactionDate());
			zeroAccountEntriesTempEntity.setZeroIdAmount(zeroAccountEntriesDto.getZeroIdAmount());
			zeroAccountEntriesTempEntity.setCreatedOn(zeroAccountEntriesDto.getCreatedOn());
			zeroAccountEntriesTempEntity.setCreatedBy(zeroAccountEntriesDto.getCreatedBy());
			zeroAccountEntriesTempEntity.setModifiedOn(zeroAccountEntriesDto.getModifiedOn());
			zeroAccountEntriesTempEntity.setModifiedBy(zeroAccountEntriesDto.getModifiedBy());
			zeroAccountEntriesTempEntity.setIsActive(zeroAccountEntriesDto.getIsActive());
			zeroAccountEntries.add(zeroAccountEntriesTempEntity);
		}
		response.setZeroAccountEntries(zeroAccountEntries);
		Set<PolicyNotesEntity> notes = new HashSet<>();
		for (PolicyNotesTempEntity policyNotesDto : policyMasterDto.getNotes()) {
			PolicyNotesEntity policyNotesTempEntity = new PolicyNotesEntity();
			policyNotesTempEntity.setPolicyNoteId(null);
			policyNotesTempEntity.setPolicyId(response.getPolicyId());
			policyNotesTempEntity.setNoteContents(policyNotesDto.getNoteContents());
			policyNotesTempEntity.setModifiedBy(policyNotesDto.getModifiedBy());
			policyNotesTempEntity.setModifiedOn(policyNotesDto.getModifiedOn());
			policyNotesTempEntity.setCreatedBy(policyNotesDto.getCreatedBy());
			policyNotesTempEntity.setCreatedOn(policyNotesDto.getCreatedOn());
			policyNotesTempEntity.setIsActive(policyNotesDto.getIsActive());
			notes.add(policyNotesTempEntity);
		}
		response.setNotes(notes);
		Set<MemberMasterEntity> memberMasterTempEntityList = new HashSet<>();
		for (MemberMasterTempEntity policyValuationDto : policyMasterDto.getMemberMaster()) {
			Long policyContributionId = null;
			MemberMasterEntity memberMasterTempEntity = convertMemberTempEntityToMemberMasterEntityForService(
					policyValuationDto, response.getPolicyId(), policyContributionId);
			memberMasterTempEntityList.add(memberMasterTempEntity);
		}
		response.setMemberMaster(memberMasterTempEntityList);
		return response;
	}

	public MemberMasterEntity convertMemberTempEntityToMemberMasterEntityForService(MemberMasterTempEntity request,
			Long policyId, Long policyContributionId) {
		MemberMasterEntity response = new MemberMasterEntity();
		response.setMemberId(null);
		response.setPolicyId(policyId);
		response.setMembershipNumber(request.getMembershipNumber());
		response.setMemberStatus(request.getMemberStatus());
		response.setLicId(request.getLicId());
		response.setTypeOfMemebership(request.getTypeOfMemebership());
		response.setAadharNumber(request.getAadharNumber());
		response.setCategoryNo(request.getCategoryNo());
		response.setCommunicationPreference(request.getCommunicationPreference());
		response.setLanguagePreference(request.getLanguagePreference());
		response.setDateOfBirth(request.getDateOfBirth());
		response.setDateOfJoining(request.getDateOfJoining());
		response.setDateOfJoiningScheme(request.getDateOfJoiningScheme());
		response.setDateOfRetrirement(request.getDateOfRetrirement());
		response.setDesignation(request.getDesignation());
		response.setEmailid(request.getEmailid());
		response.setGender(request.getGender());
		response.setFatherName(request.getFatherName());
		response.setFirstName(request.getFirstName());
		response.setMiddleName(request.getMiddleName());
		response.setLastName(request.getLastName());
		response.setSpouseName(request.getSpouseName());
		response.setMaritalStatus(request.getMaritalStatus());
		response.setMemberPan(request.getMemberPan());
		response.setLandlineNo(request.getLandlineNo());
		response.setMobileNo(request.getMobileNo());
		response.setIsZeroid(request.getIsZeroid());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		Set<MemberAddressEntity> memberAddressList = new HashSet<>();
		for (MemberAddressTempEntity dto : request.getMemberAddress()) {
			MemberAddressEntity entity = new MemberAddressEntity();
			entity.setAddressId(null);
			entity.setMemberId(response.getMemberId());
//			entity.setPolicyId(dto.getPolicyId());
			entity.setAddress1(dto.getAddress1());
			entity.setAddress2(dto.getAddress2());
			entity.setAddress3(dto.getAddress3());
			entity.setAddressType(dto.getAddressType());
			entity.setCity(dto.getCity());
			entity.setDistrict(dto.getDistrict());
			entity.setStateName(dto.getStateName());
			entity.setCountry(dto.getCountry());
			entity.setPincode(dto.getPincode());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberAddressList.add(entity);
		}
		response.setMemberAddress(memberAddressList);
		Set<MemberBankEntity> memberBankList = new HashSet<>();
		for (MemberBankTempEntity dto : request.getMemberBank()) {
			MemberBankEntity entity = new MemberBankEntity();
			entity.setMemberBankId(null);
			entity.setMemberId(response.getMemberId());
//			entity.setPolicyId(dto.getPolicyId());
			entity.setAccountNumber(dto.getAccountNumber());
			entity.setConfirmAccountNumber(dto.getConfirmAccountNumber());
			entity.setAccountType(dto.getAccountType());
			entity.setIfscCodeAvailable(dto.getIfscCodeAvailable());
			entity.setIfscCode(dto.getIfscCode());
			entity.setBankName(dto.getBankName());
			entity.setBankBranch(dto.getBankBranch());
			entity.setBankAddress(dto.getBankAddress());
			entity.setCountryCode(dto.getCountryCode());
			entity.setStdCode(dto.getStdCode());
			entity.setLandlineNumber(dto.getLandlineNumber());
			entity.setEmailId(dto.getEmailId());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberBankList.add(entity);
		}
		response.setMemberBank(memberBankList);
		Set<MemberNomineeEntity> memberNomineeList = new HashSet<>();
		for (MemberNomineeTempEntity dto : request.getMemberNominee()) {
			MemberNomineeEntity entity = new MemberNomineeEntity();
			entity.setNomineeId(null);
			entity.setMemberId(response.getMemberId());
			entity.setNomineeType(dto.getNomineeType());
			entity.setNomineeName(dto.getNomineeName());
			entity.setRelationShip(dto.getRelationShip());
			entity.setAadharNumber(dto.getAadharNumber());
			entity.setPhone(dto.getPhone());
			entity.setEmailId(dto.getEmailId());
			entity.setAddressOne(dto.getAddressOne());
			entity.setAddressTwo(dto.getAddressTwo());
			entity.setAddressThree(dto.getAddressThree());
			entity.setCountry(dto.getCountry());
			entity.setPinCode(dto.getPinCode());
			entity.setDistrict(dto.getDistrict());
			entity.setState(dto.getState());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setPercentage(dto.getPercentage());
			entity.setAge(dto.getAge());
			entity.setDateOfBirth(dto.getDateOfBirth());
			memberNomineeList.add(entity);
		}
		response.setMemberNominee(memberNomineeList);
		Set<MemberAppointeeEntity> memberAppointeeList = new HashSet<>();
		for (MemberAppointeeTempEntity dto : request.getMemberAppointee()) {
			MemberAppointeeEntity entity = new MemberAppointeeEntity();
			entity.setAppointeeId(null);
			entity.setMemberId(response.getMemberId());
			entity.setAppointeeName(dto.getAppointeeName());
			entity.setAppointeeCode(dto.getAppointeeCode());
			entity.setRelationship(dto.getRelationship());
			entity.setContactNumber(dto.getContactNumber());
			entity.setDateOfBirth(dto.getDateOfBirth());
			entity.setPan(dto.getPan());
			entity.setAadharNumber(dto.getAadharNumber());
			entity.setIfscCode(dto.getIfscCode());
			entity.setBankName(dto.getBankName());
			entity.setAccountType(dto.getAccountType());
			entity.setAccountNumber(dto.getAccountNumber());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberAppointeeList.add(entity);
		}
		response.setMemberAppointee(memberAppointeeList);
		Set<MemberContributionEntity> memberContributionList = new HashSet<>();
		for (MemberContributionTempEntity dto : request.getMemberContribution()) {
			MemberContributionEntity entity = new MemberContributionEntity();
			entity.setMemberConId(null);
			entity.setMemberId(response.getMemberId());
			entity.setPolicyConId(policyContributionId);
			entity.setPolicyId(policyId);
			entity.setContributionType(dto.getContributionType());
			entity.setContributionDate(dto.getContributionDate());
			entity.setLicId(dto.getLicId());
			entity.setOpeningBalance(dto.getOpeningBalance());
			entity.setEmployeeContribution(dto.getEmployeeContribution());
			entity.setEmployerContribution(dto.getEmployerContribution());
			entity.setVoluntaryContribution(dto.getVoluntaryContribution());
			entity.setTotalContribution(dto.getTotalContribution());
			entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());
			entity.setClosingBalance(dto.getClosingBalance());
//			entity.setIsZeroAccount(dto.getIsZeroAccount());
			entity.setIsDeposit(dto.getIsDeposit());
			entity.setFinancialYear(dto.getFinancialYear());
			entity.setVersionNo(dto.getVersionNo());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setIsActive(dto.getIsActive());
			memberContributionList.add(entity);
		}
		response.setMemberContribution(memberContributionList);
		Set<MemberContributionSummaryEntity> MemberContributionSummaryList = new HashSet<>();
		for (MemberContributionSummaryTempEntity dto : request.getMemberContributionSummary()) {
			MemberContributionSummaryEntity entity = new MemberContributionSummaryEntity();
			entity.setMemContSummaryId(null);
			entity.setMemberId(response.getMemberId());
			entity.setPolicyId(policyId);
			entity.setLicId(dto.getLicId());

			entity.setOpeningBalance(dto.getOpeningBalance());
			entity.setTotalEmployeeContribution(dto.getTotalEmployeeContribution());
			entity.setClosingBalance(dto.getClosingBalance());
			entity.setTotalEmployerContribution(dto.getTotalEmployerContribution());
			entity.setTotalVoluntaryContribution(dto.getTotalVoluntaryContribution());
			entity.setTotalContribution(dto.getTotalContribution());
			entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());
			entity.setFinancialYear(dto.getFinancialYear());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setIsActive(dto.getIsActive());
			MemberContributionSummaryList.add(entity);
		}
		response.setMemberContributionSummary(MemberContributionSummaryList);
		return response;
	}

	@Override
	public CommonResponseDto getFinancialYeartDetails(String date) {
		CommonResponseDto response = new CommonResponseDto();
		Object object = quotationRepository.findByObjectNew(date);
		if (object != null) {
			Object[] ob = (Object[]) object;
			response.setData(ob[0].toString());
			response.setFinancialYear(ob[1].toString());
			response.setQuarter(ob[2].toString());
			response.setFrequency(ob[3].toString());
			response.setTransactionStatus(CommonConstants.SUCCESS);
			response.setTransactionMessage(CommonConstants.FETCH_MESSAGE);
			return response;
		} else {
			response.setTransactionStatus(CommonConstants.FAIL);
			return response;
		}

	}

	/***
	 * @notes MPH_MASTER, where mph_id
	 */

	@Override
	public PolicyMasterEntity findPolicyDetails(String policyNumber, Long policyId) throws ApplicationException {
		PolicyMasterEntity policyMasterEntity = null;
		List<Object> list = null;
		logger.info("{},{}", policyNumber, policyId);
		if (policyId != null && policyId != 0l) {
			list = CommonUtils.objectArrayToList(policyMasterRepository.findPolicyDetails(policyId));
			if (CommonUtils.isNonEmptyArray(list)) {
				policyMasterEntity = new PolicyMasterEntity();
				policyMasterEntity.setPolicyId(policyId);
				policyMasterEntity.setPolicyNumber(NumericUtils.getNotNullStringVal(String.valueOf(list.get(0))));
				policyMasterEntity.setProductId(NumericUtils.convertStringToLong(NumericUtils.getNotNullStringVal(String.valueOf(list.get(1)))));
				policyMasterEntity.setVariant(NumericUtils.getNotNullStringVal(String.valueOf(list.get(2))));
				policyMasterEntity.setPolicyType(NumericUtils.getNotNullStringVal(String.valueOf(list.get(3))));
				policyMasterEntity.setMphId(NumericUtils.convertStringToLong(NumericUtils.getNotNullStringVal(String.valueOf(list.get(4)))));
				policyMasterEntity.setPolicyStatus(NumericUtils.getNotNullStringVal(String.valueOf(list.get(5))));
			}
		} else if (StringUtils.isNotBlank(policyNumber)) {
			list = CommonUtils.objectArrayToList(policyMasterRepository.findPolicyDetails(policyNumber));
			if (CommonUtils.isNonEmptyArray(list)) {
				policyMasterEntity = new PolicyMasterEntity();
				policyMasterEntity.setPolicyNumber(policyNumber);
				policyMasterEntity.setPolicyId(NumericUtils.convertStringToLong(NumericUtils.getNotNullStringVal(String.valueOf(list.get(0)))));
				policyMasterEntity.setProductId(NumericUtils.convertStringToLong(NumericUtils.getNotNullStringVal(String.valueOf(list.get(1)))));
				policyMasterEntity.setVariant(NumericUtils.getNotNullStringVal(String.valueOf(list.get(2))));
				policyMasterEntity.setPolicyType(NumericUtils.getNotNullStringVal(String.valueOf(list.get(3))));
				policyMasterEntity.setMphId(NumericUtils.convertStringToLong(NumericUtils.getNotNullStringVal(String.valueOf(list.get(4)))));
				policyMasterEntity.setPolicyStatus(NumericUtils.getNotNullStringVal(String.valueOf(list.get(5))));

			}
		} else {
			throw new ApplicationException(
					"No valid request to fetch the policy master details or policy ID/Number is empty");
		}
		if (policyMasterEntity == null) {
			throw new ApplicationException("No policy master details found for  the given request:(Policy Number,Policy Id)-(" + policyNumber+ "," + policyId + ")");
		}
		return policyMasterEntity;
	}

	@Override
	public MphMasterEntity findMphDetails(Long mphId) throws ApplicationException {
		MphMasterEntity mphMasterEntity = null;
		List<Object> list = null;
		if (mphId != null) {
			list = CommonUtils.objectArrayToList(policyMasterRepository.findMphDetails(mphId));
			if (CommonUtils.isNonEmptyArray(list)) {
				mphMasterEntity = new MphMasterEntity();

				mphMasterEntity.setMphName(NumericUtils.getNotNullStringVal(String.valueOf(list.get(0))));
				mphMasterEntity.setMphCode(NumericUtils.getNotNullStringVal(String.valueOf(list.get(1))));
				mphMasterEntity.setProposalNumber(NumericUtils.getNotNullStringVal(String.valueOf(list.get(2))));
				mphMasterEntity.setMphId(mphId);

			}
		} else {
			throw new ApplicationException("No valid request for MPH details");
		}
		if (mphMasterEntity == null) {
			throw new ApplicationException(
					"No MPH  master details found for  the given request:(mphId)-(" + mphId + ")");
		}
		return mphMasterEntity;
	}

	@Override
	public MemberMasterEntity searchMember(Long policyId, String licId, Long memberId, String financialYear)
			throws ApplicationException {
		String isActive = "isActive";
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MemberMasterEntity> criteriaQuery = builder.createQuery(MemberMasterEntity.class);
		Root<MemberMasterEntity> root = criteriaQuery.from(MemberMasterEntity.class);

		/*
		 * Join<MemberMasterEntity, MemberTransactionSummaryEntity>
		 * memTransactionSummary = root .join("memTransactionSummary");
		 */

		Join<MemberMasterEntity, MemberContributionEntity> memberContribution = root.join("memberContribution");
		/*
		 * Join<MemberContributionEntity, ZeroAccountEntriesEntity> zeroAccountEntries =
		 * memberContribution .join("zeroAccountEntries", JoinType.LEFT);
		 */

		List<Predicate> refPredicates = new ArrayList<>();
		refPredicates.add(builder.equal(root.get(isActive), true));
		refPredicates.add(builder.equal(root.get("policyId"), policyId));
		refPredicates.add(builder.equal(root.get("licId"), licId));
		refPredicates.add(builder.equal(root.get("memberStatus"), "Active"));

		/*
		 * refPredicates.add(builder.equal(memTransactionSummary.get(isActive), true));
		 * refPredicates.add(builder.equal(memTransactionSummary.get("financialYear"),
		 * financialYear));
		 */

		refPredicates.add(builder.equal(memberContribution.get("financialYear"), financialYear));
		refPredicates.add(builder.equal(memberContribution.get(isActive), true));
		/* refPredicates.add(builder.equal(zeroAccountEntries.get(isActive), false)); */

		/*
		 * Predicate isActiveZero = builder.equal(zeroAccountEntries.get("isActive"),
		 * false); Predicate or = builder.or(isActiveZero);
		 */
		/* refPredicates.add(or); */
		/***
		 * criteriaQuery.select(root);
		 */

		Predicate[] all = new Predicate[refPredicates.size()];
		refPredicates.toArray(all);
		criteriaQuery.where(all);

		MemberMasterEntity result = entityManager.createQuery(criteriaQuery).getSingleResult();
		if (result != null) {
			return result;
		} else {
			throw new ApplicationException("No member details found for the given request");
		}
	}

	@Override
	public List<CommonDocumentMasterDto> getDocumentDetails(CommonDocumentMasterDto commonDocumentMasterDto)
			throws ApplicationException {
		logger.info("CommonServiceImpl --getDocumentDetails-- Start");
		List<CommonDocumentMasterDto> adjDto = new ArrayList<>();

		List<CommonDocumentMasterEntity> entity = commonDocumentMasterRepository
				.findByProductIdAndVariantIdAndDocumentCategoryAndDocumentSubCategoryAndIsActiveTrue(
						commonDocumentMasterDto.getProductId(), commonDocumentMasterDto.getVariantId(),
						commonDocumentMasterDto.getDocumentCategory(),
						commonDocumentMasterDto.getDocumentSubCategory());
		if (entity.isEmpty()) {
			throw new ApplicationException("No Data Found");

		} else {
			adjDto = mapList(entity, CommonDocumentMasterDto.class);

		}
		logger.info("CommonServiceImpl --getDocumentDetails-- end");
		return adjDto;

	}

	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		logger.info("AdjustmentContributionServiceImpl:mapList:Start");
		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
	}

	@Autowired
	private MphMasterRepository mphMasterRepository;

	@Override
	public PolicyResponseDataAnnuityDto policyresponseData(String policyNumber) {
		logger.info("CommonServiceImpl -- policyresponseData -- Start");
		PolicyResponseDataAnnuityDto response = new PolicyResponseDataAnnuityDto();
		PolicyResponseAnnuityDto setPolicyResponse = new PolicyResponseAnnuityDto();
		try {
			if (policyNumber != null) {
				Long mphId = mphMasterRepository.findpolicyresponseDataBypolicyIdAnnuity(policyNumber);
				if (mphId == null) {
					response.setTransactionMessage("No policy master details found for the given request:(Policy Number-(" + policyNumber+ ")");
					response.setTransactionStatus(CommonConstants.FAIL);
				}
				MphMasterEntity mphEntity = mphMasterRepository.findByMphIdAndIsActiveTrue(mphId);
				setPolicyResponse = convertMphToPolicyResponseAnnuityDto(mphEntity);
				response.setPolicyresponseData(setPolicyResponse);
				response.setTransactionMessage(CommonConstants.FETCH);
				response.setTransactionStatus(CommonConstants.SUCCESS);
			} else {
				response.setTransactionMessage("No valid request to fetch the policy master details or policy ID/Number is empty");
				response.setTransactionStatus(CommonConstants.FAIL);
			}
			if (response.getPolicyresponseData() == null) {
				response.setTransactionMessage("No policy master details found for  the given request:(Policy Number-(" + policyNumber + ")");
				response.setTransactionStatus(CommonConstants.FAIL);
			}
		} catch (Exception e) {
			logger.error("Exception-- CommonServiceImpl-- policyresponseData --", e);
		}
		logger.info("CommonServiceImpl -- policyresponseData -- End");
		return response;
	}

	private PolicyResponseAnnuityDto convertMphToPolicyResponseAnnuityDto(MphMasterEntity mphEntity) {
		PolicyResponseAnnuityDto setPolicyResponse = new PolicyResponseAnnuityDto();

		PolMphBasicDetailsAnnuityDto setPolMphBasicDetailsAnnuity = new PolMphBasicDetailsAnnuityDto();
		PolMphBankAccountDetailsAnnuityDto setPolMphBankAccountDetailsAnnuity = new PolMphBankAccountDetailsAnnuityDto();
		List<PolMphAddressDetailsAnnuityDto> setPolMphAddressDetailsAnnuity = new ArrayList<>();
		List<PolMphContactDetailsAnnuityDto> setPolMphContactDetailsAnnuity = new ArrayList<>();

		setPolMphBasicDetailsAnnuity.setApan(mphEntity.getAlternatePan());
		setPolMphBasicDetailsAnnuity.setCin(mphEntity.getCin());
		setPolMphBasicDetailsAnnuity.setCreatedBy(mphEntity.getCreatedBy());
		setPolMphBasicDetailsAnnuity.setCreatedDate(mphEntity.getCreatedOn());
		setPolMphBasicDetailsAnnuity.setModifiedBy(mphEntity.getModifiedBy());
		setPolMphBasicDetailsAnnuity.setMphCode(mphEntity.getMphCode());
		setPolMphBasicDetailsAnnuity.setMphName(mphEntity.getMphName());
		setPolMphBasicDetailsAnnuity.setPan(mphEntity.getPan());
		for (PolicyMasterEntity policyMasterEntity : mphEntity.getPolicyMaster()) {
			setPolMphBasicDetailsAnnuity.setIntermediaryCode(policyMasterEntity.getIntermediaryOfficerCode());
			setPolMphBasicDetailsAnnuity.setIntermediaryName(policyMasterEntity.getIntermediaryOfficerName());
			setPolMphBasicDetailsAnnuity.setLineofBusiness(policyMasterEntity.getLineOfBusiness());
			setPolMphBasicDetailsAnnuity.setMarketingOfficercode(policyMasterEntity.getMarketingOfficerCode());
			setPolMphBasicDetailsAnnuity.setMarketingOfficername(policyMasterEntity.getMarketingOfficerName());
			setPolMphBasicDetailsAnnuity.setPolicyEndDate(policyMasterEntity.getArd());
			setPolMphBasicDetailsAnnuity.setPolicyIssuanceDate(policyMasterEntity.getPolicyCommencementDt());
			setPolMphBasicDetailsAnnuity.setAdjustedDate(policyMasterEntity.getAdjustmentDt());
			setPolMphBasicDetailsAnnuity.setPolicyNumber(policyMasterEntity.getPolicyNumber());
			setPolMphBasicDetailsAnnuity.setPolicyStatus(policyMasterEntity.getPolicyStatus());
			setPolMphBasicDetailsAnnuity.setProduct(null);
			setPolMphBasicDetailsAnnuity.setProductId(policyMasterEntity.getProductId());
			setPolMphBasicDetailsAnnuity.setVariant(policyMasterEntity.getVariant());
			setPolMphBasicDetailsAnnuity.setUnitCode(policyMasterEntity.getUnitId());
			setPolMphBasicDetailsAnnuity.setUnitId(null);
			setPolMphBasicDetailsAnnuity.setUnitName(policyMasterEntity.getUnitOffice());
		}
		setPolMphBasicDetailsAnnuity.setCustomerCode(null);
		setPolMphBasicDetailsAnnuity.setCustomerGroupName(null);
		setPolMphBasicDetailsAnnuity.setCustomerId(null);
		setPolMphBasicDetailsAnnuity.setCustomerName(null);
		setPolMphBasicDetailsAnnuity.setCustomerType(null);
		setPolMphBasicDetailsAnnuity.setGstBorneBy(null);
		setPolMphBasicDetailsAnnuity.setGstin(null);
		setPolMphBasicDetailsAnnuity.setGstinApplicable(null);
		setPolMphBasicDetailsAnnuity.setGstinRate(null);
		setPolMphBasicDetailsAnnuity.setIndustryType(null);
		setPolMphBasicDetailsAnnuity.setRateOfApplicable(null);
		setPolMphBasicDetailsAnnuity.setRegisteredWith(null);
		setPolMphBasicDetailsAnnuity.setNumberOfLives(null);
		setPolMphBasicDetailsAnnuity.setTotalQuotationAmount(null);
		setPolMphBasicDetailsAnnuity.setTypeofRegistration(null);
		setPolMphBasicDetailsAnnuity.setUrbanOrRural(null);

		if (mphEntity.getMphBank() != null && !mphEntity.getMphBank().isEmpty()) {
			for (MphBankEntity mphBankEntity : mphEntity.getMphBank()) {
				setPolMphBankAccountDetailsAnnuity.setAccountNumber(mphBankEntity.getAccountNumber());
				setPolMphBankAccountDetailsAnnuity.setAccountType(mphBankEntity.getAccountType());
				setPolMphBankAccountDetailsAnnuity.setBankBranch(mphBankEntity.getBankBranch());
				setPolMphBankAccountDetailsAnnuity.setBankName(mphBankEntity.getBankName());
				setPolMphBankAccountDetailsAnnuity.setBankStatus(null);
				setPolMphBankAccountDetailsAnnuity.setCountryCode(NumericUtils.convertIntegerToString(mphBankEntity.getCountryCode()));
				setPolMphBankAccountDetailsAnnuity.setCreatedBy(mphBankEntity.getCreatedBy());
				setPolMphBankAccountDetailsAnnuity.setEmailId(mphBankEntity.getEmailId());
				setPolMphBankAccountDetailsAnnuity.setIfscCode(mphBankEntity.getIfscCode());
				setPolMphBankAccountDetailsAnnuity.setLandLineNumber(NumericUtils.convertIntegerToString(NumericUtils.convertLongToInteger(mphBankEntity.getLandlineNumber())));
				setPolMphBankAccountDetailsAnnuity.setStdCode(NumericUtils.convertIntegerToString(mphBankEntity.getStdCode()));
			}
		}

		if (mphEntity.getMphAddress() != null && !mphEntity.getMphAddress().isEmpty()) {
			for (MphAddressEntity mphAddressEntity : mphEntity.getMphAddress()) {
				PolMphAddressDetailsAnnuityDto dto = new PolMphAddressDetailsAnnuityDto();
				dto.setAddressLine1(mphAddressEntity.getAddressLine1());
				dto.setAddressLine2(mphAddressEntity.getAddressLine2());
				dto.setAddressLine3(mphAddressEntity.getAddressLine3());
				dto.setAddressStatus(null);
				dto.setAddressType(mphAddressEntity.getAddressType());
				dto.setCity(mphAddressEntity.getCityId());
				dto.setCountry(NumericUtils.convertIntegerToString(mphAddressEntity.getCountryId()));
				dto.setDistrict(NumericUtils.convertIntegerToString(mphAddressEntity.getDistrictId()));
				dto.setCreatedBy(mphAddressEntity.getCreatedBy());
				dto.setPinCode(mphAddressEntity.getPincode());
				dto.setState(NumericUtils.convertIntegerToString(mphAddressEntity.getStateId()));
				setPolMphAddressDetailsAnnuity.add(dto);
			}
		}

		if (mphEntity.getMphRepresentative() != null && !mphEntity.getMphRepresentative().isEmpty()) {
			for (MphRepresentativesEntity mphRepresentativesEntity : mphEntity.getMphRepresentative()) {
				PolMphContactDetailsAnnuityDto dto = new PolMphContactDetailsAnnuityDto();
				dto.setCountryCode(mphRepresentativesEntity.getCountryCode());
				dto.setCreatedBy(mphRepresentativesEntity.getCreatedBy());
				dto.setEmailId(mphRepresentativesEntity.getEmailId());
				dto.setLandlineNumber(NumericUtils.convertIntegerToString(NumericUtils.convertLongToInteger(mphRepresentativesEntity.getLandlineNo())));
				dto.setMobileNumber(NumericUtils.convertIntegerToString(NumericUtils.convertLongToInteger(mphRepresentativesEntity.getMobileNo())));
				dto.setStdCode(NumericUtils.convertIntegerToString(mphRepresentativesEntity.getPincode()));
				dto.setFirstName(mphRepresentativesEntity.getRepresentativeName());
				dto.setLastName(null);
				dto.setMiddleName(null);
				dto.setDesignation(null);
				dto.setFaxNumber(null);
				dto.setCommunicationPreferences(null);
				dto.setContactStatus(null);
				dto.setContactType(null);
				setPolMphContactDetailsAnnuity.add(dto);
			}
		}

		setPolicyResponse.setPolMphBasicDetails(setPolMphBasicDetailsAnnuity);
		setPolicyResponse.setMphBankAccountDetails(setPolMphBankAccountDetailsAnnuity);
		setPolicyResponse.setMphAddressDetails(setPolMphAddressDetailsAnnuity);
		setPolicyResponse.setMphContactDetails(setPolMphContactDetailsAnnuity);
		return setPolicyResponse;
	}
	
	@Override
	public MemberMasterEntity searchMemberNew(Long policyId, String licId, Long memberId, String financialYear) throws ApplicationException {
		MemberMasterEntity memberMasterEntity=new MemberMasterEntity();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();

		CriteriaQuery<MemberMasterEntity> searchQuery = criteriaBuilder.createQuery(MemberMasterEntity.class);
		Root<MemberMasterEntity> root = searchQuery.from(MemberMasterEntity.class);

		Join<MemberMasterEntity, MemberContributionEntity> contribut = root.join("memberContribution");
		
		contribut.on(criteriaBuilder.equal(contribut.get("financialYear"), financialYear));
		contribut.on(criteriaBuilder.equal(contribut.get("isActive"), Boolean.TRUE));
		
		predicates.add(criteriaBuilder.equal(root.get("policyId"), policyId));
		predicates.add(criteriaBuilder.equal(root.get("licId"), licId));
		predicates.add(criteriaBuilder.equal(root.get("memberId"), memberId));
		predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
		
		searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));

		memberMasterEntity= entityManager.createQuery(searchQuery).getSingleResult();
		if(memberMasterEntity!=null) {
			
			return memberMasterEntity;
		}
		else {
			throw new ApplicationException("No member details found for the given request");
		}
	}

	@Override
	public List<DepositTransferResponse> searchDepositTransfer(SearchDepositTransferDto dto) {
//		SearchDepositTransferDto dto = new SearchDepositTransferDto();
		List<SearchDepositTransferDto> commonDto = new ArrayList<SearchDepositTransferDto>();
		SearchDepositTransferDto resDto = new SearchDepositTransferDto();
		List<DepositTransferResponse> list = new ArrayList<>();
		try {
			
			HttpHeaders headers = getAuthHeaders();
			HttpEntity<SearchDepositTransferDto> entity = new HttpEntity<>(dto, headers);

			
//			ResponseEntity<List<SearchDepositTransferDto>> response = 
//					restTemplateService.postForEntity(environment.getProperty("DEPOSIT_TRANSFER_SEARCH_URL"), 
//							entity, List<SearchDepositTransferDto>.class);
			
			ResponseEntity<List<DepositTransferResponse>> response = 
					restTemplateService.exchange(environment.getProperty("DEPOSIT_TRANSFER_SEARCH_URL"), 
						HttpMethod.POST	,entity,new ParameterizedTypeReference<List<DepositTransferResponse>>() {
						});
			
			
			if(response != null) {
				list = response.getBody();
			}
			
			
			
		} catch (Exception e) {
			
			commonDto.add(resDto);
		}
		return list;
	}
	
	
	
	public HttpHeaders getAuthHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("infra", "portal");
		return headers;
	}
	
	private String dateFormate(String date) {

		String fromDate = date;
		String[] olddate = fromDate.split("/");
		String dd = olddate[0];
		String mm = olddate[1];
		String year = olddate[2];

		String month = null;
		switch (mm) {
		case "01":
			month = "JAN";
			break;
		case "02":
			month = "FEB";
			break;
		case "03":
			month = "MAR";
			break;
		case "04":
			month = "APR";
			break;
		case "05":
			month = "MAY";
			break;
		case "06":
			month = "JUN";
			break;
		case "07":
			month = "JUL";
			break;
		case "08":
			month = "AUG";
			break;
		case "09":
			month = "SEP";
			break;
		case "10":
			month = "OCT";
			break;
		case "11":
			month = "NOV";
			break;
		case "12":
			month = "DEC";
			break;
		default:
			break;
		}

		String fromaate = dd + "-" + month + "-" + year;

		return fromaate;
	}
	private List<DepositTransferResponse> GiResp(List<DepositTransferResponseModel> resDto) {
		List<DepositTransferResponse> responses = new ArrayList<>();
		for (DepositTransferResponseModel  dto : resDto) {
			DepositTransferResponse response = new DepositTransferResponse();
			response.setAccountType(dto.getAccountType());
			response.setBankBranch(dto.getBankBranch());
			response.setBankName(dto.getBankName());
			response.setCustomerId(String.valueOf(dto.getCustomerId()));
			response.setIfscCode(dto.getIfscCode());
			response.setMphCode(dto.getMphCode());
			response.setMphName(dto.getMphName());
			response.setPolicyNumber(dto.getPolicyNumber());
			if (dto.getPolicyStatus().equalsIgnoreCase("182")) {
				response.setPolicyStatus("Active");
			}
			response.setProduct(dto.getProduct());
			response.setProductCode(dto.getProductCode());
			response.setSchemeName(dto.getSchemeName());
			response.setUnitCode(dto.getUnitCode());
			response.setVariantCode(dto.getVariantCode());
			responses.add(response);

		}

		return responses;
		

	}

	public HttpHeaders getAuthHeaderss() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(CommonConstants.AUTHORIZATION, "Bearer ");
		headers.add(CommonConstants.CORELATIONID, UUID.randomUUID().toString());
		headers.add(CommonConstants.BUSINESSCORELATIONID, UUID.randomUUID().toString());
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	private String dateFormates(String date) {

		String fromDate = date;
		String[] olddate = fromDate.split("/");
		String dd = olddate[0];
		String mm = olddate[1];
		String year = olddate[2];

		String dateFormate = year + "-" + mm + "-" + dd;

		return dateFormate;
	}

	private DepositTransferInputModel  reqGi(SearchDepositTransferDto dto) {

		String fromdate="";
		String toDate="";
		if (StringUtils.isNoneBlank(dto.getPolicyCreationFromDate())
				&& StringUtils.isNoneBlank(dto.getPolicyCreationToDate())) {
			 fromdate = dateFormates(dto.getPolicyCreationFromDate());
			 toDate = dateFormates(dto.getPolicyCreationToDate());

		}

		DepositTransferInputModel  reqDto = new DepositTransferInputModel ();
		dto.getCustomerCode();
		reqDto.setPolicyNumber(dto.getPolicyNumber());
		reqDto.setCustomerCode(0);
		long mphId = dto.getMphId();
		if(mphId!=0) {
		reqDto.setMphCode(String.valueOf(mphId));
		}else {
			reqDto.setMphCode("");
		}
		reqDto.setVanNumber("");
		reqDto.setProposalNumber("");
		reqDto.setQuotationNumber("");
		reqDto.setPanNumber("");
		reqDto.setGstIn("");
		reqDto.setPolicyCreationFromDate(fromdate);
		reqDto.setPolicyCreationToDate(toDate);
		reqDto.setMphName(dto.getMphName()!=null?dto.getMphName():"");
		reqDto.setRollType("");
		return reqDto;

	}

	private List<DepositTransferResponse> GIModuleDepositTransferSearch(SearchDepositTransferDto dto) {
		List<DepositTransferResponse> responses = null;
		try {
			List<DepositTransferResponseModel > resDto =  null;
			DepositTransferInputModel  reqDto = reqGi(dto);
			String url = environment.getProperty("GIPOLICYSEARCH");
//			String url = "https://d1utvrrpgca01.licindia.com:8443/gicservice/LIC_ePGS/gic/mphDetails/getDepositTransfer";
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				if (StringUtils.isNotBlank(url)) {
					HttpHeaders headerss = getAuthHeaderss();
					HttpEntity<DepositTransferInputModel> entity = new HttpEntity<>(reqDto, headers);

					resDto = restTemplateService.exchange(url, HttpMethod.POST, entity,
							new ParameterizedTypeReference<List<DepositTransferResponseModel>>() {
							}).getBody();
					if (CommonUtils.isNonEmptyArray(resDto)) {
					responses = GiResp(resDto);
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responses;

	}

	@Override
	public List<DepositTransferResponse> DepositTransferSearch(SearchDepositTransferDto dto) {
		List<DepositTransferResponse> responce = new ArrayList<>();
		SearchDepositTransferDto depositTransferDto = new SearchDepositTransferDto();
		List<Object[]> result = null;
		try {

			System.out.println(dto.getPolicyCreationFromDate() + " " + dto.getPolicyCreationToDate());

			String formadat=null;
			String todate=null;
			if(StringUtils.isNoneBlank(dto.getPolicyCreationFromDate()) && StringUtils.isNoneBlank(dto.getPolicyCreationToDate())) {
					
			 formadat = dateFormate(dto.getPolicyCreationFromDate());

			 todate = dateFormate(dto.getPolicyCreationToDate());
			}
			result = depositRepository.getSearchDepositTransfer(dto.getPolicyNumber(), dto.getMphId(), dto.getMphCode(),
					dto.getProposalNumber(), dto.getQuotationNumber(), dto.getPanNumber(), formadat, todate,
					dto.getProductCode(), dto.getCustomerCode());
			if (CommonUtils.isNonEmptyArray(result)) {
				for (Object[] objects : result) {
					DepositTransferResponse transferResponse = new DepositTransferResponse();

					if (String.valueOf(objects[4]).equalsIgnoreCase("In-Force")
							|| (String.valueOf(objects[4]).equalsIgnoreCase("PaidUp"))) {
						transferResponse.setPolicyStatus("Active");
					}
					transferResponse.setPolicyNumber(String.valueOf(objects[0]));
					transferResponse.setMphName(String.valueOf(objects[1]));
					transferResponse.setMphCode(String.valueOf(objects[2]));
					transferResponse.setUnitCode(String.valueOf(objects[3]));
					transferResponse.setIfscCode(String.valueOf(objects[5]));
					transferResponse.setAccountType(String.valueOf(objects[6]));
					transferResponse.setBankName(String.valueOf(objects[7]));
					transferResponse.setBankBranch(String.valueOf(objects[8]));
					transferResponse.setSchemeName(String.valueOf(objects[10]));
					transferResponse.setProductCode(String.valueOf(objects[9]));
					transferResponse.setVariantCode(String.valueOf(objects[11]));
					transferResponse.setProduct(String.valueOf(objects[12]));
					transferResponse.setCustomerId(String.valueOf(objects[13]));

					responce.add(transferResponse);
				}
			}else {
			responce=GIModuleDepositTransferSearch(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return responce;
	

	}
	
	@Override
	public PolicyResponseDataAnnuityDto depositAppropriation(String collectionNo) {

		PolicyResponseDataAnnuityDto responseDto = new PolicyResponseDataAnnuityDto();
		List<AmountAdjusted> responce = new ArrayList<>();
		List<AmountAdjusted> responces = new ArrayList<>();
		logger.info("DepositTransferServiceImpl --depositAppropriation-- started");
		try {
			long SerialNo = 0;
		/**	   0             1                 2
		  PD.CHALLAN_NO ,PD.DEPOSIT_AMOUNT,PD.ADJUSTMENT_AMOUNT, 
			         3                   4                5                      6
		  PD.AVAILABLE_AMOUNT ,PD.POLICY_ID ,PD.CHEQUE_REALISATION_DATE,CONTRIBUTION_DAT */
			
			List<Object[]> result = depositRepository.findCollectionDatailes(collectionNo);
			
			AmountAdjusted newamountAdjusteds = new AmountAdjusted();
			for (Object[] objects : result) {
				newamountAdjusteds.setPolicyId(Long.valueOf(String.valueOf(objects[4])));
				responces.add(newamountAdjusteds);
			}
			
			BigDecimal sumTotalUtilized=BigDecimal.ZERO;
			BigDecimal depositAmount=BigDecimal.ZERO;
			BigDecimal availableAmount=BigDecimal.ZERO;
			BigDecimal sumTotalUtilizeds=BigDecimal.ZERO;
			String memberCount = memberContributionRepository.memberCount(collectionNo);
			if (CommonUtils.isNonEmptyArray(result)) {
				for (Object[] objects : result) {
					AmountAdjusted amountAdjusted = new AmountAdjusted();
					amountAdjusted.setSerialNo(++SerialNo);
					amountAdjusted.setDepositDate(String.valueOf(objects[5]!=null?objects[5].toString():"0"));
					amountAdjusted.setContributionDate(String.valueOf(objects[6]!=null?objects[6].toString():"0"));
					depositAmount = new BigDecimal(String.valueOf(objects[1]!=null?objects[1].toString():"0"));
					amountAdjusted.setDepositAmount(depositAmount.subtract(sumTotalUtilizeds));
					amountAdjusted.setPremium(new BigDecimal(String.valueOf(objects[2]!=null?objects[2].toString():"0")));
					sumTotalUtilized=((BigDecimal) objects[2]).add(sumTotalUtilized);
					amountAdjusted.setTotalUtilized(sumTotalUtilized);
					amountAdjusted.setAvailableAmount(new BigDecimal(String.valueOf(objects[3]!=null?objects[3].toString():"0")));
					amountAdjusted.setBatchNoOrTransactionNo(objects[0]!=null?objects[0].toString():"0");
					amountAdjusted.setNoOfLives(memberCount);
					availableAmount=amountAdjusted.getAvailableAmount();
					sumTotalUtilizeds=amountAdjusted.getTotalUtilized();
//					amountAdjusted.setDepositAmount(availableAmount);
					responce.add(amountAdjusted);
//					amountAdjusted.setTotalUtilized(sumTotalUtilized);
//					responce.add(newamountAdjusteds);
					responseDto.setPolicyresponseData(responce);
					responseDto.setTransactionStatus(CommonConstants.SUCCESS);
					responseDto.setTransactionMessage(CommonConstants.FETCH_MESSAGE);
				}
					
			}else {
				responseDto.setTransactionStatus(CommonConstants.FAIL);
				responseDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
			}
			} catch (Exception e) {
			logger.error("Exception-- CommonServiceImpl-- depositAppropriation --", e);
			responseDto.setTransactionStatus(CommonConstants.FAIL);
			responseDto.setTransactionMessage(e.getMessage());
		}
		logger.info("DepositTransferServiceImpl --depositAppropriation-- Ended");
		return responseDto;
		
	
	}
	
	
	

}
