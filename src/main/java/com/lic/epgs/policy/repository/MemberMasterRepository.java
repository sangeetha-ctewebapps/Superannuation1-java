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

import com.lic.epgs.policy.entity.MemberMasterEntity;

@Repository
public interface MemberMasterRepository extends JpaRepository<MemberMasterEntity, Long> {

	MemberMasterEntity findByLicIdAndPolicyIdAndIsZeroidFalse(String licId, Long policyId);

	MemberMasterEntity findByMemberIdAndPolicyIdAndIsActiveTrueAndIsZeroidFalse(Long memberId, Long policyId);

	// Transfer in and out
	MemberMasterEntity findByMembershipNumberAndPolicyIdAndIsActiveTrueAndIsZeroidFalse(String memberShipId,
			Long policyId);

	@Query(value = "SELECT mm.LIC_ID ,mm.MEMBER_ID,mm.MEMBERSHIP_NUMBER,mm.FIRST_NAME,mm.MEMBER_STATUS FROM MEMBER_MASTER mm JOIN POLICY_MASTER pm ON pm.POLICY_ID  = mm.POLICY_ID "
			+ "WHERE pm.POLICY_NUMBER =:masterPolicyNo AND mm.LIC_ID =:licId  AND mm.IS_ACTIVE=:isActive and pm.unit_id=:unitCode "
			+ "AND EXISTS (SELECT 1 FROM EXCEPTION_CLAIM_POLICIES ECP WHERE ECP.POLICY_ID = PM.POLICY_ID AND ECP.LIC_ID = MM.LIC_ID AND ECP.IS_ACTIVE = 1)"
			, nativeQuery = true)
	List<Object> findByMemberDetails(String masterPolicyNo, String licId, Boolean isActive, String unitCode);

	@Query(value = "SELECT mm.LIC_ID, mm.MEMBER_ID,mm.MEMBERSHIP_NUMBER,mm.FIRST_NAME,mm.MEMBER_STATUS FROM MEMBER_MASTER mm  join Policy_master pm on pm.policy_id=mm.policy_Id "
			+ "WHERE mm.POLICY_ID =:policyId AND mm.IS_ACTIVE=:isActive  and pm.policy_status in (:policyStatus)", nativeQuery = true)
	List<Object> findMemberListByPoicyId(Long policyId, Boolean isActive, List<String> policyStatus);

	@Query(value = "SELECT mm.MEMBERSHIP_NUMBER , mm.FIRST_NAME ,pm.POLICY_NUMBER ,pm.POLICY_STATUS,mm1.MPH_CODE,mm1.MPH_NAME,mm.CATEGORY_NO,mm.DATE_OF_BIRTH ,mm.DATE_OF_JOINING,pm.PRODUCT_ID,mm.MEMBER_ID,mm.LIC_ID,mm.POLICY_ID,pm.VARIANT "
			+ "FROM MEMBER_MASTER mm JOIN POLICY_MASTER pm ON  pm.POLICY_ID  = mm.POLICY_ID "
			+ "JOIN MPH_MASTER mm1   ON  mm1.MPH_ID  = pm.MPH_ID WHERE  mm.MEMBER_ID  =:memberId AND  mm.IS_ACTIVE=:isActive", nativeQuery = true)
	Object findByMemberbyMemberId(Long memberId, Boolean isActive);

	MemberMasterEntity findByMemberIdAndPolicyIdAndAndMemberStatusAndIsActiveTrueAndIsZeroidFalse(String licId,
			Long policyId, String active);

	MemberMasterEntity findByLicIdAndPolicyIdAndAndMemberStatusAndIsActiveTrueAndIsZeroidFalse(String licId,
			Long policyId, String active);

	@Modifying
	@Transactional
	@Query("update MemberMasterEntity mem set mem.memberStatus =:memberStatus where mem.memberId =:memberId")
	void updateMemberStatusByMemberId(@Param("memberStatus") String memberStatus, @Param("memberId") Long memberId);

	@Query(value = "SELECT * FROM MEMBER_MASTER WHERE POLICY_ID=:policyId ", nativeQuery = true)
	List<MemberMasterEntity> findByPolicyIdAndIsActiveTrue(Long policyId);

	@Query(value = "SELECT * FROM MEMBER_MASTER mm  WHERE POLICY_ID =?1 AND LIC_ID =?2 AND IS_ACTIVE =1 AND MEMBER_STATUS ='Active'", nativeQuery = true)
	MemberMasterEntity fetchByPolicyIdAndLicIdAndMemberStatusAndIsActiveTrue(@Param("policyId") Long policyId,
			@Param("policyId") String licId, Boolean true1);

	@Query(value = "SELECT * FROM MEMBER_MASTER mm WHERE POLICY_ID =:policyId	AND IS_ACTIVE = 1 AND MEMBER_STATUS = 'Active' AND LIC_ID IN (:licId) AND NOT EXISTS (SELECT 1 FROM MEMBER_MASTER_TEMP mmt WHERE mmt.MASTER_MEMBER_ID = MM.MEMBER_ID AND mmt.LIC_ID = mm.LIC_ID)", nativeQuery = true)
	List<MemberMasterEntity> findPolicyDetailsAndIsActive(Long policyId, List<String> licId);

	MemberMasterEntity findByLicIdAndPolicyIdAndMemberStatusAndIsActiveTrueAndIsZeroidFalse(String licId, Long policyId,
			String active);

	MemberMasterEntity findByLicIdAndPolicyIdAndIsActiveTrueAndIsZeroidFalse(
			String licId, Long policyId);

	MemberMasterEntity findByPolicyIdAndLicIdAndMemberStatus(Long policyId, String string, String active);

	MemberMasterEntity findByPolicyIdAndLicIdAndMemberStatusAndIsActiveTrue(Long policyId, String licId, String active);
	
	
	
	
	@Query(value = "SELECT mm.MEMBERSHIP_NUMBER , mm.LIC_ID , mm.DATE_OF_BIRTH ,mm.FIRST_NAME || ' ' || mm.MIDDLE_NAME || ' ' || mm.LAST_NAME AS NAME  FROM POLICY_MASTER pm JOIN "
			+ "MEMBER_MASTER mm ON pm.POLICY_ID = mm.POLICY_ID  WHERE pm.POLICY_ID = :policyId AND pm.MPH_ID  = :mphId AND pm.UNIT_ID = :unitCode AND pm.IS_ACTIVE =1 AND "
			+ "mm.IS_ZEROID =0 AND mm.MEMBER_STATUS ='Active' ORDER BY TO_NUMBER(mm.LIC_ID)", nativeQuery = true)
	List<Object[]> findMembersByPolicyIdAndMphIdAndUnitCodeAndIsActiveAndIsZeroId(Long policyId, Long mphId,String unitCode);

	@Query(value = "select count(*) from MEMBER_MASTER mm where UPPER(NVL(mm.LIC_ID,'x')) like UPPER(NVL( :licId, NVL(mm.LIC_ID,'x')))", nativeQuery = true)
	long getAllCaseInitiateDataTableCount(String licId);

	
	@Query(value = "SELECT mm.MEMBERSHIP_NUMBER, mm.LIC_ID ,mm.MEMBER_STATUS ,mm.FIRST_NAME, mm.DATE_OF_BIRTH, mm.CATEGORY_NO "
			+ "FROM MEMBER_MASTER mm  JOIN POLICY_MASTER pm ON (pm.POLICY_ID =mm.POLICY_ID)"
			+ "WHERE (mm.LIC_ID  =:licId or nvl(:licId,'x')='x')"
			+ "AND ( pm.POLICY_ID  =:policyId or nvl(:policyId,0)=0)"
			+ "AND (mm.MEMBERSHIP_NUMBER  =:membershipNumber or nvl(:membershipNumber,'x')='x') "
			+ "and pm.IS_ACTIVE=1 AND mm.IS_ACTIVE = 1", nativeQuery = true)
	List<Object> fetchByLicIdAndPolicyIdAndMembershipNumber(@Param("licId") String licId,
			@Param("policyId") Long policyId, @Param("membershipNumber") String membershipNumber);
	
	
	
	@Query(value ="WITH\r\n"
			+ "POLICIES AS\r\n"
			+ "(\r\n"
			+ "	SELECT\r\n"
			+ "		PM.POLICY_ID\r\n"
			+ "		,PM.POLICY_NUMBER\r\n"
			+ "		,MM.MPH_NAME\r\n"
			+ "		,PD.PRODUCT_CODE\r\n"
			+ "		,MU.DESCRIPTION AS UNIT_NAME\r\n"
			+ "		,MU.ADDRESS1\r\n"
			+ "		,MU.ADDRESS2\r\n"
			+ "		,MU.ADDRESS3\r\n"
			+ "		,MU.ADDRESS4\r\n"
			+ "		,MU.CITY_NAME\r\n"
			+ "		,MU.DISTRICT_NAME\r\n"
			+ "		,MU.STATE_NAME\r\n"
			+ "		,MU.PINCODE\r\n"
			+ "		,'01/04/' || REGEXP_SUBSTR( ?2 ,'[^-]+', 1, 1) AS START_DATE\r\n"
			+ "		,'31/03/' || REGEXP_SUBSTR( ?2 ,'[^-]+', 1, 2) AS END_DATE\r\n"
			+ "		, ?2 AS FINANCIAL_YEAR\r\n"
			+ "	FROM\r\n"
			+ "		POLICY_MASTER PM\r\n"
			+ "		,MPH_MASTER MM\r\n"
			+ "		,LICCUSTOMERCOMMON.PRODUCT_DETAILS PD\r\n"
			+ "		,LICCUSTOMERCOMMON.PRODUCT_VARIANT_DETAILS PVD\r\n"
			+ "		,LICCOMMON.MASTER_UNIT MU\r\n"
			+ "	WHERE\r\n"
			+ "		PM.MPH_ID = MM.MPH_ID AND\r\n"
			+ "		PM.VARIANT = PVD.LEAD_VARIANT_ID AND\r\n"
			+ "		PD.LEAD_PRODUCT_ID = PVD.LEAD_PRODUCT_ID AND\r\n"
			+ "		PM.UNIT_ID = MU.UNIT_CODE AND\r\n"
			+ "		PM.POLICY_ID = ?1 \r\n"
			+ "),\r\n"
			+ "ACTIVE_MEMBERS AS\r\n"
			+ "(\r\n"
			+ "	SELECT\r\n"
			+ "		POL.POLICY_ID\r\n"
			+ "		,MM.MEMBER_ID\r\n"
			+ "		,MM.LIC_ID\r\n"
			+ "	FROM\r\n"
			+ "		POLICIES POL\r\n"
			+ "		,MEMBER_MASTER MM\r\n"
			+ "	WHERE\r\n"
			+ "		POL.POLICY_ID = MM.POLICY_ID AND\r\n"
			+ "		MM.MEMBER_STATUS <> 'Member In Payout'\r\n"
			+ "),\r\n"
			+ "POLICY_OB AS\r\n"
			+ "(\r\n"
			+ "    SELECT\r\n"
			+ "        POL.POLICY_ID\r\n"
			+ "        ,PTE.TRANSACTION_DATE\r\n"
			+ "        ,PTE.ENTRY_TYPE\r\n"
			+ "        ,0 AS EMPLOYEE_CONTRIBUTION\r\n"
			+ "        ,0 AS EMPLOYER_CONTRIBUTION\r\n"
			+ "        ,0 AS VOLUNTARY_CONTRIBUTION\r\n"
			+ "        ,DECODE(COALESCE(PTE.TOTAL_CONTRIBUTION,0),0,COALESCE(PTE.OPENING_BALANCE,0),COALESCE(PTE.TOTAL_CONTRIBUTION,0)) AS OPENING_BALANCE\r\n"
			+ "	FROM\r\n"
			+ "        POLICY_TRANSACTION_ENTRIES PTE\r\n"
			+ "        ,POLICIES POL\r\n"
			+ "	WHERE \r\n"
			+ "        PTE.POLICY_ID = POL.POLICY_ID AND\r\n"
			+ "        PTE.ENTRY_TYPE = 'OB' AND\r\n"
			+ "        PTE.FINANCIAL_YEAR = POL.FINANCIAL_YEAR\r\n"
			+ "),\r\n"
			+ "EXIT_EMPLOYEES AS\r\n"
			+ "(\r\n"
			+ "    SELECT\r\n"
			+ "        POL.POLICY_ID\r\n"
			+ "        ,MTE.TRANSACTION_DATE\r\n"
			+ "        ,MTE.ENTRY_TYPE\r\n"
			+ "        ,MTE.TRANSATION_TYPE\r\n"
			+ "        ,MTE.EMPLOYEE_CONTRIBUTION\r\n"
			+ "        ,MTE.EMPLOYER_CONTRIBUTION\r\n"
			+ "        ,MTE.VOLUNTARY_CONTRIBUTION\r\n"
			+ "        ,CASE\r\n"
			+ "			WHEN MTE.ENTRY_TYPE = 'OB' THEN DECODE(COALESCE(MTE.TOTAL_CONTRIBUTION,0),0,COALESCE(MTE.OPENING_BALANCE,0),COALESCE(MTE.TOTAL_CONTRIBUTION,0))\r\n"
			+ "			ELSE 0\r\n"
			+ "		END AS EXIT_OPENING_BALANCE\r\n"
			+ "		,CASE\r\n"
			+ "			WHEN MTE.ENTRY_TYPE IN ('NB', 'RA', 'SA', 'CONV', 'MER', 'TIN') THEN COALESCE(MTE.TOTAL_CONTRIBUTION,0)\r\n"
			+ "			ELSE 0\r\n"
			+ "		END AS EXIT_CONTRIBUTION\r\n"
			+ "		,CASE\r\n"
			+ "			WHEN MTE.ENTRY_TYPE = 'CLAIM' THEN COALESCE(MTE.TOTAL_CONTRIBUTION,0) ELSE 0\r\n"
			+ "		END AS TOTAL_CLAIM\r\n"
			+ "	FROM\r\n"
			+ "        MEMBER_TRANSACTION_ENTRIES MTE\r\n"
			+ "        ,POLICIES POL\r\n"
			+ "		,MEMBER_MASTER MM\r\n"
			+ "	WHERE \r\n"
			+ "        MM.POLICY_ID = POL.POLICY_ID AND\r\n"
			+ "		MTE.MEMBER_ID = MM.MEMBER_ID AND\r\n"
			+ "        -- MTE.ENTRY_TYPE = 'OB' AND\r\n"
			+ "        MTE.FINANCIAL_YEAR = POL.FINANCIAL_YEAR AND\r\n"
			+ "		MM.MEMBER_STATUS = 'Member In Payout'\r\n"
			+ "),\r\n"
			+ "EXIT_EMP_SUMMARY AS\r\n"
			+ "(\r\n"
			+ "	SELECT\r\n"
			+ "		EE.POLICY_ID\r\n"
			+ "		,SUM(EE.EXIT_OPENING_BALANCE) AS EXIT_OB\r\n"
			+ "		,SUM(EE.EXIT_CONTRIBUTION) EXIT_CON\r\n"
			+ "		,SUM(EE.TOTAL_CLAIM) EXIT_AMOUNT\r\n"
			+ "	FROM\r\n"
			+ "		EXIT_EMPLOYEES EE\r\n"
			+ "	GROUP BY\r\n"
			+ "		EE.POLICY_ID\r\n"
			+ "),\r\n"
			+ "POLICY_TRANSACTIONS AS\r\n"
			+ "(\r\n"
			+ "	SELECT\r\n"
			+ "		POL.POLICY_ID\r\n"
			+ "		,PTE.ENTRY_TYPE\r\n"
			+ "		,PTE.TRANSATION_TYPE\r\n"
			+ "		,CASE WHEN PTE.ENTRY_TYPE = 'PON' THEN COALESCE(PTE.TOTAL_CONTRIBUTION,0) ELSE 0 END AS PON\r\n"
			+ "		,CASE WHEN PTE.ENTRY_TYPE = 'POF' THEN COALESCE(PTE.TOTAL_CONTRIBUTION,0) ELSE 0 END AS POF\r\n"
			+ "		,CASE WHEN PTE.ENTRY_TYPE IN ('NB', 'RA', 'SA', 'CONV', 'MER', 'TIN') THEN COALESCE(PTE.TOTAL_CONTRIBUTION,0) ELSE 0 END AS TOTAL_CON\r\n"
			+ "	FROM\r\n"
			+ "		POLICIES POL\r\n"
			+ "		,POLICY_TRANSACTION_ENTRIES PTE\r\n"
			+ "	WHERE\r\n"
			+ "		POL.POLICY_ID = PTE.POLICY_ID AND\r\n"
			+ "		PTE.FINANCIAL_YEAR = POL.FINANCIAL_YEAR\r\n"
			+ "),\r\n"
			+ "POL_TRAN_SUMMARY AS\r\n"
			+ "(\r\n"
			+ "	SELECT\r\n"
			+ "		PT.POLICY_ID\r\n"
			+ "		,SUM(PT.PON) AS PON\r\n"
			+ "		,SUM(PT.POF) AS POF\r\n"
			+ "		,SUM(PT.TOTAL_CON) AS TOTAL_CON\r\n"
			+ "	FROM\r\n"
			+ "		POLICY_TRANSACTIONS PT\r\n"
			+ "	GROUP BY\r\n"
			+ "		PT.POLICY_ID\r\n"
			+ "),\r\n"
			+ "POL_FUND_STATEMENT AS\r\n"
			+ "(\r\n"
			+ "	SELECT\r\n"
			+ "		POL.POLICY_ID\r\n"
			+ "		,PFSS.STATEMENT_DATE AS RUNNING_ACCOUNT_AS_ON\r\n"
			+ "		,PFSS.TRAN_TO_DATE AS FUND_AS_ON\r\n"
			+ "		,(COALESCE(PFSS.TOTAL_INTEREST_RATE,0) * 100 )|| '%' AS TOTAL_INTEREST_RATE\r\n"
			+ "		,CASE\r\n"
			+ "			WHEN PFSD.ENTRY_TYPE IN ('CONV','MER','TIN') THEN PFSD.INTEREST_AMOUNT ELSE 0\r\n"
			+ "		END AS EQUITABLE_INTEREST\r\n"
			+ "		,DENSE_RANK() OVER (ORDER BY PFSS.CREATED_ON DESC) AS ROW_RANK\r\n"
			+ "	FROM\r\n"
			+ "		POLICIES POL\r\n"
			+ "		,POLICY_FUND_STATEMENT_SUMMARY PFSS\r\n"
			+ "		,POLICY_FUND_STATEMENT_DETAILS PFSD\r\n"
			+ "	WHERE\r\n"
			+ "		POL.POLICY_ID = PFSS.POLICY_ID AND\r\n"
			+ "		PFSS.POL_STATEMENT_ID = PFSD.POL_STATEMENT_ID AND\r\n"
			+ "		PFSS.IS_ACTIVE = 1\r\n"
			+ "),\r\n"
			+ "POL_FUND_SUMMARY AS\r\n"
			+ "(\r\n"
			+ "	SELECT\r\n"
			+ "		PFS.POLICY_ID\r\n"
			+ "		,PFS.RUNNING_ACCOUNT_AS_ON\r\n"
			+ "		,PFS.FUND_AS_ON\r\n"
			+ "		,PFS.TOTAL_INTEREST_RATE\r\n"
			+ "		,SUM(PFS.EQUITABLE_INTEREST) AS TOTAL_EQUI_INT\r\n"
			+ "	FROM\r\n"
			+ "		POL_FUND_STATEMENT PFS\r\n"
			+ "	WHERE\r\n"
			+ "		PFS.ROW_RANK = 1\r\n"
			+ "	GROUP BY\r\n"
			+ "		PFS.POLICY_ID\r\n"
			+ "		,PFS.RUNNING_ACCOUNT_AS_ON\r\n"
			+ "		,PFS.FUND_AS_ON\r\n"
			+ "		,PFS.TOTAL_INTEREST_RATE\r\n"
			+ "),\r\n"
			+ "MEM_FUND_STATEMENT AS\r\n"
			+ "(\r\n"
			+ "	SELECT\r\n"
			+ "		POL.POLICY_ID\r\n"
			+ "		,AM.MEMBER_ID\r\n"
			+ "		,MFSS.TOTAL_INTEREST_AMOUNT AS EQUITABLE_INTEREST\r\n"
			+ "		,DENSE_RANK() OVER (ORDER BY MFSS.CREATED_ON DESC) AS ROW_RANK\r\n"
			+ "	FROM\r\n"
			+ "		POLICIES POL\r\n"
			+ "		,ACTIVE_MEMBERS AM\r\n"
			+ "		,MEMBER_FUND_STATEMENT_SUMMARY MFSS\r\n"
			+ "	WHERE\r\n"
			+ "		POL.POLICY_ID = MFSS.POLICY_ID AND\r\n"
			+ "		POL.POLICY_ID = AM.POLICY_ID AND\r\n"
			+ "		MFSS.IS_ACTIVE = 1\r\n"
			+ "),\r\n"
			+ "MEM_FUND_SUMMARY AS\r\n"
			+ "(\r\n"
			+ "	SELECT\r\n"
			+ "		MFS.POLICY_ID\r\n"
			+ "		,SUM(MFS.EQUITABLE_INTEREST) AS TOTAL_EQUI_INT\r\n"
			+ "	FROM\r\n"
			+ "		MEM_FUND_STATEMENT MFS\r\n"
			+ "	WHERE\r\n"
			+ "		MFS.ROW_RANK = 1\r\n"
			+ "	GROUP BY\r\n"
			+ "		MFS.POLICY_ID\r\n"
			+ ")\r\n"
			+ "\r\n"
			+ "--/*\r\n"
			+ "SELECT\r\n"
			+ "	PC.POLICY_ID\r\n"
			+ "	,PC.POLICY_NUMBER\r\n"
			+ "	,PC.MPH_NAME\r\n"
			+ "	,PC.PRODUCT_CODE\r\n"
			+ "	,PC.UNIT_NAME\r\n"
			+ "	,PC.ADDRESS1\r\n"
			+ "	,PC.ADDRESS2\r\n"
			+ "	,PC.ADDRESS3\r\n"
			+ "	,PC.ADDRESS4\r\n"
			+ "	,PC.CITY_NAME\r\n"
			+ "	,PC.DISTRICT_NAME\r\n"
			+ "	,PC.STATE_NAME\r\n"
			+ "	,PC.PINCODE\r\n"
			+ "	,PC.START_DATE\r\n"
			+ "	,PC.FINANCIAL_YEAR\r\n"
			+ "	,POB.OPENING_BALANCE\r\n"
			+ "	,EES.EXIT_OB\r\n"
			+ "	,COALESCE(POB.OPENING_BALANCE,0) - COALESCE(EES.EXIT_OB,0) AS NET_OB\r\n"
			+ "	,COALESCE(PTS.PON,0) AS PON\r\n"
			+ "	,COALESCE(PTS.POF,0) AS POF\r\n"
			+ "	,(COALESCE(POB.OPENING_BALANCE,0) - COALESCE(EES.EXIT_OB,0)) + COALESCE(PTS.PON,0) - COALESCE(PTS.POF,0) AS REVISED_OB\r\n"
			+ "	,COALESCE(PTS.TOTAL_CON,0) AS TOTAL_CONTRIBUTION\r\n"
			+ "	,COALESCE(PFS.TOTAL_EQUI_INT,0) AS TOTAL_EQUI_INT\r\n"
			+ "	,COALESCE(EES.EXIT_AMOUNT,0) AS EXIT_CONTRIBUTION\r\n"
			+ "	,COALESCE(PTS.TOTAL_CON,0) + COALESCE(PFS.TOTAL_EQUI_INT,0) - COALESCE(EES.EXIT_AMOUNT,0) AS NET_CONTRIBUTION\r\n"
			+ "	,COALESCE(MFS.TOTAL_EQUI_INT,0) AS INTEREST_CREDITED\r\n"
			+ "	,((COALESCE(POB.OPENING_BALANCE,0) - COALESCE(EES.EXIT_OB,0)) + COALESCE(PTS.PON,0) - COALESCE(PTS.POF,0)) + (COALESCE(PTS.TOTAL_CON,0) + COALESCE(PFS.TOTAL_EQUI_INT,0) - COALESCE(EES.EXIT_AMOUNT,0)) + COALESCE(MFS.TOTAL_EQUI_INT,0) AS CLOSING_BALANCE\r\n"
			+ "	,COALESCE(PFS.TOTAL_INTEREST_RATE,'0%') AS TOTAL_INTEREST_RATE\r\n"
			+ "	,PC.END_DATE\r\n"
			+ "FROM\r\n"
			+ "	POLICIES PC\r\n"
			+ "	LEFT OUTER JOIN POLICY_OB POB ON (PC.POLICY_ID = POB.POLICY_ID)\r\n"
			+ "	LEFT OUTER JOIN EXIT_EMP_SUMMARY EES ON (PC.POLICY_ID = EES.POLICY_ID)\r\n"
			+ "	LEFT OUTER JOIN POL_TRAN_SUMMARY PTS ON (PC.POLICY_ID = PTS.POLICY_ID)\r\n"
			+ "	LEFT OUTER JOIN POL_FUND_SUMMARY PFS ON (PC.POLICY_ID = PFS.POLICY_ID)\r\n"
			+ "	LEFT OUTER JOIN MEM_FUND_SUMMARY MFS ON (PC.POLICY_ID = MFS.POLICY_ID)", nativeQuery = true)
	Object getFundStatementDetails(Long policyId,String financialYear);

	@Query(value ="select * from member_master where policy_id=?2 and lic_id=?1 and is_active=1", nativeQuery = true)
	MemberMasterEntity findByLicIdAndPolicyIdAndIsActive(String licId, Long policyId);
	

}
