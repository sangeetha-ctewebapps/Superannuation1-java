package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.MemberMasterTempEntity;



@Repository
public interface MemberMasterTempRepository extends JpaRepository<MemberMasterTempEntity, Long>{

	List<MemberMasterTempEntity> findAllByPolicyIdAndIsActiveTrue(Long policyId);

	@Query(value = "SELECT P_MBR_T_SEQ.nextval FROM dual", nativeQuery = true)
	public Long getMemberSeq();

	MemberMasterTempEntity findByPolicyIdAndLicIdAndMemberStatus(Long tempPolicyId, String string, String active);

}
