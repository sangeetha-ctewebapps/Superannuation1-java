package com.lic.epgs.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author pradeepramesh
 *
 */

public interface CommonConstants {

	String STATUS = "Success";
	String SAVEMESSAGE = "Saved Successfully";
	String UPDATEMESSAGE = "Updated Successfully";
	String INVALIDREQUEST = "Invalid Request";
	String FETCH = "Record found";
	String DENY = "No Result Found";
	String ERROR = "Error";
	String ACTIVE = "Active";
	String ZERO_STRING = "0";
	String ONE_STRING = "1";
	String EXCEPTION = "exception occurs";
	String EXISTING = "existing";
	String INPROGRESS = "inprogress";
	String LISTTRUSTCONTACTDETAILSSAVEMESSAGE = "Trust contact person details has been saved successfully";
	String LISTTRUSTCONTACTDETAILSUPDATEMESSAGE = "Trust contact person details has been updated successfully";

	String LISTTRUSTCONTACTDETAILSMESSAGE = "Record found";
	String LISTTRUSTCONTACTDETAILSERROR = "No Record found";
	String STAMPDUTYCONTROLLERMESSAGE = "StampDutyController-";
	String NO_RECORD_FOUND = "No Record found";
	String RECORDALREADYUSED = "Proposal Number is already mapped in another Policy";
	String RECORD_ALREADY_USED_IN_ADJUSTMENTS = "Policy Number is already mapped in another Adjustment";
	String REGULAR_ADJUSTMENTS_ALERT = "Please Do Regular Adjustment First For This Policy Number";
	
	String INVALID_REQUEST = "Invalid Request!";
	String PROPOSAL_IS_ACTIVE_AGAINST_POLICY = "Sorry We Are Unable To Create Quotation Becaasue Proposal Number is Inprogress against Policy!";
	String ACTIVE_QUOTATION_ERROR = "Sorry We Are Unable To Activate this Quotation Becasue Policy already using this proposal number!";

//	customer status
	public static final String SUCCESS = "SUCCESS";

	public static final String FAIL = "FAIL";
//	group status
	String FETCH_MESSAGE = "Fetched_Message";
	String NO_RESULT = "No_Data";
	String DELETED = "Data_Deleted";
	String OK = "ok";
	String DEPOSIT_NOT_EXITED = "Deposit Not Exited";

	String AUTHORIZATION = "Authorization";
	String CORELATIONID = "corelationid";
	String BUSINESSCORELATIONID = "businesscorelationid";
	String ACC_DEPO_TRNS_LOCK_MSG = "Records locked successfully";

	String ACC_DEPO_TRNS_UNLOCK_MSG = "Records unlocked successfully";

	String MAKER = "MAKER";
	String CHECKER = "CHECKER";
	String ACTUARY = "ACTUARY";
	String UNPAID = "UNPAID";
	String PAID = "PAID";

	/***
	 * Module
	 */
	String QUOTATION_MODULE = "QUOTATION";
	String LIC_SEQ = "LIC";
	String MEMBER_SEQ = "MEMBER";
	String CHALLAN_SEQ = "CHALLAN";

	String CLAIM_SEQ = "CLAIM";
	String CLAIM_OB_SEQ = "CLAIM_OB";
	String CLAIM_INTIMATION_SEQ = "CLAIM_INTI";

	String COMMON_DRAFT = ("1");
	String COMMON_PENDING_FOR_APPROVER = ("2");
	String COMMON_PENDING_FOR_MODIFICATION = ("3");
	String COMMON_APPROVED = ("4");
	String COMMON_REJECT = ("5");
	String COMMON_PENDING_WITH_APPROVER = ("6");
	String COMMON_CLOSE = ("7");
	String COMMON_REOPEN = ("8");
	String COMMON_INACTIVE = ("9");
	String COMMON_PENDING_FOR_ACTUARY = ("24");
	String COMMON_ACTUARY_PROCESS_COMPLETED = ("12");

	String FLC_MODULE = "FLC";
	String POLICY_MODULE = "POLICY";
	String SERVICE = "SERVICE";
	String POLICY_SURRENDER = "POLICYSURRENDER";

	String DB = "DB";
	String DC = "DC";

	public static List<Integer> inprogressMaker() {
		return Arrays.asList(1, 3, 8, 12);
	}

	public static List<Integer> existingMaker() {
		return Arrays.asList(4, 5);
	}

	public static List<Integer> existingMakerNew() {
		return Arrays.asList(4, 5, 9);
	}

	public static List<Integer> inprogressChecker() {
		return Arrays.asList(2);
	}

	public static List<Integer> inprogressActuary() {
		return Arrays.asList(24);
	}

	public static List<Integer> existingActuary() {
		return Arrays.asList(4, 5);
	}

	public static List<String> rejectedString() {
		return Arrays.asList("5");
	}

	public static List<Integer> validationMergerinprogressMaker() {
		return Arrays.asList(1, 2, 3);
	}

	/**
	 * These are using in Policy Addition of member because of status is String
	 * datatypes in policy table public static List<String> inprogressMakerString()
	 * { return Arrays.asList("1", "3", "8"); }
	 * 
	 * public static List<String> existingMakerString() { return Arrays.asList("4",
	 * "5"); }
	 * 
	 * public static List<String> inprogressCheckerString() { return
	 * Arrays.asList("2"); }
	 */
	String VAlIDATE_POLICY = "PolicyNumber Should Not be Same";
	String VAlIDATE_POLICY_TYPE = "PolicyType Should be Same";

	String VAlIDATE_DB_TYPE = "Categories Should be Same (Or) Destination Category is Greater than  Source Category";
	String VAlIDATE_DC_TYPE = "Categories Should be Same (Or) Destination Category is Greater than  Source Category";

	String SERVICE_ACTIVE = "1";
	String SERVICE_IN_ACTIVE = "2";

	String POLICY_MAPPED = "Policy Already Proccessing";
	String REMOVED = "Removed SuccessFully";
	String CLAIM_NOMINEE_SEQ = "CLAIM_NE";
	String PAYOUT_SEQ = "PAYOUT";
	String DOCUMENTREMOVEDSUCCESSFULLY = "Document Removed Successfully ";
	String ALREADY_USED_MERGING_POLICY = "Already This merging Policy Using In Merger Inprogress";
	String ALREADY_USED_DESTINATION_POLICY = "Already This distination Policy Using In Merger Inprogress";

	String MERGING_POLICY = "This policy has been merged with Policy Number ";

	public static List<Integer> inprogressMakerInteger() {
		return Arrays.asList(1, 3);
	}

	public static List<Integer> existingMakerInteger() {
		return Arrays.asList(4, 5);
	}

	public static List<Integer> TransferOutexistingCheckerInteger() {
		return Arrays.asList(2);
	}
	
	
	public static List<Integer> TransferOutinprogressMakerInteger() {
		return Arrays.asList(1, 3,19,20);
	}

	public static List<Integer> TransferOutexistingMakerInteger() {
		return Arrays.asList(4, 5);
	}

	public static List<Integer> existingCheckerInteger() {
		return Arrays.asList(2,6,20);
	}

	String DOUBLE_DOTS = "::";
	String LOGSTART = "Start";
	String LOGEND = "End";
	String INVALID_LOGIN = "Invalid Login";
	
	String ZERO_ACT_SEQ = "ZEROACT";

	String INTERNAL_APP_ERROR = "Unable to process the request due to internal application error.";
//	String INVALID_SRC_POLICY = "Invalid Source Policy";
//	String INVALID_DES_POLICY =  "Invalid Destination Policy";
	String ADJ_NUMBER_SEQ = "ADJ_CONTRIBUTION";
	String REG_NUMBER_SEQ = "REGADJ_CONTRIBUTION";
	

	public static List<String> adjustmentCheckStatus() {
		return Arrays.asList("1","2","3");
	}

	public static List<String> regularCheckStatus() {
		return Arrays.asList("4");
	}
	
	
	String ISSUANCE_POLICY_SUCCESS = "Policy is issued";
	String TRN_REGISTRATION_SUCCESS = "Success";
	String ISSUANCE_POLICY_ERROR = "Issuance_Policy Accounting Service API";
	String TRN_REGISTRATION_ERROR = "Trn_Registration Accounting Service API ";
	String ISSUANCE_POLICY_TYPE = "ISSUANCE_POLICY_API";
	String TRN_REGISTRATION_TYPE = "TRN_REGISTRATION_API";
	String NO_RESPONSE = "No response";
	String RESPONSE_DATA = "responseData";
	String ADJ_APPROVE = "Adjustment Already Approved";
	String ADJUSTMENT_USED = "This Policy is Already used in Adjustment";
	String FREQUENCY_DATE = "The Frequency Date is not more than Current date";
}