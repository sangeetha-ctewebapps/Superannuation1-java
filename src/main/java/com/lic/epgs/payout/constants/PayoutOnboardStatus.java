package com.lic.epgs.payout.constants;

/**
 *
 * @author Praveenkumar
 *
 */
public enum PayoutOnboardStatus {

	SUCCESS(1), FAILED(2);

	private Integer val;

	PayoutOnboardStatus(Integer val) {
		this.val = val;
	}

	public Integer val() {
		return val;
	}
	
	

}
