package com.lic.epgs.claim.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public interface ClaimConstants {

	public static final Integer COMUTATION_BY_PERCENTAGE = 1;
	public static final String SUCCESS = "Success";
	public static final String RETRIVE = "Retrived SuccessFully";
	public static final String APPOINTEE = "Appointee";
	public static final String UPDATE = "Updated Successfully";
	public static final String AMOUNT_PAYABLE = "2";
	public static final String FAIL = "Fail";
	public static final String NO_RECORD_FOUND = "No Record Found";
	public static final String MEMBER_ONBOARD = "Member-In Onboard";
	public static final String MEMBER_INTIMATION = "Member-In Intimation";
	public static final String MEMBER_PAYOUT = "MemberInPayout";
	public static final String ACTIVE = "Active";
	public static final String CORPUS = "CORPUS";
	public static final String COMMUTATION = "COMMUTATION";
	public static final String ANNUITY = "ANNUITY";
	
	
	
	/*** ModeOfExist ***/
	public static final Integer DEATH = 1;
	public static final Integer RETRIERMENT = 2;
	public static final Integer RESIGNATION = 3;
	public static final Integer WITHDRAWAL = 4;
	
	/*** GSAGN UNIT ***/
	public static final Long GSAGN_PRODUCT = 24l;
	public static final String GSAGN_VARIENT = "52";
	
	/*** GSADA UNIT ***/
	
	public static final String GSADA_PRODUCT = "11";
	public static final String GSADA_VARIANT= "38";
	
	
	
	
	////SuranderCalculator
	
	
	public static final String POLICY_VARIANT_DC_V1 = "51";
	public static final String POLICY_VARIANT_DB_V1 = "55";
	public static final String POLICY_VARIANT_DB_V2 = "56";
	public static final String POLICY_VARIANT_DC_V2 = "54";
	public static final String POLICY_VARIANT_DB_V3 = "37";
	public static final String POLICY_VARIANT_DC_V3 = "36";
	
	public static final String FULL_SURRENDER = "1";
	public static final String PARTIAL_SURRENDER = "2";
	public static final String INSTALLMENT = "1";
	public static final String LUMPSM = "2";
	public static final String POLICY_SURRENDER = "24";
	public static final String POLICY_SURRENDER_STATUS = "Policy Surrendered";
	public static final String DC = "DC";
	public static final String DB = "DB";
	/***Note:-AmountPayableTo ***/
	
	public static final String  MPH = "MPH";
	public static final String MEMBER = "MEMBER";
	public static final String NOMINEE = "NOMINEE";
	public static final String ANNUITY_AMT_PAYABLE = "ANNUITY";
	public static final String COMMUTATION_AMT_PAYABLE = "COMMUTATION";
	
	
	/**
	 * Note:- policyStatus is (Approved,Paidup) are (4,11) maintained
	 **/	
	public List<String> POLICY_STATUS = Arrays.asList("4", "11");
	
	
	public static final String POLICY_NOT_FOUND="Claims could be processed only for In-force and Paid up policies. Please enter the correct policy number";
	
	public static final String MEMBER_NOT_FOUND="Claims could be processed only for Active members. Please enter the correct Lic Id";
	
	public static final String REJECTCLAIMONBOARD = "Claim on board rejected";

	public static final String NGSCA_PRODUCT = "26";
	
	
	public static final String DEATH_ACCOUNT_CONTEXT ="Death Claim Payment Approval SA";
	public static final String MATURITY_ACCOUNT_CONTEXT ="Maturity OR Resignation Claim Payment Approval SA";
	public static final String WITHDRAWAL_ACCOUNT_CONTEXT ="Withdrawal Claim Payment Approval SA";
	
	
	public static final String POLICY_NOT_AVAILABLE="Policy Number not available for claim processing";

	
	/*** Annuity MinimumInstallmentAmount ***/

	public static final Double MINIMUM_INSTALLMENT_AMOUNT_MONTHLY = 1000d;
	public static final Double MINIMUM_INSTALLMENT_AMOUNT_QUARTERLY = 3000d;
	public static final Double MINIMUM_INSTALLMENT_AMOUNT_HALF_YEARLY = 6000d;
	public static final Double MINIMUM_INSTALLMENT_AMOUNT_YEARLY = 12000d;

	
	public static String annuityMode(String modeId) {
		String modeName="";
		switch (modeId) {
		case "1":
			modeName= "Monthly";
			break;
		case "2":
			modeName= "Quarterly";
			break;
		case "3":
			modeName= "Half Yearly";
			break;
		case "4":
			modeName= "Yearly";
			break;

		default:
			break;
		}
		return modeName; 

	} 
}
