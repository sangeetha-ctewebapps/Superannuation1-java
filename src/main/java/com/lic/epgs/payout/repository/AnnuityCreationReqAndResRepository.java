package com.lic.epgs.payout.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.lic.epgs.payout.entity.AnnuityCreationRequestResponseStoreEntity;


@Repository
public interface AnnuityCreationReqAndResRepository extends JpaRepository<AnnuityCreationRequestResponseStoreEntity,Long>{

	List<AnnuityCreationRequestResponseStoreEntity> findByPolicyNumberAndStatusAndType(String policyNumber,
			String status, String type);
	
	

	@Query(value="SELECT\r\n" + 
			"			u.unit_code,\r\n" + 
			"			u.description AS unit,\r\n" + 
			"			ano.an_origin AS annuity_origin,\r\n" + 
			"			to_char(an.created_date, 'DD/MM/YYYY') AS created_date,\r\n" + 
			"			an.an_no,\r\n" + 
			"			concat(concat(concat(concat(ant.ant_first_name, ' '), ant.ant_middle_name), ' '), ant.ant_last_name) AS name_of_the_annuitant,\r\n" + 
			"			to_char(ant.ant_date_of_birth, 'DD/MM/YYYY')  AS date_of_birth,\r\n" + 
			"			p.policy_no,\r\n" + 
			"			an.date_of_vesting,\r\n" + 
			"			an.an_due_date,\r\n" + 
			"			an.purchase_price,\r\n" + 
			"			an.basic_pension,\r\n" + 
			"			ano.annuity_option_desc,\r\n" + 
			"			mo.description AS paymentmode,\r\n" + 
			"			s.description AS status\r\n" + 
			"			FROM\r\n" + 
			"			     LICANNUITY.m_an an\r\n" + 
			"			JOIN LICANNUITY.m_an_mode mo ON an.m_an_mode_an_mode_id = mo.an_mode_id\r\n" + 
			"			JOIN LICANNUITY.an_status s ON an.an_status_an_status_id = s.an_status_id\r\n" + 
			"			JOIN LICANNUITY.m_ant     ant ON an.m_ant_m_ant_id = ant.m_ant_id\r\n" + 
			"			JOIN LICANNUITY.m_policy  p ON p.m_policy_id = an.m_policy_m_policy_id\r\n" + 
			"			JOIN LICANNUITY.an_option ano ON ano.an_option_id = an.an_option_an_option_id\r\n" + 
			"			LEFT JOIN LICANNUITY.an_origin ano ON an.an_origin_id = ano.an_origin_id\r\n" + 
			"			LEFT JOIN LICANNUITY.m_unit    u ON an.m_unit_unit_id = u.unit_id\r\n" + 
			"			WHERE\r\n" + 
			"			an.load_type_load_type_id = 2 " + 
			"			 AND 1 = (CASE WHEN ?1 IS NOT NULL  AND ?2 IS NOT NULL THEN CASE WHEN TRUNC(an.created_date) BETWEEN ?1 AND ?2 THEN 1 ELSE 0 END ELSE 1 END)\r\n" + 
			"            AND 1 = (CASE WHEN ?1 IS NULL AND ?2 IS NOT NULL THEN CASE WHEN TRUNC(an.created_date) <= ?2 THEN 1 ELSE 0 END ELSE 1 END)\r\n" + 
			"            AND 1 = (CASE WHEN ?1 IS NOT NULL AND ?2 IS NULL THEN CASE WHEN TRUNC(an.created_date) >= ?1 THEN 1 ELSE 0 END ELSE 1 END)\r\n" + 
			"            AND 1 = (CASE WHEN ?3 IS NOT NULL THEN CASE WHEN u.unit_id =?3 THEN 1 ELSE 0 END ELSE 1 END)" +
			"            ORDER BY\r\n" + 
			"			an.an_no",nativeQuery=true)
	List<Object[]> getAnnutiyCreation(String startDate,String  endDate,String unitId);


	AnnuityCreationRequestResponseStoreEntity findByReferenceIdAndStatus(String referenceId, String success);
	
	//List<Object[]> getAnnutiyCreation(String unitId, String startDate, String endDate);
}





