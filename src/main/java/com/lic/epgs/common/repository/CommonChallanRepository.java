package com.lic.epgs.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonChallanEntity;

@EnableJpaRepositories
@Repository
public interface CommonChallanRepository extends JpaRepository<CommonChallanEntity, Integer> {

	CommonChallanEntity findByQuotationNoAndProposalNumberAndIsActiveTrue(String quotationNo, String proposalNumber);

}
