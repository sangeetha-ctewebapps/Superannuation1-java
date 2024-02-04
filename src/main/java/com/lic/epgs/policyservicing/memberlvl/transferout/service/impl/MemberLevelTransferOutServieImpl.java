package com.lic.epgs.policyservicing.memberlvl.transferout.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.lic.epgs.policy.dto.MemberMasterDto;
import com.lic.epgs.policy.entity.MemberContributionEntity;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.policyservicing.common.constants.PolicyServicingCommonConstants;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesEntity;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesTempEntity;
import com.lic.epgs.policyservicing.memberlvl.transferout.dto.TransferInAndOutDto;
import com.lic.epgs.policyservicing.memberlvl.transferout.dto.TransferInAndOutMemberDto;
import com.lic.epgs.policyservicing.memberlvl.transferout.dto.TransferInAndOutReqDto;
import com.lic.epgs.policyservicing.memberlvl.transferout.dto.TransferInAndOutResponseDto;
import com.lic.epgs.policyservicing.memberlvl.transferout.entity.TransferInAndOutMasterEntity;
import com.lic.epgs.policyservicing.memberlvl.transferout.entity.TransferInAndOutMasterTempEntity;
import com.lic.epgs.policyservicing.memberlvl.transferout.entity.TransferInAndOutMemberEntity;
import com.lic.epgs.policyservicing.memberlvl.transferout.entity.TransferInAndOutMemberTempEntity;
import com.lic.epgs.policyservicing.memberlvl.transferout.repository.TransferInAndOutMasterRepository;
import com.lic.epgs.policyservicing.memberlvl.transferout.repository.TransferInAndOutMasterTempRepository;
import com.lic.epgs.policyservicing.memberlvl.transferout.service.MemberLevelTransferOutService;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.DateUtils;

@Service
public class MemberLevelTransferOutServieImpl implements MemberLevelTransferOutService {
	protected final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PolicyMasterRepository policyMasterRepository;
	
	@Autowired
	private TransferInAndOutMasterTempRepository transferInAndOutMasterTempRepository;
	
	@Autowired
	private TransferInAndOutMasterRepository transferInAndOutMasterRepository;
	
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private MphMasterRepository mphMasterRepository;
	
	@Autowired
	private MemberMasterRepository memberMasterRepository;
	
	@Override
	public TransferInAndOutResponseDto saveAndUpdateMemberTransferOut(TransferInAndOutDto transferInAndOutDto) {
		logger.info("MemberLevelTransferOutServieImpl:saveAndUpdateMemberTransferOut:started");
		TransferInAndOutResponseDto response = new TransferInAndOutResponseDto();

		try {
			if (validationSrcAndDestinationPolicy(transferInAndOutDto.getSourcePolicyNo(),
					transferInAndOutDto.getDestPolicyNo(),response)) {
				
				TransferInAndOutMemberDto transferInAndOutMemberDto = transferInAndOutDto.getTransOutMembers();
				
				TransferInAndOutMasterTempEntity newOrExistingTransferTemp = null;
				if(transferInAndOutDto.getTrnsfrId() != null) {
					
					newOrExistingTransferTemp = transferInAndOutMasterTempRepository.findByTrnsfrIdAndIsActiveTrue(transferInAndOutDto.getTrnsfrId());
					
					if(transferInAndOutDto.getComponentLabel().equalsIgnoreCase("ServiceDetails")) {
						newOrExistingTransferTemp.setServiceId(transferInAndOutDto.getServiceId());
						newOrExistingTransferTemp.setSourcePolicyId(transferInAndOutDto.getSourcePolicyId());
						newOrExistingTransferTemp.setSourcePolicyNo(transferInAndOutDto.getSourcePolicyNo());
						newOrExistingTransferTemp.setDestPolicyId(transferInAndOutDto.getDestPolicyId());
						newOrExistingTransferTemp.setDestPolicyNo(transferInAndOutDto.getDestPolicyNo());
						newOrExistingTransferTemp.setInterestAccrued(transferInAndOutDto.getInterestAccrued());
						newOrExistingTransferTemp.setModifiedBy(transferInAndOutDto.getCreatedBy());
						newOrExistingTransferTemp.setModifiedOn(new Date());
						
						newOrExistingTransferTemp.setCreatedBy(transferInAndOutDto.getCreatedBy());
						newOrExistingTransferTemp.setCreatedOn(new Date());;
						
						newOrExistingTransferTemp.setWorkflowStatus(transferInAndOutDto.getWorkflowStatus());
						newOrExistingTransferTemp.setTrnsfrAmount(transferInAndOutDto.getTrnsfrAmount());
						
						
						TransferInAndOutMemberTempEntity transferMemberTemp = newOrExistingTransferTemp.getTransOutMembers();
						transferMemberTemp.setModifiedBy(transferInAndOutMemberDto.getCreatedBy());
						transferMemberTemp.setModifiedOn(new Date());
						transferMemberTemp.setCreatedBy(transferInAndOutMemberDto.getCreatedBy());
						transferMemberTemp.setCreatedOn(new Date());
						
						transferMemberTemp.setMemberId(transferInAndOutMemberDto.getMemberId());

						transferMemberTemp.setMemberName(transferInAndOutMemberDto.getMemberName());
						transferMemberTemp.setSourcePolicyId(transferInAndOutMemberDto.getSourcePolicyId());
						transferMemberTemp.setSourcePolicyNo(transferInAndOutMemberDto.getSourcePolicyNo());
						transferMemberTemp.setDestPolicyId(transferInAndOutMemberDto.getDestPolicyId());
						transferMemberTemp.setDestPolicyNo(transferInAndOutMemberDto.getDestPolicyNo());
						transferMemberTemp.setMphName(transferInAndOutMemberDto.getMphName());
						transferMemberTemp.setProductId(transferInAndOutMemberDto.getProductId());
						transferMemberTemp.setProdVariant(transferInAndOutMemberDto.getProdVariant());
						transferMemberTemp.setCategory(transferInAndOutMemberDto.getCategory());
						transferMemberTemp.setEmployeeContribution(transferInAndOutMemberDto.getEmployeeContribution());
						transferMemberTemp.setEmployerContribution(transferInAndOutMemberDto.getEmployerContribution());
						transferMemberTemp.setVoluntaryContribution(transferInAndOutMemberDto.getVoluntaryContribution());
						transferMemberTemp.setTrnsfrAmount(transferInAndOutMemberDto.getTrnsfrAmount());
						transferMemberTemp.setTransferoutTemp(newOrExistingTransferTemp);
						
						transferMemberTemp.setMemberNo(transferInAndOutMemberDto.getMemberNo());;
						transferMemberTemp.setAadharNumber(transferInAndOutMemberDto.getAadharNumber());
						transferMemberTemp.setPhone(transferInAndOutMemberDto.getPhone());
						transferMemberTemp.setPan(transferInAndOutMemberDto.getPan());
						
					}else if(transferInAndOutDto.getComponentLabel().equalsIgnoreCase("Transfer_Notes")) {
						
						if(!transferInAndOutDto.getNotes().isEmpty() && transferInAndOutDto.getNotes() !=null) {
							List<PolicyServiceNotesTempEntity> notesList = newOrExistingTransferTemp.getNotes();
							for(PolicyServiceNotesDto notesDto : transferInAndOutDto.getNotes()) {
								PolicyServiceNotesTempEntity notesEntity = modelMapper.map(notesDto,PolicyServiceNotesTempEntity.class);
								notesEntity.setTrnsfrId(newOrExistingTransferTemp.getTrnsfrId());
								notesEntity.setModifiedBy(transferInAndOutMemberDto.getCreatedBy());
								notesEntity.setModifiedOn(new Date());
								notesEntity.setCreatedBy(transferInAndOutMemberDto.getCreatedBy());
								notesEntity.setCreatedOn(new Date());
								notesList.add(notesEntity);
								}
							newOrExistingTransferTemp.setNotes(notesList);
						}
					}
					
					newOrExistingTransferTemp = transferInAndOutMasterTempRepository.save(newOrExistingTransferTemp);
					

					
				}else {
					
					newOrExistingTransferTemp  = modelMapper.map(transferInAndOutDto,TransferInAndOutMasterTempEntity.class);
					newOrExistingTransferTemp.setCreatedOn(new Date());
					newOrExistingTransferTemp.setIsActive(Boolean.TRUE);
					newOrExistingTransferTemp.setTrnsfrStatus(PolicyConstants.DRAFT_NO);
		
					TransferInAndOutMemberTempEntity transferMemberTemp = newOrExistingTransferTemp.getTransOutMembers();
					transferMemberTemp.setCreatedOn(new Date());
					transferMemberTemp.setCreatedBy(newOrExistingTransferTemp.getCreatedBy());
					transferMemberTemp.setTransferoutTemp(newOrExistingTransferTemp);
					newOrExistingTransferTemp.setCreatedOn(DateUtils.sysDate());

					newOrExistingTransferTemp = transferInAndOutMasterTempRepository.save(newOrExistingTransferTemp);
					
				}

				response.setResponseData(modelMapper.map(newOrExistingTransferTemp, TransferInAndOutDto.class));
				response.setTransactionStatus(CommonConstants.SUCCESS);
				response.setTransactionMessage(CommonConstants.SAVEMESSAGE);
			}
			
		} catch (IllegalArgumentException e) {
			logger.info("MemberLevelTransferOutServieImpl:saveAndUpdateMemberTransferOut:error");
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionMessage(e.getMessage());
		}
		logger.info("MemberLevelTransferOutServieImpl:saveAndUpdateMemberTransferOut:End");
		return response;
	}
	
	
	
	
	public Boolean validationSrcAndDestinationPolicy(String srcPolicyNo, String desPolicyNo,
			TransferInAndOutResponseDto response) {
		
		if (StringUtils.isNotBlank(srcPolicyNo) && StringUtils.isNotBlank(desPolicyNo)) {
			PolicyMasterEntity srcPolicyEntity = policyMasterRepository.findByPolicyNumberAndPolicyStatusAndIsActiveTrue(srcPolicyNo, CommonConstants.COMMON_APPROVED);
			if (srcPolicyEntity == null) {
				response.setTransactionStatus(CommonConstants.FAIL);
				response.setTransactionMessage(PolicyServicingCommonConstants.INVALID_SRC_POLICY + ":-" + srcPolicyNo);
				return false;
			}
			PolicyMasterEntity desPolicyEntity = policyMasterRepository.findByPolicyNumberAndPolicyStatusAndIsActiveTrue(srcPolicyNo, CommonConstants.COMMON_APPROVED);
			if (desPolicyEntity == null) {
				response.setTransactionStatus(CommonConstants.FAIL);
				response.setTransactionMessage(PolicyServicingCommonConstants.INVALID_DES_POLICY + ":-" + desPolicyNo);
				return false;
			}
			if (srcPolicyEntity.getPolicyNumber() == desPolicyEntity.getPolicyNumber()) {
				response.setTransactionStatus(CommonConstants.FAIL);
				response.setTransactionMessage(CommonConstants.VAlIDATE_POLICY);
				return false;
			}

		}
		return true;

	}
	
	
	

	@Override
	public TransferInAndOutResponseDto getOverallDetails(TransferInAndOutReqDto reqDto) {
		logger.info("MemberLevelTransferOutServieImpl:getOverallDetails:started");
		TransferInAndOutResponseDto response = new TransferInAndOutResponseDto();
		try {
			
			
			if(reqDto.getPageName() != null) {
				
				if(reqDto.getPageName().equalsIgnoreCase("transfer-search")) {
					
					PolicyMasterEntity policyEntity = policyMasterRepository.findByPolicyIdAndIsActiveTrue(reqDto.getPolicyId());
					if(policyEntity ==null) {
						response.setTransactionStatus(CommonConstants.FAIL);
						response.setTransactionMessage(PolicyServicingCommonConstants.INVALID_SRC_POLICY);
					}else if(StringUtils.isNotBlank(policyEntity.getPolicyType())&& policyEntity.getPolicyType().equals("DB")) {
						response.setTransactionStatus(CommonConstants.FAIL);
						response.setTransactionMessage(PolicyServicingCommonConstants.INVALID_QUATATION_TYPE);
					}else {
						MemberMasterEntity memberEntity = memberMasterRepository.
								findByMembershipNumberAndPolicyIdAndIsActiveTrueAndIsZeroidFalse(reqDto.getMemberShipId(),reqDto.getPolicyId());
						
						TransferInAndOutMemberDto dto = new TransferInAndOutMemberDto();
						dto.setMemberShipId((memberEntity != null)?memberEntity.getMembershipNumber():null);
						dto.setProductId(policyEntity.getProductId());
						dto.setCategory(policyEntity.getNoOfCategory() !=null ? Long.valueOf(policyEntity.getNoOfCategory()) :null);
						
						MphMasterEntity mphEntity = mphMasterRepository.findByMphIdAndIsActiveTrue(policyEntity.getMphId()); 
						dto.setMphName((mphEntity !=null )?mphEntity.getMphName():null);
						dto.setProdVariant((StringUtils.isNotBlank(policyEntity.getVariant()))?Long.valueOf(policyEntity.getVariant()):null);
						dto.setUnitCode(policyEntity.getUnitId());
						
						
						if(memberEntity.getMemberContribution() != null) {
							for(MemberContributionEntity contribution : memberEntity.getMemberContribution()) {
								
								dto.setEmployeeContribution(contribution.getEmployeeContribution());
								dto.setEmployerContribution(contribution.getEmployerContribution());
								dto.setVoluntaryContribution(contribution.getVoluntaryContribution());
								
							}
						}
						
						if(memberEntity.getFirstName() !=null && memberEntity.getMiddleName() != null && memberEntity.getLastName() != null){
							dto.setMemberName(memberEntity.getFirstName()+" "+memberEntity.getMiddleName()+" "+memberEntity.getLastName());
						}else if(memberEntity.getFirstName() !=null && memberEntity.getMiddleName() != null){
							dto.setMemberName(memberEntity.getFirstName()+" "+memberEntity.getMiddleName());
						}else{
							dto.setMemberName(memberEntity.getFirstName());
							
						}
						
						
						
						response.setResponseData(dto);
						response.setTransactionStatus(CommonConstants.SUCCESS);
						response.setTransactionMessage(CommonConstants.FETCH);
						
						
//						if(memberEntity !=null) {
//							response.setResponseData( modelMapper.map(memberEntity,MemberMasterDto.class));
//							response.setTransactionStatus(CommonConstants.SUCCESS);
//							response.setTransactionMessage(CommonConstants.FETCH);
//						} else {
//							response.setTransactionStatus(CommonConstants.FAIL);
//							response.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
//						}
						
					}

					
				}else if(reqDto.getTrnsfrId() != null && reqDto.getPageName().equalsIgnoreCase("existing")) {
					
					TransferInAndOutMasterEntity transferInOut = transferInAndOutMasterRepository.findByTrnsfrIdAndIsActiveTrue(reqDto.getTrnsfrId());
					
					if(transferInOut != null && reqDto.getPageName().equalsIgnoreCase("existing")) {
						response.setResponseData( modelMapper.map(transferInOut,TransferInAndOutDto.class));
						response.setTransactionStatus(CommonConstants.SUCCESS);
						response.setTransactionMessage(CommonConstants.FETCH);
					} else {
						response.setTransactionStatus(CommonConstants.FAIL);
						response.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
					}
					
					
				}else if(reqDto.getTrnsfrId() != null && reqDto.getPageName().equalsIgnoreCase("inprogress")) {
					
					TransferInAndOutMasterTempEntity transferInOutTemp = transferInAndOutMasterTempRepository.findByTrnsfrIdAndIsActiveTrue(reqDto.getTrnsfrId());
					
					if (transferInOutTemp != null && reqDto.getPageName().equalsIgnoreCase("inprogress")) {
						response.setResponseData( modelMapper.map(transferInOutTemp,TransferInAndOutDto.class));
						response.setTransactionStatus(CommonConstants.SUCCESS);
						response.setTransactionMessage(CommonConstants.FETCH);
					} else {
						response.setTransactionStatus(CommonConstants.FAIL);
						response.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
					}
					
				}
				
			}else {
				response.setTransactionStatus(CommonConstants.FAIL);
				response.setTransactionMessage("Page Name is Null!!");
			}

//			if(transferInAndOutReqDto.getTrnsfrId() != null && !StringUtils.isEmpty(transferInAndOutReqDto.getPageName())) {
//				
//				TransferInAndOutMasterEntity transferInOut = transferInAndOutMasterRepository.findByTrnsfrIdAndIsActiveTrue(transferInAndOutReqDto.getTrnsfrId());
//				
//				if(transferInOut != null && transferInAndOutReqDto.getPageName().equalsIgnoreCase("existing")) {
//					response.setResponseData( modelMapper.map(transferInOut,TransferInAndOutDto.class));
//					response.setTransactionStatus(CommonConstants.SUCCESS);
//					response.setTransactionMessage(CommonConstants.FETCH);
//				}else{
//					TransferInAndOutMasterTempEntity transferInOutTemp = transferInAndOutMasterTempRepository.findByTrnsfrIdAndIsActiveTrue(transferInAndOutReqDto.getTrnsfrId());
//					if (transferInOutTemp != null && transferInAndOutReqDto.getPageName().equalsIgnoreCase("inprogress")) {
//						response.setResponseData( modelMapper.map(transferInOutTemp,TransferInAndOutDto.class));
//						response.setTransactionStatus(CommonConstants.SUCCESS);
//						response.setTransactionMessage(CommonConstants.FETCH);
//					} else {
//						response.setTransactionStatus(CommonConstants.FAIL);
//						response.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
//					}
//				}
//			}else {
//				response.setTransactionStatus(CommonConstants.FAIL);
//				response.setTransactionMessage("Transfer Id and Page Name is Null!!");
//			}
		} catch (IllegalArgumentException e) {
			logger.info("MemberLevelTransferOutServieImpl:getOverallDetails:Exception --> ",e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionMessage(CommonConstants.EXCEPTION+" "+e);
		}
		logger.info("MemberLevelTransferOutServieImpl:getOverallDetails:End");
		return response;
	}
	
	
	
	
	
	
	

	@Override
	public TransferInAndOutResponseDto memberSearch(TransferInAndOutReqDto memberSearchDto) {
		
		logger.info("MemberLevelTransferOutServieImpl:memberSearch:Start");
		
		List<MemberMasterEntity> policyMbrEntity = new ArrayList<>();
		List<MemberMasterDto> policyMbrDtoList = new ArrayList<>();
		
		
		TransferInAndOutResponseDto memberSearchApiResponse = new TransferInAndOutResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<MemberMasterEntity> createQuery = criteriaBuilder.createQuery(MemberMasterEntity.class);
			Root<MemberMasterEntity> root = createQuery.from(MemberMasterEntity.class);

			if (StringUtils.isNotBlank(memberSearchDto.getMemberShipNumber())) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("membershipNumber")),
						memberSearchDto.getMemberShipNumber().toLowerCase()));
			}
			if (StringUtils.isNotBlank(memberSearchDto.getPhone())) {
				predicates.add(criteriaBuilder.equal(root.get("phone"), memberSearchDto.getPhone()));
			}
			if (StringUtils.isNotBlank(memberSearchDto.getPan())) {
				predicates.add(criteriaBuilder.equal(root.get("memberPan"), memberSearchDto.getPan()));
			}

			if (memberSearchDto.getAadharNumber() != null) {
				predicates.add(criteriaBuilder.equal(root.get("aadharNumber"), (memberSearchDto.getAadharNumber())));
			}
			
			if (StringUtils.isNotBlank(memberSearchDto.getFirstname())) {
				predicates.add(criteriaBuilder.equal(root.get("firstName"), memberSearchDto.getFirstname()));
			}

			
			if (StringUtils.isNotBlank(memberSearchDto.getPolicyFrom())
					&& StringUtils.isNotBlank(memberSearchDto.getPolicyTo())) {
				Date fromDate = CommonDateUtils.convertStringToDate(memberSearchDto.getPolicyFrom());
				Date toDate = CommonDateUtils.convertStringToDate(memberSearchDto.getPolicyTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get("createdOn"), fromDate, toDate));
			}
			if (StringUtils.isNotBlank(memberSearchDto.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(root.get("policyNumber"), memberSearchDto.getPolicyNumber()));
			}
			
			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			policyMbrEntity = entityManager.createQuery(createQuery).getResultList();
	
			List<MemberMasterDto> policySearchResDto = mapList(policyMbrEntity, MemberMasterDto.class);

			memberSearchApiResponse.setResponseData(policySearchResDto);
			memberSearchApiResponse.setTransactionMessage(CommonConstants.FETCH);
			memberSearchApiResponse.setTransactionStatus(CommonConstants.STATUS);
		} catch (IllegalArgumentException e) {
			logger.error("Exception:MemberLevelTransferOutServieImpl:memberSearch", e);
			memberSearchApiResponse.setTransactionStatus(CommonConstants.FAIL);
			memberSearchApiResponse.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("MemberLevelTransferOutServieImpl:memberSearch:Ends");
		}
		return memberSearchApiResponse;
	}
	
	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
	}
	
	
	
	
	
	
	
	@Override
	public TransferInAndOutResponseDto sendToChecker(TransferInAndOutDto dto) {
		logger.info("MemberLevelTransferOutServieImpl:sendToChecker:started");
		TransferInAndOutResponseDto response = new TransferInAndOutResponseDto();
		try {
			
			if(dto.getUnitCode() != null && dto.getTrnsfrId() != null) {
				
				TransferInAndOutMasterTempEntity transTemp = transferInAndOutMasterTempRepository.findByTrnsfrIdAndUnitCodeAndIsActiveTrue(dto.getTrnsfrId(),
						dto.getUnitCode());
				if(transTemp != null) {
					
					if(transTemp.getTrnsfrStatus().equals(PolicyServicingCommonConstants.TRANSFER_OUT_DRAFT_STATUS) ||
							transTemp.getTrnsfrStatus().equals(PolicyServicingCommonConstants.TRANSFER_OUT_PENDING_FOR_MODIFICATION_STATUS)) {
						
						transTemp.setTrnsfrStatus(PolicyServicingCommonConstants.TRANSFER_OUT_PENDING_FOR_APPROVER_STATUS);
						transTemp.setModifiedBy(dto.getModifiedBy());
						transTemp.setModifiedOn(new Date());
						
						transTemp = transferInAndOutMasterTempRepository.save(transTemp);
	
						response.setTransactionStatus(CommonConstants.SUCCESS);
						response.setTransactionMessage(PolicyServicingCommonConstants.TRANSFER_OUT_APPROVED+
								" for "+transTemp.getTransOutMembers().getMemberShipId());
						
					}
					
				}else {
					response.setTransactionStatus(CommonConstants.FAIL);
					response.setTransactionMessage(CommonConstants.NO_RECORD_FOUND+" against Transfer Id ("+dto.getTrnsfrId()+") and Unit Code("+dto.getUnitCode()+")");
				}
				
			}else {
				response.setTransactionStatus(CommonConstants.FAIL);
				response.setTransactionMessage("Transfer Id and Unit Code is Null!!");
			}
			
			
		} catch (IllegalArgumentException e) {
			logger.info("MemberLevelTransferOutServieImpl:sendToChecker:Exception --> ",e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionMessage(CommonConstants.EXCEPTION+" "+e);
		}
		logger.info("MemberLevelTransferOutServieImpl:sendToChecker:End");
		return response;
	}
	
	
	
	@Override
	public TransferInAndOutResponseDto sendToMaker(TransferInAndOutReqDto reqDto) {
		logger.info("MemberLevelTransferOutServieImpl:sendToMaker:started");
		TransferInAndOutResponseDto response = new TransferInAndOutResponseDto();
		try {
			
			if(reqDto.getUnitCode() != null && reqDto.getTrnsfrId() != null) {
				
				TransferInAndOutMasterTempEntity transTemp = transferInAndOutMasterTempRepository
						.findByTrnsfrIdAndUnitCodeAndIsActiveTrue(reqDto.getTrnsfrId(),reqDto.getUnitCode());
				
				if(transTemp != null) {
					
					if (transTemp.getTrnsfrStatus().equals(CommonConstants.COMMON_PENDING_FOR_APPROVER)) {
						transTemp.setTrnsfrStatus(CommonConstants.COMMON_PENDING_FOR_MODIFICATION);
						transTemp.setIsActive(true);
						transTemp.setModifiedOn(new Date());

						transTemp = transferInAndOutMasterTempRepository.save(transTemp);
						response.setTransactionStatus(CommonConstants.SUCCESS);
						response.setTransactionMessage(transTemp.getTransOutMembers().getMemberShipId()
								+ PolicyServicingCommonConstants.TRANSFER_OUT_ON_HOLD);
					}
					
				}else {
					response.setTransactionStatus(CommonConstants.FAIL);
					response.setTransactionMessage(CommonConstants.NO_RECORD_FOUND+" against Transfer Id ("+reqDto.getTrnsfrId()+") and "
							+ "Unit Code("+reqDto.getUnitCode()+")");
				}
				
			}else {
				response.setTransactionStatus(CommonConstants.FAIL);
				response.setTransactionMessage("Transfer Id and Unit Code is Null!!");
			}
			
			
		} catch (IllegalArgumentException e) {
			logger.info("MemberLevelTransferOutServieImpl:sendToMaker:Exception --> ",e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionMessage(CommonConstants.EXCEPTION+" "+e);
		}
		logger.info("MemberLevelTransferOutServieImpl:sendToMaker:End");
		return response;
	}
	
	

	
	
	@Override
	public TransferInAndOutResponseDto approve(TransferInAndOutReqDto reqDto) {
		logger.info("MemberLevelTransferOutServieImpl:approve:started");
		TransferInAndOutResponseDto response = new TransferInAndOutResponseDto();
		try {
			
			if(reqDto.getUnitCode() != null && reqDto.getTrnsfrId() != null) {
				
				TransferInAndOutMasterTempEntity transTemp = transferInAndOutMasterTempRepository
						.findByTrnsfrIdAndUnitCodeAndIsActiveTrue(reqDto.getTrnsfrId(),reqDto.getUnitCode());
				
				if(transTemp != null) {
					
					if(transTemp.getSrcUnitCode().equalsIgnoreCase(transTemp.getDesUnitCode())) {
						
					}
					
					transTemp.setTrnsfrStatus(PolicyServicingCommonConstants.TRANSFER_OUT_APPROVED_STATUS);
					transTemp.setModifiedBy(reqDto.getModifiedBy());
					transTemp.setModifiedOn(new Date());
					transTemp = transferInAndOutMasterTempRepository.save(transTemp);					
					
					TransferInAndOutMasterEntity transMaster  = this.setTransferTempToMaster(transTemp);
					transMaster = this.setMemberTempToMaster(transMaster, transTemp);
					transMaster = this.setNotesTempToMaster(transMaster);

					transferInAndOutMasterRepository.save(transMaster);
					
					response.setTransactionStatus(CommonConstants.SUCCESS);
					response.setTransactionMessage(transTemp.getTransOutMembers().getMemberShipId()
							+ PolicyServicingCommonConstants.TRANSFER_OUT_APPROVED);
					
					
				}else {
					response.setTransactionStatus(CommonConstants.FAIL);
					response.setTransactionMessage(CommonConstants.NO_RECORD_FOUND+" against Transfer Id ("+reqDto.getTrnsfrId()+") and "
							+ "Unit Code("+reqDto.getUnitCode()+")");
				}
				
			}else {
				response.setTransactionStatus(CommonConstants.FAIL);
				response.setTransactionMessage("Transfer Id and Unit Code is Null!!");
			}
			
			
		} catch (IllegalArgumentException e) {
			logger.info("MemberLevelTransferOutServieImpl:approve:Exception --> ",e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionMessage(CommonConstants.EXCEPTION+" "+e);
		}
		logger.info("MemberLevelTransferOutServieImpl:approve:End");
		return response;
	}
	
	
	
	
	
	private TransferInAndOutMasterEntity setTransferTempToMaster(TransferInAndOutMasterTempEntity transTemp) {
		
		TransferInAndOutMasterEntity transMaster = new TransferInAndOutMasterEntity();
		transMaster.setServiceId(transTemp.getServiceId());
		transMaster.setServiceNo(transTemp.getServiceNo());
		transMaster.setSourcePolicyId(transTemp.getSourcePolicyId());
		transMaster.setSourcePolicyNo(transTemp.getSourcePolicyNo());
		transMaster.setDestPolicyId(transTemp.getDestPolicyId());
		transMaster.setDestPolicyNo(transTemp.getDestPolicyNo());
		transMaster.setInterestAccrued(transTemp.getInterestAccrued());
		transMaster.setCreatedBy(transTemp.getCreatedBy());
		transMaster.setCreatedOn(new Date());
		transMaster.setModifiedBy(transTemp.getModifiedBy());	
		transMaster.setModifiedOn(new Date());
		transMaster.setTrnsfrStatus(PolicyServicingCommonConstants.TRANSFER_OUT_APPROVED_STATUS);
		transMaster.setWorkflowStatus(transTemp.getWorkflowStatus());
		transMaster.setTrnsfrAmount(transTemp.getTrnsfrAmount());
		transMaster.setIsActive(Boolean.TRUE);
		transMaster.setUnitCode(transTemp.getUnitCode());
		transMaster.setSrcUnitCode(transTemp.getSrcUnitCode());
		transMaster.setDesUnitCode(transTemp.getDesUnitCode());
		transMaster.setReqReceivedDate(transTemp.getReqReceivedDate());
		transMaster.setServiceEffectiveDate(transTemp.getServiceEffectiveDate());
		transMaster.setRejectionReasonCode(transTemp.getRejectionReasonCode());
		transMaster.setRejectionRemarks(transTemp.getRejectionRemarks());		
		return transMaster;
	}
	
	
	
	
	private TransferInAndOutMasterEntity setMemberTempToMaster(TransferInAndOutMasterEntity transMaster,TransferInAndOutMasterTempEntity transTemp) {
		
		TransferInAndOutMemberTempEntity memberTemp = transTemp.getTransOutMembers();
		if(memberTemp != null) {
			TransferInAndOutMemberEntity memberMaster =  new TransferInAndOutMemberEntity();
			memberMaster.setCreatedBy(memberTemp.getCreatedBy());
			memberMaster.setCreatedOn(new Date());
			memberMaster.setModifiedBy(memberTemp.getModifiedBy());;
			memberMaster.setModifiedOn(new Date());
			memberMaster.setIsActive(Boolean.TRUE);
			memberMaster.setMemberId(memberTemp.getMemberId());
			memberMaster.setMemberNo(memberTemp.getMemberNo());
			memberMaster.setMemberName(memberTemp.getMemberName());
			memberMaster.setSourcePolicyId(memberTemp.getSourcePolicyId());
			memberMaster.setSourcePolicyNo(memberTemp.getSourcePolicyNo());
			memberMaster.setDestPolicyId(memberTemp.getDestPolicyId());
			memberMaster.setDestPolicyNo(memberTemp.getDestPolicyNo());
			memberMaster.setMphName(memberTemp.getMphName());
			memberMaster.setProductId(memberTemp.getProductId());
			memberMaster.setProdVariant(memberTemp.getProdVariant());
			memberMaster.setCategory(memberTemp.getCategory());
			memberMaster.setEmployeeContribution(memberTemp.getEmployeeContribution());
			memberMaster.setEmployerContribution(memberTemp.getEmployerContribution());
			memberMaster.setVoluntaryContribution(memberTemp.getVoluntaryContribution());
			memberMaster.setTrnsfrAmount(memberTemp.getTrnsfrAmount());
			memberMaster.setMemberShipId(memberTemp.getMemberShipId());
			memberMaster.setPan(memberTemp.getPan());
			memberMaster.setAadharNumber(memberTemp.getAadharNumber());
			memberMaster.setPhone(memberTemp.getPhone());
			memberMaster.setTransferout(transMaster);
			transMaster.setTransOutMembers(memberMaster);
		}
		return transMaster;
	}
	
	
	
	private TransferInAndOutMasterEntity setNotesTempToMaster(TransferInAndOutMasterEntity transMaster) {
		
		if(transMaster.getNotes() != null) {
			List<PolicyServiceNotesEntity> noteList = new ArrayList<>();
			transMaster.getNotes().forEach(note->{
				PolicyServiceNotesEntity noteEntity = new PolicyServiceNotesEntity();
				noteEntity.setServiceId(note.getServiceId());
				noteEntity.setPolicyId(note.getPolicyId());
				noteEntity.setNoteContents(note.getNoteContents());
				noteEntity.setCreatedBy(note.getCreatedBy());
				noteEntity.setCreatedOn(new Date());
				noteEntity.setModifiedBy(note.getModifiedBy());
				noteEntity.setModifiedOn(new Date());
				noteEntity.setIsActive(Boolean.TRUE);
				noteEntity.setTrnsfrId(transMaster.getTrnsfrId());
				noteList.add(note);
			});					
			transMaster.setNotes(noteList);
		}
		return transMaster;
	}
	
	
	
	

	@Override
	public TransferInAndOutResponseDto getPolicyDetails(TransferInAndOutReqDto dto) {
		logger.info("MemberLevelTransferOutServieImpl:getPolicyDetails:Start");
		TransferInAndOutResponseDto commonDto = new TransferInAndOutResponseDto();
		try {
			PolicyMasterEntity policyEntity = policyMasterRepository.findByPolicyNumberAndIsActiveTrue(dto.getPolicyNumber());
			if(policyEntity !=null) {
				
				MphMasterEntity mphEntity = mphMasterRepository.findByMphIdAndIsActiveTrue(policyEntity.getMphId());

				commonDto.setResponseData(mphEntity);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
				commonDto.setTransactionStatus(CommonConstants.STATUS);
			}else {
				commonDto.setTransactionStatus(CommonConstants.FAIL);
				commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
			}

		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:getPolicyDetails", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionMessage(CommonConstants.EXCEPTION+" "+e);
		} finally {
			logger.info("MemberLevelTransferOutServieImpl:getPolicyDetails:End");
		}
		return commonDto;
	}
	
	
	
	
	
	@Override
	public TransferInAndOutResponseDto reject(TransferInAndOutReqDto reqDto) {
		logger.info("MemberLevelTransferOutServieImpl:reject:Start");
		TransferInAndOutResponseDto response = new TransferInAndOutResponseDto();
		try {
			
			if(reqDto.getUnitCode() != null && reqDto.getTrnsfrId() != null) {
				
				TransferInAndOutMasterTempEntity transTemp = transferInAndOutMasterTempRepository
						.findByTrnsfrIdAndUnitCodeAndIsActiveTrue(reqDto.getTrnsfrId(),reqDto.getUnitCode());
				
				if(transTemp != null) {
					
					transTemp.setTrnsfrStatus(PolicyServicingCommonConstants.TRANSFER_OUT_REJECTED_STATUS);
					transTemp.setModifiedBy(reqDto.getModifiedBy());
					transTemp.setModifiedOn(new Date());
					transTemp = transferInAndOutMasterTempRepository.save(transTemp);					
					transTemp.setTrnsfrId(null);
					

					TransferInAndOutMasterEntity transMaster = new TransferInAndOutMasterEntity();
					
					transMaster.setServiceId(transTemp.getServiceId());
					transMaster.setServiceNo(transTemp.getServiceNo());
					transMaster.setSourcePolicyId(transTemp.getSourcePolicyId());
					transMaster.setSourcePolicyNo(transTemp.getSourcePolicyNo());
					transMaster.setDestPolicyId(transTemp.getDestPolicyId());
					transMaster.setDestPolicyNo(transTemp.getDestPolicyNo());
					transMaster.setInterestAccrued(transTemp.getInterestAccrued());
					transMaster.setCreatedBy(transTemp.getCreatedBy());
					transMaster.setCreatedOn(new Date());
					transMaster.setModifiedBy(transTemp.getModifiedBy());	
					transMaster.setModifiedOn(new Date());
					transMaster.setTrnsfrStatus(PolicyServicingCommonConstants.TRANSFER_OUT_REJECTED_STATUS);
					transMaster.setWorkflowStatus(transTemp.getWorkflowStatus());
					transMaster.setTrnsfrAmount(transTemp.getTrnsfrAmount());
					transMaster.setIsActive(Boolean.TRUE);
					transMaster.setUnitCode(transTemp.getUnitCode());
					transMaster.setSrcUnitCode(transTemp.getSrcUnitCode());
					transMaster.setDesUnitCode(transTemp.getDesUnitCode());
					transMaster.setReqReceivedDate(transTemp.getReqReceivedDate());
					transMaster.setServiceEffectiveDate(transTemp.getServiceEffectiveDate());
					transMaster.setRejectionReasonCode(transTemp.getRejectionReasonCode());
					transMaster.setRejectionRemarks(transTemp.getRejectionRemarks());					
					
					TransferInAndOutMemberTempEntity memberTemp = transTemp.getTransOutMembers();
					if(memberTemp != null) {
						TransferInAndOutMemberEntity memberMaster =  new TransferInAndOutMemberEntity();
						memberMaster.setCreatedBy(memberTemp.getCreatedBy());
						memberMaster.setCreatedOn(new Date());
						memberMaster.setModifiedBy(memberTemp.getModifiedBy());;
						memberMaster.setModifiedOn(new Date());
						memberMaster.setIsActive(Boolean.TRUE);
						memberMaster.setMemberId(memberTemp.getMemberId());
						memberMaster.setMemberNo(memberTemp.getMemberNo());
						memberMaster.setMemberName(memberTemp.getMemberName());
						memberMaster.setSourcePolicyId(memberTemp.getSourcePolicyId());
						memberMaster.setSourcePolicyNo(memberTemp.getSourcePolicyNo());
						memberMaster.setDestPolicyId(memberTemp.getDestPolicyId());
						memberMaster.setDestPolicyNo(memberTemp.getDestPolicyNo());
						memberMaster.setMphName(memberTemp.getMphName());
						memberMaster.setProductId(memberTemp.getProductId());
						memberMaster.setProdVariant(memberTemp.getProdVariant());
						memberMaster.setCategory(memberTemp.getCategory());
						memberMaster.setEmployeeContribution(memberTemp.getEmployeeContribution());
						memberMaster.setEmployerContribution(memberTemp.getEmployerContribution());
						memberMaster.setVoluntaryContribution(memberTemp.getVoluntaryContribution());
						memberMaster.setTrnsfrAmount(memberTemp.getTrnsfrAmount());
						memberMaster.setMemberShipId(memberTemp.getMemberShipId());
						memberMaster.setPan(memberTemp.getPan());
						memberMaster.setAadharNumber(memberTemp.getAadharNumber());
						memberMaster.setPhone(memberTemp.getPhone());
						memberMaster.setTransferout(transMaster);
						
						transMaster.setTransOutMembers(memberMaster);
					}
					
					if(transMaster.getNotes() != null) {
						List<PolicyServiceNotesEntity> noteList = new ArrayList<>();
						transMaster.getNotes().forEach(note->{
							PolicyServiceNotesEntity noteEntity = new PolicyServiceNotesEntity();
							noteEntity.setServiceId(note.getServiceId());
							noteEntity.setPolicyId(note.getPolicyId());
							noteEntity.setNoteContents(note.getNoteContents());
							noteEntity.setCreatedBy(note.getCreatedBy());
							noteEntity.setCreatedOn(new Date());
							noteEntity.setModifiedBy(note.getModifiedBy());
							noteEntity.setModifiedOn(new Date());
							noteEntity.setIsActive(Boolean.TRUE);
							noteEntity.setTrnsfrId(transMaster.getTrnsfrId());
							noteList.add(note);
						});					
						transMaster.setNotes(noteList);
					}
					

					transferInAndOutMasterRepository.save(transMaster);
					
					response.setTransactionStatus(CommonConstants.SUCCESS);
					response.setTransactionMessage(transTemp.getTransOutMembers().getMemberShipId()
							+ PolicyServicingCommonConstants.TRANSFER_OUT_REJECTED);
					
					
				}else {
					response.setTransactionStatus(CommonConstants.FAIL);
					response.setTransactionMessage(CommonConstants.NO_RECORD_FOUND+" against Transfer Id ("+reqDto.getTrnsfrId()+") and "
							+ "Unit Code("+reqDto.getUnitCode()+")");
				}
				
			}else {
				response.setTransactionStatus(CommonConstants.FAIL);
				response.setTransactionMessage("Transfer Id and Unit Code is Null!!");
			}
				

		} catch (IllegalArgumentException e) {
			logger.error("Exception:PolicyServiceImpl:reject", e);
			response.setTransactionStatus(CommonConstants.FAIL);
			response.setTransactionMessage(CommonConstants.EXCEPTION+" "+e);
		} finally {
			logger.info("MemberLevelTransferOutServieImpl:reject:End");
		}
		return response;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	



	@Override
	public TransferInAndOutResponseDto approvedAndRejectMemberTransferOut(
			TransferInAndOutReqDto memberLevelTransferOutReqDto, String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransferInAndOutResponseDto inprogresOrExitingTransferOut(String roleType, String inprogress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransferInAndOutResponseDto saveNotesDetails(PolicyServiceNotesDto commonNotesDto) {
		// TODO Auto-generated method stub
		return null;
	}





@Override
	public TransferInAndOutResponseDto inprogressCitrieaSearch(TransferInAndOutReqDto memberSearchDto) {
		TransferInAndOutResponseDto memberSearchApiResponse = new TransferInAndOutResponseDto();

		try {
			List<TransferInAndOutMasterTempEntity> memberLevelTransferOutTempEntity = new ArrayList<>();
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			List<Predicate> predicates = new ArrayList<>();

			CriteriaQuery<TransferInAndOutMasterTempEntity> searchQuery = criteriaBuilder
					.createQuery(TransferInAndOutMasterTempEntity.class);
			Root<TransferInAndOutMasterTempEntity> root = searchQuery.from(TransferInAndOutMasterTempEntity.class);

			Join<TransferInAndOutMasterTempEntity, TransferInAndOutMemberTempEntity> join = root
					.join("transOutMembers");
			if (StringUtils.isNotBlank(memberSearchDto.getPan())) {
				join.on(criteriaBuilder.equal(join.get("pan"), memberSearchDto.getPan()));
			}
			if (memberSearchDto.getAadharNumber() != null) {
				join.on(criteriaBuilder.equal(join.get("aadharNumber"), (memberSearchDto.getAadharNumber())));
			}

			if (StringUtils.isNotBlank(memberSearchDto.getMemberShipId())) {
				join.on(criteriaBuilder.equal(join.get("memberShipId"), memberSearchDto.getMemberShipId()));
			}

			if (StringUtils.isNotBlank(memberSearchDto.getPhone())) {
				join.on(criteriaBuilder.equal(join.get("phone"), memberSearchDto.getPhone()));
			}

			if (StringUtils.isNotBlank(memberSearchDto.getFirstname())) {
				join.on(criteriaBuilder.equal(join.get("memberName"), memberSearchDto.getFirstname()));
			}

			if (StringUtils.isNotBlank(memberSearchDto.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(root.get("policyNumber"), memberSearchDto.getPolicyNumber()));

			}

			if (StringUtils.isNotBlank(memberSearchDto.getPolicyFrom())
					&& StringUtils.isNotBlank(memberSearchDto.getPolicyTo())) {
				Date fromDate = CommonDateUtils.convertStringToDate(memberSearchDto.getPolicyFrom());
				Date toDate = CommonDateUtils.convertStringToDate(memberSearchDto.getPolicyTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get("createdOn"), fromDate, toDate));
			}

			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));

			memberLevelTransferOutTempEntity = entityManager.createQuery(searchQuery).getResultList();
			List<TransferInAndOutDto> memberLevelTransferOutDto = mapList(memberLevelTransferOutTempEntity,
					TransferInAndOutDto.class);
			memberSearchApiResponse.setResponseData(memberLevelTransferOutDto);
			memberSearchApiResponse.setTransactionMessage(CommonConstants.FETCH);
			memberSearchApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
		} catch (Exception e) {
			memberSearchApiResponse.setTransactionMessage(e.getMessage());
			memberSearchApiResponse.setTransactionStatus(CommonConstants.FAIL);
		}
		return memberSearchApiResponse;
	}

	@Override
	public TransferInAndOutResponseDto existingCitrieaSearch(TransferInAndOutReqDto memberSearchDto) {
//	
		TransferInAndOutResponseDto memberSearchApiResponse = new TransferInAndOutResponseDto();

		try {
			List<TransferInAndOutMasterEntity> memberLevelTransferOutTempEntity = new ArrayList<>();
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			List<Predicate> predicates = new ArrayList<>();

			CriteriaQuery<TransferInAndOutMasterEntity> searchQuery = criteriaBuilder
					.createQuery(TransferInAndOutMasterEntity.class);
			Root<TransferInAndOutMasterEntity> root = searchQuery.from(TransferInAndOutMasterEntity.class);

			Join<TransferInAndOutMasterEntity, TransferInAndOutMemberEntity> join = root.join("transOutMembers");
			if (StringUtils.isNotBlank(memberSearchDto.getPan())) {
				join.on(criteriaBuilder.equal(join.get("pan"), memberSearchDto.getPan()));
			}
			if (memberSearchDto.getAadharNumber() != null) {
				join.on(criteriaBuilder.equal(join.get("aadharNumber"), (memberSearchDto.getAadharNumber())));
			}

			if (StringUtils.isNotBlank(memberSearchDto.getMemberShipId())) {
				join.on(criteriaBuilder.equal(join.get("memberShipId"), memberSearchDto.getMemberShipId()));
			}

			if (StringUtils.isNotBlank(memberSearchDto.getPhone())) {
				join.on(criteriaBuilder.equal(join.get("phone"), memberSearchDto.getPhone()));
			}

			if (StringUtils.isNotBlank(memberSearchDto.getFirstname())) {
				join.on(criteriaBuilder.equal(join.get("memberName"), memberSearchDto.getFirstname()));
			}

			if (StringUtils.isNotBlank(memberSearchDto.getPolicyNumber())) {
				predicates.add(criteriaBuilder.equal(root.get("policyNumber"), memberSearchDto.getPolicyNumber()));

			}

			if (StringUtils.isNotBlank(memberSearchDto.getPolicyFrom())
					&& StringUtils.isNotBlank(memberSearchDto.getPolicyTo())) {
				Date fromDate = CommonDateUtils.convertStringToDate(memberSearchDto.getPolicyFrom());
				Date toDate = CommonDateUtils.convertStringToDate(memberSearchDto.getPolicyTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get("createdOn"), fromDate, toDate));
			}

			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));

			memberLevelTransferOutTempEntity = entityManager.createQuery(searchQuery).getResultList();
			List<TransferInAndOutDto> memberLevelTransferOutDto = mapList(memberLevelTransferOutTempEntity,
					TransferInAndOutDto.class);
			memberSearchApiResponse.setResponseData(memberLevelTransferOutDto);
			memberSearchApiResponse.setTransactionMessage(CommonConstants.FETCH);
			memberSearchApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
		} catch (Exception e) {
			memberSearchApiResponse.setTransactionMessage(e.getMessage());
			memberSearchApiResponse.setTransactionStatus(CommonConstants.FAIL);
		}
		return memberSearchApiResponse;
	}

	@Override
	public Object getInprogressLoad(String role, String unitCode) {
		TransferInAndOutResponseDto memberSearchApiResponse = new TransferInAndOutResponseDto();
		List<TransferInAndOutDto> membersTransferOutDtos = new ArrayList<>();
		List<TransferInAndOutMasterTempEntity> tempEntity = new ArrayList<>();
		try {
			if (role.equalsIgnoreCase(PolicyServicingCommonConstants.CHECKER)) {
				tempEntity = transferInAndOutMasterTempRepository
						.findAllByTrnsfrStatusInAndUnitCodeAndIsActiveTrueOrderByModifiedOnDesc(
								PolicyServicingCommonConstants.inprogressChecker(), unitCode);
			} else if (role.equalsIgnoreCase(PolicyServicingCommonConstants.MAKER)) {
				tempEntity = transferInAndOutMasterTempRepository
						.findAllByTrnsfrStatusInAndUnitCodeAndIsActiveTrueOrderByModifiedOnDesc(
								(PolicyServicingCommonConstants.inprogressMaker()), unitCode);
			}
			if (!tempEntity.isEmpty()) {
				tempEntity.forEach(tempEntities -> {
					TransferInAndOutDto transferOutDto = new TransferInAndOutDto();
					transferOutDto = modelMapper.map(tempEntities, TransferInAndOutDto.class);
					// transferOutDto = convertToEntityToDto(tempEntities);
					membersTransferOutDtos.add(transferOutDto);
				});

//				List<TransferInAndOutDto> memberLevelTransferOutDto = mapList(tempEntity,
//				TransferInAndOutDto.class);

				memberSearchApiResponse.setResponseData(membersTransferOutDtos);
				memberSearchApiResponse.setTransactionMessage(CommonConstants.FETCH);
				memberSearchApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
			} else {
				memberSearchApiResponse.setTransactionMessage(CommonConstants.INVALID_REQUEST);
				memberSearchApiResponse.setTransactionStatus(CommonConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			memberSearchApiResponse.setTransactionMessage(e.getMessage());
			memberSearchApiResponse.setTransactionStatus(CommonConstants.FAIL);

		}
		return memberSearchApiResponse;
	}

	@Override
	public Object getExistingLoad(String role, String unitCode) {
		TransferInAndOutResponseDto memberSearchApiResponse = new TransferInAndOutResponseDto();
		List<TransferInAndOutDto> membersTransferOutDtos = new ArrayList<>();
		List<TransferInAndOutMasterEntity> tempEntity = new ArrayList<>();
		try {
			if (role.equalsIgnoreCase(PolicyServicingCommonConstants.CHECKER)) {
				tempEntity = transferInAndOutMasterRepository.findAllByTrnsfrStatusInAndUnitCodeAndIsActiveTrue(
						PolicyServicingCommonConstants.existingChecker(), unitCode);
			} else if (role.equalsIgnoreCase(PolicyServicingCommonConstants.MAKER)) {
				tempEntity = transferInAndOutMasterRepository.findAllByTrnsfrStatusInAndUnitCodeAndIsActiveTrue(
						PolicyServicingCommonConstants.existingChecker(), unitCode);
			}
			if (!tempEntity.isEmpty()) {
				tempEntity.forEach(tempEntities -> {
					TransferInAndOutDto transferOutDto = new TransferInAndOutDto();

					transferOutDto = modelMapper.map(tempEntities, TransferInAndOutDto.class);
					// transferOutDto = convertExistingToEntityToDto(tempEntities);
					membersTransferOutDtos.add(transferOutDto);
				});
				memberSearchApiResponse.setResponseData(membersTransferOutDtos);
				memberSearchApiResponse.setTransactionMessage(CommonConstants.FETCH);
				memberSearchApiResponse.setTransactionStatus(CommonConstants.SUCCESS);
			} else {
				memberSearchApiResponse.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
				memberSearchApiResponse.setTransactionStatus(CommonConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			memberSearchApiResponse.setTransactionMessage(e.getMessage());
			memberSearchApiResponse.setTransactionStatus(CommonConstants.FAIL);

		}
		return memberSearchApiResponse;
	}









	




	




}
