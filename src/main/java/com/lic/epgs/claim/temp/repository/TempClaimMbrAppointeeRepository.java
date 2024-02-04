package com.lic.epgs.claim.temp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.temp.entity.TempClaimMbrAppointeeEntity;

@Repository
public interface TempClaimMbrAppointeeRepository extends JpaRepository<TempClaimMbrAppointeeEntity, Long>,
		JpaSpecificationExecutor<TempClaimMbrAppointeeEntity> {

	TempClaimMbrAppointeeEntity findByAppointeeId(Long appointeeId);

}
