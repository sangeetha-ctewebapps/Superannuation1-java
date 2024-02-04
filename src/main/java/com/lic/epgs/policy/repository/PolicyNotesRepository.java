package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.policy.entity.PolicyNotesEntity;

public interface PolicyNotesRepository extends JpaRepository<PolicyNotesEntity, Long>{

	Set<PolicyNotesEntity> findByPolicyIdAndIsActiveTrue(Long policyId);

}
