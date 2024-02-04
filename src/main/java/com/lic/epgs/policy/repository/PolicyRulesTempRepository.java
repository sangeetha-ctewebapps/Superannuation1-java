package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.PolicyRulesTempEntity;

@Repository
public interface PolicyRulesTempRepository extends JpaRepository<PolicyRulesTempEntity, Long> {

	List<PolicyRulesTempEntity> findAllByPolicyIdAndIsActiveTrue(Long policyId);

}
