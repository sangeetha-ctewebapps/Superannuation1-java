package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonNoteEntity;

@Repository
public interface CommonNoteRepository extends JpaRepository<CommonNoteEntity, Long> {

	List<CommonNoteEntity> findByMergeIdAndIsActiveTrue(Long mergeId);
	
	List<CommonNoteEntity> findAllBySurrenderIdAndIsActiveTrue(Long surrenderId);

}
