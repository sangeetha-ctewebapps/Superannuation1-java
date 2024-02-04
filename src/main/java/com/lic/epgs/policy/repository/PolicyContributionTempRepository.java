package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.PolicyContributionTempEntity;

@Repository
public interface PolicyContributionTempRepository extends JpaRepository<PolicyContributionTempEntity, Long> {

	List<PolicyContributionTempEntity> findByPolicyIdAndIsActiveTrue(Long policyId);

	Optional<PolicyContributionTempEntity> findByPolicyId(Long policyId);

	Optional<PolicyContributionTempEntity> findByPolicyIdAndIsDepositFalseAndIsActiveTrue(Long policyId);

	@Query("SELECT e from PolicyContributionTempEntity e where e.policyId =:policyId and (e.txnEntryStatus=:txnEntryStatus or e.txnEntryStatus is null) and e.isActive=:isActive")
	List<PolicyContributionTempEntity> getPolicyContributionByPolicyId(@Param("policyId") Long policyId,
			@Param("txnEntryStatus") boolean txnEntryStatus, @Param("isActive") Boolean isActive);

	List<PolicyContributionTempEntity> findByPolicyIdAndIsActiveTrueOrderByVersionNoDesc(Long policyId);

	List<PolicyContributionTempEntity> findByPolicyIdAndAdjustmentContributionIdAndIsActiveTrue(Long policyId,
			Long adjustmentContributionId);

	List<PolicyContributionTempEntity> findByPolicyIdAndRegularContributionIdAndIsActiveTrue(Long tempPolicyId,
			Long regularContributionId);

	@Query(value = "SELECT * from POLICY_CONTRIBUTION_TEMP where MASTER_POLICY_ID =:masterPolicyId And IS_ACTIVE = 1", nativeQuery = true)
	List<PolicyContributionTempEntity> findByMasterPolicyIdAndIsActiveTrue(Long masterPolicyId);

	@Query(value = "SELECT POL_CON_T_SEQ.nextval FROM dual", nativeQuery = true)
	public Long getContributionTempSeq();

	PolicyContributionTempEntity findTopByPolicyIdAndAdjustmentContributionIdAndIsActiveTrueOrderByContributionIdDesc(
			Long policyId, Long adjustmentContributionId);

	PolicyContributionTempEntity findTopByPolicyIdAndRegularContributionIdAndIsActiveTrueOrderByContributionIdDesc(
			Long tempPolicyId, Long regularContributionId);

	List<PolicyContributionTempEntity> findByPolicyIdAndFinancialYearAndIsActiveTrueOrderByVersionNoDesc(Long policyId,String calculateFinaYr);

	List<PolicyContributionTempEntity> findByPolicyIdAndAdjustmentContributionIdAndIsActiveTrueOrderByContributionIdDesc(
			Long tempPolicyId, Long adjustmentContributionId);

	List<PolicyContributionTempEntity> findByPolicyIdAndRegularContributionIdAndIsActiveTrueOrderByContributionIdDesc(
			Long tempPolicyId, Long regularContributionId);
}
