package com.lic.epgs.regularadjustmentcontribution.repository;
import java.util.List;

/**
 * @author pradeepramesh
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionEntity;

@Repository
public interface RegularAdjustmentContributionRepository extends JpaRepository< RegularAdjustmentContributionEntity, Long>, JpaSpecificationExecutor< RegularAdjustmentContributionEntity> {

	RegularAdjustmentContributionEntity findByRegularContributionId(Long regularContributionId);

	RegularAdjustmentContributionEntity findByRegularContributionIdAndIsActiveTrue(Long regularContributionId);

	RegularAdjustmentContributionEntity findByPolicyNumberAndRegularContributionStatusInAndIsActiveTrue(
			String policyNumber, List<String> regularCheckStatus);
	
	List<RegularAdjustmentContributionEntity> findAllByRegularContributionNumberAndIsActiveTrue(String regularContributionNumber);

}
