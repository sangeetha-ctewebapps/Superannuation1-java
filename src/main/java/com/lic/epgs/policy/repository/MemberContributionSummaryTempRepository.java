package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.MemberContributionSummaryTempEntity;

@Repository
public interface MemberContributionSummaryTempRepository extends JpaRepository<MemberContributionSummaryTempEntity, Long>{

	Optional<MemberContributionSummaryTempEntity> findByPolicyId(Long policyId);

}
