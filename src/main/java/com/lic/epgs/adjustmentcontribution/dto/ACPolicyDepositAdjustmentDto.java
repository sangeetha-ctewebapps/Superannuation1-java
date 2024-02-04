package com.lic.epgs.adjustmentcontribution.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ACPolicyDepositAdjustmentDto implements Serializable{

private static final long serialVersionUID = 1L;
	
	private Long policyId;
	private String UnitCode;
	private String policyNumber;
	private String mphName;
	private Long productId;
	private String variant;
	private String collectionNo;
//	private Date collectionDate;
	private String collectionDate;
	private BigDecimal depositAmount;
	private Long contributionId;
	private BigDecimal totalContribution;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
//	private Date createdOn;
	private String createdOn;
	private Long newMembers;
	private Long renewedMembers;
	private Long newPremium;
	private Long renewedPremium;
    private String voucherNumber;
//    private Date voucherDate;
    private String voucherDate;
    
    private String transactionStatus;
	private String transactionMessage;
	
	
	
	
}
