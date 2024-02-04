package com.lic.epgs.payout.dto;

import javax.persistence.Column;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayoutAnnuityCalcDto {

	private Long annuityId;

	private Double purchasePrice;

	private Long amtPaidTo;

	private String nomineeCode;

	private Double pension;

	private String annuityOption;

	private String anuityMode;

	private Boolean isJointLiveRequired;

	private Boolean isGSTApplicable;
	
	private Double netPurchasePrice;
	
	private Integer gstBondBy;
	
	private Double gstAmount;

	private String payoutNo;

	private String createdBy;
	
	private String spouseName;
	
	private String spouseDob;
	
	private String dateOfBirth;
	
	private Double rate;
	
	private String rateType;
	
	private Double incentiveRate;
	
	private Double disIncentiveRate;
	
	private Double nFactor;

	private String unitCode;
	
	private String unitName;
	
	private String unitId;
	
	private Integer	arrears	;
	
	private Double shortReserve;
	
	private String annuityPayableTo;
 
	private String  annuitantNumber;
	
    private String annuityNumber;
	
    private Integer annuityStatus;
	
    private Date firstAnnuityInstallmentDueDate;
    
    private Double gstRate;

    private Long mobileNo;
    
    private String emailid;


}
