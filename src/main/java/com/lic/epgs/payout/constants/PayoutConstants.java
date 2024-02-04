package com.lic.epgs.payout.constants;

public interface PayoutConstants {
	
//	26	NEFT-Draft	
//	27	NEFT-Pending For Approval	
//	28	NEFT-On Hold
//	29	NEFT-Approved
//	30	NEFT-Rejected

	public static final String NEFT_DRAFT = "26";
	public static final String NEFT_PENDING_FOR_APPROVAL = "27";
	public static final String NEFT_ON_HOLD = "28";
	public static final String NEFT_APPROVED = "29";
	public static final String NEFT_REJECT = "30";
	
	
	
	public static final Integer COMUTATION_BY_PERCENTAGE = 1;
	public static final String CLAIM_NO = "CLAIM_NO";
	public static final String PAYOUT_SP_STATUS = "0";

	public static String getRelationShipNamebyId(Integer relationId) {

		switch (relationId) {
		case 1:
			return "Father";
		case 2:
			return "Mother";
		case 3:
			return "Step-Father";
		case 4:
			return "Step-Mother";
		case 5:
			return "Legal Guardian";
		case 6:
			return "Husband";
		case 7:
			return "Wife";
		case 8:
			return "Son";
		case 9:
			return "Daughter";
		case 10:
			return "Step-son";
		case 11:
			return "Step-daughter";
		case 12:
			return "Brother";
		case 13:
			return "Sister";
		case 14:
			return "Other";
		case 15:
			return "Grandfather";
		case 16:
			return "Grandmother";
		case 17:
			return "Grandson";
		case 18:
			return "Granddaughter";
		case 19:
			return "Uncle";
		case 20:
			return "Aunt";
		case 21:
			return "Cousin";
		case 22:
			return "Nephew";
		case 23:
			return "Niece";
		case 24:
			return "Father-in-law";
		case 25:
			return "Mother-in-law";
		case 26:
			return "Brother-in-law";
		case 27:
			return "Sister-in-law";
		default:
			return "code to be executed if all cases are not matched";
		}
	}

}