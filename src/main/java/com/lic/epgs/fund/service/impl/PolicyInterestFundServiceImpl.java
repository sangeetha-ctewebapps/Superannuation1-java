/**
 * 
 */
package com.lic.epgs.fund.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.common.dto.InterestRequestDto;
import com.lic.epgs.common.entity.InterestErrorDetailsEntity;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.common.repository.InterestErrorDetailsRepository;
import com.lic.epgs.fund.service.FundRestApiService;
import com.lic.epgs.fund.service.PolicyInterestFundService;
import com.lic.epgs.integration.dto.InterestErrorDto;
import com.lic.epgs.integration.dto.InterestRateResponseDto;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;

/**
 * @author Muruganandam
 * @implNote This service is used to invoke the the Interest/Fund statement API
 *           services
 * @createdDate 19-NOV-2022
 */
@Service
public class PolicyInterestFundServiceImpl extends InterestRateFundHelper implements PolicyInterestFundService {

	protected final Logger logger = LogManager.getLogger(getClass());

//	@Autowired
//	private PolicyRepository policyRepository;
//
//	@Autowired
//	private PolicyMbrRepository policyMbrRepository;

	@Autowired
	private FundRestApiService fundRestApiService;

	@Autowired
	private InterestErrorDetailsRepository interestErrorDetailsRepository;

	/***
	 * @description Credit Deposit amount for Interest/Fund Statement Calculation by
	 *              Policy No as request parameter
	 * @param policyNo
	 * @throws ApplicationException
	 */
//	@Override
//	public InterestRateResponseDto creditPolicy(String policyNo) throws ApplicationException {
//		logger.info(CommonConstants.LOGSTART + "::{}", policyNo);
//		InterestRateResponseDto commonResponseDto = new InterestRateResponseDto();
//		PolicyEntity policyEntity = policyRepository.findByPolicyNumberAndIsActiveTrue(policyNo);
//		if (policyEntity != null) {
//			return creditPolicy(policyEntity);
//		}
//		commonResponseDto.setStatus(CommonConstants.ERROR);
//		logger.info(CommonConstants.LOGEND + "::{}", policyNo);
//		return commonResponseDto;
//	}

	/***
	 * @description Credit Deposit amount for Interest/Fund Statement Calculation by
	 *              Policy PolicyEntity as request parameter
	 * @param PolicyEntity
	 * @throws ApplicationException
	 */
//	@Override
//	public InterestRateResponseDto creditPolicy(PolicyEntity entity) throws ApplicationException {
//		logger.info(CommonConstants.LOGSTART + "::{}", entity.getPolicyNumber());
//		InterestFundDto dto = new InterestFundDto();
//		List<InterestErrorDto> errorData = new ArrayList<>();
//		try {
//			setInterestFundRequest(entity, dto);
//			/***
//			 * accountCreditValidator.buildDefaultValidatorFactory().getValidator().;
//			 */
//			return fundRestApiService.creditPolicy(Arrays.asList(dto));
//		} catch (ApplicationException e) {
//			errorData.add(InterestErrorDto.builder().policyNo(dto.getPolicyNumber()).memberId(dto.getPolicyNumber())
//					.error(e.getMessage()).username(entity.getModifiedBy()).policyType(entity.getQuotationType())
//					.txnType(InterestFundConstants.CREDIT).build());
//			logger.error("creditPolicy", e);
//			throw new ApplicationException(e.getMessage());
//		} finally {
//			saveErrorDetails(errorData);
//			logger.info(CommonConstants.LOGEND + "::{}", entity.getPolicyNumber());
//		}
//	}

	/***
	 * @description Credit Deposit amount for all members of DC policy to calculate
	 *              the Interest/Fund Statement by Policy number as request
	 *              parameter
	 * @param policyNo
	 */
//	@Override
//	public InterestRateResponseDto creditPolicyMembers(String policyNo) {
//		logger.info(CommonConstants.LOGSTART + "::{}", policyNo);
//		InterestRateResponseDto commonResponseDto = new InterestRateResponseDto();
//		PolicyEntity policyEntity = policyRepository.findByPolicyNumberAndIsActiveTrue(policyNo);
//		List<InterestErrorDto> errorData = new ArrayList<>();
//		if (policyEntity != null) {
//			if (StringUtils.isNotBlank(policyEntity.getQuotationType())
//					&& policyEntity.getQuotationType().equalsIgnoreCase("DC")) {
//				return creditPolicyMembers(policyEntity);
//			} else {
//				errorData.add(InterestErrorDto.builder().policyNo(policyEntity.getPolicyNumber())
//						.memberId(policyEntity.getPolicyNumber()).error("Policy Type i.e.,  \'DC\' is empty.")
//						.username(policyEntity.getModifiedBy()).policyType(policyEntity.getQuotationType())
//						.txnType(InterestFundConstants.CREDIT).build());
//				/***
//				 * throw new ApplicationException("Policy Type i.e., \'DC\' is empty.");
//				 */
//			}
//		} else {
//			errorData.add(InterestErrorDto.builder().policyNo(policyNo).memberId(policyNo)
//					.error("No policy detail found for the policy no." + policyNo).username(policyNo)
//					.policyType(policyNo).txnType(InterestFundConstants.CREDIT).build());
//
//		}
//		saveErrorDetails(errorData);
//		commonResponseDto.setErrorData(errorData);
//		commonResponseDto.setStatus(CommonConstants.ERROR);
//		logger.info(CommonConstants.LOGEND + "::{}", policyNo);
//		return commonResponseDto;
//	}

	/***
	 * @description Credit Deposit amount for all members of DC policy to calculate
	 *              the Interest/Fund Statement by PolicyEntity as request parameter
	 * @param policyEntity
	 */
//	@Override
//	public InterestRateResponseDto creditPolicyMembers(PolicyEntity entity) {
//		logger.info(CommonConstants.LOGSTART + "::{}", entity.getPolicyNumber());
//		InterestRateResponseDto commonResponseDto = new InterestRateResponseDto();
//
//		List<PolicyMbrEntity> policyMembers = policyMbrRepository.findByPolicyIdAndIsActiveTrueAndIsZeroIdFalse(entity.getPolicyId());
//		if (CommonUtils.isNonEmptyArray(policyMembers)) {
//			List<InterestFundDto> members = new ArrayList<>();
//			List<InterestErrorDto> errorData = new ArrayList<>();
//			policyMembers.forEach(member -> {
//				InterestFundDto dto = new InterestFundDto();
//
//				try {
//					setInterestFundRequest(entity, dto);
//					validateCreditRequest(dto);
//					dto.setMemberId(member.getMemberShipId());
//					InterestFundMemberContributionDto memberContribution = setInterestMemberContribution(entity,
//							member);
//					dto.setTxnType(InterestFundConstants.CREDIT);
//					dto.setMembercontribution(memberContribution);
//					validateMemberContributionRequest(memberContribution);
//					members.add(dto);
//				} catch (ApplicationException e) {
//					errorData.add(InterestErrorDto.builder().policyNo(dto.getPolicyNumber())
//							.memberId(member.getMemberShipId()).error(e.getMessage()).username(dto.getModifiedBy())
//							.policyType(dto.getPolicyType()).txnType(InterestFundConstants.CREDIT).build());
//
//				}
//
//			});
//			saveErrorDetails(errorData);
//			if (CommonUtils.isNonEmptyArray(members)) {
//				commonResponseDto = fundRestApiService.creditPolicyMembers(members);
//				commonResponseDto.setErrorData(errorData);
//				return commonResponseDto;
//			} else {
//				commonResponseDto.setStatus(CommonConstants.ERROR);
//				commonResponseDto.setErrorData(errorData);
//				commonResponseDto.setMessage(
//						"No valid member data found to process the credit transaction for the given policy number :"
//								+ entity.getPolicyNumber());
//			}
//		} else {
//			commonResponseDto.setStatus(CommonConstants.ERROR);
//			commonResponseDto.setMessage("No member found for the given policy number:" + entity.getPolicyNumber());
//		}
//		logger.info(CommonConstants.LOGEND + "::{}", entity.getPolicyNumber());
//
//		return commonResponseDto;
//	}

	/***
	 * @description Credit Deposit amount for specific MembershipId of DC policy to
	 *              calculate the Interest/Fund Statement by Policy number and
	 *              membershipId as request parameter
	 * @param policyNo,memberId
	 */
//	@Override
//	public InterestRateResponseDto creditPolicyMember(String policyNo, String memberId) {
//		logger.info(CommonConstants.LOGSTART + LOG_METHOD_PARAM, policyNo, memberId);
//		InterestRateResponseDto commonResponseDto = new InterestRateResponseDto();
//		PolicyEntity policyEntity = policyRepository.findByPolicyNumberAndIsActiveTrue(policyNo);
//		List<InterestErrorDto> errorData = new ArrayList<>();
//		if (policyEntity != null) {
//			if (StringUtils.isNotBlank(policyEntity.getQuotationType())
//					&& policyEntity.getQuotationType().equalsIgnoreCase("DC")) {
//				try {
//					return creditPolicyMember(policyEntity, memberId);
//				} catch (ApplicationException e) {
//					errorData.add(InterestErrorDto.builder().policyNo(policyEntity.getPolicyNumber())
//							.memberId(policyEntity.getPolicyNumber()).error(e.getMessage())
//							.username(policyEntity.getModifiedBy()).policyType(policyEntity.getQuotationType())
//							.txnType(InterestFundConstants.CREDIT).build());
//				}
//			} else {
//				errorData.add(InterestErrorDto.builder().policyNo(policyEntity.getPolicyNumber())
//						.memberId(policyEntity.getPolicyNumber()).error("Policy Type i.e., \'DC\' is empty.")
//						.username(policyEntity.getModifiedBy()).policyType(policyEntity.getQuotationType())
//						.txnType(InterestFundConstants.CREDIT).build());
//				/***
//				 * throw new ApplicationException("Policy Type i.e., \'DC\' is empty.");
//				 */
//			}
//		}
//		saveErrorDetails(errorData);
//		commonResponseDto.setErrorData(errorData);
//		commonResponseDto.setStatus(CommonConstants.ERROR);
//
//		logger.info(CommonConstants.LOGEND + LOG_METHOD_PARAM, policyNo, memberId);
//		return commonResponseDto;
//	}

	/***
	 * @description Credit Deposit amount for specific MembershipId of DC policy to
	 *              calculate the Interest/Fund Statement by PolicyEntity and
	 *              membershipId as request parameter
	 * @param policyNo,memberId
	 */
//	@Override
//	public InterestRateResponseDto creditPolicyMember(PolicyEntity entity, String memberId)
//			throws ApplicationException {
//		logger.info(CommonConstants.LOGSTART + LOG_METHOD_PARAM, entity.getPolicyNumber(), memberId);
//		InterestRateResponseDto commonResponseDto = null;
//
//		PolicyMbrEntity member = policyMbrRepository.findByMemberShipIdAndIsActiveTrueAndIsZeroIdFalse(memberId);
//		if (member != null) {
//			List<InterestErrorDto> errorData = new ArrayList<>();
//			InterestFundDto dto = new InterestFundDto();
//			List<InterestFundDto> members = new ArrayList<>();
//			try {
//				setInterestFundRequest(entity, dto);
//				validateCreditRequest(dto);
//				dto.setMemberId(member.getMemberShipId());
//				InterestFundMemberContributionDto memberContribution = setInterestMemberContribution(entity, member);
//				dto.setTxnType(InterestFundConstants.CREDIT);
//				dto.setMembercontribution(memberContribution);
//				validateMemberContributionRequest(memberContribution);
//				members.add(dto);
//			} catch (ApplicationException e) {
//				throw new ApplicationException(e.getMessage());
//			} finally {
//				logger.info(CommonConstants.LOGSTART + LOG_METHOD_PARAM, entity.getPolicyNumber(), memberId);
//			}
//			saveErrorDetails(errorData);
//			if (CommonUtils.isNonEmptyArray(members)) {
//				commonResponseDto = fundRestApiService.creditPolicyMembers(members);
//				commonResponseDto.setErrorData(errorData);
//				return commonResponseDto;
//			} else {
//				throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE
//						+ "No valid member data found to process the credit transaction for the given policy number:");
//			}
//		} else {
//			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE
//					+ "No member found for the given policy number:" + entity.getPolicyNumber());
//		}
//	}

	/***
	 * @description Credit Deposit amount for specific set of members of DC policy
	 *              to calculate the Interest/Fund Statement by PolicyEntity and
	 *              list of membershipId as request parameter
	 * @param policyEntity,memberIds list
	 */
//	@Override
//	public InterestRateResponseDto creditPolicyMembersByMembershipIds(PolicyEntity entity, List<String> memberIds)
//			throws ApplicationException {
//		logger.info(CommonConstants.LOGSTART + "::{}", entity.getPolicyNumber());
//
//		InterestRateResponseDto commonResponseDto = new InterestRateResponseDto();
//
//		List<PolicyMbrEntity> policyMembers = policyMbrRepository
//				.findByMemberShipIdInAndPolicyIdAndIsActiveTrueAndIsZeroIdFalse(memberIds, entity.getPolicyId());
//		if (CommonUtils.isNonEmptyArray(policyMembers)) {
//			List<InterestFundDto> members = new ArrayList<>();
//			List<InterestErrorDto> errorData = new ArrayList<>();
//			policyMembers.forEach(member -> {
//				InterestFundDto dto = new InterestFundDto();
//
//				try {
//					setInterestFundRequest(entity, dto);
//					validateCreditRequest(dto);
//					dto.setMemberId(member.getMemberShipId());
//					InterestFundMemberContributionDto memberContribution = setInterestMemberContribution(entity,
//							member);
//					dto.setTxnType(InterestFundConstants.CREDIT);
//					dto.setMembercontribution(memberContribution);
//					validateMemberContributionRequest(memberContribution);
//					members.add(dto);
//				} catch (ApplicationException e) {
//					errorData.add(InterestErrorDto.builder().policyNo(dto.getPolicyNumber())
//							.memberId(member.getMemberShipId()).error(e.getMessage()).username(dto.getModifiedBy())
//							.policyType(dto.getPolicyType()).txnType(InterestFundConstants.CREDIT).build());
//
//				}
//
//			});
//			saveErrorDetails(errorData);
//			if (CommonUtils.isNonEmptyArray(members)) {
//				commonResponseDto = fundRestApiService.creditPolicyMembers(members);
//				commonResponseDto.setErrorData(errorData);
//				return commonResponseDto;
//			} else {
//				commonResponseDto.setStatus(CommonConstants.ERROR);
//				commonResponseDto.setErrorData(errorData);
//				commonResponseDto.setMessage(
//						"No valid member data found to process the credit transaction for the given policy number:"
//								+ entity.getPolicyNumber());
//			}
//		} else {
//			throw new ApplicationException(
//					InterestFundConstants.SA_FUND_SERVICE + "No membership id found for the given request.");
//		}
//		logger.info(CommonConstants.LOGEND + "::{}", entity.getPolicyNumber());
//		return commonResponseDto;
//	}

	/***
	 * @description Credit Deposit amount for specific set of members of DC policy
	 *              to calculate the Interest/Fund Statement by PolicyEntity and
	 *              list of membershipId as request parameter
	 * @param requestDto(PolicyNo,List of MembershipIds)
	 */
//	@Override
//	@SuppressWarnings("unchecked")
//	public InterestRateResponseDto creditBulkPolicyMember(InterestRequestDto requestDto) throws ApplicationException {
//		logger.info(CommonConstants.LOGSTART + "::{}", requestDto.getPolicyNo());
//		InterestRateResponseDto commonResponseDto = new InterestRateResponseDto();
//		PolicyEntity policyEntity = policyRepository.findByPolicyNumberAndIsActiveTrue(requestDto.getPolicyNo());
//		List<InterestErrorDto> errorData = new ArrayList<>();
//		if (policyEntity != null) {
//			List<String> memberIds = requestDto.getRequestData() != null ? (List<String>) requestDto.getRequestData()
//					: Collections.emptyList();
//			if (StringUtils.isNotBlank(policyEntity.getQuotationType())
//					&& policyEntity.getQuotationType().equalsIgnoreCase("DC")) {
//				return creditPolicyMembersByMembershipIds(policyEntity, memberIds);
//			} else {
//				errorData.add(InterestErrorDto.builder().policyNo(policyEntity.getPolicyNumber())
//						.memberId(policyEntity.getPolicyNumber()).error("Policy Type i.e., \'DC\' is empty.")
//						.username(policyEntity.getModifiedBy()).policyType(policyEntity.getQuotationType())
//						.txnType(InterestFundConstants.CREDIT).build());
//				/***
//				 * throw new ApplicationException("Policy Type i.e., \'DC\' is empty.");
//				 */
//			}
//		} else {
//			errorData.add(
//					InterestErrorDto.builder().policyNo(requestDto.getPolicyNo()).memberId(requestDto.getPolicyNo())
//							.error("No policy detail found for the policy no." + requestDto.getPolicyNo())
//							.username(requestDto.getUsername()).policyType(requestDto.getServiceType())
//							.txnType(InterestFundConstants.CREDIT).build());
//
//		}
//		saveErrorDetails(errorData);
//		commonResponseDto.setErrorData(errorData);
//		commonResponseDto.setStatus(CommonConstants.ERROR);
//		logger.info(CommonConstants.LOGEND + "::{}", requestDto.getPolicyNo());
//		return commonResponseDto;
//	}

	/***
	 * @description DEBIT amount for the policy number from the Interest/Fund
	 *              service in case of claim such as Death/Closure etc.,
	 * @implNote "DB" Policy
	 * @param policyNo
	 */
//	@Override
//	public InterestRateResponseDto debitPolicy(String policyNo) throws ApplicationException {
//		logger.info(CommonConstants.LOGSTART + "::{}", policyNo);
//		InterestRateResponseDto commonResponseDto = new InterestRateResponseDto();
//		PolicyEntity policyEntity = policyRepository.findByPolicyNumberAndIsActiveTrue(policyNo);
//		List<InterestErrorDto> errorData = new ArrayList<>();
//		if (policyEntity != null) {
//			if (StringUtils.isNotBlank(policyEntity.getQuotationType())
//					&& policyEntity.getQuotationType().equalsIgnoreCase(InterestFundConstants.DB)) {
//
//				DebitRequestDto dto = new DebitRequestDto();
//				try {
//					setDebitRequestDtoForDB(policyEntity, dto);
//					return fundRestApiService.debitPolicy(dto);
//				} catch (ApplicationException e) {
//					errorData.add(InterestErrorDto.builder().policyNo(policyEntity.getPolicyNumber())
//							.memberId(policyEntity.getPolicyNumber()).error(e.getMessage())
//							.username(policyEntity.getModifiedBy()).policyType(policyEntity.getQuotationType())
//							.txnType(InterestFundConstants.DEBIT).build());
//				}
//
//			} else {
//				errorData.add(InterestErrorDto.builder().policyNo(policyEntity.getPolicyNumber())
//						.memberId(policyEntity.getPolicyNumber()).error("Policy Type should be i.e., \'DB\' or empty.")
//						.username(policyEntity.getModifiedBy()).policyType(policyEntity.getQuotationType())
//						.txnType(InterestFundConstants.DEBIT).build());
//			}
//			saveErrorDetails(errorData);
//		}
//		commonResponseDto.setStatus(CommonConstants.ERROR);
//		commonResponseDto.setErrorData(errorData);
//		logger.info(CommonConstants.LOGEND + "::{}", policyNo);
//		return commonResponseDto;
//
//	}

	/***
	 * @description DEBIT amount for the given MembershipId from the Interest/Fund
	 *              service in case of claim such as Death/Closure etc.,
	 * @implNote "DC" Policy
	 * @param policyNo, memberId
	 */
//	@Override
//	public InterestRateResponseDto debitPolicyMemberById(String policyNo, String memberId) throws ApplicationException {
//		logger.info(CommonConstants.LOGSTART + LOG_METHOD_PARAM, policyNo, memberId);
//		InterestRateResponseDto commonResponseDto = new InterestRateResponseDto();
//		PolicyEntity policyEntity = policyRepository.findByPolicyNumberAndIsActiveTrue(policyNo);
//		if (policyEntity != null) {
//			DebitRequestDto dto = new DebitRequestDto();
//			setDebitRequestDto(policyEntity, dto);
//			dto.setMemberId(memberId);
//			return fundRestApiService.debitPolicyMember(dto);
//		}
//		commonResponseDto.setStatus(CommonConstants.ERROR);
//		logger.info(CommonConstants.LOGEND + LOG_METHOD_PARAM, policyNo, memberId);
//		return commonResponseDto;
//
//	}

	/***
	 * @description DEBIT amount for all the members under the given policy number
	 *              from the Interest/Fund service in case of claim such as
	 *              Death/Closure etc.,
	 * @implNote "DC" Policy
	 * @param policyNo
	 */
//	@Override
//	public InterestRateResponseDto debitMembersByPolicy(String policyNo) {
//		logger.info(CommonConstants.LOGSTART + "::{}", policyNo);
//		InterestRateResponseDto commonResponseDto = new InterestRateResponseDto();
//		PolicyEntity policyEntity = policyRepository.findByPolicyNumberAndIsActiveTrue(policyNo);
//		if (policyEntity != null) {
//			List<DebitRequestDto> debitRequest = new ArrayList<>();
//			setDebitRequestDtoList(policyEntity, debitRequest, commonResponseDto);
//			if (CommonUtils.isNonEmptyArray(debitRequest)) {
//				InterestRateResponseDto responseDto = fundRestApiService.debitPolicyMembers(debitRequest);
//				responseDto.setErrorData(commonResponseDto.getErrorData());
//				return responseDto;
//			} else {
//				commonResponseDto.setMessage(
//						"No valid member data found to process the Debit transaction for the given policy number:"
//								+ policyEntity.getPolicyNumber());
//			}
//
//		}
//		commonResponseDto.setStatus(CommonConstants.ERROR);
//		logger.info(CommonConstants.LOGEND + "::{}", policyNo);
//		return commonResponseDto;
//
//	}

	/***
	 * @description DEBIT amount for the specific set of given members under the
	 *              given policy number from the Interest/Fund service in case of
	 *              claim such as Death/Closure etc.,
	 * @implNote "DC" Policy
	 * @param policyNo, List of members
	 */
//	@Override
//	@SuppressWarnings("unchecked")
//	public InterestRateResponseDto debitBulkPolicyMember(InterestRequestDto requestDto) throws ApplicationException {
//		logger.info(CommonConstants.LOGSTART + "::{}", requestDto.getPolicyNo());
//
//		InterestRateResponseDto commonResponseDto = new InterestRateResponseDto();
//		PolicyEntity policyEntity = policyRepository.findByPolicyNumberAndIsActiveTrue(requestDto.getPolicyNo());
//		if (policyEntity != null) {
//			List<String> memberIds = requestDto.getRequestData() != null ? (List<String>) requestDto.getRequestData()
//					: Collections.emptyList();
//			List<DebitRequestDto> debitRequest = new ArrayList<>();
//			setBulkMemberDebitRequest(policyEntity, debitRequest, commonResponseDto, memberIds);
//			if (CommonUtils.isNonEmptyArray(debitRequest)) {
//				InterestRateResponseDto responseDto = fundRestApiService.debitPolicyMembers(debitRequest);
//				responseDto.setErrorData(commonResponseDto.getErrorData());
//				return responseDto;
//			} else {
//				commonResponseDto.setMessage(
//						"No valid member data found to process the Debit transaction for the given policy number:"
//								+ policyEntity.getPolicyNumber());
//			}
//
//		}
//		commonResponseDto.setStatus(CommonConstants.ERROR);
//		logger.info(CommonConstants.LOGEND + "::{}", requestDto.getPolicyNo());
//		return commonResponseDto;
//
//	}

	/***
	 * @description Save exception/validation error in the
	 *              INTEREST_ERROR_TXN_DETAILS tbl
	 * @param errorDtos i.e., List of errors
	 */
	@Override
	public void saveErrorDetails(List<InterestErrorDto> errorDtos) {
		logger.info(CommonConstants.LOGSTART);
		if (CommonUtils.isNonEmptyArray(errorDtos)) {
			List<InterestErrorDetailsEntity> entities = new ArrayList<>();
			errorDtos.forEach(error -> {
				String refNo = "";
				if (StringUtils.isNotBlank(error.getPolicyType()) && error.getPolicyType().equalsIgnoreCase("DC")) {
					refNo = error.getMemberId();
				} else {
					refNo = error.getPolicyNo();
				}

				InterestErrorDetailsEntity entity = interestErrorDetailsRepository
						.findByRefNumberAndIsFailTrueOrderByIdDesc(refNo);
				int noOfAttempt = 0;
				if (entity == null) {
					entity = new InterestErrorDetailsEntity();
					entity.setCreatedOn(DateUtils.sysDate());
				} else {
					noOfAttempt = entity.getNoOfAttempt().intValue() + 1;
				}
				entity.setErrorDescription(error.getError());
				entity.setIpAddress("0.0.0.0");
				entity.setIsFail(true);
				entity.setMemberId(error.getMemberId());
				entity.setModifiedOn(DateUtils.sysDate());
				entity.setNoOfAttempt(noOfAttempt);
				entity.setPolicyNo(error.getPolicyNo());
				entity.setRefNumber(refNo);
				entity.setRemarks(CommonConstants.FAIL);
				entity.setTxnType(error.getTxnType());
				entity.setUserName(error.getUsername());
				entity.setRemarks(error.getPolicyType());

				entities.add(entity);
			});
			interestErrorDetailsRepository.saveAll(entities);
		}
		logger.info(CommonConstants.LOGEND);
	}

	public InterestRateResponseDto viewByPolicyNo(String policyNo) {
		return fundRestApiService.viewByPolicyNo(policyNo);
	}

	public InterestRateResponseDto viewMembersByPolicyNo(String policyNo) {
		return fundRestApiService.viewMembersByPolicyNo(policyNo);
	}

	public InterestRateResponseDto viewByMemberId(String memberId) {
		return fundRestApiService.viewByMemberId(memberId);
	}

	@Override
	public InterestRateResponseDto creditPolicy(String policyNo) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InterestRateResponseDto creditPolicyMembers(String policyNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InterestRateResponseDto creditPolicyMember(String policyNo, String memberId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InterestRateResponseDto creditBulkPolicyMember(InterestRequestDto requestDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InterestRateResponseDto debitPolicy(String policyNo) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InterestRateResponseDto debitPolicyMemberById(String policyNo, String memberId) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InterestRateResponseDto debitMembersByPolicy(String policyNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InterestRateResponseDto debitBulkPolicyMember(InterestRequestDto requestDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

}
