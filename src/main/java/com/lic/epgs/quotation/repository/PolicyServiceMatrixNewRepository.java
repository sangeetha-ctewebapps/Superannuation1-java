package com.lic.epgs.quotation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.quotation.entity.PolicyServiceMatrixNewEntity;

public interface PolicyServiceMatrixNewRepository extends JpaRepository<PolicyServiceMatrixNewEntity, String> {

	@Query(value = "SELECT CURRENT_SERVICE,ONGOING_SERVICE,IS_ALLOWED  FROM POLICY_SERVICE_MATRIX_NEW psm WHERE   CURRENT_SERVICE=?1   AND ONGOING_SERVICE =?2", nativeQuery = true)
	List<Object[]> getPolicyDetailes(String currentService,String ongoingService );
	
}