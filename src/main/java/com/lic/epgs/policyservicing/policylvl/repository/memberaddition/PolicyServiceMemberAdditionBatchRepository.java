/**
 * 
 */
package com.lic.epgs.policyservicing.policylvl.repository.memberaddition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policyservicing.policylvl.entity.memberaddition.PolicyServiceMemberAdditionBatchEntity;

/**
 * @author Karthick M
 *
 */

@Repository
public interface PolicyServiceMemberAdditionBatchRepository extends JpaRepository<PolicyServiceMemberAdditionBatchEntity, Long> {

	PolicyServiceMemberAdditionBatchEntity findByBatchIdAndIsActiveTrue(Long batchId);


}
