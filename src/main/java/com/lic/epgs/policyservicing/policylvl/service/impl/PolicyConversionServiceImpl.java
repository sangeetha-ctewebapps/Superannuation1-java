package com.lic.epgs.policyservicing.policylvl.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.NonUniqueResultException;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.common.service.impl.CommonServiceImpl;
import com.lic.epgs.policy.dto.MphMasterDto;
import com.lic.epgs.policy.dto.PolicyMasterDto;
import com.lic.epgs.policy.dto.PolicyRulesDto;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.MphMasterTempEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterTempEntity;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.MphMasterTempRepository;
import com.lic.epgs.policy.repository.PolicyDepositRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.policy.repository.PolicyMasterTempRepository;
import com.lic.epgs.policy.repository.PolicyNotesRepository;
import com.lic.epgs.policy.repository.PolicyNotesTempRepository;
import com.lic.epgs.policy.repository.PolicyRulesRepository;
import com.lic.epgs.policy.repository.PolicyRulesTempRepository;
import com.lic.epgs.policy.service.impl.PolicyServiceImpl;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceDocumentDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceDocumentEntity;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceDocumentTempEntity;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesEntity;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesTempEntity;
import com.lic.epgs.policyservicing.common.repository.PolicyServiceDocumentRepository;
import com.lic.epgs.policyservicing.common.repository.PolicyServiceDocumentTempRepository;
import com.lic.epgs.policyservicing.common.repository.PolicyServiceNotesRepository;
import com.lic.epgs.policyservicing.common.repository.PolicyServiceNotesTempRepository;
import com.lic.epgs.policyservicing.policylvl.constants.PolicyLevelConversionConstants;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.CommonResponseDto;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.PolicyConversionDetailsDto;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.PolicyConversionSearchDto;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.PolicyDetailsDto;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.PolicyLevelConversionDto;
import com.lic.epgs.policyservicing.policylvl.entity.conversion.PolicyLevelConversionEntity;
import com.lic.epgs.policyservicing.policylvl.entity.conversion.PolicyLevelConversionTempEntity;
import com.lic.epgs.policyservicing.policylvl.repository.conversion.PolicyLevelConversionRepository;
import com.lic.epgs.policyservicing.policylvl.repository.conversion.PolicyLevelConversionTempRepository;
import com.lic.epgs.policyservicing.policylvl.service.PolicyConversionService;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

/**
 * @author Logesh.D
 *
 */

@Service
public class PolicyConversionServiceImpl implements PolicyConversionService {
	

	private final Logger logger = LogManager.getLogger(PolicyConversionServiceImpl.class);

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	PolicyLevelConversionRepository policyLevelConversionRepository;

	@Autowired
	PolicyLevelConversionTempRepository policyLevelConversionTempRepository;

	@Autowired
	PolicyNotesTempRepository policyNotesTempRepository;

	@Autowired
	PolicyRulesTempRepository policyRulesTempRepository;

	@Autowired
	PolicyRulesRepository policyRulesRepository;

	@Autowired
	PolicyNotesRepository policyNotesRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	PolicyServiceImpl policyServiceImpl;

	@Autowired
	EntityManager entityManager;

	@Autowired
	private EntityManagerFactory entityManagerFactory;


	@Autowired
	PolicyDepositRepository policyDepositRepository;

	
	@Autowired
	PolicyMasterRepository policyMasterRepository;
	
	@Autowired
	PolicyServiceNotesTempRepository policyServiceNotesTempRepository;
	
	@Autowired
	PolicyServiceDocumentTempRepository policyServiceDocumentTempRepository;
	
	@Autowired
	PolicyServiceNotesRepository policyServiceNotesRepository;
	
	@Autowired
	CommonServiceImpl commonServiceImpl;
	
	@Autowired
	private PolicyMasterTempRepository policyMasterTempRepository;
	
	@Autowired
	private MphMasterTempRepository mphMasterTempRepository;
	
	@Autowired
	PolicyServiceDocumentRepository policyServiceDocumentRepository;
	
	@Autowired
	MphMasterRepository mphMasterRepository;



	public synchronized String getPolicySeq() {
		return commonService.getSequence(CommonConstants.POLICY_MODULE);
	}

	public CommonResponseDto<PolicyLevelConversionDto> savePolicyConversionDetails(
			PolicyLevelConversionDto policyLevelConversionDto) {
		logger.info("PolicyLevelConversionServiceImpl------savePolicyConversionDetails-------Started");
		CommonResponseDto<PolicyLevelConversionDto> commonResponseDto = new CommonResponseDto<>();
		PolicyLevelConversionTempEntity policyLevelConversionTempEntity = null;
		try {
			if (policyLevelConversionDto.getConversionId() != null) {
				policyLevelConversionTempEntity = policyLevelConversionTempRepository
						.findByConversionIdAndIsActiveTrue(policyLevelConversionDto.getConversionId());
				policyLevelConversionTempEntity = modelMapper.map(policyLevelConversionDto,
						PolicyLevelConversionTempEntity.class);
				policyLevelConversionTempEntity.setNewpolicyIssueDate(policyLevelConversionDto.getNewpolicyIssueDate());
				policyLevelConversionTempEntity.setNewPolicyProduct(policyLevelConversionDto.getNewPolicyProduct());
				policyLevelConversionTempEntity.setNewPolicyVariant(policyLevelConversionDto.getNewPolicyVariant());
				policyLevelConversionTempEntity.setNoOfCatalogue(policyLevelConversionDto.getNoOfCatalogue());
				policyLevelConversionTempEntity.setPrevFundBalancde(policyLevelConversionDto.getPrevFundBalancde());
				policyLevelConversionTempEntity.setNewpolicyStatus(policyLevelConversionDto.getNewpolicyStatus());
				policyLevelConversionTempEntity
						.setNewPolicyAnnualRenewalDate(policyLevelConversionDto.getNewPolicyAnnualRenewalDate());
//				 policyLevelConversionTempEntity.setCreatedOn(DateUtils.sysDate());
				policyLevelConversionTempEntity.setIsActive(true);
				policyLevelConversionTempEntity = policyLevelConversionTempRepository
						.save(policyLevelConversionTempEntity);
				commonResponseDto.setResponseData(
						modelMapper.map(policyLevelConversionTempEntity, PolicyLevelConversionDto.class));
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SAVE);
			} else {
//				if (NumericUtils.convertLongToString(policyLevelConversionDto.getNewPolicyVariant())
//						.equals(PolicyLevelConversionConstants.POLICY_VARIANT_DB)
//						|| NumericUtils.convertLongToString(policyLevelConversionDto.getNewPolicyVariant())
//								.equals(PolicyLevelConversionConstants.POLICY_VARIANT_DC)) {
//					commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAIL);
//					commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.VARIANT);
//					return commonResponseDto;
//				}
				policyLevelConversionTempEntity = modelMapper.map(policyLevelConversionDto,
						PolicyLevelConversionTempEntity.class);
//			policyLevelConversionTempEntity.setCreatedOn(DateUtils.sysDate());
				policyLevelConversionTempEntity.setIsActive(true);
				policyLevelConversionTempRepository.save(policyLevelConversionTempEntity);
				PolicyLevelConversionDto policyLevelConversion = modelMapper.map(policyLevelConversionTempEntity, PolicyLevelConversionDto.class);
				commonResponseDto.setResponseData(policyLevelConversion);
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SAVE);
			}
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- savePolicyConversionDetails --", ce);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ savePolicyConversionDetails----------- Ended");
		return commonResponseDto;
	}

	public CommonResponseDto<PolicyLevelConversionDto> sendToChecker(String conversionId, String modifiedBy) {
		logger.info("PolicyLevelConversionServiceImpl------sendToChecker-------Started");
		CommonResponseDto<PolicyLevelConversionDto> commonResponseDto = new CommonResponseDto<>();
		try {
			PolicyLevelConversionTempEntity policyLevelConversionTempEntity = policyLevelConversionTempRepository
					.findByConversionIdAndIsActiveTrue(NumericUtils.convertStringToLong(conversionId));

			if (policyLevelConversionTempEntity != null) {
				policyLevelConversionTempEntity.setConversionStatus(
						NumericUtils.convertStringToInteger(PolicyLevelConversionConstants.SEND_FOR_APPROVAL));
				policyLevelConversionTempEntity.setConversionWorkflowStatus(
						NumericUtils.convertStringToInteger(PolicyLevelConversionConstants.SEND_FOR_APPROVAL));
				policyLevelConversionTempEntity.setModifiedBy(modifiedBy);
				policyLevelConversionTempEntity.setModifiedOn(DateUtils.sysDate());
				policyLevelConversionTempRepository.save(policyLevelConversionTempEntity);
				commonResponseDto.setResponseData(
						modelMapper.map(policyLevelConversionTempEntity, PolicyLevelConversionDto.class));
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
			}
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- sendToChecker --", ce);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		} catch (PersistenceException pe) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- sendToChecker --", pe);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ sendToChecker----------- Ended");
		return commonResponseDto;
	}

	public CommonResponseDto<List<PolicyLevelConversionDto>> getInprogressPolicyConversionDetailsList(String role,
			String unitCode) {
		logger.info("PolicyLevelConversionServiceImpl------getInprogressPolicyConversionDetailsList-------Started");
		CommonResponseDto<List<PolicyLevelConversionDto>> commonResponseDto = new CommonResponseDto<>();
		List<PolicyLevelConversionTempEntity> policyLevelConversionTempEntityList = new ArrayList<>();
		List<PolicyLevelConversionDto> policyLevelConversionDtoList = new ArrayList<>();
		try {
			if (role.equalsIgnoreCase(PolicyLevelConversionConstants.MAKER)) {
				policyLevelConversionTempEntityList = policyLevelConversionTempRepository
						.findAllByConversionStatusInAndUnitCodeAndIsActiveTrueOrderByModifiedOnDesc(
								PolicyLevelConversionConstants.inProgressPolicyConversionMaker(), unitCode);
			} else if (role.equalsIgnoreCase(PolicyLevelConversionConstants.CHECKER)) {
				policyLevelConversionTempEntityList = policyLevelConversionTempRepository
						.findAllByConversionStatusInAndUnitCodeAndIsActiveTrueOrderByModifiedOnDesc(
								PolicyLevelConversionConstants.inProgressPolicyConversionChecker(), unitCode);
			}
			if (!policyLevelConversionTempEntityList.isEmpty()) {
				policyLevelConversionTempEntityList.forEach(policyConversionDetails -> {
					PolicyLevelConversionDto policyLevelConversionDto = modelMapper.map(policyConversionDetails,
							PolicyLevelConversionDto.class);
					policyLevelConversionDtoList.add(policyLevelConversionDto);
				});
				commonResponseDto.setResponseData(policyLevelConversionDtoList);
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
			}
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getInprogressPolicyConversionDetailsList --",
					ce);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		}
		logger.info(
				"PolicyLevelConversionServiceImpl ------ getInprogressPolicyConversionDetailsList----------- Ended");
		return commonResponseDto;
	}

	public CommonResponseDto<List<PolicyLevelConversionDto>> getExistingPolicyConversionDetailsList(String role,
			String unitCode) {
		logger.info("PolicyLevelConversionServiceImpl------getExistingPolicyConversionDetailsList-------Started");
		CommonResponseDto<List<PolicyLevelConversionDto>> commonResponseDto = new CommonResponseDto<>();
		List<PolicyLevelConversionEntity> policyLevelConversionEntityList = new ArrayList<>();
		List<PolicyLevelConversionDto> policyLevelConversionDtoList = new ArrayList<>();
		try {
			if (role.equalsIgnoreCase(PolicyLevelConversionConstants.MAKER)
					|| role.equalsIgnoreCase(PolicyLevelConversionConstants.CHECKER)) {
				policyLevelConversionEntityList = policyLevelConversionRepository
						.findAllByConversionStatusInAndUnitCodeAndIsActiveTrueOrderByModifiedOnDesc(
								PolicyLevelConversionConstants.existingDetails(), unitCode);
			}
			if (!policyLevelConversionEntityList.isEmpty()) {
				policyLevelConversionEntityList.forEach(policyConversionDetails -> {
					PolicyLevelConversionDto policyLevelConversionDto = modelMapper.map(policyConversionDetails,
							PolicyLevelConversionDto.class);
					policyLevelConversionDtoList.add(policyLevelConversionDto);
				});
				commonResponseDto.setResponseData(policyLevelConversionDtoList);
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
			}
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getExistingPolicyConversionDetailsList --",
					ce);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FAIL);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ getExistingPolicyConversionDetailsList----------- Ended");
		return commonResponseDto;
	}

	@Override
	public CommonResponseDto<PolicyLevelConversionDto> approvePolicyConversion(Long conversionId, String modifiedBy) {
		logger.info("PolicyLevelConversionServiceImpl------approvePolicyConversion-------Started");
		CommonResponseDto<PolicyLevelConversionDto> commonResponseDto = new CommonResponseDto<>();
		PolicyMasterEntity policyMasterEntity = new PolicyMasterEntity();
		try {
			PolicyLevelConversionTempEntity newPolicyLevelConversionTempEntity = policyLevelConversionTempRepository
					.findByConversionIdAndIsActiveTrue(conversionId);
			if (newPolicyLevelConversionTempEntity == null) {
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.INVALID_CONVERSION);
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAIL);
				return commonResponseDto;
			} else {
				newPolicyLevelConversionTempEntity.setConversionStatus(
						NumericUtils.convertStringToInteger(PolicyLevelConversionConstants.ACTIVE));
				newPolicyLevelConversionTempEntity.setConversionWorkflowStatus(
						NumericUtils.convertStringToInteger(PolicyLevelConversionConstants.APPROVED));
				newPolicyLevelConversionTempEntity.setModifiedBy(modifiedBy);
				newPolicyLevelConversionTempEntity.setModifiedOn(DateUtils.sysDate());
				policyLevelConversionTempRepository.save(newPolicyLevelConversionTempEntity);

				PolicyLevelConversionEntity policyLevelConversionEntity = policyLevelConversionRepository
						.findByPrevPolicyIdAndIsActiveTrue(newPolicyLevelConversionTempEntity.getPrevPolicyId());
				if (policyLevelConversionEntity == null) {
					policyLevelConversionEntity = new PolicyLevelConversionEntity();
				}
				policyLevelConversionEntity.setClaimPending(newPolicyLevelConversionTempEntity.getClaimPending());
				policyLevelConversionEntity.setConversionDate(newPolicyLevelConversionTempEntity.getConversionDate());
				policyLevelConversionEntity
						.setConversionStatus(newPolicyLevelConversionTempEntity.getConversionStatus());
				policyLevelConversionEntity
						.setConversionWorkflowStatus(newPolicyLevelConversionTempEntity.getConversionWorkflowStatus());
				policyLevelConversionEntity.setUnitCode(newPolicyLevelConversionTempEntity.getUnitCode());
				policyLevelConversionEntity.setServiceId(newPolicyLevelConversionTempEntity.getServiceId());
				policyLevelConversionEntity.setCreatedBy(newPolicyLevelConversionTempEntity.getCreatedBy());
				policyLevelConversionEntity.setIsActive(newPolicyLevelConversionTempEntity.getIsActive());
				policyLevelConversionEntity.setNewPolicyAnnualRenewalDate(
						newPolicyLevelConversionTempEntity.getNewPolicyAnnualRenewalDate());
				policyLevelConversionEntity.setNewPolicyId(newPolicyLevelConversionTempEntity.getNewPolicyId());
				policyLevelConversionEntity
						.setNewPolicyMphCode(newPolicyLevelConversionTempEntity.getNewPolicyMphCode());
				policyLevelConversionEntity
						.setNewPolicyMphName(newPolicyLevelConversionTempEntity.getNewPolicyMphName());
				policyLevelConversionEntity
						.setNewPolicyProduct(newPolicyLevelConversionTempEntity.getNewPolicyProduct());
				policyLevelConversionEntity
						.setNewPolicyVariant(newPolicyLevelConversionTempEntity.getNewPolicyVariant());
				policyLevelConversionEntity
						.setNewpolicyIssueDate(newPolicyLevelConversionTempEntity.getNewpolicyIssueDate());
				policyLevelConversionEntity.setNewPolicyNo(newPolicyLevelConversionTempEntity.getNewPolicyNo());
				policyLevelConversionEntity.setNoOfCatalogue(newPolicyLevelConversionTempEntity.getNoOfCatalogue());
				policyLevelConversionEntity
						.setPrevFundBalancde(newPolicyLevelConversionTempEntity.getPrevFundBalancde());
				policyLevelConversionEntity.setPrevPolicyId(newPolicyLevelConversionTempEntity.getPrevPolicyId());
				policyLevelConversionEntity.setPrevPolicyNo(newPolicyLevelConversionTempEntity.getPrevPolicyNo());
				policyLevelConversionRepository.save(policyLevelConversionEntity);
                
				 policyMasterEntity = policyMasterRepository.findByPolicyNumberAndIsActiveTrue(policyLevelConversionEntity.getPrevPolicyNo());
				
				policyMasterEntity.setPolicyStatus(PolicyLevelConversionConstants.POLICY_CONVERTED);
				policyMasterEntity.setWorkflowStatus(PolicyLevelConversionConstants.POLICY_CONVERTED);
				policyMasterRepository.save(policyMasterEntity);
				
				PolicyMasterTempEntity policyMasterTempEntity = policyMasterTempRepository.findByPolicyNumberAndIsActiveTrue(policyMasterEntity.getPolicyNumber());
				 
				MphMasterTempEntity mphMasterTempEntity = mphMasterTempRepository.findAllByMphIdAndIsActiveTrue(policyMasterTempEntity.getMphId());
				
				MphMasterEntity mphMasterEntity = commonServiceImpl.convertMphMasterTempEntityToMphMasterMasterEntityNewForService(mphMasterTempEntity, PolicyLevelConversionConstants.CONVERSION_APPROVED, modifiedBy,policyLevelConversionEntity);			
				
				mphMasterRepository.save(mphMasterEntity);
				
				policyMasterEntity = policyMasterRepository.findByMphIdAndIsActiveTrue(mphMasterEntity.getMphId());
					
				PolicyLevelConversionEntity updatedPolicyLevelConversionEntity = policyLevelConversionRepository
						.findByConversionIdAndIsActiveTrue(policyLevelConversionEntity.getConversionId());
				updatedPolicyLevelConversionEntity.setNewPolicyId(policyMasterEntity.getPolicyId());
				updatedPolicyLevelConversionEntity.setNewPolicyNo(policyMasterEntity.getPolicyNumber());
				policyLevelConversionRepository.save(updatedPolicyLevelConversionEntity);
				commonResponseDto.setResponseData(
						modelMapper.map(updatedPolicyLevelConversionEntity, PolicyLevelConversionDto.class));

				/***
				 * @description This service method is called to update the policy number in
				 *              Fund/Interest Service
				 */
//				updatePolicyFund(policyMasterEntity);

				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.APPROVED);
			}
		} catch (ConstraintViolationException  ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- approvePolicyConversion --", ce);
			/***
			 * commonResponseDto.setResponseData(null);
			 */
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
			commonResponseDto.setTransactionMessage(ce.getMessage());
		}
		logger.info("PolicyLevelConversionServiceImpl ------ approvePolicyConversion----------- Ended");
		return commonResponseDto;
	}

	

	public CommonResponseDto<PolicyServiceNotesDto> savePolicyConversionNotes(PolicyServiceNotesDto policyServiceNotesDto) {
		logger.info("PolicyLevelConversionServiceImpl------savePolicyConversionNotes-------Started");
		CommonResponseDto<PolicyServiceNotesDto> commonResponseDto = new CommonResponseDto<>();
		try {
			PolicyServiceNotesTempEntity policyNotesTempEntity = new PolicyServiceNotesTempEntity();
			policyNotesTempEntity.setPolicyId(policyServiceNotesDto.getPolicyId());
			policyNotesTempEntity.setNoteContents(policyServiceNotesDto.getNoteContents());
			policyNotesTempEntity.setCreatedBy(policyServiceNotesDto.getCreatedBy());
			policyNotesTempEntity.setCreatedOn(DateUtils.sysDate());
			policyNotesTempEntity.setServiceId(policyServiceNotesDto.getServiceId());
			policyNotesTempEntity.setIsActive(true);
			policyServiceNotesTempRepository.save(policyNotesTempEntity);
			commonResponseDto.setResponseData(modelMapper.map(policyNotesTempEntity, PolicyServiceNotesDto.class));
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SAVE);
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- savePolicyConversionNotes --", ce);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ savePolicyConversionNotes----------- Ended");
		return commonResponseDto;
	}

	public CommonResponseDto<List<PolicyRulesDto>> saveBenefitTypes(List<PolicyRulesDto> policyRulesDto) {
		logger.info("PolicyLevelConversionServiceImpl------saveBenefitTypes-------Started");
		CommonResponseDto<List<PolicyRulesDto>> commonResponseDto = new CommonResponseDto<>();
//		List<PolicyRulesDto> policyRulesDtoList = new ArrayList<>();
//		PolicyRulesEntity policyRulesTemp = new PolicyRulesEntity();
		try {
//			for (PolicyRulesDto policyRulesDetails : policyRulesDto) {
//				policyRulesTemp = policyRulesRepository.findByRuleIdAndIsActiveTrue(policyRulesDetails.getRuleId());
//				if (policyRulesTemp != null && policyRulesTemp.getRuleId() != null) {
//					PolicyRulesDto policyRulesdata = modelMapper.map(policyRulesDetails, PolicyRulesDto.class);
//					PolicyRulesEntity policyRulesTempEntity = modelMapper.map(policyRulesdata, PolicyRulesEntity.class);
//					policyRulesRepository.save(policyRulesTempEntity);
//					PolicyRulesDto policyRules = modelMapper.map(policyRulesTempEntity, PolicyRulesDto.class);
//					policyRulesDtoList.add(policyRules);
//					commonResponseDto.setResponseData(policyRulesDtoList);
//					commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
//					commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.UPDATE);
//				} else {
//					commonResponseDto.setResponseData(null);
//					commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAIL);
//					commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.INVALID_REQUEST);
//				}
//			}
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- saveBenefitTypes --", ce);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ saveBenefitTypes----------- Ended");
		return commonResponseDto;
	}

	public CommonResponseDto<PolicyConversionDetailsDto> getInprogressOverallDetails(Long serviceId) {
		logger.info("PolicyLevelConversionServiceImpl------getInprogressOverallDetails-------Started");
		CommonResponseDto<PolicyConversionDetailsDto> commonResponseDto = new CommonResponseDto<>();
		PolicyConversionDetailsDto policyConversionDetailsDto = new PolicyConversionDetailsDto();
		List<PolicyServiceNotesDto> policyNotesDtoList = new ArrayList<>();
//		List<PolicyRulesDto> policyRulesDtoList = new ArrayList<>();
//		List<PolicyMbrDto> policyMbrDtoList = new ArrayList<>();
		try {
			PolicyLevelConversionTempEntity policyLevelConversionTempEntity = policyLevelConversionTempRepository
					.findByServiceIdAndIsActiveTrue(serviceId);
			
			PolicyMasterEntity policyMasterEntity = policyMasterRepository.findByPolicyNumberAndIsActiveTrue(policyLevelConversionTempEntity.getPrevPolicyNo());
			 
			PolicyMasterDto policyDto = modelMapper.map(policyMasterEntity, PolicyMasterDto.class);
			List<PolicyServiceNotesTempEntity> policyNotesTempEntityList = policyServiceNotesTempRepository
					.findAllByServiceIdOrderByServiceNoteIdDesc(serviceId);
			for (PolicyServiceNotesTempEntity policyNotesTempEntity : policyNotesTempEntityList) {
				PolicyServiceNotesDto policyNotesDto = modelMapper.map(policyNotesTempEntity, PolicyServiceNotesDto.class);
				policyNotesDtoList.add(policyNotesDto);
			}
//			List<PolicyRulesEntity> policyRulesTempEntityList = policyRulesRepository
//					.findAllByPolicyIdAndIsActiveTrue(policyLevelConversionTempEntity.getPrevPolicyId());
//			for (PolicyRulesEntity policyRulesTempEntity : policyRulesTempEntityList) {
//				PolicyRulesDto policyRulesDto = modelMapper.map(policyRulesTempEntity, PolicyRulesDto.class);
//				policyRulesDtoList.add(policyRulesDto);
//			}
			PolicyLevelConversionDto policyLevelConversionDto = modelMapper.map(policyLevelConversionTempEntity,
					PolicyLevelConversionDto.class);
//			List<PolicyMbrEntity> policyMbrEntityList = policyMbrRepository
//					.findByPolicyIdAndIsActiveTrueAndIsZeroIdFalse(policyLevelConversionTempEntity.getPrevPolicyId());
//			if (!policyMbrEntityList.isEmpty()) {
//				for (PolicyMbrEntity policyMbrEntity : policyMbrEntityList) {
//					PolicyMbrDto policyMbrDto = modelMapper.map(policyMbrEntity, PolicyMbrDto.class);
//					policyMbrDtoList.add(policyMbrDto);
//				}
//			}
//			policyConversionDetailsDto.setPolicyMbrDtoList(policyMbrDtoList);
			policyConversionDetailsDto.setPolicyLevelConversionDto(policyLevelConversionDto);
			policyConversionDetailsDto.setPolicyNotesDto(policyNotesDtoList);
//			policyConversionDetailsDto.setPolicyRulesDto(policyRulesDtoList);
			policyConversionDetailsDto.setPolicyDetails(policyDto);
			commonResponseDto.setResponseData(policyConversionDetailsDto);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getInprogressOverallDetails --", ce);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ getInprogressOverallDetails----------- Ended");
		return commonResponseDto;
	}

	public CommonResponseDto<PolicyConversionDetailsDto> getExistingOverallDetails(Long serviceId) {
		logger.info("PolicyLevelConversionServiceImpl------getExistingOverallDetails-------Started");
		CommonResponseDto<PolicyConversionDetailsDto> commonResponseDto = new CommonResponseDto<>();
		PolicyConversionDetailsDto policyConversionDetailsDto = new PolicyConversionDetailsDto();
		List<PolicyServiceNotesDto> policyNotesDtoList = new ArrayList<>();
		List<PolicyServiceDocumentDto> policyServiceDocumentDtoList = new ArrayList<>();
		PolicyMasterDto policyMasterDto = new PolicyMasterDto();
		try {
			PolicyLevelConversionEntity policyLevelConversionEntity = policyLevelConversionRepository
					.findByServiceIdAndIsActiveTrue(serviceId);
			
			PolicyMasterEntity policyMasterEntity = policyMasterRepository.findByPolicyNumberAndIsActiveTrue(policyLevelConversionEntity.getPrevPolicyNo());
		    
			if(policyMasterEntity != null) {
				policyMasterDto = modelMapper.map(policyMasterEntity, PolicyMasterDto.class);
			}
			
			List<PolicyServiceNotesEntity> policyNotesTempEntityList = policyServiceNotesRepository
					.findAllByServiceIdOrderByServiceNoteIdDesc(serviceId);
			if (!policyNotesTempEntityList.isEmpty()) {
				for (PolicyServiceNotesEntity policyNotesTempEntity : policyNotesTempEntityList) {
					PolicyServiceNotesDto policyNotesDto = modelMapper.map(policyNotesTempEntity, PolicyServiceNotesDto.class);
					policyNotesDtoList.add(policyNotesDto);
				}
			}
			
			List<PolicyServiceDocumentEntity> policyServiceDocumentEntity = policyServiceDocumentRepository.findAllByConversionId(policyLevelConversionEntity.getConversionId());
            
			if(!policyServiceDocumentEntity.isEmpty()) {
				
				policyServiceDocumentDtoList = mapList(policyServiceDocumentEntity, PolicyServiceDocumentDto.class);
			
			}
			
			PolicyLevelConversionDto policyLevelConversionDto = modelMapper.map(policyLevelConversionEntity,
					PolicyLevelConversionDto.class);
			policyConversionDetailsDto.setPolicyLevelConversionDto(policyLevelConversionDto);
			policyConversionDetailsDto.setPolicyNotesDto(policyNotesDtoList);
			policyConversionDetailsDto.setPolicyDetails(policyMasterDto);
			policyConversionDetailsDto.setPolicyServiceDocument(policyServiceDocumentDtoList);
			commonResponseDto.setResponseData(policyConversionDetailsDto);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getExistingOverallDetails --", ce);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ getExistingOverallDetails----------- Ended");
		return commonResponseDto;
	}

	public CommonResponseDto<List<PolicyServiceNotesDto>> getNotesDetailsList(Long serviceId) {
		logger.info("PolicyLevelConversionServiceImpl------getNotesDetailsList-------Started");
		CommonResponseDto<List<PolicyServiceNotesDto>> commonResponseDto = new CommonResponseDto<>();
		List<PolicyServiceNotesDto> proposalNotesDtoList = new ArrayList<>();
		try {
			List<PolicyServiceNotesTempEntity> notesList = policyServiceNotesTempRepository
					.findAllByServiceIdAndIsActiveTrue(serviceId);
			if(!notesList.isEmpty()) {
			for (PolicyServiceNotesTempEntity policyNotesTempEntity : notesList) {
				PolicyServiceNotesDto policyNotesDto = modelMapper.map(policyNotesTempEntity, PolicyServiceNotesDto.class);
				proposalNotesDtoList.add(policyNotesDto);
			}
			commonResponseDto.setResponseData(proposalNotesDtoList);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
			}
		} catch (ConstraintViolationException ce) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getNotesDetailsList --", ce);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ getNotesDetailsList----------- Ended");
		return commonResponseDto;
	}

	@Override
	public CommonResponseDto<PolicyLevelConversionDto> sendToMaker(String conversionId, String modifiedBy) {
		logger.info("PolicyLevelConversionServiceImpl------sendToMaker-------Started");
		CommonResponseDto<PolicyLevelConversionDto> commonResponseDto = new CommonResponseDto<>();
		try {
			PolicyLevelConversionTempEntity policyLevelConversionTempEntity = policyLevelConversionTempRepository
					.findByConversionIdAndIsActiveTrue(NumericUtils.convertStringToLong(conversionId));
			if (policyLevelConversionTempEntity == null) {
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAIL);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.INVALID_CONVERSION);
			} else {
				policyLevelConversionTempEntity.setConversionStatus(
						NumericUtils.convertStringToInteger(PolicyLevelConversionConstants.SEND_TO_MAKER));
				policyLevelConversionTempEntity.setConversionWorkflowStatus(
						NumericUtils.convertStringToInteger(PolicyLevelConversionConstants.SEND_TO_MAKER));
				policyLevelConversionTempEntity.setModifiedBy(modifiedBy);
				policyLevelConversionTempEntity.setModifiedOn(DateUtils.sysDate());
				policyLevelConversionTempRepository.save(policyLevelConversionTempEntity);
				commonResponseDto.setResponseData(
						modelMapper.map(policyLevelConversionTempEntity, PolicyLevelConversionDto.class));
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.POLICY_SEND_TO_MAKER);
			}

		} catch (IllegalArgumentException e) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- sendToMaker --", e);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ sendToMaker----------- Ended");
		return commonResponseDto;
	}

	@Override
	public CommonResponseDto<PolicyLevelConversionDto> policyConversionRejection(
			PolicyLevelConversionDto policyLevelConversionDto) {
		logger.info("PolicyLevelConversionServiceImpl------policyConversionRejection-------Started");
		CommonResponseDto<PolicyLevelConversionDto> commonResponseDto = new CommonResponseDto<>();
		try {
			PolicyLevelConversionTempEntity policyLevelConversionTempEntity = policyLevelConversionTempRepository
					.findByConversionIdAndIsActiveTrue(policyLevelConversionDto.getConversionId());
			if (policyLevelConversionTempEntity == null) {
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAIL);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.INVALID_CONVERSION);
			} else {
				policyLevelConversionTempEntity.setConversionStatus(
						NumericUtils.convertStringToInteger(PolicyLevelConversionConstants.REJECT));
				policyLevelConversionTempEntity.setConversionWorkflowStatus(
						NumericUtils.convertStringToInteger(PolicyLevelConversionConstants.REJECT));
				policyLevelConversionTempEntity.setModifiedBy(policyLevelConversionDto.getModifiedBy());
				policyLevelConversionTempEntity.setModifiedOn(DateUtils.sysDate());
				policyLevelConversionTempEntity
						.setRejectionReasonCode(policyLevelConversionDto.getRejectionReasonCode());
				policyLevelConversionTempEntity.setRejectionRemarks(policyLevelConversionDto.getRejectionRemarks());
				policyLevelConversionTempRepository.save(policyLevelConversionTempEntity);
				PolicyLevelConversionEntity policyLevelConversionEntity = modelMapper
						.map(policyLevelConversionTempEntity, PolicyLevelConversionEntity.class);
				policyLevelConversionEntity.setConversionId(null);
				policyLevelConversionEntity.setDocs(null);
				policyLevelConversionRepository.save(policyLevelConversionEntity);
				List<PolicyServiceDocumentTempEntity> commonDocsListEntityList = policyServiceDocumentTempRepository.findAllByConversionIdAndIsActiveTrue(policyLevelConversionTempEntity.getConversionId());
				if(!commonDocsListEntityList.isEmpty()) {
					for(PolicyServiceDocumentTempEntity policyServiceDocumentTempEntity : commonDocsListEntityList) {						
						PolicyServiceDocumentEntity policyServiceDocumentEntity = modelMapper.map(policyServiceDocumentTempEntity,PolicyServiceDocumentEntity.class);
						policyServiceDocumentEntity.setDocumentId(null);
						policyServiceDocumentEntity.setConversionId(policyLevelConversionEntity.getConversionId());
						policyServiceDocumentRepository.save(policyServiceDocumentEntity);
					}
				}
				List<PolicyServiceNotesTempEntity> policyNotesTempEntityList = policyServiceNotesTempRepository
						.findAllByServiceIdOrderByServiceNoteIdDesc(policyLevelConversionTempEntity.getServiceId());
				if(!policyNotesTempEntityList .isEmpty()) {
				for (PolicyServiceNotesTempEntity policyNotesTempEntity : policyNotesTempEntityList) {
					PolicyServiceNotesEntity policyServiceNotesEntity = modelMapper.map(policyNotesTempEntity, PolicyServiceNotesEntity.class);
					policyServiceNotesEntity.setServiceNoteId(null);
					policyServiceNotesRepository.save(policyServiceNotesEntity);
				}
				}
				commonResponseDto
						.setResponseData(modelMapper.map(policyLevelConversionEntity, PolicyLevelConversionDto.class));
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.POLICY_REJECTED);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- policyConversionRejection --", e);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ policyConversionRejection----------- Ended");
		return commonResponseDto;
	}

	public CommonResponseDto<PolicyLevelConversionDto> getPolicyConversionDetailsBypolicyNo(String prevPolicyNo,
			String role) {
		logger.info("PolicyLevelConversionServiceImpl------getPolicyConversionDetailsBypolicyNo-------Started");
		CommonResponseDto<PolicyLevelConversionDto> commonResponseDto = new CommonResponseDto<>();
		PolicyLevelConversionTempEntity policyLevelConversionTempEntity = null;
		try {
			if (role.equalsIgnoreCase(PolicyLevelConversionConstants.CHECKER)) {
				policyLevelConversionTempEntity = policyLevelConversionTempRepository
						.findByPrevPolicyNoAndConversionStatusInAndIsActiveTrue(prevPolicyNo,
								PolicyLevelConversionConstants.inProgressPolicyConversionChecker());
			} else if (role.equalsIgnoreCase(PolicyLevelConversionConstants.MAKER)) {
				policyLevelConversionTempEntity = policyLevelConversionTempRepository
						.findByPrevPolicyNoAndConversionStatusInAndIsActiveTrue(prevPolicyNo,
								PolicyLevelConversionConstants.inProgressPolicyConversionMaker());
			}
			if (policyLevelConversionTempEntity == null) {
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.INVALID_POLICYNO);
				return commonResponseDto;
			}
			PolicyLevelConversionDto policyLevelConversionDto = modelMapper.map(policyLevelConversionTempEntity,
					PolicyLevelConversionDto.class);
			commonResponseDto.setResponseData(policyLevelConversionDto);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getPolicyConversionDetailsBypolicyNo --", e);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ getPolicyConversionDetailsBypolicyNo----------- Ended");
		return commonResponseDto;
	}

	@Override
	public CommonResponseDto<PolicyLevelConversionDto> getExistingDetailsByNewPolicyNo(String newPolicyNo) {
		logger.info("PolicyLevelConversionServiceImpl------getExistingDetailsByNewPolicyNo-------Started");
		CommonResponseDto<PolicyLevelConversionDto> commonResponseDto = new CommonResponseDto<>();
		try {
			PolicyLevelConversionEntity policyLevelConversionEntity = policyLevelConversionRepository
					.findByNewPolicyNoAndConversionStatusInAndIsActiveTrue(newPolicyNo,
							PolicyLevelConversionConstants.existingDetails());
			if (policyLevelConversionEntity == null) {
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.INVALID_REQUEST);
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
				return commonResponseDto;
			}
			PolicyLevelConversionDto policyLevelConversionDto = modelMapper.map(policyLevelConversionEntity,
					PolicyLevelConversionDto.class);
			commonResponseDto.setResponseData(policyLevelConversionDto);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getExistingDetailsByNewPolicyNo --", e);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
		}
		logger.info("PolicyLevelConversionServiceImpl ------ getExistingDetailsByNewPolicyNo----------- Ended");
		return commonResponseDto;
	}

	public CommonResponseDto<List<PolicyConversionSearchDto>> InProgressCommonSearch(
			PolicyConversionSearchDto policyConversionSearchDto) {
		logger.info("PolicyLevelConversionServiceImpl------InProgressCommonSearch-------Started");
		CommonResponseDto<List<PolicyConversionSearchDto>> commonResponseDto = new CommonResponseDto<>();
		List<PolicyConversionSearchDto> policyConversionSearchDtolist = new ArrayList<>();
		Integer pageCount = policyConversionSearchDto.getPageCount();
		Integer limit = policyConversionSearchDto.getLimit();

		if (pageCount == null || pageCount < 0) {
			pageCount = 0;
		}
		if (limit == null || limit < 0) {
			limit = 5000;
		}
		try {
			List<PolicyLevelConversionTempEntity> entity = null;
			CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
			CriteriaQuery<PolicyLevelConversionTempEntity> criteria = criteriaBuilder
					.createQuery(PolicyLevelConversionTempEntity.class);
			Root<PolicyLevelConversionTempEntity> root = criteria.from(PolicyLevelConversionTempEntity.class);
			List<Predicate> predicates = new ArrayList<>();

			if (StringUtils.isNotBlank(policyConversionSearchDto.getMphCode())) {
				predicates.add(
						criteriaBuilder.equal(root.get("newPolicyMphCode"), policyConversionSearchDto.getMphCode()));
			}

			if (StringUtils.isNotBlank(policyConversionSearchDto.getMphName())) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("newPolicyMphName")),
						"%" + policyConversionSearchDto.getMphName().toLowerCase() + "%"));
			}

			if (StringUtils.isNotBlank(policyConversionSearchDto.getStatus())) {
				predicates.add(criteriaBuilder.equal(root.get(PolicyLevelConversionConstants.CONVERSION_STATUS),
						policyConversionSearchDto.getStatus()));
			}
			if (StringUtils.isNotBlank(policyConversionSearchDto.getProduct())) {
				predicates.add(
						criteriaBuilder.equal(root.get("newPolicyProduct"), policyConversionSearchDto.getProduct()));
			}
			if (StringUtils.isNotBlank(policyConversionSearchDto.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get("unitCode"), policyConversionSearchDto.getUnitCode()));
			}

			if (policyConversionSearchDto.getStatus() != null && policyConversionSearchDto.getStatus().isEmpty()
					&& StringUtils.isNotBlank(policyConversionSearchDto.getLogin())
					&& policyConversionSearchDto.getLogin().equalsIgnoreCase(PolicyLevelConversionConstants.MAKER)) {
				Path<Integer> paths = root.get(PolicyLevelConversionConstants.CONVERSION_STATUS);
				predicates.add(paths.in(PolicyLevelConversionConstants.inProgressPolicyConversionMaker()));
			} else if (policyConversionSearchDto.getStatus() != null && policyConversionSearchDto.getStatus().isEmpty()
					&& StringUtils.isNotBlank(policyConversionSearchDto.getLogin())
					&& policyConversionSearchDto.getLogin().equalsIgnoreCase(PolicyLevelConversionConstants.CHECKER)) {
				Path<Integer> paths = root.get(PolicyLevelConversionConstants.CONVERSION_STATUS);
				predicates.add(paths.in(PolicyLevelConversionConstants.inProgressPolicyConversionChecker()));
			}

			List<Order> order = new ArrayList<>();
			order.add(criteriaBuilder.desc(root.get("modifiedOn")));
			criteria.orderBy(order);
			criteria.where(predicates.toArray(new Predicate[predicates.size()]));
			criteria.select(root).where(predicates.toArray(new Predicate[] {}));
			entity = entityManager.createQuery(criteria).getResultList();

			if (!entity.isEmpty()) {
				for (PolicyLevelConversionTempEntity policyLevelConversionTempEntity : entity) {
					PolicyConversionSearchDto newPolicyConversionSearchDto = new PolicyConversionSearchDto();
					newPolicyConversionSearchDto.setPrevPolicyNo(policyLevelConversionTempEntity.getPrevPolicyNo());
					newPolicyConversionSearchDto.setMphCode(policyLevelConversionTempEntity.getNewPolicyMphCode());
					newPolicyConversionSearchDto.setMphName(policyLevelConversionTempEntity.getNewPolicyMphName());
					newPolicyConversionSearchDto.setProduct(
							NumericUtils.convertLongToString(policyLevelConversionTempEntity.getNewPolicyProduct()));
					newPolicyConversionSearchDto.setStatus(
							NumericUtils.convertIntegerToString(policyLevelConversionTempEntity.getConversionStatus()));
					newPolicyConversionSearchDto.setUnitCode(policyLevelConversionTempEntity.getUnitCode());
					newPolicyConversionSearchDto.setServiceId(policyLevelConversionTempEntity.getServiceId());
					policyConversionSearchDtolist.add(newPolicyConversionSearchDto);
				}
			}
			commonResponseDto.setResponseData(policyConversionSearchDtolist);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
		} catch (NonUniqueResultException ue) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- InProgressCommonSearch --", ue);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
		}
		logger.info("ProposalMakerServiceImpl-InProgressCommonSearch-Ended");
		return commonResponseDto;
	}

	public CommonResponseDto<List<PolicyConversionSearchDto>> existingCommonSearch(
			PolicyConversionSearchDto policyConversionSearchDto) {
		logger.info("PolicyLevelConversionServiceImpl------InProgressCommonSearch-------Started");
		CommonResponseDto<List<PolicyConversionSearchDto>> commonResponseDto = new CommonResponseDto<>();
		List<PolicyConversionSearchDto> policyConversionSearchDtolist = new ArrayList<>();
		Integer pageCount = policyConversionSearchDto.getPageCount();
		Integer limit = policyConversionSearchDto.getLimit();

		if (pageCount == null || pageCount < 0) {
			pageCount = 0;
		}
		if (limit == null || limit < 0) {
			limit = 5000;
		}
		try {
			List<PolicyLevelConversionEntity> entity = null;
			CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
			CriteriaQuery<PolicyLevelConversionEntity> criteria = criteriaBuilder
					.createQuery(PolicyLevelConversionEntity.class);
			Root<PolicyLevelConversionEntity> root = criteria.from(PolicyLevelConversionEntity.class);
			List<Predicate> predicates = new ArrayList<>();

			if (StringUtils.isNotBlank(policyConversionSearchDto.getMphCode())) {
				predicates.add(
						criteriaBuilder.equal(root.get("newPolicyMphCode"), policyConversionSearchDto.getMphCode()));
			}

			if (StringUtils.isNotBlank(policyConversionSearchDto.getMphName())) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("newPolicyMphName")),
						"%" + policyConversionSearchDto.getMphName().toLowerCase() + "%"));
			}

			if (StringUtils.isNotBlank(policyConversionSearchDto.getStatus())) {
				predicates.add(
						criteriaBuilder.equal(root.get("conversionStatus"), policyConversionSearchDto.getStatus()));
			}
			if (StringUtils.isNotBlank(policyConversionSearchDto.getProduct())) {
				predicates.add(
						criteriaBuilder.equal(root.get("newPolicyProduct"), policyConversionSearchDto.getProduct()));
			}
			if (StringUtils.isNotBlank(policyConversionSearchDto.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get("unitCode"), policyConversionSearchDto.getUnitCode()));
			}

			List<Order> order = new ArrayList<>();
			order.add(criteriaBuilder.desc(root.get("createdOn")));
			criteria.orderBy(order);
			criteria.where(predicates.toArray(new Predicate[predicates.size()]));
			criteria.select(root).where(predicates.toArray(new Predicate[] {}));
			entity = entityManager.createQuery(criteria).getResultList();

			if (!entity.isEmpty()) {
				for (PolicyLevelConversionEntity policyLevelConversionTempEntity : entity) {
					PolicyConversionSearchDto newPolicyConversionSearchDto = new PolicyConversionSearchDto();
					newPolicyConversionSearchDto.setNewPolicyNo(policyLevelConversionTempEntity.getNewPolicyNo());
					newPolicyConversionSearchDto.setMphCode(policyLevelConversionTempEntity.getNewPolicyMphCode());
					newPolicyConversionSearchDto.setMphName(policyLevelConversionTempEntity.getNewPolicyMphName());
					newPolicyConversionSearchDto.setProduct(
							NumericUtils.convertLongToString(policyLevelConversionTempEntity.getNewPolicyProduct()));
					newPolicyConversionSearchDto.setStatus(
							NumericUtils.convertIntegerToString(policyLevelConversionTempEntity.getConversionStatus()));
					newPolicyConversionSearchDto.setUnitCode(policyLevelConversionTempEntity.getUnitCode());
					newPolicyConversionSearchDto.setServiceId(policyLevelConversionTempEntity.getServiceId());
					policyConversionSearchDtolist.add(newPolicyConversionSearchDto);
				}
			}
			commonResponseDto.setResponseData(policyConversionSearchDtolist);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
		} catch (NonUniqueResultException ue) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- InProgressCommonSearch --", ue);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
		}
		logger.info("ProposalMakerServiceImpl-InProgressCommonSearch-Ended");
		return commonResponseDto;
	}

//	@Override
//	public CommonResponseDto<PolicyMbrTempEntity> getMemberDetailsInConversion(String conversionId, String memberId) {
//		CommonResponseDto<PolicyMbrTempEntity> commonResponseDto = new CommonResponseDto<>();
//		logger.info("PolicyLevelConversionServiceImpl------getMemberDetailsInConversion-------Started");
//		try {
//			PolicyMbrTempEntity member = policyMbrTempRepository.findByConversionIdAndMemberIdAndIsActiveTrueAndIsZeroIdFalse(
//					NumericUtils.convertStringToLong(conversionId), NumericUtils.convertStringToLong(memberId));
//			commonResponseDto.setResponseData(member);
//			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
//			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
//
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getMemberDetailsInConversion --", e);
//			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
//			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
//		}
//		logger.error("PolicyLevelConversionServiceImpl------getMemberDetailsInConversion-------Ended");
//		return commonResponseDto;
//	}

//	@Override
//	public CommonResponseDto<PolicyMbrEntity> getMemberDetailsInConversionMaster(String conversionId, String memberId) {
//		CommonResponseDto<PolicyMbrEntity> commonResponseDto = new CommonResponseDto<>();
//		logger.info("PolicyLevelConversionServiceImpl------getMemberDetailsInConversion-------Started");
//		try {
//			PolicyMbrEntity member = policyMbrRepository.findByConversionIdAndMemberIdAndIsActiveTrueAndIsZeroIdFalse(
//					NumericUtils.convertStringToLong(conversionId), NumericUtils.convertStringToLong(memberId));
//			commonResponseDto.setResponseData(member);
//			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
//			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
//
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getMemberDetailsInConversion --", e);
//			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
//			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
//		}
//		logger.error("PolicyLevelConversionServiceImpl------getMemberDetailsInConversion-------Ended");
//
//		return commonResponseDto;
//	}

//	@Override
//	public CommonResponseDto<List<PolicyMbrTempEntity>> getMemberDetailsbyConversionIdInprocess(String conversionId) {
//		CommonResponseDto<List<PolicyMbrTempEntity>> commonResponseDto = new CommonResponseDto<>();
//		logger.info("PolicyLevelConversionServiceImpl------getMemberDetailsbyConversionIdInprocess-------Started");
//		try {
//			List<PolicyMbrTempEntity> member = policyMbrTempRepository
//					.findByConversionIdAndIsActiveTrueAndIsZeroIdFalse(NumericUtils.convertStringToLong(conversionId));
//			commonResponseDto.setResponseData(member);
//			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
//			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
//
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getMemberDetailsbyConversionIdInprocess --",
//					e);
//			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
//			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
//		}
//		logger.error("PolicyLevelConversionServiceImpl------getMemberDetailsbyConversionIdInprocess-------Ended");
//
//		return commonResponseDto;
//	}

//	@Override
//	public CommonResponseDto<List<PolicyMbrEntity>> getMemberDetailsbyConversionIdExisting(String conversionId) {
//		CommonResponseDto<List<PolicyMbrEntity>> commonResponseDto = new CommonResponseDto<>();
//		logger.info(
//				"PolicyLevelConversionServiceImpl------getMemberDetailgetMemberDetailsbyConversionIdExistingsInConversion-------Started");
//		try {
//			List<PolicyMbrEntity> member = policyMbrRepository
//					.findByConversionIdAndIsActiveTrueAndIsZeroIdFalse(NumericUtils.convertStringToLong(conversionId));
//			commonResponseDto.setResponseData(member);
//			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
//			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.SUCCESS_RETRIVE_MSG);
//
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getMemberDetailsbyConversionIdExisting --", e);
//			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
//			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
//		}
//		logger.error("PolicyLevelConversionServiceImpl------getMemberDetailsbyConversionIdExisting-------Ended");
//
//		return commonResponseDto;
//	}

//	@Override
//	public CommonResponseDto<PolicyMbrBankDto> removeMemberBankDetails(Integer bankId, Long memberId,
//			String modifiedBy) {
//		logger.info("PolicyLevelConversionServiceImpl------removeMemberBankDetails-------Started");
//		CommonResponseDto<PolicyMbrBankDto> commonResponseDto = new CommonResponseDto<>();
//		try {
//			PolicyMbrBankTempEntity policyMbrBankTempEntity = policyMbrBankTempRepository
//					.findByBankIdAndMemberIdAndIsActiveTrue(bankId, memberId);
//			if (policyMbrBankTempEntity != null) {
//				policyMbrBankTempEntity.setIsActive(false);
//				policyMbrBankTempEntity.setModifiedBy(modifiedBy);
//				policyMbrBankTempEntity.setModifiedOn(DateUtils.sysSQLDate());
//				policyMbrBankTempRepository.save(policyMbrBankTempEntity);
//				commonResponseDto.setResponseData(null);
//				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
//				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.DELETED);
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- PolicyLevelConversionServiceImpl-- removeMemberBankDetails --", e);
//			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
//			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
//		}
//		logger.error("PolicyLevelConversionServiceImpl------removeMemberBankDetails-------Ended");
//		return commonResponseDto;
//	}

//	@Override
//	public CommonResponseDto<PolicyMbrAddressDto> removeMemberAddressDetails(Long addressId, Long memberId,
//			String modifiedBy) {
//		logger.info("PolicyLevelConversionServiceImpl------removeMemberAddressDetails-------Started");
//		CommonResponseDto<PolicyMbrAddressDto> commonResponseDto = new CommonResponseDto<>();
//		try {
//			PolicyMbrAddressTempEntity policyMbrAddressTempEntity = policyMbrAddressTempRepository
//					.findByAddressIdAndMemberIdAndIsActiveTrue(addressId, memberId);
//			if (policyMbrAddressTempEntity != null) {
//				policyMbrAddressTempEntity.setIsActive(false);
//				policyMbrAddressTempEntity.setModifiedBy(modifiedBy);
//				policyMbrAddressTempEntity.setModifiedOn(DateUtils.sysSQLDate());
//				policyMbrAddressTempRepository.save(policyMbrAddressTempEntity);
//				commonResponseDto.setResponseData(null);
//				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
//				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.DELETED);
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- PolicyLevelConversionServiceImpl-- removeMemberAddressDetails --", e);
//			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
//			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
//		}
//		logger.error("PolicyLevelConversionServiceImpl------removeMemberAddressDetails-------Ended");
//		return commonResponseDto;
//	}

	@Override
	public CommonResponseDto<PolicyServiceDocumentDto> removeDocumentDetails(Long docId, Long conversionId, String modifiedBy) {
		logger.info("PolicyLevelConversionServiceImpl------removeDocumentDetails-------Started");
		CommonResponseDto<PolicyServiceDocumentDto> commonResponseDto = new CommonResponseDto<>();
		try {
			PolicyServiceDocumentTempEntity commonDocsTempEntity = policyServiceDocumentTempRepository
					.findByDocumentIdAndConversionIdAndIsActiveTrue(docId, conversionId);
			if (commonDocsTempEntity != null) {
				commonDocsTempEntity.setIsActive(false);
				commonDocsTempEntity.setModifiedBy(modifiedBy);
				commonDocsTempEntity.setModifiedOn(DateUtils.sysSQLDate());
				policyServiceDocumentTempRepository.save(commonDocsTempEntity);
				commonResponseDto.setResponseData(null);
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.DELETED);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- removeDocumentDetails --", e);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
		}
		logger.info("PolicyLevelConversionServiceImpl------removeDocumentDetails-------Ended");
		return commonResponseDto;
	}

	@Override
	public CommonResponseDto<PolicyDetailsDto> policyCriteriaSearch(String policyNo) {
		logger.info("PolicyLevelConversionServiceImpl------removeDocumentDetails-------Started");
		CommonResponseDto<PolicyDetailsDto> commonResponseDto = new CommonResponseDto<>();
		PolicyDetailsDto policyDetailsDto = new PolicyDetailsDto();
     try {
    	 PolicyMasterEntity policyEntity = policyMasterRepository.findByPolicyNumberAndIsActiveTrue(policyNo);
    	 if(policyEntity != null) {
    	 PolicyMasterDto policyMasterDto = modelMapper.map(policyEntity,PolicyMasterDto.class);
    	 MphMasterEntity mphMasterEntity = mphMasterRepository.findByMphIdAndIsActiveTrue(policyEntity.getMphId());
    	 MphMasterDto mphMasterDto = modelMapper.map(mphMasterEntity,MphMasterDto.class);
    	 policyDetailsDto.setPolicyMaster(policyMasterDto);    
    	 policyDetailsDto.setMphMaster(mphMasterDto);
    	 commonResponseDto.setResponseData(policyDetailsDto);
		 commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
		 commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FETCH_MESSAGE);
    	 }
     }
     catch (IllegalArgumentException e) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- removeDocumentDetails --", e);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
		}
		logger.info("PolicyLevelConversionServiceImpl------removeDocumentDetails-------Ended");
		return commonResponseDto;
	}
	
	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
	}

	@Override
	public CommonResponseDto<List<PolicyServiceDocumentDto>> getDocumentList(Long conversionId) {
		logger.info("PolicyLevelConversionServiceImpl------getDocumentList-------Started");
		CommonResponseDto<List<PolicyServiceDocumentDto>> commonResponseDto = new CommonResponseDto<>();
		List<PolicyServiceDocumentDto> commonDocsList = null;
		try {
			List<PolicyServiceDocumentTempEntity> commonDocsListEntityList = policyServiceDocumentTempRepository.findAllByConversionIdAndIsActiveTrue(conversionId);
		    if(!commonDocsListEntityList.isEmpty()) {
		    	commonDocsList = mapList(commonDocsListEntityList,PolicyServiceDocumentDto.class);
		    	commonResponseDto.setResponseData(commonDocsList);
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FETCH_MESSAGE);
		    }
		}
		catch (IllegalArgumentException e) {
			logger.error("Exception-- PolicyLevelConversionServiceImpl-- getDocumentList --", e);
			commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
			commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
		}
		logger.info("PolicyLevelConversionServiceImpl------getDocumentList-------Ended");
		return commonResponseDto;
	}

	@Override
	public CommonResponseDto<PolicyServiceDocumentDto> saveDocumentDetails(
			PolicyServiceDocumentDto policyServiceDocumentDto) {
		CommonResponseDto<PolicyServiceDocumentDto> commonResponseDto = new CommonResponseDto<>();
		 try {
			 PolicyServiceDocumentTempEntity policyServiceDocumentTempEntity = modelMapper.map(policyServiceDocumentDto,PolicyServiceDocumentTempEntity.class);
			    policyServiceDocumentTempEntity.setIsActive(true);
			    policyServiceDocumentTempRepository.save(policyServiceDocumentTempEntity);
			    commonResponseDto.setResponseData(modelMapper.map(policyServiceDocumentTempEntity,PolicyServiceDocumentDto.class));
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.SUCCESS);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.FETCH_MESSAGE);
		 }
		 catch (IllegalArgumentException e) {
				logger.error("Exception-- PolicyLevelConversionServiceImpl-- getDocumentList --", e);
				commonResponseDto.setTransactionMessage(PolicyLevelConversionConstants.ERROR);
				commonResponseDto.setTransactionStatus(PolicyLevelConversionConstants.FAILURE);
			}
			logger.info("PolicyLevelConversionServiceImpl------getDocumentList-------Ended");
			return commonResponseDto;
	}

}
