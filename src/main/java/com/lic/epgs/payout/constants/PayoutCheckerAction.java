package com.lic.epgs.payout.constants;

/**
 *
 * @author Praveenkumar
 *
 */
public enum PayoutCheckerAction {

	APPROVE(1), REJECT(2),

	SEND_TO_MAKER(3);

	private Integer val;

	PayoutCheckerAction(Integer val) {
		this.val = val;
	}

	public Integer val() {
		return val;
	}

}
