package com.lic.epgs.policy.repository;
import java.util.Date;
/**
 * @author pradeepramesh
 *
 */
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.PolicyDepositEntity;

@Repository
public interface PolicyDepositRepository extends JpaRepository<PolicyDepositEntity, Long> {

	List<PolicyDepositEntity> findByStatusAndZeroIdAndPolicyId(String depositstatusnew, Boolean false1, Long policyId);

	Optional<PolicyDepositEntity> findByStatusAndDepositIdAndZeroIdAndPolicyId(String depositstatusnew,
			Long adjustmentContributiondepositId, boolean b, Long policyId);

	List<PolicyDepositEntity> findByStatusAndPolicyIdAndIsActiveTrue(String adjested, Long policyId);

	List<PolicyDepositEntity> findByStatusAndPolicyIdAndCollectionNoInAndIsActiveTrue(String adjested, Long policyId,
			List<String> contReferenceNolist);

	List<PolicyDepositEntity> findByStatusAndAdjustmentContributionIdAndPolicyIdAndIsActiveTrue(String depositstatusnew,Long adjustmentContributionId, Long policyId);

	List<PolicyDepositEntity> findByStatusAndRegularContributionIdAndPolicyIdAndIsActiveTrue(String depositstatusnew,Long adjustmentContributionId, Long policyId);

	Set<PolicyDepositEntity> findByPolicyIdAndIsActiveTrue(Long policyId);

	
//	PDF DOWNLOAD
	PolicyDepositEntity findByPolicyIdAndAdjustmentContributionId(Long policyId, Long adjustmentContributionId);

	PolicyDepositEntity findByPolicyIdAndRegularContributionId(Long policyId, Long regularContributionId);

	PolicyDepositEntity findByPolicyIdAndContributionType(Long policyId, String reportType);

	List<PolicyDepositEntity> findByCollectionNo(String collectionNo);

	@Query(value="SELECT \r\n"
			+ "    PM.POLICY_NUMBER\r\n"
			+ "    ,MM.MPH_NAME\r\n"
			+ "    ,MM.MPH_CODE\r\n"
			+ "    ,PM.UNIT_ID\r\n"
//			+ "--    ,COALESCE(CBD.CUSTOMER_CODE,MM.MPH_CODE) AS CUSTOMER_CODE\r\n"
			+ "    ,QS.DESCRIPTION1 AS POLICY_STATUS\r\n"
			+ "    ,MB.IFSC_CODE\r\n"
			+ "    ,MB.ACCOUNT_TYPE\r\n"
			+ "    ,MB.BANK_NAME\r\n"
			+ "    ,MB.BANK_BRANCH\r\n"
			+ "    ,PD.PRODUCT_CODE\r\n"
			+ "    ,PVD.SUB_CATEGORY AS SCHEME_NAME\r\n"
			+ "    ,PVD.SUB_CATEGORY AS VARIANT_CODE\r\n"
			+ "    ,PD.PRODUCT_NAME\r\n"
			+ "    ,COALESCE(CBD.CUSTOMER_CODE,MM.MPH_CODE) AS CUSTOMER_CODE\r\n"
			
			+ "FROM \r\n"
			+ "    POLICY_MASTER PM\r\n"
			+ "    INNER JOIN MPH_MASTER MM ON (PM.MPH_ID = MM.MPH_ID)\r\n"
			+ "    LEFT OUTER JOIN MPH_BANK MB ON (MB.MPH_ID = PM.MPH_ID AND MB.IS_ACTIVE = 1)\r\n"
			+ "    LEFT OUTER JOIN QUOTATION QM ON (QM.QUOTATION_ID = PM.QUOTATION_ID)\r\n"
			+ "    INNER JOIN LICCUSTOMERCOMMON.PRODUCT_DETAILS PD ON (PM.PRODUCT_ID = PD.LEAD_PRODUCT_ID)\r\n"
			+ "    INNER JOIN LICCUSTOMERCOMMON.PRODUCT_VARIANT_DETAILS PVD ON (PM.VARIANT = PVD.LEAD_VARIANT_ID)\r\n"
			+ "    LEFT OUTER JOIN LICCUSTOMERCOMMON.CUSTOMER_BASIC_DETAIL CBD ON (MM.MPH_TYPE = 'Customer' AND MM.MPH_CODE = CBD.COUNTRY_CODE AND CBD.IS_ACTIVE = 1)\r\n"
			+ "    LEFT OUTER JOIN QUOTATION_STATUS QS ON (PM.POLICY_STATUS = QS.ID)\r\n"
			+ "WHERE\r\n"
			+ "    PM.IS_ACTIVE = 1\r\n"
			+ "    AND MM.IS_ACTIVE = 1\r\n"
			+ "    AND QS.IS_ACTIVE = 1\r\n"
			+ "    AND 1 = (CASE WHEN ?1 IS NOT NULL THEN CASE WHEN PM.POLICY_NUMBER =?1 THEN 1 ELSE 0 END ELSE 1 END)\r\n"
			+ "    AND 1 = (CASE WHEN ?2!=0 THEN CASE WHEN MM.MPH_ID =?2 THEN 1 ELSE 0 END ELSE 1 END)\r\n"
			+ "    AND 1 = (CASE WHEN ?3 IS NOT NULL THEN CASE WHEN MM.MPH_CODE =?3 THEN 1 ELSE 0 END ELSE 1 END)\r\n"
			+ "    AND 1 = (CASE WHEN ?4 IS NOT NULL THEN CASE WHEN MM.PROPOSAL_NUMBER =?4 THEN 1 ELSE 0 END ELSE 1 END)\r\n"
			+ "    AND 1 = (CASE WHEN ?5 IS NOT NULL THEN CASE WHEN QM.QUOTATION_NO  =?5 THEN 1 ELSE 0 END ELSE 1 END)\r\n"
			+ "    AND 1 = (CASE WHEN ?6 IS NOT NULL THEN CASE WHEN MM.PAN =?6 THEN 1 ELSE 0 END ELSE 1 END)\r\n"
			+ "    AND 1 = (CASE WHEN ?7 IS NOT NULL AND ?8 IS NOT NULL THEN CASE WHEN TRUNC(PM.CREATED_ON) BETWEEN ?7 AND ?8 THEN 1 ELSE 0 END ELSE 1 END)"
		
			+ "    AND 1 = (CASE WHEN ?9 IS NOT NULL THEN CASE WHEN PD.PRODUCT_CODE =?9 THEN 1 ELSE 0 END ELSE 1 END)\r\n"
			+ "    AND 1 = (CASE WHEN ?10 IS NOT NULL THEN CASE WHEN CBD.CUSTOMER_CODE =?10 THEN 1 ELSE 0 END ELSE 1 END)\r\n"
			,nativeQuery=true)	
	List<Object[]> getSearchDepositTransfer(String policyNumber, long mphId, String mphCode,
			String proposalNumber, String quotationNumber, String panNumber,
			String policyCreationFromDate,
			String policyCreationToDate,
			String productCode,
			String customerCode
			);

	
	@Query(value="SELECT  distinct PD.CHALLAN_NO ,PD.DEPOSIT_AMOUNT,PD.ADJUSTMENT_AMOUNT, PD.AVAILABLE_AMOUNT,PD.POLICY_ID \r\n"
			+ ",TO_CHAR(PD.CHEQUE_REALISATION_DATE,'DD/MM/YYYY') as CHEQUE_REALISATION_DATE ,\r\n"
			+ "TO_CHAR(PC.CONTRIBUTION_DATE,'DD/MM/YYYY') AS CONTRIBUTION_DATE\r\n"
			+ "FROM POLICY_DEPOSIT PD\r\n"
			+ "LEFT  JOIN ADJUSTMENT_CONTRIBUTION AC ON (AC.POLICY_ID=PD.POLICY_ID)\r\n"
			+ "left join policy_contribution pc  on (pc.CONT_REFERENCE_NO=PD.CHALLAN_NO)\r\n"
			+ "WHERE PD.COLLECTION_NO =:collectionNo order by CHALLAN_NO",nativeQuery = true)
	List<Object[]> findCollectionDatailes(String collectionNo);
	
	
	

}
