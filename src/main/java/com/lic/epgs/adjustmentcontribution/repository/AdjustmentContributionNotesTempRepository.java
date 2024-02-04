package com.lic.epgs.adjustmentcontribution.repository;

/**
 * @author pradeepramesh
 *
 */

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionNotesTempEntity;

public interface AdjustmentContributionNotesTempRepository extends JpaRepository<AdjustmentContributionNotesTempEntity, Long>,
JpaSpecificationExecutor<AdjustmentContributionNotesTempEntity> {

	List<AdjustmentContributionNotesTempEntity> findAllByAdjustmentContributionId(Long adjustmentContributionId);

	
	

}