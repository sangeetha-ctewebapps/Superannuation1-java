package com.lic.epgs.quotation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.policyservicing.common.entity.PolicyServiceEntity;

public interface PolicyServicerRepositry extends JpaRepository<PolicyServiceEntity, Long>{

	@Query(value = "SELECT * FROM POLICY_SERVICE WHERE POLICY_ID=:policyId ", nativeQuery = true)
	List<PolicyServiceEntity> findAllByPolicyIdAndIsActiveTrue(Long policyId);
}
