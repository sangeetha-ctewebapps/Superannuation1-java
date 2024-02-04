package com.lic.epgs.policyservicing.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesEntity;

@Repository
public interface PolicyServiceNotesRepository extends JpaRepository<PolicyServiceNotesEntity, Long>{

	List<PolicyServiceNotesEntity> findAllByServiceIdOrderByServiceNoteIdDesc(Long serviceId);

	
}
