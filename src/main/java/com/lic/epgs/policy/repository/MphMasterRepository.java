package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.MphMasterEntity;

@Repository
public interface MphMasterRepository extends JpaRepository<MphMasterEntity, Long> {

	MphMasterEntity findByProposalNumberAndIsActiveTrue(String proposalNumber);

	MphMasterEntity findByMphIdAndIsActiveTrue(Long mphId);

	MphMasterEntity findByMphCodeAndIsActiveTrue(String mphCode);

	List<MphMasterEntity> findAllByMphCodeAndIsActiveTrueOrderByMphIdDesc(String mphCode);

	@Query(value = "SELECT REGEXP_SUBSTR(STANDARD_HASH(POLICY_NO,'MD5'), '([1-9])',1,1)||POLICY_NO NEW_POLICY_NO FROM (SELECT ?1 POLICY_NO FROM DUAL)", nativeQuery = true)
	String getPolicyNumber(@Param("policyNumber") String policyNumber);

	// For Annuity Api Start
	@Query(value = "SELECT pm.MPH_ID FROM MPH_MASTER mm JOIN POLICY_MASTER pm ON (mm.MPH_ID =pm.MPH_ID) WHERE pm.POLICY_NUMBER=:policyNumber and pm.POLICY_STATUS in ('4','16','17') and pm.IS_ACTIVE=1 and mm.IS_ACTIVE=1", nativeQuery = true)
	Long findpolicyresponseDataBypolicyIdAnnuity(String policyNumber);
	// For Annuity Api End

	@Query(value = "SELECT pcs.TOTAL_CONTRIBUTION  FROM POLICY_MASTER pm JOIN POLICY_CONTRI_SUMMARY pcs ON (pm .POLICY_ID = pcs.POLICY_ID) WHERE pm.POLICY_NUMBER = :policyNumber and pm.IS_ACTIVE=1 and pcs.IS_ACTIVE=1", nativeQuery = true)
	BigDecimal findTotalContribution(String policyNumber);

	@Query(value = "SELECT MPH_ID, ALTERNATE_PAN, CIN, COUNTRY_CODE, CREATED_BY, CREATED_ON, EMAIL_ID, FAX, IS_ACTIVE, LANDLINE_NO, MOBILE_NO, MODIFIED_BY, MODIFIED_ON, MPH_CODE, MPH_NAME, MPH_TYPE, PAN, PROPOSAL_ID, PROPOSAL_NUMBER, TEMP_MPH_ID FROM MPH_MASTER WHERE MPH_ID =:mphId AND IS_ACTIVE =1", nativeQuery = true)
	Object findMphDetails(Long mphId);

	@Query(value = "select mm.mph_id,mm.mph_code,mm.mph_name,mm.proposal_number\r\n"
			+ "from mph_master mm join policy_master pm on (pm.mph_id=mm.mph_id) where mm.mph_id=?1 and pm.policy_id=?2 and mm.is_active=?3",nativeQuery=true)
	Object fetchMphBasicDetailByMphId(@Param("mphId")  Long mphId, @Param("policyId") Long policyId, @Param("true1")  Boolean true1);
	
	@Query(value = "select mph_id,mph_name from mph_master where mph_code=?1 and is_active=1 ORDER BY mph_id DESC",nativeQuery=true)
	List<Object> getByMphCodeAndIsActiveTrue(@Param ("mphCode") String mphCode);
	
// -----------for Quotation -------->
	
	@Query(value = "SELECT PROPOSAL_NUMBER  FROM MPH_MASTER mm INNER JOIN POLICY_MASTER pm ON (mm.MPH_ID =pm.MPH_ID) WHERE mm.PROPOSAL_NUMBER =:proposalNumber "
			+ "AND mm.IS_ACTIVE =1 AND pm.POLICY_STATUS <>'5'", nativeQuery = true)
	List<String> getProposalHistoryWithoutRejectStatusByProposalNumber(String proposalNumber);

	@Query(value = "SELECT MPH_CODE, MPH_NAME FROM MPH_MASTER WHERE MPH_ID =:mphId AND IS_ACTIVE =1", nativeQuery = true)
	Object findByMphId(Long mphId);
	
// --------------------------------->
	
	@Query(value = "SELECT EMAIL_ID FROM MPH_MASTER WHERE MPH_ID =:mphId AND IS_ACTIVE =1", nativeQuery = true)
	String getEmailId(Long mphId);
	
	@Query(value = "SELECT mm.mph_id,mm.proposal_number, mm.mph_name, mm.mph_code, mm.created_by, mm.cin,"
			+ " mm.pan FROM mph_master mm WHERE mph_id =?1 AND is_active = 1", nativeQuery = true)
	List<Object[]> getMphDetails(Long convertStringToLong);
	
}
