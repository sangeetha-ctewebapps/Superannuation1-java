package com.lic.epgs.payout.temp.service;

import com.lic.epgs.payout.temp.entity.TempPayoutAnnuityCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutCommutationCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutFundValueEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrAddressEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrAppointeeEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrBankDetailEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrFundValueEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrNomineeEntity;

public interface TempSavePayoutService {

	public TempPayoutMbrEntity insertPayoutMbr(String payoutNo, TempPayoutMbrEntity payoutDtlsEntity);

	public TempPayoutEntity insertPayout(String initiMationNo, TempPayoutEntity payoutEntity);

	public TempPayoutMbrAppointeeEntity insertPayoutMbrAppointee(String payoutNo,
			TempPayoutMbrAppointeeEntity payoutAppointeeEntity);

	public TempPayoutMbrNomineeEntity insertPayoutMbrNominee(String payoutNo,
			TempPayoutMbrNomineeEntity payoutMbrNomineeEntity);

	public TempPayoutAnnuityCalcEntity insertPayoutAnnuityCalc(String payoutNo, TempPayoutAnnuityCalcEntity entity);

	public TempPayoutCommutationCalcEntity insertPayoutCommutationCalc(String payoutNo,
			TempPayoutCommutationCalcEntity entity);

	public TempPayoutMbrAddressEntity insertPayoutMbrAddressEntity(String payoutNo, TempPayoutMbrAddressEntity entity);

	public TempPayoutMbrBankDetailEntity insertPayoutMbrBankEntity(String payoutNo, TempPayoutMbrBankDetailEntity entity);

	public TempPayoutFundValueEntity insertPayoutFundValue(String payoutNo, TempPayoutFundValueEntity entity);

	public TempPayoutMbrFundValueEntity insertPayoutMbrFundValue(String payoutNo, TempPayoutMbrFundValueEntity entity);

}