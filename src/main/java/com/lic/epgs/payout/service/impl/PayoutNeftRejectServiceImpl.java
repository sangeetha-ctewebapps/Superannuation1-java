package com.lic.epgs.payout.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.claim.repository.ClaimOnboardingRepository;
import com.lic.epgs.claim.repository.ClaimRepository;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.entity.IcodeMasterEntity;
import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.common.repository.IcodeMasterRepository;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.payout.constants.PayoutConstants;
import com.lic.epgs.payout.constants.PayoutErrorConstants;
import com.lic.epgs.payout.constants.PayoutStatus;
import com.lic.epgs.payout.constants.PayoutStoredProcedureConstants;
import com.lic.epgs.payout.dto.PayoutAnnuityCalcDto;
import com.lic.epgs.payout.dto.PayoutCommutationCalcDto;
import com.lic.epgs.payout.dto.PayoutDocumentDetailDto;
import com.lic.epgs.payout.dto.PayoutDto;
import com.lic.epgs.payout.dto.PayoutFundValueDto;
import com.lic.epgs.payout.dto.PayoutMbrAddressDto;
import com.lic.epgs.payout.dto.PayoutMbrAppointeeDto;
import com.lic.epgs.payout.dto.PayoutMbrBankDetailDto;
import com.lic.epgs.payout.dto.PayoutMbrDto;
import com.lic.epgs.payout.dto.PayoutMbrFundValueDto;
import com.lic.epgs.payout.dto.PayoutMbrNomineeDto;
import com.lic.epgs.payout.dto.PayoutNotesDto;
import com.lic.epgs.payout.dto.PayoutPayeeBankDetailsDto;
import com.lic.epgs.payout.dto.PayoutSearchRequestDto;
import com.lic.epgs.payout.dto.PayoutSearchResponseDto;
import com.lic.epgs.payout.dto.ReinitiateRequestDto;
import com.lic.epgs.payout.dto.ReinitiateResponse;
import com.lic.epgs.payout.entity.PayoutAnnuityCalcEntity;
import com.lic.epgs.payout.entity.PayoutCommutationCalcEntity;
import com.lic.epgs.payout.entity.PayoutEntity;
import com.lic.epgs.payout.entity.PayoutFundValueEntity;
import com.lic.epgs.payout.entity.PayoutMbrAddressEntity;
import com.lic.epgs.payout.entity.PayoutMbrAppointeeEntity;
import com.lic.epgs.payout.entity.PayoutMbrBankDetailEntity;
import com.lic.epgs.payout.entity.PayoutMbrEntity;
import com.lic.epgs.payout.entity.PayoutMbrFundValueEntity;
import com.lic.epgs.payout.entity.PayoutMbrNomineeEntity;
import com.lic.epgs.payout.entity.PayoutNotesEntity;
import com.lic.epgs.payout.entity.PayoutPayeeBankDetailsEntity;
import com.lic.epgs.payout.entity.ReinitiateStoredProcedureRequestEntity;
import com.lic.epgs.payout.entity.ReinitiateStoredProcedureResponseEntity;
import com.lic.epgs.payout.entity.StoredProcedureResponseEntity;
import com.lic.epgs.payout.repository.PayoutNotesRepository;
import com.lic.epgs.payout.repository.PayoutPayeeBankDetailsRepository;
import com.lic.epgs.payout.repository.PayoutRepository;
import com.lic.epgs.payout.repository.ReinitiateStoredProcedureRequestRepository;
import com.lic.epgs.payout.repository.ReinitiateStoredProcedureResponseRepository;
import com.lic.epgs.payout.repository.StoredProcedureResponseRepository;
import com.lic.epgs.payout.service.PayoutNeftRejectService;
import com.lic.epgs.payout.temp.entity.PayoutPayeeBankDetailsTempEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutAnnuityCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutCommutationCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutFundValueEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrAddressEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrAppointeeEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrBankDetailEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrFundValueEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrNomineeEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutNotesEntity;
import com.lic.epgs.payout.temp.repository.PayoutPayeeBankDetailsTempRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutAnnuityCalcRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutCommutationCalcRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutDocumentDetailRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutFundValueRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrAddressRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrAppointeeRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrBankDtlsRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrFundValueRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrNomineeRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutNotesRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutRepository;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

@Service
@Transactional
public class PayoutNeftRejectServiceImpl implements PayoutNeftRejectService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	@Qualifier("jsonObjectMapper")
	ObjectMapper objectMapper;
	
	@Autowired
	EntityManager entityManager;
	@Autowired
	StoredProcedureResponseRepository storedProcedureResponseRepository;
	@Autowired
	PayoutRepository payoutRepository;
	@Autowired
	ClaimRepository claimRepository;
	@Autowired
	TempPayoutRepository tempPayoutRepository;
	@Autowired
	TempPayoutMbrRepository tempPayoutDtlsRepository;
	@Autowired
	TempPayoutMbrNomineeRepository tempPayoutMbrNomineeRepository;
	@Autowired
	TempPayoutMbrBankDtlsRepository tempPayoutMbrBankDtlsRepository;
	@Autowired
	TempPayoutMbrAddressRepository tempPayoutMbrAddressRepository;
	@Autowired
	TempPayoutMbrAppointeeRepository tempPayoutMbrAppointeeRepository;
	@Autowired
	TempPayoutAnnuityCalcRepository tempPayoutAnnuityCalcRepository;
	@Autowired
	TempPayoutCommutationCalcRepository tempPayoutCommutationCalcRepository;
	@Autowired
	TempPayoutMbrFundValueRepository tempPayoutMbrFundValueRepository;
	@Autowired
	TempPayoutFundValueRepository tempPayoutFundValueRepository;
	@Autowired
	TempPayoutDocumentDetailRepository tempPayoutDocumentDetailRepository;
	@Autowired
	TempPayoutMbrRepository tempPayoutMbrRepository;

	@Autowired
	TempPayoutNotesRepository tempPayoutNotesRepository;
	@Autowired
	PayoutNotesRepository payoutNotesRepository;

	@Autowired
	PayoutPayeeBankDetailsRepository payoutPayeeBankDetailsRepository;
	@Autowired
	PayoutPayeeBankDetailsTempRepository payoutPayeeBankDetailsTempRepository;

	@Autowired
	IntegrationService integrationService;

	@Autowired
	IcodeMasterRepository icodeMasterRepository;

	@Autowired
	ClaimOnboardingRepository claimOnboardingRepository;

	@Autowired
	ReinitiateStoredProcedureResponseRepository reinitiateStoredProcedureResponseRepository;

	@Autowired
	Environment environment;

	@Autowired
	CommonService commonService;
	
	@Autowired
	ReinitiateStoredProcedureRequestRepository reinitiateStoredProcedureRequestRepository;


	public synchronized String getStoreProcedureSeq() {
		return commonService.getSequence(PayoutStoredProcedureConstants.BENEFIARY_PAYMENT_ID);
	}

	@Override
	public ApiResponseDto<Set<PayoutNotesDto>> addTempNotes(PayoutNotesDto payoutNotesDto) {
		try {
			logger.info("PayoutNeftRejectServiceImpl :: add ::Start");
			Optional<TempPayoutEntity> result = tempPayoutRepository
					.findByPayoutNoAndIsActive(payoutNotesDto.getPayoutNo(), Boolean.TRUE);
			if (result.isPresent()) {
				TempPayoutEntity payoutTemp = result.get();
				payoutTemp = convertToTempEntity(payoutNotesDto, payoutTemp);
				payoutTemp = tempPayoutRepository.save(payoutTemp);
				if (payoutTemp.getPayoutNotes() != null) {
					Set<PayoutNotesDto> response = payoutTemp.getPayoutNotes().stream()
							.map(this::convertTempEntityToDto).collect(Collectors.toSet());
					return ApiResponseDto.success(response, "");
				}
				return ApiResponseDto.success(null, "Notes saved succesfully.");
			} else {
				return ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PayoutNeftRejectServiceImpl:add : ", e);
			return ApiResponseDto.error(ErrorDto.builder().message("Error saving payout notes").build());
		} finally {
			logger.info("PayoutNeftRejectServiceImpl :: add ::Ends ");
		}
	}

	private TempPayoutEntity convertToTempEntity(PayoutNotesDto payoutNotesDto, TempPayoutEntity tempPayouts) {
		List<TempPayoutNotesEntity> notesList = tempPayouts.getPayoutNotes();
		TempPayoutNotesEntity entity = new TempPayoutNotesEntity();
		entity.setCreatedBy(payoutNotesDto.getCreatedBy());
		entity.setCreatedOn(CommonDateUtils.sysDate());
		entity.setNoteContents(payoutNotesDto.getNoteContents());
		entity.setPayout(tempPayouts);
		notesList.add(entity);
		tempPayouts.setPayoutNotes(notesList);
		return tempPayouts;
	}

	@Override
	public ApiResponseDto<Set<PayoutNotesDto>> getTempNotesByPayoutNo(String payoutNo) {
		try {
			logger.info("PayoutNeftRejectServiceImpl :: add ::Start");
			List<TempPayoutNotesEntity> result = tempPayoutNotesRepository.findByPayoutNo(payoutNo);
			if (!result.isEmpty()) {
				Set<PayoutNotesDto> response = result.stream().map(this::convertTempEntityToDto)
						.collect(Collectors.toSet());
				return ApiResponseDto.success(response);
			}
			return ApiResponseDto.success(Collections.emptySet());
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PayoutNeftRejectServiceImpl:add", e);
			return ApiResponseDto.error(ErrorDto.builder().message("Error fetching payout notes").build());
		} finally {
			logger.info("PayoutNeftRejectServiceImpl :: add ::Ends");
		}
	}

	private PayoutNotesDto convertTempEntityToDto(TempPayoutNotesEntity entity) {
		PayoutNotesDto payoutNotesDto = new PayoutNotesDto();
		payoutNotesDto.setPayoutNo(entity.getPayoutNo());
		payoutNotesDto.setNoteContents(entity.getNoteContents());
		payoutNotesDto.setCreatedBy(entity.getCreatedBy());
		payoutNotesDto.setCreatedOn(entity.getCreatedOn());
		return payoutNotesDto;
	}

	@Override
	public ApiResponseDto<Set<PayoutNotesDto>> add(PayoutNotesDto payoutNotesDto) {
		try {
			logger.info("PayoutNeftRejectServiceImpl :: add ::Start");
			Optional<PayoutEntity> result = payoutRepository.findByPayoutNoAndIsActive(payoutNotesDto.getPayoutNo(),
					Boolean.TRUE);
			if (result.isPresent()) {
				PayoutEntity payoutEntity = result.get();
				PayoutNotesEntity entity = convertToMainEntity(payoutNotesDto, payoutEntity);
				entity.setPayout(payoutEntity);
				payoutNotesRepository.save(entity);
				return ApiResponseDto.success(null, "Notes saved succesfully.");
			} else {
				return ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PayoutNeftRejectServiceImpl:add", e);
			return ApiResponseDto.error(ErrorDto.builder().message("Error saving payout notes").build());
		} finally {
			logger.info("PayoutNeftRejectServiceImpl :: add ::Ends");
		}
	}

	private PayoutNotesEntity convertToMainEntity(PayoutNotesDto payoutNotesDto, PayoutEntity payouts) {
		PayoutNotesEntity entity = new PayoutNotesEntity();
		entity.setCreatedBy(payoutNotesDto.getCreatedBy());
		entity.setCreatedOn(CommonDateUtils.sysDate());
		entity.setNoteContents(payoutNotesDto.getNoteContents());
		entity.setPayoutNo(payouts.getPayoutNo());
		return entity;
	}

	@Override
	public ApiResponseDto<Set<PayoutNotesDto>> getNotesByPayoutNo(String payoutNo) {
		try {
			logger.info("PayoutNeftRejectServiceImpl :: add ::Start");

			List<PayoutNotesEntity> result = payoutNotesRepository.findByPayoutNo(payoutNo);

			if (!result.isEmpty()) {
				Set<PayoutNotesDto> response = result.stream().map(this::convertMainEntityToDto)
						.collect(Collectors.toSet());
				return ApiResponseDto.success(response);
			}
			return ApiResponseDto.success(Collections.emptySet());
		} catch (IllegalArgumentException e) {
			logger.error("Exception:PayoutNeftRejectServiceImpl:add", e);
			return ApiResponseDto.error(ErrorDto.builder().message("Error fetching payout notes").build());
		} finally {
			logger.info("PayoutNeftRejectServiceImpl :: add ::Ends");
		}
	}

	private PayoutNotesDto convertMainEntityToDto(PayoutNotesEntity entity) {
		PayoutNotesDto payoutNotesDto = new PayoutNotesDto();
		payoutNotesDto.setPayoutNo(entity.getPayoutNo());
		payoutNotesDto.setNoteContents(entity.getNoteContents());
		payoutNotesDto.setCreatedBy(entity.getCreatedBy());
		payoutNotesDto.setCreatedOn(entity.getCreatedOn());
		return payoutNotesDto;
	}

	public ReinitiateResponse reinitiateStoredProcedure(Long payoutId, String oldBeneficiaryPaymentId,String newReinitiateBeneficiaryPaymentId) {
		logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure Method:Start");
		ReinitiateResponse reinitiateResponse = new ReinitiateResponse();
		try {
			ReinitiateRequestDto reinitiateRequestDto = new ReinitiateRequestDto();
			StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery(environment.getProperty("accounting.schema.name") + "."+ environment.getProperty("neft.reject.store.proc.name"));
			setReinitiateInputParameters(storedProcedureQuery);
			setReinitiateOutputParameters(storedProcedureQuery);
			TempPayoutEntity payoutEntity = tempPayoutRepository.findByPayoutIdAndIsActiveTrue(payoutId);
			if (payoutEntity != null) {
				logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure Payout :Start");
				PayoutPayeeBankDetailsTempEntity payoutPayeeBankDetailsTempEntity = payoutPayeeBankDetailsTempRepository.findByBenefiaryPaymentIdAndIsActiveTrue(newReinitiateBeneficiaryPaymentId);
				if (payoutPayeeBankDetailsTempEntity != null) {
					logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure PayoutPayeeBank :Start");
					TempPayoutMbrEntity tempPayoutMbrEntity = tempPayoutMbrRepository.findByMemberId(payoutPayeeBankDetailsTempEntity.getPayoutMbrEntity().getMemberId());
					if (tempPayoutMbrEntity != null) {
						logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure Payout Member :Start");
						TempPayoutCommutationCalcEntity tempPayoutCommutationCalcEntity = tempPayoutCommutationCalcRepository.getCommutationDetails(tempPayoutMbrEntity.getMemberId());

						logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure Request Starts:Start");
						storedProcedureQuery.setParameter("accountRuleContext", "Initiate Reapproval For NEFT Reject");
						reinitiateRequestDto.setAccountRuleContext("Initiate Reapproval For NEFT Reject");
						
						String claimOnboardNo = claimOnboardingRepository.getClaimOnboardNo(payoutEntity.getClaimNo());
						if (claimOnboardNo != null) {
							String newClaimOnboardNo = claimOnboardNo.substring(6);
							storedProcedureQuery.setParameter("RefNo", newClaimOnboardNo);
							reinitiateRequestDto.setRefNo(newClaimOnboardNo);
						}
						storedProcedureQuery.setParameter("BeneficiaryBankIfsc",payoutPayeeBankDetailsTempEntity.getIfscCode());
						reinitiateRequestDto.setBeneficiaryBankIfsc(payoutPayeeBankDetailsTempEntity.getIfscCode());
						
						storedProcedureQuery.setParameter("BeneficiaryAccountNumber",payoutPayeeBankDetailsTempEntity.getAccountNumber());
						reinitiateRequestDto.setBeneficiaryAccountNumber(payoutPayeeBankDetailsTempEntity.getAccountNumber());
						
						storedProcedureQuery.setParameter("EffectiveDateOfPayment", LocalDate.now());
						reinitiateRequestDto.setEffectiveDateOfPayment(String.valueOf(LocalDate.now()));
						
						storedProcedureQuery.setParameter("PayoutSourceModule", "Superannuation");
						reinitiateRequestDto.setPayoutSourceModule("Superannuation");
						
						storedProcedureQuery.setParameter("PaymentIdOld", oldBeneficiaryPaymentId);
						reinitiateRequestDto.setPaymentIdOld(oldBeneficiaryPaymentId);
						
						storedProcedureQuery.setParameter("BeneficiaryPaymentId", newReinitiateBeneficiaryPaymentId);
						reinitiateRequestDto.setBeneficiaryPaymentId(newReinitiateBeneficiaryPaymentId);
						
						logger.info("PayoutNeftRejectServiceImpl:Product Details:Start");
						
						JsonNode response = integrationService.getProductDetailsByProductId(NumericUtils.stringToLong(payoutEntity.getProduct()));
						if (response != null) {
							JsonNode proposeDetails = response.path("responseData");
							String product = proposeDetails.path("productCode").textValue();
							storedProcedureQuery.setParameter("ProductCode", product);
							reinitiateRequestDto.setProductCode(product);
						}
						JsonNode newResponse = integrationService.getVariantDetailsByProductVariantId(NumericUtils.stringToLong(payoutEntity.getVariant()));
						if (newResponse != null) {
							JsonNode proposeDetails = newResponse.path("responseData");
							String variantCode = proposeDetails.path("subCategory").textValue();
							String variantVersion = proposeDetails.path("variantVersion").textValue();

							storedProcedureQuery.setParameter("VariantCode", variantCode);
							reinitiateRequestDto.setVariantCode(variantCode);
							
							if (tempPayoutCommutationCalcEntity != null) {
								//if (payoutEntity.getModeOfExit().equals(PayoutStoredProcedureConstants.MODE_OF_EXIT)) {
									storedProcedureQuery.setParameter("TotalAmount",BigDecimal.valueOf(tempPayoutCommutationCalcEntity.getNetAmount()));
									reinitiateRequestDto.setTotalAmount(NumericUtils.doubleToBigDecimal(tempPayoutCommutationCalcEntity.getNetAmount()));
//								} else {
//									storedProcedureQuery.setParameter("TotalAmount",BigDecimal.valueOf(tempPayoutCommutationCalcEntity.getCommutationAmt()));
//									reinitiateRequestDto.setTotalAmount(String.valueOf(tempPayoutCommutationCalcEntity.getNetAmount()));
//								}
							}
							storedProcedureQuery.setParameter("OperatingUnit", payoutEntity.getUnitCode());
							reinitiateRequestDto.setUnitCode(payoutEntity.getUnitCode());
							storedProcedureQuery.setParameter("OperatingUnitType", "UO");
							reinitiateRequestDto.setOperatingUnitType("UO");
							storedProcedureQuery.setParameter("PaymentMode", "N");
							reinitiateRequestDto.setPaymentMode("N");
							storedProcedureQuery.setParameter("PolicyNo", payoutEntity.getMasterPolicyNo());
							reinitiateRequestDto.setPolicyNo(payoutEntity.getMasterPolicyNo());	
							reinitiateRequestDto.setCreatedBy(payoutEntity.getCreatedBy());
							
							if (payoutEntity.getLineOfBusiness() != null) {
								storedProcedureQuery.setParameter("LOB", payoutEntity.getLineOfBusiness());
								reinitiateRequestDto.setLOB(payoutEntity.getLineOfBusiness());
							} else {
								storedProcedureQuery.setParameter("LOB", "");
								reinitiateRequestDto.setLOB("");
							}
							storedProcedureQuery.setParameter("Product", payoutEntity.getProduct());
							reinitiateRequestDto.setProduct(payoutEntity.getProduct());
							storedProcedureQuery.setParameter("MPHCODE", payoutEntity.getMphCode());
							reinitiateRequestDto.setMPHCODE(payoutEntity.getMphCode());
							storedProcedureQuery.setParameter("ProductVariant", payoutEntity.getVariant());
							reinitiateRequestDto.setProductVariant(payoutEntity.getVariant());
							logger.info("PayoutNeftRejectServiceImpl:Icode Details :Start");
							setIcodeDetails(variantVersion, storedProcedureQuery,reinitiateRequestDto);
							
							  if (payoutEntity.getModeOfExit().equals(PayoutStoredProcedureConstants.MODE_OF_EXIT)) {
								storedProcedureQuery.setParameter("BeneficiaryName",
										payoutEntity.getMphName() != null ? payoutEntity.getMphName().substring(0, 70)
												: "");
								reinitiateRequestDto.setBeneficiaryName(
										payoutEntity.getMphName() != null ? payoutEntity.getMphName().substring(0, 70)
												: "");
							} else {
								storedProcedureQuery.setParameter("BeneficiaryName",
										tempPayoutMbrEntity.getFirstName() != null
												? tempPayoutMbrEntity.getFirstName().substring(0, 70)
												: "");
								reinitiateRequestDto.setBeneficiaryName(tempPayoutMbrEntity.getFirstName() != null
										? tempPayoutMbrEntity.getFirstName().substring(0, 70)
										: "");
							}
							
							storedProcedureQuery.setParameter("BeneficiaryBankName",payoutPayeeBankDetailsTempEntity.getBankName());
							reinitiateRequestDto.setBeneficiaryBankName(payoutPayeeBankDetailsTempEntity.getBankName());
							storedProcedureQuery.setParameter("BeneficiaryBranchIFSC",payoutPayeeBankDetailsTempEntity.getIfscCode());
							reinitiateRequestDto.setBeneficiaryBranchIFSC(payoutPayeeBankDetailsTempEntity.getIfscCode());
							storedProcedureQuery.setParameter("BeneficiaryBranchName",payoutPayeeBankDetailsTempEntity.getBankBranch());
							reinitiateRequestDto.setBeneficiaryBranchName(payoutPayeeBankDetailsTempEntity.getBankBranch());
							storedProcedureQuery.setParameter("BeneficiaryAccountType", "AC4");
							reinitiateRequestDto.setBeneficiaryAccountType("AC4");
							storedProcedureQuery.setParameter("BeneficiaryLei", "");
							reinitiateRequestDto.setBeneficiaryLei("");
							storedProcedureQuery.setParameter("SenderLei", "");
							reinitiateRequestDto.setSenderLei("");
							storedProcedureQuery.setParameter("UnitCode", payoutEntity.getUnitCode());
							reinitiateRequestDto.setUnitCode(payoutEntity.getUnitCode());
							storedProcedureQuery.setParameter("PolicyNumber", payoutEntity.getMasterPolicyNo());
							reinitiateRequestDto.setPolicyNumber(payoutEntity.getMasterPolicyNo());
							storedProcedureQuery.setParameter("MemberNumber", tempPayoutMbrEntity.getLicId());
							reinitiateRequestDto.setMemberNumber(tempPayoutMbrEntity.getLicId());
							storedProcedureQuery.setParameter("PaymentCategory", "PCM002");
							reinitiateRequestDto.setPaymentCategory("PCM002");
							storedProcedureQuery.setParameter("PaymentSubCategory", "o");
							reinitiateRequestDto.setPaymentSubCategory("o");
							storedProcedureQuery.setParameter("NroAccount", "");
							reinitiateRequestDto.setNroAccount("");
							storedProcedureQuery.setParameter("Iban", "");
							reinitiateRequestDto.setIban("");
							storedProcedureQuery.setParameter("Remarks", "");
							reinitiateRequestDto.setRemarks("");
							
							logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure Request End:Start");
							
							String requestString = objectMapper.writeValueAsString(reinitiateRequestDto);
							reinitiateResponse.setApiRequestString(CommonUtils.stringMax(requestString));
							reinitiateResponse.setRequestDate(new Date());
							
							saveReinitiateStoredProcedureRequest(reinitiateRequestDto);
							
							storedProcedureQuery.execute();
							
							logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure Process Executed");
							
							reinitiateResponse.setPayoutId(payoutId);
							reinitiateResponse.setBenefiaryPaymentId(newReinitiateBeneficiaryPaymentId);
							reinitiateResponse.setPaymentIdOld(oldBeneficiaryPaymentId);
							reinitiateResponse.setJournalNumber((Long) storedProcedureQuery.getOutputParameterValue("OUT_JournalNumber"));
							reinitiateResponse.setDebitAccount((Long) storedProcedureQuery.getOutputParameterValue("OUT_DebitAccount"));
							reinitiateResponse.setCreditAccount((Long) storedProcedureQuery.getOutputParameterValue("OUT_CreditAccount"));
							reinitiateResponse.setAmount((BigDecimal) storedProcedureQuery.getOutputParameterValue("OUT_TotalAmount"));
							reinitiateResponse.setCrditIcode((String) storedProcedureQuery.getOutputParameterValue("OUT_CreditICode"));
							reinitiateResponse.setDebitIcode((String) storedProcedureQuery.getOutputParameterValue("OUT_DebitICode"));
							reinitiateResponse.setOutMessage((String) storedProcedureQuery.getOutputParameterValue("OUT_Message"));
							reinitiateResponse.setOutStatus((String) storedProcedureQuery.getOutputParameterValue("OUT_Status"));
							reinitiateResponse.setStatusCode((String) storedProcedureQuery.getOutputParameterValue("OUT_StatusCode"));
							reinitiateResponse.setSqlCode((Integer) storedProcedureQuery.getOutputParameterValue("P_SQLCODE"));
							reinitiateResponse.setSqlErrorMsg((String) storedProcedureQuery.getOutputParameterValue("P_SQLERROR_MESSAGE"));

							reinitiateResponse.setApiResponseString((String) storedProcedureQuery.getOutputParameterValue("OUT_Status"));
							reinitiateResponse.setResponseDate(new Date());
							reinitiateResponse.setType("Reinitiate Stored Procedure");
							reinitiateResponse.setRemark("Reinitiate Stored Procedure");
							reinitiateResponse.setErrorResponse((String) storedProcedureQuery.getOutputParameterValue("P_SQLERROR_MESSAGE"));
							logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure save Response:Start");
							saveReinitiateStoredProcedure(payoutId, reinitiateResponse);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure Method -Error:", e);
		}
		logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure Method:End");
		return reinitiateResponse;

	}

	private void setReinitiateInputParameters(StoredProcedureQuery storedProcedureQuery) {
		storedProcedureQuery.registerStoredProcedureParameter("accountRuleContext", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("RefNo", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("BeneficiaryBankIfsc", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("BeneficiaryAccountNumber", String.class,ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("EffectiveDateOfPayment", LocalDate.class,ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("PayoutSourceModule", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("PaymentIdOld", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("BeneficiaryPaymentId", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("ProductCode", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("VariantCode", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("TotalAmount", BigDecimal.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("OperatingUnit", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("OperatingUnitType", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("PaymentMode", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("PolicyNo", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("LOB", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("Product", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("MPHCODE", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("ProductVariant", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("IcodeForLOB", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("IcodeForProductLine", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("IcodeForVarient", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("IcodeForBusinessType", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("IcodeForParticipatingType", String.class,ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("IcodeForBusinessSegment", String.class,ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("IcodeForInvestmentPortfolio", String.class,ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("BeneficiaryName", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("BeneficiaryBankName", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("BeneficiaryBranchIFSC", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("BeneficiaryBranchName", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("BeneficiaryAccountType", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("BeneficiaryLei", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("SenderLei", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("UnitCode", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("PolicyNumber", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("MemberNumber", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("PaymentCategory", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("PaymentSubCategory", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("NroAccount", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("Iban", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("Remarks", String.class, ParameterMode.IN);
			
	}

	private void setReinitiateOutputParameters(StoredProcedureQuery storedProcedureQuery) {

		storedProcedureQuery.registerStoredProcedureParameter("OUT_JournalNumber", Long.class, ParameterMode.OUT);
		storedProcedureQuery.registerStoredProcedureParameter("OUT_DebitAccount", Long.class, ParameterMode.OUT);
		storedProcedureQuery.registerStoredProcedureParameter("OUT_CreditAccount", Long.class, ParameterMode.OUT);
		storedProcedureQuery.registerStoredProcedureParameter("OUT_TotalAmount", BigDecimal.class, ParameterMode.OUT);
		storedProcedureQuery.registerStoredProcedureParameter("OUT_CreditICode", String.class, ParameterMode.OUT);
		storedProcedureQuery.registerStoredProcedureParameter("OUT_DebitICode", String.class, ParameterMode.OUT);
		storedProcedureQuery.registerStoredProcedureParameter("OUT_Message", String.class, ParameterMode.OUT);
		storedProcedureQuery.registerStoredProcedureParameter("OUT_Status", String.class, ParameterMode.OUT);
		storedProcedureQuery.registerStoredProcedureParameter("OUT_StatusCode", String.class, ParameterMode.OUT);
		storedProcedureQuery.registerStoredProcedureParameter("P_SQLCODE", Integer.class, ParameterMode.OUT);
		storedProcedureQuery.registerStoredProcedureParameter("P_SQLERROR_MESSAGE", String.class, ParameterMode.OUT);
	}

	private void setIcodeDetails(String variantVersion, StoredProcedureQuery storedProcedureQuery, ReinitiateRequestDto reinitiateRequestDto) {
		IcodeMasterEntity icodeMasterEntity = icodeMasterRepository.findByVariant(variantVersion);
		if (icodeMasterEntity != null) {
			logger.info("PayoutNeftRejectServiceImpl:Icode Details:Start");
			storedProcedureQuery.setParameter("IcodeForLOB", String.valueOf(icodeMasterEntity.getIcodeForLob()));
			reinitiateRequestDto.setIcodeForLOB(String.valueOf(icodeMasterEntity.getIcodeForLob()));
			storedProcedureQuery.setParameter("IcodeForProductLine",String.valueOf(icodeMasterEntity.getIcodeForProductLine()));
			reinitiateRequestDto.setIcodeForProductLine(String.valueOf(icodeMasterEntity.getIcodeForProductLine()));
			storedProcedureQuery.setParameter("IcodeForVarient",String.valueOf(icodeMasterEntity.getIcodeForVariant()));
			reinitiateRequestDto.setIcodeForVarient(String.valueOf(icodeMasterEntity.getIcodeForVariant()));
			storedProcedureQuery.setParameter("IcodeForBusinessType",String.valueOf(icodeMasterEntity.getIcodeForBusinessType()));
			reinitiateRequestDto.setIcodeForBusinessType(String.valueOf(icodeMasterEntity.getIcodeForBusinessType()));
			storedProcedureQuery.setParameter("IcodeForParticipatingType",String.valueOf(icodeMasterEntity.getIcodeForParticipating()));
			reinitiateRequestDto.setIcodeForParticipatingType(String.valueOf(icodeMasterEntity.getIcodeForParticipating()));
			storedProcedureQuery.setParameter("IcodeForBusinessSegment",String.valueOf(icodeMasterEntity.getIcodeForBusinessSegment()));
			reinitiateRequestDto.setIcodeForBusinessSegment(String.valueOf(icodeMasterEntity.getIcodeForBusinessSegment()));
			storedProcedureQuery.setParameter("IcodeForInvestmentPortfolio",String.valueOf(icodeMasterEntity.getIcodeForInvestmentPortFolio()));
			reinitiateRequestDto.setIcodeForInvestmentPortfolio(String.valueOf(icodeMasterEntity.getIcodeForInvestmentPortFolio()));
		}
	}

	private void saveReinitiateStoredProcedure(Long payoutId, ReinitiateResponse reinitiateResponse) {
		logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure Response Started:");
		ReinitiateStoredProcedureResponseEntity reinitiateStoredProcedureResponseEntity = new ReinitiateStoredProcedureResponseEntity();
		if (reinitiateResponse != null) {
//			reinitiateStoredProcedureResponseEntity.setPayoutId(payoutId);
//			reinitiateStoredProcedureResponseEntity.setBenefiaryPaymentId(reinitiateResponse.getBenefiaryPaymentId());
//			reinitiateStoredProcedureResponseEntity.setPaymentIdOld(reinitiateResponse.getPaymentIdOld());
//			reinitiateStoredProcedureResponseEntity.setJournalNo(reinitiateResponse.getJournalNumber());
//			reinitiateStoredProcedureResponseEntity.setDebitAccount(reinitiateResponse.getDebitAccount());
//			reinitiateStoredProcedureResponseEntity.setCreditAccount(reinitiateResponse.getCreditAccount());
//			reinitiateStoredProcedureResponseEntity.setTotalAmount(reinitiateResponse.getAmount());
//			reinitiateStoredProcedureResponseEntity.setCreditCode(reinitiateResponse.getCrditIcode());
//			reinitiateStoredProcedureResponseEntity.setDebitCode(reinitiateResponse.getDebitIcode());
//			reinitiateStoredProcedureResponseEntity.setMessage(reinitiateResponse.getOutMessage());
//			reinitiateStoredProcedureResponseEntity.setStatusCode(reinitiateResponse.getStatusCode());
//			reinitiateStoredProcedureResponseEntity.setStatus(reinitiateResponse.getOutStatus());
//			reinitiateStoredProcedureResponseEntity.setSqlCode(reinitiateResponse.getSqlCode());
//			reinitiateStoredProcedureResponseEntity.setSqlErrorMessage(reinitiateResponse.getSqlErrorMsg());
//			reinitiateStoredProcedureResponseEntity.setCreatedOn(DateUtils.sysDate());
			
			
			reinitiateStoredProcedureResponseEntity.setReinitiateSpId(null);
			reinitiateStoredProcedureResponseEntity.setPayoutId(payoutId);
			reinitiateStoredProcedureResponseEntity.setJournalNo(reinitiateResponse.getJournalNumber());
			reinitiateStoredProcedureResponseEntity.setDebitAccount(reinitiateResponse.getDebitAccount());
			reinitiateStoredProcedureResponseEntity.setTotalAmount(reinitiateResponse.getAmount());
			reinitiateStoredProcedureResponseEntity.setCreditAccount(reinitiateResponse.getCreditAccount());
			reinitiateStoredProcedureResponseEntity.setCreditCode(reinitiateResponse.getCrditIcode());
			reinitiateStoredProcedureResponseEntity.setDebitCode(reinitiateResponse.getDebitIcode());
			reinitiateStoredProcedureResponseEntity.setMessage(reinitiateResponse.getOutMessage());
			reinitiateStoredProcedureResponseEntity.setStatus(reinitiateResponse.getOutStatus());
			reinitiateStoredProcedureResponseEntity.setStatusCode(reinitiateResponse.getStatusCode());
			reinitiateStoredProcedureResponseEntity.setSqlCode(reinitiateResponse.getSqlCode());
			reinitiateStoredProcedureResponseEntity.setSqlErrorMessage(reinitiateResponse.getSqlErrorMsg());
			reinitiateStoredProcedureResponseEntity.setCreatedOn(DateUtils.sysDate());
			reinitiateStoredProcedureResponseEntity.setModifiedOn(DateUtils.sysDate());
			reinitiateStoredProcedureResponseEntity.setBenefiaryPaymentId(reinitiateResponse.getBenefiaryPaymentId());
			reinitiateStoredProcedureResponseEntity.setPaymentIdOld(reinitiateResponse.getPaymentIdOld());
			reinitiateStoredProcedureResponseEntity.setApiRequest(reinitiateResponse.getApiRequest());
			reinitiateStoredProcedureResponseEntity.setApiRequestString(reinitiateResponse.getApiRequestString());
			reinitiateStoredProcedureResponseEntity.setApiResponse(reinitiateResponse.getApiResponse());
			reinitiateStoredProcedureResponseEntity.setApiResponseString(reinitiateResponse.getApiResponseString());
			reinitiateStoredProcedureResponseEntity.setRequestDate(reinitiateResponse.getRequestDate());
			reinitiateStoredProcedureResponseEntity.setResponseDate(reinitiateResponse.getResponseDate());
			reinitiateStoredProcedureResponseEntity.setType(reinitiateResponse.getType());
			reinitiateStoredProcedureResponseEntity.setApiUrl(reinitiateResponse.getApiUrl());
			reinitiateStoredProcedureResponseEntity.setRemark(reinitiateResponse.getRemark());
			reinitiateStoredProcedureResponseEntity.setErrorResponse(reinitiateResponse.getErrorResponse());
			logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure Response Save:");
			reinitiateStoredProcedureResponseRepository.save(reinitiateStoredProcedureResponseEntity);
		}

	}
	
	
	public void saveReinitiateStoredProcedureRequest(ReinitiateRequestDto reinitiateRequestDto) {
		if(reinitiateRequestDto != null) {
		ReinitiateStoredProcedureRequestEntity reinitiateStoredProcedureRequestEntity = new ReinitiateStoredProcedureRequestEntity();
		logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure Request Save start:Start");
		reinitiateStoredProcedureRequestEntity.setAccountRuleContext(reinitiateRequestDto.getAccountRuleContext());
		reinitiateStoredProcedureRequestEntity.setBeneficiaryAccountNumber(reinitiateRequestDto.getBeneficiaryAccountNumber());
		reinitiateStoredProcedureRequestEntity.setBeneficiaryAccountType(reinitiateRequestDto.getBeneficiaryAccountType());
		reinitiateStoredProcedureRequestEntity.setBeneficiaryBankIfsc(reinitiateRequestDto.getBeneficiaryBankIfsc());
		reinitiateStoredProcedureRequestEntity.setBeneficiaryBankName(reinitiateRequestDto.getBeneficiaryBankName());
		reinitiateStoredProcedureRequestEntity.setBeneficiaryBranchIFSC(reinitiateRequestDto.getBeneficiaryBranchIFSC());
		reinitiateStoredProcedureRequestEntity.setBeneficiaryBranchName(reinitiateRequestDto.getBeneficiaryBranchName());
		reinitiateStoredProcedureRequestEntity.setBeneficiaryLei(reinitiateRequestDto.getBeneficiaryLei());
		reinitiateStoredProcedureRequestEntity.setBeneficiaryPaymentId(reinitiateRequestDto.getBeneficiaryPaymentId());
		reinitiateStoredProcedureRequestEntity.setCreatedOn(DateUtils.sysDate());
		reinitiateStoredProcedureRequestEntity.setEffectiveDateOfPayment(DateUtils.convertStringToDateYYYYMMDD(reinitiateRequestDto.getEffectiveDateOfPayment()));
		reinitiateStoredProcedureRequestEntity.setIban(reinitiateRequestDto.getIban());
		reinitiateStoredProcedureRequestEntity.setIcodeForBusinessSegment(reinitiateRequestDto.getIcodeForBusinessSegment());
		reinitiateStoredProcedureRequestEntity.setIcodeForBusinessType(reinitiateRequestDto.getIcodeForBusinessType());
		reinitiateStoredProcedureRequestEntity.setIcodeForInvestmentPortfolio(reinitiateRequestDto.getIcodeForInvestmentPortfolio());
		reinitiateStoredProcedureRequestEntity.setIcodeForLOB(reinitiateRequestDto.getIcodeForLOB());
		reinitiateStoredProcedureRequestEntity.setIcodeForParticipatingType(reinitiateRequestDto.getIcodeForParticipatingType());
		reinitiateStoredProcedureRequestEntity.setIcodeForProductLine(reinitiateRequestDto.getIcodeForProductLine());
		reinitiateStoredProcedureRequestEntity.setIcodeForVarient(reinitiateRequestDto.getIcodeForVarient());
		reinitiateStoredProcedureRequestEntity.setLob(reinitiateRequestDto.getLOB());
		reinitiateStoredProcedureRequestEntity.setMemberNumber(reinitiateRequestDto.getMemberNumber());
		reinitiateStoredProcedureRequestEntity.setMphCode(reinitiateRequestDto.getMPHCODE());
		reinitiateStoredProcedureRequestEntity.setNroAccount(reinitiateRequestDto.getNroAccount());
		reinitiateStoredProcedureRequestEntity.setOperatingUnit(reinitiateRequestDto.getOperatingUnit());
		reinitiateStoredProcedureRequestEntity.setOperatingUnitType(reinitiateRequestDto.getOperatingUnitType());
		reinitiateStoredProcedureRequestEntity.setPaymentCategory(reinitiateRequestDto.getPaymentCategory());
		reinitiateStoredProcedureRequestEntity.setPaymentIdOld(reinitiateRequestDto.getPaymentIdOld());
		reinitiateStoredProcedureRequestEntity.setPaymentMode(reinitiateRequestDto.getPaymentMode());
		reinitiateStoredProcedureRequestEntity.setPaymentSubCategory(reinitiateRequestDto.getPaymentSubCategory());
		reinitiateStoredProcedureRequestEntity.setPayoutSourceModule(reinitiateRequestDto.getPayoutSourceModule());
		reinitiateStoredProcedureRequestEntity.setPolicyNo(reinitiateRequestDto.getPolicyNo());
		reinitiateStoredProcedureRequestEntity.setPolicyNumber(reinitiateRequestDto.getPolicyNumber());
		reinitiateStoredProcedureRequestEntity.setProduct(reinitiateRequestDto.getProduct());
		reinitiateStoredProcedureRequestEntity.setProductCode(reinitiateRequestDto.getProductCode());
		reinitiateStoredProcedureRequestEntity.setProductVariant(reinitiateRequestDto.getProductVariant());
		reinitiateStoredProcedureRequestEntity.setRefNo(reinitiateRequestDto.getRefNo());
		reinitiateStoredProcedureRequestEntity.setRemarks(reinitiateRequestDto.getRemarks());
		reinitiateStoredProcedureRequestEntity.setSenderLei(reinitiateRequestDto.getSenderLei());
		reinitiateStoredProcedureRequestEntity.setTotalAmount(reinitiateRequestDto.getTotalAmount());
		reinitiateStoredProcedureRequestEntity.setUnitCode(reinitiateRequestDto.getUnitCode());
		reinitiateStoredProcedureRequestEntity.setVariantCode(reinitiateRequestDto.getVariantCode());	
		reinitiateStoredProcedureRequestEntity.setCreatedBy(reinitiateRequestDto.getCreatedBy());
		reinitiateStoredProcedureRequestRepository.save(reinitiateStoredProcedureRequestEntity);
		logger.info("PayoutNeftRejectServiceImpl:reinitiateStoredProcedure request Save End");
		}
		}


// NEFT APPROVE STARTS	
	@Override
	public ApiResponseDto<PayoutDto> sentToApprove(String payoutNo) {
		ApiResponseDto<PayoutDto> responseDto=new ApiResponseDto<>();
		try {
			logger.info("PayoutNeftRejectServiceImpl :: sentToApprove :: Start -- PayOut No: {},", payoutNo);
			if (payoutNo != null) {
				PayoutEntity payoutEntity = payoutRepository.findByPayoutNoAndIsActiveTrue(payoutNo);
				if (payoutEntity != null) {
					if (payoutEntity.getPayoutMbr() != null) {
						PayoutPayeeBankDetailsEntity updatePayoutPayeeBankEntity = updatePayoutPayeeBankEntity(payoutEntity.getPayoutMbr());
						if (updatePayoutPayeeBankEntity != null) {
							payoutPayeeBankDetailsRepository.save(updatePayoutPayeeBankEntity);
							
							payoutPayeeBankDetailsTempRepository.updateMasterBankId(updatePayoutPayeeBankEntity.getBankAccountId(),updatePayoutPayeeBankEntity.getTempBankAccountId());
							
							logger.info("PayoutNeftRejectServiceImpl :: sentToApprove :: Call Reinitiate Stored Procuce :: Starts -- PayOut No: {},",payoutNo + PayoutErrorConstants.APPROVED);
							String oldbenefiaryPaymentId = payoutPayeeBankDetailsRepository.getOldBenefiaryPaymentId(payoutNo);
							String benefiaryPaymentId = payoutPayeeBankDetailsRepository.getCurrentBenefiaryPaymentId(payoutNo);
							Long temppayoutId = tempPayoutRepository.getPayoutIdByPayoutNoAndIsActiveTrue(payoutNo);
							ReinitiateResponse reinitiateResponse = reinitiateStoredProcedure(temppayoutId, oldbenefiaryPaymentId, benefiaryPaymentId);
							
							logger.info("PayoutNeftRejectServiceImpl :: sentToApprove :: Call Reinitiate Stored Procuce :: Ends -- PayOut No: {},",payoutNo + PayoutErrorConstants.APPROVED);
							if(reinitiateResponse != null) {
							if(reinitiateResponse.getStatusCode() != null && reinitiateResponse.getStatusCode().equals("00")) {
								payoutRepository.updateStatus(NumericUtils.convertStringToInteger(PayoutConstants.NEFT_APPROVED), payoutNo);
								tempPayoutRepository.updateStatus(NumericUtils.convertStringToInteger(PayoutConstants.NEFT_APPROVED), payoutNo);
								logger.info("PayoutNeftRejectServiceImpl :: sentToApprove :: Ends -- PayOut No: {},",payoutNo + PayoutErrorConstants.APPROVED);
								responseDto= ApiResponseDto.success(null,"PayoutNo :" + payoutNo + " " + PayoutErrorConstants.APPROVED);
							}else {
								logger.info("PayoutNeftRejectServiceImpl :: sentToApprove :: Ends -- PayOut No: {},",payoutNo  + PayoutErrorConstants.STOREPRODUCERREJCTED);
								responseDto= ApiResponseDto.success(null,"PayoutNo :" + payoutNo + " " + PayoutErrorConstants.STOREPRODUCERREJCTED);
							}
							}else {
								logger.info("PayoutNeftRejectServiceImpl :: sentToApprove :: Ends -- PayOut No: {},",payoutNo  + PayoutErrorConstants.STOREPRODUCERREJCTED);
								responseDto= ApiResponseDto.success(null,"PayoutNo :" + payoutNo + " " + PayoutErrorConstants.STOREPRODUCERREJCTED);
							}
//						logger.info("PayoutNeftRejectServiceImpl :: sentToApprove :: Call Reinitiate Stored Procuce :: Ends -- PayOut No: {},",payoutNo + PayoutErrorConstants.APPROVED);
//						payoutRepository.updateStatus(NumericUtils.convertStringToInteger(PayoutConstants.NEFT_APPROVED), payoutNo);
//						tempPayoutRepository.updateStatus(NumericUtils.convertStringToInteger(PayoutConstants.NEFT_APPROVED), payoutNo);
//						return ApiResponseDto.success(null,"PayoutNo :" + payoutNo + " " + PayoutErrorConstants.APPROVED);
						} else {
							logger.info("PayoutNeftRejectServiceImpl :: sentToApprove :: Ends -- PayOut No: {},",payoutNo + PayoutErrorConstants.INVALID_PAYOUT_PAYEE_BANK);
							responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_PAYEE_BANK).build());
						}
					} else {
						logger.info("PayoutNeftRejectServiceImpl :: sentToApprove :: Ends -- PayOut No: {},",payoutNo + PayoutErrorConstants.INVALID_PAYOUTMEMBER);
						responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUTMEMBER).build());
					}
				} else {
					logger.info("PayoutNeftRejectServiceImpl :: sentToApprove :: Ends -- PayOut No: ",payoutNo + PayoutErrorConstants.INVALID_PAYOUT_STATUS);
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_STATUS).build());
				}
			} else {
				logger.info("PayoutNeftRejectServiceImpl :: sentToApprove :: Ends -- PayOut No: {},",payoutNo + PayoutErrorConstants.INVALID_PAYOUT_NO);
				responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseDto;
	}

	private PayoutPayeeBankDetailsEntity updatePayoutPayeeBankEntity(PayoutMbrEntity oldPayoutMbrEntity) {
		logger.info("PayoutNeftRejectServiceImpl :: updatePayoutPayeeBankEntity :: Start -- PayOut No: {},",
				oldPayoutMbrEntity.getPayoutNo());
		List<PayoutPayeeBankDetailsEntity> payoutPayeeBankDetailsEntities = oldPayoutMbrEntity.getPayoutPayeeBank();
		if (payoutPayeeBankDetailsEntities != null && !payoutPayeeBankDetailsEntities.isEmpty()) {
			for (PayoutPayeeBankDetailsEntity oldPayeeBankDetailsEntity : payoutPayeeBankDetailsEntities) {
				PayoutPayeeBankDetailsTempEntity oldPayeeTempBankDetailsEntity = payoutPayeeBankDetailsTempRepository
						.findByMasterBankAccountIdAndBenefiaryPaymentId(oldPayeeBankDetailsEntity.getBankAccountId(),
								oldPayeeBankDetailsEntity.getBenefiaryPaymentId());
				if (oldPayeeBankDetailsEntity.getBankAccountId()
						.equals(oldPayeeTempBankDetailsEntity.getMasterBankAccountId())
						&& oldPayeeBankDetailsEntity.getIsActive() == Boolean.TRUE) {
					oldPayeeBankDetailsEntity.setIsActive(Boolean.FALSE);
					payoutPayeeBankDetailsRepository.save(oldPayeeBankDetailsEntity);
					logger.info("PayoutNeftRejectServiceImpl :: updatePayoutPayeeBankEntity :: Ends -- PayOut No: {},",
							oldPayoutMbrEntity.getPayoutNo());
					return makeDuplicatePayoutPayeeBankDetailsEntity(oldPayeeTempBankDetailsEntity,
							oldPayeeBankDetailsEntity, oldPayoutMbrEntity);
				}
			}
		}
		logger.info("PayoutNeftRejectServiceImpl :: updatePayoutPayeeBankEntity :: Ends -- PayOut No: {},",
				oldPayoutMbrEntity.getPayoutNo());
		return null;
	}

	private PayoutPayeeBankDetailsEntity makeDuplicatePayoutPayeeBankDetailsEntity(
			PayoutPayeeBankDetailsTempEntity oldPayeeTempBankDetailsEntity,
			PayoutPayeeBankDetailsEntity oldPayeeBankDetailsEntity, PayoutMbrEntity oldPayoutMbrEntity) {
		logger.info(
				"PayoutNeftRejectServiceImpl :: makeDuplicatePayoutPayeeBankDetailsEntity :: Start -- PayOut No: {},",
				oldPayoutMbrEntity.getPayoutNo());
		PayoutPayeeBankDetailsEntity newPayeeBankDetailsEntity = new PayoutPayeeBankDetailsEntity();

		BeanUtils.copyProperties(oldPayeeTempBankDetailsEntity, newPayeeBankDetailsEntity);
		newPayeeBankDetailsEntity.setBankAccountId(null);
		newPayeeBankDetailsEntity.setPayoutMbrEntity(oldPayoutMbrEntity);

		Long tempBankAccountId = payoutPayeeBankDetailsTempRepository.findBankAccountIdByTypeandIsActive(
				newPayeeBankDetailsEntity.getType(), oldPayeeTempBankDetailsEntity.getPayoutMbrEntity().getMemberId());
		newPayeeBankDetailsEntity.setTempBankAccountId(NumericUtils.convertLongToInteger(tempBankAccountId));

		String benefiaryPaymentId = payoutPayeeBankDetailsTempRepository.findbenefiaryPaymentIdByTypeandIsActive(
				newPayeeBankDetailsEntity.getType(), oldPayeeTempBankDetailsEntity.getPayoutMbrEntity().getMemberId());
		newPayeeBankDetailsEntity.setBenefiaryPaymentId(benefiaryPaymentId);

//		newPayeeBankDetailsEntity.setTempBankAccountId(NumericUtils.convertLongToInteger(oldPayeeTempBankDetailsEntity.getBankAccountId()));
		newPayeeBankDetailsEntity.setIsActive(Boolean.TRUE);
//		newPayeeBankDetailsEntity.setBenefiaryPaymentId(oldPayeeTempBankDetailsEntity.getBenefiaryPaymentId());
		newPayeeBankDetailsEntity.setModifiedOn(new Date());
		;
		Integer versionNo = NumericUtils.convertStringToInteger(oldPayeeBankDetailsEntity.getVersionNo());
		newPayeeBankDetailsEntity.setVersionNo(
				NumericUtils.convertIntegerToString(versionNo != null && versionNo >= 1 ? versionNo + 1 : 1));
		logger.info(
				"PayoutNeftRejectServiceImpl :: makeDuplicatePayoutPayeeBankDetailsEntity :: Ends -- PayOut No: {},",
				oldPayoutMbrEntity.getPayoutNo());
		return newPayeeBankDetailsEntity;
	}

// NEFT APPROVE ENDS

// NEFT Inprogress search Starts	
	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> inprogress(PayoutSearchRequestDto request) {
		logger.info("PayoutNeftRejectServiceImpl :: inprogress :: Start ");
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = searchTemp(request);
		logger.info("PayoutNeftRejectServiceImpl :: inprogress :: Ends ");
		return responseDto;
	}

	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> inprogressByPayoutNo(String payoutNo) {
		logger.info("PayoutNeftRejectServiceImpl :: inprogressByPayoutNo :: Start ");
		PayoutSearchRequestDto request = new PayoutSearchRequestDto();
		request.setPayoutNo(payoutNo);
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = searchTemp(request);
		logger.info("PayoutNeftRejectServiceImpl :: inprogressByPayoutNo :: Ends ");
		return responseDto;
	}

	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> searchTemp(PayoutSearchRequestDto request) {
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto=new ApiResponseDto<>();
		try {
			logger.info("PayoutNeftRejectServiceImpl :: searchTemp :: Start ");
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<TempPayoutEntity> searchQuery = criteriaBuilder.createQuery(TempPayoutEntity.class);
			Root<TempPayoutEntity> root = searchQuery.from(TempPayoutEntity.class);
			Join<TempPayoutEntity, TempPayoutMbrEntity> join = root.join("payoutMbr");
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(request.getMasterPolicyNo())) {
				predicates.add(criteriaBuilder.equal(root.get("masterPolicyNo"), request.getMasterPolicyNo()));
			}
			if (StringUtils.isNotBlank(request.getMph())) {
				predicates.add(criteriaBuilder.equal(root.get("mphName"), request.getMph()));
			}
			if (StringUtils.isNotBlank(request.getPan())) {
				predicates.add(criteriaBuilder.equal(join.get("pan"), request.getPan()));
			}
			if (StringUtils.isNotBlank(request.getAadhar())) {
				predicates.add(criteriaBuilder.equal(join.get("aadharNumber"), request.getAadhar()));
			}

			if (StringUtils.isNotBlank(request.getPayoutNo())) {
				predicates.add(criteriaBuilder.equal(root.get("payoutNo"), request.getPayoutNo()));
			}

			if (StringUtils.isNotBlank(request.getClaimNo())) {
				predicates.add(criteriaBuilder.equal(root.get("claimNo"), request.getClaimNo()));
			}

			if (StringUtils.isNotBlank(request.getInitiMationNo())) {
				predicates.add(criteriaBuilder.like(root.get("initiMationNo"), "%" + request.getInitiMationNo()));
			}

			if (StringUtils.isNotBlank(request.getUnitCode())) {
				predicates.add(criteriaBuilder.equal(root.get("unitCode"), request.getUnitCode()));
			}

			if (request.getPayoutStatus() != null && !request.getPayoutStatus().isEmpty()) {
				Expression<String> statusExpression = root.get("status");
				predicates.add(statusExpression.in(request.getPayoutStatus()));
			}

			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));

			List<TempPayoutEntity> result = entityManager.createQuery(searchQuery).getResultList();
			if (result.isEmpty()) {
				logger.info("No Data Found For given Request ");
				logger.info("PayoutNeftRejectServiceImpl :: searchTemp :: Ends ");
				responseDto= ApiResponseDto.error(ErrorDto.builder().message("No Data Found ").build());
			}
			logger.info("PayoutSearchServiceImpl:search:Ends");
			List<PayoutSearchResponseDto> response = result.stream().map(this::convertTempEntityToDto)
					.collect(Collectors.toList());
			logger.info("PayoutNeftRejectServiceImpl :: searchTemp :: Ends ");
			responseDto= ApiResponseDto.success(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutNeftRejectServiceImpl :: searchTemp :: error is  "+e.getMessage());
		}
		logger.info("PayoutNeftRejectServiceImpl :: searchTemp :: Ends ");
		return responseDto;
	}

	private PayoutSearchResponseDto convertTempEntityToDto(TempPayoutEntity tempPayoutsEntity) {
		logger.info("PayoutNeftRejectServiceImpl :: convertTempEntityToDto :: Start -- PayOut No: {},",
				tempPayoutsEntity.getPayoutNo());
		PayoutSearchResponseDto response = new PayoutSearchResponseDto();
		response.setPayoutNo(tempPayoutsEntity.getPayoutNo());
		response.setInitiMationNo(tempPayoutsEntity.getInitiMationNo());
		response.setPayoutStatus(tempPayoutsEntity.getStatus());
		response.setMasterPolicyNo(tempPayoutsEntity.getMasterPolicyNo());
		response.setMphCode(tempPayoutsEntity.getMphCode());
		response.setMphName(tempPayoutsEntity.getMphName());
		response.setClaimNo(tempPayoutsEntity.getClaimNo());
		TempPayoutMbrEntity payoutMbr = tempPayoutsEntity.getPayoutMbr();
		if (payoutMbr != null) {
			response.setAadhar(payoutMbr.getAadharNumber());
			response.setFirstName(payoutMbr.getFirstName());
			response.setLastName(payoutMbr.getLastName());
			response.setMemberShipNo(payoutMbr.getTypeOfMembershipNo());
			response.setPan(payoutMbr.getPan());
			response.setLicId(payoutMbr.getLicId());
		}
		logger.info("PayoutNeftRejectServiceImpl :: convertTempEntityToDto :: Ends -- PayOut No: {},",
				tempPayoutsEntity.getPayoutNo());
		return response;
	}
// NEFT Inprogress search Ends

// NEFT Existing search Starts	

	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> existing(PayoutSearchRequestDto request) {
		logger.info("PayoutNeftRejectServiceImpl :: existing :: Start ");
		request.setPayoutStatus(Arrays.asList(PayoutStatus.NEFT_APPROVED.val(), PayoutStatus.NEFT_REJECT.val()));
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = searchMain(request);
		logger.info("PayoutNeftRejectServiceImpl :: existing :: Ends ");
		return responseDto;
	}

	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> existingByPayoutNo(String payoutNo) {
		logger.info("PayoutNeftRejectServiceImpl :: existingByPayoutNo :: Start ");
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = null;
		try {
			PayoutSearchRequestDto request = new PayoutSearchRequestDto();
			request.setPayoutStatus(Arrays.asList(PayoutStatus.NEFT_APPROVED.val(), PayoutStatus.NEFT_REJECT.val()));
			request.setPayoutNo(payoutNo);
			responseDto = searchMain(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutNeftRejectServiceImpl :: existingByPayoutNo :: error is  "+e.getMessage());
		}
		logger.info("PayoutNeftRejectServiceImpl :: existingByPayoutNo :: Ends ");
		return responseDto;
	}

	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> searchMain(PayoutSearchRequestDto request) {
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto=new ApiResponseDto<>();
		try {
			logger.info("PayoutNeftRejectServiceImpl :: searchMain :: Start");
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			List<Predicate> predicates = new ArrayList<>();

			CriteriaQuery<PayoutEntity> searchQuery = criteriaBuilder.createQuery(PayoutEntity.class);
			Root<PayoutEntity> root = searchQuery.from(PayoutEntity.class);
			Join<PayoutEntity, PayoutMbrEntity> join = root.join("payoutMbr");
			if (StringUtils.isNotBlank(request.getMasterPolicyNo())) {
				predicates.add(criteriaBuilder.equal(root.get("masterPolicyNo"), request.getMasterPolicyNo()));
			}
			if (StringUtils.isNotBlank(request.getMph())) {
				predicates.add(criteriaBuilder.equal(root.get("mphName"), request.getMph()));
			}
			if (StringUtils.isNotBlank(request.getPan())) {
				predicates.add(criteriaBuilder.equal(join.get("pan"), request.getPan()));
			}
			if (StringUtils.isNotBlank(request.getAadhar())) {
				predicates.add(criteriaBuilder.equal(join.get("aadharNumber"), request.getAadhar()));
			}
			if (StringUtils.isNotBlank(request.getPayoutNo())) {
				predicates.add(criteriaBuilder.like(root.get("payoutNo"), "%" + request.getPayoutNo()));
			}
			if (StringUtils.isNotBlank(request.getClaimNo())) {
				predicates.add(criteriaBuilder.equal(root.get("claimNo"), request.getClaimNo()));
			}
			if (StringUtils.isNotBlank(request.getMembershipNumber())) {
				predicates.add(criteriaBuilder.equal(join.get("membershipNumber"), request.getMembershipNumber()));
			}
			if (StringUtils.isNotBlank(request.getLicId())) {
				predicates.add(criteriaBuilder.equal(join.get("licId"), request.getLicId()));
			}
			if (StringUtils.isNotBlank(request.getDateOfBirth())) {
				predicates.add(criteriaBuilder.equal(join.get("dateOfBirth"), request.getDateOfBirth()));
			}
			if (StringUtils.isNotBlank(request.getPhone())) {
				predicates.add(criteriaBuilder.equal(join.get("phone"), request.getPhone()));
			}

			predicates.add(criteriaBuilder.equal(root.get("unitCode"), request.getUnitCode()));

			if (request.getPayoutStatus() != null && !request.getPayoutStatus().isEmpty()) {
				Expression<String> statusExpression = root.get("status");
				predicates.add(statusExpression.in(request.getPayoutStatus()));
			}

			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));

			List<PayoutEntity> result = entityManager.createQuery(searchQuery).getResultList();
			if (result.isEmpty()) {
				logger.info("No Data Found For given Request");
				logger.info("PayoutNeftRejectServiceImpl :: searchMain :: Ends");
				responseDto= ApiResponseDto.error(ErrorDto.builder().message("No Data Found").build());
			}
			List<PayoutSearchResponseDto> response = result.stream().map(this::convertMainEntityToDto)
					.collect(Collectors.toList());
			logger.info("PayoutNeftRejectServiceImpl :: searchMain :: Ends");
			responseDto= ApiResponseDto.success(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutNeftRejectServiceImpl :: searchMain :: error is "+e.getMessage());
		}
		logger.info("PayoutNeftRejectServiceImpl :: searchMain :: Ends");
		return responseDto;
	}

	private PayoutSearchResponseDto convertMainEntityToDto(PayoutEntity payoutsEntity) {
		logger.info("PayoutNeftRejectServiceImpl :: convertMainEntityToDto :: Start -- PayOut No: {},",
				payoutsEntity.getPayoutNo());
		PayoutSearchResponseDto response = new PayoutSearchResponseDto();
		response.setPayoutNo(payoutsEntity.getPayoutNo());
		response.setPayoutStatus(payoutsEntity.getStatus());
		response.setMasterPolicyNo(payoutsEntity.getMasterPolicyNo());
		response.setMphCode(payoutsEntity.getMphCode());
		response.setInitiMationNo(payoutsEntity.getInitiMationNo());
		response.setMphName(payoutsEntity.getMphName());
		response.setClaimNo(payoutsEntity.getClaimNo());
		response.setDtOfExit(DateUtils.dateToStringDDMMYYYY(payoutsEntity.getDtOfExit()));
		response.setModeOfExit(NumericUtils.convertIntegerToString(payoutsEntity.getModeOfExit()));
		response.setUnitCode(payoutsEntity.getUnitCode());
		PayoutMbrEntity payoutMbr = payoutsEntity.getPayoutMbr();
		if (payoutMbr != null) {
			response.setAadhar(payoutMbr.getAadharNumber());
			response.setFirstName(payoutMbr.getFirstName());
			response.setLastName(payoutMbr.getLastName());
			response.setMemberShipNo(payoutMbr.getTypeOfMembershipNo());
			response.setPan(payoutMbr.getPan());
			response.setLicId(payoutMbr.getLicId());
		}
		logger.info("PayoutNeftRejectServiceImpl :: convertMainEntityToDto :: Ends -- PayOut No: {},",
				payoutsEntity.getPayoutNo());
		return response;
	}
// NEFT Existing search Ends

// Sent To Checker Starts	
	@Override
	public ApiResponseDto<PayoutDto> sentToChecker(String payoutNo) {
		logger.info("PayoutNeftRejectServiceImpl :: sentToChecker :: Start -- PayOut No: {},", payoutNo);
		if (payoutNo != null) {
			TempPayoutEntity tempPayoutEntity = tempPayoutRepository.findByPayoutNoAndIsActiveTrue(payoutNo);
			if (Objects.equals(tempPayoutEntity.getStatus(),
					NumericUtils.convertStringToInteger(PayoutConstants.NEFT_DRAFT))
					|| Objects.equals(tempPayoutEntity.getStatus(),
							NumericUtils.convertStringToInteger(PayoutConstants.NEFT_ON_HOLD))) {
				tempPayoutRepository.updateStatus(
						NumericUtils.convertStringToInteger(PayoutConstants.NEFT_PENDING_FOR_APPROVAL), payoutNo);
				logger.info("PayoutNeftRejectServiceImpl :: sentToChecker :: Ends -- PayOut No:  {},",
						payoutNo + PayoutErrorConstants.SEND_TO_CHECKER);
				return ApiResponseDto.success(null,
						"PayoutNo :" + payoutNo + " " + PayoutErrorConstants.SEND_TO_CHECKER);
			} else {
				logger.info("PayoutNeftRejectServiceImpl :: sentToChecker :: Ends -- PayOut No: {},",
						payoutNo + PayoutErrorConstants.INVALID_PAYOUT_STATUS);
				return ApiResponseDto
						.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_STATUS).build());
			}
		} else {
			logger.info("PayoutNeftRejectServiceImpl :: sentToChecker :: Ends -- PayOut No: {},",
					payoutNo + PayoutErrorConstants.INVALID_PAYOUT_NO);
			return ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
		}
	}
// Sent To Checker Ends	

// Sent To Maker Starts	
	@Override
	public ApiResponseDto<PayoutDto> sentToMaker(String payoutNo) {
		logger.info("PayoutNeftRejectServiceImpl :: sentToMaker :: Start -- PayOut No: {},", payoutNo);
		if (payoutNo != null) {
			TempPayoutEntity tempPayoutEntity = tempPayoutRepository.findByPayoutNoAndIsActiveTrue(payoutNo);
			if (Objects.equals(tempPayoutEntity.getStatus(),
					NumericUtils.convertStringToInteger(PayoutConstants.NEFT_PENDING_FOR_APPROVAL))) {
				tempPayoutRepository.updateStatus(NumericUtils.convertStringToInteger(PayoutConstants.NEFT_ON_HOLD),
						payoutNo);
				logger.info("PayoutNeftRejectServiceImpl :: sentToMaker :: Ends -- PayOut No:  {},",
						payoutNo + PayoutErrorConstants.SEND_TO_MAKER);
				return ApiResponseDto.success(null,
						"Payout No :" + payoutNo + " " + PayoutErrorConstants.SEND_TO_MAKER);
			} else {
				logger.info("PayoutNeftRejectServiceImpl :: sentToMaker :: Ends -- PayOut No: {},",
						payoutNo + PayoutErrorConstants.INVALID_PAYOUT_STATUS);
				return ApiResponseDto
						.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_STATUS).build());
			}
		} else {
			logger.info("PayoutNeftRejectServiceImpl :: sentToMaker :: Ends -- PayOut No: {},",
					payoutNo + PayoutErrorConstants.INVALID_PAYOUT_NO);
			return ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
		}
	}
// Sent To Maker Ends	

// Sent To Reject Starts
	@Override
	public ApiResponseDto<PayoutDto> sentToReject(String payoutNo) {
		logger.info("PayoutNeftRejectServiceImpl :: sentToReject :: Start -- PayOut No: {},", payoutNo);
		if (payoutNo != null) {
			TempPayoutEntity tempPayoutEntity = tempPayoutRepository.findByPayoutNoAndIsActiveTrue(payoutNo);
			if (Objects.equals(tempPayoutEntity.getStatus(),
					NumericUtils.convertStringToInteger(PayoutConstants.NEFT_PENDING_FOR_APPROVAL))) {
				tempPayoutRepository.updateStatus(NumericUtils.convertStringToInteger(PayoutConstants.NEFT_REJECT),
						payoutNo);
				logger.info("PayoutNeftRejectServiceImpl :: sentToReject :: Ends --  PayOut No: {},",
						payoutNo + PayoutErrorConstants.REJECT);
				return ApiResponseDto.success(null, "Payout No :" + payoutNo + " " + PayoutErrorConstants.REJECT);
			} else {
				logger.info("PayoutNeftRejectServiceImpl :: sentToReject :: Ends -- PayOut No: {},",
						payoutNo + PayoutErrorConstants.INVALID_PAYOUT_STATUS);
				return ApiResponseDto
						.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_STATUS).build());
			}
		} else {
			logger.info("PayoutNeftRejectServiceImpl :: sentToReject :: Ends -- PayOut No: {},",
					payoutNo + PayoutErrorConstants.INVALID_PAYOUT_NO);
			return ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
		}
	}
// Sent To Reject Ends

// Maker Bank Edit	Starts

	@Override
	public ApiResponseDto<PayoutPayeeBankDetailsDto> editTempPayoutPayeeBankValueDetails(String payoutNo,
			PayoutPayeeBankDetailsTempEntity newPayeeTemp) {
		logger.info("PayoutNeftRejectServiceImpl :: editTempPayoutPayeeBankValueDetails :: Start -- PayOut No: {},",
				payoutNo);
		PayoutPayeeBankDetailsDto newPayeeBankDetailsDto = new PayoutPayeeBankDetailsDto();
		try {
			TempPayoutEntity tempPayoutEntity = tempPayoutRepository.findByPayoutNoAndIsActiveTrue(payoutNo);
			if (tempPayoutEntity != null) {
				PayoutPayeeBankDetailsTempEntity oldPayeeTemp = payoutPayeeBankDetailsTempRepository
						.findByBankAccountIdAndIsActiveTrue(newPayeeTemp.getBankAccountId());
				if (oldPayeeTemp != null) {
					PayoutPayeeBankDetailsTempEntity updatePayoutPayeeBankEntity = updatePayoutPayeeBankEntity(
							oldPayeeTemp.getPayoutMbrEntity(), newPayeeTemp);
					payoutPayeeBankDetailsTempRepository.save(updatePayoutPayeeBankEntity);
					BeanUtils.copyProperties(updatePayoutPayeeBankEntity, newPayeeBankDetailsDto);
					tempPayoutRepository.updateStatus(NumericUtils.convertStringToInteger(PayoutConstants.NEFT_DRAFT),
							payoutNo);
				} else {

					logger.info(
							"PayoutNeftRejectServiceImpl :: editTempPayoutPayeeBankValueDetails  :: Ends -- PayOut No: {},",
							payoutNo);
				}
			} else {
				logger.info(
						"PayoutNeftRejectServiceImpl :: editTempPayoutPayeeBankValueDetails :: Ends -- PayOut No: {},",
						payoutNo);
				return ApiResponseDto
						.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUTMASTER).build());
			}
		} catch (Exception e) {
			logger.error("Exception :: PayoutNeftRejectServiceImpl :: editTempPayoutPayeeBankValueDetails :: Ends -- ",
					e);
		}
		logger.info("PayoutNeftRejectServiceImpl :: editTempPayoutPayeeBankValueDetails :: Ends -- PayOut No: {},",
				payoutNo);
		return ApiResponseDto.success(newPayeeBankDetailsDto);
	}

	private PayoutPayeeBankDetailsTempEntity updatePayoutPayeeBankEntity(TempPayoutMbrEntity oldPayoutMbrEntity,
			PayoutPayeeBankDetailsTempEntity newPayeeTemp) {
		logger.info(
				"PayoutNeftRejectServiceImpl :: updatePayoutPayeeBankEntity :: Start -- PayOut No: {}, BankAccID : {} , MasterBankAccID :{}, ",
				oldPayoutMbrEntity.getPayoutNo(), newPayeeTemp.getBankAccountId(),
				newPayeeTemp.getMasterBankAccountId());
		List<PayoutPayeeBankDetailsTempEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity.getPayoutPayeeBank();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (PayoutPayeeBankDetailsTempEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				if (oldEntity != null && oldEntity.getBankAccountId().equals(newPayeeTemp.getBankAccountId())
						&& oldEntity.getIsActive() == Boolean.TRUE) {
					oldEntity.setIsActive(Boolean.FALSE);
					payoutPayeeBankDetailsTempRepository.save(oldEntity);
					newPayeeTemp = makeDuplicatePayoutPayeeBankDetailsTempEntity(newPayeeTemp, oldPayoutMbrEntity);
					logger.info(
							"PayoutNeftRejectServiceImpl :: updatePayoutPayeeBankEntity :: OLD BankAccID : {} , OLD MasterBankAccID :{}, ",
							oldEntity.getBankAccountId(), oldEntity.getMasterBankAccountId());
					logger.info(
							"PayoutNeftRejectServiceImpl :: updatePayoutPayeeBankEntity :: NEW BankAccID : {} , NEW MasterBankAccID :{}, ",
							newPayeeTemp.getBankAccountId(), newPayeeTemp.getMasterBankAccountId());
				}
			}
		}
		logger.info(
				"PayoutNeftRejectServiceImpl :: updatePayoutPayeeBankEntity :: Start -- PayOut No: {}, BankAccID : {} , MasterBankAccID :{}, ",
				oldPayoutMbrEntity.getPayoutNo(), newPayeeTemp.getBankAccountId(),
				newPayeeTemp.getMasterBankAccountId());
		return newPayeeTemp;
	}

	private PayoutPayeeBankDetailsTempEntity makeDuplicatePayoutPayeeBankDetailsTempEntity(
			PayoutPayeeBankDetailsTempEntity oldPayeeBankDetailsTempEntity, TempPayoutMbrEntity newPayoutMbrEntity) {
		logger.info(
				"PayoutNeftRejectServiceImpl :: makeDuplicatePayoutPayeeBankDetailsTempEntity :: Start -- PayOut No: {},",
				newPayoutMbrEntity.getPayoutNo());
		PayoutPayeeBankDetailsTempEntity newPayeeBankDetailsTempEntity = new PayoutPayeeBankDetailsTempEntity();
		BeanUtils.copyProperties(oldPayeeBankDetailsTempEntity, newPayeeBankDetailsTempEntity);
		newPayeeBankDetailsTempEntity.setBankAccountId(null);
		newPayeeBankDetailsTempEntity.setPayoutMbrEntity(newPayoutMbrEntity);
		newPayeeBankDetailsTempEntity.setIsActive(Boolean.TRUE);
		newPayeeBankDetailsTempEntity.setMasterBankAccountId(oldPayeeBankDetailsTempEntity.getMasterBankAccountId());
		String benefiaryPaymentId = getStoreProcedureSeq();
		if (benefiaryPaymentId != null) {
			newPayeeBankDetailsTempEntity.setBenefiaryPaymentId(benefiaryPaymentId);
		} else {
			newPayeeBankDetailsTempEntity.setBenefiaryPaymentId(null);
		}
		Integer versionNo = NumericUtils.convertStringToInteger(oldPayeeBankDetailsTempEntity.getVersionNo());
		newPayeeBankDetailsTempEntity.setVersionNo(
				NumericUtils.convertIntegerToString(versionNo != null && versionNo >= 1 ? versionNo + 1 : 1));
		logger.info(
				"PayoutNeftRejectServiceImpl :: makeDuplicatePayoutPayeeBankDetailsTempEntity :: Ends -- PayOut No: {},",
				newPayoutMbrEntity.getPayoutNo());
		return newPayeeBankDetailsTempEntity;
	}

// Maker Bank Edit Ends	

//NEFT NEW SEARCH STARTS

	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> existingfindPayoutNeftDetails(PayoutSearchRequestDto request) {
		logger.info("PayoutNeftRejectServiceImpl :: existingfindPayoutNeftDetails :: Start ");
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = null;
		try {
			request.setPayoutStatus(Arrays.asList(PayoutStatus.APPROVE.val(), PayoutStatus.REJECT.val()));
			responseDto = search(request);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutNeftRejectServiceImpl :: existingfindPayoutNeftDetails :: error is  "+e.getMessage());
		}
		logger.info("PayoutNeftRejectServiceImpl :: existingfindPayoutNeftDetails :: Ends ");
		return responseDto;
	}

	@Override
	public ApiResponseDto<List<PayoutSearchResponseDto>> search(PayoutSearchRequestDto request) {
		ApiResponseDto<List<PayoutSearchResponseDto>>  responseDto=new ApiResponseDto<>();
		try {
			logger.info("PayoutNeftRejectServiceImpl :: search :: Start -");
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			List<Predicate> predicates = new ArrayList<>();

			CriteriaQuery<PayoutEntity> searchQuery = criteriaBuilder.createQuery(PayoutEntity.class);
			Root<PayoutEntity> root = searchQuery.from(PayoutEntity.class);
			Join<PayoutEntity, PayoutMbrEntity> join = root.join("payoutMbr");
			if (StringUtils.isNotBlank(request.getMasterPolicyNo())) {
				predicates.add(criteriaBuilder.equal(root.get("masterPolicyNo"), request.getMasterPolicyNo()));
			}
			if (StringUtils.isNotBlank(request.getMph())) {
				predicates.add(criteriaBuilder.equal(root.get("mphName"), request.getMph()));
			}
			if (StringUtils.isNotBlank(request.getPan())) {
				predicates.add(criteriaBuilder.equal(join.get("pan"), request.getPan()));
			}
			if (StringUtils.isNotBlank(request.getAadhar())) {
				predicates.add(criteriaBuilder.equal(join.get("aadharNumber"), request.getAadhar()));
			}
			if (StringUtils.isNotBlank(request.getPayoutNo())) {
				predicates.add(criteriaBuilder.like(root.get("payoutNo"), "%" + request.getPayoutNo()));
			}
			if (StringUtils.isNotBlank(request.getClaimNo())) {
				predicates.add(criteriaBuilder.equal(root.get("claimNo"), request.getClaimNo()));
			}
			if (StringUtils.isNotBlank(request.getMembershipNumber())) {
				predicates.add(criteriaBuilder.equal(join.get("membershipNumber"), request.getMembershipNumber()));
			}
			if (StringUtils.isNotBlank(request.getLicId())) {
				predicates.add(criteriaBuilder.equal(join.get("licId"), request.getLicId()));
			}
			if (StringUtils.isNotBlank(request.getDateOfBirth())) {
				predicates.add(criteriaBuilder.equal(join.get("dateOfBirth"), request.getDateOfBirth()));
			}
			if (StringUtils.isNotBlank(request.getPhone())) {
				predicates.add(criteriaBuilder.equal(join.get("phone"), request.getPhone()));
			}
			predicates.add(criteriaBuilder.equal(root.get("unitCode"), request.getUnitCode()));
			if (request.getPayoutStatus() != null && !request.getPayoutStatus().isEmpty()) {
				Expression<String> statusExpression = root.get("status");
				predicates.add(statusExpression.in(request.getPayoutStatus()));
			}
			predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE));
			searchQuery.select(root).where(predicates.toArray(new Predicate[] {}));

			List<PayoutEntity> result = entityManager.createQuery(searchQuery).getResultList();
			if (result.isEmpty()) {
				logger.info("No Data Found For given Request");
				logger.info("PayoutNeftRejectServiceImpl :: search :: Ends --");
				responseDto= ApiResponseDto.error(ErrorDto.builder().message("No Data Found").build());
			}
			List<PayoutSearchResponseDto> response = setConvertEntityToDto(result);
			logger.info("PayoutNeftRejectServiceImpl :: search :: Ends ");
			responseDto= ApiResponseDto.success(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutNeftRejectServiceImpl :: search :: error is  "+e.getMessage());
		}
		logger.info("PayoutNeftRejectServiceImpl :: search :: Ends ");
		return responseDto;
	}

	private Boolean checkStroedProcedureResponse(String payoutNo) {
		logger.info("PayoutNeftRejectServiceImpl :: checkStroedProcedureResponse :: Start -- PayOut Id: {},", payoutNo);
		Boolean isSuccess = false;
		Optional<StoredProcedureResponseEntity> optional = storedProcedureResponseRepository.checkStatus(payoutNo);
//                                .findByPayoutIdAndUtrNoIsNotNull(payoutId);
		if (optional.isPresent()) {
			if (optional.isPresent()) {
				isSuccess = true;
				isSuccess = true;
			}
		}
		logger.info("PayoutNeftRejectServiceImpl :: checkStroedProcedureResponse :: Ends -- PayOut Id: {},", payoutNo);
		return isSuccess;
	}

	private List<PayoutSearchResponseDto> setConvertEntityToDto(List<PayoutEntity> result) {
		logger.info("PayoutNeftRejectServiceImpl :: setConvertEntityToDto :: Start ");
		List<PayoutSearchResponseDto> responseList = new ArrayList<>();
		for (PayoutEntity payoutsEntity : result) {

//                       Boolean trueOrFalse = checkStroedProcedureResponse(payoutsEntity.getPayoutId());
			Boolean trueOrFalse = checkStroedProcedureResponse(payoutsEntity.getPayoutNo());
//                        if (trueOrFalse != Boolean.TRUE) {
			if (trueOrFalse == Boolean.TRUE) {
				PayoutSearchResponseDto response = new PayoutSearchResponseDto();
				response.setPayoutNo(payoutsEntity.getPayoutNo());
				response.setPayoutNo(payoutsEntity.getPayoutNo());
				response.setPayoutStatus(payoutsEntity.getStatus());
				response.setPayoutStatus(payoutsEntity.getStatus());
				response.setMasterPolicyNo(payoutsEntity.getMasterPolicyNo());
				response.setMasterPolicyNo(payoutsEntity.getMasterPolicyNo());
				response.setMphCode(payoutsEntity.getMphCode());
				response.setMphCode(payoutsEntity.getMphCode());
				response.setInitiMationNo(payoutsEntity.getInitiMationNo());
				response.setInitiMationNo(payoutsEntity.getInitiMationNo());
				response.setMphName(payoutsEntity.getMphName());
				response.setMphName(payoutsEntity.getMphName());
				response.setClaimNo(payoutsEntity.getClaimNo());
				response.setClaimNo(payoutsEntity.getClaimNo());
				response.setDtOfExit(DateUtils.dateToStringDDMMYYYY(payoutsEntity.getDtOfExit()));
				response.setDtOfExit(DateUtils.dateToStringDDMMYYYY(payoutsEntity.getDtOfExit()));
				response.setModeOfExit(NumericUtils.convertIntegerToString(payoutsEntity.getModeOfExit()));
				response.setModeOfExit(NumericUtils.convertIntegerToString(payoutsEntity.getModeOfExit()));
				response.setUnitCode(payoutsEntity.getUnitCode());
				response.setUnitCode(payoutsEntity.getUnitCode());
				PayoutMbrEntity payoutMbr = payoutsEntity.getPayoutMbr();
				if (payoutMbr != null) {
					response.setAadhar(payoutMbr.getAadharNumber());
					response.setAadhar(payoutMbr.getAadharNumber());
					response.setFirstName(payoutMbr.getFirstName());
					response.setFirstName(payoutMbr.getFirstName());
					response.setLastName(payoutMbr.getLastName());
					response.setLastName(payoutMbr.getLastName());
					response.setMemberShipNo(payoutMbr.getTypeOfMembershipNo());
					response.setMemberShipNo(payoutMbr.getTypeOfMembershipNo());
					response.setPan(payoutMbr.getPan());
					response.setPan(payoutMbr.getPan());
					response.setLicId(payoutMbr.getLicId());
					response.setLicId(payoutMbr.getLicId());
				}
				responseList.add(response);
			}
		}
		logger.info("PayoutNeftRejectServiceImpl :: setConvertEntityToDto :: Ends ");
		return responseList;
	}

//NEFT NEW SEARCH ENDS	

// HYPER LINK BY PAYOUT STARTS	

	@Override
	@Transactional
	public ApiResponseDto<PayoutDto> findTempPayoutDetails(String payoutNo) {
		ApiResponseDto<PayoutDto> responseDto=new ApiResponseDto<>();
		try {
			logger.info("PayoutNeftRejectServiceImpl :: findTempPayoutDetails :: Start -- PayOut No: {},", payoutNo);
			if (payoutNo != null && !payoutNo.equalsIgnoreCase("")) {
				Optional<TempPayoutEntity> result = tempPayoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
				if (result.isPresent()) {
					TempPayoutEntity payoutEntity = result.get();
					logger.info("PayoutNeftRejectServiceImpl :: findTempPayoutDetails :: Ends - PayOut No: {},", payoutNo);
					responseDto= ApiResponseDto.success(tempPayoutDto(payoutEntity));
				} else {
					logger.info("PayoutNeftRejectServiceImpl :: findTempPayoutDetails :: Ends -- PayOut No: {},", payoutNo);
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
				}
			} else {
				logger.info("PayoutNeftRejectServiceImpl :: findTempPayoutDetails :: Ends --- PayOut No: {},", payoutNo);
				responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutNeftRejectServiceImpl :: findTempPayoutDetails :: error is  -- PayOut No: {},", payoutNo+""+e.getMessage());
		}
		logger.info("PayoutNeftRejectServiceImpl :: findTempPayoutDetails :: ended -- PayOut No: {},", payoutNo);
		return responseDto;
	}

	public PayoutDto tempPayoutDto(TempPayoutEntity from) {
		logger.info("PayoutNeftRejectServiceImpl :: tempPayoutDto :: Start -- PayOut No: {},", from.getPayoutNo());
		String claimOBNo = claimRepository.fetchByClaimNoAndIsActive(from.getClaimNo(), Boolean.TRUE);

		PayoutDto to = new PayoutDto();
		to.setDtOfExit(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDtOfExit()));
		BeanUtils.copyProperties(from, to);
		to.setPayoutDocDetails(tempPayoutDocDetails(from));
		to.setPayoutMbr(tempPayoutMbr(from));
		to.setPayoutNotes(tempPayoutNotes(from));

		Double commutatiomSum = 0d;
		Double annuitySum = 0d;
		if (!to.getPayoutMbr().getPayoutCommutationCalc().isEmpty()) {
			List<Double> commutaionSR = to.getPayoutMbr().getPayoutAnuityCalc().stream()
					.map(PayoutAnnuityCalcDto::getShortReserve).collect(Collectors.toList());
			commutatiomSum = commutaionSR.stream().collect(Collectors.summingDouble(Double::doubleValue));
		}
		if (!to.getPayoutMbr().getPayoutAnuityCalc().isEmpty()) {
			List<Double> annuitySR = to.getPayoutMbr().getPayoutAnuityCalc().stream()
					.map(PayoutAnnuityCalcDto::getShortReserve).collect(Collectors.toList());
			annuitySum = annuitySR.stream().collect(Collectors.summingDouble(Double::doubleValue));
		}
		to.setTotalShortReserve(commutatiomSum + annuitySum);
		to.setClaimOnBoadingNumber(claimOBNo);
		logger.info("PayoutNeftRejectServiceImpl :: tempPayoutDto :: Ends -- PayOut No: {},", from.getPayoutNo());
		return to;
	}

	public List<PayoutDocumentDetailDto> tempPayoutDocDetails(TempPayoutEntity payoutEntity) {
		logger.info("PayoutNeftRejectServiceImpl :: tempPayoutDocDetails :: Start -- PayOut No: {},",
				payoutEntity.getPayoutNo());
		List<PayoutDocumentDetailDto> tos = new ArrayList<>();
		payoutEntity.getPayoutDocDetails().forEach(from -> {
			PayoutDocumentDetailDto to = new PayoutDocumentDetailDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		logger.info("PayoutNeftRejectServiceImpl :: tempPayoutDocDetails :: Ends -- PayOut No: {},",
				payoutEntity.getPayoutNo());
		return tos;
	}

	public PayoutMbrDto tempPayoutMbr(TempPayoutEntity payoutEntity) {
		logger.info("PayoutNeftRejectServiceImpl :: tempPayoutMbr :: Start -- PayOut No: {},",
				payoutEntity.getPayoutNo());
		TempPayoutMbrEntity from = payoutEntity.getPayoutMbr();
		PayoutMbrDto to = new PayoutMbrDto();
		to.setDateOfBirth(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDateOfBirth()));
		to.setDateOfJoining(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDateOfJoining()));
		to.setDateOfRetirement(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDateOfRetirement()));
		BeanUtils.copyProperties(from, to);
		to.setPayoutAnuityCalc(tempPayoutAnuityCalc(from.getPayoutAnuityCalc()));
		to.setPayoutCommutationCalc(tempPayoutCommutationCalc(from.getPayoutCommutationCalc()));
		to.setPayoutFundValue(tempPayoutFundValue(from.getPayoutFundValue()));
		to.setPayoutMbrAddresses(tempPayoutMbrAddresses(from.getPayoutMbrAddresses()));
		to.setPayoutMbrAppointeeDtls(tempPayoutMbrAppointeeDtls(from.getPayoutMbrAppointeeDtls()));
		to.setPayoutMbrBankDetails(tempPayoutMbrBankDetails(from.getPayoutMbrBankDetails()));
		to.setPayoutMbrFundValue(tempPayoutMbrFundValue(from.getPayoutMbrFundValue()));
		to.setPayoutMbrNomineeDtls(tempPayoutMbrNomineeDtls(from.getPayoutMbrNomineeDtls()));
		to.setPayoutPayeeBank(tempPayoutPayeeBank(from.getPayoutPayeeBank()));
		logger.info("PayoutNeftRejectServiceImpl :: tempPayoutMbr :: Ends -- PayOut No: {},",
				payoutEntity.getPayoutNo());
		return to;
	}

	public List<PayoutAnnuityCalcDto> tempPayoutAnuityCalc(List<TempPayoutAnnuityCalcEntity> froms) {
		List<PayoutAnnuityCalcDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutAnnuityCalcDto to = new PayoutAnnuityCalcDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutCommutationCalcDto> tempPayoutCommutationCalc(List<TempPayoutCommutationCalcEntity> froms) {
		List<PayoutCommutationCalcDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutCommutationCalcDto to = new PayoutCommutationCalcDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutFundValueDto> tempPayoutFundValue(List<TempPayoutFundValueEntity> froms) {
		List<PayoutFundValueDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutFundValueDto to = new PayoutFundValueDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutMbrAddressDto> tempPayoutMbrAddresses(List<TempPayoutMbrAddressEntity> froms) {
		List<PayoutMbrAddressDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrAddressDto to = new PayoutMbrAddressDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutMbrAppointeeDto> tempPayoutMbrAppointeeDtls(List<TempPayoutMbrAppointeeEntity> froms) {
		List<PayoutMbrAppointeeDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrAppointeeDto to = new PayoutMbrAppointeeDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutMbrBankDetailDto> tempPayoutMbrBankDetails(List<TempPayoutMbrBankDetailEntity> froms) {
		List<PayoutMbrBankDetailDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrBankDetailDto to = new PayoutMbrBankDetailDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutMbrFundValueDto> tempPayoutMbrFundValue(List<TempPayoutMbrFundValueEntity> froms) {
		List<PayoutMbrFundValueDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrFundValueDto to = new PayoutMbrFundValueDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutMbrNomineeDto> tempPayoutMbrNomineeDtls(List<TempPayoutMbrNomineeEntity> froms) {
		List<PayoutMbrNomineeDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrNomineeDto to = new PayoutMbrNomineeDto();
			BeanUtils.copyProperties(from, to);
			to.setDob(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDob()));
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutPayeeBankDetailsDto> tempPayoutPayeeBank(List<PayoutPayeeBankDetailsTempEntity> froms) {
		List<PayoutPayeeBankDetailsDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			if (from.getIsActive() == Boolean.TRUE) {
				PayoutPayeeBankDetailsDto to = new PayoutPayeeBankDetailsDto();
				BeanUtils.copyProperties(from, to);
				to.setBankAddressOne(from.getBankAddressOne());
				tos.add(to);
			}
		});
		return tos;
	}

	public List<PayoutNotesDto> tempPayoutNotes(TempPayoutEntity payoutEntity) {
		List<TempPayoutNotesEntity> froms = payoutEntity.getPayoutNotes();
		List<PayoutNotesDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutNotesDto to = new PayoutNotesDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

// HYPER LINK BY PAYOUT TMEP ENDS

// HYPER LINK BY PAYOUT STARTS

	@Override
	@Transactional
	public ApiResponseDto<PayoutDto> findPayoutDetails(String payoutNo) {
		ApiResponseDto<PayoutDto> responseDto=new ApiResponseDto<>();
		try {
			logger.info("PayoutNeftRejectServiceImpl :: findPayoutDetails :: Start -- PayOut No: {},", payoutNo);
			if (payoutNo != null && !payoutNo.equalsIgnoreCase("")) {
				Optional<PayoutEntity> result = payoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
				if (result.isPresent()) {
					PayoutEntity payoutEntity = result.get();
					logger.info("PayoutNeftRejectServiceImpl :: findPayoutDetails :: Ends - PayOut No: {},", payoutNo);
					responseDto= ApiResponseDto.success(payoutDto(payoutEntity));
				} else {
					logger.info("PayoutNeftRejectServiceImpl :: findPayoutDetails :: Ends -- PayOut No: {},",
							payoutNo + PayoutErrorConstants.INVALID_PAYOUT_NO);
					responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
				}
			} else {
				logger.info("PayoutNeftRejectServiceImpl :: findPayoutDetails :: Ends --- PayOut No: {},",
						payoutNo + PayoutErrorConstants.INVALID_PAYOUT_NO);
				responseDto= ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutNeftRejectServiceImpl :: findPayoutDetails :: error is "+e.getMessage()+"-- PayOut No: {},", payoutNo);
			
		}
		logger.info("PayoutNeftRejectServiceImpl :: findPayoutDetails :: ended -- PayOut No: {},", payoutNo);
		return responseDto;
	}

	public PayoutDto payoutDto(PayoutEntity from) {
		logger.info("PayoutNeftRejectServiceImpl :: payoutDto :: Start -- PayOut No: {},", from.getPayoutNo());
		String claimOBNo = claimRepository.fetchByClaimNoAndIsActive(from.getClaimNo(), Boolean.TRUE);

		PayoutDto to = new PayoutDto();
		to.setDtOfExit(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDtOfExit()));
		BeanUtils.copyProperties(from, to);
		to.setPayoutDocDetails(payoutDocDetails(from));
		to.setPayoutMbr(payoutMbr(from));
		to.setPayoutNotes(payoutNotes(from));

		Double commutatiomSum = 0d;
		Double annuitySum = 0d;
		if (!to.getPayoutMbr().getPayoutCommutationCalc().isEmpty()) {
			List<Double> commutaionSR = to.getPayoutMbr().getPayoutAnuityCalc().stream()
					.map(PayoutAnnuityCalcDto::getShortReserve).collect(Collectors.toList());
			commutatiomSum = commutaionSR.stream().collect(Collectors.summingDouble(Double::doubleValue));
		}
		if (!to.getPayoutMbr().getPayoutAnuityCalc().isEmpty()) {
			List<Double> annuitySR = to.getPayoutMbr().getPayoutAnuityCalc().stream()
					.map(PayoutAnnuityCalcDto::getShortReserve).collect(Collectors.toList());
			annuitySum = annuitySR.stream().collect(Collectors.summingDouble(Double::doubleValue));
		}
		to.setTotalShortReserve(commutatiomSum + annuitySum);
		to.setClaimOnBoadingNumber(claimOBNo);
		logger.info("PayoutNeftRejectServiceImpl :: payoutDto :: Ends -- PayOut No: {},", from.getPayoutNo());
		return to;
	}

	public List<PayoutDocumentDetailDto> payoutDocDetails(PayoutEntity payoutEntity) {
		logger.info("PayoutNeftRejectServiceImpl :: payoutDocDetails :: Start -- PayOut No: {},",
				payoutEntity.getPayoutNo());
		List<PayoutDocumentDetailDto> tos = new ArrayList<>();
		payoutEntity.getPayoutDocDetails().forEach(from -> {
			PayoutDocumentDetailDto to = new PayoutDocumentDetailDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		logger.info("PayoutNeftRejectServiceImpl :: payoutDocDetails :: Ends -- PayOut No: {},",
				payoutEntity.getPayoutNo());
		return tos;
	}

	public PayoutMbrDto payoutMbr(PayoutEntity payoutEntity) {
		logger.info("PayoutNeftRejectServiceImpl :: payoutMbr :: Start -- PayOut No: {},", payoutEntity.getPayoutNo());
		PayoutMbrEntity from = payoutEntity.getPayoutMbr();
		PayoutMbrDto to = new PayoutMbrDto();
		to.setDateOfBirth(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDateOfBirth()));
		to.setDateOfJoining(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDateOfJoining()));
		to.setDateOfRetirement(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDateOfRetirement()));
		BeanUtils.copyProperties(from, to);
		to.setPayoutAnuityCalc(payoutAnuityCalc(from.getPayoutAnuityCalc()));
		to.setPayoutCommutationCalc(payoutCommutationCalc(from.getPayoutCommutationCalc()));
		to.setPayoutFundValue(payoutFundValue(from.getPayoutFundValue()));
		to.setPayoutMbrAddresses(payoutMbrAddresses(from.getPayoutMbrAddresses()));
		to.setPayoutMbrAppointeeDtls(payoutMbrAppointeeDtls(from.getPayoutMbrAppointeeDtls()));
		to.setPayoutMbrBankDetails(payoutMbrBankDetails(from.getPayoutMbrBankDetails()));
		to.setPayoutMbrFundValue(payoutMbrFundValue(from.getPayoutMbrFundValue()));
		to.setPayoutMbrNomineeDtls(payoutMbrNomineeDtls(from.getPayoutMbrNomineeDtls()));
		to.setPayoutPayeeBank(payoutPayeeBank(from.getPayoutPayeeBank()));
		logger.info("PayoutNeftRejectServiceImpl :: payoutMbr :: Ends -- PayOut No: {},", payoutEntity.getPayoutNo());
		return to;
	}

	public List<PayoutAnnuityCalcDto> payoutAnuityCalc(List<PayoutAnnuityCalcEntity> froms) {
		List<PayoutAnnuityCalcDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutAnnuityCalcDto to = new PayoutAnnuityCalcDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutCommutationCalcDto> payoutCommutationCalc(List<PayoutCommutationCalcEntity> froms) {
		List<PayoutCommutationCalcDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutCommutationCalcDto to = new PayoutCommutationCalcDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutFundValueDto> payoutFundValue(List<PayoutFundValueEntity> froms) {
		List<PayoutFundValueDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutFundValueDto to = new PayoutFundValueDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutMbrAddressDto> payoutMbrAddresses(List<PayoutMbrAddressEntity> froms) {
		List<PayoutMbrAddressDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrAddressDto to = new PayoutMbrAddressDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutMbrAppointeeDto> payoutMbrAppointeeDtls(List<PayoutMbrAppointeeEntity> froms) {
		List<PayoutMbrAppointeeDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrAppointeeDto to = new PayoutMbrAppointeeDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutMbrBankDetailDto> payoutMbrBankDetails(List<PayoutMbrBankDetailEntity> froms) {
		List<PayoutMbrBankDetailDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrBankDetailDto to = new PayoutMbrBankDetailDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutMbrFundValueDto> payoutMbrFundValue(List<PayoutMbrFundValueEntity> froms) {
		List<PayoutMbrFundValueDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrFundValueDto to = new PayoutMbrFundValueDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutMbrNomineeDto> payoutMbrNomineeDtls(List<PayoutMbrNomineeEntity> froms) {
		List<PayoutMbrNomineeDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutMbrNomineeDto to = new PayoutMbrNomineeDto();
			BeanUtils.copyProperties(from, to);
			to.setDob(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDob()));
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutPayeeBankDetailsDto> payoutPayeeBank(List<PayoutPayeeBankDetailsEntity> froms) {
		List<PayoutPayeeBankDetailsDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutPayeeBankDetailsDto to = new PayoutPayeeBankDetailsDto();
			BeanUtils.copyProperties(from, to);
			to.setBankAddressOne(from.getBankAddressOne());
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutNotesDto> payoutNotes(PayoutEntity payoutEntity) {
		List<PayoutNotesEntity> froms = payoutEntity.getPayoutNotes();
		List<PayoutNotesDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutNotesDto to = new PayoutNotesDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

// HYPER LINK BY PAYOUT ENDS

}