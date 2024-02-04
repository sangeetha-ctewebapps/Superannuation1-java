package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.MphBankEntity;

@Repository
public interface MphBankRepository extends JpaRepository<MphBankEntity, Long> {

	MphBankEntity findByMphIdAndIsActiveTrue(Long mphId);

	List<MphBankEntity> findAllByMphIdAndIsActiveTrue(Long mphId);

	Set<MphBankEntity> findAllByMphIdAndIsActive(Long mphId, Boolean true1);

	MphBankEntity findByMphIdAndIsActiveTrueAndIsDefaultTrue(Long mphId);
	
	@Query(value="select *  from mph_bank mb  join  mph_master mm on mb.mph_id=mm.mph_id join policy_master pm on pm.mph_id=mm.mph_id where pm.mph_id=?1 and pm.is_active=1",nativeQuery=true)
	MphBankEntity findByBankDetailsBymphId(Long mphId);

}
