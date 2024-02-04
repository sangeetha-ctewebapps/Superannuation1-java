package com.lic.epgs.policyservicing.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesTempEntity;

public interface PolicyServiceNotesTempRepository extends JpaRepository<PolicyServiceNotesTempEntity, Long>{

	List<PolicyServiceNotesTempEntity> findAllByPolicyIdAndIsActiveTrue(Long policyId);

	List<PolicyServiceNotesTempEntity> findAllByServiceIdAndIsActiveTrue(Long serviceId);

	List<PolicyServiceNotesTempEntity> findAllByServiceIdOrderByServiceNoteIdDesc(Long serviceId);
}
