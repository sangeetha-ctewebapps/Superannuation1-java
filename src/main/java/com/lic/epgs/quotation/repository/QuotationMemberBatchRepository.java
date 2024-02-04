package com.lic.epgs.quotation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.quotation.entity.QuotationMemberBatchEntity;


@Repository
public interface QuotationMemberBatchRepository extends JpaRepository<QuotationMemberBatchEntity, Long> {

	QuotationMemberBatchEntity findByBatchIdAndIsActiveTrue(Long batchId);
	List<QuotationMemberBatchEntity> findAllByOrderByBatchIdDesc();

}
