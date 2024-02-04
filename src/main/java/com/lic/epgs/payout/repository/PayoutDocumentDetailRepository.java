package com.lic.epgs.payout.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.entity.PayoutDocumentDetail;

@Repository
public interface PayoutDocumentDetailRepository
		extends JpaRepository<PayoutDocumentDetail, Long>, JpaSpecificationExecutor<PayoutDocumentDetail> {

	PayoutDocumentDetail findByDocumentId(Long documentId);

	List<PayoutDocumentDetail> findByPayoutNoAndIsDeleted(String payoutNo, Boolean false1);

}
