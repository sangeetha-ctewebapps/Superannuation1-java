package com.lic.epgs.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.entity.ClaimFundValueEntity;


@Repository
public interface ClaimFundValueRepository extends JpaRepository<ClaimFundValueEntity, Long>,
		JpaSpecificationExecutor<ClaimFundValueEntity> {

}
