/**
 * 
 */
package com.lic.epgs.fund.service.impl;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.lic.epgs.common.dto.FundChangeDto;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.fund.constants.InterestFundConstants;
import com.lic.epgs.integration.dto.DebitRequestDto;
import com.lic.epgs.integration.dto.InterestFundDto;
import com.lic.epgs.integration.dto.InterestFundMemberContributionDto;
import com.lic.epgs.utils.CommonUtils;

/**
 * @author Muruganandam
 *
 */
@Service
public class InterestRateFundHelper {

//	@Autowired
//	private PolicyDepositRepository policyDepositRepository;
//
//	@Autowired
//	private PolicyDepositAdjestmentRepository depositAdjestmentRepository;
//
//	@Autowired
//	private PolicyMbrRepository policyMbrRepository;

//	@Autowired
//	private PolicyInterestFundService policyInterestFundService;
//
//	@Autowired
//	private FundRestApiService fundRestApiService;

	public String getVariant(String variant) {
		switch (variant) {
		case "11":
		case "14":
			return InterestFundConstants.V1;
		case "12":
		case "15":
			return InterestFundConstants.V2;
		case "13":
		case "17":
			return InterestFundConstants.V3;
		default:
			return InterestFundConstants.V3;
		}
	}

//	public void setInterestFundRequest(PolicyEntity entity, InterestFundDto dto) throws ApplicationException {
//		List<PolicyDepositEntity> list = policyDepositRepository.findAllByPolicyId(entity.getPolicyId());
//		List<PolicyDepositAdjustmentEntity> adjustments = depositAdjestmentRepository
//				.findAllByPolicyId(entity.getPolicyId());
//		PolicyDepositAdjustmentEntity policyDepositAdjustmentEntity = null;
//		PolicyDepositEntity depositEntity = new PolicyDepositEntity();
//		if (CommonUtils.isNonEmptyArray(adjustments)) {
//			policyDepositAdjustmentEntity = adjustments.get(0);
//		}
//		/**
//		 * else if (entity.getTotalContribution().compareTo(BigDecimal.ZERO) > 0) {
//		 * policyDepositAdjustmentEntity.setAdjestmentAmount(entity.getTotalContribution
//		 * ()); }
//		 */
//		else {
//			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE
//					+ "No Policy Deposit/Adjustment found for the given policy no. : " + entity.getPolicyNumber());
//		}
//
//		if (CommonUtils.isNonEmptyArray(list)) {
//			depositEntity = list.get(0);
//			if (depositEntity.getCollectionDate() == null) {
//				depositEntity.setCollectionDate(entity.getCreatedOn());
//			}
//		}
//
//		else if (entity.getModifiedOn() != null) {
//			depositEntity.setCollectionDate(policyDepositAdjustmentEntity.getCollectionDate());
//			depositEntity.setCollectionNo(policyDepositAdjustmentEntity.getCollectionNo());
//			depositEntity.setDepositId(policyDepositAdjustmentEntity.getAdjestmentId());
//		} else {
//			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE
//					+ "No Policy Deposit/Adjustment found for the given policy number :" + entity.getPolicyNumber());
//		}
//
//		dto.setCreatedBy(StringUtils.isNotBlank(entity.getCreatedBy()) ? entity.getCreatedBy()
//				: InterestFundConstants.SACHECKER);
//		dto.setModifiedBy(dto.getCreatedBy());
//		dto.setEffectiveTxnDate(DateUtils.dateToStringDDMMYYYY(depositEntity.getCollectionDate()));
//		dto.setIsOpeningbal(false);
//		dto.setMemberId(null);
//		dto.setPolicyNumber(entity.getPolicyNumber());
//		dto.setPolicyType(entity.getQuotationType());
//		dto.setProposalNumber(entity.getProposalNumber());
//		dto.setStream(InterestFundConstants.SUPERANNUATION);
//		dto.setTxnAmount(policyDepositAdjustmentEntity.getAdjestmentAmount());
//		dto.setTxnDate(DateUtils.dateToStringDDMMYYYY(policyDepositAdjustmentEntity.getCreatedOn()));
//		dto.setTxnType(InterestFundConstants.CREDIT);
//		dto.setVariant(getVariant(entity.getVariant()));
//		dto.setDepositId(NumericUtils.convertLongToString(depositEntity.getDepositId()));
//		dto.setCollectionNo(depositEntity.getCollectionNo());
//
//		validateCreditRequest(dto);
//	}
//
//	public void setDebitRequestDtoList(PolicyEntity entity, List<DebitRequestDto> debitRequest,
//			InterestRateResponseDto commonResponseDto) {
//		List<PolicyMbrEntity> policyMembers = policyMbrRepository.findByPolicyIdAndIsActiveTrueAndIsZeroIdFalse(entity.getPolicyId());
//		if (CommonUtils.isNonEmptyArray(policyMembers)) {
//			List<InterestErrorDto> errorData = new ArrayList<>();
//			policyMembers.forEach(member -> {
//				DebitRequestDto dto = new DebitRequestDto();
//
//				dto.setCreatedBy(StringUtils.isNotBlank(entity.getCreatedBy()) ? entity.getCreatedBy()
//						: InterestFundConstants.SACHECKER);
//				dto.setModifiedBy(dto.getCreatedBy());
//				dto.setPolicyNumber(entity.getPolicyNumber());
//				dto.setPolicyType(entity.getQuotationType());
//				dto.setProposalNumber(entity.getProposalNumber());
//				dto.setStream(InterestFundConstants.SUPERANNUATION);
//				dto.setTxnAmount(member.getTotalContribution());
//				dto.setTxnType(InterestFundConstants.DEBIT);
//				dto.setVariant(getVariant(entity.getVariant()));
//				dto.setFrequency(getVariant(entity.getVariant()));
//				dto.setPolicyNumber(entity.getPolicyNumber());
//				dto.setTxnDate(DateUtils.dateToStringDDMMYYYY(entity.getModifiedOn()));
//				dto.setMemberId(member.getMemberShipId());
//
//				InterestFundMemberContributionDto memberContribution = setInterestMemberContribution(entity, member);
//
//				dto.setMemberContribution(memberContribution);
//				dto.setTxnType(InterestFundConstants.DEBIT);
//
//				try {
//					validateDebitRequest(dto);
//					validateMemberContributionRequest(memberContribution);
//
//				} catch (ApplicationException e) {
//					errorData.add(InterestErrorDto.builder().policyNo(dto.getPolicyNumber())
//							.memberId(member.getMemberShipId()).error(e.getMessage()).username(entity.getModifiedBy())
//							.policyType(entity.getQuotationType()).txnType(InterestFundConstants.DEBIT).build());
//				}
//				debitRequest.add(dto);
//			});
//			policyInterestFundService.saveErrorDetails(errorData);
//			commonResponseDto.setErrorData(errorData);
//		}
//
//	}
//
//	public void setDebitRequestDtoForDB(PolicyEntity entity, DebitRequestDto dto) throws ApplicationException {
//		dto.setCreatedBy(StringUtils.isNotBlank(entity.getCreatedBy()) ? entity.getCreatedBy()
//				: InterestFundConstants.SACHECKER);
//		dto.setModifiedBy(dto.getCreatedBy());
//		dto.setPolicyNumber(entity.getPolicyNumber());
//		dto.setPolicyType(entity.getQuotationType());
//		dto.setProposalNumber(entity.getProposalNumber());
//		dto.setStream(InterestFundConstants.SUPERANNUATION);
//		dto.setTxnAmount(entity.getTotalContribution());
//		dto.setTxnType(InterestFundConstants.DEBIT);
//		dto.setVariant(getVariant(entity.getVariant()));
//		dto.setFrequency(getVariant(entity.getVariant()));
//		dto.setPolicyNumber(entity.getPolicyNumber());
//		dto.setTxnDate(DateUtils.dateToStringDDMMYYYY(entity.getModifiedOn()));
//		validateDebitRequest(dto);
//	}
//
//	public void setDebitRequestDto(PolicyEntity entity, DebitRequestDto dto) throws ApplicationException {
//		PolicyMbrEntity member = policyMbrRepository.findByMemberShipIdAndIsActiveTrueAndIsZeroIdFalse(dto.getMemberId());
//		if (member != null) {
//			dto.setCreatedBy(StringUtils.isNotBlank(entity.getCreatedBy()) ? entity.getCreatedBy()
//					: InterestFundConstants.SACHECKER);
//			dto.setModifiedBy(dto.getCreatedBy());
//			dto.setPolicyNumber(entity.getPolicyNumber());
//			dto.setPolicyType(entity.getQuotationType());
//			dto.setProposalNumber(entity.getProposalNumber());
//			dto.setStream(InterestFundConstants.SUPERANNUATION);
//			dto.setTxnAmount(member.getTotalContribution());
//			dto.setTxnType(InterestFundConstants.DEBIT);
//			dto.setVariant(getVariant(entity.getVariant()));
//			dto.setFrequency(getVariant(entity.getVariant()));
//			dto.setPolicyNumber(entity.getPolicyNumber());
//			validateDebitRequest(dto);
//			dto.setMemberId(member.getMemberShipId());
//			InterestFundMemberContributionDto memberContribution = setInterestMemberContribution(entity, member);
//			validateMemberContributionRequest(memberContribution);
//			dto.setMemberContribution(memberContribution);
//			dto.setMemberId(member.getMemberShipId());
//		}
//	}
//
//	public void setBulkMemberDebitRequest(PolicyEntity entity, List<DebitRequestDto> debitRequest,
//			InterestRateResponseDto commonResponseDto, List<String> memberIds) throws ApplicationException {
//		List<PolicyMbrEntity> policyMembers = policyMbrRepository
//				.findByMemberShipIdInAndPolicyIdAndIsActiveTrueAndIsZeroIdFalse(memberIds, entity.getPolicyId());
//		if (CommonUtils.isNonEmptyArray(policyMembers)) {
//			List<InterestErrorDto> errorData = new ArrayList<>();
//			policyMembers.forEach(member -> {
//				DebitRequestDto dto = new DebitRequestDto();
//
//				dto.setCreatedBy(StringUtils.isNotBlank(entity.getCreatedBy()) ? entity.getCreatedBy()
//						: InterestFundConstants.SACHECKER);
//				dto.setModifiedBy(dto.getCreatedBy());
//				dto.setPolicyNumber(entity.getPolicyNumber());
//				dto.setPolicyType(entity.getQuotationType());
//				dto.setProposalNumber(entity.getProposalNumber());
//				dto.setStream(InterestFundConstants.SUPERANNUATION);
//				dto.setTxnAmount(member.getTotalContribution());
//				dto.setTxnType(InterestFundConstants.DEBIT);
//				dto.setVariant(getVariant(entity.getVariant()));
//				dto.setFrequency(getVariant(entity.getVariant()));
//				dto.setPolicyNumber(entity.getPolicyNumber());
//				dto.setTxnDate(DateUtils.dateToStringDDMMYYYY(entity.getModifiedOn()));
//				dto.setMemberId(member.getMemberShipId());
//
//				InterestFundMemberContributionDto memberContribution = setInterestMemberContribution(entity, member);
//
//				dto.setMemberContribution(memberContribution);
//				dto.setTxnType(InterestFundConstants.DEBIT);
//
//				try {
//					validateDebitRequest(dto);
//					validateMemberContributionRequest(memberContribution);
//
//				} catch (ApplicationException e) {
//					errorData.add(InterestErrorDto.builder().policyNo(dto.getPolicyNumber())
//							.memberId(member.getMemberShipId()).error(e.getMessage()).username(entity.getModifiedBy())
//							.policyType(entity.getQuotationType()).txnType(InterestFundConstants.DEBIT).build());
//				}
//				debitRequest.add(dto);
//			});
//			policyInterestFundService.saveErrorDetails(errorData);
//			commonResponseDto.setErrorData(errorData);
//		} else {
//			throw new ApplicationException(
//					InterestFundConstants.SA_FUND_SERVICE + "No membership id found for the given request.");
//		}
//
//	}
//
//	public InterestFundMemberContributionDto setInterestMemberContribution(PolicyEntity entity,
//			PolicyMbrEntity member) {
//		InterestFundMemberContributionDto memberContribution = new InterestFundMemberContributionDto();
//		memberContribution.setEmployeeContributionAmount(member.getEmployeeContribution());
//		memberContribution.setEmployerContributionAmount(member.getEmployerContribution());
//		memberContribution.setVoluntaryContributionAmount(member.getVoluntaryContribution());
//		memberContribution.setTotalContributionAmount(member.getTotalContribution());
//		memberContribution.setMemberId(member.getMemberShipId());
//		memberContribution.setPolicyNumber(entity.getPolicyNumber());
//		return memberContribution;
//	}

	/****
	 * @description To update the policy/member fund based on policy
	 *              number/membership number
	 * @apiNote updateType :: CONVERSION/MERGE/SURRENDER/FREELOOK, stream ::
	 *          Superannuation, module :: Policy Merge/Conversion/Freelook
	 *          Cancellation/Surrender
	 * 
	 */
//	public void setFundChangeDtoRequest(PolicyEntity entity, FundChangeDto dto) throws ApplicationException {
//
//		InterestRateResponseDto responseDto = fundRestApiService.viewByPolicyNo(entity.getPolicyNumber());
//		if (responseDto != null) {
//			if (StringUtils.isNotBlank(responseDto.getStatus())
//					&& responseDto.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
//				List<PolicyDepositEntity> list = policyDepositRepository.findAllByPolicyId(entity.getPolicyId());
//				List<PolicyDepositAdjustmentEntity> adjustments = depositAdjestmentRepository
//						.findAllByPolicyId(entity.getPolicyId());
//				PolicyDepositAdjustmentEntity policyDepositAdjustmentEntity = null;
//				PolicyDepositEntity depositEntity = new PolicyDepositEntity();
//				if (CommonUtils.isNonEmptyArray(adjustments)) {
//					policyDepositAdjustmentEntity = adjustments.get(0);
//				}
//				/**
//				 * else if (entity.getTotalContribution().compareTo(BigDecimal.ZERO) > 0) {
//				 * policyDepositAdjustmentEntity.setAdjestmentAmount(entity.getTotalContribution
//				 * ()); }
//				 */
//				else {
//					throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE
//							+ "No Policy Deposit/Adjustment found for the given policy no. :"
//							+ entity.getPolicyNumber());
//				}
//
//				if (CommonUtils.isNonEmptyArray(list)) {
//					depositEntity = list.get(0);
//					if (depositEntity.getCollectionDate() == null) {
//						depositEntity.setCollectionDate(entity.getCreatedOn());
//					}
//				}
//
//				else if (entity.getModifiedOn() != null) {
//					depositEntity.setCollectionDate(policyDepositAdjustmentEntity.getCollectionDate());
//					depositEntity.setCollectionNo(policyDepositAdjustmentEntity.getCollectionNo());
//					depositEntity.setDepositId(policyDepositAdjustmentEntity.getAdjestmentId());
//				} else {
//					throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE
//							+ "No Policy Deposit/Adjustment found for the given policy no. :"
//							+ entity.getPolicyNumber());
//				}
//
//				dto.setCreatedBy(StringUtils.isNotBlank(entity.getCreatedBy()) ? entity.getCreatedBy()
//						: InterestFundConstants.SACHECKER);
//				dto.setPolicyNumber(entity.getPolicyNumber());
//				dto.setPolicyType(entity.getQuotationType());
//				dto.setStream(InterestFundConstants.SUPERANNUATION);
//				dto.setTxnDate(DateUtils.dateToStringDDMMYYYY(DateUtils.sysDate()));
//				dto.setVariant(getVariant(entity.getVariant()));
//				dto.setTxnAmount(policyDepositAdjustmentEntity.getAdjestmentAmount());
//
//				validateFundRequest(dto);
//			} else {
//				policyInterestFundService.creditPolicy(entity);
//				dto.setIsNew(true);
//
//				/***
//				 * setFundChangeDtoRequest(entity, dto);
//				 */
//			}
//		} else {
//			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "No response");
//		}
//
//	}

	/***
	 * @author Muruganandam
	 * @implNote Validate the mandatory parameter for credit transaction
	 * @implNote Policy Type : DB and DC
	 */
	public void validateCreditRequest(InterestFundDto dto) throws ApplicationException {

		if (StringUtils.isBlank(dto.getEffectiveTxnDate())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Effective Date is required");
		}
		if (StringUtils.isBlank(dto.getCreatedBy())) {
			throw new ApplicationException("Username is required.");
		}
		/**
		 * if (StringUtils.isBlank(dto.getModifiedBy())) { throw new
		 * ApplicationException("Modifiying Username is required"); }
		 */
		if (StringUtils.isBlank(dto.getPolicyNumber())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Policy Number is required.");
		}
		if (StringUtils.isBlank(dto.getPolicyType())) {
			throw new ApplicationException(
					InterestFundConstants.SA_FUND_SERVICE + "Policy Type i.e., DB/DC is required.");
		}
		if (StringUtils.isBlank(dto.getProposalNumber())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Proposal Number is required");
		}
		if (dto.getTxnAmount() == null
				|| dto.getTxnAmount() != null && dto.getTxnAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new ApplicationException(
					InterestFundConstants.SA_FUND_SERVICE + "Transaction/Adjusted Amount is required");
		}
		if (StringUtils.isBlank(dto.getEffectiveTxnDate())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Transaction Date is required.");
		}
		if (StringUtils.isBlank(dto.getVariant())) {
			throw new ApplicationException(
					InterestFundConstants.SA_FUND_SERVICE + "Variant i.e., V1/V2/V3 is required.");
		}
		if (StringUtils.isBlank(dto.getTxnType())) {
			throw new ApplicationException(
					InterestFundConstants.SA_FUND_SERVICE + "Transaction Type i.e., CREDIT is required");
		}
		if (StringUtils.isBlank(dto.getCollectionNo())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Collection Number is required");
		}
		if (StringUtils.isBlank(dto.getDepositId())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Deposit ID is required");
		}

	}

	/***
	 * @author Muruganandam
	 * @implNote Validate the mandatory parameter for Member contribution
	 * @implNote Policy Type : DC
	 */
	public void validateMemberContributionRequest(InterestFundMemberContributionDto dto) throws ApplicationException {

		if (dto.getEmployeeContributionAmount() == null
				|| dto.getEmployeeContributionAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Employee Contribution is required");
		}

		if (dto.getEmployerContributionAmount() == null
				|| dto.getEmployerContributionAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Employer Contribution is required");
		}
		if (dto.getVoluntaryContributionAmount() == null
				|| dto.getVoluntaryContributionAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new ApplicationException(
					InterestFundConstants.SA_FUND_SERVICE + "Voluntary Contribution is required");
		}
		if (dto.getTotalContributionAmount() == null
				|| dto.getTotalContributionAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Total Contribution is required");
		}

		if (dto.getPolicyId() == null) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Policy Number is required.");
		}
		if (StringUtils.isBlank(dto.getMemberId())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "LIC Id is required");
		}
	}

	/***
	 * @author Muruganandam
	 * @implNote Validate the mandatory parameter for DEBIT Transaction
	 * @implNote Policy Type : DB and DC
	 */
	public void validateDebitRequest(DebitRequestDto dto) throws ApplicationException {

		if (StringUtils.isBlank(dto.getCreatedBy())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Username is required");
		}
		/**
		 * if (StringUtils.isBlank(dto.getModifiedBy())) { throw new
		 * ApplicationException("Modifiying Username is required"); }
		 */
		if (StringUtils.isBlank(dto.getPolicyNumber())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Policy Number is required");
		}
		if (StringUtils.isBlank(dto.getPolicyType())) {
			throw new ApplicationException(
					InterestFundConstants.SA_FUND_SERVICE + "Policy Type i.e., DB/DC is required");
		}
		if (StringUtils.isBlank(dto.getProposalNumber())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Proposal Number is required");
		}
		if (dto.getTxnAmount() != null && dto.getTxnAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new ApplicationException(
					InterestFundConstants.SA_FUND_SERVICE + "Transactio/Adjusted Amount is required");
		}
		if (StringUtils.isBlank(dto.getTxnDate())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Transaction Date is required");
		}
		if (StringUtils.isBlank(dto.getVariant())) {
			throw new ApplicationException(
					InterestFundConstants.SA_FUND_SERVICE + "Variant i.e., V1/V2/V3 is required ");
		}
		if (StringUtils.isBlank(dto.getTxnType())) {
			throw new ApplicationException(
					InterestFundConstants.SA_FUND_SERVICE + "Transaction Type i.e., DEBIT is required");
		}
		if (StringUtils.isBlank(dto.getCollectionNo())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Collection Number is required");
		}
		if (StringUtils.isBlank(dto.getDepositId())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Deposit ID CREDIT is required");
		}
	}

	public static void validateFundRequest(FundChangeDto dto) throws ApplicationException {

		if (StringUtils.isBlank(dto.getCreatedBy())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Username is required");
		}
		/**
		 * if (StringUtils.isBlank(dto.getModifiedBy())) { throw new
		 * ApplicationException("Modifiying Username is required"); }
		 */
		/*
		 * if (StringUtils.isBlank(dto.getDestinationPolicyNumber())) { throw new
		 * ApplicationException(InterestFundConstants.SA_FUND_SERVICE +
		 * "Policy Number is required"); }
		 */

		if (StringUtils.isBlank(dto.getPolicyType())) {
			throw new ApplicationException(
					InterestFundConstants.SA_FUND_SERVICE + "Policy Type i.e., DB/DC is required");
		}

		if (StringUtils.isBlank(dto.getVariant())) {
			throw new ApplicationException(
					InterestFundConstants.SA_FUND_SERVICE + "Variant i.e., V1/V2/V3 is required");
		}

		if (StringUtils.isBlank(dto.getUpdateType())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE
					+ "Update Type i.e., CONVERSION/MERGE/PARTIAL/FULL is required");
		}

		if (StringUtils.isNotBlank(dto.getPolicyType()) && dto.getPolicyType().equalsIgnoreCase("DC")
				&& !CommonUtils.isNonEmptyArray(dto.getMembers())) {
			throw new ApplicationException(
					InterestFundConstants.SA_FUND_SERVICE + "Variant i.e., V1/V2/V3 is required");
		}

		if (StringUtils.isBlank(dto.getEffectiveTxnDate())) {
			throw new ApplicationException(InterestFundConstants.SA_FUND_SERVICE + "Transaction Date is required");
		}

	}
}
