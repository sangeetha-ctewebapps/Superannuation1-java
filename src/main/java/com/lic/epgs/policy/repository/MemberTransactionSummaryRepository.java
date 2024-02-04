package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lic.epgs.policy.entity.MemberTransactionSummaryEntity;

@Repository
public interface MemberTransactionSummaryRepository extends JpaRepository<MemberTransactionSummaryEntity, Long> {

	List<MemberTransactionSummaryEntity> findByPolicyIdAndIsActiveTrueOrderByFinancialYearAsc(String code);

	MemberTransactionSummaryEntity findByPolicyIdAndMemberIdAndIsActiveTrueOrderByFinancialYearDesc(Long policyId,
			String memberId);

	MemberTransactionSummaryEntity findTopByMemberIdOrderByFinancialYearDesc(String code);

	MemberTransactionSummaryEntity findTopByPolicyIdAndMemberIdAndIsActiveTrueOrderByFinancialYearDesc(Long policyId,
			String memberId);

	MemberTransactionSummaryEntity findTopByPolicyIdAndMemberIdAndFinancialYearAndIsActiveTrueOrderByFinancialYearDesc(
			Long policyId, String memberId, String financialYr);

	MemberTransactionSummaryEntity findTopByPolicyIdAndMemberIdAndQuarterAndFinancialYearAndIsActiveTrueOrderByFinancialYearDesc(
			Long policyId, String memberId, Integer quarterMonth, String financialYrByDateString);

	MemberTransactionSummaryEntity findTopByPolicyIdAndMemberIdAndIsExitTrueAndIsActiveTrueOrderByFinancialYearDesc(
			Long policyId, String memberId);

	List<MemberTransactionSummaryEntity> findByPolicyIdAndIsActiveTrueOrderByFinancialYearAsc(Long policyId);

	MemberTransactionSummaryEntity findTopByPolicyIdAndIsActiveTrueOrderByFinancialYearDesc(Long policyId);

	List<MemberTransactionSummaryEntity> findByPolicyIdAndMemberIdAndIsActiveTrueOrderByFinancialYearAsc(Long policyId,
			String memberId);

	/***
	 * @notes Fetch By Policy Number/LIC ID and Financial Year/Quarter depends on
	 *        Variant
	 */
	List<MemberTransactionSummaryEntity> findByPolicyIdAndFinancialYearAndIsActiveTrueOrderByCreatedOnDesc(
			Long policyId, String financialYear);

	MemberTransactionSummaryEntity findTopByPolicyIdAndFinancialYearAndIsActiveTrueOrderByCreatedOnDesc(Long policyId,
			String financialYear);

	MemberTransactionSummaryEntity findTopByPolicyIdAndQuarterAndFinancialYearAndIsActiveTrueOrderByIdDesc(
			Long policyId, Integer quarter, String financialYear);

	List<MemberTransactionSummaryEntity> findByPolicyIdAndMemberIdAndFinancialYearAndIsActiveTrueOrderByCreatedOnDesc(
			Long policyId, String memberId, String financialYear);

//	MemberTransactionSummaryEntity findTopByPolicyIdAndMemberIdAndFinancialYearAndIsActiveTrueOrderByIdDesc(
//			Long policyId, String memberId, String financialYear);

	List<MemberTransactionSummaryEntity> findByFinancialYearAndIsExitTrueAndIsActiveTrueOrderByFinancialYearAsc(
			String financialYrByDate);

	MemberTransactionSummaryEntity findByPolicyIdAndMemberIdAndFinancialYearAndIsActiveTrueOrderByFinancialYearDesc(
			Long policyId, String licId, String financialYrByDt);

	MemberTransactionSummaryEntity findTopByPolicyIdAndMemberIdAndFinancialYearAndQuarterAndIsActiveTrueOrderByFinancialYearDesc(
			Long policyId, String licId, String financialYear, Integer frequency);
	
//	@Modifying
//	@Transactional
//	@Query("update MemberTransactionSummaryEntity cont set cont.stmtId =:stmtId where cont.id =:id")
//	void updateMemberTransactionSummaryFundStmtId(@Param("id") Long id, @Param("stmtId") Long stmtId);

}
