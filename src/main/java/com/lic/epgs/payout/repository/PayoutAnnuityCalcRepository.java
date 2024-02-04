package com.lic.epgs.payout.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.entity.PayoutAnnuityCalcEntity;

@Repository
public interface PayoutAnnuityCalcRepository extends JpaRepository<PayoutAnnuityCalcEntity, Long>,
		JpaSpecificationExecutor<PayoutAnnuityCalcEntity> {
	
	
	@Query(value ="select pac.*\r\n"
			+ " from payout_annuity_calc  pac join payout_mbr pm on pm.member_id=pac.payout_member_id\r\n"
			+ "join payout p on p.payout_id=pm.payout_id\r\n"
			+ "join payout_payee_bank ppb on pm.member_id=ppb.payout_member_id\r\n"
			+ "where p.is_active=1 and ppb.account_number=:accountNumber and (pac.pan=:pan or pm.aadhar_number=:aadharNo) and p.master_policy_no=:policyNumber",nativeQuery = true)
	List<PayoutAnnuityCalcEntity> fetchAnnutantDetails(@Param("accountNumber")  String accountNumber,@Param("pan")  String pan,
			@Param("aadharNo") String aadharNo,@Param("policyNumber") String policyNumber);

}



