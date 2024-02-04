package com.lic.epgs.adjustmentcontribution.repository;

/**
 * @author pradeepramesh
 *
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionTempEntity;

@Repository
public interface AdjustmentContributionTempRepository extends JpaRepository< AdjustmentContributionTempEntity, Long>,JpaSpecificationExecutor< AdjustmentContributionTempEntity> {

	AdjustmentContributionTempEntity findByAdjustmentContributionIdAndPolicyIdAndIsActiveTrue(Long adjustmentContributionId, Long policyId);
	Optional<AdjustmentContributionTempEntity> findByPolicyIdAndAdjustmentContributionIdAndIsActiveTrue(Long policyId,Long adjustmentContributionId);
	AdjustmentContributionTempEntity findByAdjustmentContributionIdAndIsActiveTrue(Long adjustmentContributionId);
	AdjustmentContributionTempEntity findByAdjustmentContributionIdAndIsActiveFalse(Long adjustmentContributionId);
	AdjustmentContributionTempEntity findByPolicyNumberAndAdjustmentContributionStatusInAndIsActiveTrue(String policyNumber, List<String> adjustmentCheckStatus);
	@Query(value = "SELECT * FROM ADJUSTMENT_CONTRIBUTION_TEMP WHERE POLICY_ID=:policyId ", nativeQuery = true)
	List<AdjustmentContributionTempEntity> findByPolicyIdAndIsActiveTrue(Long policyId);

}