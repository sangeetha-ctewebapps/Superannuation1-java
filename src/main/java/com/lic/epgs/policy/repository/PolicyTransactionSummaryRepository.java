package com.lic.epgs.policy.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lic.epgs.policy.entity.PolicyTransactionSummaryEntity;

@Repository
public interface PolicyTransactionSummaryRepository extends JpaRepository<PolicyTransactionSummaryEntity, Long> {

//	PolicyTransactionSummaryEntity findTopByPolicyMasterEntityPolicyIdAndIsActiveTrueOrderByIdDesc(Long policyId);
//
//	PolicyTransactionSummaryEntity findTopByPolicyMasterEntityPolicyIdAndQuarterAndFinancialYearAndIsActiveTrueOrderByIdDesc(
//			Long policyId, Integer quarter, String financialYear);

	@Modifying
	@Transactional
	@Query("update PolicyTransactionSummaryEntity set interestPerRate =:interestPerRate where id =:id")
	void updatePolicyInterestRate(@Param("interestPerRate") Double interestPerRate, @Param("id") Long id);

//	List<PolicyTransactionSummaryEntity> findByPolicyMasterEntityPolicyIdAndFinancialYearAndIsExitFalseAndIsActiveTrueOrderByFinancialYearAsc(
//			Long policyId, String financialYear);
//
//	PolicyTransactionSummaryEntity findTopByPolicyMasterEntityPolicyIdAndQuarterAndFinancialYearAndIsExitFalseAndIsActiveTrueOrderByFinancialYearDesc(
//			Long policyId, Integer quarter, String financialYear);

	List<PolicyTransactionSummaryEntity> findByFinancialYearAndIsExitFalseAndIsActiveTrueOrderByFinancialYearAsc(
			String financialYrByDate);

//	PolicyTransactionSummaryEntity findTopByPolicyMasterEntityPolicyIdAndFinancialYearAndIsActiveTrueOrderByIdDesc(
//			Long policyId, String financialYrByDateString);
//
//	PolicyTransactionSummaryEntity findTopByPolicyMasterEntityPolicyNumberAndFinancialYearAndIsActiveTrueOrderByIdDesc(
//			String policyNumber, String financialYrByDateString);
//
//	PolicyTransactionSummaryEntity findTopByPolicyMasterEntityPolicyIdAndFinancialYearAndIsExitFalseAndIsActiveTrueAndIsActiveTrueOrderByIdDesc(
//			Long policyId, String financialYrByDateString);

//	PolicyTransactionSummaryEntity findTopByPolicyMasterEntityPolicyIdAndFinancialYearAndIsExitFalseAndIsActiveTrueAndIsActiveTrueOrderByCreatedOn(
//			Long policyId, String financialYrByDateString);

	@Query(value = "select total_contribution from policy_transaction_summary where policy_id=?1 and financial_year=?2", nativeQuery = true)

	BigDecimal fetchByPolicyIdAndfinancialYearAndTotalContributionAndDesOrder(@Param("policyId") Long policyId,
			@Param("financialYear") String financialYear);

	@Query(value = "select total_contribution from policy_transaction_summary where policy_id=?1 and financial_year=?2 and statement_frequency=?3", nativeQuery = true)
	BigDecimal fetchByPolicyIdAndfinancialYearAndQuarterAndTotalContributionAndDesOrder(@Param("policyId") Long  policyId,
			@Param("financialYear") String financialYear, @Param("frequency") Integer frequency);
	
	@Query(value = "select OPENING_BALANCE from policy_transaction_summary where policy_id=?1 and financial_year=?2 and statement_frequency=?3 and  is_active=1", nativeQuery = true)
	BigDecimal getByPolicyIdAndfinancialYearAndQuarterAndTotalContributionAndDesOrder(@Param("policyId") Long  policyId,
			@Param("financialYear") String financialYear, @Param("frequency") Integer frequency);
	
	@Query(value = "select OPENING_BALANCE from policy_transaction_summary where policy_id=?1 and financial_year=?2  and is_active=1", nativeQuery = true)
	BigDecimal getByPolicyIdAndfinancialYearAndTotalContributionAndDesOrder(@Param("policyId") Long  policyId,
			@Param("financialYear") String financialYear);

	PolicyTransactionSummaryEntity findTopByPolicyIdAndQuarterAndFinancialYearAndIsActiveTrueOrderByIdDesc(
			Long policyId, Integer quarterByStrDate, String financialYrByDateString);

	PolicyTransactionSummaryEntity findTopByPolicyIdAndFinancialYearAndIsActiveTrueOrderByIdDesc(Long policyId,
			String financialYrByDateString);
}
