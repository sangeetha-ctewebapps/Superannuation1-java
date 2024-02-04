package com.lic.epgs.quotation.constants;

/**
 *
 * @author KrunalGothiwala
 *
 */
public interface QuotationConstants {

	public String APPROVED = "Approved";
	public String REJECT = "Reject";

	String CONVERSION="CONVERSION";
	String POLICYDETAILSCHANGE = "POLICYDETAILSCHANGE";
	String MEMBER_ADDITION = "MEMBER_ADDITION";
	String FREELOOKCANCEL = "FREELOOKCANCEL";
	String MERGE = "MERGE";
	String MEMBERADDITION = "MEMBER_ADDITION";
	String CLAIM = "CLAIM";
	String POLICYSURRENDER = "POLICY_SURRENDER";
	String REGULARADJUSTMENT = "Regular Adjustment";
	String SUBSEQUENTADJUSTMENT = "Subsequent Adjustment";
	String MEMBERTRASFERINOUT = "MEMBERTRASFERINOUT";
	String SURRENDER = "Surrender";
	String RECORD_FOUND = "Record found";
	public static final String MEMBER_ONBOARD = "Member-In Onboard";
	public static final String MEMBER_INTIMATION = "Member-In IntimatiMation";
	public static final String MEMBER_PAYOUT = "MemberInPayout";
	
	String COMMON_DRAFT = ("1");
	String COMMON_PENDING_FOR_APPROVER = ("2");
	String COMMON_PENDING_FOR_MODIFICATION = ("3");
	String COMMON_REJECT = ("5");
}