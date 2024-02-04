package com.lic.epgs.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.entity.ClaimMbrNomineeEntity;

@Repository
public interface ClaimMbrNomineeRepository
		extends JpaRepository<ClaimMbrNomineeEntity, Long>, JpaSpecificationExecutor<ClaimMbrNomineeEntity> {

}
