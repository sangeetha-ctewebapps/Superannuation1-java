package com.lic.epgs.adjustmentcontribution.constants;
/**
 * @author pradeepramesh
 *
 */
public interface AdjustmentContibutionConstants {  
	

	public static final Integer COMUTATION_BY_PERCENTAGE = 1;

	String INSERT_POLICY_TEMP = "INSERT INTO POLICY_MASTER_TEMP (POLICY_ID,	ADJUSTMENT_DT, ADVANCEOTARREARS, AMT_TO_BE_ADJUSTED, ARD, CON_TYPE,	CONTRIBUTION_FREQUENCY,	CREATED_BY,	CREATED_ON,	FIRST_PREMIUM, INTERMEDIARY_OFFICER_CODE, INTERMEDIARY_OFFICER_NAME, IS_ACTIVE, IS_COMMENCEMENT_DATE_ONEYR,	LEAD_ID, LINE_OF_BUSINESS,	MARKETING_OFFICER_CODE,	MARKETING_OFFICER_NAME, MASTER_POLICY_ID, MODIFIED_BY, MODIFIED_ON,	MPH_ID,	NO_OF_CATEGORY,	POLICY_COMMENCEMENT_DT,	POLICY_DISPATCH_DATE, POLICY_NUMBER, POLICY_RECIEVED_DATE, POLICY_STATUS, POLICY_TYPE, PRODUCT_ID, PROPOSAL_ID,	QUOTATION_ID, REJECTION_REASON_CODE, REJECTION_REMARKS, RENEWAL_PREMIUM, SINGLE_PREMIUM_FIRSTYR, STAMP_DUTY, SUBSEQUENT_SINGLE_PREMIUM, TOTAL_MEMBER, UNIT_ID, UNIT_OFFICE, VARIANT, WORKFLOW_STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	String CREATED_ON = "createdOn";  
	String MODIFIED_ON = "modifiedOn";
	String POLICY_NUMBER = "policyNumber";
	String ADJUSTMENTCONTRIBUTION_NUMBER = "adjustmentContributionNumber";  
	String QUOTATION_NUMBER = "quotatonNumber";
	String COLLECTION_NUMBER = "collectionNumber";  
	String PROPOSAL_NUMBER = "proposalNumber";
	String ADJUSTMENT_TYPE = "adjustmentType";
	String POLICY_STATUS = "policyStatus";
	String ADJUSTMENTCONTRIBUTION_STATUS = "adjustmentContributionStatus";
	String MPH_CODE = "mphCode";
	String MPH_NAME = "mphName";
	String PRODUCT = "product";
	String LINE_OF_BUSINESS = "lineOfBusiness";
	String VARIANT = "variant";
	String UNIT_OFFICE = "unitOffice";
	String ROLE = "role";
	String UNIT_CODE = "unitCode";
	String IS_ACTIVE = "isActive";
	String FROM_DATE = "from";
	String TO_DATE = "to";
	String MAKER_REJECT = "26";
} 
