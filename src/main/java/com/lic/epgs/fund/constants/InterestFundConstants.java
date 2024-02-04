/**
 * 
 */
package com.lic.epgs.fund.constants;

/**
 * @author Muruganandam
 *
 */
public interface InterestFundConstants {
	String V1 = "V1";
	String V2 = "V2";
	String V3 = "V3";
	String SUPERANNUATION = "Superannuation";
	String CREDIT = "CREDIT";
	String DEBIT = "DEBIT";

	String CR = "CR";
	String DR = "DR";

	String SACHECKER = "sachecker";
	String DB = "DB";
	String DC = "DC";
	String LOG_METHOD_PARAM = "::{}::{}";

	Long MIN_DAYS_FOR_POLICY_SURRENDER = 15l;

	String SA_FUND_SERVICE = "SA Fund Service API - ";
	
	String SA_UPDATE_EFFECTIVE_DATE="SA- fund Effective API - ";

	public static final String STREAM_SUPERANNUATION = "SUPERANNUATION";
	public static final String POLICY_ACCOUNT = "POLICY";
	public static final String POLICY_MEMBER_ACCOUNT = "MEMBER";
	public static final String POLICY_MEMBER_ACCOUNT_CONTRIBUTION = "CONTRIBUTION_MEMBER";

	public static final String EMPLOYEE_CONTRIBUTION = "EMPLOYEE";
	public static final String EMPLOYER_CONTRIBUTION = "EMPLOYER";
	public static final String VOLUNTARY_CONTRIBUTION = "VOLUNTARY";

	public static final String CONTRIBUTION_ADJUSTMENT = "NB_ADJUSTMENT";
	public static final String REGULAR_ADJUSTMENT = "REGULAR_ADJUSTMENT";
	public static final String SUBSEQUENT_ADJUSTMENT = "SUBSEQUENT_ADJUSTMENT";
	public static final String POLICY_CONVERSION = "POLICY_CONVERSION";
	public static final String POLICY_MERGE = "POLICY_MERGE";
	public static final String POLICY_SURRENDER = "SURRENDER";
	public static final String POLICY_SURRENDER_FULL = "FULL_SURRENDER";
	public static final String POLICY_SURRENDER_PARTIAL = "PARTIAL_SURRENDER";

	public static final String POLICY_FREELOOK = "FREELOOK";
	public static final String MEMBER_OUT_IN = "MEMBER_OUT_IN";
	public static final String POLICY_CLAIM = "CLAIM";

}
