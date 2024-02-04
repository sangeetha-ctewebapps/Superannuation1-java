package com.lic.epgs.policy.service.impl;

/**
 * @author pradeepramesh
 *
 */

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.policy.dto.CommonResponsePolicyCalcDto;
import com.lic.epgs.policy.dto.MembersContributionDetailsDto;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MemberTransactionEntriesEntity;
import com.lic.epgs.policy.entity.MemberTransactionSummaryEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.PolicyTransactionEntriesEntity;
import com.lic.epgs.policy.entity.PolicyTransactionSummaryEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntriesEntity;
import com.lic.epgs.policy.repository.MemberContributionRepository;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.MemberTransactionEntriesRepository;
import com.lic.epgs.policy.repository.MemberTransactionSummaryRepository;
import com.lic.epgs.policy.repository.PolicyContributionRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.policy.repository.PolicyTransactionEntriesRepository;
import com.lic.epgs.policy.repository.PolicyTransactionSummaryRepository;
import com.lic.epgs.policy.repository.ZeroAccountEntriesRepository;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

@Service
public class PolicyCalcServiceImpl {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	PolicyMasterRepository policyMasterRepository;

	@Autowired
	PolicyTransactionEntriesRepository policyTransactionEntriesRepository;

	@Autowired
	PolicyTransactionSummaryRepository policyTransactionSummaryRepository;

	@Autowired
	PolicyContributionRepository policyContributionRepository;

	@Autowired
	MemberTransactionEntriesRepository memberTransactionEntriesRepository;

	@Autowired
	MemberTransactionSummaryRepository memberTransactionSummaryRepository;

	@Autowired
	MemberContributionRepository memberContributionRepository;

	@Autowired
	ZeroAccountEntriesRepository zeroAccountEntriesRepository;

	@Autowired
	MemberMasterRepository memberMasterRepository;

	public static final String V1 = "V1";
	public static final String V2 = "V2";
	public static final String V3 = "V3";

	public static final String CONTRIBUTION_ADJUSTMENT = "NB";
	public static final String REGULAR_ADJUSTMENT = "RA";
	public static final String SUBSEQUENT_ADJUSTMENT = "SA";

	public static final String CREDIT = "CREDIT";
	public static final String DEBIT = "DEBIT";

	public static final String CR = "CR";
	public static final String DR = "DR";

	public static final String DC = "DC";
	public static final String DB = "DB";

	@Transactional
	public CommonResponsePolicyCalcDto<Map<String, Object>> setZeroAccEntriesToTxnEntires(Long policyId,
			String variantType) throws ApplicationException {
		logger.info(" PolicyCalcServiceImpl - SetZeroAccEntriesToTxnEntires Starts - policyId - {}  ", policyId);
		String financialYear = DateUtils.getFinancialYrByDt(DateUtils.sysDate());

		PolicyMasterEntity policyMasterEntity = policyMasterRepository.findByPolicyIdAndIsActiveTrue(policyId);
		if (policyMasterEntity != null) {
			logger.info(" PolicyCalcServiceImpl - SetZeroAccEntriesToTxnEntires Ends - policyId - {} ", policyId);
			return savePolicyAndMemberTxnEntries(policyMasterEntity, financialYear, variantType);
		} else {
			logger.info(" PolicyCalcServiceImpl - SetZeroAccEntriesToTxnEntires Exception ");
			throw new ApplicationException("No policy details found for the given policy id:" + policyId);
		}
	}

	private CommonResponsePolicyCalcDto<Map<String, Object>> savePolicyAndMemberTxnEntries(
			PolicyMasterEntity masterEntity, String financialYear, String variantType) throws ApplicationException {
		logger.info("savePolicyAndMemberContributionEntries::{},{}", CommonConstants.LOGSTART,
				masterEntity.getPolicyId());
		long start = System.currentTimeMillis();
		CommonResponsePolicyCalcDto<Map<String, Object>> responseDto = new CommonResponsePolicyCalcDto<>();
		List<String> errors = new ArrayList<>();
		boolean isPolicySuccess = true;
		List<ZeroAccountEntriesEntity> policyContributionEntities = fetchZeroAccEntiresByVariant(masterEntity,
				variantType);
		Map<String, Object> memberMap = new HashMap<>();

		Map<Long, List<ZeroAccountEntriesEntity>> zeroEntriesMap = new HashMap<>();

		if (CommonUtils.isNonEmptyArray(policyContributionEntities)) {
			/***
			 * @notes Set of txnEntryStatus with false/null
			 */
			List<ZeroAccountEntriesEntity> policyContributions = policyContributionEntities.stream()
					.filter(pc -> !NumericUtils.isBooleanNotNull(pc.getTxnEntryStatus())).collect(Collectors.toList());
			/***
			 * @notes Map Policy contribution to Policy Transaction Entries
			 */
			zeroEntriesMap = getZeroAccountEntriesByPolicyId(policyContributions);
			List<PolicyTransactionEntriesEntity> policyEntries = mapZeroAccountEntriesAndTxnEntries(zeroEntriesMap,
					masterEntity, variantType);
			policyTransactionEntriesRepository.saveAll(policyEntries);

			printContributions(policyContributions, policyEntries);
			policyTransactionSummary(policyEntries, masterEntity, variantType);
			/***
			 * @notes update transaction status for policy and member contribution
			 */
			if (CommonUtils.isNonEmptyArray(policyEntries)) {
				updatePolicyContributionTxnStatus(policyContributions);
			}
			if (!CommonUtils.isNonEmptyArray(policyEntries)) {
				isPolicySuccess = false;
				errors.add("No Policy contribution found for the policy id:" + masterEntity.getPolicyId()
						+ ",financial year:" + financialYear);
			} else {
				memberMap.put("Policy contribution count", policyEntries.size());
			}
		} else {
			isPolicySuccess = false;
			errors.add("No Policy contribution found for the for the policy id:" + masterEntity.getPolicyId()
					+ ",financial year:" + financialYear);
		}

		processDCPolicy(responseDto, masterEntity, memberMap, errors, isPolicySuccess, variantType, zeroEntriesMap);

		long end = System.currentTimeMillis();
		double totalTime = (end - start) / 1000.0;
		String time = NumericUtils.convertDoubleToString(totalTime) + " sec";
		logger.info("Time taken to copy the contribution to fund entries:{}", time);
		responseDto.setCode(time);
		responseDto.setErrors(errors);
		responseDto.setStatusId(1);
		logger.info("savePolicyAndMemberContributionEntries::{},{}", CommonConstants.LOGEND,
				masterEntity.getPolicyId());
		return responseDto;
	}

	@Transactional
	public void processDCPolicy(CommonResponsePolicyCalcDto<Map<String, Object>> responseDto,
			PolicyMasterEntity masterEntity, Map<String, Object> memberMap, List<String> errors,
			boolean isPolicySuccess, String variantType, Map<Long, List<ZeroAccountEntriesEntity>> zeroEntriesMap)
			throws ApplicationException {
		if (StringUtils.isNotEmpty(masterEntity.getPolicyType()) && DC.equalsIgnoreCase(masterEntity.getPolicyType())) {
			boolean isMemberSuccess = true;
			/***
			 * @notes Set empty set for Member Transaction entries
			 */
			List<ZeroAccountEntriesEntity> memberContributionEntities = new ArrayList<>();
			/***
			 * @notes Copy Non entry member contribution to Transaction entries
			 */
			List<MemberTransactionEntriesEntity> memberContributionEntries = getMemberTransactionEntries(masterEntity,
					memberContributionEntities, zeroEntriesMap, variantType);

			printMemberContribution(memberContributionEntries);

			if (CommonUtils.isNonEmptyArray(memberContributionEntries)) {
				memberTransactionEntriesRepository.saveAll(memberContributionEntries);
			}

			memberTransactionSummary(memberContributionEntries, masterEntity, variantType);

			if (!CommonUtils.isNonEmptyArray(memberContributionEntries)) {
				errors.add("No Member contribution found for the policy id:" + masterEntity.getPolicyId()
						+ ", financial year: " + DateUtils.getFinancialYrByDt(DateUtils.sysDate()));
				isMemberSuccess = false;
			} else {
				memberMap.put("Member contribution count", memberContributionEntries.size());
				responseDto
						.setMessage("Policy and Member contributions are push to fund entries for the policy number/ID:"
								+ masterEntity.getPolicyNumber() + "/" + masterEntity.getPolicyId()
								+ ", financial year:" + DateUtils.getFinancialYrByDt(DateUtils.sysDate()));
			}
			if (!isPolicySuccess && !isMemberSuccess) {
				responseDto.setStatus(CommonConstants.ERROR);
			} else {
				responseDto.setStatus(CommonConstants.SUCCESS);
			}
			responseDto.setResponseData(memberMap);
		}
	}

	private List<MemberTransactionEntriesEntity> getMemberTransactionEntries(PolicyMasterEntity masterEntity,
			List<ZeroAccountEntriesEntity> zeroAccountEntriesEntities,
			Map<Long, List<ZeroAccountEntriesEntity>> zeroEntriesMap, String variantType) {
		logger.info("getMemberTransactionEntries::{},{}", CommonConstants.LOGSTART, masterEntity.getPolicyId());
		Set<MemberMasterEntity> memberMaster = masterEntity.getMemberMaster();
		if (CommonUtils.isNonEmptyArray(memberMaster)) {

			Map<Long, List<ZeroAccountEntriesEntity>> map = zeroEntriesMap;

			List<MemberTransactionEntriesEntity> memberTransactionEntriesEntities = new ArrayList<>();
			for (Entry<Long, List<ZeroAccountEntriesEntity>> entrySet : map.entrySet()) {
				List<ZeroAccountEntriesEntity> memberContributionEntitiesUnsorted = entrySet.getValue();
				if (CommonUtils.isNonEmptyArray(memberContributionEntitiesUnsorted)) {
					List<ZeroAccountEntriesEntity> memberContributionEntities = memberContributionEntitiesUnsorted
							.stream().sorted(createMemberConttributionComparator()).collect(Collectors.toList());
					/** memberContributionEntities.forEach(from -> { */
					setMemberTransactionEntries(masterEntity, memberContributionEntities, zeroAccountEntriesEntities,
							memberTransactionEntriesEntities, variantType);
					/** }); */
				}
			}
			logger.info("getMemberTransactionEntries::{},{}", CommonConstants.LOGEND, masterEntity.getPolicyId());
			return memberTransactionEntriesEntities;
			/** }); */
		}
		return Collections.emptyList();
	}

	private void setMemberTransactionEntries(PolicyMasterEntity masterEntity,
			List<ZeroAccountEntriesEntity> memberContributionEntities,
			List<ZeroAccountEntriesEntity> zeroAccountEntriesEntities,
			List<MemberTransactionEntriesEntity> memberTransactionEntriesEntities, String variantType) {
		int i = 0;
		List<MemberTransactionEntriesEntity> memberWiseSet = new ArrayList<>();
		for (ZeroAccountEntriesEntity from : memberContributionEntities) {
			try {
				MemberTransactionEntriesEntity to = new MemberTransactionEntriesEntity();
				logger.info("MemberTransactionEntries::{},{}", CommonConstants.LOGSTART, masterEntity.getPolicyId());

				MemberMasterEntity member = memberMasterRepository
						.findByPolicyIdAndLicIdAndMemberStatusAndIsActiveTrue(from.getPolicyId(), "0", "Active");
				if (member != null) {
					BeanUtils.copyProperties(from, to);
					to.setClosingBalance(BigDecimal.ZERO);
					to.setCreatedBy(from.getCreatedBy());
					to.setCreatedOn(from.getTransactionDate());
					to.setTransactionDate(getCollectionDate(from.getPolicyConId(), from.getTransactionDate()));
					to.setEntryType(StringUtils.isNotBlank(from.getTransactionType()) ? from.getTransactionType()
							: CONTRIBUTION_ADJUSTMENT);
					to.setFinancialYear(getFinancialYrByDt(DateUtils.sysDate()));
					to.setQuarter(getQuarterByDate(DateUtils.sysDate()));
					to.setIsActive(true);
					to.setModifiedBy(from.getModifiedBy());
					to.setModifiedOn(DateUtils.sysDate());
					to.setPolicyId(masterEntity.getPolicyId());
					to.setMemTranId(null);
					to.setTransationType(CREDIT);
					to.setMember(member);
					to.setLicId(member.getLicId());
					to.setIsOpeningBal(false);
					to.setEmployeeContribution(BigDecimal.ZERO);
					to.setEmployerContribution(BigDecimal.ZERO);
					to.setVoluntaryContribution(BigDecimal.ZERO);
					to.setTotalContribution(
							NumericUtils.bigDecimalValid(to.getTotalContribution()).compareTo(BigDecimal.ZERO) > 0
									? to.getTotalContribution()
									: from.getZeroIdAmount());
					to.setPolicyContributionId(from.getPolicyConId());
					to.setMemberContributionId(from.getMemberConId());
					to.setIsOpeningBal(false);

					MemberTransactionEntriesEntity memberTxnPrevRecord = getMemberTxnPrevRecord(memberWiseSet, to, i,
							variantType);
					to.setTxnRefNo(CommonUtils.txnRefNo(CR, masterEntity.getPolicyId(), from.getMemberConId()));
					to.setOpeningBalance(
							setBigDecimalValue(memberTxnPrevRecord.getClosingBalance(), from.getZeroIdAmount()));
					to.setClosingBalance(
							NumericUtils.add(memberTxnPrevRecord.getClosingBalance(), to.getTotalContribution()));
					logger.info("*********************Closing/Opening/Total*************************");
					logger.info("(Closing = {} , Opening = {} , Total Contribution = {} )",
							NumericUtils.add(memberTxnPrevRecord.getClosingBalance(), to.getTotalContribution()),
							memberTxnPrevRecord.getClosingBalance(), to.getTotalContribution());
					logger.info("**********************************************");
					if (NumericUtils.bigDecimalValid(to.getTotalContribution()).compareTo(BigDecimal.ZERO) > 0) {
						memberWiseSet.add(to);
						i++;
					}
					zeroAccountEntriesEntities.add(from);
					logger.info("MemberTransactionEntries::{},{}", CommonConstants.LOGEND, masterEntity.getPolicyId());
				}
			} catch (Exception e) {
				logger.info("Exception", e);
			}
			memberTransactionEntriesEntities.addAll(memberWiseSet);
		}
	}

	public MemberTransactionEntriesEntity getMemberTxnPrevRecord(List<MemberTransactionEntriesEntity> list,
			MemberTransactionEntriesEntity memberEntry, int i, String variantType) throws ApplicationException {
		if (CommonUtils.isNonEmptyArray(list)) {
			if (list.size() > 1 && i > 1) {
				return list.get(i - 1);
			} else {
				return list.get(0);
			}
		}
		MemberTransactionEntriesEntity lastEntry = getTopMemberTransactionEntry(memberEntry.getPolicyId(),
				memberEntry.getLicId(), DateUtils.dateToStringFormatDDMMYYYYSlash(memberEntry.getTransactionDate()),
				variantType);
		if (lastEntry != null) {
			return lastEntry;
		}
		lastEntry = new MemberTransactionEntriesEntity();
		lastEntry.setOpeningBalance(BigDecimal.ZERO);
		lastEntry.setClosingBalance(BigDecimal.ZERO);
		lastEntry.setTotalContribution(BigDecimal.ZERO);
		return lastEntry;
	}

	public MemberTransactionEntriesEntity getTopMemberTransactionEntry(Long policyId, String licId,
			String transactionDate, String variantType) throws ApplicationException {
		MemberTransactionEntriesEntity entity = null;
		logger.info("getTopMemberTransactionEntry ::{},{},{},{}::{}::", policyId, licId,
				getFinancialYrByDateString(transactionDate), variantType, getQuarterByStrDate(transactionDate));
		if (StringUtils.isNotBlank(variantType) && CommonUtils.anyMatch(variantType, V1, V2, V3)) {
			if (V2.equalsIgnoreCase(variantType)) {
				entity = memberTransactionEntriesRepository
						.findTopByPolicyIdAndLicIdAndQuarterAndFinancialYearAndIsActiveTrueOrderByTransactionDateDescCreatedOnDesc(
								policyId, licId, getQuarterByStrDate(transactionDate),
								getFinancialYrByDateString(transactionDate));
			} else {
				entity = memberTransactionEntriesRepository
						.findTopByPolicyIdAndLicIdAndFinancialYearAndIsActiveTrueOrderByTransactionDateDescCreatedOnDesc(
								policyId, licId, getFinancialYrByDateString(transactionDate));
			}
			return entity;
		} else {
			throw new ApplicationException("MemberTransactionEntry : The given variant is not valid:" + variantType
					+ " for policy id :" + policyId);
		}

	}

	public void printMemberContribution(List<MemberTransactionEntriesEntity> memberTransactionEntries) {
		logger.info("########################################");
		if (CommonUtils.isNonEmptyArray(memberTransactionEntries)) {
			logger.info("******memberContributionEntries Size::{}******", memberTransactionEntries.size());
			memberTransactionEntries.forEach(
					p -> logger.info("*****{}::{}::{}******", p.getTxnRefNo(), p.getPolicyId(), p.getMemTranId()));
		} else {
			logger.info("################## memberTransactionEntries No Size ######################");
		}
	}

	@Transactional
	public void memberTransactionSummary(List<MemberTransactionEntriesEntity> memberTxnEntries,
			PolicyMasterEntity masterEntity, String variantType) throws ApplicationException {
		logger.info("saveMemberTransactionSummary::{},{}", CommonConstants.LOGSTART, masterEntity.getPolicyId());

		Map<String, List<MemberTransactionEntriesEntity>> map = getMemberTransactionByMember(memberTxnEntries);

		for (Entry<String, List<MemberTransactionEntriesEntity>> entrySet : map.entrySet()) {
			if (CommonUtils.isNonEmptyArray(memberTxnEntries)) {
				logger.info("MemberTransactionSummary::{},{},{}", CommonConstants.LOGSTART, masterEntity.getPolicyId(),
						entrySet.getKey());
				try {
					List<MemberTransactionEntriesEntity> memberEntries1 = entrySet.getValue();
					if (CommonUtils.isNonEmptyArray(memberEntries1)) {

						memberTransactionSummaryUpdate(memberEntries1, masterEntity, variantType);

					}
				} catch (ApplicationException e) {
					logger.error("memberTransactionSummary Error:", e);
				}
				logger.info("MemberTransactionSummary::{},{},{}", CommonConstants.LOGEND, masterEntity.getPolicyId(),
						entrySet.getKey());
			}
		}
		logger.info("saveMemberTransactionSummary::{},{}", CommonConstants.LOGEND, masterEntity.getPolicyId());
	}

	private void memberTransactionSummaryUpdate(List<MemberTransactionEntriesEntity> memberEntries1,
			PolicyMasterEntity masterEntity, String variantType) throws ApplicationException {
		MemberTransactionSummaryEntity memberSummary;
		try {
			List<MemberTransactionEntriesEntity> memberEntries = new ArrayList<>(memberEntries1);
			MemberTransactionEntriesEntity memEntity = memberEntries.get(memberEntries.size() - 1);
			memberSummary = getTopMemberTransactionSummary(masterEntity.getPolicyId(), memEntity.getLicId(),
					DateUtils.dateToStringFormatDDMMYYYYSlash(memEntity.getTransactionDate()), variantType);
			if (memberSummary == null) {
				memberSummary = new MemberTransactionSummaryEntity();
				memberSummary.setOpeningBalance(BigDecimal.ZERO);
			} else {
				memberSummary.setOpeningBalance(NumericUtils.bigDecimalValid(memberSummary.getClosingBalance()));
			}
			memberSummary.setQuarter(getQuarterByDate(DateUtils.sysDate()));
			memberSummary.setFinancialYear(getFinancialYrByDt(DateUtils.sysDate()));
			memberSummary.setCreatedBy(memEntity.getCreatedBy());
			memberSummary.setCreatedOn(DateUtils.sysDate());
			MembersContributionDetailsDto dto = new MembersContributionDetailsDto();
			dto.setVariant(null);
			dto.setMemberId(memEntity.getLicId());
			dto.setPolicyId(memEntity.getPolicyId());
			dto.setTxnType(CREDIT);
			dto.setTransactionDate(DateUtils.dateToStringFormatDDMMYYYYSlash(memEntity.getTransactionDate()));
			sumOfMemberContribution(dto, variantType);
			memberSummary.setEmployeeContribution(NumericUtils.bigDecimalValid(dto.getEmployeeContributionAmount()));
			memberSummary.setEmployerContribution(NumericUtils.bigDecimalValid(dto.getEmployerContributionAmount()));
			memberSummary.setVoluntaryContribution(NumericUtils.bigDecimalValid(dto.getVoluntaryContributionAmount()));
			memberSummary.setTxnAmount(NumericUtils.add(memberSummary.getEmployeeContribution(),
					memberSummary.getEmployerContribution(), memberSummary.getVoluntaryContribution()));
			memberSummary.setTxnAmount(
					NumericUtils.bigDecimalValid(memberSummary.getTxnAmount()).compareTo(BigDecimal.ZERO) > 0
							? memberSummary.getTxnAmount()
							: dto.getTotalContributionAmount());
			memberSummary.setClosingBalance(dto.getTotalContributionAmount());
			memberSummary.setPolicyMbrId(memEntity.getMember().getMemberId());
			memberSummary.setMemberId(memEntity.getLicId());
			memberSummary.setPolicyId(memEntity.getPolicyId());
			memberSummary.setIsActive(true);
			memberSummary.setModifiedBy(memEntity.getCreatedBy());
			memberSummary.setModifiedOn(DateUtils.sysDate());
			memberSummary.setPolicyId(masterEntity.getPolicyId());
			memberTransactionSummaryRepository.save(memberSummary);
		} catch (ApplicationException e) {
			logger.error("memberTransactionSummary Error:", e);
		}

	}

	public void sumOfMemberContribution(MembersContributionDetailsDto dto, String variantType)
			throws ApplicationException {

		Integer quarter = getQuarterByStrDate(dto.getTransactionDate());
		String financialYr = getFinancialYrByDateString(dto.getTransactionDate());

		List<Object> contributionOb = null;
		if (V2.equalsIgnoreCase(variantType)) {
			contributionOb = CommonUtils.objectArrayToList(memberTransactionEntriesRepository
					.sumOfMemberTransactionEntries(dto.getMemberId(), dto.getPolicyId(), quarter, financialYr, true));
		} else {
			contributionOb = CommonUtils.objectArrayToList(memberTransactionEntriesRepository
					.sumOfMemberTransactionEntries(dto.getMemberId(), dto.getPolicyId(), financialYr, true));
		}

		if (StringUtils.isNotBlank(dto.getTxnType())) {
			if (StringUtils.isNotBlank(dto.getTxnType()) && DEBIT.equalsIgnoreCase(dto.getTxnType())) {
				dto.setTotalContributionAmount(NumericUtils
						.bigDecimalNegative(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(3)))));
				dto.setEmployeeContributionAmount(NumericUtils
						.bigDecimalNegative(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(0)))));
				dto.setEmployerContributionAmount(NumericUtils
						.bigDecimalNegative(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(1)))));
				dto.setVoluntaryContributionAmount(NumericUtils
						.bigDecimalNegative(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(2)))));
				dto.setTotalContributionAmount(NumericUtils
						.bigDecimalNegative(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(3)))));

			} else {
				dto.setTotalContributionAmount(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(3))));
				dto.setEmployeeContributionAmount(
						NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(0))));
				dto.setEmployerContributionAmount(
						NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(1))));
				dto.setVoluntaryContributionAmount(
						NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(2))));
				dto.setTotalContributionAmount(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(3))));
			}
		} else {
			throw new ApplicationException("No transaction type  found for the given LIC ID:" + dto.getMemberId());
		}

	}

	public MemberTransactionSummaryEntity getTopMemberTransactionSummary(Long policyId, String licId,
			String transactionDate, String variant) throws ApplicationException {
		MemberTransactionSummaryEntity entity = null;
		logger.info("getTopMemberTransactionSummary::{},{},{},{}::{}::", policyId, licId,
				getFinancialYrByDateString(transactionDate), variant, getQuarterByStrDate(transactionDate));

		if (StringUtils.isNotBlank(variant) && CommonUtils.anyMatch(variant, V1, V2, V3)) {
			if (V2.equalsIgnoreCase(variant)) {
				entity = memberTransactionSummaryRepository
						.findTopByPolicyIdAndMemberIdAndQuarterAndFinancialYearAndIsActiveTrueOrderByFinancialYearDesc(
								policyId, licId, getQuarterByStrDate(transactionDate),
								getFinancialYrByDateString(transactionDate));
			} else {
				entity = memberTransactionSummaryRepository
						.findTopByPolicyIdAndMemberIdAndFinancialYearAndIsActiveTrueOrderByFinancialYearDesc(policyId,
								licId, getFinancialYrByDateString(transactionDate));
			}

			return entity;
		} else {
			throw new ApplicationException("MemberTransactionSummary : The given variant is not valid :" + variant
					+ " for Policy id:" + policyId);
		}

	}

	public Map<String, List<MemberTransactionEntriesEntity>> getMemberTransactionByMember(
			List<MemberTransactionEntriesEntity> list) {
		return list.parallelStream().collect(Collectors.groupingBy(this::getPolicyAndLicId, Collectors.toList()));
	}

	public static Comparator<ZeroAccountEntriesEntity> createMemberConttributionComparator() {
		return Comparator.comparing(ZeroAccountEntriesEntity::getTransactionDate)
				.thenComparing(ZeroAccountEntriesEntity::getCreatedOn);
	}

	public static Comparator<ZeroAccountEntriesEntity> createPolicyConttributionComparator() {
		return Comparator.comparing(ZeroAccountEntriesEntity::getTransactionDate)
				.thenComparing(ZeroAccountEntriesEntity::getCreatedOn);
	}

	private List<ZeroAccountEntriesEntity> fetchZeroAccEntiresByVariant(PolicyMasterEntity masterEntity,
			String variantType) {
		if (StringUtils.isNotBlank(variantType) && CommonUtils.anyMatch(variantType, V1, V2, V3)) {
			List<ZeroAccountEntriesEntity> zeroAccEntriesEntities = null;
			zeroAccEntriesEntities = zeroAccountEntriesRepository
					.getZeroAccEntriesByPolicyIdAndTxnStatus(masterEntity.getPolicyId(), false, true);
			return zeroAccEntriesEntities;
		}
		return Collections.emptyList();
	}

	public Integer getQuarterByDate(Date dateVal) {
		LocalDate date = DateUtils.dateToLocalDate(dateVal);
		if (date != null) {
			LocalDate syntheticQuarterDate = date.minus(1, IsoFields.QUARTER_YEARS);
			return syntheticQuarterDate.get(IsoFields.QUARTER_OF_YEAR);
		}
		return 0;
	}

	public Map<Long, List<ZeroAccountEntriesEntity>> getZeroAccountEntriesByPolicyId(
			List<ZeroAccountEntriesEntity> contributions) {
		return contributions.stream()
				.collect(Collectors.groupingBy(ZeroAccountEntriesEntity::getPolicyId, Collectors.toList()));
	}

	@Transactional
	public List<PolicyTransactionEntriesEntity> mapZeroAccountEntriesAndTxnEntries(
			Map<Long, List<ZeroAccountEntriesEntity>> policyContrMap, PolicyMasterEntity masterEntity,
			String variantType) {
		logger.info("mapPolicyContributionAndEntries::{},{}", CommonConstants.LOGSTART, masterEntity.getPolicyId());
		List<PolicyTransactionEntriesEntity> tos = new ArrayList<>();

		for (Entry<Long, List<ZeroAccountEntriesEntity>> entrySet : policyContrMap.entrySet()) {

			List<ZeroAccountEntriesEntity> zeroAccountEntriesEntitiesUnsorted = entrySet.getValue();
			if (CommonUtils.isNonEmptyArray(zeroAccountEntriesEntitiesUnsorted)) {
				List<ZeroAccountEntriesEntity> zeroAccountEntriesEntitiesEntities = zeroAccountEntriesEntitiesUnsorted
						.stream().sorted(createPolicyConttributionComparator()).collect(Collectors.toList());
				List<PolicyTransactionEntriesEntity> policyWiseSet = new ArrayList<>();
				zeroAccountEntriesEntitiesEntities.stream()
						.forEach(from -> logger.info("************::{}::{}::{}::{}::************",
								from.getZeroAccEntId(), from.getTransactionDate(), from.getCreatedOn(),
								from.getZeroIdAmount()));

				setPolicyTransactionEntries(masterEntity, zeroAccountEntriesEntitiesEntities, policyWiseSet,
						variantType);
				tos.addAll(policyWiseSet);
			}
		}
		logger.info("mapPolicyContributionAndEntries::{},{}", CommonConstants.LOGEND, masterEntity.getPolicyId());
		return tos;
	}

	public void setPolicyTransactionEntries(PolicyMasterEntity masterEntity,
			List<ZeroAccountEntriesEntity> policyContributionTempEntities,
			List<PolicyTransactionEntriesEntity> policyWiseSet, String variantType) {
		int i = 0;
		for (ZeroAccountEntriesEntity from : policyContributionTempEntities) {
			/** policyContributionTempEntities.forEach(from -> { */
			try {
				if (!NumericUtils.isBooleanNotNull(from.getTxnEntryStatus())) {
					logger.info("************{}::{}::{}::************", from.getTransactionDate(), from.getCreatedOn(),
							from.getZeroIdAmount());
					PolicyTransactionEntriesEntity to = new PolicyTransactionEntriesEntity();
					BeanUtils.copyProperties(from, to);
					to.setClosingBalance(BigDecimal.ZERO);
					to.setCreatedBy(from.getCreatedBy());
					to.setCreatedOn(from.getTransactionDate());
					to.setEntryType(StringUtils.isNotBlank(from.getTransactionType()) ? from.getTransactionType()
							: CONTRIBUTION_ADJUSTMENT);
					to.setTransactionDate(getCollectionDate(from.getPolicyConId(), from.getTransactionDate()));
					to.setFinancialYear(DateUtils.getFinancialYrByDt(DateUtils.sysDate()));
					to.setQuarter(getQuarterByDate(DateUtils.sysDate()));
					to.setIsActive(true);
					to.setModifiedBy(from.getModifiedBy());
					to.setModifiedOn(DateUtils.sysDate());
					to.setOpeningBalance(BigDecimal.ZERO);
					to.setPolicyId(masterEntity.getPolicyId());
					to.setPolTranId(null);
					to.setTransationType(CREDIT);
					to.setIsOpeningBal(false);
					to.setEmployeeContribution(BigDecimal.ZERO);
					to.setEmployerContribution(BigDecimal.ZERO);
					to.setVoluntaryContribution(BigDecimal.ZERO);
					to.setTotalContribution(from.getZeroIdAmount());
					to.setTotalContribution(
							NumericUtils.bigDecimalValid(to.getTotalContribution()).compareTo(BigDecimal.ZERO) == 0
									? from.getZeroIdAmount()
									: to.getTotalContribution());
					to.setIsOpeningBal(false);
					to.setTxnRefNo(CommonUtils.txnRefNo(CR, masterEntity.getPolicyId(), from.getZeroAccEntId()));
					PolicyTransactionEntriesEntity txnPrevRecord = getPolicyTxnPrevRecord(policyWiseSet, to, i,
							variantType);
					to.setOpeningBalance(setBigDecimalValue(txnPrevRecord.getClosingBalance(), from.getZeroIdAmount()));
					to.setClosingBalance(NumericUtils.bigDecimalValid(txnPrevRecord.getClosingBalance())
							.add(NumericUtils.bigDecimalValid(to.getTotalContribution())));
					to.setPolicyContributionId(from.getPolicyConId());

					logger.info("*********************Closing/Opening/Total*************************");
					logger.info("(Closing = {} , Opening = {} , Total Contribution = {} )",
							NumericUtils.add(txnPrevRecord.getClosingBalance(), to.getTotalContribution()),
							txnPrevRecord.getClosingBalance(), to.getTotalContribution());
					logger.info("**********************************************");
					if (NumericUtils.bigDecimalValid(to.getTotalContribution()).compareTo(BigDecimal.ZERO) > 0) {
						policyWiseSet.add(to);
						i++;
					}
				}
			} catch (Exception e) {
				logger.error("Error", e);
			}
			/** }); */
		}
	}

	public Date getCollectionDate(Long contributionId, Date contributionDate) {
		Object obj = policyContributionRepository.findDepositByContributionId(contributionId);
		if (obj != null) {
			Object[] ob = (Object[]) obj;
			Date collectionDate = DateUtils.stringToDateYYYYMMDDHHMMSSHyphen(CommonUtils.objectValue(ob, 3));
			return collectionDate != null ? collectionDate : contributionDate;
		}
		return contributionDate;
	}

	public PolicyTransactionEntriesEntity getPolicyTxnPrevRecord(List<PolicyTransactionEntriesEntity> list,
			PolicyTransactionEntriesEntity newEntry, int i, String variantType) throws ApplicationException {
		if (CommonUtils.isNonEmptyArray(list)) {
			if (list.size() > 1 && i > 1) {
				return list.get(i - 1);
			} else {
				return list.get(0);
			}
		}
		PolicyTransactionEntriesEntity lastEntry = getTopPolicyTransactionEntry(newEntry.getPolicyId(),
				DateUtils.dateToStringFormatDDMMYYYYSlash(newEntry.getTransactionDate()), variantType);
		if (lastEntry != null) {
			return lastEntry;
		}
		lastEntry = new PolicyTransactionEntriesEntity();
		lastEntry.setOpeningBalance(BigDecimal.ZERO);
		lastEntry.setClosingBalance(BigDecimal.ZERO);
		lastEntry.setTotalContribution(BigDecimal.ZERO);
		return lastEntry;
	}

	public PolicyTransactionEntriesEntity getTopPolicyTransactionEntry(Long policyId, String transactionDate,
			String variantType) throws ApplicationException {
		PolicyTransactionEntriesEntity entity = null;
		logger.info("getTopPolicyTransactionEntry::{},{},{}::{}::{}", policyId, getQuarterByStrDate(transactionDate),
				getFinancialYrByDateString(transactionDate), variantType, transactionDate);

		logger.info("getTopPolicyTransactionEntry::{},{},{}::{}::{}", policyId, getQuarterByStrDate(transactionDate),
				getFinancialYrByDateString(transactionDate), variantType, transactionDate);
		if (StringUtils.isNotBlank(variantType) && CommonUtils.anyMatch(variantType, V1, V2, V3)) {
			if (V2.equalsIgnoreCase(variantType)) {
				entity = policyTransactionEntriesRepository
						.findTopByPolicyIdAndQuarterAndFinancialYearAndIsActiveTrueOrderByTransactionDateDescCreatedOnDesc(
								policyId, getQuarterByStrDate(transactionDate),
								getFinancialYrByDateString(transactionDate));
			} else {
				entity = policyTransactionEntriesRepository
						.findTopByPolicyIdAndAndFinancialYearAndIsActiveTrueOrderByTransactionDateDescCreatedOnDesc(
								policyId, getFinancialYrByDateString(transactionDate));
			}
			return entity;
		} else {
			throw new ApplicationException("PolicyTransactionEntry::The given variant is not valid:" + variantType
					+ " for policy id:" + policyId);
		}

	}

	public String getFinancialYrByDateString(String date) {
		if (StringUtils.isNotBlank(date)) {
			LocalDate txnDate = DateUtils.dateToLocalDate(DateUtils.convertStringToDateDDMMYYYYSlash(date));
			if (txnDate != null) {
				int startYear = getStartOfFinancialYear(txnDate).getYear();
				int endYear = getEndOfFinancialYear(txnDate).getYear();
				return startYear + "-" + endYear;
			}
		}
		return DateUtils.getFinancialYrByDt(DateUtils.sysDate());
	}

	private static final MonthDay FINANCIAL_START = MonthDay.of(4, 1);
	private static final MonthDay FINANCIAL_END = MonthDay.of(3, 31);

	public LocalDate getStartOfFinancialYear(LocalDate date) {
		/*** Try "the same year as the date we've been given" */
		LocalDate candidate = date.with(FINANCIAL_START);
		/***
		 * If we haven't reached that yet, subtract a year. Otherwise, use it.
		 */
		return candidate.isAfter(date) ? candidate.minusYears(1) : candidate;
	}

	public LocalDate getEndOfFinancialYear(LocalDate date) {
		/*** Try "the same year as the date we've been given" */
		LocalDate candidate = date.with(FINANCIAL_END);
		/***
		 * If we haven't reached that yet, subtract a year. Otherwise, use it.
		 */
		return candidate.isBefore(date) ? candidate.plusYears(1) : candidate;
	}

	public Integer getQuarterByStrDate(String strDate) {
		LocalDate date = DateUtils.dateToLocalDate(DateUtils.convertStringToDateDDMMYYYYSlash(strDate));
		if (date != null) {
			LocalDate syntheticQuarterDate = date.minus(1, IsoFields.QUARTER_YEARS);
			/*** Get quarter number and year as int */
			return syntheticQuarterDate.get(IsoFields.QUARTER_OF_YEAR);
		}
		return 0;
	}

	public BigDecimal setBigDecimalValue(BigDecimal from, BigDecimal to) {
		if (from != null && from.compareTo(BigDecimal.ZERO) > 0) {
			return from;
		}
		return NumericUtils.bigDecimalValid(to);
	}

	public void printContributions(List<ZeroAccountEntriesEntity> policyContributions,
			List<PolicyTransactionEntriesEntity> policyEntries) {
		logger.info("#####################printContributions###################");
		if (CommonUtils.isNonEmptyArray(policyContributions)) {
			logger.info("******policyContributions Size::{}******", policyContributions.size());
			policyContributions
					.forEach(p -> logger.info("******{}::{}******", p.getPolicyConId(), p.getTxnEntryStatus()));
		} else {
			logger.info("################## policyContributions No Size ######################");
		}
		if (CommonUtils.isNonEmptyArray(policyEntries)) {
			logger.info("******policyEntries Size::{}******", policyEntries.size());
			policyEntries.forEach(
					p -> logger.info("*****{}::{}::{}******", p.getTxnRefNo(), p.getPolicyId(), p.getPolTranId()));
		} else {
			logger.info("################## policyEntries No Size ######################");
		}
		logger.info("########################################");
	}

	@Transactional
	public void policyTransactionSummary(List<PolicyTransactionEntriesEntity> policyEntries,
			PolicyMasterEntity masterEntity, String variantType) {
		logger.info("savePolicyTransactionSummary::{},{}", CommonConstants.LOGSTART, masterEntity.getPolicyId());
		Map<Long, List<PolicyTransactionEntriesEntity>> policyMap = policyEntries.stream()
				.collect(Collectors.groupingBy(this::policyId, Collectors.toList()));
		/**
		 * Map<Long, PolicyTransactionEntriesEntity> collect2 = policyEntries.stream()
		 * .collect(Collectors.toMap(this::policyId, Function.identity()));
		 */
		for (Entry<Long, List<PolicyTransactionEntriesEntity>> entry : policyMap.entrySet()) {
			List<PolicyTransactionEntriesEntity> policies = entry.getValue();
			if (CommonUtils.isNonEmptyArray(policies)) {
				try {
					PolicyTransactionEntriesEntity entryEntity = policies.get(policies.size() - 1);
					/**
					 * policies.forEach(entryEntity -> { PolicyTransactionSummaryEntity entity =
					 * policyTransactionSummaryRepository
					 * .findTopByPolicyIdAndFinancialYearAndIsActiveTrueOrderByIdDesc(entry.getKey(),
					 * getFinancialYrByDt(DateUtils.sysDate()));
					 */
					PolicyTransactionSummaryEntity entity = getTopPolicyTransactionSummary(entry.getKey(),
							DateUtils.dateToStringFormatDDMMYYYYSlash(entryEntity.getTransactionDate()), variantType);
					if (entity == null) {
						entity = new PolicyTransactionSummaryEntity();
						entity.setOpeningBalanceAmount(BigDecimal.ZERO);
					} else {
						entity.setOpeningBalanceAmount(NumericUtils.bigDecimalValid(entity.getClosingBalance()));
					}
					entity.setCreatedBy(masterEntity.getCreatedBy());
					entity.setCreatedOn(DateUtils.sysDate());
					entity.setFinancialYear(getFinancialYrByDt(DateUtils.sysDate()));
					entity.setIsActive(true);
					entity.setModifiedBy(masterEntity.getCreatedBy());
					entity.setModifiedOn(DateUtils.sysDate());
					entity.setPolicyId(masterEntity.getPolicyId());

					MembersContributionDetailsDto dto = new MembersContributionDetailsDto();
					dto.setPolicyId(masterEntity.getPolicyId());
					dto.setTxnType(CREDIT);
					dto.setTransactionDate(DateUtils.dateToStringFormatDDMMYYYYSlash(entryEntity.getTransactionDate()));
					dto.setVariant(variantType);
					dto.setPolicyType(null);

					sumOfPolicyTransactionEntries(dto, variantType);

					entity.setOpeningBalanceAmount(NumericUtils.bigDecimalValid(entity.getClosingBalance()));
					entity.setClosingBalance(NumericUtils.bigDecimalValid(dto.getTotalContributionAmount()));
					entity.setTotalContribution(NumericUtils.bigDecimalValid(dto.getTotalContributionAmount()));
					entity.setEmployeeContribution(dto.getEmployeeContributionAmount());
					entity.setEmployerContribution(dto.getEmployerContributionAmount());
					entity.setVoluntaryContribution(dto.getVoluntaryContributionAmount());

					/**
					 * entity.setPolicyNumber(masterEntity.getPolicyNumber());
					 * entity.setStream(InterestFundConstants.SUPERANNUATION);
					 * entity.setPolicyType(NumericUtils.convertLongToString(masterEntity.
					 * getProductId())); entity.setVariant(masterEntity.getVariant());
					 */
					entity.setQuarter(getQuarterByDate(DateUtils.sysDate()));
					policyTransactionSummaryRepository.save(entity);
					/** }); */
				} catch (ApplicationException e) {
					logger.error("Error in savePolicyTransactionSummary:{}", masterEntity.getPolicyId(), e);
				}
			}
		}
		logger.info("savePolicyTransactionSummary::{},{}", CommonConstants.LOGEND, masterEntity.getPolicyId());

	}

	public void sumOfPolicyTransactionEntries(MembersContributionDetailsDto dto, String variantType)
			throws ApplicationException {
		String variant = null;
		if (StringUtils.isBlank(dto.getVariant()) || !CommonUtils.anyMatch(dto.getVariant(), V1, V2, V3)) {
			variant = variantType;
			dto.setVariant(variant);
		}
		if (StringUtils.isNotBlank(dto.getVariant()) && CommonUtils.anyMatch(dto.getVariant(), V1, V2, V3)) {
			variant = dto.getVariant();
		} else {
			throw new ApplicationException(
					"The given variant is not valid:" + dto.getVariant() + " for policy id:" + dto.getVariant());
		}

		Integer quarter = getQuarterByStrDate(dto.getTransactionDate());
		String financialYr = getFinancialYrByDateString(dto.getTransactionDate());

		List<Object> contributionOb = null;

		if (V2.equalsIgnoreCase(variant)) {
			contributionOb = CommonUtils.objectArrayToList(policyTransactionEntriesRepository
					.sumOfPolicyTransactionEntries(dto.getPolicyId(), quarter, financialYr, true));
		} else {
			contributionOb = CommonUtils.objectArrayToList(policyTransactionEntriesRepository
					.sumOfPolicyTransactionEntries(dto.getPolicyId(), financialYr, true));
		}

		if (StringUtils.isNotBlank(dto.getTxnType())) {
			dto.setTotalContributionAmount(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(3))));
			dto.setEmployeeContributionAmount(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(0))));
			dto.setEmployerContributionAmount(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(1))));
			dto.setVoluntaryContributionAmount(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(2))));
			dto.setTotalContributionAmount(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(3))));
		} else {
			throw new ApplicationException("No transaction type  found for the given POLICY ID:" + dto.getPolicyId());
		}

	}

	public String getFinancialYrByDt(Date date) {
		LocalDate txnDate = DateUtils.dateToLocalDate(date);
		if (txnDate != null) {
			int startYear = getStartOfFinancialYear(txnDate).getYear();
			int endYear = getEndOfFinancialYear(txnDate).getYear();
			return startYear + "-" + endYear;
		}
		return null;
	}

	public PolicyTransactionSummaryEntity getTopPolicyTransactionSummary(Long policyId, String transactionDate,
			String variantType) throws ApplicationException {
		PolicyTransactionSummaryEntity entity = null;
		logger.info("getTopPolicyTransactionSummary::{},{},{}::{}::{}", policyId, getQuarterByStrDate(transactionDate),
				getFinancialYrByDateString(transactionDate), variantType, transactionDate);
		if (StringUtils.isNotBlank(variantType) && CommonUtils.anyMatch(variantType, V1, V2, V3)) {
			if (V2.equalsIgnoreCase(variantType)) {
				entity = policyTransactionSummaryRepository
						.findTopByPolicyIdAndQuarterAndFinancialYearAndIsActiveTrueOrderByIdDesc(policyId,
								getQuarterByStrDate(transactionDate), getFinancialYrByDateString(transactionDate));
			} else {
				entity = policyTransactionSummaryRepository
						.findTopByPolicyIdAndFinancialYearAndIsActiveTrueOrderByIdDesc(policyId,
								getFinancialYrByDateString(transactionDate));
			}
			if (entity == null) {
				/**
				 * throw new ApplicationException( "No policy transaction summary entry found
				 * for the given policyid/transaction date:" + policyId + " ~ " +
				 * transactionDate);
				 */
				/** return new PolicyTransactionSummaryEntity(); */
			}
			return entity;
		} else {
			throw new ApplicationException("PolicyTransactionSummary:: The given variant is not valid:" + variantType
					+ " for policy id::" + policyId);
		}

	}

	public Long policyId(PolicyTransactionEntriesEntity entity) {
		return entity.getPolicyId();
	}

	public String getPolicyAndLicId(MemberTransactionEntriesEntity entity) {
		return String.valueOf(entity.getPolicyId() + " ~ " + entity.getLicId());
	}

	@Transactional
	public void updatePolicyContributionTxnStatus(List<ZeroAccountEntriesEntity> policyContributionTempEntities) {
		logger.info("updatePolicyContributionTxnStatus::{}", CommonConstants.LOGSTART);
		if (CommonUtils.isNonEmptyArray(policyContributionTempEntities)) {
			Set<ZeroAccountEntriesEntity> updatedPolicyContributions = new HashSet<>();
			policyContributionTempEntities.forEach(cont -> {
				cont.setTxnEntryStatus(true);
				updatedPolicyContributions.add(cont);
			});
			zeroAccountEntriesRepository.saveAll(updatedPolicyContributions);
		}
		logger.info("updatePolicyContributionTxnStatus::{}", CommonConstants.LOGEND);
	}

}