package com.lic.epgs.claim.constants;

/**
 *
 * @author Praveenkumar
 *
 */
public enum ClaimOnboardStatus {

	SUCCESS(4), FAILED(2);

	private Integer val;

	ClaimOnboardStatus(Integer val) {
		this.val = val;
	}

	public Integer val() {
		return val;
	}
	
	

}
