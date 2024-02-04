package com.lic.epgs.adjustmentcontribution.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionNotesEntity;

public interface AdjustmentContributionNotesRepository  extends JpaRepository<AdjustmentContributionNotesEntity, Long>,
JpaSpecificationExecutor<AdjustmentContributionNotesEntity>  {

	List<AdjustmentContributionNotesEntity> findAllByAdjustmentContributionId(Long adjustmentContributionId);

}
