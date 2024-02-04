/**
 * @author Muruganandam
 */
package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.InterestErrorDetailsEntity;

/**
 * @author Muruganandam
 *
 */
@Repository
public interface InterestErrorDetailsRepository extends JpaRepository<InterestErrorDetailsEntity, Long> {
	List<InterestErrorDetailsEntity> findByIsFailTrueAndNoOfAttemptLessThanEqual(Integer noOfAttempt);

	List<InterestErrorDetailsEntity> findByIsFailTrueOrderByIdAsc();

	InterestErrorDetailsEntity findByRefNumberAndIsFailTrueOrderByIdDesc(String refNumber);

	List<InterestErrorDetailsEntity> findByRefNumberOrderByIdAsc(String refNumber);

	List<InterestErrorDetailsEntity> findByPolicyNoOrderByIdAsc(String policyNo);

	List<InterestErrorDetailsEntity> findByMemberIdOrderByIdAsc(String memberId);
}
