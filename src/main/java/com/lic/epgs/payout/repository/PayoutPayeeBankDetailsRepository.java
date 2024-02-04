package com.lic.epgs.payout.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.entity.PayoutPayeeBankDetailsEntity;
@Repository
public interface PayoutPayeeBankDetailsRepository extends JpaRepository<PayoutPayeeBankDetailsEntity, Long>{
	
	PayoutPayeeBankDetailsEntity findByTempBankAccountIdAndIsActiveTrue(Long tempBankAccountId);

	PayoutPayeeBankDetailsEntity findByBankAccountIdAndIsActiveTrue(Long bankAccountId);	
	
	@Query(value = "select * from payout_payee_bank ppb inner join temp_payout_payee_bank tppb on (ppb.temp_bank_account_id = tppb.bank_account_id) where ppb.claim_no =:claimNo and ppb.is_active = 1", nativeQuery = true)
	List<PayoutPayeeBankDetailsEntity> GetbankDetails(String claimNo);

	@Query(value = "select BENEFIARY_PAYMENT_ID from\r\n"
			+ "   (SELECT\r\n"
			+ "    PPB.BENEFIARY_PAYMENT_ID\r\n"
			+ "    ,ROW_NUMBER() OVER (PARTITION BY PPB.CLAIM_NO ORDER BY TO_NUMBER(COALESCE(VERSION_NO,'0')) DESC) AS ROW_RANK\r\n"
			+ "FROM\r\n"
			+ "    PAYOUT P,\r\n"
			+ "    PAYOUT_PAYEE_BANK PPB\r\n"
			+ "WHERE\r\n"
			+ "    P.CLAIM_NO = PPB.CLAIM_NO\r\n"
			+ "    AND PPB.TYPE IN('COMMUTATION')\r\n"
			+ "    AND P.PAYOUT_NO = ?1) where ROW_RANK = '2'", nativeQuery = true)
	String getOldBenefiaryPaymentId(String payoutNo);

	@Query(value = "select BENEFIARY_PAYMENT_ID from\r\n"
			+ "   (SELECT\r\n"
			+ "    PPB.BENEFIARY_PAYMENT_ID\r\n"
			+ "    ,ROW_NUMBER() OVER (PARTITION BY PPB.CLAIM_NO ORDER BY TO_NUMBER(COALESCE(VERSION_NO,'0')) DESC) AS ROW_RANK\r\n"
			+ "FROM\r\n"
			+ "    PAYOUT P,\r\n"
			+ "    PAYOUT_PAYEE_BANK PPB\r\n"
			+ "WHERE\r\n"
			+ "    P.CLAIM_NO = PPB.CLAIM_NO\r\n"
			+ "    AND PPB.TYPE IN('COMMUTATION')\r\n"
			+ "    AND P.PAYOUT_NO = ?1) where ROW_RANK = '1'", nativeQuery = true)
	String getCurrentBenefiaryPaymentId(String payoutNo);
}
