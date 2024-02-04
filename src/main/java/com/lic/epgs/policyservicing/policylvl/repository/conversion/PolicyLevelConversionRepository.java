package com.lic.epgs.policyservicing.policylvl.repository.conversion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policyservicing.policylvl.entity.conversion.PolicyLevelConversionEntity;

@Repository
public interface PolicyLevelConversionRepository extends JpaRepository<PolicyLevelConversionEntity, Long> {

	List<PolicyLevelConversionEntity> findAllByConversionStatusInAndUnitCodeAndIsActiveTrueOrderByModifiedOnDesc(
			List<Integer> existingDetails, String unitCode);

	PolicyLevelConversionEntity findByPrevPolicyIdAndIsActiveTrue(Long prevPolicyId);

	PolicyLevelConversionEntity findByConversionIdAndIsActiveTrue(Long conversionId);

	PolicyLevelConversionEntity findByNewPolicyNoAndConversionStatusInAndIsActiveTrue(String newPolicyNo,
			List<Integer> existingDetails);

	PolicyLevelConversionEntity findByServiceIdAndIsActiveTrue(Long serviceId);

}
