package com.lic.epgs.payout.constants;

/**
 *
 * @author Praveenkumar
 *
 */
public enum PayoutFundValueCalcConstants {

	PURCHASE_PRICE(1), PENSION(2);

	private Integer val;

	PayoutFundValueCalcConstants(Integer paramVal) {
		this.val = paramVal;
	}

	public Integer val() {
		return val;
	}

}
