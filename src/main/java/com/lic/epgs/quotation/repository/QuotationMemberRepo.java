
package com.lic.epgs.quotation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.quotation.entity.QuotationMemberEntity;

@Repository
public interface QuotationMemberRepo extends JpaRepository<QuotationMemberEntity, Long> {
	
//	for quotation 
	QuotationMemberEntity findByMemberIdAndQuotationId(Long quotationMemberId, Integer quotationId);
//	

}
