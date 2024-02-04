package com.lic.epgs.payout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.entity.ReinitiateStoredProcedureRequestEntity;

@Repository
public interface ReinitiateStoredProcedureRequestRepository extends JpaRepository<ReinitiateStoredProcedureRequestEntity, Long> {

}
