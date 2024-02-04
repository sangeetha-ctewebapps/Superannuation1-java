package com.lic.epgs.claim.constants;

public interface ClaimErrorConstants {

	public static final String INVALID_CLAIM_NO = "Invalid claim no";
	
	public static final String INVALID_CLAIM_ONBOARD_NO = "Invalid Claim Onboard No";
	
	public static final String INVALID_CLAIM_INTIMATION_NO = "Invalid Claim Intimation No";

	public static final String CLAIM_COMMUTATION_CALC_SUCCESS = "Claim calculated succesfully";

	public static final String CLAIM_ANNUITY_CALC_SUCCESS = "Claim calculated succesfully";

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

	public static final String INVALID_CLAIM_STATUS = "Invalid claim status";

	public static final String STATUS_UPDATED_SUCCESSFULLY ="updated successfully.";

	public static final String UPDATED_SUCCESSFULLY = "Claim updated successfully";

	public static final String INVALID_CLAIM_CALCULATION_TYPE = "Invalid claim calculation type";

	public static final String FUND_VALUE_SAVE_SUCCESS = "Fund value saved successfully.";

	public static final String INVALID_FUND_VALUE_ID = "Invalid fund value id";

	public static final String INVALID_MEMBER = "Invalid Member";
	
	public static final String CLAIM_PAYEE_BANK_SAVED = "Bank Details saved successfully";

	public static final String INVALID_POLICY = "Invalid Policy";

	public static final String INVALID_CONTRIBUTION = "Invalid Contribution";

	public static final String SAVED_SUCCESSFULLY = "Saved successfully";

	public static final String APPROVED_SUCCUSSFULLY = "Approved Successfully";
	
	public static final String REJECTED_SUCCUSSFULLY = "Rejected Successfully";

	public static final String SEND_TO_CHECKER = "Sent To Checker Has Been Successfully";

	public static final String SEND_TO_MAKER = "Sent To Maker Has Been Successfully";

	public static final String TOTAL_CONTRIBUTION = "PurchasePrice Or Pension value less than TotalContribution";

	public static final String APPOINTEE_SHOULD_BE_MAJOR = "Appointee Should Be Major";
	
	public static final String INVALID_GLCODE= "Invalid Glcode";

	public static final String  ANNUITY_MODE_VALIDATION="Annuity mode is mandatory";

	public static final String LEI_NUMBER_MANDATORY = "Please update LEI number in customer details before claim ";

	public static final String INTEREST_DATE_VALIDATE = "need to recalculate fund value for the"
			+ " current date,please assign claim to intimation maker";

	public static final String NOT_FOUNT_TRANSATION_DETAILS = "Policy transactionEntries data not found";

	public static String checkPolicyClosingBalance(Double closingBalance, Double purchasePrice) {
		return "The purchase price(Rs."+purchasePrice+")  is greater than the policy closing balance(Rs. "+closingBalance+"). so please enter the amount below the policy closing balance.";

	}
	
	public static String annuityInstallmentAmount(String annuityMode, Double minimumAmount) {
		return "pension amount should be greater than Rs." + minimumAmount + " for the " + annuityMode
				+ " annuity mode; please select another mode to proceed further.";

	}
}