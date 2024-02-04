package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.MemberContributionTempEntity;

@Repository
public interface MemberContributionTempRepository extends JpaRepository<MemberContributionTempEntity, Long> {

	List<MemberContributionTempEntity> findByPolicyId(Long policyId);

	@Query("SELECT e from MemberContributionTempEntity e where  e.policyConId =:policyConId and (e.txnEntryStatus=:txnEntryStatus or e.txnEntryStatus is null) and e.isActive=:isActive")
	List<MemberContributionTempEntity> getMemberContributionByPolicyConId(@Param("policyConId") Long policyConId,
			@Param("txnEntryStatus") boolean txnEntryStatus, @Param("isActive") Boolean isActive);

	@Query("SELECT e from MemberContributionTempEntity e where   e.memberConId IN (:memberConId) and (e.txnEntryStatus=:txnEntryStatus or e.txnEntryStatus is null) and e.isActive=:isActive")
	List<MemberContributionTempEntity> getMemberContributionByMemberConIds(@Param("memberConId") Set<Long> memberConId,
			@Param("txnEntryStatus") boolean txnEntryStatus, @Param("isActive") Boolean isActive);

	Set<MemberContributionTempEntity> findByPolicyIdAndAdjustmentContributionIdAndIsActiveTrue(Long tempPolicyId,
			Long adjustmentContributionId);

	Set<MemberContributionTempEntity> findByPolicyIdAndRegularContributionIdAndIsActiveTrue(Long tempPolicyId,
			Long regularContributionId);

//	BigDecimal findByPolicyConIdAndPolicyIdAndVersionNoAndIsActiveTrue(Long policyConId, Long tempPolicyId, int i);

//	SELECT CLOSING_BALANCE FROM MEMBER_CONTRIBUTION_TEMP mct WHERE mct.LIC_ID = :licId  AND  mct.POLICY_CON_ID =:policyConId AND  mct.POLICY_ID=:tempPolicyId AND  mct.VERSION_NO =:versionNo 

	@Query(value = "SELECT CLOSING_BALANCE FROM MEMBER_CONTRIBUTION_TEMP mct WHERE mct.LIC_ID = :licId AND  mct.POLICY_ID=:tempPolicyId AND  mct.VERSION_NO =:versionNo", nativeQuery = true)
	BigDecimal findClosingBalanceByPolicyConIdAndPolicyIdAndVersionNoAndIsActiveTrue(String licId, Long tempPolicyId,
			int versionNo);

	@Query(value = "SELECT P_MBR_CO_M_SEQ.nextval FROM dual", nativeQuery = true)
	public Long getMemContributionTempSeq();
}
