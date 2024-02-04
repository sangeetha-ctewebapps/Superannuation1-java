package com.lic.epgs.payout.temp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.lic.epgs.payout.temp.entity.TempPayoutCommutationCalcEntity;

@Repository
public interface TempPayoutCommutationCalcRepository extends JpaRepository<TempPayoutCommutationCalcEntity, Long>,
		JpaSpecificationExecutor<TempPayoutCommutationCalcEntity> {
	@Query(value="select pycom.* from TEMP_PAYOUT_COMMUTATION pycom JOIN TEMP_PAYOUT_MBR pymbr ON pymbr.MEMBER_ID  = pycom.PAYOUT_MEMBER_ID where pymbr.MEMBER_ID = ?1 ",nativeQuery=true)
	TempPayoutCommutationCalcEntity getCommutationDetails(Long memberId);

	@Query(value ="SELECT commutation_amt, tds_amt FROM temp_payout_commutation tpc where payout_member_id =:payOutMemberId",nativeQuery=true)
	List<Object[]> getCommutatinDetailsConfigMaster(Long payOutMemberId);
}
