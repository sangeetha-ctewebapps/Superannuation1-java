package com.lic.epgs.policyservicing.policylvl.repository.conversion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policyservicing.policylvl.entity.conversion.PolicyLevelConversionTempEntity;

@Repository
public interface PolicyLevelConversionTempRepository extends JpaRepository<PolicyLevelConversionTempEntity, Long> {

	PolicyLevelConversionTempEntity findByConversionIdAndIsActiveTrue(Long conversionId);

	PolicyLevelConversionTempEntity findByServiceIdAndIsActiveTrue(Long serviceId);

	List<PolicyLevelConversionTempEntity> findAllByConversionStatusInAndUnitCodeAndIsActiveTrueOrderByModifiedOnDesc(
			List<Integer> inProgressPolicyConversionMaker, String unitCode);

	PolicyLevelConversionTempEntity findByPrevPolicyNoAndConversionStatusInAndIsActiveTrue(String prevPolicyNo,
			List<Integer> inProgressPolicyConversionChecker);
	
}
