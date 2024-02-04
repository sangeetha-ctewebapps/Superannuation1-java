/**
 * 
 */
package com.lic.epgs.policyservicing.policylvl.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.policy.dto.PolicySearchDto;
import com.lic.epgs.policy.old.dto.PolicyResponseDto;
import com.lic.epgs.policyservicing.common.constants.PolicyServicingCommonConstants;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceCommonResponseDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceDocumentDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceDto;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceDocumentEntity;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceDocumentTempEntity;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesEntity;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesTempEntity;
import com.lic.epgs.policyservicing.common.repository.PolicyServiceDocumentTempRepository;
import com.lic.epgs.policyservicing.common.repository.PolicyServiceNotesTempRepository;
import com.lic.epgs.policyservicing.common.service.PolicyServicingCommonService;
import com.lic.epgs.policyservicing.policylvl.constants.FreeLookCancellationConstants;
import com.lic.epgs.policyservicing.policylvl.dto.freelookcancellation.FreeLookCancellationDto;
import com.lic.epgs.policyservicing.policylvl.dto.freelookcancellation.FreeLookCancellationResponseDto;
import com.lic.epgs.policyservicing.policylvl.dto.freelookcancellation.FreeLookCancellationSearchDto;
import com.lic.epgs.policyservicing.policylvl.entity.freelookcancellation.FreeLookCancellationEntity;
import com.lic.epgs.policyservicing.policylvl.entity.freelookcancellation.FreeLookCancellationTempEntity;
import com.lic.epgs.policyservicing.policylvl.repository.freelookcancellation.FreeLookCancellationRepository;
import com.lic.epgs.policyservicing.policylvl.repository.freelookcancellation.FreeLookCancellationTempRepository;
import com.lic.epgs.policyservicing.policylvl.service.PolicyFreeLookCancelService;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.DateUtils;

/**
 * @author Muruganandam
 *
 */

@Service
public class PolicyFreeLookCancelServiceImpl implements PolicyFreeLookCancelService {
	

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private EntityManager entityManager;
//	@Autowired
//	private PolicyRepository policyRepository;
	@Autowired
	private FreeLookCancellationTempRepository freeLookCancellationTempRepo;
	@Autowired
	private PolicyServiceDocumentTempRepository freeLookCancellationDocumentTempRepo;
	@Autowired
	private PolicyServiceNotesTempRepository freeLookCancellationNotesTempRep;
	@Autowired
	private FreeLookCancellationRepository freeLookCancellationRepo;
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private PolicyServicingCommonService policyServicingCommonService;

	public synchronized String getFLCPolicySeq() {
		return commonService.getSequence(CommonConstants.FLC_MODULE);
	}
	
	
	@Override
	public PolicyServiceCommonResponseDto saveFreeLookCancellationDetails(PolicyServiceDto policyServiceDto ) {
		PolicyServiceCommonResponseDto commonDto = new PolicyServiceCommonResponseDto();
//		PolicyServiceDto serviceDto = new PolicyServiceDto();
		try {
			logger.info("FreeLookCancellationServiceImpl:savePolicyDetails---Started");

			PolicyServiceCommonResponseDto commonDtoo =	policyServicingCommonService.startService(policyServiceDto,PolicyServicingCommonConstants.FREELOOK_CANCEL_TYPE);
			
		
//			
//			FreeLookCancellationTempEntity flcTemp = modelMapper.map(policyServiceDto, FreeLookCancellationTempEntity.class);
//
//			flcTemp.setPolicyCancellationRequestNumber(
//					StringUtils.isNoneEmpty(flcTemp.getPolicyCancellationRequestNumber())
//							? flcTemp.getPolicyCancellationRequestNumber()
//							: null);
//			flcTemp.setFreeLookStatus(FreeLookCancellationConstants.DRAFT_NO);
//			flcTemp.setIsActive(true);
//
//			FreeLookCancellationTempEntity flcNew = freeLookCancellationTempRepo.save(flcTemp);
//			
//			FreeLookCancellationDto FlcDtol= modelMapper.map(flcNew, FreeLookCancellationDto.class);
//
//			serviceDto.setFlcTemp(FlcDtol);
//			
//			commonDto.setFreeLookId(flcNew.getFreeLookId());
			
//			commonDto.setPolicyServiceDto(policyServicingCommonService.startService(policyServiceDto,PolicyServicingCommonConstants.FREELOOK_CANCEL_TYPE));
			
			commonDto.setPolicyServiceDto(commonDtoo.getPolicyServiceDto());;
			commonDto.setTransactionStatus(FreeLookCancellationConstants.SUCCESS);
			commonDto.setTransactionMessage(FreeLookCancellationConstants.SAVEMESSAGE);
		} catch (IllegalArgumentException e) {
			logger.error("FreeLookCancellationServiceImpl:savePolicyDetails--Exception--", e);
			commonDto.setTransactionMessage(FreeLookCancellationConstants.ERROR);
			commonDto.setTransactionStatus(FreeLookCancellationConstants.FAIL);
		} finally {
			logger.info("FreeLookCancellationServiceImpl:savePolicyDetails---Ended");
		}
		return commonDto;
	}

//	@Override
//	public FreeLookCancellationResponseDto saveFreeLookCancellationDetails(FreeLookCancellationDto flcDto) {
//		FreeLookCancellationResponseDto commonDto = new FreeLookCancellationResponseDto();
//		try {
//			logger.info("FreeLookCancellationServiceImpl:savePolicyDetails---Started");
//
//			FreeLookCancellationTempEntity flcTemp = modelMapper.map(flcDto, FreeLookCancellationTempEntity.class);
//
//			flcTemp.setPolicyCancellationRequestNumber(
//					StringUtils.isNoneEmpty(flcTemp.getPolicyCancellationRequestNumber())
//							? flcTemp.getPolicyCancellationRequestNumber()
//							: RandomStringUtils.random(8, true, true));
//			flcTemp.setFreeLookStatus(FreeLookCancellationConstants.DRAFT_NO);
//			flcTemp.setIsActive(true);
//
//			FreeLookCancellationTempEntity flcNew = freeLookCancellationTempRepo.save(flcTemp);
//
//			commonDto.setFreeLookCancellationDto(modelMapper.map(flcNew, FreeLookCancellationDto.class));
//			commonDto.setFreeLookId(flcNew.getFreeLookId());
//			commonDto.setTransactionStatus(FreeLookCancellationConstants.SUCCESS);
//			commonDto.setTransactionMessage(FreeLookCancellationConstants.SAVEMESSAGE);
//		} catch (IllegalArgumentException e) {
//			logger.error("FreeLookCancellationServiceImpl:savePolicyDetails--Exception--", e);
//			commonDto.setTransactionMessage(FreeLookCancellationConstants.ERROR);
//			commonDto.setTransactionStatus(FreeLookCancellationConstants.FAIL);
//		} finally {
//			logger.info("FreeLookCancellationServiceImpl:savePolicyDetails---Ended");
//		}
//		return commonDto;
//	}

	@Override
	public FreeLookCancellationResponseDto getFreeLookCancellationDetails(Long policyId) {
		FreeLookCancellationResponseDto commonDto = new FreeLookCancellationResponseDto();
		try {
			logger.info("FreeLookCancellationServiceImpl:getFreeLookCancellationDetails---Started");
			Optional<FreeLookCancellationTempEntity> result = freeLookCancellationTempRepo.findById(policyId);
			if (result.isPresent()) {
				FreeLookCancellationTempEntity policy = result.get();
				FreeLookCancellationDto policyDto = modelMapper.map(policy, FreeLookCancellationDto.class);
				commonDto.setFreeLookCancellationDto(policyDto);
				commonDto.setFreeLookId(policyDto.getFreeLookId());
				commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				commonDto.setTransactionMessage(PolicyConstants.FETCH);
			} else {
				commonDto.setFreeLookCancellationDto(null);
				commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				commonDto.setTransactionMessage(PolicyConstants.LISTTRUSTCONTACTDETAILSERROR);
				logger.info("FreeLookCancellationServiceImpl:getFreeLookCancellationDetails--NO data found");
			}
		} catch (IllegalArgumentException e) {
			logger.info("FreeLookCancellationServiceImpl:getFreeLookCancellationDetails--ERROR--", e);
			commonDto.setTransactionMessage(PolicyConstants.ERROR);
			commonDto.setTransactionStatus(PolicyConstants.FAIL);

		} finally {
			logger.info("FreeLookCancellationServiceImpl:getFreeLookCancellationDetails--Ends");
		}
		return commonDto;
	}

	@Override
	public FreeLookCancellationResponseDto getFreeLookCancellationById(String status, Long policyId) {
		FreeLookCancellationResponseDto commonDto = new FreeLookCancellationResponseDto();
		try {
			logger.info("FreeLookCancellationServiceImpl:getFreeLookCancellationById:Start");
			Optional<?> policy = CommonConstants.EXISTING.equals(status)
					? freeLookCancellationRepo.findById(policyId)
					: freeLookCancellationTempRepo.findById(policyId);
			if (policy.isPresent()) {
				FreeLookCancellationDto policyDto = modelMapper.map(policy.get(), FreeLookCancellationDto.class);
				commonDto.setFreeLookId(policyDto.getFreeLookId());
				commonDto.setFreeLookCancellationDto(policyDto);
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
				commonDto.setTransactionStatus(CommonConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:FreeLookCancellationServiceImpl:getFreeLookCancellationById", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("FreeLookCancellationServiceImpl:getFreeLookCancellationById:Ends");
		}
		return commonDto;
	}

	@Override
	public FreeLookCancellationResponseDto existingCitrieaSearch(
			FreeLookCancellationSearchDto freeLookCancellationSearchDto) {
		List<FreeLookCancellationEntity> policyEntities = new ArrayList<>();
		FreeLookCancellationResponseDto commonDto = new FreeLookCancellationResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("PolicyServiceImpl:existingCitrieaSearch:Start");
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<FreeLookCancellationEntity> createQuery = criteriaBuilder
					.createQuery(FreeLookCancellationEntity.class);
			Root<FreeLookCancellationEntity> root = createQuery.from(FreeLookCancellationEntity.class);

			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(root.get("policyNumber"),
						freeLookCancellationSearchDto.getPolicyNumber()));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getMphCode())) {
				predicates.add(criteriaBuilder.equal(root.get("mphCode"), freeLookCancellationSearchDto.getMphCode()));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getMphName())) {
				predicates.add(criteriaBuilder.equal(root.get("mphName"), freeLookCancellationSearchDto.getMphName()));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getFrom())
					&& StringUtils.isNotBlank(freeLookCancellationSearchDto.getTo())) {
				Date fromDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(freeLookCancellationSearchDto.getFrom());
				Date toDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(freeLookCancellationSearchDto.getTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get("createdOn"), fromDate, toDate));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getProduct())) {
				predicates.add(criteriaBuilder.equal(root.get("product"), freeLookCancellationSearchDto.getProduct()));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getLineOfBusiness())) {
				predicates.add(criteriaBuilder.equal(root.get("lineOfBusiness"),
						freeLookCancellationSearchDto.getLineOfBusiness()));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getFreeLookStatus())) {
				predicates.add(criteriaBuilder.equal(root.get("freeLookStatus"),
						freeLookCancellationSearchDto.getFreeLookStatus()));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getUnitCode())) {
				predicates
						.add(criteriaBuilder.equal(root.get("unitCode"), freeLookCancellationSearchDto.getUnitCode()));
			}
			
			predicates.add(criteriaBuilder.not(root.get("freeLookStatus").in(Arrays.asList(PolicyConstants.DRAFT_NO,
					PolicyConstants.MAKER_NO, PolicyConstants.CHECKER_NO, PolicyConstants.REOPEN_NO))));

			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			policyEntities = entityManager.createQuery(createQuery).getResultList();
			List<FreeLookCancellationDto> policyDtos = mapList(policyEntities, FreeLookCancellationDto.class);

			commonDto.setFreeLookCancellationDtos(policyDtos);
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
	public FreeLookCancellationResponseDto inprogressCitrieaSearch(
			FreeLookCancellationSearchDto freeLookCancellationSearchDto) {
		List<FreeLookCancellationTempEntity> policyTempEntities = new ArrayList<>();
		FreeLookCancellationResponseDto commonDto = new FreeLookCancellationResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("PolicyServiceImpl:inprogressCitrieaSearch:Start");
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<FreeLookCancellationTempEntity> createQuery = criteriaBuilder
					.createQuery(FreeLookCancellationTempEntity.class);
			Root<FreeLookCancellationTempEntity> root = createQuery.from(FreeLookCancellationTempEntity.class);

			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getRole())
					&& Objects.equals(freeLookCancellationSearchDto.getRole(), PolicyConstants.MAKER)) {
				predicates.add(
						criteriaBuilder.not(root.get("freeLookStatus").in(Arrays.asList(PolicyConstants.APPROVED_NO,
								PolicyConstants.REJECTED_NO, PolicyConstants.CHECKER_NO))));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getRole())
					&& Objects.equals(freeLookCancellationSearchDto.getRole(), PolicyConstants.CHECKER)) {
				predicates.add(criteriaBuilder.not(root.get("freeLookStatus")
						.in(Arrays.asList(PolicyConstants.APPROVED_NO, PolicyConstants.REJECTED_NO,
								PolicyConstants.DRAFT_NO, PolicyConstants.MAKER_NO, PolicyConstants.REOPEN_NO))));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(root.get("policyNumber"),
						freeLookCancellationSearchDto.getPolicyNumber()));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getMphCode())) {
				predicates.add(criteriaBuilder.equal(root.get("mphCode"), freeLookCancellationSearchDto.getMphCode()));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getMphName())) {
				predicates.add(criteriaBuilder.equal(root.get("mphName"), freeLookCancellationSearchDto.getMphName()));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getFrom())
					&& StringUtils.isNotBlank(freeLookCancellationSearchDto.getTo())) {
				Date fromDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(freeLookCancellationSearchDto.getFrom());
				Date toDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(freeLookCancellationSearchDto.getTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get("createdOn"), fromDate, toDate));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getProduct())) {
				predicates.add(criteriaBuilder.equal(root.get("product"), freeLookCancellationSearchDto.getProduct()));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getLineOfBusiness())) {
				predicates.add(criteriaBuilder.equal(root.get("lineOfBusiness"),
						freeLookCancellationSearchDto.getLineOfBusiness()));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getFreeLookStatus())) {
				predicates.add(criteriaBuilder.equal(root.get("freeLookStatus"),
						freeLookCancellationSearchDto.getFreeLookStatus()));
			}
			if (StringUtils.isNotBlank(freeLookCancellationSearchDto.getUnitCode())) {
				predicates
						.add(criteriaBuilder.equal(root.get("unitCode"), freeLookCancellationSearchDto.getUnitCode()));
			}
			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			policyTempEntities = entityManager.createQuery(createQuery).getResultList();

			List<FreeLookCancellationDto> policyDtos = mapList(policyTempEntities, FreeLookCancellationDto.class);

			commonDto.setFreeLookCancellationDtos(policyDtos);
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
	public FreeLookCancellationResponseDto changeStatus(Long policyId, String status) {
		FreeLookCancellationResponseDto response = new FreeLookCancellationResponseDto();
		try {
			logger.info("PolicyServiceImpl:changeStatus:Start");
			final Optional<FreeLookCancellationTempEntity> result = freeLookCancellationTempRepo.findById(policyId);
			if (result.isPresent()) {
				FreeLookCancellationTempEntity dbPolicyTempEntity = result.get();
				dbPolicyTempEntity.setFreeLookStatus(status);
				dbPolicyTempEntity.setModifiedOn(DateUtils.sysDate());
				if (status == "4") {
					dbPolicyTempEntity.setPolicyCancellationRequestNumber(getFLCPolicySeq());
				}
				dbPolicyTempEntity = freeLookCancellationTempRepo.save(dbPolicyTempEntity);

				FreeLookCancellationDto policyDto = modelMapper.map(dbPolicyTempEntity, FreeLookCancellationDto.class);

				if (status == "4") {
					dbPolicyTempEntity.setPolicyCancellationRequestNumber(getFLCPolicySeq());
				}

				response.setFreeLookId(policyDto.getFreeLookId());
				response.setFreeLookCancellationDto(policyDto);
				response.setTransactionStatus(CommonConstants.SUCCESS);
				response.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:sendToCheker", e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:sendToCheker:Ends");
		}
		return response;
	}

	@Override
	public FreeLookCancellationResponseDto changeStatusReject(Long policyId, String status,
			FreeLookCancellationDto dto) {
		FreeLookCancellationResponseDto response = new FreeLookCancellationResponseDto();
		try {
			logger.info("PolicyServiceImpl:changeStatusReject:Start");
			final Optional<FreeLookCancellationTempEntity> result = freeLookCancellationTempRepo.findById(policyId);
			if (result.isPresent()) {
				FreeLookCancellationTempEntity dbPolicyTempEntity = result.get();
				dbPolicyTempEntity.setFreeLookStatus(status);
				dbPolicyTempEntity.setRejectionReasonCode(dto.getRejectionReasonCode());
				dbPolicyTempEntity.setRejectionRemarks(dto.getRejectionRemarks());
				dbPolicyTempEntity.setPolicyCancellationRequestNumber(null);
				dbPolicyTempEntity.setModifiedOn(DateUtils.sysDate());
				dbPolicyTempEntity.setIsActive(false);
				dbPolicyTempEntity = freeLookCancellationTempRepo.save(dbPolicyTempEntity);
				
				
				FreeLookCancellationEntity flcDtonewentity = new FreeLookCancellationEntity();
				moveToFlcMainTable(dbPolicyTempEntity, flcDtonewentity);

				FreeLookCancellationEntity policyNewMain = freeLookCancellationRepo.save(flcDtonewentity);
				
				FreeLookCancellationDto policyDto = modelMapper.map(policyNewMain, FreeLookCancellationDto.class);
				response.setFreeLookId(policyDto.getFreeLookId());
				response.setFreeLookCancellationDto(policyDto);
				response.setTransactionStatus(CommonConstants.SUCCESS);
				response.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:changeStatusReject", e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:changeStatusReject:Ends");
		}
		return response;
	}

	@Override
	public FreeLookCancellationResponseDto freeLookCancellationApproved(Long policyId, String status) {
		FreeLookCancellationResponseDto response = new FreeLookCancellationResponseDto();
		try {
			logger.info("PolicyServiceImpl:freeLookCancellationApproved:Start");
			final Optional<FreeLookCancellationTempEntity> result = freeLookCancellationTempRepo.findById(policyId);
			if (result.isPresent()) {
				FreeLookCancellationTempEntity dbPolicyTempEntity = result.get();
				dbPolicyTempEntity.setFreeLookStatus(status);
				dbPolicyTempEntity.setPolicyStatus(FreeLookCancellationConstants.CANCELFLCAPPROVED_NO);
				dbPolicyTempEntity.setWorkflowStatus(FreeLookCancellationConstants.CANCELFLCAPPROVED_NO);
				dbPolicyTempEntity.setModifiedOn(DateUtils.sysDate());
				dbPolicyTempEntity.setPolicyCancellationRequestNumber(getFLCPolicySeq());
				dbPolicyTempEntity = freeLookCancellationTempRepo.save(dbPolicyTempEntity);
				
				FreeLookCancellationEntity flcDtonewentity = new FreeLookCancellationEntity();
				moveToFlcMainTable(dbPolicyTempEntity, flcDtonewentity);

				FreeLookCancellationEntity policyNewMain = freeLookCancellationRepo.save(flcDtonewentity);
				
				
				FreeLookCancellationDto policyDto = modelMapper.map(policyNewMain, FreeLookCancellationDto.class);
//				FreeLookCancellationEntity flcEntity = modelMapper.map(policyDto, FreeLookCancellationEntity.class);
//
//				freeLookCancellationRepo.save(flcEntity);

//				PolicyEntity policyEntity = policyRepository.findByPolicyId(policyDto.getPolicyId());
//
//				if (policyEntity != null) {
//					policyEntity.setPolicyStatus(FreeLookCancellationConstants.CANCELFLCAPPROVED_NO);
//					policyEntity.setModifiedBy(policyDto.getModifiedBy());
//					policyEntity.setModifiedOn(new Date());
//					policyRepository.save(policyEntity);
//
//				} else {
//					response.setTransactionStatus(CommonConstants.FAIL);
//					response.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
//				}

				response.setFreeLookId(policyDto.getFreeLookId());
				response.setFreeLookCancellationDto(policyDto);
				response.setTransactionStatus(CommonConstants.SUCCESS);
				response.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:freeLookCancellationApproved", e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:freeLookCancellationApproved:Ends");
		}
		return response;
	}

	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
	}

	@Override
	public FreeLookCancellationResponseDto getnotesDetails(Long freeLookcancellationId) {
		FreeLookCancellationResponseDto commonDto = new FreeLookCancellationResponseDto();
		try {
			logger.info("FreeLookCancellationServiceImpl:getFreeLookCancellationDetails---Started");
			Optional<PolicyServiceNotesTempEntity> result = freeLookCancellationNotesTempRep
					.findById(freeLookcancellationId);
			if (result.isPresent()) {
				PolicyServiceNotesTempEntity policy = result.get();
				FreeLookCancellationDto policyDto = modelMapper.map(policy, FreeLookCancellationDto.class);
				commonDto.setFreeLookCancellationDto(policyDto);
				commonDto.setFreeLookId(policyDto.getFreeLookId());
				commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				commonDto.setTransactionMessage(PolicyConstants.FETCH);
			} else {
				commonDto.setFreeLookCancellationDto(null);
				commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				commonDto.setTransactionMessage(PolicyConstants.LISTTRUSTCONTACTDETAILSERROR);
				logger.info("FreeLookCancellationServiceImpl:getFreeLookCancellationDetails--NO data found");
			}
		} catch (IllegalArgumentException e) {
			logger.info("FreeLookCancellationServiceImpl:getFreeLookCancellationDetails--ERROR--", e);
			commonDto.setTransactionMessage(PolicyConstants.ERROR);
			commonDto.setTransactionStatus(PolicyConstants.FAIL);

		} finally {
			logger.info("FreeLookCancellationServiceImpl:getFreeLookCancellationDetails--Ends");
		}
		return commonDto;
	}

	@Override
	public FreeLookCancellationResponseDto removeFLCDocument(Long documentId, Long freeLookId, String modifiedby) {
		FreeLookCancellationResponseDto freeLookCancellationResponseDto = new FreeLookCancellationResponseDto();
		try {
			logger.info("FreeLookCancellationServiceImpl:removeFLCDocument---Started");
			PolicyServiceDocumentTempEntity freeLookCancellationDocumentTempEntity = freeLookCancellationDocumentTempRepo
					.findByDocumentIdAndFreeLookId(documentId, freeLookId);
			if (freeLookCancellationDocumentTempEntity != null) {
				freeLookCancellationDocumentTempEntity.setIsActive(Boolean.FALSE);
				freeLookCancellationDocumentTempEntity.setModifiedBy(modifiedby);
				freeLookCancellationDocumentTempEntity.setModifiedOn(new Date());

				freeLookCancellationDocumentTempRepo.save(freeLookCancellationDocumentTempEntity);

				freeLookCancellationResponseDto.setFreeLookId(freeLookCancellationDocumentTempEntity.getFreeLookId());
				freeLookCancellationResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				freeLookCancellationResponseDto.setTransactionMessage(CommonConstants.DOCUMENTREMOVEDSUCCESSFULLY);

			} else {
				freeLookCancellationResponseDto.setFreeLookId(freeLookId);
				freeLookCancellationResponseDto.setTransactionStatus(CommonConstants.FAIL);
				freeLookCancellationResponseDto.setTransactionMessage(CommonConstants.DENY);
				logger.info("FreeLookCancellationServiceImpl:removeFLCDocument--NO data found");
			}

		} catch (IllegalArgumentException e) {
			logger.info("FreeLookCancellationServiceImpl:removeFLCDocument--ERROR--", e);
			freeLookCancellationResponseDto.setTransactionMessage(PolicyConstants.ERROR);
			freeLookCancellationResponseDto.setTransactionStatus(PolicyConstants.FAIL);
		} finally {
			logger.info("FreeLookCancellationServiceImpl:removeFLCDocument--Ends");
		}

		return freeLookCancellationResponseDto;
	}

	@Override
	public FreeLookCancellationResponseDto getFlcDocumentDetails(Long freeLookcancellationId) {
		FreeLookCancellationResponseDto commonDto = new FreeLookCancellationResponseDto();
		try {
			logger.info("FreeLookCancellationServiceImpl:getFlcDocumentDetails---Started");
			List<PolicyServiceDocumentTempEntity> result = freeLookCancellationDocumentTempRepo
					.findByFreeLookIdAndIsActiveTrue(freeLookcancellationId);
			if (result != null) {// .isPresent()) {
//				FreeLookCancellationDocumentTempEntity policy = result.get();

				List<PolicyServiceDocumentDto> policyDto = mapList(result,
						PolicyServiceDocumentDto.class);

//				List<FreeLookCancellationDocumentDto> policyDto = modelMapper.map(result, FreeLookCancellationDocumentDto.class);

				commonDto.setFreeLookCancellationDocumentDtos(policyDto);
				commonDto.setFreeLookId(freeLookcancellationId);
				commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				commonDto.setTransactionMessage(PolicyConstants.FETCH);
			} else {
				commonDto.setFreeLookCancellationDto(null);
				commonDto.setTransactionStatus(PolicyConstants.SUCCESS);
				commonDto.setTransactionMessage(PolicyConstants.LISTTRUSTCONTACTDETAILSERROR);
				logger.info("FreeLookCancellationServiceImpl:getFlcDocumentDetails--NO data found");
			}
		} catch (IllegalArgumentException e) {
			logger.info("FreeLookCancellationServiceImpl:getFlcDocumentDetails--ERROR--", e);
			commonDto.setTransactionMessage(PolicyConstants.ERROR);
			commonDto.setTransactionStatus(PolicyConstants.FAIL);

		} finally {
			logger.info("FreeLookCancellationServiceImpl:getFlcDocumentDetails--Ends");
		}
		return commonDto;
	}

	public FreeLookCancellationEntity moveToFlcMainTable(FreeLookCancellationTempEntity flcTemp,
			FreeLookCancellationEntity flc) {

		// FreeLookCancellation

		flc.setFreeLookId(null);
		flc.setFreeLookStatus(flcTemp.getFreeLookStatus());
		flc.setWorkflowStatus(flcTemp.getWorkflowStatus());

		flc.setServiceId(flcTemp.getServiceId());
		flc.setServiceStatus(flcTemp.getServiceStatus());
		flc.setServiceNumber(flcTemp.getServiceNumber());

		flc.setPolicyId(flcTemp.getPolicyId());
		flc.setPolicyStatus(flcTemp.getPolicyStatus());
		flc.setPolicyNumber(flcTemp.getPolicyNumber());

		flc.setMphCode(flcTemp.getMphCode());
		flc.setMphName(flcTemp.getMphName());
		flc.setProduct(flcTemp.getProduct());
		flc.setLineOfBusiness(flcTemp.getLineOfBusiness());
		flc.setUnitCode(flcTemp.getUnitCode());
		flc.setUnitOffice(flcTemp.getUnitOffice());

		flc.setRejectionReasonCode(flcTemp.getRejectionReasonCode());
		flc.setRejectionRemarks(flcTemp.getRejectionRemarks());

		flc.setPolicyCancellationRequestNumber(flcTemp.getPolicyCancellationRequestNumber());
		flc.setRequestReceivedFrom(flcTemp.getRequestReceivedFrom());
		flc.setPolicyBondRequestLetterRecevied(flcTemp.getPolicyBondRequestLetterRecevied());
		flc.setReCovery(flcTemp.getReCovery());
		flc.setAgentName(flcTemp.getAgentName());

		flc.setPolicyFromDate(flcTemp.getPolicyFromDate());
		flc.setPolicyToDate(flcTemp.getPolicyToDate());

		flc.setTotalContribution(flcTemp.getTotalContribution());
		flc.setClaimsPainOnDate(flcTemp.getClaimsPainOnDate());
		flc.setCancelRequestDate(flcTemp.getCancelRequestDate());
		flc.setPolicyBondDispatchDate(flcTemp.getPolicyBondDispatchDate());
		flc.setPolicyBondRecievedDate(flcTemp.getPolicyBondRecievedDate());
		flc.setEffectiveCancellationDate(flcTemp.getEffectiveCancellationDate());

		flc.setTotalFundValue(flcTemp.getTotalFundValue());
		flc.setTotalContribution(flcTemp.getTotalContribution());
		flc.setTotalIntrestAccuredOrPaid(flcTemp.getTotalIntrestAccuredOrPaid());
		flc.setTotalrefundamount(flcTemp.getTotalrefundamount());
		flc.setCommissionAmountToBeReversed(flcTemp.getCommissionAmountToBeReversed());

		flc.setOldFundValue(flcTemp.getOldFundValue());
		flc.setOldTotalIntrestAccuredOrPaid(flcTemp.getOldTotalIntrestAccuredOrPaid());
		flc.setOldArrearValue(flcTemp.getOldArrearValue());
		flc.setOldRecoveryValue(flcTemp.getOldRecoveryValue());

		flc.setNewFundValue(flcTemp.getNewFundValue());
		flc.setNewTotalIntrestAccuredOrPaid(flcTemp.getNewTotalIntrestAccuredOrPaid());
		flc.setNewArrearValue(flcTemp.getNewArrearValue());
		flc.setNewRecoveryValue(flcTemp.getNewRecoveryValue());

		flc.setCommentFundValue(flcTemp.getCommentFundValue());
		flc.setCommentTotalIntrestAccuredOrPaid(flcTemp.getCommentTotalIntrestAccuredOrPaid());
		flc.setCommentArrearValue(flcTemp.getCommentArrearValue());
		flc.setCommentRecoveryValue(flcTemp.getCommentRecoveryValue());

		flc.setModifiedBy(flcTemp.getModifiedBy());
		flc.setModifiedOn(flcTemp.getModifiedOn());
		flc.setCreatedBy(flcTemp.getCreatedBy());
		flc.setCreatedOn(flcTemp.getCreatedOn());
		flc.setIsActive(flcTemp.getIsActive());

		// Documents
		Set<PolicyServiceDocumentEntity> documnetsList = new HashSet<>();
		for (PolicyServiceDocumentTempEntity documnetTemp : flcTemp.getDocuments()) {
			PolicyServiceDocumentEntity documents = new PolicyServiceDocumentEntity();

			documents.setDocumentId(null);

			documents.setFreeLookId(null);
			documents.setPolicyId(documnetTemp.getPolicyId());

			documents.setStatus(documnetTemp.getStatus());
			documents.setComments(documnetTemp.getComments());

			documents.setRequirement(documnetTemp.getRequirement());
			documents.setDocumentIndex(documnetTemp.getDocumentIndex());
			documents.setFolderIndex(documnetTemp.getFolderIndex());

			documents.setModifiedBy(documnetTemp.getModifiedBy());
			documents.setModifiedOn(documnetTemp.getModifiedOn());
			documents.setCreatedBy(documnetTemp.getCreatedBy());
			documents.setCreatedOn(documnetTemp.getCreatedOn());
			documents.setIsActive(documnetTemp.getIsActive());

			documnetsList.add(documents);
		}
		flc.setDocuments(documnetsList);

		// Notes
		Set<PolicyServiceNotesEntity> notesList = new HashSet<>();
		for (PolicyServiceNotesTempEntity noteTemp : flcTemp.getNotes()) {
			PolicyServiceNotesEntity notes = new PolicyServiceNotesEntity();
			notes.setServiceNoteId(null);
			notes.setFreeLookId(null);
			notes.setNoteContents(noteTemp.getNoteContents());
			notes.setCreatedBy(noteTemp.getCreatedBy());
			notes.setCreatedOn(noteTemp.getCreatedOn());
			notes.setModifiedBy(noteTemp.getModifiedBy());
			notes.setModifiedOn(noteTemp.getModifiedOn());
			notes.setIsActive(noteTemp.getIsActive());
			notesList.add(notes);
		}
		flc.setNotes(notesList);

		return flc;
	}
	
	@Override
	public PolicyResponseDto existingPolicyCitrieaSearch(PolicySearchDto policySearchDto) {
//		List<PolicyEntity> policyEntities = new ArrayList<>();
		PolicyResponseDto commonDto = new PolicyResponseDto();
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		try {
//			logger.info("PolicyServiceImpl:existingCitrieaSearch:Start");
//			List<Predicate> predicates = new ArrayList<>();
//			CriteriaQuery<PolicyEntity> createQuery = criteriaBuilder.createQuery(PolicyEntity.class);
//			Root<PolicyEntity> root = createQuery.from(PolicyEntity.class);
//
//			if (StringUtils.isNotBlank(policySearchDto.getPolicyNumber())) {
//				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("policyNumber")),
//						policySearchDto.getPolicyNumber().toLowerCase()));
//			}
//			if (StringUtils.isNotBlank(policySearchDto.getMphCode())) {
//				predicates.add(criteriaBuilder.equal(root.get("mphCode"), policySearchDto.getMphCode()));
//			}
//			if (StringUtils.isNotBlank(policySearchDto.getMphName())) {
//				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("mphName")),
//						policySearchDto.getMphName().toLowerCase()));
//			}
//			if (StringUtils.isNotBlank(policySearchDto.getFrom()) && StringUtils.isNotBlank(policySearchDto.getTo())) {
//				Date fromDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(policySearchDto.getFrom());
//				Date toDate = CommonDateUtils.stringToDateFormatYyyyMmDdDash(policySearchDto.getTo());
//				toDate = CommonDateUtils.constructeEndDateTime(toDate);
//				predicates.add(criteriaBuilder.between(root.get("createdOn"), fromDate, toDate));
//			}
//			if (StringUtils.isNotBlank(policySearchDto.getProduct())) {
//				predicates.add(criteriaBuilder.equal(root.get("product"), policySearchDto.getProduct()));
//			}
//			if (StringUtils.isNotBlank(policySearchDto.getLineOfBusiness())) {
//				predicates.add(criteriaBuilder.equal(root.get("lineOfBusiness"), policySearchDto.getLineOfBusiness()));
//			}
//			if (StringUtils.isNotBlank(policySearchDto.getVariant())) {
//				predicates.add(criteriaBuilder.equal(root.get("variant"), policySearchDto.getVariant()));
//			}
//			if (StringUtils.isNotBlank(policySearchDto.getPolicyStatus())) {
//				predicates.add(criteriaBuilder.equal(root.get("policyStatus"), policySearchDto.getPolicyStatus()));
//			}
//			if (StringUtils.isNotBlank(policySearchDto.getUnitCode())) {
//				predicates.add(criteriaBuilder.equal(root.get("unitCode"), policySearchDto.getUnitCode()));
//			}
//
//			createQuery.orderBy(criteriaBuilder.desc(root.get("modifiedOn")));
//
//			predicates.add(root.get("policyStatus").in(Arrays.asList(PolicyConstants.APPROVED_NO)));
//
//			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
//			policyEntities = entityManager.createQuery(createQuery).getResultList();
//			List<PolicyDto> policyDtos = mapList(policyEntities, PolicyDto.class);
//
//			policyDtos.forEach(policyDto -> policyDto.getDeposit()
//					.removeIf(deposit -> PolicyConstants.ADJESTED.equalsIgnoreCase(deposit.getStatus())));
//
//			commonDto.setPolicyDtos(policyDtos);
//			commonDto.setTransactionMessage(CommonConstants.FETCH);
//			commonDto.setTransactionStatus(CommonConstants.STATUS);
//		} catch (Exception e) {
//			logger.error("Exception:PolicyServiceImpl:existingCitrieaSearch", e);
//			commonDto.setTransactionStatus(CommonConstants.FAIL);
//			commonDto.setTransactionStatus(CommonConstants.ERROR);
//		} finally {
//			logger.info("PolicyServiceImpl:existingCitrieaSearch:Ends");
//		}
		return commonDto;
	}

}
