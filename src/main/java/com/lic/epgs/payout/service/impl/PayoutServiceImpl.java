package com.lic.epgs.payout.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.shaded.org.eclipse.jetty.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.claim.constants.ClaimConstants;
import com.lic.epgs.claim.constants.ClaimErrorConstants;
import com.lic.epgs.claim.dto.ClaimCheckerActionRequestDto;
import com.lic.epgs.claim.entity.ClaimEntity;
import com.lic.epgs.claim.repository.ClaimRepository;
import com.lic.epgs.claim.service.ClaimService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.common.entity.IcodeMasterEntity;
import com.lic.epgs.common.entity.RollbackEntity;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.common.integration.dto.AnnuityModeResponse;
import com.lic.epgs.common.integration.dto.AnnuityOption;
import com.lic.epgs.common.integration.dto.AnnuityOptionalResponse;
import com.lic.epgs.common.integration.dto.AnnuityRealTimeData;
import com.lic.epgs.common.integration.dto.AnnuityRealTimeResponse;
import com.lic.epgs.common.integration.dto.Annuitymode;
import com.lic.epgs.common.integration.dto.StateDetailsDto;
import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.common.repository.CommonAccountTypeRepository;
import com.lic.epgs.common.repository.IcodeMasterRepository;
import com.lic.epgs.common.repository.RollbackRepository;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.fund.service.FundRestApiService;
import com.lic.epgs.gst.dto.GstCalculationRequest;
import com.lic.epgs.gst.dto.GstCalculationResponse;
import com.lic.epgs.gst.service.GstCalculatorService;
import com.lic.epgs.integration.dto.InterestRateResponseDto;
import com.lic.epgs.integration.dto.ResponseDto;
import com.lic.epgs.integration.service.AccountingIntegrationService;
import com.lic.epgs.payout.constants.PayoutConstants;
import com.lic.epgs.payout.constants.PayoutEntityConstants;
import com.lic.epgs.payout.constants.PayoutErrorConstants;
import com.lic.epgs.payout.constants.PayoutStatus;
import com.lic.epgs.payout.constants.PayoutStoredProcedureConstants;
import com.lic.epgs.payout.dto.AnnuityLedgerDto;
import com.lic.epgs.payout.dto.PayoutAnnuityCalcDto;
import com.lic.epgs.payout.dto.PayoutCheckerActionRequestDto;
import com.lic.epgs.payout.dto.PayoutCommutationCalcDto;
import com.lic.epgs.payout.dto.PayoutDocumentDetailDto;
import com.lic.epgs.payout.dto.PayoutDto;
import com.lic.epgs.payout.dto.PayoutFundValueDto;
import com.lic.epgs.payout.dto.PayoutMakerActionRequestDto;
import com.lic.epgs.payout.dto.PayoutMbrAddressDto;
import com.lic.epgs.payout.dto.PayoutMbrAppointeeDto;
import com.lic.epgs.payout.dto.PayoutMbrBankDetailDto;
import com.lic.epgs.payout.dto.PayoutMbrDto;
import com.lic.epgs.payout.dto.PayoutMbrFundValueDto;
import com.lic.epgs.payout.dto.PayoutMbrNomineeDto;
import com.lic.epgs.payout.dto.PayoutNotesDto;
import com.lic.epgs.payout.dto.PayoutPayeeBankDetailsDto;
import com.lic.epgs.payout.dto.PayoutStoredProcedureDto;
import com.lic.epgs.payout.dto.PayoutStoredProcedureResponseDto;
import com.lic.epgs.payout.entity.AnnuityCreationRequestResponseStoreEntity;
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
import com.lic.epgs.payout.entity.StoredProcedureResponseEntity;
import com.lic.epgs.payout.repository.AnnuityCreationReqAndResRepository;
import com.lic.epgs.payout.repository.PayoutAnnuityCalcRepository;
import com.lic.epgs.payout.repository.PayoutMbrRepository;
import com.lic.epgs.payout.repository.PayoutPayeeBankDetailsRepository;
import com.lic.epgs.payout.repository.PayoutRepository;
import com.lic.epgs.payout.repository.StoredProcedureResponseRepository;
import com.lic.epgs.payout.restapi.dto.PayoutAddressRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutAnnuityRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutAnnuityRestApiRes;
import com.lic.epgs.payout.restapi.dto.PayoutBankRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutFinancialDetailRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutJointDetailRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutMbrPrslDtRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutNomineeDtlRestApiRequest;
import com.lic.epgs.payout.service.PayoutService;
import com.lic.epgs.payout.service.SavePayoutService;
import com.lic.epgs.payout.temp.entity.PayoutPayeeBankDetailsTempEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutAnnuityCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutCommutationCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutFundValueEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrAddressEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrNomineeEntity;
import com.lic.epgs.payout.temp.repository.PayoutPayeeBankDetailsTempRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrNomineeRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutRepository;
import com.lic.epgs.payout.temp.service.TempPayoutService;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.MphAddressRepository;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

@Service
@Transactional
public class PayoutServiceImpl implements PayoutService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	PayoutRepository payoutRepository;

	@Autowired
	TempPayoutService tempPayoutService;

	@Autowired
	SavePayoutService savePayoutService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private Environment environment;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	TempPayoutRepository tempPayoutRepository;

	@Autowired
	IntegrationService integrationService;

	@Autowired
	private MemberMasterRepository memberMasterRepository;

	@Autowired
	private FundRestApiService fundRestApiService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	@Qualifier(value = "restTemplateService")
	private RestTemplate restTemplateService;

	@Autowired
	PayoutMbrRepository payoutMbrRepository;

	@Autowired
	StoredProcedureResponseRepository storedProcedureResponseRepository;

	@Autowired
	private AnnuityCreationReqAndResRepository annuityCreationReqAndResRepository;

	@Autowired
	IcodeMasterRepository icodeMasterRepository;

	@Autowired
	MphAddressRepository mphAddressRepository;

	@Autowired
	CommonAccountTypeRepository commonAccountTypeRepository;
	@Autowired
	ClaimRepository claimRepository;

	@Autowired
	AccountingIntegrationService accountingIntegrationService;

	@Autowired
	PayoutAnnuityCalcRepository payoutAnnuityCalcRepository;

	@Autowired
	PayoutPayeeBankDetailsTempRepository payeeBankDetailsTempRepository;

	@Autowired
	PayoutPayeeBankDetailsRepository payeeBankDetailsRepository;

	@Autowired
	GstCalculatorService gstCalculatorService;

	@Autowired
	TempPayoutMbrNomineeRepository tempPayoutMbrNomineeRepository;

	@Autowired
	ClaimService claimService;

	@Autowired
	private RollbackRepository rollbackRepository;

	public synchronized String getStoreProcedureSeq() {
		return commonService.getSequence(PayoutStoredProcedureConstants.BENEFIARY_PAYMENT_ID);
	}

	public synchronized String getStoreProcedureSeqAnnuityChallanNo() {
		return commonService.getSequence(PayoutStoredProcedureConstants.ANNUITY_CHALLAN_NO);
	}

	private Specification<PayoutEntity> findByPayoutNo(String payoutNo) {
		return (root, query, criteriaBuilder) -> {
			criteriaBuilder.and(criteriaBuilder.equal(root.get(PayoutEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(root.get(PayoutEntityConstants.PAYOUT_NO), payoutNo);
		};
	}

	@Override
	public ApiResponseDto<String> updateMakerAction(PayoutMakerActionRequestDto request) {
		ApiResponseDto<String> responseDto = new ApiResponseDto<>();
		logger.info("PayoutServiceImpl :: findTempPayoutDetails :: Start ");
		try {
			if (request.getInitiMationNo() != null) {

				Optional<PayoutEntity> optional = payoutRepository
						.findByInitiMationNoAndIsActive(request.getInitiMationNo(), Boolean.TRUE);
				if (optional.isPresent()) {
					responseDto = ApiResponseDto
							.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_STATUS).build());
				} else {
					responseDto = tempPayoutService.updateMakerAction(request);
				}

			} else {
				responseDto = ApiResponseDto
						.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutServiceImpl :: findTempPayoutDetails :: error is  " + e.getMessage());
		}
		logger.info("PayoutServiceImpl :: findTempPayoutDetails :: ended ");
		return responseDto;

	}

	@Override
	@Transactional
	public ApiResponseDto<String> updateCheckerAction(PayoutCheckerActionRequestDto request) {
		ApiResponseDto<String> responseDto = new ApiResponseDto<>();
		logger.info("PayoutServiceImpl :: updateCheckerAction :: start ");
		try {
			if (request.getInitiMationNo() != null) {

				Optional<PayoutEntity> optional = payoutRepository
						.findByInitiMationNoAndIsActive(request.getInitiMationNo(), Boolean.TRUE);
				if (optional.isPresent()) {
					responseDto = ApiResponseDto
							.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_STATUS).build());
				} else {
					if (request.getAction().equals(PayoutStatus.APPROVE.val())
							|| request.getAction().equals(PayoutStatus.REJECT.val())) {
						tempPayoutService.updateCheckerAction(request);
						PayoutEntity payoutEntity = savePayoutService.insert(request.getInitiMationNo());
						PayoutAnnuityRestApiRes payoutAnnuityRestApiRes = new PayoutAnnuityRestApiRes();
						/*
						 * if (request.getAction() == 7) { if (payoutEntity.getModeOfExit() == 1) {
						 * payoutEntity.getPayoutMbr().getPayoutMbrNomineeDtls().forEach(
						 * payoutMbrNomineeDtls -> { PayoutEntity payoutEntityForNominee = payoutEntity;
						 * payoutEntityForNominee.getPayoutMbr().setPayoutMbrNomineeDtls(null);
						 * payoutEntityForNominee.getPayoutMbr().setPayoutAnuityCalc(null);
						 * 
						 * List<PayoutMbrNomineeEntity> nomineeList = new ArrayList<>();
						 * List<PayoutAnnuityCalcEntity> anuityCalcList = new ArrayList<>();
						 * List<PayoutPayeeBankDetailsEntity> nomineeBankList = new ArrayList<>(); if
						 * (payoutMbrNomineeDtls.getNomineeCode() != null &&
						 * payoutMbrNomineeDtls.getClaimantType().equalsIgnoreCase("Nominee")) {
						 * 
						 * nomineeList.add(payoutMbrNomineeDtls);
						 * payoutEntityForNominee.getPayoutMbr().setPayoutMbrNomineeDtls(nomineeList);
						 * 
						 * payoutEntity.getPayoutMbr().getPayoutAnuityCalc().forEach(payoutAnuityCalc ->
						 * {
						 * 
						 * if (payoutMbrNomineeDtls.getNomineeCode()
						 * .equals(payoutAnuityCalc.getNomineeCode())) {
						 * anuityCalcList.add(payoutAnuityCalc); payoutEntityForNominee.getPayoutMbr()
						 * .setPayoutAnuityCalc(anuityCalcList); } });
						 * annutityRestApi(payoutEntityForNominee);
						 * 
						 * payoutEntity.getPayoutMbr().getPayoutPayeeBank().forEach(payoutPayeeBank -> {
						 * 
						 * if (payoutMbrNomineeDtls.getNomineeCode()
						 * .equals(payoutPayeeBank.getNomineeCode())) {
						 * nomineeBankList.add(payoutPayeeBank); payoutEntityForNominee.getPayoutMbr()
						 * .setPayoutPayeeBank(nomineeBankList); } });
						 * payoutEntityForNominee.getPayoutMbr().setPayoutMbrAddresses(null);
						 * 
						 * } PayoutAnnuityRestApiRes res = annutityRestApi(payoutEntityForNominee);
						 * logger.info(res.getMessage()); }); } else { PayoutAnnuityRestApiRes res =
						 * annutityRestApi(payoutEntity); if (res != null) { logger.info("{}",
						 * res.getMessage()); } }
						 * 
						 * }
						 */

						responseDto = ApiResponseDto.success(null, PayoutErrorConstants.STATUS_UPDATED_SUCCESSFULLY);
					} else {
						responseDto = tempPayoutService.updateCheckerAction(request);
					}
				}

			} else {
				responseDto = ApiResponseDto
						.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_PAYOUT_NO).build());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutServiceImpl :: updateCheckerAction :: error  is" + e.getMessage());
		}
		logger.info("PayoutServiceImpl :: updateCheckerAction :: ended ");
		return responseDto;
	}

	@Override
	public boolean checkPayoutStatus(String payoutNo) {
		long count = payoutRepository.count(findByPayoutNo(payoutNo));
		return (count > 0);
	}

	private PayoutAnnuityRestApiRes annutityRestApi(TempPayoutEntity payoutEntity) {
		String referenceId = payoutEntity.getMasterPolicyNo() + "_" + payoutEntity.getClaimNo() + "_" + payoutEntity.getPayoutMbr().getPayoutMbrNomineeDtls() != null && payoutEntity.getPayoutMbr().getPayoutMbrNomineeDtls().size() > 0 ? payoutEntity.getPayoutMbr().getPayoutMbrNomineeDtls().get(0).getNomineeCode():"";
		AnnuityCreationRequestResponseStoreEntity request = annuityCreationReqAndResRepository.findByReferenceIdAndStatus(referenceId, CommonConstants.SUCCESS);
		if(request != null) {
			PayoutAnnuityRestApiRes payoutAnnuityRestApiRes = new PayoutAnnuityRestApiRes();
			payoutAnnuityRestApiRes.setStatus(HttpStatus.OK_200);
			payoutAnnuityRestApiRes.setMessage("Annuity creation is already processed for this payload");;
			return payoutAnnuityRestApiRes;
		}
		PayoutAnnuityRestApiRes payoutAnnuityRestApiRes = new PayoutAnnuityRestApiRes();
		AnnuityCreationRequestResponseStoreEntity storeAnnuityApi = new AnnuityCreationRequestResponseStoreEntity();
		try {
			if (!payoutEntity.getPayoutMbr().getPayoutAnuityCalc().isEmpty()) {
				PayoutAnnuityRestApiRequest payoutAnnuityRestApiReq = new PayoutAnnuityRestApiRequest();
				payoutAnnuityRestApiReq.setPolicyNumber(payoutEntity.getMasterPolicyNo());
				payoutAnnuityRestApiReq.setSourceCode("Super_Annuation_Claims_Annuities");
				payoutAnnuityRestApiReq.setUploadedBy("Admin");
				payoutAnnuityRestApiReq.setReferenceId(payoutEntity.getClaimNo());
				payoutAnnuityRestApiReq.setReferenceType(PayoutConstants.CLAIM_NO);
				payoutAnnuityRestApiReq.setUploadedOn(DateUtils.dateToStringDDMMYYYY(payoutEntity.getCreatedOn()));
				payoutAnnuityRestApiReq.setAnAddressDtlReqList(annuityApiAddressRestApi(payoutEntity));
				payoutAnnuityRestApiReq.setAnBankDetailRequest(annuityApiBankDetailsRestApi(payoutEntity));
				payoutAnnuityRestApiReq.setAnFinancialDetailRequest(annuityApiFinancialDetailRestApi(payoutEntity));
				payoutAnnuityRestApiReq.setAnJointDetailRequest(annuityApiJointDetailRestApi(payoutEntity));

//			if(payoutEntity.getModeOfExit()==1) {
				payoutAnnuityRestApiReq.setAnNomineeDtlRequestList(anNomineeDtlRestApiRequestList(payoutEntity));
//			}
//		
				payoutAnnuityRestApiReq.setAntPrslDtlRequest(anPayoutMbrPrslDtRestApiRequest(payoutEntity));

				String payout11 = payoutAnnuityRestApiReq.toString();
				logger.info("AnnuityRequest:  ============" + payout11);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				String annuityUrl = environment.getProperty("SA_ANNUITY_CAL_API_FOR_PAYOUT");
				HttpEntity<PayoutAnnuityRestApiRequest> requestEntity = new HttpEntity<>(payoutAnnuityRestApiReq,
						headers);
				payoutAnnuityRestApiRes.setMessage("Skip Annuity API call");
				if (payoutAnnuityRestApiReq != null) {
					ObjectMapper mapper = new ObjectMapper();
					mapper.writerWithDefaultPrettyPrinter();

					logger.info("AnnuityRequest: jsontoString  ============"
							+ mapper.writeValueAsString(payoutAnnuityRestApiReq));
					storeAnnuityApi.setApiRequestString(mapper.writeValueAsString(payoutAnnuityRestApiReq));
					storeAnnuityApi.setApiUrl(annuityUrl);
					storeAnnuityApi.setReferenceId("AnnuityCreation");
					storeAnnuityApi.setPolicyNumber(payoutEntity.getMasterPolicyNo());
					storeAnnuityApi
							.setMemberId(NumericUtils.convertLongToString(payoutEntity.getPayoutMbr().getMemberId()));
					storeAnnuityApi.setRequestDate(DateUtils.sysDate());
					storeAnnuityApi.setType("Annuity");
					ResponseEntity<PayoutAnnuityRestApiRes> responseEntity = restTemplate.postForEntity(annuityUrl,
							requestEntity, PayoutAnnuityRestApiRes.class);

					if (responseEntity.getBody() != null) {
						logger.info("AnnuityCal-PayoutAnnuityRestApiRes-End");
						payoutAnnuityRestApiRes = responseEntity.getBody();
						storeAnnuityApi.setApiResponseString(mapper.writeValueAsString(payoutAnnuityRestApiRes));
						storeAnnuityApi.setStatus("Success");
						storeAnnuityApi.setResponseDate(DateUtils.sysDate());

						return payoutAnnuityRestApiRes;
					}
				}
			} else {
				logger.info("PayoutAnnuity is Empty");
			}
		} catch (Exception e) {
			logger.info("AnnuityCal-PayoutAnnuityRestApiRes-Error:", e);
			storeAnnuityApi.setApiResponseString(e.getMessage());
			storeAnnuityApi.setStatus("Fail");
			storeAnnuityApi.setResponseDate(DateUtils.sysDate());
		} finally {
			storeAnnuityApi.setReferenceId(referenceId);
			annuityCreationReqAndResRepository.save(storeAnnuityApi);
		}
		return payoutAnnuityRestApiRes;
	}

	private Set<PayoutAddressRestApiRequest> annuityApiAddressRestApi(TempPayoutEntity payoutEntity) {
		Set<PayoutAddressRestApiRequest> addressReqList = new HashSet<>();
		TempPayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();
		if (payoutMbr != null) {
			List<TempPayoutMbrAddressEntity> payoutMbrAddresses = payoutMbr.getPayoutMbrAddresses();
			if (CommonUtils.isNonEmptyArray(payoutMbrAddresses)) {
				payoutMbrAddresses.forEach(address -> {
					PayoutAddressRestApiRequest payoutAddressReq = new PayoutAddressRestApiRequest();
					payoutAddressReq.setAddress1(address.getAddressLineOne());
					payoutAddressReq.setAddress2(address.getAddressLineTwo());
					payoutAddressReq.setAddress3(address.getAddressLineThree());
					payoutAddressReq.setCity(address.getCity());
					payoutAddressReq.setCountry(address.getCountry());
					payoutAddressReq.setDistrict(address.getDistrict());
					payoutAddressReq.setPinCode(address.getPinCode());
					payoutAddressReq.setState(address.getState());
					payoutAddressReq.setTypeOfAddress(address.getAddressType());
					addressReqList.add(payoutAddressReq);

				});
				return addressReqList;
			}
		}
		return null;
	}

	private PayoutBankRestApiRequest annuityApiBankDetailsRestApi(TempPayoutEntity payoutEntity) {
		TempPayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();
		if (payoutMbr != null) {
			List<PayoutPayeeBankDetailsTempEntity> payoutMbrBankDetails = payoutMbr.getPayoutPayeeBank();
			if (CommonUtils.isNonEmptyArray(payoutMbrBankDetails)) {
				PayoutPayeeBankDetailsTempEntity bank = payoutMbrBankDetails.get(0);
				PayoutBankRestApiRequest payoutBankRestApiReq = new PayoutBankRestApiRequest();
				payoutBankRestApiReq.setAccountNumber(bank.getAccountNumber());
				payoutBankRestApiReq.setAccountType(bank.getAccountType());
				payoutBankRestApiReq.setBankAddress(bank.getBankAddressOne());
				payoutBankRestApiReq.setBankBranch(bank.getBankBranch());
				payoutBankRestApiReq.setBankEmailId("text@gmail.com");
				payoutBankRestApiReq.setConfirmAccountNumber(bank.getAccountNumber());
				payoutBankRestApiReq.setCountryCode(bank.getCountryCode());
				payoutBankRestApiReq.setIfcsCode(bank.getIfscCode());
				payoutBankRestApiReq.setLandlineNumber(bank.getLandlineNumber());
				payoutBankRestApiReq.setStdCode(bank.getStdCode());

				return payoutBankRestApiReq;
			}
		}
		return null;

	}

	public String getAnnuityoptional(String input) {
		logger.info("PayoutServiceImpl -------getAnnuityoptional-------- Start");
		String response = null;
		AnnuityOptionalResponse optional = integrationService.getAnnuityOptionReponse();
		if (optional != null) {
			for (AnnuityOption annuityOptOne : optional.getAnnuityOption()) {
				if (NumericUtils.convertIntegerToString(annuityOptOne.getAnnuityOptionId()).equals(input)) {
					response = annuityOptOne.getAnnuityOptionDesc();
					logger.info("PayoutServiceImpl -------getAnnuityoptional-------- ended");
					return response;
				}
			}
		}
		return response;

	}

	public String getAnnuityMode(String input) {
		logger.info("PayoutServiceImpl -------getAnnuityMode-------- Start");
		String response = null;
		AnnuityModeResponse optional = integrationService.getAnnuityModeReponse();
		if (optional != null) {
			for (Annuitymode annuitymodeOne : optional.getAnnuityMode()) {
				if (NumericUtils.convertIntegerToString(annuitymodeOne.getAnnuityModeId()).equals(input)) {
					response = annuitymodeOne.getDescription();
					logger.info("PayoutServiceImpl -------getAnnuityMode-------- ended");
					return response;
				}
			}
		}

		return response;

	}

	private PayoutFinancialDetailRestApiRequest annuityApiFinancialDetailRestApi(TempPayoutEntity payoutEntity) {
		PayoutFinancialDetailRestApiRequest payoutFinancialDetailRestApiRequest = new PayoutFinancialDetailRestApiRequest();

		TempPayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();
		if (payoutMbr != null) {
			List<TempPayoutAnnuityCalcEntity> payoutAnuityCalc = payoutMbr.getPayoutAnuityCalc();
			if (CommonUtils.isNonEmptyArray(payoutAnuityCalc)) {
				payoutAnuityCalc.forEach(annuity -> {

					payoutFinancialDetailRestApiRequest.setAnnuityAmount(annuity.getPension().doubleValue());
					payoutFinancialDetailRestApiRequest
							.setAnnuityDueDate(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(annuity.getCreatedOn()));

					payoutFinancialDetailRestApiRequest.setAnnuityMode(getAnnuityMode(annuity.getAnuityMode()));
					payoutFinancialDetailRestApiRequest
							.setAnnuityOption(getAnnuityoptional(annuity.getAnnuityOption()));
					payoutFinancialDetailRestApiRequest.setAnOrigin("");
					payoutFinancialDetailRestApiRequest.setAnPayoutMethod("NEFT");
					payoutFinancialDetailRestApiRequest.setAnPayoutType("Arrear");
					payoutFinancialDetailRestApiRequest.setArrears(annuity.getArrears());
					if (annuity.getPension() > 0 || annuity.getPension() != null) {
						payoutFinancialDetailRestApiRequest.setBasicPension(annuity.getPension().intValue());
					}
					payoutFinancialDetailRestApiRequest.setCertailLifePeriod(0);
					payoutFinancialDetailRestApiRequest.setCertainPeriod(0);
					payoutFinancialDetailRestApiRequest
							.setDaDueDate(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(payoutEntity.getDtOfExit()));
					payoutFinancialDetailRestApiRequest
							.setDateOfExit(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(payoutEntity.getDtOfExit()));
					payoutFinancialDetailRestApiRequest.setDateOfVesting(
							CommonDateUtils.dateToStringFormatYyyyMmDdSlash(payoutEntity.getDtOfExit()));
					payoutFinancialDetailRestApiRequest.setGstAmnt(annuity.getGstAmount());
					payoutFinancialDetailRestApiRequest.setIncomeTaxDeducted(null);
					payoutFinancialDetailRestApiRequest.setIncomeTaxPending(null);
					payoutFinancialDetailRestApiRequest.setIncomeTaxProjected(null);
					payoutFinancialDetailRestApiRequest
							.setModeOfExit(NumericUtils.convertIntegerToString(payoutEntity.getModeOfExit()));
					payoutFinancialDetailRestApiRequest
							.setPensionAmtPaidTo(annuity.getAnnuityPayableTo().equalsIgnoreCase("1") ? "MPH" : "ANT");
					payoutFinancialDetailRestApiRequest.setPurchasePrice(annuity.getPurchasePrice());
					payoutFinancialDetailRestApiRequest.setRecovery(null);
				});
				return payoutFinancialDetailRestApiRequest;
			}
		}
		return null;

	}

	private PayoutJointDetailRestApiRequest annuityApiJointDetailRestApi(TempPayoutEntity payoutEntity) {
		List<String> annuityList = new ArrayList<>();
		annuityList.add("3");
		annuityList.add("4");
		annuityList.add("5");

		PayoutJointDetailRestApiRequest payoutJointDetailRestApiRequest = new PayoutJointDetailRestApiRequest();

		TempPayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();
		if (payoutMbr != null) {
			List<TempPayoutAnnuityCalcEntity> payoutAnuityCalc = payoutMbr.getPayoutAnuityCalc();
			if (CommonUtils.isNonEmptyArray(payoutAnuityCalc)) {
				payoutAnuityCalc.forEach(anuity -> {
					if (annuityList.contains(anuity.getAnnuityOption())) {
						payoutJointDetailRestApiRequest.setJointLifeDOB(anuity.getDateOfBirth());
						payoutJointDetailRestApiRequest.setJointLifeFirstName(anuity.getSpouseName());
						payoutJointDetailRestApiRequest.setJointLifeGender(null);
						payoutJointDetailRestApiRequest.setJointLifeLastName(null);
						payoutJointDetailRestApiRequest.setJointLifeMiddleName(null);
						payoutJointDetailRestApiRequest.setJointLifePercent(null);
					}
				});
				return payoutJointDetailRestApiRequest;
			}
		}
		return payoutJointDetailRestApiRequest;

	}

	private Set<PayoutNomineeDtlRestApiRequest> anNomineeDtlRestApiRequestList(TempPayoutEntity payoutEntity) {
		Set<PayoutNomineeDtlRestApiRequest> payoutNomineeDtlRestApiRequestList = new HashSet<PayoutNomineeDtlRestApiRequest>();
		TempPayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();
		if (payoutMbr != null) {
			List<TempPayoutMbrNomineeEntity> payoutMbrNomineeDtls = payoutMbr.getPayoutMbrNomineeDtls();
			if (CommonUtils.isNonEmptyArray(payoutMbrNomineeDtls)) {

				payoutMbrNomineeDtls.forEach(nominee -> {

					if (nominee != null && nominee.equals(ClaimConstants.DEATH)
							&& nominee.getClaimantType().equalsIgnoreCase(ClaimConstants.NOMINEE)) {

						TempPayoutMbrNomineeEntity parentNomineeId = tempPayoutMbrNomineeRepository
								.findByParentNomineeId(nominee.getNomineeId());

						if (parentNomineeId != null) {

							PayoutNomineeDtlRestApiRequest payoutNomineeDtlRestApiRequest = new PayoutNomineeDtlRestApiRequest();
							payoutNomineeDtlRestApiRequest
									.setAadharNo(NumericUtils.convertLongToString(nominee.getAadharNumber()));
							payoutNomineeDtlRestApiRequest.setAccountNumber(nominee.getAccountNumber());
							payoutNomineeDtlRestApiRequest.setAccountType(nominee.getAccountType());
							payoutNomineeDtlRestApiRequest.setAccountTypeId(null);
							payoutNomineeDtlRestApiRequest.setAmtPercentage(
									(nominee.getSharedPercentage() != null || nominee.getSharedPercentage() >= 0d)
											? NumericUtils.convertDoubleToString(nominee.getSharedPercentage())
											: "0");
							payoutNomineeDtlRestApiRequest.setAnnuitantName(null);
							payoutNomineeDtlRestApiRequest.setAnnuityNumber(null);
							payoutNomineeDtlRestApiRequest.setAppointeeAadharNumber(null);
							payoutNomineeDtlRestApiRequest.setAppointeeAccountNumber(null);
							payoutNomineeDtlRestApiRequest.setAppointeeAccountType(null);
							payoutNomineeDtlRestApiRequest.setAppointeeAccountTypeId(null);
							payoutNomineeDtlRestApiRequest.setAppointeeAddressId(null);
							payoutNomineeDtlRestApiRequest.setAppointeeAge(null);
							payoutNomineeDtlRestApiRequest.setAppointeeBankAddress(null);
							payoutNomineeDtlRestApiRequest.setAppointeeBankBranch(null);
							payoutNomineeDtlRestApiRequest.setAppointeeBankId(null);
							payoutNomineeDtlRestApiRequest.setAppointeeBankName(null);
							payoutNomineeDtlRestApiRequest.setAppointeeCode(null);
							payoutNomineeDtlRestApiRequest.setAppointeeContactNumber(null);
							payoutNomineeDtlRestApiRequest.setAppointeeCountryCode(null);
							payoutNomineeDtlRestApiRequest.setAppointeeDOB(null);
							payoutNomineeDtlRestApiRequest.setAppointeeFirstName(null);
							payoutNomineeDtlRestApiRequest.setAppointeeIfscCode(null);
							payoutNomineeDtlRestApiRequest.setAppointeeLandlineNo(null);
							payoutNomineeDtlRestApiRequest.setAppointeeLastName(null);
							payoutNomineeDtlRestApiRequest.setAppointeeMiddleName(null);
							payoutNomineeDtlRestApiRequest.setAppointeeName(null);
							payoutNomineeDtlRestApiRequest.setAppointeeOthers(null);
							payoutNomineeDtlRestApiRequest.setAppointeepan(null);
							payoutNomineeDtlRestApiRequest.setAppointeeStdCode(null);
							payoutNomineeDtlRestApiRequest.setBankAddress(nominee.getAddressOne());
							payoutNomineeDtlRestApiRequest.setBankBranch(nominee.getBankBranch());
							payoutNomineeDtlRestApiRequest.setBankName(nominee.getBankName());
							payoutNomineeDtlRestApiRequest
									.setContactNumber(NumericUtils.convertLongToString(nominee.getMobileNo()));
							payoutNomineeDtlRestApiRequest.setCountry(nominee.getCountry());
							payoutNomineeDtlRestApiRequest
									.setDateOfBirth(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(nominee.getDob()));
							payoutNomineeDtlRestApiRequest.setEmailId(nominee.getEmailId());
							payoutNomineeDtlRestApiRequest.setIfscCode(nominee.getIfscCode());
							payoutNomineeDtlRestApiRequest
									.setNmeStatus(NumericUtils.convertLongToString(nominee.getMaritalStatus()));
							payoutNomineeDtlRestApiRequest.setNmeType(nominee.getClaimantType());
							payoutNomineeDtlRestApiRequest.setNomineeAddress1(nominee.getAddressOne());
							payoutNomineeDtlRestApiRequest.setNomineeAddress2(nominee.getAddressTwo());
							payoutNomineeDtlRestApiRequest.setNomineeAddress3(nominee.getAddressThree());
							payoutNomineeDtlRestApiRequest.setNomineeAge(nominee.getAge());
							payoutNomineeDtlRestApiRequest.setNomineeCity(null);
							payoutNomineeDtlRestApiRequest.setNomineeCode(nominee.getNomineeCode());
							payoutNomineeDtlRestApiRequest.setNomineeCountry(nominee.getCountry());
							payoutNomineeDtlRestApiRequest.setNomineeCountryCode(null);
							payoutNomineeDtlRestApiRequest.setNomineeDistrict(nominee.getDistrict());
							payoutNomineeDtlRestApiRequest.setNomineeFirstName(nominee.getFirstName());
							payoutNomineeDtlRestApiRequest.setNomineeId(nominee.getNomineeId());
							payoutNomineeDtlRestApiRequest.setNomineeLastName(nominee.getLastName());
							payoutNomineeDtlRestApiRequest.setNomineeMiddleName(nominee.getMiddleName());
							payoutNomineeDtlRestApiRequest.setNomineeName(nominee.getFirstName());
							payoutNomineeDtlRestApiRequest.setNomineePinCode(
									nominee.getPincode() != null ? nominee.getPincode().intValue() : null);
							payoutNomineeDtlRestApiRequest.setNomineeState(nominee.getState());
							payoutNomineeDtlRestApiRequest.setPan(null);
							if (nominee.getRelationShip() != null)
								payoutNomineeDtlRestApiRequest.setRelationship(nominee.getRelationShip() > 0l
										? PayoutConstants.getRelationShipNamebyId(
												NumericUtils.convertLongToInteger(nominee.getRelationShip()))
										: "No RelationShip");
							else
								payoutNomineeDtlRestApiRequest.setRelationship("No RelationShip");
							payoutNomineeDtlRestApiRequest.setRelationShipWithNominee(
									NumericUtils.convertLongToString(nominee.getRelationShip()));
							payoutNomineeDtlRestApiRequest
									.setStatus(NumericUtils.convertLongToString(nominee.getMaritalStatus()));
							payoutNomineeDtlRestApiRequest.setStdCode(null);
							payoutNomineeDtlRestApiRequestList.add(payoutNomineeDtlRestApiRequest);
						}
					}

					PayoutNomineeDtlRestApiRequest payoutNomineeDtlRestApiRequest = new PayoutNomineeDtlRestApiRequest();
					payoutNomineeDtlRestApiRequest
							.setAadharNo(NumericUtils.convertLongToString(nominee.getAadharNumber()));
					payoutNomineeDtlRestApiRequest.setAccountNumber(nominee.getAccountNumber());
					payoutNomineeDtlRestApiRequest.setAccountType(nominee.getAccountType());
					payoutNomineeDtlRestApiRequest.setAccountTypeId(null);
					payoutNomineeDtlRestApiRequest.setAmtPercentage((nominee.getSharedPercentage() != null)
							? NumericUtils.convertDoubleToString(nominee.getSharedPercentage())
							: "0");
					payoutNomineeDtlRestApiRequest.setAnnuitantName(null);
					payoutNomineeDtlRestApiRequest.setAnnuityNumber(null);
					payoutNomineeDtlRestApiRequest.setAppointeeAadharNumber(null);
					payoutNomineeDtlRestApiRequest.setAppointeeAccountNumber(null);
					payoutNomineeDtlRestApiRequest.setAppointeeAccountType(null);
					payoutNomineeDtlRestApiRequest.setAppointeeAccountTypeId(null);
					payoutNomineeDtlRestApiRequest.setAppointeeAddressId(null);
					payoutNomineeDtlRestApiRequest.setAppointeeAge(null);
					payoutNomineeDtlRestApiRequest.setAppointeeBankAddress(null);
					payoutNomineeDtlRestApiRequest.setAppointeeBankBranch(null);
					payoutNomineeDtlRestApiRequest.setAppointeeBankId(null);
					payoutNomineeDtlRestApiRequest.setAppointeeBankName(null);
					payoutNomineeDtlRestApiRequest.setAppointeeCode(null);
					payoutNomineeDtlRestApiRequest.setAppointeeContactNumber(null);
					payoutNomineeDtlRestApiRequest.setAppointeeCountryCode(null);
					payoutNomineeDtlRestApiRequest.setAppointeeDOB(null);
					payoutNomineeDtlRestApiRequest.setAppointeeFirstName(null);
					payoutNomineeDtlRestApiRequest.setAppointeeIfscCode(null);
					payoutNomineeDtlRestApiRequest.setAppointeeLandlineNo(null);
					payoutNomineeDtlRestApiRequest.setAppointeeLastName(null);
					payoutNomineeDtlRestApiRequest.setAppointeeMiddleName(null);
					payoutNomineeDtlRestApiRequest.setAppointeeName(null);
					payoutNomineeDtlRestApiRequest.setAppointeeOthers(null);
					payoutNomineeDtlRestApiRequest.setAppointeepan(null);
					payoutNomineeDtlRestApiRequest.setAppointeeStdCode(null);
					payoutNomineeDtlRestApiRequest.setBankAddress(nominee.getAddressOne());
					payoutNomineeDtlRestApiRequest.setBankBranch(nominee.getBankBranch());
					payoutNomineeDtlRestApiRequest.setBankName(nominee.getBankName());
					payoutNomineeDtlRestApiRequest
							.setContactNumber(NumericUtils.convertLongToString(nominee.getMobileNo()));
					payoutNomineeDtlRestApiRequest.setCountry(nominee.getCountry());
					payoutNomineeDtlRestApiRequest
							.setDateOfBirth(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(nominee.getDob()));
					payoutNomineeDtlRestApiRequest.setEmailId(nominee.getEmailId());
					payoutNomineeDtlRestApiRequest.setIfscCode(nominee.getIfscCode());
					payoutNomineeDtlRestApiRequest
							.setNmeStatus(NumericUtils.convertLongToString(nominee.getMaritalStatus()));
					payoutNomineeDtlRestApiRequest.setNmeType(nominee.getClaimantType());
					payoutNomineeDtlRestApiRequest.setNomineeAddress1(nominee.getAddressOne());
					payoutNomineeDtlRestApiRequest.setNomineeAddress2(nominee.getAddressTwo());
					payoutNomineeDtlRestApiRequest.setNomineeAddress3(nominee.getAddressThree());
					payoutNomineeDtlRestApiRequest.setNomineeAge(nominee.getAge());
					payoutNomineeDtlRestApiRequest.setNomineeCity(null);
					payoutNomineeDtlRestApiRequest.setNomineeCode(nominee.getNomineeCode());
					payoutNomineeDtlRestApiRequest.setNomineeCountry(nominee.getCountry());
					payoutNomineeDtlRestApiRequest.setNomineeCountryCode(null);
					payoutNomineeDtlRestApiRequest.setNomineeDistrict(nominee.getDistrict());
					payoutNomineeDtlRestApiRequest.setNomineeFirstName(nominee.getFirstName());
					payoutNomineeDtlRestApiRequest.setNomineeId(nominee.getNomineeId());
					payoutNomineeDtlRestApiRequest.setNomineeLastName(nominee.getLastName());
					payoutNomineeDtlRestApiRequest.setNomineeMiddleName(nominee.getMiddleName());
					payoutNomineeDtlRestApiRequest.setNomineeName(nominee.getFirstName());
					payoutNomineeDtlRestApiRequest
							.setNomineePinCode(nominee.getPincode() != null ? nominee.getPincode().intValue() : null);
					payoutNomineeDtlRestApiRequest.setNomineeState(nominee.getState());
					payoutNomineeDtlRestApiRequest.setPan(null);
					if (nominee.getRelationShip() != null)
						payoutNomineeDtlRestApiRequest.setRelationship(nominee.getRelationShip() > 0l
								? PayoutConstants.getRelationShipNamebyId(
										NumericUtils.convertLongToInteger(nominee.getRelationShip()))
								: "No RelationShip");
					else
						payoutNomineeDtlRestApiRequest.setRelationship("No RelationShip");
					payoutNomineeDtlRestApiRequest
							.setRelationShipWithNominee(NumericUtils.convertLongToString(nominee.getRelationShip()));
					payoutNomineeDtlRestApiRequest
							.setStatus(NumericUtils.convertLongToString(nominee.getMaritalStatus()));
					payoutNomineeDtlRestApiRequest.setStdCode(null);
					payoutNomineeDtlRestApiRequestList.add(payoutNomineeDtlRestApiRequest);
				});
				return payoutNomineeDtlRestApiRequestList;
			}
		}
		return payoutNomineeDtlRestApiRequestList;

	}

	private String getProductCodeByproductId(Long productId)
			throws MalformedURLException, ProtocolException, IOException {
		String product = null;
		JsonNode response = integrationService.getProductDetailsByProductId(productId);
		if (response != null) {
			JsonNode proposeDetails = response.path("responseData");
			product = proposeDetails.path("productCode").textValue();
		}
		return product;
	}

	private String getVariantCodeByVariantId(Long variantId)
			throws MalformedURLException, ProtocolException, IOException {
		String product = null;
		JsonNode response = integrationService
				.getVariantDetailsByProductVariantId(NumericUtils.stringToLong(String.valueOf(variantId)));
		if (response != null) {
			JsonNode proposeDetails = response.path("responseData");
			product = proposeDetails.path("subCategory").textValue();

		}
		return product;
	}

	/*** Note:-getunitDetailsByuniyCode ***/

	private String getUnitNameByUnitCode(String unitCode) {
		ResponseDto responseDto = accountingIntegrationService.commonmasterserviceUnitByCode(unitCode);
		if (responseDto != null) {
			return responseDto.getCityName();
		} else {
			return "No unitName";
		}
	}

	private PayoutMbrPrslDtRestApiRequest anPayoutMbrPrslDtRestApiRequest(TempPayoutEntity payoutEntity)
			throws MalformedURLException, ProtocolException, IOException {
		PayoutMbrPrslDtRestApiRequest payoutMbrPrslDtRestApiRequest = new PayoutMbrPrslDtRestApiRequest();

		if (payoutEntity != null) {
			TempPayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();

			if (payoutMbr != null) {
				MemberMasterEntity mEntity = memberMasterRepository.findByLicIdAndPolicyIdAndIsActive(
						payoutEntity.getPayoutMbr().getLicId(), payoutEntity.getPolicyId());

				if (payoutMbr != null && payoutEntity.getModeOfExit() != 1) {

					payoutMbrPrslDtRestApiRequest
							.setAddharNumber(NumericUtils.convertLongToString(payoutMbr.getAadharNumber()));
					payoutMbrPrslDtRestApiRequest.setCommunicationPreference(payoutMbr.getCommunicationPreference());
					payoutMbrPrslDtRestApiRequest.setDateOfJoiningScheme(
							CommonDateUtils.dateToStringFormatYyyyMmDdSlash(payoutMbr.getDateOfJoining()));
					payoutMbrPrslDtRestApiRequest
							.setEmailId(mEntity.getEmailid() != null ? mEntity.getEmailid() : "text@gmail.com");

					payoutMbrPrslDtRestApiRequest.setMobileNo(
							mEntity.getMobileNo() != null ? NumericUtils.convertLongToString(mEntity.getMobileNo())
									: "9999999999");
					// payoutMbrPrslDtRestApiRequest.setEmailId("text@gmail.com");
					payoutMbrPrslDtRestApiRequest.setFatherName(payoutMbr.getFatherName());
					payoutMbrPrslDtRestApiRequest.setFirstName(payoutMbr.getFirstName());
					payoutMbrPrslDtRestApiRequest.setGender(payoutMbr.getGender());
					payoutMbrPrslDtRestApiRequest.setLanguagePreference(payoutMbr.getLanguagePreference());
					payoutMbrPrslDtRestApiRequest.setLastEcSubmissionDate("");
					payoutMbrPrslDtRestApiRequest.setLastName(payoutMbr.getLastName());
					payoutMbrPrslDtRestApiRequest.setMiddleName(payoutMbr.getMiddleName());
					// payoutMbrPrslDtRestApiRequest.setMobileNo("9999999999");
					payoutMbrPrslDtRestApiRequest.setPan(payoutMbr.getPan());
					payoutMbrPrslDtRestApiRequest.setPersonalDateOfBirth(
							CommonDateUtils.dateToStringFormatYyyyMmDdSlash(payoutMbr.getDateOfBirth()));
					payoutMbrPrslDtRestApiRequest.setSpouseName(payoutMbr.getSpouseName());
				} else if (payoutMbr != null && !payoutEntity.getPayoutMbr().getPayoutMbrNomineeDtls().isEmpty()) {
					TempPayoutMbrNomineeEntity nominee = payoutEntity.getPayoutMbr().getPayoutMbrNomineeDtls().get(0);
					payoutMbrPrslDtRestApiRequest
							.setAddharNumber(NumericUtils.convertLongToString(nominee.getAadharNumber()));
					payoutMbrPrslDtRestApiRequest.setCommunicationPreference(payoutMbr.getCommunicationPreference());
					payoutMbrPrslDtRestApiRequest.setDateOfJoiningScheme(
							CommonDateUtils.dateToStringFormatYyyyMmDdSlash(payoutMbr.getDateOfJoining()));
					payoutMbrPrslDtRestApiRequest
							.setEmailId(mEntity.getEmailid() != null ? mEntity.getEmailid() : "text@gmail.com");

					payoutMbrPrslDtRestApiRequest.setMobileNo(
							mEntity.getMobileNo() != null ? NumericUtils.convertLongToString(mEntity.getMobileNo())
									: "9999999999");
					payoutMbrPrslDtRestApiRequest.setFatherName("");
					payoutMbrPrslDtRestApiRequest.setFirstName(nominee.getFirstName());
					payoutMbrPrslDtRestApiRequest.setGender(payoutMbr.getGender());
					payoutMbrPrslDtRestApiRequest.setLanguagePreference(payoutMbr.getLanguagePreference());
					payoutMbrPrslDtRestApiRequest.setLastEcSubmissionDate("");
					payoutMbrPrslDtRestApiRequest.setLastName(nominee.getLastName());
					payoutMbrPrslDtRestApiRequest.setMiddleName(nominee.getMiddleName());
//				payoutMbrPrslDtRestApiRequest.setMobileNo(nominee.getMobileNo()!=null?nominee.getMobileNo()>0?NumericUtils.convertLongToString(nominee.getMobileNo()):"9999999999":"9999999999");
					payoutMbrPrslDtRestApiRequest.setPan(nominee.getPan());
					payoutMbrPrslDtRestApiRequest
							.setPersonalDateOfBirth(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(nominee.getDob()));
					payoutMbrPrslDtRestApiRequest.setSpouseName("");
				}
			}
			payoutMbrPrslDtRestApiRequest
					.setUnit(payoutEntity.getUnitCode().equalsIgnoreCase("G706") ? claimRepository.getUnitName("G715")
							: claimRepository.getUnitName(payoutEntity.getUnitCode()));
			payoutMbrPrslDtRestApiRequest.setUnitCode(
					payoutEntity.getUnitCode().equalsIgnoreCase("G706") ? "G715" : payoutEntity.getUnitCode());

			payoutMbrPrslDtRestApiRequest
					.setVariant(getVariantCodeByVariantId(NumericUtils.stringToLong(payoutEntity.getVariant())));
			payoutMbrPrslDtRestApiRequest
					.setProduct(getProductCodeByproductId(NumericUtils.stringToLong(payoutEntity.getProduct())));

		}
		return payoutMbrPrslDtRestApiRequest;
	}

	@Override
	@Transactional
	public ApiResponseDto<PayoutDto> findPayoutDetails(String initiMationNo, String payoutNo) {
		ApiResponseDto<PayoutDto> responseDto = new ApiResponseDto<>();
		logger.info("PayoutServiceImpl :: findPayoutDetails :: start ");
		try {
//		Optional<PayoutEntity> optional = payoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
//		if (optional.isPresent()) {
//			return convertEntityToDto(optional.get());
//		} else {
//			return tempPayoutService.findPayoutDetails(initiMationNo);
//		}

			if (payoutNo != null && !payoutNo.equalsIgnoreCase("")) {

				Optional<PayoutEntity> optional = payoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
				if (optional.isPresent()) {
					responseDto = ApiResponseDto.success(payoutDto(optional.get()));
				}

			} else if (initiMationNo != null && !initiMationNo.equalsIgnoreCase("")) {
				responseDto = tempPayoutService.findPayoutDetails(initiMationNo);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutServiceImpl :: findPayoutDetails :: error is  " + e.getMessage());
		}
		logger.info("PayoutServiceImpl :: findPayoutDetails :: ended ");
		return responseDto;

	}

	/*********
	 * @NARAYANAN****
	 * @start**
	 * @setter***
	 * @getter
	 ***/

	public PayoutDto payoutDto(PayoutEntity from) {
		PayoutDto to = new PayoutDto();
		String claimNo = claimRepository.fetchByClaimNoAndIsActive(from.getClaimNo(), Boolean.TRUE);
		to.setDtOfExit(null);
		to.setDtOfExit(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(from.getDtOfExit()));
		BeanUtils.copyProperties(from, to);

		to.setPayoutDocDetails(payoutDocDetails(from));
		to.setPayoutMbr(payoutMbr(from));
		to.setPayoutNotes(payoutNotes(from));
		Double commutatiomSum = 0d;
		Double annuitySum = 0d;
		if (!to.getPayoutMbr().getPayoutCommutationCalc().isEmpty()) {
			List<Double> commutaionSR = to.getPayoutMbr().getPayoutAnuityCalc().stream().map(i -> i.getShortReserve())
					.collect(Collectors.toList());
			commutatiomSum = commutaionSR.stream().collect(Collectors.summingDouble(Double::doubleValue));

		}
		if (!to.getPayoutMbr().getPayoutAnuityCalc().isEmpty()) {
			List<Double> annuitySR = to.getPayoutMbr().getPayoutAnuityCalc().stream().map(i -> i.getShortReserve())
					.collect(Collectors.toList());
			annuitySum = annuitySR.stream().collect(Collectors.summingDouble(Double::doubleValue));
		}
		to.setTotalShortReserve(commutatiomSum + annuitySum);
		to.setClaimOnBoadingNumber(claimNo);
		return to;
	}

	public List<PayoutDocumentDetailDto> payoutDocDetails(PayoutEntity payoutEntity) {
		List<PayoutDocumentDetailDto> tos = new ArrayList<>();
		payoutEntity.getPayoutDocDetails().forEach(from -> {
			PayoutDocumentDetailDto to = new PayoutDocumentDetailDto();
			BeanUtils.copyProperties(from, to);
			tos.add(to);
		});
		return tos;
	}

	public PayoutMbrDto payoutMbr(PayoutEntity payoutEntity) {
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
		return to;
	}

	public List<PayoutAnnuityCalcDto> payoutAnuityCalc(List<PayoutAnnuityCalcEntity> froms) {
		List<PayoutAnnuityCalcDto> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutAnnuityCalcDto to = new PayoutAnnuityCalcDto();
			BeanUtils.copyProperties(from, to);
			to.setSpouseDob(DateUtils.dateToStringDDMMYYYY(from.getDateOfBirth()));
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
			to.setSpouseDOB(from.getSpouseDob() != null ? DateUtils.dateToStringDDMMYYYY(from.getSpouseDob()) : "");
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
			to.setBankAddressOne(from.getBankAddressOne());
			BeanUtils.copyProperties(from, to);
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

	/*********
	 * @NARAYANAN***
	 * @End*
	 * 
	 * @setter***
	 * @getter
	 ***/

	private ApiResponseDto<PayoutDto> convertEntityToDto(PayoutEntity payoutEntity) {
		PayoutDto response = modelMapper.map(payoutEntity, PayoutDto.class);
		return ApiResponseDto.success(response);
	}

	/*** RealtimeApi Calling ***/

	private void AnnuitantDetailsUpdateInAnnuity(String policyNumber)
			throws JsonMappingException, JsonProcessingException {
		logger.info("PayoutServiceImpl--AnnuitantDetailsUpdateInAnnuity---Start");
		try {
			AnnuityRealTimeData responseData = integrationService.callAnnuityRealtimeApi(policyNumber);
			if (responseData != null) {
				for (AnnuityRealTimeResponse annuityRealTimeResponse : responseData.getDATA()) {
					annuityRealTimeResponse.setAdharNumber(
							annuityRealTimeResponse.getAdharNumber() != null ? annuityRealTimeResponse.getAdharNumber()
									: "");
					List<PayoutAnnuityCalcEntity> response = payoutAnnuityCalcRepository.fetchAnnutantDetails(
							annuityRealTimeResponse.getAccountNumber(), annuityRealTimeResponse.getPan(),
							annuityRealTimeResponse.getAdharNumber(), annuityRealTimeResponse.getPolicyNumber());

					if (!response.isEmpty()) {
						for (PayoutAnnuityCalcEntity payoutAnnuityCalcEntity : response) {
							payoutAnnuityCalcEntity.setAnnuitantNumber(annuityRealTimeResponse.getAnnuitantNumber());
							payoutAnnuityCalcEntity.setAnnuityNumber(annuityRealTimeResponse.getAnnuityNumber());
							payoutAnnuityCalcEntity.setFirstAnnuityInstallmentDueDate(
									annuityRealTimeResponse.getFirstAnnuityInstallmentDueDate());
							payoutAnnuityCalcEntity.setAnnuityStatus(responseData.getStatus());
							payoutAnnuityCalcEntity.setAnnuityStatus(responseData.getStatus());
							payoutAnnuityCalcRepository.save(payoutAnnuityCalcEntity);
							logger.info("AnnuityNumber::{}" + payoutAnnuityCalcEntity.getAnnuityNumber());
						}

						logger.info("Accountant updated succesfully");
					} else {
						logger.info("Accountant is not Existed");
						throw new ApplicationException("Accountant is not Existed");
					}
				}
			} else {
				throw new ApplicationException("AnnuiyRealTimeApi Response is Empty");
			}
		} catch (Exception e) {
			logger.info("PayoutServiceImpl--AnnuitantDetailsUpdateInAnnuity---error");
		}

	}

	/**
	 * Check InterestCalculation Date Validation
	 **/
	private Boolean checkInterestCalculationDate(TempPayoutEntity payoutEntity) {
		logger.info("TempPayoutServiceImpl -- checkInterestCalculationDate --started");
		TempPayoutFundValueEntity tempPayoutFundValueEntity = payoutEntity.getPayoutMbr().getPayoutFundValue().get(0);
		Date fromDate = CommonDateUtils.convertStringToDate(DateUtils.sysDateStringSlash());
		Date toDate = CommonDateUtils.convertStringToDate(DateUtils.sysDateStringSlash());
		toDate = CommonDateUtils.constructeEndDateTime(toDate);
		if (tempPayoutFundValueEntity.getDateOfCalculate() != null
				&& tempPayoutFundValueEntity.getDateOfCalculate().compareTo(fromDate) >= 0
				&& tempPayoutFundValueEntity.getDateOfCalculate().compareTo(toDate) <= 0) {
			logger.info("TempPayoutServiceImpl -- checkInterestCalculationDate --error");

			return false;

		}
		logger.info("TempPayoutServiceImpl -- checkInterestCalculationDate --end");
		return true;
	}

	@Override
	public ApiResponseDto<String> approvedAndRejectAction(PayoutCheckerActionRequestDto request)
			throws ApplicationException, JsonMappingException, JsonProcessingException {

		Optional<TempPayoutEntity> tempPayoutEntity = tempPayoutRepository
				.findByInitiMationNoAndIsActive(request.getInitiMationNo(), Boolean.TRUE);

		if (tempPayoutEntity.isPresent()) {

			if (request.getAction().equals(PayoutStatus.APPROVE.val())
					|| request.getAction().equals(PayoutStatus.REJECT.val())) {

				RollbackEntity rollbackEntity = rollbackRepository.findByReferenceNoAndModuleNameAndIsActiveTrue(
						tempPayoutEntity.get().getInitiMationNo(), "PAYOUT");

				if (rollbackEntity == null) {
					rollbackEntity = new RollbackEntity();
					rollbackEntity.setRollbackId(null);
					rollbackEntity.setModuleName("PAYOUT");
					rollbackEntity.setReferenceNo(tempPayoutEntity.get().getInitiMationNo());
					rollbackEntity.setCreatedBy(request.getModifiedBy());
					rollbackEntity.setCreatedOn(new Date());
					rollbackEntity.setIsActive(Boolean.TRUE);
				}

				Boolean annuityStatus = annuityRestCall(tempPayoutEntity.get(), request.getAction());
				if (annuityStatus) {
					String payoutNo = commonService.getSequence(CommonConstants.PAYOUT_SEQ);
					tempPayoutEntity.get().setPayoutNo(payoutNo);

					Boolean accountingStatus = accountingRestCall(tempPayoutEntity.get(),
							request.getAction());

					Boolean isFundBatchSuccess = processFundBatch(request, tempPayoutEntity.get());

					Boolean isTempToMasterSuccess = moveFromTempToMaster(rollbackEntity, tempPayoutEntity.get(),
							request);

					rollbackEntity
							.setFundStatus(isFundBatchSuccess ? CommonConstants.SUCCESS : CommonConstants.EXCEPTION);

					rollbackEntity.setAccountingStatus(
							accountingStatus ? CommonConstants.SUCCESS : CommonConstants.EXCEPTION);

					rollbackEntity.setInternalModuleStatus(
							isTempToMasterSuccess ? CommonConstants.SUCCESS : CommonConstants.EXCEPTION);
				}

				rollbackEntity.setAnnuityStatus(annuityStatus ? CommonConstants.SUCCESS : CommonConstants.EXCEPTION);

				rollbackEntity = rollbackRepository.save(rollbackEntity);
			}

		} else {
			return ApiResponseDto.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_INTIMATION_NO).build());
		}

		return null;
	}

	private Boolean moveFromTempToMaster(RollbackEntity rollbackEntity, TempPayoutEntity tempPayoutEntity,
			PayoutCheckerActionRequestDto request) {
		Boolean isTempToMasterSuccess = Boolean.FALSE;
		try {
			if (rollbackEntity.getInternalModuleStatus() == null
					|| rollbackEntity.getInternalModuleStatus().equalsIgnoreCase("EXCEPTION")) {

				tempPayoutEntity.setModifiedBy(request.getModifiedBy());
				tempPayoutEntity.setModifiedOn(CommonDateUtils.sysDate());
				tempPayoutEntity.setStatus(request.getAction());
				PayoutEntity payoutEntity = savePayoutService.insertNew(tempPayoutEntity);
				if (payoutEntity != null) {
					isTempToMasterSuccess = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return isTempToMasterSuccess;
	}

	private Boolean processFundBatch(PayoutCheckerActionRequestDto request, TempPayoutEntity tempPayoutEntity) {
		Boolean isFundBatchSuccess = Boolean.FALSE;
		try {

			if (request.getAction().equals(PayoutStatus.APPROVE.val())) {
				InterestRateResponseDto interestRateResponseDto = fundRestApiService.processClaim(request,
						tempPayoutEntity);
				if (interestRateResponseDto != null) {
					if (StringUtils.isNotBlank(interestRateResponseDto.getStatus())
							&& interestRateResponseDto.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
						logger.info("Claim Response::{}", interestRateResponseDto.getMessage());
						isFundBatchSuccess = Boolean.TRUE;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return isFundBatchSuccess;
	}

	private Boolean accountingRestCall(TempPayoutEntity tempPayoutEntity, Integer action) {
		Boolean isAccountingSuccess = Boolean.FALSE;

		try {
			if (tempPayoutEntity.getPayoutId() != null) {

				ApiResponseDto<List<PayoutStoredProcedureResponseDto>> responseDto = storeProcedureforAccount(
						tempPayoutEntity.getPayoutId());
				if (responseDto != null && responseDto.getError() != null && responseDto.getError().size() == 0) {
					isAccountingSuccess = Boolean.TRUE;
				}

			}

			logger.info("updateBeneficiaryId -- Starts :: {}", tempPayoutEntity.getClaimNo());
			updateBeneficiaryId(
					(tempPayoutEntity != null) ? tempPayoutEntity.getPayoutNo()
							: payoutRepository.getPayoutNoByIntimationNo(tempPayoutEntity.getInitiMationNo()),
					tempPayoutEntity.getClaimNo());
			logger.info("updateBeneficiaryId -- Ends:: {}", tempPayoutEntity.getClaimNo());

		} catch (Exception e) {
			// TODO: handle exception
		}

		return isAccountingSuccess;
	}

	private Boolean annuityRestCall(TempPayoutEntity tempPayoutEntity, Integer actionStatus) {

		Boolean isAnnuitySuccess = false;
		try {

			if (actionStatus == 7) {
				if (tempPayoutEntity.getModeOfExit() == 1) {
					for (TempPayoutMbrNomineeEntity payoutMbrNomineeDtls : tempPayoutEntity.getPayoutMbr()
							.getPayoutMbrNomineeDtls()) {
						TempPayoutEntity payoutEntityForNominee = getDuplicatePayout(tempPayoutEntity);

						List<TempPayoutMbrNomineeEntity> nomineeList = new ArrayList<>();
						List<TempPayoutAnnuityCalcEntity> anuityCalcList = new ArrayList<>();
						List<PayoutPayeeBankDetailsTempEntity> nomineeBankList = new ArrayList<>();
						if (payoutMbrNomineeDtls.getNomineeCode() != null
								&& payoutMbrNomineeDtls.getClaimantType().equalsIgnoreCase("Nominee")) {

							nomineeList.add(payoutMbrNomineeDtls);
							payoutEntityForNominee.getPayoutMbr().setPayoutMbrNomineeDtls(null);
							payoutEntityForNominee.getPayoutMbr().setPayoutMbrNomineeDtls(nomineeList);

							tempPayoutEntity.getPayoutMbr().getPayoutAnuityCalc().forEach(payoutAnuityCalc -> {

								if (payoutMbrNomineeDtls.getNomineeCode().equals(payoutAnuityCalc.getNomineeCode())) {
									anuityCalcList.add(payoutAnuityCalc);

								}
							});
							payoutEntityForNominee.getPayoutMbr().setPayoutAnuityCalc(null);
							payoutEntityForNominee.getPayoutMbr().setPayoutAnuityCalc(anuityCalcList);

							tempPayoutEntity.getPayoutMbr().getPayoutPayeeBank().forEach(payoutPayeeBank -> {

								if (payoutMbrNomineeDtls.getNomineeCode().equals(payoutPayeeBank.getNomineeCode())
										&& ClaimConstants.ANNUITY_AMT_PAYABLE
												.equalsIgnoreCase(payoutPayeeBank.getType())) {
									nomineeBankList.add(payoutPayeeBank);
								}
							});
							payoutEntityForNominee.getPayoutMbr().setPayoutPayeeBank(null);
							payoutEntityForNominee.getPayoutMbr().setPayoutPayeeBank(nomineeBankList);

							payoutEntityForNominee.getPayoutMbr().setPayoutMbrAddresses(null);

						}
						PayoutAnnuityRestApiRes res = annutityRestApi(payoutEntityForNominee);
						isAnnuitySuccess = Boolean.TRUE;
						logger.info(res.getMessage());
					}
				} else {
					List<PayoutPayeeBankDetailsTempEntity> AnnuityMbrBankList = new ArrayList<>();
					TempPayoutEntity payoutEntityForMember = getDuplicatePayout(tempPayoutEntity);

					for (PayoutPayeeBankDetailsTempEntity bank : payoutEntityForMember.getPayoutMbr()
							.getPayoutPayeeBank()) {
						if (bank != null && ClaimConstants.ANNUITY_AMT_PAYABLE.equalsIgnoreCase(bank.getType())) {
							AnnuityMbrBankList.add(bank);
						}

					}
					payoutEntityForMember.getPayoutMbr().setPayoutPayeeBank(null);
					payoutEntityForMember.getPayoutMbr().setPayoutPayeeBank(AnnuityMbrBankList);

					PayoutAnnuityRestApiRes res = annutityRestApi(payoutEntityForMember);

					if (res != null) {
						isAnnuitySuccess = Boolean.TRUE;
					}
				}

			}

			AnnuitantDetailsUpdateInAnnuity(tempPayoutEntity.getMasterPolicyNo());

		} catch (Exception e) {

		}

		return isAnnuitySuccess;
	}

	private void updateBeneficiaryId(String payoutNo, String claimNo) {

		if (claimNo != null) {
			List<PayoutPayeeBankDetailsEntity> getbankDetails = payeeBankDetailsRepository.GetbankDetails(claimNo);
			if (getbankDetails != null) {
				for (PayoutPayeeBankDetailsEntity payoutPayeeBankDetailsEntity : getbankDetails) {

					Long bankAccountId = payoutPayeeBankDetailsEntity.getBankAccountId();
					Integer tempBankAccountId = payoutPayeeBankDetailsEntity.getTempBankAccountId();

					payeeBankDetailsTempRepository.updateMasterBankId(bankAccountId, tempBankAccountId);

				}
			}
		}
	}

	private TempPayoutEntity getDuplicatePayout(TempPayoutEntity oldPayoutEntity) {
		TempPayoutEntity payoutNew = new TempPayoutEntity();
		payoutNew.setMasterPolicyNo(oldPayoutEntity.getMasterPolicyNo());
		payoutNew.setCreatedBy(oldPayoutEntity.getCreatedBy());
		payoutNew.setCreatedOn(oldPayoutEntity.getCreatedOn());
		payoutNew.setUnitCode(oldPayoutEntity.getUnitCode());
		payoutNew.setVariant(oldPayoutEntity.getVariant());
		payoutNew.setProduct(oldPayoutEntity.getProduct());
		payoutNew.setDtOfExit(oldPayoutEntity.getDtOfExit());
		payoutNew.setModeOfExit(oldPayoutEntity.getModeOfExit());
		payoutNew.setPayoutMbr(convertMbrToOtherObject(oldPayoutEntity.getPayoutMbr()));
		payoutNew.setClaimNo(oldPayoutEntity.getClaimNo());
		payoutNew.setPolicyId(oldPayoutEntity.getPolicyId());
		return payoutNew;

	}

	private List<PayoutPayeeBankDetailsTempEntity> convertBankToOtherObject(
			List<PayoutPayeeBankDetailsTempEntity> oldPayoutPayeeBankDetailsList) {
		List<PayoutPayeeBankDetailsTempEntity> newPayeeList = new ArrayList<>();
		for (PayoutPayeeBankDetailsTempEntity oldPayoutPayeeBankDetailsTemp : oldPayoutPayeeBankDetailsList) {
			PayoutPayeeBankDetailsTempEntity newPayoutPayeeBankDetailsTemp = new PayoutPayeeBankDetailsTempEntity();
			newPayoutPayeeBankDetailsTemp.setAccountNumber(oldPayoutPayeeBankDetailsTemp.getAccountNumber());
			newPayoutPayeeBankDetailsTemp.setAccountType(oldPayoutPayeeBankDetailsTemp.getAccountType());
			newPayoutPayeeBankDetailsTemp.setBankAddressOne(oldPayoutPayeeBankDetailsTemp.getBankAddressOne());
			newPayoutPayeeBankDetailsTemp.setBankBranch(oldPayoutPayeeBankDetailsTemp.getBankBranch());
			newPayoutPayeeBankDetailsTemp.setCountryCode(oldPayoutPayeeBankDetailsTemp.getCountryCode());
			newPayoutPayeeBankDetailsTemp.setIfscCode(oldPayoutPayeeBankDetailsTemp.getIfscCode());
			newPayoutPayeeBankDetailsTemp.setLandlineNumber(oldPayoutPayeeBankDetailsTemp.getLandlineNumber());
			newPayoutPayeeBankDetailsTemp.setStdCode(oldPayoutPayeeBankDetailsTemp.getStdCode());
			newPayoutPayeeBankDetailsTemp.setType(oldPayoutPayeeBankDetailsTemp.getType());
			newPayeeList.add(newPayoutPayeeBankDetailsTemp);
		}
		return newPayeeList;

	}

	private List<TempPayoutMbrAddressEntity> convertAddressToOtherObject(List<TempPayoutMbrAddressEntity> list) {
		List<TempPayoutMbrAddressEntity> newAddressList = new ArrayList<>();
		for (TempPayoutMbrAddressEntity oldPayoutPayeeAddressDetails : list) {
			TempPayoutMbrAddressEntity newPayoutPayeeAddressDetails = new TempPayoutMbrAddressEntity();
			newPayoutPayeeAddressDetails.setAddressLineOne(oldPayoutPayeeAddressDetails.getAddressLineOne());
			newPayoutPayeeAddressDetails.setAddressLineTwo(oldPayoutPayeeAddressDetails.getAddressLineTwo());
			newPayoutPayeeAddressDetails.setAddressLineThree(oldPayoutPayeeAddressDetails.getAddressLineThree());
			newPayoutPayeeAddressDetails.setAddressType(oldPayoutPayeeAddressDetails.getAddressType());
			newPayoutPayeeAddressDetails.setCity(oldPayoutPayeeAddressDetails.getCity());
			newPayoutPayeeAddressDetails.setCountry(oldPayoutPayeeAddressDetails.getCountry());
			newPayoutPayeeAddressDetails.setCreatedBy(oldPayoutPayeeAddressDetails.getCreatedBy());
			newPayoutPayeeAddressDetails.setCreatedOn(oldPayoutPayeeAddressDetails.getCreatedOn());
			newPayoutPayeeAddressDetails.setDistrict(oldPayoutPayeeAddressDetails.getDistrict());
			newPayoutPayeeAddressDetails.setModifiedBy(oldPayoutPayeeAddressDetails.getModifiedBy());
			newPayoutPayeeAddressDetails.setModifiedOn(oldPayoutPayeeAddressDetails.getModifiedOn());
			newPayoutPayeeAddressDetails.setPinCode(oldPayoutPayeeAddressDetails.getPinCode());
			newPayoutPayeeAddressDetails.setState(oldPayoutPayeeAddressDetails.getState());
			newAddressList.add(newPayoutPayeeAddressDetails);

		}
		return newAddressList;

	}

	private TempPayoutMbrEntity convertMbrToOtherObject(TempPayoutMbrEntity oldTempPayoutMbrEntity) {
		TempPayoutMbrEntity newTempPayoutMbrEntity = new TempPayoutMbrEntity();
		newTempPayoutMbrEntity.setAadharNumber(oldTempPayoutMbrEntity.getAadharNumber());
		newTempPayoutMbrEntity.setCommunicationPreference(oldTempPayoutMbrEntity.getCommunicationPreference());
		newTempPayoutMbrEntity.setDateOfJoining(oldTempPayoutMbrEntity.getDateOfJoining());
		newTempPayoutMbrEntity.setFatherName(oldTempPayoutMbrEntity.getFatherName());
		newTempPayoutMbrEntity.setGender(oldTempPayoutMbrEntity.getGender());
		newTempPayoutMbrEntity.setLastName(oldTempPayoutMbrEntity.getLastName());
		newTempPayoutMbrEntity.setFirstName(oldTempPayoutMbrEntity.getFirstName());
		newTempPayoutMbrEntity.setMiddleName(oldTempPayoutMbrEntity.getMiddleName());
		newTempPayoutMbrEntity.setPan(oldTempPayoutMbrEntity.getPan());
		newTempPayoutMbrEntity.setDateOfBirth(oldTempPayoutMbrEntity.getDateOfBirth());
		newTempPayoutMbrEntity.setSpouseName(oldTempPayoutMbrEntity.getSpouseName());
		newTempPayoutMbrEntity
				.setPayoutAnuityCalc(convertAnnuityCalcToOtherObject(oldTempPayoutMbrEntity.getPayoutAnuityCalc()));
		newTempPayoutMbrEntity
				.setPayoutMbrNomineeDtls(convertNomineeToOtherObject(oldTempPayoutMbrEntity.getPayoutMbrNomineeDtls()));
		newTempPayoutMbrEntity
				.setPayoutPayeeBank(convertBankToOtherObject(oldTempPayoutMbrEntity.getPayoutPayeeBank()));

		newTempPayoutMbrEntity
				.setPayoutMbrAddresses(convertAddressToOtherObject(oldTempPayoutMbrEntity.getPayoutMbrAddresses()));
		newTempPayoutMbrEntity.setLicId(oldTempPayoutMbrEntity.getLicId());
		return newTempPayoutMbrEntity;
	}

	private List<TempPayoutAnnuityCalcEntity> convertAnnuityCalcToOtherObject(
			List<TempPayoutAnnuityCalcEntity> oldTempPayoutAnnuityCalc) {
		List<TempPayoutAnnuityCalcEntity> resAnnuity = new ArrayList<>();
		for (TempPayoutAnnuityCalcEntity annuity : oldTempPayoutAnnuityCalc) {
			TempPayoutAnnuityCalcEntity newTempPayoutAnnuityCalc = new TempPayoutAnnuityCalcEntity();
			newTempPayoutAnnuityCalc.setPension(annuity.getPension());
			newTempPayoutAnnuityCalc.setCreatedOn(annuity.getCreatedOn());
			newTempPayoutAnnuityCalc.setAnuityMode(annuity.getAnuityMode());
			newTempPayoutAnnuityCalc.setAnnuityOption(annuity.getAnnuityOption());
			newTempPayoutAnnuityCalc.setDateOfBirth(annuity.getDateOfBirth());
			newTempPayoutAnnuityCalc.setGstAmount(annuity.getGstAmount());
			newTempPayoutAnnuityCalc.setAmtPaidTo(annuity.getAmtPaidTo());
			newTempPayoutAnnuityCalc.setPurchasePrice(annuity.getPurchasePrice());
			newTempPayoutAnnuityCalc.setAnnuityPayableTo(annuity.getAnnuityPayableTo());
			newTempPayoutAnnuityCalc.setArrears(annuity.getArrears());
			newTempPayoutAnnuityCalc.setSpouseName(annuity.getSpouseName());
			resAnnuity.add(newTempPayoutAnnuityCalc);
		}
		return resAnnuity;
	}

	private List<TempPayoutMbrNomineeEntity> convertNomineeToOtherObject(
			List<TempPayoutMbrNomineeEntity> oldNomineeTemp) {
		List<TempPayoutMbrNomineeEntity> nomineeNew = new ArrayList<>();
		for (TempPayoutMbrNomineeEntity nominee : oldNomineeTemp) {
			TempPayoutMbrNomineeEntity nomineeOne = new TempPayoutMbrNomineeEntity();
			nomineeOne.setAadharNumber(nominee.getAadharNumber());
			nomineeOne.setAccountNumber(nominee.getAccountNumber());
			nomineeOne.setAccountType(nominee.getAccountType());
			nomineeOne.setSharedAmount(nominee.getSharedAmount());
			nomineeOne.setAddressOne(nominee.getAddressOne());
			nomineeOne.setBankBranch(nominee.getBankBranch());
			nomineeOne.setMobileNo(nominee.getMobileNo());
			nomineeOne.setDob(nominee.getDob());
			nomineeOne.setEmailId(nominee.getEmailId());
			nomineeOne.setIfscCode(nominee.getIfscCode());
			nomineeOne.setMaritalStatus(nominee.getMaritalStatus());
			nomineeOne.setCountry(nominee.getCountry());
			nomineeOne.setNomineeCode(nominee.getNomineeCode());
			nomineeNew.add(nomineeOne);
		}
		return nomineeNew;
	}

	@Override
	public ApiResponseDto<List<PayoutStoredProcedureResponseDto>> storeProcedureforAccount(Long payoutId) {
		ApiResponseDto<List<PayoutStoredProcedureResponseDto>> responseDto = new ApiResponseDto<>();
		logger.info("PayoutServiceImpl :: storeProcedureforAccount :: start ");
		try {
			List<PayoutStoredProcedureResponseDto> payoutStoredProcedureResponseDto = accountingPayoutStoreProcCall(
					payoutId);
			responseDto = ApiResponseDto.success(payoutStoredProcedureResponseDto, PayoutErrorConstants.APPROVED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutServiceImpl :: storeProcedureforAccount :: error is  " + e.getMessage());
		}
		logger.info("PayoutServiceImpl :: storeProcedureforAccount :: ended ");
		return responseDto;
	}

	private Double getsumShortReserveAmount(TempPayoutEntity tempPayoutEntity, String nomineeCode) {
//		Double commutatiomSum = 0d;
		Double annuitySum = 0d;
//		if (!tempPayoutEntity.getPayoutMbr().getPayoutCommutationCalc().isEmpty()) {
//			List<Double> commutaionSR = tempPayoutEntity.getPayoutMbr().getPayoutCommutationCalc().stream()
//					.map(i -> i.getShortReserve()).collect(Collectors.toList());
////			commutatiomSum = commutaionSR.stream().collect(Collectors.summingDouble(Double::doubleValue));
//			commutatiomSum=1d;
//
//		}
		if (!tempPayoutEntity.getPayoutMbr().getPayoutAnuityCalc().isEmpty()) {
			List<Double> annuitySR = tempPayoutEntity.getPayoutMbr().getPayoutAnuityCalc().stream()
					.map(i -> i.getShortReserve()).collect(Collectors.toList());
			annuitySum = annuitySR.stream().collect(Collectors.summingDouble(Double::doubleValue));
		}
		return annuitySum;
	}

	private PayoutStoredProcedureResponseDto getStoredProcedureInput(String type, Long payoutId,
			TempPayoutEntity payoutEntity, PayoutStoredProcedureResponseDto payoutStoredProcedureResponseDto,
			String benefiaryPaymentId, Long nomineeId, String nomineeCode, String annuityChallanNumber) {
		StoredProcedureQuery storedProcedure = null;
		try {

			if (type.equals(PayoutStoredProcedureConstants.PAYOUT_DEATH)) {
				storedProcedure = entityManager.createStoredProcedureQuery("CLAIM_PAYOUT_APPROVE_DEATH");
			} else
				storedProcedure = entityManager.createStoredProcedureQuery("CLAIM_PAYOUT_APPROVE");

			storedProcedure.registerStoredProcedureParameter("PAYOUTID", Long.class, ParameterMode.IN);
			storedProcedure.setParameter("PAYOUTID", payoutId);
			if (type.equals(PayoutStoredProcedureConstants.PAYOUT_DEATH)) {
				storedProcedure.registerStoredProcedureParameter("NOMINEEID", Long.class, ParameterMode.IN);
				storedProcedure.setParameter("NOMINEEID", nomineeId);
			}
			storedProcedure.registerStoredProcedureParameter("BENEFIARYPAYMENTID", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("ANNUITYCHALLANNO", String.class, ParameterMode.IN);
			storedProcedure.setParameter("BENEFIARYPAYMENTID", benefiaryPaymentId);
			storedProcedure.setParameter("ANNUITYCHALLANNO", annuityChallanNumber);
			storedProcedure.registerStoredProcedureParameter("CGSTRATE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("SGSTRATE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("IGSTRATE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("UTGSTRATE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("PRODUCTCODE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("VARIANTCODE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("ICODEVARIANT", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("CGSTAMOUNT", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("IGSTAMOUNT", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("SGSTAMOUNT", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("UTGSTAMOUNT", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("FROMSTATECODE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("TOSTATECODE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("DRSRACCN", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("CRSRACCN", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("ICODEFORLOBANNUITY", Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("ICODEFORPRODUCTLINEANNUITY", Integer.class,
					ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("ICODEFORVARIENTANNUITY", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("ICODEFORBUSINESSTYPEANNUITY", Integer.class,
					ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("ICODEFORPARTICIPATINGTYPEANNUITY", Integer.class,
					ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("ICODEFORBUSINESSSEGMENTANNUITY", Integer.class,
					ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("ICODEFORINVESTMENTPORTFOLIOANNUITY", Integer.class,
					ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("PRODUCTCODEANNUITY", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("VARIANTCODEANNUITY", String.class, ParameterMode.IN);
			setStoreProcParamtere(storedProcedure);

			logger.info("StoreProcedure-------getStoredProcedureInput----Started");
			JsonNode response = integrationService
					.getProductDetailsByProductId(NumericUtils.stringToLong(payoutEntity.getProduct()));

			if (response != null) {
				JsonNode proposeDetails = response.path("responseData");
				String product = proposeDetails.path("productCode").textValue();
				storedProcedure.setParameter("PRODUCTCODE", product);
				System.out.println("Product" + product);
				logger.info("PRODUCTCODE = " + product);
			}

			JsonNode newResponse = integrationService
					.getVariantDetailsByProductVariantId(NumericUtils.stringToLong(payoutEntity.getVariant()));
			if (newResponse != null) {
				JsonNode proposeDetails = newResponse.path("responseData");
				String variantCode = proposeDetails.path("subCategory").textValue();
				String variantVersion = proposeDetails.path("variantVersion").textValue();

				storedProcedure.setParameter("VARIANTCODE", variantCode);
				logger.info("VARIANTCODE = " + variantCode);
				Double totalShortReserve = 0d;

//				IcodeMasterEntity icodeMasterEntity = icodeMasterRepository.findByVariant(variantVersion);
				storedProcedure = setIcodeStoreProcedure(nomineeCode, payoutEntity, storedProcedure, variantVersion,
						totalShortReserve);
				/*** ShorReserve Sum ***/
				if (payoutEntity.getModeOfExit() != 1) {
					totalShortReserve = getsumShortReserveAmount(payoutEntity, nomineeCode);

					if (totalShortReserve != null && totalShortReserve > 0d) {
						storedProcedure.setParameter("CRSRACCN", totalShortReserve);
						storedProcedure.setParameter("DRSRACCN", 0d);
					} else if (totalShortReserve != null && totalShortReserve < 0d) {
						storedProcedure.setParameter("DRSRACCN", totalShortReserve * -1);
						storedProcedure.setParameter("CRSRACCN", 0d);
					} else {
						storedProcedure.setParameter("DRSRACCN", 0d);
						storedProcedure.setParameter("CRSRACCN", 0d);
					}
				}

				/*** ShorReserve Sum **{} End */

				/* Note:GSTCALCULATION */

				GstCalculationRequest gstCalculationRequest = new GstCalculationRequest();
				List<TempPayoutAnnuityCalcEntity> dtoList = payoutEntity.getPayoutMbr().getPayoutAnuityCalc();
				gstCalculationRequest.setAmount(
						!dtoList.isEmpty() ? NumericUtils.doubleToBigDecimal(dtoList.get(0).getPurchasePrice())
								: BigDecimal.ZERO);
				gstCalculationRequest.setHsnCode("997131");

				getStateCodeAndStateName(payoutEntity.getMasterPolicyNo(), payoutEntity.getUnitCode(),
						gstCalculationRequest);

				if (!dtoList.isEmpty() && dtoList.get(0).getGstBondBy() == 1) {
					gstCalculationRequest.setIsAmountWithTax("YES");
				} else {
					gstCalculationRequest.setIsAmountWithTax("No");
				}
				logger.info("ISAMOUNTWITHTAX:{}" + gstCalculationRequest.getIsAmountWithTax());

				GstCalculationResponse gstCalculationResponse = gstCalculatorService
						.callGstCalculation(gstCalculationRequest);
//				storedProcedure.setParameter("CGSTRATE", gstCalculationResponse.getcGstRate());
//				storedProcedure.setParameter("SGSTRATE", gstCalculationResponse.getsGstRate());
//				storedProcedure.setParameter("IGSTRATE", gstCalculationResponse.getiGstRate());
//				storedProcedure.setParameter("UTGSTRATE", gstCalculationResponse.getUtGstRate());
//				storedProcedure.setParameter("CGSTAMOUNT", gstCalculationResponse.getcGstRateAmount());
//				storedProcedure.setParameter("IGSTAMOUNT", gstCalculationResponse.getiGstRateAmount());
//				storedProcedure.setParameter("SGSTAMOUNT", gstCalculationResponse.getsGstRateAmount());
//				storedProcedure.setParameter("UTGSTAMOUNT", gstCalculationResponse.getUtGstRateAmount());

				storedProcedure.setParameter("CGSTRATE",
						gstCalculationResponse.getcGstRate().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getcGstRate())
								: "0");
				storedProcedure.setParameter("SGSTRATE",
						gstCalculationResponse.getsGstRate().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getsGstRate())
								: "0");
				storedProcedure.setParameter("IGSTRATE",
						gstCalculationResponse.getiGstRate().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getiGstRate())
								: "0");
				storedProcedure.setParameter("UTGSTRATE",
						gstCalculationResponse.getUtGstRate().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getUtGstRate())
								: "0");
				storedProcedure.setParameter("CGSTAMOUNT",
						gstCalculationResponse.getcGstRateAmount().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getcGstRateAmount())
								: "0");
				storedProcedure.setParameter("IGSTAMOUNT",
						gstCalculationResponse.getiGstRateAmount().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getiGstRateAmount())
								: "0");
				storedProcedure.setParameter("SGSTAMOUNT",
						gstCalculationResponse.getsGstRateAmount().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getsGstRateAmount())
								: "0");
				storedProcedure.setParameter("UTGSTAMOUNT",
						gstCalculationResponse.getUtGstRateAmount().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getUtGstRateAmount())
								: "0");
				storedProcedure.setParameter("FROMSTATECODE",
						gstCalculationRequest.getFromStateCode() != null ? gstCalculationRequest.getFromStateCode()
								: "0");
				storedProcedure.setParameter("TOSTATECODE",
						gstCalculationRequest.getToStateCode() != null ? gstCalculationRequest.getToStateCode() : "0");

//			logger.info("CGSTRATE",gstCalculationResponse.getcGstRate().compareTo(BigDecimal.ZERO) == 0?gstCalculationResponse.getcGstRate().intValue():0);
//			logger.info("SGSTRATE", gstCalculationResponse.getsGstRate().compareTo(BigDecimal.ZERO) == 0?gstCalculationResponse.getsGstRate():0);
//			logger.info("IGSTRATE", gstCalculationResponse.getiGstRate().compareTo(BigDecimal.ZERO) == 0?gstCalculationResponse.getiGstRate():0);
//			logger.info("UTGSTRATE", gstCalculationResponse.getUtGstRate().compareTo(BigDecimal.ZERO) == 0?gstCalculationResponse.getUtGstRate():0);
//			logger.info("CGSTAMOUNT", gstCalculationResponse.getcGstRateAmount().compareTo(BigDecimal.ZERO) == 0?gstCalculationResponse.getcGstRateAmount():0);
//			logger.info("IGSTAMOUNT", gstCalculationResponse.getiGstRateAmount().compareTo(BigDecimal.ZERO) == 0?gstCalculationResponse.getiGstRateAmount():0);
//			logger.info("SGSTAMOUNT", gstCalculationResponse.getsGstRateAmount().compareTo(BigDecimal.ZERO) == 0?gstCalculationResponse.getsGstRateAmount():0);
//			logger.info("UTGSTAMOUNT", gstCalculationResponse.getUtGstRateAmount().compareTo(BigDecimal.ZERO) == 0?gstCalculationResponse.getUtGstRateAmount():0);
				logger.info("CGSTRATE",
						gstCalculationResponse.getcGstRate().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getcGstRate())
								: "0");
				logger.info("SGSTRATE",
						gstCalculationResponse.getsGstRate().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getsGstRate())
								: "0");
				logger.info("IGSTRATE",
						gstCalculationResponse.getiGstRate().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getiGstRate())
								: "0");
				logger.info("UTGSTRATE",
						gstCalculationResponse.getUtGstRate().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getUtGstRate())
								: "0");
				logger.info("CGSTAMOUNT",
						gstCalculationResponse.getcGstRateAmount().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getcGstRateAmount())
								: "0");
				logger.info("IGSTAMOUNT",
						gstCalculationResponse.getiGstRateAmount().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getiGstRateAmount())
								: "0");
				logger.info("SGSTAMOUNT",
						gstCalculationResponse.getsGstRateAmount().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getsGstRateAmount())
								: "0");
				logger.info("UTGSTAMOUNT",
						gstCalculationResponse.getUtGstRateAmount().compareTo(BigDecimal.ZERO) != 0
								? NumericUtils.convertBigDecimalToString(gstCalculationResponse.getUtGstRateAmount())
								: "0");
				logger.info("FROMSTATECODE",
						gstCalculationRequest.getFromStateCode() != null ? gstCalculationRequest.getFromStateCode()
								: "0");
				logger.info("TOSTATECODE",
						gstCalculationRequest.getToStateCode() != null ? gstCalculationRequest.getToStateCode() : "0");

//				if (icodeMasterEntity != null) {
//
//					if (variantVersion.equalsIgnoreCase(PayoutStoredProcedureConstants.VARIANT_V1)) {
//						storedProcedure.setParameter("ICODEVARIANT", icodeMasterEntity.getIcodeForVariant());
//						storedProcedure.setParameter("ICODEFORLOBANNUITY", icodeMasterEntity.getIcodeForLob());
//						storedProcedure.setParameter("ICODEFORPRODUCTLINEANNUITY",
//								icodeMasterEntity.getIcodeForProductLine());
//						storedProcedure.setParameter("ICODEFORVARIENTANNUITY", icodeMasterEntity.getIcodeForVariant());
//						storedProcedure.setParameter("ICODEFORBUSINESSTYPEANNUITY",
//								icodeMasterEntity.getIcodeForBusinessType());
//						storedProcedure.setParameter("ICODEFORPARTICIPATINGTYPEANNUITY",
//								icodeMasterEntity.getIcodeForParticipating());
//						storedProcedure.setParameter("ICODEFORBUSINESSSEGMENTANNUITY",
//								icodeMasterEntity.getIcodeForBusinessSegment());
//						storedProcedure.setParameter("ICODEFORINVESTMENTPORTFOLIOANNUITY",
//								icodeMasterEntity.getIcodeForInvestmentPortFolio());
//					}
//					if (variantVersion.equalsIgnoreCase(PayoutStoredProcedureConstants.VARIANT_V2)) {
//						storedProcedure.setParameter("ICODEVARIANT", icodeMasterEntity.getIcodeForVariant());
//						storedProcedure.setParameter("ICODEFORLOBANNUITY", icodeMasterEntity.getIcodeForLob());
//						storedProcedure.setParameter("ICODEFORPRODUCTLINEANNUITY",
//								icodeMasterEntity.getIcodeForProductLine());
//						storedProcedure.setParameter("ICODEFORVARIENTANNUITY", icodeMasterEntity.getIcodeForVariant());
//						storedProcedure.setParameter("ICODEFORBUSINESSTYPEANNUITY",
//								icodeMasterEntity.getIcodeForBusinessType());
//						storedProcedure.setParameter("ICODEFORPARTICIPATINGTYPEANNUITY",
//								icodeMasterEntity.getIcodeForParticipating());
//						storedProcedure.setParameter("ICODEFORBUSINESSSEGMENTANNUITY",
//								icodeMasterEntity.getIcodeForBusinessSegment());
//						storedProcedure.setParameter("ICODEFORINVESTMENTPORTFOLIOANNUITY",
//								icodeMasterEntity.getIcodeForInvestmentPortFolio());
//					}
//					if (variantVersion.equalsIgnoreCase(PayoutStoredProcedureConstants.VARIANT_V3)) {
//						storedProcedure.setParameter("ICODEVARIANT", icodeMasterEntity.getIcodeForVariant());
//						storedProcedure.setParameter("ICODEFORLOBANNUITY", icodeMasterEntity.getIcodeForLob());
//						storedProcedure.setParameter("ICODEFORPRODUCTLINEANNUITY",
//								icodeMasterEntity.getIcodeForProductLine());
//						storedProcedure.setParameter("ICODEFORVARIENTANNUITY", icodeMasterEntity.getIcodeForVariant());
//						storedProcedure.setParameter("ICODEFORBUSINESSTYPEANNUITY",
//								icodeMasterEntity.getIcodeForBusinessType());
//						storedProcedure.setParameter("ICODEFORPARTICIPATINGTYPEANNUITY",
//								icodeMasterEntity.getIcodeForParticipating());
//						storedProcedure.setParameter("ICODEFORBUSINESSSEGMENTANNUITY",
//								icodeMasterEntity.getIcodeForBusinessSegment());
//						storedProcedure.setParameter("ICODEFORINVESTMENTPORTFOLIOANNUITY",
//								icodeMasterEntity.getIcodeForInvestmentPortFolio());
//					}
//					if (payoutEntity.getProduct().equalsIgnoreCase(ClaimConstants.GSADA_PRODUCT)) {
//
//						if (payoutEntity.getVariant().equalsIgnoreCase(ClaimConstants.GSADA_VARIANT)) {
//							IcodeMasterEntity icodeMasterEntityGSSDA = icodeMasterRepository
//									.findByIcodeForVariant("116");
//							storedProcedure.setParameter("ICODEFORLOBANNUITY",
//									icodeMasterEntityGSSDA.getIcodeForLob());
//							storedProcedure.setParameter("ICODEFORPRODUCTLINEANNUITY",
//									icodeMasterEntityGSSDA.getIcodeForProductLine());
//							storedProcedure.setParameter("ICODEFORVARIENTANNUITY",
//									icodeMasterEntityGSSDA.getIcodeForVariant());
//							storedProcedure.setParameter("ICODEFORBUSINESSTYPEANNUITY",
//									icodeMasterEntityGSSDA.getIcodeForBusinessType());
//							storedProcedure.setParameter("ICODEFORPARTICIPATINGTYPEANNUITY",
//									icodeMasterEntityGSSDA.getIcodeForParticipating());
//							storedProcedure.setParameter("ICODEFORBUSINESSSEGMENTANNUITY",
//									icodeMasterEntityGSSDA.getIcodeForBusinessSegment());
//							storedProcedure.setParameter("ICODEFORINVESTMENTPORTFOLIOANNUITY",
//									icodeMasterEntityGSSDA.getIcodeForInvestmentPortFolio());
//
//						} else {
//							IcodeMasterEntity icodeMaster = icodeMasterRepository.findByIcodeForVariant("102");
//							storedProcedure.setParameter("ICODEFORLOBANNUITY", icodeMaster.getIcodeForLob());
//							storedProcedure.setParameter("ICODEFORPRODUCTLINEANNUITY",
//									icodeMaster.getIcodeForProductLine());
//							storedProcedure.setParameter("ICODEFORVARIENTANNUITY", icodeMaster.getIcodeForVariant());
//							storedProcedure.setParameter("ICODEFORBUSINESSTYPEANNUITY",
//									icodeMaster.getIcodeForBusinessType());
//							storedProcedure.setParameter("ICODEFORPARTICIPATINGTYPEANNUITY",
//									icodeMaster.getIcodeForParticipating());
//							storedProcedure.setParameter("ICODEFORBUSINESSSEGMENTANNUITY",
//									icodeMaster.getIcodeForBusinessSegment());
//							storedProcedure.setParameter("ICODEFORINVESTMENTPORTFOLIOANNUITY",
//									icodeMaster.getIcodeForInvestmentPortFolio());
//						}
//
//					}
//
//				}
			}

			TempPayoutMbrEntity payoutMbrEntity = payoutEntity.getPayoutMbr();

			if (payoutMbrEntity != null) {

//				storedProcedure.setParameter("CGSTRATE", "0");
//				storedProcedure.setParameter("SGSTRATE", "0");
//				storedProcedure.setParameter("IGSTRATE", "0");
//				storedProcedure.setParameter("UTGSTRATE", "0");
//		storedProcedure.setParameter("CGSTAMOUNT", "0");
//    	storedProcedure.setParameter("IGSTAMOUNT", "0");
//    	storedProcedure.setParameter("SGSTAMOUNT", "0");
//    	storedProcedure.setParameter("UTGSTAMOUNT","0");

				if (!payoutMbrEntity.getPayoutAnuityCalc().isEmpty()) {

					TempPayoutAnnuityCalcEntity payoutcal = payoutMbrEntity.getPayoutAnuityCalc().get(0);

					if (payoutcal != null && payoutcal.getIsGSTApplicable().equals(true)) {

						/*
						 * PayoutStoredProcedureDto payoutStoredProcedureDto = getAccountingDetails();
						 * if(payoutStoredProcedureDto != null) {
						 * storedProcedure.setParameter("CGSTRATE",
						 * payoutStoredProcedureDto.getCgstRate().toString());
						 * storedProcedure.setParameter("SGSTRATE",
						 * payoutStoredProcedureDto.getSgstRate().toString());
						 * storedProcedure.setParameter("IGSTRATE",
						 * payoutStoredProcedureDto.getIgstRate().toString());
						 * storedProcedure.setParameter("UTGSTRATE",
						 * payoutStoredProcedureDto.getUtgstRate()); }
						 */

//						storedProcedure.setParameter("CGSTRATE", "9");
//						storedProcedure.setParameter("SGSTRATE", "9");
//						storedProcedure.setParameter("IGSTRATE", "18");
//						storedProcedure.setParameter("UTGSTRATE", "9");

//   Object[] result = mphAddressRepository.getAddressDetails(payoutEntity.getMasterPolicyNo());		
//		
//		if(result != null) {
//		
//		Object[] obj = (Object[]) result;
//		
//		
//		Object[] object = commonAccountTypeRepository.getStateDetails(NumericUtils.convertStringToLong(String.valueOf(obj[0])));
//		
//		
//		
//		Object[] newObj = (Object[]) object;
//		
//		
//		Object[] updateObject = commonAccountTypeRepository.getStateNameDetails(payoutEntity.getUnitCode());
//		
//         Object[] newObject = (Object[]) updateObject;
//         
//         if(object != null && updateObject != null) {
//         
//        if(String.valueOf(newObj[4]).equalsIgnoreCase(String.valueOf(newObject[13]))) {
//        	
//        	if(String.valueOf(newObj[10]).equalsIgnoreCase(PayoutStoredProcedureConstants.STATE)) {
//        	
//        	String cgstAmount = String.valueOf(payoutcal.getGstAmount()/2);
//        	String sgstAmount = String.valueOf(payoutcal.getGstAmount()/2);
//        	storedProcedure.setParameter("CGSTAMOUNT", cgstAmount);
//        	storedProcedure.setParameter("SGSTAMOUNT", sgstAmount);
//        	}
//        	else {
//        		String cgstAmount = String.valueOf(payoutcal.getGstAmount()/2);
//            	String utgstAmount = String.valueOf(payoutcal.getGstAmount()/2);
//            	storedProcedure.setParameter("CGSTAMOUNT", cgstAmount);
//            	storedProcedure.setParameter("UTGSTAMOUNT", utgstAmount);
//        	}
//        }
//        else {
//        	String igstAmount = String.valueOf(payoutcal.getGstAmount());
//        	storedProcedure.setParameter("IGSTAMOUNT", igstAmount);
//        }
//		}
//		}
//	

					}
				}
				storedProcedure.execute();

				logger.info("SA_OUT_JOURNAL_NO :: {} ", storedProcedure.getOutputParameterValue("SA_OUT_JOURNAL_NO"));
				payoutStoredProcedureResponseDto
						.setJournalNo(storedProcedure.getOutputParameterValue("SA_OUT_JOURNAL_NO"));

				logger.info("SA_OUT_DEBITACCOUNT :: {} ",
						storedProcedure.getOutputParameterValue("SA_OUT_DEBITACCOUNT"));
				payoutStoredProcedureResponseDto
						.setDebitaccount(storedProcedure.getOutputParameterValue("SA_OUT_DEBITACCOUNT"));

				logger.info("SA_OUT_PAYMENTCREDITACCOUNT :: {} ",
						storedProcedure.getOutputParameterValue("SA_OUT_PAYMENTCREDITACCOUNT"));
				payoutStoredProcedureResponseDto
						.setCreditaccount(storedProcedure.getOutputParameterValue("SA_OUT_PAYMENTCREDITACCOUNT"));

				logger.info("SA_OUT_TDSCREDITACCOUNT :: {} ",
						storedProcedure.getOutputParameterValue("SA_OUT_TDSCREDITACCOUNT"));
				payoutStoredProcedureResponseDto
						.setTdsCreditaccount(storedProcedure.getOutputParameterValue("SA_OUT_TDSCREDITACCOUNT"));

				logger.info("SA_OUT_TOTALAMOUNT :: {} ", storedProcedure.getOutputParameterValue("SA_OUT_TOTALAMOUNT"));
				payoutStoredProcedureResponseDto
						.setTotalamount(storedProcedure.getOutputParameterValue("SA_OUT_TOTALAMOUNT"));

				logger.info("SA_OUT_PAYMENTAMOUNT :: {} ",
						storedProcedure.getOutputParameterValue("SA_OUT_PAYMENTAMOUNT"));
				payoutStoredProcedureResponseDto
						.setPaymentamount(storedProcedure.getOutputParameterValue("SA_OUT_PAYMENTAMOUNT"));

				logger.info("SA_OUT_TdsAMOUNT :: {} ", storedProcedure.getOutputParameterValue("SA_OUT_TdsAMOUNT"));
				payoutStoredProcedureResponseDto
						.setTdsamount(storedProcedure.getOutputParameterValue("SA_OUT_TdsAMOUNT"));

				logger.info("SA_OUT_CREDITICODE :: {} ", storedProcedure.getOutputParameterValue("SA_OUT_CREDITICODE"));
				payoutStoredProcedureResponseDto
						.setCrediticode(storedProcedure.getOutputParameterValue("SA_OUT_CREDITICODE"));

				logger.info("SA_OUT_DEBITICODE :: {} ", storedProcedure.getOutputParameterValue("SA_OUT_DEBITICODE"));
				payoutStoredProcedureResponseDto
						.setDebiticode(storedProcedure.getOutputParameterValue("SA_OUT_DEBITICODE"));

				logger.info("SA_OUT_MESSAGE :: {} ", storedProcedure.getOutputParameterValue("SA_OUT_MESSAGE"));
				payoutStoredProcedureResponseDto.setMessage(storedProcedure.getOutputParameterValue("SA_OUT_MESSAGE"));

				logger.info("SA_OUT_STATUS :: {} ", storedProcedure.getOutputParameterValue("SA_OUT_STATUS"));
				payoutStoredProcedureResponseDto.setStatus(storedProcedure.getOutputParameterValue("SA_OUT_STATUS"));

				logger.info("SA_SQLCODE :: {} ", storedProcedure.getOutputParameterValue("SA_SQLCODE"));
				payoutStoredProcedureResponseDto.setSqlcode(storedProcedure.getOutputParameterValue("SA_SQLCODE"));

				logger.info("SA_SQLERROR_MESSAGE :: {} ",
						storedProcedure.getOutputParameterValue("SA_SQLERROR_MESSAGE"));
				payoutStoredProcedureResponseDto
						.setSqlerrorMessage(storedProcedure.getOutputParameterValue("SA_SQLERROR_MESSAGE"));

			}
		} catch (IOException e) {
			logger.info("PayoutServiceImpl:getStoredProcedureInput--ERROR--", e);
		}
		return payoutStoredProcedureResponseDto;
	}

	private void getStateCodeAndStateName(String policyNumber, String unitCode,
			GstCalculationRequest gstCalculationRequest) throws IOException {
		logger.info("PayoutServiceImpl:getStateCodeAndStateName-Start");
		Integer stateId = 0;

		stateId = mphAddressRepository.getStateId(policyNumber);

		if (unitCode != null) {
			JsonNode gstStateResponse = integrationService.getStateCodeAndStateName(unitCode);
			if (gstStateResponse != null) {
				JsonNode stateDetails = gstStateResponse.path("Data");
				gstCalculationRequest.setToStateCode(stateDetails.path("stateCode").textValue());
				gstCalculationRequest.setToStateName(stateDetails.path("description").textValue());

			}
		}

		if (stateId != null) {
			StateDetailsDto dto = integrationService.getStatedetailsBystateId(stateId);
			if (dto.getStateCode() != null && dto.getDescription() != null) {
				gstCalculationRequest.setFromStateCode(dto.getStateCode());
				gstCalculationRequest.setFromStateName(dto.getDescription());

			}

		}
		gstCalculationRequest.setFromStateCode(
				gstCalculationRequest.getFromStateCode() != null ? gstCalculationRequest.getFromStateCode()
						: gstCalculationRequest.getToStateCode());
		gstCalculationRequest.setFromStateName(
				gstCalculationRequest.getFromStateName() != null ? gstCalculationRequest.getFromStateName()
						: gstCalculationRequest.getToStateName());

		System.out.println(gstCalculationRequest.getFromStateCode() + "_ " + gstCalculationRequest.getFromStateCode());

	}

	private StoredProcedureQuery setIcodeStoreProcedure(String nomineeCode, TempPayoutEntity tempPayout,
			StoredProcedureQuery storedProcedure, String variantVersion, Double totalShortReserve) {
		logger.info("setIcodeForCommutationAndAnnuity----Start{}");
		TempPayoutCommutationCalcEntity tempCommu = null;
		TempPayoutAnnuityCalcEntity tempAnnuity = null;
		if (tempPayout.getModeOfExit() == 1) {

			if (!tempPayout.getPayoutMbr().getPayoutCommutationCalc().isEmpty()) {
				for (TempPayoutCommutationCalcEntity entity : tempPayout.getPayoutMbr().getPayoutCommutationCalc()) {
					if (entity.getNomineeCode().equalsIgnoreCase(nomineeCode) && entity.getPurchasePrice() > 0d) {
						tempCommu = entity;

					}
				}
			}
			if (!tempPayout.getPayoutMbr().getPayoutAnuityCalc().isEmpty()) {
				for (TempPayoutAnnuityCalcEntity annuityEntity : tempPayout.getPayoutMbr().getPayoutAnuityCalc()) {
					if (annuityEntity.getNomineeCode().equalsIgnoreCase(nomineeCode)
							&& annuityEntity.getPurchasePrice() > 0d) {
						tempAnnuity = annuityEntity;
						totalShortReserve = annuityEntity.getShortReserve();
					}
				}
			}
			setIcodeForCommutaionAndAnnuity(tempCommu, tempAnnuity, storedProcedure, variantVersion, totalShortReserve);
		} else {
			if (!tempPayout.getPayoutMbr().getPayoutCommutationCalc().isEmpty()) {
				tempCommu = tempPayout.getPayoutMbr().getPayoutCommutationCalc().get(0);
			}
			if (!tempPayout.getPayoutMbr().getPayoutAnuityCalc().isEmpty()) {
				tempAnnuity = tempPayout.getPayoutMbr().getPayoutAnuityCalc().get(0);
				totalShortReserve = tempAnnuity.getShortReserve();
			}
			setIcodeForCommutaionAndAnnuity(tempCommu, tempAnnuity, storedProcedure, variantVersion, totalShortReserve);
		}
		return storedProcedure;
	}

	private void setIcodeForCommutaionAndAnnuity(TempPayoutCommutationCalcEntity tempCommu,
			TempPayoutAnnuityCalcEntity tempAnnuity, StoredProcedureQuery storedProcedure, String variantVersion,
			Double totalShortReserve) {
		IcodeMasterEntity icodeMasterEntity = icodeMasterRepository.findByVariant(variantVersion);
		IcodeMasterEntity icodeMasterEntityAnnuity = icodeMasterRepository.findByIcodeForVariant("102");

		if (icodeMasterEntity != null) {

			if (variantVersion.equalsIgnoreCase(PayoutStoredProcedureConstants.VARIANT_V1)) {
				if (tempCommu != null) {
					storedProcedure.setParameter("ICODEVARIANT", icodeMasterEntity.getIcodeForVariant());
				} else {
					storedProcedure.setParameter("ICODEVARIANT", "0");
				}
				if (tempAnnuity != null) {

					storedProcedure.setParameter("ICODEFORLOBANNUITY", icodeMasterEntityAnnuity.getIcodeForLob());
					storedProcedure.setParameter("ICODEFORPRODUCTLINEANNUITY",
							icodeMasterEntityAnnuity.getIcodeForProductLine());
					storedProcedure.setParameter("ICODEFORVARIENTANNUITY",
							icodeMasterEntityAnnuity.getIcodeForVariant());
					storedProcedure.setParameter("ICODEFORBUSINESSTYPEANNUITY",
							icodeMasterEntityAnnuity.getIcodeForBusinessType());
					storedProcedure.setParameter("ICODEFORPARTICIPATINGTYPEANNUITY",
							icodeMasterEntityAnnuity.getIcodeForParticipating());
					storedProcedure.setParameter("ICODEFORBUSINESSSEGMENTANNUITY",
							icodeMasterEntityAnnuity.getIcodeForBusinessSegment());
					storedProcedure.setParameter("ICODEFORINVESTMENTPORTFOLIOANNUITY",
							icodeMasterEntityAnnuity.getIcodeForInvestmentPortFolio());
					storedProcedure.setParameter("VARIANTCODEANNUITY", icodeMasterEntityAnnuity.getVariant());
					storedProcedure.setParameter("PRODUCTCODEANNUITY", icodeMasterEntityAnnuity.getProduct());
				} else {
					storedProcedure.setParameter("ICODEFORLOBANNUITY", 0);
					storedProcedure.setParameter("ICODEFORPRODUCTLINEANNUITY", 0);
					storedProcedure.setParameter("ICODEFORVARIENTANNUITY", "0");
					storedProcedure.setParameter("ICODEFORBUSINESSTYPEANNUITY", 0);
					storedProcedure.setParameter("ICODEFORPARTICIPATINGTYPEANNUITY", 0);
					storedProcedure.setParameter("ICODEFORBUSINESSSEGMENTANNUITY", 0);
					storedProcedure.setParameter("ICODEFORINVESTMENTPORTFOLIOANNUITY", 0);
					storedProcedure.setParameter("VARIANTCODEANNUITY", "0");
					storedProcedure.setParameter("PRODUCTCODEANNUITY", "0");
				}

			}
			if (variantVersion.equalsIgnoreCase(PayoutStoredProcedureConstants.VARIANT_V2)) {
				if (tempCommu != null) {
					storedProcedure.setParameter("ICODEVARIANT", icodeMasterEntity.getIcodeForVariant());
				} else {
					storedProcedure.setParameter("ICODEVARIANT", "0");
				}
				if (tempAnnuity != null) {

					storedProcedure.setParameter("ICODEFORLOBANNUITY", icodeMasterEntityAnnuity.getIcodeForLob());
					storedProcedure.setParameter("ICODEFORPRODUCTLINEANNUITY",
							icodeMasterEntityAnnuity.getIcodeForProductLine());
					storedProcedure.setParameter("ICODEFORVARIENTANNUITY",
							icodeMasterEntityAnnuity.getIcodeForVariant());
					storedProcedure.setParameter("ICODEFORBUSINESSTYPEANNUITY",
							icodeMasterEntityAnnuity.getIcodeForBusinessType());
					storedProcedure.setParameter("ICODEFORPARTICIPATINGTYPEANNUITY",
							icodeMasterEntityAnnuity.getIcodeForParticipating());
					storedProcedure.setParameter("ICODEFORBUSINESSSEGMENTANNUITY",
							icodeMasterEntityAnnuity.getIcodeForBusinessSegment());
					storedProcedure.setParameter("ICODEFORINVESTMENTPORTFOLIOANNUITY",
							icodeMasterEntityAnnuity.getIcodeForInvestmentPortFolio());
					storedProcedure.setParameter("VARIANTCODEANNUITY", icodeMasterEntityAnnuity.getVariant());
					storedProcedure.setParameter("PRODUCTCODEANNUITY", icodeMasterEntityAnnuity.getProduct());
				} else {
					storedProcedure.setParameter("ICODEFORLOBANNUITY", 0);
					storedProcedure.setParameter("ICODEFORPRODUCTLINEANNUITY", 0);
					storedProcedure.setParameter("ICODEFORVARIENTANNUITY", "0");
					storedProcedure.setParameter("ICODEFORBUSINESSTYPEANNUITY", 0);
					storedProcedure.setParameter("ICODEFORPARTICIPATINGTYPEANNUITY", 0);
					storedProcedure.setParameter("ICODEFORBUSINESSSEGMENTANNUITY", 0);
					storedProcedure.setParameter("ICODEFORINVESTMENTPORTFOLIOANNUITY", 0);
					storedProcedure.setParameter("VARIANTCODEANNUITY", "0");
					storedProcedure.setParameter("PRODUCTCODEANNUITY", "0");
				}
			}
			if (variantVersion.equalsIgnoreCase(PayoutStoredProcedureConstants.VARIANT_V3)) {
				if (tempCommu != null) {
					storedProcedure.setParameter("ICODEVARIANT", icodeMasterEntity.getIcodeForVariant());
				} else {
					storedProcedure.setParameter("ICODEVARIANT", "0");
				}
				if (tempAnnuity != null) {

					storedProcedure.setParameter("ICODEFORLOBANNUITY", icodeMasterEntityAnnuity.getIcodeForLob());
					storedProcedure.setParameter("ICODEFORPRODUCTLINEANNUITY",
							icodeMasterEntityAnnuity.getIcodeForProductLine());
					storedProcedure.setParameter("ICODEFORVARIENTANNUITY",
							icodeMasterEntityAnnuity.getIcodeForVariant());
					storedProcedure.setParameter("ICODEFORBUSINESSTYPEANNUITY",
							icodeMasterEntityAnnuity.getIcodeForBusinessType());
					storedProcedure.setParameter("ICODEFORPARTICIPATINGTYPEANNUITY",
							icodeMasterEntityAnnuity.getIcodeForParticipating());
					storedProcedure.setParameter("ICODEFORBUSINESSSEGMENTANNUITY",
							icodeMasterEntityAnnuity.getIcodeForBusinessSegment());
					storedProcedure.setParameter("ICODEFORINVESTMENTPORTFOLIOANNUITY",
							icodeMasterEntityAnnuity.getIcodeForInvestmentPortFolio());
					storedProcedure.setParameter("VARIANTCODEANNUITY", icodeMasterEntityAnnuity.getVariant());
					storedProcedure.setParameter("PRODUCTCODEANNUITY", icodeMasterEntityAnnuity.getProduct());
				} else {
					storedProcedure.setParameter("ICODEFORLOBANNUITY", 0);
					storedProcedure.setParameter("ICODEFORPRODUCTLINEANNUITY", 0);
					storedProcedure.setParameter("ICODEFORVARIENTANNUITY", "0");
					storedProcedure.setParameter("ICODEFORBUSINESSTYPEANNUITY", 0);
					storedProcedure.setParameter("ICODEFORPARTICIPATINGTYPEANNUITY", 0);
					storedProcedure.setParameter("ICODEFORBUSINESSSEGMENTANNUITY", 0);
					storedProcedure.setParameter("ICODEFORINVESTMENTPORTFOLIOANNUITY", 0);
					storedProcedure.setParameter("VARIANTCODEANNUITY", "0");
					storedProcedure.setParameter("PRODUCTCODEANNUITY", "0");
				}
			}

			if (totalShortReserve != null && totalShortReserve > 0d) {
				storedProcedure.setParameter("CRSRACCN", totalShortReserve);
				storedProcedure.setParameter("DRSRACCN", 0d);
				logger.info("CRSRACCN :" + totalShortReserve);
			} else if (totalShortReserve != null && totalShortReserve < 0d) {
				storedProcedure.setParameter("DRSRACCN", totalShortReserve * -1);
				storedProcedure.setParameter("CRSRACCN", 0d);
				logger.info("DRSRACCN :" + totalShortReserve);
			} else {
				storedProcedure.setParameter("DRSRACCN", 0d);
				storedProcedure.setParameter("CRSRACCN", 0d);
			}
		}
	}

	private StoredProcedureResponseEntity saveStoredProcedureResponse(Long payoutId, String benefiaryPaymentId,
			PayoutStoredProcedureResponseDto payoutStoredProcedureResponseDto, String annuityChallanNumber) {
		StoredProcedureResponseEntity storedProcedureResponseEntity = new StoredProcedureResponseEntity();
		if (payoutStoredProcedureResponseDto != null) {
			storedProcedureResponseEntity.setPayoutId(payoutId);
			storedProcedureResponseEntity.setJournalNo(
					NumericUtils.stringToLong(String.valueOf(payoutStoredProcedureResponseDto.getJournalNo())));
			storedProcedureResponseEntity.setPaymentCreditAccount(NumericUtils
					.stringToLong(String.valueOf(payoutStoredProcedureResponseDto.getPaymentCreditaccount())));
			storedProcedureResponseEntity.setTdsCreditAccount(
					NumericUtils.stringToLong(String.valueOf(payoutStoredProcedureResponseDto.getTdsCreditaccount())));
			storedProcedureResponseEntity.setTotalAmount(
					NumericUtils.stringToLong(String.valueOf(payoutStoredProcedureResponseDto.getTotalamount())));
			storedProcedureResponseEntity.setPaymentAmount(
					NumericUtils.stringToLong(String.valueOf(payoutStoredProcedureResponseDto.getPaymentamount())));
			storedProcedureResponseEntity.setTdsAmount(
					NumericUtils.stringToLong(String.valueOf(payoutStoredProcedureResponseDto.getTdsamount())));
			storedProcedureResponseEntity
					.setCreditCode(String.valueOf(payoutStoredProcedureResponseDto.getTdsCreditaccount()));
			storedProcedureResponseEntity
					.setDebitCode(String.valueOf(payoutStoredProcedureResponseDto.getDebitaccount()));
			storedProcedureResponseEntity.setMessage(String.valueOf(payoutStoredProcedureResponseDto.getMessage()));
			storedProcedureResponseEntity.setStatus(String.valueOf(payoutStoredProcedureResponseDto.getStatus()));
			storedProcedureResponseEntity.setSqlCode(
					NumericUtils.stringToLong(String.valueOf(payoutStoredProcedureResponseDto.getSqlcode())));
			storedProcedureResponseEntity
					.setSqlErrorMessage(String.valueOf(payoutStoredProcedureResponseDto.getSqlerrorMessage()));
			storedProcedureResponseEntity.setCreatedOn(DateUtils.sysDate());
			storedProcedureResponseEntity.setChallanNumber(annuityChallanNumber);
			storedProcedureResponseEntity.setBenefiaryPaymentId(benefiaryPaymentId);
			storedProcedureResponseRepository.save(storedProcedureResponseEntity);
		}
		return storedProcedureResponseEntity;
	}

	private List<PayoutStoredProcedureResponseDto> accountingPayoutStoreProcCall(Long payoutId) {
		logger.info("payoutAccountingRequest is accountingPayoutStoreProcCall");
		PayoutStoredProcedureResponseDto payoutStoredProcedureResponseDto = new PayoutStoredProcedureResponseDto();
		List<PayoutStoredProcedureResponseDto> payoutStoredProcedureResponseDtos = new ArrayList<>();
		String benefiaryPaymentId = "";
		String annuityChallanNumber = "";
		try {

			TempPayoutEntity payoutEntity = tempPayoutRepository.findByPayoutIdAndIsActiveTrue(payoutId);
			TempPayoutMbrEntity payoutMbrEntity = payoutEntity.getPayoutMbr();
			TempPayoutCommutationCalcEntity commutationCalcEntity = payoutMbrEntity.getPayoutCommutationCalc().get(0);

			if (payoutEntity != null) {

				if (payoutEntity.getModeOfExit().equals(PayoutStoredProcedureConstants.DEATH)) {

					List<TempPayoutMbrNomineeEntity> tempPayoutMbrNomineeEntityList = payoutMbrEntity
							.getPayoutMbrNomineeDtls();

					for (TempPayoutMbrNomineeEntity tempPayoutMbrNomineeEntity : tempPayoutMbrNomineeEntityList) {
//						benefiaryPaymentId = getStoreProcedureSeq();
						annuityChallanNumber = getStoreProcedureSeqAnnuityChallanNo();
						List<PayoutPayeeBankDetailsTempEntity> payeeList = payoutEntity.getPayoutMbr()
								.getPayoutPayeeBank().stream()
								.filter(i -> (i.getNomineeCode()
										.equalsIgnoreCase(tempPayoutMbrNomineeEntity.getNomineeCode())
										&& i.getIsActive() == true
										&& (i.getType().equalsIgnoreCase(ClaimConstants.COMMUTATION))
										|| (i.getType().equalsIgnoreCase(ClaimConstants.ANNUITY)
												&& (commutationCalcEntity.getCommutationPerc() == null
														|| commutationCalcEntity.getCommutationPerc() == 0d)
												&& (commutationCalcEntity.getSlab() == null
														|| commutationCalcEntity.getSlab() == 0d))))
								.collect(Collectors.toList());
						if (!payeeList.isEmpty()) {
							benefiaryPaymentId = payeeList.get(0).getBenefiaryPaymentId();
						}

						payoutStoredProcedureResponseDtos
								.add(getStoredProcedureInput(PayoutStoredProcedureConstants.PAYOUT_DEATH, payoutId,
										payoutEntity, payoutStoredProcedureResponseDto, benefiaryPaymentId,
										tempPayoutMbrNomineeEntity.getNomineeId(),
										tempPayoutMbrNomineeEntity.getNomineeCode(), annuityChallanNumber));
						saveStoredProcedureResponse(payoutId, benefiaryPaymentId, payoutStoredProcedureResponseDto,
								annuityChallanNumber);
					}
				} else {
					// TempPayoutMbrEntity payoutMbrEntity = payoutEntity.getPayoutMbr();
					List<PayoutPayeeBankDetailsTempEntity> tempPayoutPayeeBankDetailsEntities = payoutMbrEntity
							.getPayoutPayeeBank();
					for (PayoutPayeeBankDetailsTempEntity tempPayoutPayeeBankDetailsEntity : tempPayoutPayeeBankDetailsEntities) {
						if ((tempPayoutPayeeBankDetailsEntity.getType().equalsIgnoreCase("COMMUTATION"))
								|| (tempPayoutPayeeBankDetailsEntity.getType().equalsIgnoreCase("ANNUITY")
										&& (commutationCalcEntity.getCommutationPerc() == null
												|| commutationCalcEntity.getCommutationPerc() == 0d)
										&& (commutationCalcEntity.getSlab() == null
												|| commutationCalcEntity.getSlab() == 0d))) {
							benefiaryPaymentId = tempPayoutPayeeBankDetailsEntity.getBenefiaryPaymentId() != null
									? tempPayoutPayeeBankDetailsEntity.getBenefiaryPaymentId()
									: getStoreProcedureSeq();
							annuityChallanNumber = getStoreProcedureSeqAnnuityChallanNo();
							payoutStoredProcedureResponseDtos
									.add(getStoredProcedureInput(PayoutStoredProcedureConstants.PAYOUT_NON_DEATH,
											payoutId, payoutEntity, payoutStoredProcedureResponseDto,
											benefiaryPaymentId, 0L, "", annuityChallanNumber));

							saveStoredProcedureResponse(payoutId, benefiaryPaymentId, payoutStoredProcedureResponseDto,
									annuityChallanNumber);
						}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			logger.info("PolicyFreeLookCancelServiceImpl:getFlcDocumentDetails--ERROR--", e);
		}
		return payoutStoredProcedureResponseDtos;
	}

	private void setStoreProcParamtere(StoredProcedureQuery storedProcedure) {

		storedProcedure.registerStoredProcedureParameter("SA_OUT_JOURNAL_NO", Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SA_OUT_DEBITACCOUNT", Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SA_OUT_PAYMENTCREDITACCOUNT", Integer.class,
				ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SA_OUT_TDSCREDITACCOUNT", Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SA_OUT_TOTALAMOUNT", Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SA_OUT_PAYMENTAMOUNT", Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SA_OUT_TdsAMOUNT", Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SA_OUT_CREDITICODE", String.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SA_OUT_DEBITICODE", String.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SA_OUT_MESSAGE", String.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SA_OUT_STATUS", String.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SA_OUT_STATUSCODE", String.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SA_SQLCODE", Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SA_SQLERROR_MESSAGE", String.class, ParameterMode.OUT);

	}

	public PayoutStoredProcedureDto getAccountingDetails() {
		PayoutStoredProcedureDto responseDto = null;
		try {
			String url = environment.getProperty("ACCOUNT_DETAILS_BY_HSNCODE");

			if (StringUtils.isNotBlank(url)) {

				responseDto = restTemplateService.exchange(url, HttpMethod.GET, null, PayoutStoredProcedureDto.class)
						.getBody();
				if (responseDto == null) {
					responseDto = new PayoutStoredProcedureDto();
				}
			}
		} catch (HttpStatusCodeException e) {

			logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-Error:");
		}
		logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-End");

		return responseDto;
	}

	@Override
	public ApiResponseDto<Object> getPaymentStatus(Long payoutId, String unitCode) {
		logger.info("PayoutServiceImpl :: getPaymentStatus :: start ");
		ApiResponseDto<Object> responseDto = new ApiResponseDto<>();
		try {
			List<Map<String, String>> mapList = new ArrayList<>();

			try {

				if (payoutId != null && StringUtils.isNotBlank(unitCode)) {

					List<Object[]> payoutObjList = payoutRepository.getPayoutDetailsByPayoutIdAndUnitCode(payoutId,
							unitCode);

					if (payoutObjList != null && payoutObjList.size() > 0) {

						Object[] payoutObj = payoutObjList.get(0);

						String payoutNumber = (payoutObj[0] != null) ? payoutObj[0].toString() : null;
						String modeOfExit = (payoutObj[1] != null) ? payoutObj[1].toString() : null;
						String mphName = (payoutObj[2] != null) ? payoutObj[2].toString() : null;

						if (StringUtils.isNotBlank(modeOfExit)) {

							if (modeOfExit.equalsIgnoreCase("1")) {

								List<Object[]> paySpObjList = payoutRepository
										.getPayoutSpDetailsByPayoutNo(payoutNumber);

								if (paySpObjList != null && paySpObjList.size() > 0) {
									for (Object[] paySpObj : paySpObjList) {

										Map<String, String> map = new HashMap<>();

										String benefiaryId = (paySpObj[0] != null) ? paySpObj[0].toString() : null;
										String processedStatus = (paySpObj[1] != null) ? paySpObj[1].toString() : null;

										List<Object[]> payCommObjList = payoutRepository
												.getPayoutCommutationsDetailsForDeathByPayoutId(payoutId);

										List<Object[]> payNomiObjList = payoutRepository
												.getPayoutNomineeDetailsByPayoutId(benefiaryId);

										if (payCommObjList != null && payCommObjList.size() > 0
												&& payNomiObjList != null && payNomiObjList.size() > 0) {

											Object[] payNomiObj = payNomiObjList.get(0);

											for (Object[] payCommObj : payCommObjList) {

												String amtPayableTo = (payCommObj[0] != null) ? payCommObj[0].toString()
														: null;
												String commutationAmt = (payCommObj[1] != null)
														? payCommObj[1].toString()
														: null;
												String CommNomineeCode = (payCommObj[2] != null)
														? payCommObj[2].toString()
														: null;

												String nomineeName = (payNomiObj[0] != null) ? payNomiObj[0].toString()
														: null;
												String nomineeCode = (payNomiObj[1] != null) ? payNomiObj[1].toString()
														: null;

												if (amtPayableTo.equalsIgnoreCase("3")
														&& CommNomineeCode.equalsIgnoreCase(nomineeCode)) {

													map.put("benefiaryName", nomineeName);
													map.put("amount", commutationAmt);

												} else if (amtPayableTo.equalsIgnoreCase("1")
														&& CommNomineeCode.equalsIgnoreCase(nomineeCode)) {
													map.put("benefiaryName", mphName);
													map.put("amount", commutationAmt);
												}

											}

										}

										map.put("benefiaryPaymentId", benefiaryId);
										map.put("status", processedStatus);
										mapList.add(map);

									}
								}

								responseDto.setData(mapList);

							} else if (modeOfExit.equalsIgnoreCase("2") || modeOfExit.equalsIgnoreCase("3")
									|| modeOfExit.equalsIgnoreCase("4")) {

								List<Object[]> payCommObjList = payoutRepository
										.getPayoutCommutationsDetailsByPayoutId(payoutId);
								List<Object[]> paySpObjList = payoutRepository
										.getPayoutSpDetailsByPayoutNo(payoutNumber);

								if (payCommObjList != null && payCommObjList.size() > 0 && paySpObjList != null
										&& paySpObjList.size() > 0) {

									Map<String, String> map = new HashMap<>();

									Object[] payCommObj = payCommObjList.get(0);
									Object[] paySpObj = paySpObjList.get(0);

									String amtPayableTo = (payCommObj[0] != null) ? payCommObj[0].toString() : null;
									String commutationAmt = (payCommObj[1] != null) ? payCommObj[1].toString() : null;

									String benefiaryId = (paySpObj[0] != null) ? paySpObj[0].toString() : null;
									String processedStatus = (paySpObj[1] != null) ? paySpObj[1].toString() : null;

									if (StringUtils.isNotBlank(amtPayableTo)) {

										if (amtPayableTo.equalsIgnoreCase("1")) {
											map.put("benefiaryName", mphName);

										} else if (amtPayableTo.equalsIgnoreCase("2")) {

											map.put("benefiaryName",
													payoutRepository.getPayoutMemberNameByPayoutId(payoutId));
										}

									}

									map.put("benefiaryPaymentId", benefiaryId);
									map.put("amount", commutationAmt);
									map.put("status", processedStatus);

									mapList.add(map);

									responseDto.setData(mapList);
								}

							}

						}

					}

				} else {

					responseDto.setMessage("Payout Id / Unit Code is null!!!");
					responseDto.setStatus("SUCCESS");

				}

			} catch (Exception e) {
				e.printStackTrace();
				responseDto.setMessage(e.getMessage());
				responseDto.setStatus("EXCEPTION");
			}

			return responseDto;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("PayoutServiceImpl :: getPaymentStatus :: error is  " + e.getMessage());
		}
		logger.info("PayoutServiceImpl :: getPaymentStatus :: ended ");
		return responseDto;
	}

	@Override
	public AnnuityLedgerDto getAnnuityLedgerDetails(String payoutNo) {
		AnnuityLedgerDto annuityLedgerDto = new AnnuityLedgerDto();
		logger.info("PayoutServiceImpl :: getAnnuityLedgerDetails :: Start");
		try {
			List<Object[]> list = payoutRepository.getPayoutDetailsByPayoutNo(payoutNo, PayoutStatus.APPROVE.val());
			Object[] obj = list.get(0);
			annuityLedgerDto.setCommutationAmount(obj[0] != null ? obj[0].toString() : null);
			annuityLedgerDto.setDateOfCommutation(
					obj[1] != null ? DateUtils.stringToStringDatewithOutTime(obj[1].toString()) : null);
		} catch (Exception e) {
			logger.error("PayoutServiceImpl :: getAnnuityLedgerDetails :: Error" + e.getMessage());
			e.printStackTrace();
		}
		logger.info("PayoutServiceImpl :: getAnnuityLedgerDetails :: ended ");
		return annuityLedgerDto;
	}

	@Override
	public ApiResponseDto<String> fundDebitRecall(PayoutCheckerActionRequestDto request) {
		logger.info("PayoutServiceImpl-accountingStoreProcedureRecall-start");
		try {
			if (request.getAction().equals(PayoutStatus.APPROVE.val())) {

				Optional<TempPayoutEntity> tempPayoutOption = tempPayoutRepository
						.findByInitiMationNoAndUnitCodeAndIsActive(request.getInitiMationNo(), request.getUnitCode(),
								Boolean.TRUE);

				if (tempPayoutOption.isPresent()
						&& (tempPayoutOption.get().getStatus() == PayoutStatus.APPROVE.val())) {
					TempPayoutEntity tempPayout = tempPayoutOption.get();
					InterestRateResponseDto interestRateResponseDto = fundRestApiService.processClaim(request,
							tempPayout);
					if (interestRateResponseDto != null) {
						if (StringUtils.isNotBlank(interestRateResponseDto.getStatus())
								&& interestRateResponseDto.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
							logger.info("Claim Response::{}", interestRateResponseDto.getMessage());

						} else {
//						throw new ApplicationException(interestRateResponseDto.getMessage());
							return ApiResponseDto.error(
									ErrorDto.builder().message(PayoutErrorConstants.INVALID_INTIMATION_NO).build());

						}
					} else {
						throw new ApplicationException(
								"Error in processing the claim in Fund API service for the claim number:"
										+ tempPayout.getClaimNo());
					}

					/*******/
//				return ApiResponseDto.success(null,interestRateResponseDto.getMessage());
					logger.info("PayoutServiceImpl-AnnuityCreationApi-start");
					TempPayoutEntity payoutEntity = tempPayout;
					if (request.getAction() == 7) {
						if (payoutEntity.getModeOfExit() == 1) {
							payoutEntity.getPayoutMbr().getPayoutMbrNomineeDtls().forEach(payoutMbrNomineeDtls -> {
								TempPayoutEntity payoutEntityForNominee = getDuplicatePayout(payoutEntity);

								List<TempPayoutMbrNomineeEntity> nomineeList = new ArrayList<>();
								List<TempPayoutAnnuityCalcEntity> anuityCalcList = new ArrayList<>();
								List<PayoutPayeeBankDetailsTempEntity> nomineeBankList = new ArrayList<>();
								if (payoutMbrNomineeDtls.getNomineeCode() != null
										&& payoutMbrNomineeDtls.getClaimantType().equalsIgnoreCase("Nominee")) {

									nomineeList.add(payoutMbrNomineeDtls);
									payoutEntityForNominee.getPayoutMbr().setPayoutMbrNomineeDtls(null);
									payoutEntityForNominee.getPayoutMbr().setPayoutMbrNomineeDtls(nomineeList);

									payoutEntity.getPayoutMbr().getPayoutAnuityCalc().forEach(payoutAnuityCalc -> {

										if (payoutMbrNomineeDtls.getNomineeCode()
												.equals(payoutAnuityCalc.getNomineeCode())) {
											anuityCalcList.add(payoutAnuityCalc);

										}
									});
									payoutEntityForNominee.getPayoutMbr().setPayoutAnuityCalc(null);
									payoutEntityForNominee.getPayoutMbr().setPayoutAnuityCalc(anuityCalcList);
//							annutityRestApi(payoutEntityForNominee);

									payoutEntity.getPayoutMbr().getPayoutPayeeBank().forEach(payoutPayeeBank -> {

										if (payoutMbrNomineeDtls.getNomineeCode()
												.equals(payoutPayeeBank.getNomineeCode())
												&& ClaimConstants.ANNUITY_AMT_PAYABLE
														.equalsIgnoreCase(payoutPayeeBank.getType())) {
											nomineeBankList.add(payoutPayeeBank);
										}
									});
									payoutEntityForNominee.getPayoutMbr().setPayoutPayeeBank(null);
									payoutEntityForNominee.getPayoutMbr().setPayoutPayeeBank(nomineeBankList);

//							payoutEntityForNominee.getPayoutMbr().setPayoutMbrAddresses(null);

								}
								PayoutAnnuityRestApiRes res = annutityRestApi(payoutEntityForNominee);
								logger.info("PayoutServiceImpl-AnnuityCreationApi-end");

								logger.info(res.getMessage());
							});
						} else {
							List<PayoutPayeeBankDetailsTempEntity> AnnuityMbrBankList = new ArrayList<>();
							TempPayoutEntity payoutEntityForMember = getDuplicatePayout(payoutEntity);

							for (PayoutPayeeBankDetailsTempEntity bank : payoutEntityForMember.getPayoutMbr()
									.getPayoutPayeeBank()) {
								if (bank != null
										&& ClaimConstants.ANNUITY_AMT_PAYABLE.equalsIgnoreCase(bank.getType())) {
									AnnuityMbrBankList.add(bank);
								}

							}
							payoutEntityForMember.getPayoutMbr().setPayoutPayeeBank(null);
							payoutEntityForMember.getPayoutMbr().setPayoutPayeeBank(AnnuityMbrBankList);

							PayoutAnnuityRestApiRes res = annutityRestApi(payoutEntityForMember);
							logger.info("PayoutServiceImpl-AnnuityCreationApi-end");

							if (res != null) {
								logger.info("{}", res.getMessage());
							}
						}
						logger.info("PayoutServiceImpl-AnnuityRealtimeApi-start");
						AnnuitantDetailsUpdateInAnnuity(payoutEntity.getMasterPolicyNo());
						logger.info("PayoutServiceImpl-AnnuityRealtimeApi-start");

					}

				} else {
					ApiResponseDto
							.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_INTIMATION_NO).build());
				}
			}

		} catch (Exception e) {
			logger.error("PayoutServiceImpl-accountingStoreProcedureRecall-error");
		}
		logger.info("PayoutServiceImpl-accountingStoreProcedureRecall-end");
		return null;
	}

	@Override
	public ApiResponseDto<String> sentToIntimationMaker(PayoutCheckerActionRequestDto request) {
		logger.info("PayoutServiceImpl::{}::sentToIntimationMaker::{}::start");
		ApiResponseDto<String> response = new ApiResponseDto<>();
		try {

			Optional<TempPayoutEntity> tempPayout = tempPayoutRepository.findByInitiMationNoAndUnitCodeAndIsActive(
					request.getInitiMationNo(), request.getUnitCode(), Boolean.TRUE);
			if (tempPayout.isPresent()) {
				TempPayoutEntity tempPayoutEntity = tempPayout.get();
				Optional<ClaimEntity> claim = claimRepository.findByClaimNoAndIsActive(tempPayoutEntity.getClaimNo(),
						Boolean.TRUE);
				ClaimEntity claimEntity = claim.get();
				if (claim.isPresent()) {
					ClaimCheckerActionRequestDto requestDto = new ClaimCheckerActionRequestDto();
					requestDto.setClaimNo(claimEntity.getClaimNo());
					requestDto.setAction(PayoutStatus.SEND_TO_MAKER.val());
					requestDto.setModifiedBy(tempPayoutEntity.getModifiedBy());
					claimService.updateCheckerAction(requestDto);
					claimEntity.setIsActive(Boolean.FALSE);
					claimRepository.save(claimEntity);
					tempPayoutEntity.setIsActive(Boolean.FALSE);
					tempPayoutRepository.save(tempPayoutEntity);
					response = ApiResponseDto.success(null, PayoutErrorConstants.SENT_TO_INTIMATION_MAKER);
				} else {
					response = ApiResponseDto
							.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_NO).build());
				}
			} else {
				response = ApiResponseDto
						.error(ErrorDto.builder().message(PayoutErrorConstants.INVALID_INTIMATION_NO).build());
			}

		} catch (Exception e) {
			logger.error("PayoutServiceImpl::{}::sentToIntimationMaker::{}::error" + e.getMessage());
		}
		logger.info("PayoutServiceImpl::{}::sentToIntimationMaker::{}::end");
		return response;
	}

}
