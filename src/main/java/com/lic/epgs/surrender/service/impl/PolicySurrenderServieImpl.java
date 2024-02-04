package com.lic.epgs.surrender.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.lic.epgs.common.dto.CommonDocsDto;
import com.lic.epgs.common.dto.CommonNotesDto;
import com.lic.epgs.surrender.dto.CommonResponseDto;
import com.lic.epgs.surrender.dto.PolicySurrenderApiResponse;
import com.lic.epgs.surrender.dto.PolicySurrenderDto;
import com.lic.epgs.surrender.dto.PolicySurrenderPayOutDto;
import com.lic.epgs.surrender.service.PolicySurrenderService;

@Service
public class PolicySurrenderServieImpl implements PolicySurrenderService {
	
	protected final Logger logger = LogManager.getLogger(getClass());

	@Override
	public PolicySurrenderApiResponse saveAndUpdatePolicySurrender(PolicySurrenderDto policySurrenderDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicySurrenderApiResponse sendToMakerChecker(PolicySurrenderDto policySurrenderDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicySurrenderApiResponse approvePolicySurrender(PolicySurrenderDto policySurrednerDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicySurrenderApiResponse rejectPolicySurrender(PolicySurrenderDto policySurrednerDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicySurrenderApiResponse uploadDocument(CommonDocsDto docsDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicySurrenderApiResponse saveNotesDetails(CommonNotesDto commonNotesDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicySurrenderApiResponse inprogresOrExitingSurrender(String roleType, String inprogress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicySurrenderApiResponse getPolicySurrenderbySurrenderId(Long surrenderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicySurrenderApiResponse savePayoutDetails(PolicySurrenderPayOutDto policySurrenderPayOutDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicySurrenderApiResponse editPayoutDetails(PolicySurrenderPayOutDto policySurrenderPayOutDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicySurrenderApiResponse getPolicySurrenderBasicDetailsById(Long surrenderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonResponseDto removeDocumentDetails(Long surrenderId, Integer docId, String modifiedBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonResponseDto getSurrenderNotesList(Long surrenderId) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Autowired
//	private PolicySurrenderRepository policySurrenderRepository;
//
//	@Autowired
//	private PolicySurrenderTempRepository policySurrenderTempRepository;
//
//	@Autowired
//	private ModelMapper modelMapper;
//
//	@Autowired
//	private CommonDocsTempRepository commonDocsTempRepository;
//
//	@Autowired
//	private CommonNotesTempRepository commonNotesTempRepository;
//
//	@Autowired
//	private PolicySurrenderPayOutRepository policySurrenderPayOutRepository;
//
//	@Autowired
//	private PolicySurrenderPayOutTempRepository policySurrenderPayOutTempRepository;
//
//	@Autowired
//	private PolicyRepository policyRepository;
//
//	@Autowired
//	private CommonNoteRepository commonNoteRepository;
//
//	@Autowired
//	private CommonDocsRepository commonDocsRepository;
//
//	@Override
//	public PolicySurrenderApiResponse saveAndUpdatePolicySurrender(PolicySurrenderDto policySurrenderDto) {
//		PolicySurrenderApiResponse policySurrenderApiResponse = new PolicySurrenderApiResponse();
//		Optional<PolicySurrenderTempEntity> policySurrenderTempEntity;
//		try {
//			logger.info("PolicySurrenderServieImpl:saveAndUpdatePolicySurrender:Start");
//			PolicySurrenderTempEntity newPolicySurrenderTempEntity = modelMapper.map(policySurrenderDto,
//					PolicySurrenderTempEntity.class);
//			newPolicySurrenderTempEntity.setIsActive(Boolean.TRUE);
//			newPolicySurrenderTempEntity.setSurrenderStatus(PolicyConstants.DRAFT_NO);
//			PolicyEntity policyEntity = policyRepository
//					.findByPolicyNumberAndIsActiveTrue(policySurrenderDto.getPolicyNo());
//			if (policyEntity != null) {
//				newPolicySurrenderTempEntity.setMphCode(policyEntity.getMphCode());
//				newPolicySurrenderTempEntity.setMphName(policyEntity.getMphName());
//				newPolicySurrenderTempEntity.setProduct(policyEntity.getProduct());
//			}
//
//			policySurrenderTempEntity = policySurrenderTempRepository
//					.findAllBySurrenderIdAndIsActiveTrue(policySurrenderDto.getSurrenderId());
//			PolicySurrenderDto updatePolicySurrenderDto = modelMapper.map(policySurrenderTempEntity,
//					PolicySurrenderDto.class);
//			if (updatePolicySurrenderDto.getSurrenderId() == null) {
//
//				newPolicySurrenderTempEntity = policySurrenderTempRepository.save(newPolicySurrenderTempEntity);
//				newPolicySurrenderTempEntity.setCreatedBy(policySurrenderDto.getCreatedBy());
//				newPolicySurrenderTempEntity.setCreatedOn(DateUtils.sysDate());
//
//			} else {
//
//				PolicySurrenderTempEntity updatedSurrenderTempEntity = modelMapper.map(updatePolicySurrenderDto,
//						PolicySurrenderTempEntity.class);
//				newPolicySurrenderTempEntity = policySurrenderTempRepository.save(updatedSurrenderTempEntity);
//				newPolicySurrenderTempEntity.setCreatedBy(updatedSurrenderTempEntity.getCreatedBy());
//				newPolicySurrenderTempEntity.setCreatedOn(updatedSurrenderTempEntity.getCreatedOn());
//				newPolicySurrenderTempEntity.setModifiedBy(updatedSurrenderTempEntity.getModifiedBy());
//				newPolicySurrenderTempEntity.setModifiedOn(DateUtils.sysDate());
//			}
//			newPolicySurrenderTempEntity.setCreatedOn(DateUtils.sysDate());
//			PolicySurrenderDto resPolicySurrenderDto = modelMapper.map(newPolicySurrenderTempEntity,
//					PolicySurrenderDto.class);
//			policySurrenderApiResponse.setSurrenderDto(resPolicySurrenderDto);
//			policySurrenderApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
//			policySurrenderApiResponse.setTransactionMessage(CommonConstants.SAVEMESSAGE);
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- PolicySurrenderServiceImpl-- saveAndUpdatePolicySurrender --", e);
//			policySurrenderApiResponse.setTransactionStatus(CommonConstants.FAIL);
//			policySurrenderApiResponse.setTransactionMessage(e.getMessage());
//		}
//		logger.info("PolicySurrenderServieImpl:saveAndUpdatePolicySurrender:end");
//		return policySurrenderApiResponse;
//	}
//
//	@Override
//	public PolicySurrenderApiResponse sendToMakerChecker(PolicySurrenderDto policySurrenderDto) {
//		logger.info("PolicySurrenderServieImpl:sendToMakerChecker:Start");
//		PolicySurrenderApiResponse commonResponseDto = new PolicySurrenderApiResponse();
//		try {
//			Optional<PolicySurrenderTempEntity> policySurrenderTempEntity = policySurrenderTempRepository
//					.findBySurrenderIdAndIsActiveTrue(policySurrenderDto.getSurrenderId());
//			if (policySurrenderTempEntity != null) {
//				policySurrenderTempEntity.setSurrenderStatus(policySurrenderDto.getSurrenderStatus());
//				policySurrenderTempEntity.setModifiedBy(policySurrenderDto.getModifiedBy());
//				policySurrenderTempEntity.setModifiedOn(DateUtils.sysDate());
//				policySurrenderTempRepository.save(policySurrenderTempEntity);
//				PolicySurrenderDto resPolicySurrenderDto = modelMapper.map(policySurrenderTempEntity,
//						PolicySurrenderDto.class);
//				commonResponseDto.setSurrenderDto(resPolicySurrenderDto);
//				commonResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
//				commonResponseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
//			} else {
//				commonResponseDto.setTransactionStatus(CommonConstants.FAIL);
//				commonResponseDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- PolicySurrenderServiceImpl-- sendToMakerChecker --", e);
//			commonResponseDto.setTransactionStatus(CommonConstants.FAIL);
//			commonResponseDto.setTransactionMessage(e.getMessage());
//		}
//		logger.info("PolicySurrenderServieImpl:saveAndUpdatePolicySurrender:end");
//		return commonResponseDto;
//	}
//
//	@Override
//	public PolicySurrenderApiResponse approvePolicySurrender(PolicySurrenderDto policySurrednerDto) {
//		logger.info("PolicySurrenderServieImpl:approvedAndRejectPolicySurrender:Start");
//		PolicySurrenderApiResponse policySurrenderApiResponse = new PolicySurrenderApiResponse();
//		try {
//			Optional<PolicySurrenderTempEntity> policySurrenderTempEntity = policySurrenderTempRepository
//					.findById(policySurrednerDto.getSurrenderId());
//
//			if (policySurrenderTempEntity.isPresent()) {
//				PolicySurrenderTempEntity policySurrenderTemp = policySurrenderTempEntity.get();
//				policySurrenderTemp.setSurrenderStatus(policySurrednerDto.getSurrenderStatus());
//				policySurrenderTemp.setModifiedBy(policySurrednerDto.getModifiedBy());
//				policySurrenderTemp.setModifiedOn(DateUtils.sysDate());
//				policySurrenderTemp = policySurrenderTempRepository.save(policySurrenderTemp);
//				PolicySurrenderEntity policySurrenderEntity = modelMapper.map(policySurrenderTemp,
//						PolicySurrenderEntity.class);
//				policySurrenderEntity.setIsActive(Boolean.TRUE);
//				policySurrenderEntity.setSurrenderId(null);
//				policySurrenderEntity.setNotes(null);
//				policySurrenderEntity.setDocs(null);
//				policySurrenderEntity = policySurrenderRepository.save(policySurrenderEntity);
//				PolicySurrenderPayOutTempEntity policySurrenderPayOutTempEntity = policySurrenderPayOutTempRepository
//						.findBySurrenderIdAndIsActiveTrue(policySurrednerDto.getSurrenderId());
//				if (policySurrenderPayOutTempEntity != null) {
//					PolicySurrenderPayOutEntity policySurrenderPayOutEntity = modelMapper
//							.map(policySurrenderPayOutTempEntity, PolicySurrenderPayOutEntity.class);
//					policySurrenderPayOutEntity.setPayoutId(null);
//					policySurrenderPayOutEntity.setSurrenderId(policySurrenderEntity.getSurrenderId());
//					policySurrenderPayOutRepository.save(policySurrenderPayOutEntity);
//				}
//				List<CommonDocsTempEntity> docsTempEntityList = commonDocsTempRepository
//						.findAllBySurrenderId(policySurrednerDto.getSurrenderId());
//				if (!docsTempEntityList.isEmpty()) {
//					for (CommonDocsTempEntity commonDocsTempEntity : docsTempEntityList) {
//						CommonDocsEntity commonDocsEntity = modelMapper.map(commonDocsTempEntity,
//								CommonDocsEntity.class);
//						commonDocsEntity.setDocId(null);
//						commonDocsEntity.setSurrenderId(policySurrenderEntity.getSurrenderId());
//						commonDocsRepository.save(commonDocsEntity);
//					}
//				}
//				List<CommonNoteTempEntity> commonNoteTempEntityList = commonNotesTempRepository
//						.findAllBySurrenderId(policySurrednerDto.getSurrenderId());
//				if (!commonNoteTempEntityList.isEmpty()) {
//					for (CommonNoteTempEntity commonNoteTempEntity : commonNoteTempEntityList) {
//						CommonNoteEntity commonNoteEntity = modelMapper.map(commonNoteTempEntity,
//								CommonNoteEntity.class);
//						commonNoteEntity.setSurrenderId(policySurrenderEntity.getSurrenderId());
//						commonNoteEntity.setNoteId(null);
//						commonNoteRepository.save(commonNoteEntity);
//					}
//				}
//
//				policySurrenderApiResponse
//						.setResponseObject(modelMapper.map(policySurrenderEntity, PolicySurrenderDto.class));
//				policySurrenderApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
//				policySurrenderApiResponse.setTransactionMessage(CommonConstants.SAVEMESSAGE);
//				return policySurrenderApiResponse;
//			} else {
//				policySurrenderApiResponse.setResponseObject(policySurrenderApiResponse);
//				policySurrenderApiResponse.setTransactionStatus(CommonConstants.FAIL);
//				policySurrenderApiResponse.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
//				return policySurrenderApiResponse;
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- PolicySurrenderServiceImpl-- approvedAndRejectPolicySurrender --", e);
//			policySurrenderApiResponse.setTransactionStatus(CommonConstants.FAIL);
//			policySurrenderApiResponse.setTransactionMessage(e.getMessage());
//			return policySurrenderApiResponse;
//		}
//	}
//
//	@Override
//	public PolicySurrenderApiResponse rejectPolicySurrender(PolicySurrenderDto policySurrednerDto) {
//		logger.info("PolicySurrenderServieImpl:approvedAndRejectPolicySurrender:Start");
//		PolicySurrenderApiResponse policySurrenderApiResponse = new PolicySurrenderApiResponse();
//		try {
//			Optional<PolicySurrenderTempEntity> policySurrenderTempEntity = policySurrenderTempRepository
//					.findById(policySurrednerDto.getSurrenderId());
//
//			if (policySurrenderTempEntity.isPresent()) {
//				PolicySurrenderTempEntity policySurrenderTemp = policySurrenderTempEntity.get();
//				policySurrenderTemp.setModifiedBy(policySurrednerDto.getModifiedBy());
//				policySurrenderTemp.setSurrenderStatus(policySurrednerDto.getSurrenderStatus());
//				policySurrenderTemp.setModifiedOn(DateUtils.sysDate());
//				policySurrenderTemp = policySurrenderTempRepository.save(policySurrenderTemp);
//				PolicySurrenderEntity policySurrenderEntity = modelMapper.map(policySurrenderTemp,
//						PolicySurrenderEntity.class);
//				policySurrenderEntity.setIsActive(Boolean.TRUE);
//				policySurrenderEntity.setSurrenderId(null);
//				policySurrenderEntity.setNotes(null);
//				policySurrenderEntity.setDocs(null);
//				policySurrenderEntity = policySurrenderRepository.save(policySurrenderEntity);
//
//				policySurrenderApiResponse
//						.setResponseObject(modelMapper.map(policySurrenderEntity, PolicySurrenderDto.class));
//				policySurrenderApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
//				policySurrenderApiResponse.setTransactionMessage(CommonConstants.SAVEMESSAGE);
//				return policySurrenderApiResponse;
//			} else {
//				policySurrenderApiResponse.setResponseObject(policySurrenderApiResponse);
//				policySurrenderApiResponse.setTransactionStatus(CommonConstants.FAIL);
//				policySurrenderApiResponse.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
//				return policySurrenderApiResponse;
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- PolicySurrenderServiceImpl-- approvedAndRejectPolicySurrender --", e);
//			policySurrenderApiResponse.setTransactionStatus(CommonConstants.FAIL);
//			policySurrenderApiResponse.setTransactionMessage(e.getMessage());
//			return policySurrenderApiResponse;
//		}
//
//	}
//
//	@Override
//	public PolicySurrenderApiResponse uploadDocument(CommonDocsDto docsDto) {
//		logger.info("PolicySurrenderServieImpl:sendToMakerChecker:Start");
//		PolicySurrenderApiResponse policySurrenderApiResponse = new PolicySurrenderApiResponse();
//
//		try {
//			CommonDocsTempEntity commonDocsTempEntity = modelMapper.map(docsDto, CommonDocsTempEntity.class);
//			commonDocsTempEntity.setIsActive(true);
//			CommonDocsTempEntity saveDocsTempEntity = commonDocsTempRepository.save(commonDocsTempEntity);
//			policySurrenderApiResponse.setDocsDto(modelMapper.map(saveDocsTempEntity, CommonDocsDto.class));
//			policySurrenderApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
//			policySurrenderApiResponse.setTransactionMessage(CommonConstants.SAVEMESSAGE);
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- PolicySurrenderServiceImpl-- uploadDocument --", e);
//			policySurrenderApiResponse.setTransactionMessage(CommonConstants.FAIL);
//			policySurrenderApiResponse.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("PolicySurrenderServieImpl:saveAndUpdatePolicySurrender:end");
//		return policySurrenderApiResponse;
//	}
//
//	@Override
//	public PolicySurrenderApiResponse saveNotesDetails(CommonNotesDto commonNotesDto) {
//		PolicySurrenderApiResponse commonResponseDto = new PolicySurrenderApiResponse();
//		try {
//			logger.info("PolicySurrenderServiceImpl:saveNotesDetails:Starts");
//			CommonNoteTempEntity commonNoteTempEntity = modelMapper.map(commonNotesDto, CommonNoteTempEntity.class);
//			CommonNoteTempEntity savePolicyNotesTempEntity = commonNotesTempRepository.save(commonNoteTempEntity);
//			commonResponseDto.setNotesDto(modelMapper.map(savePolicyNotesTempEntity, CommonNotesDto.class));
//			commonResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
//			commonResponseDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
//
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception:PolicySurrenderServiceImpl:saveNotesDetails	", e);
//			commonResponseDto.setTransactionMessage(CommonConstants.FAIL);
//			commonResponseDto.setTransactionStatus(CommonConstants.ERROR);
//		} finally {
//			logger.info("PolicySurrenderServiceImpl:saveNotesDetails:Ends");
//		}
//		return commonResponseDto;
//	}
//
//	@Override
//	public PolicySurrenderApiResponse inprogresOrExitingSurrender(String roleType, String pageName) {
//		PolicySurrenderApiResponse policySurrenderApiResponse = new PolicySurrenderApiResponse();
//		try {
//			logger.info("PolicySurrenderServieImpl:inprogressMergerLoad:Start");
//			List<?> policySurrenderTempList = new ArrayList<>();
//
//			if (roleType.equalsIgnoreCase(CommonConstants.MAKER)) {
//				policySurrenderTempList = (CommonConstants.EXISTING.equals(pageName))
//						? policySurrenderRepository.findAllBySurrenderStatusInAndIsActiveTrueOrderBySurrenderId(
//								PolicySurrenderContansts.existingMaker())
//						: policySurrenderTempRepository.findAllBySurrenderStatusInAndIsActiveTrueOrderBySurrenderId(
//								PolicySurrenderContansts.inprogressMaker());
//			} else if (roleType.equalsIgnoreCase(CommonConstants.CHECKER)) {
//				policySurrenderTempList = (CommonConstants.EXISTING.equals(pageName))
//						? policySurrenderRepository.findAllBySurrenderStatusInAndIsActiveTrueOrderBySurrenderId(
//								PolicySurrenderContansts.existingChecker())
//						: policySurrenderTempRepository.findAllBySurrenderStatusInAndIsActiveTrueOrderBySurrenderId(
//								PolicySurrenderContansts.inprogressChecker());
//			} else {
//				policySurrenderApiResponse.setTransactionStatus(CommonConstants.FAIL);
//				policySurrenderApiResponse.setTransactionMessage(CommonConstants.INVALIDREQUEST);
//				return policySurrenderApiResponse;
//			}
//			List<PolicySurrenderDto> policySurrenderDtos = mapList(policySurrenderTempList, PolicySurrenderDto.class);
//			policySurrenderApiResponse.setResponseObject(policySurrenderDtos);
//			policySurrenderApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
//			policySurrenderApiResponse.setTransactionMessage(CommonConstants.FETCH_MESSAGE);
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception:PolicySurrenderServiceImpl:saveNotesDetails	", e);
//			policySurrenderApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
//			policySurrenderApiResponse.setTransactionMessage(e.getMessage());
//		}
//		logger.info("PolicySurrenderServiceImpl:saveNotesDetails:Ends");
//		return policySurrenderApiResponse;
//	}
//
//	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
//		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
//	}
//
//	@Override
//	public PolicySurrenderApiResponse getPolicySurrenderbySurrenderId(Long surrenderId) {
//		logger.info("PolicySurrenderServieImpl:inprogressMergerLoad:Start");
//		PolicySurrenderApiResponse policySurrenderApiResponse = new PolicySurrenderApiResponse();
//		PolicySurrenderPayOutDto policySurrenderPayOutDto = null;
//		try {
//			if (surrenderId != null) {
//				Optional<PolicySurrenderTempEntity> policySurrenderTempEntity = policySurrenderTempRepository
//						.findAllBySurrenderIdAndIsActiveTrue(surrenderId);
//
//				if (policySurrenderTempEntity.isPresent()) {
//					PolicySurrenderTempEntity policySurrenderTempEntityList = policySurrenderTempEntity.get();
//					PolicySurrenderPayOutTempEntity policySurrenderPayOutTempEntity = policySurrenderPayOutTempRepository
//							.findBySurrenderIdAndIsActiveTrue(surrenderId);
//					if (policySurrenderPayOutTempEntity != null) {
//						policySurrenderPayOutDto = modelMapper.map(policySurrenderPayOutTempEntity,
//								PolicySurrenderPayOutDto.class);
//					}
//					List<CommonDocsTempEntity> saveDocsTempEntity = commonDocsTempRepository
//							.findAllBySurrenderId(surrenderId);
//					List<CommonDocsDto> policyCommonDocsList = mapList(saveDocsTempEntity, CommonDocsDto.class);
//
//					List<CommonNoteTempEntity> commonNoteTempEntityList = commonNotesTempRepository
//							.findAllBySurrenderId(surrenderId);
//					PolicySurrenderDto policySurrenderDto = modelMapper.map(policySurrenderTempEntityList,
//							PolicySurrenderDto.class);
//					List<CommonNotesDto> policyCommonNotesList = mapList(commonNoteTempEntityList,
//							CommonNotesDto.class);
//					policySurrenderApiResponse.setSurrenderDto(policySurrenderDto);
//					policySurrenderApiResponse.setPayOutDto(policySurrenderPayOutDto);
//					policySurrenderApiResponse.setCommonDocsDto(policyCommonDocsList);
//					policySurrenderApiResponse.setCommonNotesDto(policyCommonNotesList);
//					policySurrenderApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
//					policySurrenderApiResponse.setTransactionMessage(CommonConstants.FETCH_MESSAGE);
//					return policySurrenderApiResponse;
//				} else {
//					policySurrenderApiResponse.setTransactionStatus(CommonConstants.FAIL);
//					policySurrenderApiResponse.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
//					return policySurrenderApiResponse;
//				}
//
//			} else {
//				policySurrenderApiResponse.setTransactionStatus(CommonConstants.FAIL);
//				policySurrenderApiResponse.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
//				return policySurrenderApiResponse;
//			}
//		} catch (IllegalArgumentException e) {
//			policySurrenderApiResponse.setTransactionStatus(CommonConstants.FAIL);
//			policySurrenderApiResponse.setTransactionMessage(e.getMessage());
//
//		}
//		logger.info("PolicySurrenderServiceImpl:saveNotesDetails:Ends");
//		return policySurrenderApiResponse;
//
//	}
//
//	@Override
//	public PolicySurrenderApiResponse savePayoutDetails(PolicySurrenderPayOutDto policySurrenderPayOutDto) {
//		logger.info("PolicySurrenderServieImpl:savePayoutDetails:Start");
//		PolicySurrenderApiResponse policySurrenderApiResponse = new PolicySurrenderApiResponse();
//		try {
//			PolicySurrenderPayOutTempEntity policySurrenderPayOutTempEntity = modelMapper.map(policySurrenderPayOutDto,
//					PolicySurrenderPayOutTempEntity.class);
//			policySurrenderPayOutTempEntity.setIsActive(true);
//			policySurrenderPayOutTempEntity = policySurrenderPayOutTempRepository.save(policySurrenderPayOutTempEntity);
//			policySurrenderPayOutTempEntity.setCreatedBy(policySurrenderPayOutDto.getCreatedBy());
//			policySurrenderPayOutTempEntity.setCreatedOn(policySurrenderPayOutDto.getCreatedOn());
//			policySurrenderPayOutTempEntity.setModifiedBy(policySurrenderPayOutDto.getModifiedBy());
//			policySurrenderPayOutTempEntity.setModifiedOn(DateUtils.sysDate());
//
//			PolicySurrenderPayOutDto resPolicySurrenderPayOutDto = modelMapper.map(policySurrenderPayOutTempEntity,
//					PolicySurrenderPayOutDto.class);
//			policySurrenderApiResponse.setPayOutDto(resPolicySurrenderPayOutDto);
//			policySurrenderApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
//			policySurrenderApiResponse.setTransactionMessage(CommonConstants.SAVEMESSAGE);
//
//		} catch (IllegalArgumentException e) {
//			logger.info("PolicySurrenderServieImpl:savePayoutDetails:error");
//			policySurrenderApiResponse.setTransactionStatus(CommonConstants.FAIL);
//			policySurrenderApiResponse.setTransactionMessage(e.getMessage());
//		}
//		logger.info("PolicySurrenderServiceImpl:saveNotesDetails:Ends");
//		return policySurrenderApiResponse;
//	}
//
//	@Override
//	public PolicySurrenderApiResponse editPayoutDetails(PolicySurrenderPayOutDto policySurrenderPayOutDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public PolicySurrenderApiResponse getPolicySurrenderBasicDetailsById(Long surrenderId) {
//		logger.info("PolicySurrenderServieImpl:getPolicySurrenderBasicDetailsById:Start");
//		PolicySurrenderApiResponse commonResponseDto = new PolicySurrenderApiResponse();
//		try {
//			PolicySurrenderTempEntity policySurrenderTempEntity = policySurrenderTempRepository
//					.findBySurrenderIdAndIsActiveTrue(surrenderId);
//			if (policySurrenderTempEntity != null) {
//				PolicySurrenderDto policySurrenderDto = modelMapper.map(policySurrenderTempEntity,
//						PolicySurrenderDto.class);
//				commonResponseDto.setSurrenderDto(policySurrenderDto);
//				commonResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
//				commonResponseDto.setTransactionMessage(CommonConstants.FETCH_MESSAGE);
//			}
//		} catch (Exception e) {
//			logger.info("PolicySurrenderServieImpl:getPolicySurrenderBasicDetailsById:error");
//			commonResponseDto.setTransactionStatus(CommonConstants.FAIL);
//			commonResponseDto.setTransactionMessage(e.getMessage());
//		}
//		logger.info("PolicySurrenderServiceImpl:getPolicySurrenderBasicDetailsById:Ends");
//		return commonResponseDto;
//	}
//
//	@Override
//	public CommonResponseDto removeDocumentDetails(Long surrenderId, Integer docId, String modifiedBy) {
//		logger.info("PolicySurrenderServieImpl:getPolicySurrenderBasicDetailsById:Start");
//		CommonResponseDto commonResponseDto = new CommonResponseDto();
//		try {
//			CommonDocsTempEntity commonDocsTempEntity = commonDocsTempRepository
//					.findByDocIdAndSurrenderIdAndIsActiveTrue(docId, surrenderId);
//			if (commonDocsTempEntity != null) {
//				commonDocsTempEntity.setIsActive(false);
//				commonDocsTempEntity.setModifiedBy(modifiedBy);
//				commonDocsTempEntity.setModifiedOn(DateUtils.sysDate());
//				commonDocsTempRepository.save(commonDocsTempEntity);
//				commonResponseDto.setResponseData(null);
//				commonResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
//				commonResponseDto.setTransactionMessage(CommonConstants.DELETED);
//			}
//		} catch (Exception e) {
//			logger.info("PolicySurrenderServieImpl:getPolicySurrenderBasicDetailsById:error");
//			commonResponseDto.setTransactionStatus(CommonConstants.FAIL);
//			commonResponseDto.setTransactionMessage(e.getMessage());
//		}
//		logger.info("PolicySurrenderServiceImpl:getPolicySurrenderBasicDetailsById:Ends");
//		return commonResponseDto;
//	}
//
//	@Override
//	public CommonResponseDto getSurrenderNotesList(Long surrenderId) {
//		logger.info("PolicySurrenderServieImpl:getSurrenderNotesList:Start");
//		CommonResponseDto commonResponseDto = new CommonResponseDto();
//		try {
//			List<CommonNoteTempEntity> commonNoteTempEntityList = commonNotesTempRepository
//					.findAllBySurrenderId(surrenderId);
//			if (!commonNoteTempEntityList.isEmpty()) {
//				List<CommonNotesDto> policyCommonNotesList = mapList(commonNoteTempEntityList, CommonNotesDto.class);
//				commonResponseDto.setResponseData(policyCommonNotesList);
//				commonResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
//				commonResponseDto.setTransactionMessage(CommonConstants.DELETED);
//			}
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception:PolicySurrenderServiceImpl:getSurrenderNotesList	", e);
//			commonResponseDto.setTransactionStatus(CommonConstants.FAIL);
//			commonResponseDto.setTransactionMessage(e.getMessage());
//
//		}
//		logger.info("PolicySurrenderServiceImpl:getSurrenderNotesList:Ends");
//		return commonResponseDto;
//	}
}
