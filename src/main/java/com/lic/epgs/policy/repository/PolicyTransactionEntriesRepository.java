package com.lic.epgs.policy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.PolicyTransactionEntriesEntity;

@Repository
public interface PolicyTransactionEntriesRepository extends JpaRepository<PolicyTransactionEntriesEntity, Long> {

//	PolicyTransactionEntriesEntity findTopByPolicyPolicyIdAndAndFinancialYearAndIsActiveTrueOrderByTransactionDateDesc(
//			Long policyId, String financialYear);

//	@Query("SELECT sum(e.employeeContribution),sum(e.employerContribution),sum(e.voluntaryContribution),sum(e.totalContribution) from  PolicyTransactionEntriesEntity e INNER JOIN e.policy ee where ee.policyId=:policyId  and e.isActive=:isActive")
//	Object sumOfPolicyContribution(Long policyId, Boolean isActive);

	PolicyTransactionEntriesEntity findTopByPolicyIdAndQuarterAndFinancialYearAndIsActiveTrueOrderByTransactionDateDescCreatedOnDesc(
			Long policyId, Integer quarterByStrDate, String financialYrByDateString);

	PolicyTransactionEntriesEntity findTopByPolicyIdAndAndFinancialYearAndIsActiveTrueOrderByTransactionDateDescCreatedOnDesc(
			Long policyId, String financialYrByDateString);

	/**
	 * For V1 and V3
	 **/
	@Query("SELECT sum(e.employeeContribution),sum(e.employerContribution),sum(e.voluntaryContribution),sum(e.totalContribution) from  PolicyTransactionEntriesEntity e where e.policyId=:policyId and e.financialYear=:financialYear and e.isActive=:isActive")
	Object sumOfPolicyTransactionEntries(Long policyId, String financialYear, Boolean isActive);

	/**
	 * For V2
	 **/
	@Query("SELECT sum(e.employeeContribution),sum(e.employerContribution),sum(e.voluntaryContribution),sum(e.totalContribution) from  PolicyTransactionEntriesEntity e where e.policyId=:policyId and e.quarter=:quarter and e.financialYear=:financialYear and e.isActive=:isActive")
	Object sumOfPolicyTransactionEntries(Long policyId, Integer quarter, String financialYear, Boolean isActive);

}
