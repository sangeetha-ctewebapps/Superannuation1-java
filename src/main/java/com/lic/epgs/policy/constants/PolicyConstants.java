package com.lic.epgs.policy.constants;
/**
 * @author pradeepramesh
 *
 */
public interface PolicyConstants {

	String STATUS = "Success";
	String SAVEMESSAGE = "Saved Successfully";
	String UPDATEMESSAGE = "Updated Successfully";
	String FETCH = "Record found";
	String LISTTRUSTCONTACTDETAILSMESSAGE = "Record found";
	String SUCCESS = "SUCCESS";
	String FETCH_MESSAGE = "Fetched_Message";

	String OK = "ok";
	String ZERO_STRING = "0";
	String ONE_STRING = "1";
	String EXISTING = "existing";
	String INPROGRESS = "inprogress";
	String CREATED = "CREATED";
	String AUTHORIZATION = "Authorization";
	String CORELATIONID = "corelationid";
	String BUSINESSCORELATIONID = "businesscorelationid";
	
	String ADJESTED = "Adjested";
	String DEPOSITSTATUSNEW = "NotAdjested";

	String INVALIDREQUEST = "Invalid Request";
	String DENY = "No Result Found";
	String ERROR = "Error";
	String EXCEPTION = "exception occurs";
	String LISTTRUSTCONTACTDETAILSERROR = "No Record found";
	String FAIL = "FAIL";
	String NO_RESULT = "No_Data";
	String DELETED = "Data_Deleted";
	String DEPOSIT_NOT_EXITED = "Deposit Not Exited";

	String ACC_DEPO_TRNS_LOCK_MSG = "Records locked successfully";
	String ACC_DEPO_TRNS_UNLOCK_MSG = "Records unlocked successfully";

	String QUOTATION_NUMBER_EMPTY = "Quotation Number shound not be Empty";
	String QUOTATION_INVALID = "Invalid Quatation Number";

	String POLICY_BANK_ID_NOT_FOUND = "Policy Bank details ID not found";
	String POLICY_BANK_ID_EMPTY = "Policy Bank details ID is Empty";

	String POLICY_NUMBER_GENERATOR = "PLCY";
	String POLICY_NUMBER_EMPTY = "Policy Number shound not be Empty";
	String POLICY_INVALID = "Invalid Policy Number";
	String DEPOSIT_INVALID = "Invalid Deposit Number";

	String POLICY_CONTRIBUTION_INVALID = "Policy Contribution is Not Available";
	
	String DRAFT = "Draft";
	String ONHOLD = "OnHold";
	String PENDINGFORAPPROVAL = "Pending For Approval";
	String MAKER = "Maker";
	String CHECKER = "Checker";
	String APPROVED = "Approved";
	String REJECT = "Reject";
	String REOPEN = "ReOpen";

	String REJECTED_NO = "5";
	String APPROVED_NO = "4";
	String MAKER_NO = "3";
	String CHECKER_NO = "2";
	String DRAFT_NO = "1";
	String REOPEN_NO = "8";

	String CHANGE_MAKER_NO = "15";
	String CHANGE_CHECKER_NO = "14";
	String CHANGE_DRAFT_NO = "13";
	String CONVERSION_NO = "16";
	String MERGER_NO = "17";
	String CANCELFLCAPPROVED_NO = "18";
	String POLICYCOMMENCEMENTDATE_EMPTY = "policy Commencement Date shound not be Empty";
	String POLICYSTATUS = "policyStatus";
	
	
	String POLICYNUMBER = "policyNumber";

	String MPHCODE = "mphCode";
	
	String MPHNAME = "mphName";
	String CREATEDON = "createdOn";
	String PRODUCT = "product";
	String LINEOFBUSINESS = "lineOfBusiness";
	String FREELOOKSTATUS = "freeLookStatus";
	String UNITCODE = "unitCode";
	String VARIANT ="variant";
	String ZEROID_INVALID = "Invalid Zero ID Number";

	
	String AMOUNTLOCK="LOCK";
	String AMOUNTUNLOCK ="UNLOCK";
	String RESPONSEUNLOCK ="AMOUNT UNLOCKED";
	String MAKERREJECT="ANMAKERREJECTED";
	String CHECKERREJECT="ANCHECKERREJECTED";
	String CHECKERAPPROVED ="ANCHECKERAPPROVED";
	String SENDTOMAKER = "ANSENDTOMAKER";
	String SUCESSSTATUS="200";
	String ERRORSTATUS="400";
	String NEWBUISSNESS = "NB";
	String SUBSQEUENTADJUSTMENT = "SUBSQEUENTADJUSTMENT";
	String REGULARADJUSTMENT = "REGULARADJUSTMENT";
	String SUBSQEUENTADJUSTMENTNEW = "SA";
	String REGULARADJUSTMENTNEW = "RA";
	String ADJUSTEDMESSAGE =  "Adjusted Successfully";
	
	String ADJUSTMENT_CONTRIBUTION_INVALID = "Invalid Adjustement";
	
	String DB_V2 = "56";
	String DC_V2 = "54";
	String SA_STAMPCONSUMPTION_FUND_SERVICE = "StampDuty Consumption API ";
	String SUCCESSRESPONSEFORSTAMPCONSUMPTION = "Success";
	String FAILURERESPONSEFORSTAMPCONSUMPTION = "FAILURE";
	String FAILURE_MESSAGE_FORSTAMPCONSUMPTION = "StampDuty Amount in Insufficient. Kindly Add Some StampDuty Amount to proceed further";
	String STATUS_STRING = "STATUS";	
	
	//policyDetail Search
	
	String SCHEME_NAME = "SuperAnnuation";
	String OPERATING_UNIT_TYPE = "UO";
	String TYPE_OF_CLIENT = "Regular";
	
	
}
