package com.lic.epgs.payout.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.entity.PayoutEntity;

@Repository
public interface PayoutRepository extends JpaRepository<PayoutEntity, Long>, JpaSpecificationExecutor<PayoutEntity> {

	Optional<PayoutEntity> findByPayoutNoAndIsActive(String payoutNo, Boolean isActive);

	PayoutEntity findTopByPayoutNoAndIsActiveOrderByPayoutIdDesc(String payoutNo, Boolean true1);

	List<PayoutEntity> findByPayoutNoAndIsActiveTrueOrderByPayoutIdDesc(String payoutNo);

	Optional<PayoutEntity> findByInitiMationNoAndIsActive(String initiMationNo, Boolean true1);

	PayoutEntity findByPayoutNoAndStatusAndIsActiveTrue(String payoutNo, Integer status);

	PayoutEntity findByPayoutIdAndIsActiveTrue(Long payoutId);

	PayoutEntity findByPayoutNoAndIsActiveTrue(String payoutNo);

	@Modifying
	@Query(value = "update PAYOUT set PAYOUT_STATUS = :status where PAYOUT_NO = :payoutNo  AND IS_ACTIVE =1", nativeQuery = true)
	void updateStatus(Integer status, String payoutNo);

//----------payment status api---------------

	@Query(value = "select payout_no,mode_of_exit,mph_name from payout where payout_id=:payoutId and unit_code=:unitCode", nativeQuery = true)
	List<Object[]> getPayoutDetailsByPayoutIdAndUnitCode(Long payoutId, String unitCode);

	@Query(value = "select amt_payable_to,commutation_amt from payout_commutation where payout_member_id in(select member_id from payout_mbr where payout_id=:payoutId)", nativeQuery = true)
	List<Object[]> getPayoutCommutationsDetailsByPayoutId(Long payoutId);

	@Query(value = "select benefiary_payment_id,processed_status from payout_stored_procedure_response where payout_id in(select payout_id from temp_payout where payout_no=:payoutNo)", nativeQuery = true)
	List<Object[]> getPayoutSpDetailsByPayoutNo(String payoutNo);

	@Query(value = "select concat(concat(concat(CONCAT(firstname,' '),middle_name),' '),last_name) from payout_mbr where payout_id=:payoutId", nativeQuery = true)
	String getPayoutMemberNameByPayoutId(Long payoutId);

	@Query(value = "select amt_payable_to,commutation_amt,nominee_code from payout_commutation where payout_member_id in"
			+ "(select member_id from payout_mbr where payout_id=:payoutId)", nativeQuery = true)
	List<Object[]> getPayoutCommutationsDetailsForDeathByPayoutId(Long payoutId);

	@Query(value = "select concat(concat(concat(CONCAT(first_name,' '),middle_name),' '),last_name),nominee_code from payout_mbr_nominee where nominee_code = "
			+ "(select nominee_code from payout_payee_bank where benefiary_payment_id =:benefiaryId)", nativeQuery = true)
	List<Object[]> getPayoutNomineeDetailsByPayoutId(String benefiaryId);

	@Query(value = "select pc.commutation_amt,pc.created_on from PAYOUT_COMMUTATION pc \r\n"
			+ "join PAYOUT_MBR pm on pm.MEMBER_ID=pc.PAYOUT_MEMBER_ID \r\n"
			+ "join PAYOUT p on p.payout_id=pm.payout_id where p.payout_no=:payoutNo and p.is_active=1 and "
			+ "p.payout_status=:payoutStatus", nativeQuery = true)
	List<Object[]> getPayoutDetailsByPayoutNo(String payoutNo, Integer payoutStatus);

//-----------------------------------------------

	@Query(value = "select payout_no from payout where intimation_no=:initiMationNo  AND IS_ACTIVE =1", nativeQuery = true)
	String getPayoutNoByIntimationNo(String initiMationNo);

}
