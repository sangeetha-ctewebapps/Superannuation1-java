package com.lic.epgs.quotation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.quotation.entity.QuotationMemberTempEntity;

@Repository
public interface QuotationMemberTempRepo extends JpaRepository<QuotationMemberTempEntity, Long> {

//	for quotation 
	
	QuotationMemberTempEntity findByMemberIdAndQuotationId(Long quotationMemberId, Integer quotationId);
	List<QuotationMemberTempEntity> findAllByQuotationIdAndIsActiveTrueOrderByMemberIdDesc(Integer quotationId);
	
//
	
}
