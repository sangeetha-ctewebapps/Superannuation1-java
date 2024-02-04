/**
 * 
 */
package com.lic.epgs.adjustmentcontribution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionBatchEntity;

/**
 * @author Karthick M
 *
 */

@Repository
public interface AdjustmentContributionBatchRepository extends JpaRepository<AdjustmentContributionBatchEntity, Long>{

	AdjustmentContributionBatchEntity findByBatchIdAndIsActiveTrue(Long batchId);
	List<AdjustmentContributionBatchEntity> findAllByAdjustmentContributionIdAndIsActiveTrue(Long adjustmentId);

}
