package com.lic.epgs.claim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.entity.ClaimNotesEntity;

@Repository
public interface ClaimNotesRepository
		extends JpaRepository<ClaimNotesEntity, Long>, JpaSpecificationExecutor<ClaimNotesEntity> {

	List<ClaimNotesEntity> findByClaimNo(String claimNo);

}
