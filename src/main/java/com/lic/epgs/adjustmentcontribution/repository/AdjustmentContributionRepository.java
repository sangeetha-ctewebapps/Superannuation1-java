package com.lic.epgs.adjustmentcontribution.repository;
/**
 * @author pradeepramesh
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;




import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionEntity;

@Repository
public interface AdjustmentContributionRepository extends JpaRepository< AdjustmentContributionEntity, Long>,
		JpaSpecificationExecutor< AdjustmentContributionEntity> {

	AdjustmentContributionEntity findByAdjustmentContributionIdAndIsActiveTrue(Long adjustmentContributionId);
	
	
	@Query(value="\r\n"
			+ "     SELECT\r\n"
			+ "     PD.POLICY_ID\r\n"
			+ "    ,PM.UNIT_ID\r\n"
			+ "    ,PM.POLICY_NUMBER\r\n"
			+ "    ,MM.MPH_NAME\r\n"
			+ "    ,PD.COLLECTION_NO AS DEPOSIT_NUMBER\r\n"
			+ "    ,TRUNC(PD.COLLECTION_DATE) AS DEPOSIT_DATE\r\n"
			+ "    ,DEPOSIT_AMOUNT\r\n"
			+ "    ,PC.CONTRIBUTION_ID AS ADJUSTMENT_NO\r\n"
			+ "    ,PC.TOTAL_CONTRIBUTION AS ADJUSTMENT_AMOUNT\r\n"
			+ "    ,TRUNC(PC.CREATED_ON) AS ADJUSTMENT_DATE\r\n"
			+ "    ,0 AS NEW_MEMBERS\r\n"
			+ "    ,(SELECT COUNT(1) FROM MEMBER_CONTRIBUTION MC WHERE MC.POLICY_CON_ID = PC.CONTRIBUTION_ID AND MC.POLICY_ID = PC.POLICY_ID) AS RENEWED_MEMBERS\r\n"
			+ "    ,0 AS NEW_PREMIUM\r\n"
			+ "    ,PC.TOTAL_CONTRIBUTION AS RENEWAL_PREMIUM\r\n"
			+ "    ,PD.VOUCHER_NO\r\n"
			+ "    ,PD.VOUCHER_DATE\r\n"
			+ "FROM\r\n"
			+ "    POLICY_MASTER PM\r\n"
			+ "    ,MPH_MASTER MM\r\n"
			+ "    ,POLICY_DEPOSIT PD\r\n"
			+ "    ,POLICY_CONTRIBUTION PC\r\n"
			+ "WHERE\r\n"
			+ "    PM.POLICY_ID = PD.POLICY_ID\r\n"
			+ "    AND PM.MPH_ID = MM.MPH_ID\r\n"
			+ "    AND PM.POLICY_ID = PC.POLICY_ID\r\n"
			+ "    AND PD.POLICY_ID = PC.POLICY_ID\r\n"
			+ "    AND PD.COLLECTION_NO = PC.CONT_REFERENCE_NO\r\n"
			+ "    AND 1 = (CASE WHEN ?1 IS NOT NULL THEN CASE WHEN PM.POLICY_NUMBER =?1 THEN 1 ELSE 0 END ELSE 1 END)\r\n"
			+ "    AND 1 = (CASE WHEN ?2 IS NOT NULL THEN CASE WHEN PM.UNIT_ID =?2 THEN 1 ELSE 0 END ELSE 1 END)\r\n"
			+ "    AND 1 = (CASE WHEN ?3 IS NOT NULL AND ?4 IS NOT NULL THEN CASE WHEN TRUNC(PD.COLLECTION_DATE) BETWEEN to_date(?3,'dd-mm-yyyy') AND to_date(?4,'dd-mm-yyyy') THEN 1 ELSE 0 END ELSE 1 END)\r\n"
			+ "    AND 1 = (CASE WHEN ?5 IS NOT NULL AND ?6 IS NOT NULL THEN CASE WHEN TRUNC(PC.CREATED_ON) BETWEEN to_date(?5,'dd-mm-yyyy') AND to_date(?6,'dd-mm-yyyy') THEN 1 ELSE 0 END ELSE 1 END)\r\n"
			+ "    AND PD.IS_ACTIVE = 1",nativeQuery=true)
	List<Object[]> getPolicyDepositAdjustment(String policyNumber,String unitId, String receivedFrom , String receivedTo, String adjustedFrom, String adjustedTo);

	
	
	List<AdjustmentContributionEntity> findAllByAdjustmentContributionNumberAndIsActiveTrue(String adjustmentContributionNumber);
}
