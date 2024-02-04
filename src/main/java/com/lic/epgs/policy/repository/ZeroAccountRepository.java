package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.ZeroAccountEntity;

@Repository
public interface ZeroAccountRepository extends JpaRepository<ZeroAccountEntity, Long> {

	ZeroAccountEntity findByPolicyIdAndIsActiveTrue(Long policyId);

	Optional<ZeroAccountEntity> findByIsActiveTrueAndPolicyId(Long policyId);

}
