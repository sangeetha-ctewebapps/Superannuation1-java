/**
 * 
 */
package com.lic.epgs.adjustmentcontribution.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionBulkErrorEntity;

/**
 * @author Karthick M
 *
 */

@Repository
public interface AdjustmentContributionBulkErrorRepository extends JpaRepository<AdjustmentContributionBulkErrorEntity, Long>{

	List<AdjustmentContributionBulkErrorEntity> findAllByBatchIdAndIsActiveTrue(Long batchId);

}
