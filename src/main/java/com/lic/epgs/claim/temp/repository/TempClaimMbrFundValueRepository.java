package com.lic.epgs.claim.temp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.temp.entity.TempClaimMbrFundValueEntity;

@Repository
public interface TempClaimMbrFundValueRepository extends JpaRepository<TempClaimMbrFundValueEntity, Long>,
		JpaSpecificationExecutor<TempClaimMbrFundValueEntity> {

}
