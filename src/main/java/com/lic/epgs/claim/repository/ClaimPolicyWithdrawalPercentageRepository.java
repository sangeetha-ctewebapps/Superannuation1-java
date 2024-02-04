package com.lic.epgs.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.claim.entity.ClaimPolicyWithdrawalPercentageEntity;

public interface ClaimPolicyWithdrawalPercentageRepository extends JpaRepository<ClaimPolicyWithdrawalPercentageEntity, Long> {

	ClaimPolicyWithdrawalPercentageEntity findByPolicyIdAndStatusAndIsActiveTrue(Long policyId, String active);

	ClaimPolicyWithdrawalPercentageEntity findByPolicyNoAndStatusAndIsActiveTrue(String masterPolicyNo, String active);

}
