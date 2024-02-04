package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.MemberContributionEntity;

@Repository
public interface MemberContributionRepository extends JpaRepository<MemberContributionEntity, Long> {

	List<MemberContributionEntity> findByPolicyId(Long policyId);

	List<MemberContributionEntity> findByPolicyConIdAndPolicyIdAndLicIdAndFinancialYearAndIsActiveTrueOrderByVersionNoDesc(
			Long contributionId, Long policyId, String licId, String fincialyearN);

	/***
	 * @notes for V2
	 */
	/**
	 * @Query("SELECT
	 * sum(e.employeeContribution),sum(e.employerContribution),sum(e.voluntaryContribution),sum(e.totalContribution)
	 * from MemberContributionEntity e where e.licId = :licId and
	 * e.policyId=:policyId and e.quarter=:quarter and
	 * e.financialYear=:financialYear and e.isActive=:isActive" ) Object
	 * sumOfMemberContribution(String licId, Long policyId, Integer quarter, String
	 * financialYear, Boolean isActive);
	 */

	/**
	 * @notes For V1 and V3
	 **/

	@Query("SELECT sum(e.employeeContribution),sum(e.employerContribution),sum(e.voluntaryContribution),sum(e.totalContribution) from  MemberContributionEntity e where e.memberId = :memberId and e.policyId=:policyId and e.financialYear=:financialYear   and e.isActive=:isActive")
	Object sumOfMemberContribution(Long memberId, Long policyId, String financialYear, Boolean isActive);

	@Query(value = "WITH\r\n"
			+ "ACTIVE_MEMBERS AS\r\n"
			+ "(\r\n"
			+ "	SELECT\r\n"
			+ "		PM.POLICY_ID\r\n"
			+ "		,PM.POLICY_NUMBER\r\n"
			+ "		,PM.VARIANT\r\n"
			+ "		,PM.POLICY_STATUS\r\n"
			+ "		,MM.MEMBER_ID\r\n"
			+ "		,MM.LIC_ID\r\n"
			+ "		,MM.MEMBER_STATUS\r\n"
			+ "	FROM\r\n"
			+ "		POLICY_MASTER PM,\r\n"
			+ "		MEMBER_MASTER MM\r\n"
			+ "	WHERE \r\n"
			+ "		PM.POLICY_ID = MM.POLICY_ID (+) AND\r\n"
			+ "		PM.POLICY_ID = ?1 AND MM.LIC_ID = ?7 AND \r\n"
			+ "		MM.MEMBER_STATUS (+) = 'Active'\r\n"
			+ "),\r\n"
			+ "MEMBER_OB AS\r\n"
			+ "(\r\n"
			+ "	SELECT\r\n"
			+ "		'-' AS \"CONTRIBUTION NUMBER\"\r\n"
			+ "		,1 TRAN_ORDER\r\n"
			+ "		,TRUNC(MTE.TRANSACTION_DATE) AS \"DATE\"\r\n"
			+ "		,MTE.ENTRY_TYPE AS \"TYPE\"\r\n"
			+ "		,0 AS EMPLOYEE_CONTRIBUTION\r\n"
			+ "		,0 AS EMPLOYER_CONTRIBUTION\r\n"
			+ "		,0 AS VOLUNTARY_CONTRIBUTION\r\n"
			+ "		,DECODE(COALESCE(MTE.TOTAL_CONTRIBUTION,0),0,MTE.OPENING_BALANCE) AS TOTAL_CONTRIBUTION\r\n"
			+ "		,DECODE(COALESCE(MTE.TOTAL_CONTRIBUTION,0),0,MTE.OPENING_BALANCE) AS ACTUAL_AMOUNT\r\n"
			+ "	FROM\r\n"
			+ "		MEMBER_TRANSACTION_ENTRIES MTE,\r\n"
			+ "		ACTIVE_MEMBERS AM\r\n"
			+ "	WHERE \r\n"
			+ "		MTE.POLICY_ID = AM.POLICY_ID AND\r\n"
			+ "		MTE.MEMBER_ID = AM.MEMBER_ID AND\r\n"
			+ "		MTE.ENTRY_TYPE = 'OB' AND\r\n"
			+ "		MTE.FINANCIAL_YEAR = ?2 AND\r\n"
			+ "		MTE.STATEMENT_FREQUENCY = CASE WHEN AM.VARIANT IN (?5,?6) THEN ?4 ELSE 0 END\r\n"
			+ "),\r\n"
			+ "MEM_CONTRIBUTIONS AS\r\n"
			+ "(\r\n"
			+ "	SELECT \r\n"
			+ "		TO_CHAR(MC.POLICY_CON_ID) AS \"CONTRIBUTION NUMBER\"\r\n"
			+ "		,2 TRAN_ORDER\r\n"
			+ "		,MC.CONTRIBUTION_DATE AS \"DATE\"\r\n"
			+ "		,MC.CONTRIBUTION_TYPE AS \"TYPE\"\r\n"
			+ "		,SUM(EMPLOYEE_CONTRIBUTION)\r\n"
			+ "		,SUM(EMPLOYER_CONTRIBUTION)\r\n"
			+ "		,SUM(VOLUNTARY_CONTRIBUTION)\r\n"
			+ "		,SUM(TOTAL_CONTRIBUTION)\r\n"
			+ "		,SUM(TOTAL_CONTRIBUTION) AS ACTUAL_AMOUNT\r\n"
			+ "	FROM\r\n"
			+ "		MEMBER_CONTRIBUTION MC,\r\n"
			+ "		ACTIVE_MEMBERS AM\r\n"
			+ "	WHERE \r\n"
			+ "		AM.MEMBER_ID = MC.MEMBER_ID (+) AND\r\n"
			+ "		MC.FINANCIAL_YEAR (+) = ?2 AND\r\n"
			+ "		MC.QUARTER (+) = CASE WHEN AM.VARIANT IN (?5,?6) THEN ?3 ELSE 'Q0' END\r\n"
			+ "	GROUP BY\r\n"
			+ "		TO_CHAR(MC.POLICY_CON_ID)\r\n"
			+ "		,MC.CONTRIBUTION_DATE\r\n"
			+ "		,MC.CONTRIBUTION_TYPE\r\n"
			+ "	ORDER BY\r\n"
			+ "		TO_CHAR(MC.POLICY_CON_ID)\r\n"
			+ "),\r\n"
			+ "MEM_TRANSACTIONS AS\r\n"
			+ "( \r\n"
			+ "	SELECT \r\n"
			+ "		AM.POLICY_ID\r\n"
			+ "        ,2 TRAN_ORDER\r\n"
			+ "		,MTE.FINANCIAL_YEAR\r\n"
			+ "		,MTE.TRANSACTION_DATE AS \"DATE\"\r\n"
			+ "		,MTE.ENTRY_TYPE AS \"TYPE\"\r\n"
			+ "        ,MTE.TRANSATION_TYPE AS TRANSACTION_TYPE\r\n"
			+ "		,COALESCE(MTE.EMPLOYEE_CONTRIBUTION,0) AS EMPLOYEE_CONTRIBUTION\r\n"
			+ "		,COALESCE(MTE.EMPLOYER_CONTRIBUTION,0) AS EMPLOYER_CONTRIBUTION\r\n"
			+ "		,COALESCE(MTE.VOLUNTARY_CONTRIBUTION,0) AS VOLUNTARY_CONTRIBUTION\r\n"
			+ "		,COALESCE(MTE.TOTAL_CONTRIBUTION,0) AS TOTAL_CONTRIBUTION\r\n"
			+ "	FROM\r\n"
			+ "		MEMBER_TRANSACTION_ENTRIES MTE, ACTIVE_MEMBERS AM\r\n"
			+ "	WHERE \r\n"
			+ "		AM.POLICY_ID = MTE.POLICY_ID AND\r\n"
			+ "        AM.MEMBER_ID = MTE.MEMBER_ID AND\r\n"
			+ "        MTE.TRANSATION_TYPE = 'DEBIT' AND\r\n"
			+ "        MTE.ENTRY_TYPE <> 'POF' AND\r\n"
			+ "		MTE.FINANCIAL_YEAR = ?2 AND\r\n"
			+ "		MTE.STATEMENT_FREQUENCY = CASE WHEN AM.VARIANT IN (?5,?6) THEN ?4 ELSE 0 END\r\n"
			+ "	ORDER BY\r\n"
			+ "		MTE.TRANSACTION_DATE\r\n"
			+ ")\r\n"
			+ "SELECT\r\n"
			+ "	ROWNUM ROWNO\r\n"
			+ "	,TC.\"CONTRIBUTION NUMBER\"\r\n"
			+ "    ,TC.\"DATE\"\r\n"
			+ "    ,TC.\"TYPE\"\r\n"
			+ "    ,TC.EMPLOYEE_CONTRIBUTION\r\n"
			+ "    ,TC.EMPLOYER_CONTRIBUTION\r\n"
			+ "    ,TC.VOLUNTARY_CONTRIBUTION\r\n"
			+ "    ,TC.TOTAL_CONTRIBUTION\r\n"
			+ "    ,SUM(TC.ACTUAL_AMOUNT) OVER (ORDER BY TC.TRAN_ORDER,TC.\"DATE\") AS \"CLOSING BALANCE\"\r\n"
			+ "FROM\r\n"
			+ "	(\r\n"
			+ "		SELECT\r\n"
			+ "			*\r\n"
			+ "		FROM\r\n"
			+ "			MEMBER_OB MOB\r\n"
			+ "		UNION\r\n"
			+ "		SELECT\r\n"
			+ "			*\r\n"
			+ "		FROM\r\n"
			+ "			MEM_CONTRIBUTIONS MC\r\n"
			+ "		UNION\r\n"
			+ "		SELECT\r\n"
			+ "			'-' AS \"CONTRIBUTION NUMBER\"\r\n"
			+ "			,MT.TRAN_ORDER\r\n"
			+ "			,MT.\"DATE\"\r\n"
			+ "			,MT.\"TYPE\"\r\n"
			+ "			,MT.EMPLOYEE_CONTRIBUTION\r\n"
			+ "			,MT.EMPLOYER_CONTRIBUTION\r\n"
			+ "			,MT.VOLUNTARY_CONTRIBUTION\r\n"
			+ "			,MT.TOTAL_CONTRIBUTION\r\n"
			+ "			,CASE\r\n"
			+ "				WHEN MT.TRANSACTION_TYPE = 'DEBIT' THEN MT.TOTAL_CONTRIBUTION * -1 ELSE MT.TOTAL_CONTRIBUTION\r\n"
			+ "			END AS ACTUAL_AMOUNT\r\n"
			+ "		FROM\r\n"
			+ "			MEM_TRANSACTIONS MT\r\n"
			+ "	) TC\r\n"
			+ "	ORDER BY TRAN_ORDER,TC.\"DATE\" ", nativeQuery = true)
	List<Object[]> getMemberContributionDetails(Long policyId, String financialyear, String quarter, Integer frequency,
			String dc, String db, Integer licId);

	@Query(value = "SELECT * FROM (SELECT MC.MEMBER_CON_ID,	MC.ADJ_CON_ID, MC.CLOSING_BALANCE, MC.CONTRIBUTION_DATE, MC.CONTRIBUTION_TYPE, MC.CREATED_BY, MC.CREATED_ON, MC.EMPLOYEE_CONTRIBUTION, MC.EMPLOYER_CONTRIBUTION, MC.FINANCIAL_YEAR, MC.IS_ACTIVE, MC.IS_DEPOSIT, MC.LIC_ID,	MC.MEMBER_ID, MC.MODIFIED_BY, MC.MODIFIED_ON, MC.OPENING_BALANCE, MC.POLICY_CON_ID,	MC.POLICY_ID, MC.REG_CON_ID, MC.TEMP_MEMBER_ID, MC.TEMP_POLICY_ID, MC.TOTAL_CONTRIBUTION, MC.TOTAL_INTREST_ACCURED, MC.TXN_ENTRY_STATUS, MC.VERSION_NO, MC.VOLUNTARY_CONTRIBUTION,	MC.ZERO_ACCOUNT_ENTRIES, MC.IS_ZERO_ACCOUNT, MC.QUARTER, ROW_NUMBER() OVER (PARTITION BY PC. POLICY_ID ORDER BY	CREATED_ON DESC, CONTRIBUTION_DATE DESC, VERSION_NO DESC) ROWNO	FROM MEMBER_CONTRIBUTION MC	WHERE PC.POLICY_ID =?1 AND PC.FINANCIAL_YEAR =?2 AND PC.QUARTER =?3) WHERE ROWNO = 1", nativeQuery = true)
	MemberContributionEntity findMemberDetailsAndIsActive(Long policyId, String financialYear, String quarter);
	
	@Query(value = "SELECT MC.POLICY_CON_ID AS \"CONTRIBUTION NUMBER\",TRUNC(MC.CONTRIBUTION_DATE) AS \"DATE\",MC.LIC_ID AS \"LIC ID\",COALESCE(MC.EMPLOYER_CONTRIBUTION,0) AS \"EMPLOYER CONTRIBUTION\",COALESCE(MC.EMPLOYEE_CONTRIBUTION,0) AS \"EMPLOYEE CONTRIBUTION\",COALESCE(MC.VOLUNTARY_CONTRIBUTION,0) AS \"VOLUNTARY CONTRIBUTION\",COALESCE(MC.TOTAL_CONTRIBUTION,0) AS \"TOTAL CONTRIBUTION\"FROM MEMBER_CONTRIBUTION MC WHERE MC.POLICY_CON_ID=?1 ORDER BY TO_NUMBER(MC.LIC_ID);", nativeQuery = true)
	MemberContributionEntity getInvidualContributionDetails(Long policyConId);

	@Query(value = "SELECT MC.POLICY_CON_ID AS \"CONTRIBUTION NUMBER\",TRUNC(MC.CONTRIBUTION_DATE) AS \"DATE\",MC.LIC_ID AS \"LIC ID\",COALESCE(MC.EMPLOYER_CONTRIBUTION,0) AS \"EMPLOYER CONTRIBUTION\",COALESCE(MC.EMPLOYEE_CONTRIBUTION,0) AS \"EMPLOYEE CONTRIBUTION\",COALESCE(MC.VOLUNTARY_CONTRIBUTION,0) AS \"VOLUNTARY CONTRIBUTION\",COALESCE(MC.TOTAL_CONTRIBUTION,0) AS \"TOTAL CONTRIBUTION\"FROM MEMBER_CONTRIBUTION MC WHERE  MC.POLICY_ID=?1 AND MC.POLICY_CON_ID=?2 ORDER BY TO_NUMBER(MC.LIC_ID)", nativeQuery = true)
	List<Object[]> getPolicyContributionSummary(Long policyId, Long adjConId);

//	@Query(value = "SELECT DISTINCT CAST(CONTRIBUTION_DATE  AS DATE) AS CONTRIBUTIONDATE, CAST(CREATED_ON  AS DATE) AS CONTRIBUTION_DATE  FROM MEMBER_CONTRIBUTION WHERE FINANCIAL_YEAR = :finicialYear AND CONTRIBUTION_TYPE = 'R'AND POLICY_ID = :policyId ORDER BY CONTRIBUTIONDATE ASC", nativeQuery = true)
//	List<Object[]> findByDistinctDate(Long policyId, String finicialYear);
//
//	@Query(value = "SELECT  LIC_ID,TRUNC(CONTRIBUTION_DATE) AS \"DUE_ON\",TOTAL_CONTRIBUTION FROM MEMBER_CONTRIBUTION WHERE FINANCIAL_YEAR = :finicialYear AND CONTRIBUTION_TYPE = 'R'	AND POLICY_ID = :policyId ORDER BY LIC_ID,DUE_ON", nativeQuery = true)
//	List<Object[]> findByContributionDataByFinicialYear(Long policyId, String finicialYear);

	@Query(value = "SELECT DISTINCT CAST(CONTRIBUTION_DATE  AS DATE) AS CONTRIBUTIONDATE, CAST(CREATED_ON  AS DATE) AS CONTRIBUTION_DATE  FROM MEMBER_CONTRIBUTION WHERE FINANCIAL_YEAR = :finicialYear AND POLICY_ID = :policyId ORDER BY CONTRIBUTIONDATE ASC", nativeQuery = true)
	List<Object[]> findByDistinctDate(Long policyId, String finicialYear);

	@Query(value = "SELECT  LIC_ID,TRUNC(CONTRIBUTION_DATE) AS \"DUE_ON\",TOTAL_CONTRIBUTION FROM MEMBER_CONTRIBUTION WHERE FINANCIAL_YEAR = :finicialYear AND POLICY_ID = :policyId ORDER BY LIC_ID,DUE_ON", nativeQuery = true)
	List<Object[]> findByContributionDataByFinicialYear(Long policyId, String finicialYear);

	
//	List<MemberContributionEntity> getMemberContributionsByPolicyIdsAndLicIdsAndQuarter(Long policyId,
//			List<String> partitionIds, boolean b, String financialYear, boolean c, String quraterVal);
//
//	List<MemberContributionEntity> getMemberContributionsByPolicyIdsAndLicIds(Long policyId, List<String> partitionIds,
//			boolean b, String financialYear, boolean c);
	
	@Query("SELECT e from MemberContributionEntity e where e.policyId IN (:policyIds) and e.licId IN (:licIds) and (e.txnEntryStatus=:txnEntryStatus or e.txnEntryStatus is null) and e.financialYear=:financialYear  and e.isActive=:isActive order by e.contributionDate Asc  ,e.createdOn Asc, e.memberConId Asc")
	List<MemberContributionEntity> getMemberContributionsByPolicyIdsAndLicIds(
			@Param("policyIds") Long policyIds,
			@Param("licIds") List<String> licIds,
			@Param("txnEntryStatus") boolean txnEntryStatus,
			@Param("financialYear") String financialYear,
			@Param("isActive") Boolean isActive);

	@Query("SELECT e from MemberContributionEntity e where e.policyId IN (:policyIds) and e.licId IN (:licIds) and (e.txnEntryStatus=:txnEntryStatus or e.txnEntryStatus is null) and e.financialYear=:financialYear  and e.isActive=:isActive  and e.quarter=:quarter  order by e.contributionDate Asc  ,e.createdOn Asc, e.memberConId Asc")
	List<MemberContributionEntity> getMemberContributionsByPolicyIdsAndLicIdsAndQuarter(
			@Param("policyIds") Long policyIds,
			@Param("licIds") List<String> licIds,
			@Param("txnEntryStatus") boolean txnEntryStatus,
			@Param("financialYear") String financialYear,
			@Param("isActive") Boolean isActive,
			@Param("quarter") String quarter);
	

   @Query(value ="select count(member_status) as member_statuscnt from\r\n"
			+ "(SELECT mm.MEMBER_ID, ac.COLLECTION_NO,count(mm.member_status)member_status  FROM member_master mm \r\n"
			+ "JOIN POLICY_DEPOSIT AC ON (AC.POLICY_ID=mm.POLICY_ID)\r\n"
			+ "where AC.COLLECTION_NO=:collectionNo and mm.member_status='Active' \r\n"
			+ "group by mm.MEMBER_ID, ac.COLLECTION_NO) ",nativeQuery = true)
	String memberCount(String collectionNo);

}
