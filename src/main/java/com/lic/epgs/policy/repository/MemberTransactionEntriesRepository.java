package com.lic.epgs.policy.repository;
import java.util.List;

/**
 * @author pradeepramesh
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.MemberTransactionEntriesEntity;

@Repository
public interface MemberTransactionEntriesRepository extends JpaRepository<MemberTransactionEntriesEntity, Long> {
	MemberTransactionEntriesEntity findTopByPolicyIdAndLicIdAndFinancialYearAndIsActiveTrueOrderByTransactionDateDesc(
			Long policyId, String memberId, String financialYrByDateString);

	@Query("SELECT sum(e.employeeContribution),sum(e.employerContribution),sum(e.voluntaryContribution),sum(e.totalContribution) from  MemberTransactionEntriesEntity e where e.licId = :licId and e.policyId=:policyId  and e.isActive=:isActive")
	Object sumOfMemberContribution(String licId, Long policyId, Boolean isActive);

	@Query("SELECT sum(e.employeeContribution),sum(e.employerContribution),sum(e.voluntaryContribution),sum(e.totalContribution) from  MemberTransactionEntriesEntity e where e.policyId=:policyId  and e.isActive=:isActive")
	Object sumOfMemberContributionByPolicyId(Long policyId, Boolean isActive);

	MemberTransactionEntriesEntity findTopByPolicyIdAndLicIdAndQuarterAndFinancialYearAndIsActiveTrueOrderByTransactionDateDescCreatedOnDesc(
			Long policyId, String licId, Integer quarterByStrDate, String financialYrByDateString);

	MemberTransactionEntriesEntity findTopByPolicyIdAndLicIdAndFinancialYearAndIsActiveTrueOrderByTransactionDateDescCreatedOnDesc(
			Long policyId, String licId, String financialYrByDateString);


	/***
	 * @notes for V2
	 */
	@Query("SELECT sum(e.employeeContribution),sum(e.employerContribution),sum(e.voluntaryContribution),sum(e.totalContribution) from  MemberTransactionEntriesEntity e where e.licId = :licId and e.policyId=:policyId and e.quarter=:quarter and e.financialYear=:financialYear   and e.isActive=:isActive")
	Object sumOfMemberTransactionEntries(String licId, Long policyId, Integer quarter, String financialYear,
			Boolean isActive);

	/**
	 * @notes For V1 and V3
	 **/

	@Query("SELECT sum(e.employeeContribution),sum(e.employerContribution),sum(e.voluntaryContribution),sum(e.totalContribution) from  MemberTransactionEntriesEntity e where e.licId = :licId and e.policyId=:policyId and e.financialYear=:financialYear   and e.isActive=:isActive")
	Object sumOfMemberTransactionEntries(String licId, Long policyId, String financialYear, Boolean isActive);

	List<MemberTransactionEntriesEntity> findByPolicyId(Long policyId);
}
