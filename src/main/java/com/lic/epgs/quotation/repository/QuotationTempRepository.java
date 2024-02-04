package com.lic.epgs.quotation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.quotation.entity.QuotationTempEntity;

public interface QuotationTempRepository extends JpaRepository<QuotationTempEntity, Integer> {
	
//	for Quotation 
	List<QuotationTempEntity> findByStatus(String quotationStatus);
	List<QuotationTempEntity> findAllByStatusInAndIsActiveTrueAndUnitCodeOrderByQuotationIdDesc(List<Integer> makerInprogress, String unitCode);
	List<QuotationTempEntity> findAllByproposalNumberAndIsActiveTrueAndUnitCodeOrderByQuotationIdDesc(String proposalNumber, String unitCode);
	QuotationTempEntity findByIsActiveTrueAndQuotationNoAndStatusInAndUnitCode(String quotationNo,List<Integer> inprogressActuary, String unitOffice);
	QuotationTempEntity findByQuotationIdAndIsActiveTrue(Integer quotationId);
//	
	
	
	
}
