package com.lic.epgs.policyservicing.policylvl.repository.freelookcancellation;


/**
 * @author pradeepramesh
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesEntity;

@Repository
public interface FreeLookCancellationNotesRepository extends JpaRepository<PolicyServiceNotesEntity, Long>{
	
}
