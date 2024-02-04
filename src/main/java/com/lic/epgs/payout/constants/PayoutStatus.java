package com.lic.epgs.payout.constants;

/**
 *
 * @author Praveenkumar
 *
 */
public enum PayoutStatus {
	
//	public static final String NEFT_DRAFT = "26";
//	public static final String NEFT_PENDING_FOR_APPROVAL = "27";
//	public static final String NEFT_ON_HOLD = "28";
//	public static final String NEFT_APPROVED = "29";
//	public static final String NEFT_REJECT = "30";

	ONBORADED(1),PRELIMINARY_REQUIREMENT_REVIEW(2), CLOSE_WITHOUT_SETTLEMENT_OTHERS(3),
	CLOSE_WITHOUT_SETTLEMENT_PAYOUT_WRONGLY_BOOKED(4), REPUDIATE(5), 
	SEND_CHECKER(6), APPROVE(7),REJECT(8),SEND_TO_MAKER(9),
	NEFT_DRAFT(26),NEFT_PENDING_FOR_APPROVAL(27),NEFT_ON_HOLD(28),NEFT_APPROVED(29),NEFT_REJECT(30);

	private Integer val;

	PayoutStatus(Integer val) {
		this.val = val;
	}

	public Integer val() {
		return val;
	}

}
