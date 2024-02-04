package com.lic.epgs.policy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.PolicyTransactionSummaryTempEntity;

@Repository
public interface PolicyTransactionSummaryTempRepository extends JpaRepository<PolicyTransactionSummaryTempEntity, Long> {

}
