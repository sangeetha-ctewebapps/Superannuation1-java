package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.policy.entity.PolicyNotesTempEntity;

public interface PolicyNotesTempRepository extends JpaRepository<PolicyNotesTempEntity, Long>{

	List<PolicyNotesTempEntity> findAllByPolicyId(Long policyId);

}
