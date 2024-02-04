package com.lic.epgs.payout.temp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;

@Repository
public interface TempPayoutMbrRepository
		extends JpaRepository<TempPayoutMbrEntity, Long>, JpaSpecificationExecutor<TempPayoutMbrEntity> {

	
	@Query(value="SELECT *  FROM  TEMP_PAYOUT_MBR TPM   JOIN  TEMP_PAYOUT TP  ON TP.PAYOUT_ID =TPM .PAYOUT_ID  \r\n"
			+ "WHERE TPM.LIC_ID =?1 AND TP.MASTER_POLICY_NO  =?2 AND TP.PAYOUT_STATUS=?3 AND TP.IS_ACTIVE =1",nativeQuery=true)
TempPayoutMbrEntity checkPayoutMemberExit(@Param ("licId")String licId, @Param ("policyNo")String policyNo, @Param ("val")Integer val);

	TempPayoutMbrEntity findByMemberId(Long memberId);

}
