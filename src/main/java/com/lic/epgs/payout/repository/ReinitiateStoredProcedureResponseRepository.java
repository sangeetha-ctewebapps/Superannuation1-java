package com.lic.epgs.payout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.entity.ReinitiateStoredProcedureResponseEntity;

@Repository
public interface ReinitiateStoredProcedureResponseRepository extends JpaRepository<ReinitiateStoredProcedureResponseEntity, Long> {

}
