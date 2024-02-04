package com.lic.epgs.payout.PdfDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ClaimForwardingLetterDto {
	
	private String processingUnitName;
	private String processingUnitAddressLine1;
	private String processingUnitTelephoneandFax;
	private String processingUnitEmail;
	private String memberName;
	private String memberAddressLine1;
	private String memberAddressLine2;
	private String memberAddressLine3;
	private String memberState;
	private String policyNo;
	private Integer modeOfExit;
	private String nameOfMember;
	private String annuityNumber;
	private String dueDate;
	private Double annuityAmount;
	private String modeOfAnnuity;
	private Double purchasePrice;
	private String nameOfNominee;
	private String typeOfAnnuity;
	private String dobOfNominee;
	private String iFSCCode;
	private Double commutationAmount1;
	private Boolean tdsAppliedOnCommutation;
	private String neftAndChequeNo;
	private String dateOfNEFTandCheque;
	private Double commutationAmount2;
	private String memberName1;
	private String authorisedPersonName;
	private String mphName;
	private String trusteeName;
	private String trusteeAddressLine1;
	private String trusteeAddressLine2;
	private String trusteeAddressLine3;
	private String trusteeAddressLine4;
	private String schemeName;
	private String fundAmount;
	private String amountToWords;
	private String favouring;
	private Double sumOfAnnuityNetAmount;
	private Double sumOfAnnuityPurchasePrice;
	private Double sumOfTdsAmount;
	private String memberDob;
	private String firstAnnuityInstallmentDueDate;
	
	private Double sumOfCommutationAmount;
	private  List<NomineeDetails> nomineeList;
	
	
	
	
	
}
