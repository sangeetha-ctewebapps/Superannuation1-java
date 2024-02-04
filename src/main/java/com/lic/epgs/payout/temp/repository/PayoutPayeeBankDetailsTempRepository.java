package com.lic.epgs.payout.temp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.temp.entity.PayoutPayeeBankDetailsTempEntity;

@Repository
public interface PayoutPayeeBankDetailsTempRepository extends JpaRepository<PayoutPayeeBankDetailsTempEntity, Long> {

	PayoutPayeeBankDetailsTempEntity findByMasterBankAccountIdAndIsActiveTrue(Long bankAccountId);

	PayoutPayeeBankDetailsTempEntity findByBankAccountIdAndIsActiveTrue(Long bankAccountId);

	@Modifying
	@Query(value = "update TEMP_PAYOUT_PAYEE_BANK  set  MASTER_BANK_ACCOUNT_ID = :bankAccountId where BANK_ACCOUNT_ID = :tempBankAccountId  AND IS_ACTIVE =1", nativeQuery = true)
	void updateMasterBankId(Long bankAccountId, Integer tempBankAccountId);

	PayoutPayeeBankDetailsTempEntity findByBenefiaryPaymentIdAndIsActiveTrueAndVersionNo(String benefiaryPaymentId,
			String versionNo);

	PayoutPayeeBankDetailsTempEntity findByMasterBankAccountIdAndBenefiaryPaymentIdAndIsActiveTrue(Long bankAccountId,
			String benefiaryPaymentId);

	PayoutPayeeBankDetailsTempEntity findByMasterBankAccountIdAndBenefiaryPaymentId(Long bankAccountId,
			String benefiaryPaymentId);

	@Query(value = "select BANK_ACCOUNT_ID from temp_payout_payee_bank where TYPE=:type and is_active=1 and payout_member_id =2777", nativeQuery = true)
	Long findBankAccountIdByTypeandIsActive(String type);

	@Query(value = "select BANK_ACCOUNT_ID from temp_payout_payee_bank where TYPE=:type and is_active=1 and payout_member_id =:memberId", nativeQuery = true)
	Long findBankAccountIdByTypeandIsActive(String type,Long memberId);

	@Query(value = "select BENEFIARY_PAYMENT_ID from temp_payout_payee_bank where TYPE=:type and is_active=1 and payout_member_id =:memberId", nativeQuery = true)
	String findbenefiaryPaymentIdByTypeandIsActive(String type, Long memberId);

	PayoutPayeeBankDetailsTempEntity findByBenefiaryPaymentIdAndIsActiveTrue(String reinitiateBeneficiaryPaymentId);

}