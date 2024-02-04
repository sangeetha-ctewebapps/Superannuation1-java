package com.lic.epgs.payout.constants;

/**
 *
 * @author Praveenkumar
 *
 */
public enum PayoutMakerAction {

	PRELIMINARY_REQUIREMENT_REVIEW(1), CLOSE_WITHOUT_SETTLEMENT_OTHERS(2),
	CLOSE_WITHOUT_SETTLEMENT_PAYOUT_WRONGLY_BOOKED(3), REPUDIATE(4), INTIMATE_PAYOUT_AND_BOOK_LIABILITY(5);

	private Integer val;

	PayoutMakerAction(Integer val) {
		this.val = val;
	}

	public Integer val() {
		return val;
	}

}
