/**
 * 
 */
package com.lic.epgs.fund.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.claim.temp.entity.TempClaimEntity;
import com.lic.epgs.claim.temp.repository.TempClaimRepository;
import com.lic.epgs.common.dto.ClaimRequestDto;
import com.lic.epgs.common.dto.FundChangeDto;
import com.lic.epgs.common.dto.FundRequestDto;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.fund.constants.InterestFundConstants;
import com.lic.epgs.fund.dto.VouchereffectiveDto;
import com.lic.epgs.fund.dto.VouchereffectiveRequest;
import com.lic.epgs.fund.service.FundRestApiService;
import com.lic.epgs.integration.dto.CommonResponseDto;
import com.lic.epgs.integration.dto.DebitRequestDto;
import com.lic.epgs.integration.dto.FundResponseDto;
import com.lic.epgs.integration.dto.InterestFundDto;
import com.lic.epgs.integration.dto.InterestFundResponseDto;
import com.lic.epgs.integration.dto.InterestRateResponseDto;
import com.lic.epgs.payout.constants.PayoutStatus;
import com.lic.epgs.payout.dto.PayoutCheckerActionRequestDto;
import com.lic.epgs.payout.repository.PayoutRepository;
import com.lic.epgs.payout.temp.entity.TempPayoutAnnuityCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutCommutationCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;
import com.lic.epgs.policy.entity.PolicyContributionTempEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterTempEntity;
import com.lic.epgs.policy.old.dto.PolicyDto;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

/**
 * @author Muruganandam
 * @description This Service is used to consume/integrate the APIs of
 *              Interest/Fund Statement Service
 *
 */
@Service
public class FundRestApiServiceImpl implements FundRestApiService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	@Qualifier("restTemplateService")
	private RestTemplate restTemplateService;

	@Autowired
	@Qualifier("jsonObjectMapper")
	private ObjectMapper objectMapper;

	@Autowired
	private Environment environment;

	@Autowired
	private PayoutRepository payoutRepository;

	@Autowired
	private TempClaimRepository claimRepository;
	
	@Autowired
	IntegrationService integrationService;

	@Autowired
	private CommonService commonService;
	public static final String URL_EMPTY = "URL is empty";

	public HttpHeaders getAuthHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(CommonConstants.AUTHORIZATION, "Bearer ");
		headers.add(CommonConstants.CORELATIONID, UUID.randomUUID().toString());
		headers.add(CommonConstants.BUSINESSCORELATIONID, UUID.randomUUID().toString());
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	public InterestFundDto getRequestDto(List<InterestFundDto> dto) {
		return dto.stream().filter(p -> StringUtils.isNotBlank(p.getPolicyNumber())).findAny()
				.orElse(new InterestFundDto());

	}

	public DebitRequestDto getDebitRequestDto(List<DebitRequestDto> accountsDto) {
		if (CommonUtils.isNonEmptyArray(accountsDto)) {
			return accountsDto.stream().findFirst().orElse(new DebitRequestDto());
		}
		return new DebitRequestDto();
	}

	@Override
	public void setPolicyDetails(PolicyMasterTempEntity entity, InterestFundDto dto) {
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setPolicyId(entity.getPolicyId());
		dto.setPolicyNumber(entity.getPolicyNumber());
		dto.setPolicyType(entity.getPolicyType());
		dto.setUnitCode(entity.getUnitId());
		dto.setVariant(entity.getVariant());
		dto.setStream(InterestFundConstants.STREAM_SUPERANNUATION);
	}

	@Override
	public void setPolicyDetails(PolicyMasterEntity sourceEntity, PolicyMasterEntity destinationEntity,
			FundChangeDto dto) {
		dto.setCreatedBy(destinationEntity.getCreatedBy());
		dto.setPolicyId(sourceEntity.getPolicyId());
		dto.setSourcePolicyNumber(sourceEntity.getPolicyId());
		dto.setPolicyType(destinationEntity.getPolicyType());
		dto.setUnitCode(destinationEntity.getUnitId());
		dto.setDestinationPolicyNumber(destinationEntity.getPolicyId());
		dto.setVariant(destinationEntity.getVariant());
		dto.setStream(InterestFundConstants.STREAM_SUPERANNUATION);
	}

	@Override
	public InterestFundDto setContributionRequest(PolicyMasterTempEntity entity, String txnSubType) {
		InterestFundDto dto = new InterestFundDto();
		Set<PolicyContributionTempEntity> policyContributions = entity.getPolicyContributions();

		if (CommonUtils.isNonEmptyArray(policyContributions)) {

			PolicyContributionTempEntity contributionEntity = policyContributions.stream().findFirst().orElse(null);

			if (contributionEntity != null) {
				List<Long> contributionIds = policyContributions.stream()
						.filter(con -> !NumericUtils.isBoolTrue(con.getTxnEntryStatus()))
						.map(PolicyContributionTempEntity::getContributionId).collect(Collectors.toList());
				dto.setContributionIds(contributionIds);
				dto.setEffectiveTxnDate(DateUtils.dateToStringDDMMYYYY(contributionEntity.getContributionDate()));
				dto.setEntityType(InterestFundConstants.POLICY_ACCOUNT);
				dto.setIsZeroAccount(false);
				dto.setTrnxDate(DateUtils.dateToStringDDMMYYYY(contributionEntity.getContributionDate()));
				dto.setTxnEntryStatus(false);
				dto.setTxnSubType(txnSubType);
				dto.setTxnType("CREDIT");
				setPolicyDetails(entity, dto);
			}
		}
		return dto;
	}

	/***
	 * @return
	 * @throws ApplicationException
	 * @notes to invoke the fund API to process the claim for the given member
	 */
	@Override
	@Transactional
	public InterestRateResponseDto processClaim(PayoutCheckerActionRequestDto request, TempPayoutEntity payoutEntity)
			throws ApplicationException {
		if (request.getAction().equals(PayoutStatus.APPROVE.val())) {
			if (payoutEntity != null) {
				TempPayoutMbrEntity payoutMbrEntity = payoutEntity.getPayoutMbr();

				Optional<TempClaimEntity> claimOptional = claimRepository
						.findByClaimNoAndIsActive(payoutEntity.getClaimNo(), true);
				if (claimOptional.isPresent()) {
					TempClaimEntity claimEntity = claimOptional.get();
					TempClaimEntity firstClaimEntity = claimRepository
							.findTopByClaimNoOrderByClaimIdDesc(payoutEntity.getClaimNo());

					if (firstClaimEntity != null) {
						claimEntity.setCreatedOn(firstClaimEntity.getCreatedOn());
					}

					PolicyMasterEntity policyMasterEntity = commonService.findPolicyDetails(null,
							claimEntity.getPolicyId());

					if (payoutMbrEntity != null) {
						FundChangeDto fundChangeDto = setClaimRequest(policyMasterEntity, payoutEntity, claimEntity,
								payoutMbrEntity);
						return updatePolicyFund(fundChangeDto);
					} else {
						throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE
								+ "No Claim member details found for the given claim number:"
								+ payoutEntity.getClaimNo());
					}
				} else {
					throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE
							+ "No Claim  details found for the given claim number:" + payoutEntity.getClaimNo());
				}
			} else {
				throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE
						+ "No Claim Payout details found for the given pyaout number:" + request.getPayoutNo());
			}

		}
		throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE
				+ "No Claim Payout details found to process the fund for the given pyaout number:"
				+ request.getPayoutNo());
	}

	public void setCommutationAndPurchasePrice(PolicyMasterEntity policyMasterEntity, TempClaimEntity claimEntity,
			TempPayoutEntity payoutEntity, TempPayoutMbrEntity payoutMbrEntity, FundChangeDto changeDto)
			throws ApplicationException {
		if (StringUtils.isNotBlank(policyMasterEntity.getPolicyType())
				&& policyMasterEntity.getPolicyType().equalsIgnoreCase(InterestFundConstants.DB)) {
			changeDto.setNewLicId(claimEntity.getClaimNo());
			changeDto.setCommutationAmt(BigDecimal.ZERO);
			List<TempPayoutCommutationCalcEntity> payoutCommutationCalc = payoutMbrEntity.getPayoutCommutationCalc();

			if (CommonUtils.isNonEmptyArray(payoutCommutationCalc)) {
				double totalCommAmt = payoutCommutationCalc.stream().filter(comm -> comm.getCommutationAmt() != null)
						.mapToDouble(TempPayoutCommutationCalcEntity::getCommutationAmt).sum();
				changeDto.setCommutationAmt(NumericUtils.doubleToBigDecimal(totalCommAmt));
				changeDto.setEffectiveTxnDate(DateUtils.dateToStringDDMMYYYY(payoutEntity.getModifiedOn()));

				/**
				 * if (totalCommAmt == 0.0) { throw new ApplicationException( "Commutation
				 * Amount is zero for the Policy/MemberId/Claim/Payout No.: " +
				 * policyMasterEntity.getPolicyNumber() + "/" + payoutMbrEntity.getMemberId() +
				 * "/" + claimEntity.getClaimNo() + "/" + payoutEntity.getPayoutNo()); }
				 */
			}
			/**
			 * else { throw new ApplicationException( "No Payout Commutation details found
			 * for the Policy/MemberId/Claim/Payout No.: " +
			 * policyMasterEntity.getPolicyNumber() + "/" + payoutMbrEntity.getMemberId() +
			 * "/" + claimEntity.getClaimNo() + "/" + payoutEntity.getPayoutNo()); }
			 */
			
			/* Note:@author Narayanan added for withdrawal claim:Start */
			changeDto.setDateOfExit(DateUtils.dateToStringDDMMYYYY(payoutEntity.getDtOfExit()));
			changeDto.setPurchasePrice(NumericUtils.doubleToBigDecimal(0d));
			changeDto.setAnnuityApplicable(false);
			if(!payoutMbrEntity.getPayoutAnuityCalc().isEmpty()) {
				/* Note:@author Narayanan added for withdrawal claim:end */
			List<TempPayoutAnnuityCalcEntity> payoutFundValue = payoutMbrEntity.getPayoutAnuityCalc();
			if (CommonUtils.isNonEmptyArray(payoutFundValue)) {
				double totalGstAmount=0.0;
				double totalPurchasePrice = payoutFundValue.stream().filter(comm -> comm.getPurchasePrice() != null)
						.mapToDouble(TempPayoutAnnuityCalcEntity::getPurchasePrice).sum();
				changeDto.setPurchasePrice(NumericUtils.doubleToBigDecimal(totalPurchasePrice));
				changeDto.setDateOfExit(DateUtils.dateToStringDDMMYYYY(payoutEntity.getDtOfExit()));
				changeDto.setAnnuityApplicable(true);

				 totalGstAmount = payoutFundValue.stream().filter(gst -> gst.getGstBondBy() == 3 && gst.getGstAmount() != null)
							.mapToDouble(TempPayoutAnnuityCalcEntity::getGstAmount).sum();
					
					totalGstAmount += totalPurchasePrice;
					
					changeDto.setPurchasePrice(NumericUtils.doubleToBigDecimal(totalGstAmount));
					
				if (totalPurchasePrice == 0.0) {
					throw new ApplicationException("Purchase price is zero for the Policy/MemberId/Claim/Payout No.: "
							+ policyMasterEntity.getPolicyNumber() + "/" + payoutMbrEntity.getMemberId() + "/"
							+ claimEntity.getClaimNo() + "/" + payoutEntity.getPayoutNo());
				}
			} else {
				throw new ApplicationException(
						"No Purchase price details found for the Policy/MemberId/Claim/Payout No.: "
								+ policyMasterEntity.getPolicyNumber() + "/" + payoutMbrEntity.getMemberId() + "/"
								+ claimEntity.getClaimNo() + "/" + payoutEntity.getPayoutNo());
			}
		}
			changeDto.setTxnAmount(changeDto.getCommutationAmt().add(changeDto.getPurchasePrice()));
		}

	}

	private String getVariantVersionByVariantId(Long variantId)
			throws MalformedURLException, ProtocolException, IOException {
		String product = null;
		JsonNode response = integrationService
				.getVariantDetailsByProductVariantId(NumericUtils.stringToLong(String.valueOf(variantId)));
		if (response != null) {
			JsonNode proposeDetails = response.path("responseData");
			product = proposeDetails.path("variantVersion").textValue();

		}
		return product;
	}

	public FundChangeDto setClaimRequest(PolicyMasterEntity policyMasterEntity, TempPayoutEntity payoutEntity,
			TempClaimEntity claimEntity, TempPayoutMbrEntity payoutMbrEntity) throws ApplicationException {
		FundChangeDto debitRequestDto = new FundChangeDto();

		if (policyMasterEntity != null) {
			debitRequestDto
					.setCreatedBy(StringUtils.isNotBlank(payoutEntity.getCreatedBy()) ? payoutEntity.getCreatedBy()
							: InterestFundConstants.POLICY_CLAIM);

			debitRequestDto.setEffectiveTxnDate(DateUtils.dateToStringDDMMYYYY(claimEntity.getCreatedOn()));
			debitRequestDto.setMemberId(payoutMbrEntity.getMemberId());
			debitRequestDto.setLicId(payoutMbrEntity.getLicId());
			debitRequestDto.setModule(InterestFundConstants.POLICY_CLAIM);
			debitRequestDto.setPolicyId(claimEntity.getPolicyId());
			debitRequestDto.setDestinationPolicyNumber(policyMasterEntity.getPolicyId());
			debitRequestDto.setSourcePolicyNumber(policyMasterEntity.getPolicyId());
			debitRequestDto.setPolicyType(policyMasterEntity.getPolicyType());
			try {
				debitRequestDto.setVariant(getVariantVersionByVariantId(NumericUtils.stringToLong(policyMasterEntity.getVariant())));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			debitRequestDto.setStream(InterestFundConstants.STREAM_SUPERANNUATION);
			debitRequestDto.setTxnAmount(payoutMbrEntity.getTotalContribution() != null
					? BigDecimal.valueOf(payoutMbrEntity.getTotalContribution())
					: BigDecimal.ZERO);
			debitRequestDto.setUpdateType(InterestFundConstants.POLICY_CLAIM);
			debitRequestDto.setUpdateSubType(InterestFundConstants.POLICY_CLAIM);
			debitRequestDto.setCreatedBy(InterestFundConstants.POLICY_CLAIM);

			setCommutationAndPurchasePrice(policyMasterEntity, claimEntity, payoutEntity, payoutMbrEntity,
					debitRequestDto);

		} else {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE
					+ "No policy details found for the given policy Id:" + claimEntity.getPolicyId());
		}
		return debitRequestDto;

	}

	@Override
	public InterestRateResponseDto contributionAdjustmentForFund(PolicyDto policyDto,
			PolicyMasterTempEntity policyMasterEntity, String contributionType) throws ApplicationException {
		if (policyMasterEntity != null) {
			InterestFundDto contributionRequest = setContributionRequest(policyMasterEntity, contributionType);
			contributionRequest.setApprovedPolicyId(policyDto.getPolicyId());

			InterestRateResponseDto responseDto = null;

			if (StringUtils.isNotBlank(policyMasterEntity.getPolicyType())
					&& policyMasterEntity.getPolicyType().equalsIgnoreCase("DC")) {
				responseDto = creditPolicyMembers(contributionRequest);
			} else {
				responseDto = creditPolicy(contributionRequest);
			}

			if (CommonUtils.isSuccessResponse(responseDto)) {
				logger.info("Contribution pushed to fund:{}", policyMasterEntity.getPolicyNumber());
				return responseDto;
			} /**
				 * else { throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE +
				 * policyDto.getPolicyNumber()); }
				 */
		} else {
			throw new ApplicationException("No policy master found for the given MPH:" + contributionType);
		}
		return null;

	}

	public ClaimRequestDto setClaimRequestDto(FundChangeDto dto) {
		ClaimRequestDto requestDto = new ClaimRequestDto();
		requestDto.setCommutationAmt(dto.getCommutationAmt());
		requestDto.setCreatedBy(dto.getCreatedBy());
		requestDto.setDateOfExit(dto.getDateOfExit());
		requestDto.setLicId(dto.getLicId());
		requestDto.setMemberId(dto.getMemberId());
		requestDto.setModule(InterestFundConstants.POLICY_CLAIM);
		requestDto.setPolicyId(dto.getDestinationPolicyNumber());
		requestDto.setPurchasePrice(dto.getPurchasePrice());
		requestDto.setStream(InterestFundConstants.SUPERANNUATION);
		requestDto.setTransactionDate(dto.getEffectiveTxnDate());
		requestDto.setTxnAmount(dto.getTxnAmount());
		try {
			System.out.println(objectMapper.writeValueAsString(requestDto));
		} catch (JsonProcessingException e) {
			logger.error("setClaimRequestDto Error:", e);
		}
		return requestDto;
	}

	/****
	 * @throws ApplicationException
	 * @description API to update the policy/member fund based on policy
	 *              number/membership number
	 * @apiNote updateType :: CONVERSION/MERGE/SURRENDER/FREELOOK, stream ::
	 *          Superannuation, module :: Policy Merge/Conversion/Freelook
	 *          Cancellation/Surrender
	 * 
	 */
	@Override
	public InterestRateResponseDto updatePolicyFund(FundChangeDto dto) throws ApplicationException {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("updatePolicyFund::Start::{},", dto.getDestinationPolicyNumber());
		try {
			try {
				System.out.println(objectMapper.writeValueAsString(dto));
			} catch (JsonProcessingException e) {
				logger.error("updatePolicyFund Error:", e);
			}
			String url = environment.getProperty("POLICY_UPDATE_FUND");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				HttpEntity<FundChangeDto> entity = new HttpEntity<>(dto, headers);

				responseDto = restTemplateService.postForObject(url, entity, InterestRateResponseDto.class);

				if (responseDto != null) {
					if (CommonUtils.isSuccessResponse(responseDto)) {
						return responseDto;
					} else {
						throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE
								+ +dto.getDestinationPolicyNumber() + " " + responseDto.getMessage());
					}

				} else {
					throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Null response");
				}
			} else {
				throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + URL_EMPTY);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Exception updatePolicyFund::End::{},", dto.getDestinationPolicyNumber(), e);
			responseDto = new InterestRateResponseDto();
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());

		} catch (HttpClientErrorException e) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
		}
		logger.info("updatePolicyFund::End::{},", dto.getDestinationPolicyNumber());
		return responseDto;
	}

	/****
	 * @throws ApplicationException
	 * @description API to push/credit the policy amount details. This is applicable
	 *              only for "GSSDB" ProductType. Frequency : V1,V2,V3
	 */
	@Override
	public InterestRateResponseDto creditPolicy(InterestFundDto requestDto) throws ApplicationException {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("creditPolicy::Start::{},", requestDto.getPolicyNumber());
		try {
			try {
//				String valueAsString = objectMapper.writeValueAsString(dto);
				System.out.println(objectMapper.writeValueAsString(requestDto));
			} catch (JsonProcessingException e) {
				logger.error("Error:", e);
			}
			String url = environment.getProperty("POLICY_CREDIT");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				HttpEntity<InterestFundDto> entity = new HttpEntity<>(requestDto, headers);
				InterestRateResponseDto rateResponseDto = restTemplateService.postForObject(url, entity,
						InterestRateResponseDto.class);
				if (rateResponseDto != null) {
					if (StringUtils.isNotBlank(rateResponseDto.getStatus())
							&& rateResponseDto.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
						return rateResponseDto;
					} else {
						throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + " - "
								+ requestDto.getPolicyNumber() + " " + rateResponseDto.getMessage());
					}
				} else {
					throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "No response.");
				}
			} else {
				throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + URL_EMPTY);
			}
		} catch (IllegalArgumentException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception creditPolicy::End::{},", requestDto.getPolicyNumber(), e);
		} catch (HttpClientErrorException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception creditPolicy::End::{},", requestDto.getPolicyNumber(), e);
		}
		logger.info("creditPolicy::End::{},", requestDto.getPolicyNumber());
		return null;
	}

	/****
	 * @throws ApplicationException
	 * @description API to push/credit the policy member amount. This is applicable
	 *              only for "GSSDC" ProductType. Frequency : V1,V2,V3
	 *              POLICY_MEMBER_CREDIT"
	 */
	@Override
	public InterestRateResponseDto creditPolicyMembers(InterestFundDto requestDto) throws ApplicationException {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("creditPolicy::Start::{},", requestDto.getPolicyNumber());
		try {
			try {
//				String valueAsString = objectMapper.writeValueAsString(dto);
				System.out.println(objectMapper.writeValueAsString(requestDto));
			} catch (JsonProcessingException e) {
				logger.error("Error:", e);
			}
			String url = environment.getProperty("POLICY_MEMBER_CREDIT");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				HttpEntity<InterestFundDto> entity = new HttpEntity<>(requestDto, headers);
				InterestRateResponseDto rateResponseDto = restTemplateService.postForObject(url, entity,
						InterestRateResponseDto.class);
				if (rateResponseDto != null) {
					if (StringUtils.isNotBlank(rateResponseDto.getStatus())
							&& rateResponseDto.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
						return rateResponseDto;
					} else {
						throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + " - "
								+ requestDto.getPolicyNumber() + " " + rateResponseDto.getMessage());
					}
				} else {
					throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "No response.");
				}
			} else {
				throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + URL_EMPTY);
			}
		} catch (HttpClientErrorException | IllegalArgumentException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception creditPolicy::End::{},", requestDto.getPolicyNumber(), e);
		}
		logger.info("creditPolicy::End::{},", requestDto.getPolicyNumber());
		return null;
	}

	/***
	 * @description To view the policy statement by policy Number
	 */
	@Override
	public InterestRateResponseDto viewByPolicyNo(String policyNo) {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("viewByPolicyNo::Start::{},", policyNo);
		try {
			String url = environment.getProperty("POLICY_VIEWBYPOLICYNO");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				url = url + policyNo;
				return restTemplateService.exchange(url, HttpMethod.GET, null, InterestRateResponseDto.class, headers)
						.getBody();
			}
		} catch (IllegalArgumentException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception viewByPolicyNo::End::{},", policyNo, e);
		}
		logger.info("viewByPolicyNo::End::{},", policyNo);
		return responseDto;
	}

	/***
	 * @description To view the policy member statement by policy Number
	 */
	@Override
	public InterestRateResponseDto viewMembersByPolicyNo(String policyNo) {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("viewMembersByPolicyNo::Start::{},", policyNo);
		try {
			String url = environment.getProperty("POLICY_MEMBER_VIEWMEMBERSBYPOLICYNO");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				url = url + policyNo;
				return restTemplateService.exchange(url, HttpMethod.GET, null, InterestRateResponseDto.class, headers)
						.getBody();
			}
		} catch (IllegalArgumentException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception viewMembersByPolicyNo::End::{},", policyNo, e);
		} catch (HttpClientErrorException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception creditPolicy::End::{},", policyNo, e);
		}
		logger.info("viewMembersByPolicyNo::End::{},", policyNo);
		return null;
	}

	/***
	 * @description To view the policy member statement by Member Id
	 */
	@Override
	public InterestRateResponseDto viewByMemberId(String memberId) {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("viewByMemberId::Start::{},", memberId);
		try {
			String url = environment.getProperty("POLICY_MEMBER_VIEWBYMEMBERID");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				url = url + memberId;
				return restTemplateService.exchange(url, HttpMethod.GET, null, InterestRateResponseDto.class, headers)
						.getBody();
			}
		} catch (IllegalArgumentException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception viewByMemberId::End::{},", memberId, e);
		} catch (HttpClientErrorException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception creditPolicy::End::{},", memberId, e);
		}
		logger.info("viewByMemberId::End::{},", memberId);
		return null;
	}

	@Override
	public InterestRateResponseDto viewHistoryPolicyNo(String policyNo) {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("viewHistoryPolicyNo::Start::{},", policyNo);
		try {
			String url = environment.getProperty("POLICY_VIEWHISTORYPOLICYNO");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				url = url + policyNo;
				return restTemplateService.exchange(url, HttpMethod.GET, null, InterestRateResponseDto.class, headers)
						.getBody();
			}
		} catch (IllegalArgumentException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception viewHistoryPolicyNo::End::{},", policyNo, e);
		} catch (HttpClientErrorException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception creditPolicy::End::{},", policyNo, e);
		}
		logger.info("viewHistoryPolicyNo::End::{},", policyNo);
		return responseDto;
	}

	@Override
	public InterestRateResponseDto viewHistoryByMemberId(String memberId) {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("viewHistoryByMemberId::Start::{},", memberId);
		try {
			String url = environment.getProperty("POLICY_VIEWHISTORYBYMEMBERID");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				url = url + memberId;
				return restTemplateService.exchange(url, HttpMethod.GET, null, InterestRateResponseDto.class, headers)
						.getBody();
			}
		} catch (IllegalArgumentException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception viewHistoryByMemberId::End::{},", memberId, e);
		} catch (HttpClientErrorException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception creditPolicy::End::{},", memberId, e);
		}
		logger.info("viewHistoryByMemberId::End::{},", memberId);
		return responseDto;
	}

	@Override
	public InterestRateResponseDto viewMembersHistoryByPolicyNo(String policyNo) {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("viewMembersHistoryByPolicyNo::Start::{},", policyNo);
		try {
			String url = environment.getProperty("POLICY_VIEWMEMBERSHISTORYBYPOLICYNO");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				url = url + policyNo;
				return restTemplateService.exchange(url, HttpMethod.GET, null, InterestRateResponseDto.class, headers)
						.getBody();
			}
		} catch (IllegalArgumentException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception viewMembersHistoryByPolicyNo::End::{},", policyNo, e);
		} catch (HttpClientErrorException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception creditPolicy::End::{},", policyNo, e);
		}
		logger.info("viewMembersHistoryByPolicyNo::End::{},", policyNo);
		return responseDto;
	}

	/****
	 * @throws ApplicationException
	 * @description API to push/credit the policy amount details. This is applicable
	 *              only for "GSSDB" ProductType. Frequency : V1,V2,V3
	 */
	@Override
	public InterestFundResponseDto viewMemberFundDetails(FundRequestDto requestDto) throws ApplicationException {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("viewMemberFundDetails::Start::{},", requestDto.getPolicyNumber());
		try {
			try {
				String request = objectMapper.writeValueAsString(requestDto);
				System.out.println(request);
				logger.info("viewMemberFundDetails - Request: {}", request);

			} catch (JsonProcessingException e) {
				logger.error("Error:", e);
			}
			String url = environment.getProperty("MEMBER_FUND_DETAILS_BY_LICID");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				HttpEntity<FundRequestDto> entity = new HttpEntity<>(requestDto, headers);

				FundResponseDto<InterestFundResponseDto> response = restTemplateService.exchange(url, HttpMethod.POST,
						entity, new ParameterizedTypeReference<FundResponseDto<InterestFundResponseDto>>() {
						}).getBody();

				if (response != null) {
					if (StringUtils.isNotBlank(response.getStatus())
							&& response.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)
							&& response.getResponseData() != null) {
						try {
							String res = objectMapper.writeValueAsString(response.getResponseData());
							System.out.println(res);
							logger.info("viewMemberFundDetails - Request: {}", res);

						} catch (JsonProcessingException e) {
							logger.error("Error:", e);
						}
						return response.getResponseData();
					} else {
						throw new ApplicationException(
								InterestFundConstants.SA_FUND_SERVICE + " " + response.getMessage());
					}
				} else {
					throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "No response.");
				}
			} else {
				throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + URL_EMPTY);
			}
		} catch (IllegalArgumentException | HttpClientErrorException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception viewMemberFundDetails::End::{},", requestDto.getPolicyNumber(), e);
		}
		logger.info("viewMemberFundDetails::End::{},", requestDto.getPolicyNumber());
		return null;
	}

	@Override
	@Async
	public CommonResponseDto<Map<String, Object>> contirbutionToTransEntries(FundRequestDto requestDto)
			throws ApplicationException {
		logger.info("contirbutionToTransEntries::Start::{},", requestDto.getPolicyId());
		CommonResponseDto<Map<String, Object>> responseDto = new CommonResponseDto<>();
		try {
			responseDto.setTransactionStatus(CommonConstants.ERROR);
			String url = environment.getProperty("CONTIRBUTIONTOTRANSENTRIES");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				url = url + requestDto.getPolicyId() + "/" + DateUtils.getFinancialYrByDt(DateUtils.sysDate());
				responseDto = restTemplateService.exchange(url, HttpMethod.GET, null,
						new ParameterizedTypeReference<CommonResponseDto<Map<String, Object>>>() {
						}, headers).getBody();
				if (responseDto != null && StringUtils.isNotBlank(responseDto.getTransactionStatus())
						&& CommonConstants.SUCCESS.equalsIgnoreCase(responseDto.getTransactionStatus())) {
					return responseDto;
				}
			}

		} catch (Exception e) {
			responseDto.setTransactionStatus(CommonConstants.ERROR);
			responseDto.setTransactionMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception contirbutionToTransEntries::End::{},", requestDto.getPolicyNumber(), e);
		}
		logger.info("contirbutionToTransEntries::End::{},", requestDto.getPolicyId());
		return responseDto;
	}

	/****
	 * @throws ApplicationException
	 * @description API to push/credit the policy amount details. This is applicable
	 *              only for "GSSDB" ProductType. Frequency : V1,V2,V3
	 */
	@Override
	public InterestFundResponseDto viewPolicyFundDetails(FundRequestDto requestDto) throws ApplicationException {
		InterestRateResponseDto responseDto = new InterestRateResponseDto();
		responseDto.setStatus(CommonConstants.ERROR);
		logger.info("viewPolicyFund::Start::{},", requestDto.getPolicyNumber());
		try {
			try {
				String request = objectMapper.writeValueAsString(requestDto);
				logger.info("viewMemberFundDetails - Request: {}", request);
			} catch (JsonProcessingException e) {
				logger.error("Error:", e);
			}
			String url = environment.getProperty("POLICY_FUND_DETAILS_BY_POLICYID");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				HttpEntity<FundRequestDto> entity = new HttpEntity<>(requestDto, headers);

				FundResponseDto<InterestFundResponseDto> response = restTemplateService.exchange(url, HttpMethod.POST,
						entity, new ParameterizedTypeReference<FundResponseDto<InterestFundResponseDto>>() {
						}).getBody();

				if (response != null) {
					if (StringUtils.isNotBlank(response.getStatus())
							&& response.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)
							&& response.getResponseData() != null) {
						return response.getResponseData();
					} else {
						throw new ApplicationException(
								InterestFundConstants.SA_FUND_SERVICE + " " + response.getMessage());
					}
				} else {
					throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "No response.");
				}
			} else {
				throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + URL_EMPTY);
			}
		} catch (IllegalArgumentException | HttpClientErrorException e) {
			responseDto.setStatus(CommonConstants.ERROR);
			responseDto.setMessage(InterestFundConstants.SA_FUND_SERVICE + e.getMessage());
			logger.error("Exception viewPolicyFund::End::{},", requestDto.getPolicyNumber(), e);
		}
		logger.info("viewPolicyFund::End::{},", requestDto.getPolicyNumber());
		return null;
	}

//	@Override
//	public VouchereffectiveDto getCollectionData()throws ApplicationException {
//		VouchereffectiveRequest request = new VouchereffectiveRequest();
//	
//		VouchereffectiveDto vouchereffectiveDto = null;
//		logger.info("getCollectionData::Start::{},", collectionNo);
//		try {
////			request.setCollectionNo(collectionNo);
////			Date date=new SimpleDateFormat("yyyy-mm-dd").parse(voucherEffective);
////			request.setNewEffectiveDateOfPayment(date);
//			request.setCollectionNo(collectionNo);
//			Date date=new SimpleDateFormat("DD/MM/YYYY").parse(voucherEffective);
//			String string = DateUtils.dateToStringDDMMYYYY(date);
//			Date dates=new SimpleDateFormat("YYYY-MM-DD").parse(string);
//			request.setNewEffectiveDateOfPayment(dates);
//			String url = environment.getProperty("ACCOUNT_UPDATE_EFFECTIVE_DATE");
//			if (StringUtils.isNotBlank(url)) {
//				HttpHeaders headers = getAuthHeaders();
//				if (StringUtils.isNotBlank(url)) {
//					HttpHeaders headerss = getAuthHeaders();
//					HttpEntity<VouchereffectiveRequest> entity = new HttpEntity<>(request, headers);
//					vouchereffectiveDto = restTemplateService.exchange(url, HttpMethod.POST, entity,
//							new ParameterizedTypeReference<VouchereffectiveDto>() {
//							}).getBody();
//					if (vouchereffectiveDto == null) {
//						vouchereffectiveDto = new VouchereffectiveDto();
//					}
//				} else {
//					throw new ApplicationException(InterestFundConstants.SA_UPDATE_EFFECTIVE_DATE + URL_EMPTY);
//				}
//			}
//		} catch (Exception e) {
//			logger.error("Exception getCollectionData::End::{},collectionId", e);
//		}
//		logger.info("getCollectionData::End::{},", collectionNo);
//		return vouchereffectiveDto;
//	}

	@Override
	public VouchereffectiveDto getCollectionData(VouchereffectiveRequest dto) {
		VouchereffectiveRequest request = new VouchereffectiveRequest();
	
		VouchereffectiveDto vouchereffectiveDto = null;
		logger.info("getCollectionData::Start::{},", dto.getCollectionNo());
		try {
			request.setCollectionNo(dto.getCollectionNo());
			String newEffectiveDateOfPayment = dto.getNewEffectiveDateOfPayment();
			Date date=DateUtils.convertStringToDate(newEffectiveDateOfPayment);
			String dates=DateUtils.DateTostringYYYYMMDDHyphen(date);
			request.setNewEffectiveDateOfPayment(dates);
			String url = environment.getProperty("ACCOUNT_UPDATE_EFFECTIVE_DATE");
			if (StringUtils.isNotBlank(url)) {
				HttpHeaders headers = getAuthHeaders();
				if (StringUtils.isNotBlank(url)) {
					HttpHeaders headerss = getAuthHeaders();
					HttpEntity<VouchereffectiveRequest> entity = new HttpEntity<>(request, headers);
					vouchereffectiveDto = restTemplateService.exchange(url, HttpMethod.POST, entity,
							new ParameterizedTypeReference<VouchereffectiveDto>() {
							}).getBody();
					if (vouchereffectiveDto == null) {
						vouchereffectiveDto = new VouchereffectiveDto();
					}
				} else {
					throw new ApplicationException(InterestFundConstants.SA_UPDATE_EFFECTIVE_DATE + URL_EMPTY);
				}
			}
		} catch (Exception e) {
			logger.error("Exception getCollectionData::End::{},collectionId", e);
		}
		logger.info("getCollectionData::End::{},", dto.getCollectionNo());
		return vouchereffectiveDto;
	}

	
}
