/**
 * 
 */
package com.lic.epgs.policyservicing.memberlvl.transferout.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policyservicing.memberlvl.transferout.entity.TransferInAndOutMasterEntity;
import com.lic.epgs.policyservicing.memberlvl.transferout.entity.TransferInAndOutMemberEntity;

/**
 * @author Karthick M
 *
 */

@Repository
public interface TransferInAndOutMasterRepository extends JpaRepository<TransferInAndOutMasterEntity, Long>{

	TransferInAndOutMasterEntity findByTrnsfrIdAndIsActiveTrue(Long trnsfrId);

	List<TransferInAndOutMasterEntity> findAllByTrnsfrStatusInAndUnitCodeAndIsActiveTrue(List<String> existingChecker,
			String unitCode);

}
