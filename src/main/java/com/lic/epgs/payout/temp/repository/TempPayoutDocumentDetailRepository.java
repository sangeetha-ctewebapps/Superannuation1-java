package com.lic.epgs.payout.temp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.temp.entity.TempPayoutDocumentDetail;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;

@Repository
public interface TempPayoutDocumentDetailRepository
		extends JpaRepository<TempPayoutDocumentDetail, Long>, JpaSpecificationExecutor<TempPayoutDocumentDetail> {

	TempPayoutDocumentDetail findByDocumentId(Long documentId);

	List<TempPayoutDocumentDetail> findByPayoutNoAndIsDeleted(String payoutNo, Boolean false1);
	
	List<TempPayoutDocumentDetail> findByPayoutAndIsDeleted( TempPayoutEntity tempPayoutEntity, Boolean false1);

}
