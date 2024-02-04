package com.lic.epgs.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.entity.ClaimMbrEntity;

@Repository
public interface ClaimMbrRepository
		extends JpaRepository<ClaimMbrEntity, Long>, JpaSpecificationExecutor<ClaimMbrEntity> {

}
