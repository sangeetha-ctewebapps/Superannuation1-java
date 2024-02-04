package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonNoteTempEntity;

@Repository
public interface CommonNotesTempRepository extends JpaRepository<CommonNoteTempEntity, Integer>{

	List<CommonNoteTempEntity> findByMergeIdAndIsActiveTrue(Long mergeId);

	List<CommonNoteTempEntity> findAllBySurrenderId(Long surrenderId);

	List<CommonNoteTempEntity> findByMergeId(Long mergeId);

	List<CommonNoteTempEntity> findByTrnsfrIdAndIsActiveTrue(Long convertStringToLong);

}
