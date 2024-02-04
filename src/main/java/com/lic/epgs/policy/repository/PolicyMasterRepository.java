package com.lic.epgs.policy.repository;

/**
 * @author pradeepramesh
 *
 */
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//import com.lic.epgs.depositerefundpolicy.dto.RefundDetails;
import com.lic.epgs.policy.entity.PolicyMasterEntity;

@Repository
public interface PolicyMasterRepository extends JpaRepository<PolicyMasterEntity, Long> {

// POLICY STARTS	

	@Query(value = "SELECT mm.MPH_ID as mphId ,mm.MPH_CODE as mphCode ,mm.MPH_NAME as mphName,pm.POLICY_ID as policyId,pm.POLICY_NUMBER as policyNo,pm.POLICY_STATUS as policyStatus,pm.UNIT_ID as unitCode, mm.PROPOSAL_NUMBER as proposalNumber , pm.PRODUCT_ID as product "
			+ "FROM MPH_MASTER mm JOIN POLICY_MASTER pm ON (mm.MPH_ID =pm.MPH_ID) "
			+ "WHERE pm.POLICY_NUMBER LIKE %:policyNumber and pm.UNIT_ID =:unitId and pm.IS_ACTIVE=:isActive", nativeQuery = true)
	List<Object> policySearchPradeepInPolicyModule(String policyNumber, String unitId, Boolean isActive);

	@Query(value = "SELECT mm.PROPOSAL_NUMBER ,pm.POLICY_NUMBER,pm.POLICY_STATUS,"
			+ "TO_CHAR(pm.POLICY_COMMENCEMENT_DT,'dd/mm/yyyy HH24:mi:ss') POLICY_COMMENCEMENT_DT,"
			+ "TO_CHAR(pm.POLICY_RECIEVED_DATE,'dd/mm/yyyy HH24:mi:ss') POLICY_RECIEVED_DATE,"
			+ "TO_CHAR(pm.ADJUSTMENT_DT,'dd/mm/yyyy HH24:mi:ss') ADJUSTMENT_DT,mm.MPH_NAME ,mm.MPH_CODE ,"
			+ "pm.PRODUCT_ID ,pm.MARKETING_OFFICER_NAME,pm.MARKETING_OFFICER_CODE,pm.ADVANCEOTARREARS,"
			+ "pm.POLICY_ID,pm.amt_to_be_adjusted,TO_CHAR(pm.ard,'dd/mm/yyyy HH24:mi:ss') ard,"
			+ "pm.no_of_category,pm.con_type,pm.contribution_frequency,pm.line_of_business,"
			+ "TO_CHAR(pm.policy_dispatch_date,'dd/mm/yyyy HH24:mi:ss') policy_dispatch_date,pm.quotation_id,"
			+ "pm.policy_type,pm.temp_policy_id,pm.total_member,pm.unit_id,pm.unit_office,"
			+ "pm.variant,pm.first_premium,pm.single_premium_firstyr,pm.renewal_premium,"
			+ "pm.subsequent_single_premium,  " + "pm.stamp_Duty FROM POLICY_MASTER pm "
			+ "JOIN MPH_MASTER mm ON (mm.MPH_ID =pm.MPH_ID) "
			+ "WHERE pm.MPH_ID =?1 and pm.POLICY_ID=?2 and pm.IS_ACTIVE=?3", nativeQuery = true)
	Object fetchPolicyBasicDetailByMphId(@Param("mphId") Long mphId, @Param("policyId") Long policyId,
			@Param("true1") Boolean true1);

	@Query(value = "SELECT ARD, MPH_ID, POLICY_NUMBER,UNIT_OFFICE FROM POLICY_MASTER pm WHERE pm.POLICY_ID =:policyId and pm.IS_ACTIVE=1", nativeQuery = true)
	List<Object[]> findByPolicyEntity(Long policyId);

	@Query(value = "SELECT pm.POLICY_NUMBER, mm.PROPOSAL_NUMBER ,pm.PRODUCT_ID ,pm.VARIANT FROM POLICY_MASTER pm JOIN MPH_MASTER mm ON pm.MPH_ID = mm.MPH_ID WHERE pm.POLICY_NUMBER=:policyNumber and pm.IS_ACTIVE=1", nativeQuery = true)
	Object getIssuePolicyRequestDetailsByPolicyNumber(String policyNumber);

	@Query(value = "SELECT pm.POLICY_NUMBER, mm.PROPOSAL_NUMBER ,pm.PRODUCT_ID ,pm.VARIANT,TO_CHAR(pm.ARD,'dd/mm/yyyy') ARD FROM POLICY_MASTER pm JOIN MPH_MASTER mm ON pm.MPH_ID = mm.MPH_ID WHERE pm.POLICY_NUMBER=:policyNumber and pm.IS_ACTIVE=1", nativeQuery = true)
	Object getTrnRegistrationRequestDetailsByPolicyNumber(String policyNumber);

	@Modifying
	@Transactional
	@Query(value = "update fund_api_request_response_log set remark = 'UNCOMPLETED' where id in (select id from fund_api_request_response_log where type=:type and policy_number=:policyNumber and status = :status)", nativeQuery = true)
	void inactivePreviousRecord(String policyNumber, String type, String status);

	@Modifying
	@Transactional
	@Query(value = "update fund_api_request_response_log set remark = 'COMPLETED' where id in (select id from fund_api_request_response_log  where type=:type and policy_number=:policyNumber and status = :status)", nativeQuery = true)
	void activePreviousRecord(String policyNumber, String type, String status);

	@Query(value = "select pm.policy_number from policy_master pm join fund_api_request_response_log farrl on farrl.policy_number = pm. policy_number where farrl.status not in ('SUCCESS') and farrl.type in ('ISSUANCE_POLICY_API','TRN_REGISTRATION_API') group by pm.policy_number", nativeQuery = true)
	List<String> checktrnandissuancePolicy();

	PolicyMasterEntity findByPolicyIdAndIsActiveTrue(Long policyId);

// POLICY ENDS

// Subsequent Adjustment Starts	
	@Query(value = "SELECT mm.MPH_CODE,mm.MPH_ID,mm.MPH_NAME,mm.PROPOSAL_NUMBER,mm.TEMP_MPH_ID,\r\n"
			+ "pm.TEMP_POLICY_ID,pm.POLICY_ID,pm.POLICY_NUMBER,\r\n"
			+ "pm.POLICY_STATUS,pm.POLICY_TYPE,TO_CHAR(pm.POLICY_COMMENCEMENT_DT,'dd/mm/yyyy')\r\n"
			+ "POLICY_COMMENCEMENT_DT,pm.PRODUCT_ID,pm.VARIANT,pm.UNIT_ID,pm.CONTRIBUTION_FREQUENCY\r\n"
			+ "FROM POLICY_MASTER pm JOIN MPH_MASTER mm ON (mm.MPH_ID =pm.MPH_ID)\r\n"
			+ "WHERE pm.MPH_ID =:mphId and pm.POLICY_ID =:policyId and pm.IS_ACTIVE=:isActive", nativeQuery = true)
	List<Object> getPolicyDetailsByMphID(Long mphId, Long policyId, Boolean isActive);

	@Query(value = "SELECT DISTINCT mm.MPH_ID as mphId ,mm.MPH_CODE as mphCode ,mm.MPH_NAME as mphName,pm.POLICY_ID as policyId,\r\n"
			+ "pm.POLICY_NUMBER as policyNo,pm.POLICY_STATUS as policyStatus,pm.UNIT_ID as unitCode,\r\n"
			+ "mm.PROPOSAL_NUMBER as proposalNumber , pm.PRODUCT_ID as product,pm.CONTRIBUTION_FREQUENCY as frequency   \r\n"
			+ "FROM MPH_MASTER mm JOIN POLICY_MASTER pm ON (mm.MPH_ID =pm.MPH_ID) \r\n"
			+ "WHERE  pm.POLICY_NUMBER =:policyNumber and pm.UNIT_ID =:unitCode and pm.IS_ACTIVE=:isActive and pm.policy_status in ('4','11') ", nativeQuery = true)
	List<Object> policySearchPradeepForSusequentSearch(String policyNumber, String unitCode, Boolean isActive);

	@Query(value = "SELECT POLICY_ID, ADJUSTMENT_DT, ADVANCEOTARREARS, AMT_TO_BE_ADJUSTED, ARD, CON_TYPE, CONTRIBUTION_FREQUENCY, CREATED_BY, CREATED_ON, FIRST_PREMIUM, INTERMEDIARY_OFFICER_CODE, INTERMEDIARY_OFFICER_NAME, IS_ACTIVE, IS_COMMENCEMENT_DATE_ONEYR, LEAD_ID, LINE_OF_BUSINESS, MARKETING_OFFICER_CODE, MARKETING_OFFICER_NAME, MODIFIED_BY, MODIFIED_ON, MPH_ID, NO_OF_CATEGORY, POLICY_COMMENCEMENT_DT, POLICY_DISPATCH_DATE, POLICY_NUMBER, POLICY_RECIEVED_DATE, POLICY_STATUS, POLICY_TYPE, PRODUCT_ID, PROPOSAL_ID, QUOTATION_ID, REJECTION_REASON_CODE, REJECTION_REMARKS, RENEWAL_PREMIUM, SINGLE_PREMIUM_FIRSTYR, STAMP_DUTY, SUBSEQUENT_SINGLE_PREMIUM, TEMP_POLICY_ID, TOTAL_MEMBER, UNIT_ID, UNIT_OFFICE, VARIANT, WORKFLOW_STATUS FROM POLICY_MASTER  WHERE POLICY_ID =:policyId AND IS_ACTIVE =1", nativeQuery = true)
	Object findPolicyDetailsAndIsActive(Long policyId);

	@Query(value = "SELECT MPH_ID FROM POLICY_MASTER pm WHERE pm.POLICY_ID = :policyId and pm.IS_ACTIVE=:isActive", nativeQuery = true)
	Long findMphIdfromPolicy(Long policyId, Boolean isActive);

	@Query(value = "SELECT pm.POLICY_ID AS MASTER_POLICY_ID ,pm.TEMP_POLICY_ID AS TEMP_POLICY_ID ,pm.MPH_ID AS MASTER_MPH_ID ,pmt.MPH_ID AS TEMP_MPH_ID FROM POLICY_MASTER_TEMP pmt JOIN POLICY_MASTER pm ON pm.POLICY_ID =pmt.MASTER_POLICY_ID WHERE pm.POLICY_ID =:policyId and pm.is_active=1", nativeQuery = true)
	Object getIdsofPolicy(Long policyId);

// Subsequent Adjustment Ends	

// Regular Adjustment Starts

	@Query(value = "SELECT ARD FROM POLICY_MASTER pm WHERE POLICY_ID =:policyId", nativeQuery = true)
	Date getArdDateByPolicyId(Long policyId);

	@Query(value = "SELECT POLICY_COMMENCEMENT_DT FROM POLICY_MASTER pm WHERE POLICY_ID =:policyId", nativeQuery = true)
	Date getCommemcementDateByPolicyId(Long policyId);

	PolicyMasterEntity findByPolicyId(Long policyId);

// Regular Adjustment Ends	

// POLICY DETAILS CHANGE STARTS	

	PolicyMasterEntity findByPolicyNumberAndIsActiveTrue(String masterPolicyNo);

//	POLICY DETAILS CHANGE ENDS	

// POLICY CONVERSION STARTS

	PolicyMasterEntity findByMphIdAndIsActiveTrue(Long mphId);

// POLICY CONVERSION ENDS

	@Query(value = "SELECT mm.MPH_ID as mphId ,mm.MPH_CODE as mphCode ,mm.MPH_NAME as mphName,"
			+ "pm.POLICY_ID as policyId,pm.POLICY_NUMBER as policyNo,"
			+ " pm.UNIT_ID as unitCode,pm.VARIANT as variant,mm.EMAIL_ID as emailId,mm.MOBILE_NO as mobileNo FROM MPH_MASTER mm "
			+ "JOIN POLICY_MASTER pm ON (mm.MPH_ID =pm.MPH_ID) "
			+ "WHERE pm.POLICY_NUMBER =:policyNumber and pm.IS_ACTIVE = '1' ", nativeQuery = true)
	Object findPolicyDetail(String policyNumber);

	@Query(value = "select policy_number,product_id,variant,policy_type,MPH_ID,policy_status from policy_master where policy_id=:policyId and policy_status in ('4','16','17','11') and is_active=1", nativeQuery = true)
	Object findPolicyDetails(@Param("policyId") Long policyId);

	@Query(value = "select policy_id,product_id,variant,policy_type,MPH_ID,policy_status from policy_master where policy_number=:policyNumber and policy_status in ('4','16','17','11')  and is_active=1", nativeQuery = true)
	Object findPolicyDetails(@Param("policyNumber") String policyNumber);

	@Query(value = "select MPH_NAME,MPH_CODE,MPH_TYPE,PROPOSAL_NUMBER,MPH_ID from MPH_MASTER where mph_id=:mphId and is_active=1", nativeQuery = true)
	Object findMphDetails(@Param("mphId") Long mphId);

	@Query(value = "SELECT mm.MPH_ID as mphId ,mm.MPH_CODE as mphCode ,mm.MPH_NAME as mphName,"
			+ "pm.POLICY_ID as policyId,pm.POLICY_NUMBER as policyNo,pm.POLICY_STATUS as policyStatus,pm.UNIT_ID as unitCode, pm.VARIANT as variant "
			+ "FROM MPH_MASTER mm JOIN POLICY_MASTER pm ON (mm.MPH_ID =pm.MPH_ID) "
			+ "WHERE pm.POLICY_NUMBER LIKE %:policyNumber and pm.IS_ACTIVE=:isActive and pm.unit_id=:unitCode and pm.policy_status in ('4','11')"
			+ "AND EXISTS (SELECT 1 FROM EXCEPTION_CLAIM_POLICIES ECP WHERE ECP.POLICY_ID = PM.POLICY_ID AND ECP.IS_ACTIVE = 1)", nativeQuery = true)
	List<Object> policySearchPradeep(String policyNumber, Boolean isActive, String unitCode);

	PolicyMasterEntity findByPolicyIdAndIsActive(Long policyId, Boolean true1);

	@Query(value = "SELECT pm.POLICY_NUMBER,pm.POLICY_STATUS,"
			+ "TO_CHAR(pm.POLICY_COMMENCEMENT_DT,'dd/mm/yyyy HH24:mi:ss') POLICY_COMMENCEMENT_DT,"
			+ "TO_CHAR(pm.POLICY_RECIEVED_DATE,'dd/mm/yyyy HH24:mi:ss') POLICY_RECIEVED_DATE,"
			+ "TO_CHAR(pm.ADJUSTMENT_DT,'dd/mm/yyyy HH24:mi:ss') ADJUSTMENT_DT,pm.PRODUCT_ID ,"
			+ "pm.MARKETING_OFFICER_NAME,pm.MARKETING_OFFICER_CODE,pm.ADVANCEOTARREARS,"
			+ "pm.POLICY_ID,pm.amt_to_be_adjusted,TO_CHAR(pm.ard,'dd/mm/yyyy HH24:mi:ss') ard,"
			+ "pm.no_of_category,pm.con_type,pm.contribution_frequency," + "pm.line_of_business,"
			+ "TO_CHAR(pm.policy_dispatch_date,'dd/mm/yyyy HH24:mi:ss') policy_dispatch_date,"
			+ "pm.quotation_id,pm.policy_type,pm.temp_policy_id,pm.total_member,"
			+ "pm.unit_id,pm.unit_office,pm.variant,mm.PROPOSAL_NUMBER, "
			+ "mm.mph_id FROM POLICY_MASTER pm JOIN MPH_MASTER mm ON (mm.MPH_ID =pm.MPH_ID)  WHERE pm.policy_number=:policyNumber and pm.is_active=1 and pm.policy_status in ('4','16','17','11')", nativeQuery = true)
	Object fetchPolicyDetaislBypolicyNumber(String policyNumber);

	// Transfer In and out
	PolicyMasterEntity findByPolicyNumberAndPolicyStatusAndIsActiveTrue(String srcPolicyNo, String commonApproved);

//	@Query("SELECT new com.lic.epgs.depositerefundpolicy.dto.RefundDetails(policy.policyNumber,mph.mphName,policy.policyStatus,mph.mphCode,mphbank.accountNumber,mphbank.accountType,mphbank.bankBranch,mphbank.bankName,mphbank.ifscCode,mph.emailId,mph.mobileNo,mph.pan)"
//			+ "FROM PolicyMasterEntity policy  join MphMasterEntity mph "
//			+ "On  policy.policyNumber =:policyNumber "
//			+ "And policy.mphId = mph.mphId  "
//			+ "join MphBankEntity mphbank "
//			+ "On mph.mphId = mphbank.mphId ")
//	RefundDetails findByPolicyNumber(String policyNumber);

	@Query(value = "SELECT MPH_ID FROM POLICY_MASTER WHERE POLICY_NUMBER =:policyNumber And IS_ACTIVE =:isActive", nativeQuery = true)
	Long getMphId(String policyNumber, Boolean isActive);

	@Query(value = "SELECT POLICY_NUMBER, MODIFIED_ON, POLICY_COMMENCEMENT_DT, POLICY_TYPE,"
			+ "POLICY_STATUS,ARD, CREATED_BY, CREATED_ON, MODIFIED_BY, MPH_ID, POLICY_ID"
			+ " FROM POLICY_MASTER  WHERE POLICY_NUMBER =?1 AND IS_ACTIVE =1", nativeQuery = true)
	List<Object[]> getPolicyMasterData(String policyNumber);

//	Depsoit Refund Policy Search

	@Query(value = "SELECT  pm.POLICY_NUMBER,\r\n" + " pm.POLICY_ID,\r\n" + " mm.MPH_ID ,\r\n" + " mm.MPH_NAME,\r\n"
			+ " mm.MPH_CODE,\r\n" + " pm.unit_id,\r\n" + " pm.POLICY_STATUS,\r\n" + " mb.IFSC_CODE, \r\n"
			+ " mb.ACCOUNT_TYPE,\r\n" + " mb.BANK_NAME, \r\n" + " mb.BANK_BRANCH,\r\n" + " pm.PRODUCT_ID,\r\n"
			+ " PD.PRODUCT_CODE,\r\n" + " pm.VARIANT,\r\n" + " mb.account_number,\r\n"
			+ " mb.confirm_account_number\r\n" + "			FROM MPH_MASTER mm \r\n"
			+ "		    left JOIN POLICY_MASTER pm ON (pm.MPH_ID =mm.MPH_ID)\r\n"
			+ "		    left JOIN MPH_BANK mb  ON (mb.MPH_ID=mm.MPH_ID)\r\n"
			+ "			left JOIN PRODUCT_DETAILS pd ON (PM.PRODUCT_ID = PD.LEAD_PRODUCT_ID)\r\n"
			+ "			left JOIN PRODUCT_VARIANT_DETAILS pvd ON (PM.VARIANT = PVD.LEAD_VARIANT_ID)\r\n"
			+ "		    left JOIN ICODE_MASTER IM ON (IM.VARIANT  = pm.VARIANT)\r\n"
			+ "			 WHERE PM.POLICY_NUMBER =:policyNumber AND  pm.UNIT_ID=:unitId AND pm.POLICY_STATUS in ('4', '11') and pm.IS_ACTIVE =1", nativeQuery = true)
	Object getDepsoitRefundPolicySearch(String policyNumber, String unitId);

	@Query(value = " SELECT  " + "    PM.POLICY_ID as policyId " + "    ,PM.POLICY_NUMBER as policyNumber "
			+ "    ,CASE " + "        WHEN PFSS.POLICY_ID IS NULL THEN 0 ELSE 1 " + "    END AS isFundGenerated "
			+ "FROM " + "    POLICY_MASTER PM " + "    LEFT OUTER JOIN POLICY_FUND_STATEMENT_SUMMARY PFSS " + "    ON  "
			+ "    ( " + "        PM.POLICY_ID = PFSS.POLICY_ID AND "
			+ "        PFSS.STATEMENT_TYPE = 'QUARTER_BATCH' AND " + "        PFSS.FINANCIAL_YEAR =:finYear AND  "
			+ "        PFSS.STATEMENT_FREQUENCY =:frequency  " + "        AND PFSS.IS_ACTIVE = 1 " + "    ) " + "WHERE "
			+ "    PM.UNIT_ID =:unitId AND  "
			+ "    PM.POLICY_STATUS IN (4,11) AND variant =:variant", nativeQuery = true)
	List<Object[]> findByYearQtrVariant(String unitId, String finYear, String frequency, String variant);

}
