package com.lic.epgs.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.lic.epgs.claim.entity.ProcSaStoredProcedureResponseEntity;


@Repository
public interface ProcSaStoredProcedureResponseRepository  extends JpaRepository<ProcSaStoredProcedureResponseEntity, Double>{

//	ProcSaStoredProcedureResponseEntity findByPayoutIdAndProcSaSprId(Long payoutId,Long procSaSprId);
//
//	ProcSaStoredProcedureResponseEntity findByPayoutNo(String payoutNo);

	List<ProcSaStoredProcedureResponseEntity> findByClaimOnBoardNoAndIsActiveTrue(String claimOnboardNo);

}
