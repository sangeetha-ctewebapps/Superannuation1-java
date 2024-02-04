package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.MphAddressTempEntity;

@Repository
public interface MphAddressTempRepository extends JpaRepository<MphAddressTempEntity, Long> {

	List<MphAddressTempEntity> findAllByMphIdAndIsActiveTrue(Long mphId);

}
