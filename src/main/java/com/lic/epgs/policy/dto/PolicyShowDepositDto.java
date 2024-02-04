package com.lic.epgs.policy.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyShowDepositDto  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String collectionNo;
	private Date collectionDate;
    private String collectionType;
    private String collectionMode;
    private BigDecimal collectionAmount;
    private BigDecimal usedAmount;
    private BigDecimal unusedAmount;
    private Date voucherEffectiveDate;
    private String collectionStatus;
    private String lockStatus;
    private Integer challanNo;
    private String collectionRefNo;
    private Boolean adjustmentAvailability;
}
