package com.lic.epgs.quotation.service.impl;

import static com.lic.epgs.utils.CommonConstants.LOGEND;
import static com.lic.epgs.utils.CommonConstants.LOGSTART;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.quotation.dto.QuotationFinancialResponseDto;
import com.lic.epgs.quotation.dto.QuotationMemberAddressDto;
import com.lic.epgs.quotation.dto.QuotationMemberApiResponseDto;
import com.lic.epgs.quotation.dto.QuotationMemberAppointeeDto;
import com.lic.epgs.quotation.dto.QuotationMemberBankDetailDto;
import com.lic.epgs.quotation.dto.QuotationMemberBulkResponseDto;
import com.lic.epgs.quotation.dto.QuotationMemberDto;
import com.lic.epgs.quotation.dto.QuotationMemberErrorDto;
import com.lic.epgs.quotation.dto.QuotationMemberNomineeDto;
import com.lic.epgs.quotation.entity.QuotationEntity;
import com.lic.epgs.quotation.entity.QuotationMemberAddressTempEntity;
import com.lic.epgs.quotation.entity.QuotationMemberAppointeeTempEntity;
import com.lic.epgs.quotation.entity.QuotationMemberBankDetailTempEntity;
import com.lic.epgs.quotation.entity.QuotationMemberBatchEntity;
import com.lic.epgs.quotation.entity.QuotationMemberEntity;
import com.lic.epgs.quotation.entity.QuotationMemberErrorEntity;
import com.lic.epgs.quotation.entity.QuotationMemberNomineeTempEntity;
import com.lic.epgs.quotation.entity.QuotationMemberTempEntity;
import com.lic.epgs.quotation.entity.QuotationTempEntity;
import com.lic.epgs.quotation.repository.QuotationMemberAddressTempRepository;
import com.lic.epgs.quotation.repository.QuotationMemberBatchRepository;
import com.lic.epgs.quotation.repository.QuotationMemberErrorRepository;
import com.lic.epgs.quotation.repository.QuotationMemberNomineeTempRepo;
import com.lic.epgs.quotation.repository.QuotationMemberRepo;
import com.lic.epgs.quotation.repository.QuotationMemberTempRepo;
import com.lic.epgs.quotation.repository.QuotationRepository;
import com.lic.epgs.quotation.repository.QuotationTempRepository;
import com.lic.epgs.quotation.service.QuotationMemberService;
import com.lic.epgs.utils.CommonConstants;

@Service
public class QuotationMemberServiceImpl implements QuotationMemberService {

	@Autowired
	private QuotationMemberTempRepo quotationMemberTempRepo;

	@Autowired
	private QuotationMemberRepo quotationMemberRepo;

	@Autowired
	private QuotationTempRepository quotationTempRepository;

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CommonService commonSequenceService;
	
	@Autowired
	private QuotationMemberAddressTempRepository quotationMemberAddressTempRepository;
	
	@Autowired 
	private QuotationMemberNomineeTempRepo quotationMemberNomineeTempRepo;
	
	
	@Autowired
	private QuotationMemberBatchRepository quotationMemberBatchRepository;
	
	@Autowired
	private QuotationMemberErrorRepository quotationMemberErrorRepository;
	
	
	@Autowired
	private EntityManager entityManager;

	protected final Logger logger = LogManager.getLogger(getClass());

	public synchronized String getLicSeq() {
		return commonSequenceService.getSequence(CommonConstants.LIC_SEQ);
	}
	
	
	

	@Override
	public QuotationMemberApiResponseDto saveQuotationMember(QuotationMemberDto quotationMemberDto) {
		logger.info("{}", LOGSTART);

		QuotationMemberApiResponseDto commonDto = new QuotationMemberApiResponseDto();
		try {

			if(quotationMemberDto.getMemberId() != null) {
				
				if(quotationMemberDto.getQuotationId() != null) {
					
					QuotationMemberTempEntity existingMember = quotationMemberTempRepo.findByMemberIdAndQuotationId(quotationMemberDto.getMemberId(),
															   quotationMemberDto.getQuotationId());
					
					if(existingMember !=null) {
						
						logger.info("QuotationServiceImpl:saveQuotationMember:--Existing member edit ---Start");
						
						
						
						if(quotationMemberDto.getComponentLabel().equalsIgnoreCase("Personal data")) {
							
							existingMember = this.setQuotationMemberPersonalDetails(existingMember, quotationMemberDto);
						}
						
						
						
						
						if(quotationMemberDto.getComponentLabel().equalsIgnoreCase("Financial Detail")) {
							
							existingMember = this.setQuotationMemberFinancialDetails(existingMember, quotationMemberDto);
						}
						
						
						
						
						if(quotationMemberDto.getComponentLabel().equalsIgnoreCase("Address Detail")) {
							
							existingMember = this.setQuotationMemberAddressDetails(existingMember, quotationMemberDto);
						}
						
						
						
						
						
						if(quotationMemberDto.getComponentLabel().equalsIgnoreCase("Bank Detail")) {
							
							existingMember = this.setQuotationMemberBankDetails(existingMember, quotationMemberDto);
							
						}
						
						
						
						if(quotationMemberDto.getComponentLabel().equalsIgnoreCase("Nominee/Claimant Details")) {
							
							existingMember = this.setQuotationMemberNomineeDetails(existingMember, quotationMemberDto);
						}
						
						
						
						
						if(quotationMemberDto.getComponentLabel().equalsIgnoreCase("Quotation Appointee")) {
							
							existingMember = this.setQuotationMemberAppointeeDetails(existingMember, quotationMemberDto);
						}
						

						existingMember = quotationMemberTempRepo.save(existingMember);
						
						
						
//						Set<QuotationMemberTempEntity> memberList = new HashSet<>();
//						for(QuotationMemberTempEntity memberTemp : quotationTemp.getMembers()) {
//							if(memberTemp.getMemberId() == existingMember.getMemberId()) {
//								memberList.add(existingMember);
//							}else {
//								memberList.add(memberTemp);
//							}
//						}
//						
	//
						
						QuotationTempEntity quotationTemp = quotationTempRepository.findByQuotationIdAndIsActiveTrue(quotationMemberDto.getQuotationId());
						
						
						
						if(quotationTemp.getQuotationType().equalsIgnoreCase("DC")) {
							
							quotationTemp = this.calculateMembersContributions(quotationTemp, quotationMemberDto);
							
							quotationTempRepository.save(quotationTemp);
						}
						

//						for(QuotationMemberTempEntity member : quotationTemp.getMembers()) {
//							if(member.getMemberId() == quotationMemberDto.getMemberId()) {
//								existingMember = member;
//							}
//						}
//						
						
						
						QuotationMemberDto customQuotationDto = modelMapper.map(existingMember, QuotationMemberDto.class);
						
						if(!customQuotationDto.getQuotationMemberNominees().isEmpty()) {
							List<QuotationMemberNomineeDto> nomineeList = customQuotationDto.getQuotationMemberNominees();
							nomineeList.removeIf(nominee->nominee.getIsActive() == Boolean.FALSE);
							nomineeList.sort(Comparator.comparing(QuotationMemberNomineeDto::getNomineeId));
							customQuotationDto.setQuotationMemberNominees(nomineeList);
						}
						
						
						commonDto.setQuotationMemberDto(customQuotationDto);
						commonDto.setTransactionStatus(CommonConstants.SUCCESS);
						commonDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);	
						
						logger.info("QuotationServiceImpl:saveQuotationMember:--Existing member edit ---End");
						
					}else {
						
						commonDto.setTransactionStatus(CommonConstants.FAIL);
						commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND+" against Member Id "+quotationMemberDto.getMemberId());
						
					}
					
				}else {
					commonDto.setTransactionStatus(CommonConstants.FAIL);
					commonDto.setTransactionMessage("Quotation Id is null!");
				}
				
				
			}else {
				
				logger.info("QuotationServiceImpl:saveQuotationMember:--New member ---Start");
				QuotationTempEntity quotationTemp = quotationTempRepository.findByQuotationIdAndIsActiveTrue(quotationMemberDto.getQuotationId());
				QuotationMemberTempEntity newMember = modelMapper.map(quotationMemberDto,QuotationMemberTempEntity.class);
				newMember.setQuotationId(quotationTemp.getQuotationId());

				Set<QuotationMemberTempEntity> quoteMembers = quotationTemp.getMembers();
				quoteMembers.add(newMember);
				quotationTemp.setMembers(quoteMembers);
	
				quotationTemp = quotationTempRepository.save(quotationTemp);
				logger.info("QuotationServiceImpl:saveQuotationMember:--Saved successfully");
				
				
				
				TreeSet<Long> memberIdList = new TreeSet<>(Collections.reverseOrder());
				for(QuotationMemberTempEntity member : quotationTemp.getMembers()) { memberIdList.add(member.getMemberId());}
				for(QuotationMemberTempEntity member : quotationTemp.getMembers()) {
					if(member.getMemberId() == memberIdList.first())
						newMember = member;
				}

				commonDto.setQuotationMemberDto(modelMapper.map(newMember, QuotationMemberDto.class));
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);	
				
				logger.info("QuotationServiceImpl:saveQuotationMember:--New member ---End");
			}
			
			
			
			
			
			
			
//			if(quotationMemberDto.getMemberId() !=null && !quotationMemberDto.getMemberId().equals("") && quotationMemberDto.getQuotationId() != null) {
//				
//					QuotationTempEntity quotationTemp = quotationTempRepository.findByQuotationIdAndIsActiveTrue(quotationMemberDto.getQuotationId());
//					
//					if(quotationTemp != null) {
//						
//						QuotationMemberTempEntity existingMember = quotationMemberTempRepo.findByMemberIdAndQuotationId(quotationMemberDto.getMemberId(), quotationMemberDto.getQuotationId());
//						
//						if(existingMember !=null) {
//							
//							if(quotationMemberDto.getComponentLabel().equalsIgnoreCase("Personal data")) {
//								
//								existingMember.setMemberShipId(quotationMemberDto.getMemberShipId());
//								existingMember.setFirstName(quotationMemberDto.getFirstName());
//								existingMember.setMiddleName(quotationMemberDto.getMiddleName());
//								existingMember.setLastName(quotationMemberDto.getLastName());
//								existingMember.setDateOfBirth(quotationMemberDto.getDateOfBirth());
//								existingMember.setDateOfJoining(quotationMemberDto.getDateOfJoining());
//								existingMember.setGender(quotationMemberDto.getGender());
//								existingMember.setFatherName(quotationMemberDto.getFatherName());
//								existingMember.setPan(quotationMemberDto.getPan());
//								existingMember.setAadharNumber(quotationMemberDto.getAadharNumber());
//								existingMember.setMaritalStatus(quotationMemberDto.getMaritalStatus());
//								existingMember.setSpouseName(quotationMemberDto.getSpouseName());
//								existingMember.setDesignation(quotationMemberDto.getDesignation());	
//								existingMember.setCommunicationPreference(quotationMemberDto.getCommunicationPreference());
//								existingMember.setLanguagePreference(existingMember.getLanguagePreference());
//								existingMember.setEmailId(existingMember.getEmailId());
//								existingMember.setPhone(existingMember.getPhone());
//								
//							}
//							
//							
//							
//							if(quotationMemberDto.getComponentLabel().equalsIgnoreCase("Financial Detail")) {
//								
//								existingMember.setCategory(quotationMemberDto.getCategory());
//								existingMember.setFrequency(quotationMemberDto.getFrequency());
//								existingMember.setEmployerContribution(quotationMemberDto.getEmployerContribution());
//								existingMember.setEmployeeContribution(quotationMemberDto.getEmployeeContribution());
//								existingMember.setVoluntaryContribution(quotationMemberDto.getVoluntaryContribution());;
//								existingMember.setTotalContribution(quotationMemberDto.getTotalContribution());
//								
//							}
//							
//							
//							if(quotationMemberDto.getComponentLabel().equalsIgnoreCase("Address Detail")) {
//								
//								if(!quotationMemberDto.getQuotationMemberAddresses().isEmpty() && quotationMemberDto.getQuotationMemberAddresses() !=null) {
//									List<QuotationMemberAddressTempEntity> addressList = new ArrayList<>();
//									
//									for(QuotationMemberAddressDto addressDto : quotationMemberDto.getQuotationMemberAddresses()) {
//										
//										QuotationMemberAddressTempEntity addressEntity = modelMapper.map(addressDto, QuotationMemberAddressTempEntity.class);
//										addressList.add(addressEntity);
//
//										}
//
//									
//									existingMember.setQuotationMemberAddresses(addressList);						
//
//								}
//								
//							}
//							
//							
//							
//							
//							
//							if(quotationMemberDto.getComponentLabel().equalsIgnoreCase("Bank Detail")) {
//								
//								if(!quotationMemberDto.getQuotationMemberBankDetails().isEmpty() && quotationMemberDto.getQuotationMemberBankDetails() !=null) {
//									List<QuotationMemberBankDetailTempEntity> bankList = new ArrayList<>();
//									
//									for(QuotationMemberBankDetailDto bankDto : quotationMemberDto.getQuotationMemberBankDetails()) {
//										
//										QuotationMemberBankDetailTempEntity addressEntity = modelMapper.map(bankDto, QuotationMemberBankDetailTempEntity.class);
//										bankList.add(addressEntity);
//
//										}
//
//									
//									existingMember.setQuotationMemberBankDetails(bankList);
//								}
//								
//							}
//							
//							
//							
//							if(quotationMemberDto.getComponentLabel().equalsIgnoreCase("Nominee/Claimant Details")) {
//								
//								if(!quotationMemberDto.getQuotationMemberNominees().isEmpty() && quotationMemberDto.getQuotationMemberNominees() !=null) {
//									List<QuotationMemberNomineeTempEntity> nomineeList = new ArrayList<>();
//									
//									for(QuotationMemberNomineeDto nomineeDto : quotationMemberDto.getQuotationMemberNominees()) {
//										
//										QuotationMemberNomineeTempEntity nomineeEntity = modelMapper.map(nomineeDto, QuotationMemberNomineeTempEntity.class);
//										nomineeList.add(nomineeEntity);
//
//										}
//
//									
//									existingMember.setQuotationMemberNominees(nomineeList);
//								}
//								
//							}
//							
//							
//							
//							
//							if(quotationMemberDto.getComponentLabel().equalsIgnoreCase("Quotation Appointee")) {
//								
//								if(!quotationMemberDto.getQuotationMemberAppointees().isEmpty() && quotationMemberDto.getQuotationMemberAppointees() !=null) {
//									List<QuotationMemberAppointeeTempEntity> appointeeList = new ArrayList<>();
//									
//									for(QuotationMemberAppointeeDto nomineeDto : quotationMemberDto.getQuotationMemberAppointees()) {
//										
//										QuotationMemberAppointeeTempEntity nomineeEntity = modelMapper.map(nomineeDto, QuotationMemberAppointeeTempEntity.class);
//										appointeeList.add(nomineeEntity);
//
//										}
//
//									
//									existingMember.setQuotationMemberAppointees(appointeeList);
//								}
//								
//							}
//
//
//							
//							existingMember = quotationMemberTempRepo.save(existingMember);	
//							
//							
//							commonDto.setQuotationMemberDto(modelMapper.map(existingMember, QuotationMemberDto.class));
//							commonDto.setTransactionStatus(CommonConstants.SUCCESS);
//							commonDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
//						}
//						
//					}
//				
//				
//			}else {
//				
//				Set<QuotationMemberTempEntity> membersList = new HashSet<>();
//				
//				if(quotationMemberDto.getQuotationId() != null) {
//					Long memberId = null;
//					QuotationMemberTempEntity newOrExistMember = null;
//					QuotationTempEntity quotationTemp = quotationTempRepository.findByQuotationIdAndIsActiveTrue(quotationMemberDto.getQuotationId());
//					
//					if(quotationTemp != null) {
//						newOrExistMember = modelMapper.map(quotationMemberDto,QuotationMemberTempEntity.class);
//						newOrExistMember.setQuotationId(quotationTemp.getQuotationId());
//						Set<QuotationMemberTempEntity> quoteMembers = quotationTemp.getMembers();
//						
//						if(quotationMemberDto.getMemberId()!=null) {
//
//							for(QuotationMemberTempEntity member : quotationTemp.getMembers()) {
//								if(member.getMemberId().equals(quotationMemberDto.getMemberId())) {
//									memberId = member.getMemberId();
//									membersList.add(newOrExistMember);
//								}else {
//									membersList.add(member);
//								}
//						
//							}
//						
//							quotationTemp.setMembers(membersList);
//							
//						}else {
//							
//							quoteMembers.add(newOrExistMember);
//							quotationTemp.setMembers(quoteMembers);
//						}
//						
//						
//						quotationTemp = quotationTempRepository.save(quotationTemp);
//						
//						
//						if(quotationTemp.getQuotationType().equalsIgnoreCase("DC")) {
//							
////							Set<QuotationMemberTempEntity> quoMbrTemp  = getBetweenQuotationTempAndQuotationTempMembers(quotationMemberDto);
//							
//							QuotationTempEntity temp = getBetweenQuotationTempAndQuotationTempMembers(quotationMemberDto.getQuotationId());
//							
//							Set<QuotationMemberTempEntity> quoMbrTemp =(temp != null)?temp.getMembers():new HashSet<>();
//							
//							if(!quoMbrTemp.isEmpty() && quotationMemberDto.getEmployerContribution()!=null) {
//								
//								BigDecimal totalEmployeeContribution =BigDecimal.ZERO,totalEmployercontribution =BigDecimal.ZERO,totalOfTotalContribution =BigDecimal.ZERO,totalVoluntaryContribution =BigDecimal.ZERO;
//								for(QuotationMemberTempEntity itr :quoMbrTemp){
//									
//									totalEmployeeContribution = totalEmployeeContribution.add((itr.getEmployeeContribution()!=null)?itr.getEmployeeContribution():BigDecimal.ZERO);
//									totalEmployercontribution = totalEmployercontribution.add((itr.getEmployerContribution()!=null)?itr.getEmployerContribution():BigDecimal.ZERO);
//									totalVoluntaryContribution = totalVoluntaryContribution.add((itr.getVoluntaryContribution()!=null)?itr.getVoluntaryContribution():BigDecimal.ZERO);
//									totalOfTotalContribution = totalOfTotalContribution.add((itr.getTotalContribution()!=null)?itr.getTotalContribution():BigDecimal.ZERO);
//									
//								}
//								
//								quotationTemp.setEmployeeContribution(totalEmployeeContribution);
//								
//								quotationTemp.setEmployerContribution(totalEmployercontribution);
//								
//								quotationTemp.setVoluntaryContribution(totalVoluntaryContribution);
//								
//								quotationTemp.setTotalContribution(totalOfTotalContribution);
//								
//								quotationTemp.setTotalMember(quoMbrTemp.size());
//							}
//							
//							
//							
//						}else {
//							
//							
//							
//							Set<QuotationLiabilityTempEntity> liabilityList = quotationTemp.getLiabilities();
//							BigDecimal totalContribution =BigDecimal.ZERO;
//							for(QuotationLiabilityTempEntity liability :liabilityList) {
//								
//								if(liability.getExternalaluation() != null && liability.getExternalaluation() != BigDecimal.ZERO ) {
//									totalContribution = totalContribution.add(liability.getExternalaluation());
//								}else if(liability.getInternalValuation() != null && liability.getInternalValuation() != BigDecimal.ZERO) {
//									totalContribution = totalContribution.add(liability.getInternalValuation());
//								}
//							}
//							quotationTemp.setTotalContribution(totalContribution);
//							
//						}
//						
//						quotationTemp = quotationTempRepository.save(quotationTemp);
//						
//						
//						
//						
//						
////						if(memberId != null) {
////							final Long temp = memberId;
////							List<QuotationMemberTempEntity> quoMbrTempList = quotationMemberTempRepo.findAllByQuotationIdAndIsActiveTrueOrderByMemberIdDesc(quotationTemp.getQuotationId());
////							quoMbrTempList.removeIf(deposit -> deposit.getMemberId() != temp);
////							newOrExistMember.setMemberId(memberId);
////							commonDto.setMemberId(quoMbrTempList.get(0).getMemberId());
////							commonDto.setQuotationMemberDto(modelMapper.map(quoMbrTempList.get(0), QuotationMemberDto.class));
////						}else {
//							
//							List<QuotationMemberTempEntity> quoMbrTempList = quotationMemberTempRepo.findAllByQuotationIdAndIsActiveTrueOrderByMemberIdDesc(quotationTemp.getQuotationId());
//							final Long memberIdFinal = memberId;
//							
//							if(memberIdFinal != null)
//								quoMbrTempList.removeIf(deposit -> deposit.getMemberId() != memberIdFinal);
//							
//							if(!quoMbrTempList.isEmpty() && quoMbrTempList !=null)
//								commonDto.setMemberId(quoMbrTempList.get(0).getMemberId());
//							
//							commonDto.setQuotationMemberDto(modelMapper.map(quoMbrTempList.get(0), QuotationMemberDto.class));
//							
////						}
//						commonDto.setTransactionStatus(CommonConstants.SUCCESS);
//						commonDto.setTransactionMessage(CommonConstants.SAVEMESSAGE);
//					}else {
//						commonDto.setTransactionStatus(CommonConstants.FAIL);
//						commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND+" against Quotation Id "+quotationMemberDto.getQuotationId());
//					}
//				}else {
//					commonDto.setTransactionStatus(CommonConstants.FAIL);
//					commonDto.setTransactionMessage("Quotation Id is null!");
//				}
//				
//			}
			
			
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- QuotationMemberServiceImpl-- saveQuotationMember --", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("{}", LOGEND);
		return commonDto;
	}
	
	
	
	
	
	
	private QuotationMemberTempEntity setQuotationMemberPersonalDetails(QuotationMemberTempEntity existingMember , QuotationMemberDto quotationMemberDto) {
		logger.info("QuotationServiceImpl:saveQuotationMember:--Personal data---Start");
		
		existingMember.setMemberShipId(quotationMemberDto.getMemberShipId());
		existingMember.setFirstName(quotationMemberDto.getFirstName());
		existingMember.setMiddleName(quotationMemberDto.getMiddleName());
		existingMember.setLastName(quotationMemberDto.getLastName());
		existingMember.setDateOfBirth(quotationMemberDto.getDateOfBirth());
		existingMember.setDateOfJoining(quotationMemberDto.getDateOfJoining());
		existingMember.setGender(quotationMemberDto.getGender());
		existingMember.setFatherName(quotationMemberDto.getFatherName());
		existingMember.setPan(quotationMemberDto.getPan());
		existingMember.setAadharNumber(quotationMemberDto.getAadharNumber());
		existingMember.setMaritalStatus(quotationMemberDto.getMaritalStatus());
		existingMember.setSpouseName(quotationMemberDto.getSpouseName());
		existingMember.setDesignation(quotationMemberDto.getDesignation());	
		existingMember.setCommunicationPreference(quotationMemberDto.getCommunicationPreference());
		existingMember.setLanguagePreference(quotationMemberDto.getLanguagePreference());
		existingMember.setEmailId(quotationMemberDto.getEmailId());
		existingMember.setPhone(quotationMemberDto.getPhone());
		
		logger.info("QuotationServiceImpl:saveQuotationMember:--Personal data---End");
		return existingMember;
	}
	
	
	
	private QuotationMemberTempEntity setQuotationMemberFinancialDetails(QuotationMemberTempEntity existingMember , QuotationMemberDto quotationMemberDto) {
		logger.info("QuotationServiceImpl:saveQuotationMember:--Financial Detail---Start");
		
		QuotationTempEntity quotationTemp = quotationTempRepository.findByQuotationIdAndIsActiveTrue(existingMember.getQuotationId());
		
		
		if(quotationTemp.getQuotationType().equalsIgnoreCase("DC")) {
			
			if (quotationTemp.getContributionType().equalsIgnoreCase("Non Contributory")) {

				existingMember.setEmployerContribution(quotationMemberDto.getEmployerContribution());
				existingMember.setEmployeeContribution(BigDecimal.ZERO);
				existingMember.setVoluntaryContribution(BigDecimal.ZERO);
				existingMember.setTotalContribution(quotationMemberDto.getEmployerContribution());

			} else {
				
				existingMember.setEmployerContribution(quotationMemberDto.getEmployerContribution());
				existingMember.setEmployeeContribution(quotationMemberDto.getEmployeeContribution());
				existingMember.setVoluntaryContribution(quotationMemberDto.getVoluntaryContribution());
				existingMember.setTotalContribution(quotationMemberDto.getTotalContribution());
			}
			
		}else {
			
			existingMember.setEmployerContribution(quotationMemberDto.getEmployerContribution());
			existingMember.setEmployeeContribution(quotationMemberDto.getEmployeeContribution());
			existingMember.setVoluntaryContribution(quotationMemberDto.getVoluntaryContribution());
			existingMember.setTotalContribution(quotationMemberDto.getTotalContribution());
		}
		

		existingMember.setCategory(quotationMemberDto.getCategory());
		existingMember.setFrequency(quotationMemberDto.getFrequency());

		logger.info("QuotationServiceImpl:saveQuotationMember:--Financial Detail---End");
		return existingMember;
	}
	
	
	
	private QuotationMemberTempEntity setQuotationMemberAddressDetails(QuotationMemberTempEntity existingMember , QuotationMemberDto quotationMemberDto) {
		logger.info("QuotationServiceImpl:saveQuotationMember:--Address Detail---Start");
		
		if(!quotationMemberDto.getQuotationMemberAddresses().isEmpty() && quotationMemberDto.getQuotationMemberAddresses() !=null) {
			Set<QuotationMemberAddressTempEntity> addressList = new HashSet<>();
			
			for(QuotationMemberAddressDto addressDto : quotationMemberDto.getQuotationMemberAddresses()) {
				
				QuotationMemberAddressTempEntity addressEntity = modelMapper.map(addressDto, QuotationMemberAddressTempEntity.class);
				addressList.add(addressEntity);

				}

			
			existingMember.setQuotationMemberAddresses(addressList);	
			
			
//			for(QuotationMemberAddressTempEntity addressTemp : existingMember.getQuotationMemberAddresses()) {
//				
//				for(QuotationMemberAddressDto addressDto : quotationMemberDto.getQuotationMemberAddresses()) {
//					
//					if(addressDto.getAddressId() == null) {
//						addressList.add(modelMapper.map(addressDto, QuotationMemberAddressTempEntity.class));
//					}else if(addressDto.getAddressId() == addressTemp.getAddressId()) {
//						
//					}
//					
//					QuotationMemberAddressTempEntity addressEntity = modelMapper.map(addressDto, QuotationMemberAddressTempEntity.class);
//					addressList.add(addressEntity);
//
//					}
//				
//			}

		}

		logger.info("QuotationServiceImpl:saveQuotationMember:--Address Detail---End");
		return existingMember;
	}
	
	
	
	
	private QuotationMemberTempEntity setQuotationMemberBankDetails(QuotationMemberTempEntity existingMember , QuotationMemberDto quotationMemberDto) {
		if(!quotationMemberDto.getQuotationMemberBankDetails().isEmpty() && quotationMemberDto.getQuotationMemberBankDetails() !=null) {
			
			logger.info("QuotationServiceImpl:saveQuotationMember:--Bank Detail---Start");
			Set<QuotationMemberBankDetailTempEntity> bankList = new HashSet<>();
			
			for(QuotationMemberBankDetailDto bankDto : quotationMemberDto.getQuotationMemberBankDetails()) {
				
				QuotationMemberBankDetailTempEntity addressEntity = modelMapper.map(bankDto, QuotationMemberBankDetailTempEntity.class);
				bankList.add(addressEntity);

				}

			
			existingMember.setQuotationMemberBankDetails(bankList);
			
			logger.info("QuotationServiceImpl:saveQuotationMember:--Bank Detail---End");
		}
		return existingMember;
	}
	
	
	
	
	private QuotationMemberTempEntity setQuotationMemberNomineeDetails(QuotationMemberTempEntity existingMember , QuotationMemberDto quotationMemberDto) {
		
		if(!quotationMemberDto.getQuotationMemberNominees().isEmpty() && quotationMemberDto.getQuotationMemberNominees() !=null) {
			
			logger.info("QuotationServiceImpl:saveQuotationMember:--Nominee/Claimant Details---Start");
			
			Set<QuotationMemberNomineeTempEntity> nomineeList = new HashSet<>();
			
			if(quotationMemberDto.getQuotationMemberNominees().size() >= existingMember.getQuotationMemberNominees().size()) {
				
				for(QuotationMemberNomineeDto nomineeDto : quotationMemberDto.getQuotationMemberNominees()) {
				
				QuotationMemberNomineeTempEntity nomineeEntity = modelMapper.map(nomineeDto, QuotationMemberNomineeTempEntity.class);
				nomineeEntity.setIsActive(Boolean.TRUE);
				nomineeList.add(nomineeEntity);

				}
				
			}else {
				Set<QuotationMemberNomineeTempEntity> existingList = existingMember.getQuotationMemberNominees();
				
				List<QuotationMemberNomineeTempEntity> newList = mapList(quotationMemberDto.getQuotationMemberNominees(), QuotationMemberNomineeTempEntity.class);

				for (QuotationMemberNomineeTempEntity nominee : newList) {
					
					existingList.removeIf(itr-> itr.getNomineeId() == nominee.getNomineeId());
				}
				
				
				existingList.forEach(itr->itr.setIsActive(Boolean.FALSE));
				
				
				for(QuotationMemberNomineeDto nomineeDto : quotationMemberDto.getQuotationMemberNominees()) {
					
					QuotationMemberNomineeTempEntity nomineeEntity = modelMapper.map(nomineeDto, QuotationMemberNomineeTempEntity.class);
					nomineeList.add(nomineeEntity);

					}
				
				nomineeList.addAll(existingList);
				
				
			}
			
			
			
			
			
//			List<Long> nomineeIdList = new ArrayList<>();
//			
//			for(QuotationMemberNomineeDto nomineeDto : quotationMemberDto.getQuotationMemberNominees()) {
//				for(QuotationMemberNomineeTempEntity nomineeTemp : existingMember.getQuotationMemberNominees()) {
//					
//					if(nomineeTemp.getNomineeId() == nomineeDto.getNomineeId()) {
//						
//						QuotationMemberNomineeTempEntity nomineeEntity = modelMapper.map(nomineeDto, QuotationMemberNomineeTempEntity.class);
//						nomineeList.add(nomineeEntity);
//						nomineeIdList.add(nomineeTemp.getNomineeId());
//
//						break;
//						
//					}else if(nomineeDto.getNomineeId() ==null) {
//						
//						QuotationMemberNomineeTempEntity nomineeEntity = modelMapper.map(nomineeDto, QuotationMemberNomineeTempEntity.class);
//						nomineeList.add(nomineeEntity);
//					}else {
//						
//						for(Long nomineeId : nomineeIdList) {
//							if(nomineeId != nomineeDto.getNomineeId()) {
//								nomineeTemp.setIsActive(Boolean.FALSE);
//								nomineeList.add(nomineeTemp);
//							}else {
//								
//							}
//						}
//						
//					}
//					
////					QuotationMemberNomineeTempEntity nomineeEntity = modelMapper.map(nomineeDto, QuotationMemberNomineeTempEntity.class);
////					nomineeList.add(nomineeEntity);
//
//					}
//			}
//			
			
			
			
			
			
//			for(QuotationMemberNomineeDto nomineeDto : quotationMemberDto.getQuotationMemberNominees()) {
//				
//				QuotationMemberNomineeTempEntity nomineeEntity = modelMapper.map(nomineeDto, QuotationMemberNomineeTempEntity.class);
//				nomineeList.add(nomineeEntity);
//
//				}

			
			existingMember.setQuotationMemberNominees(nomineeList);
			
			
			logger.info("QuotationServiceImpl:saveQuotationMember:--Nominee/Claimant Details---End");
		}
		
		return existingMember;
	}
	
	
	
	
	private QuotationMemberTempEntity setQuotationMemberAppointeeDetails(QuotationMemberTempEntity existingMember , QuotationMemberDto quotationMemberDto) {
		if(!quotationMemberDto.getQuotationMemberAppointees().isEmpty() && quotationMemberDto.getQuotationMemberAppointees() !=null) {
			
			logger.info("QuotationServiceImpl:saveQuotationMember:--Quotation Appointee---Start");
			
			Set<QuotationMemberAppointeeTempEntity> appointeeList = new HashSet<>();
			
			for(QuotationMemberAppointeeDto nomineeDto : quotationMemberDto.getQuotationMemberAppointees()) {
				
				QuotationMemberAppointeeTempEntity nomineeEntity = modelMapper.map(nomineeDto, QuotationMemberAppointeeTempEntity.class);
				appointeeList.add(nomineeEntity);

				}

			
			existingMember.setQuotationMemberAppointees(appointeeList);
			
			logger.info("QuotationServiceImpl:saveQuotationMember:--Quotation Appointee---End");
		}
		return existingMember;
	}
	
	
	
	private QuotationTempEntity calculateMembersContributions(QuotationTempEntity quotationTemp, QuotationMemberDto quotationMemberDto) {
		
		logger.info("QuotationServiceImpl:saveQuotationMember:--contribution Calculation---start");
		
		Set<QuotationMemberTempEntity> quoMbrTemp  = quotationTemp.getMembers();
		List<QuotationMemberTempEntity> replicaMembers = new ArrayList<>();
		quoMbrTemp.forEach(member-> replicaMembers.add(member));
		replicaMembers.removeIf(member-> member.getIsActive() == Boolean.FALSE);
		
		
		if(!replicaMembers.isEmpty() && quotationMemberDto.getEmployerContribution()!=null) {
			
			BigDecimal totalEmployeeContribution =BigDecimal.ZERO,totalEmployercontribution =BigDecimal.ZERO,totalOfTotalContribution =BigDecimal.ZERO,totalVoluntaryContribution =BigDecimal.ZERO;
			for(QuotationMemberTempEntity itr :replicaMembers){
				
				totalEmployeeContribution = totalEmployeeContribution.add((itr.getEmployeeContribution()!=null)?itr.getEmployeeContribution():BigDecimal.ZERO);
				totalEmployercontribution = totalEmployercontribution.add((itr.getEmployerContribution()!=null)?itr.getEmployerContribution():BigDecimal.ZERO);
				totalVoluntaryContribution = totalVoluntaryContribution.add((itr.getVoluntaryContribution()!=null)?itr.getVoluntaryContribution():BigDecimal.ZERO);
				totalOfTotalContribution = totalOfTotalContribution.add((itr.getTotalContribution()!=null)?itr.getTotalContribution():BigDecimal.ZERO);
				
			}
			
			quotationTemp.setEmployeeContribution(totalEmployeeContribution);
			
			quotationTemp.setEmployerContribution(totalEmployercontribution);
			
			quotationTemp.setVoluntaryContribution(totalVoluntaryContribution);
			
			quotationTemp.setTotalContribution(totalOfTotalContribution);
			
			quotationTemp.setTotalMember(replicaMembers.size());
			
		}
	
		logger.info("QuotationServiceImpl:saveQuotationMember:--contribution Calculation---End");
		
		return quotationTemp;
	}
	
	
	
//	private Set<QuotationMemberTempEntity> getBetweenQuotationTempAndQuotationTempMembers(QuotationMemberDto dto) {
//		Set<QuotationMemberTempEntity> memberTemp =null;
//		try {
//			
//			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//
//			CriteriaQuery<QuotationTempEntity> searchQuery = criteriaBuilder.createQuery(QuotationTempEntity.class);
//			Root<QuotationTempEntity> root = searchQuery.from(QuotationTempEntity.class);
//			Join<QuotationTempEntity, QuotationMemberTempEntity> join = root.join("members");
//			join.on(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
//			join.on(criteriaBuilder.equal(root.get("quotationId"),dto.getQuotationId()));
//			List<Predicate> predicates = new ArrayList<>();
//			predicates.add(criteriaBuilder.equal(root.get("quotationId"), dto.getQuotationId()));
//			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
//			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));
//
//			QuotationTempEntity result = entityManager.createQuery(searchQuery).getSingleResult();
//			if(result != null) {
//				memberTemp =result.getMembers();
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//			memberTemp = new HashSet<>();
//		}
//		return memberTemp;
//	}

	@Override
	public QuotationMemberApiResponseDto getQuotationMember(Long quotationMemberId, Integer quotationId) {

		QuotationMemberApiResponseDto commonDto = new QuotationMemberApiResponseDto();
		logger.info("quotationMemberId::{}quotationId::{}::{}", quotationMemberId, quotationId, LOGSTART);
		try {
			QuotationMemberTempEntity quotationMemberTemp = quotationMemberTempRepo.findByMemberIdAndQuotationId(quotationMemberId, quotationId);
			if (quotationMemberTemp != null) {
				commonDto.setMemberId(quotationMemberTemp.getMemberId());
				
				QuotationMemberDto quotationMemberDto = modelMapper.map(quotationMemberTemp, QuotationMemberDto.class);
				
				if(!quotationMemberDto.getQuotationMemberNominees().isEmpty()) {
					List<QuotationMemberNomineeDto> nomineeList = quotationMemberDto.getQuotationMemberNominees();
					nomineeList.removeIf(nominee->nominee.getIsActive() == Boolean.FALSE);
					nomineeList.sort(Comparator.comparing(QuotationMemberNomineeDto::getNomineeId));
					quotationMemberDto.setQuotationMemberNominees(nomineeList);
				}
				
				commonDto.setQuotationMemberDto(quotationMemberDto);
				
				
				commonDto.setTransactionStatus(CommonConstants.STATUS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
			} else {

				QuotationMemberEntity quoMember = quotationMemberRepo.findByMemberIdAndQuotationId(quotationMemberId,
						quotationId);
				if (quoMember != null) {
					commonDto.setMemberId(quoMember.getMemberId());
					
					QuotationMemberDto quotationMemberDto = modelMapper.map(quoMember, QuotationMemberDto.class);
					
					if(!quotationMemberDto.getQuotationMemberNominees().isEmpty()) {
						List<QuotationMemberNomineeDto> nomineeList = quotationMemberDto.getQuotationMemberNominees();
						nomineeList.removeIf(nominee->nominee.getIsActive() == Boolean.FALSE);
						nomineeList.sort(Comparator.comparing(QuotationMemberNomineeDto::getNomineeId));
						quotationMemberDto.setQuotationMemberNominees(nomineeList);
					}
					
					commonDto.setQuotationMemberDto(quotationMemberDto);
					commonDto.setTransactionStatus(CommonConstants.STATUS);
					commonDto.setTransactionMessage(CommonConstants.FETCH);
				} else {

					logger.info("Quotation member not found for the given QuotationMemberId: {} and QuotationId: {}",
							quotationMemberId, quotationId);
					commonDto.setTransactionMessage(CommonConstants.FAIL);
					commonDto.setTransactionStatus("Quotation member not found for the given QuotationMemberId: "
							+ quotationMemberId + " and QuotationId: " + quotationId);
				}
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception-- QuotationMemberServiceImpl-- getQuotationMember--", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		logger.info("quotationMemberId::{}quotationId::{}::{}", quotationMemberId, quotationId, LOGEND);
		return commonDto;
	}

	@Override
	public QuotationMemberApiResponseDto getQuotationMembers(Integer quotationId, String pageName) {
		logger.info("getQuotationMembers::quotationId::{}::{}", quotationId, LOGSTART);

		QuotationMemberApiResponseDto commonDto = new QuotationMemberApiResponseDto();

		try {

			Set<?> quotationMember = null;
			
			
			if(quotationId !=null && pageName != null) {
				
				if(pageName.equalsIgnoreCase(CommonConstants.EXISTING)) {
					
					QuotationEntity quotation = getBetweenQuotationAndQuotationMembers(quotationId);
					
					if(quotation !=null)
						if(quotation.getStatus() == 5 || quotation.getStatus() == 4 || quotation.getStatus() == 9) {
							
							quotationMember =quotation.getMembers();
							
//						Set<QuotationMemberEntity> memberList = quotation.getMembers();
//						List<QuotationMemberEntity> list = new ArrayList<>();
//						memberList.forEach(mbr->{
//							list.add(mbr);
//						});
//						
//							quotationMember =list;
						}
								
						
				}else {
					
					QuotationTempEntity quotationTemp = getBetweenQuotationTempAndQuotationTempMembers(quotationId);
					
					if(quotationTemp != null)
						if(quotationTemp.getStatus() != 5 || quotationTemp.getStatus() != 4 || quotationTemp.getStatus() != 9) {
							
							
							quotationMember =quotationTemp.getMembers();
							
//							Set<QuotationMemberTempEntity> memberList = quotationTemp.getMembers();
//							List<QuotationMemberTempEntity> list = new ArrayList<>();
//							memberList.forEach(mbr->{
//								list.add(mbr);
//							});
//								quotationMember =list;
						}
//							quotationMember =quotationTemp.getMembers();
					
				}
				
			}


			if (!CollectionUtils.isEmpty(quotationMember)) {

				List<QuotationMemberDto> quotationMemberDtos = mapSet(quotationMember, QuotationMemberDto.class);
				commonDto.setQuotationMemberDtos(quotationMemberDtos);
				commonDto.setTransactionStatus(CommonConstants.STATUS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
			} else {
				logger.info("Quotation members not found for the given QuotationId: {}", quotationId);
				commonDto.setTransactionMessage(CommonConstants.STATUS);
				commonDto.setTransactionStatus("Quotation members not found for the given QuotationId: " + quotationId);
			}
		} catch (Exception e) {
			logger.error("Exception-- QuotationMemberServiceImpl-- getQuotationMembers--", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL);
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		
		logger.info("getQuotationMembers::quotationId::{}::{}", quotationId);
		return commonDto;
	}

	
	
	private QuotationEntity getBetweenQuotationAndQuotationMembers(Integer quotationId) {
		QuotationEntity quotation =null;
		try {
			
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

			CriteriaQuery<QuotationEntity> searchQuery = criteriaBuilder.createQuery(QuotationEntity.class);
			Root<QuotationEntity> root = searchQuery.from(QuotationEntity.class);
			Join<QuotationEntity, QuotationMemberEntity> members = root.join("members");
			
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.equal(members.get("quotationId"),quotationId));
			predicates.add(criteriaBuilder.equal(members.get("isActive"), Boolean.TRUE));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			
			EntityGraph<QuotationEntity> fetchGraph = entityManager.createEntityGraph(QuotationEntity.class);
			fetchGraph.addSubgraph("members");

			QuotationEntity result = entityManager.createQuery(searchQuery).setHint("javax.persistence.loadgraph", fetchGraph).getResultList().stream().findFirst().orElse(null);
			quotation = result;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return quotation;
	}
	
	
	
	
	private QuotationTempEntity getBetweenQuotationTempAndQuotationTempMembers(Integer quotationId) {
		QuotationTempEntity quotationTemp =null;
		try {
			
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

			CriteriaQuery<QuotationTempEntity> searchQuery = criteriaBuilder.createQuery(QuotationTempEntity.class);
			Root<QuotationTempEntity> root = searchQuery.from(QuotationTempEntity.class);
			Join<QuotationTempEntity, QuotationMemberTempEntity> members = root.join("members");
			
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.equal(members.get("quotationId"),quotationId));
			predicates.add(criteriaBuilder.equal(members.get("isActive"), Boolean.TRUE));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));
			
			EntityGraph<QuotationTempEntity> fetchGraph = entityManager.createEntityGraph(QuotationTempEntity.class);
			fetchGraph.addSubgraph("members");

			QuotationTempEntity result = entityManager.createQuery(searchQuery).setHint("javax.persistence.loadgraph", fetchGraph).getResultList().stream().findFirst().orElse(null);
			quotationTemp = result;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return quotationTemp;
	}
	
	
	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
	}
	private <S, T> List<T> mapSet(Set<S> source, Class<T> targetClass) {
		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
	}

	@Override
	public QuotationMemberBulkResponseDto saveBulkQuotationMember(final Integer quotationId,
			final MultipartFile multipartFile) throws IllegalStateException, IOException {
		/***
		 * 
		 */
		return null;
	}

	public boolean isFileValid(MultipartFile file) {
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		return Arrays.asList("xlsx").contains(ext);

	}

	@Override
	public QuotationFinancialResponseDto getFinancialCalculation(final Integer quotationId) {
//		logger.info("quotationId::{}::{}", quotationId, LOGSTART);
		QuotationFinancialResponseDto commonDto = new QuotationFinancialResponseDto();
//		QuotationFinancialCalcDto calcDto = new QuotationFinancialCalcDto();
//
//		try {
//			logger.info("Quotation get financial calculation QuotationId: {}", quotationId);
//			final List<QuotationMemberTempEntity> mebers = quotationMemberTempRepo.findAllByQuotationId(quotationId);
//			BigDecimal employeeContributions = BigDecimal.ZERO;
//			BigDecimal employerContributions = BigDecimal.ZERO;
//			for (QuotationMemberTempEntity member : mebers) {
//				employeeContributions = employeeContributions.add(member.getEmployeeContribution());
//				employerContributions = employerContributions.add(member.getEmployerContribution());
//			}
//			BigDecimal totalContributions = employeeContributions.add(employerContributions);
//			calcDto.setEmployeeContribution(employeeContributions);
//			calcDto.setEmployerContribution(employerContributions);
//			calcDto.setTotalContribution(totalContributions);
//			calcDto.setTotalMember(mebers.size());
//
//			commonDto.setQuotationId(quotationId);
//			commonDto.setFinancialCalcDto(calcDto);
//			commonDto.setTransactionStatus(CommonConstants.STATUS);
//			commonDto.setTransactionMessage(CommonConstants.FETCH);
//		} catch (IllegalArgumentException e) {
//			logger.error("Exception-- QuotationMemberServiceImpl-- getFinancialCalculation--", e);
//			commonDto.setTransactionMessage(CommonConstants.FAIL);
//			commonDto.setTransactionStatus(CommonConstants.ERROR);
//		}
//		logger.info("quotationId::{}::{}", quotationId, LOGEND);
		return commonDto;
	}
	
	
	
	
	
	
	
//	ram prasad codes
	@Override
	public QuotationMemberApiResponseDto deleteAddressesDetails(Long addressId, Long memberId) {
		logger.info("QuotationServiceImpl:removeAddressesDetails:Start");
		QuotationMemberApiResponseDto response = new QuotationMemberApiResponseDto();
		QuotationMemberAddressTempEntity addressTempEntity = null;
		try {
			
			addressTempEntity = quotationMemberAddressTempRepository.findByMemberIdAndAddressIdAndIsActiveTrue(memberId,addressId);
			addressTempEntity.setIsActive(false);
			quotationMemberAddressTempRepository.save(addressTempEntity);
			
			response.setTransactionStatus(CommonConstants.SUCCESS);
			response.setTransactionMessage("Address Id "+addressId+" is Removed Successfully!");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("QuotationServiceImpl:removeAddressesDetails:Exception-->",e);
		}
		
		logger.info("QuotationServiceImpl:removeAddressesDetails:ended");
		return response;
	}

	@Override
	public QuotationMemberApiResponseDto deleteNomineeDetails(Long nomineeId, Long memberId) {
		logger.info("QuotationServiceImpl:removeNomineeDetails:Start");
		QuotationMemberApiResponseDto response = new QuotationMemberApiResponseDto();
		try {
			QuotationMemberNomineeTempEntity memberNomineeTempEntity = quotationMemberNomineeTempRepo.findByMemberIdAndNomineeIdAndIsActiveTrue(memberId, nomineeId);
			memberNomineeTempEntity.setIsActive(false);
			memberNomineeTempEntity = quotationMemberNomineeTempRepo.save(memberNomineeTempEntity);
			
			response.setTransactionStatus(CommonConstants.SUCCESS);
			response.setTransactionMessage("Nominee Id "+nomineeId+" is Removed Successfully!");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("QuotationServiceImpl:removeNomineeDetails: Exception-->",e);
		}
		logger.info("QuotationServiceImpl:removeNomineeDetails:ended");
		return response;
	}




	@Override
	public void download(Long batchId, String fileType, HttpServletResponse response) {
		try {
			logger.info("QuotationMemberServiceImpl:download:Starts");
			if(batchId !=null && fileType!=null) {
				QuotationMemberBatchEntity batch = quotationMemberBatchRepository.findByBatchIdAndIsActiveTrue(batchId);
				 
				if(batch!=null) {
					
					byte[] bytes =((fileType.equalsIgnoreCase("raw"))?batch.getRawFile():(fileType.equalsIgnoreCase("success"))? 
							batch.getSuccessFile():batch.getFailedFile());
					
					response.setContentType("Content-Type: text/csv");
					response.setHeader("Content-Disposition","attachment;filename="+
							((fileType.equalsIgnoreCase("raw"))?"Raw":(fileType.equalsIgnoreCase("success"))? 
									"Success":"Failed")+" file_"+batchId+".csv");
					ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
					baos.write(bytes, 0, bytes.length);
					OutputStream os = response.getOutputStream();
					response.setHeader("STATUS","File is Downloaded Successfully!!!");
					baos.writeTo(os);
					os.flush();
					os.close();
				}else {
					response.setHeader("STATUS", "No files Available against this BathId "+batchId);
				}
				
			}else {
				response.setHeader("STATUS","BatchId and FileType is null!!");
			}

		} catch (Exception e) {
			logger.error("Exception-- QuotationMemberServiceImpl-- download --", e);
		
		}
		
		logger.info("QuotationMemberServiceImpl:download:Ends");
	}




	@Override
	public QuotationMemberApiResponseDto getBatches(Long batchId) {
		QuotationMemberApiResponseDto commonDto = new QuotationMemberApiResponseDto();
		QuotationMemberBulkResponseDto dto = new QuotationMemberBulkResponseDto();
		
		try {
			logger.info("QuotationMemberServiceImpl:getBatches:Starts");
			
			QuotationMemberBatchEntity batch = quotationMemberBatchRepository.findByBatchIdAndIsActiveTrue(batchId);
			
			if(batch != null) {
				
				List<QuotationMemberErrorEntity> errorList = quotationMemberErrorRepository.findAllByBatchId(batch.getBatchId());
				List<QuotationMemberErrorDto> errorDto = mapList(errorList, QuotationMemberErrorDto.class);
				dto.setBatchId(batch.getBatchId());
				dto.setQuotationId((batch.getQuotationId() != null)?batch.getQuotationId().intValue():null);
				dto.setCreatedOn(batch.getCreatedOn());
				dto.setFileName(batch.getFileName());
				dto.setTotalCount(batch.getTotalCount());
				dto.setSuccessCount(batch.getSuccessCount());
				dto.setFailedCount(batch.getFailedCount());
				dto.setError(errorDto);
				commonDto.setResponseData(dto);
				commonDto.setTransactionStatus(CommonConstants.STATUS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
				
			}else {
				commonDto.setTransactionStatus(CommonConstants.FAIL);
				commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
			}


		} catch (Exception e) {
			logger.error("Exception-- QuotationMemberServiceImpl:getBatches --", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL+" ---> "+e.getMessage());
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		
		logger.info("QuotationMemberServiceImpl:getBatches:Ends");
		return commonDto;
	}




	@Override
	public QuotationMemberApiResponseDto getBatchIdList() {
		QuotationMemberApiResponseDto commonDto = new QuotationMemberApiResponseDto();
		try {
			logger.info("QuotationMemberServiceImpl:getBatchIdList:Starts");
			
			List<QuotationMemberBatchEntity> batches = quotationMemberBatchRepository.findAllByOrderByBatchIdDesc();
			
			if(!batches.isEmpty()) {
				List<Long> batchIdList = new ArrayList<>();
				batches.forEach(batch->batchIdList.add(batch.getBatchId()));
				commonDto.setResponseData(batchIdList);
				commonDto.setTransactionStatus(CommonConstants.STATUS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
				
			}else {
				commonDto.setTransactionStatus(CommonConstants.FAIL);
				commonDto.setTransactionMessage(CommonConstants.NO_RECORD_FOUND);
			}


		} catch (Exception e) {
			logger.error("Exception-- QuotationMemberServiceImpl:getBatchIdList --", e);
			commonDto.setTransactionMessage(CommonConstants.FAIL+" ---> "+e.getMessage());
			commonDto.setTransactionStatus(CommonConstants.ERROR);
		}
		
		logger.info("QuotationMemberServiceImpl:getBatchIdList:Ends");
		return commonDto;
	}
}
