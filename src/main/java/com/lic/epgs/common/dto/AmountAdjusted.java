package com.lic.epgs.common.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AmountAdjusted {

	private long serialNo;
//	private Date date;
	private String date;
	private String depositDate;
	private String contributionDate;
	private BigDecimal depositAmount;
	private BigDecimal availableAmount;
	private BigDecimal balanceAmount;
	private String batchNoOrTransactionNo;
	private String noOfLives;
	private BigDecimal premium;
	private Long gst;
	private Long others;
	private Long policyId;
	private BigDecimal refund;
	private BigDecimal totalUtilized;
	private BigDecimal closingBalance;
	private String lic_id;
	
  
}
