package com.lic.epgs.policy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.PolicyTransactionEntriesTempEntity;

@Repository
public interface PolicyTransactionEntriesTempRepository extends JpaRepository<PolicyTransactionEntriesTempEntity, Long> {
	

}
