package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.MphBankTempEntity;

@Repository
public interface MphBankTempRepository extends JpaRepository<MphBankTempEntity, Long> {

	MphBankTempEntity findByMphBankId(Long mphBankId);

	List<MphBankTempEntity> findAllByMphIdAndIsActiveTrue(Long mphId);

}
