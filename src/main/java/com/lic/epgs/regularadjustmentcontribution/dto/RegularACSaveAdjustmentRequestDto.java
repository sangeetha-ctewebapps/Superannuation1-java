package com.lic.epgs.regularadjustmentcontribution.dto;

/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegularACSaveAdjustmentRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long adjustmentContributionAdjestmentId;

	private Long regularContributionId;

	private Long regularContributiondepositId;

	private Long policyId;

	private String zeroId;
	
	private String isZeroId;
	
	private Boolean isMphBalance;
	
	private BigDecimal totalDeposit;
	
	private String role;
	
    private String voucherNo;
	
	private Date voucherDate;

}
