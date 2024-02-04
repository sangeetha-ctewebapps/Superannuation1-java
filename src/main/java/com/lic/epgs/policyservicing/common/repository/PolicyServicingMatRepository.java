package com.lic.epgs.policyservicing.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.policyservicing.common.entity.PolicyServiceMatrixEntity;

public interface PolicyServicingMatRepository extends JpaRepository<PolicyServiceMatrixEntity, Long> {

	@Query(value = "SELECT SERVICE_TYPE,MEMBERTRASFERINOUT FROM POL_SRV_MAT psm WHERE SERVICE_TYPE  IN "
			+ "(SELECT SERVICE_TYPE from POL_SRV ps WHERE ?1 ) AND ?2 = 'yes'", nativeQuery = true)
	List<Object> getServiceMap(Long policyid, String servicetype);

//SELECT psm.SERVICE_TYPE,psm.MEMBERTRASFERINOUT FROM POL_SRV_MAT psm, POL_SRV ps WHERE psm.SERVICE_TYPE=ps.SERVICE_TYPE AND ps.policy_id = 62 AND psm.MEMBERTRASFERINOUT = 'yes'

//	@Query(value = "SELECT psm.SERVICE_TYPE,psm.MEMBERTRASFERINOUT FROM POL_SRV_MAT psm, POL_SRV ps "
//			+ "WHERE psm.SERVICE_TYPE=ps.SERVICE_TYPE AND ps.policy_id = :policyid AND psm.MEMBERTRASFERINOUT = :servicetype ", nativeQuery = true)
//	List<Object> getServiceMap(Long policyid, String servicetype);
	
//	 WHERE t.published=true", nativeQuery = true

	PolicyServiceMatrixEntity findByServiceTypeAndIsActiveTrue(String serviceType);

	PolicyServiceMatrixEntity findByServiceTypeAndServiceStatusAndIsActiveTrue(String serviceType, String string);

}
