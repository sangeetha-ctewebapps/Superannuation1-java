package com.lic.epgs.policy.repository;
import java.util.List;

/**
 * @author pradeepramesh
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.MphMasterTempEntity;

@Repository
public interface MphMasterTempRepository extends JpaRepository<MphMasterTempEntity, Long> {
	MphMasterTempEntity findByMphIdAndIsActiveTrue(Long mphId);

	MphMasterTempEntity findAllByMphIdAndIsActiveTrue(Long mphId);

	MphMasterTempEntity findByProposalNumberAndIsActiveTrue(String proposalNumber);

	MphMasterTempEntity findByMphIdAndIsActiveFalse(Long mphId);
	
	
	@Query(value = "SELECT MPH_ID, ALTERNATE_PAN, CIN, COUNTRY_CODE, CREATED_BY, CREATED_ON, EMAIL_ID, FAX, IS_ACTIVE, LANDLINE_NO, MASTER_MPH_ID, MOBILE_NO, MODIFIED_BY, MODIFIED_ON, MPH_CODE, MPH_NAME, MPH_TYPE, PAN, PROPOSAL_ID, PROPOSAL_NUMBER FROM mph_master_temp WHERE MASTER_MPH_ID =:mphId AND IS_ACTIVE =1", nativeQuery = true)
	Object findMasterMphIdbyMphId(Long mphId);
	

	@Query(value = "SELECT MPH_T_SEQ.nextval FROM dual", nativeQuery = true)
	public Long getMphTempSeq();
	
	@Query(value = "SELECT mmt.MPH_NAME FROM MPH_MASTER_TEMP mmt WHERE mmt.MPH_ID =:mphId AND mmt.IS_ACTIVE =1", nativeQuery = true)
	String getMphName(Long mphId);
	
// -----------for Quotation -------->
	
	@Query(value = "SELECT mmt.PROPOSAL_NUMBER  FROM MPH_MASTER_TEMP mmt INNER JOIN POLICY_MASTER_TEMP pmt ON (mmt.MPH_ID =pmt.MPH_ID) WHERE mmt.PROPOSAL_NUMBER =:proposalNumber "
				+ "AND mmt.IS_ACTIVE =1 AND pmt.POLICY_STATUS <>'5'", nativeQuery = true)
	List<String> getProposalHistoryWithoutRejectStatusByProposalNumber(String proposalNumber);
		
// --------------------------------->
}
