package com.lic.epgs.policyservicing.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.policyservicing.common.entity.PolicyServiceEntity;
import com.lic.epgs.policyservicing.policylvl.entity.merger.PolicyLevelServiceEntity;

public interface PolicyServicingCommonRepository  extends JpaRepository<PolicyServiceEntity, Long> {

	@Query(value = " SELECT MODULE_SEQ_FUNC(?1) FROM DUAL ", nativeQuery = true)
	String getSequence(String type);

	Optional<PolicyServiceEntity> findByPolicyId(Long policyId);

	Optional<PolicyServiceEntity> findByPolicyIdAndServiceId(Long policyId, Long serviceId);

	Optional<PolicyServiceEntity> findByPolicyIdAndIsActiveTrue(Long policyId);

//	Optional<PolicyServiceEntity> findByPolicyIdAndServiceStatusAndIsActiveTrue(Long policyId, String string);
	
	List<PolicyServiceEntity> findByPolicyIdAndServiceStatusAndIsActiveTrue(Long policyId, String string);

	PolicyServiceEntity findByPolicyIdAndServiceIdAndIsActiveTrue(Long policyId, Long serviceId);

	List<PolicyServiceEntity> findByPolicyIdAndServiceStatusAndIsActiveTrueOrderByServiceIdAsc(Long policyId,String string);
    
	Optional<PolicyLevelServiceEntity>  findByServiceIdAndIsActiveTrue(Long serviceId);

}
