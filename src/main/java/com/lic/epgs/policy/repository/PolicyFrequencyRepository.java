package com.lic.epgs.policy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.policy.entity.PolicyFrequencyDetailsEntity;

public interface PolicyFrequencyRepository extends JpaRepository<PolicyFrequencyDetailsEntity, Long> {


	List<PolicyFrequencyDetailsEntity> findAllByPolicyIdAndStatusAndIsActiveTrue(Long policyId, String string);

	PolicyFrequencyDetailsEntity findByPolicyIdAndIsActiveTrue(Long policyId);


	List<PolicyFrequencyDetailsEntity> findAllByPolicyIdAndIsActiveTrueOrderByFrequencyDatesAsc(Long policyId);

	List<PolicyFrequencyDetailsEntity> findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyDatesAsc(Long policyId, String unpaid);

 	 List<PolicyFrequencyDetailsEntity> findAllByPolicyIdAndIsActiveTrue(Long policyId);

	List<PolicyFrequencyDetailsEntity> findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyDatesDesc(Long policyId,String unpaid);

	List<PolicyFrequencyDetailsEntity> findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyIdAsc(Long policyId, String unpaid);

	List<PolicyFrequencyDetailsEntity> findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyIdDesc(Long policyId, String unpaid);


}
