package com.lic.epgs.payout.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.entity.StoredProcedureResponseEntity;

@Repository
public interface StoredProcedureResponseRepository extends JpaRepository<StoredProcedureResponseEntity,Long> {

	Optional<StoredProcedureResponseEntity> findByBenefiaryPaymentIdAndUtrNoIsNull(String benefiaryPaymentId);

	Optional<StoredProcedureResponseEntity> findByPayoutIdAndUtrNoIsNotNull(Long payoutId);

	StoredProcedureResponseEntity findByPayoutIdAndBenefiaryPaymentId(Long payoutId, String beneficiaryPaymentId);

	@Query(value = 
			  "select * from payout_stored_procedure_response pspr "
			+ "where "
			+ "pspr.processed_status in ('NEFT Rejected') "
			+ "and "
			+ "pspr.payout_id in (select tp.payout_id from temp_payout tp where tp.payout_no =:payoutNo and tp.is_active=1)", nativeQuery = true)
	Optional<StoredProcedureResponseEntity> checkStatus1(String payoutNo);

	
	@Query(value = 
			  "select * from payout_stored_procedure_response pspr "
			  + "where "
			  + "pspr.processed_status in ('NEFT Rejected') and "
			  + "pspr.benefiary_payment_id in (select ppb.benefiary_payment_id from payout_payee_bank ppb where ppb.is_active=1) and "
			  + "pspr.payout_id in (select tp.payout_id from temp_payout tp where tp.payout_no =:payoutNo and tp.is_active=1)", nativeQuery = true)
	Optional<StoredProcedureResponseEntity> checkStatus(String payoutNo);

	
	
	@Query(value="select pspr.* from temp_payout tp "
			+ "join payout_stored_procedure_response pspr on pspr.payout_id=tp.payout_id "
			+ "where tp.payout_status=7 and  tp.is_active=1 and pspr.status=:spStatus and tp.payout_no=:payoutNo",nativeQuery = true)
	
	StoredProcedureResponseEntity findByUTRNoAndPaymentStatus(String payoutNo,String spStatus);
}
