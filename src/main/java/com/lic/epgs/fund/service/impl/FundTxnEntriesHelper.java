/**
 * 
 */
package com.lic.epgs.fund.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
import com.lic.epgs.fund.constants.InterestFundConstants;
import com.lic.epgs.integration.dto.CommonResponseDto;
import com.lic.epgs.integration.dto.InterestFundMemberContributionDto;
import com.lic.epgs.policy.entity.MemberContributionEntity;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MemberTransactionEntriesEntity;
import com.lic.epgs.policy.entity.MemberTransactionSummaryEntity;
import com.lic.epgs.policy.entity.PolicyContributionEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.PolicyTransactionEntriesEntity;
import com.lic.epgs.policy.entity.PolicyTransactionSummaryEntity;
import com.lic.epgs.policy.repository.MemberContributionRepository;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.MemberTransactionEntriesRepository;
import com.lic.epgs.policy.repository.MemberTransactionSummaryRepository;
import com.lic.epgs.policy.repository.PolicyContributionRepository;
import com.lic.epgs.policy.repository.PolicyTransactionEntriesRepository;
import com.lic.epgs.policy.repository.PolicyTransactionSummaryRepository;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

/**
 * @author Muruganandam
 *
 */
@Service
public class FundTxnEntriesHelper extends DateUtils implements InterestFundConstants {
	private Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private MemberTransactionEntriesRepository memberTransactionEntriesRepository;

	@Autowired
	private PolicyTransactionEntriesRepository policyTransactionEntriesRepository;

	@Autowired
	private PolicyContributionRepository policyContributionRepository;

	@Autowired
	private MemberContributionRepository memberContributionRepository;

	@Autowired
	private MemberMasterRepository memberMasterRepository;

	@Autowired
	private PolicyTransactionSummaryRepository policyTransactionSummaryRepository;

	@Autowired
	private MemberTransactionSummaryRepository memberTransactionSummaryRepository;

	public Set<Long> memberContIds(Set<MemberContributionEntity> froms) {
		if (CommonUtils.isNonEmptyArray(froms)) {
			return froms.stream().filter(mem -> mem.getMemberConId() != null)
					.map(MemberContributionEntity::getMemberConId).collect(Collectors.toSet());
		}
		return Collections.emptySet();
	}

	public Set<PolicyTransactionEntriesEntity> mapPolicyContributionAndEntries(
			Map<Long, Set<PolicyContributionEntity>> policyContrMap, PolicyMasterEntity masterEntity) {

		Set<PolicyTransactionEntriesEntity> tos = new HashSet<>();
		int i = 0;
		for (Entry<Long, Set<PolicyContributionEntity>> entrySet : policyContrMap.entrySet()) {
			Set<PolicyContributionEntity> policyContributionTempEntities = entrySet.getValue();
			if (CommonUtils.isNonEmptyArray(policyContributionTempEntities)) {
				List<PolicyTransactionEntriesEntity> policyWiseSet = new ArrayList<>();
				for (PolicyContributionEntity from : policyContributionTempEntities) {
					/** policyContributionTempEntities.forEach(from -> { */
					if (!NumericUtils.isBoolTrue(from.getTxnEntryStatus())) {
						PolicyTransactionEntriesEntity to = new PolicyTransactionEntriesEntity();
						BeanUtils.copyProperties(from, to);
						to.setClosingBalance(BigDecimal.ZERO);
						to.setCreatedBy(from.getCreatedBy());
						to.setCreatedOn(DateUtils.sysDate());
						to.setEntryType(from.getContributionType());
						to.setFinancialYear(getFinancialYrByDt(to.getCreatedOn()));
						to.setIsActive(true);
						to.setModifiedBy(from.getModifiedBy());
						to.setModifiedOn(DateUtils.sysDate());
						to.setOpeningBalance(BigDecimal.ZERO);
//						to.setPolicy(masterEntity);
						to.setPolTranId(null);

						to.setTransactionDate(from.getContributionDate());
						to.setTransationType(CREDIT);
						to.setIsOpeningBal(false);
						to.setEmployeeContribution(from.getEmployeeContribution());
						to.setEmployerContribution(from.getEmployerContribution());
						to.setVoluntaryContribution(from.getVoluntaryContribution());
						to.setTotalContribution(NumericUtils.add(from.getEmployeeContribution(),
								from.getEmployerContribution(), from.getVoluntaryContribution()));

						to.setTotalContribution(
								NumericUtils.bigDecimalValid(to.getTotalContribution()).compareTo(BigDecimal.ZERO) == 0
										? from.getTotalContribution()
										: to.getTotalContribution());

						to.setIsOpeningBal(false);
						to.setTxnRefNo(CommonUtils.txnRefNo(CR, masterEntity.getPolicyId(), from.getContributionId()));
//						PolicyTransactionEntriesEntity txnPrevRecord = getPolicyTxnPrevRecord(policyWiseSet, to, i);
//
//						to.setOpeningBalance(txnPrevRecord.getClosingBalance());
//						to.setClosingBalance(txnPrevRecord.getClosingBalance().add(to.getTotalContribution()));

						if (NumericUtils.bigDecimalValid(to.getTotalContribution()).compareTo(BigDecimal.ZERO) > 0) {
							policyWiseSet.add(to);
							i++;
						}

					}
					/** }); */
				}
				tos.addAll(policyWiseSet);
			}
		}

		return tos;

	}

	public Set<MemberTransactionEntriesEntity> mapMemberContributionAndEntries(
			Set<MemberContributionEntity> memberContributionByMemberConIds, PolicyMasterEntity masterEntity) {
		if (CommonUtils.isNonEmptyArray(memberContributionByMemberConIds)) {
			logger.info("Member entry");
		}
		return Collections.emptySet();
	}

	public void updatePolicyContributionTxnStatus(Set<PolicyContributionEntity> policyContributionTempEntities) {
		if (CommonUtils.isNonEmptyArray(policyContributionTempEntities)) {
			Set<PolicyContributionEntity> updatedPolicyContributions = new HashSet<>();
			policyContributionTempEntities.forEach(cont -> {
				cont.setTxnEntryStatus(true);
				updatedPolicyContributions.add(cont);
			});
			policyContributionRepository.saveAll(updatedPolicyContributions);
		}
	}

	@Transactional
	public void updateMemberContributionTxnStatus(Set<MemberContributionEntity> memberContributionTempEntities) {
		if (CommonUtils.isNonEmptyArray(memberContributionTempEntities)) {
			Set<MemberContributionEntity> updatedPolicyContributions = new HashSet<>();
			memberContributionTempEntities.forEach(cont -> {
				cont.setTxnEntryStatus(true);
				updatedPolicyContributions.add(cont);
			});
			memberContributionRepository.saveAll(updatedPolicyContributions);
		}
	}

	public MemberTransactionEntriesEntity getMemberTxnPrevRecord(List<MemberTransactionEntriesEntity> list,
			MemberTransactionEntriesEntity memberEntry, int i) {
		if (CommonUtils.isNonEmptyArray(list)) {
			if (list.size() > 1 && i > 1) {
				return list.get(i - 1);
			} else {
				return list.get(0);
			}
		}
		MemberTransactionEntriesEntity lastEntry = memberTransactionEntriesRepository
				.findTopByPolicyIdAndLicIdAndFinancialYearAndIsActiveTrueOrderByTransactionDateDesc(
						memberEntry.getPolicyId(), memberEntry.getLicId(), memberEntry.getFinancialYear());
		if (lastEntry != null) {
			return lastEntry;
		}
		lastEntry = new MemberTransactionEntriesEntity();
		lastEntry.setOpeningBalance(BigDecimal.ZERO);
		lastEntry.setClosingBalance(BigDecimal.ZERO);
		lastEntry.setTotalContribution(BigDecimal.ZERO);
		return lastEntry;
	}

//	public PolicyTransactionEntriesEntity getPolicyTxnPrevRecord(List<PolicyTransactionEntriesEntity> list,
//			PolicyTransactionEntriesEntity newEntry, int i) {
//		if (CommonUtils.isNonEmptyArray(list)) {
//			if (list.size() > 1 && i > 1) {
//				return list.get(i - 1);
//			} else {
//				return list.get(0);
//			}
//		}
//		PolicyTransactionEntriesEntity lastEntry = policyTransactionEntriesRepository
//				.findTopByPolicyPolicyIdAndAndFinancialYearAndIsActiveTrueOrderByTransactionDateDesc(
//						newEntry.getPolicy().getPolicyId(), newEntry.getFinancialYear());
//		if (lastEntry != null) {
//			return lastEntry;
//		}
//		lastEntry = new PolicyTransactionEntriesEntity();
//		lastEntry.setOpeningBalance(BigDecimal.ZERO);
//		lastEntry.setClosingBalance(BigDecimal.ZERO);
//		lastEntry.setTotalContribution(BigDecimal.ZERO);
//		return lastEntry;
//	}

	public String getPolicyAndLicId(MemberContributionEntity entity) {
		return String.valueOf(entity.getPolicyId() + "*" + entity.getLicId());
	}

	public String getPolicyAndLicId(MemberTransactionEntriesEntity entity) {
		return String.valueOf(entity.getPolicyId() + "*" + entity.getLicId());
	}

	public Map<String, Set<MemberContributionEntity>> getMemberContributionsByMember(
			Set<MemberMasterEntity> memberMaster) {
		List<MemberContributionEntity> list = memberMaster.stream().flatMap(t -> t.getMemberContribution().stream())
				.filter(mc -> !NumericUtils.isBoolTrue(mc.getTxnEntryStatus())).collect(Collectors.toList());

		return list.stream().collect(Collectors.groupingBy(this::getPolicyAndLicId, Collectors.toSet()));

	}

	public Map<Long, Set<PolicyContributionEntity>> getPolicyContributionsByPolicyId(
			Set<PolicyContributionEntity> contributions) {
		return contributions.stream()
				.collect(Collectors.groupingBy(PolicyContributionEntity::getPolicyId, Collectors.toSet()));
	}

	public Set<MemberTransactionEntriesEntity> getMemberTransactionEntries(PolicyMasterEntity masterEntity,
			Set<MemberContributionEntity> memberContributionEntities1) {
		Set<MemberMasterEntity> memberMaster = masterEntity.getMemberMaster();

		if (CommonUtils.isNonEmptyArray(memberMaster)) {

			Map<String, Set<MemberContributionEntity>> map = getMemberContributionsByMember(memberMaster);

			/** memberMaster.forEach(member -> { */
			/**
			 * Set<MemberContributionEntity> memberContributionEntities =
			 * member.getMemberContribution();
			 */
			Set<MemberTransactionEntriesEntity> memberTransactionEntriesEntities = new HashSet<>();
			for (Entry<String, Set<MemberContributionEntity>> entrySet : map.entrySet()) {
				Set<MemberContributionEntity> memberContributionEntities = entrySet.getValue();
				if (CommonUtils.isNonEmptyArray(memberContributionEntities)) {

					/** memberContributionEntities.forEach(from -> { */
					int i = 0;
					List<MemberTransactionEntriesEntity> memberWiseSet = new ArrayList<>();
					for (MemberContributionEntity from : memberContributionEntities) {
						if (!NumericUtils.isBoolTrue(from.getTxnEntryStatus())) {
							MemberTransactionEntriesEntity to = new MemberTransactionEntriesEntity();

							MemberMasterEntity member = memberMasterRepository
									.findByMemberIdAndPolicyIdAndIsActiveTrueAndIsZeroidFalse(from.getMemberId(),
											from.getPolicyId());

							BeanUtils.copyProperties(from, to);
							to.setClosingBalance(BigDecimal.ZERO);
							to.setCreatedBy(from.getCreatedBy());
							to.setCreatedOn(DateUtils.sysDate());

							to.setEntryType(
									StringUtils.isNotBlank(from.getContributionType()) ? from.getContributionType()
											: CONTRIBUTION_ADJUSTMENT);
							to.setFinancialYear(getFinancialYrByDt(to.getCreatedOn()));
							to.setIsActive(true);
							to.setModifiedBy(from.getModifiedBy());
							to.setModifiedOn(DateUtils.sysDate());

							to.setPolicyId(masterEntity.getPolicyId());
							to.setMemTranId(null);
							to.setTransactionDate(from.getContributionDate() != null ? from.getContributionDate()
									: from.getCreatedOn());
							to.setTransationType(CREDIT);
							to.setMember(member);
							to.setLicId(member.getLicId());
							to.setIsOpeningBal(false);
							to.setEmployeeContribution(from.getEmployeeContribution());
							to.setEmployerContribution(from.getEmployerContribution());
							to.setVoluntaryContribution(from.getVoluntaryContribution());
							to.setTotalContribution(NumericUtils.add(from.getEmployeeContribution(),
									from.getEmployerContribution(), from.getVoluntaryContribution()));

							to.setTotalContribution(NumericUtils.bigDecimalValid(to.getTotalContribution())
									.compareTo(BigDecimal.ZERO) > 0 ? to.getTotalContribution()
											: from.getTotalContribution());

							to.setIsOpeningBal(false);
							MemberTransactionEntriesEntity memberTxnPrevRecord = getMemberTxnPrevRecord(memberWiseSet,
									to, i);
							to.setTxnRefNo(CommonUtils.txnRefNo(CR, masterEntity.getPolicyId(), from.getMemberConId()));
							to.setOpeningBalance(memberTxnPrevRecord.getClosingBalance());
							to.setClosingBalance(
									memberTxnPrevRecord.getClosingBalance().add(to.getTotalContribution()));

							if (NumericUtils.bigDecimalValid(to.getTotalContribution())
									.compareTo(BigDecimal.ZERO) > 0) {
								memberWiseSet.add(to);
								i++;
							}

							memberContributionEntities1.add(from);
						}
					}
					memberTransactionEntriesEntities.addAll(memberWiseSet);
					/** }); */
				}
			}
			return memberTransactionEntriesEntities;
			/** }); */
		}
		return Collections.emptySet();
	}

	@Transactional
	public CommonResponseDto<Object> savePolicyAndMemberContributionEntries(PolicyMasterEntity masterEntity) {
		logger.info(masterEntity.getPolicyNumber());
		CommonResponseDto<Object> responseDto = new CommonResponseDto<>();
		List<String> errors = new ArrayList<>();
		Set<PolicyContributionEntity> policyContributionEntities = masterEntity.getPolicyContributions();
		if (CommonUtils.isNonEmptyArray(policyContributionEntities)) {
			/***
			 * @notes Set of txnEntryStatus with false/null
			 */
			Set<PolicyContributionEntity> policyContributions = policyContributionEntities.stream()
					.filter(pc -> !NumericUtils.isBoolTrue(pc.getTxnEntryStatus())).collect(Collectors.toSet());

			/***
			 * @notes Map Policy contribution to Policy Transaction Entries
			 */
			Map<Long, Set<PolicyContributionEntity>> policyContrMap = getPolicyContributionsByPolicyId(
					policyContributions);
			Set<PolicyTransactionEntriesEntity> policyEntries = mapPolicyContributionAndEntries(policyContrMap,
					masterEntity);
			masterEntity.setPolicyContributions(policyContributions);

			policyTransactionEntriesRepository.saveAll(policyEntries);

//			policyTransactionSummary(policyEntries, masterEntity);

			/***
			 * @notes update transaction status for policy and member contribution
			 */
			updatePolicyContributionTxnStatus(policyContributions);

			/**
			 * policyMasterRepository.save(masterEntity); memberMasterRepository.save(null)
			 */

			if (StringUtils.isNotEmpty(masterEntity.getPolicyType())
					&& DC.equalsIgnoreCase(masterEntity.getPolicyType())) {
				/***
				 * @notes Set empty set for Member Transaction entries
				 */
				Set<MemberContributionEntity> memberContributionEntities = new HashSet<>();
				/***
				 * @notes Copy Non entry member contribution to Transaction entries
				 */
				Set<MemberTransactionEntriesEntity> memberContributionEntries = getMemberTransactionEntries(
						masterEntity, memberContributionEntities);
				memberTransactionEntriesRepository.saveAll(memberContributionEntries);
				memberTransactionSummary(memberContributionEntries, masterEntity);
				updateMemberContributionTxnStatus(memberContributionEntities);
				if (!CommonUtils.isNonEmptyArray(memberContributionEntries)) {
					errors.add("No Member contribution found for the policy id:" + masterEntity.getPolicyId());
				}
				responseDto.setTransactionMessage(
						"Policy and Member contributions are push to fund entries for the policy number:"
								+ masterEntity.getPolicyNumber());
			} else {
				responseDto.setTransactionMessage("Policy contributions are push to fund entries for the policy number:"
						+ masterEntity.getPolicyNumber());
			}

			if (!CommonUtils.isNonEmptyArray(policyEntries)) {
				errors.add("No Policy contribution found for the policy id:" + masterEntity.getPolicyId());
			}
			/** responseDto.setErrors(errors); */
			responseDto.setTransactionStatus(CommonConstants.SUCCESS);
			/** responseDto.setStatusId(1); */

			return responseDto;
		}
		return responseDto;
	}

	/**
	 * public List<MemberTransactionEntriesEntity>
	 * getMemberContributionEntries(PolicyMasterTempEntity tempEntity,
	 * PolicyMasterEntity masterEntity, Set<MemberContributionEntity>
	 * memberContributionByMemberConIds) {
	 * logger.info(tempEntity.getPolicyNumber());
	 * 
	 * if (CommonUtils.isNonEmptyArray(memberContributionByMemberConIds)) {
	 * mapMemberContributionAndEntries(memberContributionByMemberConIds,
	 * masterEntity); } return Collections.emptyList(); }
	 */

	public PolicyTransactionEntriesEntity policyTransactionEntriesEntity(
			Set<PolicyTransactionEntriesEntity> policyEntries) {
		return policyEntries.stream().findAny().orElse(new PolicyTransactionEntriesEntity());

	}

//	public Long policyId(PolicyTransactionEntriesEntity entity) {
//		return entity.getPolicy().getPolicyId();
//	}

	public void policyTransactionSummaryByPolicyType(PolicyMasterEntity masterEntity,
			PolicyTransactionSummaryEntity entity) throws ApplicationException {

		InterestFundMemberContributionDto dto = new InterestFundMemberContributionDto();
		dto.setPolicyId(masterEntity.getPolicyId());
		dto.setTxnType(CREDIT);

		sumOfPolicyContribution(dto);
		entity.setOpeningBalanceAmount(NumericUtils.bigDecimalValid(entity.getClosingBalance()));
		entity.setClosingBalance(NumericUtils.bigDecimalNegative(dto.getTotalContributionAmount()));
		entity.setTotalContribution(NumericUtils.bigDecimalValid(dto.getTotalContributionAmount()));
		entity.setEmployeeContribution(dto.getEmployeeContributionAmount());
		entity.setEmployerContribution(dto.getEmployerContributionAmount());
		entity.setVoluntaryContribution(dto.getVoluntaryContributionAmount());

		/**
		 * if (StringUtils.isNotEmpty(masterEntity.getPolicyType()) &&
		 * DC.equalsIgnoreCase(masterEntity.getPolicyType())) {
		 * memberStatementService.sumOfMemberContributionByPolicyId(dto);
		 * entity.setEmployeeContribution(NumericUtils.bigDecimalValid(dto.
		 * getEmployeeContributionAmount()));
		 * entity.setEmployerContribution(NumericUtils.bigDecimalValid(dto.
		 * getEmployerContributionAmount()));
		 * entity.setVoluntaryContribution(NumericUtils.bigDecimalValid(dto.
		 * getVoluntaryContributionAmount()));
		 * 
		 * entity.setTotalContribution(NumericUtils.add(entity.getEmployeeContribution()
		 * , entity.getEmployerContribution(), entity.getVoluntaryContribution())); }
		 * else {
		 * 
		 * policyBatchStatementService.sumOfPolicyContribution(dto);
		 * 
		 * entity.setEmployeeContribution(NumericUtils.bigDecimalValid(dto.
		 * getEmployeeContributionAmount()));
		 * entity.setEmployerContribution(NumericUtils.bigDecimalValid(dto.
		 * getEmployerContributionAmount()));
		 * entity.setVoluntaryContribution(NumericUtils.bigDecimalValid(dto.
		 * getVoluntaryContributionAmount()));
		 * 
		 * entity.setTotalContribution(NumericUtils.bigDecimalValid(dto.
		 * getTotalContributionAmount())); }
		 */

	}

//	@Transactional
//	public void policyTransactionSummary(Set<PolicyTransactionEntriesEntity> policyEntries,
//			PolicyMasterEntity masterEntity) {
//
//		Map<Long, Set<PolicyTransactionEntriesEntity>> policyMap = policyEntries.stream()
//				.collect(Collectors.groupingBy(this::policyId, Collectors.toSet()));
//
//		/**
//		 * Map<Long, PolicyTransactionEntriesEntity> collect2 = policyEntries.stream()
//		 * .collect(Collectors.toMap(this::policyId, Function.identity()));
//		 */
//		for (Entry<Long, Set<PolicyTransactionEntriesEntity>> entry : policyMap.entrySet()) {
//
//			Set<PolicyTransactionEntriesEntity> policies = entry.getValue();
//
//			if (CommonUtils.isNonEmptyArray(policies)) {
//				policies.forEach(entryEntity -> {
//					PolicyTransactionSummaryEntity entity = policyTransactionSummaryRepository
//							.findTopByPolicyMasterEntityPolicyIdAndFinancialYearAndIsActiveTrueOrderByIdDesc(
//									entry.getKey(), getFinancialYrByDt(DateUtils.sysDate()));
//
//					if (entity == null) {
//						entity = new PolicyTransactionSummaryEntity();
//						entity.setOpeningBalanceAmount(BigDecimal.ZERO);
//					} else {
//						entity.setOpeningBalanceAmount(NumericUtils.bigDecimalValid(entity.getClosingBalance()));
//					}
//
//					entity.setCreatedBy(masterEntity.getCreatedBy());
//					entity.setCreatedOn(DateUtils.sysDate());
//
//					entity.setFinancialYear(getFinancialYrByDt(entity.getCreatedOn()));
//					entity.setIsActive(true);
//					entity.setModifiedBy(masterEntity.getModifiedBy());
//					entity.setModifiedOn(DateUtils.sysDate());
//					entity.setPolicyMasterEntity(masterEntity);
//					try {
//						policyTransactionSummaryByPolicyType(masterEntity, entity);
//					} catch (ApplicationException e) {
//						logger.error("policyTransactionSummary Error:", e);
//					}
//					entity.setClosingBalance(entity.getTotalContribution());
//
//					/**
//					 * entity.setPolicyNumber(masterEntity.getPolicyNumber());
//					 * entity.setStream(InterestFundConstants.SUPERANNUATION);
//					 * entity.setPolicyType(NumericUtils.convertLongToString(masterEntity.
//					 * getProductId())); entity.setVariant(masterEntity.getVariant());
//					 */
//					entity.setQuarter(getQuarterByDate(entryEntity.getTransactionDate()));
//					policyTransactionSummaryRepository.save(entity);
//				});
//			}
//		}
//	}

	public Map<String, Set<MemberTransactionEntriesEntity>> getMemberTransactionByMember(
			Set<MemberTransactionEntriesEntity> list) {
		return list.stream().collect(Collectors.groupingBy(this::getPolicyAndLicId, Collectors.toSet()));

	}

	@Transactional
	public void memberTransactionSummary(Set<MemberTransactionEntriesEntity> memberTxnEntries,
			PolicyMasterEntity masterEntity) {

		/**
		 * Double employeeAmount = memberTxnEntries.stream().filter(p ->
		 * p.getEmployeeContribution() != null) .mapToDouble(op ->
		 * op.getEmployeeContribution().doubleValue()).sum();
		 * 
		 * Double employerAmount = memberTxnEntries.stream().filter(p ->
		 * p.getEmployerContribution() != null) .mapToDouble(op ->
		 * op.getEmployerContribution().doubleValue()).sum();
		 * 
		 * Double volunAmount = memberTxnEntries.stream().filter(p ->
		 * p.getVoluntaryContribution() != null) .mapToDouble(op ->
		 * op.getVoluntaryContribution().doubleValue()).sum();
		 * 
		 * Double totAmount = memberTxnEntries.stream().filter(p ->
		 * p.getTotalContribution() != null) .mapToDouble(op ->
		 * op.getTotalContribution().doubleValue()).sum();
		 */
		Map<String, Set<MemberTransactionEntriesEntity>> map = getMemberTransactionByMember(memberTxnEntries);

		for (Entry<String, Set<MemberTransactionEntriesEntity>> entrySet : map.entrySet()) {
			Set<MemberTransactionEntriesEntity> memberEntries = entrySet.getValue();
			if (CommonUtils.isNonEmptyArray(memberEntries)) {
				memberEntries.forEach(memEntity -> {
					MemberTransactionSummaryEntity memberSummary = memberTransactionSummaryRepository
							.findTopByPolicyIdAndMemberIdAndFinancialYearAndIsActiveTrueOrderByFinancialYearDesc(
									masterEntity.getPolicyId(), memEntity.getLicId(),
									getFinancialYrByDt(DateUtils.sysDate()));

					if (memberSummary == null) {
						memberSummary = new MemberTransactionSummaryEntity();
						memberSummary.setOpeningBalance(BigDecimal.ZERO);
					} else {
						memberSummary
								.setOpeningBalance(NumericUtils.bigDecimalValid(memberSummary.getClosingBalance()));
					}
					memberSummary.setQuarter(getQuarterByDate(memEntity.getTransactionDate()));

					memberSummary.setCreatedBy(masterEntity.getCreatedBy());
					memberSummary.setCreatedOn(DateUtils.sysDate());

					InterestFundMemberContributionDto dto = new InterestFundMemberContributionDto();
					dto.setMemberId(memEntity.getLicId());
					dto.setPolicyId(memEntity.getPolicyId());
					dto.setTxnType(CREDIT);

					try {
						sumOfMemberContribution(dto);
					} catch (ApplicationException e) {
						logger.error("memberTransactionSummary Error:", e);
					}

					memberSummary
							.setEmployeeContribution(NumericUtils.bigDecimalValid(dto.getEmployeeContributionAmount()));
					memberSummary
							.setEmployerContribution(NumericUtils.bigDecimalValid(dto.getEmployerContributionAmount()));

					memberSummary.setVoluntaryContribution(
							NumericUtils.bigDecimalValid(dto.getVoluntaryContributionAmount()));

					memberSummary.setTxnAmount(NumericUtils.add(memberSummary.getEmployeeContribution(),
							memberSummary.getEmployerContribution(), memberSummary.getVoluntaryContribution()));

					memberSummary.setClosingBalance(memberSummary.getTxnAmount());

					/* memberSummary.setMemberMasterEntity(memEntity.getMember()); */
					memberSummary.setMemberId(memEntity.getLicId());
					memberSummary.setPolicyId(memEntity.getPolicyId());

					memberSummary.setFinancialYear(getFinancialYrByDt(memberSummary.getCreatedOn()));
					memberSummary.setIsActive(true);
					memberSummary.setModifiedBy(masterEntity.getModifiedBy());
					memberSummary.setModifiedOn(DateUtils.sysDate());

					memberSummary.setPolicyId(masterEntity.getPolicyId());
					memberTransactionSummaryRepository.save(memberSummary);
				});
				/** memberSummaryList.add(memberSummary); */
				/** memberTransactionSummaryRepository.saveAll(memberSummaryList); */
			}
		}

	}

	public void sumOfPolicyContribution(InterestFundMemberContributionDto dto) throws ApplicationException {
//		List<Object> contributionOb = CommonUtils
//				.objectArrayToList(policyTransactionEntriesRepository.sumOfPolicyContribution(dto.getPolicyId(), true));
//
//		if (StringUtils.isNotBlank(dto.getTxnType())) {
//			dto.setTotalContributionAmount(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(3))));
//			dto.setEmployeeContributionAmount(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(0))));
//			dto.setEmployerContributionAmount(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(1))));
//			dto.setVoluntaryContributionAmount(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(2))));
//			dto.setTotalContributionAmount(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(3))));
//		} else {
//			throw new ApplicationException("No transaction type  found for the given POLICY ID:" + dto.getPolicyId());
//		}

	}

	public void sumOfMemberContribution(InterestFundMemberContributionDto dto) throws ApplicationException {
		List<Object> contributionOb = CommonUtils.objectArrayToList(
				memberTransactionEntriesRepository.sumOfMemberContribution(dto.getMemberId(), dto.getPolicyId(), true));
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
				dto.setTotalContributionAmount(
						NumericUtils.bigDecimalNegative(NumericUtils.add(dto.getEmployeeContributionAmount(),
								dto.getEmployerContributionAmount(), dto.getVoluntaryContributionAmount())));

			} else {
				dto.setTotalContributionAmount(NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(3))));
				dto.setEmployeeContributionAmount(
						NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(0))));
				dto.setEmployerContributionAmount(
						NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(1))));
				dto.setVoluntaryContributionAmount(
						NumericUtils.stringToBigDecimal(String.valueOf(contributionOb.get(2))));
				dto.setTotalContributionAmount(NumericUtils.add(dto.getEmployeeContributionAmount(),
						dto.getEmployerContributionAmount(), dto.getVoluntaryContributionAmount()));
			}
		} else {
			throw new ApplicationException("No transaction type  found for the given LIC ID:" + dto.getMemberId());
		}

	}
}
