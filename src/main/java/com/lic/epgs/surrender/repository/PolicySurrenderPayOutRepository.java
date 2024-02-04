package com.lic.epgs.surrender.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.surrender.entity.PolicySurrenderPayOutEntity;
import com.lic.epgs.surrender.entity.PolicySurrenderTempEntity;

@Repository
public interface PolicySurrenderPayOutRepository  extends JpaRepository<PolicySurrenderPayOutEntity, Long>{
	

	Optional<PolicySurrenderTempEntity> findBySurrenderId(Long mergeId);



}
