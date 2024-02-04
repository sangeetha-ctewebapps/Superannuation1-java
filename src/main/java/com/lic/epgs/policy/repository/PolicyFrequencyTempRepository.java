package com.lic.epgs.policy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lic.epgs.policy.entity.PolicyFrequencyDetailsTempEntity;

public interface PolicyFrequencyTempRepository extends JpaRepository<PolicyFrequencyDetailsTempEntity, Long> {


	List<PolicyFrequencyDetailsTempEntity> findAllByPolicyIdAndStatusAndIsActiveTrue(Long policyId, String string);

	PolicyFrequencyDetailsTempEntity findByPolicyIdAndIsActiveTrue(Long policyId);


	List<PolicyFrequencyDetailsTempEntity> findAllByPolicyIdAndIsActiveTrueOrderByFrequencyDatesAsc(Long policyId);

	List<PolicyFrequencyDetailsTempEntity> findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyDatesAsc(Long policyId, String unpaid);

 	 List<PolicyFrequencyDetailsTempEntity> findAllByPolicyIdAndIsActiveTrue(Long policyId);

	List<PolicyFrequencyDetailsTempEntity> findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyDatesDesc(Long policyId,String unpaid);

	List<PolicyFrequencyDetailsTempEntity> findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyIdAsc(Long policyId, String unpaid);

	List<PolicyFrequencyDetailsTempEntity> findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyIdDesc(Long policyId, String unpaid);


}
