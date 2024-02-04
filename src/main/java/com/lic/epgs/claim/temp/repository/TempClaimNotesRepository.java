package com.lic.epgs.claim.temp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.temp.entity.TempClaimNotesEntity;

@Repository
public interface TempClaimNotesRepository
		extends JpaRepository<TempClaimNotesEntity, Long>, JpaSpecificationExecutor<TempClaimNotesEntity> {

	List<TempClaimNotesEntity> findByClaimNo(String claimNo);

}
