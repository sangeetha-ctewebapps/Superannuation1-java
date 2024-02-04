package com.lic.epgs.claim.temp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.temp.entity.TempClaimDocumentDetail;

@Repository
public interface TempClaimDocumentDetailRepository
		extends JpaRepository<TempClaimDocumentDetail, Long>, JpaSpecificationExecutor<TempClaimDocumentDetail> {

	TempClaimDocumentDetail findByDocumentId(Long documentId);

	List<TempClaimDocumentDetail> findByClaimNoAndIsDeleted(String claimNo, Boolean false1);

}
