package com.lic.epgs.policyservicing.policylvl.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.policy.dto.MphAddressDto;
import com.lic.epgs.policy.dto.MphBankDto;
import com.lic.epgs.policy.dto.MphMasterDto;
import com.lic.epgs.policy.dto.PolicyRulesDto;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.dto.PolicySearchResponseDto;
import com.lic.epgs.policy.entity.MphAddressEntity;
import com.lic.epgs.policy.entity.MphAddressTempEntity;
import com.lic.epgs.policy.entity.MphBankEntity;
import com.lic.epgs.policy.entity.MphBankTempEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.MphMasterTempEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterTempEntity;
import com.lic.epgs.policy.entity.PolicyRulesEntity;
import com.lic.epgs.policy.entity.PolicyRulesTempEntity;
import com.lic.epgs.policy.old.dto.PolicyAddressOldDto;
import com.lic.epgs.policy.old.dto.PolicyBankOldDto;
import com.lic.epgs.policy.old.dto.PolicyDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policy.old.dto.PolicyRulesOldDto;
import com.lic.epgs.policy.repository.MphAddressRepository;
import com.lic.epgs.policy.repository.MphAddressTempRepository;
import com.lic.epgs.policy.repository.MphBankRepository;
import com.lic.epgs.policy.repository.MphBankTempRepository;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.MphMasterTempRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.policy.repository.PolicyMasterTempRepository;
import com.lic.epgs.policy.repository.PolicyRulesRepository;
import com.lic.epgs.policy.repository.PolicyRulesTempRepository;
import com.lic.epgs.policy.service.impl.PolicyCommonServiceImpl;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceEntity;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesTempEntity;
import com.lic.epgs.policyservicing.common.repository.PolicyServiceNotesTempRepository;
import com.lic.epgs.policyservicing.common.repository.PolicyServicingCommonRepository;
import com.lic.epgs.policyservicing.policylvl.dto.policydetailschange.PolicyDetailsChangeDto;
import com.lic.epgs.policyservicing.policylvl.dto.policydetailschange.PolicyDtlsResponseDto;
import com.lic.epgs.policyservicing.policylvl.entity.policydetailschange.policyDetailsChangeEntity;
import com.lic.epgs.policyservicing.policylvl.entity.policydetailschange.policyDetailsChangeTempEntity;
import com.lic.epgs.policyservicing.policylvl.repository.policydetailschange.PolicyDetailsChangeRepository;
import com.lic.epgs.policyservicing.policylvl.repository.policydetailschange.PolicyDetailsChangeTempRepository;
import com.lic.epgs.policyservicing.policylvl.service.PolicyDetailsChangeService;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

@Service
public class PolicyDetailsChangeServiceImpl implements PolicyDetailsChangeService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PolicyDetailsChangeRepository policyDtlsChangeRepo;
	@Autowired
	private PolicyDetailsChangeTempRepository policyDtlsChangeTempRepo;
	@Autowired
	private PolicyMasterTempRepository policyMasterTempRepository;
	@Autowired
	private PolicyMasterRepository policyMasterRepository;
	@Autowired
	private PolicyCommonServiceImpl policyCommonServiceImpl;
	@Autowired
	private PolicyServiceNotesTempRepository policyServiceNotesTempRepo;
	@Autowired
	private MphMasterTempRepository mphMasterTempRepository;
	@Autowired
	private MphMasterRepository mphMasterRepository;
	@Autowired
	private MphBankTempRepository mphBankTempRepository;
	@Autowired
	private MphBankRepository mphBankRepository;
	@Autowired
	private MphAddressTempRepository mphAddressTempRepository;
	@Autowired
	private MphAddressRepository mphAddressRepository;
	@Autowired
	private PolicyRulesRepository policyRulesRepository;
	@Autowired
	private PolicyRulesTempRepository policyRulesTempRepository;
	@Autowired
	private PolicyRulesTempRepository rulesTempRepository;
	@Autowired
	private PolicyDetailsChangeTempRepository policyDetailsChangeTempRepository;

	@Autowired
	private PolicyServicingCommonRepository policyServicingCommonRepository;

	@Override
	public PolicyDtlsResponseDto editPolicyDetails(PolicyDto policyDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyDtlsResponseDto changeStatus(Long policyDtlsId, String status) {
		PolicyDtlsResponseDto response = new PolicyDtlsResponseDto();
		try {
			logger.info("PolicyDetailsChangeServiceImpl:changeStatus:Start");
			final Optional<policyDetailsChangeTempEntity> result = policyDtlsChangeTempRepo.findById(policyDtlsId);
			if (result.isPresent()) {
				policyDetailsChangeTempEntity dbPolicyTempEntity = result.get();
				dbPolicyTempEntity.setPolicyStatus(status);
				dbPolicyTempEntity.setModifiedOn(DateUtils.sysDate());

				dbPolicyTempEntity = policyDtlsChangeTempRepo.save(dbPolicyTempEntity);

				PolicyDetailsChangeDto policyDto = modelMapper.map(dbPolicyTempEntity, PolicyDetailsChangeDto.class);
				response.setPolicyId(dbPolicyTempEntity.getPolicyId());
				response.setPolicyDtlsDto(policyDto);
				response.setTransactionStatus(CommonConstants.SUCCESS);
				response.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyDetailsChangeServiceImpl:sendToCheker", e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyDetailsChangeServiceImpl:sendToCheker:Ends");
		}
		return response;
	}

	@Override
	public PolicyDtlsResponseDto policyApproved(Long policyDtlsId) {
		PolicyDtlsResponseDto response = new PolicyDtlsResponseDto();
		policyDetailsChangeTempEntity tempEntitys = new policyDetailsChangeTempEntity();
		MphMasterDto mphMasterDto = new MphMasterDto();
		List<PolicyAddressOldDto> dtos = new ArrayList<>();
		List<PolicyBankOldDto> bankDto = new ArrayList<>();
		List<MphAddressDto> addressDtos = new ArrayList<>();
		List<MphBankDto> mphBankDto = new ArrayList<>();
		List<PolicyRulesDto> policyRulesNewDto = new ArrayList<>();
		List<PolicyRulesOldDto> policyRulesOldDto = new ArrayList<>();
		try {
			logger.info("PolicyDetailsChangeServiceImpl:changeStatus:Start");
			Optional<policyDetailsChangeTempEntity> tempEntity = policyDtlsChangeTempRepo.findById(policyDtlsId);
			if (tempEntity.isPresent()) {
				tempEntitys = tempEntity.get();
				policyDetailsChangeEntity mainEntity = modelMapper.map(tempEntitys, policyDetailsChangeEntity.class);
				mainEntity.setPolicyDtlsId(null);
				mainEntity.setPolicyStatus(PolicyConstants.APPROVED_NO);
				mainEntity.setIsActive(true);
				mainEntity.setRules(null);
				mainEntity.setNotes(null);
				policyDetailsChangeEntity responseEntity = policyDtlsChangeRepo.save(mainEntity);
				policyDetailsChangeTempEntity tempPolicyEntity = modelMapper.map(tempEntitys,
						policyDetailsChangeTempEntity.class);
				tempPolicyEntity.setPolicyStatus(PolicyConstants.APPROVED_NO);
				tempPolicyEntity.setIsActive(true);
				policyDtlsChangeTempRepo.save(tempPolicyEntity);
				PolicyDetailsChangeDto policyDto = modelMapper.map(responseEntity, PolicyDetailsChangeDto.class);
				if (policyDto.getPolicyId() != null) {
					
					Optional<PolicyMasterTempEntity> entitys = policyMasterTempRepository.findByPolicyIdAndIsActiveTrue(policyDto.getPolicyId());
					PolicyMasterTempEntity enti =entitys.get();
					PolicyMasterEntity entity = policyMasterRepository.findByPolicyNumberAndIsActiveTrue(enti.getPolicyNumber());
					entity.setPolicyId(entity.getPolicyId());
//					PolicyMasterEntity entity = policyMasterRepository.findByPolicyId(policyDto.getPolicyId());
//					
					entity.setPolicyType(responseEntity.getPolicyType());
					entity.setStampDuty(responseEntity.getStampDuty());
					entity.setArd(responseEntity.getArd());
					entity.setNoOfCategory(responseEntity.getNoOfCategory());
					entity.setContributionFrequency(responseEntity.getContributionFrequency());
					entity.setIntermediaryOfficerCode(responseEntity.getIntermediaryOfficerCode());
					entity.setIntermediaryOfficerName(responseEntity.getIntermediaryOfficerName());
					entity.setLineOfBusiness(responseEntity.getLineOfBusiness());
					entity.setMarketingOfficerCode(responseEntity.getMarketingOfficerCode());
					entity.setMarketingOfficerName(responseEntity.getMarketingOfficerName());
					entity.setMphId(responseEntity.getMphId());
					entity.setAdjustmentDt(responseEntity.getAdjustmentDt());
					entity.setPolicyNumber(responseEntity.getPolicyNumber());
					entity.setPolicyStatus(responseEntity.getPolicyStatus());
					entity.setProductId(responseEntity.getProductId());
					entity.setProposalId(responseEntity.getProposalId());
					entity.setQuotationId(responseEntity.getQuotationId());
					entity.setLeadId(responseEntity.getLeadId());
					entity.setRejectionReasonCode(responseEntity.getRejectionReasonCode());
					entity.setRejectionRemarks(responseEntity.getRejectionRemarks());
					entity.setTotalMember(responseEntity.getTotalMember());
					entity.setUnitId(responseEntity.getUnitId());
					entity.setUnitOffice(responseEntity.getUnitOffice());
					entity.setVariant(responseEntity.getVariant());
					entity.setWorkflowStatus(responseEntity.getWorkflowStatus());
					entity.setPolicyCommencementDt(responseEntity.getPolicyCommencementDt());
					entity.setPolicyDispatchDate(responseEntity.getPolicyDispatchDate());
					entity.setPolicyRecievedDate(responseEntity.getPolicyRecievedDate());
					entity.setIsCommencementdateOneYr(responseEntity.getIsCommencementdateOneYr());
					entity.setConType(responseEntity.getConType());
					entity.setIsActive(responseEntity.getIsActive());
					entity.setCreatedBy(responseEntity.getCreatedBy());
					entity.setCreatedOn(responseEntity.getCreatedOn());
					entity.setModifiedBy(responseEntity.getModifiedBy());
					entity.setModifiedOn(responseEntity.getModifiedOn());
					entity.setAmountToBeAdjusted(responseEntity.getAmountToBeAdjusted());
					entity.setFirstPremium(responseEntity.getFirstPremium());
					entity.setSinglePremiumFirstYr(responseEntity.getSinglePremiumFirstYr());
					entity.setRenewalPremium(responseEntity.getRenewalPremium());
					entity.setSubsequentSinglePremium(responseEntity.getSubsequentSinglePremium());

					policyMasterRepository.save(entity);

					// rules
					List<PolicyRulesTempEntity> policyRulesTempEntity = policyRulesTempRepository
							.findAllByPolicyIdAndIsActiveTrue(entity.getPolicyId());
					if (policyRulesTempEntity != null) {
						// temp save
						for (PolicyRulesTempEntity entit : policyRulesTempEntity) {
							if (entit.getRuleId() != null) {
								PolicyRulesTempEntity masterTempEntity = modelMapper.map(entit,
										PolicyRulesTempEntity.class);
								masterTempEntity.setIsActive(true);
								policyRulesTempRepository.save(masterTempEntity);
								policyRulesNewDto.add(modelMapper.map(masterTempEntity, PolicyRulesDto.class));
							}
						}
						// master save
						List<PolicyRulesEntity> policyRulesEntity = convertPolicyRulesTempEntityToPolicyRulesEntity(
								policyRulesTempEntity);
						for (PolicyRulesEntity entit : policyRulesEntity) {
							if (entit.getRuleId() == null) {
								entit.setIsActive(true);
								entit.setRuleId(null);
								policyRulesRepository.save(entit);
								policyRulesNewDto.add(modelMapper.map(entit, PolicyRulesDto.class));
							}
						}
						for (PolicyRulesDto dto : policyRulesNewDto) {
							PolicyRulesOldDto oldDto = new PolicyRulesOldDto();
							oldDto = convertNewDtoToOld(dto, oldDto);
							policyRulesOldDto.add(oldDto);
						}
						response.setPolicyRulesOldDto(policyRulesOldDto);

					}

					// mph address
					List<MphAddressTempEntity> mphAddressTempEntity = mphAddressTempRepository
							.findAllByMphIdAndIsActiveTrue(entity.getMphId());
					if (mphAddressTempEntity != null) {
						// temp save
						for (MphAddressTempEntity entit : mphAddressTempEntity) {
							if (entit.getAddressId() != null) {
								MphAddressTempEntity masterTempEntity = modelMapper.map(entit,
										MphAddressTempEntity.class);
								masterTempEntity.setIsActive(true);
								mphAddressTempRepository.save(masterTempEntity);
								addressDtos.add(modelMapper.map(masterTempEntity, MphAddressDto.class));
							}
						}
						// master save
						List<MphAddressEntity> mphAddressEntitys = convertMphAddressTempEntityToMphAddressEntityNew(
								mphAddressTempEntity);
						for (MphAddressEntity entit : mphAddressEntitys) {
							if (entit.getAddressId() == null) {
								entit.setIsActive(true);
								entit.setAddressId(null);
								mphAddressRepository.save(entit);
								addressDtos.add(modelMapper.map(entit, MphAddressDto.class));
							}
						}
						for (MphAddressDto dto : addressDtos) {
							PolicyAddressOldDto oldDto = new PolicyAddressOldDto();
							oldDto = convertNewAddressDtoToOldAddress(dto, oldDto);
							dtos.add(oldDto);
						}
						response.setAddressList(dtos);

					}

					// mph bank
					List<MphBankTempEntity> mphBankTempEntity = mphBankTempRepository
							.findAllByMphIdAndIsActiveTrue(entity.getMphId());
					if (mphBankTempEntity != null) {
						// temp save
						for (MphBankTempEntity entit : mphBankTempEntity) {
							if (entit.getMphBankId() != null) {
								MphBankTempEntity masterTempEntity = modelMapper.map(entit, MphBankTempEntity.class);
								masterTempEntity.setIsActive(true);
								mphBankTempRepository.save(masterTempEntity);
								mphBankDto.add(modelMapper.map(masterTempEntity, MphBankDto.class));
							}
						}
						// master save
						List<MphBankEntity> mphBankEntitys = convertMphBankTempEntityToMphBankEntityNew(
								mphBankTempEntity);
						for (MphBankEntity entit : mphBankEntitys) {
							if (entit.getMphBankId() == null) {
								entit.setIsActive(true);
								entit.setMphBankId(null);
								mphBankRepository.save(entit);
								mphBankDto.add(modelMapper.map(entit, MphBankDto.class));
							}
						}
						for (MphBankDto dto : mphBankDto) {
							PolicyBankOldDto oldDto = new PolicyBankOldDto();
							oldDto = convertNewDtoToOld(dto, oldDto);
							bankDto.add(oldDto);
						}
						response.setBankList(bankDto);

					}

				}

				response.setPolicyDtlsDto(policyDto);
//				response.setPolicyDtlsDtos(policyDtos);
				response.setPolicyId(policyDto.getPolicyId());
				response.setTransactionStatus(CommonConstants.SUCCESS);
				response.setTransactionMessage(CommonConstants.FETCH);
			} else {
				response.setTransactionMessage(PolicyConstants.FAIL);
				response.setTransactionStatus(PolicyConstants.ERROR);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyDetailsChangeServiceImpl:sendToCheker", e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyDetailsChangeServiceImpl:sendToCheker:Ends");
		}
		return response;

	}

	@Override
	public PolicyDtlsResponseDto savePolicyDetailsChange(PolicyDetailsChangeDto policyDtlsDto) {
		PolicyDtlsResponseDto commonDto = new PolicyDtlsResponseDto();
		try {
			logger.info("PolicyServiceImpl:editPolicyDetails---Started");
			if (policyDtlsDto.getPolicyId() != null) {
				Optional<PolicyMasterTempEntity> result = policyMasterTempRepository
						.findByPolicyIdAndIsActiveTrue(policyDtlsDto.getPolicyId());
				if (!result.isPresent()) {
					logger.info("PolicyServiceImpl:savePolicyDetailsChange--NO data found");
					commonDto.setTransactionMessage(PolicyConstants.POLICY_INVALID);
					commonDto.setTransactionStatus(PolicyConstants.FAIL);
					return commonDto;
				} else if(policyDtlsDto.getPolicyDtlsId()!= null) {
					policyDetailsChangeTempEntity update = modelMapper.map(policyDtlsDto,
							policyDetailsChangeTempEntity.class);
					
					
					Optional<PolicyMasterTempEntity> entity = policyMasterTempRepository.findByPolicyIdAndIsActiveTrue(policyDtlsDto.getPolicyId());
					PolicyMasterTempEntity enti = entity.get();
					
					update.setServiceId(policyDtlsDto.getServiceId());
					update.setPolicyDtlsId(policyDtlsDto.getPolicyDtlsId());
					update.setServiceNo(policyDtlsDto.getServiceNo());
					update.setServiceStatus(policyDtlsDto.getServiceStatus());
					update.setCreatedBy(policyDtlsDto.getCreatedBy());
					update.setMphId(enti.getMphId());
					update.setMphCode(policyDtlsDto.getMphCode());
					update.setMphName(policyDtlsDto.getMphName());
//					update.setUnitId(policyDtlsDto.getUnitCode());
//					update.setUnitOffice(policyDtlsDto.getUnit());
					update.setPolicyCommencementDt(policyDtlsDto.getPolicyCommencementDate());
					update.setPolicyDispatchDate(policyDtlsDto.getPolicyDispatchDate());
					update.setPolicyRecievedDate(policyDtlsDto.getPolicyRecievedDate());
				    
				   
					update.setPolicyStatus(policyDtlsDto.getPolicyStatus());
				    policyDetailsChangeTempEntity entity3 = policyDetailsChangeTempRepository.save(update);
					PolicyDetailsChangeDto newPolicyDto = modelMapper.map(entity3, PolicyDetailsChangeDto.class);
					commonDto.setPolicyDtlsDto(newPolicyDto);
					
					commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
					commonDto.setTransactionMessage(PolicyConstants.UPDATEMESSAGE);
				}
				else {
					PolicyMasterTempEntity policyOld = result.get();
					policyDetailsChangeTempEntity policyNew = modelMapper.map(policyDtlsDto,
							policyDetailsChangeTempEntity.class);
					
//					policyDetailsChangeTempEntity s = moveTemptoPolicy(policyNew, policyOld);
//					PolicyMasterTempEntity enti =entitys.get();
					Optional<PolicyMasterTempEntity> entity = policyMasterTempRepository.findByPolicyIdAndIsActiveTrue(policyDtlsDto.getPolicyId());
					PolicyMasterTempEntity enti = entity.get();
					
					policyNew.setServiceId(policyDtlsDto.getServiceId());
					policyNew.setServiceNo(policyDtlsDto.getServiceNo());
					policyNew.setServiceStatus(policyDtlsDto.getServiceStatus());
					policyNew.setCreatedBy(policyDtlsDto.getCreatedBy());
					policyNew.setMphId(enti.getMphId());
					policyNew.setMphCode(policyDtlsDto.getMphCode());
					policyNew.setMphName(policyDtlsDto.getMphName());
					policyNew.setPolicyCommencementDt(policyDtlsDto.getPolicyCommencementDate());
					policyNew.setPolicyDispatchDate(policyDtlsDto.getPolicyDispatchDate());
					policyNew.setPolicyRecievedDate(policyDtlsDto.getPolicyRecievedDate());
//					policyNew.setUnitId(policyDtlsDto.getUnitCode());
//					policyNew.setUnitOffice(policyDtlsDto.getUnit());
					policyNew.setPolicyStatus(PolicyConstants.DRAFT_NO);
				    policyDetailsChangeTempEntity entity3 = policyDetailsChangeTempRepository.save(policyNew);
					PolicyDetailsChangeDto newPolicyDto = modelMapper.map(entity3, PolicyDetailsChangeDto.class);
					commonDto.setPolicyDtlsDto(newPolicyDto);
					
					commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
					commonDto.setTransactionMessage(PolicyConstants.SAVEMESSAGE);
				}
			}
		} catch (IllegalArgumentException e) {
			logger.error("PolicyServiceImpl:savePolicyDetailsChange---ERROR", e);
			commonDto.setTransactionMessage(PolicyConstants.FAIL);
			commonDto.setTransactionStatus(PolicyConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:savePolicyDetailsChanges--Ends");
		}
		return commonDto;
	}

	private policyDetailsChangeTempEntity moveTemptoPolicy(policyDetailsChangeTempEntity policy,
			PolicyMasterTempEntity policyOld) {

		policyDetailsChangeTempEntity temp1Entity = new policyDetailsChangeTempEntity();

		temp1Entity.setPolicyId(policyOld.getPolicyId());
//		temp1Entity.setServiceId(policyOld.gets
//		temp1Entity.setServiceNo(policyOld.get);
//		temp1Entity.setServiceStatus(policyOld.get);
		temp1Entity.setPolicyType(policyOld.getPolicyType());
		temp1Entity.setStampDuty(policyOld.getStampDuty());
		temp1Entity.setArd(policyOld.getArd());
		temp1Entity.setNoOfCategory(policyOld.getNoOfCategory());
		temp1Entity.setContributionFrequency(policyOld.getContributionFrequency());
		temp1Entity.setIntermediaryOfficerCode(policyOld.getIntermediaryOfficerCode());
		temp1Entity.setIntermediaryOfficerName(policyOld.getIntermediaryOfficerName());
		temp1Entity.setLineOfBusiness(policyOld.getLineOfBusiness());
		temp1Entity.setMarketingOfficerName(policyOld.getMarketingOfficerName());
		temp1Entity.setMarketingOfficerCode(policyOld.getMarketingOfficerCode());
		temp1Entity.setMphId(policyOld.getMphId());
		temp1Entity.setAdjustmentDt(policyOld.getAdjustmentDt());
		temp1Entity.setPolicyNumber(policyOld.getPolicyNumber());
		temp1Entity.setPolicyStatus(policyOld.getPolicyStatus());
		temp1Entity.setProductId(policyOld.getProductId());
		temp1Entity.setProposalId(policyOld.getProposalId());
		temp1Entity.setQuotationId(policyOld.getQuotationId());
		temp1Entity.setLeadId(policyOld.getLeadId());
		temp1Entity.setWorkflowStatus(policyOld.getWorkflowStatus());
		temp1Entity.setRejectionReasonCode(policyOld.getRejectionReasonCode());
		temp1Entity.setRejectionRemarks(policyOld.getRejectionRemarks());
		temp1Entity.setTotalMember(policyOld.getTotalMember());
		temp1Entity.setUnitId(policyOld.getUnitId());
		temp1Entity.setUnitOffice(policyOld.getUnitOffice());
		temp1Entity.setVariant(policyOld.getVariant());
		temp1Entity.setPolicyCommencementDt(policyOld.getPolicyCommencementDt());
		temp1Entity.setPolicyDispatchDate(policyOld.getPolicyDispatchDate());
		temp1Entity.setPolicyRecievedDate(policyOld.getPolicyRecievedDate());
		temp1Entity.setIsCommencementdateOneYr(policyOld.getIsCommencementdateOneYr());
		temp1Entity.setConType(policyOld.getConType());
		temp1Entity.setAdvanceotarrears(policyOld.getAdvanceotarrears());
		temp1Entity.setIsActive(policyOld.getIsActive());
		temp1Entity.setModifiedBy(policyOld.getModifiedBy());
		temp1Entity.setModifiedOn(policyOld.getModifiedOn());
		temp1Entity.setCreatedBy(policyOld.getCreatedBy());
		temp1Entity.setCreatedOn(policyOld.getCreatedOn());
		temp1Entity.setAmountToBeAdjusted(policyOld.getAmountToBeAdjusted());
		temp1Entity.setFirstPremium(policyOld.getFirstPremium());
		temp1Entity.setSinglePremiumFirstYr(policyOld.getSinglePremiumFirstYr());
		temp1Entity.setRenewalPremium(policyOld.getRenewalPremium());
		temp1Entity.setSubsequentSinglePremium(policyOld.getSubsequentSinglePremium());

		return temp1Entity;

	}

	@Override
	public PolicyDtlsResponseDto saveNotesDetails(PolicyServiceNotesDto policyServiceNotesDto) {
		PolicyDtlsResponseDto commonDto = new PolicyDtlsResponseDto();
		try {
			logger.info("PolicyServiceImpl:saveNotesDetails:Starts");
			PolicyServiceNotesTempEntity policyNotesTempEntity = modelMapper.map(policyServiceNotesDto,
					PolicyServiceNotesTempEntity.class);
			policyNotesTempEntity.setIsActive(true);
			PolicyServiceNotesTempEntity savePolicyNotesTempEntity = policyServiceNotesTempRepo
					.save(policyNotesTempEntity);
			commonDto.setServiceNotes(modelMapper.map(savePolicyNotesTempEntity, PolicyServiceNotesDto.class));
			commonDto.setPolicyId(savePolicyNotesTempEntity.getPolicyId());
			commonDto.setTransactionStatus(CommonConstants.SUCCESS);
			commonDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);

		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:saveNotesDetails::", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:saveNotesDetails:Ends");
		}
		return commonDto;
	}

	@Override
	public PolicyDtlsResponseDto getNoteList(Long policyId, Long serviceId) {
		PolicyDtlsResponseDto commonDto = new PolicyDtlsResponseDto();
		List<PolicyServiceNotesDto> dtos = new ArrayList<>();

		try {
			logger.info("PolicyServiceImpl:saveNotesDetails:Starts");
			List<PolicyServiceNotesTempEntity> policyEntity = policyServiceNotesTempRepo
					.findAllByPolicyIdAndIsActiveTrue(policyId);
			if (policyEntity != null) {
				for (PolicyServiceNotesTempEntity entity : policyEntity) {
					dtos.add(modelMapper.map(entity, PolicyServiceNotesDto.class));
				}
				commonDto.setServiceNotesList(dtos);
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
	public PolicyDtlsResponseDto inprogressCitrieaSearch(PolicySearchDto policySearchDto) {
		List<policyDetailsChangeTempEntity> policyTempEntities = new ArrayList<>();
		PolicyDtlsResponseDto commonDto = new PolicyDtlsResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("PolicyServiceImpl:inprogressCitrieaSearch:Start");
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<policyDetailsChangeTempEntity> createQuery = criteriaBuilder
					.createQuery(policyDetailsChangeTempEntity.class);
			Root<policyDetailsChangeTempEntity> root = createQuery.from(policyDetailsChangeTempEntity.class);

			if (StringUtils.isNotBlank(policySearchDto.getRole())
					&& Objects.equals(policySearchDto.getRole(), PolicyConstants.MAKER)) {
				predicates.add(
						root.get("policyStatus").in(Arrays.asList(PolicyConstants.DRAFT_NO, PolicyConstants.MAKER_NO)));

			}
			if (StringUtils.isNotBlank(policySearchDto.getRole())
					&& Objects.equals(policySearchDto.getRole(), PolicyConstants.CHECKER)) {
				predicates.add(root.get("policyStatus").in(Arrays.asList(PolicyConstants.CHECKER_NO)));

			}
			if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(root.get("policyNumber"), policySearchDto.getPolicyNumber()));
			}

			if (StringUtils.isNotBlank(policySearchDto.getMphCode())) {
				predicates.add(criteriaBuilder.equal(root.get("mphCode"), policySearchDto.getMphCode()));
			}

			if (StringUtils.isNotBlank(policySearchDto.getMphName())) {
				predicates.add(criteriaBuilder.equal(root.get("mphName"), policySearchDto.getMphName()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getFrom()) && StringUtils.isNotBlank(policySearchDto.getTo())) {
				Date fromDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(policySearchDto.getFrom());
				Date toDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(policySearchDto.getTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get("createdOn"), fromDate, toDate));
			}
			if (StringUtils.isNotBlank(policySearchDto.getProduct())) {
				predicates.add(criteriaBuilder.equal(root.get("product"), policySearchDto.getProduct()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getLineOfBusiness())) {
				predicates.add(criteriaBuilder.equal(root.get("lineOfBusiness"), policySearchDto.getLineOfBusiness()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getVariant())) {
				predicates.add(criteriaBuilder.equal(root.get("variant"), policySearchDto.getVariant()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getPolicyStatus())) {
				predicates.add(criteriaBuilder.equal(root.get("policyStatus"), policySearchDto.getPolicyStatus()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getUnitOffice())) {
				predicates.add(criteriaBuilder.equal(root.get("unitCode"), policySearchDto.getUnitOffice()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get("unitId"), policySearchDto.getUnitCode()));
			}

			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			policyTempEntities = entityManager.createQuery(createQuery).getResultList();
			List<PolicyDetailsChangeDto> policyDtos = mapList(policyTempEntities, PolicyDetailsChangeDto.class);
			commonDto.setPolicyDtlsDtos(policyDtos);
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

	@Override
	public PolicyDtlsResponseDto removeBankDetails(Long policyId, Long bankAccountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyDtlsResponseDto getPolicyById(String inprogress, Long policyDtlsId) {
		PolicyDtlsResponseDto commonDto = new PolicyDtlsResponseDto();
		MphMasterDto mphMasterDto = new MphMasterDto();
		try {
			logger.info("PolicyServiceImpl:getPolicyById:Start");
			Optional<?> policy = CommonConstants.EXISTING.equals(inprogress)
					? policyDtlsChangeRepo.findById(policyDtlsId)
					: policyDtlsChangeTempRepo.findById(policyDtlsId);

			if (policy.isPresent()) {
				PolicyDetailsChangeDto policyDtlsDto = modelMapper.map(policy.get(), PolicyDetailsChangeDto.class);
				commonDto.setPolicyId(policyDtlsDto.getPolicyId());
				Optional<?> mainPolicy = policyMasterTempRepository.findById(policyDtlsDto.getPolicyId());

				if (mainPolicy.isPresent()) {
					PolicyDto policyDto = modelMapper.map(mainPolicy.get(), PolicyDto.class);
//					commonDto.setPolicyDto(policyDto);
					MphMasterTempEntity mphMasterTempEntity = mphMasterTempRepository
							.findByMphIdAndIsActiveTrue(policyDto.getMphId());
					if (mphMasterTempEntity != null) {
						mphMasterDto = policyCommonServiceImpl
								.convertMphMasterTempEntityToMphMasterDto(mphMasterTempEntity);
						PolicyDto poilcDto = policyCommonServiceImpl.convertNewResponseToOldResponse(mphMasterDto);
						commonDto.setPolicyDto(poilcDto);
						;
					}
					List<PolicyServiceNotesTempEntity> entity = policyServiceNotesTempRepo
							.findAllByPolicyIdAndIsActiveTrue(policyDto.getPolicyId());
					List<PolicyServiceNotesDto> dtos = new ArrayList<>();
					for (PolicyServiceNotesTempEntity notesEntity : entity) {
						PolicyServiceNotesDto dto = modelMapper.map(notesEntity, PolicyServiceNotesDto.class);
						dtos.add(dto);
					}

					commonDto.setServiceNotesList(dtos);
					commonDto.setPolicyDtlsDto(policyDtlsDto);
					commonDto.setTransactionStatus(CommonConstants.SUCCESS);
					commonDto.setTransactionMessage(CommonConstants.FETCH);
				}

			} else {
				commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
				commonDto.setTransactionStatus(CommonConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:getPolicyById", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:getPolicyById:Ends");
		}
		return commonDto;

	}

	@Override
	public PolicyDtlsResponseDto saveRulesDetails(PolicyRulesOldDto saveRulesDetails) {
		PolicyDtlsResponseDto commonDto = new PolicyDtlsResponseDto();

		try {
			logger.info("PolicyServiceImpl:saveRulesDetails:Starts");

			PolicyRulesDto policyRulesDto = new PolicyRulesDto();
			policyRulesDto = convertOldRequestToNewRequest(saveRulesDetails, policyRulesDto);

			if (policyRulesDto.getRuleId() == null) {

				PolicyRulesTempEntity policyRulesTempEntity = modelMapper.map(policyRulesDto,
						PolicyRulesTempEntity.class);
				PolicyRulesTempEntity saveRulesTempEntity = rulesTempRepository.save(policyRulesTempEntity);
				PolicyRulesDto dto = modelMapper.map(saveRulesTempEntity, PolicyRulesDto.class);
				PolicyRulesOldDto oldDto = new PolicyRulesOldDto();
				oldDto = convertNewDtoToOld(dto, oldDto);

//				commonDto.setPolicyRulesOldDto(modelMapper.map(saveRulesTempEntity, PolicyRulesOldDto.class));
				commonDto.setPolicyId(saveRulesTempEntity.getPolicyId());
				commonDto.setPolicyRulesDto(oldDto);				
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
			} else {
				Optional<PolicyRulesTempEntity> result = rulesTempRepository.findById(policyRulesDto.getRuleId());
				PolicyRulesTempEntity policyOld = result.get();
				PolicyRulesTempEntity policyNew = modelMapper.map(policyRulesDto, PolicyRulesTempEntity.class);
				PolicyRulesTempEntity rules = new PolicyRulesTempEntity();

				rules.setRuleId(policyOld.getRuleId());
				rules.setPolicyId(policyOld.getPolicyId());
				rules.setCategory(policyNew.getCategory());
				rules.setNormalAgeRetirement(policyNew.getNormalAgeRetirement());
				rules.setMinAgeRetirement(policyNew.getMinAgeRetirement());
				rules.setMaxAgeRetirement(policyNew.getMaxAgeRetirement());
				rules.setMinServicePension(policyNew.getMinServicePension());
				rules.setMaxServicePension(policyNew.getMinServicePension());
				rules.setMinServiceWithdrawal(policyNew.getMinServiceWithdrawal());
				rules.setMaxServiceWithdrawal(policyNew.getMaxServiceWithdrawal());
				rules.setContributionType(policyNew.getContributionType());
				rules.setMinPension(policyNew.getMinPension());
				rules.setMaxPension(policyNew.getMaxPension());
				rules.setPercentageCorpus(policyNew.getPercentageCorpus());
				rules.setBenifitPayableTo(policyNew.getBenifitPayableTo());
				rules.setAnnuityOption(policyNew.getAnnuityOption());
				rules.setCommutationBy(policyNew.getCommutationBy());
				rules.setCommutationAmt(policyNew.getCommutationAmt());

				rules.setModifiedBy(policyNew.getModifiedBy());
				rules.setModifiedOn(policyNew.getModifiedOn());
				rules.setCreatedBy(policyNew.getCreatedBy());
				rules.setCreatedOn(policyNew.getCreatedOn());
				rules.setIsActive(policyNew.getIsActive());

				PolicyRulesTempEntity entity3 = rulesTempRepository.save(rules);
				PolicyRulesDto dto = modelMapper.map(entity3, PolicyRulesDto.class);
				PolicyRulesOldDto oldDto = new PolicyRulesOldDto();
				oldDto = convertNewDtoToOld(dto, oldDto);
				commonDto.setPolicyRulesDto(oldDto);
				

//				commonDto.setPolicyRulesOldDto(modelMapper.map(entity3, PolicyRulesOldDto.class));
				commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				commonDto.setTransactionMessage(PolicyConstants.UPDATEMESSAGE);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:saveRulesDetails", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:saveRulesDetails:Ends");
		}

		return commonDto;

	}

	private PolicyRulesDto convertOldRequestToNewRequest(PolicyRulesOldDto saveRulesDetails,
			PolicyRulesDto policyRulesDto) {

		policyRulesDto.setRuleId(saveRulesDetails.getRuleId());
		policyRulesDto.setPolicyId(saveRulesDetails.getPolicyId());
		policyRulesDto.setCategory(saveRulesDetails.getCategory());
		policyRulesDto.setNormalAgeRetirement(saveRulesDetails.getNormalAgeRetirement());
		policyRulesDto.setMinAgeRetirement(saveRulesDetails.getMinAgeRetirement());
		policyRulesDto.setMaxAgeRetirement(saveRulesDetails.getMaxAgeRetirement());
		policyRulesDto.setMinServicePension(saveRulesDetails.getMinServicePension());
		policyRulesDto.setMaxServicePension(saveRulesDetails.getMaxServicePension());
		policyRulesDto.setMinServiceWithdrawal(saveRulesDetails.getMinServiceWithdrawal());
		policyRulesDto.setMaxServiceWithdrawal(saveRulesDetails.getMaxServiceWithdrawal());
		policyRulesDto.setContributionType(saveRulesDetails.getContributionType());
		policyRulesDto.setMinPension(saveRulesDetails.getMinPension());
		policyRulesDto.setMaxPension(saveRulesDetails.getMaxPension());
		policyRulesDto.setBenifitPayableTo(saveRulesDetails.getBenifitPayableTo());
		policyRulesDto.setAnnuityOption(saveRulesDetails.getAnnuityOption());
		policyRulesDto.setCommutationBy(saveRulesDetails.getCommutationBy());
		policyRulesDto.setCommutationAmt(saveRulesDetails.getCommutationAmt());
		policyRulesDto.setModifiedBy(saveRulesDetails.getModifiedBy());
		policyRulesDto.setModifiedOn(saveRulesDetails.getModifiedOn());
		policyRulesDto.setCreatedBy(saveRulesDetails.getCreatedBy());
		policyRulesDto.setCreatedOn(saveRulesDetails.getCreatedOn());
		policyRulesDto.setIsActive(saveRulesDetails.getIsActive());
		policyRulesDto.setEffectiveFrom(saveRulesDetails.getEffectiveFrom());
		policyRulesDto.setPercentageCorpus(saveRulesDetails.getPercentageCorpus());

		return policyRulesDto;
	}

	@Override
	public PolicyDtlsResponseDto existingCitrieaSearch(PolicySearchDto policySearchDto) {
		List<policyDetailsChangeEntity> policyEntities = new ArrayList<>();
		PolicyDtlsResponseDto commonDto = new PolicyDtlsResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("PolicyServiceImpl:existingCitrieaSearch:Start");
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<policyDetailsChangeEntity> createQuery = criteriaBuilder
					.createQuery(policyDetailsChangeEntity.class);
			Root<policyDetailsChangeEntity> root = createQuery.from(policyDetailsChangeEntity.class);
			if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyNumber")),
						policySearchDto.getPolicyNumber().toLowerCase()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getMphCode())) {
				predicates.add(criteriaBuilder.equal(root.get("mphCode"), policySearchDto.getMphCode()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getMphName())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("mphName")),
						policySearchDto.getMphName().toLowerCase()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getFrom()) && StringUtils.isNotBlank(policySearchDto.getTo())) {
				Date fromDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(policySearchDto.getFrom());
				Date toDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(policySearchDto.getTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get("createdOn"), fromDate, toDate));
			}
			if (StringUtils.isNotBlank(policySearchDto.getProduct())) {
				predicates.add(criteriaBuilder.equal(root.get("product"), policySearchDto.getProduct()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getLineOfBusiness())) {
				predicates.add(criteriaBuilder.equal(root.get("lineOfBusiness"), policySearchDto.getLineOfBusiness()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getVariant())) {
				predicates.add(criteriaBuilder.equal(root.get("variant"), policySearchDto.getVariant()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getPolicyStatus())) {
				predicates.add(criteriaBuilder.equal(root.get("policyStatus"), policySearchDto.getPolicyStatus()));
			}
			if (StringUtils.isNotBlank(policySearchDto.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get("unitId"), policySearchDto.getUnitCode()));
			}

			createQuery.orderBy(criteriaBuilder.desc(root.get("modifiedOn")));

			predicates.add(root.get("policyStatus")
					.in(Arrays.asList(PolicyConstants.APPROVED_NO, PolicyConstants.REJECTED_NO)));

			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			policyEntities = entityManager.createQuery(createQuery).getResultList();
			List<PolicyDetailsChangeDto> policyDtos = mapList(policyEntities, PolicyDetailsChangeDto.class);

			commonDto.setPolicyDtlsDtos(policyDtos);
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

	@Override
	public PolicyResponseDto newcitrieaSearch(PolicySearchDto policySearchDto) {
		PolicyResponseDto commonDto = new PolicyResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("PolicyServiceImpl:inprogressCitrieaSearch:Start");
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

//			if (StringUtils.isNotBlank(policySearchDto.getUnitCode())) {
//				join.on(criteriaBuilder.equal(join.get("unitId"), policySearchDto.getUnitCode()));
//			}
			if (StringUtils.isNotBlank(policySearchDto.getUnitCode())) {
//				join.on(criteriaBuilder.equal(join.get("unitId"), policySearchDto.getUnitCode()));
				predicates.add(criteriaBuilder.equal(join.get("unitId"), policySearchDto.getUnitCode()));
			}

			predicates.add(join.get(PolicyConstants.POLICYSTATUS).in(Arrays.asList(PolicyConstants.APPROVED_NO)));

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
//			response.forEach(policyDto -> policyDto.getDeposit()
//					.removeIf(deposit -> PolicyConstants.ADJESTED.equalsIgnoreCase(deposit.getStatus())));

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

//			commonDto.setPolicySearchResponse(response);
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

	@Override
	public PolicyDtlsResponseDto sendToReject(Long policyDtlsId, String rejectedNo, String rejectionRemarks,
			String rejectionCode) {
		
		PolicyDtlsResponseDto response = new PolicyDtlsResponseDto();
		policyDetailsChangeTempEntity tempEntitys = new policyDetailsChangeTempEntity();
		try {
			logger.info("PolicyDetailsChangeServiceImpl:changeStatus:Start");
			Optional<policyDetailsChangeTempEntity> tempEntity = policyDtlsChangeTempRepo.findById(policyDtlsId);
			if (tempEntity.isPresent()) {
				tempEntitys = tempEntity.get();
//				policyDetailsChangeEntity mainEntity = modelMapper.map(tempEntitys, policyDetailsChangeEntity.class);
				policyDetailsChangeEntity mainEntity = new policyDetailsChangeEntity();
				
				mainEntity.setPolicyId(tempEntitys.getPolicyId());
				mainEntity.setPolicyType(tempEntitys.getPolicyType());
				mainEntity.setStampDuty(tempEntitys.getStampDuty());
				mainEntity.setArd(tempEntitys.getArd());
				mainEntity.setNoOfCategory(tempEntitys.getNoOfCategory());
				mainEntity.setContributionFrequency(tempEntitys.getContributionFrequency());
				mainEntity.setIntermediaryOfficerCode(tempEntitys.getIntermediaryOfficerCode());
				mainEntity.setIntermediaryOfficerName(tempEntitys.getIntermediaryOfficerName());
				mainEntity.setLineOfBusiness(tempEntitys.getLineOfBusiness());
				mainEntity.setMarketingOfficerName(tempEntitys.getMarketingOfficerName());
				mainEntity.setMarketingOfficerCode(tempEntitys.getMarketingOfficerCode());
				mainEntity.setMphId(tempEntitys.getMphId());
				mainEntity.setAdjustmentDt(tempEntitys.getAdjustmentDt());
				mainEntity.setPolicyNumber(tempEntitys.getPolicyNumber());
				mainEntity.setPolicyStatus(tempEntitys.getPolicyStatus());
				mainEntity.setProductId(tempEntitys.getProductId());
				mainEntity.setProposalId(tempEntitys.getProposalId());
				mainEntity.setQuotationId(tempEntitys.getQuotationId());
				mainEntity.setLeadId(tempEntitys.getLeadId());
				mainEntity.setWorkflowStatus(tempEntitys.getWorkflowStatus());
				mainEntity.setRejectionReasonCode(tempEntitys.getRejectionReasonCode());
				mainEntity.setRejectionRemarks(tempEntitys.getRejectionRemarks());
				mainEntity.setTotalMember(tempEntitys.getTotalMember());
				mainEntity.setUnitId(tempEntitys.getUnitId());
				mainEntity.setUnitOffice(tempEntitys.getUnitOffice());
				mainEntity.setVariant(tempEntitys.getVariant());
				mainEntity.setPolicyCommencementDt(tempEntitys.getPolicyCommencementDt());
				mainEntity.setPolicyDispatchDate(tempEntitys.getPolicyDispatchDate());
				mainEntity.setPolicyRecievedDate(tempEntitys.getPolicyRecievedDate());
				mainEntity.setIsCommencementdateOneYr(tempEntitys.getIsCommencementdateOneYr());
				mainEntity.setConType(tempEntitys.getConType());
				mainEntity.setAdvanceotarrears(tempEntitys.getAdvanceotarrears());
				mainEntity.setIsActive(tempEntitys.getIsActive());
				mainEntity.setModifiedBy(tempEntitys.getModifiedBy());
				mainEntity.setModifiedOn(tempEntitys.getModifiedOn());
				mainEntity.setCreatedBy(tempEntitys.getCreatedBy());
				mainEntity.setCreatedOn(tempEntitys.getCreatedOn());
				mainEntity.setAmountToBeAdjusted(tempEntitys.getAmountToBeAdjusted());
				mainEntity.setFirstPremium(tempEntitys.getFirstPremium());
				mainEntity.setSinglePremiumFirstYr(tempEntitys.getSinglePremiumFirstYr());
				mainEntity.setRenewalPremium(tempEntitys.getRenewalPremium());
				mainEntity.setSubsequentSinglePremium(tempEntitys.getSubsequentSinglePremium());
				
				mainEntity.setPolicyDtlsId(null);
				mainEntity.setIsActive(false);
				mainEntity.setPolicyStatus(PolicyConstants.REJECTED_NO);
				mainEntity.setRejectionReasonCode(rejectionCode);
				mainEntity.setRejectionRemarks(rejectionRemarks);
				policyDetailsChangeEntity responseEntity = policyDtlsChangeRepo.save(mainEntity);
				policyDetailsChangeTempEntity tempPolicyEntity = modelMapper.map(tempEntitys,
						policyDetailsChangeTempEntity.class);
				tempPolicyEntity.setPolicyStatus(PolicyConstants.REJECTED_NO);
				tempPolicyEntity.setIsActive(false);
				tempPolicyEntity.setRejectionReasonCode(rejectionCode);
				tempPolicyEntity.setRejectionRemarks(rejectionRemarks);
				policyDtlsChangeTempRepo.save(tempPolicyEntity);
				PolicyDetailsChangeDto policyDto = modelMapper.map(responseEntity, PolicyDetailsChangeDto.class);
				response.setPolicyDtlsDto(policyDto);
				response.setPolicyId(policyDto.getPolicyId());
				response.setTransactionStatus(CommonConstants.SUCCESS);
				response.setTransactionMessage(CommonConstants.FETCH);
			} else {
				response.setTransactionMessage(PolicyConstants.FAIL);
				response.setTransactionStatus(PolicyConstants.ERROR);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyDetailsChangeServiceImpl:sendToCheker", e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyDetailsChangeServiceImpl:sendToCheker:Ends");
		}
		return response;
	
	}

	@Override
	public PolicyDtlsResponseDto newcitrieaSearchById(String policyNumber) {
		PolicyDtlsResponseDto commonDto = new PolicyDtlsResponseDto();
		MphMasterDto mphMasterDto = new MphMasterDto();

		try {
			logger.info("PolicyServiceImpl:getPolicyById:Start");

//			policyNumber 
			PolicyMasterTempEntity policyMasterTempEntity = policyMasterTempRepository
					.findByPolicyNumberAndIsActiveTrue(policyNumber);

			MphMasterTempEntity mphMasterTempEntity = mphMasterTempRepository
					.findByMphIdAndIsActiveTrue(policyMasterTempEntity.getMphId());
			if (mphMasterTempEntity != null) {

				mphMasterDto = policyCommonServiceImpl.convertMphMasterTempEntityToMphMasterDto(mphMasterTempEntity);

				PolicyDto poilcDto = policyCommonServiceImpl.convertNewResponseToOldResponse(mphMasterDto);
				poilcDto.getDeposit()
						.removeIf(deposit -> PolicyConstants.ADJESTED.equalsIgnoreCase(deposit.getStatus()));
				poilcDto.getMembers().removeIf(member -> Boolean.TRUE.equals(member.getIsZeroId()));
				poilcDto.getAdjustments()
						.removeIf(deposits -> PolicyConstants.DEPOSITSTATUSNEW.equalsIgnoreCase(deposits.getStatus()));

				commonDto.setPolicyDto(poilcDto);
				commonDto.setPolicyId(commonDto.getPolicyDto().getPolicyId());
				commonDto.setMphId(commonDto.getPolicyDto().getMphId());
				commonDto.setTransactionMessage(PolicyConstants.FETCH);
				commonDto.setTransactionStatus(PolicyConstants.SUCCESS);

			} else {
				commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
				commonDto.setTransactionStatus(CommonConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:getPolicyById", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:getPolicyById:Ends");
		}
		return commonDto;
	}


	@Override
	public PolicyDtlsResponseDto saveAddressDetails(PolicyAddressOldDto addressDto) {
		PolicyDtlsResponseDto commonDto = new PolicyDtlsResponseDto();

		try {
			logger.info("PolicyServiceImpl:saveAddressDetails:Starts");
			MphAddressDto mphAddressNewDto = new MphAddressDto();
			mphAddressNewDto = convertOldAddressToMphAddressDto(addressDto, mphAddressNewDto);

			if (mphAddressNewDto.getAddressId() == null) {

				MphAddressTempEntity policyAddressTempEntity = modelMapper.map(mphAddressNewDto,
						MphAddressTempEntity.class);
				policyAddressTempEntity.setIsActive(true);
				MphAddressTempEntity savePolicyAddressTempEntity = mphAddressTempRepository
						.save(policyAddressTempEntity);
				MphAddressDto dto = modelMapper.map(savePolicyAddressTempEntity, MphAddressDto.class);
				PolicyAddressOldDto oldDto = new PolicyAddressOldDto();
				oldDto = convertNewAddressDtoToOldAddress(dto, oldDto);
				commonDto.setPolicyAddressDto(oldDto);
				commonDto.setMphId(oldDto.getMphId());
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
			} else {
				Optional<MphAddressTempEntity> result = mphAddressTempRepository
						.findById(mphAddressNewDto.getAddressId());
				MphAddressTempEntity policyOld = result.get();
				MphAddressTempEntity policyNew = modelMapper.map(mphAddressNewDto, MphAddressTempEntity.class);
				policyNew.setIsActive(true);
				policyNew.setMphId(mphAddressNewDto.getMphId());
				MphAddressTempEntity updateEntity = mphAddressTempRepository.save(policyNew);
				MphAddressDto dto = modelMapper.map(updateEntity, MphAddressDto.class);
				PolicyAddressOldDto oldDto = new PolicyAddressOldDto();
				oldDto = convertNewAddressDtoToOldAddress(dto, oldDto);
				commonDto.setPolicyAddressDto(oldDto);

				commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				commonDto.setTransactionMessage(PolicyConstants.UPDATEMESSAGE);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:saveAddressDetails", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:saveAddressDetails:Ends");
		}
		return commonDto;
	}

	@Override
	public PolicyDtlsResponseDto saveBankDetails(PolicyBankOldDto policyBankDto) {
		PolicyDtlsResponseDto commonDto = new PolicyDtlsResponseDto();
		try {
			logger.info("PolicyServiceImpl:saveBankDetails:Starts");
			MphBankDto bankDto = new MphBankDto();
			bankDto = convertPolicyBankToMphBankDto(policyBankDto, bankDto);

			if (bankDto.getMphBankId() == null) {

				MphBankTempEntity mphBankTempEntity = modelMapper.map(bankDto, MphBankTempEntity.class);
				mphBankTempEntity.setIsActive(true);
				MphBankTempEntity saveEntity = mphBankTempRepository.save(mphBankTempEntity);
				MphBankDto dto = modelMapper.map(saveEntity, MphBankDto.class);
				PolicyBankOldDto oldDto = new PolicyBankOldDto();
				oldDto = convertNewDtoToOld(dto, oldDto);
				commonDto.setPolicyBankDto(oldDto);
				commonDto.setMphId(oldDto.getMphId());
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
			} else {
				Optional<MphBankTempEntity> result = mphBankTempRepository.findById(bankDto.getMphBankId());
				MphBankTempEntity policyOld = result.get();
				MphBankTempEntity policyNew = modelMapper.map(bankDto, MphBankTempEntity.class);

				MphBankTempEntity banks = new MphBankTempEntity();

				banks.setMphBankId(policyOld.getMphBankId());
				banks.setMphId(policyOld.getMphId());
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
				banks.setIsActive(true);

//				banks.setIsMapped(policyNew.getIsMapped());

				banks.setModifiedBy(policyNew.getModifiedBy());
				banks.setModifiedOn(policyNew.getModifiedOn());
				banks.setCreatedBy(policyNew.getCreatedBy());
				banks.setCreatedOn(policyNew.getCreatedOn());

				MphBankTempEntity updateEntity = mphBankTempRepository.save(banks);
				MphBankDto dto = modelMapper.map(updateEntity, MphBankDto.class);
				PolicyBankOldDto oldDto = new PolicyBankOldDto();
				oldDto = convertNewDtoToOld(dto, oldDto);
				commonDto.setPolicyBankDto(oldDto);
				commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				commonDto.setTransactionMessage(PolicyConstants.UPDATEMESSAGE);
			}

		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:saveBankDetails", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:saveBankDetails:Ends");
		}
		return commonDto;

	}

	@Override
	public PolicyDtlsResponseDto getBankList(Long mphId) {
		PolicyDtlsResponseDto commonDto = new PolicyDtlsResponseDto();
		List<PolicyBankOldDto> dtos = new ArrayList<>();
		List<MphBankDto> bankDtos = new ArrayList<>();
		try {
			logger.info("PolicyServiceImpl:getBankList:Starts");
			List<MphBankTempEntity> mphBankTempEntity = mphBankTempRepository.findAllByMphIdAndIsActiveTrue(mphId);
			if (mphBankTempEntity != null) {
				for (MphBankTempEntity entity : mphBankTempEntity) {
					bankDtos.add(modelMapper.map(entity, MphBankDto.class));
				}
				for (MphBankDto dto : bankDtos) {
					PolicyBankOldDto oldDto = new PolicyBankOldDto();
					oldDto = convertNewDtoToOld(dto, oldDto);
					dtos.add(oldDto);
				}
				commonDto.setBankList(dtos);
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonDto.setTransactionStatus(CommonConstants.ERROR);
				commonDto.setTransactionMessage(CommonConstants.DENY);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:getBankList", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:getBankList:Ends");
		}
		return commonDto;
	}

	@Override
	public PolicyDtlsResponseDto getAddressList(Long mphId) {
		PolicyDtlsResponseDto commonDto = new PolicyDtlsResponseDto();
		List<PolicyAddressOldDto> dtos = new ArrayList<>();
		List<MphAddressDto> bankDtos = new ArrayList<>();
		try {
			logger.info("PolicyServiceImpl:getAddressList:Starts");
			List<MphAddressTempEntity> mphAddressTempEntity = mphAddressTempRepository
					.findAllByMphIdAndIsActiveTrue(mphId);
			if (mphAddressTempEntity != null) {
				for (MphAddressTempEntity entity : mphAddressTempEntity) {
					bankDtos.add(modelMapper.map(entity, MphAddressDto.class));
				}
				for (MphAddressDto dto : bankDtos) {
					PolicyAddressOldDto oldDto = new PolicyAddressOldDto();
					oldDto = convertNewAddressDtoToOldAddress(dto, oldDto);
					dtos.add(oldDto);
				}
				commonDto.setAddressList(dtos);
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonDto.setTransactionStatus(CommonConstants.ERROR);
				commonDto.setTransactionMessage(CommonConstants.DENY);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:getAddressList", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("PolicyServiceImpl:getAddressList:Ends");
		}
		return commonDto;
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

	private MphAddressDto convertOldAddressToMphAddressDto(PolicyAddressOldDto addressDto,
			MphAddressDto mphAddressNewDto) {
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

	private PolicyAddressOldDto convertNewAddressDtoToOldAddress(MphAddressDto mphAddressNewDto,
			PolicyAddressOldDto addressDto) {

		addressDto.setAddressId(mphAddressNewDto.getAddressId());
		addressDto.setAddressLine1(mphAddressNewDto.getAddressLine1());
		addressDto.setAddressLine2(mphAddressNewDto.getAddressLine2());
		addressDto.setAddressLine3(mphAddressNewDto.getAddressLine3());
		addressDto.setAddressType(mphAddressNewDto.getAddressType());
		addressDto.setCityId(mphAddressNewDto.getCityId());
		addressDto.setDistrictId(mphAddressNewDto.getDistrictId());
		addressDto.setCountryId(mphAddressNewDto.getCountryId());
		addressDto.setStateId(mphAddressNewDto.getStateId());
		addressDto.setPinCode(NumericUtils.convertIntegerToString(mphAddressNewDto.getPincode()));
		addressDto.setTownLocality(mphAddressNewDto.getCityLocality());
		addressDto.setCreatedBy(mphAddressNewDto.getCreatedBy());
		addressDto.setCreatedOn(mphAddressNewDto.getCreatedOn());
		addressDto.setModifiedBy(mphAddressNewDto.getModifiedBy());
		addressDto.setModifiedOn(mphAddressNewDto.getModifiedOn());
		addressDto.setMphId(mphAddressNewDto.getMphId());
		return addressDto;

	}

	public List<MphAddressEntity> convertMphAddressTempEntityToMphAddressEntityNew(
			List<MphAddressTempEntity> mphAddressTempEntitys) {
		List<MphAddressEntity> mphAddressEntity = new ArrayList<>();

		for (MphAddressTempEntity mphAddressDto : mphAddressTempEntitys) {
			MphAddressEntity mphAddressTempEntity = new MphAddressEntity();
			mphAddressTempEntity.setAddressId(null);
			mphAddressTempEntity.setMphId(mphAddressDto.getMphId());
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
			mphAddressEntity.add(mphAddressTempEntity);
		}

		return mphAddressEntity;
	}

	public List<MphBankEntity> convertMphBankTempEntityToMphBankEntityNew(List<MphBankTempEntity> mphBankTempEntitys) {
		List<MphBankEntity> mphAddressEntitys = new ArrayList<>();

		for (MphBankTempEntity mphBankDto : mphBankTempEntitys) {
			MphBankEntity mphBankTempEntity = new MphBankEntity();
			mphBankTempEntity.setMphBankId(null);
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
			mphAddressEntitys.add(mphBankTempEntity);
		}
		return mphAddressEntitys;
	}

	public List<PolicyRulesEntity> convertPolicyRulesTempEntityToPolicyRulesEntity(
			List<PolicyRulesTempEntity> policyRulesTempEntity) {
		List<PolicyRulesEntity> policyRulesEntity = new ArrayList<>();

		for (PolicyRulesTempEntity policyRulesDto : policyRulesTempEntity) {
			PolicyRulesEntity policyAnnuityTempEntity = new PolicyRulesEntity();
			policyAnnuityTempEntity.setRuleId(null);
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
			policyRulesEntity.add(policyAnnuityTempEntity);
		}
		return policyRulesEntity;
	}

	private PolicyRulesOldDto convertNewDtoToOld(PolicyRulesDto dto, PolicyRulesOldDto oldDto) {

		oldDto.setRuleId(dto.getPolicyId());
		oldDto.setPolicyId(dto.getPolicyId());
		oldDto.setCategory(dto.getCategory());
		oldDto.setNormalAgeRetirement(dto.getNormalAgeRetirement());
		oldDto.setMinAgeRetirement(dto.getMinAgeRetirement());
		oldDto.setMaxAgeRetirement(dto.getMaxAgeRetirement());
		oldDto.setMinServicePension(dto.getMinServicePension());
		oldDto.setMaxServicePension(dto.getMaxServicePension());
		oldDto.setMinServiceWithdrawal(dto.getMinServiceWithdrawal());
		oldDto.setMaxServiceWithdrawal(dto.getMaxServiceWithdrawal());
		oldDto.setContributionType(dto.getContributionType());
		oldDto.setMinPension(dto.getMinPension());
		oldDto.setMaxPension(dto.getMaxPension());
		oldDto.setBenifitPayableTo(dto.getBenifitPayableTo());
		oldDto.setAnnuityOption(dto.getAnnuityOption());
		oldDto.setCommutationBy(dto.getCommutationBy());
		oldDto.setCommutationAmt(dto.getCommutationAmt());
		oldDto.setModifiedBy(dto.getModifiedBy());
		oldDto.setModifiedOn(dto.getModifiedOn());
		oldDto.setCreatedBy(dto.getCreatedBy());
		oldDto.setCreatedOn(dto.getCreatedOn());
		oldDto.setIsActive(dto.getIsActive());
		oldDto.setEffectiveFrom(dto.getEffectiveFrom());
		oldDto.setPercentageCorpus(dto.getPercentageCorpus());

		return oldDto;
	}

	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
	}

}