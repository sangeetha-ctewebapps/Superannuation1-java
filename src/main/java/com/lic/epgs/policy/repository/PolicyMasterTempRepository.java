package com.lic.epgs.policy.repository;
import java.util.Date;
/**
 * @author pradeepramesh
 *
 */
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.PolicyMasterTempEntity;

@Repository
public interface PolicyMasterTempRepository extends JpaRepository<PolicyMasterTempEntity, Long>{

	PolicyMasterTempEntity findAllByMphIdAndIsActiveTrue(Long mphId);
	
//	PolicyMasterTempEntity findByPolicyIdAndIsActiveTrue(Long policyId);

	Optional<PolicyMasterTempEntity> findByPolicyIdAndIsActiveTrue(Long policyId);
	
	PolicyMasterTempEntity findByPolicyNumberAndIsActiveTrue(String policyNumber);

	@Query(value = "SELECT POLICY_ID FROM policy_master_temp WHERE MASTER_POLICY_ID=:policyId AND IS_ACTIVE =1", nativeQuery = true)
	public Long findPolicyDetailsAndIsActive(Long policyId);
	
	@Query(value = "SELECT POL_T_SEQUENCE.nextval FROM dual", nativeQuery = true)
	public Long getPolicyTempSeq();

//	Date getArdDateByPolicyId(Long policyId);
//
//	Date getCommemcementDateByPolicyId(Long policyId);

	@Query(value = "SELECT ARD FROM POLICY_MASTER_TEMP pm WHERE POLICY_ID =:policyId", nativeQuery = true)
	Date getArdDateByPolicyId(Long policyId);
	
	@Query(value = "SELECT POLICY_COMMENCEMENT_DT FROM POLICY_MASTER_TEMP pm WHERE POLICY_ID =:policyId", nativeQuery = true)
	Date getCommemcementDateByPolicyId(Long policyId);
	
	@Query(value = "SELECT MPH_ID FROM policy_master WHERE POLICY_ID=:policyId AND IS_ACTIVE =1", nativeQuery = true)
	public Long findMphIdDetailsAndIsActive(Long policyId);

	@Query(value = "SELECT pmt.MPH_ID FROM policy_master_temp pmt WHERE pmt.MASTER_POLICY_ID=:policyId AND pmt.IS_ACTIVE =1", nativeQuery = true)
	public Long findMPHDetailsAndIsActive(Long policyId);

}
