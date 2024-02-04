package com.lic.epgs.policy.repository;
import java.util.List;

/**
 * @author pradeepramesh
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.MemberContributionSummaryEntity;

@Repository
public interface MemberContributionSummaryRepository extends JpaRepository<MemberContributionSummaryEntity, Long> {

	MemberContributionSummaryEntity findByPolicyIdAndLicIdAndIsActiveTrue(Long policyId, String string);

	MemberContributionSummaryEntity findTopByPolicyIdAndMemberIdAndFinancialYearAndIsActiveTrueOrderByMemContSummaryIdDesc(
			Long policyId, Long memberId, String financialYear);

	List<MemberContributionSummaryEntity> findByPolicyIdAndMemberIdAndFinancialYearAndIsActiveTrueOrderByMemContSummaryIdDesc(
			Long policyId, Long memberId, String financialYear);

}
