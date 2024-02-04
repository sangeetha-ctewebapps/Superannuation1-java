package com.lic.epgs.regularadjustmentcontribution.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionTempEntity;

/**
 * @author pradeepramesh
 *
 */
@Repository
public interface RegularAdjustmentContributionTempRepository extends JpaRepository< RegularAdjustmentContributionTempEntity, Long>,
		JpaSpecificationExecutor< RegularAdjustmentContributionTempEntity> {

	Optional<RegularAdjustmentContributionTempEntity> findByRegularContributionId(Long regularContributionId);

	RegularAdjustmentContributionTempEntity findByRegularContributionIdAndIsActiveTrue(Long regularContributionId);


	Optional<RegularAdjustmentContributionTempEntity> findByPolicyIdAndRegularContributionIdAndIsActiveTrue(Long policyId,
			Long regularContributionId);

	RegularAdjustmentContributionTempEntity findByRegularContributionIdAndPolicyIdAndIsActiveTrue(
			Long regularContributionId, Long policyId);

	Optional<RegularAdjustmentContributionTempEntity> findByPolicyIdAndRegularContributionId(Long policyId,
			Long regularContributionId);

//	RegularAdjustmentContributionTempEntity findByTempPolicyIdAndRegularContributionStatusInAndIsActiveTrue(
//			Long policyId, List<String> adjustmentCheckStatus);

	RegularAdjustmentContributionTempEntity findByPolicyNumberAndRegularContributionStatusInAndIsActiveTrue(
			String policyNumber, List<String> adjustmentCheckStatus);

	@Query(value = "SELECT * FROM REG_CONTRIBUTION_TEMP WHERE POLICY_ID=:policyId ", nativeQuery = true)
	List<RegularAdjustmentContributionTempEntity> findByPolicyIdAndIsActiveTrue(Long policyId);


}
