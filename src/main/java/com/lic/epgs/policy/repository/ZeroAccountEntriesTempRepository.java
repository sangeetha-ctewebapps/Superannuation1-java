package com.lic.epgs.policy.repository;
import java.util.List;

/**
 * @author pradeepramesh
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.ZeroAccountEntriesTempEntity;

@Repository
public interface ZeroAccountEntriesTempRepository extends JpaRepository<ZeroAccountEntriesTempEntity, Long> {

	List<ZeroAccountEntriesTempEntity> findByPolicyIdAndIsMovedFalseAndIsActiveTrue(Long tempPolicyId);
	
//	List<ZeroAccountEntriesTempEntity> findByPolicyIdAndIsActiveTrue(Long tempPolicyId);

}
