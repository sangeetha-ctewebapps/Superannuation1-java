/**
 * 
 */
package com.lic.epgs.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.RollbackEntity;

/**
 * @author Karthick M
 *
 */

@Repository
public interface RollbackRepository extends JpaRepository<RollbackEntity, Long>{

	RollbackEntity findByReferenceNoAndModuleNameAndIsActiveTrue(String payoutNo, String string);


}
