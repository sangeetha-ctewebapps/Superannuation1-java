/**
 * 
 */
package com.lic.epgs.regularadjustmentcontribution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionBatchEntity;

/**
 * @author Karthick M
 *
 */

@Repository
public interface RegularAdjustmentContributionBatchRepository extends JpaRepository<RegularAdjustmentContributionBatchEntity, Long>{

	RegularAdjustmentContributionBatchEntity findByBatchIdAndIsActiveTrue(Long batchId);
	List<RegularAdjustmentContributionBatchEntity> findAllByRegularContributionIdAndIsActiveTrue(Long regularContributionId);

}
