package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.PolicyContributionSummaryTempEntity;

@Repository
public interface PolicyContributionSummaryTempRepository extends JpaRepository<PolicyContributionSummaryTempEntity, Long> {

	Optional<PolicyContributionSummaryTempEntity> findByPolicyIdAndIsActiveTrue(Long policyId);

	Optional<PolicyContributionSummaryTempEntity> findByPolicyId(Long policyId);

	List<PolicyContributionSummaryTempEntity> findAllByPolicyIdAndIsActiveTrue(Long policyId);

}
