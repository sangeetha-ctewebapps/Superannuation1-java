package com.lic.epgs.policy.repository;
import java.math.BigDecimal;
/**
 * @author pradeepramesh
 *
 */
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lic.epgs.policy.entity.PolicyContributionEntity;

@Repository
public interface PolicyContributionRepository extends JpaRepository<PolicyContributionEntity, Long> {

	Optional<PolicyContributionEntity> findByPolicyId(Long policyId);

	List<PolicyContributionEntity> findByPolicyIdAndIsActiveTrue(Long policyId);

	PolicyContributionEntity findByPolicyIdAndFinancialYearAndIsActiveTrueOrderByVersionNoDesc(Long policyId,
			String fincialyearN);

	@Query(value = "SELECT pc.CONTRIBUTION_DATE FROM POLICY_CONTRIBUTION pc WHERE pc.POLICY_ID=:policyId and pc.FINANCIAL_YEAR=:fincialyearN  ORDER BY pc.VERSION_NO DESC", nativeQuery = true)
	List<Date> findContributionDateByPolicyId(Long policyId, String fincialyearN);

	PolicyContributionEntity findTopByPolicyIdAndFinancialYearAndIsActiveTrueOrderByContributionIdDesc(Long policyId,
			String financialYr);

//	/**
//	 * For V1 and V3
//	 **/
//	@Query("SELECT sum(e.employee_Contribution),sum(e.employer_Contribution),sum(e.voluntary_Contribution),sum(e.total_Contribution) from POLICY_CONTRIBUTION e where e.policy_Id=:policyId and e.financial_Year=:financialYear and e.is_Active=:isActive")
//	Object sumOfPolicyContribution(Long policyId, String financialYear, Boolean isActive);
//
//	/**
//	 * For V2
//	 **/
//	  @Query("SELECT sum(e.employee_Contribution),sum(e.employer_Contribution),sum(e.voluntary_Contribution),sum(e.total_Contribution) from POLICY_CONTRIBUTION e where e.policy_Id=:policyId and e.quarter=:quarter and e.financial_Year=:financialYear and e.is_Active=:isActive")
//	  Object sumOfPolicyContribution(Long policyId, String quarter, String financialYear, Boolean isActive);
	 
	
	
	/**
	 * For V1 and V3
	 **/
	@Query("SELECT sum(e.employeeContribution),sum(e.employerContribution),sum(e.voluntaryContribution),sum(e.totalContribution) from  PolicyContributionEntity e where e.policyId=:policyId and e.financialYear=:financialYear and e.isActive=:isActive")
	Object sumOfPolicyContribution(Long policyId, String financialYear, Boolean isActive);

	/**
	 * For V2
	 **/
	
	  @Query("SELECT sum(e.employeeContribution),sum(e.employerContribution),sum(e.voluntaryContribution),sum(e.totalContribution) from PolicyContributionEntity e where e.policyId=:policyId and e.quarter=:quarter and e.financialYear=:financialYear and e.isActive=:isActive" ) 
	  Object sumOfPolicyContribution(Long policyId, String quarter, String financialYear, Boolean isActive);
	 

	//Logesh .D
	
	  @Query(value = "WITH\r\n"
		  		+ "POLICIES AS\r\n"
		  		+ "(\r\n"
		  		+ "	SELECT\r\n"
		  		+ "		PM.POLICY_ID,\r\n"
		  		+ "		PM.POLICY_NUMBER,\r\n"
		  		+ "		PM.VARIANT,\r\n"
		  		+ "		PM.POLICY_STATUS\r\n"
		  		+ "	FROM\r\n"
		  		+ "		POLICY_MASTER PM\r\n"
		  		+ "	WHERE\r\n"
		  		+ "		PM.POLICY_ID = ?1 \r\n"
		  		+ "),\r\n"
		  		+ "ACTIVE_MEMBERS AS\r\n"
		  		+ "(\r\n"
		  		+ "	SELECT\r\n"
		  		+ "		PC.POLICY_ID,\r\n"
		  		+ "		MM.MEMBER_ID,\r\n"
		  		+ "		MM.LIC_ID,\r\n"
		  		+ "		MM.MEMBER_STATUS,\r\n"
		  		+ "		PC.VARIANT\r\n"
		  		+ "	FROM\r\n"
		  		+ "		POLICIES PC,\r\n"
		  		+ "		MEMBER_MASTER MM\r\n"
		  		+ "	WHERE \r\n"
		  		+ "		PC.POLICY_ID = MM.POLICY_ID (+)\r\n"
		  		+ "),\r\n"
		  		+ "POLICY_OB AS\r\n"
		  		+ "(\r\n"
		  		+ "	SELECT\r\n"
		  		+ "		'-' AS \"CONTRIBUTION NUMBER\"\r\n"
		  		+ "		,1 TRAN_ORDER\r\n"
		  		+ "		,TRUNC(PTE.TRANSACTION_DATE) AS TRANSACTION_DATE\r\n"
		  		+ "		,NULL AS ADJUSTED_FOR\r\n"
		  		+ "		,PTE.ENTRY_TYPE AS \"TYPE\"\r\n"
		  		+ "		,0 AS EMPLOYEE_CONTRIBUTION\r\n"
		  		+ "		,0 AS EMPLOYER_CONTRIBUTION\r\n"
		  		+ "		,0 AS VOLUNTARY_CONTRIBUTION\r\n"
		  		+ "		,DECODE(COALESCE(PTE.TOTAL_CONTRIBUTION,0),0,COALESCE(PTE.OPENING_BALANCE,0),COALESCE(PTE.TOTAL_CONTRIBUTION,0)) AS TOTAL_CONTRIBUTION\r\n"
		  		+ "		,DECODE(COALESCE(PTE.TOTAL_CONTRIBUTION,0),0,COALESCE(PTE.OPENING_BALANCE,0),COALESCE(PTE.TOTAL_CONTRIBUTION,0)) AS ACTUAL_AMOUNT\r\n"
		  		+ "	FROM\r\n"
		  		+ "		POLICY_TRANSACTION_ENTRIES PTE,\r\n"
		  		+ "		POLICIES PC\r\n"
		  		+ "	WHERE \r\n"
		  		+ "		PTE.POLICY_ID = PC.POLICY_ID AND\r\n"
		  		+ "		PTE.ENTRY_TYPE = 'OB' AND\r\n"
		  		+ "		PTE.FINANCIAL_YEAR = ?2 AND\r\n"
		  		+ "		PTE.STATEMENT_FREQUENCY = CASE WHEN PC.VARIANT IN (?5,?6) THEN ?4 ELSE 0 END\r\n"
		  		+ "),\r\n"
		  		+ "POL_CONTRIBUTIONS AS\r\n"
		  		+ "(\r\n"
		  		+ "	SELECT\r\n"
		  		+ "		CONTRIBUTION_ID,\r\n"
		  		+ "		2 TRAN_ORDER,\r\n"
		  		+ "		TRUNC(TRANSACTION_DATE) AS TRANSACTION_DATE,\r\n"
		  		+ "		TRUNC(ADJUSTED_FOR) AS ADJUSTED_FOR,\r\n"
		  		+ "		\"TYPE\",\r\n"
		  		+ "		SUM(EMPLOYEE_CONTRIBUTION) AS EMPLOYEE_CONTRIBUTION,\r\n"
		  		+ "		SUM(EMPLOYER_CONTRIBUTION) AS EMPLOYER_CONTRIBUTION,\r\n"
		  		+ "		SUM(VOLUNTARY_CONTRIBUTION) AS VOLUNTARY_CONTRIBUTION,\r\n"
		  		+ "		SUM(TOTAL_CONTRIBUTION) AS TOTAL_CONTRIBUTION\r\n"
		  		+ "	FROM\r\n"
		  		+ "		(\r\n"
		  		+ "			SELECT \r\n"
		  		+ "				POL.POLICY_ID,\r\n"
		  		+ "				TO_CHAR(PC.CONTRIBUTION_ID) AS CONTRIBUTION_ID,\r\n"
		  		+ "				PC.FINANCIAL_YEAR,\r\n"
		  		+ "				PC.CONTRIBUTION_DATE AS TRANSACTION_DATE,\r\n"
		  		+ "				PC.ADJUSTMENT_DUE_DATE AS ADJUSTED_FOR,\r\n"
		  		+ "				PC.CONTRIBUTION_TYPE AS \"TYPE\",\r\n"
		  		+ "				COALESCE(PC.EMPLOYEE_CONTRIBUTION,0) AS EMPLOYEE_CONTRIBUTION,\r\n"
		  		+ "				COALESCE(PC.EMPLOYER_CONTRIBUTION,0) AS EMPLOYER_CONTRIBUTION,\r\n"
		  		+ "				COALESCE(PC.VOLUNTARY_CONTRIBUTION,0) AS VOLUNTARY_CONTRIBUTION,\r\n"
		  		+ "				COALESCE(PC.TOTAL_CONTRIBUTION,0) AS TOTAL_CONTRIBUTION\r\n"
		  		+ "			FROM\r\n"
		  		+ "				POLICY_CONTRIBUTION PC, POLICIES POL\r\n"
		  		+ "			WHERE \r\n"
		  		+ "				POL.POLICY_ID = PC.POLICY_ID AND\r\n"
		  		+ "				PC.FINANCIAL_YEAR = ?2  AND\r\n"
		  		+ "				PC.QUARTER = CASE WHEN POL.VARIANT IN (?5,?6) THEN ?3 ELSE 'Q0' END\r\n"
		  		+ "			ORDER BY\r\n"
		  		+ "				PC.CONTRIBUTION_ID\r\n"
		  		+ "		)\r\n"
		  		+ "	GROUP BY\r\n"
		  		+ "		CONTRIBUTION_ID\r\n"
		  		+ "		,TRUNC(TRANSACTION_DATE)\r\n"
		  		+ "		,TRUNC(ADJUSTED_FOR)\r\n"
		  		+ "		,\"TYPE\"\r\n"
		  		+ "),\r\n"
		  		+ "MEM_CONTRIBUTIONS AS\r\n"
		  		+ "(\r\n"
		  		+ "	SELECT\r\n"
		  		+ "		POLICY_CON_ID\r\n"
		  		+ "		,2 TRAN_ORDER\r\n"
		  		+ "		,TRUNC(TRANSACTION_DATE) AS TRANSACTION_DATE\r\n"
		  		+ "		,TRUNC(ADJUSTED_FOR) AS ADJUSTED_FOR\r\n"
		  		+ "		,\"TYPE\"\r\n"
		  		+ "		,SUM(EMPLOYEE_CONTRIBUTION) AS EMPLOYEE_CONTRIBUTION\r\n"
		  		+ "		,SUM(EMPLOYER_CONTRIBUTION) AS EMPLOYER_CONTRIBUTION\r\n"
		  		+ "		,SUM(VOLUNTARY_CONTRIBUTION) AS VOLUNTARY_CONTRIBUTION\r\n"
		  		+ "		,SUM(TOTAL_CONTRIBUTION) AS TOTAL_CONTRIBUTION\r\n"
		  		+ "	FROM\r\n"
		  		+ "		(\r\n"
		  		+ "			SELECT \r\n"
		  		+ "				AM.POLICY_ID\r\n"
		  		+ "				,TO_CHAR(MC.POLICY_CON_ID) AS POLICY_CON_ID\r\n"
		  		+ "				,MC.LIC_ID\r\n"
		  		+ "				,MC.FINANCIAL_YEAR\r\n"
		  		+ "				,MC.CONTRIBUTION_DATE AS TRANSACTION_DATE\r\n"
		  		+ "				,MC.ADJUSTMENT_DUE_DATE AS ADJUSTED_FOR\r\n"
		  		+ "				,MC.CONTRIBUTION_TYPE AS \"TYPE\"\r\n"
		  		+ "				,COALESCE(MC.EMPLOYEE_CONTRIBUTION,0) AS EMPLOYEE_CONTRIBUTION\r\n"
		  		+ "				,COALESCE(MC.EMPLOYER_CONTRIBUTION,0) AS EMPLOYER_CONTRIBUTION\r\n"
		  		+ "				,COALESCE(MC.VOLUNTARY_CONTRIBUTION,0) AS VOLUNTARY_CONTRIBUTION\r\n"
		  		+ "				,COALESCE(MC.TOTAL_CONTRIBUTION,0) AS TOTAL_CONTRIBUTION\r\n"
		  		+ "			FROM\r\n"
		  		+ "				MEMBER_CONTRIBUTION MC,\r\n"
		  		+ "				ACTIVE_MEMBERS AM\r\n"
		  		+ "			WHERE \r\n"
		  		+ "				AM.MEMBER_ID = MC.MEMBER_ID(+) AND\r\n"
		  		+ "				MC.FINANCIAL_YEAR (+) = ?2 AND\r\n"
		  		+ "				MC.QUARTER (+) = CASE WHEN AM.VARIANT IN (?5,?6) THEN ?3 ELSE 'Q0' END\r\n"
		  		+ "			ORDER BY\r\n"
		  		+ "				MC.POLICY_CON_ID\r\n"
		  		+ "		)\r\n"
		  		+ "	GROUP BY \r\n"
		  		+ "		POLICY_CON_ID\r\n"
		  		+ "		,TRUNC(TRANSACTION_DATE)\r\n"
		  		+ "		,TRUNC(ADJUSTED_FOR)\r\n"
		  		+ "		,\"TYPE\"\r\n"
		  		+ "),\r\n"
		  		+ "POL_TRANSACTIONS AS\r\n"
		  		+ "( \r\n"
		  		+ "	SELECT \r\n"
		  		+ "		POL.POLICY_ID\r\n"
		  		+ "		,2 TRAN_ORDER\r\n"
		  		+ "		,PTE.FINANCIAL_YEAR\r\n"
		  		+ "		,PTE.TRANSACTION_DATE AS TRANSACTION_DATE\r\n"
		  		+ "		,NULL AS ADJUSTED_FOR\r\n"
		  		+ "		,PTE.ENTRY_TYPE AS \"TYPE\"\r\n"
		  		+ "		,PTE.TRANSATION_TYPE AS TRANSACTION_TYPE\r\n"
		  		+ "		,COALESCE(PTE.EMPLOYEE_CONTRIBUTION,0) AS EMPLOYEE_CONTRIBUTION\r\n"
		  		+ "		,COALESCE(PTE.EMPLOYER_CONTRIBUTION,0) AS EMPLOYER_CONTRIBUTION\r\n"
		  		+ "		,COALESCE(PTE.VOLUNTARY_CONTRIBUTION,0) AS VOLUNTARY_CONTRIBUTION\r\n"
		  		+ "		,COALESCE(PTE.TOTAL_CONTRIBUTION,0) AS TOTAL_CONTRIBUTION\r\n"
		  		+ "	FROM\r\n"
		  		+ "		POLICY_TRANSACTION_ENTRIES PTE, POLICIES POL \r\n"
		  		+ "	WHERE \r\n"
		  		+ "		POL.POLICY_ID = PTE.POLICY_ID AND\r\n"
		  		+ "		PTE.TRANSATION_TYPE = 'DEBIT' AND\r\n"
		  		+ "		PTE.FINANCIAL_YEAR = ?2 AND\r\n"
		  		+ "		PTE.STATEMENT_FREQUENCY = CASE WHEN POL.VARIANT IN (?5,?6) THEN ?4 ELSE 0 END\r\n"
		  		+ "	ORDER BY\r\n"
		  		+ "		PTE.TRANSACTION_DATE\r\n"
		  		+ ")\r\n"
		  		+ "SELECT\r\n"
		  		+ "	ROWNUM ROWNO\r\n"
		  		+ "	,TC.\"CONTRIBUTION NUMBER\"\r\n"
		  		+ "	,TC.TRANSACTION_DATE\r\n"
		  		+ "	,TC.ADJUSTED_FOR\r\n"
		  		+ "    ,TC.\"TYPE\"\r\n"
		  		+ "	,TC.EMPLOYEE_CONTRIBUTION\r\n"
		  		+ "	,TC.EMPLOYER_CONTRIBUTION\r\n"
		  		+ "	,TC.VOLUNTARY_CONTRIBUTION\r\n"
		  		+ "	,TC.TOTAL_CONTRIBUTION\r\n"
		  		+ "	,SUM(TC.ACTUAL_AMOUNT) OVER (ORDER BY TC.TRAN_ORDER,TC.TRANSACTION_DATE,ROWNUM) AS \"CLOSING BALANCE\"\r\n"
		  		+ "FROM\r\n"
		  		+ "	(\r\n"
		  		+ "		SELECT\r\n"
		  		+ "			*\r\n"
		  		+ "		FROM\r\n"
		  		+ "			POLICY_OB POB\r\n"
		  		+ "		WHERE\r\n"
		  		+ "			TRANSACTION_DATE IS NOT NULL\r\n"
		  		+ "		UNION\r\n"
		  		+ "		SELECT\r\n"
		  		+ "			COALESCE(MC.POLICY_CON_ID,PC.CONTRIBUTION_ID)\r\n"
		  		+ "			,COALESCE(MC.TRAN_ORDER,PC.TRAN_ORDER) AS TRAN_ORDER\r\n"
		  		+ "			,COALESCE(MC.TRANSACTION_DATE,PC.TRANSACTION_DATE)\r\n"
		  		+ "			,COALESCE(MC.ADJUSTED_FOR,PC.ADJUSTED_FOR)\r\n"
		  		+ "			,COALESCE(MC.\"TYPE\",PC.\"TYPE\")\r\n"
		  		+ "			,COALESCE(MC.EMPLOYEE_CONTRIBUTION,PC.EMPLOYEE_CONTRIBUTION)\r\n"
		  		+ "			,COALESCE(MC.EMPLOYER_CONTRIBUTION,PC.EMPLOYER_CONTRIBUTION)\r\n"
		  		+ "			,COALESCE(MC.VOLUNTARY_CONTRIBUTION,PC.VOLUNTARY_CONTRIBUTION)\r\n"
		  		+ "			,COALESCE(MC.TOTAL_CONTRIBUTION,PC.TOTAL_CONTRIBUTION)\r\n"
		  		+ "			,COALESCE(MC.TOTAL_CONTRIBUTION,PC.TOTAL_CONTRIBUTION) AS ACTUAL_AMOUNT\r\n"
		  		+ "		FROM\r\n"
		  		+ "			POL_CONTRIBUTIONS PC,\r\n"
		  		+ "			MEM_CONTRIBUTIONS MC\r\n"
		  		+ "		WHERE\r\n"
		  		+ "			PC.CONTRIBUTION_ID = MC.POLICY_CON_ID (+)\r\n"
		  		+ "			AND PC.TRANSACTION_DATE IS NOT NULL\r\n"
		  		+ "		UNION\r\n"
		  		+ "		SELECT\r\n"
		  		+ "			'-' AS \"CONTRIBUTION NUMBER\"\r\n"
		  		+ "			,PT.TRAN_ORDER\r\n"
		  		+ "			,PT.TRANSACTION_DATE\r\n"
		  		+ "			,PT.ADJUSTED_FOR\r\n"
		  		+ "			,PT.\"TYPE\"\r\n"
		  		+ "			,PT.EMPLOYEE_CONTRIBUTION\r\n"
		  		+ "			,PT.EMPLOYER_CONTRIBUTION\r\n"
		  		+ "			,PT.VOLUNTARY_CONTRIBUTION\r\n"
		  		+ "			,PT.TOTAL_CONTRIBUTION\r\n"
		  		+ "			,CASE\r\n"
		  		+ "				WHEN PT.TRANSACTION_TYPE = 'DEBIT' AND PT.TOTAL_CONTRIBUTION >= 0 THEN PT.TOTAL_CONTRIBUTION * -1\r\n"
		  		+ "				WHEN PT.TRANSACTION_TYPE = 'DEBIT' AND PT.TOTAL_CONTRIBUTION < 0 THEN PT.TOTAL_CONTRIBUTION	\r\n"
		  		+ "			END AS ACTUAL_AMOUNT\r\n"
		  		+ "		FROM\r\n"
		  		+ "			POL_TRANSACTIONS PT\r\n"
		  		+ "		WHERE\r\n"
		  		+ "			PT.TRANSACTION_DATE IS NOT NULL\r\n"
		  		+ "	) TC\r\n"
		  		+ "ORDER BY\r\n"
		  		+ "	TRAN_ORDER\r\n"
		  		+ "	,TC.TRANSACTION_DATE", nativeQuery = true)
			List<Object[]> getPolicyContributionDetails(Long policyId, String financialyear, String quarter, Integer frequency,
					String dc, String db);

			
			
//	//old PolicyContribution Fileds
//	@Query(value = "SELECT * FROM (SELECT PC.CONTRIBUTION_ID, PC.ADJ_CON_ID, PC.CLOSING_BALANCE, PC.CONT_REFERENCE_NO, PC.CONTRIBUTION_DATE, PC.CONTRIBUTION_TYPE, PC.CREATED_BY, PC. CREATED_ON, PC.EMPLOYEE_CONTRIBUTION, PC.EMPLOYER_CONTRIBUTION, PC.FINANCIAL_YEAR, PC.IS_ACTIVE, PC.IS_DEPOSIT, PC.MODIFIED_BY, PC.MODIFIED_ON, PC.OPENING_BALANCE, PC .POLICY_ID, PC.REG_CON_ID, PC.TEMP_POLICY_ID, PC.TOTAL_CONTRIBUTION, PC.TXN_ENTRY_STATUS, PC.VERSION_NO, PC.VOLUNTARY_CONTRIBUTION, PC.ZERO_ACCOUNT_ENTRIES, PC. AMT_TO_BE_ADJUSTED, PC.FIRST_PREMIUM, PC.RENEWAL_PREMIUM, PC.SINGLE_PREMIUM_FIRSTYR, PC.SUBSEQUENT_SINGLE_PREMIUM, PC.QUARTER,ROW_NUMBER() OVER (PARTITION BY PC. POLICY_ID ORDER BY CREATED_ON DESC,CONTRIBUTION_DATE DESC,VERSION_NO DESC) ROWNO FROM POLICY_CONTRIBUTION PC WHERE PC.POLICY_ID =?1 AND PC.FINANCIAL_YEAR =?2 ) WHERE ROWNO=1", nativeQuery = true)
//	Object findPolicyContributionByPolicyIdAndFinancialYr(Long policyId, String financialYear);

	@Query(value = "SELECT * FROM (SELECT PC.CONTRIBUTION_ID, PC.ADJ_CON_ID, PC.CLOSING_BALANCE, PC.CONT_REFERENCE_NO, PC.CONTRIBUTION_DATE, PC.CONTRIBUTION_TYPE, PC.CREATED_BY, PC. CREATED_ON, PC.EMPLOYEE_CONTRIBUTION, PC.EMPLOYER_CONTRIBUTION, PC.FINANCIAL_YEAR, PC.IS_ACTIVE, PC.IS_DEPOSIT, PC.MODIFIED_BY, PC.MODIFIED_ON, PC.OPENING_BALANCE, PC .POLICY_ID, PC.REG_CON_ID, PC.TEMP_POLICY_ID, PC.TOTAL_CONTRIBUTION, PC.TXN_ENTRY_STATUS, PC.VERSION_NO, PC.VOLUNTARY_CONTRIBUTION, PC.ZERO_ACCOUNT_ENTRIES, PC.QUARTER,ROW_NUMBER() OVER (PARTITION BY PC. POLICY_ID ORDER BY CREATED_ON DESC,CONTRIBUTION_DATE DESC,VERSION_NO DESC) ROWNO FROM POLICY_CONTRIBUTION PC WHERE PC.POLICY_ID =?1 AND PC.FINANCIAL_YEAR =?2 ) WHERE ROWNO=1", nativeQuery = true)
	Object findPolicyContributionByPolicyIdAndFinancialYr(Long policyId, String financialYear);

//	List<PolicyContributionEntity> findByPolicyIdAndIsActiveTrueOrderByVersionNoDesc(Long masterPolicyId);
	
	PolicyContributionEntity findByPolicyIdAndIsActiveTrueOrderByVersionNoDesc(Long masterPolicyId);

	
//	PDF DOWNLOAD
	PolicyContributionEntity findByPolicyIdAndAdjustmentContributionId(Long policyId, Long adjustmentContributionId);

	PolicyContributionEntity findByPolicyIdAndRegularContributionId(Long policyId, Long regularContributionId);

	List<PolicyContributionEntity> findByPolicyIdAndFinancialYearAndIsActiveTrueOrderByContributionIdDesc(Long masterPolicyId,
			String financialYear);

	@Query(value = "SELECT SUM(PC.CLOSING_BALANCE) AS TOTAL_CONTRIBUTION_AMOUNT FROM POLICY_CONTRIBUTION PC WHERE PC.POLICY_ID =?1 AND PC.FINANCIAL_YEAR =?2", nativeQuery = true)
	BigDecimal gettotalContribuitonAmountByFinancialYear(Long policyId, String financialyear);

	
	@Query(value = "SELECT SUM(TOTAL_CONTRIBUTION) FROM POLICY_MASTER PM, POLICY_TRANSACTION_ENTRIES PTE WHERE PTE.ENTRY_TYPE = 'CAILM' AND PTE.POLICY_ID = PM.POLICY_ID AND PTE.POLICY_ID =?1 AND PTE.FINANCIAL_YEAR =?2", nativeQuery = true)
//	@Query(value = "SELECT SUM(pfss.TOTAL_DEBIT_AMT) AS CLAIM_AMOUNT FROM POLICY_FUND_STATEMENT_SUMMARY pfss  WHERE pfss.POLICY_ID =?1 AND pfss.FINANCIAL_YEAR =?2", nativeQuery = true)
	BigDecimal getClaimAmountByFinancialYear(Long policyId, String financialyear);

	@Modifying
	@Transactional
	@Query(value = "UPDATE POLICY_MASTER SET STAMP_DUTY=?2 WHERE POLICY_ID=?1", nativeQuery = true)
	void stampDutyUpdate(Long policyId, BigDecimal newstampduty);
	
	@Query(value = "SELECT pc.CONTRIBUTION_DATE FROM POLICY_CONTRIBUTION pc WHERE pc.POLICY_ID=:policyId ORDER BY pc.contribution_date DESC", nativeQuery = true)
	List<Date> findContributionDateByPolicyIdOrderContributionDate(Long policyId);

	PolicyContributionEntity findByPolicyIdAndContributionType(Long policyId, String reportType);
	
	@Query("SELECT e from PolicyContributionEntity e where e.policyId =:policyId and (e.txnEntryStatus=:txnEntryStatus or e.txnEntryStatus is null) and e.financialYear=:financialYear and e.isActive=:isActive order by e.contributionDate Asc  ,e.createdOn Asc, e.contributionId Asc")
	List<PolicyContributionEntity> getPolicyContributionsByPolicyIdAndTxnStatus(@Param("policyId") Long policyId,
			@Param("txnEntryStatus") boolean txnEntryStatus, @Param("financialYear") String financialYear,
			@Param("isActive") Boolean isActive);

	@Query("SELECT e from PolicyContributionEntity e where e.policyId =:policyId and (e.txnEntryStatus=:txnEntryStatus or e.txnEntryStatus is null) and e.financialYear=:financialYear and e.isActive=:isActive and e.quarter=:quarter order by e.contributionDate Asc  ,e.createdOn Asc, e.contributionId Asc")
	List<PolicyContributionEntity> getPolicyContributionsByPolicyIdAndTxnStatusAndQuarter(
			@Param("policyId") Long policyId, @Param("txnEntryStatus") boolean txnEntryStatus,
			@Param("financialYear") String financialYear, @Param("isActive") Boolean isActive,
			@Param("quarter") String quarter);

	@Query(value = " SELECT * FROM (select deposit_id,challan_no,CHEQUE_REALISATION_DATE,collection_date,collection_no,collection_status from policy_deposit where collection_no in (select cont_reference_no from policy_contribution where CONTRIBUTION_ID=:contributionId) order by  deposit_id desc) WHERE ROWNUM = 1 ", nativeQuery = true)
	Object findDepositByContributionId(@Param("contributionId") Long contributionId);

	@Query(value = "SELECT TO_CHAR(ADJUSTMENT_DUE_DATE,'dd/mm/yyyy') as ADJUSTMENT_DUE_DATE  FROM  POLICY_CONTRIBUTION WHERE POLICY_ID=:policyId AND CONTRIBUTION_TYPE IN ('R','RA')   AND ADJUSTMENT_DUE_DATE is not null  ORDER BY ADJUSTMENT_DUE_DATE   desc  FETCH FIRST 1 ROW ONLY ", nativeQuery = true)
	String findPolicyId(Long policyId);
	
//	PolicyContributionEntity findByPolicyIdAndFinancialYearAndIsActiveTrueOrderByVersionNoDesc(Long policyId,String calculateFinaYr);

//	@Query(value = "", nativeQuery = true)
//	String getContributionDate(Long policyId);
	
	
	@Query(value = "select cont_reference_no,TO_CHAR(contribution_date,'dd/mm/yyyy')"
			+ " as contribution_date,adjustment_due_date,total_contribution "
			+ " from policy_contribution WHERE cont_reference_no=?1",nativeQuery = true)
	Object findByContReferenceNo(String challanNo );
	
	
}
