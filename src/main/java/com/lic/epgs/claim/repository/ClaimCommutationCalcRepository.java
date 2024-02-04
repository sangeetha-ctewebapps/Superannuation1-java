package com.lic.epgs.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.entity.ClaimCommutationCalcEntity;

@Repository
public interface ClaimCommutationCalcRepository extends JpaRepository<ClaimCommutationCalcEntity, Long>,
		JpaSpecificationExecutor<ClaimCommutationCalcEntity> {

}
