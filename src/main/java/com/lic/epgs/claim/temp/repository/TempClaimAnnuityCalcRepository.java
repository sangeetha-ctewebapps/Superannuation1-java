package com.lic.epgs.claim.temp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.lic.epgs.claim.temp.entity.TempClaimAnnuityCalcEntity;

@Repository
public interface TempClaimAnnuityCalcRepository extends JpaRepository<TempClaimAnnuityCalcEntity, Long>,
		JpaSpecificationExecutor<TempClaimAnnuityCalcEntity> {

	TempClaimAnnuityCalcEntity findByNomineeCode(String nomineeCode);

	@Query(value="SELECT tpac.purchase_price, tpac.gst_borne_by , tpac.gst_amount  FROM temp_payout_annuity_calc tpac where payout_member_id = ?1 ",nativeQuery=true)
	List<Object[]>  getPayOutAnnuityDetails(Long payOutMemberId);

}
