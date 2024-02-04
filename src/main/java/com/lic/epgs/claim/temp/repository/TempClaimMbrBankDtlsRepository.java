package com.lic.epgs.claim.temp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.temp.entity.TempClaimMbrBankDetailEntity;

@Repository
public interface TempClaimMbrBankDtlsRepository extends JpaRepository<TempClaimMbrBankDetailEntity, Long>,
		JpaSpecificationExecutor<TempClaimMbrBankDetailEntity> {

	List<TempClaimMbrBankDetailEntity> findByClaimNoOrderByBankIdDesc(String claimNo);

}
