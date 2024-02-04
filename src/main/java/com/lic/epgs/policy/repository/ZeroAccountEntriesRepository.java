package com.lic.epgs.policy.repository;
import java.util.List;

/**
 * @author pradeepramesh
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.ZeroAccountEntriesEntity;

@Repository
public interface ZeroAccountEntriesRepository extends JpaRepository<ZeroAccountEntriesEntity, Long> {

	List<ZeroAccountEntriesEntity> findByPolicyIdAndIsActiveTrue(Long policyId);

	@Query("SELECT e from ZeroAccountEntriesEntity e where e.policyId =:policyId and (e.txnEntryStatus=:txnEntryStatus or e.txnEntryStatus is null) "
			+ "and e.isActive=:isActive order by e.transactionDate Asc  ,e.createdOn Asc, e.zeroAccEntId Asc")
	List<ZeroAccountEntriesEntity> getZeroAccEntriesByPolicyIdAndTxnStatus(
			@Param("policyId") Long policyId, @Param("txnEntryStatus") boolean txnEntryStatus, @Param("isActive") Boolean isActive);

}
