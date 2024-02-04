package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.PolicyDepositTempEntity;

@Repository
public interface PolicyDepositTempRepository extends JpaRepository<PolicyDepositTempEntity, Long> {

	List<PolicyDepositTempEntity> findAllByPolicyIdAndIsActiveTrue(Long policyId);

	Optional<PolicyDepositTempEntity> findByStatusAndDepositIdAndZeroIdAndPolicyId(Object object, Long depositId,
			boolean b, Long policyId);

	List<PolicyDepositTempEntity> findByStatusAndPolicyIdAndIsActiveTrue(String depositstatusnew, Long policyId);

	List<PolicyDepositTempEntity> findByStatusAndPolicyIdAndCollectionNoInAndIsActiveTrue(String adjested,
			Long policyId, List<String> contReferenceNolist);

	List<PolicyDepositTempEntity> findByStatusAndAdjustmentContributionIdAndPolicyIdAndIsActiveTrue(String adjested,
			Long adjustmentContributionId, Long policyId);

	Optional<PolicyDepositTempEntity> findByStatusAndDepositIdAndZeroIdAndPolicyIdAndAdjustmentContributionId(
			String depositstatusnew, Long adjustmentContributiondepositId, boolean b, Long policyId,
			Long adjustmentContributionId);

	List<PolicyDepositTempEntity> findByPolicyIdAndAdjustmentContributionIdAndIsActiveTrue(Long policyId,
			Long adjustmentContributionId);

	List<PolicyDepositTempEntity> findByStatusAndRegularContributionIdAndPolicyIdAndIsActiveTrue(
			String depositstatusnew, Long adjustmentContributionId, Long policyId);

	Optional<PolicyDepositTempEntity> findByStatusAndDepositIdAndZeroIdAndPolicyIdAndRegularContributionId(
			String depositstatusnew, Long regularContributiondepositId, boolean b, Long policyId,
			Long regularContributionId);

	List<PolicyDepositTempEntity> findByPolicyIdAndRegularContributionIdAndIsActiveTrue(Long tempPolicyId,
			Long regularContributionId);

}
