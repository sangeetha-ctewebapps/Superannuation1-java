package com.lic.epgs.policy.repository;

import java.math.BigDecimal;
import java.util.List;
/**
 * @author pradeepramesh
 *
 */
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.PolicyContributionSummaryEntity;

@Repository
public interface PolicyContributionSummaryRepository extends JpaRepository<PolicyContributionSummaryEntity, Long> {

	Optional<PolicyContributionSummaryEntity> findByPolicyId(Long policyId);

	Optional<PolicyContributionSummaryEntity> findByPolicyIdAndIsActiveTrue(Long policyId);

	PolicyContributionSummaryEntity findTopByPolicyIdAndFinancialYearAndIsActiveTrueOrderByPolContSummaryIdDesc(Long policyId, String financialYear);

	Set<PolicyContributionSummaryEntity> findByPolicyIdAndIsActive(Long policyId, Boolean true1);

	List<PolicyContributionSummaryEntity> findByPolicyIdAndFinancialYearAndIsActiveTrueOrderByPolContSummaryIdDesc(
			Long policyId, String financialYear);

	@Query(value = "SELECT TOTAL_CONTRIBUTION , POLICY_ID  FROM POLICY_CONTRI_SUMMARY pcs WHERE POLICY_ID =:policyId AND IS_ACTIVE =1", nativeQuery = true)
	BigDecimal findByPolicy(Long policyId);

	@Query(value = "SELECT  SUM(TOTAL_CONTRIBUTION) \"TOTAL_CONTRIBUTION\" FROM POLICY_CONTRI_SUMMARY pcs WHERE POLICY_ID =:policyId AND FINANCIAL_YEAR = :finicialYear AND IS_ACTIVE =1",nativeQuery = true)
	BigDecimal findByPolicyByFinicialYear(Long policyId, String finicialYear);
	
	@Query(value = "SELECT  SUM(TOTAL_CONTRIBUTION) \"TOTAL_CONTRIBUTION\" FROM POLICY_CONTRI_SUMMARY pcs WHERE POLICY_ID =:policyId AND IS_ACTIVE =1",nativeQuery = true)
	BigDecimal findByPolicyTotalContributionByFinicialYear(Long policyId);
}
