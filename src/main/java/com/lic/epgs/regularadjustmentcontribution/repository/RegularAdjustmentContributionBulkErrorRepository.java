/**
 * 
 */
package com.lic.epgs.regularadjustmentcontribution.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionBulkErrorEntity;

/**
 * @author Karthick M
 *
 */

@Repository
public interface RegularAdjustmentContributionBulkErrorRepository extends JpaRepository<RegularAdjustmentContributionBulkErrorEntity, Long>{

	List<RegularAdjustmentContributionBulkErrorEntity> findAllByBatchIdAndIsActiveTrue(Long batchId);

}
