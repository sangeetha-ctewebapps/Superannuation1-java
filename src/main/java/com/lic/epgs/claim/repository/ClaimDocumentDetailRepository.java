package com.lic.epgs.claim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.entity.ClaimDocumentDetail;

@Repository
public interface ClaimDocumentDetailRepository
		extends JpaRepository<ClaimDocumentDetail, Long>, JpaSpecificationExecutor<ClaimDocumentDetail> {

	ClaimDocumentDetail findByDocumentId(Long documentId);

	List<ClaimDocumentDetail> findByClaimNoAndIsDeleted(String claimNo, Boolean false1);

}
