package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.PolicyValuationTempEntity;

@Repository
public interface PolicyValuationTempRepository extends JpaRepository<PolicyValuationTempEntity, Long>{

	PolicyValuationTempEntity findAllByPolicyIdAndIsActiveTrue(Long policyId);

}
