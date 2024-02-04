package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.ZeroAccountTempEntity;

@Repository
public interface ZeroAccountTempRepository extends JpaRepository<ZeroAccountTempEntity, Long> {

	Optional<ZeroAccountTempEntity> findByIsActiveTrueAndPolicyId(Long policyId);

	ZeroAccountTempEntity findByPolicyIdAndIsActiveTrue(Long policyId);
}
