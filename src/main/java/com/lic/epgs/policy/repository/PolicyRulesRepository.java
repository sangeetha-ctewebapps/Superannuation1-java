package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.PolicyRulesEntity;



@Repository
public interface PolicyRulesRepository extends JpaRepository<PolicyRulesEntity, Long> {

//	PolicyRulesEntity findByPolicyIdAndCategoryAndIsActiveTrue(Long policyId, String category);


//	PolicyRulesEntity findByPolicyIdAndCategoryAndIsActiveTrue(Long policyId, String category);

	Set<PolicyRulesEntity> findByPolicyIdAndIsActiveTrue(Long policyId);
	
	PolicyRulesEntity findTopByPolicyIdAndCategory(Long policyId,String category);
	
	@Query(value = "Select ANNUITY_OPTION FROM POLICY_RULES WHERE POLICY_ID =:policyId AND IS_ACTIVE = 1", nativeQuery = true)
	String findByPolicyId(Long policyId);

}
