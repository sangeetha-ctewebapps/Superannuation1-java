package com.lic.epgs.quotation.service.impl;

import static com.lic.epgs.utils.CommonConstants.LOGEND;
import static com.lic.epgs.utils.CommonConstants.LOGSTART;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionEntity;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionTempEntity;
import com.lic.epgs.adjustmentcontribution.repository.AdjustmentContributionRepository;
import com.lic.epgs.adjustmentcontribution.repository.AdjustmentContributionTempRepository;
import com.lic.epgs.common.dto.CommonChallanDto;
import com.lic.epgs.common.dto.CommonDocsDto;
import com.lic.epgs.common.dto.CommonNotesDto;
import com.lic.epgs.common.dto.CommonRuleDto;
import com.lic.epgs.common.entity.CommonDocsEntity;
import com.lic.epgs.common.entity.CommonDocsTempEntity;
import com.lic.epgs.common.entity.CommonNoteEntity;
import com.lic.epgs.common.entity.CommonNoteTempEntity;
import com.lic.epgs.common.entity.CommonRuleEntity;
import com.lic.epgs.common.entity.CommonRuleTempEntity;
import com.lic.epgs.common.repository.CommonDocsTempRepository;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.MphMasterTempEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.MphMasterTempRepository;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceDto;
import com.lic.epgs.policyservicing.common.entity.PolicyServiceEntity;
import com.lic.epgs.quotation.constants.QuotationConstants;
import com.lic.epgs.quotation.dto.LiabilityDto;
import com.lic.epgs.quotation.dto.PolicyServiceMatrixNewDto;
import com.lic.epgs.quotation.dto.QuotationApiResponseDto;
import com.lic.epgs.quotation.dto.QuotationDto;
import com.lic.epgs.quotation.dto.QuotationSearchDto;
import com.lic.epgs.quotation.entity.PolicyServiceMatrixNewEntity;
import com.lic.epgs.quotation.entity.QuotationEntity;
import com.lic.epgs.quotation.entity.QuotationHistoryEntity;
import com.lic.epgs.quotation.entity.QuotationLiabilityEntity;
import com.lic.epgs.quotation.entity.QuotationLiabilityTempEntity;
import com.lic.epgs.quotation.entity.QuotationMemberAddressEntity;
import com.lic.epgs.quotation.entity.QuotationMemberAddressTempEntity;
import com.lic.epgs.quotation.entity.QuotationMemberAppointeeEntity;
import com.lic.epgs.quotation.entity.QuotationMemberAppointeeTempEntity;
import com.lic.epgs.quotation.entity.QuotationMemberBankDetailEntity;
import com.lic.epgs.quotation.entity.QuotationMemberBankDetailTempEntity;
import com.lic.epgs.quotation.entity.QuotationMemberEntity;
import com.lic.epgs.quotation.entity.QuotationMemberNomineeEntity;
import com.lic.epgs.quotation.entity.QuotationMemberNomineeTempEntity;
import com.lic.epgs.quotation.entity.QuotationMemberTempEntity;
import com.lic.epgs.quotation.entity.QuotationTempEntity;
import com.lic.epgs.quotation.repository.PolicyServiceMatrixNewRepository;
import com.lic.epgs.quotation.repository.PolicyServicerRepositry;
import com.lic.epgs.quotation.repository.QuotationRepository;
import com.lic.epgs.quotation.repository.QuotationTempRepository;
import com.lic.epgs.quotation.service.QuotationService;
import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionEntity;
import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionTempEntity;
import com.lic.epgs.regularadjustmentcontribution.repository.RegularAdjustmentContributionRepository;
import com.lic.epgs.regularadjustmentcontribution.repository.RegularAdjustmentContributionTempRepository;
import com.lic.epgs.surrender.entity.PolicySurrenderEntity;
import com.lic.epgs.surrender.entity.PolicySurrenderTempEntity;
import com.lic.epgs.surrender.repository.PolicySurrenderRepository;
import com.lic.epgs.surrender.repository.PolicySurrenderTempRepository;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

@Transactional
@Service
public class QuotationServiceImpl implements QuotationService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private QuotationTempRepository quotationTempRepository;

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private CommonDocsTempRepository commonDocsTempRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private CommonService commonSequenceService;
	
	@Autowired
	MphMasterTempRepository mphMasterTempRepository;
	
	@Autowired
	MphMasterRepository mphMasterRepository;
	
	@Autowired
	MemberMasterRepository memberMasterRepository;

	@Autowired
	RegularAdjustmentContributionRepository regularAdjustmentContributionRepository;

	@Autowired
	RegularAdjustmentContributionTempRepository regularAdjustmentContributionTempRepository;

	@Autowired
	AdjustmentContributionRepository adjustmentContributionRepository;

	@Autowired
	AdjustmentContributionTempRepository adjustmentContributionTempRepository;

	@Autowired
	PolicySurrenderTempRepository policySurrenderTempRepository;

	@Autowired
	PolicySurrenderRepository policySurrenderRepository;

	@Autowired
	private PolicyServicerRepositry policyServicerRepositry;

	@Autowired
	private PolicyServiceMatrixNewRepository policyServiceMatrixNewRepository;


	public static final String STATUS = "status";

	public synchronized String getChallanSeq() {
		return commonSequenceService.getSequence(CommonConstants.CHALLAN_SEQ);
	}

	
	
	
	
	
	@Override
	public QuotationApiResponseDto saveQuotation(QuotationDto quotationDto) {
		QuotationApiResponseDto commonDto = new QuotationApiResponseDto();
		logger.info("{}", LOGSTART);
		try {
			logger.info("QuotationServiceImpl:saveQuotation:Start");
			
			List<String> proposalNumberList = mphMasterRepository.getProposalHistoryWithoutRejectStatusByProposalNumber(quotationDto.getProposalNumber());
			
			if (!proposalNumberList.isEmpty()) {
				commonDto.setTransactionMessage(CommonConstants.PROPOSAL_IS_ACTIVE_AGAINST_POLICY);
				commonDto.setTransactionStatus(CommonConstants.FAIL);
			} else {

				List<String> proposalNumberTempList = mphMasterTempRepository.getProposalHistoryWithoutRejectStatusByProposalNumber(quotationDto.getProposalNumber());

				
				if (!proposalNumberTempList.isEmpty()) {					
					commonDto.setTransactionMessage(CommonConstants.PROPOSAL_IS_ACTIVE_AGAINST_POLICY);
					commonDto.setTransactionStatus(CommonConstants.FAIL);
				} else {
					
					if(quotationDto.getQuotationId() != null) {
						
						QuotationTempEntity existQuotationTemp = quotationTempRepository.findByQuotationIdAndIsActiveTrue(quotationDto.getQuotationId());
						

						if(quotationDto.getComponentLabel().equalsIgnoreCase("Quotation Details")) {
							
							existQuotationTemp = this.setQuotationBasicDetails(existQuotationTemp, quotationDto);
						}
						
						
						if(quotationDto.getComponentLabel().equalsIgnoreCase("Benefit type")) {
							
							existQuotationTemp = this.setBenefitType(existQuotationTemp, quotationDto);
						}
						
						
						if(quotationDto.getComponentLabel().equalsIgnoreCase("Valuation")) {
							
							existQuotationTemp = this.setValuationDetails(existQuotationTemp, quotationDto);
						}
						

						if(quotationDto.getComponentLabel().equalsIgnoreCase("Valuation Data")) {
							
							existQuotationTemp = this.setValuationDataAndCalculateTotalContribution(existQuotationTemp, quotationDto);
							
						}
						
						
						if(quotationDto.getComponentLabel().equalsIgnoreCase("Contribution Calculation")) {
							existQuotationTemp.setTotalMember(quotationDto.getTotalMember());
							existQuotationTemp.setTotalContribution(quotationDto.getTotalContribution());
						}

						
						if(quotationDto.getComponentLabel().equalsIgnoreCase("Note")) {
							
							existQuotationTemp = this.setNotesDetails(existQuotationTemp, quotationDto);
							
						}
						

						
						existQuotationTemp = quotationTempRepository.save(existQuotationTemp);
						QuotationDto quoResDto = modelMapper.map(existQuotationTemp, QuotationDto.class);
						
						
						if(!quoResDto.getDocs().isEmpty()) {
							List<CommonDocsDto> docList = quoResDto.getDocs();
							docList.removeIf(doc->doc.getIsActive() == Boolean.FALSE);
							docList.sort(Comparator.comparing(CommonDocsDto::getDocId));
							quoResDto.setDocs(docList);
						}
						

						commonDto.setQuotationDto(quoResDto);
						commonDto.setTransactionStatus(CommonConstants.SUCCESS);
						commonDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
									
						
					}else {
						
						
						QuotationTempEntity quotationTemp = modelMapper.map(quotationDto, QuotationTempEntity.class);
						quotationTemp.setQuotationNo(StringUtils.isNoneEmpty(quotationDto.getQuotationNo())
								? quotationDto.getQuotationNo()
								: createQuotationNumber(quotationDto.getUnitCode(), quotationDto.getProposalNumber()));

						if (quotationTemp.getQuotationNo().equalsIgnoreCase("exceed")) {
							commonDto.setTransactionStatus(CommonConstants.FAIL);
							commonDto.setTransactionMessage("Maximum Limit Of Quotation is Exceeded!");
							return commonDto;
						}

						
						quotationTemp.setIsActive(true);
						quotationTemp = quotationTempRepository.save(quotationTemp);
						commonDto.setQuotationDto(modelMapper.map(quotationTemp, QuotationDto.class));
						commonDto.setTransactionStatus(CommonConstants.SUCCESS);
						commonDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);	
						
					}
					
					
					
				}

			}

		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:saveQuotation", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:saveQuotation:Ends");
		}
		logger.info("{}", LOGEND);

		return commonDto;
	}
	
	
	
	
	private QuotationTempEntity setQuotationBasicDetails(QuotationTempEntity quotationTemp , QuotationDto quotationDto) {
		quotationTemp.setProposalNumber(quotationDto.getProposalNumber());
		quotationTemp.setAdvanceOrArrears(quotationDto.getAdvanceOrArrears());
		quotationTemp.setContributionType(quotationDto.getContributionType());
		quotationTemp.setFrequency(quotationDto.getFrequency());
		quotationTemp.setLeadNumber(quotationDto.getLeadNumber());
		quotationTemp.setLineOfBusiness(quotationDto.getLineOfBusiness());
		quotationTemp.setMphCode(quotationDto.getMphCode());
		quotationTemp.setMphName(quotationDto.getMphName());
		quotationTemp.setNoOfCategory(quotationDto.getNoOfCategory());
		quotationTemp.setNoOfLives(quotationDto.getNoOfLives());
		quotationTemp.setPolicyCommencementDate(quotationDto.getPolicyCommencementDate());
		quotationTemp.setProductType(quotationDto.getProduct());
		quotationTemp.setProductType(quotationDto.getProductType());
		quotationTemp.setQuotationDate(quotationDto.getQuotationDate());
		quotationTemp.setQuotationId(quotationDto.getQuotationId());
		quotationTemp.setQuotationNo(quotationDto.getQuotationNo());
		quotationTemp.setQuotationType(quotationDto.getQuotationType());
		quotationTemp.setUnitCode(quotationDto.getUnitCode());
		quotationTemp.setVariant(quotationDto.getVariant());
		
		
		if (quotationDto.getContributionType().equalsIgnoreCase("Non Contributory")) {
			quotationTemp = this.setContribucation(quotationTemp, quotationDto);
			if(quotationTemp.getQuotationType().equalsIgnoreCase("DC")) {
				quotationTemp = this.calculateMembersContributions(quotationTemp);
			}
		}
		
		return quotationTemp;
	}
	
	private QuotationTempEntity setContribucation(QuotationTempEntity quotationTemp, QuotationDto quotationDto) {
		
		
		if(!quotationTemp.getMembers().isEmpty()) {
			Set<QuotationMemberTempEntity> newQuotationMemberTempEntity = new HashSet<>();
			Set<QuotationMemberTempEntity> quoMbrTempList = quotationTemp.getMembers();
			for (QuotationMemberTempEntity quoMbrTemp : quoMbrTempList) {
				
				if(quoMbrTemp.getIsActive() == Boolean.TRUE) {
					quoMbrTemp.setEmployerContribution(quoMbrTemp.getEmployerContribution());
					quoMbrTemp.setEmployeeContribution(BigDecimal.ZERO);
					quoMbrTemp.setVoluntaryContribution(BigDecimal.ZERO);
					quoMbrTemp.setTotalContribution(quoMbrTemp.getEmployerContribution());
					newQuotationMemberTempEntity.add(quoMbrTemp);
				}
				
			}
			
			quotationTemp.setMembers(newQuotationMemberTempEntity);

		}

		return quotationTemp;
	}
	
	
	
	
	
	private QuotationTempEntity calculateMembersContributions(QuotationTempEntity quotationTemp) {

		logger.info("QuotationServiceImpl:saveQuotationMember:--contribution Calculation---start");

		Set<QuotationMemberTempEntity> quoMbrTemp = quotationTemp.getMembers();
		List<QuotationMemberTempEntity> replicaMembers = new ArrayList<>();
		quoMbrTemp.forEach(member -> replicaMembers.add(member));
		replicaMembers.removeIf(member -> member.getIsActive() == Boolean.FALSE);

		BigDecimal totalEmployeeContribution = BigDecimal.ZERO, totalEmployercontribution = BigDecimal.ZERO,
				totalOfTotalContribution = BigDecimal.ZERO, totalVoluntaryContribution = BigDecimal.ZERO;
		
		for (QuotationMemberTempEntity itr : replicaMembers) {

			totalEmployeeContribution = totalEmployeeContribution
					.add((itr.getEmployeeContribution() != null) ? itr.getEmployeeContribution() : BigDecimal.ZERO);
			totalEmployercontribution = totalEmployercontribution
					.add((itr.getEmployerContribution() != null) ? itr.getEmployerContribution() : BigDecimal.ZERO);
			totalVoluntaryContribution = totalVoluntaryContribution
					.add((itr.getVoluntaryContribution() != null) ? itr.getVoluntaryContribution() : BigDecimal.ZERO);
			totalOfTotalContribution = totalOfTotalContribution
					.add((itr.getTotalContribution() != null) ? itr.getTotalContribution() : BigDecimal.ZERO);

		}

		quotationTemp.setEmployeeContribution(totalEmployeeContribution);

		quotationTemp.setEmployerContribution(totalEmployercontribution);

		quotationTemp.setVoluntaryContribution(totalVoluntaryContribution);

		quotationTemp.setTotalContribution(totalOfTotalContribution);

		quotationTemp.setTotalMember(replicaMembers.size());

		logger.info("QuotationServiceImpl:saveQuotationMember:--contribution Calculation---End");

		return quotationTemp;
	}
	
	
	
	private QuotationTempEntity setBenefitType(QuotationTempEntity quotationTemp , QuotationDto quotationDto) {
		if(!quotationDto.getRules().isEmpty() && quotationDto.getRules() !=null) {
			Set<CommonRuleTempEntity> ruleList = new HashSet<>();
			
			for(CommonRuleDto ruleDto : quotationDto.getRules()) {
				
				if(ruleDto.getRuleId() !=null) {
					
					ruleList.add(modelMapper.map(ruleDto, CommonRuleTempEntity.class));								
					
				}else {
					CommonRuleTempEntity ruleEntity = modelMapper.map(ruleDto, CommonRuleTempEntity.class);
					ruleEntity.setRuleId(null);
					ruleList.add(ruleEntity);
					
					
				}
			}
			
			quotationTemp.setRules(ruleList);
			
		}
		return quotationTemp;
	}
	
	
	
	private QuotationTempEntity setValuationDetails(QuotationTempEntity quotationTemp , QuotationDto quotationDto) {
		
		if(quotationTemp.getValuationType() !=null)
			if(!quotationTemp.getValuationType().equalsIgnoreCase(quotationDto.getValuationType()))
				quotationTemp.setTotalContribution(BigDecimal.ZERO);
		
		quotationTemp.setValuationType(quotationDto.getValuationType());
		quotationTemp.setReportReceived(quotationDto.getReportReceived());
		quotationTemp.setAttritionRate(quotationDto.getAttritionRate());
		quotationTemp.setSalaryEscalation(quotationDto.getSalaryEscalation());
		quotationTemp.setDeathRate(quotationDto.getDeathRate());
		quotationTemp.setDisRateSalaryEsc(quotationDto.getDisRateSalaryEsc());
		quotationTemp.setAnnuityOption(quotationDto.getAnnuityOption());
		quotationTemp.setMinPension(quotationDto.getMinPension());
		quotationTemp.setMaxPension(quotationDto.getMaxPension());
		quotationTemp.setAccuralRateFactor(quotationDto.getAccuralRateFactor());
		quotationTemp.setWithdrawalRate(quotationDto.getWithdrawalRate());
		return quotationTemp;
	}
	
	
	
	private QuotationTempEntity setValuationDataAndCalculateTotalContribution(QuotationTempEntity quotationTemp , QuotationDto quotationDto) {
		
		if(!quotationDto.getLiabilities().isEmpty() && quotationDto.getLiabilities() !=null) {
			Set<QuotationLiabilityTempEntity> liabilitiesList = new HashSet<>();
			BigDecimal totalContribution =BigDecimal.ZERO;
			for(LiabilityDto liabilityDto : quotationDto.getLiabilities()) {
				
				QuotationLiabilityTempEntity liabilityEntity = modelMapper.map(liabilityDto, QuotationLiabilityTempEntity.class);
				liabilitiesList.add(liabilityEntity);
				
					if(liabilityEntity.getExternalaluation() != null && liabilityEntity.getExternalaluation() != BigDecimal.ZERO ) {
						totalContribution = totalContribution.add(liabilityEntity.getExternalaluation());
					}else if(liabilityEntity.getInternalValuation() != null && liabilityEntity.getInternalValuation() != BigDecimal.ZERO) {
						totalContribution = totalContribution.add(liabilityEntity.getInternalValuation());
					}

				}

			
			quotationTemp.setLiabilities(liabilitiesList);
			quotationTemp.setTotalContribution(totalContribution);
			

		}
		return quotationTemp;
	}
	
	
	
	
	private QuotationTempEntity setNotesDetails(QuotationTempEntity quotationTemp , QuotationDto quotationDto) {
		
		if(!quotationDto.getNotes().isEmpty() && quotationDto.getNotes() !=null) {
			List<CommonNoteTempEntity> notesList = new ArrayList<>();
			
			for(CommonNotesDto notesDto : quotationDto.getNotes()) {
				
				CommonNoteTempEntity notesEntity = modelMapper.map(notesDto, CommonNoteTempEntity.class);
				notesList.add(notesEntity);

				}

			
			quotationTemp.setNotes(notesList);							

		}
		
		return quotationTemp;
	}
	
	
	

	private String createQuotationNumber(String unitCode, String proposalNo) {
		List<QuotationTempEntity> quotationTemp = null;
		String result = "";
		logger.info("unitCode::{}proposalNo::{}::{}", unitCode, proposalNo, LOGSTART);
		try {
			logger.info("QuotationServiceImpl:createQuotationNumber:start");
			if (proposalNo != null && unitCode != null) {
				quotationTemp = quotationTempRepository.findAllByproposalNumberAndIsActiveTrueAndUnitCodeOrderByQuotationIdDesc(proposalNo, unitCode);
				
				String latestQuo =(!quotationTemp.isEmpty())?quotationTemp.get(0).getQuotationNo():null;
				
				if (latestQuo != null) {
					if (Integer.valueOf(latestQuo.substring(proposalNo.length())) < 9) {
						result = latestQuo.substring(0, proposalNo.length()) + "0" + (Integer.valueOf(latestQuo.substring(proposalNo.length())) + 1);
					} else if (Integer.valueOf(latestQuo.substring(proposalNo.length())) >= 9
							&& Integer.valueOf(latestQuo.substring(proposalNo.length())) < 99) {
						result = latestQuo.substring(0, proposalNo.length())
								+ String.valueOf(Integer.valueOf(latestQuo.substring(proposalNo.length())) + 1);
					} else {
						return result = "exceed";
					}
				} else {
					result = proposalNo + "01";
				}
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:createQuotationNumber", e);
		}

		logger.info("QuotationServiceImpl:createQuotationNumber:Ends");
		logger.info("unitCode::{}proposalNo::{}::{}", unitCode, proposalNo, LOGEND);
		return result;

	}

	@Override
	public QuotationApiResponseDto getQuotations() {
		QuotationApiResponseDto commonDto = new QuotationApiResponseDto();
		try {
			logger.info("QuotationServiceImpl:getQuotations:Start");
			List<QuotationTempEntity> quotations = quotationTempRepository.findAll();
			List<QuotationDto> quotationDtos = mapList(quotations, QuotationDto.class);
			commonDto.setResponseData(quotationDtos);
			commonDto.setTransactionStatus(CommonConstants.STATUS);
			commonDto.setTransactionMessage(CommonConstants.FETCH);
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:getQuotations", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:getQuotations:Ends");
		}
		return commonDto;
	}

	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
	}

	@Override
	public QuotationApiResponseDto getQuotationById(QuotationSearchDto dto) {
		QuotationApiResponseDto commonDto = new QuotationApiResponseDto();
		try {
			logger.info("QuotationServiceImpl:getQuotationById:Start");
			List<?> quotations = new ArrayList<>();
			if (dto.getIsLoad()) {
				if (dto.getRole().equalsIgnoreCase(CommonConstants.MAKER)) {
					quotations = (CommonConstants.EXISTING.equals(dto.getPageName()))
							? quotationRepository.findAllByStatusInAndUnitCodeOrderByQuotationIdDesc(
									CommonConstants.existingMakerNew(), dto.getUnitOffice())
							: quotationTempRepository.findAllByStatusInAndIsActiveTrueAndUnitCodeOrderByQuotationIdDesc(
									CommonConstants.inprogressMaker(), dto.getUnitOffice());
				} else if (dto.getRole().equalsIgnoreCase(CommonConstants.CHECKER)) {
					quotations = (CommonConstants.EXISTING.equals(dto.getPageName()))
							? quotationRepository.findAllByStatusInAndIsActiveTrueAndUnitCodeOrderByQuotationIdDesc(
									CommonConstants.existingMaker(), dto.getUnitOffice())
							: quotationTempRepository.findAllByStatusInAndIsActiveTrueAndUnitCodeOrderByQuotationIdDesc(
									CommonConstants.inprogressChecker(), dto.getUnitOffice());
				} else if (dto.getRole().equalsIgnoreCase(CommonConstants.ACTUARY)) {
					quotations = (CommonConstants.EXISTING.equals(dto.getPageName()))
							? quotationRepository.findAllByStatusInAndIsActiveTrueAndUnitCodeOrderByQuotationIdDesc(
									CommonConstants.existingActuary(), dto.getUnitOffice())
							: quotationTempRepository.findAllByStatusInAndIsActiveTrueAndUnitCodeOrderByQuotationIdDesc(
									CommonConstants.inprogressActuary(), dto.getUnitOffice());
				}
				List<QuotationDto> quotationDtos = mapList(quotations, QuotationDto.class);
				if (quotationDtos != null) {
					commonDto.setResponseObject(quotationDtos);
					commonDto.setTransactionStatus(CommonConstants.SUCCESS);
					commonDto.setTransactionMessage(CommonConstants.FETCH);
				} else {
					commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
					commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				}

			} else {

				if (CommonConstants.EXISTING.equals(dto.getPageName())) {

					QuotationEntity quotation = null;

					if (CommonConstants.MAKER.equalsIgnoreCase(dto.getRole())) {
						quotation = quotationRepository.findByQuotationNoAndStatusInAndUnitCode(dto.getQuotationNo(),
								CommonConstants.existingMakerNew(), dto.getUnitOffice());
					} else if (CommonConstants.CHECKER.equalsIgnoreCase(dto.getRole())) {
						quotation = quotationRepository.findByIsActiveTrueAndQuotationNoAndStatusInAndUnitCode(
								dto.getQuotationNo(), CommonConstants.existingMaker(), dto.getUnitOffice());
					} else {
						quotation = quotationRepository.findByIsActiveTrueAndQuotationNoAndStatusInAndUnitCode(
								dto.getQuotationNo(), CommonConstants.existingActuary(), dto.getUnitOffice());
					}

					if (quotation != null) {
						
						List<CommonNoteEntity> noteList = quotation.getNotes();
						noteList.sort(Comparator.comparing(CommonNoteEntity::getNoteId));
						quotation.setNotes(noteList);
						
						
						QuotationDto quotationDto = modelMapper.map(quotation, QuotationDto.class);
						commonDto.setQuotationId(quotationDto.getQuotationId());
						
						if(!quotationDto.getDocs().isEmpty()) {
							List<CommonDocsDto> docsDto = quotationDto.getDocs();
							docsDto.removeIf(doc->doc.getIsActive() == Boolean.FALSE);
							docsDto.sort(Comparator.comparing(CommonDocsDto::getDocId));
							quotationDto.setDocs(docsDto);
						}
						
						commonDto.setResponseObject(quotationDto);

						commonDto.setTransactionStatus(CommonConstants.SUCCESS);
						commonDto.setTransactionMessage(CommonConstants.FETCH);
					} else {
						commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
						commonDto.setTransactionStatus(CommonConstants.SUCCESS);
					}
				} else {

					QuotationTempEntity quotationTemp = null;
					if (CommonConstants.MAKER.equalsIgnoreCase(dto.getRole())) {
						quotationTemp = quotationTempRepository.findByIsActiveTrueAndQuotationNoAndStatusInAndUnitCode(
								dto.getQuotationNo(), CommonConstants.inprogressMaker(), dto.getUnitOffice());
					} else if (CommonConstants.CHECKER.equalsIgnoreCase(dto.getRole())) {
						quotationTemp = quotationTempRepository.findByIsActiveTrueAndQuotationNoAndStatusInAndUnitCode(
								dto.getQuotationNo(), CommonConstants.inprogressChecker(), dto.getUnitOffice());
					} else {
						quotationTemp = quotationTempRepository.findByIsActiveTrueAndQuotationNoAndStatusInAndUnitCode(
								dto.getQuotationNo(), CommonConstants.inprogressActuary(), dto.getUnitOffice());
					}

					if (quotationTemp != null) {

						List<CommonNoteTempEntity> noteList = quotationTemp.getNotes();
						noteList.sort(Comparator.comparing(CommonNoteTempEntity::getNoteId));
						quotationTemp.setNotes(noteList);
						
						
						
						QuotationDto quotationDto = modelMapper.map(quotationTemp, QuotationDto.class);
						
						
						if(!quotationDto.getDocs().isEmpty()) {
							List<CommonDocsDto> docsDto = quotationDto.getDocs();
							docsDto.removeIf(doc->doc.getIsActive() == Boolean.FALSE);
							docsDto.sort(Comparator.comparing(CommonDocsDto::getDocId));
							quotationDto.setDocs(docsDto);
						}
						
						
//						Set<QuotationMemberDto> memberList = quotationDto.getMembers();
//						
//						for(QuotationMemberDto member : memberList) {
//							
//							if(!member.getQuotationMemberNominees().isEmpty()) {
//								List<QuotationMemberNomineeDto> nomineeList = member.getQuotationMemberNominees();
//								nomineeList.removeIf(nominee->nominee.getIsActive() == Boolean.FALSE);
//								nomineeList.sort(Comparator.comparing(QuotationMemberNomineeDto::getNomineeId));
//								member.setQuotationMemberNominees(nomineeList);
//							}
//							
//						}
//						
//						quotationDto.setMembers(memberList);						
						
						
						
						commonDto.setQuotationId(quotationDto.getQuotationId());
						commonDto.setResponseObject(quotationDto);
						commonDto.setTransactionStatus(CommonConstants.SUCCESS);
						commonDto.setTransactionMessage(CommonConstants.FETCH);

					} else {
						commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
						commonDto.setTransactionStatus(CommonConstants.SUCCESS);
					}
				}

//				Optional<?> quotation = CommonConstants.EXISTING.equals(dto.getPageName())
//						? quotationRepository.findByQuotationNoAndIsActiveTrue(dto.getQuotationNo())
//						: quotationTempRepository.findByQuotationNoAndIsActiveTrue(dto.getQuotationNo());
//
//				if (quotation.isPresent()) {
//					QuotationDto quotationDto = modelMapper.map(quotation.get(), QuotationDto.class);
//					commonDto.setQuotationId(quotationDto.getQuotationId());
//					commonDto.setResponseObject(quotationDto);
//					commonDto.setTransactionStatus(CommonConstants.SUCCESS);
//					commonDto.setTransactionMessage(CommonConstants.FETCH);
//				} else {
//					commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
//					commonDto.setTransactionStatus(CommonConstants.FAIL);
//				}
			}

		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:getQuotationById", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:getQuotationById:Ends");
		}
		return commonDto;
	}
	
//	private <S, T> Set<T> mapSet(Set<S> source, Class<T> targetClass) {
//		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toSet());
//	}
//	
//	private QuotationEntity getBetweenQuotationAndCommonDocsEntity(Integer quotationId) {
//		QuotationEntity quotation = null;
//		try {
//			
//			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//
//			CriteriaQuery<QuotationEntity> searchQuery = criteriaBuilder.createQuery(QuotationEntity.class);
//			Root<QuotationEntity> root = searchQuery.from(QuotationEntity.class);
//
//			Join<QuotationEntity, CommonDocsEntity> join = root.join("docs");
//			join.on(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
//			join.on(criteriaBuilder.equal(root.get("quotationId"),quotationId));
//	
//			
//			
//			List<Predicate> predicates = new ArrayList<>();
//			predicates.add(criteriaBuilder.equal(root.get("quotationId"),quotationId));
//			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
//			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));
//
//			QuotationEntity result = entityManager.createQuery(searchQuery).getResultList().stream().findFirst().orElse(null);
//			if(result != null) {
//				quotation =result;
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		return quotation;
//	}
//	
//	
//	
//	
//	private QuotationTempEntity getBetweenQuotationTempAndCommonDocsTempEntity(Integer quotationId,String purpose,Integer docId) {
//		QuotationTempEntity quotationTemp = null;
//		try {
//			
//			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//
//			CriteriaQuery<QuotationTempEntity> searchQuery = criteriaBuilder.createQuery(QuotationTempEntity.class);
//			Root<QuotationTempEntity> root = searchQuery.from(QuotationTempEntity.class);
//			
//
//			Join<QuotationTempEntity, CommonDocsTempEntity> members = root.join("docs");
////			members.on(criteriaBuilder.equal(members.get("isActive"), Boolean.TRUE));
//////			if(purpose.equalsIgnoreCase("uploadDocument")) {
//////				if(docId != null) {
////					members.on(criteriaBuilder.equal(members.get("quotationId"),quotationId));
//////					members.on(criteriaBuilder.equal(members.get("docId"),docId));
//////				}
//////			}else {
//////				members.on(criteriaBuilder.equal(root.get("quotationId"),quotationId));
//////			}
//			
//		
//			
//			
//			List<Predicate> predicates = new ArrayList<>();
//			predicates.add(criteriaBuilder.equal(members.get("quotationId"),quotationId));
//			predicates.add(criteriaBuilder.equal(members.get("isActive"), Boolean.TRUE));
//			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));
//			
//			EntityGraph<QuotationTempEntity> fetchGraph = entityManager.createEntityGraph(QuotationTempEntity.class);
//			fetchGraph.addSubgraph("docs");
//
//			QuotationTempEntity result = entityManager.createQuery(searchQuery).setHint("javax.persistence.loadgraph", fetchGraph).getResultList().stream().findFirst().orElse(null);
//			if(result != null) {
//				quotationTemp =result;
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		return quotationTemp;
//	}

	@Override
	public QuotationApiResponseDto inprogressCitrieaSearch(QuotationSearchDto quotationSearchDto) {
		List<QuotationTempEntity> quotationTempEntities = new ArrayList<>();
		QuotationApiResponseDto commonDto = new QuotationApiResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("QuotationServiceImpl:existingCitrieaSearch:Start");
			if (quotationSearchDto.getRole() != null && quotationSearchDto.getUnitOffice() != null) {
				List<Predicate> predicates = new ArrayList<>();
				CriteriaQuery<QuotationTempEntity> createQuery = criteriaBuilder.createQuery(QuotationTempEntity.class);
				Root<QuotationTempEntity> root = createQuery.from(QuotationTempEntity.class);

				if (StringUtils.isNotBlank(quotationSearchDto.getFrom())
						&& StringUtils.isNotBlank(quotationSearchDto.getTo())) {
					Date fromDate = CommonDateUtils.convertStringToDate(quotationSearchDto.getFrom());
					Date toDate = CommonDateUtils.convertStringToDate(quotationSearchDto.getTo());
					toDate = CommonDateUtils.constructeEndDateTime(toDate);
					predicates.add(criteriaBuilder.between(root.get("quotationDate"), fromDate, toDate));
				}
				if (quotationSearchDto.getProposalNumber() != null) {
					predicates.add(
							criteriaBuilder.equal(root.get("proposalNumber"), quotationSearchDto.getProposalNumber()));
				}
				if (StringUtils.isNotBlank(quotationSearchDto.getMphCode())) {
					predicates.add(criteriaBuilder.equal(root.get("mphCode"), quotationSearchDto.getMphCode()));
				}
				if (StringUtils.isNotBlank(quotationSearchDto.getMphName())) {
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("mphName")),
							"%" + quotationSearchDto.getMphName().toLowerCase() + "%"));
				}
				if (StringUtils.isNotBlank(quotationSearchDto.getProduct())) {
					predicates.add(criteriaBuilder.equal(root.get("product"), quotationSearchDto.getProduct()));
				}
				if (quotationSearchDto.getLineOfBusiness() != null) {
					predicates.add(
							criteriaBuilder.equal(root.get("lineOfBusiness"), quotationSearchDto.getLineOfBusiness()));
				}

				if (StringUtils.isNotBlank(quotationSearchDto.getUnitOffice())) {
					predicates.add(criteriaBuilder.equal(root.get("unitCode"), quotationSearchDto.getUnitOffice()));
				}
				predicates = checkLoginInprogress(quotationSearchDto.getQuotationStatus(), quotationSearchDto.getRole(),
						criteriaBuilder, root, predicates);
				predicates.add(criteriaBuilder.isTrue(root.get("isActive").as(Boolean.class)));
				createQuery.orderBy(criteriaBuilder.desc(root.get("quotationId")));
				createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
				quotationTempEntities = entityManager.createQuery(createQuery).getResultList();
				List<QuotationDto> quotationDtos = mapList(quotationTempEntities, QuotationDto.class);
				commonDto.setResponseData(quotationDtos);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
				commonDto.setTransactionStatus(CommonConstants.STATUS);

			} else {
				commonDto.setTransactionStatus(CommonConstants.FAIL);
				commonDto.setTransactionMessage("UnitOffice or UserRole is null!");
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:existingCitrieaSearch", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:existingCitrieaSearch:Ends");
		}
		return commonDto;
	}

	public List<Predicate> checkLoginInprogress(String mergerStatus, String roleType, CriteriaBuilder criteriaBuilder,
			Root<QuotationTempEntity> root, List<Predicate> predicates) {
		if (mergerStatus == null && roleType.equalsIgnoreCase(CommonConstants.MAKER)) {
			Path<Integer> paths = root.get(STATUS);
			predicates.add(paths.in(CommonConstants.inprogressMaker()));
		} else if (mergerStatus == null && roleType.equalsIgnoreCase(CommonConstants.CHECKER)) {
			Path<Integer> paths = root.get(STATUS);
			predicates.add(paths.in(CommonConstants.inprogressChecker()));
		} else if (mergerStatus == null && roleType.equalsIgnoreCase(CommonConstants.ACTUARY)) {
			Path<Integer> paths = root.get(STATUS);
			predicates.add(paths.in(CommonConstants.inprogressActuary()));
		} else if (mergerStatus != null) {
			predicates.add(criteriaBuilder.equal(root.get(STATUS), NumericUtils.convertStringToInteger(mergerStatus)));
		}
		return predicates;
	}

	@Override
	public QuotationApiResponseDto existingCitrieaSearch(QuotationSearchDto quotationSearchDto) {

		List<QuotationEntity> quotationEntities = new ArrayList<>();
		QuotationApiResponseDto commonDto = new QuotationApiResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("QuotationServiceImpl:inprogressCitrieaSearch:Start");

			if (quotationSearchDto.getRole() != null && quotationSearchDto.getUnitOffice() != null) {

				List<Predicate> predicates = new ArrayList<>();
				CriteriaQuery<QuotationEntity> createQuery = criteriaBuilder.createQuery(QuotationEntity.class);
				Root<QuotationEntity> root = createQuery.from(QuotationEntity.class);

				if (StringUtils.isNotBlank(quotationSearchDto.getFrom())
						&& StringUtils.isNotBlank(quotationSearchDto.getTo())) {
					Date fromDate = CommonDateUtils.convertStringToDate(quotationSearchDto.getFrom());
					Date toDate = CommonDateUtils.convertStringToDate(quotationSearchDto.getTo());
					toDate = CommonDateUtils.constructeEndDateTime(toDate);
					predicates.add(criteriaBuilder.between(root.get("quotationDate"), fromDate, toDate));
				}
				if (quotationSearchDto.getProposalNumber() != null) {
					predicates.add(
							criteriaBuilder.equal(root.get("proposalNumber"), quotationSearchDto.getProposalNumber()));
				}
				if (StringUtils.isNotBlank(quotationSearchDto.getMphCode())) {
					predicates.add(criteriaBuilder.equal(root.get("mphCode"), quotationSearchDto.getMphCode()));
				}
				if (StringUtils.isNotBlank(quotationSearchDto.getMphName())) {
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("mphName")),
							"%" + quotationSearchDto.getMphName().toLowerCase() + "%"));
				}
				if (StringUtils.isNotBlank(quotationSearchDto.getProduct())) {
					predicates.add(criteriaBuilder.equal(root.get("product"), quotationSearchDto.getProduct()));
				}
				if (quotationSearchDto.getLineOfBusiness() != null) {
					predicates.add(
							criteriaBuilder.equal(root.get("lineOfBusiness"), quotationSearchDto.getLineOfBusiness()));
				}

				if (!quotationSearchDto.getRole().equalsIgnoreCase(CommonConstants.MAKER)) {
					predicates.add(criteriaBuilder.isTrue(root.get("isActive").as(Boolean.class)));
				}

				predicates = checkLoginMaster(quotationSearchDto.getQuotationStatus(), quotationSearchDto.getRole(),
						criteriaBuilder, root, predicates);

				createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
				quotationEntities = entityManager.createQuery(createQuery).getResultList();
				List<QuotationDto> quotationDtos = mapList(quotationEntities, QuotationDto.class);
				commonDto.setResponseData(quotationDtos);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
				commonDto.setTransactionStatus(CommonConstants.STATUS);

			} else {
				commonDto.setTransactionStatus(CommonConstants.FAIL);
				commonDto.setTransactionMessage("UnitOffice or UserRole is null!");
			}

		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:inprogressCitrieaSearch", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:inprogressCitrieaSearch:Ends");
		}
		return commonDto;
	}

	public List<Predicate> checkLoginMaster(String mergerStatus, String roleType, CriteriaBuilder criteriaBuilder,
			Root<QuotationEntity> root, List<Predicate> predicates) {
		logger.info("{}", LOGSTART);
		if (mergerStatus == null && roleType.equalsIgnoreCase(CommonConstants.MAKER)) {
			Path<Integer> paths = root.get(STATUS);
			predicates.add(paths.in(CommonConstants.existingMakerInteger()));
		} else if (mergerStatus == null && roleType.equalsIgnoreCase(CommonConstants.CHECKER)) {
			Path<Integer> paths = root.get(STATUS);
			predicates.add(paths.in(CommonConstants.existingMakerInteger()));
		} else if (mergerStatus == null && roleType.equalsIgnoreCase(CommonConstants.ACTUARY)) {
			Path<Integer> paths = root.get(STATUS);
			predicates.add(paths.in(CommonConstants.existingActuary()));
		} else if (mergerStatus != null) {
			predicates.add(criteriaBuilder.equal(root.get(STATUS), NumericUtils.convertStringToInteger(mergerStatus)));
		}
		logger.info("{}", LOGSTART);
		return predicates;
	}

	@Override
	public QuotationApiResponseDto sendToCheker(Integer quotationId, String quotationStatus) {
		QuotationApiResponseDto quotationApiResponseDto = new QuotationApiResponseDto();
		logger.info("sendToCheker::quotationId::{}quotationStatus::{}::{}", quotationId, quotationStatus, LOGSTART);
		try {
			
			if(quotationId !=null && !quotationStatus.equalsIgnoreCase("")) {
				QuotationTempEntity quotationTemp = quotationTempRepository.findByQuotationIdAndIsActiveTrue(quotationId);
				if (quotationTemp !=null ) {
					quotationTemp.setStatus(NumericUtils.convertStringToInteger(quotationStatus));
					quotationTemp.setModifiedOn(DateUtils.sysDate());
					quotationTemp = quotationTempRepository.save(quotationTemp);
					quotationApiResponseDto.setQuotationId(quotationTemp.getQuotationId());
					quotationApiResponseDto.setQuotationDto(modelMapper.map(quotationTemp, QuotationDto.class));
					quotationApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
					quotationApiResponseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
				}else {
					quotationApiResponseDto.setTransactionStatus(CommonConstants.FAIL);
					quotationApiResponseDto.setTransactionStatus(CommonConstants.NO_RECORD_FOUND+" against this Quotation Id "+quotationId);
				}
			}else {
				quotationApiResponseDto.setTransactionStatus(CommonConstants.FAIL);
				quotationApiResponseDto.setTransactionStatus("Quotation Id / Quotation Status is Null");
			}

		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:sendToCheker", e);
			quotationApiResponseDto.setTransactionStatus(CommonConstants.FAIL);
			quotationApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:sendToCheker:Ends");
			logger.info("sendToCheker::quotationId::{}quotationStatus::{}::{}", quotationId, quotationStatus, LOGEND);
		}
		return quotationApiResponseDto;
	}

	@Override
	public QuotationApiResponseDto sendToMaker(Integer quotationId, String quotationStatus) {
		QuotationApiResponseDto quotationApiResponseDto = new QuotationApiResponseDto();
		logger.info("sendToMaker::quotationId::{}quotationStatus::{}::{}", quotationId, quotationStatus, LOGSTART);
		try {
			
			if(quotationId !=null && !quotationStatus.equalsIgnoreCase("")) {
				QuotationTempEntity quotationTemp = quotationTempRepository.findByQuotationIdAndIsActiveTrue(quotationId);
				if (quotationTemp !=null ) {
					quotationTemp.setStatus(NumericUtils.convertStringToInteger(quotationStatus));
					quotationTemp.setModifiedOn(DateUtils.sysDate());
					quotationTemp = quotationTempRepository.save(quotationTemp);
					quotationApiResponseDto.setQuotationId(quotationTemp.getQuotationId());
					quotationApiResponseDto.setQuotationDto(modelMapper.map(quotationTemp, QuotationDto.class));
					quotationApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
					quotationApiResponseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
				}else {
					quotationApiResponseDto.setTransactionStatus(CommonConstants.FAIL);
					quotationApiResponseDto.setTransactionStatus(CommonConstants.NO_RECORD_FOUND+" against this Quotation Id "+quotationId);
				}
			}else {
				quotationApiResponseDto.setTransactionStatus(CommonConstants.FAIL);
				quotationApiResponseDto.setTransactionStatus("Quotation Id / Quotation Status is Null");
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:sendToMaker", e);
			quotationApiResponseDto.setTransactionStatus(CommonConstants.FAIL);
			quotationApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:sendToMaker:Ends");
			logger.info("sendToMaker::quotationId::{}quotationStatus::{}::{}", quotationId, quotationStatus, LOGEND);
		}
		return quotationApiResponseDto;
	}

	@Override
	public QuotationApiResponseDto searchChecker(String quotationStatus) {
		QuotationApiResponseDto commonDto = new QuotationApiResponseDto();
		try {
			logger.info("quotationStatus::{}::{}", quotationStatus, LOGSTART);
			List<QuotationTempEntity> quotationTempEntities = quotationTempRepository.findByStatus(quotationStatus);
			if (!CollectionUtils.isEmpty(quotationTempEntities)) {
				List<QuotationDto> quotationDtos = mapList(quotationTempEntities, QuotationDto.class);
				commonDto.setResponseData(quotationDtos);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
				commonDto.setTransactionStatus(CommonConstants.FETCH_MESSAGE);
			} else {
				commonDto.setTransactionStatus(CommonConstants.DENY);
				commonDto.setTransactionMessage(CommonConstants.NO_RESULT);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:searchChecker", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionMessage(e.getMessage());
		}
		logger.info("quotationStatus::{}::{}", quotationStatus, LOGEND);
		return commonDto;
	}

	@Override
	public QuotationApiResponseDto sendToApprover(Integer quotationId, String quotationStatus) {
		QuotationApiResponseDto quotationApiResponseDto = new QuotationApiResponseDto();
		try {
			logger.info("quotationId::{}quotationStatus::{}::{}", quotationId, quotationStatus, LOGEND);
			QuotationTempEntity quotationTemp = quotationTempRepository.findByQuotationIdAndIsActiveTrue(quotationId);
			
			if (quotationTemp != null) {
				quotationTemp.setStatus(NumericUtils.convertStringToInteger(quotationStatus));
				quotationTemp.setModifiedOn(DateUtils.sysDate());
				quotationTemp = quotationTempRepository.save(quotationTemp);
				
				quotationApiResponseDto.setQuotationId(quotationTemp.getQuotationId());
				quotationApiResponseDto.setQuotationDto(modelMapper.map(quotationTemp, QuotationDto.class));
				quotationApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				quotationApiResponseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
			} else {
				quotationApiResponseDto.setTransactionStatus(CommonConstants.FAIL);
				quotationApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
				return quotationApiResponseDto;
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:sendToApprover", e);
			quotationApiResponseDto.setTransactionStatus(CommonConstants.FAIL);
			quotationApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:sendToApprover:Ends");
			logger.info("quotationId::{}quotationStatus::{}::{}", quotationId, quotationStatus, LOGEND);
		}
		return quotationApiResponseDto;
	}

	@Transactional
	@Override
	public QuotationApiResponseDto approveOrReject(QuotationDto dto) {
		logger.info("dto.getQuotationId()::{}::{}", dto.getQuotationId(), LOGSTART);
		QuotationApiResponseDto quotationApiResponseDto = new QuotationApiResponseDto();

		try {

			QuotationTempEntity quotationTemp = quotationTempRepository.findByQuotationIdAndIsActiveTrue(dto.getQuotationId());
			if (quotationTemp != null) {
				
				quotationTemp.setStatus(dto.getStatus());
				quotationTemp.setModifiedOn(DateUtils.sysDate());
				
				if (dto.getStatus() == Integer.valueOf(CommonConstants.COMMON_REJECT)) {
					quotationTemp.setRejectionRemarks(dto.getRejectionRemarks());
					quotationTemp.setRejectionReasonCode(dto.getRejectionReasonCode());
				}
				
				quotationTemp = quotationTempRepository.save(quotationTemp);

//				deActivateQuotation(quotationTemp.getProposalNumber());

				if (dto.getProposalNumber() != null && dto.getUnitCode() != null && quotationTemp.getStatus()== Integer.valueOf(CommonConstants.COMMON_APPROVED)) {
					
					QuotationEntity currentQuotation = quotationRepository.findByProposalNumberAndIsActiveTrueAndUnitCodeAndStatus(
									dto.getProposalNumber(), dto.getUnitCode(),Integer.valueOf(CommonConstants.COMMON_APPROVED));

					if (currentQuotation != null) {
						Set<QuotationHistoryEntity> historyList = currentQuotation.getHistory();
						
						QuotationHistoryEntity quoHistory = modelMapper.map(currentQuotation,QuotationHistoryEntity.class);
						quoHistory.setCreatedOn(new Date());
						quoHistory.setHistoryId(null);
						quoHistory.setCreatedBy(dto.getCreatedBy());
						quoHistory.setIsActive(Boolean.TRUE);
						historyList.add(quoHistory);
						
						currentQuotation.setHistory(historyList);
						currentQuotation.setIsActive(Boolean.FALSE);
						currentQuotation.setStatus(Integer.valueOf(CommonConstants.COMMON_INACTIVE));
						quotationRepository.save(currentQuotation);
					}

				}

				QuotationEntity quotation = modelMapper.map(quotationTemp, QuotationEntity.class);
				quotation = moveQuotationTempToQuotation(quotationTemp, quotation);
				quotation.setIsActive(Boolean.TRUE);
				quotation.setQuotationId(null);
				quotation = quotationRepository.save(quotation);

				
				
//				List<QuotationMemberTempEntity> memberList = quotationMemberTempRepo.findAllByQuotationId(dto.getQuotationId());
//				
//				Integer quotationId = quotation.getQuotationId();
				
				

				quotationApiResponseDto.setQuotationDto(modelMapper.map(quotation, QuotationDto.class));
				quotationApiResponseDto.setQuotationId(quotation.getQuotationId());
				quotationApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				quotationApiResponseDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
			} else {
				quotationApiResponseDto.setTransactionStatus(CommonConstants.FAIL);
				quotationApiResponseDto.setTransactionMessage(CommonConstants.NO_RESULT);
				return quotationApiResponseDto;
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:approveOrReject", e);
			quotationApiResponseDto.setTransactionStatus(CommonConstants.FAIL);
			quotationApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("dto.getQuotationId()::{}::{}", dto.getQuotationId(), LOGEND);

		return quotationApiResponseDto;
	}

	public QuotationEntity moveQuotationTempToQuotation(QuotationTempEntity quotationTemp, QuotationEntity quotation) {
		logger.info("{}", LOGSTART);

		Set<CommonRuleEntity> rulesList = new HashSet<>();
		for (CommonRuleTempEntity rulesTemp : quotationTemp.getRules()) {
			CommonRuleEntity rules = new CommonRuleEntity();
			rules.setRuleId(null);
			rules.setCategory(rulesTemp.getCategory());
			rules.setNormalAgeRetirement(rulesTemp.getNormalAgeRetirement());
			rules.setMinAgeRetirement(rulesTemp.getMinAgeRetirement());
			rules.setMinServicePension(rulesTemp.getMinServicePension());
			rules.setMinServiceWithdrawal(rulesTemp.getMinServiceWithdrawal());
			rules.setContributionType(rulesTemp.getContributionType());
			rules.setMinPension(rulesTemp.getMinPension());
			rules.setMaxPension(rulesTemp.getMaxPension());
			rules.setBenifitPayableTo(rulesTemp.getBenifitPayableTo());
			rules.setCommutationBy(rulesTemp.getCommutationBy());
			rules.setCommutationAmt(rulesTemp.getCommutationAmt());
			rules.setModifiedBy(rulesTemp.getModifiedBy());
			rules.setModifiedOn(rulesTemp.getModifiedOn());
			rules.setCreatedBy(rulesTemp.getCreatedBy());
			rules.setCreatedOn(rulesTemp.getCreatedOn());
			rules.setAnnuityOption(rulesTemp.getAnnuityOption());
			rules.setPercentageCorpus(rulesTemp.getPercentageCorpus());
			rulesList.add(rules);
		}
		quotation.setRules(rulesList);


		List<CommonDocsEntity> docsList = new ArrayList<>();
		for (CommonDocsTempEntity docsTemp : quotationTemp.getDocs()) {
			CommonDocsEntity docs = new CommonDocsEntity();
			docs.setDocId(null);
			docs.setRequirement(docsTemp.getRequirement());
			docs.setQuotationId(null);
			docs.setStatus(docsTemp.getStatus());
			docs.setComments(docsTemp.getComments());
			docs.setModifiedBy(docsTemp.getModifiedBy());
			docs.setModifiedOn(docsTemp.getModifiedOn());
			docs.setCreatedBy(docsTemp.getCreatedBy());
			docs.setCreatedOn(docsTemp.getCreatedOn());
			docs.setDocumentIndex(docsTemp.getDocumentIndex());
			docs.setFolderIndex(docsTemp.getFolderIndex());
			docs.setMergeId(null);
			docs.setSurrenderId(null);
			docs.setIsActive(docsTemp.getIsActive());
			;
			docsList.add(docs);
		}
		quotation.setDocs(docsList);

		List<CommonNoteEntity> notesList = new ArrayList<>();
		for (CommonNoteTempEntity notesTemp : quotationTemp.getNotes()) {
			CommonNoteEntity notes = new CommonNoteEntity();
			notes.setNoteId(null);
			notes.setNoteContents(notesTemp.getNoteContents());
			notes.setIsActive(Boolean.TRUE);
			notes.setModifiedBy(notesTemp.getModifiedBy());
			notes.setModifiedOn(notesTemp.getModifiedOn());
			notes.setCreatedBy(notesTemp.getCreatedBy());
			notes.setCreatedOn(notesTemp.getCreatedOn());
			notes.setMergeId(null);
			notes.setSurrenderId(null);
			notes.setTrnsfrId(null);

			notesList.add(notes);
		}
		quotation.setNotes(notesList);

		Set<QuotationLiabilityEntity> liabilityList = new HashSet<>();
		for (QuotationLiabilityTempEntity liabilityTemp : quotationTemp.getLiabilities()) {
			QuotationLiabilityEntity liability = new QuotationLiabilityEntity();

			liability.setLiabilityId(null);
			liability.setLiability(liabilityTemp.getLiability());
			liability.setInternalValuation(liabilityTemp.getInternalValuation());
			liability.setExternalaluation(liabilityTemp.getExternalaluation());
			liability.setValidity(liabilityTemp.getValidity());
			liability.setCreatedBy(liabilityTemp.getCreatedBy());
			liability.setCreatedOn(liabilityTemp.getCreatedOn());
			liability.setModifiedBy(liabilityTemp.getModifiedBy());
			liability.setModifiedOn(liabilityTemp.getModifiedOn());

			liabilityList.add(liability);
		}
		quotation.setLiabilities(liabilityList);
		
		Integer mannualLicId =0;
		Set<QuotationMemberEntity> memberList = new HashSet<>();
		for(QuotationMemberTempEntity itr :quotationTemp.getMembers()) {
			QuotationMemberEntity member = modelMapper.map(itr, QuotationMemberEntity.class);
			mannualLicId++;
			member.setMemberId(null);
			member.setMemberStatus(CommonConstants.ACTIVE);
			member.setQuotationId(null);
			member.setLicId(mannualLicId.toString());
			member = moveQuotationMembersTempToQuotationMembers(itr, member);
			memberList.add(member);
		};
		
		quotation.setMembers(memberList);
		
		
		
		return quotation;
	}

	public QuotationMemberEntity moveQuotationMembersTempToQuotationMembers(QuotationMemberTempEntity quotationMemberTempEntity,
			QuotationMemberEntity quotationMemberEntity) {
		logger.info("{}", LOGSTART);

		Set<QuotationMemberAddressEntity> quotationMemberAddressEntityList = new HashSet<>();
		for (QuotationMemberAddressTempEntity quotationMemberAddressTempEntity : quotationMemberTempEntity
				.getQuotationMemberAddresses()) {
			QuotationMemberAddressEntity quotationMemberAddressEntity = new QuotationMemberAddressEntity();
			quotationMemberAddressEntity.setAddressType(quotationMemberAddressTempEntity.getAddressType());
			quotationMemberAddressEntity.setCountry(quotationMemberAddressTempEntity.getCountry());
			quotationMemberAddressEntity.setPinCode(quotationMemberAddressTempEntity.getPinCode());
			quotationMemberAddressEntity.setDistrict(quotationMemberAddressTempEntity.getDistrict());
			quotationMemberAddressEntity.setState(quotationMemberAddressTempEntity.getState());
			quotationMemberAddressEntity.setCity(quotationMemberAddressTempEntity.getCity());
			quotationMemberAddressEntity.setAddressLineOne(quotationMemberAddressTempEntity.getAddressLineOne());
			quotationMemberAddressEntity.setAddressLineTwo(quotationMemberAddressTempEntity.getAddressLineTwo());
			quotationMemberAddressEntity.setAddressLineThree(quotationMemberAddressTempEntity.getAddressLineThree());
			quotationMemberAddressEntity.setIsActive(true);
			quotationMemberAddressEntity.setCreatedBy(quotationMemberAddressTempEntity.getCreatedBy());
			quotationMemberAddressEntity.setCreatedOn(quotationMemberAddressTempEntity.getCreatedOn());
			quotationMemberAddressEntity.setModifiedBy(quotationMemberAddressTempEntity.getModifiedBy());
			quotationMemberAddressEntity.setModifiedOn(quotationMemberAddressTempEntity.getModifiedOn());
			quotationMemberAddressEntity.setAddressId(null);
			quotationMemberAddressEntityList.add(quotationMemberAddressEntity);
		}
		quotationMemberEntity.setQuotationMemberAddresses(quotationMemberAddressEntityList);
		Set<QuotationMemberBankDetailEntity> quotationMemberBankDetailEntityList = new HashSet<>();
		for (QuotationMemberBankDetailTempEntity quotationMemberBankDetailTempEntity : quotationMemberTempEntity
				.getQuotationMemberBankDetails()) {
			QuotationMemberBankDetailEntity quotationMemberBankDetailEntity = new QuotationMemberBankDetailEntity();
			quotationMemberBankDetailEntity.setAccountNumber(quotationMemberBankDetailTempEntity.getAccountNumber());
			quotationMemberBankDetailEntity
					.setConfirmAccountNumber(quotationMemberBankDetailTempEntity.getConfirmAccountNumber());
			quotationMemberBankDetailEntity.setAccountType(quotationMemberBankDetailTempEntity.getAccountType());
			quotationMemberBankDetailEntity
					.setIfscCodeAvailable(quotationMemberBankDetailTempEntity.getIfscCodeAvailable());
			quotationMemberBankDetailEntity.setBankId(null);
			quotationMemberBankDetailEntity.setIfscCode(quotationMemberBankDetailTempEntity.getIfscCode());
			quotationMemberBankDetailEntity.setBankName(quotationMemberBankDetailTempEntity.getBankName());
			quotationMemberBankDetailEntity.setBankBranch(quotationMemberBankDetailTempEntity.getBankBranch());
			quotationMemberBankDetailEntity.setBankAddress(quotationMemberBankDetailTempEntity.getBankAddress());
			quotationMemberBankDetailEntity.setCountryCode(quotationMemberBankDetailTempEntity.getCountryCode());
			quotationMemberBankDetailEntity.setStdCode(quotationMemberBankDetailTempEntity.getStdCode());
			quotationMemberBankDetailEntity.setLandlineNumber(quotationMemberBankDetailTempEntity.getLandlineNumber());
			quotationMemberBankDetailEntity.setEmailId(quotationMemberBankDetailTempEntity.getEmailId());
			quotationMemberBankDetailEntity.setIsActive(true);
			quotationMemberBankDetailEntity.setCreatedBy(quotationMemberBankDetailTempEntity.getCreatedBy());
			quotationMemberBankDetailEntity.setCreatedOn(quotationMemberBankDetailTempEntity.getCreatedOn());
			quotationMemberBankDetailEntity.setModifiedBy(quotationMemberBankDetailTempEntity.getModifiedBy());
			quotationMemberBankDetailEntity.setModifiedOn(quotationMemberBankDetailTempEntity.getModifiedOn());
			quotationMemberBankDetailEntityList.add(quotationMemberBankDetailEntity);

		}
		quotationMemberEntity.setQuotationMemberBankDetails(quotationMemberBankDetailEntityList);

		Set<QuotationMemberAppointeeEntity> quotationMemberAppointeeEntityList = new HashSet<>();
		for (QuotationMemberAppointeeTempEntity quotationMemberAppointeeTempEntity : quotationMemberTempEntity
				.getQuotationMemberAppointees()) {
			QuotationMemberAppointeeEntity quotationMemberAppointeeEntity = new QuotationMemberAppointeeEntity();
			quotationMemberAppointeeEntity.setAppointeeId(null);
			quotationMemberAppointeeEntity.setAppointeeName(quotationMemberAppointeeTempEntity.getAppointeeName());
			quotationMemberAppointeeEntity.setAppointeeCode(quotationMemberAppointeeTempEntity.getAppointeeCode());
			quotationMemberAppointeeEntity.setRelationship(quotationMemberAppointeeTempEntity.getRelationship());
			quotationMemberAppointeeEntity.setContactNumber(quotationMemberAppointeeTempEntity.getContactNumber());
			quotationMemberAppointeeEntity.setDateOfBirth(quotationMemberAppointeeTempEntity.getDateOfBirth());
			quotationMemberAppointeeEntity.setPan(quotationMemberAppointeeTempEntity.getPan());
			quotationMemberAppointeeEntity.setAadharNumber(quotationMemberAppointeeTempEntity.getAadharNumber());
			quotationMemberAppointeeEntity.setIfscCode(quotationMemberAppointeeTempEntity.getIfscCode());
			quotationMemberAppointeeEntity.setBankName(quotationMemberAppointeeTempEntity.getBankName());
			quotationMemberAppointeeEntity.setAccountType(quotationMemberAppointeeTempEntity.getAccountType());
			quotationMemberAppointeeEntity.setAccountNumber(quotationMemberAppointeeTempEntity.getAccountNumber());
			quotationMemberAppointeeEntity.setIsActive(true);
			quotationMemberAppointeeEntity.setCreatedBy(quotationMemberAppointeeTempEntity.getCreatedBy());
			quotationMemberAppointeeEntity.setCreatedOn(quotationMemberAppointeeTempEntity.getCreatedOn());
			quotationMemberAppointeeEntity.setModifiedBy(quotationMemberAppointeeTempEntity.getModifiedBy());
			quotationMemberAppointeeEntity.setModifiedOn(quotationMemberAppointeeTempEntity.getModifiedOn());
			quotationMemberAppointeeEntityList.add(quotationMemberAppointeeEntity);
		}
		quotationMemberEntity.setQuotationMemberAppointees(quotationMemberAppointeeEntityList);

		Set<QuotationMemberNomineeEntity> nomineeEntityList = new HashSet<>();

		for (QuotationMemberNomineeTempEntity nomineeTemp : quotationMemberTempEntity.getQuotationMemberNominees()) {
			QuotationMemberNomineeEntity nominee = new QuotationMemberNomineeEntity();
			nominee.setNomineeId(null);
			nominee.setNomineeType(nomineeTemp.getNomineeType());
			nominee.setNomineeName(nomineeTemp.getNomineeName());
			nominee.setRelationShip(nomineeTemp.getRelationShip());
			nominee.setAadharNumber(nomineeTemp.getAadharNumber());
			nominee.setPhone(nomineeTemp.getPhone());
			nominee.setEmailId(nomineeTemp.getEmailId());
			nominee.setAddressOne(nomineeTemp.getAddressOne());
			nominee.setAddressTwo(nomineeTemp.getAddressTwo());
			nominee.setAddressThree(nomineeTemp.getAddressThree());
			nominee.setCountry(nomineeTemp.getCountry());
			nominee.setPinCode(nomineeTemp.getPinCode());
			nominee.setDistrict(nomineeTemp.getDistrict());
			nominee.setState(nomineeTemp.getState());
			nominee.setCreatedBy(nomineeTemp.getCreatedBy());
			nominee.setCreatedOn(new Date());
			nominee.setModifiedBy(nomineeTemp.getModifiedBy());
			nominee.setModifiedOn(new Date());
			nominee.setIsActive(nomineeTemp.getIsActive());
			nominee.setPercentage(nomineeTemp.getPercentage());
			nominee.setAge(nomineeTemp.getAge());
			nominee.setDateOfBirth(nomineeTemp.getDateOfBirth());
			nomineeEntityList.add(nominee);

		}
		quotationMemberEntity.setQuotationMemberNominees(nomineeEntityList);
		logger.info("{}", LOGEND);

		return quotationMemberEntity;
	}
	
	
	
//dont remove !!!!!!!!!!!!!!!!!!!!!!!	

//	public void deActivateQuotation(String proposalNo) {
//		logger.info("deActivateQuotation::ProposalNo::{}::{}", proposalNo, LOGSTART);
//
//		List<QuotationTempEntity> quotationTempData = quotationTempRepository.findByProposalNumber(proposalNo);
//		if (quotationTempData != null) {
//			quotationTempData.stream().forEach(quotation -> quotation.setIsActive(Boolean.FALSE));
//			quotationTempRepository.saveAll(quotationTempData);
//		}
//
//		List<QuotationEntity> quotationData = quotationRepository.findByProposalNumber(proposalNo);
//		if (quotationData != null) {
//			quotationData.stream().forEach(quotation -> quotation.setIsActive(Boolean.FALSE));
//			quotationRepository.saveAll(quotationData);
//		}
//		logger.info("deActivateQuotation::ProposalNo::{}::{}", proposalNo, LOGEND);
//	}

	@Override
	public QuotationApiResponseDto getQuotationByProposalNumber(String proposalNumber) {
		logger.info("ProposalNo::{}::{}", proposalNumber, LOGSTART);
		QuotationApiResponseDto commonDto = new QuotationApiResponseDto();
		try {
			logger.info("QuotationServiceImpl:getQuotationByProposalNumber:Start");
			QuotationEntity quotation = quotationRepository.findByProposalNumberAndStatusAndIsActiveTrue(proposalNumber,Integer.valueOf(CommonConstants.COMMON_APPROVED));
			MphMasterEntity policyCheck = mphMasterRepository.findByProposalNumberAndIsActiveTrue(proposalNumber);
			MphMasterTempEntity policyTempCheck = mphMasterTempRepository.findByProposalNumberAndIsActiveTrue(proposalNumber);
			
			if (quotation != null && policyCheck == null && policyTempCheck == null ) {
				QuotationDto quotationDto = modelMapper.map(quotation, QuotationDto.class);
				quotationDto.setLeadNumber(null);
				commonDto.setQuotationId(quotationDto.getQuotationId());
				commonDto.setQuotationDto(quotationDto);
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
			} else if ( policyCheck != null || policyTempCheck != null ) {																					// {
				commonDto.setTransactionMessage(CommonConstants.RECORDALREADYUSED);
				commonDto.setTransactionStatus(CommonConstants.FAIL);
			} else {
				commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
				commonDto.setTransactionStatus(CommonConstants.FAIL);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:getQuotationByProposalNumber", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("ProposalNo::{}::{}", proposalNumber, LOGEND);
			logger.info("QuotationServiceImpl:getQuotationByProposalNumber:Ends");
		}
		return commonDto;
	}

	@Override
	public QuotationApiResponseDto getQuotationOtherCriteria(QuotationSearchDto quotationSearchDto) {
		List<QuotationEntity> quotationEntities = new ArrayList<>();
		QuotationApiResponseDto commonDto = new QuotationApiResponseDto();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		try {
			logger.info("QuotationServiceImpl:getQuotationOtherCriteria:Start");
			List<Predicate> predicates = new ArrayList<>();
			CriteriaQuery<QuotationEntity> createQuery = criteriaBuilder.createQuery(QuotationEntity.class);
			Root<QuotationEntity> root = createQuery.from(QuotationEntity.class);

			if (StringUtils.isNotBlank(quotationSearchDto.getFrom())
					&& StringUtils.isNotBlank(quotationSearchDto.getTo())) {
				Date fromDate = CommonDateUtils.convertStringToDate(quotationSearchDto.getFrom());
				Date toDate = CommonDateUtils.convertStringToDate(quotationSearchDto.getTo());
				toDate = CommonDateUtils.constructeEndDateTime(toDate);
				predicates.add(criteriaBuilder.between(root.get("createdOn"), fromDate, toDate));
			}
			if (StringUtils.isNotBlank(quotationSearchDto.getProposalNumber()) && quotationSearchDto.getProposalNumber() != null) {
				predicates.add(criteriaBuilder.equal(root.get("proposalNumber"), quotationSearchDto.getProposalNumber()));
			}
			if (StringUtils.isNotBlank(quotationSearchDto.getMphCode()) && quotationSearchDto.getMphCode() != null) {
				predicates.add(criteriaBuilder.equal(root.get("mphCode"), quotationSearchDto.getMphCode()));
			}
			if (StringUtils.isNotBlank(quotationSearchDto.getMphName()) && quotationSearchDto.getMphName() != null) {
				predicates.add(criteriaBuilder.like(root.get("mphName"), "%" + quotationSearchDto.getMphName() + "%"));
			}
			
			
			if (StringUtils.isNotBlank(quotationSearchDto.getProduct()) && quotationSearchDto.getProduct() != null) {
				predicates.add(criteriaBuilder.equal(root.get("product"), quotationSearchDto.getProduct()));
			}
			if ( StringUtils.isNotBlank(quotationSearchDto.getLineOfBusiness()) && quotationSearchDto.getLineOfBusiness() != null) {
				predicates
						.add(criteriaBuilder.equal(root.get("lineOfBusiness"), quotationSearchDto.getLineOfBusiness()));
			}
			if (StringUtils.isNotBlank(quotationSearchDto.getQuotationStatus())) {
				predicates.add(criteriaBuilder.equal(root.get(STATUS), quotationSearchDto.getQuotationStatus()));
			}
			predicates.add(criteriaBuilder.isTrue(root.get("isActive").as(Boolean.class)));
			createQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			quotationEntities = entityManager.createQuery(createQuery).getResultList();
			List<QuotationDto> quotationDtos = mapList(quotationEntities, QuotationDto.class);
			commonDto.setResponseData(quotationDtos);
			commonDto.setTransactionMessage(CommonConstants.FETCH);
			commonDto.setTransactionStatus(CommonConstants.STATUS);

		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:getQuotationOtherCriteria", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:getQuotationOtherCriteria:Ends");
		}
		return commonDto;
	}

	@Override
	public QuotationApiResponseDto sendToActuary(QuotationSearchDto dto) {
		QuotationApiResponseDto quotationApiResponseDto = new QuotationApiResponseDto();
		try {
			if (dto.getQuotationNo() != null && dto.getUnitOffice() != null && dto.getQuotationStatus() != null) {
				QuotationTempEntity result = quotationTempRepository.findByQuotationIdAndIsActiveTrue(dto.getQuotationId());
				if (result != null) {
					if (dto.getQuotationStatus().equalsIgnoreCase(CommonConstants.COMMON_PENDING_FOR_ACTUARY)) {
						result.setStatus(NumericUtils.convertStringToInteger(dto.getQuotationStatus()));
						result.setModifiedOn(DateUtils.sysDate());
						result.setModifiedBy(dto.getModifiedBy());
						result = quotationTempRepository.save(result);
						quotationApiResponseDto.setQuotationId(result.getQuotationId());
						quotationApiResponseDto.setQuotationDto(modelMapper.map(result, QuotationDto.class));
						quotationApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
						quotationApiResponseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
					}
				}
			} else {
				quotationApiResponseDto.setTransactionStatus(CommonConstants.FAIL);
				quotationApiResponseDto.setTransactionMessage(CommonConstants.INVALID_REQUEST);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:sendToActuary", e);
			quotationApiResponseDto.setTransactionStatus(CommonConstants.FAIL);
			quotationApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:sendToActuary:Ends");
		}
		return quotationApiResponseDto;
	}

	@Override
	public QuotationApiResponseDto uploadDocument(CommonDocsDto docsDto) {
		QuotationApiResponseDto quotationApiResponseDto = new QuotationApiResponseDto();
		logger.info("docsDto.getDocId()::{}::{}", docsDto.getDocId(), LOGSTART);
		try {
			QuotationTempEntity quotationTemp = quotationTempRepository.findByQuotationIdAndIsActiveTrue(docsDto.getQuotationId());
			if(quotationTemp != null){
				
				List<CommonDocsTempEntity> docsList = quotationTemp.getDocs();
				CommonDocsTempEntity docsTemp = modelMapper.map(docsDto, CommonDocsTempEntity.class);
				docsTemp.setDocId(null);
				docsTemp.setIsActive(Boolean.TRUE);
				docsList.add(docsTemp);
				quotationTemp.setDocs(docsList);
				
				quotationTempRepository.save(quotationTemp);				
				
				quotationApiResponseDto.setDocsDto(modelMapper.map(docsTemp, CommonDocsDto.class));
				quotationApiResponseDto.setQuotationId(quotationTemp.getQuotationId());
				quotationApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
				quotationApiResponseDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
				
			}
		

		} catch (IllegalArgumentException e) {
			logger.error("Exception-- QuotationServiceImpl-- quotationApiResponseDto --", e);
			quotationApiResponseDto.setTransactionMessage(CommonConstants.FAIL);
			quotationApiResponseDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("docsDto.getDocId()::{}::{}", docsDto.getDocId(), LOGEND);
		return quotationApiResponseDto;
	}

	@Override
	public QuotationApiResponseDto generateChellanForQuotation(CommonChallanDto commonChallanDto) {
		QuotationApiResponseDto commonDto = new QuotationApiResponseDto();
		try {
			logger.info("QuotationServiceImpl:generateChellanForQuotation:Start");

//			if (commonChallanDto != null) {
//				CommonChallanEntity challanEntity = modelMapper.map(commonChallanDto, CommonChallanEntity.class);
//				challanEntity.setChallanNo(getChallanSeq());
//				challanEntity.setIsActive(Boolean.TRUE);
//				challanEntity.setCreatedBy(commonChallanDto.getCreatedBy());
//				challanEntity = commonChallanRepository.save(challanEntity);
//
//				commonDto.setResponseObject(modelMapper.map(challanEntity, CommonChallanDto.class));
//				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
//				commonDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
//
//			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:generateChellanForQuotation", e);
			commonDto.setTransactionMessage(CommonConstants.SUCCESS);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:generateChellanForQuotation:Ends");
		}
		return commonDto;
	}

	@Override
	public QuotationApiResponseDto getChellanNumber(CommonChallanDto dto) {
		QuotationApiResponseDto commonDto = new QuotationApiResponseDto();
		try {
			logger.info("QuotationServiceImpl:getChellanNumber:Start");

//			if (dto.getQuotationNo() != null && dto.getProposalNumber() != null) {
//				CommonChallanEntity challanEntity = commonChallanRepository
//						.findByQuotationNoAndProposalNumberAndIsActiveTrue(dto.getQuotationNo(),
//								dto.getProposalNumber());
//				if (challanEntity != null) {
//					commonDto.setResponseObject(modelMapper.map(challanEntity, CommonChallanDto.class));
//					commonDto.setTransactionStatus(CommonConstants.SUCCESS);
//					commonDto.setTransactionMessage(CommonConstants.FETCH);
//				} else {
//					commonDto.setTransactionStatus(CommonConstants.SUCCESS);
//					commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
//
//				}
//
//			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:getChellanNumber", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionMessage(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:getChellanNumber:Ends");
		}
		return commonDto;
	}

	@Override
	public QuotationApiResponseDto removeDocumentByremoveDocumentId(String docId, String quotationId) {
		QuotationApiResponseDto commonDto = new QuotationApiResponseDto();
		try {
			logger.info("QuotationServiceImpl:removeDocumentByremoveDocumentId:Start");
			
			
//			QuotationTempEntity quotationTemp = quotationTempRepository.findByQuotationIdAndIsActiveTrue(Integer.valueOf(quotationId));
//			
//			if(quotationTemp != null){
//				
//				Set<CommonDocsTempEntity> docsList = quotationTemp.getDocs();
//				CommonDocsTempEntity docsTemp = modelMapper.map(docsDto, CommonDocsTempEntity.class);
//				docsTemp.setDocId(null);
//				docsTemp.setIsActive(Boolean.TRUE);
//				docsList.add(docsTemp);
//				quotationTemp.setDocs(docsList);
//				
//				quotationTempRepository.save(quotationTemp);				
//				
//				quotationApiResponseDto.setDocsDto(modelMapper.map(docsTemp, CommonDocsDto.class));
//				quotationApiResponseDto.setQuotationId(quotationTemp.getQuotationId());
//				quotationApiResponseDto.setTransactionStatus(CommonConstants.SUCCESS);
//				quotationApiResponseDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
//				
//			}
//			
			
			
			

			if (StringUtils.isNotBlank(docId) && StringUtils.isNotBlank(quotationId)) {
				CommonDocsTempEntity commonDocsTempEntity = commonDocsTempRepository
						.findByDocIdAndQuotationIdAndIsActiveTrue(NumericUtils.stringToInteger(docId),
								NumericUtils.stringToInteger(quotationId));
				if (commonDocsTempEntity != null) {
					commonDocsTempEntity.setIsActive(false);
					commonDocsTempRepository.save(commonDocsTempEntity);
					commonDto.setResponseObject(modelMapper.map(commonDocsTempEntity, CommonDocsDto.class));
					commonDto.setTransactionStatus(CommonConstants.SUCCESS);
					commonDto.setTransactionMessage(CommonConstants.REMOVED);
				} else {
					commonDto.setTransactionMessage(CommonConstants.SUCCESS);
					commonDto.setTransactionStatus(CommonConstants.NO_RECORD_FOUND);
				}

			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:removeDocumentByremoveDocumentId", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:removeDocumentByremoveDocumentId:Ends");
		}
		return commonDto;
	}

	public QuotationApiResponseDto getDocumentDetails(Integer quotationId) {
		QuotationApiResponseDto commonDto = new QuotationApiResponseDto();
		logger.info("quotationId::{}::{}", quotationId, LOGSTART);

		try {
			List<CommonDocsTempEntity> docsTempList = commonDocsTempRepository
					.findAllByQuotationIdAndIsActiveTrue(quotationId);

			if (docsTempList != null) {
				List<CommonDocsDto> dcosDtoList = new ArrayList<>();
				docsTempList.forEach(itr -> {
					CommonDocsTempEntity docsTempEntity = itr;
					dcosDtoList.add(modelMapper.map(docsTempEntity, CommonDocsDto.class));
				});

				commonDto.setResponseObject(dcosDtoList);
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:getDocumentDetails", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionMessage(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:getChellanNumber:Ends");
			logger.info("quotationId::{}::{}", quotationId, LOGEND);
		}
		return commonDto;
	}

	@Override
	public QuotationApiResponseDto activateQuotation(QuotationDto dto) {
		QuotationApiResponseDto commonDto = new QuotationApiResponseDto();
		try {
			logger.info("dto.getProposalNumber()::{}dto.getUnitCode()::{}::{}", dto.getProposalNumber(),
					dto.getUnitCode(), LOGSTART);
			
//			
//			List<PolicyEntity> policy = policyRepository.findAllByProposalNumberAndPolicyStatusNotIn(
//					dto.getProposalNumber(), CommonConstants.rejectedString());
//
//			if (policy.size() > 0) {
//				commonDto.setTransactionMessage(CommonConstants.ACTIVE_QUOTATION_ERROR);
//				commonDto.setTransactionStatus(CommonConstants.FAIL);
//			} else {
//
//				List<PolicyTempEntity> policyTemp = policyTempRepository.findAllByProposalNumberAndPolicyStatusNotIn(
//						dto.getProposalNumber(), CommonConstants.rejectedString());
//
//				if (policyTemp.size() > 0) {
//					commonDto.setTransactionMessage(CommonConstants.ACTIVE_QUOTATION_ERROR);
//					commonDto.setTransactionStatus(CommonConstants.FAIL);
//
//				}else {
			
		
			if(dto.getProposalNumber() != null && dto.getUnitCode() != null) {
				
				QuotationEntity approvedQuotation = quotationRepository.
						findByProposalNumberAndIsActiveTrueAndUnitCodeAndStatus(dto.getProposalNumber(), dto.getUnitCode(),NumericUtils.convertStringToInteger(CommonConstants.COMMON_APPROVED));

				if (approvedQuotation != null) {
					approvedQuotation.setIsActive(Boolean.FALSE);
					approvedQuotation.setStatus(NumericUtils.convertStringToInteger(CommonConstants.COMMON_INACTIVE));
					approvedQuotation.setModifiedBy(dto.getModifiedBy());
					approvedQuotation.setModifiedOn(new Date());
					
					
					
					Set<QuotationHistoryEntity> histories = approvedQuotation.getHistory();
					
					
					QuotationHistoryEntity quoHistory = modelMapper.map(approvedQuotation, QuotationHistoryEntity.class);
					quoHistory.setCreatedOn(new Date());
					quoHistory.setStatus(NumericUtils.convertStringToInteger(CommonConstants.COMMON_INACTIVE));
					quoHistory.setCreatedBy(dto.getCreatedBy());
					quoHistory.setIsActive(Boolean.TRUE);
					quoHistory.setHistoryId(null);
					histories.add(quoHistory);
		
					approvedQuotation.setHistory(histories);
					approvedQuotation = quotationRepository.save(approvedQuotation);
					
					
					if(approvedQuotation !=null) {

						QuotationEntity quotation = quotationRepository.
								findByProposalNumberAndQuotationNoAndUnitCodeAndIsActiveFalseAndStatusNot(dto.getProposalNumber(), dto.getQuotationNo(), dto.getUnitCode(), 5);

					if (quotation != null) {
	
						quotation.setIsActive(Boolean.TRUE);
						quotation.setModifiedBy(dto.getModifiedBy());
						quotation.setModifiedOn(new Date());
						quotation.setStatus(NumericUtils.convertStringToInteger(CommonConstants.COMMON_APPROVED));
						quotation = quotationRepository.save(quotation);
	
						commonDto.setResponseObject(modelMapper.map(quotation, QuotationDto.class));
						commonDto.setTransactionStatus(CommonConstants.SUCCESS);
						commonDto.setTransactionMessage(CommonConstants.FETCH);
	
					} else {
						commonDto.setTransactionStatus(CommonConstants.SUCCESS);
						commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
					}
						
					}


				} else {
					commonDto.setTransactionStatus(CommonConstants.SUCCESS);
					commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
				}
				
			}
				
					
//				}
//				
//			}

			
		} catch (IllegalArgumentException e) {
			logger.error("Exception:QuotationServiceImpl:activateQuotation", e);
			commonDto.setTransactionStatus(CommonConstants.FAIL);
			commonDto.setTransactionMessage(CommonConstants.ERROR);
		} finally {
			logger.info("QuotationServiceImpl:activateQuotation:Ends");
			logger.info("dto.getProposalNumber()::{}dto.getUnitCode()::{}::{}", dto.getProposalNumber(),
					dto.getUnitCode(), LOGEND);
		}
		return commonDto;
	}






	@Override
	public QuotationApiResponseDto getMatrix(Long policyId, String currentservice) {
		   QuotationApiResponseDto responseDto = new QuotationApiResponseDto();
		  logger.info("QuotationServiceImpl:calling all services:Start");
		try {
			List<PolicyServiceMatrixNewDto> policymtxdto = new ArrayList<>();
			List<PolicyMasterEntity> masterEntities = new ArrayList<>();
//		 all policyservice
			List<PolicyServiceEntity> servicetype = policyServicerRepositry.findAllByPolicyIdAndIsActiveTrue(policyId);
			if (CommonUtils.isNonEmptyArray(servicetype) || currentservice != null) {

				if (CommonUtils.isNonEmptyArray(servicetype)) {
					for (PolicyServiceEntity policyServiceEntity : servicetype) {
						if (policyServiceEntity.getServiceStatus().equalsIgnoreCase(CommonConstants.ONE_STRING)
								|| policyServiceEntity.getServiceStatus().equalsIgnoreCase("Service - Active")){
							PolicyMasterEntity entity = new PolicyMasterEntity();
							entity.setPolicyId(policyServiceEntity.getPolicyId());
							entity.setPolicyStatus(policyServiceEntity.getServiceType());
							masterEntities.add(entity);

						}
					}
				}
//		clime
				List<MemberMasterEntity> list = memberMasterRepository.findByPolicyIdAndIsActiveTrue(policyId);
				if (CommonUtils.isNonEmptyArray(list)) {
					for (MemberMasterEntity masterEntity : list) {
						if (masterEntity.getMemberStatus().equalsIgnoreCase(QuotationConstants.MEMBER_ONBOARD)
								|| masterEntity.getMemberStatus().equalsIgnoreCase(QuotationConstants.MEMBER_INTIMATION)
								|| masterEntity.getMemberStatus().equalsIgnoreCase(QuotationConstants.MEMBER_PAYOUT)) {
//						List<PolicyServiceEntity> servicetypes = policyServicerRepositry
//								.findAllByPolicyIdAndIsActiveTrue(policyId);
//						for (PolicyServiceEntity serviceEntity : servicetypes) {
//							newEntity = policySericeMatrixChecking(currentservice, serviceEntity.getServiceStatus());
//							PolicyServiceMatrixNewDto map = modelMapper.map(newEntity, PolicyServiceMatrixNewDto.class);
//							policymtxdto.add(map);
//						}
							PolicyMasterEntity entity = new PolicyMasterEntity();
							entity.setPolicyId(masterEntity.getPolicyId());
							entity.setPolicyStatus("CLAIM");
							masterEntities.add(entity);

						}
					}
				}
//		REGULARADJUSTMENT
//				List<RegularAdjustmentContributionEntity> rontributionEntity = regularAdjustmentContributionRepository
//						.findByPolicyIdAndIsActiveTrue(policyId);
//				if (CommonUtils.isNonEmptyArray(rontributionEntity)) {
//					if (CommonUtils.isNonEmptyArray(rontributionEntity)) {
//						for (RegularAdjustmentContributionEntity contributionEntity : rontributionEntity) {
//							if (contributionEntity.getRegularContributionStatus().equalsIgnoreCase(QuotationConstants.COMMON_DRAFT)
//									|| contributionEntity.getRegularContributionStatus()
//											.equalsIgnoreCase(QuotationConstants.COMMON_PENDING_FOR_APPROVER)
//									|| contributionEntity.getRegularContributionStatus()
//											.equalsIgnoreCase(QuotationConstants.COMMON_PENDING_FOR_MODIFICATION)){
//								PolicyMasterEntity entity = new PolicyMasterEntity();
//								entity.setPolicyStatus("Regular Adjustment");
//								masterEntities.add(entity);
//							}
//						}
//					}
//				}
				List<RegularAdjustmentContributionTempEntity> rcontributionTemps = regularAdjustmentContributionTempRepository
						.findByPolicyIdAndIsActiveTrue(policyId);
				if (CommonUtils.isNonEmptyArray(rcontributionTemps)) {
					for (RegularAdjustmentContributionTempEntity rcontributionTemp : rcontributionTemps) {
						if (rcontributionTemp.getRegularContributionStatus().equalsIgnoreCase(QuotationConstants.COMMON_DRAFT)
								|| rcontributionTemp.getRegularContributionStatus()
										.equalsIgnoreCase(QuotationConstants.COMMON_PENDING_FOR_APPROVER)
								|| rcontributionTemp.getRegularContributionStatus()
										.equalsIgnoreCase(QuotationConstants.COMMON_PENDING_FOR_MODIFICATION)) {
							PolicyMasterEntity entity = new PolicyMasterEntity();
							entity.setPolicyStatus("Regular Adjustment");
							masterEntities.add(entity);
						}

					}
				}
//		SUBSEQUENTADJUSTMENT

//				List<AdjustmentContributionEntity> adjustmentContribution = adjustmentContributionRepository
//						.findByPolicyIdAndIsActiveTrue(policyId);
//				if (CommonUtils.isNonEmptyArray(adjustmentContribution)) {
//					for (AdjustmentContributionEntity entity : adjustmentContribution) {
//
//						if (entity.getAdjustmentContributionStatus().equalsIgnoreCase(QuotationConstants.COMMON_DRAFT)
//								|| entity.getAdjustmentContributionStatus().equalsIgnoreCase(QuotationConstants.COMMON_PENDING_FOR_APPROVER)
//								|| entity.getAdjustmentContributionStatus()
//										.equalsIgnoreCase(QuotationConstants.COMMON_PENDING_FOR_MODIFICATION)
//								|| entity.getAdjustmentContributionStatus().equalsIgnoreCase(QuotationConstants.COMMON_REJECT)) {
//
//							PolicyMasterEntity entitys = new PolicyMasterEntity();
//							entitys.setPolicyStatus("Subsequent Adjustment");
//							masterEntities.add(entitys);
//						}
//					}
//				}
				List<AdjustmentContributionTempEntity> entities = adjustmentContributionTempRepository
						.findByPolicyIdAndIsActiveTrue(policyId);
				if (CommonUtils.isNonEmptyArray(entities)) {
					for (AdjustmentContributionTempEntity entity : entities) {

						if (entity.getAdjustmentContributionStatus().equalsIgnoreCase(QuotationConstants.COMMON_DRAFT)
								|| entity.getAdjustmentContributionStatus().equalsIgnoreCase(QuotationConstants.COMMON_PENDING_FOR_APPROVER)
								|| entity.getAdjustmentContributionStatus()
										.equalsIgnoreCase(QuotationConstants.COMMON_PENDING_FOR_MODIFICATION)) {
							PolicyMasterEntity entitys = new PolicyMasterEntity();
							entitys.setPolicyStatus("Subsequent Adjustment");
							masterEntities.add(entitys);
						}

					}
				}
//			SURRENDER
//				List<PolicySurrenderEntity> policySurrenderEntities=policySurrenderRepository.findByPolicyIdAndIsActiveTrue(policyId);
//				if (CommonUtils.isNonEmptyArray(policySurrenderEntities)) {
//					
//					for(PolicySurrenderEntity surrenderEntity :policySurrenderEntities ) {
//						if(surrenderEntity.getSurrenderStatus().equalsIgnoreCase(QuotationConstants.COMMON_DRAFT)||
//						   surrenderEntity.getSurrenderStatus().equalsIgnoreCase(QuotationConstants.COMMON_PENDING_FOR_APPROVER)||
//						   surrenderEntity.getSurrenderStatus().equalsIgnoreCase(QuotationConstants.COMMON_PENDING_FOR_MODIFICATION)||
//						   surrenderEntity.getSurrenderStatus().equalsIgnoreCase(QuotationConstants.COMMON_REJECT)) {
//							
//							PolicyMasterEntity entitys = new PolicyMasterEntity();
//							entitys.setPolicyStatus("Surrender");
//							masterEntities.add(entitys);
//							
//						}
//					}
//				}
				List<PolicySurrenderTempEntity> surrenderTempEntities=policySurrenderTempRepository.findByPolicyIdAndIsActiveTrue(policyId);
				if (CommonUtils.isNonEmptyArray(surrenderTempEntities)) {
					
					for(PolicySurrenderTempEntity surrenderTempEntity : surrenderTempEntities) {
						if(surrenderTempEntity.getSurrenderStatus().equalsIgnoreCase(QuotationConstants.COMMON_DRAFT)||
						   surrenderTempEntity.getSurrenderStatus().equalsIgnoreCase(QuotationConstants.COMMON_PENDING_FOR_APPROVER)||
						   surrenderTempEntity.getSurrenderStatus().equalsIgnoreCase(QuotationConstants.COMMON_PENDING_FOR_MODIFICATION)) {
							PolicyMasterEntity entitys = new PolicyMasterEntity();
							entitys.setPolicyStatus("Surrender");
							masterEntities.add(entitys);
							
						}
					}
				}
				
				

				for (PolicyMasterEntity policyMasterEntity : masterEntities) {

					if (policyMasterEntity.getPolicyStatus() != null) {

						PolicyServiceMatrixNewEntity matrixNewEntity = policySericeMatrixChecking(currentservice,
								policyMasterEntity.getPolicyStatus());
						if (matrixNewEntity.getOngoingService() != null && matrixNewEntity.getCurrentService() != null) {
							PolicyServiceMatrixNewDto map = modelMapper.map(matrixNewEntity,
									PolicyServiceMatrixNewDto.class);
							policymtxdto.add(map);
						}
					}
				}
				
				

				if (CommonUtils.isNonEmptyArray(policymtxdto)) {
					responseDto.setResponseObject(policymtxdto);
					responseDto.setTransactionStatus(CommonConstants.SUCCESS);
					responseDto.setTransactionMessage(CommonConstants.FETCH);
				} else {
					responseDto.setTransactionStatus(CommonConstants.FAIL);
					responseDto.setTransactionMessage(CommonConstants.ERROR);
				}
			}
		} catch (Exception e) {
			 logger.error("Exception:QuotationServiceImpl:calling all services", e);
			responseDto.setTransactionStatus(CommonConstants.FAIL);
			responseDto.setTransactionMessage(CommonConstants.ERROR);
		}finally {
			logger.info("QuotationServiceImpl:all:Ends");
		}
		
		return responseDto;
	}
	
	private PolicyServiceMatrixNewEntity policySericeMatrixChecking(String Servicetype, String ongoingService) {
		List<Object[]> listOfService = null;
		
		PolicyServiceMatrixNewEntity matrixNewEntity = new PolicyServiceMatrixNewEntity();
		switch (Servicetype) {
		case QuotationConstants.POLICYDETAILSCHANGE:
			listOfService = policyServiceMatrixNewRepository.getPolicyDetailes(Servicetype, ongoingService);
			if (CommonUtils.isNonEmptyArray(listOfService)) {
				for (Object[] obj : listOfService) {
					matrixNewEntity.setCurrentService((obj[0] != null) ? obj[0].toString() : null);
					matrixNewEntity.setOngoingService((obj[1] != null) ? obj[1].toString() : null);
					matrixNewEntity.setIsAllowed((obj[2] != null) ? obj[2].toString() : null);
				}
			}
			break;
		case QuotationConstants.MERGE:
			listOfService = policyServiceMatrixNewRepository.getPolicyDetailes(Servicetype, ongoingService);
			if (CommonUtils.isNonEmptyArray(listOfService)) {
				for (Object[] obj : listOfService) {
					matrixNewEntity.setCurrentService((obj[0] != null) ? obj[0].toString() : null);
					matrixNewEntity.setOngoingService((obj[1] != null) ? obj[1].toString() : null);
					matrixNewEntity.setIsAllowed((obj[2] != null) ? obj[2].toString() : null);
				}
			}
			break;
		case QuotationConstants.CONVERSION:
			listOfService = policyServiceMatrixNewRepository.getPolicyDetailes(Servicetype, ongoingService);
			if (CommonUtils.isNonEmptyArray(listOfService)) {
				for (Object[] obj : listOfService) {
					matrixNewEntity.setCurrentService((obj[0] != null) ? obj[0].toString() : null);
					matrixNewEntity.setOngoingService((obj[1] != null) ? obj[1].toString() : null);
					matrixNewEntity.setIsAllowed((obj[2] != null) ? obj[2].toString() : null);
				}
			}
			break;
		case QuotationConstants.MEMBER_ADDITION:
			listOfService = policyServiceMatrixNewRepository.getPolicyDetailes(Servicetype, ongoingService);
			if (CommonUtils.isNonEmptyArray(listOfService)) {
				for (Object[] obj : listOfService) {
					matrixNewEntity.setCurrentService((obj[0] != null) ? obj[0].toString() : null);
					matrixNewEntity.setOngoingService((obj[1] != null) ? obj[1].toString() : null);
					matrixNewEntity.setIsAllowed((obj[2] != null) ? obj[2].toString() : null);
				}
			}
			break;
		case QuotationConstants.FREELOOKCANCEL:
			listOfService = policyServiceMatrixNewRepository.getPolicyDetailes(Servicetype, ongoingService);
			if (CommonUtils.isNonEmptyArray(listOfService)) {
				for (Object[] obj : listOfService) {
					matrixNewEntity.setCurrentService((obj[0] != null) ? obj[0].toString() : null);
					matrixNewEntity.setOngoingService((obj[1] != null) ? obj[1].toString() : null);
					matrixNewEntity.setIsAllowed((obj[2] != null) ? obj[2].toString() : null);
				}
			}
			break;
		case QuotationConstants.SURRENDER:
			listOfService = policyServiceMatrixNewRepository.getPolicyDetailes(Servicetype, ongoingService);
			if (CommonUtils.isNonEmptyArray(listOfService)) {
				for (Object[] obj : listOfService) {
					matrixNewEntity.setCurrentService((obj[0] != null) ? obj[0].toString() : null);
					matrixNewEntity.setOngoingService((obj[1] != null) ? obj[1].toString() : null);
					matrixNewEntity.setIsAllowed((obj[2] != null) ? obj[2].toString() : null);
				}
			}
			break;
		case QuotationConstants.MEMBERTRASFERINOUT:
			listOfService = policyServiceMatrixNewRepository.getPolicyDetailes(Servicetype, ongoingService);
			if (CommonUtils.isNonEmptyArray(listOfService)) {
				for (Object[] obj : listOfService) {
					matrixNewEntity.setCurrentService((obj[0] != null) ? obj[0].toString() : null);
					matrixNewEntity.setOngoingService((obj[1] != null) ? obj[1].toString() : null);
					matrixNewEntity.setIsAllowed((obj[2] != null) ? obj[2].toString() : null);
				}
			}
			break;
		case QuotationConstants.CLAIM:
			listOfService = policyServiceMatrixNewRepository.getPolicyDetailes(Servicetype, ongoingService);
			if (CommonUtils.isNonEmptyArray(listOfService)) {
				for (Object[] obj : listOfService) {
					matrixNewEntity.setCurrentService((obj[0] != null) ? obj[0].toString() : null);
					matrixNewEntity.setOngoingService((obj[1] != null) ? obj[1].toString() : null);
					matrixNewEntity.setIsAllowed((obj[2] != null) ? obj[2].toString() : null);
				}
			}
			break;
		case QuotationConstants.REGULARADJUSTMENT:
			listOfService = policyServiceMatrixNewRepository.getPolicyDetailes(Servicetype, ongoingService);
			if (CommonUtils.isNonEmptyArray(listOfService)) {
				for (Object[] obj : listOfService) {
					matrixNewEntity.setCurrentService((obj[0] != null) ? obj[0].toString() : null);
					matrixNewEntity.setOngoingService((obj[1] != null) ? obj[1].toString() : null);
					matrixNewEntity.setIsAllowed((obj[2] != null) ? obj[2].toString() : null);
				}
			}
			break;
		case QuotationConstants.SUBSEQUENTADJUSTMENT:
			listOfService = policyServiceMatrixNewRepository.getPolicyDetailes(Servicetype, ongoingService);
			if (CommonUtils.isNonEmptyArray(listOfService)) {
				for (Object[] obj : listOfService) {
					matrixNewEntity.setCurrentService((obj[0] != null) ? obj[0].toString() : null);
					matrixNewEntity.setOngoingService((obj[1] != null) ? obj[1].toString() : null);
					matrixNewEntity.setIsAllowed((obj[2] != null) ? obj[2].toString() : null);
				}
			}
			break;
		}
		return matrixNewEntity;
	}

}
