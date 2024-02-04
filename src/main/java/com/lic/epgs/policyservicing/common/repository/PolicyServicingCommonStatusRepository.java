package com.lic.epgs.policyservicing.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.policyservicing.common.entity.PolicyServiceStatusEntity;

public interface PolicyServicingCommonStatusRepository  extends JpaRepository<PolicyServiceStatusEntity, Long> {


	List<PolicyServiceStatusEntity> findAllByIsActiveTrue();

}
