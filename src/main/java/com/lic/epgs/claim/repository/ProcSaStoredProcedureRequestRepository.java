package com.lic.epgs.claim.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.claim.entity.ProcSaStoredProcedureRequestEntity;

public interface ProcSaStoredProcedureRequestRepository extends JpaRepository<ProcSaStoredProcedureRequestEntity, Double> {

	ProcSaStoredProcedureRequestEntity findByClaimOnBoardNoAndIsActiveTrue(String claimOnboardNo);

	
}
