package com.lic.epgs.claim.constants;

/**
 *
 * @author Praveenkumar
 *
 */
public enum ClaimFundValueCalcConstants {

	PURCHASE_PRICE(1), PENSION(2);

	private Integer val;

	ClaimFundValueCalcConstants(Integer val) {
		this.val = val;
	}

	public Integer val() {
		return val;
	}

}
