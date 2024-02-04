package com.lic.epgs.claim.temp.service;

import com.lic.epgs.claim.dto.ClaimAnnuityCalcDto;
import com.lic.epgs.claim.temp.entity.ClaimPayeeTempBankDetailsEntity;
import com.lic.epgs.claim.temp.entity.TempClaimAnnuityCalcEntity;
import com.lic.epgs.claim.temp.entity.TempClaimCommutationCalcEntity;
import com.lic.epgs.claim.temp.entity.TempClaimEntity;
import com.lic.epgs.claim.temp.entity.TempClaimFundValueEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrAddressEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrAppointeeEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrBankDetailEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrFundValueEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrNomineeEntity;

public interface TempSaveClaimService {

	public TempClaimMbrEntity insertClaimMbr(String claimNo, TempClaimMbrEntity claimDtlsEntity);

	public TempClaimEntity insertClaim(String claimNo, TempClaimEntity claimEntity);

	public TempClaimMbrAppointeeEntity insertClaimMbrAppointee(String claimNo,
			TempClaimMbrAppointeeEntity claimAppointeeEntity);

	public TempClaimMbrNomineeEntity insertClaimMbrNominee(String claimNo,
			TempClaimMbrNomineeEntity claimMbrNomineeEntity);

	public TempClaimAnnuityCalcEntity insertClaimAnnuityCalc(String claimNo, TempClaimAnnuityCalcEntity entity,ClaimAnnuityCalcDto request);

	public TempClaimCommutationCalcEntity insertClaimCommutationCalc(String claimNo,
			TempClaimCommutationCalcEntity entity);

	public TempClaimMbrAddressEntity insertClaimMbrAddressEntity(String claimNo, TempClaimMbrAddressEntity entity);

	public TempClaimMbrBankDetailEntity insertClaimMbrBankEntity(String claimNo, TempClaimMbrBankDetailEntity entity);

	public TempClaimFundValueEntity insertClaimFundValue(String claimNo, TempClaimFundValueEntity entity);

	public TempClaimMbrFundValueEntity insertClaimMbrFundValue(String claimNo, TempClaimMbrFundValueEntity entity);

	public ClaimPayeeTempBankDetailsEntity insertClaimMbrBankPayee(String claimNo,
			ClaimPayeeTempBankDetailsEntity entity);

	public TempClaimMbrFundValueEntity insertClaimMbrFundValueRefesh(String claimNo,
			TempClaimMbrFundValueEntity entity, String nomineeCode);

	public TempClaimCommutationCalcEntity insertClaimCommutationCalcRefesh(String claimNo,
			TempClaimCommutationCalcEntity tempClaimCommutationCalcEntity, String nomineeCode);

	
	/** Change to withdrawal **/
	public String insertDataForChangetoWithdrawalGSAGNPolicy(String claimNo, String type);

}