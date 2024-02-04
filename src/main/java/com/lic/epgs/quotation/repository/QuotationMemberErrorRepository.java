/**
 * 
 */
package com.lic.epgs.quotation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.quotation.entity.QuotationMemberErrorEntity;

/**
 * @author Muruganandam
 *
 */
public interface QuotationMemberErrorRepository extends JpaRepository<QuotationMemberErrorEntity, Long> {

	List<QuotationMemberErrorEntity> findByQuotationIdAndBatchId(Integer quotationId, Long batchId);
	List<QuotationMemberErrorEntity> findAllByBatchId(Long batchId);

}
