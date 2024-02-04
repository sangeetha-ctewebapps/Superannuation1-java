package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.PolicyValuationEntity;

@Repository
public interface PolicyValuationRepository extends JpaRepository<PolicyValuationEntity, Long>{

	Set<PolicyValuationEntity> findByPolicyIdAndIsActiveTrue(Long policyId);

}
