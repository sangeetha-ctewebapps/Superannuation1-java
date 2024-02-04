package com.lic.epgs.topupda.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.topupda.dto.AnnuityRequestDto;
import com.lic.epgs.topupda.dto.AnnutityResponseDto;
import com.lic.epgs.topupda.dto.TopupdaApiResponseDto;
import com.lic.epgs.topupda.dto.TopupdaDto;
import com.lic.epgs.topupda.dto.TopupdaNotesDto;
import com.lic.epgs.topupda.dto.TopupdaRquestDto;
import com.lic.epgs.topupda.entity.TopupdaEntity;
import com.lic.epgs.topupda.entity.TopupdaNotesEntity;
import com.lic.epgs.topupda.entity.TopupdaNotesTempEntity;
import com.lic.epgs.topupda.entity.TopupdaTempEntity;
import com.lic.epgs.topupda.repository.TopupdaNotesTempRepository;
import com.lic.epgs.topupda.repository.TopupdaRepository;
import com.lic.epgs.topupda.repository.TopupdaTempRepository;
import com.lic.epgs.topupda.service.TopupdaService;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.DateUtils;

/**
 *
 * @author Dhanush
 *
 */

@Service
public class TopupdaServiceImpl implements TopupdaService {

	@Autowired
	private TopupdaTempRepository topUpDaTempRepository;

	@Autowired
	private TopupdaRepository topUpDaRepository;

	@Autowired
	private TopupdaNotesTempRepository notesTempRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EntityManager entityManager;

	protected final Logger logger = LogManager.getLogger(getClass());

	@Override
	public TopupdaApiResponseDto saveTopUpDa(TopupdaDto topUpDaDto) {
		TopupdaApiResponseDto topUpDaApiResponse = new TopupdaApiResponseDto();
		try {
			logger.info("TopupdaServiceImpl:saveTopUpDa:Started");
			TopupdaTempEntity topUpDaTempEntity = modelMapper.map(topUpDaDto, TopupdaTempEntity.class);
			topUpDaTempEntity.setTopupStatus(CommonConstants.COMMON_DRAFT);
			topUpDaTempEntity.setIsActive(true);
			topUpDaTempEntity.setCreatedOn(DateUtils.sysDate());
			TopupdaTempEntity responseEntity = topUpDaTempRepository.save(topUpDaTempEntity);
			TopupdaDto dto = modelMapper.map(responseEntity, TopupdaDto.class);
			topUpDaApiResponse.setResponseDto(dto);
			topUpDaApiResponse.setTransactionMessage(CommonConstants.SAVEMESSAGE);
			topUpDaApiResponse.setTransactionStatus(CommonConstants.SUCCESS);

		} catch (IllegalArgumentException e) {
			logger.error("Exception:TopupdaServiceImpl:saveTopUpDa", e);
			topUpDaApiResponse.setTransactionMessage(CommonConstants.ERROR);
			topUpDaApiResponse.setTransactionStatus(CommonConstants.FAIL);
		}
		logger.info("TopupdaServiceImpl:saveTopUpDa:Ended");
		return topUpDaApiResponse;
	}

	@Override
	public TopupdaApiResponseDto changeStatus(Long topupId, String status) {
		TopupdaApiResponseDto response = new TopupdaApiResponseDto();
		try {
			logger.info("TopupdaServiceImpl:changeStatus:Started");
			TopupdaTempEntity result = topUpDaTempRepository.findByTopupIdAndIsActiveTrue(topupId);
			if (result != null) {
				TopupdaTempEntity topupEntity = result;
				topupEntity.setTopupStatus(status);
				topupEntity.setModifiedOn(DateUtils.sysDate());
				topupEntity = topUpDaTempRepository.save(topupEntity);
				TopupdaDto dto = modelMapper.map(topupEntity, TopupdaDto.class);
				response.setResponseDto(dto);
				response.setTransactionStatus(CommonConstants.SUCCESS);
				response.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:TopupdaServiceImpl:changeStatus", e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("TopupdaServiceImpl:changeStatus:Ended");
		return response;
	}

	@Override
	public TopupdaApiResponseDto sendToApprove(Long topupId) {
		TopupdaApiResponseDto response = new TopupdaApiResponseDto();
		try {
			logger.info("TopupdaServiceImpl:sendToApprove:Started");
			TopupdaTempEntity result = topUpDaTempRepository.findByTopupIdAndIsActiveTrue(topupId);
			if (result != null) {
				TopupdaTempEntity topupEntity = result;
				topupEntity.setTopupStatus(CommonConstants.COMMON_APPROVED);
				topupEntity.setModifiedOn(DateUtils.sysDate());
				topupEntity.setAmountStatus(PolicyConstants.AMOUNTLOCK);
				topupEntity = topUpDaTempRepository.save(topupEntity);
				TopupdaDto dto = modelMapper.map(topupEntity, TopupdaDto.class);

				TopupdaEntity topUpDaEntity = modelMapper.map(dto, TopupdaEntity.class);
				topUpDaEntity.setTopupId(null);
				Set<TopupdaNotesEntity> notesList = new HashSet<>();
				for (TopupdaNotesEntity notesEntity : topUpDaEntity.getNotes()) {
					notesEntity.setTopupdaNoteId(null);
					notesEntity.setTopupId(topUpDaEntity.getTopupId());
					notesEntity.setIsActive(true);
					notesEntity.setNoteContents(notesEntity.getNoteContents());
					notesList.add(notesEntity);
				}
				topUpDaEntity.setNotes(notesList);
				TopupdaEntity responseEntity = topUpDaRepository.save(topUpDaEntity);
				TopupdaDto mainDto = modelMapper.map(responseEntity, TopupdaDto.class);
				response.setResponseDto(mainDto);
				response.setTransactionStatus(CommonConstants.SUCCESS);
				response.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:TopupdaServiceImpl:sendToApprove", e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("TopupdaServiceImpl:sendToApprove:Ended");
		return response;
	}

	@Override
	public TopupdaApiResponseDto sendToReject(TopupdaDto topUpDaDto) {
		TopupdaApiResponseDto response = new TopupdaApiResponseDto();
		try {
			logger.info("TopupdaServiceImpl:sendToReject:Started");
			TopupdaTempEntity result = topUpDaTempRepository.findByTopupIdAndIsActiveTrue(topUpDaDto.getTopupId());
			if (result != null) {
				TopupdaTempEntity topupEntity = result;
				topupEntity.setTopupStatus(PolicyConstants.REJECTED_NO);
				topupEntity.setModifiedOn(DateUtils.sysDate());
				topupEntity = topUpDaTempRepository.save(topupEntity);
				TopupdaDto dto = modelMapper.map(topupEntity, TopupdaDto.class);

				TopupdaEntity topUpDaEntity = modelMapper.map(dto, TopupdaEntity.class);
				topUpDaEntity.setTopupId(null);
				Set<TopupdaNotesEntity> notesList = new HashSet<>();
				for (TopupdaNotesEntity notesEntity : topUpDaEntity.getNotes()) {
					notesEntity.setTopupdaNoteId(null);
					notesEntity.setTopupId(topUpDaEntity.getTopupId());
					notesEntity.setIsActive(true);
					notesEntity.setNoteContents(notesEntity.getNoteContents());
					notesList.add(notesEntity);
				}
				topUpDaEntity.setNotes(notesList);
				TopupdaEntity responseEntity = topUpDaRepository.save(topUpDaEntity);
				TopupdaDto mainDto = modelMapper.map(responseEntity, TopupdaDto.class);
				response.setResponseDto(mainDto);
				response.setTransactionStatus(CommonConstants.SUCCESS);
				response.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:TopupdaServiceImpl:sendToReject", e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("TopupdaServiceImpl:sendToReject:Ended");
		return response;
	}

	@Override
	public TopupdaApiResponseDto inprogressCitrieaLoad(TopupdaRquestDto topUpDaDto) {
		TopupdaApiResponseDto commonDto = new TopupdaApiResponseDto();
		try {
			logger.info("TopupdaServiceImpl:inprogressCitrieaLoad:Started");
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<TopupdaTempEntity> createQuery = criteriaBuilder.createQuery(TopupdaTempEntity.class);
			Root<TopupdaTempEntity> root = createQuery.from(TopupdaTempEntity.class);
			if (StringUtils.isNotBlank(topUpDaDto.getRole())
					&& Objects.equals(topUpDaDto.getRole(), PolicyConstants.MAKER)) {
				predicates.add(root.get("topupStatus").in(
						Arrays.asList(CommonConstants.COMMON_DRAFT, CommonConstants.COMMON_PENDING_FOR_MODIFICATION)));
			}
			if (StringUtils.isNotBlank(topUpDaDto.getRole())
					&& Objects.equals(topUpDaDto.getRole(), PolicyConstants.CHECKER)) {
				predicates.add(root.get("topupStatus").in(Arrays.asList(CommonConstants.COMMON_PENDING_FOR_APPROVER)));
			}
			if (StringUtils.isNotBlank(topUpDaDto.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get("unitCode"), topUpDaDto.getUnitCode()));
			}
			if (StringUtils.isNotBlank(topUpDaDto.getQuoationNumber())) {
				predicates.add(criteriaBuilder.equal(root.get("quotationNumber"), topUpDaDto.getQuoationNumber()));
			}
			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			createQuery.orderBy(criteriaBuilder.desc(root.get("modifiedOn")));

			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			List<TopupdaTempEntity> result = entityManager.createQuery(createQuery).getResultList();
			List<TopupdaDto> response = result.stream().map(this::convertTempEntityToDto).collect(Collectors.toList());
			commonDto.setResponseDtos(response);
			commonDto.setTransactionMessage(CommonConstants.FETCH);
			commonDto.setTransactionStatus(CommonConstants.STATUS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception:TopupdaServiceImpl:inprogressCitrieaLoad", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("TopupdaServiceImpl:inprogressCitrieaLoad:Ended");
		return commonDto;
	}

	@Override
	public TopupdaApiResponseDto existingCitrieaLoad(TopupdaRquestDto topUpDaDto) {
		TopupdaApiResponseDto commonDto = new TopupdaApiResponseDto();
		try {
			logger.info("TopupdaServiceImpl:existingCitrieaLoad:Started");
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<TopupdaEntity> createQuery = criteriaBuilder.createQuery(TopupdaEntity.class);
			Root<TopupdaEntity> root = createQuery.from(TopupdaEntity.class);
			if (StringUtils.isNotBlank(topUpDaDto.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get("unitCode"), topUpDaDto.getUnitCode()));
			}
			if (StringUtils.isNotBlank(topUpDaDto.getQuoationNumber())) {
				predicates.add(criteriaBuilder.equal(root.get("quotationNumber"), topUpDaDto.getQuoationNumber()));
			}
			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			predicates.add(root.get("topupStatus")
					.in(Arrays.asList(CommonConstants.COMMON_APPROVED, CommonConstants.COMMON_REJECT)));
			createQuery.orderBy(criteriaBuilder.desc(root.get("modifiedOn")));

			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			List<TopupdaEntity> result = entityManager.createQuery(createQuery).getResultList();
			List<TopupdaDto> response = result.stream().map(this::convertEntityToDto).collect(Collectors.toList());
			commonDto.setResponseDtos(response);
			commonDto.setTransactionMessage(CommonConstants.FETCH);
			commonDto.setTransactionStatus(CommonConstants.STATUS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception:TopupdaServiceImpl:existingCitrieaLoad", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("TopupdaServiceImpl:existingCitrieaLoad:Ended");
		return commonDto;
	}

	@Override
	public TopupdaApiResponseDto getTopupById(String status, Long topupId) {
		TopupdaApiResponseDto commonDto = new TopupdaApiResponseDto();
		try {
			logger.info("TopupdaServiceImpl:getTopupById:Started");
			Optional<?> topUpEntity = CommonConstants.EXISTING.equals(status) ? topUpDaRepository.findById(topupId)
					: topUpDaTempRepository.findById(topupId);

			if (topUpEntity.isPresent()) {
				TopupdaDto topupdaDto = modelMapper.map(topUpEntity.get(), TopupdaDto.class);
				commonDto.setResponseDto(topupdaDto);
//				List<TopupdaNotesDto> dtos = new ArrayList<>();
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
			}

			else {
				commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
				commonDto.setTransactionStatus(CommonConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:TopupdaServiceImpl:getTopupById", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("TopupdaServiceImpl:getTopupById:Ended");
		return commonDto;
	}

	private TopupdaDto convertEntityToDto(TopupdaEntity entity) {
		TopupdaDto dto = new TopupdaDto();

		dto.setTopupId(entity.getTopupId());
		dto.setMessage(entity.getMessage());
		dto.setCustomErrorCode(entity.getCustomErrorCode());
		dto.setQuotationNumber(entity.getQuotationNumber());
		dto.setQuotationStatus(entity.getQuotationStatus());
		dto.setPolicyNumber(entity.getPolicyNumber());
		dto.setPolicyStatus(entity.getPolicyStatus());
		dto.setMphName(entity.getMphName());
		dto.setProduct(entity.getProduct());
		dto.setVarient(entity.getVarient());
		dto.setCalculationValue(entity.getCalculationValue());
		dto.setTypeOfTopup(entity.getTypeOfTopup());
		dto.setGstBorneBy(entity.getGstBorneBy());
		dto.setGrossPurchasePrice(entity.getGrossPurchasePrice());
		dto.setGst(entity.getGst());
		dto.setQuotationAmount(entity.getQuotationAmount());
		dto.setPurchasePrice(entity.getPurchasePrice());
		dto.setArrears(entity.getArrears());
		dto.setRecovery(entity.getRecovery());
		dto.setTopupStagId(entity.getTopupStagId());
		dto.setTopupStatus(entity.getTopupStatus());
		dto.setUnitCode(entity.getUnitCode());
		dto.setModifiedBy(entity.getModifiedBy());
		dto.setModifiedOn(entity.getModifiedOn());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreatedOn(entity.getCreatedOn());
		dto.setIsActive(entity.getIsActive());
		dto.setRejectionRemarks(entity.getRejectionRemarks());
		dto.setRejectioncode(entity.getRejectionReasonCode());
		return dto;
	}

	private TopupdaDto convertTempEntityToDto(TopupdaTempEntity entity) {
		TopupdaDto dto = new TopupdaDto();

		dto.setTopupId(entity.getTopupId());
		dto.setMessage(entity.getMessage());
		dto.setCustomErrorCode(entity.getCustomErrorCode());
		dto.setQuotationNumber(entity.getQuotationNumber());
		dto.setQuotationStatus(entity.getQuotationStatus());
		dto.setPolicyNumber(entity.getPolicyNumber());
		dto.setPolicyStatus(entity.getPolicyStatus());
		dto.setMphName(entity.getMphName());
		dto.setProduct(entity.getProduct());
		dto.setVarient(entity.getVarient());
		dto.setCalculationValue(entity.getCalculationValue());
		dto.setTypeOfTopup(entity.getTypeOfTopup());
		dto.setGstBorneBy(entity.getGstBorneBy());
		dto.setGrossPurchasePrice(entity.getGrossPurchasePrice());
		dto.setGst(entity.getGst());
		dto.setQuotationAmount(entity.getQuotationAmount());
		dto.setPurchasePrice(entity.getPurchasePrice());
		dto.setArrears(entity.getArrears());
		dto.setRecovery(entity.getRecovery());
		dto.setTopupStagId(entity.getTopupStagId());
		dto.setTopupStatus(entity.getTopupStatus());
		dto.setUnitCode(entity.getUnitCode());
		dto.setModifiedBy(entity.getModifiedBy());
		dto.setModifiedOn(entity.getModifiedOn());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreatedOn(entity.getCreatedOn());
		dto.setIsActive(entity.getIsActive());
		dto.setRejectionRemarks(entity.getRejectionRemarks());
		dto.setRejectioncode(entity.getRejectionReasonCode());
		return dto;
	}

	@Override
	public TopupdaApiResponseDto saveNotes(TopupdaNotesDto topUpDaNotesDto) {
		TopupdaApiResponseDto topUpDaApiResponse = new TopupdaApiResponseDto();
		try {
			logger.info("TopupdaServiceImpl:saveNotes:Started");
			TopupdaNotesTempEntity topNotesTempEntity = modelMapper.map(topUpDaNotesDto, TopupdaNotesTempEntity.class);
			topNotesTempEntity.setIsActive(true);
			topNotesTempEntity.setCreatedOn(DateUtils.sysDate());
			TopupdaNotesTempEntity responseEntity = notesTempRepository.save(topNotesTempEntity);
			TopupdaNotesDto dto = modelMapper.map(responseEntity, TopupdaNotesDto.class);
			topUpDaApiResponse.setResponseNoteDto(dto);
			topUpDaApiResponse.setTransactionMessage(CommonConstants.SAVEMESSAGE);
			topUpDaApiResponse.setTransactionStatus(CommonConstants.SUCCESS);

		} catch (IllegalArgumentException e) {
			logger.error("Exception:TopupdaServiceImpl:saveNotes", e);
			topUpDaApiResponse.setTransactionMessage(CommonConstants.ERROR);
			topUpDaApiResponse.setTransactionStatus(CommonConstants.FAIL);
		}
		logger.info("TopupdaServiceImpl:saveNotes:Ended");
		return topUpDaApiResponse;
	}

	@Override
	public TopupdaApiResponseDto getNotesList(Long topupId) {
		TopupdaApiResponseDto topUpDaApiResponse = new TopupdaApiResponseDto();
		List<TopupdaNotesDto> dtos = new ArrayList<>();
		try {
			logger.info("TopupdaServiceImpl:getNotesList:Started");
			List<TopupdaNotesTempEntity> topNotesTempEntity = notesTempRepository
					.findAllByTopupIdAndIsActiveTrue(topupId);
			if (topNotesTempEntity != null) {
				for (TopupdaNotesTempEntity entity : topNotesTempEntity) {
					TopupdaNotesDto dto = modelMapper.map(entity, TopupdaNotesDto.class);
					dtos.add(dto);
				}
				topUpDaApiResponse.setResponseNoteDtos(dtos);
				topUpDaApiResponse.setTransactionMessage(CommonConstants.FETCH_MESSAGE);
				topUpDaApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
			} else {
				topUpDaApiResponse.setTransactionMessage(CommonConstants.DENY);
				topUpDaApiResponse.setTransactionStatus(CommonConstants.ERROR);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:TopupdaServiceImpl:getNotesList", e);
			topUpDaApiResponse.setTransactionMessage(CommonConstants.ERROR);
			topUpDaApiResponse.setTransactionStatus(CommonConstants.FAIL);
		}
		logger.info("TopupdaServiceImpl:getNotesList:Ended");
		return topUpDaApiResponse;
	}

	@Override
	public AnnutityResponseDto annutityApi(AnnuityRequestDto annuityRequestDto) {
		AnnutityResponseDto dto = new AnnutityResponseDto();
		try {
			logger.info("TopupdaServiceImpl:annutityApi:Started");
			TopupdaEntity entity = topUpDaRepository
					.findByQuotationNumberAndIsActiveTrue(annuityRequestDto.getQuotationNumber());
			if (entity != null) {
				if (StringUtils.isNotBlank(annuityRequestDto.getAction())
						&& Objects.equals(annuityRequestDto.getAction(), PolicyConstants.MAKERREJECT)
						|| StringUtils.isNotBlank(annuityRequestDto.getAction())
								&& Objects.equals(annuityRequestDto.getAction(), PolicyConstants.CHECKERREJECT)
						|| StringUtils.isNotBlank(annuityRequestDto.getAction())
								&& Objects.equals(annuityRequestDto.getAction(), PolicyConstants.SENDTOMAKER)) {
					dto.setMessage(PolicyConstants.RESPONSEUNLOCK);
					dto.setStatusCode(PolicyConstants.SUCESSSTATUS);
					entity.setAmountStatus(PolicyConstants.AMOUNTUNLOCK);
					entity.setAnReason(annuityRequestDto.getReason());
					topUpDaRepository.save(entity);

				} else if (StringUtils.isNotBlank(annuityRequestDto.getAction())
						&& Objects.equals(annuityRequestDto.getAction(), PolicyConstants.CHECKERAPPROVED)) {
					dto.setMessage(PolicyConstants.STATUS);
					dto.setStatusCode(PolicyConstants.SUCESSSTATUS);
					entity.setAmountStatus(PolicyConstants.AMOUNTLOCK);
					entity.setAnReason(annuityRequestDto.getReason());
					topUpDaRepository.save(entity);
				}

			} else {
				dto.setErrorCode(PolicyConstants.ERRORSTATUS);
				dto.setMessage(PolicyConstants.QUOTATION_INVALID);
			}

		} catch (IllegalArgumentException e) {
			dto.setErrorCode(PolicyConstants.ERRORSTATUS);
		}
		logger.info("TopupdaServiceImpl:annutityApi:Ended");
		return dto;

	}
}
