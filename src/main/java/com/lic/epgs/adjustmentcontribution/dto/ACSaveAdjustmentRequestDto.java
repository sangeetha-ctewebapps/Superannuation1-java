package com.lic.epgs.adjustmentcontribution.dto;

/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ACSaveAdjustmentRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long adjustmentContributionAdjestmentId;

	private Long adjustmentContributionId;

	private Long adjustmentContributiondepositId;

	private Long policyId;

	private String zeroId;
	
	private String isZeroId;
	
	private Boolean isMphBalance;
	
	private String role;
	
    private String voucherNo;
	
	private Date voucherDate;


}
