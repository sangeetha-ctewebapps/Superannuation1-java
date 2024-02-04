package com.lic.epgs.claim.constants;

/**
 *
 * @author Praveenkumar
 *
 */
public enum ClaimStatus {
	
	
	ONBORADED(1),

	PRELIMINARY_REQUIREMENT_REVIEW(2), CLOSE_WITHOUT_SETTLEMENT_OTHERS(3),
	CLOSE_WITHOUT_SETTLEMENT_CLAIM_WRONGLY_BOOKED(4), REPUDIATE(5), SEND_CHECKER(6), APPROVE(7),
	REJECT(8),

	SEND_TO_MAKER(9),
	CLAIM_DECLINE(10);

	private Integer val;

	ClaimStatus(Integer val) {
		this.val = val;
	}

	public Integer val() {
		return val;
	}

}
