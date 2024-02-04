package com.lic.epgs.payout.temp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;

@Repository
public interface TempPayoutRepository extends JpaRepository<TempPayoutEntity, Long>, JpaSpecificationExecutor<TempPayoutEntity> {

	Optional<TempPayoutEntity> findByPayoutNoAndIsActive(String payoutNo, Boolean isActive);

	Optional<TempPayoutEntity> findByInitiMationNoAndIsActive(String initiMationNo, Boolean true1);

	Optional<TempPayoutEntity> findByPayoutIdAndIsActive(String payoutNo, Boolean true1);

	TempPayoutEntity findByPayoutIdAndIsActiveTrue(Long payoutId);

	@Query(value = "SELECT * FROM TEMP_PAYOUT_MBR TPM JOIN TEMP_PAYOUT TP ON TP.PAYOUT_ID = TPM .PAYOUT_ID WHERE TPM.LIC_ID =?1 AND TP.MASTER_POLICY_NO  =?2 AND TP.PAYOUT_STATUS=?3 AND TP.IS_ACTIVE =1", nativeQuery = true)
	TempPayoutMbrEntity checkPayoutMemberExit(String licId, String policyNo, Integer val);

	TempPayoutEntity findByPayoutNoAndIsActiveTrue(String payoutNo);

	@Modifying
	@Query(value = "update TEMP_PAYOUT set PAYOUT_STATUS = :status where PAYOUT_NO = :payoutNo  AND IS_ACTIVE =1", nativeQuery = true)
	void updateStatus(Integer status, String payoutNo);

	@Query(value = "SELECT TP.PAYOUT_ID FROM TEMP_PAYOUT TP WHERE TP.PAYOUT_NO =:payoutNo AND TP.IS_ACTIVE =1", nativeQuery = true)
	Long getPayoutIdByPayoutNoAndIsActiveTrue(String payoutNo);

	Optional<TempPayoutEntity> findByInitiMationNoAndUnitCodeAndIsActive(String initiMationNo, String unitCode,
			Boolean true1);

}
