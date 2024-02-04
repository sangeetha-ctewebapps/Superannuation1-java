package com.lic.epgs.payout.constants;

import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.ErrorDto;

public interface PayoutErrorConstants {

	public static final String INVALID_PAYOUT_NO = "Invalid payout no";

	public static final String PAYOUT_COMMUTATION_CALC_SUCCESS = "Payout calculated succesfully";

	public static final String PAYOUT_ANNUITY_CALC_SUCCESS = "Payout calculated succesfully";

	public static final String INVALID_ANNUITY_ID = "Invalid Annuity ID";

	public static final String INVALID_COMMUTATION_ID = "Invalid commutation id";

	public static final String INVALID_APPOINTEE_ID = "Invalid appointee id";

	public static final String APPOINTEE_SAVE_SUCCESS = "Appointee saved succesfully.";

	public static final String NOMINEE_SAVE_SUCCESS = "Nominee saved successfully";

	public static final String INVALID_NOMINEE_ID = "Invalid nominee id";

	public static final String ANNUITY_SAVED_SUCCESS = "Annuity calculation saved succesfully.";

	public static final String DB_FUND_SAVED_SUCCESSFULLY = "Fund value saved succesfully.";

	public static final String DC_FUND_SAVED_SUCCESSFULLY = "Fundvalue saved succesfully.";

	public static final String COMMUTATION_CALC_SUCCESS = "Commutation calculation saved succesfully.";

	public static final String INVALID_PAYOUT_STATUS = "Invalid payout status";

	public static final String STATUS_UPDATED_SUCCESSFULLY = "Payout status updated successfully.";

	public static final String UPDATED_SUCCESSFULLY = "Payout updated successfully";

	public static final String INVALID_PAYOUT_CALCULATION_TYPE = "Invalid payout calculation type";

	public static final String FUND_VALUE_SAVE_SUCCESS = "Fund value saved successfully.";

	public static final String INVALID_FUND_VALUE_ID = "Invalid fund value id";
	
	public static final String SEND_TO_CHECKER = "Sent To Checker Has Been Successfully";

	public static final String INVALID_INTIMATION_NO = "Invalid intimation no";

	public static final String APPROVED = "Approved Successfully";
	public static final String REJECT = "Rejected Successfully";
	public static final String SEND_TO_MAKER = "Sent To Maker Has Been Successfully";

	public static final String INVALID_PAYOUTMASTER = "Invalid Payout Master";

	public static final String INVALID_PAYOUTMEMBER = "Invalid Payout Member ";

	public static final String INVALID_PAYOUT_PAYEE_BANK ="Invalid Payout Payee Bank";

	public static final String STOREPRODUCERREJCTED = "Stored Procecure Failed from Accounting";
	
	
	public static final String INTEREST_DATE_VALIDATE = "need to recalculate fund value for the"
			+ " current date,please assign claim to intimation maker";

	public static final String SENT_TO_INTIMATION_MAKER = " Sent To Intimation  Maker Has Been Successfully";

	
}