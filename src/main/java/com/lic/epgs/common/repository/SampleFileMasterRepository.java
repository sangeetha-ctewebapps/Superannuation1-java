/**
 * 
 */
package com.lic.epgs.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.SampleFileMasterEntity;

/**
 * @author Karthick M
 *
 */

@Repository
public interface SampleFileMasterRepository extends JpaRepository<SampleFileMasterEntity, Long>{

	SampleFileMasterEntity findByModuleNameAndIsActiveTrue(String fileType);

}
